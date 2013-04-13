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

package randori.compiler.internal.access;

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
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.access.IASProjectAccess;
import randori.compiler.visitor.as.IASVisitor;

public class ProjectAccessVisitor implements IASVisitor
{

    private ProjectAccess access;

    public ProjectAccessVisitor(IASProjectAccess access)
    {
        this.access = (ProjectAccess) access;
    }

    @Override
    public boolean visitProject(IASProject element)
    {
        return true;
    }

    @Override
    public boolean visitCompilationUnit(ICompilationUnit element)
    {
        return true;
    }

    @Override
    public boolean visitFile(IFileNode node)
    {
        return true;
    }

    @Override
    public boolean visitPackage(IPackageDefinition element)
    {
        access.addPackage(element);
        return true;
    }

    @Override
    public boolean visitClass(IClassDefinition definition)
    {
        access.addClass(definition);
        return true;
    }

    @Override
    public boolean visitInterface(IInterfaceDefinition definition)
    {
        access.addInterface(definition);
        return true;
    }

    @Override
    public boolean visitFunction(IFunctionDefinition definition)
    {
        access.addFunction(definition);
        return true;
    }

    @Override
    public boolean visitAccessor(IAccessorDefinition definition)
    {
        if (definition instanceof IGetterDefinition)
        {
            access.addGetAccessor((IGetterDefinition) definition);
        }
        else if (definition instanceof ISetterDefinition)
        {
            access.addSetAccessor((ISetterDefinition) definition);
        }
        return true;
    }

    @Override
    public boolean visitVariable(IVariableDefinition definition)
    {
        access.addVariable(definition);
        return true;
    }

    @Override
    public boolean visitNamespace(INamespaceDefinition element)
    {
        return true;
    }

    @Override
    public boolean visitConstant(IConstantDefinition definition)
    {
        access.addConstant(definition);
        return true;
    }

    @Override
    public boolean visitMethod(IFunctionDefinition definition)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visitEvent(IEventDefinition definition)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visitTypeMetaTag(IMetaTag tag)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visitStyle(IStyleDefinition definition)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visitEffect(IEffectDefinition definition)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visitSkinState(IMetaTag tag)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visitSkinPart(IMetaTag tag)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
