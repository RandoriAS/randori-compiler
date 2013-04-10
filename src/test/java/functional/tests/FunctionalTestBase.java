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

package functional.tests;

import java.io.File;
import java.util.List;

import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.js.codegen.ResourceTestBase;

/**
 * @author Michael Schmalle
 */
public abstract class FunctionalTestBase extends ResourceTestBase
{

    private static final String SRC_PATH = "\\randori-compiler\\src\\test\\resources\\functional";

    @Override
    protected void addSourcePaths(List<File> sourcePaths)
    {
        sourcePaths
                .add(new File(FilenameNormalization.normalize(getBasePath())));
    }

    @Override
    protected String getBasePath()
    {
        return TestConstants.RandoriASFramework + SRC_PATH;
    }
}
