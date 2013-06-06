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
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.internal.definitions.AppliedVectorDefinition;
import org.apache.flex.compiler.internal.definitions.ClassTraitsDefinition;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;

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

    /**
     * Returns a qualified name based on the annotation <code>JavaScript</code>
     * <code>name</code> attribute.
     * 
     * @param definition The definition.
     * @param project The definition's {@link IASProject}.
     */
    public static String toQualifiedName(IDefinition definition,
            ICompilerProject project)
    {
        return null;
    }

    /**
     * Returns the superclass access for a <code>super</code> call.
     * <p>
     * IE <code>super.(other expression)</code> will return
     * <code>foo.bar.BaseClass</code>.
     * 
     * @param node
     * @param project
     */
    public static String toSuperAccessQualifiedName(IASNode node,
            ICompilerProject project)
    {
        IClassNode typeNode = (IClassNode) DefinitionUtils
                .findParentTypeNode(node.getParent());
        String qualifiedName = DefinitionUtils.toBaseClassQualifiedName(
                typeNode.getDefinition(), project);
        return qualifiedName;
    }

    /**
     * Returns a package name based on the annotation <code>JavaScript</code>
     * <code>name</code> attribute.
     * 
     * @param definition The definition.
     * @param project The definition's {@link IASProject}.
     */
    public static String toPackageName(IDefinition definition,
            ICompilerProject project)
    {
        return null;
    }

    public static String toFieldPrefix(IVariableDefinition definition,
            ICompilerProject project)
    {
        final ITypeDefinition type = (ITypeDefinition) definition.getParent();
        final String qualifiedName = MetaDataUtils.getExportQualifiedName(type);
        StringBuilder sb = new StringBuilder();
        sb.append(qualifiedName);
        sb.append(".");
        sb.append(definition.getBaseName());
        return sb.toString();
    }

    public static String toMethodPrefix(IFunctionDefinition definition,
            ICompilerProject project)
    {
        final ITypeDefinition type = (ITypeDefinition) definition.getParent();
        final String qualifiedName = MetaDataUtils.getExportQualifiedName(type);
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

    public static boolean isJQueryStaticJ(IExpressionNode left,
            IExpressionNode right)
    {
        if (left instanceof IIdentifierNode && right instanceof IIdentifierNode)
        {
            IIdentifierNode ileft = (IIdentifierNode) left;
            IIdentifierNode iright = (IIdentifierNode) right;
            if (ileft.getName().equals("JQueryStatic")
                    && iright.getName().equals("J"))
                return true;
        }
        return false;
    }

    public static boolean isConstantMemberAccess(IExpressionNode left,
            IExpressionNode right, ICompilerProject project)
    {
        if (left instanceof IIdentifierNode && right instanceof IIdentifierNode)
        {
            IIdentifierNode ileft = (IIdentifierNode) left;
            IIdentifierNode iright = (IIdentifierNode) right;
            IDefinition dleft = ileft.resolveType(project);
            if (dleft instanceof ClassTraitsDefinition)
            {
                IDefinition dright = iright.resolve(project);
                if (dright instanceof IConstantDefinition)
                    return true;
            }
        }
        return false;
    }

    public static boolean isTransparentAccessorAccess(IExpressionNode left,
            IExpressionNode right)
    {
        // for now, the transparent access can be;
        // - a static accessor 'Window.console.log()'

        // is the left member access and right and identifier, '{[Window].[console]}.[log]()'
        if (left instanceof IMemberAccessExpressionNode)
        {
            IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) left;

            if (mnode.getLeftOperandNode() instanceof IIdentifierNode)
            {
                IIdentifierNode ileft = (IIdentifierNode) mnode
                        .getLeftOperandNode();
                if (ileft.getName().equals("Window"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTransparentMemberAccess(IExpressionNode left,
            IExpressionNode right)
    {
        // for now, the transparent access can be;
        // - a static method 'Window.alert()'

        // is the left identifier and right and identifier, '[Window.].[log]()'
        if (left instanceof IIdentifierNode && right instanceof IIdentifierNode)
        {
            IIdentifierNode ileft = (IIdentifierNode) left;
            if (ileft.getName().equals("Window"))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isGlobalStatic(IExpressionNode left,
            IExpressionNode right, ICompilerProject project)
    {
        if (left instanceof IIdentifierNode && right instanceof IIdentifierNode)
        {
            IIdentifierNode ileft = (IIdentifierNode) left;
            IDefinition dleft = ileft.resolveType(project);
            if (dleft instanceof ClassTraitsDefinition)
            {
                if (MetaDataUtils.isGlobal(dleft))
                    return true;
            }
        }
        else if (left instanceof IMemberAccessExpressionNode)
        {
            // possible qualified name to static global class
            // [foo.bar.Baz].myFunction()
            IMemberAccessExpressionNode ileft = (IMemberAccessExpressionNode) left;
            IDefinition dleft = ileft.resolveType(project);
            if (dleft instanceof ClassTraitsDefinition)
            {
                if (MetaDataUtils.isGlobal(dleft))
                    return true;
            }
        }
        return false;
    }

    public static boolean isValidInjectType(ITypeDefinition definition)
    {
        if (definition == null)
            return false;

        if (definition instanceof AppliedVectorDefinition)
            return false;

        IMetaTag tag = null;

        tag = definition.getMetaTagByName("native");
        if (tag != null)
            return false;

        return true;
    }
}
