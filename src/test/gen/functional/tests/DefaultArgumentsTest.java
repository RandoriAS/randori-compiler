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

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

/**
 * @author Michael Schmalle
 */
public class DefaultArgumentsTest extends FunctionalTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        //assertOut("");
    }

    @Test
    public void no_arguments()
    {
        IFunctionNode node = findFunction("no_arguments", classNode);
        visitor.visitFunction(node);
        assertOut("demo.args.DefaultArguments.prototype.no_arguments = function() {"
                + "\n\tthis.no_arguments();\n}");
    }

    @Test
    public void one_argument()
    {
        IFunctionNode node = findFunction("one_argument", classNode);
        visitor.visitFunction(node);
        assertOut("demo.args.DefaultArguments.prototype.one_argument = function(foo) {"
                + "\n\tthis.one_argument(\"bar\");\n}");
    }

    @Test
    public void one_argument_default()
    {
        IFunctionNode node = findFunction("one_argument_default", classNode);
        visitor.visitFunction(node);
        assertOut("demo.args.DefaultArguments.prototype.one_argument_default = "
                + "function(foo) {\n\tthis.one_argument_default(\"goo\");\n\t"
                + "this.one_argument_default(\"bar\");\n}");
    }

    @Test
    public void two_argument_default()
    {
        IFunctionNode node = findFunction("two_argument_default", classNode);
        visitor.visitFunction(node);
        assertOut("demo.args.DefaultArguments.prototype.two_argument_default = "
                + "function(foo, bar) {\n\tthis.two_argument_default(\"goo\", 42);"
                + "\n\tthis.two_argument_default(\"bar\", 42);\n\tthis."
                + "two_argument_default(\"bar\", 420);\n}");
    }

    @Test
    public void three_params_two_argument_default()
    {
        IFunctionNode node = findFunction("three_params_two_argument_default",
                classNode);
        visitor.visitFunction(node);
        assertOut("demo.args.DefaultArguments.prototype.three_params_two_argument_default"
                + " = function(foo, bar, baz) {\n\tthis.three_params_two_argument_default"
                + "(\"bar\", 42);\n\tthis.three_params_two_argument_default(\"bar\", 420);"
                + "\n\tthis.three_params_two_argument_default(\"bar\", 420, null);\n}");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.args.DefaultArguments";
    }
}
