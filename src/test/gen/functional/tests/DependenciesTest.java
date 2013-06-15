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

package functional.tests;

import junit.framework.Assert;

import org.junit.Test;

public class DependenciesTest extends FunctionalTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);

        Assert.assertEquals(6, getEmitter().getModel().getRuntimeDependencies()
                .size());
        Assert.assertEquals(3, getEmitter().getModel().getStaticDependencies()
                .size());

        assertOut("if (typeof demo == \"undefined\")\n\tvar demo = {};\nif (typeof "
                + "demo.foo == \"undefined\")\n\tdemo.foo = {};\n\ndemo.foo.Dependencies "
                + "= function() {\n\t\n};\n\ndemo.foo.Dependencies.pfoo = demo.foo.ClassB."
                + "FOO;\n\ndemo.foo.Dependencies.pbar = demo.foo.MyFunction();\n\ndemo.foo."
                + "Dependencies.pbar2 = demo.foo.MyTestFunction();\n\ndemo.foo.Dependencies."
                + "prototype.runtime_dependencies = function() {\n\tvar myClass = demo.foo."
                + "support.Mode1;\n\tvar x = new demo.foo.support.Bar();\n\tdemo.foo.support."
                + "trace(\"Yo\");\n\tdemo.foo.support.AnotherStaticClass.set_current(3);"
                + "\n\tdemo.foo.support.SupportClassA.inputMode = demo.foo.support.Static2."
                + "property;\n};\n\ndemo.foo.Dependencies.className = \"demo.foo.Dependencies"
                + "\";\n\ndemo.foo.Dependencies.getRuntimeDependencies = function(t) {\n\tvar"
                + " p;\n\tp = [];\n\tp.push('demo.foo.support.trace');\n\tp.push('demo.foo."
                + "support.SupportClassA');\n\tp.push('demo.foo.support.AnotherStaticClass');"
                + "\n\tp.push('demo.foo.support.Static2');\n\tp.push('demo.foo.support.Mode1');"
                + "\n\tp.push('demo.foo.support.Bar');\n\treturn p;\n};\n\ndemo.foo.Dependencies"
                + ".getStaticDependencies = function(t) {\n\tvar p;\n\tp = [];\n\tp.push('demo."
                + "foo.MyFunction');\n\tp.push('demo.foo.ClassB');\n\tp.push('demo.foo."
                + "MyTestFunction');\n\treturn p;\n};\n\ndemo.foo.Dependencies.injectionPoints "
                + "= function(t) {\n\treturn [];\n};\n");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.Dependencies";
    }
}