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
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * Handles the production of the {@link IMemberAccessExpressionNode}.
 * 
 * @author Michael Schmalle
 */
public class MemberAccessExpressionEmitter extends BaseSubEmitter implements
        ISubEmitter<IMemberAccessExpressionNode>
{

    public MemberAccessExpressionEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IMemberAccessExpressionNode node)
    {
        ICompilerProject project = getEmitter().getWalker().getProject();

        IExpressionNode left = node.getLeftOperandNode();
        IDefinition leftDef = left.resolve(project);

        IExpressionNode right = node.getRightOperandNode();
        IDefinition rightDef = right.resolve(project);

        boolean isTransparent = RandoriUtils.isTransparentMemberAccess(left,
                right);
        boolean isGlobalStatic = RandoriUtils.isGlobalStatic(left, right,
                project);
        // the left is 'Window', the right is the static method
        getModel().setSkipOperator(isTransparent || isGlobalStatic);

        // JQueryStatic
        if (RandoriUtils.isJQueryStaticJ(left, right))
        {
            write("jQuery");
            return;
        }
        
        if (RandoriUtils.isTransparentAccessorAccess(left, right))
        {
            // '[Window.console].[log]()'
            // just write the lefts's right operand
            IMemberAccessExpressionNode parent = (IMemberAccessExpressionNode) ((IMemberAccessExpressionNode) node
                    .getLeftOperandNode());
            IIdentifierNode r = (IIdentifierNode) parent.getRightOperandNode();
            write(r.getName());
            write(".");
            write(((IIdentifierNode) right).getName());
            return;
        }

        if (RandoriUtils.isConstantMemberAccess(left, right, project))
        {
            // if Foo.BAR, we skip below and just write the scalar value
            IConstantDefinition cdef = (IConstantDefinition) right
                    .resolve(project);
            Object value = cdef.resolveInitialValue(project);
            if (value != null)
                write(value.toString());
            return;
        }

        if (!getModel().skipOperator())
            getWalker().walk(left);

        if (!getModel().skipOperator())
        {
            if (!getModel().isInAssignment()
                    && leftDef instanceof IAccessorDefinition
                    && left.getParent() instanceof IMemberAccessExpressionNode)
            {
                writeIfNotNative("()", leftDef);
            }
        }

        if (left.getParent() instanceof IMemberAccessExpressionNode)
        {
            // we are handling 'this' so don't write the '.'
            if (!DefinitionUtils.isThisIdentifier(left))
            {
                // if there is a transparent access, skip the '.'
                if (!getModel().skipOperator())
                    write(node.getOperator().getOperatorText());
            }
        }
        else
        {
            if (leftDef instanceof IAccessorDefinition)
            {
                writeIfNotNative("(", leftDef);
            }
            else
            {
                write(node.getOperator().getOperatorText());
            }
        }

        getEmitter().getWalker().walk(right);

        getModel().setSkipOperator(false);

        if (!getModel().isInAssignment()
                && rightDef instanceof IAccessorDefinition
                // c.get_foo().bar(42) was; c.get_foo()().bar(42)
                && !(right.getParent().getParent() instanceof IMemberAccessExpressionNode))
        {
            writeIfNotNative("()", rightDef);
        }
    }

}
