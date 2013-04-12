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

import org.apache.flex.compiler.internal.projects.CompilerProject;
import org.apache.flex.compiler.internal.projects.DefinitionPriority;
import org.apache.flex.compiler.internal.projects.DefinitionPriority.BasePriority;
import org.apache.flex.compiler.internal.units.ASCompilationUnit;
import org.apache.flex.compiler.targets.ITarget.TargetType;

/**
 * RandoriCompilationUnit is the CompilationUnit for compiling ActionScript
 * source files to JavasScript.
 * <p>
 * RandoriCompilationUnit is derived from ASCompilationUnit and overrides the
 * parts that generate the code.
 */
public class RandoriCompilationUnit extends ASCompilationUnit
{

    public RandoriCompilationUnit(CompilerProject project, String path,
            BasePriority basePriority)
    {
        super(project, path, DefinitionPriority.BasePriority.LIBRARY_PATH);
    }

    public RandoriCompilationUnit(CompilerProject project, String path,
            BasePriority basePriority, int order)
    {
        super(project, path, basePriority, order);
    }

    public RandoriCompilationUnit(CompilerProject project, String path,
            BasePriority basePriority, int order, String qname)
    {
        super(project, path, basePriority, order, qname);
    }

    public RandoriCompilationUnit(CompilerProject project, String path,
            BasePriority basePriority, String qualifiedName)
    {
        super(project, path, basePriority, 0, qualifiedName);
    }

    @Override
    public void startBuildAsync(TargetType targetType)
    {
        getSyntaxTreeRequest();
        getFileScopeRequest();
        getOutgoingDependenciesRequest();
    }
}
