package randori.compiler.internal.codegen.js.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

import org.apache.flex.compiler.codegen.as.IASEmitter;
import org.apache.flex.compiler.constants.IASKeywordConstants;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IParameterNode;

import randori.compiler.internal.utils.DefinitionUtils;

/**
 * @author Michael Schmalle
 */
public class GenericEmitUtils
{
    public static final void emitDefaultParameterCodeBlock(IFunctionNode node,
            IASEmitter emitter)
    {
        // TODO (mschmalle) test for ... rest 
        // if default parameters exist, produce the init code
        IParameterNode[] pnodes = node.getParameterNodes();
        Map<Integer, IParameterNode> defaults = DefinitionUtils
                .getDefaults(pnodes);

        if (pnodes.length == 0)
            return;

        if (defaults != null)
        {
            boolean hasBody = node.getScopedNode().getChildCount() > 0;

            if (!hasBody)
            {
                emitter.indentPush();
                emitter.write("\t");
            }

            final StringBuilder code = new StringBuilder();

            List<IParameterNode> parameters = new ArrayList<IParameterNode>(
                    defaults.values());
            Collections.reverse(parameters);

            int len = defaults.size();
            // make the header in reverse order
            for (IParameterNode pnode : parameters)
            {
                if (pnode != null)
                {
                    code.setLength(0);
                    code.append(IASKeywordConstants.IF);
                    code.append(" (arguments.length < ");
                    code.append(len);
                    code.append(") {");

                    emitter.write(code.toString());

                    emitter.indentPush();
                    emitter.writeNewline();
                }
                len--;
            }

            Collections.reverse(parameters);
            for (int i = 0, n = parameters.size(); i < n; i++)
            {
                IParameterNode pnode = parameters.get(i);

                if (pnode != null)
                {
                    code.setLength(0);

                    code.append(pnode.getName());
                    code.append(" = ");
                    code.append(pnode.getDefaultValue());
                    code.append(";");
                    emitter.write(code.toString());

                    emitter.indentPop();
                    emitter.writeNewline();

                    emitter.write("}");

                    if (i == n - 1 && !hasBody)
                        emitter.indentPop();

                    emitter.writeNewline();
                }
            }
        }
    }
}
