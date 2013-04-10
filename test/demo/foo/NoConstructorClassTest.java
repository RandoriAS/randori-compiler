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

package demo.foo;

import static org.junit.Assert.assertNull;

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.js.codegen.ResourceTestBase;

/**
 * @author Michael Schmalle
 */
public class NoConstructorClassTest extends ResourceTestBase
{
    @Test
    public void test_constructor()
    {
        IFunctionNode node = findFunction("NoConstructorClass", classNode);
        assertNull(node);
    }

    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        assertOut("if (typeof demo == \"undefined\")\n\tvar demo = {};\nif (typeof "
                + "demo.foo == \"undefined\")\n\tdemo.foo = {};\n\ndemo.foo.NoConstructorClass = "
                + "function() {\n\tthis.port = null;\n\tthis.debugMode = true;\n\t\n};\n\ndemo.foo."
                + "NoConstructorClass.className = \"demo.foo.NoConstructorClass\";\n\ndemo.foo."
                + "NoConstructorClass.getClassDependencies = function(t) {\n\tvar p;\n\treturn "
                + "[];\n};\n\ndemo.foo.NoConstructorClass.injectionPoints = function(t) "
                + "{\n\treturn [];\n};\n");
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
        return "demo.foo.NoConstructorClass";
    }
}
