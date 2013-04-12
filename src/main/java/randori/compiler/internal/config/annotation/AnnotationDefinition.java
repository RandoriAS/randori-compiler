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

import org.apache.commons.lang3.StringUtils;
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
import randori.compiler.internal.utils.MetaDataUtils;

/**
 * @author Michael Schmalle
 */
public class AnnotationDefinition implements IAnnotationDefinition
{
    private static final String RANDORI_ANNOTATIONS = "randori.annotations";

    @SuppressWarnings("unused")
    private static final String ANNOTATION_ANNOTATION = "Annotation";

    private static final String ANNOTATION_TARGET = "Target";

    @SuppressWarnings("unused")
    private static final String ANNOTATION_RETENTION = "Retention";

    @SuppressWarnings("unused")
    private static final String ENUM_ELEMENT_TYPE = "ElementType";

    @SuppressWarnings("unused")
    private static final String ENUM_RETENTION_POLICY = "RetentionPolicy";

    private final IClassDefinition definition;

    private ArrayList<IConstantDefinition> targets = new ArrayList<IConstantDefinition>();

    public AnnotationDefinition(IClassDefinition definition)
    {
        this.definition = definition;
    }

    void resolve(IASProject project, List<ICompilerProblem> problems)
    {
        // every Annotation has a builtin retention RetentionPolicy
        resolveRetention(project, problems);

        // every Annotation has a builtin target collection [ElementType, ..]
        resolveTargets(project, problems);
    }

    private void resolveRetention(IASProject project,
            List<ICompilerProblem> problems)
    {
        // [Retention("RetentionPolicy.RUNTIME")]

    }

    private void resolveTargets(IASProject project,
            List<ICompilerProblem> problems)
    {
        // [Target("ElementType.TYPE")]
        IMetaTag[] tags = definition.getMetaTagsByName(ANNOTATION_TARGET);
        if (tags.length > 1)
        {
            // XXX (Annotation) Add MultipleAnnotationProblem
            return;
        }
        else if (tags.length == 1)
        {
            String value = MetaDataUtils.getValue(tags[0]);
            proccessValidTarget(project, value);
        }
    }

    private void proccessValidTarget(IASProject project, String value)
    {
        String[] split = value.split(",");
        for (String type : split)
        {
            String fieldName = AnnotationUtils.toEnumFieldName(type);
            String packageName = RANDORI_ANNOTATIONS;
            String baseName = AnnotationUtils.toEnumQualifiedName(type);
            // public(randori.annotations) class ElementType
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
            if (!targets.contains(target))
            {
                targets.add(target);
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
    public Collection<IConstantDefinition> getTargets()
    {
        return targets;
    }

    @Override
    public boolean isValidTarget(String type)
    {
        return isValid(type);
    }

    @Override
    public boolean isValidTarget(IDefinition definition)
    {
        if (isTargetAll())
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

    private boolean isTargetAll()
    {
        if (targets.size() == 1)
            return targets.get(0).getBaseName().equals(TARGET_ALL);
        return false;
    }

    private boolean isValid(String type)
    {
        for (IConstantDefinition definition : targets)
        {
            // XXX Use a locale here for lowercase?
            if (type.equals(StringUtils.lowerCase(definition.getBaseName())))
                return true;
        }
        return false;
    }

    String toTarget(IDefinition definition)
    {
        if (isTargetAll())
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
