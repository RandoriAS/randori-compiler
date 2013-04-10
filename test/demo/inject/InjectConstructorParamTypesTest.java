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

package demo.inject;

import org.junit.Test;

import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.js.codegen.ResourceTestBase;

/**
 * @author Michael Schmalle
 */
public class InjectConstructorParamTypesTest extends ResourceTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        assertOut("if (typeof demo == \"undefined\")\n\tvar demo = {};\nif (typeof demo.inject "
                + "== \"undefined\")\n\tdemo.inject = {};\n\ndemo.inject.InjectConstructorParamTypes"
                + " = function(p1, p2, p3) {\n};\n\ndemo.inject.InjectConstructorParamTypes.className"
                + " = \"demo.inject.InjectConstructorParamTypes\";\n\ndemo.inject."
                + "InjectConstructorParamTypes.getClassDependencies = function(t) {\n\tvar "
                + "p;\n\treturn [];\n};\n\ndemo.inject.InjectConstructorParamTypes.injectionPoints "
                + "= function(t) {\n\tvar p;\n\tswitch (t) {\n\t\tcase 0:\n\t\t\tp = [];\n\t\t\tp."
                + "push({n:'p1', t:'Array'});\n\t\t\tp.push({n:'p2', t:'uint'});\n\t\t\tp.push("
                + "{n:'p3', t:'Object'});\n\t\t\tbreak;\n\t\tdefault:\n\t\t\tp = [];\n\t\t\tbreak;"
                + "\n\t}\n\treturn p;\n};\n\n");
    }

    @Override
    protected String getBasePath()
    {
        return TestConstants.RandoriASFramework
                + "/randori-compiler/test/resources";
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.inject.InjectConstructorParamTypes";
    }
}
