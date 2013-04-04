/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.config.ConfigurationBuffer;
import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.config.ICompilerSettingsConstants;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.exceptions.ConfigurationException.IOError;
import org.apache.flex.compiler.exceptions.ConfigurationException.MustSpecifyTarget;
import org.apache.flex.compiler.internal.projects.ASProject;
import org.apache.flex.compiler.internal.projects.CompilerProject;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ConfigurationProblem;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.problems.InternalCompilerProblem;
import org.apache.flex.compiler.problems.UnableToBuildSWFProblem;
import org.apache.flex.compiler.targets.ITargetProgressMonitor;
import org.apache.flex.compiler.targets.ITargetSettings;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriApplication;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.config.RandoriConfiguration;
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.projects.IRandoriApplicationProject;

/**
 * XXX When I have time, I want to research subclassing {@link ASProject}. Other
 * than SWC stuff in FlexProject, we do not need it and it adds a HUGE overhead.
 * 
 * @author Michael Schmalle
 */
public class RandoriApplicationProject extends FlexProject implements
        IRandoriApplicationProject
{

    private IRandoriBackend backend;

    /**
     * target settings
     */
    private IRandoriTargetSettings targetSettings;

    private Configurator projectConfigurator;

    private Configuration configuration;

    public RandoriConfiguration getConfiguration()
    {
        return (RandoriConfiguration) configuration;
    }

    private ConfigurationBuffer configBuffer;

    private ProblemQuery problems;

    public ProblemQuery getProblemQuery()
    {
        return problems;
    }

    private IRandoriTarget target;

    public IRandoriTargetSettings getTargetSettings()
    {
        if (targetSettings == null)
            targetSettings = (IRandoriTargetSettings) projectConfigurator
                    .getTargetSettings(null);

        return targetSettings;
    }

    public void setTargetSettings(IRandoriTargetSettings value)
    {
        targetSettings = value;
    }

    public RandoriApplicationProject(Workspace workspace,
            IRandoriBackend backend, IASDocBundleDelegate asDocBundleDelegate)
    {
        super(workspace, asDocBundleDelegate);
        this.backend = backend;

        getSourceCompilationUnitFactory().addHandler(
                backend.getSourceFileHandlerInstance());
    }

    public RandoriApplicationProject(Workspace workspace)
    {
        // XXX TEMP until I figure out what to do with the backend
        this(workspace, new RandoriBackend(), IASDocBundleDelegate.NIL_DELEGATE);
    }

    public boolean configure(final String[] args)
    {
        projectConfigurator = createConfigurator();

        try
        {
            projectConfigurator.setConfiguration(args,
                    ICompilerSettingsConstants.FILE_SPECS_VAR);
            projectConfigurator.applyToProject(this);

            problems = new ProblemQuery(
                    projectConfigurator.getCompilerProblemSettings());

            // Get the configuration and configBuffer which are now initialized.
            configuration = projectConfigurator.getConfiguration();
            configBuffer = projectConfigurator.getConfigurationBuffer();
            problems.addAll(projectConfigurator.getConfigurationProblems());

            // Print version if "-version" is present.
            if (configBuffer.getVar("version") != null) //$NON-NLS-1$
            {
                return false;
            }

            if (problems.hasErrors())
                return false;

            validateTargetFile();
            return true;
        }
        catch (ConfigurationException e)
        {
            final ICompilerProblem problem = new ConfigurationProblem(e);
            problems.add(problem);
            return false;
        }
        catch (Exception e)
        {
            final ICompilerProblem problem = new ConfigurationProblem(null, -1,
                    -1, -1, -1, e.getMessage());
            problems.add(problem);
            return false;
        }
        finally
        {
            // If we couldn't create a configuration, then create a default one
            // so we can exit without throwing an exception.
            if (configuration == null)
            {
                configuration = new Configuration();
                configBuffer = new ConfigurationBuffer(Configuration.class,
                        Configuration.getAliases());
            }
        }
    }

    public boolean compile(boolean doBuild)
    {
        return startCompile(doBuild);
    }

    protected boolean startCompile(boolean doBuild)
    {
        boolean compilationSuccess = false;

        try
        {
            target = createApplicationTarget(getTargetSettings(), null);

            IRandoriApplication application = build();

            if (target != null)
            {
                Collection<ICompilerProblem> errors = new ArrayList<ICompilerProblem>();
                Collection<ICompilerProblem> warnings = new ArrayList<ICompilerProblem>();

                if (!configuration.getCreateTargetWithErrors())
                {
                    problems.getErrorsAndWarnings(errors, warnings);
                    if (errors.size() > 0)
                        return false;
                }

                // for now we let warnings pass
                problems.getErrorsAndWarnings(errors, warnings);
                if (errors.size() > 0)
                    return false;

                if (doBuild)
                {
                    compilationSuccess = buildTarget(application);
                }
                else
                {
                    compilationSuccess = true;
                }
            }
        }
        catch (Exception e)
        {
            final ICompilerProblem problem = new InternalCompilerProblem(e);
            problems.add(problem);
        }

        return compilationSuccess;
    }

    private boolean buildTarget(IRandoriApplication application)
    {
        return application.compile((IRandoriBackend) backend, problems);
    }

    private IRandoriApplication build() throws InterruptedException,
            IOException, ConfigurationException
    {
        final List<ICompilerProblem> problemsBuildingSWF = new ArrayList<ICompilerProblem>();

        final IRandoriApplication app = buildApplication(this,
                problemsBuildingSWF);
        problems.addAll(problemsBuildingSWF);
        if (app == null)
        {
            ICompilerProblem problem = new UnableToBuildSWFProblem(
                    configuration.getOutput());
            problems.add(problem);
        }

        return app;
    }

    private IRandoriApplication buildApplication(CompilerProject project,
            Collection<ICompilerProblem> problems) throws InterruptedException
    {
        Collection<ICompilerProblem> fatalProblems = project.getFatalProblems();

        if (!fatalProblems.isEmpty())
        {
            problems.addAll(fatalProblems);
            return null;
        }

        return target.build(problems);
    }

    /**
     * Validate target file.
     * 
     * @throws MustSpecifyTarget
     * @throws IOError
     */
    protected void validateTargetFile() throws ConfigurationException
    {
        //        final String targetFile = config.getTargetFile();
        //        if (targetFile == null)
        //            throw new ConfigurationException.MustSpecifyTarget(null, null, -1);
        //
        //        final File file = new File(targetFile);
        //        if (!file.exists())
        //            throw new ConfigurationException.IOError(targetFile);
    }

    /**
     * Create a new Configurator. This method may be overridden to allow
     * Configurator subclasses to be created that have custom configurations.
     * 
     * @return a new instance or subclass of {@link Configurator}.
     */
    protected Configurator createConfigurator()
    {
        return backend.createConfigurator();
    }

    public IRandoriTarget createApplicationTarget(
            ITargetSettings targetSettings,
            ITargetProgressMonitor progressMonitor) throws InterruptedException
    {
        this.targetSettings = (IRandoriTargetSettings) targetSettings;

        return (IRandoriTarget) backend
                .createTarget(this, targetSettings, null);
    }
}
