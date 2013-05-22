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

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.clients.COMPC;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.swc.SWC;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.bundle.Bundle;
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
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.projects.IRandoriBundleProject;

import com.google.common.io.Files;

/**
 * @author Michael Schmalle
 */
public class RandoriBundleProject extends RandoriProject implements
        IRandoriBundleProject
{
    private IBundleConfiguration bundleConfiguration;

    private IMutableBundle bundle;

    private File outputRBL;

    private File outputDir;

    private File tempDir;

    private IBundleConfiguration getBundleConfiguration()
    {
        return bundleConfiguration;
    }

    public RandoriBundleProject(Workspace workspace)
    {
        super(workspace, IASDocBundleDelegate.NIL_DELEGATE,
                new RandoriBackend());
    }

    @Override
    public boolean configure(IBundleConfiguration configuration)
    {
        this.bundleConfiguration = configuration;
        // TODO Validate IBundleConfiguration
        ArrayList<File> files = new ArrayList<File>();

        Collection<String> bundles = configuration.getBundlePaths();
        // if -bundle-path is present, add all SWCs from the bundles
        if (bundles.size() > 0)
        {
            File output = new File(
                    FilenameNormalization.normalize(configuration.getOutput()));
            addSWCsFromBundles((List<String>) bundles, files,
                    output.getParentFile());
        }

        // add the swcs for the .rbl archives onto the library path
        for (File file : files)
        {
            bundleConfiguration.addLibraryPath(file.getAbsolutePath());
        }

        files = new ArrayList<File>();
        bundles = configuration.getExternalBundlePaths();
        // if -bundle-path is present, add all SWCs from the bundles
        if (bundles.size() > 0)
        {
            File output = new File(
                    FilenameNormalization.normalize(configuration.getOutput()));
            addSWCsFromBundles((List<String>) bundles, files,
                    output.getParentFile());
        }

        // add them as external so they don't get included int he rbl archive
        for (File file : files)
        {
            bundleConfiguration.addExternalLibraryPath(file.getAbsolutePath());
        }

        return true;
    }

    @Override
    protected void validateConfiguration() throws ConfigurationException
    {
    }

    @Override
    protected boolean startCompile(boolean doBuild)
    {
        outputRBL = new File(getBundleConfiguration().getOutput());
        outputDir = outputRBL.getParentFile();

        tempDir = Files.createTempDir();
        tempDir.mkdirs();

        bundle = new Bundle(outputRBL);

        setVersionInfo(bundle);

        BundleLibrary library = new BundleLibrary(getBundleConfiguration()
                .getBundelName());
        // add the libraries to the bundle
        bundle.addLibrary(library);

        // TODO this has to be done based off of compiler args -js-classes-as-files
        library.addContainer(Type.JS).addCategory(
                IBundleCategory.Type.MONOLITHIC);

        for (IBundleConfigurationEntry entry : getBundleConfiguration()
                .getEntries())
        {
            // run the randori
            compileRandori(library, entry);

            // run the compc
            compileSWC(library, entry);
        }

        // write the bundle to disk
        BundleDirectoryWriter writer;
        try
        {
            writer = new BundleDirectoryWriter(outputDir.getAbsolutePath());
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

        finish();

        return true;
    }

    @Override
    protected boolean export()
    {
        File bundleFile = new File(getBundleConfiguration().getOutput());
        bundle.setBundleFile(bundleFile);

        // write the bundle to disk
        BundleWriter writer;
        try
        {
            writer = new BundleWriter(bundle.getBundleFile().getAbsolutePath());
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
            tempDir.deleteOnExit();
    }

    private void compileRandori(IBundleLibrary library,
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

        String name = entry.getName();

        IBundleCategory category = library.getContainer(
                IBundleContainer.Type.JS).getCategory(
                IBundleCategory.Type.MONOLITHIC);

        category.addFile(new File(tempDir, name + ".js"), name + ".js");
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

        for (String path : entry.getSourcePaths())
        {
            arguments.addSourcepath(FilenameNormalization.normalize(path));
        }

        for (String path : entry.getSourcePaths())
        {
            arguments.addIncludedSources(FilenameNormalization.normalize(path));
        }
    }

    private void compileSWC(IBundleLibrary library,
            IBundleConfigurationEntry entry)
    {
        CompilerArguments arguments = new CompilerArguments();
        configureCOMPCCompiler(library, entry, arguments);

        COMPC compc = new COMPC();

        @SuppressWarnings("unused")
        int code = compc.mainNoExit(arguments.toArguments());
        getProblemQuery().addAll(compc.getProblems().getProblems());

        library.addSWC(new SWC(new File(tempDir, entry.getName() + ".swc")));
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
