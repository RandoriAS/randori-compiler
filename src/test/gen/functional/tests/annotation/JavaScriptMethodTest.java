/**
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
 */

package functional.tests.annotation;

import functional.tests.FunctionalTestBase;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

public class JavaScriptMethodTest extends FunctionalTestBase {
    @Test
    public void emptyAnnotation_withInstanceMethodCall_doesNotAddDot() {
        IFunctionNode fnode = findFunction("testTimeout", classNode);

        visitor.visitFunction(fnode);

        assertOut(getTypeUnderTest() + ".prototype.testTimeout"
                + " = function(timeout) {\n\tvar timer = timeout(function() {\n\t\treturn;\n\t}, 2000);\n\ttimer.cancelTimer();\n}");
    }

    @Test
    public void emptyAnnotation_withStaticMethodCall_doesNotAddDot() {
        IFunctionNode fnode = findFunction("testJQueryStaticJ", classNode);

        visitor.visitFunction(fnode);

        assertOut(getTypeUnderTest() + ".prototype.testJQueryStaticJ = function() {\n\tjQuery(\"sup\");\n\tjQuery(\"sup2\");\n}");
    }

    @Override
    protected String getTypeUnderTest () {
        return "functional.tests.annotation.JavaScriptMethodTest";
    }
}
