/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package randori.compiler.internal.utils;

import org.apache.flex.compiler.common.DependencyType;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IGetterDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.references.IReference;
import org.apache.flex.compiler.internal.definitions.ClassDefinition;
import org.apache.flex.compiler.internal.definitions.ClassTraitsDefinition;
import org.apache.flex.compiler.internal.definitions.FunctionDefinition;
import org.apache.flex.compiler.internal.definitions.VariableDefinition;
import org.apache.flex.compiler.internal.scopes.ASScope;
import org.apache.flex.compiler.internal.tree.as.ExpressionNodeBase;
import org.apache.flex.compiler.internal.tree.as.FunctionCallNode;
import org.apache.flex.compiler.internal.tree.as.MemberAccessExpressionNode;
import org.apache.flex.compiler.internal.tree.as.NodeBase;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.scopes.IASScope;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IDynamicAccessNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IForLoopNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode.LanguageIdentifierKind;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.IUnaryOperatorNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

/**
 * @author Michael Schmalle
 */
public class ExpressionUtils
{
    public static IExpressionNode getNode(IASNode iNode, Boolean toRight,
            ICompilerProject project)
    {
        try
        {
            IASNode node = iNode;
            while (node != null)
            {
                if (node instanceof IBinaryOperatorNode
                        && !(node instanceof MemberAccessExpressionNode))
                {
                    if (toRight)
                        node = ((IBinaryOperatorNode) node)
                                .getRightOperandNode();
                    else
                        node = ((IBinaryOperatorNode) node)
                                .getLeftOperandNode();
                }
                else if (node instanceof IFunctionCallNode)
                    node = ((IFunctionCallNode) node).getNameNode();
                else if (node instanceof IDynamicAccessNode)
                    node = ((IDynamicAccessNode) node).getLeftOperandNode();
                else if (node instanceof IUnaryOperatorNode)
                    node = ((IUnaryOperatorNode) node).getOperandNode();
                else if (node instanceof IForLoopNode)
                    node = ((IForLoopNode) node).getChild(0).getChild(0);
                else if (node instanceof IVariableNode)
                {
                    if (toRight)
                        node = ((IVariableNode) node).getAssignedValueNode();
                    else
                        node = ((IVariableNode) node).getVariableTypeNode();
                }
                else if (node instanceof IExpressionNode)
                {
                    //                    IDefinition def = ((IExpressionNode) node).resolve(project);
                    //                    if (def instanceof VariableDefinition)
                    //                    {
                    //                        final VariableDefinition variable = (VariableDefinition) def;
                    //                        def = variable.resolveType(project);
                    //                    }
                    //                    else if (def instanceof FunctionDefinition)
                    //                    {
                    //                        final FunctionDefinition functionDef = (FunctionDefinition) def;
                    //                        final IReference typeRef = functionDef
                    //                                .getReturnTypeReference();
                    //                        if (typeRef != null)
                    //                            def = typeRef.resolve(project,
                    //                                    (ASScope) getScopeFromNode(iNode),
                    //                                    DependencyType.INHERITANCE, false);
                    //                    }
                    //                    else if (def instanceof IGetterDefinition)
                    //                    {
                    //                        final ITypeDefinition returnType = ((IGetterDefinition) def)
                    //                                .resolveReturnType(project);
                    //                        //                        def = m_sharedData.getDefinition(returnType
                    //                        //                                .getQualifiedName());
                    //                        def = returnType; // XXX figure out
                    //                    }
                    //
                    //                    if (def != null && def instanceof ClassDefinition)
                    //                    {
                    //                        return def;
                    //                    }
                    return (IExpressionNode) node;
                }
                else
                {
                    node = null;
                }
            }
        }
        catch (Exception e)
        {
            // getDefinitionForNode(iNode,toRight);

            // getDefinition() sometimes crashes, e.g. when looking at a cast to an interface in some cases,
            // FunctionDefinition.getParameters() returns null and ExpressionNodeBase.determineIfFunction() chokes on it
            //           printWarning(iNode, "getDefinitionForNode() failed for" + iNode);
        }
        return null;
    }

    /**
     * Returns a {@link IClassDefinition} for the node passed
     * 
     * @param iNode
     * @param toRight
     * @param project
     * @return
     */
    public static IDefinition getDefinitionForNode(IASNode iNode,
            Boolean toRight, ICompilerProject project)
    {
        // This is based on an idea Peter came up with...
        // 
        try
        {
            IASNode node = iNode;
            while (node != null)
            {
                if (node instanceof IBinaryOperatorNode
                        && !(node instanceof MemberAccessExpressionNode))
                {
                    if (toRight)
                        node = ((IBinaryOperatorNode) node)
                                .getRightOperandNode();
                    else
                        node = ((IBinaryOperatorNode) node)
                                .getLeftOperandNode();
                }
                else if (node instanceof IFunctionCallNode)
                    node = ((IFunctionCallNode) node).getNameNode();
                else if (node instanceof IDynamicAccessNode)
                    node = ((IDynamicAccessNode) node).getLeftOperandNode();
                else if (node instanceof IUnaryOperatorNode)
                    node = ((IUnaryOperatorNode) node).getOperandNode();
                else if (node instanceof IForLoopNode)
                    node = ((IForLoopNode) node).getChild(0).getChild(0);
                else if (node instanceof IVariableNode)
                {
                    if (toRight)
                        node = ((IVariableNode) node).getAssignedValueNode();
                    else
                        node = ((IVariableNode) node).getVariableTypeNode();
                }
                else if (node instanceof IExpressionNode)
                {
                    IDefinition def = ((IExpressionNode) node).resolve(project);
                    if (def instanceof VariableDefinition)
                    {
                        final VariableDefinition variable = (VariableDefinition) def;
                        def = variable.resolveType(project);
                    }
                    else if (def instanceof FunctionDefinition)
                    {
                        final FunctionDefinition functionDef = (FunctionDefinition) def;
                        final IReference typeRef = functionDef
                                .getReturnTypeReference();
                        if (typeRef != null)
                            def = typeRef.resolve(project,
                                    (ASScope) getScopeFromNode(iNode),
                                    DependencyType.INHERITANCE, false);
                    }
                    else if (def instanceof IGetterDefinition)
                    {
                        final ITypeDefinition returnType = ((IGetterDefinition) def)
                                .resolveReturnType(project);
                        //                        def = m_sharedData.getDefinition(returnType
                        //                                .getQualifiedName());
                        def = returnType; // XXX figure out
                    }

                    if (def != null && def instanceof ClassDefinition)
                    {
                        return def;
                    }
                    node = null;
                }
                else
                {
                    node = null;
                }
            }
        }
        catch (Exception e)
        {
            // getDefinitionForNode(iNode,toRight);

            // getDefinition() sometimes crashes, e.g. when looking at a cast to an interface in some cases,
            // FunctionDefinition.getParameters() returns null and ExpressionNodeBase.determineIfFunction() chokes on it
            //           printWarning(iNode, "getDefinitionForNode() failed for" + iNode);
        }
        return null;
    }

    public static IASScope getScopeFromNode(IASNode iNode)
    {
        NodeBase id = (NodeBase) iNode;

        // protected ASScope scope = id.getASScope();
        // This is the implementation from NodeBase::getASScope:

        IScopedNode scopeNode = id.getScopeNode(); // id.getContainingScope();
        IASScope scope = scopeNode != null ? scopeNode.getScope() : null;
        // If the ScopedNode had a null scope, keep looking up the tree until we
        // find one with a non-null scope.
        // TODO: Is it a bug that an IScopedNode returns null for it's scope?
        // TODO: this seems like a leftover from block scoping - for example, a
        // TODO: ForLoopNode is an IScopedNode, but it doesn't really have a scope
        while (scope == null && scopeNode != null)
        {
            scopeNode = scopeNode.getContainingScope();
            scope = scopeNode != null ? scopeNode.getScope() : null;
        }

        return scope;
    }

    public static IExpressionNode getLeftBase(IExpressionNode node)
    {
        ExpressionNodeBase enode = (ExpressionNodeBase) node;
        ExpressionNodeBase baseExpression = enode.getBaseExpression();
        if (baseExpression != null)
        {
            if (baseExpression instanceof IMemberAccessExpressionNode)
            {
                IMemberAccessExpressionNode baseMember = (IMemberAccessExpressionNode) baseExpression;
                return baseMember.getLeftOperandNode();
            }
            return baseExpression;
        }

        if (node instanceof IBinaryOperatorNode)
        {
            IBinaryOperatorNode binary = (IBinaryOperatorNode) node;
            if (binary.getLeftOperandNode() instanceof IMemberAccessExpressionNode)
            {
                return getLeftBase((IBinaryOperatorNode) binary
                        .getLeftOperandNode());
            }
            else if (binary.getLeftOperandNode() instanceof IDynamicAccessNode)
            {
                return getLeftBase((IBinaryOperatorNode) binary
                        .getLeftOperandNode());
            }
            return binary.getLeftOperandNode();
        }

        return node;
    }

    public static boolean isValidThis(IIdentifierNode node,
            ICompilerProject project)
    {
        // added super.foo(), wanted to 'this' behind foo
        if (node.getParent() instanceof IMemberAccessExpressionNode)
        {
            IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) node
                    .getParent();
            if (mnode.getLeftOperandNode().getNodeID() == ASTNodeID.SuperID)
                return false;

            IExpressionNode indentFromThis = getIndentFromThis(node);
            if (node == indentFromThis)
                return true;

            // test that this is the base expression
            ExpressionNodeBase enode = (ExpressionNodeBase) node;
            ExpressionNodeBase baseExpression = enode.getBaseExpression();
            if (indentFromThis == null && baseExpression != null
                    && baseExpression != node)
                return false;

            // check to see if the left is a type
            ITypeDefinition type = mnode.getLeftOperandNode().resolveType(
                    project);

            // A.{foo} : Left is a Type
            // XXX going to have to test packgeName to com.acme.A
            if (type instanceof ClassTraitsDefinition
                    && mnode.getLeftOperandNode() == node)
            {
                return false;
            }
            // this.{foo} : explicit 'this', in js we are ignoring explicit this identifiers
            // because we are inserting all of them with the emitter
            else if (indentFromThis == null)
            {
                //return false;
            }

        }

        IDefinition definition = node.resolve(project);
        if (definition == null)
            return false; // Is this correct?
        if (definition instanceof IParameterDefinition)
            return false;
        if (definition.getParent() instanceof IMemberAccessExpressionNode)
            return false;
        if (!(definition.getParent() instanceof IClassDefinition))
            return false;

        if (definition instanceof IVariableDefinition)
        {
            IVariableDefinition variable = (IVariableDefinition) definition;
            if (variable.isStatic())
                return false;
        }
        if (definition instanceof IFunctionDefinition)
        {
            IFunctionDefinition function = (IFunctionDefinition) definition;
            if (function.isStatic())
                return false;
        }

        return true;
    }

    public static boolean isLeftSide(IASNode node)
    {
        // if parent is a Binary and this node is the leftside operand
        IASNode parent = node.getParent();
        if (parent instanceof IDynamicAccessNode)
            return false;
        if (parent.getParent() instanceof IMemberAccessExpressionNode)
        {
            // test get_data().foo
            IMemberAccessExpressionNode bnode = (IMemberAccessExpressionNode) parent;
            if (bnode.getLeftOperandNode() == node)
                return false;

        }

        ExpressionNodeBase base = (ExpressionNodeBase) node;
        ExpressionNodeBase parent1 = (ExpressionNodeBase) base
                .getBaseExpression();

        if (parent.getParent() instanceof IFunctionCallNode)
            return false;

        if (parent1 != null)
        {
            parent = parent1.getParent();
            if (parent instanceof IMemberAccessExpressionNode)
                parent = parent.getParent();
            if (parent instanceof IBinaryOperatorNode)
            {
                IBinaryOperatorNode bnode = (IBinaryOperatorNode) parent;
                if (bnode.getLeftOperandNode() == node.getParent())
                    return true;
            }

        }

        if (parent instanceof IBinaryOperatorNode)
        {
            IBinaryOperatorNode bnode = (IBinaryOperatorNode) parent;
            if (bnode.getLeftOperandNode() == node)
                return true;
        }

        return false;
    }

    public static boolean injectThisArgument(FunctionCallNode node,
            boolean allowMembers)
    {
        // if super isSuper checks the nameNode
        if (node.isSuperExpression() && !node.isNewExpression())
            return true;

        ExpressionNodeBase base = node.getNameNode();
        if (base.getNodeID() == ASTNodeID.IdentifierID)
            return false;

        if (allowMembers && base instanceof IMemberAccessExpressionNode)
        {
            //  foo.super()
            IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) base;
            if (mnode.getLeftOperandNode().getNodeID() == ASTNodeID.SuperID)
                return true;
        }

        return false;
    }

    public static boolean isBinding(IIdentifierNode node,
            ICompilerProject project)
    {
        IDefinition resolve = node.resolve(project);

        if (resolve != null && resolve.isPrivate() && !isField(resolve))
        {
            //if (resolve instanceof IFunctionDefinition)
            IExpressionNode rightSide = ExpressionUtils.getNode(node, true,
                    project);
            IBinaryOperatorNode parent = (IBinaryOperatorNode) node
                    .getAncestorOfType(IBinaryOperatorNode.class);
            if (isThisLeftOf(node))
                parent = (IBinaryOperatorNode) parent
                        .getAncestorOfType(IBinaryOperatorNode.class);

            IVariableNode vparent = (IVariableNode) node
                    .getAncestorOfType(IVariableNode.class);
            if (vparent != null)
            {
                IExpressionNode indentFromThis = getIndentFromThis(node);
                if (vparent.getAssignedValueNode() == node
                        || ((IBinaryOperatorNode) vparent
                                .getAssignedValueNode()).getRightOperandNode() == indentFromThis)
                    return true;
            }

            if (rightSide == node && parent != null/*|| isThisLeftOf(node)*/)
            {
                return true;
            }
        }

        return false;
    }

    private static boolean isField(IDefinition node)
    {
        return !(node instanceof IFunctionDefinition);
    }

    private static boolean isThisLeftOf(IIdentifierNode node)
    {
        if (node.getParent() instanceof IMemberAccessExpressionNode)
        {
            IMemberAccessExpressionNode parent = (IMemberAccessExpressionNode) node
                    .getParent();
            if (parent.getLeftOperandNode() instanceof ILanguageIdentifierNode
                    && ((ILanguageIdentifierNode) parent.getLeftOperandNode())
                            .getKind() == LanguageIdentifierKind.THIS)
                return true;
        }
        return false;
    }

    private static IExpressionNode getIndentFromThis(IIdentifierNode node)
    {
        if (node.getParent() instanceof IMemberAccessExpressionNode)
        {
            IMemberAccessExpressionNode parent = (IMemberAccessExpressionNode) node
                    .getParent();
            if (parent.getLeftOperandNode() instanceof ILanguageIdentifierNode
                    && ((ILanguageIdentifierNode) parent.getLeftOperandNode())
                            .getKind() == LanguageIdentifierKind.THIS)
                return parent.getRightOperandNode();
        }
        return null;
    }

    public static String toInitialValue(IVariableDefinition field,
            ICompilerProject project)
    {
        Object value = field.resolveInitialValue(project);
        if (value != null)
            return value.toString();
        IReference reference = field.getTypeReference();
        if (reference == null)
            return "undefined";
        if (reference.getName().equals("int")
                || reference.getName().equals("uint")
                || reference.getName().equals("Number"))
            return "0";
        return "null";
    }

}
