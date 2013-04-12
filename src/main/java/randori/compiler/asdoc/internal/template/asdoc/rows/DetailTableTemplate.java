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

import java.util.ArrayList;
import java.util.List;

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IMetadataDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateFactory;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.template.ITemplateRenderer;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;
import randori.compiler.asdoc.template.asdoc.IDetailRowTemplate;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class DetailTableTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String NAME = "DetailTableTemplate";

    // --------------------------------------------------------------------------
    //
    // Private :: Fields
    //
    // --------------------------------------------------------------------------

    private List<IDefinition> elements;
    private String elementKind;
    private List<String> rows;
    private boolean allInherited;

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ----------------------------------
    // elements
    // ----------------------------------

    /**
     * @return the elements
     */
    public List<IDefinition> getElements()
    {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(List<IDefinition> elements)
    {
        this.elements = elements;
    }

    // ----------------------------------
    // rows
    // ----------------------------------

    /**
     * @param elementKind the elementKind to set
     */
    public void setElementKind(String elementKind)
    {
        this.elementKind = elementKind;
    }

    /**
     * @return the elementKind
     */
    public String getElementKind()
    {
        return elementKind;
    }

    // ----------------------------------
    // rows
    // ----------------------------------

    /**
     * @return the rows
     */
    public List<String> getRows()
    {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<String> rows)
    {
        this.rows = rows;
        set("rows", rows);
    }

    // ----------------------------------
    // allInherited
    // ----------------------------------

    /**
     * @return the allInherited
     */
    public boolean isAllInherited()
    {
        return allInherited;
    }

    /**
     * @param allInherited the allInherited to set
     */
    public void setAllInherited(boolean allInherited)
    {
        this.allInherited = allInherited;
        set("allInherited", allInherited);
    }

    // --------------------------------------------------------------------------
    //
    // Construtor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public DetailTableTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.DETAIL_TABLE_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    public void initialize(ITemplateRenderer template)
    {
        super.initialize(template);
        // XXX get rid of set()
        template.setProperty("lcModifier", get("lcModifier"));
        template.setProperty("ucModifier", get("ucModifier"));
        template.setProperty("ucTypeSingular", get("ucTypeSingular"));
    }

    @Override
    protected Boolean renderTemplate()
    {
        List<String> result = new ArrayList<String>();

        boolean flag = false;
        boolean hasInherited = false;
        boolean allInherited = true;

        for (IDefinition e : elements)
        {
            IDetailRowTemplate tpl = ASDocTemplateFactory.createDetailRow(e,
                    getConfiguration());
            if (tpl == null)
                return false;
            
            ITypeDefinition parent = null;
            if (e instanceof IMetadataDefinition)
            {
                parent = (ITypeDefinition) ((IMetadataDefinition) e)
                        .getDecoratedDefinition();
            }
            else
            {
                parent = (ITypeDefinition) e.getParent();
            }

            boolean inherited = !parent.equals(getDefinition());
            if (inherited)
                continue;

            if (inherited)
                hasInherited = true;
            else
                allInherited = false;

            tpl.setRenderingDefinition(parent);
            // XXX            tpl.set("rowFlag", flag);

            tpl.putRow("isInherited", inherited);

            if (inherited)
            {
                hasInherited = true;
            }
            else
            {
                allInherited = false;
            }

            initialize(tpl);

            tpl.setDefinition(e);

            result.add(tpl.render());

            flag = !flag;
        }

        setAllInherited(allInherited);

        set("hasInherited", hasInherited);
        set("rows", result);

        return false;
    }
}
