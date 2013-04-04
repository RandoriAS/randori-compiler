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

import java.io.IOException;

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

    private void printProblems(Iterable<ICompilerProblem> problems)
    {
        final WorkspaceProblemFormatter formatter = new WorkspaceProblemFormatter(
                workspace);
        final ProblemPrinter printer = new ProblemPrinter(formatter);
        printer.printProblems(problems);
    }
}
