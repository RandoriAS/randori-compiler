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
import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.targets.ITargetSettings;
import org.apache.flex.compiler.targets.ITarget.TargetType;

import randori.compiler.config.IRandoriTargetSettings;

/**
 * An override of the {@link Configurator} to allow a custom target settings
 * implementation.
 * 
 * @author Michael Schmalle
 * @see IRandoriTargetSettings
 */
public class RandoriConfigurator extends Configurator
{

    public RandoriConfigurator()
    {
    }

    public RandoriConfigurator(Class<? extends Configuration> configurationClass)
    {
        super(configurationClass);
    }

    @Override
    public ITargetSettings getTargetSettings(TargetType targetType)
    {
        // I am not quite sure if I can leave this super call out yet
        super.getTargetSettings(targetType);

        // this is queried in RandoriBackend and is passed the RandoriConfiguration
        return new RandoriTargetSettings(getConfiguration());
    }

}
