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
import org.apache.flex.compiler.internal.projects.ISourceFileHandler;
import org.apache.flex.compiler.units.ICompilationUnit;

/**
 * @author Michael Schmalle
 */
public class RandoriSourceFileHandler implements ISourceFileHandler
{
    public static final String EXTENSION = "as";

    public static final RandoriSourceFileHandler INSTANCE = new RandoriSourceFileHandler();

    private RandoriSourceFileHandler()
    {
    }

    @Override
    public String[] getExtensions()
    {
        return new String[] { EXTENSION };
    }

    @Override
    public ICompilationUnit createCompilationUnit(CompilerProject project,
            String path, DefinitionPriority.BasePriority basePriority,
            int order, String qualifiedName, String locale)
    {
        return new RandoriCompilationUnit(project, path, basePriority,
                qualifiedName);
    }

    @Override
    public boolean needCompilationUnit(CompilerProject project, String path,
            String qname, String locale)
    {
        return true;
    }

    @Override
    public boolean canCreateInvisibleCompilationUnit()
    {
        return false;
    }
}
