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
package randori.compiler.plugin.jar;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.JarFile;

/**
 * @author Roland Zwaga <roland@stackandheap.com>
 */
public class JarLoader implements IJarLoader
{

    private HashMap<String, ClassLoader> _loaders;

    public JarLoader()
    {
        _loaders = new HashMap<String, ClassLoader>();
    }

    @Override
    public Boolean isLoaded(File jarPath)
    {
        return _loaders.containsKey(jarPath.getAbsolutePath());
    }

    private ClassLoader loadJar(File jarPath)
    {
        if (isLoaded(jarPath) == false)
        {
            ClassLoader loader = null;
            try
            {
                loader = URLClassLoader.newInstance(new URL[] { jarPath.toURI()
                        .toURL() }, getClass().getClassLoader());
                _loaders.put(jarPath.getAbsolutePath(), loader);
                return loader;
            }
            catch (MalformedURLException e)
            {
            }
            return loader;
        }
        else
        {
            return _loaders.get(jarPath);
        }
    }

    @Override
    public ClassLoader getLoader(File jarPath)
    {
        return loadJar(jarPath);
    }

    @Override
    public JarFile getJarFile(File jarPath) throws IOException
    {
        loadJar(jarPath);
        return new JarFile(jarPath);
    }
}
