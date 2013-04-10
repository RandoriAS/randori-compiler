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

package randori.compiler.visitor.as;

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

/**
 * @author Michael Schmalle
 */
public interface IASVisitor
{
    boolean visitProject(IASProject project);

    boolean visitCompilationUnit(ICompilationUnit unit);

    boolean visitFile(IFileNode node);

    boolean visitPackage(IPackageDefinition definition);

    boolean visitClass(IClassDefinition definition);

    boolean visitInterface(IInterfaceDefinition definition);

    boolean visitFunction(IFunctionDefinition definition);

    boolean visitAccessor(IAccessorDefinition definition);

    boolean visitVariable(IVariableDefinition definition);

    boolean visitNamespace(INamespaceDefinition definition);

    boolean visitConstant(IConstantDefinition definition);

    boolean visitEvent(IEventDefinition definition);

    boolean visitTypeMetaTag(IMetaTag tag);

    boolean visitStyle(IStyleDefinition definition);

    boolean visitEffect(IEffectDefinition definition);

    boolean visitSkinState(IMetaTag tag);

    boolean visitSkinPart(IMetaTag tag);

}
