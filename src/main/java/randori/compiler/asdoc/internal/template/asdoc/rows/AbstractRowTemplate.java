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

import java.util.HashMap;
import java.util.Map;

import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.VelocityTemplate;
import randori.compiler.asdoc.template.asdoc.IASDocRowConverter;
import randori.compiler.asdoc.template.asdoc.IRowTemplate;

/**
 * An abstraction of a template class detail row.
 * <p>
 * This template is held and created inside the
 * <code>AbstractTableTemplate</code> template.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class AbstractRowTemplate extends VelocityTemplate implements
        IRowTemplate
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String ROW_NAME = "row";

    // --------------------------------------------------------------------------
    //
    // Private :: Fields
    //
    // --------------------------------------------------------------------------

    private Map<String, Object> row;

    private ITypeDefinition renderingDefinition;

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    protected IASDocRowConverter getRowConverter()
    {
        return getConfiguration().getRowConverter();
    }

    // ----------------------------------
    // renderingDefinition
    // ----------------------------------

    /**
     * Sets the <code>ITypeDefinition</code> that owns this detail row.
     * 
     * @param renderingDefinition the renderingDefinition to set
     */
    @Override
    public void setRenderingDefinition(ITypeDefinition renderingDefinition)
    {
        this.renderingDefinition = renderingDefinition;
    }

    /**
     * Returns the <code>ITypeDefinition</code> that owns this detail row.
     */
    @Override
    public ITypeDefinition getRenderingDefinition()
    {
        return renderingDefinition;
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public AbstractRowTemplate(IDocConfiguration config)
    {
        super(config);

        row = createMap(ROW_NAME);
    }

    // --------------------------------------------------------------------------
    //
    // Public API :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    public Object getRow(String name)
    {
        return row.get(name);
    }

    /**
     * Sets a template property on the current <code>row</code>.
     * 
     * @param name The name of the template property.
     * @param value The value of the template property.
     */
    @Override
    public void putRow(String name, Object value)
    {
        row.put(name, value);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected final Boolean renderTemplate()
    {
        convertRow();
        renderRow();
        return false;
    }

    // --------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    // --------------------------------------------------------------------------

    /**
     * Returns the current row <code>Map</code>.
     */
    protected Map<String, Object> getRow()
    {
        return row;
    }

    /**
     * Creates a <code>Map</code> that is inserted into the data map for the
     * template.
     * 
     * @param name The name of the map in the data map of the template.
     * @return A <code>Map</code> instance already inserted into the data map.
     */
    protected Map<String, Object> createMap(String name)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        // getData().put( name, map );
        set(name, map);
        return map;
    }

    // --------------------------------------------------------------------------
    //
    // Abstract Protected :: Methods
    //
    // --------------------------------------------------------------------------

    /**
     * Pre converts the row data, called in <code>renderTemplate()</code> before
     * <code>renderRow()</code>.
     */
    abstract protected void convertRow();

    /**
     * Override insubclasses to add convertion properties into the row template.
     */
    abstract protected void renderRow();
}
