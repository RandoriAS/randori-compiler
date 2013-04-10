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

package randori.compiler.asdoc.internal.template.asdoc.details;

import org.apache.flex.compiler.definitions.IConstantDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.rows.DetailRowTemplate;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * The variable detail row template.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class ConstantDetailRowTemplate extends DetailRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String NAME = "ConstantDetailRowTemplate";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public ConstantDetailRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.CONSTANT_DETAIL_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected void renderRow()
    {
        IConstantDefinition constant = (IConstantDefinition) getDefinition();
        getRowConverter().addValueType(this, constant);
        getRowConverter().addModifiers(this, constant);
        getRowConverter().addLongDescription(this, constant);
        getRowConverter().addValue(this, constant);
        //        getPreConverter().addValueType(getElement(), getRow());
        //        getPreConverter().addModifiers((IASMember) getElement(), getRow());
        //        getPreConverter().addLongDescription((IDocCommentAware) getElement(),
        //                getRow());
        //
        //        IASField field = (IASField) getElement();
        //        setRowProperty("value", field.getInitializer().toString());
        //
        //        ASDocTemplateFactory.addSeeTags((IDocCommentAware) getElement(),
        //                getRow(), getConverter());
        //        // ASDocTemplateFactory.addExampleTags(getElement(), getRow(),
        //        // getConverter());
        //
        //        String table = ASDocTemplateFactory.addEventTable(
        //                (IASField) getElement(), getBook().getAccess(), getConverter(),
        //                getRow());
        //        if (table != null)
        //        {
        //            setRowProperty("eventTable", table);
        //        }
        //
        //        IDocCommentAware aware = (IASField) getElement();
        //
        //        setRowProperty("sinceTag",
        //                ASDocTemplateFactory.addSinceTag(aware, getConverter()));
        //
        //        setRowProperty("authorTags",
        //                ASDocTemplateFactory.addAuthorTags(aware, getConverter()));
        //
        //        setRowProperty("copyrightTags",
        //                ASDocTemplateFactory.addCopyrightTags(aware, getConverter()));
        //
        //        setRowProperty("seeTags",
        //                ASDocTemplateFactory.addSeeTags(aware, getConverter(), "see"));
    }

}
