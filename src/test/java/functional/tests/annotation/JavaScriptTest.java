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

    @Override
    protected String getTypeUnderTest()
    {
        return "functional.tests.annotation.JavaScriptTest";
    }
}
