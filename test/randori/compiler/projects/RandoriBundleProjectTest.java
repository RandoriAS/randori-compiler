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

import java.io.File;
import java.io.IOException;

import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.bundle.BundleConfiguration;
import randori.compiler.bundle.IBundleConfigurationEntry;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.projects.RandoriBundleProject;

/**
 * @author Michael Schmalle
 */
public class RandoriBundleProjectTest
{
    File builtinSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-sdk\\randori-framework\\bin\\swc\\builtin.swc");
    File htmlCoreLibSWC = new File(TestConstants.RandoriASFramework
            + "\\randori-sdk\\randori-framework\\bin\\swc\\HTMLCoreLib.swc");
    File jQuerySWC = new File(TestConstants.RandoriASFramework
            + "\\randori-sdk\\randori-framework\\bin\\swc\\JQuery.swc");

    String randoriSrc = TestConstants.RandoriASFramework
            + "\\randori-framework\\src";
    String randoriGuiceSrc = TestConstants.RandoriASFramework
            + "\\randori-guice-framework\\src";

    private BundleConfiguration configuration;

    private Workspace workspace;

    private RandoriBundleProject project;

    @Before
    public void setUp() throws IOException
    {
        workspace = new Workspace();
        createSDKConfiguration();
        project = new RandoriBundleProject(workspace);
    }

    @After
    public void tearDown()
    {
        configuration = null;
        workspace = null;
        project = null;
    }

    @Test
    public void test_compile()
    {
        project.configure(configuration);
        boolean success = project.compile(true, true);
        Assert.assertTrue(success);
    }

    private void createSDKConfiguration() throws IOException
    {
        String path = TestConstants.RandoriASFramework
                + "\\randori-compiler\\temp\\bundle\\randori-sdk-0.2.3.rbl";

        configuration = new BundleConfiguration("randori-sdk", path);

        // dependent compiled libraries
        configuration.addLibraryPath(builtinSWC.getAbsolutePath());
        configuration.addLibraryPath(jQuerySWC.getAbsolutePath());
        configuration.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());

        IBundleConfigurationEntry randori = configuration
                .addEntry("randori-framework");
        randori.addSourcePath(randoriGuiceSrc);
        randori.addSourcePath(randoriSrc);
        randori.addIncludeSources(randoriSrc);

        IBundleConfigurationEntry guice = configuration
                .addEntry("randori-guice-framework");
        guice.addSourcePath(randoriGuiceSrc);
    }
}
