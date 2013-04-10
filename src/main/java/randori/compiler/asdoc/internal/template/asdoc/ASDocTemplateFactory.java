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

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IEffectDefinition;
import org.apache.flex.compiler.definitions.IEventDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IStyleDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.definitions.ISkinPartDefinition;
import randori.compiler.asdoc.definitions.ISkinStateDefinition;
import randori.compiler.asdoc.internal.template.asdoc.details.AccessorDetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.details.ConstantDetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.details.EventDetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.details.MethodDetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.details.VariableDetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.rows.DetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.rows.SummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.AccessorSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.ConstantSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.EffectSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.EventSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.MethodSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.SkinPartSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.SkinStateSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.StyleSummaryRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.summaries.VariableSummaryRowTemplate;
import randori.compiler.asdoc.template.asdoc.IDetailRowTemplate;

public class ASDocTemplateFactory
{

    public static final SummaryRowTemplate createSummaryRow(
            IDefinition definition, IDocConfiguration config)
    {
        SummaryRowTemplate tpl = null;

        if (definition instanceof IConstantDefinition)
        {
            tpl = new ConstantSummaryRowTemplate(config);
        }
        else if (definition instanceof IAccessorDefinition)
        {
            tpl = new AccessorSummaryRowTemplate(config);
        }
        else if (definition instanceof IFunctionDefinition)
        {
            tpl = new MethodSummaryRowTemplate(config);
        }
        else if (definition instanceof IVariableDefinition)
        {
            tpl = new VariableSummaryRowTemplate(config);
        }
        else if (definition instanceof IEventDefinition)
        {
            tpl = new EventSummaryRowTemplate(config);
        }
        else if (definition instanceof IEffectDefinition)
        {
            tpl = new EffectSummaryRowTemplate(config);
        }
        else if (definition instanceof IStyleDefinition)
        {
            tpl = new StyleSummaryRowTemplate(config);
        }
        else if (definition instanceof ISkinPartDefinition)
        {
            tpl = new SkinPartSummaryRowTemplate(config);
        }
        else if (definition instanceof ISkinStateDefinition)
        {
            tpl = new SkinStateSummaryRowTemplate(config);
        }
        return tpl;
    }

    public static IDetailRowTemplate createDetailRow(IDefinition definition,
            IDocConfiguration config)
    {
        DetailRowTemplate tpl = null;

        if (definition instanceof IConstantDefinition)
        {
            tpl = new ConstantDetailRowTemplate(config);
        }
        else if (definition instanceof IAccessorDefinition)
        {
            tpl = new AccessorDetailRowTemplate(config);
        }
        else if (definition instanceof IFunctionDefinition)
        {
            tpl = new MethodDetailRowTemplate(config);
        }
        else if (definition instanceof IVariableDefinition)
        {
            tpl = new VariableDetailRowTemplate(config);
        }
        else if (definition instanceof IEventDefinition)
        {
            tpl = new EventDetailRowTemplate(config);
        }

        return tpl;
    }
}
