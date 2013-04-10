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

package randori.compiler.internal.visitor.as;

import java.io.IOException;
import java.util.List;

import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.internal.client.RandoriCompilerTestBase;
import randori.compiler.internal.projects.RandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class ASWalkerTest extends RandoriCompilerTestBase
{

    private Workspace workspace;

    private RandoriApplicationProject project;

    protected List<ICompilerProblem> getProblems()
    {
        return project.getProblemQuery().getProblems();
    }

    protected IRandoriTargetSettings getTargetSettings()
    {
        return project.getTargetSettings();
    }

    @Before
    public void setUp()
    {
        workspace = new Workspace();
        project = new RandoriApplicationProject(workspace);

        setUpExtras();
        initializeArgs();
        getArgs().setJsOutputAsFiles(false);
    }

    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Test
    public void test_project() throws IOException
    {

        //        ASWalker walker = new ASWalker(new NullASVisitor() {
        //            @Override
        //            public void visitProject(IASProject element)
        //            {
        //                super.visitProject(element);
        //            }
        //        });
        //        walker.walkProject(project);
    }

}
