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

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.tree.as.IFunctionNode;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.rows.SummaryRowTemplate;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class AccessorSummaryRowTemplate extends SummaryRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String NAME = "AccessorSummaryRowTemplate";

    public static final String TEMPLATE_FILE = "summary/accessor-summary-row.vm";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public AccessorSummaryRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(new File(TEMPLATE_FILE));
    }

    @Override
    protected void renderRow()
    {
        IAccessorDefinition accessor = (IAccessorDefinition) getDefinition();
        getRowConverter().addValueType(this, getDefinition());

        IFunctionNode node = accessor.getFunctionNode();
        if (node != null)
        {
            String atype = "";
            if (accessor.isOverride())
            {
                atype = "[override]";
            }
            else if (node.isGetter())
            {
                atype = "[read-only]";
            }
            else if (node.isSetter())
            {
                atype = "[write-only]";
            }
            putRow("accessType", atype);
        }

        getRowConverter().addImplementedFrom(this, accessor);

        //        getPreConverter().addValueType(getElement(), getRow());
        //
        //        IASAccessor aelement = (IASAccessor) getElement();
        //
        //        String atype = "";
        //        if (aelement.hasModifier("override"))
        //        {
        //            atype = "[override]";
        //        }
        //        else if (aelement.isReadOnly())
        //        {
        //            atype = "[read-only]";
        //        }
        //        else if (aelement.isWriteOnly())
        //        {
        //            atype = "[write-only]";
        //        }
        //
        //        setRowProperty("accessType", atype);
        //
        //        if (getTypeElement() instanceof IASClassType)
        //        {
        //
        //            setRowProperty(
        //                    "implementedFrom",
        //                    getConverter().returnImplementedFrom(
        //                            (IASClassType) getTypeElement(), aelement));
        //        }

        // getPreConverter().addValueType( getElement(), getRow() );
        // getPreConverter().addDeprecated( getElement(), getRow() );
        // getPreConverter().addReplacement( getElement(), getRow() );
        // getPreConverter().addBindable( getElement(), getRow() );
        // getPreConverter().addDefinedByID( getElement(), getRow() );
    }

}
