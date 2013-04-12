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
import randori.compiler.asdoc.internal.template.VelocityTemplate;
import randori.compiler.asdoc.internal.template.asdoc.data.SubNavigationVO;
import randori.compiler.asdoc.internal.template.asdoc.pages.FramePageTemplate;
import randori.compiler.asdoc.template.ITemplateRenderer;
import randori.compiler.asdoc.template.asdoc.IASDocConverter;
import randori.compiler.asdoc.template.asdoc.IASDocTemplateRenderer;

public abstract class ASDocTemplateRenderer extends VelocityTemplate implements
        IASDocTemplateRenderer
{
    protected IASDocConverter getConvertor()
    {
        return getConfiguration().getConverter();
    }

    // ----------------------------------
    // framePage
    // ----------------------------------

    // FIXME Move to abstract superclass for all framepagecontent classes

    private FramePageTemplate framePage;

    /**
     * @return the framePage
     */
    @Override
    public FramePageTemplate getFramePage()
    {
        return framePage;
    }

    /**
     * @param framePage the framePage to set
     */
    @Override
    public void setFramePage(FramePageTemplate framePage)
    {
        this.framePage = framePage;
    }

    // ---------------------------------
    // subNavigation
    // --------------------------------

    private SubNavigationVO subNavigation = null;

    /**
     * @return integer
     */
    @Override
    public SubNavigationVO getSubNavigation()
    {
        return subNavigation;
    }

    /**
     * @param integer $value
     */
    @Override
    public void setSubNavigation(SubNavigationVO subNavigation)
    {
        this.subNavigation = subNavigation;
    }

    // ---------------------------------
    // title
    // ---------------------------------

    private String title = null;

    /**
     * @return string
     */
    @Override
    public String getTitle()
    {
        return title;
    }

    /**
     * @param string title
     */
    @Override
    public void setTitle(String title)
    {
        this.title = title;
        set("title", title);
    }

    // ---------------------------------
    // mainTitle
    // ---------------------------------

    private String mainTitle = null;

    /**
     * @return string
     */
    @Override
    public String getMainTitle()
    {
        return mainTitle;
    }

    /**
     * @param string title
     */
    @Override
    public void setMainTitle(String mainTitle)
    {
        this.mainTitle = mainTitle;
        set("mainTitle", mainTitle);
    }

    // ---------------------------------
    // windowTitle
    // ---------------------------------

    private String windowTitle = null;

    /**
     * @return string
     */
    @Override
    public String getWindowTitle()
    {
        return windowTitle;
    }

    /**
     * @param string title
     */
    @Override
    public void setWindowTitle(String windowTitle)
    {
        this.windowTitle = mainTitle;
        set("windowTitle", windowTitle);
    }

    // ---------------------------------
    // subTitle
    // ---------------------------------

    private String subTitle = " ";

    /**
     * @return string
     */
    @Override
    public String getSubTitle()
    {
        return subTitle;
    }

    /**
     * @param string title
     */
    @Override
    public void setSubTitle(String subTitle)
    {
        this.subTitle = subTitle;
        set("subTitle", subTitle);
    }

    public ASDocTemplateRenderer(IDocConfiguration config)
    {
        super(config);
    }

    @Override
    protected abstract Boolean renderTemplate();

    @Override
    public void initialize(ITemplateRenderer template)
    {
        super.initialize(template);
        if (template instanceof IASDocTemplateRenderer)
        {
            IASDocTemplateRenderer asdoc = (IASDocTemplateRenderer) template;
            asdoc.setBasePath(getBasePath());
            asdoc.setTitle(title);
            asdoc.setSubTitle(subTitle);
            asdoc.setMainTitle(mainTitle);
        }
    }
}
