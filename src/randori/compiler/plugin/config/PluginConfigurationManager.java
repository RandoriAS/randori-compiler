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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import randori.compiler.plugin.factory.IPluginFactory;
import randori.compiler.plugin.factory.PluginFactory;
import randori.compiler.plugin.jar.IJarLoader;
import randori.compiler.plugin.jar.JarLoader;

/**
 * @author Roland Zwaga <roland@stackandheap.com>
 */
@SuppressWarnings("rawtypes")
public class PluginConfigurationManager implements IPluginConfigurationManager
{

    private List<Class> _pluginInterfaces;
    private HashMap<String, IPluginFactory> _factories;
    private IJarLoader _jarLoader;
    private File _basePath;

    public PluginConfigurationManager(List<Class> pluginInterfaces,
            String basePath)
    {
        _pluginInterfaces = pluginInterfaces;
        _basePath = new File(basePath);
        _factories = new HashMap<String, IPluginFactory>();

        _jarLoader = new JarLoader();
    }

    @Override
    public IPluginFactory loadFromXML(String path, String configId)
            throws ParserConfigurationException, SAXException, IOException
    {
        if (_factories.containsKey(configId))
        {
            return _factories.get(configId);
        }
        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        return loadFromXML(doc, configId);
    }

    @Override
    public IPluginFactory loadFromXML(Document xml, String configId)
            throws IOException
    {
        if (_factories.containsKey(configId))
        {
            return _factories.get(configId);
        }
        Element doc = xml.getDocumentElement();

        HashMap<String, File> pluginInfo = extractPluginInfo(doc, configId);

        PluginFactory factory = null;
        factory = new PluginFactory(_jarLoader);
        factory.loadJars(pluginInfo, _pluginInterfaces, _jarLoader);
        _factories.put(configId, factory);

        return factory;
    }

    public HashMap<String, File> extractPluginInfo(Element xml, String configId)
    {
        Element runConfigElement = getElementById(xml, "run-config", configId);
        NodeList pluginNodes = runConfigElement.getElementsByTagName("plugin");
        int len = pluginNodes.getLength();
        HashMap<String, File> pluginInfo = new HashMap<String, File>();
        for (int i = 0; i < len; i++)
        {
            Node node = pluginNodes.item(i);
            extractPluginInfoEntry(xml, pluginInfo, node);
        }
        return pluginInfo;
    }

    public Element getElementById(Element xml, String elementName,
            String configId)
    {
        NodeList runConfigs = xml.getElementsByTagName(elementName);
        int len = runConfigs.getLength();
        Element runConfig = null;
        for (int i = 0; i < len; i++)
        {
            Node node = runConfigs.item(i);
            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            if (id.equals(configId))
            {
                runConfig = (Element) node;
                break;
            }
        }
        return runConfig;
    }

    public void extractPluginInfoEntry(Element xml,
            HashMap<String, File> pluginInfo, Node node)
    {
        String pluginId = node.getAttributes().getNamedItem("ref")
                .getNodeValue();
        Element pluginElement = getElementById(xml, "plugin-jar", pluginId);

        String path = pluginElement.getAttributes().getNamedItem("path")
                .getNodeValue();
        String id = pluginElement.getAttributes().getNamedItem("id")
                .getNodeValue();

        File filePath = normalizePath(path);

        pluginInfo.put(id, filePath);
    }

    public File normalizePath(String path)
    {
        File test = new File(path);
        if (test.isAbsolute() == false)
        {
            File file = new File(_basePath, path);
            return file;
        }
        else
        {
            return test;
        }
    }
}
