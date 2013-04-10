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

import org.apache.flex.compiler.definitions.IFunctionDefinition;

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
public class MethodDetailRowTemplate extends DetailRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public MethodDetailRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.METHOD_DETAIL_TEMPLATE);
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
        IFunctionDefinition method = (IFunctionDefinition) getDefinition();

        getRowConverter().addModifiers(this, method);
        getRowConverter().addParameters(this, method);
        getRowConverter().addLongDescription(this, method);

        putRow("isConstructor", method.isConstructor());
        getRowConverter().addReturnType(this, method);
        getRowConverter().addParamTags(this, method);
        getRowConverter().addReturnTag(this, method);
        getRowConverter().addUses(this, method);

        //        ASDocTemplateFactory.addParamTag(method, getRow(), getConverter());
        //        ASDocTemplateFactory.addReturnTag(method, getRow(), getConverter());
        //        // ASDocTemplateFactory.addExampleTags(method, getRow(),
        //        // getConverter());
        //        ASDocTemplateFactory.addSeeTags((IDocCommentAware) getElement(),
        //                getRow(), getConverter());
        //
        //        // ASDocTemplateFactory.addEventTags(getElement(), getRow(),
        //
        //        IDocCommentAware aware = (IASMethod) getElement();
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

        /*
         * getPreConverter().addReturnType( method, getRow() );
         * getPreConverter().addParameters( getElement(), getRow() );
         * getPreConverter().addDefinedByID( getElement(), getRow() );
         * getPreConverter().addLongDescription( getElement(), getRow() );
         * getPreConverter().addModifiers( getElement(), getRow() );
         * setRowProperty( "isConstructor", method.isConstructor() );
         * ASDocTemplateFactory.addParamTag( getElement(), getRow() );
         * ASDocTemplateFactory.addReturnTag( getElement(), getRow() ); //
         * Shared getPreConverter().addUses( getElement(), getRow() );
         * getPreConverter().addUsedby( getElement(), getRow() );
         * ASDocTemplateFactory.addAuthorTags( this );
         * ASDocTemplateFactory.addDefaultTag( this, getElement() );
         * ASDocTemplateFactory.addDateTags( this );
         * ASDocTemplateFactory.addSeeTags( this );
         * ASDocTemplateFactory.addInternalTag( this, getElement() );
         * ASDocTemplateFactory.addExampleTag( this, getElement() );
         * ASDocTemplateFactory.addSourceTag( getElement(), getRow() );
         * getPreConverter().addExtensionPoint( getElement(), getRow() );
         * getPreConverter().addDynamicMeta( getElement(), getRow() );
         */
    }
}
