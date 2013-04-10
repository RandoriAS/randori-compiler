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

package randori.compiler.internal.config.annotation;

import org.apache.flex.compiler.definitions.IClassDefinition;

import randori.compiler.config.IAnnotationManager;
import randori.compiler.internal.visitor.as.NullASVisitor;

public class AnnotationValidator extends NullASVisitor
{

    @SuppressWarnings("unused")
    private final IAnnotationManager manager;

    public AnnotationValidator(IAnnotationManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean visitClass(IClassDefinition definition)
    {
        // validate class level annotations
        //        IMetaTag[] tags = definition.getAllMetaTags();
        //        for (IMetaTag tag : tags)
        //        {
        //            //String name = tag.getTagName();
        //
        //        }
        return true;
    }

}
