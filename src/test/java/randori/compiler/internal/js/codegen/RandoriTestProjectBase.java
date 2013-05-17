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

package randori.compiler.internal.js.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.flex.compiler.internal.units.SourceCompilationUnitFactory;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IGetterNode;
import org.apache.flex.compiler.tree.as.IInterfaceNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.tree.as.IScopedDefinitionNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.ISetterNode;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.Assert;

import randori.compiler.driver.IBackend;
import randori.compiler.internal.TestBase;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.driver.RandoriBackend;

/**
 * @author Michael Schmalle
 */
public abstract class RandoriTestProjectBase extends TestBase
{
    private String normalizedMainFileName;

    protected IFileNode fileNode;

    protected IClassNode classNode;

    protected IInterfaceNode interfaceNode;

    protected IFunctionNode functionNode;

    private Collection<ICompilerProblem> problems;

    protected Collection<ICompilerProblem> getFileProblems()
    {
        return problems;
    }

    @Override
    public void setUp()
    {
        super.setUp();
        String typeUnderTest = getTypeUnderTest().replaceAll("\\.", "/");
        normalizedMainFileName = FilenameNormalization.normalize(getBasePath()
                + "\\" + typeUnderTest + ".as");

        fileNode = compile(normalizedMainFileName);
        if (fileNode == null)
            return;
        Assert.assertNotNull(fileNode);
        // child 0 is always the PackageNode
        IScopedDefinitionNode scopedNode = (IScopedDefinitionNode) findFirstDescendantOfType(
                fileNode.getChild(0), IScopedDefinitionNode.class);
        Assert.assertNotNull(scopedNode);
        if (scopedNode instanceof IClassNode)
            classNode = (IClassNode) scopedNode;
        else if (scopedNode instanceof IInterfaceNode)
            interfaceNode = (IInterfaceNode) scopedNode;
        else if (scopedNode instanceof IFunctionNode)
            functionNode = (IFunctionNode) scopedNode;
    }

    abstract protected String getTypeUnderTest();

    protected String getBasePath()
    {
        return null;
    }

    @Override
    protected IFileNode compile(String main)
    {
        addDependencies();

        ICompilationUnit cu = null;
        String normalizedMainFileName = FilenameNormalization.normalize(main);

        SourceCompilationUnitFactory compilationUnitFactory = project
                .getSourceCompilationUnitFactory();
        File normalizedMainFile = new File(normalizedMainFileName);
        if (compilationUnitFactory.canCreateCompilationUnit(normalizedMainFile))
        {
            Collection<ICompilationUnit> mainFileCompilationUnits = workspace
                    .getCompilationUnits(normalizedMainFileName, project);

            for (ICompilationUnit cu2 : mainFileCompilationUnits)
            {
                if (cu2 != null)
                    cu = cu2;
            }
        }

        if (cu == null)
            return null;

        problems = new ArrayList<ICompilerProblem>();

        // Build the AST.
        IFileNode fileNode = null;
        try
        {
            fileNode = (IFileNode) cu.getSyntaxTreeRequest().get().getAST();
            Collections.addAll(problems, cu.getSyntaxTreeRequest().get()
                    .getProblems());
            Collections.addAll(problems, cu.getFileScopeRequest().get()
                    .getProblems());
            Collections.addAll(problems, cu.getOutgoingDependenciesRequest()
                    .get().getProblems());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return fileNode;
    }

    @Override
    protected void addLibraries(List<File> libraries)
    {
        super.addLibraries(libraries);

        String base = TestConstants.RandoriASFramework;
        libraries.add(new File(FilenameNormalization.normalize(base
                + "\\randori-libraries\\HTMLCoreLib\\bin\\HTMLCoreLib.swc")));
        libraries.add(new File(FilenameNormalization.normalize(base
                + "\\randori-libraries\\JQuery\\bin\\JQuery.swc")));
        libraries.add(new File(FilenameNormalization.normalize(base
                + "\\randori-framework\\bin\\randori-framework.swc")));
        libraries
                .add(new File(
                        FilenameNormalization
                                .normalize(base
                                        + "\\randori-guice-framework\\bin\\randori-guice-framework.swc")));
    }

    @Override
    protected void addSourcePaths(List<File> sourcePaths)
    {
        //super.addSourcePaths(sourcePaths);
        String base = TestConstants.RandoriASFramework;
        //        sourcePaths.add(new File(FilenameNormalization.normalize(base
        //                + "\\randori-libraries\\HTMLCoreLib\\src")));
        //        sourcePaths.add(new File(FilenameNormalization.normalize(base
        //                + "\\randori-libraries\\JQuery\\src")));
        //        sourcePaths.add(new File(FilenameNormalization.normalize(base
        //                + "\\randori-framework\\src")));
        //        sourcePaths.add(new File(FilenameNormalization.normalize(base
        //                + "\\randori-guice-framework\\src")));
        sourcePaths.add(new File(FilenameNormalization.normalize(base
                + "\\randori-demos-bundle\\HMSS\\src")));
    }

    @Override
    protected IBackend createBackend()
    {
        return new RandoriBackend();
    }

    protected IVariableNode findField(String name, IClassNode node)
    {
        IDefinitionNode[] nodes = node.getAllMemberNodes();
        for (IDefinitionNode inode : nodes)
        {
            if (inode.getName().equals(name))
                return (IVariableNode) inode;
        }
        return null;
    }

    protected IFunctionNode findFunction(String name, IClassNode node)
    {
        IDefinitionNode[] nodes = node.getAllMemberNodes();
        for (IDefinitionNode inode : nodes)
        {
            if (inode.getName().equals(name))
                return (IFunctionNode) inode;
        }
        return null;
    }

    protected IGetterNode findGetter(String name, IClassNode node)
    {
        IDefinitionNode[] nodes = node.getAllMemberNodes();
        for (IDefinitionNode inode : nodes)
        {
            if (inode.getName().equals(name) && inode instanceof IGetterNode)
                return (IGetterNode) inode;
        }
        return null;
    }

    protected ISetterNode findSetter(String name, IClassNode node)
    {
        IDefinitionNode[] nodes = node.getAllMemberNodes();
        for (IDefinitionNode inode : nodes)
        {
            if (inode.getName().equals(name) && inode instanceof ISetterNode)
                return (ISetterNode) inode;
        }
        return null;
    }

    protected ITypeNode findTypeNode(IPackageNode node)
    {
        IScopedNode scope = node.getScopedNode();
        for (int i = 0; i < scope.getChildCount(); i++)
        {
            IASNode child = scope.getChild(i);
            if (child instanceof ITypeNode)
                return (ITypeNode) child;
        }
        return null;
    }
}
