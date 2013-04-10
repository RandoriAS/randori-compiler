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

import org.apache.flex.compiler.definitions.IAccessorDefinition;

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
public class AccessorDetailRowTemplate extends DetailRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String NAME = "AccessorDetailRowTemplate";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public AccessorDetailRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.ACCESSOR_DETAIL_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    /**
      * 
      */

    @Override
    protected void renderRow()
    {
        IAccessorDefinition accessor = (IAccessorDefinition) getDefinition();
        getRowConverter().addValueType(this, getDefinition());
        getRowConverter().addModifiers(this, getDefinition());
        getRowConverter().addLongDescription(this, accessor);
        getRowConverter().addAccess(this, accessor);

        //        getPreConverter().addValueType(getElement(), getRow());
        //        getPreConverter().addModifiers((IASMember) getElement(), getRow());
        //        getPreConverter().addLongDescription((IDocCommentAware) getElement(),
        //                getRow());
        //        getPreConverter().addAccess((IScriptElement) getElement(), getRow());
        //
        //        ASDocTemplateFactory.addDefaultTag((IDocCommentAware) getElement(),
        //                getRow(), getConverter());
        //
        //        ASDocTemplateFactory.addSeeTags((IDocCommentAware) getElement(),
        //                getRow(), getConverter());
        //
        //        // ASDocTemplateFactory.addEventTags(getElement(), getRow(),
        //        // getConverter());
        //
        //        // ASDocTemplateFactory.addExampleTags(getElement(), getRow(),
        //        // getConverter());
        //
        //        String bindable = getConverter().returnBindable(
        //                (IScriptElement) getElement());
        //        if (bindable != null)
        //        {
        //            setRowProperty("bindable", bindable);
        //        }
        //        IDocCommentAware aware = (IASAccessor) getElement();
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
