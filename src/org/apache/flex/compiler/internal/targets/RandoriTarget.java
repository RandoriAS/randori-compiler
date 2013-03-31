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

package org.apache.flex.compiler.internal.targets;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.flex.compiler.exceptions.BuildCanceledException;
import org.apache.flex.compiler.internal.projects.CompilerProject;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.targets.ITargetProgressMonitor;
import org.apache.flex.compiler.targets.ITargetReport;
import org.apache.flex.compiler.targets.ITargetSettings;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriApplication;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.driver.RandoriApplication;

import com.google.common.collect.Iterables;

/**
 * @author Michael Schmalle
 */
public class RandoriTarget extends Target implements IRandoriTarget
{

    private RootedCompilationUnits rootedCompilationUnits;

    protected IRandoriTargetSettings getSettings()
    {
        return (IRandoriTargetSettings) getTargetSettings();
    }

    public RandoriTarget(CompilerProject project,
            ITargetSettings targetSettings,
            ITargetProgressMonitor progressMonitor)
    {
        super(project, targetSettings, progressMonitor);
    }

    @Override
    public TargetType getTargetType()
    {
        return null;
    }

    @Override
    protected ITargetReport computeTargetReport() throws InterruptedException
    {
        return null;
    }

    @Override
    protected RootedCompilationUnits computeRootedCompilationUnits()
            throws InterruptedException
    {

        Collection<ICompilationUnit> units = project.getCompilationUnits();
        HashSet<ICompilationUnit> set = new HashSet<ICompilationUnit>();
        for (ICompilationUnit unit : units)
        {
            set.add(unit);
        }

        return new Target.RootedCompilationUnits(set,
                Collections.<ICompilerProblem> emptyList());

        //        return new Target.RootedCompilationUnits(
        //                Collections.<ICompilationUnit> emptySet(),
        //                Collections.<ICompilerProblem> emptyList());
    }

    @Override
    public RootedCompilationUnits getRootedCompilationUnits()
            throws InterruptedException
    {
        if (rootedCompilationUnits == null)
            rootedCompilationUnits = computeRootedCompilationUnits();
        return rootedCompilationUnits;
    }

    @Override
    public IRandoriApplication build(Collection<ICompilerProblem> problems)
    {
        buildStarted();
        try
        {
            Iterable<ICompilerProblem> fatalProblems = getFatalProblems();
            if (!Iterables.isEmpty(fatalProblems))
            {
                Iterables.addAll(problems, fatalProblems);
                return null;
            }

            Set<ICompilationUnit> compilationUnitSet = new HashSet<ICompilationUnit>();
            Target.RootedCompilationUnits rootedCompilationUnits = getRootedCompilationUnits();
            Iterables.addAll(problems, rootedCompilationUnits.getProblems());

            compilationUnitSet.addAll(rootedCompilationUnits.getUnits());

            // multithreaded parse, ast, scope, definition creation
            buildAndCollectProblems(compilationUnitSet, problems);

            // !!! end multithreaded parsing
            // all units have been parsed; scopes and definitions have been created
            List<ICompilationUnit> reachableCompilationUnits = project
                    .getReachableCompilationUnitsInSWFOrder(rootedCompilationUnits
                            .getUnits());

            IRandoriApplication application = initializeApplication(reachableCompilationUnits);
            return application;
        }
        catch (BuildCanceledException bce)
        {
            return null;
        }
        catch (InterruptedException ie)
        {
            return null;
        }
        finally
        {
            buildFinished();
        }
    }

    protected void buildAndCollectProblems(
            final Set<ICompilationUnit> compilationUnits,
            final Collection<ICompilerProblem> problems)
            throws InterruptedException
    {
        BuiltCompilationUnitSet builtCompilationUnits = getBuiltCompilationUnitSet();

        //        final ICompilationUnit rootCU = getRootClassCompilationUnit();
        //        compilationUnits.clear();
        //        compilationUnits.add(rootCU);

        Iterables.addAll(problems, builtCompilationUnits.problems);
    }

    private IRandoriApplication initializeApplication(
            List<ICompilationUnit> reachableCompilationUnits)
    {
        // we create the main model that will be passed back to the compiler 
        // for eventual generation. For now the Application is acting as the
        // backend renderer and this Target is the front end AST compilation unit
        // manager that passes it's results to the backend which is happening right here
        // Note; By passing the target settings here, we allow the Application to
        // configure itself using those settings and out job here is done.
        RandoriApplication application = new RandoriApplication(
                (FlexProject) project, reachableCompilationUnits, getSettings());

        return application;
    }

}
