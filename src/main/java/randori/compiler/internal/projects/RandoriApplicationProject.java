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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.projects.CompilerProject;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ConfigurationProblem;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.problems.InternalCompilerProblem;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.io.BundleUtils;
import randori.compiler.driver.IRandoriApplication;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.problems.UnableToBuildApplicationProblem;
import randori.compiler.projects.IRandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class RandoriApplicationProject extends RandoriProject implements
        IRandoriApplicationProject
{

    private IRandoriTarget target;

    public RandoriApplicationProject(Workspace workspace,
            IRandoriBackend backend)
    {
        super(workspace, IASDocBundleDelegate.NIL_DELEGATE, backend);
    }

    public RandoriApplicationProject(Workspace workspace)
    {
        super(workspace, IASDocBundleDelegate.NIL_DELEGATE,
                new RandoriBackend());
    }

    @Override
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
            ICompilerProblem problem = new UnableToBuildApplicationProblem(
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

        IRandoriApplication application = target.build(problems);

        application.analyze(getProblemQuery());

        return application;
    }

    @Override
    protected void validateConfiguration() throws ConfigurationException
    {
        List<File> files = new ArrayList<File>();

        populateSDKBundleOrPath(files, getTargetSettings().getOutput());
        mergeLibraries(files);
    }

    @Override
    protected boolean export()
    {
        // XXX If the SDK Path is specified, not need to pass SWCS?
        String path = getTargetSettings().getSDKPath();
        // normailze with "" will return the execution root
        File file = new File(FilenameNormalization.normalize(path));
        if (!new File(path).exists())
        {
            // if the compiler is trying to export with compile()
            // this is a failed compile, return false
            final ICompilerProblem problem = new ConfigurationProblem(null, -1,
                    -1, -1, -1, "sdk-path " + file.getAbsolutePath()
                            + " is not a directory or valid bundle");
            getProblemQuery().add(problem);
            return false;
        }

        IBundle bundle = BundleUtils.getBundle(file);
        File libraryDir = setupLibraryDirectory();
        if (!file.isDirectory())
            file = null;

        try
        {
            BundleUtils.copyJSFilesFromBundle(libraryDir, file, bundle);
        }
        catch (IOException e)
        {
            // TODO final ICompilerProblem problem;
            //getProblemQuery().add(problem);
            return false;
        }

        return true;
    }

    @Override
    protected void finish()
    {
        super.finish();
    }

    private File setupLibraryDirectory()
    {
        File outputDir = new File(getTargetSettings().getOutput()
                .getAbsolutePath());
        if (!outputDir.exists())
            outputDir.mkdirs();

        File libraryDir = new File(outputDir, getTargetSettings()
                .getJsLibraryPath());
        if (!libraryDir.exists())
        {
            //logger.debug("The library path '" + libPath + "' doesn't exist, creating it now.");
            libraryDir.mkdirs();
        }

        return libraryDir;
    }

}
