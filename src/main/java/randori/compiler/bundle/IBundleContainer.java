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

/**
 * @author Michael Schmalle
 */
public interface IBundleContainer
{
    /**
     * Default {@link IBundleContainer} types the core bundles implement.
     */
    public enum Type implements IBundleContainerType
    {
        /**
         * A binary container.
         */
        BIN("bin"),

        /**
         * A javascript source code container.
         */
        JS("js"),

        /**
         * A actionscript source code container.
         */
        AS("as"),

        /**
         * A actionscript document container.
         */
        ASDOC("asdoc");
        
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
     * Returns the container's {@link IBundleLibrary}.
     */
    IBundleLibrary getLibrary();

    /**
     * Returns the type of {@link IBundleContainer}.
     */
    IBundleContainerType getType();

    /**
     * The name of the container.
     */
    String getName();

    /**
     * Adds a {@link IBundleCategory} to the container by type.
     * 
     * @param type The {@link IBundleCategoryType} to add.
     * @return A new {@link IBundleCategory}.
     */
    IBundleCategory addCategory(IBundleCategoryType type);

    /**
     * Returns a collection of all the {@link IBundleCategory}s in this
     * container.
     */
    Collection<IBundleCategory> getCategories();

    /**
     * Returns a specific {@link IBundleCategory} by type held in this
     * container.
     * 
     * @param type The {@link IBundleCategoryType} type.
     * @return The {@link IBundleCategory}, <code>null</code> if not found.
     */
    IBundleCategory getCategory(IBundleCategoryType type);

}
