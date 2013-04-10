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

package randori.compiler.driver;

import java.util.Collection;
import java.util.zip.ZipOutputStream;

import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.targets.ITarget;

import randori.compiler.bundle.IBundle;

/**
 * The {@link IBundleTarget} builds an {@link IBundle} model to an output
 * stream.
 * 
 * @author Michael Schmalle
 */
public interface IBundleTarget extends ITarget
{
    /**
     * Builds this target and writes the resulting {@link IBundle} data to the
     * specified ZipOutputStream.
     * 
     * @param output ZipOutputStream to which entries are added for the
     * {@link IBundle} data.
     * @param problemCollection Collection to which any {@link ICompilerProblem}
     * are added.
     * @return true, when the {@link IBundle} was successfully compiled and
     * written to the specified channel. If false, then problemCollection will
     * have at least one entry explaining why the build failed.
     */
    boolean addToZipOutputStream(ZipOutputStream output,
            Collection<ICompilerProblem> problems);

    /**
     * Build the target {@link IBundle} and collect problems.
     * <p>
     * Every time the method is called, a new {@link IBundle} model is created.
     * 
     * @param problems compilation problems output
     * @return {@link IBundle} if build is success
     */
    IBundle build(Collection<ICompilerProblem> problems);
}
