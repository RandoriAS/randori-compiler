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

package randori.compiler.projects;

import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.projects.IASProject;

import randori.compiler.access.IASProjectAccess;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.config.annotation.IAnnotationManager;
import randori.compiler.internal.config.RandoriConfiguration;
import randori.compiler.plugin.factory.IPluginFactory;

/**
 * The {@link IRandoriProject} API allows clients to access compiler projects
 * for the Randori framework implementation in ActionScript.
 * 
 * @author Michael Schmalle
 */
public interface IRandoriProject extends IASProject
{
    /**
     * Returns the project's {@link ProblemQuery} specific to the current
     * session.
     * 
     * @return
     */
    ProblemQuery getProblemQuery();

    /**
     * Returns the project's {@link IPluginFactory} that can have compiler
     * plugins registered against.
     */
    IPluginFactory getPluginFactory();

    /**
     * Returns the project's {@link IRandoriTargetSettings} that have been
     * configured by the compiler arguments of the custom {@link Configuration}.
     */
    IRandoriTargetSettings getTargetSettings();

    /**
     * Returns the project's {@link IAnnotationManager} that is responsible for
     * tracking the annotations used in the compiler.
     */
    IAnnotationManager getAnnotationManager();

    /**
     * Returns the project's {@link IASProjectAccess} that allows symbol table
     * query API.
     */
    IASProjectAccess getProjectAccess();

    /**
     * Configures the project with an array of compiler arguments specified in
     * the {@link RandoriConfiguration} class.
     * 
     * @param args The String compiler arguments for the compiler.
     * @return A boolean indicating success.
     */
    boolean configure(String[] args);

    /**
     * Parses and builds the project's build target if the <code>doBuild</code>
     * argument is true.
     * 
     * @param doBuild Whether to call <code>build()</code> on the porject's
     * build target.
     * @return A boolean indicating success.
     */
    boolean compile(boolean doBuild);

    /**
     * Parses and builds the project's build target if the <code>doBuild</code>
     * argument is true and will call the <code>export()</code> method of the
     * project if <code>doExport</code> is true.
     * 
     * @param doBuild Whether to call <code>build()</code> on the porject's
     * build target.
     * @param doExport Whether to call the project's <code>export()</code>
     * method after build.
     * @return A boolean indicating success.
     */
    boolean compile(boolean doBuild, boolean doExport);

}
