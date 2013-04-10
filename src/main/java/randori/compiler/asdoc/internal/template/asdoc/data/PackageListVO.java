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
 * Variable context for the package list templates
 * 
 * @see PackageListPaneProxy
 * @see PackageListPageContentProxy
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class PackageListVO implements Comparable<PackageListVO>
{
    // --------------------------------------------------------------------------
    //
    // Private :: Fields
    //
    // --------------------------------------------------------------------------

    private String name;

    private String anchor;

    private String shortDescription;

    private Boolean rowFlag;

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
    // anchor
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
    // shortDescription
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
    // rowFlag
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
    public int compareTo(PackageListVO o)
    {
        return name.compareTo(o.getName());
    }
}
