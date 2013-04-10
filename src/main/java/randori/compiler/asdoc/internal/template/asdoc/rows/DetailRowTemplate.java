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

package randori.compiler.asdoc.internal.template.asdoc.rows;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.template.asdoc.IDetailRowTemplate;

/**
 * An abstraction of a template class detail row.
 * <p>
 * This template is held and created inside the <code>DetailTableTemplate</code>
 * template.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class DetailRowTemplate extends AbstractRowTemplate implements
        IDetailRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public DetailRowTemplate(IDocConfiguration config)
    {
        super(config);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    /**
     * Pre converts the row data, called in <code>renderTemplate()</code> before
     * <code>renderRow()</code>.
     */
    @Override
    protected void convertRow()
    {
        getRowConverter().addName(this, getDefinition());
        getRowConverter().addShortDescription(this,
                (IDocumentableDefinition) getDefinition());

        putRow("elementKind", getElementKind(getDefinition()));
        getRowConverter().addSeeTags(this,
                (IDocumentableDefinition) getDefinition());
    }

    public static String getElementKind(IDefinition definition)
    {
        if (definition instanceof IFunctionDefinition)
        {
            if (definition instanceof IAccessorDefinition)
                return "accessor";
            return "method";
        }
        else if (definition instanceof IVariableDefinition)
        {
            if (definition.isStatic())
                return "constant";
            return "variable";
        }

        return null;
    }

    // --------------------------------------------------------------------------
    //
    // Abstract Protected :: Methods
    //
    // --------------------------------------------------------------------------

    /**
     * Override insubclasses to add convertion properties into the row template.
     */
    abstract protected void renderRow();
}
