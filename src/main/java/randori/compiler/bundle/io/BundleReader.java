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
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.flex.compiler.problems.FileInLibraryIOProblem;
import org.apache.flex.compiler.problems.FileInLibraryNotFoundProblem;
import org.apache.flex.compiler.problems.LibraryNotFoundProblem;

import randori.compiler.bundle.Bundle;
import randori.compiler.bundle.IBundle;

/**
 * @author Michael Schmalle
 */
public class BundleReader implements IBundleReader
{
    static final String MANIFEST_XML = "manifest.xml";

    private File bundleFile;

    private Bundle bundle;

    private StAXManifestReader catalogReader;

    public BundleReader(String filename)
    {
        this.bundleFile = new File(filename);
        this.bundle = new Bundle(bundleFile);

        if (!bundleFile.exists() || !bundleFile.isFile())
        {
            bundle.addProblem(new LibraryNotFoundProblem(bundleFile
                    .getAbsolutePath()));
            return;
        }

        ZipFile zipFile = null;
        catalogReader = null;
        try
        {
            try
            {
                zipFile = new ZipFile(bundleFile, ZipFile.OPEN_READ);
                final InputStream catalogInputStream = getInputStream(zipFile,
                        MANIFEST_XML);
                if (catalogInputStream == null)
                {
                    bundle.addProblem(new FileInLibraryNotFoundProblem(
                            bundleFile.getAbsolutePath(), MANIFEST_XML));
                    return;
                }
                catalogReader = new StAXManifestReader(new BufferedInputStream(
                        catalogInputStream), bundle);
                catalogReader.parse();
                catalogReader.close();
                catalogReader = null;
            }
            catch (Exception e)
            {
                bundle.addProblem(new FileInLibraryIOProblem(MANIFEST_XML,
                        bundleFile.getAbsolutePath(), e.getLocalizedMessage()));
                return;
            }
        }
        finally
        {
            try
            {
                if (catalogReader != null)
                    catalogReader.close();

                if (zipFile != null)
                    zipFile.close();
            }
            catch (Exception e)
            {
                // ignore
            }
        }
    }

    @Override
    public File getFile()
    {
        return bundleFile;
    }

    @Override
    public IBundle getBundle()
    {
        return bundle;
    }

    public static InputStream getInputStream(ZipFile zipFile, String filename)
            throws IOException
    {
        ZipEntry zipEntry = null;
        for (final Enumeration<? extends ZipEntry> entryEnum = zipFile
                .entries(); entryEnum.hasMoreElements();)
        {
            final ZipEntry entry = entryEnum.nextElement();
            if (entry.getName().equals(filename))
            {
                zipEntry = entry;
                break;
            }
        }

        if (zipEntry == null)
            return null;
        else
            return zipFile.getInputStream(zipEntry);
    }
}
