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

import java.util.ArrayList;
import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;

import randori.compiler.config.annotation.IAnnotationDefinition;
import randori.compiler.config.annotation.IAnnotationManager;
import randori.compiler.internal.visitor.as.NullASVisitor;

/**
 * @author Michael Schmalle
 */
public class AnnotationVisitor extends NullASVisitor
{
    List<IAnnotationDefinition> definitions = new ArrayList<IAnnotationDefinition>();

    private final IAnnotationManager manager;

    public AnnotationVisitor(IAnnotationManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean visitClass(IClassDefinition definition)
    {
        if (manager.isAnnotation(definition))
        {
            manager.registerDefinition(definition);
        }
        return false;
    }
}
