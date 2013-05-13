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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.flex.swc.ISWC;
import org.apache.flex.swc.SWC;

import randori.compiler.bundle.IBundleContainer.Type;
import randori.compiler.bundle.io.BundleUtils;

/**
 * @author Michael Schmalle
 */
public class BundleLibrary implements IBundleLibrary
{

    private final String name;

    private HashMap<String, ISWC> swcs = new HashMap<String, ISWC>();

    private LinkedHashMap<IBundleContainerType, IBundleContainer> containers = new LinkedHashMap<IBundleContainerType, IBundleContainer>();

    private IBundleContainer binContainer;

    private IBundleCategory swcCategory;

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getPath()
    {
        return getName();
    }

    public BundleLibrary(String name)
    {
        this.name = name;
    }

    @Override
    public Collection<IBundleContainer> getContainers()
    {
        return containers.values();
    }

    @Override
    public IBundleContainer getContainer(IBundleContainerType type)
    {
        return containers.get(type);
    }

    @Override
    public IBundleContainer addContainer(IBundleContainerType type)
    {
        if (containers.containsKey(type))
            return containers.get(type);

        IBundleContainer container = new BundleContainer(this, type);
        containers.put(type, container);

        return container;
    }

    @Override
    public Collection<ISWC> getSWCS()
    {
        return swcs.values();
    }

    public Collection<ISWC> getSWCS(File output, File bundleFile)
            throws IOException
    {
        // XXX TEMP
        if (swcs.size() == 0)
        {
            IBundleContainer container = getContainer(IBundleContainer.Type.BIN);
            if (container != null)
            {
                IBundleCategory category = container
                        .getCategory(IBundleCategory.Type.SWC);
                if (category != null)
                {
                    for (IBundleEntry entry : category.getEntries())
                    {
                        final File file = new File(output, entry.getPath())
                                .getAbsoluteFile();
                        String name = entry.getPath();
                        writeFile(output, entry);
                        swcs.put(name, new SWC(file));
                    }
                }
            }
        }
        return swcs.values();
    }

    void writeFile(File output, IBundleEntry entry) throws IOException
    {
        final File file = new File(output, entry.getPath()).getAbsoluteFile();
        final File parentFolder = file.getParentFile();
        if (!parentFolder.isDirectory())
            parentFolder.mkdirs();
        file.createNewFile();
        final OutputStream outputStream = new FileOutputStream(file);
        final InputStream fileInputStream = entry.createInputStream();
        IOUtils.copy(fileInputStream, outputStream);
        fileInputStream.close();
        outputStream.close();
    }

    @Override
    public ISWC getSWC(String name)
    {
        return swcs.get(name);
    }

    @Override
    public void addSWC(ISWC swc)
    {
        if (binContainer == null)
            createBinContainer();

        String name = BundleUtils.toSWCName(swc);
        swcs.put(name, swc);
        swcCategory.addFile(swc.getSWCFile(), name + ".swc");
    }

    private void createBinContainer()
    {
        // for now we hard wire this in since SWCs are specific to the implementation API
        binContainer = addContainer(Type.BIN);
        swcCategory = binContainer.addCategory(IBundleCategory.Type.SWC);
    }
}
