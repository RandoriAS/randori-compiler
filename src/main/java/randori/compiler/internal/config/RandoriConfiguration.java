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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.config.ConfigurationValue;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.exceptions.ConfigurationException.CannotOpen;
import org.apache.flex.compiler.internal.config.annotations.Arguments;
import org.apache.flex.compiler.internal.config.annotations.Config;
import org.apache.flex.compiler.internal.config.annotations.InfiniteArguments;
import org.apache.flex.compiler.internal.config.annotations.Mapping;

import com.google.common.collect.ImmutableList;

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
    // 'bundle-path'
    //

    private final List<String> bundlePath = new ArrayList<String>();

    public List<String> getBundlePath()
    {
        return bundlePath;
    }

    @Config(allowMultiple = true, isPath = true)
    @Mapping({ "bundle-path" })
    @Arguments(Arguments.PATH_ELEMENT)
    @InfiniteArguments
    public void setBundlePath(ConfigurationValue cv, String[] pathlist)
            throws CannotOpen
    {
        final ImmutableList<String> resolvedPaths = expandTokens(
                Arrays.asList(pathlist), Arrays.asList(""), cv, false);
        bundlePath.addAll(resolvedPaths);
    }

    //
    // 'external-bundle-path'
    //

    private final List<String> externalBundlePath = new ArrayList<String>();

    public List<String> getExternalBundlePath()
    {
        return externalBundlePath;
    }

    @Config(allowMultiple = true, isPath = true)
    @Mapping({ "external-bundle-path" })
    @Arguments(Arguments.PATH_ELEMENT)
    @InfiniteArguments
    public void setExternalBundlePath(ConfigurationValue cv, String[] pathlist)
            throws CannotOpen
    {
        final ImmutableList<String> resolvedPaths = expandTokens(
                Arrays.asList(pathlist), Arrays.asList(""), cv, false);
        externalBundlePath.addAll(resolvedPaths);
    }

    //
    // 'exclude-packages'
    //

    private String excludePackages = "";

    public String getExcludePackages()
    {
        return excludePackages;
    }

    @Config(allowMultiple = false)
    @Mapping("exclude-packages")
    public void setExcludePackages(ConfigurationValue cv, String value)
            throws ConfigurationException
    {
        excludePackages = value;
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

    //
    // 'js-merged-file'
    //

    private final List<MergedFileSettings> jsMergedFile = new ArrayList<MergedFileSettings>();

    public List<MergedFileSettings> getJsMergedFiles()
    {
        return jsMergedFile;
    }

    @Config(allowMultiple = true)
    @Mapping("js-merged-file")
    @InfiniteArguments
    public void setJsMergedFile(ConfigurationValue cv, String[] list)
            throws CannotOpen
    {
        int i = 0;
        String fileName = list[0];

        List<String> qualifiedNames = new ArrayList<String>();
        for (i = 1; i < list.length; i++)
        {
            qualifiedNames.add(list[i]);
        }

        jsMergedFile.add(new MergedFileSettings(fileName, qualifiedNames));
    }

    //--------------------------------------------------------------------------
    // BundleProject configuration
    //--------------------------------------------------------------------------

    //
    // 'bundle-libraries'
    //

    private final List<String> bundleLibraries = new ArrayList<String>();

    public List<String> getBundleLibraries()
    {
        return bundleLibraries;
    }

    @Config(allowMultiple = false)
    @Mapping("bundle-libraries")
    @InfiniteArguments
    public void setBundleLibraries(ConfigurationValue cv, String[] list)
            throws CannotOpen
    {
        for (String library : list)
        {
            bundleLibraries.add(library);
        }
    }

    //
    // 'bundle-source-path'
    //

    private final Map<String, PathCollection> bundleSourcePaths = new HashMap<String, PathCollection>();

    public Map<String, PathCollection> getBundleSourcePaths()
    {
        return bundleSourcePaths;
    }

    @Config(allowMultiple = true)
    @Mapping("bundle-source-path")
    @InfiniteArguments
    public void setBundleSourcePaths(ConfigurationValue cv, String[] list)
            throws CannotOpen
    {
        String libraryName = list[0];
        String path = list[1];

        PathCollection info = bundleSourcePaths.get(libraryName);
        if (info == null)
        {
            info = new PathCollection(libraryName);
            bundleSourcePaths.put(libraryName, info);
        }
        info.addPath(path);
    }

    //
    // 'bundle-include-sources'
    //

    private final Map<String, PathCollection> bundleIncludeSources = new HashMap<String, PathCollection>();

    public Map<String, PathCollection> getBundleIncludeSources()
    {
        return bundleIncludeSources;
    }

    @Config(allowMultiple = true)
    @Mapping("bundle-include-sources")
    @InfiniteArguments
    public void setBundleIncludeSources(ConfigurationValue cv, String[] list)
            throws CannotOpen
    {
        String libraryName = list[0];
        String path = list[1];

        PathCollection info = bundleIncludeSources.get(libraryName);
        if (info == null)
        {
            info = new PathCollection(libraryName);
            bundleIncludeSources.put(libraryName, info);
        }
        info.addPath(path);
    }

}
