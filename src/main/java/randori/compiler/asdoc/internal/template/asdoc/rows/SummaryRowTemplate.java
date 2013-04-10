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

import org.apache.flex.compiler.definitions.IDocumentableDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;

/**
 * An abstraction of a template class summary row.
 * <p>
 * This template is held and created inside the
 * <code>SummaryTableTemplate</code> template.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class SummaryRowTemplate extends AbstractRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SummaryRowTemplate(IDocConfiguration config)
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
        getRowConverter().addDefinedBy(this, getDefinition());
        getRowConverter().addShortDescription(this,
                (IDocumentableDefinition) getDefinition());

        putRow("isStatic", getDefinition().isStatic());

        // setRowProperty( "elementKind",
        // ElementFactory.getElementKind(getElement()));
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
