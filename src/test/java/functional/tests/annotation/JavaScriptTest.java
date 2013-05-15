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

package functional.tests.annotation;

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

import functional.tests.FunctionalTestBase;

public class JavaScriptTest extends FunctionalTestBase
{
    //----------------------------------
    // mode
    //----------------------------------

    @Test
    public void test_json_with_default_args()
    {
        IFunctionNode node = findFunction("test_json_with_default_args",
                classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_json_with_default_args"
                + " = function() {\n\tvar menuItems = [{name:\"Targets\", url:\"views\\/targets.html\","
                + " isRedirect:true, param4:\"bah\", param5:42}, {name:\"Labs\", "
                + "url:\"views\\/labs.html\", isRedirect:false, param4:\"bar\", param5:142}, "
                + "{name:\"Intel\", url:\"views\\/intel.html\", isRedirect:true, param4:\"bah\", "
                + "param5:42}];\n}");
    }

    // [JavaScript(mode="global")]
    @Test
    public void test_export_global()
    {
        IFunctionNode node = findFunction("test_export_global", classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_export_global "
                + "= function() {\n\tstaticMethod();\n\tthing = \"Foo\";\n}");
    }

    // [JavaScript(mode="prototype")]
    @Test
    public void test_export_prototype()
    {
        IFunctionNode node = findFunction("test_export_prototype", classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_export_prototype "
                + "= function() {\n\tfunctional.tests.annotation.support.ExportPrototype."
                + "staticMethod();\n\tfunctional.tests.annotation.support.ExportPrototype.thing "
                + "= \"Foo\";\n}");
    }

    // [JavaScript]
    @Test
    public void test_export_prototype_default()
    {
        IFunctionNode node = findFunction("test_export_prototype_default",
                classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_export_prototype_default "
                + "= function() {\n\tfunctional.tests.annotation.support.ExportPrototypeDefault."
                + "staticMethod();\n\tfunctional.tests.annotation.support.ExportPrototypeDefault.thing "
                + "= \"Foo\";\n}");
    }

    // [JavaScript(mode="json")]
    @Test
    public void test_export_json_constructor()
    {
        IFunctionNode node = findFunction("test_export_json_constructor",
                classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_export_json_constructor = "
                + "function() {\n\tvar x1 = {prop:\"7\", thing:\"8\"};\n}");
    }

    // [JavaScript(mode="json")]
    @Test
    public void test_export_json_variable()
    {
        IFunctionNode node = findFunction("test_export_json_variable",
                classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_export_json_variable "
                + "= function() {\n\tvar x1 = {};\n\t"
                + "x1.prop = \"7\";\n\tx1.thing = \"8\";\n}");
    }

    //----------------------------------
    // name
    //----------------------------------

    // [JavaScript]
    @Test
    public void test_name_empty()
    {
        IFunctionNode node = findFunction("test_name_empty", classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_name_empty "
                + "= function() {\n\tvar x = new functional.tests.annotation."
                + "support.NameEmpty();\n}");
    }

    // [JavaScript(name="Bar")]
    @Test
    public void test_name_simple()
    {
        IFunctionNode node = findFunction("test_name_simple", classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_name_simple "
                + "= function() {\n\tvar x = new Case(1);\n\tx.thing = \"7\";"
                + "\n\tx.method();\n\tCase.staticMethod();\n}");
    }

    // [JavaScript(name="")] For now this is just generating nothing as discussed
    @Test
    public void test_name_simple_null()
    {
        IFunctionNode node = findFunction("test_name_simple_null", classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype."
                + "test_name_simple_null = function() {\n\tvar x = new ();\n\tx.thing "
                + "= \"7\";\n\tx.method();\n\t.staticMethod();\n}");
    }

    // [JavaScript(name="foo.Bar")]
    @Test
    public void test_name_qualified()
    {
        IFunctionNode node = findFunction("test_name_qualified", classNode);
        visitor.visitFunction(node);
        assertOut("functional.tests.annotation.JavaScriptTest.prototype.test_name_qualified "
                + "= function() {\n\tvar x = new test.Case(1);\n\tx.thing = \"7\";"
                + "\n\tx.method();\n\ttest.Case.staticMethod();\n}");
    }

    // XXX [JavaScript(omitconstructor="true")] Produces a CannotCallPrivateConstructorProblem

    @Override
    protected String getTypeUnderTest()
    {
        return "functional.tests.annotation.JavaScriptTest";
    }
}
