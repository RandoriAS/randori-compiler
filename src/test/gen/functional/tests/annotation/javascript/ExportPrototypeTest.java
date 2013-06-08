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

package functional.tests.annotation.javascript;

import org.junit.Test;

import functional.tests.FunctionalTestBase;

/**
 * @author Michael Schmalle
 */
public class ExportPrototypeTest extends FunctionalTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        assertOut("if (typeof functional == \"undefined\")\n\tvar functional = {};"
                + "\nif (typeof functional.tests == \"undefined\")\n\tfunctional.tests "
                + "= {};\nif (typeof functional.tests.annotation == \"undefined\")\n\t"
                + "functional.tests.annotation = {};\nif (typeof functional.tests."
                + "annotation.support == \"undefined\")\n\tfunctional.tests.annotation."
                + "support = {};\n\nfunctional.tests.annotation.support.ExportPrototype "
                + "= function() {\n\t\n};\n\nfunctional.tests.annotation.support."
                + "ExportPrototype.thing = \"Mike\";\n\nfunctional.tests.annotation."
                + "support.ExportPrototype.staticMethod = function() {\n};\n\n"
                + "functional.tests.annotation.support.ExportPrototype.className "
                + "= \"functional.tests.annotation.support.ExportPrototype\";\n\n"
                + "functional.tests.annotation.support.ExportPrototype.getClassDependencies "
                + "= function(t) {\n\tvar p;\n\treturn [];\n};\n\nfunctional.tests."
                + "annotation.support.ExportPrototype.injectionPoints = function(t) "
                + "{\n\treturn [];\n};\n");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "functional.tests.annotation.support.ExportPrototype";
    }
}
