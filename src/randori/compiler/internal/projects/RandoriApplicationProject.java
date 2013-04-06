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

package randori.compiler.internal.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.projects.CompilerProject;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.problems.InternalCompilerProblem;
import org.apache.flex.compiler.problems.UnableToBuildSWFProblem;

import randori.compiler.driver.IRandoriApplication;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.projects.IRandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class RandoriApplicationProject extends RandoriProject implements
        IRandoriApplicationProject
{

    private IRandoriTarget target;

    public RandoriApplicationProject(Workspace workspace)
    {
        super(workspace, IASDocBundleDelegate.NIL_DELEGATE, new RandoriBackend());
    }

    protected boolean startCompile(boolean doBuild)
    {
        boolean compilationSuccess = false;

        try
        {
            target = createTarget(getTargetSettings(), null);

            IRandoriApplication application = build();

            if (target != null)
            {
                Collection<ICompilerProblem> errors = new ArrayList<ICompilerProblem>();
                Collection<ICompilerProblem> warnings = new ArrayList<ICompilerProblem>();

                if (!getConfiguration().getCreateTargetWithErrors())
                {
                    getProblemQuery().getErrorsAndWarnings(errors, warnings);
                    if (errors.size() > 0)
                        return false;
                }

                // for now we let warnings pass
                getProblemQuery().getErrorsAndWarnings(errors, warnings);
                if (errors.size() > 0)
                    return false;

                if (doBuild)
                {
                    compilationSuccess = buildTarget(application);
                }
                else
                {
                    compilationSuccess = true;
                }
            }
        }
        catch (Exception e)
        {
            final ICompilerProblem problem = new InternalCompilerProblem(e);
            getProblemQuery().add(problem);
        }

        return compilationSuccess;
    }

    private boolean buildTarget(IRandoriApplication application)
    {
        return application.compile(getBackend(), getProblemQuery());
    }

    private IRandoriApplication build() throws InterruptedException,
            IOException, ConfigurationException
    {
        final List<ICompilerProblem> buildProblems = new ArrayList<ICompilerProblem>();

        final IRandoriApplication app = buildApplication(this, buildProblems);

        getProblemQuery().addAll(buildProblems);

        if (app == null)
        {
            ICompilerProblem problem = new UnableToBuildSWFProblem(
                    getConfiguration().getOutput());
            getProblemQuery().add(problem);
        }

        return app;
    }

    private IRandoriApplication buildApplication(CompilerProject project,
            Collection<ICompilerProblem> problems) throws InterruptedException
    {
        Collection<ICompilerProblem> fatalProblems = project.getFatalProblems();

        if (!fatalProblems.isEmpty())
        {
            problems.addAll(fatalProblems);
            return null;
        }

        return target.build(problems);
    }

    @Override
    protected void validateConfiguration() throws ConfigurationException
    {
    }

    @Override
    protected boolean export()
    {
        return false;
    }
}
