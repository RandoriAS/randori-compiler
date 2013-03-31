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

import java.util.List;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;

import randori.compiler.codegen.as.IASEmitter;
import randori.compiler.internal.codegen.as.ASEmitter;

/**
 * @author Michael Schmalle
 */
public class RandoriUtils
{
    public static List<ICompilerProblem> getProblems(IASEmitter emitter)
    {
        return ((ASEmitter) emitter).getProblems();
    }

    public static String toFieldPrefix(IVariableDefinition definition,
            ICompilerProject project)
    {
        ITypeDefinition type = (ITypeDefinition) definition.getParent();
        String qualifiedName = type.getQualifiedName();
        StringBuilder sb = new StringBuilder();
        sb.append(qualifiedName);
        sb.append(".");
        sb.append(definition.getBaseName());
        return sb.toString();
    }

    public static String toMethodPrefix(IFunctionDefinition definition,
            ICompilerProject project)
    {
        ITypeDefinition type = (ITypeDefinition) definition.getParent();
        // foo.bar.baz.A
        String qualifiedName = type.getQualifiedName();
        StringBuilder sb = new StringBuilder();
        sb.append(qualifiedName);

        if (MetaDataUtils.isProtoExport(definition)
                || !definition.isConstructor())
        {
            if (!definition.isStatic())
            {
                sb.append(".");
                sb.append("prototype");
            }
            sb.append(".");
            String name = null;
            if (definition instanceof IAccessorDefinition)
            {
                name = MetaDataUtils.getAccessorName(
                        (IAccessorDefinition) definition, project);
                IAccessorDefinition accessor = (IAccessorDefinition) definition;
                String kind = accessor.getNode().getNodeID() == ASTNodeID.GetterID ? "get"
                        : "set";
                name = kind + "_" + name;
            }
            else
            {
                name = MetaDataUtils.getFunctionName(definition);
            }
            sb.append(name);
        }

        return sb.toString();
    }

}
