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

package randori.compiler.bundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.flex.compiler.filespecs.IBinaryFileSpecification;
import org.apache.flex.compiler.problems.CompilerProblem;
import org.apache.flex.swc.SWC;
import org.apache.flex.utils.DAByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import randori.compiler.bundle.IBundleContainer.Type;
import randori.compiler.bundle.io.BundleWriter;
import randori.compiler.common.VersionInfo;
import randori.compiler.internal.constants.TestConstants;

@Ignore
public class BundleWriterTest
{
    private static final String J_QUERY = "JQuery";

    private static final String HTML_CORE_LIB = "HTMLCoreLib";

    private static final String BUILTIN = "builtin";

    private static final String RANDORI = "randori-framework";

    private static final String RANDORI_GUICE = "randori-guice-framework";

    // File tempDir = new File(TestConstants.RandoriASFramework);
    File tempDir = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp");

    private Bundle bundle;

    private BundleLibrary randoriLibrary;

    private BundleLibrary randoriGuiceLibrary;

    private File bundleFile;

    @Before
    public void setUp() throws IOException
    {
        createSDKBundle();
        assertTrue(bundleFile.exists());
    }

    @After
    public void tearDown()
    {
        //assertTrue(bundleFile.delete());
    }

    private void createSDKBundle() throws IOException
    {
        String path = TestConstants.RandoriASFramework
                + "\\randori-compiler\\temp\\randori-sdk.zip";

        bundleFile = new File(path);
        bundle = new Bundle(bundleFile);

        // target will call this eventually
        setVersionInfo(bundle);

        randoriLibrary = new BundleLibrary(RANDORI);
        randoriGuiceLibrary = new BundleLibrary(RANDORI_GUICE);

        randoriLibrary.addSWC(new SWC(new File(TestConstants.RandoriASFramework
                + "\\randori-sdk\\bin\\builtin.swc")));
        randoriLibrary.addSWC(new SWC(new File(tempDir,
                "\\randori-framework.swc")));
        randoriLibrary.addSWC(new SWC(new File(tempDir, "\\JQuery.swc")));
        randoriLibrary.addSWC(new SWC(new File(tempDir, "\\HTMLCoreLib.swc")));

        IBundleContainer container = randoriLibrary.addContainer(Type.JS);
        IBundleCategory entry = container
                .addCategory(IBundleCategory.Type.MONOLITHIC);
        // for now this takes care of wrapping it in a source entry
        entry.addFile(new File(tempDir, "\\Randori.js"), "Randori.js");

        //---------------------------------------------------

        randoriGuiceLibrary.addSWC(new SWC(new File(tempDir,
                "\\randori-guice-framework.swc")));

        container = randoriGuiceLibrary.addContainer(Type.JS);
        entry = container.addCategory(IBundleCategory.Type.MONOLITHIC);

        // for now this takes care of wrapping it in a source entry
        entry.addFile(new File(tempDir, "\\RandoriGuice.js"), "RandoriGuice.js");

        // dd the libraries to the bundle
        bundle.addLibrary(randoriLibrary);
        bundle.addLibrary(randoriGuiceLibrary);

        // write the bundle to disk
        BundleWriter writer = new BundleWriter(bundle.getBundleFile()
                .getAbsolutePath());
        writer.write(bundle);
    }

    /*
     * 1) Create a Bundle
     * 2) Set version info on the bundle
     * 3) Create a BundleLibrary with name
     * 4) Add SWCs to the library
     * 5) Create a source container of Type
     * 6) Add source paths to the container (no directories... YET)
     * 7) Add the library to the bundle
     * 8) Create a BundleWriter
     * 9) Write the bundle
     */

    @Test
    public void test_setup() throws IOException
    {
        assertNotNull(bundle);
        assertNotNull(bundleFile);
        assertNotNull(randoriGuiceLibrary);
        assertNotNull(randoriLibrary);
    }

    @Test
    public void test_getBundleFile() throws IOException
    {
        assertEquals(bundleFile, bundle.getBundleFile());
    }

    @Test
    public void test_getVersion() throws IOException
    {
        assertNotNull(bundle.getVersion());
    }

    @Test
    public void test_getLibraries() throws IOException
    {
        Collection<IBundleLibrary> libraries = bundle.getLibraries();
        assertNotNull(libraries);
        assertEquals(2, libraries.size());
    }

    @Test
    public void test_getLibrary() throws IOException
    {
        IBundleLibrary lib1 = bundle.getLibrary(RANDORI);
        assertNotNull(lib1);
        assertEquals(RANDORI, lib1.getName());
        IBundleLibrary lib2 = bundle.getLibrary(RANDORI_GUICE);
        assertNotNull(lib2);
        assertEquals(RANDORI_GUICE, lib2.getName());
    }

    @Test
    public void test_getSWCLibraries() throws IOException
    {
        assertEquals(4, bundle.getSWCLibraries(RANDORI).size());
        assertEquals(1, bundle.getSWCLibraries(RANDORI_GUICE).size());
    }

    @Test
    public void test_getSWCLibrary_Randori() throws IOException
    {
        assertNotNull(bundle.getSWCLibrary(RANDORI, BUILTIN));
        assertNotNull(bundle.getSWCLibrary(RANDORI, RANDORI));
        assertNotNull(bundle.getSWCLibrary(RANDORI, HTML_CORE_LIB));
        assertNotNull(bundle.getSWCLibrary(RANDORI, J_QUERY));
    }

    @Test
    public void test_getSWCLibrary_RandoriGuice() throws IOException
    {
        assertNotNull(bundle.getSWCLibrary(RANDORI_GUICE,
                "randori-guice-framework"));
    }

    @Test
    public void test_getContainers_Randori() throws IOException
    {
        // bin, js
        assertEquals(2, bundle.getContainers(RANDORI).size());
    }

    @Test
    public void test_getContainers_RandoriGuice() throws IOException
    {
        // bin, js
        assertEquals(2, bundle.getContainers(RANDORI_GUICE).size());
    }

    @Test
    public void test_getProblems() throws IOException
    {
        assertEquals(0, bundle.getProblems().size());
        bundle.addProblem(new CompilerProblem("foo") {
        });
        assertEquals(1, bundle.getProblems().size());
    }

    /**
     * Returns the contents of a binary file as an array of bytes, or null if
     * the file cannot be read.
     */
    @SuppressWarnings("unused")
    private byte[] getContents(IBinaryFileSpecification fileSpec)
    {
        byte[] contents = null;

        try
        {
            final DAByteArrayOutputStream buffer = new DAByteArrayOutputStream();
            final InputStream fileInputStream = fileSpec.createInputStream();
            IOUtils.copy(fileInputStream, buffer);
            IOUtils.closeQuietly(buffer);
            IOUtils.closeQuietly(fileInputStream);
            contents = buffer.getDirectByteArray();
        }
        catch (IOException e)
        {
        }

        return contents;
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
