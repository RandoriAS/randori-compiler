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

package randori.compiler.bundle;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.internal.constants.TestConstants;

/**
 * @author Michael Schmalle
 */
public class BundleConfigurationTest
{
    private BundleConfiguration config;

    File builtinSWC = new File(TestConstants.RandoriASFramework
            + "/randori-sdk/randori-framework/bin/swc/builtin.swc");
    File htmlCoreLibSWC = new File(TestConstants.RandoriASFramework
            + "/randori-sdk/randori-framework/bin/swc/HTMLCoreLib.swc");
    File jQuerySWC = new File(TestConstants.RandoriASFramework
            + "/randori-sdk/randori-framework/bin/swc/JQuery.swc");

    String randoriSrc = TestConstants.RandoriASFramework
            + "/randori-framework/src";
    String randoriGuiceSrc = TestConstants.RandoriASFramework
            + "/randori-guice-framework/src";

    @Before
    public void setUp() throws IOException
    {
        config = new BundleConfiguration("foo", "/foo/output/path");
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void test_getBundelName()
    {
        Assert.assertEquals("foo", config.getBundelName());
    }

    @Test
    public void test_getOutput()
    {
        Assert.assertEquals("/foo/output/path", config.getOutput());
    }

    @Test
    public void test_addGet_LibraryPaths()
    {
        config.addLibraryPath("/foo/One.swc");
        config.addLibraryPath("/foo/Two.swc");
        config.addLibraryPath("/foo/Three.swc");
        // test duplicate
        config.addLibraryPath("/foo/One.swc");
        Assert.assertEquals(3, config.getLibraryPaths().size());
    }

    @Test
    public void test_addGet_Entries()
    {
        IBundleConfigurationEntry foo = config.addEntry("Foo");
        IBundleConfigurationEntry bar = config.addEntry("Bar");
        IBundleConfigurationEntry goo = config.addEntry("Goo");
        Assert.assertEquals(3, config.getEntries().size());
        Assert.assertTrue(config.getEntries().contains(foo));
        Assert.assertTrue(config.getEntries().contains(bar));
        Assert.assertTrue(config.getEntries().contains(goo));
        Assert.assertEquals("Foo", config.getEntry("Foo").getName());
        Assert.assertEquals("Bar", config.getEntry("Bar").getName());
        Assert.assertEquals("Goo", config.getEntry("Goo").getName());
    }

    @Test
    public void test_Entry_addGet_SourcePath()
    {
        IBundleConfigurationEntry foo = config.addEntry("Foo");
        foo.addSourcePath("foo/src");
        foo.addSourcePath("bar/src");
        foo.addSourcePath("goo/src");
        foo.addSourcePath("foo/src");
        Assert.assertEquals(3, foo.getSourcePaths().size());
    }

    @Test
    public void test_Entry_getLibraryPaths()
    {
        config.addLibraryPath("/foo/One.swc");
        config.addLibraryPath("/foo/Two.swc");
        config.addLibraryPath("/foo/Three.swc");
        config.addLibraryPath("/foo/Three.swc");
        IBundleConfigurationEntry foo = config.addEntry("Foo");
        // foo entry inherits the librarys
        Assert.assertEquals(3, foo.getLibraryPaths().size());
    }

    @Test
    public void test_toArguments()
    {
        String path = TestConstants.RandoriASFramework
                + "/randori-compiler/temp/bundle/randori-sdk-test.rbl";

        BundleConfiguration configuration = new BundleConfiguration(
                "randori-framework", path);

        // dependent compiled libraries
        configuration.addExternalLibraryPath(builtinSWC.getAbsolutePath());
        configuration.addExternalLibraryPath(jQuerySWC.getAbsolutePath());
        configuration.addExternalLibraryPath(htmlCoreLibSWC.getAbsolutePath());

        IBundleConfigurationEntry randori = configuration
                .addEntry("randori-framework");
        randori.addSourcePath(randoriGuiceSrc);
        randori.addSourcePath(randoriSrc);
        randori.addIncludeSources(randoriSrc);

        IBundleConfigurationEntry guice = configuration
                .addEntry("randori-guice-framework");
        guice.addSourcePath(randoriGuiceSrc);
        guice.addLibraryPath(builtinSWC.getAbsolutePath());
        guice.addLibraryPath(jQuerySWC.getAbsolutePath());
        guice.addLibraryPath(htmlCoreLibSWC.getAbsolutePath());

        String string = configuration.toString();
        Assert.assertEquals(
                "-external-library-path=C:\\Users\\Work\\Documents\\git-randori\\randori-sdk"
                        + "\\randori-framework\\bin\\swc\\builtin.swc -external-library-path=C:\\"
                        + "Users\\Work\\Documents\\git-randori\\randori-sdk\\randori-framework\\"
                        + "bin\\swc\\JQuery.swc -external-library-path=C:\\Users\\Work\\Documents"
                        + "\\git-randori\\randori-sdk\\randori-framework\\bin\\swc\\HTMLCoreLib.swc"
                        + " -bundle-libraries=randori-guice-framework,randori-framework -output="
                        + "C:/Users/Work/Documents/git-randori/randori-compiler/temp/bundle/randori"
                        + "-sdk-test.rbl -bundle-source-path=randori-guice-framework,C:/Users/Work/"
                        + "Documents/git-randori/randori-guice-framework/src -bundle-library-path="
                        + "randori-guice-framework,C:\\Users\\Work\\Documents\\git-randori\\randori-"
                        + "sdk\\randori-framework\\bin\\swc\\builtin.swc -bundle-library-path=randori"
                        + "-guice-framework,C:\\Users\\Work\\Documents\\git-randori\\randori-sdk\\"
                        + "randori-framework\\bin\\swc\\JQuery.swc -bundle-library-path=randori-guice"
                        + "-framework,C:\\Users\\Work\\Documents\\git-randori\\randori-sdk\\randori"
                        + "-framework\\bin\\swc\\HTMLCoreLib.swc -bundle-source-path=randori-framework,"
                        + "C:/Users/Work/Documents/git-randori/randori-guice-framework/src -bundle-"
                        + "source-path=randori-framework,C:/Users/Work/Documents/git-randori/randori"
                        + "-framework/src -bundle-include-sources=randori-framework,C:/Users/Work/"
                        + "Documents/git-randori/randori-framework/src", string);

        // now from arguments to the BundleConfiguration instance

        //BundleConfiguration.create(configuration.toArguments());

    }
}
