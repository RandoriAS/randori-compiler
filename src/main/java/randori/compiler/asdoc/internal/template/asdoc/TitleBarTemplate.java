////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package randori.compiler.asdoc.internal.template.asdoc;

import java.util.List;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.data.NavigationVO;
import randori.compiler.asdoc.internal.template.asdoc.data.PrimaryLink;
import randori.compiler.asdoc.internal.template.asdoc.data.SubNavigationVO;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class TitleBarTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ---------------------------------
    // target
    // ---------------------------------

    private Boolean target = false;

    /**
     * @return integer
     */
    public Boolean getTarget()
    {
        return target;
    }

    /**
     * @param integer $value
     */
    public void setTarget(Boolean target)
    {
        this.target = target;
        set("target", target);
    }

    // ---------------------------------
    // visible
    // ---------------------------------

    private Boolean visible = null;

    /**
     * @return integer
     */
    public Boolean getVisible()
    {
        return visible;
    }

    /**
     * @param integer $value
     */
    public void setVisible(Boolean visible)
    {
        this.visible = visible;
        set("visible", visible);
    }

    // ---------------------------------
    // navigation
    // --------------------------------

    private NavigationVO navigation = null;

    /**
     * @return integer
     */
    public NavigationVO getNavigation()
    {
        return navigation;
    }

    /**
     * @param integer $value
     */
    public void setNavigation(NavigationVO navigation)
    {
        this.navigation = navigation;
        set("navigation", navigation);
    }

    // ---------------------------------
    // subNavigation
    // --------------------------------

    private SubNavigationVO subNavigation = null;

    /**
     * @return integer
     */
    public SubNavigationVO getSubNnavigation()
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
        set("subNavigation", subNavigation);
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public TitleBarTemplate(IDocConfiguration config)
    {
        super(config);
        setTemplateFile(IASDocTemplate.TITLE_BAR_TEMPLATE);

        setVisible(true);
    }

    @Override
    public Boolean renderTemplate()
    {
        NavigationVO nav = new NavigationVO();

        nav.setFrames(getConfiguration().getFrames());
        nav.setPrimaryLinks(getConfiguration().getPrimaryLinks());

        set("mainTitle", getConfiguration().getMainTitle());

        set("logoAlt", "Teoti Graphix, LLC");
        set("logoTitle", "Teoti Graphix, LLC");
        set("logoSrc", getConfiguration().getLogo());

        String url = getRestoreFramesURL();
        nav.setRestoreFramesPath(url);

        setNavigation(nav);

        return false;
    }

    private String getRestoreFramesURL()
    {
        StringBuffer sb = new StringBuffer();

        List<PrimaryLink> items = getConfiguration().getPrimaryLinks();

        for (PrimaryLink vo : items)
        {
            if (vo.getName().equals(getTitle()))
            {
                sb.append("index.html");
                sb.append("?");
                sb.append(vo.getClassFrame());
                sb.append("&");
                sb.append(vo.getClassListFrame());

                return sb.toString();
            }
        }
        /*
                if (getElement() == null) {
                    return "";
                }

                if (getElement() instanceof IASType) {
                    IASType element = (IASType) getElement();

                    String packagePath = element.getPackageName().replace('.', '/');
                    String elementPath = element.getQName().getQualifiedName()
                            .replace('.', '/')
                            + ".html";

                    sb.append(getBasePath());
                    sb.append("index.html");
                    sb.append("?");
                    sb.append(elementPath);
                    sb.append("&");
                    sb.append(packagePath);
                    sb.append("/class-list.html");
                } else if (getElement() instanceof IASPackage) {
                    IASPackage element = (IASPackage) getElement();

                    String packagePath = element.getName().replace('.', '/');

                    sb.append(getBasePath());
                    sb.append("index.html");
                    sb.append("?");
                    sb.append(packagePath);
                    sb.append("/package-detail.html");
                    sb.append("&");
                    sb.append(packagePath);
                    sb.append("/class-list.html");
                }
        */
        return sb.toString();
    }
}
