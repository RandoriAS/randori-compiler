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
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * Renders the <code>output/package-frame.html</code> index.
 * <p>
 * This is the left (top left / bottom left) empty frame that loads the
 * <code>package-list.html (top left)</code> and <code>all-classes.html</code>
 * (bottom left) panes.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class PackageFrameTemplate extends VelocityTemplate
{

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PackageFrameTemplate(IDocConfiguration config)
    {
        super(config);
        setOutputFile(IASDocTemplate.PACKAGE_FRAME_OUTPUT);
        setTemplateFile(IASDocTemplate.PACKAGE_FRAME_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected Boolean renderTemplate()
    {
        return true;
    }
}
