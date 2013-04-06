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

package randori.compiler.bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import randori.compiler.bundle.io.BundleReader;
import randori.compiler.bundle.io.IBundleReader;

/**
 * @author Michael Schmalle
 */
public class BundleEntry implements IBundleEntry
{

    private File file;

    private final String path;

    private String bundlePath;

    /**
     * Creates an entry from the {@link IBundleReader}.
     * 
     * @param path The path of the entry in the {@link IBundle}.
     */
    public BundleEntry(String bundlePath, String path)
    {
        this.bundlePath = bundlePath;
        this.path = path;
    }

    public BundleEntry(File file, String path)
    {
        this.file = file;
        this.path = path;
    }

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public InputStream createInputStream() throws IOException
    {
        ZipFile bundleFile = new ZipFile(bundlePath);
        InputStream inputStream = BundleReader.getInputStream(bundleFile, path);
        return inputStream;
    }
}
