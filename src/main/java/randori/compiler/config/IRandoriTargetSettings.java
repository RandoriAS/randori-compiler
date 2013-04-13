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

package randori.compiler.config;

import java.io.File;
import java.util.Collection;

import org.apache.flex.compiler.targets.ITargetSettings;

import randori.compiler.access.IASProjectAccess;
import randori.compiler.clients.Randori;
import randori.compiler.driver.IRandoriApplication;

/**
 * The {@link IRandoriTargetSettings} API creates a contract between the
 * {@link IRandoriApplication} and the {@link Randori} compiler's target output.
 * 
 * @author Michael Schmalle
 */
public interface IRandoriTargetSettings extends ITargetSettings
{
    IAnnotationManager getAnnotationManager();

    void setAnnotationManager(IAnnotationManager manager);
    
    IASProjectAccess getProjectAccess();

    void setProjectAccess(IASProjectAccess access);

    /**
     * Returns the path to an existing Randori SDK bundle extracted or zip.
     */
    String getSDKPath();

    /**
     * Returns the application name for the compile session.
     */
    String getAppName();

    /**
     * The base path that all output will be rendered to in the application's
     * project.
     * <p>
     * Note; As it stands, the randori libraries are dependent on the
     * <code>js-library-path</code> argument and their output location is not
     * determined with this argument.
     * 
     * @return The configured output base path.
     */
    String getJsBasePath();

    /**
     * The base path for the randori libraries.
     * 
     * @return The configured output base path.
     */
    String getJsLibraryPath();

    /**
     * When the <code>js-classes-as-files</code> argument is set, a file is
     * created for each exported class in the project, starting at the
     * <code>js-base-path</code>.
     * <p>
     * The output rendered here is based on the {@link #getJsBasePath()} path.
     * <p>
     * The default is <code>true</code>.
     * 
     * @return Whether to output class structure as directories based on their
     * package name(<code>true</code>) or as a monolithic file(
     * <code>false</code>).
     */
    boolean getJsClassesAsFiles();

    @Override
    Collection<File> getIncludeSources();

    Collection<String> getRawIncludeSources();

    Collection<String> getIncrementalFiles();

    void addIncrementalFile(String absoluteFilename);
}
