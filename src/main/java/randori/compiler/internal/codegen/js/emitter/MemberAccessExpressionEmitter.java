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
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IContainerNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
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
        IDefinition rightDefType = right.resolveType(project);

        boolean isTransparent = RandoriUtils.isTransparentMemberAccess(left,
                right);
        boolean isGlobalStatic = RandoriUtils.isGlobalStatic(left, right,
                project);

        boolean wasDelegated = false;

        // the left is 'Window', the right is the static method
        getModel().setSkipOperator(isTransparent || isGlobalStatic);

        // JQueryStatic
        if (RandoriUtils.isJQueryStaticJ(left, right))
        {
            write("jQuery");
            return;
        }

        if (rightDefType != null
                && rightDefType.getBaseName().equals("Function"))
        {
            if (getModel().isInArguments()
                    && node.getParent() instanceof IContainerNode)
            {
                write(IRandoriEmitter.STATIC_DELEGATE_NAME);
                String string = getEmitter().stringifyNode(left);
                write("(" + string + ", ");
                wasDelegated = true;
            }
        }

        if (RandoriUtils.isTransparentAccessorAccess(left, right))
        {
            // '[Window.console].[log]()'
            // just write the lefts's right operand
            IMemberAccessExpressionNode parent = ((IMemberAccessExpressionNode) node
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
            IConstantDefinition definition = (IConstantDefinition) right
                    .resolve(project);
            String value = DefinitionUtils.returnInitialConstantValue(
                    definition, getProject());
            write(value);
            return;
        }
        
        // this is Randori specific, where we skip things like 'Window' for now
        // and the operator
        if (!getModel().skipOperator())
        {
            // if the left def is a class and the
            if (leftDef instanceof IClassDefinition && rightDef != null
                    && rightDef.isStatic())
            {
                // this takes care of 'Foo.bar()' and 'foo.bar.Baz.goo()' calls
                String qualifiedName = MetaDataUtils
                        .getExportName((ITypeDefinition) leftDef);
                write(qualifiedName);
            }
            else
            {
                getWalker().walk(left);
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

        if (wasDelegated)
        {
            write(")");
        }

        getModel().setSkipOperator(false);
    }

}
