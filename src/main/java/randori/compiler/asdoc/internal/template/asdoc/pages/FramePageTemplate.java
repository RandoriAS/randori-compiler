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

import java.io.File;

import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.TemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.TitleBarTemplate;
import randori.compiler.asdoc.internal.template.asdoc.data.SubNavigationVO;
import randori.compiler.asdoc.template.ITemplateRenderer;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * Renders a classFrame page with content, using setContentRendererInstance().
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class FramePageTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Private :: Fields
    //
    // --------------------------------------------------------------------------

    private Class<?> contentRenderer;

    private ASDocTemplateRenderer contentRendererInstance;

    private File contentTemplateFile;

    private Object subNavigationElements;

    private TitleBarTemplate titleBar;

    private ITemplateRenderer content;

    private String footer;

    @SuppressWarnings("unused")
    private IPackageBundle bundle;

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ----------------------------------
    // contentRenderer
    // ----------------------------------

    /**
	 * 
	 */
    public Class<?> getContentRenderer()
    {
        return contentRenderer;
    }

    /**
     * @param outputFile the outputFile to set
     */
    public void setContentRenderer(Class<?> contentRenderer)
    {
        this.contentRenderer = contentRenderer;
    }

    // ----------------------------------
    // contentRendererInstance
    // ----------------------------------

    /**
	 * 
	 */
    public ITemplateRenderer getContentRendererInstance()
    {
        return contentRendererInstance;
    }

    /**
     * @param outputFile the outputFile to set
     */
    public void setContentRendererInstance(
            TemplateRenderer contentRendererInstance)
    {
        this.contentRendererInstance = (ASDocTemplateRenderer) contentRendererInstance;
        contentRendererInstance.setBundle(getBundle());
    }

    // ----------------------------------
    // contentTemplateFile
    // ----------------------------------

    /**
     * @return the contentTemplateFile
     */
    public File getContentTemplateFile()
    {
        return contentTemplateFile;
    }

    /**
     * @param contentTemplateFile the contentTemplateFile to set
     */
    public void setContentTemplateFile(File contentTemplateFile)
    {
        this.contentTemplateFile = contentTemplateFile;
    }

    // ----------------------------------
    // subNavigationElements
    // ----------------------------------

    /**
     * Gets the elements that will appear in the subNavigation.
     */
    public Object getSubNavigationElements()
    {
        return subNavigationElements;
    }

    /**
     * @param contentTemplateFile the contentTemplateFile to set
     */
    public void setSubNavigationElements(Object subNavigationElements)
    {
        this.subNavigationElements = subNavigationElements;
    }

    // ----------------------------------
    // titleBar
    // ----------------------------------

    /**
     * Gets the elements that will appear in the subNavigation.
     */
    public TitleBarTemplate getTitleBar()
    {
        return titleBar;
    }

    /**
     * @param titleBar the titleBar to set
     */
    public void setTitleBar(TitleBarTemplate titleBar)
    {
        this.titleBar = titleBar;
        set("titleBar", titleBar);
    }

    // ----------------------------------
    // content
    // ----------------------------------

    /**
     * Gets the elements that will appear in the subNavigation.
     */
    public ITemplateRenderer getContent()
    {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(ITemplateRenderer content)
    {
        this.content = content;
        set("content", content);
    }

    // ----------------------------------
    // footer
    // ----------------------------------

    /**
     * Gets the elements that will appear in the subNavigation.
     */
    public String getFooter()
    {
        return footer;
    }

    /**
     * @param content the content to set
     */
    public void setFooter(String footer)
    {
        this.footer = footer;
        set("footer", footer);
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public FramePageTemplate(IDocConfiguration config)
    {
        super(config);

        setTemplateFile(IASDocTemplate.CLASS_FRAMEPAGE_TEMPLATE);
    }

    @Override
    public Boolean renderTemplate()
    {
        contentRendererInstance.setTemplateFile(getContentTemplateFile());
        contentRendererInstance.setContentData(getContentData());
        contentRendererInstance.setFramePage(this);

        setContent(contentRendererInstance);

        TitleBarTemplate titleBar = new TitleBarTemplate(getConfiguration());
        titleBar.setVisible(false);

        SubNavigationVO subNav = contentRendererInstance.getSubNavigation();
        if (subNav == null)
        {
            subNav = new SubNavigationVO();
        }

        titleBar.setSubNavigation(subNav);

        initialize(titleBar);

        setTitleBar(titleBar);

        setFooter(getConfiguration().getFooter());

        return true;
    }

}
