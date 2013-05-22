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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.config.Configurator;
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

    private Collection<File> bundlePaths;

    private Collection<File> externalBundlePaths;

    private Collection<String> excludePackages;

    private Collection<File> includeSources;

    private Collection<String> incrementalFiles = new ArrayList<String>();

    public RandoriTargetSettings(Configuration configuration)
    {
        super(configuration);
        // need to save an instance ref so we can proxy, configuration is private
        this.configuration = (RandoriConfiguration) configuration;
    }

    @Override
    public String getSDKPath()
    {
        return configuration.getSDKPath();
    }

    @Override
    public String getAppName()
    {
        return configuration.getAppName();
    }

    @Override
    public Collection<String> getExcludePackages()
    {
        if (excludePackages == null)
        {
            excludePackages = new ArrayList<String>();
            final String packages = configuration.getExcludePackages();
            if (packages != null && !packages.isEmpty())
            {
                for (String pckg : packages.split(","))
                {
                    excludePackages.add(pckg.trim());
                }
            }
        }
        return excludePackages;
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

    @Override
    public Collection<File> getBundlePaths()
    {
        if (bundlePaths == null)
            bundlePaths = Configurator
                    .toFileList(configuration.getBundlePath());

        return bundlePaths;
    }

    @Override
    public Collection<File> getExternalBundlePaths()
    {
        if (externalBundlePaths == null)
            externalBundlePaths = Configurator.toFileList(configuration
                    .getExternalBundlePath());

        return externalBundlePaths;
    }

    @Override
    public Collection<String> getRawIncludeSources()
    {
        return configuration.getIncludeSources();
    }

    @Override
    public Collection<String> getIncrementalFiles()
    {
        return incrementalFiles;
    }

    @Override
    public void addIncrementalFile(String absoluteFilename)
    {
        if (incrementalFiles.contains(absoluteFilename))
            return;
        incrementalFiles.add(absoluteFilename);
    }

    @Override
    public Collection<File> getIncludeSources()
    {
        if (includeSources == null)
        {
            includeSources = new HashSet<File>();

            List<File> files = Configurator.toFileList(configuration
                    .getIncludeSources());
            for (File file : files)
            {
                if (file.isFile())
                {
                    includeSources.add(file);
                    continue;
                }
                else if (file.isDirectory())
                {
                    for (File fileInFolder : FileUtils.listFiles(file,
                            new String[] { "as" }, true))
                        includeSources.add(fileInFolder);
                }
            }
        }

        return includeSources;
    }

}
