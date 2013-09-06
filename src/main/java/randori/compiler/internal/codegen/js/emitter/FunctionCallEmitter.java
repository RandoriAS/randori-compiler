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

import java.util.List;

import org.apache.flex.compiler.constants.IASKeywordConstants;
import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.IScopedDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.apache.flex.compiler.internal.definitions.AppliedVectorDefinition;
import org.apache.flex.compiler.internal.definitions.ClassTraitsDefinition;
import org.apache.flex.compiler.internal.tree.as.ContainerNode;
import org.apache.flex.compiler.internal.tree.as.FunctionCallNode;
import org.apache.flex.compiler.internal.tree.as.NumericLiteralNode;
import org.apache.flex.compiler.internal.tree.as.TypedExpressionNode;
import org.apache.flex.compiler.internal.tree.as.VectorLiteralNode;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.codegen.js.utils.GenericEmitUtils;
import randori.compiler.internal.utils.DefinitionNameUtils;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.ExpressionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.MetaDataUtils.MetaData;
import randori.compiler.internal.utils.MetaDataUtils.MetaData.Mode;
import randori.compiler.internal.utils.RandoriUtils;
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
                if (node.getNameNode().getNodeID() == ASTNodeID.MemberAccessExpressionID)
                {
                    write("new ");
                    getWalker().walk(node.getNameNode());
                    write("(");
                    walkParameters(node);
                    write(")");

                }
                else
                {
                    emitNew(node);
                }

                return;
            }

            // continue to produce the new expression since it is no bound to a type
            // this probably means is a variable IE var foo:Function; foo();
            writeToken("new");
        }
        else if (definition instanceof IClassDefinition)
        {
            // TODO Think about giving a warning if ((IIdentifierNode)node.getNameNode()).getName() == "int" case
            String castName = ((IIdentifierNode)node.getNameNode()).getName() + "";
            // cast Foo(bar); just walk the value in parens
            if (castName.equals("int"))
            {
                write("~~");
                write("(");
                walkParameters(node);
                write(")");
                return;
            }
            else
            {
                walkParameters(node);
                return;
            }
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
                walkParameters(node);
                write(")");
                return;
            }
        }

        if (definition != null && definition.getNode() != null
                && definition.isStatic())
        {
            // stati cmethod call
            IScopedDefinition parent = (IScopedDefinition) definition
                    .getParent();

            getModel().addDependency(parent, node);
            String name = DefinitionNameUtils.toExportQualifiedName(definition,
                    getProject());
            write(name);
        }
        else
        {
            // check for package level function for dep
            if (DefinitionUtils.isPackageFunction(definition))
            {
                getModel().addDependency((IScopedDefinition) definition, node);
            }

            // this injects super transform, new transform
            // super() to 'foo.bar.Baz.call'
            getWalker().walk(node.getNameNode());
        }

        write("(");
        walkParameters(node);
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
                walkParameters(node);
                write(")");

                return;
            }
            else
            {
                write("new");
                write(" ");
                getEmitter().getWalker().walk(
                        vdef.getVariableNode().getNameExpressionNode());
                write("(");
                walkParameters(node);
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
            getEmitter().getModel().addDependency(newDefinition, node);
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
                write("[");
                IExpressionNode[] argumentNodes = node.getArgumentNodes();
                AppliedVectorDefinition vector = (AppliedVectorDefinition) newDefinition;
                if (argumentNodes.length == 1)
                {
                    IExpressionNode arg = argumentNodes[0];
                    if (arg.getNodeID() == ASTNodeID.LiteralIntegerID)
                    {
                        NumericLiteralNode literal = (NumericLiteralNode) arg;
                        Integer value = Integer.parseInt(literal.getValue());
                        // IE String, int
                        String vectorElementType = vector.getBaseName();
                        String defaultValue = "null";
                        if (vectorElementType.contains("Number")
                                || vectorElementType.contains("int")
                                || vectorElementType.contains("unit"))
                        {
                            defaultValue = "0";
                        }
                        for (int i = 0; i < value; i++)
                        {
                            write(defaultValue);
                            if (i < value - 1)
                                write(", ");
                        }
                    }
                }
                else
                {
                    walkParameters(node);
                }

                write("]");
                return;
            }
            else if (baseName.equals("Array"))
            {
                write("[");
                walkParameters(node);
                write("]");
                return;
            }
            else
            {
                IMetaTag tag = MetaDataUtils.findTag(expression,
                        MetaData.JavaScriptConstructor);
                if (tag != null)
                {
                    String factoryMethod = tag
                            .getAttributeValue("factoryMethod");
                    if (factoryMethod != null)
                    {
                        int start = factoryMethod.indexOf("...");
                        if (start != -1)
                        {
                            final String head = factoryMethod.substring(0,
                                    factoryMethod.indexOf("("));
                            write(head);
                            write("(");
                            walkArguments(node);
                            write(")");
                        }
                        else
                        {
                            write(factoryMethod);
                        }
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
                    String name = MetaDataUtils
                            .getExportQualifiedName(newDefinition);
                    if (name.equals("AudioContext"))
                    {
                        name = "webkit" + name;
                    }
                    write("new");
                    write(" ");
                    write(name);
                    write("(");
                    walkParameters(node);
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
        walkParameters(node);
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
            int len = parameters.length;
            int lena = arguments.length;
            boolean flag = false;
            for (IParameterDefinition parameter : parameters)
            {
                String arg = null;

                if (i < arguments.length)
                {
                    arg = getWalker().getEmitter().stringifyNode(arguments[i]);
                }

                String value = null;

                if (parameter.getVariableNode() != null)
                {
                    value = DefinitionUtils.returnInitialVariableValue(
                            parameter.getVariableNode(), getEmitter());
                }

                if (arg != null
                        && arg.equals("undefined")
                        || (arg == null && value != null && value
                                .equals("undefined")))
                {
                    flag = true;
                    lena--;
                    i++;
                    continue;
                }

                // name
                write(parameter.getBaseName());
                write(":");

                // vaule
                if (i < arguments.length)
                {
                    write(arg);
                }
                else
                {
                    // default
                    write(value);
                }
                // this is crap, but I will get back to it
                if ((!flag && i < len - 1) || (flag && i < lena))
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
        walkParameters(node);
        write(")");
    }

    protected void walkParameters(IFunctionCallNode node)
    {
        getModel().setInArguments(true);

        // we will loop through the actual parameters of the method
        // IDefinition, this way we can see if a default argument
        // needs to be inserted as a placeholder for a missing argument

        IFunctionDefinition functionDefinition = null;
        IDefinition definition = node.resolveCalledExpression(getProject());

        // only a Vector Literal will be emitted in the first if,
        if (definition instanceof AppliedVectorDefinition)
        {
            if (!(node.getNameNode() instanceof TypedExpressionNode))
            {
                VectorLiteralNode vnode = (VectorLiteralNode) node
                        .getNameNode();
                ContainerNode contentsNode = vnode.getContentsNode();
                emitContainerNode(contentsNode);
            }
        }
        else if (definition instanceof IClassDefinition)
        {
            functionDefinition = ((IClassDefinition) definition)
                    .getConstructor();
            if (hasRest(functionDefinition))
            {
                walkArguments(node);
                return;
            }
        }
        else if (definition instanceof IFunctionDefinition)
        {
            ITypeDefinition returnType = ((IFunctionDefinition) definition)
                    .resolveReturnType(getProject());

            functionDefinition = (IFunctionDefinition) definition;

            if (returnType.getBaseName().equals("Function")
                    || returnType.getBaseName().equals("Object")
                    || returnType.getBaseName().equals("*"))
            {
                // if this is a Function function call, we cannot know what
                // the paramters are, so just emit the arguments present
                walkArguments(node);
                return;
            }
        }
        else if (!ExpressionUtils.isSuperExpression(node.getNameNode()))
        {
            walkArguments(node);
            return;
        }

        FunctionCallNode fnode = (FunctionCallNode) node;
        IExpressionNode[] arguments = node.getArgumentNodes();
        int argLen = arguments.length;
        // only add 'this' to a constructor super() call, not a super.foo() call
        if (ExpressionUtils.injectThisArgument(fnode, false))
        {
            write("this");
            if (argLen > 0)
                writeToken(",");
        }

        // constructor
        if (functionDefinition == null)
        {
            IClassNode type = (IClassNode) node
                    .getAncestorOfType(IClassNode.class);
            if (type != null)
            {
                functionDefinition = type.getDefinition()
                        .resolveBaseClass(getProject()).getConstructor();
            }
        }

        // 1. check for a IFunctionDefinition
        if (functionDefinition != null)
        {
            List<IParameterDefinition> parameters = GenericEmitUtils
                    .getParameters(node, functionDefinition, getProject());

            int paramLen = parameters.size();
            if (argLen > paramLen)
            {
                walkArguments(fnode);
                return;
            }

            for (int i = 0; i < paramLen; i++)
            {
                IParameterDefinition parameter = parameters.get(i);
                if (i < argLen)
                {
                    IExpressionNode expression = arguments[i];
                    // use the argument
                    if (expression.getNodeID() == ASTNodeID.IdentifierID)
                    {
                        // test for Functions to be wrapped with createDelegate()
                        emitArgumentIdentifier((IIdentifierNode) expression);
                    }
                    else
                    {
                        RandoriUtils.addMemberExpressionDependency(expression,
                                getModel(), getProject());

                        getWalker().walk(expression);
                    }
                }
                else
                {
                    // use the default parameter
                    if (parameter.hasDefaultValue())
                    {
                        String value = ExpressionUtils.toInitialValue(
                                parameter, getProject());
                        write(value);
                    }
                    else
                    {
                        // XXX throw error, is this invalid?
                    }
                }

                // XXX This might not work here
                if (i < paramLen - 1)
                {
                    writeToken(",");
                }
                else if (i == paramLen - 1)
                {
                    emitExtra(node);
                }
            }
        }

        getModel().setInArguments(false);
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
            //if (definition.getBaseName().equals("parseInt")
            //        && node.getArgumentNodes().length == 1)
            //    write(", null");
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
            getModel().addDependency(type, node);
        }
        else if (type instanceof IClassDefinition
                && type.getBaseName().equals("Function"))
        {
            write(IRandoriEmitter.STATIC_DELEGATE_NAME);
            write("(this, ");
            String string = getEmitter().stringifyNode(node);
            write(string);
            write(")");
        }
        else
        {
            getWalker().walk(node);
        }
    }

    private void emitContainerNode(ContainerNode node)
    {
        final int len = node.getChildCount();
        for (int i = 0; i < len; i++)
        {
            IExpressionNode inode = (IExpressionNode) node.getChild(i);
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
                writeToken(",");
        }
    }

    private String toPrototype(ITypeDefinition definition)
    {
        // this 'would' be a IFunctionCallNode.getNameNode()
        String name = MetaDataUtils.getExportQualifiedName(definition);
        return name;
    }

    public boolean hasRest(IFunctionDefinition definition)
    {
        for (IParameterDefinition parameter : definition.getParameters())
        {
            if (parameter.isRest())
                return true;
        }

        return false;
    }
}
