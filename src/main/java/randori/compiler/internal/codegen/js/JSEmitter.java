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

import org.apache.flex.compiler.internal.tree.as.FunctionNode;
import org.apache.flex.compiler.tree.as.IFunctionObjectNode;

import randori.compiler.codegen.js.IJSEmitter;
import randori.compiler.internal.codegen.as.ASEmitter;

/**
 * @author Michael Schmalle
 */
public class JSEmitter extends ASEmitter implements IJSEmitter
{

    public JSEmitter(FilterWriter out)
    {
        super(out);
    }

    @Override
    public void emitFunctionObject(IFunctionObjectNode node)
    {
        FunctionNode fnode = node.getFunctionNode();
        write("function");
        emitParamters(fnode.getParameterNodes());
        emitFunctionScope(fnode.getScopedNode());
    }

}
