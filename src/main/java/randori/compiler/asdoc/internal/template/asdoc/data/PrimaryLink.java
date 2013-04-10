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

public class PrimaryLink
{
    private String name; // link name

    private String packageListFrame; // top left

    private String classListFrame; // bottom left

    private String classFrame; // main content

    private Boolean included = true; // link included

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPackageListFrame()
    {
        return packageListFrame;
    }

    public void setPackageListFrame(String packageListFrame)
    {
        this.packageListFrame = packageListFrame;
    }

    public String getClassListFrame()
    {
        return classListFrame;
    }

    public void setClassListFrame(String classListFrame)
    {
        this.classListFrame = classListFrame;
    }

    public String getClassFrame()
    {
        return classFrame;
    }

    public void setClassFrame(String classFrame)
    {
        this.classFrame = classFrame;
    }

    public Boolean getIncluded()
    {
        return included;
    }

    public PrimaryLink(String name, String packageListFrame,
            String classListFrame, String classFrame)
    {
        this.name = name;
        this.packageListFrame = packageListFrame;
        this.classListFrame = classListFrame;
        this.classFrame = classFrame;
    }

}
