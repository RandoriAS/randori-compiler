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

package randori.compiler.internal.driver.as;

import java.io.FilterWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.internal.projects.ISourceFileHandler;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.targets.ITarget;
import org.apache.flex.compiler.targets.ITargetProgressMonitor;
import org.apache.flex.compiler.targets.ITargetSettings;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.codegen.as.IASEmitter;
import randori.compiler.codegen.as.IASWriter;
import randori.compiler.driver.IBackend;
import randori.compiler.internal.codegen.as.ASAfterNodeStrategy;
import randori.compiler.internal.codegen.as.ASBeforeNodeStrategy;
import randori.compiler.internal.codegen.as.ASBlockWalker;
import randori.compiler.internal.codegen.as.ASEmitter;
import randori.compiler.internal.codegen.as.ASFilterWriter;
import randori.compiler.internal.codegen.as.ASWriter;
import randori.compiler.internal.visitor.as.ASNodeSwitch;
import randori.compiler.internal.visitor.as.BeforeAfterStrategy;
import randori.compiler.visitor.as.IASBlockWalker;

/**
 * A concrete implementation of the {@link IBackend} API where the
 * {@link ASBlockWalker} is used to traverse the {@link IFileNode} AST.
 * 
 * @author Michael Schmalle
 */
public class ASBackend implements IBackend
{
    @Override
    public String getOutputExtension()
    {
        return "as";
    }

    @Override
    public ISourceFileHandler getSourceFileHandlerInstance()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Configurator createConfigurator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ITarget createTarget(IASProject project, ITargetSettings settings,
            ITargetProgressMonitor monitor)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public IASBlockWalker createWalker(IASProject project,
            List<ICompilerProblem> errors, IASEmitter emitter)
    {
        ASBlockWalker walker = new ASBlockWalker(errors, project, emitter);

        BeforeAfterStrategy strategy = new BeforeAfterStrategy(
                new ASNodeSwitch(walker), new ASBeforeNodeStrategy(emitter),
                new ASAfterNodeStrategy(emitter));

        walker.setStrategy(strategy);

        return walker;
    }

    @Override
    public ASFilterWriter createWriterBuffer(IASProject project)
    {
        StringWriter out = new StringWriter();
        ASFilterWriter writer = new ASFilterWriter(out);
        return writer;
    }

    @Override
    public IASEmitter createEmitter(FilterWriter writer)
    {
        return new ASEmitter(writer);
    }

    @Override
    public IASWriter createWriter(IASProject project,
            List<ICompilerProblem> problems, ICompilationUnit compilationUnit,
            boolean enableDebug)
    {
        return new ASWriter(project, problems, compilationUnit, enableDebug);
    }

}
