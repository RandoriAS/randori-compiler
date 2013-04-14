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
package randori.compiler.plugin.factory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import randori.compiler.plugin.ICompilerPlugin;
import randori.compiler.plugin.jar.IJarLoader;

/**
 * @author Roland Zwaga <roland@stackandheap.com>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PluginFactory implements IPluginFactory
{

    @SuppressWarnings("unused")
    private IJarLoader _loader;

    private HashMap<Class, List<Class>> interfaceLookup;

    private HashMap<Class, List<?>> instanceLookup;

    @Override
    public <E> List<E> getPluginInstances(Class interfaze)
    {
        if (!instanceLookup.containsKey(interfaze))
        {
            createInstances(interfaze);
        }
        return (List<E>) instanceLookup.get(interfaze);
    }

    @Override
    public void registerPlugin(Class<? extends ICompilerPlugin> api,
            Class<?> implementation)
    {
        if (!hasPlugin(api))
        {
            interfaceLookup.put(api, new ArrayList<Class>());
        }
        List<Class> clazzez = interfaceLookup.get(api);
        clazzez.add(implementation);
    }

    @Override
    public boolean hasPlugin(Class<? extends ICompilerPlugin> api)
    {
        return interfaceLookup.containsKey(api);
    }

    private <E> void createInstances(Class interfaze)
    {
        ArrayList<E> instances = new ArrayList<E>();
        if (hasPlugin(interfaze))
        {
            addInstances(instances, interfaceLookup.get(interfaze));
        }
        instanceLookup.put(interfaze, instances);
    }

    private <E> void addInstances(List<E> instances, List<Class> clazzez)
    {
        Iterator<Class> it = clazzez.iterator();
        while (it.hasNext())
        {
            Class clazz = it.next();
            try
            {
                //Constructor<?> ctor = clazz.getConstructor();
                //instances.add((E) ctor.newInstance());
                instances.add((E) clazz.newInstance());
            }
            //catch (NoSuchMethodException e)
            //{
            //}
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            //catch (InvocationTargetException e)
            //{
            //}
        }
    }

    public PluginFactory(IJarLoader jarLoader)
    {
        _loader = jarLoader;
        interfaceLookup = new HashMap<Class, List<Class>>();
        instanceLookup = new HashMap<Class, List<?>>();
    }

    public void loadJars(HashMap<String, File> pluginInfo,
            List<Class> pluginInterfaces, IJarLoader jarLoader)
            throws MalformedURLException, IOException
    {
        Iterator it = pluginInfo.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pairs = (Map.Entry) it.next();
            File jarPath = (File) pairs.getValue();
            JarFile jarFile = jarLoader.getJarFile(jarPath);
            buildInterfacesLookups(jarFile, pluginInterfaces);
        }
    }

    public void buildInterfacesLookups(JarFile jarFile,
            List<Class> pluginInterfaces)
    {
        Enumeration allEntries = jarFile.entries();
        while (allEntries.hasMoreElements())
        {
            JarEntry entry = (JarEntry) allEntries.nextElement();
            try
            {
                Class clazz = Class.forName(entry.getName());
                buildInterfaceLookup(clazz, pluginInterfaces, interfaceLookup);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void buildInterfaceLookup(Class pluginImplementingClass,
            List<Class> pluginInterfaces,
            HashMap<Class, List<Class>> interfaceLookup)
    {
        Iterator<Class> it = pluginInterfaces.iterator();
        while (it.hasNext())
        {
            Class pluginInterface = it.next();
            if ((pluginImplementingClass.isInterface() == false)
                    && (pluginInterface
                            .isAssignableFrom(pluginImplementingClass)))
            {
                addClassToLookup(pluginInterface, pluginImplementingClass,
                        interfaceLookup);
            }
        }
    }

    public void addClassToLookup(Class pluginInterface,
            Class pluginImplementingClass,
            HashMap<Class, List<Class>> interfaceLookup)
    {
        if (interfaceLookup.containsKey(pluginInterface) == false)
        {
            interfaceLookup.put(pluginInterface, new ArrayList<Class>());
        }
        List<Class> clazzez = interfaceLookup.get(pluginInterface);
        clazzez.add(pluginImplementingClass);
    }

}
