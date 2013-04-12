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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import randori.compiler.asdoc.access.IASProjectAccess;
import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.PackageListVO;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * Renders the <code>output/package-list.html</code> index.
 * <p>
 * This is the top left "All Packages" pane.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class PackageListPaneTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ---------------------------------
    // topLevelElement
    // ---------------------------------

    private String topLevelElement = null;

    /**
     * @return string
     */
    public String getTopLevelElement()
    {
        return topLevelElement;
    }

    /**
     * @param string topLevelElement
     */
    public void setTopLevelElement(String topLevelElement)
    {
        this.topLevelElement = topLevelElement;
        set("topLevelElement", topLevelElement);
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PackageListPaneTemplate(IDocConfiguration config)
    {
        super(config);
        setTitle(IASDocTemplate.PACKAGE_LIST_TITLE);

        setOutputFile(IASDocTemplate.PACKAGE_LIST_OUTPUT);
        setTemplateFile(IASDocTemplate.PACKAGE_LIST_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected Boolean renderTemplate()
    {

        List<PackageListVO> result = new ArrayList<PackageListVO>();

        IASProjectAccess access = getConfiguration().getAccess();
        Collection<IPackageBundle> packages = access.getPackageBundles();

        for (IPackageBundle element : packages)
        {
            PackageListVO vo = new PackageListVO();
            vo.setName(element.getQualifiedName());
            vo.setAnchor(getConvertor().returnPackageListPaneLink(element));

            if (element.equals("toplevel"))
            {
                set("hasTopLevel", true);
            }
            else
            {
                result.add(vo);
            }
        }

        Collections.sort(result);

        set("packages", result);

        return true;
    }
}
