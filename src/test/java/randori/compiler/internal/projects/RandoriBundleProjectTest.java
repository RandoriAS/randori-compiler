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
import org.junit.*;

import randori.compiler.bundle.BundleConfiguration;
import randori.compiler.bundle.IBundleConfigurationEntry;
import randori.compiler.internal.constants.TestConstants;

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

    @Before
    public void setUp() throws IOException
    {
        workspace = new Workspace();
        createSDKConfiguration();
        project = new RandoriBundleProject(workspace);

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
    public void test_compile()
    {
        project.configure(configuration);
        boolean success = project.compile(true, true);
        Assert.assertTrue(success);
    }

    @Test
    public void test_compile_with_rbl()
    {
        String source = TestConstants.RandoriASFramework + "/randori-compiler/"
                + "src/test/resources/functional_compiler";
        String path = TestConstants.RandoriASFramework
                + "/randori-compiler/temp/bundle/test-bundle.rbl";
        File target = new File(path);

        Assert.assertFalse(target.exists());

        BundleConfiguration config = new BundleConfiguration("test-bundle",
                path);

        // add the sdk as external so the swcs will not get packaged
        // within the new bundle
        config.addExternalBundlePath(sdkRBL.getAbsolutePath());

        IBundleConfigurationEntry randori = config.addEntry("test-library");
        randori.addSourcePath(source);

        project.configure(config);
        boolean success = project.compile(true, true);
        Assert.assertTrue(success);
        Assert.assertTrue(target.exists());
        Assert.assertTrue(target.delete());
    }

    private void createSDKConfiguration() throws IOException
    {
        String path = TestConstants.RandoriASFramework
                + "/randori-compiler/temp/bundle/randori-sdk-0.2.3.rbl";

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
                + "/generated/libs");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("CommonModule");
        entry.addSourcePath(hmssProjectPath + "/CommonModule/src");

        configuration.addExternalBundlePath(getTestDataPath()
                + "/sdk/randori-sdk-0.2.3.rbl");

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
                + "/generated/libs");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("LabModule");
        entry.addSourcePath(hmssProjectPath + "/LabModule/src");

        configuration.addBundlePath(hmssProjectPath + "/CommonModule/src");
        configuration.addExternalBundlePath(getTestDataPath()
                + "/sdk/randori-sdk-0.2.3.rbl");

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
                + "/generated/libs");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("LabModule");
        entry.addSourcePath(hmssProjectPath + "/LabModule/src");

        configuration.addExternalBundlePath(hmssProjectPath
                + "/LabModule/libs/CommonModule.rbl");
        configuration.addExternalBundlePath(getTestDataPath()
                + "/sdk/randori-sdk-0.2.3.rbl");

        project.configure(configuration);
        boolean success = project.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", project
                .getProblemQuery().hasErrors());
    }

    @Ignore
    @Test
    public void compileHMSSWithRblSources()
    {
        //
    }

    @Ignore
    @Test
    public void compileHMSSWithRblFiles()
    {
        //
    }
}
