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

import java.io.Writer;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.flex.swc.catalog.XMLFormatter;

import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleEntry;
import randori.compiler.bundle.IBundleLibrary;
import randori.compiler.bundle.IBundleVersion;

/**
 * @author Michael Schmalle
 */
public class StAXManifestWriter
{
    static final String XMLNS_BUNDLE_MANIFEST_1 = "http://www.randoriframework.com/bundle/manifest/1";

    static final String TAG_BUNDLE = "bundle";
    static final String TAG_COMPILER = "compiler";
    static final String TAG_VERSIONS = "versions";
    static final String TAG_RANDORI = "randori";
    static final String TAG_LIBRARIES = "libraries";
    static final String TAG_LIBRARY = "library";

    static final String ATTR_XMLNS = "xmlns";
    static final String ATTR_BUILD = "build";
    static final String ATTR_NAME = "name";
    static final String ATTR_VERSION = "version";
    static final String ATTR_MINIMUM_SUPPORTED_VERSION = "minimumSupportedVersion";

    private final IBundle bundle;

    private final XMLFormatter xmlWriter;

    public StAXManifestWriter(final IBundle bundle, final Writer writer)
            throws XMLStreamException, FactoryConfigurationError
    {
        this.bundle = bundle;

        final XMLOutputFactory factory = XMLOutputFactory.newInstance();
        this.xmlWriter = new XMLFormatter(factory.createXMLStreamWriter(writer));
    }

    /**
     * Write serialized XML to output.
     * 
     * @throws XMLStreamException error
     */
    public void write() throws XMLStreamException
    {
        xmlWriter.writeStartDocument(); // start XML

        {
            xmlWriter.writeStartElement(TAG_BUNDLE);
            xmlWriter.writeAttribute(ATTR_XMLNS, XMLNS_BUNDLE_MANIFEST_1);
            writeVersions();
            //writeFeatures();
            //writeComponents();
            writeLibraries();

            //writeFiles();
            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndDocument(); // end XML

        xmlWriter.flush();
        xmlWriter.close();
    }

    private void writeSources(IBundleLibrary library) throws XMLStreamException
    {

        for (IBundleContainer container : library.getContainers())
        {
            xmlWriter.writeStartElement(container.getType().getName());

            for (IBundleCategory entry : container.getCategories())
            {
                //xmlWriter.writeStartElement(entry.getType().getName());

                for (IBundleEntry element : entry.getEntries())
                {
                    // writeFile(element.getFile(), element.getPath());
                    xmlWriter.writeEmptyElement(entry.getType().getName());
                    xmlWriter.writeAttribute("path", element.getPath());
                    xmlWriter.writeAttribute("type", entry.getType().getName());
                }

                // xmlWriter.writeEndElement();
            }

            xmlWriter.writeEndElement();
        }
    }

    private void writeLibraries() throws XMLStreamException
    {
        xmlWriter.writeStartElement(TAG_LIBRARIES);

        for (IBundleLibrary library : bundle.getLibraries())
        {
            xmlWriter.writeStartElement(TAG_LIBRARY);
            xmlWriter.writeAttribute("name", library.getName());

            writeSources(library);

            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement();
    }

    private void writeVersions() throws XMLStreamException
    {
        final IBundleVersion version = bundle.getVersion();
        if (version == null)
            return;

        xmlWriter.writeStartElement(TAG_VERSIONS);

        // Bundle version
        final String swcVersion = version.getBundleVersion();
        if (swcVersion != null)
        {
            xmlWriter.writeEmptyElement(TAG_BUNDLE);
            xmlWriter.writeAttribute(ATTR_VERSION, swcVersion);
        }

        // Randori version
        final String randoriVersion = version.getRandoriVersion();
        if (randoriVersion != null)
        {
            xmlWriter.writeEmptyElement(TAG_RANDORI);
            xmlWriter.writeAttribute(ATTR_VERSION, randoriVersion);

            final String randoriBuild = version.getRandoriBuild();
            if (randoriBuild != null)
                xmlWriter.writeAttribute(ATTR_BUILD, randoriBuild);

            final String minSupportVersion = version
                    .getRandoriMinSupportedVersion();
            if (minSupportVersion != null)
                xmlWriter.writeAttribute(ATTR_MINIMUM_SUPPORTED_VERSION,
                        minSupportVersion);
        }

        // Compiler version
        final String compilerVersion = version.getCompilerVersion();
        if (compilerVersion != null)
        {
            xmlWriter.writeEmptyElement(TAG_COMPILER);

            final String compilerName = version.getCompilerName();
            if (compilerName != null)
                xmlWriter.writeAttribute(ATTR_NAME, compilerName);

            xmlWriter.writeAttribute(ATTR_VERSION, compilerVersion);

            final String compilerBuild = version.getCompilerBuild();
            if (compilerBuild != null)
                xmlWriter.writeAttribute(ATTR_BUILD, compilerBuild);
        }

        xmlWriter.writeEndElement();
    }
}
