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

package randori.compiler.internal.projects;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.clients.problems.ProblemPrinter;
import org.apache.flex.compiler.clients.problems.WorkspaceProblemFormatter;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.internal.client.RandoriCompilerTestBase;
import randori.compiler.projects.IRandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class RandoriApplicationTest extends RandoriCompilerTestBase
{
    private Workspace workspace;

    private IRandoriApplicationProject project;

    protected List<ICompilerProblem> getProblems()
    {
        return project.getProblemQuery().getProblems();
    }

    @Before
    public void setUp()
    {
        workspace = new Workspace();
        project = new RandoriApplicationProject(workspace);

        setUpExtras();
        initializeArgs();
    }

    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Test
    public void test_basic_compile() throws IOException
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());

        project.configure(getArgs().toArguments());
        boolean success = project.compile(true);
        if (!success)
        {
            printProblems(project.getProblemQuery().getFilteredProblems());
        }
        Assert.assertTrue(success);
        assertOutFileLength(4);

        FileUtils.deleteDirectory(outDir);
    }

    @Test
    public void test_export_with_sdk_problem() throws IOException
    {
        getArgs().addSourcepath(basepathDir.getAbsolutePath());

        project.configure(getArgs().toArguments());
        boolean success = project.compile(true, true);
        Assert.assertFalse(success);

        Assert.assertEquals(1, getProblems().size());
    }

    @Test
    public void test_basic_compile_with_export_from_directory()
            throws IOException
    {
        // with passing export= true, the compiler will copy
        // the libs from the sdk to the js-library-path
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().setJsLibraryPath("libs");
        getArgs().setSDKPath(sdkDir.getAbsolutePath());

        project.configure(getArgs().toArguments());
        boolean success = project.compile(true, true);
        Assert.assertTrue(success);

        Assert.assertTrue(new File(outDir, "libs").isDirectory());
        Assert.assertTrue(new File(outDir, "libs/Randori.js").exists());
        Assert.assertTrue(new File(outDir, "libs/RandoriGuice.js").exists());
    }

    @Test
    public void test_basic_compile_with_export_from_rbl() throws IOException
    {
        // with passing export= true, the compiler will copy
        // the libs from the sdk to the js-library-path
        getArgs().addSourcepath(basepathDir.getAbsolutePath());
        getArgs().setJsLibraryPath("libs");
        getArgs().setSDKPath(sdkRBL.getAbsolutePath());

        project.configure(getArgs().toArguments());
        boolean success = project.compile(true, true);
        Assert.assertTrue(success);

        Assert.assertTrue(new File(outDir, "libs").isDirectory());
        Assert.assertTrue(new File(outDir, "libs/Randori.js").exists());
        Assert.assertTrue(new File(outDir, "libs/RandoriGuice.js").exists());
    }

    private void printProblems(Iterable<ICompilerProblem> problems)
    {
        final WorkspaceProblemFormatter formatter = new WorkspaceProblemFormatter(
                workspace);
        final ProblemPrinter printer = new ProblemPrinter(formatter);
        printer.printProblems(problems);
    }
}
