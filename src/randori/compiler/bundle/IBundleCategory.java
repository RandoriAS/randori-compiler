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

/**
 * @author Michael Schmalle
 */
public interface IBundleCategory
{
    /**
     * Default {@link IBundleCategory} types the core bundles implement.
     */
    public enum Type implements IBundleCategoryType
    {
        /**
         * A binary <code>swc</code> category.
         */
        SWC("swc"),

        /**
         * A source code category.
         */
        CLASSES("classes"),

        /**
         * A source code minimized category.
         */
        CLASSES_MINIFIED("classes_minified"),

        /**
         * A monolithic non minimized source code category.
         */
        MONOLITHIC("monolithic"),

        /**
         * A monolithic minimized source code category.
         */
        MONOLITHIC_MINIFIED("monolithic_minified");

        private String value;

        Type(String value)
        {
            this.value = value;
        }

        @Override
        public String getValue()
        {
            return value;
        }

        public static Type toType(String type)
        {
            for (Type iterable : values())
            {
                if (iterable.getValue().equals(type))
                    return iterable;
            }
            return null;
        }
    }

    /**
     * Returns the name of this {@link IBundleCategory} which is the String
     * representation of it's {@link #getType()}.
     */
    String getName();

    /**
     * Returns the type of {@link IBundleCategory}.
     */
    IBundleCategoryType getType();

    /**
     * Adds a file entry to this {@link BundleCategory}.
     * 
     * @param file The full path he the {@link File} location on disk.
     * @param relativePath The path given to the serializer when this file is
     * added to the {@link IBundle}.
     */
    IBundleEntry addFile(File file, String relativePath);

    IBundleEntry addFile(String bundlePath, String relativePath);

    /**
     * Returns a collection of all entries found on the {@link IBundleCategory}.
     */
    Collection<IBundleEntry> getEntries();

    /**
     * Returns the containing {@link IBundleLibrary}.
     */
    IBundleLibrary getLibrary();

    /**
     * Returns the containing {@link IBundleContainer}.
     */
    IBundleContainer getContainer();

}
