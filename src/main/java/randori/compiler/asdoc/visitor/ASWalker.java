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

package randori.compiler.asdoc.visitor;

import java.util.Collection;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IEffectDefinition;
import org.apache.flex.compiler.definitions.IEventDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.INamespaceDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.IStyleDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.scopes.IFileScope;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IInterfaceNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.units.requests.IFileScopeRequestResult;

public class ASWalker implements IASWalker
{

    protected IASVisitor visitor;

    private ICompilerProject project;

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public ASWalker(IASVisitor visitor)
    {
        this.visitor = visitor;
    }

    @Override
    public void walkProject(IASProject project)
    {
        this.project = project;

        visitor.visitProject(project);

        Collection<ICompilationUnit> units = project.getCompilationUnits();
        for (ICompilationUnit unit : units)
        {
            System.out.println(unit.getAbsoluteFilename());
            walkCompilationUnit(unit);
        }

        this.project = null;
    }

    @Override
    public void walkCompilationUnit(ICompilationUnit element)
    {
        visitor.visitCompilationUnit(element);

        IFileScopeRequestResult result = null;
        try
        {
            result = element.getFileScopeRequest().get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        if (result.getScopes().length == 0)
            return;

        IFileScope scope = (IFileScope) result.getScopes()[0];
        IFileNode node = (IFileNode) scope.getScopeNode();
        if (node == null || node.getChildCount() == 0)
        {
            return; // SWC
        }

        IPackageNode child = getPackageNode(node);
        if (child == null) // included files
            return;

        IPackageDefinition definition = (IPackageDefinition) child
                .getDefinition();
        walkPackage(definition);

        IDefinition[] definitions = node.getTopLevelDefinitions(false, false);
        if (definitions == null || definitions.length == 0)
            return;

        if (definitions[0] instanceof IClassDefinition)
        {
            walkClass((IClassDefinition) definitions[0]);
        }
        else if (definitions[0] instanceof IInterfaceDefinition)
        {
            walkInterface((IInterfaceDefinition) definitions[0]);
        }
        else if (definitions[0] instanceof INamespaceDefinition)
        {

        }
    }

    private IPackageNode getPackageNode(IFileNode node)
    {
        for (int i = 0; i < node.getChildCount(); i++)
        {
            if (node.getChild(i) instanceof IPackageNode)
                return (IPackageNode) node.getChild(i);
        }
        return null;
    }

    @Override
    public void walkPackage(IPackageDefinition element)
    {
        visitor.visitPackage(element);
    }

    @Override
    public void walkClass(IClassDefinition definition)
    {
        visitor.visitClass(definition);

        IClassNode node = (IClassNode) definition.getNode();
        if (node == null)
            return;

        IEventDefinition[] events = definition.getEventDefinitions(project
                .getWorkspace());
        for (IEventDefinition event : events)
        {
            visitor.visitEvent(event);
        }

        IEffectDefinition[] effects = definition.getEffectDefinitions(project
                .getWorkspace());
        for (IEffectDefinition effect : effects)
        {
            visitor.visitEffect(effect);
        }

        IStyleDefinition[] styles = definition.getStyleDefinitions(project
                .getWorkspace());
        for (IStyleDefinition style : styles)
        {
            visitor.visitStyle(style);
        }

        IMetaTag[] skinParts = definition.getSkinParts(null);
        for (IMetaTag tag : skinParts)
        {
            visitor.visitSkinPart(tag);
        }

        IMetaTag[] tags = definition.getAllMetaTags();
        for (IMetaTag tag : tags)
        {
            String tagName = tag.getTagName();
            if (tagName.equals("SkinState"))
            {
                visitor.visitSkinState(tag);
            }
        }

        IDefinitionNode[] members = node.getAllMemberNodes();
        for (IDefinitionNode child : members)
        {
            IDefinition cdefinition = child.getDefinition();
            if (cdefinition instanceof IConstantDefinition)
            {
                walkConstant((IConstantDefinition) cdefinition);
            }
            else if (cdefinition instanceof IAccessorDefinition)
            {
                walkAccessor((IAccessorDefinition) cdefinition);
            }
            else if (cdefinition instanceof IVariableDefinition)
            {
                walkVariable((IVariableDefinition) cdefinition);
            }
            else if (cdefinition instanceof IFunctionDefinition)
            {
                walkFunction((IFunctionDefinition) cdefinition);
            }
            else if (cdefinition instanceof INamespaceDefinition)
            {
                walkNamespace((INamespaceDefinition) cdefinition);
            }
        }
    }

    @Override
    public void walkAccessor(IAccessorDefinition definition)
    {
        visitor.visitAccessor(definition);
    }

    @Override
    public void walkInterface(IInterfaceDefinition element)
    {
        visitor.visitInterface(element);

        IInterfaceNode node = (IInterfaceNode) element.getNode();
        IDefinitionNode[] members = node.getAllMemberDefinitionNodes();
        for (IDefinitionNode child : members)
        {
            IDefinition definition = child.getDefinition();
            if (definition instanceof IAccessorDefinition)
            {
                walkAccessor((IAccessorDefinition) definition);
            }
            else if (definition instanceof IFunctionDefinition)
            {
                walkFunction((IFunctionDefinition) definition);
            }
        }
    }

    @Override
    public void walkNamespace(INamespaceDefinition definition)
    {
        visitor.visitNamespace(definition);
    }

    @Override
    public void walkFunction(IFunctionDefinition definition)
    {
        visitor.visitFunction(definition);
    }

    @Override
    public void walkVariable(IVariableDefinition definition)
    {
        visitor.visitVariable(definition);
    }

    @Override
    public void walkConstant(IConstantDefinition definition)
    {
        visitor.visitConstant(definition);
    }

}
