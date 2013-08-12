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

package randori.compiler.internal.codegen.js;

import java.io.FilterWriter;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.internal.tree.as.ChainedVariableNode;
import org.apache.flex.compiler.internal.tree.as.FunctionNode;
import org.apache.flex.compiler.internal.tree.as.FunctionObjectNode;
import org.apache.flex.compiler.internal.tree.as.NamespaceAccessExpressionNode;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IContainerNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IDynamicAccessNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IForLoopNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IFunctionObjectNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode;
import org.apache.flex.compiler.tree.as.ILiteralNode;
import org.apache.flex.compiler.tree.as.ILiteralNode.LiteralType;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.tree.as.IParameterNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.tree.as.IUnaryOperatorNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISessionModel;
import randori.compiler.internal.codegen.js.emitter.BinaryOperatorEmitter;
import randori.compiler.internal.codegen.js.emitter.DynamicAccessEmitter;
import randori.compiler.internal.codegen.js.emitter.FieldEmitter;
import randori.compiler.internal.codegen.js.emitter.FooterEmitter;
import randori.compiler.internal.codegen.js.emitter.FunctionCallEmitter;
import randori.compiler.internal.codegen.js.emitter.FunctionFooterEmitter;
import randori.compiler.internal.codegen.js.emitter.HeaderEmitter;
import randori.compiler.internal.codegen.js.emitter.IdentifierEmitter;
import randori.compiler.internal.codegen.js.emitter.MemberAccessExpressionEmitter;
import randori.compiler.internal.codegen.js.emitter.MethodEmitter;
import randori.compiler.internal.codegen.js.emitter.SuperCallEmitter;
import randori.compiler.internal.utils.ASNodeUtils;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.MetaDataUtils.MetaData.Mode;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * The base ship...
 * 
 * @author Michael Schmalle
 */
public class RandoriEmitter extends JSEmitter implements IRandoriEmitter
{
    @Override
    public List<ICompilerProblem> getProblems()
    {
        return super.getProblems();
    }

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // model
    //----------------------------------

    private final ISessionModel model;

    @Override
    public final ISessionModel getModel()
    {
        return model;
    }

    //--------------------------------------------------------------------------
    // Emitters
    //--------------------------------------------------------------------------

    private IdentifierEmitter identifier;

    private MemberAccessExpressionEmitter memberAccessExpression;

    private DynamicAccessEmitter dynamicAccessEmitter;

    private BinaryOperatorEmitter binaryOperator;

    private MethodEmitter method;

    private FieldEmitter field;

    private FunctionCallEmitter functionCall;

    @SuppressWarnings("unused")
    private SuperCallEmitter superCall;

    private HeaderEmitter header;

    private FooterEmitter footer;

    private FunctionFooterEmitter functionFooter;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public RandoriEmitter(FilterWriter out)
    {
        super(out);

        model = new SessionModel();

        createEmitters();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates the sub emitters.
     */
    protected void createEmitters()
    {
        method = new MethodEmitter(this);
        field = new FieldEmitter(this);

        identifier = new IdentifierEmitter(this);
        memberAccessExpression = new MemberAccessExpressionEmitter(this);
        dynamicAccessEmitter = new DynamicAccessEmitter(this);
        binaryOperator = new BinaryOperatorEmitter(this);
        functionCall = new FunctionCallEmitter(this);
        superCall = new SuperCallEmitter(this);

        header = new HeaderEmitter(this);
        footer = new FooterEmitter(this);
        functionFooter = new FunctionFooterEmitter(this);
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void emitPackageHeader(IPackageDefinition definition)
    {
        // TODO (mschmalle) emit package render comments
    }

    @Override
    public void emitPackageHeaderContents(IPackageDefinition definition)
    {
        IPackageNode node = definition.getNode();
        ITypeNode tnode = DefinitionUtils.findTypeNode(node);
        if (tnode != null && !MetaDataUtils.isGlobal((IClassNode) tnode))
        {
            header.emit(definition);
        }
        IFunctionNode fnode = DefinitionUtils.findFunctionNode(node);
        if (fnode != null)
        {
            Mode mode = MetaDataUtils.getMode(fnode.getDefinition());
            if (mode != Mode.GLOBAL)
            {
                header.emit(definition);
            }
        }
    }

    @Override
    public void emitPackageContents(IPackageDefinition definition)
    {
        IPackageNode node = definition.getNode();
        ITypeNode tnode = DefinitionUtils.findTypeNode(node);
        if (tnode != null)
        {
            writeNewline();
            getWalker().walk(tnode); // IClassNode | IInterfaceNode
        }
        // try the function
        IFunctionNode fnode = DefinitionUtils.findFunctionNode(node);
        if (fnode != null)
        {
            writeNewline();
            getWalker().walk(fnode); // IFunctionNode
        }
    }

    @Override
    public void emitPackageFooter(IPackageDefinition definition)
    {
        IFunctionNode fnode = DefinitionUtils.findFunctionNode(definition
                .getNode());
        if (fnode != null)
        {
            Mode mode = MetaDataUtils.getMode(fnode.getDefinition());
            if (mode != Mode.GLOBAL)
            {
                functionFooter.emit(fnode);
            }
            return;
        }

        IClassNode node = (IClassNode) DefinitionUtils.findTypeNode(definition
                .getNode());
        if (node == null)
            return; // temp because of unit tests

        if (!MetaDataUtils.isGlobal(node))
        {
            footer.emit(node);
        }
    }

    @Override
    public void emitClass(IClassNode node)
    {
        forInitCounter = 0;
        forInitCounter = 1;
        
        // fields, methods
        final IDefinitionNode[] members = node.getAllMemberNodes();
        if (members.length > 0)
        {
            if (!MetaDataUtils.isGlobal(node))
            {
                IFunctionDefinition constructor = node.getDefinition()
                        .getConstructor();
                IFunctionNode cnode = (IFunctionNode) constructor.getNode();
                if (cnode != null)
                {
                    method.emit(cnode);
                }
                else
                {
                    method.emitConstructor(constructor);
                }

                writeNewline(";");
            }
        }

        if (members.length > 0)
        {
            writeNewline();

            final int len = members.length;
            int i = 0;
            for (IDefinitionNode member : members)
            {
                IDefinition definition = member.getDefinition();

                if (member.getNodeID() == ASTNodeID.FunctionID)
                {
                    if (((IFunctionDefinition) definition).isConstructor())
                        continue;
                }

                if (member.getNodeID() == ASTNodeID.VariableID)
                {
                    model.addInjection(definition);
                    model.addViewInjection(definition);

                    if (definition.isStatic()
                            || definition instanceof IConstantDefinition)
                    {
                        getWalker().walk(member);

                        write(";");
                        writeNewline();
                        writeNewline();
                    }
                }
                else if (member.getNodeID() == ASTNodeID.FunctionID)
                {
                    model.addInjection(definition);

                    getWalker().walk(member);

                    write(";");
                    writeNewline();
                    writeNewline();
                }
                else if (member.getNodeID() == ASTNodeID.GetterID
                        || member.getNodeID() == ASTNodeID.SetterID)
                {
                    model.addInjection(definition);
                    model.addViewInjection(definition);

                    getWalker().walk(member);

                    write(";");
                    if (i < len - 1)
                    {
                        writeNewline();
                        writeNewline();
                    }
                }
                i++;
            }
        }
    }

    @Override
    public void emitFunction(IFunctionNode node)
    {
        FunctionNode fnode = (FunctionNode) node;
        fnode.parseFunctionBody(problems);

        Mode mode = MetaDataUtils.getMode(fnode.getDefinition());
        if (mode == Mode.GLOBAL)
        {
            IScopedNode scopedNode = node.getScopedNode();
            final int len = scopedNode.getChildCount();
            for (int i = 0; i < len; i++)
            {
                getWalker().walk(scopedNode.getChild(i));
                write(";");
                writeNewline();
            }
            return;
        }

        IPackageNode pnode = (IPackageNode) fnode.getParent().getParent();
        String packageName = pnode.getName();
        if (packageName != null && !packageName.isEmpty())
        {
            write(packageName);
            write(".");
        }
        write(fnode.getName());
        write(" = function");
        emitParamters(node.getParameterNodes());
        emitFunctionScope(node.getScopedNode());
    }

    @Override
    public void emitField(IVariableNode node)
    {
        field.emit(node);
    }

    @Override
    public void emitFunctionBlockHeader(IFunctionNode node)
    {
        method.emitHeader(node);
    }

    @Override
    public void emitMethod(IFunctionNode node)
    {
        method.emit(node);
    }

    @Override
    protected void emitMethodScope(IScopedNode node)
    {
        getModel().setInScope(true);
        write(" ");
        getWalker().walk(node);
        getModel().setInScope(false);
    }

    @Override
    public void emitFunctionObject(IFunctionObjectNode node)
    {
        FunctionObjectNode f = (FunctionObjectNode) node;
        FunctionNode fnode = f.getFunctionNode();
        //write(IRandoriEmitter.ANON_DELEGATE_NAME);
        //write("(");
        //write("this, ");
        write("function");
        emitParamters(fnode.getParameterNodes());
        emitType(fnode.getTypeNode());
        emitFunctionScope(fnode.getScopedNode());
        //write(")");
    }

    @Override
    public void emitFunctionCall(IFunctionCallNode node)
    {
        IExpressionNode nameNode = node.getNameNode();
        @SuppressWarnings("unused")
        IExpressionNode left = null;
        if (nameNode.getNodeID() == ASTNodeID.MemberAccessExpressionID)
        {
            left = ((IMemberAccessExpressionNode) nameNode)
                    .getLeftOperandNode();
        }

        //if (node.isCallToSuper() || ExpressionUtils.isSuperExpression(left))
        //{
        //    superCall.emit(node);
        //}
        //else
        //{
        functionCall.emit(node);
        //}
    }

    @Override
    public void emitParameter(IParameterNode node)
    {
        getWalker().walk(node.getNameExpressionNode());
    }

    @Override
    protected void walkArguments(IExpressionNode[] nodes)
    {
    }

    private int forInitCounter = 0;

    private int forCounter = 1;

    @Override
    public void emitForEachLoop(IForLoopNode node)
    {
        IContainerNode conditionalNode = node.getConditionalsContainerNode();
        IContainerNode containerNode = (IContainerNode) node.getChild(1);

        final int value = forInitCounter++;
        final int name = forCounter++;

        writeNewline("var $" + name + ";");
        writeToken("for");
        write("(var $" + value);
        IBinaryOperatorNode bnode = (IBinaryOperatorNode) conditionalNode
                .getChild(0);
        write(" in ($" + name + " = ");
        getWalker().walk(bnode.getRightOperandNode());
        write("))");

        if (!isImplicit(containerNode))
            write(" ");

        final int len = node.getStatementContentsNode().getChildCount();
        if (len > 0)
            writeNewline("{", true);
        else
            writeNewline("{");

        for (int i = 0; i < len; i++)
        {
            if (i == 0)
            {
                getWalker().walk(bnode.getLeftOperandNode());
                writeNewline(" = $" + name + "[$" + value + "];");
            }

            getWalker().walk(node.getStatementContentsNode().getChild(i));
            if (i < len - 1)
                writeNewline();
        }

        if (len > 0)
        {
            indentPop();
            writeNewline();
        }

        write("}");
    }

    @Override
    public void emitAsOperator(IBinaryOperatorNode node)
    {
        getWalker().walk(node.getLeftOperandNode());
    }

    @Override
    public void emitIsOperator(IBinaryOperatorNode node)
    {
        if (ASNodeUtils.hasParenOpen(node))
            write("(");
        getWalker().walk(node.getLeftOperandNode());
        write(" instanceof ");
        IDefinition definition = node.getRightOperandNode().resolve(
                getWalker().getProject());
        if (definition instanceof IClassDefinition)
        {
            write(MetaDataUtils.getExportQualifiedName(definition));
        }
        else
        {
            getWalker().walk(node.getRightOperandNode());
        }
        if (ASNodeUtils.hasParenClose(node))
            write(")");
    }

    @Override
    public void emitBinaryOperator(IBinaryOperatorNode node)
    {
        binaryOperator.emit(node);
    }

    @Override
    public void emitMemberAccessExpression(IMemberAccessExpressionNode node)
    {
        memberAccessExpression.emit(node);
    }

    @Override
    public void emitDynamicAccess(IDynamicAccessNode node)
    {
        dynamicAccessEmitter.emit(node);
    }

    @Override
    public void emitIdentifier(IIdentifierNode node)
    {
        identifier.emit(node);
    }

    @Override
    public void emitNamespaceAccessExpression(NamespaceAccessExpressionNode node)
    {
        //getWalker().walk(node.getLeftOperandNode());
        //write(node.getOperator().getOperatorText());
        getWalker().walk(node.getRightOperandNode());
    }

    @Override
    protected void emitType(IExpressionNode node)
    {
    }

    @Override
    public void emitLanguageIdentifier(ILanguageIdentifierNode node)
    {
        if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.ANY_TYPE)
        {
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.REST)
        {
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.SUPER)
        {
            IIdentifierNode inode = (IIdentifierNode) node;
            if (inode.getParent() instanceof IMemberAccessExpressionNode)
            {
                IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) inode
                        .getParent();
                IDefinition rightDef = mnode.getRightOperandNode().resolve(
                        getWalker().getProject());
                // takes care of super.get_foo() accessor
                if (rightDef instanceof IAccessorDefinition)
                {
                    String qualifiedName = RandoriUtils
                            .toSuperAccessQualifiedName(inode, getWalker()
                                    .getProject());
                    write(qualifiedName + ".prototype");
                    getModel().setCall(true);
                }
            }
            else
            {
                // takes care of super.foo() function call
                String qualifiedName = RandoriUtils.toSuperAccessQualifiedName(
                        inode, getWalker().getProject());
                write(qualifiedName + ".call");
            }
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.THIS)
        {
            IIdentifierNode inode = (IIdentifierNode) node;
            if (!(inode.getParent() instanceof IMemberAccessExpressionNode))
                write("this");
        }
        else if (node.getKind() == ILanguageIdentifierNode.LanguageIdentifierKind.VOID)
        {
        }
    }

    @Override
    public void emitLiteral(ILiteralNode node)
    {
        String value = node.getValue(false);
        if (node.getLiteralType() == LiteralType.STRING)
        {
            value = "\"" + StringEscapeUtils.escapeEcmaScript(value) + "\"";
        }
        else if (node.getLiteralType() == LiteralType.REGEXP)
        {
            value = node.getValue(true);
        }
        write(value);
    }

    @Override
    public String toNodeString(IExpressionNode node)
    {
        return stringifyNode(node);
    }

    @Override
    public void emitParamters(IFunctionNode node)
    {
        emitParamters(node.getParameterNodes());
    }

    @Override
    public void emitMethodScope(IFunctionNode node)
    {
        emitMethodScope(node.getScopedNode());
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

        else if (node.getNodeID() == ASTNodeID.Op_PostIncrID)
        {
            emitPostAssignment(node, "+");
        }
        else if (node.getNodeID() == ASTNodeID.Op_PostDecrID)
        {
            emitPostAssignment(node, "-");
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

    private void emitPostAssignment(IUnaryOperatorNode node, String operator)
    {
        IDefinition definition = node.getOperandNode().resolve(
                getWalker().getProject());
        if (definition instanceof IAccessorDefinition)
        {
            String name = definition.getBaseName();
            IExpressionNode operandNode = node.getOperandNode();
            String prefix = "this";
            if (operandNode.getNodeID() == ASTNodeID.MemberAccessExpressionID)
            {
                prefix = stringifyNode(((IMemberAccessExpressionNode) operandNode)
                        .getLeftOperandNode());
            }

            write(prefix + ".set_" + name + "(");
            write(prefix + ".get_" + name + "() " + operator + " 1)");
        }
        else
        {
            getWalker().walk(node.getOperandNode());
            write(node.getOperator().getOperatorText());
        }
    }

    @Override
    public void emitVarDeclaration(IVariableNode node)
    {
        if (!(node instanceof ChainedVariableNode))
        {
            writeToken("var");
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

    @Override
    public String toQualifiedNameFromType(ITypeNode node)
    {
        //IClassNode typeNode = (IClassNode) DefinitionUtils
        //        .findParentTypeNode(definition.getNode().getParent());
        String qualifiedName = DefinitionUtils.toBaseClassQualifiedName(
                (ITypeDefinition) node.getDefinition(), getWalker()
                        .getProject());
        return qualifiedName;
    }
}
