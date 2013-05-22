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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    static class BundleConfigurationEntry implements IBundleConfigurationEntry
    {
        private final IBundleConfiguration configuration;

        private String name;

        private List<String> sourcePaths = new ArrayList<String>();

        private List<String> includeSources = new ArrayList<String>();

        private File javaScript;

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public File getJavaScript()
        {
            return javaScript;
        }

        @Override
        public String getJavaScriptName()
        {
            return name + ".js";
        }

        @Override
        public Collection<String> getLibraryPaths()
        {
            return configuration.getLibraryPaths();
        }

        @Override
        public Collection<String> getExternalLibraryPaths()
        {
            return configuration.getExternalLibraryPaths();
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
    }

}
