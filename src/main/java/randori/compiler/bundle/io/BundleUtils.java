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

package randori.compiler.bundle.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.flex.swc.ISWC;
import org.apache.flex.swc.SWC;

import randori.compiler.bundle.Bundle;
import randori.compiler.bundle.BundleLibrary;
import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IBundleEntry;
import randori.compiler.bundle.IBundleLibrary;
import randori.compiler.bundle.IMutableBundle;

/**
 * @author Michael Schmalle
 */
public class BundleUtils
{
    private static final String SLASH = "/";

    private static final String DOT_SWC = ".swc";

    public static String toPath(IBundleCategory category, String path)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(category.getLibrary().getName());
        sb.append(SLASH);
        sb.append(category.getContainer().getName());
        sb.append(SLASH);
        sb.append(category.getType().getValue());
        sb.append(SLASH);
        sb.append(path);
        return sb.toString();
    }

    public static String toSWCName(ISWC swc)
    {
        return swc.getSWCFile().getName().replace(DOT_SWC, "");
    }

    public static IBundle getBundleFromRBL(File sdkRBL)
    {
        BundleReader reader = new BundleReader(sdkRBL.getAbsolutePath());
        IBundle bundle = reader.getBundle();
        return bundle;
    }

    public static IBundle getBundle(File sdkRootOrRBL)
    {
        IMutableBundle bundle = null;

        // get the sdk bundle or path
        if (sdkRootOrRBL.isDirectory())
        {
            File manifestFile = new File(sdkRootOrRBL, "manifest.xml");
            if (!manifestFile.exists())
            {
                //logger.error("manifest.xml does not exist in SDK path:" + sdkRoot.getPath());
                return null;
            }
            bundle = new Bundle(null);
            populateBundle(manifestFile, bundle);
        }
        else if (sdkRootOrRBL.isFile()
                && sdkRootOrRBL.getAbsolutePath().endsWith("rbl"))
        {
            BundleReader reader = new BundleReader(
                    sdkRootOrRBL.getAbsolutePath());
            bundle = (IMutableBundle) reader.getBundle();
        }

        return bundle;
    }

    public static void populateBundle(File manifestFile, IMutableBundle bundle)
    {
        StAXManifestReader manifestReader = null;
        try
        {
            manifestReader = new StAXManifestReader(new BufferedInputStream(
                    new FileInputStream(manifestFile)), bundle);
            manifestReader.parse();
        }
        catch (IOException e)
        {
            //logger.error(LogUtils.dumpStackTrace(Thread.currentThread().getStackTrace()));
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            //logger.error(LogUtils.dumpStackTrace(Thread.currentThread().getStackTrace()));
            e.printStackTrace();
        }
        finally
        {
            if (manifestReader != null)
            {
                try
                {
                    manifestReader.close();
                }
                catch (IOException e)
                {
                    //logger.error(LogUtils.dumpStackTrace(Thread.currentThread().getStackTrace()));
                    e.printStackTrace();
                }
            }
        }
    }

    // XXX throw a IOException or something
    public static void copyJSFilesFromBundle(File destinationDir, File sdkRoot,
            IBundle sdkBundle) throws IOException
    {
        // if sdkRoot is null, this means we are copying from the IBundle stream
        Collection<IBundleLibrary> libraries = sdkBundle.getLibraries();
        for (IBundleLibrary library : libraries)
        {
            copyJSFilesFromLibrary(destinationDir, sdkRoot, library);
        }
    }

    private static void copyJSFilesFromLibrary(File destinationDir,
            File sdkRoot, IBundleLibrary library)
    {
        IBundleContainer container = library
                .getContainer(IBundleContainer.Type.JS);
        if (container != null)
        {
            copyJSFilesFromContainer(destinationDir, sdkRoot, container);
        }
    }

    private static void copyJSFilesFromContainer(File destinationDir,
            File sdkRoot, IBundleContainer container)
    {
        IBundleCategory category = container
                .getCategory(IBundleCategory.Type.MONOLITHIC);
        if (category != null)
        {
            copyJSFilesFromCategory(destinationDir, sdkRoot, category);
        }
    }

    private static void copyJSFilesFromCategory(File destinationDir,
            File sdkRoot, IBundleCategory category)
    {
        Collection<IBundleEntry> entries = category.getEntries();
        for (IBundleEntry entry : entries)
        {
            copyJSEntry(destinationDir, sdkRoot, entry);
        }
    }

    // dest C:\Users\Work\Documents\git-randori\randori-compiler\temp\out\libs
    // entry:path randori-framework/js/mono/Randori.js
    private static void copyJSEntry(File destinationDir, File sdkRoot,
            IBundleEntry entry)
    {
        if (sdkRoot == null)
        {
            try
            {
                FileUtils.copyInputStreamToFile(entry.createInputStream(),
                        new File(destinationDir + File.separator
                                + new File(entry.getPath()).getName()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            File sourceFile = new File(sdkRoot.getPath() + File.separator
                    + entry.getPath());
            File destinationFile = new File(destinationDir.getPath()
                    + File.separator + sourceFile.getName());
            try
            {
                FileUtils.copyFile(sourceFile, destinationFile);
                //logger.debug("Copied JS file " + sourceFile.getName()
                //        + " to library path " + destinationDir.getPath());
            }
            catch (IOException e)
            {
                //logger.error(LogUtils.dumpStackTrace(Thread.currentThread()
                //        .getStackTrace()));
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the swcs into a temp directory from the {@link IBundle} libraries.
     * 
     * @param bundleFile The absolute location of the <code>.rbl</code> bundle
     * file archive.
     * @param tempOutput The temporary location the swcs are extracted to.
     * @return The {@link ISWC} collection.
     * @throws IOException
     */
    public static Collection<ISWC> tempWriteSWCs(File bundleFile,
            File tempOutput) throws IOException
    {
        BundleReader reader = new BundleReader(bundleFile.getPath());
        Bundle bundle = (Bundle) reader.getBundle();
        Collection<ISWC> result = new ArrayList<ISWC>();
        for (IBundleLibrary library : bundle.getLibraries())
        {
            BundleLibrary bl = (BundleLibrary) library;
            Collection<ISWC> swcs = bl.getSWCS(tempOutput,
                    bundle.getBundleFile());
            if (swcs != null)
            {
                result.addAll(swcs);
            }
        }
        return result;
    }

    public static Collection<ISWC> getSWCsFromBundleDir(File dirRoot)
            throws IOException
    {
        Bundle bundle = (Bundle) getBundle(dirRoot);
        Collection<ISWC> result = new ArrayList<ISWC>();
        for (IBundleLibrary library : bundle.getLibraries())
        {
            Collection<ISWC> swcs = new ArrayList<ISWC>();
            IBundleContainer container = library
                    .getContainer(IBundleContainer.Type.BIN);
            IBundleCategory category = container
                    .getCategory(IBundleCategory.Type.SWC);

            for (IBundleEntry entry : category.getEntries())
            {
                String path = entry.getPath();
                File file = new File(dirRoot, path);
                swcs.add(new SWC(file));
            }

            if (swcs != null)
            {
                result.addAll(swcs);
            }
        }
        return result;
    }
}
