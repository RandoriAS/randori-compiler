package randori.compiler.internal.projects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.bundle.BundleConfiguration;
import randori.compiler.bundle.IBundleConfigurationEntry;
import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.Randori;
import randori.compiler.internal.driver.RandoriBackend;

/**
 * @author Frédéric THOMAS Date: 23/05/13 Time: 23:02
 */
public class HMSSTest
{
    private static final String RESOURCES_TEST_BASE_PATH = "src/test/resources/";

    private final String hmssProjectPath = getTestDataPath() + "test/HMSS_RBL";
    private final String randoriSdkRblPath;

    private CompilerArguments arguments;
    private BundleConfiguration configuration;

    private RandoriApplicationProject applicationCompiler;
    private RandoriBundleProject bundleCompiler;

    private Workspace workspace;
    private RandoriBackend backend;
    @SuppressWarnings("unused")
    private Randori randori;

    public HMSSTest()
    {
        randoriSdkRblPath = getTestDataPath() + "/sdk/randori-sdk.rbl";
    }

    @Before
    public void setUp() throws IOException
    {
        backend = new RandoriBackend();
        randori = new Randori(backend);

        workspace = new Workspace();
        bundleCompiler = new RandoriBundleProject(workspace);
        applicationCompiler = new RandoriApplicationProject(workspace);

        // Cleanup before tests to allow test output examination.
        cleanupGenerated();
    }

    @After
    public void tearDown()
    {
        configuration = null;
        arguments = null;

        backend = null;
        randori = null;
        arguments = null;
        workspace = null;

        bundleCompiler = null;
        applicationCompiler = null;
    }

    /**
     * Return relative path to the test data. Not intended to be overridden.
     * 
     * @return Project relative path to the test data.
     */
    private String getTestDataPath()
    {
        return RESOURCES_TEST_BASE_PATH
                + this.getClass().getPackage().getName().replace(".", "/")
                + "/";
    }

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

        configuration.setSDKPath(randoriSdkRblPath);

        bundleCompiler.configure(configuration);
        boolean success = bundleCompiler.compile(true, true);

        Assert.assertTrue("CommonModule should compile", success);
        Assert.assertFalse("No problems should be reported", bundleCompiler
                .getProblemQuery().hasErrors());

        File fileToTest = new File(hmssProjectPath
                + "/generated/libs/CommonModule.rbl");
        Assert.assertTrue("CommonModule.rbl should exist", fileToTest.exists());
        Assert.assertTrue("CommonModule.rbl size should be > 0",
                fileToTest.length() > 0);

        // Prepare the next tests.
        try
        {
            FileUtils.copyFile(fileToTest, new File(hmssProjectPath
                    + "/LabModule/libs/CommonModule.rbl"));
            FileUtils.copyFile(fileToTest, new File(hmssProjectPath
                    + "/libs/CommonModule.rbl"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileToTest = new File(hmssProjectPath
                + "/LabModule/libs/CommonModule.rbl");
        Assert.assertTrue("CommonModule.rbl should have been copied into "
                + hmssProjectPath + "/LabModule/libs", fileToTest.exists());
        fileToTest = new File(hmssProjectPath + "/libs/CommonModule.rbl");
        Assert.assertTrue("CommonModule.rbl should have been copied into "
                + hmssProjectPath + "/libs", fileToTest.exists());
    }

    @Test
    public void compileLabModuleWithRblSource()
    {
        configuration = new BundleConfiguration("LabModule", hmssProjectPath
                + "/generated/libs/LabModule.rbl");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("LabModule");
        entry.addSourcePath(hmssProjectPath + "/CommonModule/src");
        entry.addSourcePath(hmssProjectPath + "/LabModule/src");

        configuration.setSDKPath(randoriSdkRblPath);

        bundleCompiler.configure(configuration);
        boolean success = bundleCompiler.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", bundleCompiler
                .getProblemQuery().hasErrors());

        File fileToTest = new File(hmssProjectPath
                + "/generated/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should exist", fileToTest.exists());
        Assert.assertTrue("LabModule.rbl size should be > 0",
                fileToTest.length() > 0);

        // Prepare the next tests.
        try
        {
            FileUtils.copyFile(fileToTest, new File(hmssProjectPath
                    + "/libs/LabModule.rbl"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileToTest = new File(hmssProjectPath + "/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should have been copied into "
                + hmssProjectPath + "/libs", fileToTest.exists());
    }

    @Test
    public void compileLabModuleWithRblFile()
    {
        configuration = new BundleConfiguration("LabModule", hmssProjectPath
                + "/generated/libs/LabModule.rbl");

        final IBundleConfigurationEntry entry = configuration
                .addEntry("LabModule");
        entry.addSourcePath(hmssProjectPath + "/LabModule/src");

        configuration.setSDKPath(randoriSdkRblPath);
        configuration.addExternalBundlePath(hmssProjectPath
                + "/LabModule/libs/CommonModule.rbl");

        bundleCompiler.configure(configuration);
        boolean success = bundleCompiler.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", bundleCompiler
                .getProblemQuery().hasErrors());

        File fileToTest = new File(hmssProjectPath
                + "/generated/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should exist", fileToTest.exists());
        Assert.assertTrue("LabModule.rbl size should be > 0",
                fileToTest.length() > 0);

        // Prepare the next tests.
        try
        {
            FileUtils.copyFile(fileToTest, new File(hmssProjectPath
                    + "/libs/LabModule.rbl"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileToTest = new File(hmssProjectPath + "/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should have been copied into "
                + hmssProjectPath + "/libs", fileToTest.exists());
    }

    @Test
    public void compileHmssWithRblSources()
    {
        arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmssProjectPath);
        arguments.addSourcepath(hmssProjectPath + "/src");
        arguments.setJsBasePath("generated");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addSourcepath(hmssProjectPath + "/CommonModule/src");
        arguments.addSourcepath(hmssProjectPath + "/LabModule/src");

        arguments.setSDKPath(randoriSdkRblPath);

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported",
                applicationCompiler.getProblemQuery().hasErrors());

        final List<String> paths = new ArrayList<String>(9);
        paths.add(hmssProjectPath + "/generated/behaviors");
        paths.add(hmssProjectPath + "/generated/mediators");
        paths.add(hmssProjectPath + "/generated/services");
        paths.add(hmssProjectPath + "/generated/startup");
        paths.add(hmssProjectPath + "/generated/behaviors/EchoBehavior.js");
        paths.add(hmssProjectPath + "/generated/mediators/IndexMediator.js");
        paths.add(hmssProjectPath + "/generated/mediators/TargetsMediator.js");
        paths.add(hmssProjectPath + "/generated/services/TargetsService.js");
        paths.add(hmssProjectPath + "/generated/startup/DemoContext.js");

        for (String path : paths)
        {
            final File file = new File(path);
            Assert.assertTrue(file.getName() + " should exist", file.exists());
        }
    }

    @Test
    public void compileHmssWithRblFiles()
    {
        arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmssProjectPath);
        arguments.addSourcepath(hmssProjectPath + "/src");
        arguments.setJsBasePath("generated");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addBundlePath(hmssProjectPath
                + "/LabModule/libs/CommonModule.rbl");
        arguments.addBundlePath(hmssProjectPath + "/libs/LabModule.rbl");

        arguments.setSDKPath(randoriSdkRblPath);

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported",
                applicationCompiler.getProblemQuery().hasErrors());

        final List<String> paths = new ArrayList<String>(9);
        paths.add(hmssProjectPath + "/generated/behaviors");
        paths.add(hmssProjectPath + "/generated/mediators");
        paths.add(hmssProjectPath + "/generated/services");
        paths.add(hmssProjectPath + "/generated/startup");
        paths.add(hmssProjectPath + "/generated/behaviors/EchoBehavior.js");
        paths.add(hmssProjectPath + "/generated/mediators/IndexMediator.js");
        paths.add(hmssProjectPath + "/generated/mediators/TargetsMediator.js");
        paths.add(hmssProjectPath + "/generated/services/TargetsService.js");
        paths.add(hmssProjectPath + "/generated/startup/DemoContext.js");

        File fileToTest;
        for (String path : paths)
        {
            fileToTest = new File(path);
            Assert.assertTrue(fileToTest.getName() + " should exist",
                    fileToTest.exists());
        }
    }
}
