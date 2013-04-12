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

package randori.compiler.asdoc.config;

import java.io.File;
import java.util.List;

import randori.compiler.asdoc.access.IASProjectAccess;
import randori.compiler.asdoc.internal.access.MemberType;
import randori.compiler.asdoc.internal.config.RandoriDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.data.PrimaryLink;
import randori.compiler.asdoc.template.asdoc.IASDocConverter;
import randori.compiler.asdoc.template.asdoc.IASDocRowConverter;

public interface IDocConfiguration
{
    List<File> getOutputs();

    List<File> getTemplateBases();

    int getLeftFrameWidth();

    Boolean getFrames();

    List<PrimaryLink> getPrimaryLinks();

    String getMainTitle();

    String getFooter();

    String getLogo();

    IASProjectAccess getAccess();

    void setAccess(IASProjectAccess value);

    IASDocConverter getConverter();

    void setConverter(IASDocConverter value);

    IASDocRowConverter getRowConverter();

    void setRowConverter(IASDocRowConverter value);

    boolean isTagActive(String tagName);

    List<String> getDocNamespace();

    List<MemberType> getDocMemberTypes();

    boolean getRetainMemberOrder();

    List<File> getSourcePaths();

    void setConfiguration(RandoriDocConfiguration configuration);
}
