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

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.bundle.BundleConfiguration;
import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleConfigurationEntry;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IBundleLibrary;
import randori.compiler.bundle.io.BundleReader;
import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.Randori;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.driver.RandoriBackend;

/**
 * @author Michael Schmalle
 */
public class RandoriBundleProjectTest extends RandoriTestCaseBase
{
    File builtinSWC = new File(TestConstants.RandoriASFramework
            + "/randori-sdk/randori-framework/bin/swc/builtin.swc");
    File htmlCoreLibSWC = new File(TestConstants.RandoriASFramework
            + "/randori-sdk/randori-framework/bin/swc/HTMLCoreLib.swc");
    File jQuerySWC = new File(TestConstants.RandoriASFramework
            + "/randori-sdk/randori-framework/bin/swc/JQuery.swc");

    String randoriSrc = TestConstants.RandoriASFramework
            + "/randori-framework/src";
    String randoriGuiceSrc = TestConstants.RandoriASFramework
            + "/randori-guice-framework/src";

    protected File sdkRBL = new File(
            FilenameNormalization
                    .normalize("src/test/resources/libs/randori-sdk.rbl"));

    private BundleConfiguration configuration;

    private Workspace workspace;

    private RandoriBundleProject project;
    private final String hmssProjectPath = getTestDataPath() + "test/HMSS_RBL";
    private RandoriApplicationProject applicationCompiler;

    @Before
    public void setUp() throws IOException
    {
        workspace = new Workspace();
        //createSDKConfiguration();
        project = new RandoriBundleProject(workspace, new RandoriBackend());
        applicationCompiler = new RandoriApplicationProject(new Workspace());
        // Cleanup before tests to allow test output examination.
        cleanupGenerated();
    }

    @After
    public void tearDown()
    {
        configuration = null;
        workspace = null;
        project = null;
    }

    @Test
    public void test_commandline_compiler()
    {
        String path = TestConstants.RandoriASFramework
                + "/randori-compiler/temp/bundle/randori-sdk-test.rbl";

        configuration = new BundleConfiguration("randori-framework", path);

        // dependent compiled libraries
        configuration.addLibraryPath(builtinSWC.getAbsolutePath());
        configuration.addLibraryPath(jQuerySWC.getAbsolutePath());
        configuration.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());

        IBundleConfigurationEntry randori = configuration
                .addEntry("randori-framework");
        randori.addSourcePath(randoriGuiceSrc);
        randori.addSourcePath(randoriSrc);
        randori.addIncludeSources(randoriSrc);

        IBundleConfigurationEntry guice = configuration
                .addEntry("randori-guice-framework");
        guice.addSourcePath(randoriGuiceSrc);

        int code = Randori.staticMainNoExit(configuration.toArguments(), null);
        Assert.assertEquals(0, code);
    }

    @Test
    public void test_compile()
    {
        String path = TestConstants.RandoriASFramework
                + "/randori-compiler/temp/bundle/randori-sdk-test.rbl";

        configuration = new BundleConfiguration("randori-framework", path);

        // dependent compiled libraries
        configuration.addLibraryPath(builtinSWC.getAbsolutePath());
        configuration.addLibraryPath(jQuerySWC.getAbsolutePath());
        configuration.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());

        IBundleConfigurationEntry randori = configuration
                .addEntry("randori-framework");
        randori.addSourcePath(randoriGuiceSrc);
        randori.addSourcePath(randoriSrc);
        randori.addIncludeSources(randoriSrc);

        IBundleConfigurationEntry guice = configuration
                .addEntry("randori-guice-framework");
        guice.addSourcePath(randoriGuiceSrc);

        project.configure(configuration);
        boolean success = project.compile(true, true);
        Assert.assertTrue(success);
    }

    @Test
    public void test_compile_with_rbl()
    {
        /*
         * 1. Create a BundleConfiguration.
         * 2. Add -external-bundle-path
         * 3. And a bundle config entry that acts like a BundleLibrary
         * 4. Add -source-path to the entry
         * 5. configure()
         * 6. compile()
         */
        String source = TestConstants.RandoriASFramework + "/randori-compiler/"
                + "src/test/resources/functional_compiler";
        String outputFile = TestConstants.RandoriASFramework
                + "/randori-compiler/temp/bundle/test-bundle.rbl";

        File target = new File(outputFile);
        Assert.assertFalse(target.exists());

        BundleConfiguration config = new BundleConfiguration("test-bundle",
                outputFile);

        // add the sdk as external so the swcs will not get packaged
        // within the new bundle
        config.setSDKPath(sdkRBL.getAbsolutePath());

        String libraryName = "test-library";
        IBundleConfigurationEntry randori = config.addEntry(libraryName);
        randori.addSourcePath(source);

        project.configure(config);
        boolean success = project.compile(true, true);

        Assert.assertTrue(success);
        Assert.assertTrue(target.exists());

        // check the manifest that is only has 2 entries
        BundleReader reader = new BundleReader(outputFile);
        IBundle bundle = reader.getBundle();
        IBundleLibrary library = bundle.getLibrary(libraryName);
        IBundleCategory swcs = library.getContainer(IBundleContainer.Type.BIN)
                .getCategory(IBundleCategory.Type.SWC);
        Assert.assertEquals(1, swcs.getEntries().size());

        Assert.assertTrue(FileUtils.deleteQuietly(target));
    }

    /////////////////////////////////////////////////////////

    private void cleanupGenerated()
    {
        try
        {
            final File generated = new File(hmssProjectPath + "/generated");
            FileUtils.deleteDirectory(generated);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void compileCommonModule()
    {
        configuration = new BundleConfiguration("CommonModule", hmssProjectPath
                + "/generated/libs/CommonModule.rbl");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("CommonModule");
        entry.addSourcePath(hmssProjectPath + "/CommonModule/src");

        configuration.addExternalBundlePath(getTestDataPath()
                + "/sdk/randori-sdk.rbl");

        project.configure(configuration);
        boolean success = project.compile(true, true);

        Assert.assertTrue("CommonModule should compile", success);
        Assert.assertFalse("No problems should be reported", project
                .getProblemQuery().hasErrors());
    }

    @Test
    public void compileLabModuleWithRblSource()
    {
        configuration = new BundleConfiguration("LabModule", hmssProjectPath
                + "/generated/libs/LabModule.rbl");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("LabModule");
        entry.addSourcePath(hmssProjectPath + "/LabModule/src");
        entry.addSourcePath(hmssProjectPath + "/CommonModule/src");
        //configuration.addBundlePath(hmssProjectPath + "/CommonModule/src");
        //configuration.addExternalBundlePath(getTestDataPath()
        //        + "/sdk/randori-sdk-0.2.3.rbl");
        configuration.addExternalBundlePath(sdkRBL.getAbsolutePath());

        project.configure(configuration);
        boolean success = project.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", project
                .getProblemQuery().hasErrors());
    }

    @Test
    public void compileLabModuleWithRblFile()
    {
        configuration = new BundleConfiguration("LabModule", hmssProjectPath
                + "/generated/libs/LabModule.rbl");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("LabModule");
        entry.addSourcePath(hmssProjectPath + "/LabModule/src");

        configuration.setSDKPath(getTestDataPath() + "/sdk/randori-sdk.rbl");
        configuration.addExternalBundlePath(hmssProjectPath
                + "/LabModule/libs/CommonModule.rbl");

        project.configure(configuration);
        boolean success = project.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", project
                .getProblemQuery().hasErrors());
    }

    @Test
    public void compileHMSSWithRblSources()
    {
        CompilerArguments arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmssProjectPath + "/generated");
        arguments.addSourcepath(hmssProjectPath + "/src");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addSourcepath(hmssProjectPath + "/CommonModule/src");
        arguments.addSourcepath(hmssProjectPath + "/LabModule/src");

        arguments.setSDKPath(getTestDataPath() + "/sdk/randori-sdk.rbl");

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported",
                applicationCompiler.getProblemQuery().hasErrors());
    }

    @Test
    public void compileHMSSWithRblFiles()
    {
        CompilerArguments arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmssProjectPath + "/generated");
        arguments.addSourcepath(hmssProjectPath + "/src");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        //arguments.addBundlePath(hmssProjectPath + "/libs/CommonModule.rbl");
        arguments.addBundlePath(hmssProjectPath + "/libs/LabModule.rbl");

        arguments.setSDKPath(getTestDataPath() + "/sdk/randori-sdk.rbl");

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported",
                applicationCompiler.getProblemQuery().hasErrors());
    }
}
