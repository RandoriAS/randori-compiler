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
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.render.IConvertor;

public interface IASDocConverter extends IConvertor, IASDocTagConvertor
{

    String returnPackageListPaneLink(IPackageBundle bundle);

    String returnTypeListPaneLink(ITypeDefinition definition);

    String returnTypeContentLink(ITypeDefinition element, IPackageBundle bundle);

    String returnTypeLink(ITypeDefinition type, String context);

    String returnTypeLink(ITypeDefinition definition, String context,
            boolean showQualified);

    String returnModifiers(IDefinition definition);

    String returnParameterDescription(IParameterDefinition definition);

    String returnShortDescription(IDocumentableDefinition aware);

    String returnLongDescription(IDocumentableDefinition aware);

    String returnPackageDescription(IPackageBundle bundle);

    String returnDefinitionLink(IDocumentableDefinition definition,
            String context);
}
