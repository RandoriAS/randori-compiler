package randori.compiler.bundle;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.apache.flex.compiler.clients.COMPC;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.swc.SWC;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import randori.compiler.bundle.IBundleContainer.Type;
import randori.compiler.bundle.io.BundleWriter;
import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.Randori;
import randori.compiler.common.VersionInfo;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.driver.RandoriBackend;

@Ignore
/**
 * A functional test of compiling the SDK bundle and reading it, while testing
 * it's state along the way.
 */
public class SDKBundleTest
{
    File tempDirectory = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp");

    File builtinSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-sdk\\bin\\builtin.swc");
    File htmlCoreLibSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp\\HTMLCoreLib.swc");
    File jQuerySWC = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp\\JQuery.swc");
    File randoriFrameworkSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp\\randori-framework.swc");
    File randoriGuiceFrameworkSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp\\randori-guice-framework.swc");

    File randoriFrameworkJS = new File(tempDirectory, "\\Randori.js");
    File randoriGuiceFrameworkJS = new File(tempDirectory, "\\RandoriGuice.js");

    String htmlCoreLibSrc = TestConstants.RandoriASFramework
            + "\\randori-libraries\\HTMLCoreLib\\src";
    String jQuerySrc = TestConstants.RandoriASFramework
            + "\\randori-libraries\\JQuery\\src";
    String randoriSrc = TestConstants.RandoriASFramework
            + "\\randori-framework\\src";
    String randoriGuiceSrc = TestConstants.RandoriASFramework
            + "\\randori-guice-framework\\src";

    @Before
    public void setUp() throws IOException
    {

    }

    private void buildRandoriJS()
    {
        CompilerArguments arguments = new CompilerArguments();
        arguments.setOutput(tempDirectory.getAbsolutePath());

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());
        arguments.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());
        arguments.addLibraryPath(jQuerySWC.getAbsolutePath());

        arguments.addSourcepath(randoriGuiceSrc);
        arguments.addSourcepath(randoriSrc);

        HashSet<ICompilerProblem> problems = new HashSet<ICompilerProblem>();

        RandoriBackend backend = new RandoriBackend();
        Randori randori = new Randori(backend);
        final int code = randori.mainNoExit(arguments.toArguments(), problems);
        Assert.assertEquals(0, code);
        //Assert.assertEquals(0, problems.size());
    }

    private void buildJQuerySWC()
    {
        CompilerArguments arguments = new CompilerArguments();
        arguments.setOutput(jQuerySWC.getAbsolutePath());

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());

        arguments.addSourcepath(htmlCoreLibSrc);

        arguments.addIncludedSource(jQuerySrc);

        COMPC compc = new COMPC();
        compc.mainNoExit(arguments.toArguments());
    }

    private void buildHTMLCoreLibSWC()
    {
        CompilerArguments arguments = new CompilerArguments();
        arguments.setOutput(htmlCoreLibSWC.getAbsolutePath());

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());

        arguments.addSourcepath(htmlCoreLibSrc);

        arguments.addIncludedSource(htmlCoreLibSrc);

        COMPC compc = new COMPC();
        compc.mainNoExit(arguments.toArguments());
    }

    private void buildRandoriSWC()
    {
        CompilerArguments arguments = new CompilerArguments();
        arguments.setOutput(randoriFrameworkSWC.getAbsolutePath());

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());
        arguments.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());
        arguments.addLibraryPath(jQuerySWC.getAbsolutePath());

        arguments.addSourcepath(randoriGuiceSrc);
        arguments.addSourcepath(randoriSrc);

        arguments.addIncludedSource(randoriSrc);

        COMPC compc = new COMPC();
        compc.mainNoExit(arguments.toArguments());
    }

    private void buildRandoriGuiceSWC()
    {
        CompilerArguments arguments = new CompilerArguments();
        arguments.setOutput(randoriGuiceFrameworkSWC.getAbsolutePath());

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());
        arguments.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());
        arguments.addLibraryPath(jQuerySWC.getAbsolutePath());

        arguments.addSourcepath(randoriGuiceSrc);

        arguments.addIncludedSource(randoriGuiceSrc);

        COMPC compc = new COMPC();
        compc.mainNoExit(arguments.toArguments());
    }

    @After
    public void tearDown()
    {
        //assertTrue(bundleFile.delete());
    }

    @Test
    public void test_compc() throws IOException
    {
        buildHTMLCoreLibSWC();
        buildJQuerySWC();

        buildRandoriJS();

        //-buildRandoriGuiceJS();
        buildRandoriSWC();
        buildRandoriGuiceSWC();

        createSDKBundle();
    }

    @SuppressWarnings("unused")
    private static final String J_QUERY = "JQuery";

    @SuppressWarnings("unused")
    private static final String HTML_CORE_LIB = "HTMLCoreLib";

    @SuppressWarnings("unused")
    private static final String BUILTIN = "builtin";

    private static final String RANDORI = "randori-framework";

    @SuppressWarnings("unused")
    private static final String RANDORI_GUICE = "randori-guice-framework";

    private Bundle bundle;

    private BundleLibrary randoriLibrary;

    @SuppressWarnings("unused")
    private BundleLibrary randoriGuiceLibrary;

    private File bundleFile;

    private void createSDKBundle() throws IOException
    {
        String path = TestConstants.RandoriASFramework
                + "\\randori-compiler\\temp\\randori-sdk-0.2.1.zip";

        bundleFile = new File(path);
        bundle = new Bundle(bundleFile);

        // target will call this eventually
        setVersionInfo(bundle);

        randoriLibrary = new BundleLibrary(RANDORI);

        randoriLibrary.addSWC(new SWC(builtinSWC));
        randoriLibrary.addSWC(new SWC(jQuerySWC));
        randoriLibrary.addSWC(new SWC(htmlCoreLibSWC));
        randoriLibrary.addSWC(new SWC(randoriFrameworkSWC));
        randoriLibrary.addSWC(new SWC(randoriGuiceFrameworkSWC));

        IBundleContainer container = randoriLibrary.addContainer(Type.JS);
        IBundleCategory entry = container
                .addCategory(IBundleCategory.Type.MONO);

        entry.addFile(randoriFrameworkJS, "Randori.js");
        entry.addFile(randoriGuiceFrameworkJS, "RandoriGuice.js");

        // add the libraries to the bundle
        bundle.addLibrary(randoriLibrary);

        // write the bundle to disk
        BundleWriter writer = new BundleWriter(bundle.getBundleFile()
                .getAbsolutePath());
        writer.write(bundle);
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
