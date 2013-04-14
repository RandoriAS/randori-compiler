/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal.config;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.junit.Test;

import randori.compiler.config.IAnnotationDefinition;
import randori.compiler.internal.utils.DefinitionUtils;

/**
 * @author Michael Schmalle
 */
public class JavaScriptAnnotationTest extends AnnotationTestBase
{
    @Override
    protected void compile()
    {
        getArgs().addSourcepath(srcAnnotationCore.getAbsolutePath());
        getArgs().addIncludedSources(srcAnnotationCore.getAbsolutePath());
        getArgs().addSourcepath(srcAnnotation.getAbsolutePath());
        project.configure(getArgs().toArguments());
        @SuppressWarnings("unused")
        boolean success = project.compile(false);
        //assertSuccess(success);
    }

    @Test
    public void test_JavaScriptAnnotated()
    {
        includeClass("classes.javascript.JavaScriptAnnotated");
        compile();
        IAnnotationDefinition definition = project.getAnnotationManager()
                .getDefinition("randori.annotations.JavaScript");
        Set<ICompilationUnit> set = project.getScope()
                .getCompilationUnitsByDefinitionName("JavaScriptAnnotated");
        ICompilationUnit[] units = set.toArray(new ICompilationUnit[] {});

        IClassDefinition cdef = DefinitionUtils.getClassDefinition(units[0]);

        assertTrue(definition.isValidTarget(cdef));
        assertTrue(definition.isValidTarget("class"));
    }

}
