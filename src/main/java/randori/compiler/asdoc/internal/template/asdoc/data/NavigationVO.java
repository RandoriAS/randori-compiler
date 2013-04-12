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

import java.util.ArrayList;
import java.util.List;

public class NavigationVO
{
    private Boolean hasFrames = true;
    private String restoreFramesPath;

    private List<PrimaryLink> primaryLinks = new ArrayList<PrimaryLink>();

    public Boolean getHasFrames()
    {
        return hasFrames;
    }

    public void setFrames(Boolean hasFrames)
    {
        this.hasFrames = hasFrames;
    }

    /**
     * @param restoreFramesPath the restoreFramesPath to set
     */
    public void setRestoreFramesPath(String restoreFramesPath)
    {
        this.restoreFramesPath = restoreFramesPath;
    }

    /**
     * @return the restoreFramesPath
     */
    public String getRestoreFramesPath()
    {
        return restoreFramesPath;
    }

    /**
     * @param primaryLinks the primaryLinks to set
     */
    public void setPrimaryLinks(List<PrimaryLink> primaryLinks)
    {
        this.primaryLinks = primaryLinks;
    }

    /**
     * @return the primaryLinks
     */
    public List<PrimaryLink> getPrimaryLinks()
    {
        return primaryLinks;
    }
}
