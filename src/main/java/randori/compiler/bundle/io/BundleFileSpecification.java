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

import static org.apache.flex.compiler.filespecs.CombinedFile.getBOM;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.flex.compiler.filespecs.CombinedFile.BOM;
import org.apache.flex.compiler.filespecs.IBinaryFileSpecification;

import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleFileEntry;

/**
 * @author Michael Schmalle
 */
public class BundleFileSpecification implements IBinaryFileSpecification
{
    private final String path;

    private final IBundleFileEntry fileEntry;

    /**
     * @param path Filename contained within the manifest.
     * @param fileEntry IBundleFileEntry within the {@link IBundle}. May be null
     * if the file does not exist within the {@link IBundle}. FileNotFound
     * exception will be thrown if this IFileSpecification is read
     */
    public BundleFileSpecification(String path, IBundleFileEntry fileEntry)
    {
        this.path = path;
        this.fileEntry = fileEntry;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public Reader createReader() throws FileNotFoundException
    {
        if (fileEntry == null)
            throw new FileNotFoundException(path);

        try
        {
            return createNIOReader();
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
        catch (final IOException e)
        {
            // could not open the file, but the file seems to exist.
            // we can either return null, or maybe a reader that pretends
            // the file is empty or throws an IOException as soon as you try to read
            // from it.

            // We'll return a Reader that just throws the above IOException when
            // someone tries to read some characters.
            return new Reader() {
                @Override
                public int read(char[] cbuf, int off, int len)
                        throws IOException
                {
                    throw e;
                }

                @Override
                public void close() throws IOException
                {
                }
            };
        }
    }

    @Override
    public long getLastModified()
    {
        if (fileEntry == null)
            return Long.MAX_VALUE - 1;

        return fileEntry.getLastModified();
    }

    @Override
    public boolean isOpenDocument()
    {
        return false;
    }

    @Override
    public InputStream createInputStream() throws IOException
    {
        if (fileEntry == null)
            throw new FileNotFoundException(path);

        return fileEntry.createInputStream();
    }

    private Reader createNIOReader() throws IOException
    {
        InputStream strm = createInputStream();
        BufferedInputStream bufferedStrm;
        if (strm instanceof BufferedInputStream)
            bufferedStrm = (BufferedInputStream) strm;
        else
            bufferedStrm = new BufferedInputStream(strm);

        // Skip BOM header.
        final BOM bom = getBOM(bufferedStrm);
        bufferedStrm.skip(bom.pattern.length);

        return new BufferedReader(new InputStreamReader(bufferedStrm,
                bom.charset));
    }
}
