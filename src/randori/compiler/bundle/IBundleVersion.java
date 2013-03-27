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

package randori.compiler.bundle;

/**
 * @author Michael Schmalle
 */
public interface IBundleVersion
{
    /**
     * The version the {@link IBundle} was created with.
     */
    String getBundleVersion();

    void setBundleVersion(String value);

    /**
     * The version Randori framework.
     */
    String getRandoriVersion();

    void setRandoriVersion(String value);

    /**
     * The build number of the Randori framework.
     */
    String getRandoriBuild();

    void setRandoriBuild(String value);

    /**
     * The minimum supported version of the Randori framework.
     */
    String getRandoriMinSupportedVersion();

    void setRandoriMinSupportedVersion(String value);

    /**
     * An integer combining all 3 versions into on integer.
     */
    int getMinSupportedVersionInt();

    /**
     * The version of the bundle compiler.
     */
    String getCompilerVersion();

    void setCompilerVersion(String value);

    /**
     * The bundle compiler name.
     */
    String getCompilerName();

    void setCompilerName(String value);

    /**
     * The bundle compiler build number.
     */
    String getCompilerBuild();

    void setCompilerBuild(String value);

}
