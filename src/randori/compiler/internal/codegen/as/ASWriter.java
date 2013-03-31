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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.codegen.as.IASWriter;

/**
 * @author Michael Schmalle
 */
public class ASWriter implements IASWriter
{
    @SuppressWarnings("unused")
    private IASProject project;

    @SuppressWarnings("unused")
    private List<ICompilerProblem> problems;

    @SuppressWarnings("unused")
    private ICompilationUnit compilationUnit;

    @SuppressWarnings("unused")
    private boolean enableDebug;

    /**
     * Create a ASWriter writer.
     * 
     * @param project
     * @param problems
     * @param compilationUnit
     * @param enableDebug
     */
    public ASWriter(IASProject project, List<ICompilerProblem> problems,
            ICompilationUnit compilationUnit, boolean enableDebug)
    {
        this.project = project;
        this.problems = problems;
        this.compilationUnit = compilationUnit;
        this.enableDebug = enableDebug;
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void writeTo(OutputStream out)
    {
    }

    @Override
    public int writeTo(File out) throws FileNotFoundException, IOException
    {
        return 0;
    }

}
