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

import java.io.IOException;

import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleFileEntry;
import randori.compiler.bundle.IBundleLibrary;

/**
 * @author Michael Schmalle
 */
public abstract class BundleWriterBase implements IBundleWriter
{
    protected static final String MANIFEST_XML = "manifest.xml";

    protected BundleWriterBase()
    {
    }

    @Override
    public void write(IBundle bundle) throws IOException
    {
        if (bundle == null)
            throw new NullPointerException("IBundle model can't be null.");

        prepare(bundle);

        for (final IBundleLibrary library : bundle.getLibraries())
        {
            writeLibrary(library);
        }

        // Write the catalog after the library has been written
        // in case the library added a digest.
        writeManifest(bundle);

        //        for (final IBundleFileEntry entry : bundle.getFiles().values())
        //        {
        //            writeFile(entry);
        //        }

        finish(bundle);
    }

    /**
     * Before writing {@link IBundle} contents.
     * 
     * @param bundle {@link IBundle} model
     */
    abstract void prepare(IBundle bundle) throws IOException;

    /**
     * Write "manifest.xml" to the target {@link IBundle}.
     * 
     * @param bundle {@link IBundle} model.
     */
    abstract void writeManifest(IBundle bundle) throws IOException;

    /**
     * Add a library to the target {@link IBundle}.
     * 
     * @param library {@link IBundleLibrary} library.
     */
    abstract void writeLibrary(IBundleLibrary library) throws IOException;

    /**
     * Add a file entry to the target {@link IBundle}.
     * 
     * @param entry {@link IBundleFileEntry} library.
     */
    abstract void writeFile(IBundleFileEntry entry) throws IOException;

    /**
     * Clean up resources after writing out the {@link IBundle}.
     * 
     * @param bundle {@link IBundle} model.
     * @throws IOException
     */
    abstract void finish(IBundle bundle) throws IOException;

}
