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

public interface IASVisitor
{
    void visitProject(IASProject element);

    void visitCompilationUnit(ICompilationUnit element);

    void visitPackage(IPackageDefinition element);

    void visitClass(IClassDefinition element);

    void visitInterface(IInterfaceDefinition element);

    void visitFunction(IFunctionDefinition element);

    void visitAccessor(IAccessorDefinition definition);

    void visitVariable(IVariableDefinition element);

    void visitNamespace(INamespaceDefinition element);

    void visitConstant(IConstantDefinition definition);

    void visitEvent(IEventDefinition event);

    void visitStyle(IStyleDefinition tag);

    void visitEffect(IEffectDefinition tag);

    void visitSkinState(IMetaTag tag);

    void visitSkinPart(IMetaTag tag);
}
