/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal.codegen.js.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.flex.compiler.constants.IASKeywordConstants;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.apache.flex.compiler.internal.definitions.metadata.MetaTagAttribute;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.*;
import randori.compiler.codegen.as.IASEmitter;
import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.ExpressionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Schmalle
 */
public class GenericEmitUtils
{
    public static final List<IParameterDefinition> getParameters(
            IFunctionCallNode node, IFunctionDefinition definition,
            ICompilerProject project)
    {
        IExpressionNode[] arguments = node.getArgumentNodes();
        IParameterDefinition[] parameters = definition.getParameters();

        // we are using these for argument render so
        // we only include the parameters that have null or undefined
        // IF they do not have a corresponding argument OR they are at
        // the end of the signature with no other defaults other than
        // null or undefined following them

        // 1. get the parameters in reverse order
        List<ParameterInfo> infos = new ArrayList<ParameterInfo>();
        int i = 0;
        for (IParameterDefinition parameter : parameters)
        {
            IExpressionNode argument = null;
            if (i < arguments.length)
                argument = arguments[i];
            infos.add(new ParameterInfo(parameter, argument));
            i++;
        }

        Collections.reverse(infos);

        boolean skipParameter = true;
        List<IParameterDefinition> definitions = new ArrayList<IParameterDefinition>();
        for (ParameterInfo info : infos)
        {
            if (info.hasArgument())
            {
                definitions.add(info.parameter);
            }
            else
            {
                // if there is no argument, that means we are trying a default value
                if (info.parameter.hasDefaultValue())
                {
                    String value = ExpressionUtils.toInitialValue(
                            info.parameter, project);
                    if (skipParameter)
                    {
                        if (value.equals("undefined") || value.equals("null"))
                        {
                            skipParameter = true; //
                        }
                        else
                        {
                            skipParameter = false;
                        }
                    }
                }
                if (!skipParameter)
                    definitions.add(info.parameter);
            }
        }

        Collections.reverse(definitions);

        return definitions;
    }

    static class ParameterInfo
    {
        private IParameterDefinition parameter;

        private IExpressionNode argument;

        ParameterInfo(IParameterDefinition parameter, IExpressionNode argument)
        {
            this.parameter = parameter;
            this.argument = argument;
        }

        boolean hasArgument()
        {
            return argument != null;
        }
    }

    public static final void emitDefaultParameterCodeBlock(IFunctionNode node,
            IASEmitter emitter)
    {
        // TODO (mschmalle) test for ... rest 
        // if default parameters exist, produce the init code
        IParameterNode[] pnodes = node.getParameterNodes();
        Map<Integer, IParameterNode> defaults = DefinitionUtils
                .getDefaults(pnodes);

        if (pnodes.length == 0)
            return;

        if (defaults != null)
        {
            boolean hasBody = node.getScopedNode().getChildCount() > 0;

            if (!hasBody)
            {
                emitter.indentPush();
                emitter.write("\t");
            }

            final StringBuilder code = new StringBuilder();

            List<IParameterNode> parameters = new ArrayList<IParameterNode>(
                    defaults.values());
            Collections.reverse(parameters);

            int len = defaults.size();
            // make the header in reverse order
            for (IParameterNode pnode : parameters)
            {
                if (pnode != null)
                {
                    code.setLength(0);
                    code.append(IASKeywordConstants.IF);
                    code.append(" (arguments.length < ");
                    code.append(len);
                    code.append(") {");

                    emitter.write(code.toString());

                    emitter.indentPush();
                    emitter.writeNewline();
                }
                len--;
            }

            Collections.reverse(parameters);
            for (int i = 0, n = parameters.size(); i < n; i++)
            {
                IParameterNode pnode = parameters.get(i);

                if (pnode != null)
                {
                    code.setLength(0);

                    code.append(pnode.getName());
                    code.append(" = ");
                    code.append(pnode.getDefaultValue());
                    code.append(";");
                    emitter.write(code.toString());

                    emitter.indentPop();
                    emitter.writeNewline();

                    emitter.write("}");

                    if (i == n - 1 && !hasBody)
                        emitter.indentPop();

                    emitter.writeNewline();
                }
            }
        }
    }


    public static final void emitEmbedFactory(IMetaTag factoryTag, IMetaTag embedTag, IVariableDefinition field,
            IASEmitter emitter)
    {
        String factory = factoryTag.getAttributeValue("factoryClass");
        String type = factoryTag.getAttributeValue("type");


        emitter.write(factory);
        emitter.write("(");

        emitter.write("\"");
        emitter.write(type);
        emitter.write("\"");
        emitter.write(", ");

        IMetaTagAttribute[] atts1 = factoryTag.getAllAttributes();

        // write properties
        List<IMetaTagAttribute> list = new ArrayList<IMetaTagAttribute>();
        for (IMetaTagAttribute att : atts1)
        {
            if (!att.getKey().equals("factoryClass")
                    && !att.getKey().equals("type"))
            {
                list.add(att);
            }
        }
        if (embedTag != null)
        {
            IMetaTagAttribute[] atts2 = embedTag.getAllAttributes();
            for (IMetaTagAttribute att : atts2)
            {
                //if (!att.getKey().equals("source"))
                //{
                    list.add(att);
               // }
            }
        }
        else
        {
            // Use the value as the source
            String value = DefinitionUtils.returnInitialVariableValue(
                    (IVariableNode) field.getNode(), (IRandoriEmitter) emitter);
            list.add(new MetaTagAttribute("source", StringEscapeUtils.escapeJava(value)));
        }

        emitter.write("{");
        int i = 0;
        final int len = list.size();
        for (IMetaTagAttribute attribute : list)
        {
            emitter.write(attribute.getKey());
            emitter.write(":");
            emitter.write("\"");
            emitter.write(attribute.getValue());
            emitter.write("\"");
            if (i < len - 1)
                emitter.write(", ");
            i++;
        }
        emitter.write("}");

        emitter.write(")");
    }
}
