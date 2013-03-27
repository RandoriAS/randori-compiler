/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.clients;

import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.config.ConfigurationBuffer;
import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.config.ICompilerSettingsConstants;
import org.apache.flex.compiler.driver.IBackend;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.exceptions.ConfigurationException.IOError;
import org.apache.flex.compiler.exceptions.ConfigurationException.MustSpecifyTarget;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.projects.ISourceFileHandler;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ConfigurationProblem;
import org.apache.flex.compiler.problems.ICompilerProblem;

/**
 * A base compiler implementation that handles configuration and compiler state.
 * <p>
 * Compiler functionality
 * <ul>
 * <li>Configuration; load the configuration and config files, setup the main
 * {@link Configuration} instance, validate the build targets.</li>
 * <li>Pre build, setup project specific handlers, listeners</li>
 * <li>Build setup; creates the build targets</li>
 * <li>Build the target application</li>
 * <li>Compile; prebuild, setup build, build, target compile</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public class BaseCompiler
{
    //----------------------------------
    // Configuration
    //----------------------------------

    private ISourceFileHandler sourceFileHandler;

    private Configuration configuration;

    private Configurator projectConfigurator;

    private ConfigurationBuffer configBuffer;

    //----------------------------------
    // Main state
    //----------------------------------

    private Workspace workspace;

    private FlexProject project;

    private ProblemQuery problems;

    private IBackend backend;

    public Workspace getWorkspace()
    {
        return workspace;
    }

    public FlexProject getProject()
    {
        return project;
    }

    public ProblemQuery getProblems()
    {
        return problems;
    }

    public IBackend getBackend()
    {
        return backend;
    }

    public BaseCompiler(Workspace workspace, FlexProject project,
            ProblemQuery problems, IBackend backend)
    {
        this.workspace = workspace;
        this.project = project;
        this.problems = problems;
        this.backend = backend;

        sourceFileHandler = backend.getSourceFileHandlerInstance();
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

    /**
     * Load configurations from all the sources.
     * 
     * @param args command line arguments
     * @return True if mxmlc should continue with compilation.
     */
    protected boolean configure(final String[] args)
    {
        project.getSourceCompilationUnitFactory().addHandler(sourceFileHandler);
        projectConfigurator = createConfigurator();

        try
        {
            //            // Print brief usage if no arguments provided.
            //            if (args.length == 0)
            //            {
            //                final String usage = CommandLineConfigurator.brief(
            //                        getProgramName(), DEFAULT_VAR,
            //                        LocalizationManager.get(), L10N_CONFIG_PREFIX);
            //                if (usage != null)
            //                    println(usage);
            //                return false;
            //            }
            //

            projectConfigurator.setConfiguration(args,
                    ICompilerSettingsConstants.FILE_SPECS_VAR);
            projectConfigurator.applyToProject(project);

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
}
