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

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
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
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.visitor.as.IASVisitor;

/**
 * @author Michael Schmalle
 */
public class NullASVisitor implements IASVisitor
{

    public NullASVisitor()
    {
    }

    @Override
    public boolean visitProject(IASProject project)
    {
        return true;
    }

    @Override
    public boolean visitCompilationUnit(ICompilationUnit unit)
    {
        return true;
    }

    @Override
    public boolean visitFile(IFileNode node)
    {
        return true;
    }

    @Override
    public boolean visitPackage(IPackageDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitClass(IClassDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitInterface(IInterfaceDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitFunction(IFunctionDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitAccessor(IAccessorDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitVariable(IVariableDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitNamespace(INamespaceDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitConstant(IConstantDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitTypeMetaTag(IMetaTag tag)
    {
        return true;
    }

    @Override
    public boolean visitEvent(IEventDefinition definition)
    {
        return true;
    }

    @Override
    public boolean visitStyle(IStyleDefinition tag)
    {
        return true;
    }

    @Override
    public boolean visitEffect(IEffectDefinition tag)
    {
        return true;
    }

    @Override
    public boolean visitSkinState(IMetaTag tag)
    {
        return true;
    }

    @Override
    public boolean visitSkinPart(IMetaTag tag)
    {
        return true;
    }

}
