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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.swc.ISWC;

/**
 * @author Michael Schmalle
 */
public class Bundle implements IBundle
{

    private final File file;

    private final BundleVersion version;

    private final Collection<ICompilerProblem> problems = new ArrayList<ICompilerProblem>();

    //private final HashMap<String, IBundleFileEntry> files;

    private final HashMap<String, IBundleLibrary> libraries;

    public Bundle(File file)
    {
        this.file = file;

        libraries = new HashMap<String, IBundleLibrary>();
        //files = new HashMap<String, IBundleFileEntry>();
        version = new BundleVersion();
    }

    @Override
    public File getBundleFile()
    {
        return file;
    }

    @Override
    public IBundleVersion getVersion()
    {
        return version;
    }

    @Override
    public Collection<IBundleLibrary> getLibraries()
    {
        return libraries.values();
    }

    @Override
    public IBundleLibrary getLibrary(String name)
    {
        return libraries.get(name);
    }

    @Override
    public Collection<IBundleContainer> getContainers(String libraryName)
    {
        IBundleLibrary library = getLibrary(libraryName);
        if (library == null)
            return null;

        return library.getContainers();
    }

    @Override
    public IBundleContainer getContainer(String libraryName,
            IBundleContainerType type)
    {
        IBundleLibrary library = getLibrary(libraryName);
        if (library == null)
            return null;

        return library.getContainer(type);
    }

    @Override
    public Collection<ISWC> getSWCLibraries(String libraryName)
    {
        IBundleLibrary library = getLibrary(libraryName);
        if (library == null)
            return null;

        return library.getSWCS();
    }

    @Override
    public ISWC getSWCLibrary(String libraryName, String swcName)
    {
        IBundleLibrary library = getLibrary(libraryName);
        if (library == null)
            return null;

        return library.getSWC(swcName);
    }

    //    @Override
    //    public Map<String, IBundleFileEntry> getFiles()
    //    {
    //        return files;
    //    }
    //
    //    public IBundleFileEntry getFile(String fileName)
    //    {
    //        return files.get(fileName);
    //    }
    //
    //    public void addFile(IBundleFileEntry file)
    //    {
    //        files.put(file.getPath(), file);
    //    }

    @Override
    public Collection<ICompilerProblem> getProblems()
    {
        return problems;
    }

    public void addLibrary(IBundleLibrary library)
    {
        libraries.put(library.getPath(), library);
    }

    /**
     * Add a file that will be written into {@link IBundle} file during
     * creation.
     * 
     * @param path destination path of the file relative to the root of
     * {@link IBundle}.
     * @param mod last modified date for the file
     * @param contentByteArray byte array representing the content of the file
     * to write.
     */
    public void _addFile(final String path, final long mod,
            final byte[] contentByteArray)
    {
        @SuppressWarnings("unused")
        IBundleFileEntry entry = new IBundleFileEntry() {
            @Override
            public String getPath()
            {
                return path;
            }

            @Override
            public long getLastModified()
            {
                return mod;
            }

            @Override
            public String getContainingBundlePath()
            {
                return file.getAbsolutePath();
            }

            @Override
            public InputStream createInputStream() throws IOException
            {
                return new ByteArrayInputStream(contentByteArray);
            }
        };

        //files.put(entry.getPath(), entry);
    }

    /**
     * Record a problem encountered while reading or writing the {@link IBundle}
     * .
     * 
     * @param problem the compiler problem to add to this {@link IBundle}.
     */
    public void addProblem(ICompilerProblem problem)
    {
        problems.add(problem);
    }

    @Override
    public String toString()
    {
        return getBundleFile().toString();
    }
}
