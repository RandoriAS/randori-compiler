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

package randori.compiler.config;

import java.util.Collection;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;

/**
 * @author Michael Schmalle
 */
public interface IAnnotationDefinition
{
    public enum AnnotationTargets
    {
        Class("class"),

        Constructor("constructor"),

        Property("property"),

        Field("field"),

        All("all");

        private final String value;

        AnnotationTargets(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    IClassDefinition getDefinition();

    String getBaseName();

    String getQualifiedName();

    Collection<AnnotationTargets> getValidOn();

    boolean isValidOn(IDefinition definition);
}
