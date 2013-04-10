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

package randori.compiler.asdoc.internal.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import randori.compiler.asdoc.access.IPackageBundle;

public class PackageBundle implements IPackageBundle
{

    private List<IPackageDefinition> packages = new ArrayList<IPackageDefinition>();

    private String path;

    private String baseName;

    private String qualifiedName;

    private Map<String, IClassDefinition> classes = new TreeMap<String, IClassDefinition>();

    private Map<String, IInterfaceDefinition> interfaces = new TreeMap<String, IInterfaceDefinition>();

    public PackageBundle(String path, String baseName, String qualifiedName)
    {
        this.path = path;
        this.baseName = baseName;
        this.qualifiedName = qualifiedName;
    }

    @Override
    public String getBaseName()
    {
        return baseName;
    }

    @Override
    public String getQualifiedName()
    {
        return qualifiedName;
    }

    @Override
    public List<IPackageDefinition> getPackages()
    {
        return packages;
    }

    void add(IPackageDefinition definition)
    {
        if (!packages.contains(definition))
        {
            packages.add(definition);
        }
    }

    void addClass(IClassDefinition definition)
    {
        if (!classes.containsKey(definition.getQualifiedName()))
        {
            classes.put(definition.getQualifiedName(), definition);
        }
    }

    void addInterface(IInterfaceDefinition definition)
    {
        if (!interfaces.containsKey(definition.getQualifiedName()))
        {
            interfaces.put(definition.getQualifiedName(), definition);
        }
    }

    @Override
    public String getAbsolutePath()
    {
        return path;
    }
}
