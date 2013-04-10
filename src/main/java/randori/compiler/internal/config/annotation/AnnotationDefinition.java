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
import java.util.Collection;
import java.util.List;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;

import randori.compiler.config.IAnnotationDefinition;
import randori.compiler.internal.utils.AnnotationUtils;

/**
 * @author Michael Schmalle
 */
public class AnnotationDefinition implements IAnnotationDefinition
{
    private static final String RANDORI_ANNOTATIONS = "randori.annotations";

    private static final String ANNOTATION_USAGE = "AnnotationUsage";

    private final IClassDefinition definition;

    private ArrayList<IConstantDefinition> validOn = new ArrayList<IConstantDefinition>();

    public AnnotationDefinition(IClassDefinition definition)
    {
        this.definition = definition;
    }

    void reslove(IASProject project, List<ICompilerProblem> problems)
    {
        // find targets
        IMetaTag[] tags = definition.getMetaTagsByName(ANNOTATION_USAGE);
        if (tags.length > 1)
        {
            // XXX (Annotation) Add MultipleAnnotationProblem
            return;
        }
        else if (tags.length == 1)
        {
            String value = tags[0].getAttributeValue("validOn");
            proccessValidOn(project, value);
        }
    }

    private void proccessValidOn(IASProject project, String value)
    {
        String[] split = value.split(",");
        for (String type : split)
        {
            String fieldName = AnnotationUtils.toEnumFieldName(type);
            String packageName = RANDORI_ANNOTATIONS;
            String baseName = AnnotationUtils.toEnumQualifiedName(type);
            IClassDefinition validOnDefinition = AnnotationUtils
                    .getClassDefinitionQualified(project, packageName, baseName);

            if (validOnDefinition == null)
            {
                // XXX (Annotation) Add MissingDefinitionAnnotationProblem 
                return;
            }

            ConstantEnumDefinition enumDef = new ConstantEnumDefinition(
                    validOnDefinition);
            IConstantDefinition target = enumDef.valueOf(fieldName);
            if (!validOn.contains(target))
            {
                validOn.add(target);
            }
            else
            {
                // XXX (Annotation) Add DuplicateAnnotationProblem 
            }
        }
    }

    @Override
    public IClassDefinition getDefinition()
    {
        return definition;
    }

    @Override
    public String getBaseName()
    {
        return definition.getBaseName();
    }

    @Override
    public String getQualifiedName()
    {
        return definition.getQualifiedName();
    }

    @Override
    public Collection<IConstantDefinition> getValidOn()
    {
        return validOn;
    }

    @Override
    public boolean isValidOn(String type)
    {
        return isValid(type);
    }

    @Override
    public boolean isValidOn(IDefinition definition)
    {
        if (isValidOnAll())
            return true;

        if (definition instanceof IClassDefinition)
        {
            return isValid(TARGET_CLASS);
        }
        else if (definition instanceof IInterfaceDefinition)
        {
            return isValid(TARGET_INTERFACE);
        }
        else if (definition instanceof IAccessorDefinition)
        {
            return isValid(TARGET_PROPERTY);
        }
        else if (definition instanceof IFunctionDefinition)
        {
            IFunctionDefinition function = (IFunctionDefinition) definition;
            if (function.isConstructor())
            {
                return isValid(TARGET_CONSTRUCTOR);
            }
            else
            {
                return isValid(TARGET_METHOD);
            }
        }
        else if (definition instanceof IVariableDefinition)
        {
            return isValid(TARGET_FIELD);
        }

        return false;
    }

    private boolean isValidOnAll()
    {
        if (validOn.size() == 1)
            return validOn.get(0).getBaseName().equals(TARGET_ALL);
        return false;
    }

    private boolean isValid(String type)
    {
        for (IConstantDefinition definition : validOn)
        {
            if (definition.getBaseName().equals(type))
                return true;
        }
        return false;
    }

    String toValidOn(IDefinition definition)
    {
        if (isValidOnAll())
            return TARGET_ALL;

        if (definition instanceof IClassDefinition)
        {
            return TARGET_CLASS;
        }
        else if (definition instanceof IInterfaceDefinition)
        {
            return TARGET_INTERFACE;
        }
        else if (definition instanceof IAccessorDefinition)
        {
            return TARGET_PROPERTY;
        }
        else if (definition instanceof IFunctionDefinition)
        {
            IFunctionDefinition function = (IFunctionDefinition) definition;
            if (function.isConstructor())
            {
                return TARGET_CONSTRUCTOR;
            }
            else
            {
                return TARGET_METHOD;
            }
        }
        else if (definition instanceof IVariableDefinition)
        {
            return TARGET_FIELD;
        }

        return null;
    }

    class ConstantEnumDefinition
    {
        private final IClassDefinition definition;

        ConstantEnumDefinition(IClassDefinition definition)
        {
            this.definition = definition;
        }

        IConstantDefinition valueOf(String value)
        {
            Collection<IDefinition> definitions = definition
                    .getContainedScope().getAllLocalDefinitions();
            for (IDefinition definition : definitions)
            {
                if (definition instanceof IConstantDefinition)
                {
                    if (definition.getBaseName().equals(value))
                        return (IConstantDefinition) definition;
                }
            }
            return null;
        }
    }
}
