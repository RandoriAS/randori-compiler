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
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.flex.compiler.common.ModifiersSet;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.ITypeNode;

import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.comment.IDocComment;
import randori.compiler.asdoc.comment.IDocTag;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.renderer.Convertor;
import randori.compiler.asdoc.internal.utils.HTMLUtils;
import randori.compiler.asdoc.template.asdoc.IASDocConverter;

public class ASDocConverter extends Convertor implements IASDocConverter
{

    public ASDocConverter(IDocConfiguration config)
    {
        super(config);
    }

    @Override
    public String returnPackageListPaneLink(IPackageBundle element)
    {
        String name = element.getQualifiedName();
        String path = HTMLUtils.convertPackageNameToPath(name);

        StringWriter sw = new StringWriter();
        HTMLUtils.html(sw).a().attr("target", "classFrame")
                .attr("onclick", returnLoadClassFrameJS(name))
                .attr("href", path + "/package-detail.html").text(name)
                .endAll();

        return sw.getBuffer().toString();
    }

    private static String returnLoadClassFrameJS(String name)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("javascript:loadClassListFrame('");
        sb.append(HTMLUtils.convertPackageNameToPath(name));

        if (name.equals(""))
        {
            sb.append("class-list.html');");
        }
        else
        {
            sb.append("/class-list.html');");
        }

        return sb.toString();
    }

    @Override
    public String returnPackageDescription(IPackageBundle bundle)
    {
        // look for the first description on the element's source path
        File file = new File(bundle.getAbsolutePath() + "/"
                + "package-description.html");

        if (file.exists())
        {
            try
            {
                return FileUtils.readFileToString(file);
            }
            catch (IOException e)
            {
            }
        }

        // look in the source paths by order List<String> paths =
        // configuration.getSourcePaths();
        // for (String path : paths) {
        // String name = element.getName();
        // if (name.equals("toplevel"))
        // name = "";
        // String cp = element.getName().replace(".", "/");
        // path = path + "/" + cp + "/package-description.html";
        // file = new File(path);
        // if (file.exists()) {
        // try {
        // return FileUtil.readContent(file);
        // } catch (IOException e) {
        // }
        // }
        // }

        return "TODO impl returnPackageDescription()";
    }

    @Override
    public String returnTypeListPaneLink(ITypeDefinition definition)
    {
        // org/example/core/ClassA
        String qname = definition.getQualifiedName();
        String name = definition.getBaseName();
        String pname = definition.getPackageName();
        if (pname == null || pname.equals("toplevel"))
        {
            qname = name;
        }

        String path = HTMLUtils.convertTypeNameToPath(qname);

        StringWriter sw = new StringWriter();
        HTMLUtils.html(sw).a().attr("target", "classFrame").attr("href", path)
                .text(name).endAll();

        return sw.getBuffer().toString();
    }

    @Override
    public String returnTypeContentLink(ITypeDefinition definition,
            IPackageBundle bundle)
    {
        if (definition == null || bundle == null)
        {
            return ""; // FIXME
        }

        // check to see if this is included from a SWC file
        // the node ref will be null
        ITypeNode node = definition.getNode();
        if (node == null)
        {
            return definition.getQualifiedName();
        }

        String qname = definition.getQualifiedName();
        String name = definition.getBaseName();
        String contextPath = bundle.getQualifiedName();

        if (definition.getPackageName() == null
                || definition.getPackageName().equals("toplevel"))
        {
            qname = name;
        }

        String path = HTMLUtils.convertTypeNameToPath(qname);
        String base = HTMLUtils.returnReletiveBasePath(contextPath, "\\.");

        StringWriter sw = new StringWriter();
        HTMLUtils.html(sw).a().attr("href", base + path).text(name).endAll();
        return sw.getBuffer().toString();
    }

    @Override
    public String returnShortDescription(IDocumentableDefinition aware)
    {
        String result = "&nbsp;";
        if (aware.hasExplicitComment())
        {
            IDocComment comment = (IDocComment) aware
                    .getExplicitSourceComment();
            String ldesc = comment.getDescription();
            if (ldesc != null)
            {
                int index = ldesc.indexOf(".");
                if (index != -1)
                {
                    result = ldesc.substring(0, index + 1);
                }
                else
                {
                    index = ldesc.indexOf("\\n");
                    if (index != -1)
                        result = ldesc.substring(0, index + 1);
                    else
                        result = ldesc;
                }
            }
        }
        return result;
    }

    @Override
    public String returnLongDescription(IDocumentableDefinition aware)
    {
        String result = "";
        if (aware.hasExplicitComment())
        {
            IDocComment comment = (IDocComment) aware
                    .getExplicitSourceComment();
            String sdesc = comment.getDescription();
            if (sdesc != null)
            {
                int index = sdesc.indexOf(".");
                if (index != -1)
                {
                    result = sdesc.substring(index + 1);
                }
            }
        }
        return result;
    }

    @Override
    public String returnSourceTag(IDefinition definition)
    {
        int start = definition.getAbsoluteStart();
        int end = definition.getAbsoluteEnd();
        String path = definition.getContainingFilePath();
        File file = new File(path);
        if (file.exists())
        {
            try
            {
                String data = FileUtils.readFileToString(file);
                data = data.substring(start, end);
                data = data.replace("<", "&lt;");
                data = data.replace(">", "&gt;");
                data = data.replace("\t", "    ");
                // XXX make a listener hook here for code modification
                return data;
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String returnTypeLink(ITypeDefinition definition, String context)
    {
        return returnTypeLink(definition, context, false);
    }

    @Override
    public String returnTypeLink(ITypeDefinition definition, String context,
            boolean showQualified)
    {
        ITypeNode node = definition.getNode();
        if (node == null)
        {
            return definition.getQualifiedName();
        }

        String name = definition.getBaseName().trim();
        String qname = definition.getQualifiedName();

        String path = HTMLUtils.convertTypeNameToPath(qname);
        String base = HTMLUtils.returnReletiveBasePath(context, "\\.");

        StringWriter sw = new StringWriter();
        if (showQualified)
            name = qname;
        HTMLUtils.html(sw).a().attr("href", base + path).text(name).endAll();
        // XXX Figure out how to stop the HTML formating newlines
        return sw.getBuffer().toString().replaceAll("\\n", "");
    }

    @Override
    public String returnDefinitionLink(IDocumentableDefinition definition,
            String context)
    {
        return "<a href=\"#\">TODO returnDefinitionLink()</a>";
    }

    @Override
    public String returnModifiers(IDefinition definition)
    {
        ModifiersSet modifiers = definition.getModifiers();
        IDefinitionNode node = definition.getNode();

        String result = "";
        if (node.hasNamespace("public"))
            result = "public ";
        else if (node.hasNamespace("protected"))
            result = "protected ";
        else if (node.hasNamespace("private"))
            result = "private ";
        // XXX USE HTMLUtils.toModifiers() here
        if (modifiers.hasModifiers())
            result += " " + modifiers.toString();

        return result;
    }

    @Override
    public String returnParameterDescription(IParameterDefinition definition)
    {
        if (!((IDocumentableDefinition) definition.getParent())
                .hasExplicitComment())
            return "";

        IDocComment comment = (IDocComment) ((IDocumentableDefinition) definition
                .getParent()).getExplicitSourceComment();

        String name = definition.getBaseName();
        List<IDocTag> tags = comment.getTags("param");
        for (IDocTag tag : tags)
        {
            String desc = tag.getDescription();
            if (desc != null)
            {
                desc = desc.trim();
                if (desc.indexOf(name) == 0)
                    return desc.substring(name.length());
            }
        }

        return null;
    }

    @Override
    public String returnReturnTag(IFunctionDefinition definition)
    {
        if (!definition.hasExplicitComment())
            return null;

        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        if (comment.hasTag("return"))
        {
            IDocTag tag = comment.getTag("return");
            if (tag.hasDescription())
            {
                ITypeDefinition rtype = definition
                        .resolveReturnType(getAccess().getProject());

                String description = tag.getDescription();
                String anchor = "";
                if (rtype != null)
                    anchor = returnTypeLink(rtype, getPackageContextString());
                return anchor + " - " + description.trim();
            }
        }

        // TODO Auto-generated method stub
        return null;
    }

}
