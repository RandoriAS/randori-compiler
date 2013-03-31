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

import org.apache.flex.compiler.tree.ASTNodeID;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IContainerNode;
import org.apache.flex.compiler.tree.as.IContainerNode.ContainerType;

import randori.compiler.codegen.IEmitter;
import randori.compiler.codegen.as.IASEmitter;
import randori.compiler.visitor.as.IASNodeStrategy;

/**
 * A concrete implementation of the {@link IASNodeStrategy} that allows
 * {@link IASNode} processing before the current node handler.
 * 
 * @author Michael Schmalle
 */
public class ASBeforeNodeStrategy implements IASNodeStrategy
{
    private final IASEmitter emitter;

    public ASBeforeNodeStrategy(IASEmitter emitter)
    {
        this.emitter = emitter;
    }

    @Override
    public void handle(IASNode node)
    {
        if (node.getNodeID() == ASTNodeID.BlockID)
        {
            IASNode parent = node.getParent();
            IContainerNode container = (IContainerNode) node;
            ContainerType type = container.getContainerType();

            if (parent.getNodeID() != ASTNodeID.LabledStatementID)
            {
                if (node.getChildCount() != 0)
                    emitter.indentPush();
            }

            // switch cases are SYNTHESIZED
            if (type != ContainerType.IMPLICIT
                    && type != ContainerType.SYNTHESIZED)
            {
                ((IEmitter) emitter).write("{");
            }

            if (parent.getNodeID() != ASTNodeID.LabledStatementID)
            {
                ((IEmitter) emitter).writeNewline();
            }
        }
    }

}
