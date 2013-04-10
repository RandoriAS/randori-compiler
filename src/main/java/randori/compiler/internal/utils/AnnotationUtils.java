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

package randori.compiler.internal.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.scopes.IDefinitionSet;

/**
 * @author Michael Schmalle
 */
public class AnnotationUtils
{
    public static IClassDefinition getClassDefinitionQualified(
            IASProject project, String packageName, String baseName)
    {
        IDefinition definition = null;
        IDefinitionSet set = project.getScope().getLocalDefinitionSetByName(
                baseName);
        String qualifiedName = baseName;
        if (!packageName.equals(""))
            qualifiedName = packageName + "." + baseName;

        final int len = set.getSize();
        for (int i = 0; i < len; i++)
        {
            definition = set.getDefinition(i);
            if (definition.getQualifiedName().equals(qualifiedName))
                return (IClassDefinition) definition;
        }
        return null;
    }

    public static String toEnumFieldName(String type)
    {
        if (type.indexOf(".") == -1)
            return type;
        return StringUtils.substringAfterLast(type, ".");
    }

    public static String toEnumQualifiedName(String type)
    {
        if (type.indexOf(".") == -1)
            return type;
        return StringUtils.substringBeforeLast(type, ".");
    }
}
