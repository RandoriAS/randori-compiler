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

public class BaseBTest extends FunctionalTestBase
{
    @Test
    public void test_constrructor()
    {
        IFunctionNode node = findFunction("BaseB", classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.support.BaseB = function() {"
                + "\n\tdemo.foo.support.BaseA.call(this, \"is the best\", "
                + "\"randori\");\n}");
    }

    @Test
    public void test_foo()
    {
        IFunctionNode node = findFunction("foo", classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.support.BaseB.prototype.foo = function() {"
                + "\n\tdemo.foo.support.BaseA.prototype.foo.call(this,\"something\", "
                + "\"randori\");\n}");
    }

    @Test
    public void test_bar()
    {
        IFunctionNode node = findFunction("test_bar", classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.support.BaseB.prototype.test_bar = function() {"
                + "\n\tthis.bar(\"foo\", \"randori\");\n}");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.support.BaseB";
    }
}
