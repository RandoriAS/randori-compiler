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

package randori.compiler.asdoc.template.asdoc;

import org.apache.flex.compiler.definitions.IDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.rows.DetailRowTemplate;
import randori.compiler.asdoc.internal.template.asdoc.rows.SummaryRowTemplate;

public interface IASDocTemplateFactory
{
    SummaryRowTemplate createSummaryRow(IDefinition element,
            IDocConfiguration config);

    DetailRowTemplate createDetailRow(IDefinition definition,
            IDocConfiguration config);
}
