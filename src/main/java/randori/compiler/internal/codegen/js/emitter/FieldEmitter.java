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

import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.tree.as.IEmbedNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * Handles the production of the {@link IVariableNode} method and it's
 * associated header block.
 * 
 * @author Michael Schmalle
 */
public class FieldEmitter extends BaseSubEmitter implements
        ISubEmitter<IVariableNode>
{

    public FieldEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IVariableNode node)
    {
        IVariableDefinition definition = (IVariableDefinition) node
                .getDefinition();

        String prefix = RandoriUtils.toFieldPrefix(definition, getWalker()
                .getProject());
        write(prefix);
        emitAssignedValue(node.getAssignedValueNode());
    }

    private void emitAssignedValue(IExpressionNode node)
    {
        if (node instanceof IEmbedNode)
            return; // TODO what is Embed node going to do
        
        if (node == null)
            return;

        write(" ");
        write("=");
        write(" ");
        getWalker().walk(node);
    }

}
