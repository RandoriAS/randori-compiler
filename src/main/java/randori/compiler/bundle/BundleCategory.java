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

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;

import randori.compiler.bundle.io.BundleUtils;

/**
 * @author Michael Schmalle
 */
public class BundleCategory implements IBundleCategory
{
    private final IBundleCategoryType type;

    private final IBundleContainer container;

    private LinkedHashMap<String, IBundleEntry> entries = new LinkedHashMap<String, IBundleEntry>();

    @Override
    public String getName()
    {
        return type.getValue();
    }

    @Override
    public IBundleCategoryType getType()
    {
        return type;
    }

    @Override
    public IBundleContainer getContainer()
    {
        return container;
    }

    @Override
    public IBundleLibrary getLibrary()
    {
        return container.getLibrary();
    }

    @Override
    public Collection<IBundleEntry> getEntries()
    {
        return entries.values();
    }

    public BundleCategory(IBundleContainer container, IBundleCategoryType type)
    {
        this.container = container;
        this.type = type;
    }

    @Override
    public IBundleEntry addFile(File file, String relativePath)
    {
        String path = BundleUtils.toPath(this, relativePath);
        BundleEntry entry = new BundleEntry(file, path);
        entries.put(path, entry);
        return entry;
    }

    @Override
    public IBundleEntry addFile(String bundlePath, String relativePath)
    {
        String path = relativePath;
        BundleEntry entry = new BundleEntry(bundlePath, relativePath);
        entries.put(path, entry);
        return entry;
    }

}
