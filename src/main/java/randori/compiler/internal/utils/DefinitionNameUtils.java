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

package randori.compiler.internal.utils;

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.ITypeNode;

/**
 * @author Michael Schmalle
 */
public class DefinitionNameUtils
{
    /**
     * Returns a qualified name using the <code>JavaScript</code> annotation if
     * it exists, other wise thelement's {@link ITypeNode}'s qualified name plus
     * the definition's base name.
     * 
     * @param definition The definition.
     * @param project The definition's {@link IASProject}.
     */
    public static String toExportQualifiedName(IDefinition definition,
            ICompilerProject project)
    {
        IClassNode typeNode = null;
        if (definition instanceof ITypeDefinition)
        {
            typeNode = (IClassNode) definition.getNode();
            return MetaDataUtils.getExportQualifiedName(typeNode
                    .getDefinition());
        }

        typeNode = (IClassNode) DefinitionUtils.findParentTypeNode(definition
                .getNode().getParent());
        String qualifiedName = MetaDataUtils.getExportQualifiedName(typeNode
                .getDefinition());
        String baseName = definition.getBaseName();
        if (definition instanceof IFunctionDefinition)
        {
            baseName = MetaDataUtils
                    .getFunctionBaseName((IFunctionDefinition) definition);
            if (MetaDataUtils.hasJavaScriptName(definition)
                    || MetaDataUtils.isGlobal(typeNode))
                return baseName;
        }
        return qualifiedName + "." + baseName;
    }
}
