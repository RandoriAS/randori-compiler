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

package randori.compiler.bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flex.utils.StringUtils;

/**
 * @author Michael Schmalle
 */
public class BundleConfiguration implements IBundleConfiguration
{
    private String output;

    private String bundleName;

    private Collection<String> libraryPaths = new ArrayList<String>();

    private Collection<String> externalLibraryPaths = new ArrayList<String>();

    private Collection<String> bundlePaths = new ArrayList<String>();

    private Collection<String> externalBundlePaths = new ArrayList<String>();

    private Map<String, IBundleConfigurationEntry> entries = new HashMap<String, IBundleConfigurationEntry>();

    private String sdkPath;

    private Boolean jsOutputAsFiles = null;

    @Override
    public String getOutput()
    {
        return output;
    }

    @Override
    public String getBundelName()
    {
        return bundleName;
    }

    @Override
    public Collection<String> getLibraryPaths()
    {
        return libraryPaths;
    }

    @Override
    public Collection<String> getExternalLibraryPaths()
    {
        return externalLibraryPaths;
    }

    @Override
    public Collection<String> getBundlePaths()
    {
        return bundlePaths;
    }

    @Override
    public Collection<String> getExternalBundlePaths()
    {
        return externalBundlePaths;
    }

    @Override
    public Collection<IBundleConfigurationEntry> getEntries()
    {
        return entries.values();
    }

    @Override
    public IBundleConfigurationEntry getEntry(String name)
    {
        return entries.get(name);
    }

    public BundleConfiguration(String bundleName, String output)
    {
        this.bundleName = bundleName;
        this.output = output;
    }

    @Override
    public void addLibraryPath(String path)
    {
        if (libraryPaths.contains(path))
            return;
        libraryPaths.add(path);
    }

    @Override
    public void addExternalLibraryPath(String path)
    {
        if (externalLibraryPaths.contains(path))
            return;
        externalLibraryPaths.add(path);
    }

    @Override
    public void addBundlePath(String path)
    {
        if (bundlePaths.contains(path))
            return;
        bundlePaths.add(path);
    }

    @Override
    public void addExternalBundlePath(String path)
    {
        if (externalBundlePaths.contains(path))
            return;
        externalBundlePaths.add(path);
    }

    public BundleConfigurationEntry addEntry(String name)
    {
        BundleConfigurationEntry entry = new BundleConfigurationEntry(this,
                name);
        entries.put(name, entry);
        return entry;
    }

    public void setSDKPath(String value)
    {
        sdkPath = value;
    }

    public String getSDKPath()
    {
        return sdkPath;
    }

    public Boolean isJsOutputAsFiles()
    {
        return jsOutputAsFiles;
    }

    public void setJsOutputAsFiles(Boolean value)
    {
        jsOutputAsFiles = value;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    static class BundleConfigurationEntry implements IBundleConfigurationEntry
    {
        private final IBundleConfiguration configuration;

        private String name;

        private List<String> sourcePaths = new ArrayList<String>();

        private List<String> libraryPaths = new ArrayList<String>();

        private List<String> externalLibraryPaths = new ArrayList<String>();

        private List<String> includeSources = new ArrayList<String>();

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public Collection<String> getLibraryPaths()
        {
            List<String> result = new ArrayList<String>();
            result.addAll(libraryPaths);
            result.addAll(configuration.getLibraryPaths());
            return result;
        }

        @Override
        public Collection<String> getExternalLibraryPaths()
        {
            List<String> result = new ArrayList<String>();
            result.addAll(externalLibraryPaths);
            result.addAll(configuration.getExternalLibraryPaths());
            return result;
        }

        @Override
        public Collection<String> getSourcePaths()
        {
            return sourcePaths;
        }

        @Override
        public Collection<String> getIncludeSources()
        {
            return includeSources;
        }

        public BundleConfigurationEntry(IBundleConfiguration configuration,
                String name)
        {
            this.configuration = configuration;
            this.name = name;
        }

        @Override
        public void addSourcePath(String path)
        {
            if (sourcePaths.contains(path))
                return;
            sourcePaths.add(path);
        }

        @Override
        public void addIncludeSources(String path)
        {
            if (includeSources.contains(path))
                return;
            includeSources.add(path);
        }

        @Override
        public void addLibraryPath(String path)
        {
            if (libraryPaths.contains(path))
                return;
            libraryPaths.add(path);
        }

        @Override
        public void addExternalLibraryPath(String path)
        {
            if (externalLibraryPaths.contains(path))
                return;
            externalLibraryPaths.add(path);
        }
    }

    public String[] toArguments()
    {
        List<String> result = new ArrayList<String>();

        for (String arg : bundlePaths)
        {
            result.add("-bundle-path=" + arg);
        }

        for (String arg : libraryPaths)
        {
            result.add("-library-path=" + arg);
        }

        for (String arg : externalLibraryPaths)
        {
            result.add("-external-library-path=" + arg);
        }

        List<String> libraries = getLibraries();
        if (libraries.size() > 0)
        {
            result.add("-bundle-libraries="
                    + StringUtils.join(libraries.toArray(new String[] {}), ","));
        }

        if (isJsOutputAsFiles() != null)
        {
            result.add("-js-classes-as-files="
                    + (isJsOutputAsFiles() ? "true" : "false"));
        }

        String sdk = getSDKPath();
        if (sdk != null && !sdk.equals(""))
            result.add("-sdk-path=" + sdk);

        result.add("-output=" + getOutput());

        // Entry specific
        for (IBundleConfigurationEntry entry : getEntries())
        {
            // -bundle-source-path
            for (String path : entry.getSourcePaths())
            {
                result.add("-bundle-source-path=" + entry.getName() + ","
                        + path);
            }

            // -bundle-library-path
            for (String path : entry.getLibraryPaths())
            {
                result.add("-bundle-library-path=" + entry.getName() + ","
                        + path);
            }

            // -bundle-include-sources
            for (String path : entry.getIncludeSources())
            {
                result.add("-bundle-include-sources=" + entry.getName() + ","
                        + path);
            }
        }

        return result.toArray(new String[] {});
    }

    private List<String> getLibraries()
    {
        List<String> result = new ArrayList<String>();
        for (IBundleConfigurationEntry entry : getEntries())
        {
            result.add(entry.getName());
        }
        return result;
    }

    @Override
    public String toString()
    {
        return StringUtils.join(toArguments(), " ");
    }

    public static IBundleConfiguration create(String[] arguments)
    {
        //BundleConfiguration configuration = new BundleConfiguration();
        return null;
    }

}
