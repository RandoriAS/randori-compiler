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

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.workspaces.Workspace;

import randori.compiler.internal.driver.RandoriBackend;
import randori.compiler.projects.IRandoriBundleProject;

/**
 * @author Michael Schmalle
 */
public class RandoriBundleProject extends RandoriProject implements
        IRandoriBundleProject
{

    public RandoriBundleProject(Workspace workspace)
    {
        super(workspace, IASDocBundleDelegate.NIL_DELEGATE,
                new RandoriBackend());
    }

    @Override
    public boolean configure(String[] args)
    {
        return super.configure(args);
    }

    @Override
    public boolean compile(boolean doBuild)
    {
        return compile(doBuild, false);
    }

    @Override
    public boolean compile(boolean doBuild, boolean doExport)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void validateConfiguration() throws ConfigurationException
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean startCompile(boolean doBuild)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean export()
    {
        return false;
    }
}
