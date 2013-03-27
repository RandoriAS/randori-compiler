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

package randori.compiler.internal.driver;

import java.io.FilterWriter;
import java.util.List;

import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.internal.driver.as.ASBackend;
import org.apache.flex.compiler.internal.projects.CompilerProject;
import org.apache.flex.compiler.internal.projects.ISourceFileHandler;
import org.apache.flex.compiler.internal.targets.RandoriTarget;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.targets.ITargetProgressMonitor;
import org.apache.flex.compiler.targets.ITargetSettings;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.codegen.js.RandoriEmitter;
import randori.compiler.internal.codegen.js.RandoriWriter;
import randori.compiler.internal.config.RandoriConfiguration;
import randori.compiler.internal.config.RandoriConfigurator;

/**
 * The backend for the {@link IRandoriEmitter}.
 * 
 * @author Michael Schmalle
 */
public class RandoriBackend extends ASBackend implements IRandoriBackend
{

    private boolean parseOnly;

    @Override
    public String getOutputExtension()
    {
        return "js";
    }

    @Override
    public ISourceFileHandler getSourceFileHandlerInstance()
    {
        return RandoriSourceFileHandler.INSTANCE;
    }

    @Override
    public Configurator createConfigurator()
    {
        return new RandoriConfigurator(RandoriConfiguration.class);
    }

    @Override
    public IRandoriTarget createTarget(IASProject project,
            ITargetSettings settings, ITargetProgressMonitor monitor)
    {
        return new RandoriTarget((CompilerProject) project, settings, monitor);
    }

    @Override
    public RandoriWriter createWriter(IASProject project,
            List<ICompilerProblem> problems, ICompilationUnit compilationUnit,
            boolean enableDebug)
    {
        return new RandoriWriter(this, project, problems, compilationUnit,
                enableDebug);
    }

    @Override
    public IRandoriEmitter createEmitter(FilterWriter out)
    {
        IRandoriEmitter emitter = new RandoriEmitter(out);
        emitter.setDocEmitter(createDocEmitter(emitter));
        return emitter;
    }

    public boolean isParseOnly()
    {
        return parseOnly;
    }

    public void parseOnly(boolean value)
    {
        parseOnly = value;
    }
}
