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

import org.apache.flex.compiler.definitions.IStyleDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class StyleSummaryRowTemplate extends MetaTagSummaryRow
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String TEMPLATE_FILE = "summary/style-summary-row.vm";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public StyleSummaryRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(new File(TEMPLATE_FILE));
    }

    @Override
    protected void renderRow()
    {
        IStyleDefinition style = (IStyleDefinition) getDefinition();

        String type = style.getTypeAsDisplayString();
        String format = style.getFormat();
        String inherit = style.getInherit();
        String theme = null;
        String[] themes = style.getThemes();
        if (themes != null && themes.length > 0)
            theme = themes[0];
        //        String type = (String) getMetaDataElement().getParameterValue("type");
        //        String format = (String) getMetaDataElement().getParameterValue(
        //                "format");
        //        String inherit = (String) getMetaDataElement().getParameterValue(
        //                "inherit");
        //        String theme = (String) getMetaDataElement().getParameterValue("theme");
        //
        //        setRowProperty("type", type);
        //        setRowProperty("format", format);
        //        setRowProperty("inherit", inherit);
        //        setRowProperty("theme", theme);
        //
        //        getPreConverter().addLongDescription((IDocCommentAware) getElement(),
        //                getRow());
        //
        //        ASDocTemplateFactory.addDefaultTag((IDocCommentAware) getElement(),
        //                getRow(), getConverter());
        putRow("type", type);
        putRow("format", format);
        putRow("inherit", inherit);
        putRow("theme", theme);
        getRowConverter().addLongDescription(this, style);

    }
}
