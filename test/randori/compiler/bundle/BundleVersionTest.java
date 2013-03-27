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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import randori.compiler.bundle.BundleVersion;

/**
 * @author Michael Schmalle
 */
public class BundleVersionTest
{
    @Test
    public void test_all()
    {
        BundleVersion version = new BundleVersion();
        version.setBundleVersion("1.0");
        version.setRandoriMinSupportedVersion("0.8.4");
        version.setRandoriVersion("1.5.1");
        version.setRandoriBuild("420");

        assertThat("bundle version", version.getBundleVersion(), is("1.0"));
        assertThat("randori version", version.getRandoriVersion(), is("1.5.1"));
        assertThat("build version", version.getRandoriBuild(), is("420"));
        assertThat("min support version",
                version.getRandoriMinSupportedVersion(), is("0.8.4"));
        assertThat("min int version", version.getMinSupportedVersionInt(),
                is(524292));
    }

}
