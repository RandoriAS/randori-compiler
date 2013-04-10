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

package randori.compiler.codegen.js;

import java.util.Collection;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.units.ICompilationUnit;

/**
 * The {@link ISessionModel} is the only class that will keep any kind of state
 * during one {@link ITypeDefinition} compile.
 * <p>
 * So we think of this as the "traverse session" model for a single
 * {@link ICompilationUnit}.
 * <p>
 * If multiple threads are used in the future, any access to members of this
 * class can have locks applied to synchronize against.
 * 
 * @author Michael Schmalle
 */
public interface ISessionModel
{
    /**
     * Adds an {@link ITypeDefinition} to the sessions dependency collection.
     * <p>
     * Note; The implementing class usually will apply rules to check whether
     * the type is already contained and other export/native rules.
     * 
     * @param definition The {@link ITypeDefinition} to add as a dependency of
     * the session.
     */
    void addDependency(ITypeDefinition definition);

    /**
     * Returns a collection of {@link ITypeDefinition} that have been added as
     * dependencies of the session.
     */
    Collection<ITypeDefinition> getDependencies();

    /**
     * Returns all {@link IVariableDefinition} <code>[Inject]</code> metadata
     * tags that have been encountered during the traversal of the
     * {@link ITypeDefinition}'s AST.
     * 
     * @return A {@link Collection} of <code>[Inject]</code> {@link IMetaTag}s.
     */
    Collection<IMetaTag> getPropertyInjections();

    /**
     * Returns all {@link IFunctionDefinition} <code>[Inject]</code> metadata
     * tags that have been encountered during the traversal of the
     * {@link ITypeDefinition}'s AST.
     * 
     * @return A {@link Collection} of <code>[Inject]</code> {@link IMetaTag}s.
     */
    Collection<IMetaTag> getMethodInjections();

    /**
     * Returns all {@link IVariableDefinition} <code>[View]</code> metadata tags
     * that have been encountered during the traversal of the
     * {@link ITypeDefinition}'s AST.
     * 
     * @return A {@link Collection} of <code>[View]</code> {@link IMetaTag}s.
     */
    Collection<IMetaTag> getViewInjections();

    /**
     * Adds a <code>Inject</code> tag if the definition contains one.
     * 
     * @param definition The {@link IDefinition} to check for
     * <code>Inject</code> metatag.
     */
    void addInjection(IDefinition definition);

    /**
     * Adds a <code>View</code> tag if the definition contains one.
     * <p>
     * Only possible on {@link IVariableDefinition} and
     * {@link IAccessorDefinition} definition.
     * 
     * @param definition The {@link IDefinition} to check for <code>View</code>
     * metatag.
     */
    void addViewInjection(IDefinition definition);

    /**
     * Whether the emitter is in an assignment state.
     */
    boolean isInAssignment();

    boolean setInAssignment(boolean value);

    /**
     * Hack; Whether the next operator will be skipped, this has to do with
     * reducing things like <code>Window.window</code> to <code>window</code> or
     * <code>window.setTimeout()</code> to <code>setTimeout()</code>.
     */
    boolean skipOperator();

    boolean setSkipOperator(boolean value);

}
