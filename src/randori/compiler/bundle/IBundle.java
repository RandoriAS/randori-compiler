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

import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.swc.ISWC;

/**
 * The {@link IBundle} API is the main interface into a randori bundle model.
 * <p>
 * This model is used to manage;
 * <ul>
 * <li>The {@link IBundleVersion} of the bundle from which the bundle was
 * created from.</li>
 * <li>A collection of {@link IBundleLibrary}s that were saved with the bundle.</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public interface IBundle
{
    /**
     * Returns the {@link File} instance for the {@link IBundle}.
     */
    File getBundleFile();

    /**
     * The {@link IBundleVersion} of the bundle when it was created by the
     * compiler.
     */
    IBundleVersion getVersion();

    /**
     * A collection of {@link IBundleLibrary}s contained within the
     * {@link IBundle}.
     */
    Collection<IBundleLibrary> getLibraries();

    /**
     * Returns a library located at the specific path which was the name given
     * for the library during the bundles compile.
     * 
     * @param name The library name.
     */
    IBundleLibrary getLibrary(String name);

    /**
     * All {@link ISWC} libraries contained in an {@link IBundleLibrary}.
     * 
     * @param libraryName The name of the {@link IBundleLibrary} to return
     * {@link ISWC} collection for.
     */
    Collection<ISWC> getSWCLibraries(String libraryName);

    /**
     * Returns a specific {@link ISWC} instance using the {@link IBundleLibrary}
     * name and the name of the {@link ISWC} without the <code>.swc</code>
     * extension.
     * 
     * @param libraryName The name of the {@link IBundleLibrary}.
     * @param swcName The name of the {@link ISWC} without the <code>.swc</code>
     * extension.
     */
    ISWC getSWCLibrary(String libraryName, String swcName);

    /**
     * Returns a collection of {@link IBundleContainer} defined in the
     * {@link IBundle}.
     * 
     * @param libraryName The name of the {@link IBundleLibrary}.
     */
    Collection<IBundleContainer> getContainers(String libraryName);

    /**
     * Returns the {@link IBundleContainer} for the {@link IBundleLibrary} name
     * passed by type.
     * <p>
     * The {@link IBundleContainerType} is a unique container found within the
     * {@link IBundleLibrary}.
     * 
     * @param libraryName The name of the {@link IBundleLibrary}.
     * @param type The {@link IBundleContainerType} of the
     * {@link IBundleContainer}.
     */
    IBundleContainer getContainer(String libraryName, IBundleContainerType type);

    // For now we are not implementing direct file addition through this API
    //Map<String, IBundleFileEntry> getFiles();

    //IBundleFileEntry getFile(String fileName);

    /**
     * Returns a collection of {@link ICompilerProblem}s that may have been
     * produced during a read or write of the {@link IBundle}.
     */
    Collection<ICompilerProblem> getProblems();

}
