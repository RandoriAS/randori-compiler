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

package randori.compiler.bundle.io;

import org.apache.flex.swc.ISWC;

import randori.compiler.bundle.IBundleCategory;

/**
 * @author Michael Schmalle
 */
public class BundleUtils
{
    private static final String SLASH = "/";

    private static final String DOT_SWC = ".swc";

    public static String toPath(IBundleCategory category, String path)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(category.getLibrary().getName());
        sb.append(SLASH);
        sb.append(category.getContainer().getName());
        sb.append(SLASH);
        sb.append(category.getType().getValue());
        sb.append(SLASH);
        sb.append(path);
        return sb.toString();
    }

    public static String toSWCName(ISWC swc)
    {
        return swc.getSWCFile().getName().replace(DOT_SWC, "");
    }
}
