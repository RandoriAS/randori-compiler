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

package randori.compiler.internal.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.flex.compiler.clients.problems.ProblemPrinter;
import org.apache.flex.compiler.clients.problems.WorkspaceProblemFormatter;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.config.IAnnotationDefinition;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.internal.client.RandoriCompilerTestBase;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.projects.RandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class AnnotationTest extends RandoriCompilerTestBase
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

    // C:\Users\Work\Documents\git-randori\randori-compiler\test\as\src_annotation

    protected File srcAnnotation = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-compiler\\test\\as\\src_annotation"));

    @Test
    public void test_AnnotationUsage()
    {
        getArgs().addSourcepath(srcAnnotation.getAbsolutePath());
        project.configure(getArgs().toArguments());
        boolean success = project.compile(false);
        assertSuccess(success);
        
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition("AnnotationUsage");
        Assert.assertEquals("AnnotationUsage", definition.getBaseName());
        Assert.assertEquals("AnnotationUsage", definition.getQualifiedName());
    }
    
    @Test
    public void test_JavaScript()
    {
        getArgs().addSourcepath(srcAnnotation.getAbsolutePath());

        project.configure(getArgs().toArguments());
        boolean success = project.compile(false);
        assertSuccess(success);

        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition("JavaScript");

        Assert.assertNotNull(definition);
        Assert.assertEquals("JavaScript", definition.getBaseName());
        Assert.assertEquals("JavaScript", definition.getQualifiedName());
    }

    protected void assertSuccess(boolean success)
    {
        if (!success)
        {
            printProblems(project.getProblemQuery().getFilteredProblems());
        }
        Assert.assertTrue(success);
    }

    private void printProblems(Iterable<ICompilerProblem> problems)
    {
        final WorkspaceProblemFormatter formatter = new WorkspaceProblemFormatter(
                workspace);
        final ProblemPrinter printer = new ProblemPrinter(formatter);
        printer.printProblems(problems);
    }

}
