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

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.config.ConfigurationBuffer;
import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.config.ICompilerSettingsConstants;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.projects.ASProject;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ConfigurationProblem;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.targets.ITargetProgressMonitor;
import org.apache.flex.compiler.targets.ITargetSettings;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.config.RandoriConfiguration;
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.projects.IRandoriProject;

/**
 * XXX When I have time, I want to research subclassing {@link ASProject}. Other
 * than SWC stuff in FlexProject, we do not need it and it adds a HUGE overhead.
 * 
 * @author Michael Schmalle
 */
public abstract class RandoriProject extends FlexProject implements
        IRandoriProject
{
    private IRandoriBackend backend;

    private IRandoriTargetSettings targetSettings;

    private Configurator projectConfigurator;

    private Configuration configuration;

    private ConfigurationBuffer configBuffer;

    private ProblemQuery problems;

    protected IRandoriBackend getBackend()
    {
        return backend;
    }

    protected void setBackend(RandoriBackend value)
    {
        backend = value;
    }

    protected RandoriConfiguration getConfiguration()
    {
        return (RandoriConfiguration) configuration;
    }

    public ProblemQuery getProblemQuery()
    {
        return problems;
    }

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

    public RandoriProject(Workspace workspace,
            IASDocBundleDelegate asDocBundleDelegate, IRandoriBackend backend)
    {
        super(workspace, asDocBundleDelegate);
        
        this.backend = backend;
        
        getSourceCompilationUnitFactory().addHandler(
                getBackend().getSourceFileHandlerInstance());
    }

    @Override
    public boolean configure(String[] args)
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

            validateConfiguration();

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

    @Override
    public boolean compile(boolean doBuild)
    {
        return compile(doBuild, false);
    }

    @Override
    public boolean compile(boolean doBuild, boolean doExport)
    {
        boolean success = startCompile(doBuild);
        if (!success)
            return false;

        if (!doExport)
            return true;

        success = export();

        return success;
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

    protected abstract void validateConfiguration()
            throws ConfigurationException;

    protected abstract boolean startCompile(boolean doBuild);

    protected IRandoriTarget createTarget(ITargetSettings targetSettings)
            throws InterruptedException
    {
        return createTarget(targetSettings, null);
    }

    protected IRandoriTarget createTarget(ITargetSettings targetSettings,
            ITargetProgressMonitor progressMonitor) throws InterruptedException
    {
        setTargetSettings(targetSettings);

        return getBackend().createTarget(this, targetSettings, progressMonitor);
    }

    protected abstract boolean export();

}
