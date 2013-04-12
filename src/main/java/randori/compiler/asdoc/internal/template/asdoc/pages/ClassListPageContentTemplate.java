////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package randori.compiler.asdoc.internal.template.asdoc.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.ClassListVO;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class ClassListPageContentTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public ClassListPageContentTemplate(IDocConfiguration config)
    {
        super(config);
    }

    @Override
    protected Boolean renderTemplate()
    {
        List<ClassListVO> result = new ArrayList<ClassListVO>();

        Collection<ITypeDefinition> types = getConfiguration().getAccess()
                .getTypes();

        for (ITypeDefinition element : types)
        {
            ClassListVO vo = new ClassListVO();

            // 'Name' column
            vo.setName(element.getBaseName());
            vo.setIsClass(element instanceof IClassDefinition);
            vo.setAnchor(getConvertor().returnTypeListPaneLink(element));

            // 'Package' column
            IPackageBundle bundle = getConfiguration().getAccess()
                    .getPackageBundle(element);
            vo.setPackageAnchor(getConvertor()
                    .returnPackageListPaneLink(bundle));

            // 'Description' column
            vo.setShortDescription(getConvertor().returnShortDescription(
                    element));

            result.add(vo);
        }

        Collections.sort(result);

        Boolean flag = false;
        for (ClassListVO vo : result)
        {
            vo.setRowFlag(flag);
            flag = !flag;
        }

        set("pages", result);

        return false;
    }
}
