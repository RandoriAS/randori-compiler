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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.flex.compiler.codegen.as.IASEmitter;
import org.apache.flex.compiler.codegen.js.IJSWriter;
import org.apache.flex.compiler.driver.IBackend;
import org.apache.flex.compiler.internal.codegen.as.ASFilterWriter;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.visitor.as.IASBlockWalker;

import randori.compiler.common.VersionInfo;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * @author Michael Schmalle
 */
public class RandoriWriter implements IJSWriter
{

    private IASProject project;

    private List<ICompilerProblem> problems;

    private ICompilationUnit compilationUnit;

    @SuppressWarnings("unused")
    private boolean enableDebug;

    private final IBackend backend;

    /**
     * Create a RandoriApplication writer.
     * 
     * @param backend
     * 
     * @param application the RandoriApplication model to be encoded
     */
    public RandoriWriter(IBackend backend, IASProject project,
            List<ICompilerProblem> problems, ICompilationUnit compilationUnit,
            boolean enableDebug)
    {
        this.backend = backend;
        this.project = project;
        this.problems = problems;
        this.compilationUnit = compilationUnit;
        this.enableDebug = enableDebug;
    }

    @Override
    public void writeTo(OutputStream out)
    {
        ASFilterWriter writer = backend.createWriterBuffer(project);
        IASEmitter emitter = backend.createEmitter(writer);
        IASBlockWalker visitor = backend.createWalker(project, problems,
                emitter);

        visitor.visitCompilationUnit(compilationUnit);

        List<ICompilerProblem> eproblems = RandoriUtils.getProblems(emitter);
        if (eproblems.size() > 0)
        {
            problems.addAll(eproblems);
            // XXX throw error or return, need to write out all at once, so nothing gets written
            // if there are problems
            throw new RuntimeException("Problems during build");
        }

        String result = writer.toString();

        result = "/** Compiled by the Randori compiler v"
                + VersionInfo.getCompilerVersion() + " on "
                + new Date().toString() + " */\n\n" + result;

        try
        {
            out.write(result.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int writeTo(File out) throws FileNotFoundException, IOException
    {
        return 0;
    }

    @Override
    public void close() throws IOException
    {
    }

    public String getString()
    {
        ASFilterWriter writer = backend.createWriterBuffer(project);
        IASEmitter emitter = backend.createEmitter(writer);
        IASBlockWalker visitor = backend.createWalker(project, problems,
                emitter);

        visitor.visitCompilationUnit(compilationUnit);

        return writer.toString();
    }

}
