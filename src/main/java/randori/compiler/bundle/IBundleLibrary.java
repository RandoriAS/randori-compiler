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

import org.apache.flex.swc.ISWC;

/**
 * The {@link IBundleLibrary} represents a grouping of related items that will
 * get packaged and assigned to a library by name and type.
 * <p>
 * The {@link IBundleLibrary} will handle the <code>bin</code>
 * {@link IBundleContainer} with an <code>swc</code> {@link IBundleCategory}
 * automatically.
 * 
 * @author Michael Schmalle
 */
public interface IBundleLibrary
{
    /**
     * The name of the {@link IBundleLibrary}.
     */
    String getName();

    /**
     * The path of the {@link IBundleLibrary}.
     * <p>
     * The path is used to create the relative url for the library within the
     * {@link IBundle}.
     */
    String getPath();

    /**
     * Returns the collection of {@link IBundleContainer} found within this
     * library.
     */
    Collection<IBundleContainer> getContainers();

    /**
     * Returns a specific {@link IBundleContainer} by type.
     * 
     * @param type The {@link IBundleContainerType}.
     */
    IBundleContainer getContainer(IBundleContainerType type);

    /**
     * Adds a new {@link IBundleContainer} by type.
     * 
     * @param type The new {@link IBundleContainer} type to add to the library.
     */
    IBundleContainer addContainer(IBundleContainerType type);

    /**
     * Returns a collection of {@link ISWC} entries in the <code>bin</code>
     * container.
     * <p>
     * Note: the <code>bin</code> container is an explicit container created by
     * the {@link IBundleLibrary} which allows custom api for swc access on the
     * library.
     */
    Collection<ISWC> getSWCS();

    /**
     * Returns an {@link ISWC} by name without the <code>.swc</code> extension.
     * 
     * @param name The name of the {@link ISWC} without extenison.
     * @return A {@link ISWC} instance or <code>null</code> if not found.
     */
    ISWC getSWC(String name);

    /**
     * Adds a {@link ISWC} instance to the <code>bin</code> container of the
     * {@link IBundleLibrary}.
     * 
     * @param swc The {@link ISWC} to add to the <code>bin</code> container.
     */
    void addSWC(ISWC swc);

}
