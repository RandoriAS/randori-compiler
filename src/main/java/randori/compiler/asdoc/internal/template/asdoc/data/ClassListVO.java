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

/**
 * Variable context for the class list templates.
 * 
 * @see ClassListPaneProxy
 * @see ClassListPageContentProxy
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class ClassListVO implements Comparable<ClassListVO>
{
    // --------------------------------------------------------------------------
    //
    // Private :: Fields
    //
    // --------------------------------------------------------------------------

    private String name = "";

    private String anchor = "";

    private String packageAnchor = "";

    private Boolean isClass = false;

    private String shortDescription = "";

    private Boolean rowFlag = false;

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ---------------------------------
    // name
    // ---------------------------------

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    // ---------------------------------
    // name
    // ---------------------------------

    /**
     * @return the anchor
     */
    public String getAnchor()
    {
        return anchor;
    }

    /**
     * @param anchor the anchor to set
     */
    public void setAnchor(String anchor)
    {
        this.anchor = anchor;
    }

    // ---------------------------------
    // name
    // ---------------------------------

    /**
     * @return the packageAnchor
     */
    public String getPackageAnchor()
    {
        return packageAnchor;
    }

    /**
     * @param packageAnchor the packageAnchor to set
     */
    public void setPackageAnchor(String packageAnchor)
    {
        this.packageAnchor = packageAnchor;
    }

    // ---------------------------------
    // name
    // ---------------------------------

    /**
     * @return the isClass
     */
    public Boolean getIsClass()
    {
        return isClass;
    }

    /**
     * @param isClass the isClass to set
     */
    public void setIsClass(Boolean isClass)
    {
        this.isClass = isClass;
    }

    // ---------------------------------
    // name
    // ---------------------------------

    /**
     * @return the shortDescription
     */
    public String getShortDescription()
    {
        return shortDescription;
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    // ---------------------------------
    // name
    // ---------------------------------

    /**
     * @return the rowFlag
     */
    public Boolean getRowFlag()
    {
        return rowFlag;
    }

    /**
     * @param rowFlag the rowFlag to set
     */
    public void setRowFlag(Boolean rowFlag)
    {
        this.rowFlag = rowFlag;
    }

    // --------------------------------------------------------------------------
    //
    // Comparable API :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    public int compareTo(ClassListVO o)
    {
        return name.compareTo(o.getName());
    }
}
