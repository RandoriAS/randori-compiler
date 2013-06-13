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

import java.util.Collection;

/**
 * @author Michael Schmalle
 */
public interface IBundleConfigurationEntry
{
    /**
     * Returns the name of the bundle library.
     * <p>
     * A {@link IBundle} is composed of libraries held within, this is the name
     * of a single library.
     */
    String getName();

    /**
     * Returns the <code>-bundle-library-path</code> arguments for the library.
     */
    Collection<String> getLibraryPaths();

    /**
     * Returns the <code>-bundle-external-library-path</code> arguments for the
     * library.
     */
    Collection<String> getExternalLibraryPaths();

    /**
     * Returns the <code>-bundle-source-path</code> arguments for the library.
     */
    Collection<String> getSourcePaths();

    /**
     * Returns the <code>-bundle-include-sources</code> arguments for the
     * library.
     */
    Collection<String> getIncludeSources();

    /**
     * Adds a library path to the library configuration.
     * 
     * @param path The library path.
     */
    void addLibraryPath(String path);

    /**
     * Adds an external library path to the library configuration.
     * 
     * @param path The external library path.
     */
    void addExternalLibraryPath(String path);

    /**
     * Adds a source path to the library configuration.
     * 
     * @param path The source path.
     */
    void addSourcePath(String path);

    /**
     * Adds a include sources path to the library configuration.
     * 
     * @param path The include sources path.
     */
    void addIncludeSources(String path);

}
