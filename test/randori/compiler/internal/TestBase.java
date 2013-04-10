/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.projects.FlexProjectConfigurator;
import org.apache.flex.compiler.internal.tree.as.FunctionNode;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.mxml.IMXMLNamespaceMapping;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import randori.compiler.codegen.as.IASEmitter;
import randori.compiler.driver.IBackend;
import randori.compiler.internal.codegen.as.ASFilterWriter;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.driver.as.ASBackend;
import randori.compiler.visitor.as.IASBlockWalker;

@Ignore
/**
 * @author Michael Schmalle
 */
public class TestBase
{
    protected List<ICompilerProblem> errors;

    protected static Workspace workspace = new Workspace();

    protected FlexProject project;

    protected IBackend backend;

    protected ASFilterWriter writer;

    protected IASEmitter emitter;

    protected IASBlockWalker visitor;

    protected String mCode;

    protected File tempDir;

    private List<File> sourcePaths = new ArrayList<File>();

    private List<File> libraries = new ArrayList<File>();

    private List<IMXMLNamespaceMapping> namespaceMappings = new ArrayList<IMXMLNamespaceMapping>();

    @Before
    public void setUp()
    {
        errors = new ArrayList<ICompilerProblem>();

        project = new FlexProject(workspace);
        FlexProjectConfigurator.configure(project);

        backend = createBackend();
        writer = backend.createWriterBuffer(project);
        emitter = backend.createEmitter(writer);
        visitor = backend.createWalker(project, errors, emitter);

        sourcePaths = new ArrayList<File>();
        libraries = new ArrayList<File>();
        namespaceMappings = new ArrayList<IMXMLNamespaceMapping>();

        tempDir = new File(FilenameNormalization.normalize("temp")); // ensure this exists
    }

    @After
    public void tearDown()
    {
        backend = null;
        writer = null;
        emitter = null;
        visitor = null;
    }

    protected IBackend createBackend()
    {
        return new ASBackend();
    }

    protected IASNode compile(String code)
    {
        File tempFile = null;
        try
        {
            String tempFileName = getClass().getSimpleName();

            tempFile = File.createTempFile(tempFileName, ".as", tempDir);
            tempFile.deleteOnExit();

            BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
            out.write(code);
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        addDependencies();

        String normalizedMainFileName = FilenameNormalization
                .normalize(tempFile.getAbsolutePath());

        Collection<ICompilationUnit> mainFileCompilationUnits = workspace
                .getCompilationUnits(normalizedMainFileName, project);

        ICompilationUnit cu = null;
        for (ICompilationUnit cu2 : mainFileCompilationUnits)
        {
            if (cu2 != null)
                cu = cu2;
        }

        IASNode fileNode = null;
        try
        {
            fileNode = cu.getSyntaxTreeRequest().get().getAST();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return fileNode;
    }

    protected void assertOut(String code)
    {
        mCode = writer.toString();
        //System.out.println(mCode);
        assertThat(mCode, is(code));
    }

    /**
     * Overridable setup of dependencies, default adds source, libraries and
     * namepsaces.
     * <p>
     * The test will then set the dependencies on the current
     * {@link ICompilerProject}.
     */
    protected void addDependencies()
    {
        addSourcePaths(sourcePaths);
        addLibraries(libraries);
        addNamespaceMappings(namespaceMappings);

        project.setSourcePath(sourcePaths);
        project.setLibraries(libraries);
        project.setNamespaceMappings(namespaceMappings);
    }

    protected void addLibraries(List<File> libraries)
    {
        String base = TestConstants.RandoriASFramework;
        libraries.add(new File(FilenameNormalization.normalize(base
                + "\\randori-sdk\\randori-framework\\bin\\swc\\builtin.swc")));
    }

    protected void addSourcePaths(List<File> sourcePaths)
    {
        sourcePaths.add(tempDir);
    }

    protected void addNamespaceMappings(
            List<IMXMLNamespaceMapping> namespaceMappings)
    {
    }

    protected IASNode findFirstDescendantOfType(IASNode node,
            Class<? extends IASNode> nodeType)
    {
        int n = node.getChildCount();
        for (int i = 0; i < n; i++)
        {
            IASNode child = node.getChild(i);
            if (child instanceof FunctionNode)
            {
                ((FunctionNode) child).parseFunctionBody(errors);
            }
            if (nodeType.isInstance(child))
                return child;

            IASNode found = findFirstDescendantOfType(child, nodeType);
            if (found != null)
                return found;
        }

        return null;
    }

    @Override
    public String toString()
    {
        return writer.toString();
    }
}
