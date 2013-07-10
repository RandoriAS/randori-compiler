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
public class QualifiedNamesTest extends FunctionalTestBase
{
    /*
     * ClassA.CONSTANT -> demo.foo.ClassA.CONSTANT
     */
    @Test
    public void test_public_constant()
    {
        IFunctionNode node = findFunction("public_constant", classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.QualifiedNames.prototype.public_constant = function() {"
                + "\n\tvar a = 0;\n}");
    }

    @Test
    public void new_inner_class()
    {
        IFunctionNode node = findFunction("new_inner_class", classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.QualifiedNames.prototype.new_inner_class = function() {"
                + "\n\tvar i = new demo.foo.QualifiedNames$InnerClass();\n\ti."
                + "set_property(0);\n}");
    }

    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        //assertOut("");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.QualifiedNames";
    }
}
