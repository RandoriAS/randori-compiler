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
import randori.compiler.asdoc.internal.access.MemberType;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateFactory;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.template.ITemplateRenderer;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;
import randori.compiler.asdoc.template.asdoc.IRowTemplate;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SummaryTableTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Private :: Fields
    //
    // --------------------------------------------------------------------------

    private List<IDefinition> definitions;
    private String headerText;
    private String mainHeaderText;
    private String lcTitleID;
    private String titleID;
    private String inheritedTitle;
    private List<String> rows;
    private String ucModifier;
    private String ucTypeSingular;
    private boolean allInherited;
    private MemberType elementKind;

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
    public List<IDefinition> getDefinitions()
    {
        return definitions;
    }

    /**
     * @param elements the elements to set
     */
    public void setDefinitions(List<IDefinition> elements)
    {
        this.definitions = elements;
    }

    // ----------------------------------
    // rows
    // ----------------------------------

    /**
     * @param elementKind the elementKind to set
     */
    public void setMemberType(MemberType type)
    {
        this.elementKind = type;
    }

    /**
     * @return the elementKind
     */
    public MemberType getMemberType()
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
    // inheritedTitle
    // ----------------------------------

    /**
     * @return the inheritedTitle
     */
    public String getInheritedTitle()
    {
        return inheritedTitle;
    }

    /**
     * @param inheritedTitle the inheritedTitle to set
     */
    public void setInheritedTitle(String inheritedTitle)
    {
        this.inheritedTitle = inheritedTitle;
        set("inheritedTitle", inheritedTitle);
    }

    // ----------------------------------
    // titleID
    // ----------------------------------

    /**
     * @return the titleID
     */
    public String getTitleID()
    {
        return titleID;
    }

    /**
     * @param titleID the titleID to set
     */
    public void setTitleID(String titleID)
    {
        this.titleID = titleID;
        set("titleID", titleID);
    }

    // ----------------------------------
    // lcTitleID
    // ----------------------------------

    /**
     * @return the lcTitleID
     */
    public String getLcTitleID()
    {
        return lcTitleID;
    }

    /**
     * @param lcTitleID the lcTitleID to set
     */
    public void setLcTitleID(String lcTitleID)
    {
        this.lcTitleID = lcTitleID;
        set("lcTitleID", lcTitleID);
    }

    // ----------------------------------
    // mainHeaderText
    // ----------------------------------

    /**
     * @param mainHeaderText the mainHeaderText to set
     */
    public void setMainHeaderText(String mainHeaderText)
    {
        this.mainHeaderText = mainHeaderText;
        set("mainHeaderText", mainHeaderText);
    }

    /**
     * @return the mainHeaderText
     */
    public String getMainHeaderText()
    {
        return mainHeaderText;
    }

    // ----------------------------------
    // ucModifier
    // ----------------------------------

    /**
     * @return the ucModifier
     */
    public String getUcModifier()
    {
        return ucModifier;
    }

    /**
     * @param ucModifier the ucModifier to set
     */
    public void setUcModifier(String ucModifier)
    {
        this.ucModifier = ucModifier;
        set("ucModifier", ucModifier);
    }

    // ----------------------------------
    // ucTypeSingular
    // ----------------------------------

    /**
     * @return the ucTypeSingular
     */
    public String getUcTypeSingular()
    {
        return ucTypeSingular;
    }

    /**
     * @param ucTypeSingular the ucTypeSingular to set
     */
    public void setUcTypeSingular(String ucTypeSingular)
    {
        this.ucTypeSingular = ucTypeSingular;
        set("ucTypeSingular", ucTypeSingular);
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

    // ----------------------------------
    // headerText
    // ----------------------------------

    /**
     * @return the headerText
     */
    public String getHeaderText()
    {
        return headerText;
    }

    /**
     * @param headerText the headerText to set
     */
    public void setHeaderText(String headerText)
    {
        this.headerText = headerText;
        set("headerText", headerText);
    }

    // --------------------------------------------------------------------------
    //
    // Construtor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SummaryTableTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.SUMMARY_TABLE_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    public void initialize(ITemplateRenderer template)
    {
        IRowTemplate asdoc = (IRowTemplate) template;
        if (asdoc == null)
        {
            return; // XXX
        }
        asdoc.setBasePath(getBasePath());
        //asdoc.setTitle(getTitle());
        //asdoc.setSubTitle(getSubTitle());
        //asdoc.setMainTitle(getMainTitle());
        // XXX get rid of set()
        asdoc.setProperty("ucModifier", getUcModifier());
        asdoc.setProperty("ucTypeSingular", getUcTypeSingular());

        intializeRow((AbstractRowTemplate) template);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected Boolean renderTemplate()
    {
        List<String> rows = new ArrayList<String>();

        boolean flag = false;
        boolean hasInherited = false;
        boolean allInherited = true;

        for (IDefinition e : definitions)
        {
            SummaryRowTemplate rowTemplate = ASDocTemplateFactory
                    .createSummaryRow(e, getConfiguration());

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
                hasInherited = true;
            else
                allInherited = false;

            initialize(rowTemplate);

            rowTemplate.putRow("isInherited", inherited);
            rowTemplate.setDefinition(e);
            rowTemplate.setProperty("rowFlag", flag);

            rows.add(rowTemplate.render());
            
            if (!inherited)
                flag = !flag;
        }

        setAllInherited(allInherited);

        set("hasInherited", hasInherited);
        set("rows", rows);

        return false;
    }

    // --------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    // --------------------------------------------------------------------------

    /**
     * Initializes the <code>AbstractRowTemplate</code>.
     * 
     * @param The <code>AbstractRowTemplate</code> instance.
     */
    protected void intializeRow(AbstractRowTemplate row)
    {
        row.setRenderingDefinition((ITypeDefinition) getDefinition());
    }
}
