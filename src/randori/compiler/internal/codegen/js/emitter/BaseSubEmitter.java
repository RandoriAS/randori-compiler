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

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.projects.ICompilerProject;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISessionModel;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.visitor.as.IASBlockWalker;

/**
 * Base emitter for sub composites of the {@link IRandoriEmitter}.
 * <p>
 * This whole framework is prototype, needed a way to pull everything a part.
 * 
 * @author Michael Schmalle
 */
public abstract class BaseSubEmitter
{
    private final IRandoriEmitter emitter;

    public BaseSubEmitter(IRandoriEmitter emitter)
    {
        this.emitter = emitter;
    }

    public final IRandoriEmitter getEmitter()
    {
        return emitter;
    }

    protected final ICompilerProject getProject()
    {
        return emitter.getWalker().getProject();
    }

    protected final IASBlockWalker getWalker()
    {
        return (IASBlockWalker) emitter.getWalker();
    }

    protected final ISessionModel getModel()
    {
        return emitter.getModel();
    }

    //    protected void write(ASEmitterTokens value)
    //    {
    //        emitter.write(value);
    //    }

    protected void write(String value)
    {
        emitter.write(value);
    }

    //    protected void writeToken(ASEmitterTokens value)
    //    {
    //        emitter.writeToken(value);
    //    }

    protected void writeToken(String value)
    {
        emitter.writeToken(value);
    }

    protected void writeNewline()
    {
        emitter.writeNewline();
    }

    protected void writeNewline(String value)
    {
        emitter.writeNewline(value);
    }

    protected void writeNewline(String value, boolean pushIndent)
    {
        emitter.writeNewline(value, pushIndent);
    }

    protected void indentPush()
    {
        emitter.indentPush();
    }

    protected void indentPop()
    {
        emitter.indentPop();
    }

    protected void writeIfNotNative(String value, IDefinition definition)
    {
        if (!MetaDataUtils.isNative(definition))
            emitter.write(value);
    }

}
