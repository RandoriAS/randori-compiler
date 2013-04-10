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

package randori.compiler.asdoc.internal.visitor;

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
import org.apache.flex.compiler.units.ICompilationUnit;
import randori.compiler.asdoc.visitor.IASVisitor;

public class NullASVisitor implements IASVisitor
{

    public NullASVisitor()
    {
    }

    @Override
    public void visitProject(IASProject element)
    {
    }

    @Override
    public void visitCompilationUnit(ICompilationUnit element)
    {
    }

    @Override
    public void visitPackage(IPackageDefinition element)
    {
    }

    @Override
    public void visitClass(IClassDefinition definition)
    {
    }

    @Override
    public void visitInterface(IInterfaceDefinition definition)
    {
    }

    @Override
    public void visitFunction(IFunctionDefinition definition)
    {
    }

    @Override
    public void visitAccessor(IAccessorDefinition definition)
    {
    }

    @Override
    public void visitVariable(IVariableDefinition definition)
    {
    }

    @Override
    public void visitNamespace(INamespaceDefinition element)
    {
    }

    @Override
    public void visitConstant(IConstantDefinition definition)
    {

    }

    @Override
    public void visitEvent(IEventDefinition tag)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitStyle(IStyleDefinition tag)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitEffect(IEffectDefinition tag)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitSkinState(IMetaTag tag)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitSkinPart(IMetaTag tag)
    {
        // TODO Auto-generated method stub

    }

}
