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

import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IEventDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.tree.metadata.IEventTagNode;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.rows.DetailRowTemplate;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * The event detail row template.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class EventDetailRowTemplate extends DetailRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String NAME = "EventDetailRowTemplate";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public EventDetailRowTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.EVENT_DETAIL_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected void convertRow()
    {
        getRowConverter().addName(this, getDefinition());
        getRowConverter().addShortDescription(this,
                (IDocumentableDefinition) getDefinition());
        getRowConverter().addLongDescription(this,
                (IDocumentableDefinition) getDefinition());
        //        getPreConverter().addLongDescription((IDocCommentAware) getElement(),
        //                getRow());
        //        setRowProperty("elementKind", getElementKind(getElement()));
    }

    /**
      * 
      */

    @Override
    protected void renderRow()
    {
        IEventDefinition event = (IEventDefinition) getDefinition();
        IEventTagNode node = event.getNode();

        String eventID = event.getBaseName();
        String eventFragmentName = eventID;
        String eventQualifiedName = node.getAttributeValue("type");
        ITypeDefinition type = getConfiguration().getAccess().getType(
                eventQualifiedName);

        String eventLocalName = eventQualifiedName;

        IConstantDefinition constant = getOwner(eventQualifiedName, eventID);
        if (constant == null)
        {

        }
        else
        {
            //            eventLocalName = getConfiguration().getConverter()
            //                    .returnLink(
            //                            constant,
            //                            getConfiguration().getConverter()
            //                                    .getPackageContextString());

            eventFragmentName = constant.getBaseName();
        }

        if (type != null)
        {
            eventLocalName = type.getBaseName();
            eventQualifiedName = getConfiguration().getConverter()
                    .returnTypeLink(
                            type,
                            getConfiguration().getConverter()
                                    .getPackageContextString(), true);
        }
        //        String eventFragmentName = "";
        //        String eventStringName = "";
        //        String eventTable = "";
        //
        //        // - $row
        //        // - eventID ../../mx/events/FlexEvent.html
        //        // - eventQualifiedName mx.events.FlexEvent
        //        // - eventLocalName FlexEvent
        //        // - eventFragmentName BUTTON_DOWN
        //        // - eventStringName buttonDown
        //        // - eventTable <table>...</table>
        //
        //        // [Event(name="myEvent",type="flash.events.Event")]
        //        String qualifiedName = (String) e.getParameterValue("type");
        //        String eventType = (String) e.getParameterValue("name");
        //        IASField owner = getOwner(qualifiedName, eventType);
        //        if (owner == null)
        //        {
        //            // event class not included
        //            eventID = null;
        //            eventQualifiedName = (String) e.getParameterValue("type");
        //            eventLocalName = StringUtils.substringAfterLast(eventQualifiedName,
        //                    ".");
        //            eventStringName = eventFragmentName = (String) e
        //                    .getParameterValue("name");
        //            eventTable = "";
        //        }
        //        else
        //        {
        //            IASType parent = LinkUtils.getParentType(owner);
        //            ILinkElement link = getBook().getAccess().getLink(parent);
        //            eventID = getConverter().returnLinkID(link,
        //                    getConverter().getRenderContextString());
        //            eventQualifiedName = parent.getQName().getQualifiedName();
        //            eventLocalName = parent.getQName().getLocalName();
        //            eventFragmentName = owner.getName();
        //            eventStringName = owner.getInitializer().toString()
        //                    .replace("\"", ""); // "myEvent"
        //            eventTable = "";
        //        }
        //
        //        setRowProperty("eventID", eventID);
        //        setRowProperty("eventQualifiedName", eventQualifiedName);
        //        setRowProperty("eventLocalName", eventLocalName);
        //        setRowProperty("eventFragmentName", eventFragmentName);
        //        setRowProperty("eventStringName", eventStringName);
        //        setRowProperty("eventTable", eventTable);
        //
        //        if (owner != null)
        //        {
        //            String table = ASDocTemplateFactory.addEventTable(owner, getBook()
        //                    .getAccess(), getConverter(), getRow());
        //            if (table != null)
        //            {
        //                setRowProperty("eventTable", table);
        //            }
        //        }
        putRow("eventQualifiedName", eventQualifiedName);
        putRow("eventLocalName", eventLocalName);
        putRow("eventFragmentName", eventFragmentName);
    }

    /**
     * The owner is the constant that defines the @eventType tag in the event
     * class. So this method looks for a type with the qualifiedName then loops
     * through it's fields looking for the constant that defines an @eventType
     * tag with the eventType.
     * 
     * @return
     */
    private IConstantDefinition getOwner(String qualifiedName, String eventType)
    {
        ITypeDefinition type = getConfiguration().getAccess().getType(
                qualifiedName);
        if (type == null)
            return null;

        List<IConstantDefinition> constants = getConfiguration().getAccess()
                .getConstants((IClassDefinition) type, "public", true);
        for (IConstantDefinition constant : constants)
        {
            String value = constant.resolveValue(
                    getConfiguration().getAccess().getProject()).toString();
            if (value != null && value.equals(eventType))
                return constant;
            /*
            IDocTag tag = field.getExplicitSourceComment().("eventType");
            if (tag != null)
            {
                String name = tag.getBody().trim();
                if (name.equals(eventType))
                    return field;
            }
            */
        }

        return null;
    }
}
