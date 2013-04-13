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

package randori.compiler.access;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.projects.ICompilerProject;

/**
 * @author Michael Schmalle
 */
public interface IASProjectAccess
{
    ICompilerProject getProject();

    void process();

    Set<String> getPackages();

    Collection<IPackageBundle> getPackageBundles();

    IPackageBundle getPackageBundle(IDefinition definition);

    Collection<ITypeDefinition> getTypes();

    Collection<ITypeDefinition> getTypes(IPackageBundle bundle);

    Collection<IClassDefinition> getClasses();

    Collection<IClassDefinition> getSubClasses(IClassDefinition definition);

    Collection<IInterfaceDefinition> getInterfaces();

    Collection<IClassDefinition> getInterfaceImplementors(
            IInterfaceDefinition definition);

    Collection<IInterfaceDefinition> getSubInterfaces(
            IInterfaceDefinition definition);

    List<IVariableDefinition> getVariables(IClassDefinition definition,
            String visibility, boolean inherit);

    List<IConstantDefinition> getConstants(IClassDefinition definition,
            String visibility, boolean inherit);

    List<IAccessorDefinition> getAccessors(ITypeDefinition definition,
            String visibility, boolean inherit);

    List<IFunctionDefinition> getFunctions(ITypeDefinition definition,
            String visibility, boolean inherit);

    ITypeDefinition getType(String qualifiedName);

    IDocumentableDefinition getDefinition(String uri);
}
