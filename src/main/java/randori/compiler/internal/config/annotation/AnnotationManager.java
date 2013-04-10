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
import org.apache.flex.compiler.internal.definitions.ClassDefinition;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;

import randori.compiler.config.IAnnotationDefinition;
import randori.compiler.config.IAnnotationManager;

/**
 * @author Michael Schmalle
 */
public class AnnotationManager implements IAnnotationManager
{
    private static ClassDefinition annotationDefinition;

    private final IASProject project;

    Map<String, IAnnotationDefinition> map = new HashMap<String, IAnnotationDefinition>();

    private List<ICompilerProblem> problems = new ArrayList<ICompilerProblem>();

    @Override
    public List<ICompilerProblem> getProblems()
    {
        return problems;
    }

    public AnnotationManager(IASProject project)
    {
        this.project = project;

        annotationDefinition = (ClassDefinition) project.getScope()
                .getLocalDefinitionSetByName("Annotation");
    }

    @Override
    public IAnnotationDefinition registerDefinition(IClassDefinition definition)
    {
        if (map.containsKey(definition.getQualifiedName()))
            return null;
        
        AnnotationDefinition annotation = new AnnotationDefinition(definition);
        List<ICompilerProblem> annotationProblems = new ArrayList<ICompilerProblem>();
        annotation.reslove(project, annotationProblems);
        
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
        if (definition.isInstanceOf(annotationDefinition, project))
            return true;
        return false;
    }

    @Override
    public void addProblem(ICompilerProblem problem)
    {
        problems.add(problem);
    }

}
