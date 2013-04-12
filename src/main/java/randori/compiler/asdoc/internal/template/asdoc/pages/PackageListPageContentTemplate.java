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

package randori.compiler.asdoc.internal.template.asdoc.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.PackageListVO;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class PackageListPageContentTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PackageListPageContentTemplate(IDocConfiguration config)
    {
        super(config);
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

        Collection<IPackageBundle> packages = getConfiguration().getAccess()
                .getPackageBundles();

        PackageListVO toplevel = null;

        for (IPackageBundle element : packages)
        {
            PackageListVO vo = new PackageListVO();

            String anchor = element.getQualifiedName();

            vo.setName(anchor);
            vo.setShortDescription(getConvertor().returnPackageDescription(
                    element));
            vo.setAnchor(getConvertor().returnPackageListPaneLink(element));

            if (!element.getQualifiedName().equals("toplevel"))
                result.add(vo);
            else
                toplevel = vo;
        }

        Collections.sort(result);

        if (toplevel != null)
            result.add(0, toplevel);

        Boolean flag = false;
        for (PackageListVO vo : result)
        {
            vo.setRowFlag(flag);
            flag = !flag;
        }

        set("packages", result);

        return false;
    }
}
