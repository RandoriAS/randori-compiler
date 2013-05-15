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

import org.apache.flex.compiler.constants.IASKeywordConstants;
import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.apache.flex.compiler.internal.definitions.AppliedVectorDefinition;
import org.apache.flex.compiler.internal.definitions.ClassTraitsDefinition;
import org.apache.flex.compiler.internal.tree.as.FunctionCallNode;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.ExpressionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.MetaDataUtils.MetaData;
import randori.compiler.internal.utils.MetaDataUtils.MetaData.Mode;
import randori.compiler.problems.CannotCallPrivateConstructorProblem;

/**
 * Handles the production of the {@link IFunctionCallNode}.
 * 
 * @author Michael Schmalle
 */
public class FunctionCallEmitter extends BaseSubEmitter implements
        ISubEmitter<IFunctionCallNode>
{
    public FunctionCallEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IFunctionCallNode node)
    {
        FunctionCallNode fnode = (FunctionCallNode) node;
        IDefinition definition = node.resolveCalledExpression(getProject());

        if (node.isNewExpression())
        {
            if (definition != null)
            {
                emitNew(node);
                return;
            }

            // continue to produce the new expression since it is no bound to a type
            // this probably means is a variable IE var foo:Function; foo();
            writeToken("new");
        }
        else if (definition instanceof IClassDefinition)
        {
            // cast Foo(bar); just walk the value in parens
            walkArguments(node);
            return;
        }
        else if (fnode.getNameNode() instanceof IMemberAccessExpressionNode)
        {
            IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) fnode
                    .getNameNode();
            if (mnode.getLeftOperandNode().getNodeID() == ASTNodeID.SuperID)
            {
                String baseName = DefinitionUtils.toSuperQualifiedName(node,
                        getProject());
                write(baseName);
                write(".prototype");
                // TODO where is the '.' getting added?
                getWalker().walk(node.getNameNode());
                write(".call");
                write("(");
                write("this");
                if (node.getArgumentNodes().length > 0)
                    write(",");
                walkArguments(node);
                write(")");
                return;
            }
        }

        // this injects super transform, new transform
        // super() to 'foo.bar.Baz.call'
        getWalker().walk(node.getNameNode());

        write("(");
        walkArguments(node);
        write(")");
    }

    protected void emitNew(IFunctionCallNode node)
    {
        ICompilerProject project = getWalker().getProject();
        IDefinition expression = node.resolveCalledExpression(project);
        if (!(expression instanceof IClassDefinition))
        {
            // variable or accessor reference getting instantiated
            IVariableDefinition vdef = (IVariableDefinition) expression;
            if (vdef instanceof IAccessorDefinition)
            {
                // XXX HACK FIX ERROR; this is a problem when the call resolves
                // to a variable or accessor type, I know how to fix
                //System.err.println("ERROR emitNewStatement()");
                write("new");
                write(" ");
                //String name = RandoriUtils.toNewAccessorPrefix(node, (IFunctionDefinition) vdef, project);
                write("(");
                getWalker().walk(node.getNameNode());
                write(")");
                write("(");
                walkArguments(node);
                write(")");

                return;
            }
            else
            {
                // XXX HACK FIX ERROR; this is a problem when the call resolves
                // to a variable or accessor type, I know how to fix
                //System.err.println("ERROR emitNewStatement()");
                write("new");
                write(" ");
                // XXX change this to walk
                write(vdef.getBaseName());
                write("(");
                walkArguments(node);
                write(")");
                return;
            }

        }

        IClassDefinition newDefinition = (IClassDefinition) expression;
        IClassDefinition definiton = DefinitionUtils.getClassDefinition(node);

        // check for ommit constructor
        IMetaTag ctag = MetaDataUtils.findJavaScriptTag(newDefinition);
        if (ctag != null)
        {
            IMetaTagAttribute attribute = ctag.getAttribute("omitconstructor");
            if (attribute != null && attribute.getValue().equals("true"))
            {
                getEmitter().getProblems().add(
                        new CannotCallPrivateConstructorProblem(node,
                                newDefinition.getBaseName()));
            }
        }

        // if the called expression type is NOT the same as the parent class
        if (definiton != newDefinition)
        {
            getEmitter().getModel().addDependency(newDefinition);
        }

        if (expression instanceof IClassDefinition)
        {
            // write out Object, Array and Vector in simple form
            // TODO this is not taking into account the arguments
            String baseName = ((IClassDefinition) expression).getBaseName();
            if (baseName.equals("Object"))
            {
                write("{}");
                return;
            }
            else if (newDefinition instanceof AppliedVectorDefinition)
            {
                write("[]");
                return;
            }
            else if (baseName.equals("Array"))
            {
                //IExpressionNode[] nodes = node.getArgumentNodes();
                write("[");
                walkArguments(node);
                write("]");
                return;
            }
            else if (!MetaDataUtils.isExport(expression))
            {
                IMetaTag tag = MetaDataUtils.findTag(expression,
                        MetaData.JavaScriptConstructor);
                if (tag != null)
                {
                    String factoryMethod = tag
                            .getAttributeValue("factoryMethod");
                    if (factoryMethod != null)
                    {
                        write(factoryMethod);
                        return;
                    }
                }
            }
        }

        // first see if there is a JavaScript
        if (MetaDataUtils.hasJavaScriptTag(newDefinition))
        {
            // is the class an export
            if (MetaDataUtils.isClassExport(newDefinition))
            {
                // since the class is an export, get the 'mode'
                // the default is 'prototype'
                Mode mode = MetaDataUtils.getMode(newDefinition);
                switch (mode)
                {
                case GLOBAL:
                    emitGlobal(node, newDefinition);
                    break;
                case JSON:
                    emitJson(node, newDefinition);
                    break;
                default: // PROTOTYPE:
                    emitPrototype(node, newDefinition);
                    break;
                }
            }
            else
            {
                // [JavaScript(export="false",name="Object")]
                Mode mode = MetaDataUtils.getMode(newDefinition);
                switch (mode)
                {
                case JSON:
                    emitJson(node, newDefinition);
                    break;
                default:
                    String name = MetaDataUtils.getExportName(newDefinition);
                    if (name.equals("AudioContext"))
                    {
                        name = "webkit" + name;
                    }
                    write("new");
                    write(" ");
                    write(name);
                    write("(");
                    walkArguments(node);
                    write(")");
                    break;
                }
            }
        }
        else
        {
            emitPrototype(node, newDefinition);
        }
    }

    private void emitPrototype(IFunctionCallNode node, IClassDefinition type)
    {
        // fully qualified name ie; bar = new foo.bar.Baz(42, foo);
        write(IASKeywordConstants.NEW);
        write(" ");

        // foo.bar.baz.A
        String proto = toPrototype(type);
        write(proto);

        //emitter.getWalker().walk(node.getNameNode());

        write("(");
        walkArguments(node);
        write(")");
    }

    private void emitJson(IFunctionCallNode node, IClassDefinition type)
    {
        // foreach parameter in the types constructor, match the argument
        // if there is no argument for the parameter, use the default value.

        IFunctionDefinition constructor = type.getConstructor();
        IParameterDefinition[] parameters = constructor.getParameters();
        IExpressionNode[] arguments = node.getArgumentNodes();

        write("{");
        if (arguments.length > 0)
        {
            int i = 0;
            final int len = parameters.length;
            for (IParameterDefinition parameter : parameters)
            {
                // name
                write(parameter.getBaseName());
                write(":");

                // vaule
                if (i < arguments.length)
                {
                    getWalker().walk(arguments[i]);
                }
                else
                {
                    // default
                    String value = DefinitionUtils.returnInitialVariableValue(
                            parameter.getVariableNode(), getEmitter());
                    write(value);
                }

                if (i < len - 1)
                    write(", ");
                i++;
            }
        }
        write("}");
    }

    private void emitGlobal(IFunctionCallNode node, ITypeDefinition type)
    {
        write("new");
        write(" ");

        write("(");
        walkArguments(node);
        write(")");
    }

    protected void walkArguments(IFunctionCallNode node)
    {
        getModel().setInArguments(true);

        FunctionCallNode fnode = (FunctionCallNode) node;
        IExpressionNode[] nodes = node.getArgumentNodes();
        int len = nodes.length;
        // only add 'this' to a constructor super() call, not a super.foo() call
        if (ExpressionUtils.injectThisArgument(fnode, false))
        {
            write("this");
            if (len > 0)
                writeToken(",");
        }

        for (int i = 0; i < len; i++)
        {
            IExpressionNode inode = nodes[i];
            if (inode.getNodeID() == ASTNodeID.IdentifierID)
            {
                // test for Functions to be wrapped with createDelegate()
                emitArgumentIdentifier((IIdentifierNode) inode);
            }
            else
            {
                getWalker().walk(inode);
            }

            if (i < len - 1)
            {
                writeToken(",");
            }
            else if (i == len - 1)
            {
                emitExtra(node);
            }
        }

        getModel().setInArguments(false);
    }

    // TODO (mschmalle) Eventually we need a plugin for these edge case language diffs
    /*
     * - parseInt; add null if 1 argument
     */
    private void emitExtra(IFunctionCallNode node)
    {
        // parseInt()
        IDefinition definition = node.getNameNode().resolve(getProject());
        if (definition != null)
        {
            if (definition.getBaseName().equals("parseInt")
                    && node.getArgumentNodes().length == 1)
                write(", null");
        }
    }

    private void emitArgumentIdentifier(IIdentifierNode node)
    {
        ITypeDefinition type = node.resolveType(getProject());
        if (type instanceof ClassTraitsDefinition)
        {
            String name = MetaDataUtils
                    .getClassExportName((ClassTraitsDefinition) type);
            write(name);
            getModel().addDependency(type);
        }
        else if (type instanceof IClassDefinition
                && type.getBaseName().equals("Function"))
        {
            write(IRandoriEmitter.STATIC_DELEGATE_NAME);
            write("(this, ");
            getWalker().walk(node);
            write(")");
        }
        else
        {
            getWalker().walk(node);
        }
    }

    private String toPrototype(ITypeDefinition definition)
    {
        // this 'would' be a IFunctionCallNode.getNameNode()
        String name = MetaDataUtils.getExportName(definition);
        //return definition.getQualifiedName();
        return name;
    }
}
