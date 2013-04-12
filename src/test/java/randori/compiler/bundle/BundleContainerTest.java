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
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Schmalle
 */
public class BundleContainerTest
{
    private IBundleLibrary library;

    private IBundleContainer container;

    @Before
    public void setUp() throws IOException
    {
        library = new BundleLibrary("MyLibrary");
        container = new BundleContainer(library, IBundleContainer.Type.BIN);
    }

    @After
    public void tearDown()
    {
        library = null;
        container = null;
    }

    @Test
    public void test_getLibrary()
    {
        assertSame(library, container.getLibrary());
    }

    @Test
    public void test_getType()
    {
        assertEquals(IBundleContainer.Type.BIN, container.getType());
    }

    @Test
    public void test_addCategory()
    {
        IBundleCategory classes = container
                .addCategory(IBundleCategory.Type.CLASSES);
        IBundleCategory classesMin = container
                .addCategory(IBundleCategory.Type.CLASSES_MINIFIED);
        IBundleCategory mono = container
                .addCategory(IBundleCategory.Type.MONOLITHIC);
        assertNotNull(classes);
        assertNotNull(classesMin);
        assertNotNull(mono);
    }

    @Test
    public void test_getCategories()
    {
        assertNotNull(container.addCategory(IBundleCategory.Type.CLASSES));
        assertNotNull(container
                .addCategory(IBundleCategory.Type.CLASSES_MINIFIED));
        assertNotNull(container.addCategory(IBundleCategory.Type.MONOLITHIC));

        IBundleCategory[] categories = container.getCategories().toArray(
                new IBundleCategory[0]);
        assertEquals(3, categories.length);
        assertEquals("classes", categories[0].getName());
        assertEquals("classes_minified", categories[1].getName());
        assertEquals("monolithic", categories[2].getName());
    }

    @Test
    public void test_getCategory()
    {
        IBundleCategory classes = container
                .addCategory(IBundleCategory.Type.CLASSES);
        IBundleCategory classesMin = container
                .addCategory(IBundleCategory.Type.CLASSES_MINIFIED);
        assertSame(classes, container.getCategory(IBundleCategory.Type.CLASSES));
        assertSame(classesMin,
                container.getCategory(IBundleCategory.Type.CLASSES_MINIFIED));
    }

}
