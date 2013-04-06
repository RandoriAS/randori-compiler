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

package randori.compiler.bundle.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleFileEntry;

/**
 * A {@link IBundle} archive.
 * <p>
 * Used when reading the {@link IBundle} with the {@link IBundleReader}.
 * 
 * @author Michael Schmalle
 */
public class BundleFileEntry implements IBundleFileEntry
{
    private final String containingBundlePath;

    private final String path;

    private final long lastModified;

    public BundleFileEntry(String containingBundlePath, String path,
            long modificationTime)
    {
        this.containingBundlePath = containingBundlePath;
        this.path = path;
        this.lastModified = modificationTime;
    }

    @Override
    public String getContainingBundlePath()
    {
        return containingBundlePath;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public long getLastModified()
    {
        return lastModified;
    }

    @Override
    public InputStream createInputStream() throws IOException
    {
        ZipFile file = new ZipFile(containingBundlePath);
        InputStream inputStream = BundleReader.getInputStream(file, path);
        return inputStream;
    }

}
