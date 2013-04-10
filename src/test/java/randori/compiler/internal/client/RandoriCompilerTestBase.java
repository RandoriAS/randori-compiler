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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import org.junit.Before;

import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.Randori;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.driver.RandoriBackend;

/**
 * @author Michael Schmalle
 */
public class RandoriCompilerTestBase
{
    protected static final String JS_PATH_CLASS_TWO_A = "test\\two\\ClassTwoA.js";
    protected static final String JS_PATH_SUB_CLASS_ONE_A = "test\\one\\a\\SubClassOneA.js";
    protected static final String JS_PATH_CLASS_ONE_A = "test\\one\\ClassOneA.js";
    protected static final String JS_PATH_ROOT_CLASS_AS = "test\\RootClass.js";

    protected static final String PATH_CLASS_TWO_A = "test\\two\\ClassTwoA.as";
    protected static final String PATH_SUB_CLASS_ONE_A = "test\\one\\a\\SubClassOneA.as";
    protected static final String PATH_CLASS_ONE_A = "test\\one\\ClassOneA.as";
    protected static final String PATH_ROOT_CLASS_AS = "test\\RootClass.as";

    protected static final String PATH_JAVASCRIPT_AS = "test\\JavaScript.as";

    protected File tempDir = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-compiler\\temp"));

    protected File outDir = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-compiler\\temp\\out"));

    protected File builtinSWC = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-compiler\\temp\\builtin.swc"));

    protected File basepathDir = new File(
            FilenameNormalization
                    .normalize(TestConstants.RandoriASFramework
                            + "\\randori-compiler\\src\\test\\resources\\functional_compiler"));

    protected File sdkDir = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-sdk"));

    protected File sdkRBL = new File(tempDir, "randori-sdk-0.2.3.rbl");

    //----------------------------------
    // 
    //----------------------------------

    protected File RootClassFile = new File(basepathDir, PATH_ROOT_CLASS_AS);

    protected File JavaScriptFile = new File(basepathDir, PATH_JAVASCRIPT_AS);

    protected File ClassOneAFile = new File(basepathDir, PATH_CLASS_ONE_A);

    protected File SubClassOneAFile = new File(basepathDir,
            PATH_SUB_CLASS_ONE_A);

    protected File ClassTwoAFile = new File(basepathDir, PATH_CLASS_TWO_A);

    //----------------------------------
    // 
    //----------------------------------

    private RandoriBackend backend;

    private Randori randori;

    private List<ICompilerProblem> problems;

    private CompilerArguments arguments;

    protected List<ICompilerProblem> getProblems()
    {
        return problems;
    }

    protected CompilerArguments getArgs()
    {
        return arguments;
    }

    @Before
    public void setUp()
    {
        backend = new RandoriBackend();
        randori = new Randori(backend);

        setUpExtras();
        initializeArgs();
    }

    protected void initializeArgs()
    {
        getArgs().addLibraryPath(builtinSWC.getAbsolutePath());
        getArgs().setOutput(outDir.getAbsolutePath());
        getArgs().setJsOutputAsFiles(true);
    }

    protected void setUpExtras()
    {
        problems = new ArrayList<ICompilerProblem>();
        arguments = new CompilerArguments();
    }

    @After
    public void tearDown() throws IOException
    {
        backend = null;
        randori = null;
        arguments = null;

        FileUtils.deleteDirectory(outDir);
        assertFalse(outDir.exists());
    }

    protected void compile()
    {
        final int code = randori.mainNoExit(getArgs().toArguments(),
                new HashSet<ICompilerProblem>(getProblems()));
        assertFinish(code);
    }

    protected void assertOutFileLength(int numFiles)
    {
        String[] extensions = new String[] { "js" };
        Collection<File> files = FileUtils.listFiles(outDir, extensions, true);
        for (File file : files)
        {
            assertTrue(file.isFile());
        }
        assertEquals(numFiles, files.size());
    }

    protected void assertFinish(int code)
    {
        if (problems.size() > 0)
        {
            System.out.println(problems);
        }
        assertEquals("Prolems ", 0, getProblems().size());
        assertEquals("Error code ", 0, code);
    }

}
