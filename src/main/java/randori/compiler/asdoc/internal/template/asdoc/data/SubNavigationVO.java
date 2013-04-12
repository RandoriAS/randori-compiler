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

package randori.compiler.asdoc.internal.template.asdoc.data;

import randori.compiler.asdoc.internal.access.MemberType;

public class SubNavigationVO
{
    private Boolean showVariables = false;
    private Boolean showProperties = false;
    private Boolean showMethods = false;
    private Boolean showEvents = false;
    private Boolean showStyles = false;
    private Boolean showParts = false;
    private Boolean showStates = false;
    private Boolean showEffects = false;
    private Boolean showConstants = false;
    private Boolean showExamples = false;
    private Boolean showClasses = false;
    private Boolean showInterfaces = false;

    public Boolean getShowVariables()
    {
        return showVariables;
    }

    public void setShowVariables(Boolean showVariables)
    {
        this.showVariables = showVariables;
    }

    public Boolean getShowProperties()
    {
        return showProperties;
    }

    public void setShowProperties(Boolean showProperties)
    {
        this.showProperties = showProperties;
    }

    public Boolean getShowMethods()
    {
        return showMethods;
    }

    public void setShowMethods(Boolean showMethods)
    {
        this.showMethods = showMethods;
    }

    public Boolean getShowEvents()
    {
        return showEvents;
    }

    public void setShowEvents(Boolean showEvents)
    {
        this.showEvents = showEvents;
    }

    public Boolean getShowStyles()
    {
        return showStyles;
    }

    public void setShowStyles(Boolean showStyles)
    {
        this.showStyles = showStyles;
    }

    public Boolean getShowParts()
    {
        return showParts;
    }

    public void setShowParts(Boolean showParts)
    {
        this.showParts = showParts;
    }

    public Boolean getShowStates()
    {
        return showStates;
    }

    public void setShowStates(Boolean showStates)
    {
        this.showStates = showStates;
    }

    public Boolean getShowEffects()
    {
        return showEffects;
    }

    public void setShowEffects(Boolean showEffects)
    {
        this.showEffects = showEffects;
    }

    public Boolean getShowConstants()
    {
        return showConstants;
    }

    public void setShowConstants(Boolean showConstants)
    {
        this.showConstants = showConstants;
    }

    public Boolean getShowExamples()
    {
        return showExamples;
    }

    public void setShowExamples(Boolean showExamples)
    {
        this.showExamples = showExamples;
    }

    public Boolean getShowClasses()
    {
        return showClasses;
    }

    public void setShowClasses(Boolean showClasses)
    {
        this.showClasses = showClasses;
    }

    public Boolean getShowInterfaces()
    {
        return showInterfaces;
    }

    public void setShowInterfaces(Boolean showInterfaces)
    {
        this.showInterfaces = showInterfaces;
    }

    public void setSubNavItem(MemberType type, boolean show)
    {
        if (type.equals(MemberType.VARIALBE))
        {
            setShowVariables(show);
        }
        if (type.equals(MemberType.CONSTANT))
        {
            setShowConstants(show);
        }
        if (type.equals(MemberType.ACCESSOR))
        {
            setShowProperties(show);
        }
        if (type.equals(MemberType.METHOD))
        {
            setShowMethods(show);
        }
        if (type.equals(MemberType.SKINSTATE))
        {
            setShowStates(show);
        }
        if (type.equals(MemberType.SKINPART))
        {
            setShowParts(show);
        }
        if (type.equals(MemberType.STYLE))
        {
            setShowStyles(show);
        }
        if (type.equals(MemberType.EFFECT))
        {
            setShowEffects(show);
        }
        if (type.equals(MemberType.EVENT))
        {
            setShowEvents(show);
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append(showVariables.toString());
        buffer.append(", ");
        buffer.append(showProperties.toString());
        buffer.append(", ");
        buffer.append(showMethods.toString());
        buffer.append(", ");
        buffer.append(showEvents.toString());
        buffer.append(", ");
        buffer.append(showStyles.toString());
        buffer.append(", ");
        buffer.append(showParts.toString());
        buffer.append(", ");
        buffer.append(showStates.toString());
        buffer.append(", ");
        buffer.append(showEffects.toString());
        buffer.append(", ");
        buffer.append(showConstants.toString());
        buffer.append(", ");
        buffer.append(showExamples.toString());
        buffer.append(", ");
        buffer.append(showClasses.toString());
        buffer.append(", ");
        buffer.append(showInterfaces.toString());

        return buffer.toString();
    }

}
