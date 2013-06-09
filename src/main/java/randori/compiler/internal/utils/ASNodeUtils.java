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

package randori.compiler.internal.utils;

import java.util.ArrayList;

import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IBlockNode;
import org.apache.flex.compiler.tree.as.IConditionalNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.ISwitchNode;
import org.apache.flex.compiler.tree.as.ITerminalNode;

/**
 * @author Michael Schmalle
 */
public class ASNodeUtils
{
    //--------------------------------------------------------------------------
    // Temp: These need JIRA tickets
    //--------------------------------------------------------------------------

    // there seems to be a bug in the ISwitchNode.getCaseNodes(), need to file a bug
    public static final IConditionalNode[] getCaseNodes(ISwitchNode node)
    {
        IBlockNode block = (IBlockNode) node.getChild(1);
        int childCount = block.getChildCount();
        ArrayList<IConditionalNode> retVal = new ArrayList<IConditionalNode>(
                childCount);

        for (int i = 0; i < childCount; i++)
        {
            IASNode child = block.getChild(i);
            if (child instanceof IConditionalNode)
                retVal.add((IConditionalNode) child);
        }

        return retVal.toArray(new IConditionalNode[0]);
    }

    // there seems to be a bug in the ISwitchNode.getDefaultNode(), need to file a bug
    public static final ITerminalNode getDefaultNode(ISwitchNode node)
    {
        IBlockNode block = (IBlockNode) node.getChild(1);
        int childCount = block.getChildCount();
        for (int i = childCount - 1; i >= 0; i--)
        {
            IASNode child = block.getChild(i);
            if (child instanceof ITerminalNode)
                return (ITerminalNode) child;
        }

        return null;
    }

    /**
     * Returns whether the node has explicit parenthesis parsed in the source
     * code.
     * 
     * @param node
     */
    public static boolean hasParenOpen(IExpressionNode node)
    {
        return node.hasParenthesis();
    }

    /**
     * Returns whether the node has explicit parenthesis parsed in the source
     * code.
     * 
     * @param node
     */
    public static boolean hasParenClose(IExpressionNode node)
    {
        return node.hasParenthesis();
    }

    public static boolean isString(IExpressionNode node)
    {
        return node.getNodeID() == ASTNodeID.LiteralStringID;
    }
}
