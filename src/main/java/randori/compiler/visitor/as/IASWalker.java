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

import java.io.IOException;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.INamespaceDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;

/**
 * @author Michael Schmalle
 */
public interface IASWalker
{
    /**
     * Walks an <code>IASProject</code> and recurses through all nodes in the
     * AST tree.
     * 
     * @param project The <code>IASProject</code>.
     * @throws IOException
     */
    void walkProject(IASProject project) throws IOException;

    /**
     * Walks an <code>IASCompilationUnit</code> of the <code>IASProject</code>.
     * 
     * @param unit The <code>IASCompilationUnit</code> of the
     * <code>IASProject</code>.
     * @throws IOException
     */
    void walkCompilationUnit(ICompilationUnit unit) throws IOException;

    void walkFile(IFileNode node);

    /**
     * Walks an <code>IASPackage</code> of the <code>IASCompilationUnit</code>.
     * 
     * @param element The <code>IASPackage</code> of the
     * <code>IASCompilationUnit</code>.
     */
    void walkPackage(IPackageDefinition definition);

    void walkClass(IClassDefinition definition);

    void walkInterface(IInterfaceDefinition definition);

    void walkTypeMetaTags(ITypeDefinition definition);

    void walkNamespace(INamespaceDefinition definition);

    /**
     * Visits a package level function.
     * 
     * @param definition
     */
    void walkMethod(IFunctionDefinition definition);

    //--------------------------------------------------------------------------
    // Memebers
    //--------------------------------------------------------------------------

    void walkConstant(IConstantDefinition definition);

    void walkAccessor(IAccessorDefinition definition);

    void walkField(IVariableDefinition definition);

}
