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

import org.apache.flex.abc.ABCConstants;
import org.apache.flex.abc.semantics.Namespace;
import org.apache.flex.compiler.common.DependencyType;
import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
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
import org.apache.flex.compiler.internal.tree.as.BinaryOperatorAssignmentNode;
import org.apache.flex.compiler.internal.tree.as.BinaryOperatorNodeBase;
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
import org.apache.flex.compiler.tree.as.INonResolvingIdentifierNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.IUnaryOperatorNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

/**
 * @author Michael Schmalle
 */
public class ExpressionUtils
{
    public static boolean isRight(IBinaryOperatorNode assignNode,
            IIdentifierNode node)

    {
        if (node.getParent() == assignNode)
            return true;
        // if the left node IS the right most node, we are a setter
        // if the left node is member access and is not the right most
        // in the lhs, we cannot be a setter
        if (assignNode instanceof BinaryOperatorAssignmentNode)
        {
            IExpressionNode right = getRightOfMemberAccess(assignNode
                    .getLeftOperandNode());
            return node == right;
        }
        return false;
    }

    private static IExpressionNode getRightOfMemberAccess(IExpressionNode node)
    {
        if (node instanceof IMemberAccessExpressionNode)
        {
            IASNode right = ((BinaryOperatorNodeBase) node)
                    .getRightOperandNode();
            if (right instanceof IBinaryOperatorNode)
                return getRightOfMemberAccess((IBinaryOperatorNode) right);
            return (IExpressionNode) right;
        }
        return node;
    }

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
                return getLeftBase(binary.getLeftOperandNode());
            }
            else if (binary.getLeftOperandNode() instanceof IDynamicAccessNode)
            {
                return getLeftBase(binary.getLeftOperandNode());
            }
            return binary.getLeftOperandNode();
        }

        return node;
    }

    public static boolean isValidThis(IIdentifierNode node,
            ICompilerProject project)
    {
        if (node instanceof INonResolvingIdentifierNode)
            return false;

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
        if (definition instanceof IConstantDefinition)
            return false;
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
        ExpressionNodeBase parent1 = base.getBaseExpression();

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
            //  super.foo()
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

    /*
     * The constant value of the initial value, if one can be determined, 
     * or null if it can't be determined. This will be a String, int, double, 
     * boolean, or Namespace depending on what the initial value was. The 
     * value could also be org.apache.flex.abc.ABCConstants.UNDEFINED_VALUE 
     * if the initial value was the undefined constant value Callers will 
     * need to use instanceof to see what type the value is.
     */

    public static String toInitialValue(IVariableDefinition field,
            ICompilerProject project)
    {
        Object value = field.resolveInitialValue(project);
        if (value != null)
        {
            if (value instanceof String)
                return "\"" + value.toString() + "\"";
            else if (value instanceof Integer)
                return Integer.toString((Integer) value);
            else if (value instanceof Double)
                return Double.toString((Double) value);
            else if (value instanceof Boolean)
                return Boolean.toString((Boolean) value);
            else if (value instanceof Namespace)
                return value.toString();
            else if (value == ABCConstants.UNDEFINED_VALUE)
                return "undefined";
            else if (value == ABCConstants.NULL_VALUE)
                return "null";
        }
        IReference reference = field.getTypeReference();
        if (reference == null)
            return "undefined";
        if (reference.getName().equals("int")
                || reference.getName().equals("uint")
                || reference.getName().equals("Number"))
            return "0";
        return "null";
    }

    /**
     * Returns whether the node constitues an assignment.
     * 
     * @param node
     */
    public static boolean isInAssignment(IBinaryOperatorNode node)
    {
        if (node instanceof BinaryOperatorAssignmentNode)
            return true;
        return false;
    }

    /**
     * Returns whether the node is a compound expression.
     * 
     * @param node
     * @param lhsDefinition
     */
    public static boolean isCompoundAssignment(IBinaryOperatorNode node,
            IDefinition lhsDefinition)
    {
        if (!(lhsDefinition instanceof IAccessorDefinition))
            return false;

        switch (node.getNodeID())
        {
        case Op_LeftShiftAssignID:
        case Op_RightShiftAssignID:
        case Op_UnsignedRightShiftAssignID:
        case Op_MultiplyAssignID:
        case Op_DivideAssignID:
        case Op_ModuloAssignID:
        case Op_BitwiseAndAssignID:
        case Op_BitwiseXorAssignID:

        case Op_BitwiseOrAssignID:
        case Op_AddAssignID:
        case Op_SubtractAssignID:
        case Op_LogicalAndAssignID:
            return true;
        }
        return false;
    }

    /**
     * Returns whether the {@link IExpressionNode} is a <code>super</code>
     * expression.
     * <p>
     * Will return <code>false</code> if argument is <code>null</code>.
     * 
     * @param node The node to check for super.
     */
    public static boolean isSuperExpression(IExpressionNode node)
    {
        if (node == null)
            return false;
        return node instanceof ILanguageIdentifierNode
                && ((ILanguageIdentifierNode) node).getKind() == LanguageIdentifierKind.SUPER;
    }
}
