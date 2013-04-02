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
import java.util.Set;

import org.apache.flex.compiler.problems.ICompilerProblem;
import org.junit.After;
import org.junit.Assert;
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

    @Before
    public void setUp()
    {
        backend = new RandoriBackend();
        //backend.parseOnly(true);
        randori = new Randori(backend);
    }

    @After
    public void tearDown()
    {
        backend = null;
        randori = null;
    }

    @Test
    public void test_init()
    {
        CompilerArguments arguments = new CompilerArguments();

        arguments.setOutput(outDir.getAbsolutePath());
        arguments.setJsOutputAsFiles(true);
        arguments.addLibraryPath(builtinSWC.getAbsolutePath());
        arguments.addSourcepath(new File(basepath).getAbsolutePath());

        // need to only parse not generate
        final int code = randori.mainNoExit(arguments.toArguments(), problems);

        Assert.assertEquals(0, code);

    }
}
