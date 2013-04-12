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

import org.apache.flex.compiler.common.ISourceLocation;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.problems.CompilerProblem;

import randori.compiler.config.IAnnotationDefinition;
import randori.compiler.config.IAnnotationManager;
import randori.compiler.internal.visitor.as.NullASVisitor;

/**
 * @author Michael Schmalle
 */
public class AnnotationValidator extends NullASVisitor
{

    private final IAnnotationManager manager;

    public AnnotationValidator(IAnnotationManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean visitClass(IClassDefinition definition)
    {
        if (manager.isAnnotation(definition))
            return true;

        // find out if the class is annotated which in this context means
        // any metadata at all
        IMetaTag[] tags = definition.getAllMetaTags();
        for (IMetaTag tag : tags)
        {
            validateClassAnnotation(definition, tag);
        }

        return true;
    }

    private void validateClassAnnotation(IClassDefinition definition,
            IMetaTag tag)
    {
        IAnnotationDefinition annotation = manager.getAnnotation(tag);
        if (annotation == null)
        {
            // XXX (Annotation) AnnotationNotDefinedProblem
            manager.addProblem(new AnnotationProblem(definition));
            return;
        }

        if (!annotation.isValidTarget(definition))
        {
            // XXX (Annotation) InvlaidTargetForAnnotationProblem
            manager.addProblem(new AnnotationProblem(definition));
        }
    }

    @Override
    public boolean visitMethod(IFunctionDefinition definition)
    {
        IMetaTag[] tags = definition.getAllMetaTags();
        for (IMetaTag tag : tags)
        {
            validateMemberAnnotation(definition, tag);
        }

        return false;
    }

    private void validateMemberAnnotation(IDefinition definition, IMetaTag tag)
    {
        IAnnotationDefinition annotation = manager.getAnnotation(tag);
        if (annotation == null)
        {
            // XXX (Annotation) AnnotationNotDefinedProblem
            manager.addProblem(new AnnotationProblem(definition));
            return;
        }

        if (!annotation.isValidTarget(definition))
        {
            // XXX (Annotation) InvlaidTargetForAnnotationProblem
            manager.addProblem(new InvlaidTargetForAnnotationProblem(tag,
                    "method", annotation.getQualifiedName()));
        }
    }

    public class AnnotationProblem extends CompilerProblem
    {
        public AnnotationProblem(IDefinition site)
        {
            super(site);
        }

        AnnotationProblem(ISourceLocation site)
        {
            super(site);
        }

        public AnnotationProblem(String sourcePath)
        {
            super(sourcePath);
        }
    }

    public class InvlaidTargetForAnnotationProblem extends AnnotationProblem
    {
        public static final String DESCRIPTION = "Invalid target: ${target} for annotation: ${tag}";

        public final String tag;

        public final String target;

        public InvlaidTargetForAnnotationProblem(ISourceLocation location,
                String target, String tag)
        {
            super(location);
            this.target = target;
            this.tag = tag;
        }

    }

}
