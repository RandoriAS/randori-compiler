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
    // 'annotation'
    //

    private String annotation = "ignore";

    public String getAnnotation()
    {
        return annotation;
    }

    @Config(allowMultiple = false)
    @Mapping("annotation")
    public void setAnnotation(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        if (value.equals("ignore") || value.equals("warn")
                || value.equals("error"))
        {
            annotation = value;
        }
        else
        {
            // XXX Impl custom ConfigurationException
            throw new ConfigurationException(cv.getVar(), "", -1);
        }
    }

    //
    // 'sdk-path'
    //

    private String sdkPath = "";

    public String getSDKPath()
    {
        return sdkPath;
    }

    @Config(allowMultiple = false)
    @Mapping("sdk-path")
    public void setSDKPath(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        sdkPath = value;
    }

    //
    // 'app-name'
    //

    private String appName = "";

    public String getAppName()
    {
        return appName;
    }

    @Config(allowMultiple = false)
    @Mapping("app-name")
    public void setAppName(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        appName = value;
    }

    //
    // 'js-base-path'
    //

    private String jsBasePath = "";

    public String getJsBasePath()
    {
        return jsBasePath;
    }

    @Config(allowMultiple = false)
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

    @Config(allowMultiple = false)
    @Mapping("js-library-path")
    public void setJsLibraryPath(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        jsLibraryPath = value;
    }

    //
    // 'js-classes-as-files'
    //

    private boolean jsClassesAsFiles = true;

    public boolean getJsClassesAsFiles()
    {
        return jsClassesAsFiles;
    }

    @Config(allowMultiple = false)
    @Mapping("js-classes-as-files")
    public void setJsClassesAsFiles(ConfigurationValue cv, boolean value)
            throws ConfigurationException
    {
        jsClassesAsFiles = value;
    }

}
