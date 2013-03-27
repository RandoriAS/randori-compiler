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
import org.apache.flex.compiler.config.ConfigurationValue;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.config.annotations.Config;
import org.apache.flex.compiler.internal.config.annotations.Mapping;

/**
 * The configuration specific randori compiler arguments.
 * 
 * @author Michael Schmalle
 */
public class RandoriConfiguration extends Configuration
{
    //
    // 'create-target-with-errors'
    //

    private boolean createTargetWithErrors = false;

    @Override
    public boolean getCreateTargetWithErrors()
    {
        return createTargetWithErrors;
    }

    @Override
    @Config(hidden = true)
    public void setCreateTargetWithErrors(ConfigurationValue cv, boolean value)
            throws ConfigurationException
    {
        createTargetWithErrors = value;
    }

    public RandoriConfiguration()
    {
    }

    //
    // 'js-base-path'
    //

    private String jsBasePath = "";

    public String getJsBasePath()
    {
        return jsBasePath;
    }

    @Config
    @Mapping("js-base-path")
    public void setJsBasePath(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        jsBasePath = value;
    }

    //
    // 'js-library-path'
    //

    private String jsLibraryPath = "";

    public String getJsLibraryPath()
    {
        return jsLibraryPath;
    }

    @Config
    @Mapping("js-library-path")
    public void setJsLibraryPath(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        jsLibraryPath = value;
    }

    //
    // 'js-classes-as-files'
    //

    private boolean jsClassesAsFiles = false;

    public boolean getJsClassesAsFiles()
    {
        return jsClassesAsFiles;
    }

    @Config
    @Mapping("js-classes-as-files")
    public void setJsClassesAsFiles(ConfigurationValue cv, boolean value)
            throws ConfigurationException
    {
        jsClassesAsFiles = value;
    }

}
