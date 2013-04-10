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

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

public class ClassDetailDescriptionTemplate extends ASDocTemplateRenderer
{
    @Override
    public void setDefinition(IDefinition definition)
    {
        super.setDefinition(definition);
    }

    /**
     * Constructor.
     */
    public ClassDetailDescriptionTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.CLASS_DETAIL_DESCRIPTION_TEMPLATE);
    }

    @Override
    protected Boolean renderTemplate()
    {
        ITypeDefinition e = (ITypeDefinition) getDefinition();

        set("shortDescription", getConvertor().returnShortDescription(e));
        set("longDescription", getConvertor().returnLongDescription(e));

        // set("tipTags", ASDocTemplateFactory.addTipTag(aware,
        // getConverter()));
        //
        // set("sinceTag", ASDocTemplateFactory.addSinceTag(aware,
        // getConverter()));
        //
        // set("authorTags",
        // ASDocTemplateFactory.addAuthorTags(aware, getConverter()));
        //
        // set("copyrightTags",
        // ASDocTemplateFactory.addCopyrightTags(aware, getConverter()));
        //
        // set("seeTags",
        // ASDocTemplateFactory.addSeeTags(aware, getConverter(), "see"));
        //
        // if (e instanceof IASClassType) {
        // String hostComponent = getConverter().returnHostComponent(
        // (IASType) getElement());
        //
        // set("hostComponent", hostComponent);
        //
        // // this will show up in classes the are HostComponents to skins
        // List<String> hostedSkins = getConverter().returnHostedSkins(
        // (IASType) getElement());
        // if (hostedSkins != null) {
        // set("hostedSkins", hostedSkins);
        // }
        //
        // List<String> stateNames = getConverter().returnSkinStates(e);
        //
        // set("states", stateNames);
        //
        // String defaultProperty = getConverter().returnDefaultProperty(e);
        // if (defaultProperty != null) {
        // set("defaultProperty", defaultProperty);
        // }
        //
        // }
        //
        // Map<String, String> deprecated = getConverter().returnDeprecated(
        // (IMetaTagAware) e.getParent());
        // if (deprecated != null) {
        // set("deprecated", deprecated);
        // }
        //
        // String bindable = getConverter().returnBindable(e.getParent());
        // if (bindable != null) {
        // set("bindable", bindable);
        // }
        //
        // set("exampleTags", ASDocTemplateFactory.addExampleTags(
        // (IDocCommentAware) getElement(), getConverter()));
        //
        // set("imageTags", ASDocTemplateFactory.addImageTags(
        // (IDocCommentAware) getElement(), getConverter(), getBasePath()));

        /*
         * set("exampleTags",
         * ASDocTemplateFactory.addExampleTags((IDocCommentAware) getElement(),
         * getConverter())); String hostComponent =
         * getConverter().returnHostComponent((IASType) getElement());
         * 
         * if (hostComponent != null) { set("hostComponent", hostComponent); }
         * 
         * 
         * List<String> hostedSkins = getConverter().returnHostedSkins((IASType)
         * getElement()); if (hostedSkins != null) { set("hostedSkins",
         * hostedSkins); } String bindable =
         * getConverter().returnBindable((IASType) getElement()); if (bindable
         * != null) { set("bindable", bindable); } String defaultProperty =
         * getConverter().returnDefaultProperty((IASType) getElement()); if
         * (defaultProperty != null) { set("defaultProperty", defaultProperty);
         * }
         */

        /*
         * ASDocTemplateFactory.addAuthorTags( this );
         * ASDocTemplateFactory.addDateTags( this );
         * ASDocTemplateFactory.addSeeTags( this );
         * ASDocTemplateFactory.addInternalTag( this, e );
         * ASDocTemplateFactory.addExampleTag( this, e );
         * getPreConverter().addUses( getElement(), getData() );
         * getPreConverter().addUsedby( getElement(), getData() );
         * getPreConverter().addExtensionPoint( getElement(), getData() );
         * getPreConverter().addDynamicMeta( getElement(), getData() );
         */

        return false;
    }
}
