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

import randori.compiler.internal.js.codegen.ResourceTestBase;

/**
 * @author Michael Schmalle
 */
public class InjectTest extends ResourceTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        assertOut("if (typeof demo == \"undefined\")\n\tvar demo = {};\nif (typeof "
                + "demo.inject == \"undefined\")\n\tdemo.inject = {};\n\ndemo.inject."
                + "InjectTest = function(param1, param2) {\n\tthis.injectField = null;"
                + "\n\tthis.viewField = null;\n\tif (arguments.length < 2) {\n\t\tparam2 = "
                + "1;\n\t}\n\tvar div = document.createElement('div');\n\tvar a = new demo."
                + "foo.ClassA();\n\tvar b = new guice.reflection.TypeDefinition();\n};\n\n"
                + "demo.inject.InjectTest.prototype.get_injectAccessor = function() {\n\treturn "
                + "null;\n};\n\ndemo.inject.InjectTest.prototype.set_injectAccessor = function"
                + "(value) {\n};\n\ndemo.inject.InjectTest.prototype.injectMethod = function"
                + "(param1, param2) {\n\tif (arguments.length < 2) {\n\t\tparam2 = \"foo\";"
                + "\n\t}\n};\n\ndemo.inject.InjectTest.className = \"demo.inject.InjectTest\""
                + ";\n\ndemo.inject.InjectTest.getClassDependencies = function(t) {\n\tvar "
                + "p;\n\tp = [];\n\tp.push('guice.reflection.TypeDefinition');\n\tp.push('"
                + "demo.foo.ClassA');\n\treturn p;\n};\n\ndemo.inject.InjectTest.injectionPoints"
                + " = function(t) {\n\tvar p;\n\tswitch (t) {\n\t\tcase 0:\n\t\t\tp = [];"
                + "\n\t\t\tp.push({n:'param1', t:'String'});\n\t\t\tp.push({n:'param2', "
                + "t:'int'});\n\t\t\tbreak;\n\t\tcase 1:\n\t\t\tp = [];\n\t\t\tp.push("
                + "{n:'injectField', t:'demo.foo.ClassA', r:0, v:null});\n\t\t\tp.push({n:"
                + "'injectAccessor', r:0, v:null});\n\t\t\tbreak;\n\t\tcase 2:\n\t\t\tp = "
                + "[];\n\t\t\tp.push({n:'injectMethod', p:[{n:'param1', t:'demo.foo.ClassB'},"
                + " {n:'param2'}]});\n\t\t\tbreak;\n\t\tcase 3:\n\t\t\tp = [];\n\t\t\tp.push("
                + "{n:'viewField'});\n\t\t\tbreak;\n\t\tdefault:\n\t\t\tp = [];\n\t\t\tbreak;"
                + "\n\t}\n\treturn p;\n};\n\n");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.inject.InjectTest";
    }
}
