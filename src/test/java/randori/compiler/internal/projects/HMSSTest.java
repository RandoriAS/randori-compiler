package randori.compiler.internal.projects;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frédéric THOMAS Date: 23/05/13 Time: 23:02
 */
public class HMSSTest
{
    private static final String RESOURCES_TEST_BASE_PATH = "src/test/resources/";

    // Project with sources for each modules (HMSS + CommonModule + LabModule)
    private final String hmss = getTestDataPath() + "test/HMSS_RBL";

    // Project with sources of the main module and the nested one (HMSS + LabModule) + CommonModule.rbl
    private final String hmssWithCommonRbl = getTestDataPath() + "test/HMSS_RBL1";

    // Project with sources of the main (HMSS) + LabModule.rbl + CommonModule.rbl
    private final String hmssWithCommonAndNestedRbls = getTestDataPath() + "test/HMSS_RBL2";

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
        bundleCompiler = new RandoriBundleProject(workspace, backend);
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
        return RESOURCES_TEST_BASE_PATH + this.getClass().getPackage().getName().replace(".", "/") + "/";
    }

    private void cleanupGenerated()
    {
        try
        {
            File generated = new File(hmss + "/generated");
            FileUtils.deleteDirectory(generated);

            generated = new File(hmssWithCommonRbl + "/generated");
            FileUtils.deleteDirectory(generated);

            generated = new File(hmssWithCommonAndNestedRbls + "/generated");
            FileUtils.deleteDirectory(generated);
            
            // delete libs that are copied from rbl archives
            FileUtils.deleteQuietly(new File(hmssWithCommonRbl + "/libs/CommonModule.js"));

            FileUtils.deleteQuietly(new File(hmssWithCommonAndNestedRbls + "/libs/CommonModule.js"));
            FileUtils.deleteQuietly(new File(hmssWithCommonAndNestedRbls + "/libs/LabModule.js"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Test
    public void parseHmss()
    {
        arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmss);
        arguments.addSourcepath(hmss + "/src");
        arguments.setJsBasePath("generated");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addSourcepath(hmss + "/CommonModule/src");
        arguments.addSourcepath(hmss + "/LabModule/src");

        arguments.setSDKPath(randoriSdkRblPath);

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(false, false);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported", applicationCompiler.getProblemQuery().hasErrors());
    }

    @Test
    /**
     * Compile the CommonModule and copy the generated rbl to hmssWithCommonRbl and hmssWithCommonAndNestedRbls.
     */
    public void compileCommonModuleFromSources()
    {
        configuration = new BundleConfiguration("CommonModule", hmss + "/generated/libs/CommonModule.rbl");

        final IBundleConfigurationEntry entry = configuration.addEntry("CommonModule");
        entry.addSourcePath(hmss + "/CommonModule/src");

        configuration.setSDKPath(randoriSdkRblPath);

        bundleCompiler.configure(configuration);
        boolean success = bundleCompiler.compile(true, true);

        Assert.assertTrue("CommonModule should compile", success);
        Assert.assertFalse("No problems should be reported", bundleCompiler.getProblemQuery().hasErrors());

        File fileToTest = new File(hmss + "/generated/libs/CommonModule.rbl");
        Assert.assertTrue("CommonModule.rbl should exist", fileToTest.exists());
        Assert.assertTrue("CommonModule.rbl size should be > 0", fileToTest.length() > 0);

        // Prepare the next tests.
        try
        {
            FileUtils.copyFile(fileToTest, new File(hmssWithCommonRbl + "/LabModule/libs/CommonModule.rbl"));
            FileUtils.copyFile(fileToTest, new File(hmssWithCommonAndNestedRbls + "/libs/CommonModule.rbl"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileToTest = new File(hmssWithCommonRbl + "/LabModule/libs/CommonModule.rbl");
        Assert.assertTrue("CommonModule.rbl should have been copied into " + hmssWithCommonRbl + "/LabModule/libs",
                fileToTest.exists());
        fileToTest = new File(hmssWithCommonAndNestedRbls + "/libs/CommonModule.rbl");
        Assert.assertTrue("CommonModule.rbl should have been copied into " + hmssWithCommonAndNestedRbls + "/libs",
                fileToTest.exists());
    }

    @Test
    /**
     * Compile the LabModule and copy the generated rbl to hmssWithCommonAndNestedRbls.
     */
    public void compileLabModuleFromSources()
    {
        configuration = new BundleConfiguration("LabModule", hmss + "/generated/libs/LabModule.rbl");

        final IBundleConfigurationEntry entry = configuration.addEntry("LabModule");
        entry.addSourcePath(hmss + "/CommonModule/src");
        entry.addSourcePath(hmss + "/LabModule/src");

        configuration.setSDKPath(randoriSdkRblPath);

        bundleCompiler.configure(configuration);
        boolean success = bundleCompiler.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", bundleCompiler.getProblemQuery().hasErrors());

        File fileToTest = new File(hmss + "/generated/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should exist", fileToTest.exists());
        Assert.assertTrue("LabModule.rbl size should be > 0", fileToTest.length() > 0);

        // Prepare the next tests.
        try
        {
            FileUtils.copyFile(fileToTest, new File(hmssWithCommonAndNestedRbls + "/libs/LabModule.rbl"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileToTest = new File(hmssWithCommonAndNestedRbls + "/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should have been copied into " + hmssWithCommonAndNestedRbls + "/libs",
                fileToTest.exists());
    }

    @Test
    /**
     * Compile the LabModule using CommonModule.rbl and copy the generated rbl to hmssWithCommonAndNestedRbls.
     */
    public void compileLabModuleWithCommonRblFile()
    {
        configuration = new BundleConfiguration("LabModule", hmssWithCommonRbl + "/generated/libs/LabModule.rbl");

        final IBundleConfigurationEntry entry = configuration.addEntry("LabModule");
        entry.addSourcePath(hmssWithCommonRbl + "/LabModule/src");

        configuration.setSDKPath(randoriSdkRblPath);
        configuration.addExternalBundlePath(hmssWithCommonRbl + "/LabModule/libs/CommonModule.rbl");

        bundleCompiler.configure(configuration);
        boolean success = bundleCompiler.compile(true, true);

        Assert.assertTrue("LabModule should compile", success);
        Assert.assertFalse("No problems should be reported", bundleCompiler.getProblemQuery().hasErrors());

        File fileToTest = new File(hmssWithCommonRbl + "/generated/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should exist", fileToTest.exists());
        Assert.assertTrue("LabModule.rbl size should be > 0", fileToTest.length() > 0);

        // Prepare the next tests.
        try
        {
            FileUtils.copyFile(fileToTest, new File(hmssWithCommonAndNestedRbls + "/libs/LabModule.rbl"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        fileToTest = new File(hmssWithCommonAndNestedRbls + "/libs/LabModule.rbl");
        Assert.assertTrue("LabModule.rbl should have been copied into " + hmssWithCommonAndNestedRbls + "/libs",
                fileToTest.exists());
    }

    @Test
    /**
     * Compile HMSS using sources of the common and the nested modules.
     */
    public void compileHmssWithModuleSources()
    {
        arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmss);
        arguments.addSourcepath(hmss + "/src");
        arguments.setJsBasePath("generated");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addSourcepath(hmss + "/CommonModule/src");
        arguments.addSourcepath(hmss + "/LabModule/src");

        arguments.setSDKPath(randoriSdkRblPath);

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported", applicationCompiler.getProblemQuery().hasErrors());

        final List<String> paths = new ArrayList<String>(13);
        paths.add(hmss + "/generated/behaviors");
        paths.add(hmss + "/generated/mediator");
        paths.add(hmss + "/generated/mediators");
        paths.add(hmss + "/generated/services");
        paths.add(hmss + "/generated/startup");
        paths.add(hmss + "/generated/behaviors/EchoBehavior.js");
        paths.add(hmss + "/generated/mediator/LabMediator.js");
        paths.add(hmss + "/generated/mediators/IndexMediator.js");
        paths.add(hmss + "/generated/mediators/TargetsMediator.js");
        paths.add(hmss + "/generated/services/parser/GenericJsonParser.js");
        paths.add(hmss + "/generated/services/LabService.js");
        paths.add(hmss + "/generated/services/TargetsService.js");
        paths.add(hmss + "/generated/startup/DemoContext.js");

        for (String path : paths)
        {
            final File file = new File(path);
            Assert.assertTrue(file.getName() + " should exist", file.exists());
        }
    }

    @Test
    /**
     * Compile HMSS using the RBL of the common module and the sources of the nested modules.
     */
    public void compileHmssWithSourcesAndCommonRblFiles()
    {
        arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmssWithCommonRbl);
        arguments.addSourcepath(hmssWithCommonRbl + "/src");
        arguments.setJsBasePath("generated");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addBundlePath(hmssWithCommonRbl + "/LabModule/libs/CommonModule.rbl");
        arguments.addSourcepath(hmssWithCommonRbl + "/LabModule/src");

        arguments.setSDKPath(randoriSdkRblPath);

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported", applicationCompiler.getProblemQuery().hasErrors());

        final List<String> paths = new ArrayList<String>(13);
        paths.add(hmssWithCommonRbl + "/generated/behaviors");
        paths.add(hmssWithCommonRbl + "/generated/mediator");
        paths.add(hmssWithCommonRbl + "/generated/mediators");
        paths.add(hmssWithCommonRbl + "/generated/services");
        paths.add(hmssWithCommonRbl + "/generated/startup");
        paths.add(hmssWithCommonRbl + "/generated/behaviors/EchoBehavior.js");
        paths.add(hmssWithCommonRbl + "/generated/mediator/LabMediator.js");
        paths.add(hmssWithCommonRbl + "/generated/mediators/IndexMediator.js");
        paths.add(hmssWithCommonRbl + "/generated/mediators/TargetsMediator.js");
        paths.add(hmssWithCommonRbl + "/generated/services/LabService.js");
        paths.add(hmssWithCommonRbl + "/generated/services/TargetsService.js");
        paths.add(hmssWithCommonRbl + "/generated/startup/DemoContext.js");
        
        paths.add(hmssWithCommonRbl + "/libs/CommonModule.js");

        File fileToTest;
        for (String path : paths)
        {
            fileToTest = new File(path);
            Assert.assertTrue(fileToTest.getName() + " should exist", fileToTest.exists());
        }
    }

    @Test
    /**
     * Compile HMSS using RBLs of the common and the nested modules.
     */
    public void compileHmssWithRblFiles()
    {
        arguments = new CompilerArguments();

        arguments.setAppName("HMSS");
        arguments.setOutput(hmssWithCommonAndNestedRbls);
        arguments.addSourcepath(hmssWithCommonAndNestedRbls + "/src");
        arguments.setJsBasePath("generated");
        arguments.setJsLibraryPath("libs");
        arguments.setJsOutputAsFiles(true);

        arguments.addBundlePath(hmssWithCommonAndNestedRbls + "/libs/CommonModule.rbl");
        arguments.addBundlePath(hmssWithCommonAndNestedRbls + "/libs/LabModule.rbl");

        arguments.setSDKPath(randoriSdkRblPath);

        applicationCompiler.configure(arguments.toArguments());
        boolean success = applicationCompiler.compile(true, true);

        Assert.assertTrue("HMSS should compile", success);
        Assert.assertFalse("No problems should be reported", applicationCompiler.getProblemQuery().hasErrors());

        final List<String> paths = new ArrayList<String>(13);
        paths.add(hmssWithCommonAndNestedRbls + "/generated/behaviors");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/mediators");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/services");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/startup");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/behaviors/EchoBehavior.js");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/mediators/IndexMediator.js");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/mediators/TargetsMediator.js");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/services/TargetsService.js");
        paths.add(hmssWithCommonAndNestedRbls + "/generated/startup/DemoContext.js");
        
        paths.add(hmssWithCommonAndNestedRbls + "/libs/LabModule.js");
        paths.add(hmssWithCommonAndNestedRbls + "/libs/CommonModule.js");
        
        File fileToTest;
        for (String path : paths)
        {
            fileToTest = new File(path);
            Assert.assertTrue(fileToTest.getName() + " should exist", fileToTest.exists());
        }
    }
}
