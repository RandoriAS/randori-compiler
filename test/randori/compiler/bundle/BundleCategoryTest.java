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
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Schmalle
 */
public class BundleCategoryTest extends BaseBundleTest
{

    private IBundleLibrary library;

    private IBundleContainer container;

    private BundleCategory category;

    @Before
    public void setUp() throws IOException
    {
        library = new BundleLibrary("MyLibrary");
        container = new BundleContainer(library, IBundleContainer.Type.BIN);
        category = new BundleCategory(container, IBundleCategory.Type.SWC);
    }

    @After
    public void tearDown()
    {
        library = null;
        container = null;
        category = null;
    }

    @Test
    public void test_getType()
    {
        assertEquals(IBundleCategory.Type.SWC, category.getType());
        assertEquals("swc", category.getType().getName());
        assertEquals("swc", category.getName());
    }

    @Test
    public void test_addFile()
    {
        assertEquals(0, category.getEntries().size());
        category.addFile(new File("foo"), "bar.swc");
        assertEquals(1, category.getEntries().size());
        assertEquals("MyLibrary/bin/swc/bar.swc", category.getEntries()
                .iterator().next().getPath());
    }

    @Test
    public void test_addFile_entries()
    {
        category.addFile(new File("foo"), "foo.swc");
        category.addFile(new File("foo/bar"), "foo/bar.swc");
        IBundleEntry[] entries = category.getEntries().toArray(
                new IBundleEntry[0]);
        assertEquals("MyLibrary/bin/swc/foo.swc", entries[0].getPath());
        assertEquals("MyLibrary/bin/swc/foo/bar.swc", entries[1].getPath());
    }

    @Test
    public void test_addFile_duplicates()
    {
        // duplicate is determined from the path, not the File
        assertEquals(0, category.getEntries().size());
        category.addFile(new File("foo"), "bar.swc");
        category.addFile(new File("foo"), "bar.swc");
        category.addFile(new File("foo"), "goo.swc");
        assertEquals(2, category.getEntries().size());
    }

    @Test
    public void test_getEntries()
    {
        assertEquals(0, category.getEntries().size());
        category.addFile(new File("foo"), "foo.swc");
        category.addFile(new File("bar"), "bar.swc");
        category.addFile(new File("baz"), "baz.swc");
        assertEquals(3, category.getEntries().size());
    }

    @Test
    public void test_getLibrary()
    {
        assertSame(library, category.getLibrary());
        assertSame(library, category.getContainer().getLibrary());
    }

    @Test
    public void test_getContainer()
    {
        assertSame(container, category.getContainer());
    }

}
