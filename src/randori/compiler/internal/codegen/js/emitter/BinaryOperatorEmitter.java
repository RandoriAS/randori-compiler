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
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;
import org.apache.flex.compiler.utils.ASNodeUtils;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
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
        IDefinition leftDefinition = left.resolve(project);

        IExpressionNode right = node.getRightOperandNode();
        IDefinition rightDefinition = right.resolve(project);

        // XXX Added see if this causes problems
        if (ASNodeUtils.hasParenOpen(node))
            write("(");

        // if on the left side with '=' , we are in setter mode
        getModel().setInAssignment(isInAssignment(node));

        getEmitter().getWalker().walk(left);

        if (!MetaDataUtils.isNative(leftDefinition)
                && getModel().isInAssignment()
                && leftDefinition instanceof IAccessorDefinition)
        {
            write("(");
        }
        else
        {
            if (leftDefinition instanceof IAccessorDefinition)
            {
                // if (data == null) || ... if (this.get_data() == null) || 
                writeIfNotNative("()", leftDefinition);
            }
            // if not in assignment with setter, write the operator
            if (node.getNodeID() != ASTNodeID.Op_CommaID)
                write(" ");
            write(node.getOperator().getOperatorText());
            write(" ");
        }

        boolean wasAssignment = getModel().isInAssignment();
        getModel().setInAssignment(false);

        getEmitter().getWalker().walk(right);

        // cases like 'this.accessor' will resolve to an accessor definition,
        // we need to pass here and let emitMemeberAccess() take care of this
        if (!(right instanceof IMemberAccessExpressionNode)
                && rightDefinition instanceof IAccessorDefinition)
        {
            writeIfNotNative("()", rightDefinition);
        }

        if (!MetaDataUtils.isNative(leftDefinition) && wasAssignment
                && leftDefinition instanceof IAccessorDefinition)
        {
            writeIfNotNative(")", leftDefinition);
        }

        // XXX Added see if this causes problems
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
