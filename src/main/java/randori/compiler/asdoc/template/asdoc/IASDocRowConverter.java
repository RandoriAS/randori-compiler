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

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IMetadataDefinition;

public interface IASDocRowConverter
{
    void addName(IRowTemplate template, IDefinition definition);

    void addModifiers(IRowTemplate template, IDefinition definition);

    void addDefinedBy(IRowTemplate template, IDefinition e);

    void addDefinedBy(IRowTemplate template, IMetadataDefinition definition);

    void addValueType(IRowTemplate template, IDefinition definition);

    //--------------------------------------------------------------------------
    //
    // Description
    //
    //--------------------------------------------------------------------------

    void addShortDescription(IRowTemplate template,
            IDocumentableDefinition definition);

    void addLongDescription(IRowTemplate template,
            IDocumentableDefinition definition);

    void addInternalDescription(IRowTemplate template,
            IDocumentableDefinition definition);

    void addParameters(IRowTemplate template, IFunctionDefinition definition);

    void addReturnType(IRowTemplate template, IFunctionDefinition definition);

    void addAccess(IRowTemplate template, IAccessorDefinition definition);

    void addImplementedFrom(IRowTemplate template,
            IFunctionDefinition definition);

    void addParamTags(IRowTemplate template, IFunctionDefinition definition);

    void addReturnTag(IRowTemplate template, IFunctionDefinition definition);

    void addUses(IRowTemplate template, IFunctionDefinition method);

    void addValue(IRowTemplate template, IConstantDefinition definition);

    void addSeeTags(IRowTemplate template, IDocumentableDefinition definition);
}
