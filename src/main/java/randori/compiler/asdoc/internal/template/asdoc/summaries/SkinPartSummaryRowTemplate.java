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

package randori.compiler.asdoc.internal.template.asdoc.summaries;

import java.io.File;

import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.definitions.ISkinPartDefinition;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SkinPartSummaryRowTemplate extends MetaTagSummaryRow
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String TEMPLATE_FILE = "summary/skinpart-summary-row.vm";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SkinPartSummaryRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(new File(TEMPLATE_FILE));
    }

    @Override
    protected void renderRow()
    {
        super.renderRow();

        ISkinPartDefinition part = (ISkinPartDefinition) getDefinition();

        String valueType = part.getTypeAsDisplayString();
        ITypeDefinition type = part.getDecoratedVariable().resolveType(
                getConfiguration().getAccess().getProject());
        if (type != null)
        {
            valueType = getConfiguration().getConverter()
                    .returnTypeLink(
                            type,
                            getConfiguration().getConverter()
                                    .getPackageContextString());
        }
        putRow("valueType", valueType);

        getRowConverter()
                .addShortDescription(this, part.getDecoratedVariable());

        //        IASField var = (IASField) getElement();
        //
        //        IASMetaTag part = var.getMetaTag("SkinPart");
        //
        //        String required = (String) part.getParameterValue("required");
        //        if (required == null || required.equals(""))
        //        {
        //            required = "false";
        //        }
        //
        //        String partType = (String) part.getParameterValue("type");
        //        if (partType == null || partType.equals(""))
        //        {
        //            partType = "Static";
        //        }
        //        else
        //        {
        //            // try to get a link
        //            ILinkElement elink = getConverter().getLink(partType);
        //            if (elink != null)
        //            {
        //                partType = getConverter().returnSeeLink(elink,
        //                        getConverter().getRenderContextString(), partType);
        //            }
        //        }
        //
        //        setRowProperty("isRequired", required);
        //        setRowProperty("partType", partType);
    }
}
