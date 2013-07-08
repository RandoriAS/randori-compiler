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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.clients.COMPC;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.swc.SWC;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.bundle.Bundle;
import randori.compiler.bundle.BundleConfiguration;
import randori.compiler.bundle.BundleLibrary;
import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleConfiguration;
import randori.compiler.bundle.IBundleConfigurationEntry;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IBundleContainer.Type;
import randori.compiler.bundle.IBundleLibrary;
import randori.compiler.bundle.IBundleVersion;
import randori.compiler.bundle.IMutableBundle;
import randori.compiler.bundle.io.BundleDirectoryWriter;
import randori.compiler.bundle.io.BundleWriter;
import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.Randori;
import randori.compiler.common.VersionInfo;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.internal.config.PathCollection;
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.projects.IRandoriBundleProject;

/**
 * @author Michael Schmalle
 */
public class RandoriBundleProject extends RandoriProject implements
        IRandoriBundleProject
{
    private IBundleConfiguration bundleConfiguration;

    private IMutableBundle bundle;

    private File tempDir;

    private IBundleConfiguration getBundleConfiguration()
    {
        return bundleConfiguration;
    }

    public RandoriBundleProject(Workspace workspace, IRandoriBackend backend)
    {
        super(workspace, IASDocBundleDelegate.NIL_DELEGATE, backend);
    }

    @Override
    public boolean configure(String[] args)
    {
        boolean result = super.configure(args);
        if (!result)
            return false;

        String bundleName = getConfiguration().getAppName();
        String output = getConfiguration().getOutput();
        BundleConfiguration bundleConfig = new BundleConfiguration(bundleName,
                output);

        bundleConfig.setJsOutputAsFiles(getConfiguration()
                .getJsClassesAsFiles());

        // -bundle-path
        for (String path : getConfiguration().getBundlePath())
        {
            bundleConfig.addBundlePath(path);
        }

        // add global -library-path
        for (String path : getConfiguration().getCompilerLibraryPath())
        {
            bundleConfig.addLibraryPath(path);
        }

        for (String path : getConfiguration().getCompilerExternalLibraryPath())
        {
            bundleConfig.addExternalLibraryPath(path);
        }

        List<String> libraries = getConfiguration().getBundleLibraries();
        for (String libraryName : libraries)
        {
            IBundleConfigurationEntry entry = bundleConfig
                    .addEntry(libraryName);
            PathCollection collection = null;

            // -bundle-library-path
            collection = getConfiguration().getBundleLibraryPaths().get(
                    libraryName);
            if (collection != null)
            {
                for (String path : collection.getPaths())
                {
                    entry.addLibraryPath(path);
                }
            }

            // -bundle-source-path
            collection = getConfiguration().getBundleSourcePaths().get(
                    libraryName);
            if (collection != null)
            {
                for (String path : collection.getPaths())
                {
                    entry.addSourcePath(path);
                }
            }

            // -bundle-include-sources
            collection = getConfiguration().getBundleIncludeSources().get(
                    libraryName);
            if (collection != null)
            {
                for (String path : collection.getPaths())
                {
                    entry.addIncludeSources(path);
                }
            }
        }

        result = configure(bundleConfig);
        return result;
    }

    @Override
    public boolean configure(IBundleConfiguration configuration)
    {
        this.bundleConfiguration = configuration;

        CompilerArguments arguments = new CompilerArguments();
        arguments.setOutput(configuration.getOutput());
        arguments.setSDKPath(configuration.getSDKPath());

        initializeConfiguration(arguments.toArguments());

        // if the -sdk-path is set, add the swcs as -external-library-path args
        addSDKLibraries(bundleConfiguration);

        // if we have -bundle-path entries, add the swcs as -library-path args
        addBundlePathLibraries(bundleConfiguration);

        // if we have -external-bundle-path entries, add the swcs as -external-library-path args
        addExternalBundlePathLibraries(bundleConfiguration);

        return true;
    }

    private void addSDKLibraries(IBundleConfiguration configuration)
    {
        List<File> files = new ArrayList<File>();
        final File outputDirectory = getOutputDirectory();

        // Add libraries contained in the sdk if -sdk-path is set
        populateSDKBundleOrPath(files, outputDirectory);
        // temp, the populateSDKBundleOrPath() adds to the external in configuration
        for (File file : files)
        {
            configuration.addExternalLibraryPath(file.getAbsolutePath());
        }
    }

    private void addBundlePathLibraries(IBundleConfiguration configuration)
    {
        List<File> files = new ArrayList<File>();
        Collection<String> bundles = configuration.getBundlePaths();
        File outputDirectory = getOutputDirectory();

        // if -bundle-path is present, add all SWCs from the bundles
        if (bundles.size() > 0)
        {
            addSWCsFromBundles((List<String>) bundles, files, outputDirectory);

            // add the swcs for the .rbl archives onto the library path
            for (File file : files)
            {
                configuration.addLibraryPath(file.getAbsolutePath());
            }
        }
    }

    private void addBundleJS(IBundleConfigurationEntry entry,
            Collection<String> bundlePaths)
    {
        List<File> files = new ArrayList<File>();
        File outputDirectory = getOutputDirectory();

        // if -bundle-path is present, add all SWCs from the bundles
        if (bundlePaths.size() > 0)
        {
            String name = entry.getName();
            IBundleLibrary library = bundle.getLibrary(name);
            IBundleContainer container = library
                    .getContainer(IBundleContainer.Type.JS);
            if (container == null)
            {
                container = library.addContainer(IBundleContainer.Type.JS);
                container.addCategory(IBundleCategory.Type.MONOLITHIC);
            }

            IBundleCategory category = container
                    .getCategory(IBundleCategory.Type.MONOLITHIC);

            // XXX copy the js from the rbl
            addJSFromBundles(new ArrayList<String>(bundlePaths), files,
                    outputDirectory, category);
        }
    }

    private void addExternalBundlePathLibraries(
            IBundleConfiguration configuration)
    {
        ArrayList<File> files = new ArrayList<File>();
        Collection<String> bundles = configuration.getExternalBundlePaths();
        File outputDirectory = getOutputDirectory();

        // if -bundle-path is present, add all SWCs from the bundles
        if (bundles.size() > 0)
        {
            addSWCsFromBundles((List<String>) bundles, files, outputDirectory);
        }

        // add them as external so they don't get included int he rbl archive
        for (File file : files)
        {
            configuration.addExternalLibraryPath(file.getAbsolutePath());
        }
    }

    @Override
    protected void validateConfiguration() throws ConfigurationException
    {
    }

    @Override
    protected boolean startCompile(boolean doBuild)
    {
        final File outputDirectory = getOutputDirectory();
        bundle = new Bundle(getTargetSettings().getOutput());

        tempDir = new File(outputDirectory, "___bundle___");
        tempDir.mkdirs();

        setVersionInfo(bundle);

        // LOOP through the entries and create libraries foreach
        for (IBundleConfigurationEntry entry : getBundleConfiguration()
                .getEntries())
        {
            BundleLibrary library = new BundleLibrary(entry.getName());
            // add the libraries to the bundle
            bundle.addLibrary(library);

            // TODO this has to be done based off of compiler args -js-classes-as-files
            library.addContainer(Type.JS).addCategory(
                    IBundleCategory.Type.MONOLITHIC);

            addBundleJS(entry, getBundleConfiguration().getBundlePaths());

            boolean success;

            // run the randori
            success = compileRandori(library, entry);
            if (!success)
                return false;

            // run the compc
            success = compileSWC(library, entry);
            if (!success)
                return false;
        }

        // write the bundle to disk
        BundleDirectoryWriter writer;
        try
        {
            writer = new BundleDirectoryWriter(
                    outputDirectory.getAbsolutePath());
            writer.write(bundle);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected boolean export()
    {
        bundle.setBundleFile(getTargetSettings().getOutput());

        // write the .rbl bundle to disk
        BundleWriter writer;
        try
        {
            writer = new BundleWriter(bundle.getBundleFile());
            writer.write(bundle);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void finish()
    {
        super.finish();

        if (tempDir != null)
        {
            try
            {
                FileUtils.deleteDirectory(tempDir);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean compileRandori(IBundleLibrary library,
            IBundleConfigurationEntry entry)
    {
        CompilerArguments arguments = new CompilerArguments();
        configureRandoriCompiler(library, entry, arguments);

        HashSet<ICompilerProblem> problems = new HashSet<ICompilerProblem>();

        RandoriBackend backend = new RandoriBackend();
        Randori randori = new Randori(backend);

        @SuppressWarnings("unused")
        final int code = randori.mainNoExit(arguments.toArguments(), problems);
        getProblemQuery().addAll(problems);
        if (getProblemQuery().hasErrors())
            return false;

        String name = entry.getName();

        IBundleCategory category = library.getContainer(
                IBundleContainer.Type.JS).getCategory(
                IBundleCategory.Type.MONOLITHIC);

        category.addFile(new File(tempDir, name + ".js"), name + ".js");

        return true;
    }

    private void configureRandoriCompiler(IBundleLibrary library,
            IBundleConfigurationEntry entry, CompilerArguments arguments)
    {
        arguments.setOutput(tempDir.getAbsolutePath());
        arguments.setJsOutputAsFiles(false);
        arguments.setAppName(entry.getName());

        for (String path : getBundleConfiguration().getLibraryPaths())
        {
            arguments.addLibraryPath(FilenameNormalization.normalize(path));
        }

        // added for commandline bundle compiler
        for (String path : entry.getLibraryPaths())
        {
            arguments.addLibraryPath(FilenameNormalization.normalize(path));
        }

        for (String path : getBundleConfiguration().getExternalLibraryPaths())
        {
            arguments.addLibraryPath(FilenameNormalization.normalize(path));
        }

        for (String path : entry.getSourcePaths())
        {
            arguments.addSourcepath(FilenameNormalization.normalize(path));
        }

        for (String path : entry.getIncludeSources())
        {
            arguments.addIncludedSources(FilenameNormalization.normalize(path));
        }
    }

    private void configureCOMPCCompiler(IBundleLibrary library,
            IBundleConfigurationEntry entry, CompilerArguments arguments)
    {
        arguments.setOutput(tempDir + "/" + entry.getName() + ".swc");

        // XXX Implement external-library-path

        for (String path : entry.getLibraryPaths())
        {
            arguments.addLibraryPath(FilenameNormalization.normalize(path));
            library.addSWC(new SWC(new File(FilenameNormalization
                    .normalize(path))));
        }

        for (String path : entry.getExternalLibraryPaths())
        {
            arguments.addLibraryPath(FilenameNormalization.normalize(path));
        }

        // added for commandline bundle compiler
        for (String path : entry.getLibraryPaths())
        {
            arguments.addLibraryPath(FilenameNormalization.normalize(path));
            library.addSWC(new SWC(new File(FilenameNormalization
                    .normalize(path))));
        }

        for (String path : entry.getSourcePaths())
        {
            arguments.addSourcepath(FilenameNormalization.normalize(path));
        }

        for (String path : entry.getSourcePaths())
        {
            arguments.addIncludedSources(FilenameNormalization.normalize(path));
        }
        
        // add Randori metadata
        arguments.addKeepMetadata("JavaScript");
        arguments.addKeepMetadata("JavaScriptConstructor");
        arguments.addKeepMetadata("JavaScriptMethod");
        arguments.addKeepMetadata("JavaScriptProperty");
        arguments.addKeepMetadata("JavaScriptCode");
        arguments.addKeepMetadata("View");
        arguments.addKeepMetadata("Inject");
    }

    private boolean compileSWC(IBundleLibrary library,
            IBundleConfigurationEntry entry)
    {
        CompilerArguments arguments = new CompilerArguments();
        configureCOMPCCompiler(library, entry, arguments);

        COMPC compc = new COMPC();

        final String[] args = arguments.toArguments();
        @SuppressWarnings("unused")
        int code = compc.mainNoExit(args);

        List<ICompilerProblem> problems = compc.getProblems().getProblems();
        getProblemQuery().addAll(problems);
        if (compc.getProblems().hasErrors())
            return false;

        library.addSWC(new SWC(new File(tempDir, entry.getName() + ".swc")));

        return true;
    }

    private File getOutputDirectory()
    {
        return getTargetSettings().getOutput().getParentFile();
    }

    private void setVersionInfo(IBundle bundle)
    {
        IBundleVersion version = bundle.getVersion();
        version.setBundleVersion(VersionInfo.getLibVersion());

        version.setRandoriVersion(VersionInfo.getRandoriVersion());
        version.setRandoriBuild(VersionInfo.getBuild());
        //version.setRandoriMinSupportedVersion(targetSettings.getMinimumSupportedVersion());
        version.setRandoriMinSupportedVersion("0");

        version.setCompilerName(VersionInfo.getCompilerName());
        version.setCompilerVersion(VersionInfo.getCompilerVersion());
        version.setCompilerBuild(VersionInfo.getCompilerBuild());
    }

}
