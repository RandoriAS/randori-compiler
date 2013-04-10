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

package randori.compiler.bundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import randori.compiler.bundle.io.BundleReader;
import randori.compiler.common.VersionInfo;

@Ignore
public class BundleReaderTest
{
    @SuppressWarnings("unused")
    private static final String J_QUERY = "JQuery";

    @SuppressWarnings("unused")
    private static final String HTML_CORE_LIB = "HTMLCoreLib";

    @SuppressWarnings("unused")
    private static final String BUILTIN = "builtin";

    private static final String RANDORI = "randori-framework";

    private static final String RANDORI_GUICE = "randori-guice-framework";

    String path = "C:\\Users\\Work\\Documents\\git-randori\\randori-compiler\\temp\\randori-sdk.zip";

    private BundleReader reader;

    private IBundle bundle;

    @Before
    public void setUp() throws IOException
    {
        reader = new BundleReader(path);
        bundle = reader.getBundle();
    }

    @After
    public void tearDown()
    {
        reader = null;
    }

    @Test
    public void test_init()
    {
        Assert.assertNotNull(reader.getBundle());
        Assert.assertEquals(path, reader.getFile().getAbsolutePath());
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
        //assertEquals(4, bundle.getSWCLibraries(RANDORI).size());
        //assertEquals(1, bundle.getSWCLibraries(RANDORI_GUICE).size());
    }

    @Test
    public void test_getSWCLibrary_Randori() throws IOException
    {
        //assertNotNull(bundle.getSWCLibrary(RANDORI, BUILTIN));
        //assertNotNull(bundle.getSWCLibrary(RANDORI, RANDORI));
        //assertNotNull(bundle.getSWCLibrary(RANDORI, HTML_CORE_LIB));
        //assertNotNull(bundle.getSWCLibrary(RANDORI, J_QUERY));
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
    public void test_version() throws IOException
    {
        assertEquals(VersionInfo.getRandoriVersion(), bundle.getVersion()
                .getRandoriVersion());
    }
}
