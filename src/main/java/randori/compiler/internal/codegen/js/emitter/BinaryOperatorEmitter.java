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
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.ASNodeUtils;
import randori.compiler.internal.utils.ExpressionUtils;
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

    @Override
    public void emit(IBinaryOperatorNode node)
    {
        ICompilerProject project = getEmitter().getWalker().getProject();

        IExpressionNode left = node.getLeftOperandNode();
        IDefinition lhsDefinition = left.resolve(project);

        IExpressionNode right = node.getRightOperandNode();
        //IDefinition rhsDefinition = right.resolve(project);

        // Compound statements
        if (ExpressionUtils.isCompoundAssignment(node, lhsDefinition))
        {
            emitCompoundAssignment(node, lhsDefinition, right);
            return;
        }

        if (ASNodeUtils.hasParenOpen(node))
            write("(");

        // if on the left side with '=' , we are in setter mode
        getModel().setInAssignment(ExpressionUtils.isInAssignment(node));
        getModel().setAssign(node);

        getEmitter().getWalker().walk(left);

        if (!MetaDataUtils.isNative(lhsDefinition)
                && getModel().isInAssignment()
                && lhsDefinition instanceof IAccessorDefinition)
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

        if (!MetaDataUtils.isNative(lhsDefinition) && wasAssignment
                && lhsDefinition instanceof IAccessorDefinition)
        {
            writeIfNotNative(")", lhsDefinition);
        }

        if (ASNodeUtils.hasParenClose(node))
            write(")");
    }

    private void emitCompoundAssignment(IBinaryOperatorNode node,
            IDefinition left, IExpressionNode right)
    {
        IAccessorDefinition accessor = (IAccessorDefinition) left;
        String name = accessor.getBaseName();
        write("this.set_" + name + "(");
        write("this.get_" + name + "()");
        write(" ");
        write(node.getOperator().getOperatorText().replace("=", ""));
        write(" ");
        getWalker().walk(right);
        write("))");
    }

}
