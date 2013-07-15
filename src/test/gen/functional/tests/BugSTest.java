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

public class BugSTest extends FunctionalTestBase
{
    @Test
    public void test_constructor()
    {
        IFunctionNode node = findFunction("BugS", classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.support.BugS = function() {"
                + "\n\tthis.num = 0;\n\tparseFloat(\"100\");\n\tvar x = {};"
                + "\n\tthis.num = parseFloat(x.attribute(\"name\"));\n\t"
                + "demo.foo.support.BugT.call(this, \"test1\", 111, 67, 1532);\n}");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.support.BugS";
    }
}
