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
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.internal.tree.as.ArrayLiteralNode;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;
import org.apache.flex.compiler.tree.as.IParameterNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.DefinitionNameUtils;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.ExpressionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * Handles the production of the {@link IIdentifierNode}.
 * 
 * @author Michael Schmalle
 */
public class IdentifierEmitter extends BaseSubEmitter implements
        ISubEmitter<IIdentifierNode>
{
    public IdentifierEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IIdentifierNode node)
    {
        IExpressionNode lbase = ExpressionUtils.getNode(node, false,
                getProject());
        if (lbase == node)
        {
            if (ExpressionUtils.isValidThis(node, getProject()))
                write("this.");
        }

        IDefinition definition = node.resolve(getProject());

        if (definition instanceof IAccessorDefinition)
        {
            emitIdentifierAccessor(node, (IAccessorDefinition) definition);
        }
        else if (definition instanceof IFunctionDefinition)
        {
            emitIdentifierFunction(node, (IFunctionDefinition) definition);
        }
        else if (definition instanceof IParameterDefinition)
        {
            emitIdentifierParameter(node, (IParameterDefinition) definition);
        }
        else if (definition instanceof IConstantDefinition)
        {
            emitIdentifierConstant(node, (IConstantDefinition) definition);
        }
        else if (definition instanceof IVariableDefinition)
        {
            emitIdentifierVariable(node, (IVariableDefinition) definition);
        }
        else if (definition instanceof IClassDefinition)
        {
            emitIdentifierClass(node, (IClassDefinition) definition);
        }
        else
        {
            write(node.getName());
        }
    }

    private void emitIdentifierClass(IIdentifierNode node,
            IClassDefinition definition)
    {
        // figure out Class.FOO static var and qualify it
        if (node.getParent() instanceof IMemberAccessExpressionNode)
        {
            IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) node
                    .getParent();
            if (mnode.getRightOperandNode() instanceof IIdentifierNode)
            {
                IDefinition rightDef = mnode.getRightOperandNode().resolve(
                        getProject());
                if (rightDef instanceof IVariableDefinition)
                {
                    IVariableDefinition vdef = (IVariableDefinition) rightDef;
                    IClassDefinition cdef = definition;
                    if (vdef.isStatic())
                    {
                        String qualifiedName = cdef.getQualifiedName();
                        write(qualifiedName);
                        return;
                    }
                }
            }
        }
        else if (node.getParent() instanceof IVariableNode)
        {
            if (node == ((IVariableNode) node.getParent())
                    .getAssignedValueNode())
            {
                getModel().addDependency(definition, node);
            }
        }
        else
        {
            // just a class definition
            getModel().addDependency(definition, node);
        }

        // proto, don't know if I should put this here, checks and
        // adds a class from [], this is special since we don't have
        // a specific emit for array literal
        if (node.getAncestorOfType(ArrayLiteralNode.class) != null)
        {
            getModel().addDependency(definition, node);
        }

        String name = MetaDataUtils.getExportQualifiedName(definition);
        write(name);
        //write(node.getName());
    }

    private void emitIdentifierVariable(IIdentifierNode node,
            IVariableDefinition definition)
    {
        // TODO this is volitale a needs to use the identifier Class check on the left operand eventually
        if (definition.isStatic()
                && !(node.getParent() instanceof IMemberAccessExpressionNode))
        {
            // output the qualified name and identifier IE FOO[bar] where FOO is static
            IClassDefinition cdef = DefinitionUtils
                    .getClassDefinition(definition);
            if (cdef != null)
            {
                String qualifiedName = cdef.getQualifiedName();
                write(qualifiedName);
                write(".");
                write(definition.getBaseName());
            }
        }
        else
        {
            write(node.getName());
        }
    }

    private void emitIdentifierConstant(IIdentifierNode node,
            IConstantDefinition definition)
    {
        if (!(definition.getParent() instanceof IClassDefinition))
        {
            write(definition.getBaseName());
            return;
        }

        if (definition.getBaseName().equals("undefined"))
        {
            write("undefined");
            return;
        }

        String value = RandoriUtils.returnInitialConstantValue(definition,
                getProject());
        if (value != null)
        {
            write(value);
            return;
        }

        if (!getModel().isInAssignment())
        {
            // not in assignment, output the full name
            String name = DefinitionNameUtils.toExportQualifiedName(definition,
                    getProject());
            write(name);
            return;
        }
    }

    private void emitIdentifierAccessor(IIdentifierNode node,
            IAccessorDefinition definition)
    {
        if (!MetaDataUtils.isNative(definition))
        {
            String baseName = MetaDataUtils.getAccessorName(definition,
                    getProject());
            String headName = getAccessorName(definition, node, getProject());

            if (getModel().skipOperator())
            {
                // TODO make sure this logic is correct; if (Window.console != null) {
                // for now this also assumes we are native/global
                write(baseName);
                return;
            }

            if (!MetaDataUtils.hasJavaScriptPropertyName(definition,
                    getProject()) && headName != null)
            {
                write(headName + ".");
            }

            if (getModel().isInAssignment()
                    && ExpressionUtils.isRight(getModel().getAssign(), node))
            {
                if (getModel().isCall())
                    write("set_" + baseName + ".call");
                else
                    write("set_" + baseName);
                // we do not set setCall(false) since the binary value will pick it up
                // and add 'this' to the arguments
            }
            else
            {
                if (getModel().isCall())
                    write("get_" + baseName + ".call(this)");
                else
                    write("get_" + baseName + "()");
                getModel().setCall(false);
            }
        }
        else
        {
            write(node.getName());
        }
    }

    private void emitIdentifierFunction(IIdentifierNode node,
            IFunctionDefinition definition)
    {
        String name = MetaDataUtils.getFunctionBaseName(definition);
        if (DefinitionUtils.isPackageFunction(definition))
        {
            name = MetaDataUtils.getPackageFunctionExportName(definition);
            if (node.getAncestorOfType(ArrayLiteralNode.class) != null
                    || DefinitionUtils.isPackageFunction(definition))
            {
                getModel().addDependency(definition, node);
            }
        }
        write(name);
    }

    private void emitIdentifierParameter(IIdentifierNode node,
            IParameterDefinition definition)
    {
        if (definition.isRest()
                && !(node.getParent() instanceof IParameterNode))
        {
            write("arguments");
        }
        else
        {
            write(node.getName());
        }
    }

    // XXX HACK this needs to be worked out with MemberAccess that is adding the
    // qname
    public static String getAccessorName(IAccessorDefinition definition,
            IIdentifierNode node, ICompilerProject project)
    {
        if (definition.isStatic()
                && !(node.getParent() instanceof IMemberAccessExpressionNode))
        {
            String typeName = RandoriUtils.toTypeAccessQualifiedName(
                    definition.getNode(), project);
            return typeName;
        }

        return null;
    }
}
