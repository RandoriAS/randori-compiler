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
public class ExportGlobalTest extends FunctionalTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        assertOut("\n\nfunctional.tests.annotation.support.ExportGlobal.thing "
                + "= \"Mike\";\n\nstaticMethod = function() {\n};\n\n");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "functional.tests.annotation.support.ExportGlobal";
    }
}
