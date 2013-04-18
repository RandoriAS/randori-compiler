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

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Schmalle
 */
public class RandoriTest extends RandoriCompilerTestBase
{

    @Override
    @Before
    public void setUp()
    {
        super.setUp();

        getArgs().setJsOutputAsFiles(true);
    }

    @Override
    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Test
    public void test_compile_from_sourcepath()
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());

        compile();
        assertOutFileLength(4);
    }

    @Test
    public void test_compile_RootClass()
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().addIncludedSources(RootClassFile.getAbsolutePath());

        compile();
        assertOutFileLength(1);
    }

    @Test
    public void test_compile_SubClassOneA()
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().addIncludedSources(SubClassOneAFile.getAbsolutePath());

        compile();
        assertOutFileLength(2);
    }

    @Test
    public void test_compile_ClassTwoA()
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().addIncludedSources(ClassTwoAFile.getAbsolutePath());

        compile();
        assertOutFileLength(1);
    }

}
