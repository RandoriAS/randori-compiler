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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.problems.ICompilerProblem;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.Randori;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.driver.RandoriBackend;

/**
 * @author Michael Schmalle
 */
public class RandoriTest
{
    private RandoriBackend backend;

    private Randori randori;

    private Set<ICompilerProblem> problems;

    String basepath = TestConstants.RandoriASFramework
            + "\\randori-compiler\\test\\src1";

    String RootClass = basepath + "\\test\\RootClass.as";

    String ClassOneA = basepath + "\\test\\ClassOneA.as";

    String SubClassOneA = basepath + "\\test\\SubClassOneA.as";

    String ClassTwoA = basepath + "\\test\\ClassTwoA.as";

    File builtinSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp\\builtin.swc");

    File outDir = new File(TestConstants.RandoriASFramework
            + "\\randori-compiler\\temp\\out");

    private CompilerArguments arguments;

    @Before
    public void setUp()
    {
        backend = new RandoriBackend();
        //backend.parseOnly(true);
        randori = new Randori(backend);

        problems = new HashSet<ICompilerProblem>();
        arguments = new CompilerArguments();

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());
        arguments.setJsOutputAsFiles(true);
        arguments.setOutput(outDir.getAbsolutePath());
    }

    @After
    public void tearDown()
    {
        backend = null;
        randori = null;
        arguments = null;

        assertTrue(outDir.delete());
    }

    @Test
    public void test_compile_from_sourcepath()
    {
        // add all base sources
        arguments.addSourcepath(new File(basepath).getAbsolutePath());

        final int code = randori.mainNoExit(arguments.toArguments(), problems);

        assertEquals(0, code);
        assertEquals(0, problems.size());

        String[] extensions = new String[] { "js" };
        Collection<File> files = FileUtils.listFiles(outDir, extensions, true);
        assertEquals(4, files.size());
    }

    @Test
    public void test_compile_RootClass()
    {
        // add all base sources
        arguments.addSourcepath(new File(basepath).getAbsolutePath());
        // once the compiler senses an include source, it will only parse
        // it and it's dependencies
        arguments.addIncludedSources(RootClass);

        final int code = randori.mainNoExit(arguments.toArguments(), problems);

        assertEquals(0, code);
        assertEquals(0, problems.size());

        String[] extensions = new String[] { "js" };
        Collection<File> files = FileUtils.listFiles(outDir, extensions, true);
        assertEquals(1, files.size());
    }
}
