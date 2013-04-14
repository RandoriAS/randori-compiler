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

package randori.compiler.internal.access;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.utils.FilenameNormalization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.access.IASProjectAccess;
import randori.compiler.clients.CompilerArguments;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.internal.client.RandoriCompilerTestBase;
import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.projects.RandoriApplicationProject;
import randori.compiler.plugin.IPreProcessPlugin;

/**
 * @author Michael Schmalle
 */
public class SimpleAccessorTest extends RandoriCompilerTestBase
{
    // the test has to run the compiler
    protected File srcAccessorDir = new File(
            FilenameNormalization.normalize(TestConstants.RandoriASFramework
                    + "/randori-compiler/src/test/resources/as/src_accessor"));

    private Workspace workspace;

    private RandoriApplicationProject project;

    @SuppressWarnings("unused")
    private ArrayList<ICompilerProblem> problems;

    private CompilerArguments arguments;

    @Override
    protected List<ICompilerProblem> getProblems()
    {
        return project.getProblemQuery().getProblems();
    }

    protected IRandoriTargetSettings getTargetSettings()
    {
        return project.getTargetSettings();
    }

    @Override
    @Before
    public void setUp()
    {
        workspace = new Workspace();
        project = new RandoriApplicationProject(workspace);
        project.getPluginFactory().registerPlugin(IPreProcessPlugin.class,
                MyPlugin.class);

        problems = new ArrayList<ICompilerProblem>();
        arguments = new CompilerArguments();

        arguments.addLibraryPath(builtinSWC.getAbsolutePath());
        arguments.addSourcepath(srcAccessorDir.getAbsolutePath());
    }

    @Override
    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Test
    public void test_init()
    {
        project.configure(arguments.toArguments());
        project.compile(false);

        Collection<ITypeDefinition> types = getAccess().getTypes();
        //assertEquals(10, types.size());

        //assertEquals(4, getAccess().getClasses().size());
        //assertEquals(6, getAccess().getInterfaces().size());
    }

    @Test
    public void test_getSubClasses()
    {
        project.configure(arguments.toArguments());
        project.compile(false);

        IClassDefinition type = (IClassDefinition) getAccess().getType(
                "one.two.ClassOneTwo1");
        assertNotNull(type);
        assertEquals(2, getAccess().getSubClasses(type).size());
    }

    @Test
    public void test_getInterfaceImplementors()
    {
        project.configure(arguments.toArguments());
        project.compile(false);

        IInterfaceDefinition type = (IInterfaceDefinition) getAccess().getType(
                "one.two.api.InterfaceB");
        assertNotNull(type);
        assertEquals(1, getAccess().getInterfaceImplementors(type).size());
    }

    private IASProjectAccess getAccess()
    {
        return getTargetSettings().getProjectAccess();
    }
}
