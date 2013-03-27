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
public class BundleVersion implements IBundleVersion
{

    private String bundleVersion;

    private String randoriVersion;

    private String randoriBuild;

    private String randoriMinSupportedVersion;

    private int randoriMinSupportedVersionInt;

    private String compilerVersion;

    private String compilerName;

    private String compilerBuild;

    @Override
    public String getBundleVersion()
    {
        return bundleVersion;
    }

    @Override
    public void setBundleVersion(String value)
    {
        bundleVersion = value;
    }

    @Override
    public String getRandoriVersion()
    {
        return randoriVersion;
    }

    @Override
    public void setRandoriVersion(String value)
    {
        randoriVersion = value;
    }

    @Override
    public String getRandoriBuild()
    {
        return randoriBuild;
    }

    @Override
    public void setRandoriBuild(String value)
    {
        randoriBuild = value;
    }

    @Override
    public String getRandoriMinSupportedVersion()
    {
        return randoriMinSupportedVersion;
    }

    @Override
    public void setRandoriMinSupportedVersion(String value)
    {
        randoriMinSupportedVersion = value;
        randoriMinSupportedVersionInt = versionStringToInt(randoriMinSupportedVersion);
    }

    @Override
    public int getMinSupportedVersionInt()
    {
        return randoriMinSupportedVersionInt;
    }

    @Override
    public String getCompilerVersion()
    {
        return compilerVersion;
    }

    @Override
    public void setCompilerVersion(String value)
    {
        compilerVersion = value;
    }

    @Override
    public String getCompilerName()
    {
        return compilerName;
    }

    @Override
    public void setCompilerName(String value)
    {
        compilerName = value;
    }

    @Override
    public String getCompilerBuild()
    {
        return compilerBuild;
    }

    @Override
    public void setCompilerBuild(String value)
    {
        compilerBuild = value;
    }

    private static int versionStringToInt(final String versionString)
    {
        if (versionString == null)
            return -1;

        String results[] = versionString.split("\\.");
        int major = 0;
        int minor = 0;
        int revision = 0;

        int n = results.length;
        for (int i = 0; i < n; i++)
        {
            if (i == 0)
                major = Integer.parseInt(results[0]);
            else if (i == 1)
                minor = Integer.parseInt(results[1]);
            else if (i == 2)
                revision = Integer.parseInt(results[2]);
        }

        int version = (major << 24) + (minor << 16) + revision;
        return version;
    }

}
