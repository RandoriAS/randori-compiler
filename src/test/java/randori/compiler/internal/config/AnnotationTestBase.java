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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.flex.compiler.clients.problems.ProblemPrinter;
import org.apache.flex.compiler.clients.problems.WorkspaceProblemFormatter;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import org.junit.Before;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.internal.client.RandoriCompilerTestBase;
import randori.compiler.internal.config.annotation.AnnotationManager;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.projects.RandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class AnnotationTestBase extends RandoriCompilerTestBase
{
    protected Workspace workspace;

    protected RandoriApplicationProject project;

    protected File srcAnnotationCore = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-compiler\\src\\test\\resources"
                    + "\\as\\src_annotation_core"));

    protected File srcAnnotation = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "\\randori-compiler\\src\\test\\resources"
                    + "\\as\\src_annotation"));

    @Override
    protected List<ICompilerProblem> getProblems()
    {
        return project.getProblemQuery().getProblems();
    }

    protected IRandoriTargetSettings getTargetSettings()
    {
        return project.getTargetSettings();
    }

    @Override
    @Before
    public void setUp()
    {
        workspace = new Workspace();
        project = new RandoriApplicationProject(workspace);
        
        //XXX Temp until we get this into the core SWC
        ((AnnotationManager) project.getAnnotationManager()).setEnabled(true);

        setUpExtras();
        initializeArgs();
        getArgs().setJsOutputAsFiles(false);
    }

    @Override
    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Override
    protected void compile()
    {
        boolean configure = project.configure(getArgs().toArguments());
        assertSuccess(configure);
        boolean success = project.compile(false);
        assertSuccess(success);
    }

    protected void includeClass(String qualifiedName)
    {
        File file = new File(srcAnnotation, qualifiedName.replace(".", "/")
                + ".as");
        assertTrue(file.exists());
        getArgs().addIncludedSources(file.getAbsolutePath());
    }

    protected void assertSuccess(boolean success)
    {
        if (!success)
        {
            printProblems(project.getProblemQuery().getFilteredProblems());
        }
        assertTrue(success);
    }

    protected void printProblems(Iterable<ICompilerProblem> problems)
    {
        final WorkspaceProblemFormatter formatter = new WorkspaceProblemFormatter(
                workspace);
        final ProblemPrinter printer = new ProblemPrinter(formatter);
        printer.printProblems(problems);
    }

}
