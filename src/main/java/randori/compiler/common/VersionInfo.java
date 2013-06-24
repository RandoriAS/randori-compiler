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

package randori.compiler.common;

/**
 * @author Michael Schmalle
 */
public class VersionInfo
{
    public static final String RANDORI_MAJOR_VERSION = "0";

    public static final String RANDORI_MINOR_VERSION = "2";

    public static final String RANDORI_NANO_VERSION = "4";

    // XXX TEMP
    public static final String RANDORI_BUILD = "1";
    public static final String RANDORI_BUILD_VERSION = "1";

    public static final String RANDORI_COMPILER_VERSION = "0.2.4.23";
    public static final String RANDORI_COMPILER_NAME = "randori";

    public static final String LIB_VERSION_1_0 = "1.0";

    public static final String LIB_MAJOR_VERSION = "1";
    public static final String LIB_MINOR_VERSION = "0";

    static String RANDORI_VERSION_NUMBER;
    static String LIB_VERSION_NUMBER;

    public static String getRandoriVersion()
    {
        if (RANDORI_VERSION_NUMBER == null)
        {
            RANDORI_VERSION_NUMBER = RANDORI_MAJOR_VERSION + "."
                    + RANDORI_MINOR_VERSION + "." + RANDORI_NANO_VERSION;
        }
        return RANDORI_VERSION_NUMBER;
    }

    public static String getLibVersion()
    {
        if (LIB_VERSION_NUMBER == null)
        {
            LIB_VERSION_NUMBER = LIB_MAJOR_VERSION + "." + LIB_MINOR_VERSION;
        }
        return LIB_VERSION_NUMBER;
    }

    public static String getCompilerName()
    {
        return RANDORI_COMPILER_NAME;
    }

    public static String getCompilerVersion()
    {
        return RANDORI_COMPILER_VERSION;
    }

    public static String getCompilerBuild()
    {
        return RANDORI_BUILD_VERSION;
    }

    public static String getBuild()
    {
        return RANDORI_BUILD;
    }
}
