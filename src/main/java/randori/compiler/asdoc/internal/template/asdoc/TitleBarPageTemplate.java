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

package randori.compiler.asdoc.internal.template.asdoc;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class TitleBarPageTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ---------------------------------
    // titleBar
    // ---------------------------------

    private TitleBarTemplate titleBar = null;

    /**
     * @return integer
     */
    public TitleBarTemplate getTitleBar()
    {
        return titleBar;
    }

    /**
     * @param integer $value
     */
    public void setTitleBar(TitleBarTemplate titleBar)
    {
        this.titleBar = titleBar;
        initialize(titleBar);
        set("titleBar", titleBar);
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public TitleBarPageTemplate(IDocConfiguration config)
    {
        super(config);
        setOutputFile(IASDocTemplate.TITLE_BAR_PAGE_OUTPUT);
        setTemplateFile(IASDocTemplate.TITLE_BAR_PAGE_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected Boolean renderTemplate()
    {
        TitleBarTemplate titleBar = new TitleBarTemplate(getConfiguration());

        titleBar.setTemplateFile(IASDocTemplate.TITLE_BAR_TEMPLATE);
        titleBar.setTarget(true);
        setTitleBar(titleBar);

        return true;
    }
}
