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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IGetterDefinition;
import org.apache.flex.compiler.definitions.IMetadataDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ISetterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;

import randori.compiler.asdoc.comment.IDocComment;
import randori.compiler.asdoc.comment.IDocTag;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.template.asdoc.IASDocConverter;
import randori.compiler.asdoc.template.asdoc.IASDocRowConverter;
import randori.compiler.asdoc.template.asdoc.IRowTemplate;

public class ASDocRowConverter implements IASDocRowConverter
{

    private IDocConfiguration configuration;

    public IASDocConverter getConverter()
    {
        return configuration.getConverter();
    }

    public IDocConfiguration getConfiguration()
    {
        return configuration;
    }

    public ASDocRowConverter(IDocConfiguration config)
    {
        super();
        this.configuration = config;
    }

    @Override
    public void addSeeTags(IRowTemplate template,
            IDocumentableDefinition definition)
    {
        template.putRow("seeTags", addSeeTags(template, definition, "see"));
    }

    public List<String> addSeeTags(IRowTemplate template,
            IDocumentableDefinition definition, String tagName)
    {
        IDocComment comment = (IDocComment) definition
                .getExplicitSourceComment();
        if (!comment.hasTag(tagName))
            return null;

        List<String> result = new ArrayList<String>();

        List<IDocTag> tags = comment.getTags(tagName);
        for (IDocTag tag : tags)
        {

            @SuppressWarnings("unused")
            String text = null;
            String anchor = null;
            String body = tag.getDescription().trim();
            String linx = StringUtils.substringBefore(body, " ");
            if (linx != null && !linx.equals(""))
                anchor = linx;

            int index = body.indexOf(" ");
            if (index != -1)
                text = StringUtils.substring(body, index);

            if (anchor == null)
                continue;

            if (anchor.indexOf("://") != -1 || anchor.indexOf("mailto:") != -1)
            {
                //anchor = returnLink(anchor, text);
            }
            else if (body.indexOf('"') == 0)
            {
                anchor = body.trim();
            }
            else if (body.indexOf(".html") == body.length() - 5)
            {
                //anchor = returnLink(anchor, text);
            }
            else if (body.indexOf("#") == 0)
            {
                IDocumentableDefinition member = getConfiguration().getAccess()
                        .getDefinition(body);
                if (member == null)
                {
                    anchor = body.trim();
                }
                else
                {
                    anchor = getConverter().returnDefinitionLink(member,
                            getConverter().getPackageContextString());
                }
            }
            else
            {
                ITypeDefinition type = getConfiguration().getAccess().getType(
                        body.trim());
                if (type != null)
                {
                    anchor = getConverter().returnTypeLink(type,
                            getConverter().getPackageContextString());
                }
                else
                {
                    anchor = body.trim();
                }
            }

            result.add(anchor);
        }

        return result;
    }

    @Override
    public void addName(IRowTemplate template, IDefinition definition)
    {
        String title = definition.getBaseName();
        // if (((IDeprecatable) e).isDeprecated())
        // {
        // nameTitle = "<span style=\"text-decoration:line-through\">"
        // + nameTitle + "</span>";
        // }

        template.putRow("nameTitle", title);
        template.putRow("name", definition.getBaseName());
    }

    @Override
    public void addDefinedBy(IRowTemplate template, IDefinition definition)
    {
        ITypeDefinition parent = getParentDefinition(definition);

        String name = parent.getBaseName();
        if (!(Boolean) template.getRow("isInherited"))
        {
            template.putRow("definedBy", name);
            return;
        }

        String anchor = definition.getBaseName();
        anchor = getConverter().returnTypeLink(parent,
                getConverter().getPackageContextString());

        template.putRow("definedBy", anchor);

        addDefinedByID(template, definition);
    }

    @Override
    public void addDefinedBy(IRowTemplate template,
            IMetadataDefinition definition)
    {
        String name = definition.getDecoratedDefinition().getBaseName();
        if (!(Boolean) template.getRow("isInherited"))
        {
            template.putRow("definedBy", name);
            return;
        }

        String anchor = definition.getBaseName();
        ITypeDefinition parent = getParentDefinition(definition);
        anchor = getConverter().returnTypeLink(parent,
                getConverter().getPackageContextString());

        template.putRow("definedBy", anchor);

        addDefinedByID(template, definition);
    }

    private ITypeDefinition getParentDefinition(IDefinition definition)
    {
        if (definition instanceof IMetadataDefinition)
            return (ITypeDefinition) ((IMetadataDefinition) definition)
                    .getDecoratedDefinition();
        return (ITypeDefinition) definition.getParent();
    }

    public void addDefinedByID(IRowTemplate template, IDefinition definition)
    {
        // String anchor = getParentTypeASQname(e).getQualifiedName();
        //ILinkElement link = converter.getLink(anchor);
        // String id = converter.returnLinkID(link,
        //       converter.getRenderContextString());

        //o.put("definedByID", id);
    }

    @Override
    public void addValueType(IRowTemplate template, IDefinition definition)
    {
        IVariableDefinition vdef = (IVariableDefinition) definition;
        String name = vdef.getTypeAsDisplayString();
        ITypeDefinition type = vdef.resolveType(getConfiguration().getAccess()
                .getProject());
        String link = name;
        if (type != null)
        {
            link = getConverter().returnTypeLink(type,
                    getConverter().getPackageContextString());
        }

        template.putRow("valueType", link);
    }

    @Override
    public void addShortDescription(IRowTemplate template,
            IDocumentableDefinition definition)
    {
        template.putRow("shortDescription", getConverter()
                .returnShortDescription(definition));
    }

    @Override
    public void addLongDescription(IRowTemplate template,
            IDocumentableDefinition definition)
    {
        template.putRow("longDescription", getConverter()
                .returnLongDescription(definition));
    }

    @Override
    public void addInternalDescription(IRowTemplate template,
            IDocumentableDefinition definition)
    {
        //        o.put("internalDescription", getConvertor()
        //                .returnInternalDescription(e));
    }

    @Override
    public void addModifiers(IRowTemplate template, IDefinition definition)
    {
        template.putRow("modifiers", getConverter().returnModifiers(definition));
    }

    @Override
    public void addParameters(IRowTemplate template,
            IFunctionDefinition definition)
    {
        StringBuffer result = new StringBuffer();
        IParameterDefinition[] parameters = definition.getParameters();
        int i = 0;
        int len = parameters.length;
        for (IParameterDefinition param : parameters)
        {
            result.append(param.getBaseName());
            ITypeDefinition type = param.resolveType(getConfiguration()
                    .getAccess().getProject());
            if (type == null)
            {
                result.append(":");
                result.append(param.getTypeAsDisplayString());
            }
            else
            {
                String stype = getConverter().returnTypeLink(type,
                        getConverter().getPackageContextString());
                result.append(":");
                result.append(stype);
            }
            if (i < len - 1)
                result.append(", ");
            i++;
        }

        template.putRow("parameters", result);
    }

    @Override
    public void addReturnType(IRowTemplate template,
            IFunctionDefinition definition)
    {
        StringBuffer result = new StringBuffer();
        String anchor = definition.getReturnTypeAsDisplayString();
        ITypeDefinition type = definition.resolveReturnType(getConfiguration()
                .getAccess().getProject());
        if (type != null)
        {
            anchor = getConverter().returnTypeLink(type,
                    getConverter().getPackageContextString());
        }
        result.append(anchor);

        template.putRow("returnType", result);
    }

    @Override
    public void addAccess(IRowTemplate template, IAccessorDefinition definition)
    {
        String access = "";

        IAccessorDefinition sibling = definition
                .resolveCorrespondingAccessor(getConfiguration().getAccess()
                        .getProject());

        if (sibling != null)
        {
            access = "[read-write]";
        }
        else if (definition instanceof IGetterDefinition)
        {
            access = "[read-only]";
        }
        else if (definition instanceof ISetterDefinition)
        {
            access = "[write-only]";
        }

        template.putRow("access", access);
    }

    @Override
    public void addImplementedFrom(IRowTemplate template,
            IFunctionDefinition definition)
    {
        IFunctionDefinition impl = definition
                .resolveImplementedFunction(getConfiguration().getAccess()
                        .getProject());

        if (impl != null)
        {
            ITypeDefinition type = (ITypeDefinition) impl.getParent();
            // XXX need the link for the member
            String anchor = type.getBaseName();
            anchor = getConverter().returnTypeLink(type,
                    getConverter().getPackageContextString());

            template.putRow("implementedFrom", "Implemented from - " + anchor
                    + "");
        }
    }

    @Override
    public void addParamTags(IRowTemplate template,
            IFunctionDefinition definition)
    {
        List<String> tags = convertParamters(definition, getConverter());
        template.putRow("paramTags", tags);
    }

    private static List<String> convertParamters(IFunctionDefinition element,
            IASDocConverter converter)
    {
        IParameterDefinition[] parameters = element.getParameters();
        if (parameters == null || parameters.length == 0)
            return null;

        List<String> result = new ArrayList<String>();

        for (IParameterDefinition param : parameters)
        {
            result.add(convertParamter(param, converter));
        }

        return result;
    }

    private static String convertParamter(IParameterDefinition element,
            IASDocConverter converter)
    {
        String name = element.getBaseName();
        if (element.isRest())
        {
            return "..." + "<span class=\"label\"><code>" + name
                    + "</code></span>" + " - "
                    + converter.returnParameterDescription(element);
        }

        // need to get the method
        //ITypeDefinition parent = (ITypeDefinition) element.getParent()
        //        .getParent();
        ITypeDefinition type = element.resolveType(converter.getAccess()
                .getProject());
        String comment = converter.returnParameterDescription(element);

        StringBuffer sb = new StringBuffer();
        sb.append("<span class=\"label\"><code>" + name + "</code></span>");

        if (type != null)
        {
            sb.append(" : ");
            sb.append(converter.returnTypeLink(type,
                    converter.getPackageContextString()));
        }
        else if (element.getTypeReference() != null)
        {
            sb.append(" : ");
            sb.append(element.getTypeAsDisplayString());
        }

        if (comment != null && !comment.equals(""))
        {
            sb.append(" - ");
            sb.append(comment);
        }

        return sb.toString();
    }

    @Override
    public void addReturnTag(IRowTemplate template,
            IFunctionDefinition definition)
    {
        template.putRow("returnTag", getConverter().returnReturnTag(definition));
    }

    @Override
    public void addUses(IRowTemplate template, IFunctionDefinition method)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void addValue(IRowTemplate template, IConstantDefinition definition)
    {
        Object resolveValue = definition.resolveValue(getConfiguration()
                .getAccess().getProject());
        if (resolveValue != null)
        {
            template.putRow("value", resolveValue.toString());
        }
    }
}
