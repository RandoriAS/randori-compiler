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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.VelocityTemplate;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

public class IndexFrameTemplate extends VelocityTemplate
{

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ---------------------------------
    // leftFramesetWidth
    // ---------------------------------

    private int leftFramesetWidth;

    /**
     * Returns the leftFramesetWidth.
     * 
     * @return integer
     */
    public int getLeftFramesetWidth()
    {
        return leftFramesetWidth;
    }

    /**
     * Sets the leftFramesetWidth.
     * 
     * @param integer leftFramesetWidth
     */
    public void setLeftFramesetWidth(int leftFramesetWidth)
    {
        this.leftFramesetWidth = leftFramesetWidth;
        set("leftFramesetWidth", leftFramesetWidth);
    }

    public IndexFrameTemplate(IDocConfiguration config)
    {
        super(config);
        setOutputFile(IASDocTemplate.INDEX_OUTPUT);
        setTemplateFile(IASDocTemplate.INDEX_TEMPLATE);
    }

    @Override
    protected Boolean renderTemplate()
    {
        setLeftFramesetWidth(getConfiguration().getLeftFrameWidth());

        try
        {
            // copy images directory
            FileUtils.copyDirectory(new File(getTemplateBase(),
                    IASDocTemplate.MEDIA_IMAGES), new File(getOutputBase(),
                    "images"));

            // copy asdoc.js, cookies.js, print.css, style.css
            FileUtils.copyFile(new File(getTemplateBase(),
                    IASDocTemplate.MEDIA_ASDOC_JS), new File(getOutputBase(),
                    "asdoc.js"));
            FileUtils.copyFile(new File(getTemplateBase(),
                    IASDocTemplate.MEDIA_COOKIES_JS), new File(getOutputBase(),
                    "cookies.js"));
            FileUtils.copyFile(new File(getTemplateBase(),
                    IASDocTemplate.MEDIA_PRINT_CSS), new File(getOutputBase(),
                    "print.css"));
            FileUtils.copyFile(new File(getTemplateBase(),
                    IASDocTemplate.MEDIA_STYLE_CSS), new File(getOutputBase(),
                    "style.css"));

        }
        catch (IOException e)
        {
            // Log.error("IndexFrameTemplate::renderTemplate() - "
            // + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
