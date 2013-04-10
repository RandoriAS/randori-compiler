package randori.compiler.internal.config.annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.scopes.IDefinitionSet;

import randori.compiler.config.IAnnotationDefinition;

public class AnnotationDefinition implements IAnnotationDefinition
{

    private static final String ANNOTATION_USAGE = "AnnotationUsage";

    private static final String ANNOTATION_TARGETS = "AnnotationTargets";

    private final IClassDefinition definition;

    private Collection<AnnotationTargets> validOn = new ArrayList<AnnotationTargets>();

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
            // XXX Add MultipleAnnotationProblem
            return;
        }
        else if (tags.length == 1)
        {
            String type = tags[0].getAttributeValue("validOn");
            String qualifiedName = toEnumQualifiedName(type);
            IDefinitionSet set = project.getScope()
                    .getLocalDefinitionSetByName(qualifiedName);
        }
        else
        {
            validOn.add(AnnotationTargets.All);
        }

    }

    private static String toEnumQualifiedName(String type)
    {
        if (type.indexOf(".") == -1)
            return type;
        return StringUtils.substringAfterLast(type, ".");
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
    public Collection<AnnotationTargets> getValidOn()
    {
        return validOn;
    }

    @Override
    public boolean isValidOn(IDefinition definition)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
