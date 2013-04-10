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

package randori.compiler.asdoc.internal.access;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IEffectDefinition;
import org.apache.flex.compiler.definitions.IEventDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IGetterDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.INamespaceDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.ISetterDefinition;
import org.apache.flex.compiler.definitions.IStyleDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.tree.as.IAccessorNode;
import org.apache.flex.compiler.tree.metadata.IEffectTagNode;
import org.apache.flex.compiler.tree.metadata.IEventTagNode;
import org.apache.flex.compiler.tree.metadata.IStyleTagNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.asdoc.access.IASProjectAccess;
import randori.compiler.asdoc.comment.IDocComment;
import randori.compiler.asdoc.visitor.IASVisitor;

public class ProjectAccessVisitor implements IASVisitor
{

    private ProjectAccess access;

    public ProjectAccessVisitor(IASProjectAccess access)
    {
        this.access = (ProjectAccess) access;
    }

    @Override
    public void visitProject(IASProject element)
    {
        //List<ISWC> libraries = element.getLibraries();

    }

    @Override
    public void visitCompilationUnit(ICompilationUnit element)
    {
    }

    @Override
    public void visitPackage(IPackageDefinition element)
    {
        access.addPackage(element);
    }

    @Override
    public void visitClass(IClassDefinition definition)
    {
        IDocComment asDocComment = (IDocComment) definition
                .getExplicitSourceComment();
        if (asDocComment == null)
            return;

        asDocComment.compile();

        access.addClass(definition);
    }

    @Override
    public void visitInterface(IInterfaceDefinition definition)
    {
        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        comment.compile();
        access.addInterface(definition);
    }

    @Override
    public void visitFunction(IFunctionDefinition definition)
    {
        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        comment.compile();

        access.addFunction(definition);
    }

    @Override
    public void visitAccessor(IAccessorDefinition definition)
    {
        if (!(definition.getNode() instanceof IAccessorNode))
        {
            // this is a VariableNode that is a synthetic 
            // SyntheticBindableGetterDefinition Bindable Accessor
        }

        // it's still safe to use variable node since the accessor
        // implements it
        //IVariableNode node = (IVariableNode) definition.getNode();

        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        comment.compile();

        if (definition instanceof IGetterDefinition)
        {
            access.addGetAccessor((IGetterDefinition) definition);
        }
        else if (definition instanceof ISetterDefinition)
        {
            access.addSetAccessor((ISetterDefinition) definition);
        }
    }

    @Override
    public void visitVariable(IVariableDefinition definition)
    {
        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        comment.compile();
        access.addVariable(definition);
    }

    @Override
    public void visitNamespace(INamespaceDefinition element)
    {
    }

    @Override
    public void visitConstant(IConstantDefinition definition)
    {
        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        comment.compile();
        access.addConstant(definition);
    }

    @Override
    public void visitEvent(IEventDefinition definition)
    {
        IEventTagNode node = definition.getNode();
        IDocComment comment = (IDocComment) node.getASDocComment();
        comment.compile();
        access.addEvent(definition);
    }

    @Override
    public void visitStyle(IStyleDefinition definition)
    {
        IStyleTagNode node = definition.getNode();
        IDocComment comment = (IDocComment) node.getASDocComment();
        comment.compile();
        access.addStyle(definition);
    }

    @Override
    public void visitEffect(IEffectDefinition definition)
    {
        IEffectTagNode node = definition.getNode();
        if (node != null)
        {
            IDocComment comment = (IDocComment) node.getASDocComment();
            comment.compile();
        }

        access.addEffect(definition);
    }

    @Override
    public void visitSkinState(IMetaTag tag)
    {
        access.addSkinState(tag);
    }

    @Override
    public void visitSkinPart(IMetaTag tag)
    {
        access.addSkinPart(tag);
    }

}
