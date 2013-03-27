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
public class EmptyInheritTest extends ResourceTestBase
{
    @Test
    public void test_file()
    {
        asBlockWalker.visitFile(fileNode);
        assertOut("if (typeof demo == \"undefined\")\n\tvar demo = {};\nif ("
                + "typeof demo.inject == \"undefined\")\n\tdemo.inject = {};\n"
                + "\ndemo.inject.EmptyInherit = function() {\nrandori.behaviors."
                + "AbstractBehavior.call(this);\n};\n\n$inherit(demo.inject.EmptyInherit, "
                + "randori.behaviors.AbstractBehavior);\n\ndemo.inject.EmptyInherit.className"
                + " = \"demo.inject.EmptyInherit\";\n\ndemo.inject.EmptyInherit."
                + "getClassDependencies = function(t) {\n\tvar p;\n\treturn [];\n};"
                + "\n\ndemo.inject.EmptyInherit.injectionPoints = function(t) {\n\t"
                + "var p;\n\tswitch (t) {\n\t\tcase 1:\n\t\t\tp = randori.behaviors."
                + "AbstractBehavior.injectionPoints(t);\n\t\t\tbreak;\n\t\tcase 2:\n\t"
                + "\t\tp = randori.behaviors.AbstractBehavior.injectionPoints(t);\n\t\t"
                + "\tbreak;\n\t\tcase 3:\n\t\t\tp = randori.behaviors.AbstractBehavior."
                + "injectionPoints(t);\n\t\t\tbreak;\n\t\tdefault:\n\t\t\tp = [];\n\t\t"
                + "\tbreak;\n\t}\n\treturn p;\n};\n\n");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.inject.EmptyInherit";
    }
}
