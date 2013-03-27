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

import org.apache.flex.compiler.driver.IBackend;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.targets.ITarget;
import org.apache.flex.compiler.targets.ITargetProgressMonitor;
import org.apache.flex.compiler.targets.ITargetSettings;

import randori.compiler.internal.config.RandoriConfiguration;

/**
 * The {@link IRandoriTarget} interface allows the compiler an abstraction to
 * <em>how</em> the actual JavaScript is built.
 * <p>
 * The interface ties into the {@link IBackend} and is created at the start of
 * compile before the {@link RandoriConfiguration} class is configured.
 * 
 * @author Michael Schmalle
 * 
 * @see IBackend#createJSTarget(IASProject, ITargetSettings,
 * ITargetProgressMonitor)
 */
public interface IRandoriTarget extends ITarget
{
    /**
     * Build the target JavaScript application and collect problems. Every time
     * the method is called, a new {@link IRandoriApplication} model is created.
     * 
     * @param problems compilation problems output
     * @return IRandoriApplication if build is a success.
     */
    IRandoriApplication build(Collection<ICompilerProblem> problems);
}
