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

import org.apache.flex.compiler.definitions.*;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;
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
        
        if (rightDef instanceof IConstantDefinition) {
            String value = DefinitionUtils.returnInitialConstantValue(
                    (IConstantDefinition) rightDef, getProject());
            write(value);
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
            RandoriUtils.addMemberExpressionDependency(node, getModel(),
                    getProject());
            return;
        }

        // this is Randori specific, where we skip things like 'Window' for now
        // and the operator
        if (!getModel().skipOperator())
        {
            // trans 'Foo.bar()' and 'foo.bar.Baz.goo()' calls
            if (leftDef instanceof IClassDefinition) {

                if (rightDef != null && rightDef.isStatic()) {
                    // add the static access's parent ClassDefinition
                    getModel().addDependency((IScopedDefinition) leftDef, node);
                }

                // shortcut for both shorthand class mentions and longhand
                // i.e. both Baz and foo.bar.Baz
                if (right.getParent().getNodeID() == ASTNodeID.MemberAccessExpressionID)
                {
                    String qualifiedName = MetaDataUtils
                            .getExportQualifiedName((ITypeDefinition) leftDef);
                    write(qualifiedName);
                }
                else
                {
                    getWalker().walk(left);
                }
            }
            /*
                        // trans '_staticVar.foo' to 'my.package._staticVar.foo'
                        else if (leftDef instanceof IVariableDefinition
                                && !(leftDef instanceof IAccessorDefinition)
                                && leftDef.isStatic())
                        {
                            //IDefinition definition = (IDefinition) right.resolve(project);

                            IClassDefinition parent = (IClassDefinition) leftDef
                                    .getParent();
                            // append the parent's qualified name on the static variable
                            if (MetaDataUtils.isClassExport(parent))
                            {
                                write(parent.getQualifiedName());
                                write(".");
                            }

                            getWalker().walk(left);
                        }
 */
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

                // turn off the "." operator for functions with a JavaScriptMethod() of ""
                // If this were up higher, the entire left side would not get written
                if ((rightDef instanceof IFunctionDefinition) && MetaDataUtils.getFunctionBaseName((IFunctionDefinition)rightDef).equals("")) {
                    getModel().setSkipOperator(true);
                }

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
