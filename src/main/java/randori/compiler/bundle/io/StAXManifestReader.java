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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.flex.compiler.problems.FileInLibraryIOProblem;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.bundle.BundleContainer;
import randori.compiler.bundle.BundleLibrary;
import randori.compiler.bundle.BundleVersion;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IMutableBundle;

/**
 * @author Michael Schmalle
 */
public class StAXManifestReader
{
    private static final Object TAG_LIBRARY = "library";

    private final XMLStreamReader reader;

    private final IMutableBundle bundle;

    private BundleLibrary library;

    private BundleVersion version;

    public StAXManifestReader(final InputStream in, final IMutableBundle bundle)
            throws XMLStreamException
    {
        if (in == null)
            throw new NullPointerException("InputStream can't be null.");
        if (bundle == null)
            throw new NullPointerException("Bundle model can't be null");

        this.bundle = bundle;

        // A filter only keeps start and end XML elements.
        final StreamFilter filter = new StreamFilter() {
            @Override
            public boolean accept(XMLStreamReader reader)
            {
                return reader.isStartElement()
                        || (reader.isEndElement() && reader.getName()
                                .getLocalPart().equals(TAG_LIBRARY));
            }
        };

        // Configure the XML factory.
        final XMLInputFactory factory = XMLInputFactory.newInstance();

        // Create Stream XML reader.
        final XMLStreamReader xmlStreamReader = factory
                .createXMLStreamReader(in);
        reader = factory.createFilteredReader(xmlStreamReader, filter);
    }

    public void close() throws IOException
    {
        try
        {
            reader.close();
        }
        catch (XMLStreamException e)
        {
            throw new IOException(e.getMessage());
        }
    }

    public void parse()
    {
        String bundlePath = null;
        if (bundle.getBundleFile() != null)
            bundlePath = FilenameNormalization.normalize(bundle.getBundleFile()
                    .getAbsolutePath());

        try
        {
            while (reader.hasNext())
            {
                final int next = reader.next();
                if (next != XMLStreamConstants.START_ELEMENT)
                    continue;

                final String tagName = reader.getName().getLocalPart();
                if (tagName.equals(TAG_LIBRARY))
                {
                    if (reader.isStartElement())
                    {
                        library = new BundleLibrary(reader.getAttributeValue(
                                null, "name"));
                        bundle.addLibrary(library);
                    }
                    else if (reader.isEndElement())
                    {

                        library = null;
                    }
                }
                else if (library != null)
                {
                    final String containerType = tagName;
                    final String categoryType = reader.getAttributeValue(null,
                            "type");
                    BundleContainer container = (BundleContainer) library
                            .getContainer(IBundleContainer.Type
                                    .toType(containerType));

                    if (container == null)
                    {
                        container = (BundleContainer) library
                                .addContainer(IBundleContainer.Type
                                        .toType(containerType));
                    }

                    IBundleCategory category = container
                            .addCategory(IBundleCategory.Type
                                    .toType(categoryType));

                    category.addFile(bundlePath,
                            reader.getAttributeValue(null, "path"));
                }
                else if (tagName.equals("versions"))
                {
                    version = (BundleVersion) bundle.getVersion();
                }
                else if (tagName.equals("bundle"))
                {
                    version.setBundleVersion(reader.getAttributeValue(null,
                            "version"));
                }
                else if (tagName.equals("randori"))
                {
                    version.setRandoriVersion(reader.getAttributeValue(null,
                            "version"));
                    version.setRandoriBuild(reader.getAttributeValue(null,
                            "build"));
                    version.setRandoriMinSupportedVersion(reader
                            .getAttributeValue(null, "minimumSupportedVersion"));
                }
                else if (tagName.equals("compiler"))
                {
                    version.setCompilerName(reader.getAttributeValue(null,
                            "name"));
                    version.setCompilerVersion(reader.getAttributeValue(null,
                            "version"));
                }
                else if (tagName.equals("version"))
                {

                }
            }
        }
        catch (XMLStreamException e)
        {
            File bundleFile = bundle.getBundleFile();
            final String file = (bundleFile != null) ? FilenameNormalization
                    .normalize(bundleFile.getAbsolutePath()) : "";
            bundle.addProblem(new FileInLibraryIOProblem(
                    BundleReader.MANIFEST_XML, file, e.getLocalizedMessage()));
        }
    }
}
