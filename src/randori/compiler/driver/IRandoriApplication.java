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

package randori.compiler.driver;

import java.util.Collection;

import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.clients.Randori;

/**
 * The {@link IRandoriApplication} API defines a model contract between the
 * {@link IRandoriTarget} and the {@link Randori} front end compiler.
 * 
 * @author Michael Schmalle
 */
public interface IRandoriApplication
{
    /**
     * The main output compilation method for the compiler.
     * <p>
     * All {@link ICompilationUnit}s have been parsed and are ready to be sorted
     * and generated.
     * 
     * @param backend The {@link IRandoriBackend} used to create instances of
     * writers, emitters, visitors etc for the code generation.
     * @param problems The current {@link ProblemQuery} for the compile session.
     * @return A {@link Boolean} indicating whether the compilation of all the
     * units was successful.
     */
    boolean compile(IRandoriBackend backend, ProblemQuery problems);
    
    void export(Collection<ICompilerProblem> problems);
}
