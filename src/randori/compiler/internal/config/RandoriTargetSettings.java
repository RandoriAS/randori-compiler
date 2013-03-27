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

package randori.compiler.internal.config;

import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.internal.config.TargetSettings;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriApplication;
import randori.compiler.driver.IRandoriTarget;

/**
 * The wrapped configuration API for the {@link IRandoriTarget}.
 * <p>
 * The {@link IRandoriApplication} uses these configs along with the inherited
 * configs to properly setup it's generated source code.
 * 
 * @author Michael Schmalle
 */
public class RandoriTargetSettings extends TargetSettings implements
        IRandoriTargetSettings
{

    private RandoriConfiguration configuration;

    public RandoriTargetSettings(Configuration configuration)
    {
        super(configuration);
        // need to save an instance ref so we can proxy, configuration is private
        this.configuration = (RandoriConfiguration) configuration;
    }

    @Override
    public String getJsBasePath()
    {
        return configuration.getJsBasePath();
    }

    @Override
    public String getJsLibraryPath()
    {
        return configuration.getJsLibraryPath();
    }

    @Override
    public boolean getJsClassesAsFiles()
    {
        return configuration.getJsClassesAsFiles();
    }

}
