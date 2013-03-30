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

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author Michael Schmalle
 */
public class BundleContainer implements IBundleContainer
{
    private final IBundleLibrary library;

    private final IBundleContainerType type;

    private LinkedHashMap<IBundleContainerType, IBundleCategory> categories = new LinkedHashMap<IBundleContainerType, IBundleCategory>();

    @Override
    public IBundleLibrary getLibrary()
    {
        return library;
    }

    @Override
    public IBundleContainerType getType()
    {
        return type;
    }

    @Override
    public String getName()
    {
        return type.getValue();
    }

    public BundleContainer(IBundleLibrary library, IBundleContainerType type)
    {
        this.library = library;
        this.type = type;
    }

    @Override
    public IBundleCategory addCategory(IBundleCategoryType type)
    {
        IBundleCategory entry = new BundleCategory(this, type);
        categories.put(type, entry);
        return entry;
    }

    @Override
    public Collection<IBundleCategory> getCategories()
    {
        return categories.values();
    }

    @Override
    public IBundleCategory getCategory(IBundleCategoryType type)
    {
        return categories.get(type);
    }

}
