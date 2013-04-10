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

package randori.compiler.internal.client;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Michael Schmalle
 */
public class RandoriCompilerArgsTest extends RandoriCompilerTestBase
{
    @Override
    @Before
    public void setUp()
    {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Test
    public void test_js_base_path()
    {
        File generated = new File(outDir, "generated");
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().setJsBasePath(generated.getName());

        compile();
        assertOutFileLength(4);

        Assert.assertTrue(generated.isDirectory());
        Assert.assertTrue(new File(generated, JS_PATH_ROOT_CLASS_AS).isFile());
        Assert.assertTrue(new File(generated, JS_PATH_CLASS_ONE_A).isFile());
        Assert.assertTrue(new File(generated, JS_PATH_SUB_CLASS_ONE_A).isFile());
        Assert.assertTrue(new File(generated, JS_PATH_CLASS_TWO_A).isFile());
    }

    @Test
    public void test_js_output_as_files_True()
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().setJsOutputAsFiles(true);

        compile();
        assertOutFileLength(4);
    }

    @Test
    public void test_js_output_as_files_False()
    {
        // TODO (mschmalle) setJsOutputAsFiles() has project as root folder, what is correct?
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().setAppName("FooBar");
        getArgs().setJsOutputAsFiles(false);

        compile();
        assertOutFileLength(1);
    }

    @Ignore
    @Test
    public void test_js_output_as_files_False_NoAppNameProblem()
    {
        // TODO (mschmalle) Implement test_js_output_as_files_False_NoAppNameProblem()
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().setJsOutputAsFiles(false);

        compile();
        assertOutFileLength(1);
    }
}
