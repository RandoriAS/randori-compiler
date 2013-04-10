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

package randori.compiler.asdoc.internal.utils;

import java.io.StringWriter;

import org.apache.flex.compiler.common.ASModifier;
import org.apache.flex.compiler.common.ModifiersSet;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.INamespaceDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;

import com.googlecode.jatl.Html;

public final class HTMLUtils
{

    public static Html html(StringWriter writer)
    {
        return new Html(writer);
    }

    public static String returnPackageDetailLinkSimple(String packageName)
    {
        String base = convertToHTMLPath(packageName);
        String link = base + "/package-detail.html";
        return link;
    }

    public static String returnReletiveBasePath(String path, String splitter)
    {
        String value = "";
        if (path != null && path != "" && path != "toplevel")
        {
            String[] split = path.split(splitter);
            int len = split.length;
            for (int i = 0; i < len; i++)
            {
                value += "../";
            }
        }
        return value;
    }

    public static String returnFullHTMLPath(ITypeDefinition element)
    {
        return convertToHTMLPath(element.getQualifiedName()) + ".html";
    }

    public static String convertPackageNameToPath(String name)
    {
        return name.replace(".", "/");
    }

    public static String convertTypeNameToPath(String name)
    {
        return name.replace(".", "/") + ".html";
    }

    public static String returnClassListLinkSimple(String packageName)
    {
        String base = convertToHTMLPath(packageName);
        String link = base + "/class-list.html";
        return link;
    }

    private static String convertToHTMLPath(String name)
    {
        if (name.indexOf("toplevel.") == 0)
            name = name.replace("toplevel.", "");
        return name.replace(".", "/");
    }

    public static String toModifierString(ModifiersSet modifiers)
    {
        StringBuffer sb = new StringBuffer();
        for (ASModifier modifier : modifiers.getAllModifiers())
        {
            sb.append(modifier.toString());
            sb.append(" ");
        }
        String result = sb.toString().trim();
        if (result.equals(""))
            return null;

        return result;
    }

    public static String toFileType(ITypeDefinition definition)
    {
        String type = "";
        if (definition instanceof IClassDefinition)
        {
            type = "Class";
        }
        else if (definition instanceof IInterfaceDefinition)
        {
            type = "Interface";
        }
        else if (definition instanceof IFunctionDefinition)
        {
            type = "Function";
        }
        else if (definition instanceof INamespaceDefinition)
        {
            type = "Namespace";
        }

        return type;
    }
}
