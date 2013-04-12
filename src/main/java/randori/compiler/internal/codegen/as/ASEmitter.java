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

package randori.compiler.internal.codegen.as;

import java.io.FilterWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.common.ASModifier;
import org.apache.flex.compiler.common.IImportTarget;
import org.apache.flex.compiler.common.ModifiersSet;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.internal.tree.as.ChainedVariableNode;
import org.apache.flex.compiler.internal.tree.as.ContainerNode;
import org.apache.flex.compiler.internal.tree.as.FunctionNode;
import org.apache.flex.compiler.internal.tree.as.LabeledStatementNode;
import org.apache.flex.compiler.internal.tree.as.NamespaceAccessExpressionNode;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IAccessorNode;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.ICatchNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IConditionalNode;
import org.apache.flex.compiler.tree.as.IContainerNode;
import org.apache.flex.compiler.tree.as.IContainerNode.ContainerType;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IDynamicAccessNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IForLoopNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IFunctionObjectNode;
import org.apache.flex.compiler.tree.as.IGetterNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IIfNode;
import org.apache.flex.compiler.tree.as.IImportNode;
import org.apache.flex.compiler.tree.as.IInterfaceNode;
import org.apache.flex.compiler.tree.as.IIterationFlowNode;
import org.apache.flex.compiler.tree.as.IKeywordNode;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode;
import org.apache.flex.compiler.tree.as.ILiteralContainerNode;
import org.apache.flex.compiler.tree.as.ILiteralNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;
import org.apache.flex.compiler.tree.as.INamespaceNode;
import org.apache.flex.compiler.tree.as.INumericLiteralNode;
import org.apache.flex.compiler.tree.as.IObjectLiteralValuePairNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.tree.as.IParameterNode;
import org.apache.flex.compiler.tree.as.IReturnNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.ISetterNode;
import org.apache.flex.compiler.tree.as.IStatementNode;
import org.apache.flex.compiler.tree.as.ISwitchNode;
import org.apache.flex.compiler.tree.as.ITerminalNode;
import org.apache.flex.compiler.tree.as.ITernaryOperatorNode;
import org.apache.flex.compiler.tree.as.IThrowNode;
import org.apache.flex.compiler.tree.as.ITryNode;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.tree.as.ITypedExpressionNode;
import org.apache.flex.compiler.tree.as.IUnaryOperatorNode;
import org.apache.flex.compiler.tree.as.IVariableExpressionNode;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.apache.flex.compiler.tree.as.IWhileLoopNode;
import org.apache.flex.compiler.tree.as.IWithNode;
import org.apache.flex.compiler.tree.metadata.IMetaTagNode;

import randori.compiler.codegen.IEmitter;
import randori.compiler.codegen.as.IASEmitter;
import randori.compiler.internal.utils.ASNodeUtils;
import randori.compiler.visitor.as.IASBlockWalker;

/**
 * The base implementation for an ActionScript emitter.
 * 
 * @author Michael Schmalle
 */
public class ASEmitter implements IASEmitter, IEmitter
{
    private final FilterWriter out;

    private boolean bufferWrite;

    protected boolean isBufferWrite()
    {
        return bufferWrite;
    }

    protected void setBufferWrite(boolean value)
    {
        bufferWrite = value;
    }

    private StringBuilder builder;

    protected StringBuilder getBuilder()
    {
        return builder;
    }

    protected void flushBuilder()
    {
        setBufferWrite(false);
        write(builder.toString());
        builder.setLength(0);
    }

    protected List<ICompilerProblem> problems;

    // (mschmalle) think about how this should be implemented, we can add our
    // own problems to this, they don't just have to be parse problems
    @Override
    public List<ICompilerProblem> getProblems()
    {
        return problems;
    }

    private int currentIndent = 0;

    protected int getCurrentIndent()
    {
        return currentIndent;
    }

    private IASBlockWalker walker;

    @Override
    public IASBlockWalker getWalker()
    {
        return walker;
    }

    @Override
    public void setWalker(IASBlockWalker value)
    {
        walker = value;
    }

    //    @Override
    //    public IDocEmitter getDocEmitter()
    //    {
    //        return null;
    //    }
    //
    //    @Override
    //    public void setDocEmitter(IDocEmitter value)
    //    {
    //    }

    public ASEmitter(FilterWriter out)
    {
        this.out = out;
        builder = new StringBuilder();
        problems = new ArrayList<ICompilerProblem>();
    }

    @Override
    public void write(String value)
    {
        try
        {
            if (!bufferWrite)
                out.write(value);
            else
                builder.append(value);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected String getIndent(int numIndent)
    {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numIndent; i++)
            sb.append("\t");
        return sb.toString();
    }

    @Override
    public void indentPush()
    {
        currentIndent++;
    }

    @Override
    public void indentPop()
    {
        currentIndent--;
    }

    @Override
    public void writeNewline()
    {
        write("\n");
        write(getIndent(currentIndent));
    }

    @Override
    public void writeNewline(String value)
    {
        write(value);
        writeNewline();
    }

    @Override
    public void writeNewline(String value, boolean pushIndent)
    {
        if (pushIndent)
            indentPush();
        else
            indentPop();
        write(value);
        writeNewline();
    }

    public void writeSymbol(String value)
    {
        write(value);
    }

    @Override
    public void writeToken(String value)
    {
        write(value);
        write(" ");
    }

    //--------------------------------------------------------------------------
    // IPackageNode
    //--------------------------------------------------------------------------

    @Override
    public void emitImport(IImportNode node)
    {
        IImportTarget target = node.getImportTarget();
        writeToken("import");
        write(target.toString());
    }

    @Override
    public void emitPackageHeader(IPackageDefinition definition)
    {
        write("package");

        IPackageNode node = definition.getNode();
        String name = node.getQualifiedName();
        if (name != null && !name.equals(""))
        {
            write(" ");
            getWalker().walk(node.getNameExpressionNode());
        }

        write(" ");
        write("{");
    }

    @Override
    public void emitPackageHeaderContents(IPackageDefinition definition)
    {
    }

    @Override
    public void emitPackageContents(IPackageDefinition definition)
    {
        IPackageNode node = definition.getNode();
        ITypeNode tnode = findTypeNode(node);
        if (tnode != null)
        {
            indentPush();
            writeNewline();
            getWalker().walk(tnode); // IClassNode | IInterfaceNode
        }
    }

    @Override
    public void emitPackageFooter(IPackageDefinition definition)
    {
        indentPop();
        writeNewline();
        write("}");
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    @Override
    public void emitClass(IClassNode node)
    {
        writeToken(node.getNamespace());

        if (node.hasModifier(ASModifier.FINAL))
        {
            writeToken("final");
        }
        else if (node.hasModifier(ASModifier.DYNAMIC))
        {
            writeToken("dynamic");
        }

        writeToken("class");
        getWalker().walk(node.getNameExpressionNode());
        write(" ");

        IExpressionNode bnode = node.getBaseClassExpressionNode();
        if (bnode != null)
        {
            writeToken("extends");
            getWalker().walk(bnode);
            write(" ");
        }

        IExpressionNode[] inodes = node.getImplementedInterfaceNodes();
        final int ilen = inodes.length;
        if (ilen != 0)
        {
            writeToken("implements");
            for (int i = 0; i < ilen; i++)
            {
                getWalker().walk(inodes[i]);
                if (i < ilen - 1)
                {
                    writeToken(",");
                }
            }
            write(" ");
        }

        write("{");

        // fields, methods, namespaces
        final IDefinitionNode[] members = node.getAllMemberNodes();
        if (members.length > 0)
        {
            indentPush();
            writeNewline();

            final int len = members.length;
            int i = 0;
            for (IDefinitionNode mnode : members)
            {
                getWalker().walk(mnode);
                if (mnode.getNodeID() == ASTNodeID.VariableID)
                {
                    write(";");
                    if (i < len - 1)
                        writeNewline();
                }
                else if (mnode.getNodeID() == ASTNodeID.FunctionID)
                {
                    if (i < len - 1)
                        writeNewline();
                }
                else if (mnode.getNodeID() == ASTNodeID.GetterID
                        || mnode.getNodeID() == ASTNodeID.SetterID)
                {
                    if (i < len - 1)
                        writeNewline();
                }
                i++;
            }

            indentPop();
        }

        writeNewline();
        write("}");
    }

    @Override
    public void emitInterface(IInterfaceNode node)
    {
        writeToken(node.getNamespace());

        writeToken("interface");
        getWalker().walk(node.getNameExpressionNode());
        write(" ");

        IExpressionNode[] inodes = node.getExtendedInterfaceNodes();
        final int ilen = inodes.length;
        if (ilen != 0)
        {
            writeToken("extends");
            for (int i = 0; i < ilen; i++)
            {
                getWalker().walk(inodes[i]);
                if (i < ilen - 1)
                {
                    writeToken(",");
                }
            }
            write(" ");
        }

        write("{");

        final IDefinitionNode[] members = node.getAllMemberDefinitionNodes();
        if (members.length > 0)
        {
            indentPush();
            writeNewline();

            final int len = members.length;
            int i = 0;
            for (IDefinitionNode mnode : members)
            {
                getWalker().walk(mnode);
                write(";");
                if (i < len - 1)
                    writeNewline();
                i++;
            }

            indentPop();
        }

        writeNewline();
        write("}");
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    @Override
    public void emitVarDeclaration(IVariableNode node)
    {
        if (!(node instanceof ChainedVariableNode))
        {
            emitMemberKeyword(node);
        }

        emitDeclarationName(node);
        emitType(node.getVariableTypeNode());
        emitAssignedValue(node.getAssignedValueNode());

        if (!(node instanceof ChainedVariableNode))
        {
            // check for chained variables
            int len = node.getChildCount();
            for (int i = 0; i < len; i++)
            {
                IASNode child = node.getChild(i);
                if (child instanceof ChainedVariableNode)
                {
                    writeToken(",");
                    emitVarDeclaration((IVariableNode) child);
                }
            }
        }

        // the client such as IASBlockWalker is responsible for the 
        // semi-colon and newline handling
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    @Override
    public void emitFieldDocumentation(IVariableNode node)
    {
    }

    @Override
    public void emitField(IVariableNode node)
    {
        emitFieldDocumentation(node);

        IVariableDefinition definition = (IVariableDefinition) node
                .getDefinition();

        if (!(node instanceof ChainedVariableNode))
        {
            emitNamespaceIdentifier(node);
            emitModifiers(definition);
            emitMemberKeyword(node);
        }

        emitMemberName(node);
        emitType(node.getVariableTypeNode());
        emitAssignedValue(node.getAssignedValueNode());

        if (!(node instanceof ChainedVariableNode))
        {
            // check for chained variables
            int len = node.getChildCount();
            for (int i = 0; i < len; i++)
            {
                IASNode child = node.getChild(i);
                if (child instanceof ChainedVariableNode)
                {
                    writeToken(",");
                    emitField((IVariableNode) child);
                }
            }
        }

        // the client such as IASBlockWalker is responsible for the 
        // semi-colon and newline handling
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    @Override
    public void emitMethodDocumentation(IFunctionNode node)
    {
    }

    @Override
    public void emitMethod(IFunctionNode node)
    {
        // see below, this is temp, I don't want a bunch of duplicated code
        // at them moment, subclasses can refine anyways, we are generalizing
        if (node instanceof IGetterNode)
        {
            emitGetAccessorDocumentation((IGetterNode) node);
        }
        else if (node instanceof ISetterNode)
        {
            emitSetAccessorDocumentation((ISetterNode) node);
        }
        else
        {
            emitMethodDocumentation(node);
        }

        FunctionNode fn = (FunctionNode) node;
        fn.parseFunctionBody(problems);

        IFunctionDefinition definition = node.getDefinition();

        emitNamespaceIdentifier(node);
        emitModifiers(definition);
        emitMemberKeyword(node);

        // I'm cheating right here, I haven't "seen" the light
        // on how to properly and efficiently deal with accessors since they are SO alike
        // I don't want to lump them in with methods because implementations in the
        // future need to know the difference without loopholes
        if (node instanceof IAccessorNode)
        {
            emitAccessorKeyword(((IAccessorNode) node).getAccessorKeywordNode());
        }

        emitMemberName(node);
        emitParamters(node.getParameterNodes());
        emitType(node.getReturnTypeNode());
        if (node.getParent().getParent().getNodeID() == ASTNodeID.ClassID)
        {
            emitMethodScope(node.getScopedNode());
        }

        // the client such as IASBlockWalker is responsible for the 
        // semi-colon and newline handling
    }

    @Override
    public void emitMethodScope(IFunctionNode node)
    {
    }

    @Override
    public void emitParamters(IFunctionNode node)
    {
    }

    @Override
    public void emitGetAccessorDocumentation(IGetterNode node)
    {
    }

    @Override
    public void emitGetAccessor(IGetterNode node)
    {
        // just cheat for now, IGetterNode is a IFunctionNode
        emitMethod(node);
    }

    @Override
    public void emitSetAccessorDocumentation(ISetterNode node)
    {
    }

    @Override
    public void emitSetAccessor(ISetterNode node)
    {
        // just cheat for now, ISetterNode is a IFunctionNode
        emitMethod(node);
    }

    @Override
    public void emitFunctionObject(IFunctionObjectNode node)
    {
        FunctionNode fnode = node.getFunctionNode();
        write("function");
        emitParamters(fnode.getParameterNodes());
        emitType(fnode.getTypeNode());
        emitFunctionScope(fnode.getScopedNode());
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    @Override
    public void emitNamespace(INamespaceNode node)
    {
        emitNamespaceIdentifier(node);
        writeToken("namespace");
        emitMemberName(node);
        write(" ");
        writeToken("=");
        getWalker().walk(node.getNamespaceURINode());
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    protected void emitNamespaceIdentifier(IDefinitionNode node)
    {
        String namespace = node.getNamespace();
        if (namespace != null && !namespace.equals("internal"))
        {
            writeToken(namespace);
        }
    }

    protected void emitModifiers(IDefinition definition)
    {
        ModifiersSet modifierSet = definition.getModifiers();
        if (modifierSet.hasModifiers())
        {
            for (ASModifier modifier : modifierSet.getAllModifiers())
            {
                writeToken(modifier.toString());
            }
        }
    }

    protected void emitMemberKeyword(IDefinitionNode node)
    {
        if (node instanceof IFunctionNode)
        {
            writeToken("function");
        }
        else if (node instanceof IVariableNode)
        {
            writeToken(((IVariableNode) node).isConst() ? "const" : "var");
        }
    }

    protected void emitMemberName(IDefinitionNode node)
    {
        getWalker().walk(node.getNameExpressionNode());
    }

    protected void emitDeclarationName(IDefinitionNode node)
    {
        getWalker().walk(node.getNameExpressionNode());
    }

    protected void emitParamters(IParameterNode[] nodes)
    {
        write("(");
        int len = nodes.length;
        for (int i = 0; i < len; i++)
        {
            IParameterNode node = nodes[i];
            getWalker().walk(node); //emitParameter
            if (i < len - 1)
            {
                writeToken(",");
            }
        }
        write(")");
    }

    @Override
    public void emitParameter(IParameterNode node)
    {
        getWalker().walk(node.getNameExpressionNode());
        write(":");
        getWalker().walk(node.getVariableTypeNode());
        IExpressionNode anode = node.getAssignedValueNode();
        if (anode != null)
        {
            write(" ");
            writeToken("=");
            getWalker().walk(anode);
        }
    }

    protected void emitType(IExpressionNode node)
    {
        // TODO (mschmalle) node.getVariableTypeNode() will return "*" if undefined, what to use?
        // or node.getReturnTypeNode()
        if (node != null)
        {
            write(":");
            getWalker().walk(node);
        }
    }

    protected void emitAssignedValue(IExpressionNode node)
    {
        if (node != null)
        {
            write(" ");
            writeToken("=");
            getWalker().walk(node);
        }
    }

    @Override
    public void emitFunctionBlockHeader(IFunctionNode node)
    {
        // nothing to do in AS
    }

    protected void emitMethodScope(IScopedNode node)
    {
        write(" ");
        getWalker().walk(node);
    }

    protected void emitAccessorKeyword(IKeywordNode node)
    {
        getWalker().walk(node);
        write(" ");
    }

    protected void emitFunctionScope(IScopedNode node)
    {
        emitMethodScope(node);
    }

    //--------------------------------------------------------------------------
    // Statements
    //--------------------------------------------------------------------------

    @Override
    public void emitStatement(IASNode node)
    {
        getWalker().walk(node);
        // XXX (mschmalle) this should be in the after handler?
        if (node.getParent().getNodeID() != ASTNodeID.LabledStatementID
                && !(node instanceof IStatementNode))
        {
            write(";");
        }

        if (!isLastStatement(node))
            writeNewline();
    }

    @Override
    public void emitIf(IIfNode node)
    {
        IConditionalNode conditional = (IConditionalNode) node.getChild(0);

        IContainerNode xnode = (IContainerNode) conditional
                .getStatementContentsNode();

        writeToken("if");
        //write(SPACE);
        write("(");
        getWalker().walk(conditional.getChild(0)); // conditional expression
        write(")");
        if (!isImplicit(xnode))
            write(" ");

        getWalker().walk(conditional.getChild(1)); // BlockNode
        IConditionalNode[] nodes = node.getElseIfNodes();
        if (nodes.length > 0)
        {
            for (int i = 0; i < nodes.length; i++)
            {
                IConditionalNode enode = nodes[i];
                IContainerNode snode = (IContainerNode) enode
                        .getStatementContentsNode();

                final boolean isImplicit = isImplicit(snode);
                if (isImplicit)
                    writeNewline();
                else
                    write(" ");

                writeToken("else");
                writeToken("if");
                write("(");
                getWalker().walk(enode.getChild(0));
                write(")");
                if (!isImplicit)
                    write(" ");

                getWalker().walk(enode.getChild(1)); // ConditionalNode
            }
        }

        ITerminalNode elseNode = node.getElseNode();
        if (elseNode != null)
        {
            IContainerNode cnode = (IContainerNode) elseNode.getChild(0);
            // if an implicit if, add a newline with no space
            final boolean isImplicit = isImplicit(cnode);
            if (isImplicit)
                writeNewline();
            else
                write(" ");
            write("else");
            if (!isImplicit)
                write(" ");

            getWalker().walk(elseNode); // TerminalNode
        }
    }

    @Override
    public void emitForEachLoop(IForLoopNode node)
    {
        IContainerNode xnode = (IContainerNode) node.getChild(1);
        writeToken("for");
        writeToken("each");
        write("(");

        IContainerNode cnode = node.getConditionalsContainerNode();
        getWalker().walk(cnode.getChild(0));

        write(")");
        if (!isImplicit(xnode))
            write(" ");

        getWalker().walk(node.getStatementContentsNode());
    }

    @Override
    public void emitForLoop(IForLoopNode node)
    {
        IContainerNode xnode = (IContainerNode) node.getChild(1);

        writeToken("for");
        write("(");

        IContainerNode cnode = node.getConditionalsContainerNode();
        final IASNode node0 = cnode.getChild(0);
        if (node0.getNodeID() == ASTNodeID.Op_InID)
        {
            getWalker().walk(cnode.getChild(0));
        }
        else
        {
            visitForBody(cnode);
        }

        write(")");
        if (!isImplicit(xnode))
            write(" ");

        getWalker().walk(node.getStatementContentsNode());
    }

    @Override
    public void emitSwitch(ISwitchNode node)
    {
        writeToken("switch");
        write("(");
        getWalker().walk(node.getChild(0));
        writeToken(")");
        writeNewline("{", true);

        IConditionalNode[] cnodes = ASNodeUtils.getCaseNodes(node);
        ITerminalNode dnode = ASNodeUtils.getDefaultNode(node);

        for (int i = 0; i < cnodes.length; i++)
        {
            IConditionalNode casen = cnodes[i];
            IContainerNode cnode = (IContainerNode) casen.getChild(1);
            writeToken("case");
            getWalker().walk(casen.getConditionalExpressionNode());
            write(":");
            if (!isImplicit(cnode))
                write(" ");
            getWalker().walk(casen.getStatementContentsNode());
            if (i == cnodes.length - 1 && dnode == null)
            {
                indentPop();
                writeNewline();
            }
            else
                writeNewline();
        }
        if (dnode != null)
        {
            IContainerNode cnode = (IContainerNode) dnode.getChild(0);
            write("default");
            write(":");
            if (!isImplicit(cnode))
                write(" ");
            getWalker().walk(dnode);
            indentPop();
            writeNewline();
        }
        write("}");
    }

    @Override
    public void emitWhileLoop(IWhileLoopNode node)
    {
        IContainerNode cnode = (IContainerNode) node.getChild(1);
        writeToken("while");
        write("(");
        getWalker().walk(node.getConditionalExpressionNode());
        write(")");
        if (!isImplicit(cnode))
            write(" ");
        getWalker().walk(node.getStatementContentsNode());
    }

    @Override
    public void emitDoLoop(IWhileLoopNode node)
    {
        IContainerNode cnode = (IContainerNode) node.getChild(0);
        write("do");
        if (!isImplicit(cnode))
            write(" ");
        getWalker().walk(node.getStatementContentsNode());
        if (!isImplicit(cnode))
            write(" ");
        else
            writeNewline(); // TODO (mschmalle) there is something wrong here, block should NL
        write("while");
        write(" ");
        write("(");
        getWalker().walk(node.getConditionalExpressionNode());
        write(")");
        write(";");
    }

    @Override
    public void emitWith(IWithNode node)
    {
        IContainerNode cnode = (IContainerNode) node.getChild(1);
        writeToken("with");
        write("(");
        getWalker().walk(node.getTargetNode());
        write(")");
        if (!isImplicit(cnode))
            write(" ");
        getWalker().walk(node.getStatementContentsNode());
    }

    @Override
    public void emitThrow(IThrowNode node)
    {
        writeToken("throw");
        getWalker().walk(node.getThrownExpressionNode());
    }

    @Override
    public void emitTry(ITryNode node)
    {
        writeToken("try");
        getWalker().walk(node.getStatementContentsNode());
        for (int i = 0; i < node.getCatchNodeCount(); i++)
        {
            getWalker().walk(node.getCatchNode(i));
        }
        ITerminalNode fnode = node.getFinallyNode();
        if (fnode != null)
        {
            write(" ");
            writeToken("finally");
            getWalker().walk(fnode);
        }
    }

    @Override
    public void emitCatch(ICatchNode node)
    {
        write(" ");
        writeToken("catch");
        write("(");
        getWalker().walk(node.getCatchParameterNode());
        writeToken(")");
        getWalker().walk(node.getStatementContentsNode());
    }

    @Override
    public void emitReturn(IReturnNode node)
    {
        write("return");
        IExpressionNode rnode = node.getReturnValueNode();
        if (rnode != null && rnode.getNodeID() != ASTNodeID.NilID)
        {
            write(" ");
            getWalker().walk(rnode);
        }
    }

    //--------------------------------------------------------------------------
    // Expressions
    //--------------------------------------------------------------------------

    @Override
    public void emitFunctionCall(IFunctionCallNode node)
    {
        if (node.isNewExpression())
        {
            writeToken("new");
        }

        getWalker().walk(node.getNameNode());

        write("(");
        walkArguments(node.getArgumentNodes());
        write(")");
    }

    //--------------------------------------------------------------------------
    // Operators
    //--------------------------------------------------------------------------

    @Override
    public void emitAsOperator(IBinaryOperatorNode node)
    {
        getWalker().walk(node.getLeftOperandNode());
        write(" ");
        writeToken(node.getOperator().getOperatorText());
        getWalker().walk(node.getRightOperandNode());
    }

    @Override
    public void emitIsOperator(IBinaryOperatorNode node)
    {
        getWalker().walk(node.getLeftOperandNode());
        write(" ");
        writeToken(node.getOperator().getOperatorText());
        getWalker().walk(node.getRightOperandNode());
    }

    @Override
    public void emitBinaryOperator(IBinaryOperatorNode node)
    {
        if (ASNodeUtils.hasParenOpen(node))
            write("(");
        getWalker().walk(node.getLeftOperandNode());
        if (node.getNodeID() != ASTNodeID.Op_CommaID)
            write(" ");
        writeToken(node.getOperator().getOperatorText());
        getWalker().walk(node.getRightOperandNode());
        if (ASNodeUtils.hasParenClose(node))
            write(")");
    }

    //--------------------------------------------------------------------------
    // Utility
    //--------------------------------------------------------------------------

    protected ITypeNode findTypeNode(IPackageNode node)
    {
        IScopedNode scope = node.getScopedNode();
        for (int i = 0; i < scope.getChildCount(); i++)
        {
            IASNode child = scope.getChild(i);
            if (child instanceof ITypeNode)
                return (ITypeNode) child;
        }
        return null;
    }

    protected ITypeDefinition findType(Collection<IDefinition> definitions)
    {
        for (IDefinition definition : definitions)
        {
            if (definition instanceof ITypeDefinition)
                return (ITypeDefinition) definition;
        }
        return null;
    }

    protected void walkArguments(IExpressionNode[] nodes)
    {
        int len = nodes.length;
        for (int i = 0; i < len; i++)
        {
            IExpressionNode node = nodes[i];
            getWalker().walk(node);
            if (i < len - 1)
            {
                writeToken(",");
            }
        }
    }

    //--------------------------------------------------------------------------
    // Static Utility
    //--------------------------------------------------------------------------

    protected static IFunctionNode getConstructor(IDefinitionNode[] members)
    {
        for (IDefinitionNode node : members)
        {
            if (node instanceof IFunctionNode)
            {
                IFunctionNode fnode = (IFunctionNode) node;
                if (fnode.isConstructor())
                    return fnode;
            }
        }
        return null;
    }

    protected static boolean isLastStatement(IASNode node)
    {
        return getChildIndex(node.getParent(), node) == node.getParent()
                .getChildCount() - 1;
    }

    // this is not fair that we have to do this if (i < len - 1)
    private static int getChildIndex(IASNode parent, IASNode node)
    {
        final int len = parent.getChildCount();
        for (int i = 0; i < len; i++)
        {
            if (parent.getChild(i) == node)
                return i;
        }
        return -1;
    }

    protected static final boolean isImplicit(IContainerNode node)
    {
        return node.getContainerType() == ContainerType.IMPLICIT
                || node.getContainerType() == ContainerType.SYNTHESIZED;
    }

    protected void visitForBody(IContainerNode node)
    {
        final IASNode node0 = node.getChild(0);
        final IASNode node1 = node.getChild(1);
        final IASNode node2 = node.getChild(2);

        // initializer
        if (node0 != null)
        {
            getWalker().walk(node0);
            write(";");
            if (node1.getNodeID() != ASTNodeID.NilID)
                write(" ");
        }
        // condition or target
        if (node1 != null)
        {
            getWalker().walk(node1);
            write(";");
            if (node2.getNodeID() != ASTNodeID.NilID)
                write(" ");
        }
        // iterator
        if (node2 != null)
        {
            getWalker().walk(node2);
        }
    }

    @Override
    public void emitLiteral(ILiteralNode node)
    {
        write(node.getValue(true));
    }

    @Override
    public void emitLiteralContainer(ILiteralContainerNode node)
    {
        final ContainerNode cnode = node.getContentsNode();
        final ContainerType type = cnode.getContainerType();
        String postFix = "";

        if (type == ContainerType.BRACES)
        {
            write("{");
            postFix = "}";
        }
        else if (type == ContainerType.BRACKETS)
        {
            write("[");
            postFix = "]";
        }
        else if (type == ContainerType.IMPLICIT)
        {
            // nothing to write, move along
        }
        else if (type == ContainerType.PARENTHESIS)
        {
            write("(");
            postFix = ")";
        }

        final int len = cnode.getChildCount();
        for (int i = 0; i < len; i++)
        {
            IASNode child = cnode.getChild(i);
            getWalker().walk(child);
            if (i < len - 1)
                writeToken(",");
        }

        if (postFix != "")
            write(postFix);
    }

    @Override
    public void emitIdentifier(IIdentifierNode node)
    {
        write(node.getName());
    }

    @Override
    public void emitNumericLiteral(INumericLiteralNode node)
    {
        write(node.getNumericValue().toString());
    }

    @Override
    public void emitKeyword(IKeywordNode node)
    {
        write(node.getNodeID().getParaphrase());
    }

    @Override
    public void emitIterationFlow(IIterationFlowNode node)
    {
        write(node.getKind().toString().toLowerCase());
        IIdentifierNode lnode = node.getLabelNode();
        if (lnode != null)
        {
            write(" ");
            getWalker().walk(lnode);
        }
    }

    @Override
    public void emitMemberAccessExpression(IMemberAccessExpressionNode node)
    {
        getWalker().walk(node.getLeftOperandNode());
        write(node.getOperator().getOperatorText());
        getWalker().walk(node.getRightOperandNode());
    }

    @Override
    public void emitDynamicAccess(IDynamicAccessNode node)
    {
        getWalker().walk(node.getLeftOperandNode());
        write("[");
        getWalker().walk(node.getRightOperandNode());
        write("]");
    }

    @Override
    public void emitTypedExpression(ITypedExpressionNode node)
    {
        getWalker().walk(node.getCollectionNode());
        write(".");
        write("<");
        getWalker().walk(node.getTypeNode());
        write(">");
    }

    @Override
    public void emitVariableExpression(IVariableExpressionNode node)
    {
        getWalker().walk(node.getTargetVariable());
    }

    @Override
    public void emitTernaryOperator(ITernaryOperatorNode node)
    {
        getWalker().walk(node.getConditionalNode());
        write(" ");
        writeToken("?");
        getWalker().walk(node.getLeftOperandNode());
        write(" ");
        writeToken(":");
        getWalker().walk(node.getRightOperandNode());
    }

    @Override
    public void emitObjectLiteralValuePair(IObjectLiteralValuePairNode node)
    {
        getWalker().walk(node.getNameNode());
        write(":");
        getWalker().walk(node.getValueNode());
    }

    @Override
    public void emitLabelStatement(LabeledStatementNode node)
    {
        writeToken(node.getLabel());
        writeToken(":");
        getWalker().walk(node.getLabeledStatement());
    }

    @Override
    public void emitNamespaceAccessExpression(NamespaceAccessExpressionNode node)
    {
        getWalker().walk(node.getLeftOperandNode());
        write(node.getOperator().getOperatorText());
        getWalker().walk(node.getRightOperandNode());
    }

    @Override
    public void emitUnaryOperator(IUnaryOperatorNode node)
    {
        if (node.getNodeID() == ASTNodeID.Op_PreIncrID
                || node.getNodeID() == ASTNodeID.Op_PreDecrID
                || node.getNodeID() == ASTNodeID.Op_BitwiseNotID
                || node.getNodeID() == ASTNodeID.Op_LogicalNotID
                || node.getNodeID() == ASTNodeID.Op_SubtractID
                || node.getNodeID() == ASTNodeID.Op_AddID)
        {
            write(node.getOperator().getOperatorText());
            getWalker().walk(node.getOperandNode());
        }

        else if (node.getNodeID() == ASTNodeID.Op_PostIncrID
                || node.getNodeID() == ASTNodeID.Op_PostDecrID)
        {
            getWalker().walk(node.getOperandNode());
            write(node.getOperator().getOperatorText());
        }
        else if (node.getNodeID() == ASTNodeID.Op_DeleteID
                || node.getNodeID() == ASTNodeID.Op_VoidID)
        {
            writeToken(node.getOperator().getOperatorText());
            getWalker().walk(node.getOperandNode());
        }
        else if (node.getNodeID() == ASTNodeID.Op_TypeOfID)
        {
            write(node.getOperator().getOperatorText());
            write("(");
            getWalker().walk(node.getOperandNode());
            write(")");
        }
    }

    @Override
    public void emitLanguageIdentifier(ILanguageIdentifierNode node)
    {
        if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.ANY_TYPE)
        {
            write("*");
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.REST)
        {
            write("...");
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.SUPER)
        {
            write("super");
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.THIS)
        {
            write("this");
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.VOID)
        {
            write("void");
        }
    }

    @Override
    public void emitMetaTag(IMetaTagNode node)
    {
    }

    @Override
    public String stringifyNode(IASNode node)
    {
        setBufferWrite(true);
        getWalker().walk(node);
        String result = getBuilder().toString();
        getBuilder().setLength(0);
        setBufferWrite(false);
        return result;
    }

    @Override
    public String toNodeString(IExpressionNode node)
    {
        return stringifyNode(node);
    }

}
