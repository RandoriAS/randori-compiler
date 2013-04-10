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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;

import org.apache.flex.swc.SWC;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
/**
 * @author Michael Schmalle
 */
public class BundleLibraryTest
{
    private IBundleLibrary library;

    @Before
    public void setUp() throws IOException
    {
        library = new BundleLibrary("MyLibrary");
    }

    @After
    public void tearDown()
    {
        library = null;
    }

    @Test
    public void test_getName()
    {
        assertEquals("MyLibrary", library.getName());
    }

    @Test
    public void test_getPath()
    {
        assertEquals("MyLibrary", library.getPath());
    }

    @Test
    public void test_getContainers()
    {
        assertEquals(0, library.getContainers().size());
        assertNotNull(library.addContainer(IBundleContainer.Type.BIN));
        assertNotNull(library.addContainer(IBundleContainer.Type.AS));
        assertNull(library.addContainer(IBundleContainer.Type.AS));
        assertNotNull(library.addContainer(IBundleContainer.Type.JS));
        assertEquals(3, library.getContainers().size());
    }

    @Test
    public void test_getContainer()
    {
        IBundleContainer c1 = library.addContainer(IBundleContainer.Type.BIN);
        IBundleContainer c2 = library.addContainer(IBundleContainer.Type.AS);
        assertSame(c1, library.getContainer(IBundleContainer.Type.BIN));
        assertSame(c2, library.getContainer(IBundleContainer.Type.AS));
        assertNull(library.getContainer(IBundleContainer.Type.JS));
    }

    @Test
    public void test_addContainer()
    {
        assertEquals(0, library.getContainers().size());
        assertNotNull(library.addContainer(IBundleContainer.Type.BIN));
        assertNotNull(library.addContainer(IBundleContainer.Type.AS));
        assertNull(library.addContainer(IBundleContainer.Type.AS));
        assertNotNull(library.addContainer(IBundleContainer.Type.JS));
        assertEquals(3, library.getContainers().size());
    }

    @Test
    public void test_getSWCS()
    {
        assertEquals(0, library.getSWCS().size());
        library.addSWC(new SWC(new File("foo")));
        assertEquals(1, library.getSWCS().size());
    }

    @Test
    public void test_getSWC()
    {
        assertEquals(0, library.getSWCS().size());
        SWC swc = new SWC(new File("foo"));
        library.addSWC(swc);
        assertEquals(1, library.getSWCS().size());
        assertSame(swc, library.getSWC("foo"));
    }

    @Test
    public void test_addSWC()
    {
        assertEquals(0, library.getContainers().size());

        assertEquals(0, library.getSWCS().size());
        library.addSWC(new SWC(new File("foo")));
        library.addSWC(new SWC(new File("bar")));
        library.addSWC(new SWC(new File("bar")));
        library.addSWC(new SWC(new File("baz")));
        assertEquals(3, library.getSWCS().size());

        assertEquals(1, library.getContainers().size());
        assertNotNull(library.getContainer(IBundleContainer.Type.BIN));
        assertNotNull(library.getContainer(IBundleContainer.Type.BIN)
                .getCategory(IBundleCategory.Type.SWC));
    }

}
