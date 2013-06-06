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

package randori.compiler.internal.codegen.js.emitter;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.internal.tree.as.BinaryOperatorAssignmentNode;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.ASNodeUtils;
import randori.compiler.internal.utils.MetaDataUtils;

/**
 * Handles the production of the {@link IBinaryOperatorNode}.
 * 
 * @author Michael Schmalle
 */
public class BinaryOperatorEmitter extends BaseSubEmitter implements
        ISubEmitter<IBinaryOperatorNode>
{

    public BinaryOperatorEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    private boolean isCompoundAssignment(IBinaryOperatorNode node,
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

    @Override
    public void emit(IBinaryOperatorNode node)
    {
        ICompilerProject project = getEmitter().getWalker().getProject();

        IExpressionNode left = node.getLeftOperandNode();
        IDefinition leftDefinition = left.resolve(project);

        IExpressionNode right = node.getRightOperandNode();
        //IDefinition rightDefinition = right.resolve(project);

        // compund statements
        if (isCompoundAssignment(node, leftDefinition))
        {
            IAccessorDefinition accessor = (IAccessorDefinition) leftDefinition;
            String name = accessor.getBaseName();
            write("this.set_" + name + "(");
            write("this.get_" + name + "()");
            write(" ");
            write(node.getOperator().getOperatorText().replace("=", ""));
            write(" ");
            getWalker().walk(right);
            write("))");
            return;
        }

        if (ASNodeUtils.hasParenOpen(node))
            write("(");

        // if on the left side with '=' , we are in setter mode
        getModel().setInAssignment(isInAssignment(node));
        getModel().setAssign(node);

        getEmitter().getWalker().walk(left);

        if (!MetaDataUtils.isNative(leftDefinition)
                && getModel().isInAssignment()
                && leftDefinition instanceof IAccessorDefinition)
        {
            if (getModel().isCall())
                write("(this, ");
            else
                write("(");
            getModel().setCall(false);
        }
        else
        {
            // if not in assignment with setter, write the operator
            if (node.getNodeID() != ASTNodeID.Op_CommaID)
                write(" ");
            write(node.getOperator().getOperatorText());
            write(" ");
        }

        boolean wasAssignment = getModel().isInAssignment();
        getModel().setInAssignment(false);

        getEmitter().getWalker().walk(right);

        if (!MetaDataUtils.isNative(leftDefinition) && wasAssignment
                && leftDefinition instanceof IAccessorDefinition)
        {
            writeIfNotNative(")", leftDefinition);
        }

        if (ASNodeUtils.hasParenClose(node))
            write(")");
    }

    private boolean isInAssignment(IBinaryOperatorNode node)
    {
        if (node instanceof BinaryOperatorAssignmentNode)
            return true;
        return false;
    }

}
