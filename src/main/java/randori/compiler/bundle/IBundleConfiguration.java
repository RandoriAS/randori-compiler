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
 * The {@link IBundleConfiguration} allows the bundle compiler to be configured
 * from the out side without using string compiler arguments.
 * 
 * @author Michael Schmalle
 */
public interface IBundleConfiguration
{
    /**
     * The output directory of the compiled {@link IBundle}.
     */
    String getOutput();

    /**
     * The {@link IBundle#getBundleFile()}'s name used when writting to disk on
     * the output path.
     */
    String getBundelName();

    /**
     * Returns a collection of all <code>-library-path</code> compiler arguments
     * used for the bundle compiler.
     */
    Collection<String> getLibraryPaths();

    /**
     * Returns a collection of all <code>-bundle-path</code> compiler arguments
     * used for the bundle compiler.
     */
    Collection<String> getBundlePaths();

    /**
     * Returns the entries of the bundle compiler that will be compiled into
     * individual libraries within the bundle.
     */
    Collection<IBundleConfigurationEntry> getEntries();

    /**
     * Adds a <code>-library-path</code> compiler argument that will be used for
     * all bundle entry compilation.
     * 
     * @param path The path to the swc library.
     */
    void addLibraryPath(String path);

    /**
     * Adds a <code>-bundle-path</code> compiler argument that will be used for
     * all bundle entry compilation.
     * 
     * @param path The path to the rbl bundle.
     */
    void addBundlePath(String path);

    /**
     * Returns an entry that exists as a bundle library within the
     * configuration.
     * 
     * @param name The name of the library entry to return.
     */
    IBundleConfigurationEntry getEntry(String name);

}
