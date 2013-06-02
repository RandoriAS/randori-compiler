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

package randori.compiler.asdoc;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.clients.CompilerArguments;
import randori.compiler.clients.RandoriDoc;
import randori.compiler.internal.constants.TestConstants;

/**
 * @author Michael Schmalle
 */
public class RandoriDocTest
{
    protected File tempDir = new File(TestConstants.TEMP_DIR, "asdoc");

    @Before
    public void setUp() throws IOException
    {

    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void test_main()
    {
        CompilerArguments arguments = new CompilerArguments();

        arguments.addSourcepath(TestConstants.RANDORI_SRC.getAbsolutePath());
        arguments.addSourcepath(TestConstants.RANDORI_GUICE_SRC
                .getAbsolutePath());

        arguments.addLibraryPath(TestConstants.BUILTIN_SWC.getAbsolutePath());
        arguments.addLibraryPath(TestConstants.HTMLCORELIB_SWC
                .getAbsolutePath());
        arguments.addLibraryPath(TestConstants.JQUERY_SWC.getAbsolutePath());
        arguments.setOutput(tempDir.getAbsolutePath());

        // RandoriDoc specific args

        arguments.setMainTitle("Main Title");
        arguments.setFooter("The footer");

        arguments.addDocMember("variable");
        arguments.addDocMember("accessor");
        arguments.addDocMember("method");
        arguments.addDocMember("constant");

        arguments.addDocNamespace("public");
        arguments.addDocNamespace("protected");
        arguments.addDocNamespace("private");

        String[] args = arguments.toArguments();
        int code = RandoriDoc.staticMainNoExit(args);
        Assert.assertEquals(0, code);
    }
}
