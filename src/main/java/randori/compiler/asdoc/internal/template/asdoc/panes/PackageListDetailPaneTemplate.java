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

package randori.compiler.asdoc.internal.template.asdoc.panes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.INamespaceDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.PackageListDetailPaneVO;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class PackageListDetailPaneTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String OUTPUT_FILE = "package-list.html";

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ---------------------------------
    // classes
    // ---------------------------------

    private List<PackageListDetailPaneVO> classes = null;

    /**
     * @return string
     */
    public List<PackageListDetailPaneVO> getClasses()
    {
        return classes;
    }

    /**
     * @param string topLevelElement
     */
    public void setClasses(List<PackageListDetailPaneVO> classes)
    {
        this.classes = classes;
        set("classes", classes);
    }

    // ---------------------------------
    // interfaces
    // ---------------------------------

    private List<PackageListDetailPaneVO> interfaces = null;

    /**
     * @return string
     */
    public List<PackageListDetailPaneVO> getInterfaces()
    {
        return interfaces;
    }

    /**
     * @param string topLevelElement
     */
    public void setInterfaces(List<PackageListDetailPaneVO> interfaces)
    {
        this.interfaces = interfaces;
        set("interfaces", interfaces);
    }

    // ---------------------------------
    // functions
    // ---------------------------------

    private List<PackageListDetailPaneVO> functions = null;

    /**
     * @return string
     */
    public List<PackageListDetailPaneVO> getFunctions()
    {
        return functions;
    }

    /**
     * @param string topLevelElement
     */
    public void setFunctions(List<PackageListDetailPaneVO> functions)
    {
        this.functions = functions;
        set("functions", functions);
    }

    // ---------------------------------
    // namespaces
    // ---------------------------------

    private List<PackageListDetailPaneVO> namespaces = null;

    private IPackageBundle bundle;

    /**
     * @return string
     */
    public List<PackageListDetailPaneVO> getNamespaces()
    {
        return namespaces;
    }

    /**
     * @param string topLevelElement
     */
    public void setNamespaces(List<PackageListDetailPaneVO> namespaces)
    {
        this.namespaces = namespaces;
        set("namespaces", namespaces);
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PackageListDetailPaneTemplate(IDocConfiguration config)
    {
        super(config);
        setOutputFile(new File(OUTPUT_FILE));
        setTitle("All Packages");
    }

    @Override
    protected Boolean renderTemplate()
    {
        List<PackageListDetailPaneVO> classes = new ArrayList<PackageListDetailPaneVO>();
        List<PackageListDetailPaneVO> interfaces = new ArrayList<PackageListDetailPaneVO>();
        List<PackageListDetailPaneVO> functions = new ArrayList<PackageListDetailPaneVO>();
        List<PackageListDetailPaneVO> namespaces = new ArrayList<PackageListDetailPaneVO>();

        Collection<ITypeDefinition> types = getConfiguration().getAccess()
                .getTypes(bundle);

        for (ITypeDefinition type : types)
        {

            PackageListDetailPaneVO vo = new PackageListDetailPaneVO();

            vo.setName(type.getBaseName());
            vo.setAnchor(getConvertor().returnTypeContentLink(type, bundle));

            if (type instanceof IClassDefinition)
            {
                classes.add(vo);
            }
            else if (type instanceof IInterfaceDefinition)
            {
                interfaces.add(vo);
            }
            else if (type instanceof IFunctionDefinition)
            {
                functions.add(vo);
            }
            else if (type instanceof INamespaceDefinition)
            {
                namespaces.add(vo);
            }
        }

        setClasses(classes);
        setInterfaces(interfaces);
        setFunctions(functions);
        setNamespaces(namespaces);

        Collections.sort(classes);
        Collections.sort(interfaces);
        Collections.sort(functions);
        Collections.sort(namespaces);

        return true;
    }

    public void setBundle(IPackageBundle bundle)
    {
        this.bundle = bundle;
    }
}
