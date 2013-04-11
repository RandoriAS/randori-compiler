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
import java.util.Set;

import org.apache.flex.compiler.clients.problems.ProblemPrinter;
import org.apache.flex.compiler.clients.problems.WorkspaceProblemFormatter;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import static org.junit.Assert.*;
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

        setUpExtras();
        initializeArgs();
        getArgs().setJsOutputAsFiles(false);

        compile();
    }

    @Override
    protected void compile()
    {
        getArgs().addSourcepath(srcAnnotation.getAbsolutePath());
        project.configure(getArgs().toArguments());
        boolean success = project.compile(false);
        assertSuccess(success);
    }

    @Override
    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    //--------------------------------------------------------------------------
    // Annotation Definitions
    //--------------------------------------------------------------------------
    
    @Test
    public void test_Annotation()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.Annotation");
        assertEquals("Annotation", definition.getBaseName());
        assertEquals("randori.annotations.Annotation",
                definition.getQualifiedName());
    }
    
    @Test
    public void test_Retention()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.Retention");
        assertEquals("Retention", definition.getBaseName());
        assertEquals("randori.annotations.Retention",
                definition.getQualifiedName());
    }
    
    @Test
    public void test_Target()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.Target");
        assertEquals("Target", definition.getBaseName());
        assertEquals("randori.annotations.Target",
                definition.getQualifiedName());
    }

    @Test
    public void test_JavaScript()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.JavaScript");

        assertNotNull(definition);
        assertEquals("JavaScript", definition.getBaseName());
        assertEquals("randori.annotations.JavaScript",
                definition.getQualifiedName());
    }

    //--------------------------------------------------------------------------
    // Annotation Attributes
    //--------------------------------------------------------------------------
    
    @Test
    public void test_JavaScript_validTarget()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.JavaScript");

        assertEquals(1, definition.getTargets().size());
        assertTrue(definition.isValidTarget(IAnnotationDefinition.TARGET_CLASS));
        assertFalse(definition.isValidTarget(IAnnotationDefinition.TARGET_ALL));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_CONSTRUCTOR));
        assertFalse(definition.isValidTarget(IAnnotationDefinition.TARGET_FIELD));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_INTERFACE));
        assertFalse(definition.isValidTarget(IAnnotationDefinition.TARGET_METHOD));
        assertFalse(definition.isValidTarget(IAnnotationDefinition.TARGET_PROPERTY));
    }

    @Test
    public void test_JavaScriptAnnotated()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.JavaScript");
        Set<ICompilationUnit> set = project.getScope()
                .getCompilationUnitsByDefinitionName("JavaScriptAnnotated");
        ICompilationUnit[] units = set.toArray(new ICompilationUnit[] {});

        IClassDefinition cdef = getClassDefinition(units[0]);

        assertTrue(definition.isValidTarget(cdef));
        assertTrue(definition.isValidTarget("class"));
    }

    protected void assertSuccess(boolean success)
    {
        if (!success)
        {
            printProblems(project.getProblemQuery().getFilteredProblems());
        }
        assertTrue(success);
    }

    private void printProblems(Iterable<ICompilerProblem> problems)
    {
        final WorkspaceProblemFormatter formatter = new WorkspaceProblemFormatter(
                workspace);
        final ProblemPrinter printer = new ProblemPrinter(formatter);
        printer.printProblems(problems);
    }

    private IClassDefinition getClassDefinition(ICompilationUnit unit)
    {
        IFileNode fileNode = null;
        try
        {
            fileNode = (IFileNode) unit.getSyntaxTreeRequest().get().getAST();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        IDefinition[] definitions = fileNode.getTopLevelDefinitions(false,
                false);
        return (IClassDefinition) definitions[0];
    }

}
