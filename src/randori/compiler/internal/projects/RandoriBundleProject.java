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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.flex.compiler.asdoc.IASDocBundleDelegate;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.swc.SWC;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.bundle.Bundle;
import randori.compiler.bundle.BundleLibrary;
import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleConfiguration;
import randori.compiler.bundle.IBundleConfigurationEntry;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IBundleContainer.Type;
import randori.compiler.bundle.IBundleVersion;
import randori.compiler.bundle.io.BundleWriter;
import randori.compiler.common.VersionInfo;
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

    IBundleConfiguration config;

    private IBundleConfiguration getConfig()
    {
        return config;
    }

    @Override
    public boolean configure(IBundleConfiguration config)
    {
        this.config = config;
        return true;
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
        File bundleFile = getTargetSettings().getOutput();

        // XXX probably save the bundle to a filed since we are in a project
        Bundle bundle = new Bundle(bundleFile);

        setVersionInfo(bundle);

        BundleLibrary library = new BundleLibrary(getConfig().getBundelName());
        for (String path : getConfig().getLibraryPaths())
        {
            library.addSWC(new SWC(new File(FilenameNormalization
                    .normalize(path))));
        }

        IBundleContainer container = library.addContainer(Type.JS);
        IBundleCategory category = container
                .addCategory(IBundleCategory.Type.MONO);

        for (IBundleConfigurationEntry entry : getConfig().getEntries())
        {
            category.addFile(entry.getJavaScript(), entry.getJavaScriptName());
        }

        // add the libraries to the bundle
        bundle.addLibrary(library);

        // write the bundle to disk
        BundleWriter writer;
        try
        {
            writer = new BundleWriter(bundle.getBundleFile().getAbsolutePath());
            writer.write(bundle);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private void setVersionInfo(IBundle bundle)
    {
        IBundleVersion version = bundle.getVersion();
        version.setBundleVersion(VersionInfo.getLibVersion());

        version.setRandoriVersion(VersionInfo.getRandoriVersion());
        version.setRandoriBuild(VersionInfo.getBuild());
        //version.setRandoriMinSupportedVersion(targetSettings.getMinimumSupportedVersion());
        version.setRandoriMinSupportedVersion("0");

        version.setCompilerName(VersionInfo.getCompilerName());
        version.setCompilerVersion(VersionInfo.getCompilerVersion());
        version.setCompilerBuild(VersionInfo.getCompilerBuild());
    }

}
