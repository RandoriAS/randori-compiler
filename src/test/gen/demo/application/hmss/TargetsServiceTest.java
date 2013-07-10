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

package demo.application.hmss;

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Ignore;
import org.junit.Test;

import randori.compiler.internal.RandoriTestBase;
import randori.compiler.internal.constants.TestConstants;

/**
 * @author Michael Schmalle
 */
public class TargetsServiceTest extends RandoriTestBase
{
    @Test
    public void test_constructor()
    {
        IFunctionNode node = findFunction("TargetsService", classNode);
        visitor.visitFunction(node);
        assertOut("services.TargetsService = function(xmlHttpRequest, config, targets) {"
                + "\n\tthis.path = null;\n\trandori.service.AbstractService.call("
                + "this, xmlHttpRequest);\n\tthis.config = config;\n\tthis.targets = "
                + "targets;\n\tthis.path = \"assets\\/data\\/targets.txt\";\n}");
    }

    @Ignore
    @Test
    public void test_get()
    {
        IFunctionNode node = findFunction("get", classNode);
        visitor.visitFunction(node);
        assertOut("services.TargetsService.prototype.get = function() {"
                + "\n\tvar promise = this.sendRequest(\"GET\", this.path);\n\tvar "
                + "parserPromise = promise.then($createStaticDelegate(this.targets, "
                + "this.targets.parseResult));\n\treturn parserPromise;\n}");
    }

    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
    }

    @Override
    protected String getBasePath()
    {
        return TestConstants.RandoriASFramework
                + "\\randori-demos-bundle\\HMSS\\src";
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "services.TargetsService";
    }
}
