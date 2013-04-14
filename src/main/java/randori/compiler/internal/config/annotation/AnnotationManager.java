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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.internal.definitions.ClassDefinition;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.scopes.IDefinitionSet;

import randori.compiler.config.annotation.IAnnotationDefinition;
import randori.compiler.config.annotation.IAnnotationManager;

/**
 * @author Michael Schmalle
 */
public class AnnotationManager implements IAnnotationManager
{
    private static final String BULTIN_ANNOTATION = "randori.annotations.Annotation";

    private static final String ANNOTATION = "Annotation";

    private ClassDefinition annotationDefinition;

    private final IASProject project;

    Map<String, IAnnotationDefinition> map = new HashMap<String, IAnnotationDefinition>();

    private List<ICompilerProblem> problems = new ArrayList<ICompilerProblem>();

    private boolean enabled = false;

    ClassDefinition loadAnnotationDefinition()
    {
        // XXX implement multiple Annotation possibility search
        ClassDefinition definition = (ClassDefinition) project.getScope()
                .getLocalDefinitionSetByName(ANNOTATION);
        if (definition == null)
            throw new RuntimeException("Annotation definition not found");
        if (!definition.getQualifiedName().equals(BULTIN_ANNOTATION))
            throw new RuntimeException(BULTIN_ANNOTATION
                    + " definition not found");
        return definition;
    }

    ClassDefinition getAnnotationDefintion()
    {
        if (annotationDefinition == null)
            annotationDefinition = loadAnnotationDefinition();
        return annotationDefinition;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public void setEnabled(boolean value)
    {
        enabled = value;
    }

    @Override
    public List<ICompilerProblem> getProblems()
    {
        return problems;
    }

    public AnnotationManager(IASProject project)
    {
        this.project = project;
    }

    @Override
    public IAnnotationDefinition registerDefinition(IClassDefinition definition)
    {
        if (map.containsKey(definition.getQualifiedName()))
            return null;

        AnnotationDefinition annotation = new AnnotationDefinition(definition);
        List<ICompilerProblem> annotationProblems = new ArrayList<ICompilerProblem>();
        annotation.resolve(project, annotationProblems);

        if (annotationProblems.size() > 0)
        {
            problems.addAll(annotationProblems);
            return null;
        }

        map.put(definition.getQualifiedName(), annotation);

        return annotation;
    }

    @Override
    public IAnnotationDefinition getDefinition(String qualifiedName)
    {
        return map.get(qualifiedName);
    }

    @Override
    public boolean isAnnotation(IClassDefinition definition)
    {
        if (definition.isInstanceOf(getAnnotationDefintion(), project))
            return true;
        return false;
    }

    @Override
    public void addProblem(ICompilerProblem problem)
    {
        problems.add(problem);
    }

    @Override
    public IAnnotationDefinition getAnnotation(IMetaTag tag)
    {
        @SuppressWarnings("unused")
        IDefinition definition = tag.getDecoratedDefinition();

        IDefinitionSet set = project.getScope().getLocalDefinitionSetByName(
                tag.getTagName());
        if (set.getSize() > 1)
        {
            // XXX (Annotation) MultipleAnnotationDefinition without import
        }
        else
        {
            IClassDefinition classDef = (IClassDefinition) set;
            return getDefinition(classDef.getQualifiedName());
        }

        return null;
    }

}
