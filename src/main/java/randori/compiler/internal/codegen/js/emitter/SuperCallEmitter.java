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

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.internal.definitions.ClassTraitsDefinition;
import org.apache.flex.compiler.internal.tree.as.ExpressionNodeBase;
import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.MetaDataUtils;

public class SuperCallEmitter extends BaseSubEmitter implements
        ISubEmitter<IFunctionCallNode>
{

    public SuperCallEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IFunctionCallNode node)
    {
        getWalker().walk(node.getNameNode());
        write("(");
        walkArguments(node);
        write(")");
    }

    protected void walkArguments(IFunctionCallNode node)
    {
        getModel().setInArguments(true);

        // the arguments are the expressions the function calls with
        IExpressionNode[] nodes = node.getArgumentNodes();

        // the parameters are the expressions the function is defined with

        int len = nodes.length;

        // only add 'this' to a constructor super() call, not a super.foo() call
        emitThisArgument(node, len > 0);

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
                //emitExtra(node);
            }
        }

        getModel().setInArguments(false);
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private void emitThisArgument(IFunctionCallNode node,
            boolean hasMoreArguments)
    {
        if (injectThisArgument(node, false))
        {
            write("this");
            if (hasMoreArguments)
                writeToken(",");
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
            getWalker().walk(node);
            write(")");
        }
        else
        {
            getWalker().walk(node);
        }
    }

    //--------------------------------------------------------------------------

    public static boolean injectThisArgument(IFunctionCallNode node,
            boolean allowNonConstructors)
    {
        // if super isSuper checks the nameNode
        if (node.isSuperExpression() && !node.isNewExpression())
            return true;

        ExpressionNodeBase base = (ExpressionNodeBase) node.getNameNode();
        if (base.getNodeID() == ASTNodeID.IdentifierID)
            return false;

        if (allowNonConstructors
                && base.getNodeID() == ASTNodeID.MemberAccessExpressionID)
        {
            //  super.foo()
            IMemberAccessExpressionNode mnode = (IMemberAccessExpressionNode) base;
            if (mnode.getLeftOperandNode().getNodeID() == ASTNodeID.SuperID)
                return true;
        }

        return false;
    }
}
