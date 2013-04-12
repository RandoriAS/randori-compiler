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

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.rows.SummaryRowTemplate;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class VariableSummaryRowTemplate extends SummaryRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String TEMPLATE_FILE = "summary/variable-summary-row.vm";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public VariableSummaryRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(new File(TEMPLATE_FILE));
    }

    @Override
    protected void renderRow()
    {
        getRowConverter().addValueType(this, getDefinition());
 
        //        Map<String, String> deprecated = getConverter().returnDeprecated(
        //                (IMetaTagAware) getElement());
        //
        //        if (deprecated != null) {
        //            setRowProperty("deprecated", deprecated);
        //        }

        // getPreConverter().addValueType( getElement(), getRow() );
        // getPreConverter().addDeprecated( getElement(), getRow() );
        // getPreConverter().addReplacement( getElement(), getRow() );
        // getPreConverter().addBindable( getElement(), getRow() );
        // getPreConverter().addDefinedByID( getElement(), getRow() );
    }
}
