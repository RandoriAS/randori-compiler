/***
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
 * @author Roland Zwaga <roland@stackandheap.com>
 */
package randori.compiler.plugin.config;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import randori.compiler.plugin.factory.IPluginFactory;

/**
 * @author Roland Zwaga <roland@stackandheap.com>
 */
public interface IPluginConfigurationManager
{
    IPluginFactory loadFromXML(String path, String configId)
            throws ParserConfigurationException, SAXException, IOException;

    IPluginFactory loadFromXML(Document xml, String configId)
            throws IOException;
}
