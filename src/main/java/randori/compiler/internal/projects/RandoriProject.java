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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.apache.flex.swc.ISWC;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.access.IASProjectAccess;
import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.io.BundleUtils;
import randori.compiler.common.VersionInfo;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.config.annotation.IAnnotationManager;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.access.ProjectAccess;
import randori.compiler.internal.config.RandoriConfiguration;
import randori.compiler.internal.config.annotation.AnnotationManager;
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.plugin.factory.IPluginFactory;
import randori.compiler.plugin.factory.PluginFactory;
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

    private IPluginFactory pluginFactory;

    private File tempOutput;

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

    @Override
    public ProblemQuery getProblemQuery()
    {
        return problems;
    }

    @Override
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

    private IAnnotationManager annotationManager;

    @Override
    public IAnnotationManager getAnnotationManager()
    {
        return annotationManager;
    }

    private IASProjectAccess projectAccess;

    @Override
    public IASProjectAccess getProjectAccess()
    {
        return projectAccess;
    }

    @Override
    public IPluginFactory getPluginFactory()
    {
        return pluginFactory;
    }

    public RandoriProject(Workspace workspace,
            IASDocBundleDelegate asDocBundleDelegate, IRandoriBackend backend)
    {
        super(workspace, asDocBundleDelegate);

        this.backend = backend;

        getSourceCompilationUnitFactory().addHandler(
                getBackend().getSourceFileHandlerInstance());

        // TEMP
        problems = new ProblemQuery();
        pluginFactory = new PluginFactory(null);
        annotationManager = new AnnotationManager(this);
        projectAccess = new ProjectAccess(this);
    }

    @Override
    public boolean configure(String[] args)
    {
        return initializeConfiguration(args);
    }

    protected boolean initializeConfiguration(String[] args)
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
        System.out.println("Randori Compiler v"
                + VersionInfo.RANDORI_COMPILER_VERSION);
        boolean success = startCompile(doBuild);
        if (!success)
            return false;

        if (!doExport)
        {
            finish();
            return true;
        }

        success = export();

        finish();

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

    protected void populateBundleSWCs(List<File> files, File outputDirectory)
    {
        List<String> bundles = getConfiguration().getExternalBundlePath();
        // if -bundle-path is present, add all SWCs from the bundles
        if (bundles.size() > 0)
        {
            addSWCsFromBundles(bundles, files, outputDirectory);
        }

        bundles = getConfiguration().getBundlePath();
        // if -bundle-path is present, add all SWCs from the bundles
        if (bundles.size() > 0)
        {
            addSWCsFromBundles(bundles, files, outputDirectory);
        }

        // if we do have new files, add the original libraries
        // to the new result
        for (ISWC swc : getLibraries())
        {
            files.add(swc.getSWCFile());
        }
    }

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

    protected void finish()
    {
        if (tempOutput != null)
        {
            try
            {
                FileUtils.deleteDirectory(tempOutput);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            tempOutput = null;
        }
    }

    protected void populateSDKBundleOrPath(List<File> files,
            File outputDirectory)
    {
        // if the -sdk-path is set, get all libraries from that
        String path = getTargetSettings().getSDKPath();
        if (path != null && !path.isEmpty())
        {
            File sdk = new File(FilenameNormalization.normalize(path));
            if (sdk.isDirectory())
            {
                addSWCsFromSDKPath(path, files);
            }
            else
            {
                if (sdk.getName().endsWith(".rbl"))
                {
                    // sdk path is a bundle, add it to the -bundle-path
                    getConfiguration().getExternalBundlePath().add(
                            sdk.getAbsolutePath());
                }
            }

            populateBundleSWCs(files, outputDirectory);
        }
    }

    private void addSWCsFromSDKPath(String path, Collection<File> files)
    {
        File dir = new File(FilenameNormalization.normalize(path));
        if (dir.exists() && dir.isDirectory())
        {
            Collection<ISWC> swcs = null;
            try
            {
                swcs = BundleUtils.getSWCsFromBundleDir(dir);
            }
            catch (IOException e)
            {
                // TODO (mschmalle) add Problem
                e.printStackTrace();
            }

            for (ISWC swc : swcs)
            {
                files.add(swc.getSWCFile());
            }
        }
    }

    protected void addJSFromBundles(List<String> bundles, List<File> files,
            File outputDirectory, IBundleCategory category)
    {
        // temporarily copy swcs in bundles
        for (String bundle : bundles)
        {
            tempOutput = new File(outputDirectory, "___temp___");
            try
            {
                tempOutput.mkdirs();
                IBundle bundle2 = BundleUtils.getBundle(new File(bundle));
                // root == null means use InputStream to copy
                List<File> copiedFiles = BundleUtils.copyJSFilesFromBundle(
                        tempOutput, null, bundle2);
                for (File file : copiedFiles)
                {
                    category.addFile(file, file.getName());
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void addSWCsFromBundles(List<String> bundles, List<File> files,
            File outputDirectory)
    {
        Collection<ISWC> result = new ArrayList<ISWC>();

        // temporarily copy swcs in bundles
        for (String bundle : bundles)
        {
            Collection<ISWC> swcs = null;
            // XXX Figure out what random dir name is best here
            tempOutput = new File(outputDirectory, "___temp___");
            try
            {
                tempOutput.mkdirs();
                swcs = BundleUtils.tempWriteSWCs(
                        new File(FilenameNormalization.normalize(bundle)),
                        tempOutput);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            result.addAll(swcs);
        }

        // add all the swc file to the library path
        for (ISWC swc : result)
        {
            files.add(swc.getSWCFile());
        }
    }

    protected void mergeLibraries(List<File> files)
    {
        if (files.size() > 0)
        {
            for (ISWC swc : getLibraries())
            {
                files.add(swc.getSWCFile());
            }
            setLibraries(files);
        }
    }
}
