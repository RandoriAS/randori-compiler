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

import org.apache.flex.compiler.definitions.IPackageDefinition;

import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.codegen.js.RandoriEmitter;

/**
 * Handles the production of the specialized header Randori requires for package
 * scope.
 * 
 * @author Michael Schmalle
 */
public class HeaderEmitter extends BaseSubEmitter implements
        ISubEmitter<IPackageDefinition>
{

    public HeaderEmitter(RandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IPackageDefinition definition)
    {
        String qualifiedName = definition.getQualifiedName();
        String[] split = qualifiedName.split("\\.");
        final int len = split.length;
        if (len == 1 && split[0].trim().length() == 0)
            return;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++)
        {
            if (i != 0 && i < len)
                sb.append(".");
            sb.append(split[i]);
            write("if (typeof " + sb.toString() + " == \"undefined\")");
            indentPush();
            writeNewline();
            if (i == 0)
                write("var ");
            write(sb.toString() + " = {};");
            indentPop();
            writeNewline();
        }
    }

}
