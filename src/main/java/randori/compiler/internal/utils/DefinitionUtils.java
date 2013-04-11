/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flex.compiler.common.ASModifier;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.references.IReference;
import org.apache.flex.compiler.definitions.references.IResolvedQualifiersReference;
import org.apache.flex.compiler.internal.definitions.ClassDefinition;
import org.apache.flex.compiler.internal.tree.as.FunctionCallNode;
import org.apache.flex.compiler.internal.units.SWCCompilationUnit;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IContainerNode;
import org.apache.flex.compiler.tree.as.IContainerNode.ContainerType;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode.LanguageIdentifierKind;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.tree.as.IParameterNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.codegen.js.IRandoriEmitter;

/**
 * Various utilities used with {@link IDefinition} logic.
 * 
 * @author Michael Schmalle
 */
public class DefinitionUtils
{
    public static final boolean hasSuperCall(IFunctionNode node,
            ICompilerProject project)
    {
        IClassDefinition definition = DefinitionUtils.getBaseClassDefinition(
                node.getDefinition(), project);

        if (MetaDataUtils.isNative(definition))
            return true; // fake saying we have super for native classes

        final int len = node.getScopedNode().getChildCount();
        for (int i = 0; i < len; i++)
        {
            IASNode child = node.getScopedNode().getChild(i);
            if (child instanceof FunctionCallNode)
                return ((FunctionCallNode) child).isSuperExpression();
        }
        return false;
    }

    public static final String toBaseClassQualifiedName(
            ITypeDefinition definition, ICompilerProject project)
    {
        IClassNode typeNode = (IClassNode) definition.getNode();
        IClassDefinition bclass = typeNode.getDefinition().resolveBaseClass(
                project);
        if (bclass != null)
        {
            return bclass.getQualifiedName();
        }
        IReference reference = typeNode.getDefinition().getBaseClassReference();

        List<String> imports = resolveImports(typeNode.getDefinition());
        String qualifiedName = toQualifiedName(reference);
        if (qualifiedName.indexOf(".") != -1)
            return qualifiedName;

        // scan the imports for a match endName
        for (String imp : imports)
        {
            if (imp.endsWith(qualifiedName))
                return imp;
        }

        if (typeNode.getPackageName().indexOf(".") != -1)
            return typeNode.getPackageName() + "." + qualifiedName;

        return qualifiedName;
    }

    public static final List<String> resolveImports(ITypeDefinition type)
    {
        ClassDefinition cdefinition = (ClassDefinition) type;
        ArrayList<String> list = new ArrayList<String>();
        IScopedNode scopeNode = type.getContainedScope().getScopeNode();
        if (scopeNode != null)
        {
            scopeNode.getAllImports(list);
        }
        else
        {
            // MXML
            String[] implicitImports = cdefinition.getImplicitImports();
            for (String imp : implicitImports)
            {
                list.add(imp);
            }
        }
        return list;
    }

    public static final String toQualifiedName(IReference reference)
    {
        String qualifiedName = reference.getName();
        if (reference instanceof IResolvedQualifiersReference)
            qualifiedName = ((IResolvedQualifiersReference) reference)
                    .getDisplayString();
        return qualifiedName;
    }

    public static final IClassNode findClassNode(IPackageNode node)
    {
        IScopedNode scope = node.getScopedNode();
        for (int i = 0; i < scope.getChildCount(); i++)
        {
            IASNode child = scope.getChild(i);
            if (child instanceof IClassNode)
                return (IClassNode) child;
        }
        return null;
    }

    public static final ITypeNode findTypeNode(IASNode node)
    {
        IASNode parent = node.getParent();
        while (parent != null)
        {
            if (parent instanceof ITypeNode)
                return (ITypeNode) parent;
            parent = parent.getParent();
        }
        return null;
    }

    public static ITypeDefinition getTypeDefinition(IDefinitionNode node)
    {
        ITypeNode tnode = (ITypeNode) node.getAncestorOfType(ITypeNode.class);
        return (ITypeDefinition) tnode.getDefinition();
    }

    public static IClassDefinition getClassDefinition(IASNode node)
    {
        IClassNode tnode = (IClassNode) node
                .getAncestorOfType(IClassNode.class);
        return tnode.getDefinition();
    }

    public static final boolean hasConstructorParameters(
            IClassDefinition definiton)
    {
        return definiton.getConstructor().getParameters().length > 0;
    }

    public static String returnInitialVariableValue(IVariableNode node,
            IRandoriEmitter emitter)
    {
        IVariableDefinition definition = (IVariableDefinition) node
                .getDefinition();

        IExpressionNode valueNode = node.getAssignedValueNode();
        if (valueNode != null)
            return emitter.toNodeString(valueNode);
        else
            return ExpressionUtils.toInitialValue(definition, emitter
                    .getWalker().getProject());
    }

    public static final boolean isImplicit(IContainerNode node)
    {
        return node.getContainerType() == ContainerType.IMPLICIT
                || node.getContainerType() == ContainerType.SYNTHESIZED;
    }

    public static ITypeDefinition getTypeDefinition(IDefinition definition)
    {
        while (definition != null)
        {
            if (definition instanceof ITypeDefinition)
                return (ITypeDefinition) definition;
            definition = definition.getParent();
        }
        return null;
    }

    public static IClassDefinition getClassDefinition(IDefinition definition)
    {
        while (definition != null)
        {
            if (definition instanceof IClassDefinition)
                return (IClassDefinition) definition;
            definition = definition.getParent();
        }
        return null;
    }

    public static String toSuperBaseName(IASNode node)
    {
        ITypeNode tnode = (ITypeNode) node.getAncestorOfType(ITypeNode.class);
        return toSuperBaseName((ITypeDefinition) tnode.getDefinition());
    }

    public static String toSuperBaseName(ITypeDefinition type)
    {
        if (type instanceof IClassDefinition)
        {
            IClassDefinition cdefintion = (IClassDefinition) type;
            IReference reference = cdefintion.getBaseClassReference();
            if (reference != null)
                return reference.getName();
        }
        return null;
    }

    public static String toSuperQualifiedName(IASNode node,
            ICompilerProject project)
    {
        ITypeNode tnode = (ITypeNode) node.getAncestorOfType(ITypeNode.class);
        return toSuperQualifiedName((ITypeDefinition) tnode.getDefinition(),
                project);
    }

    public static String toSuperQualifiedName(ITypeDefinition type,
            ICompilerProject project)
    {
        if (type instanceof IClassDefinition)
        {
            IClassDefinition cdefintion = (IClassDefinition) type;
            IClassDefinition baseClass = cdefintion.resolveBaseClass(project);
            if (baseClass != null)
                return baseClass.getQualifiedName();
        }
        return null;
    }

    public static final boolean isInSWCCompilationUnit(IDefinition definition,
            ICompilerProject project)
    {
        IClassDefinition type = getClassDefinition(definition);
        if (type == null)
            return false;

        String qName = type.getQualifiedName();
        ICompilationUnit unit = project.resolveQNameToCompilationUnit(qName);
        if (unit instanceof SWCCompilationUnit)
            return true;

        return false;
    }

    public static IClassDefinition getBaseClassDefinition(
            IFunctionDefinition definition, ICompilerProject project)
    {
        IClassDefinition type = getClassDefinition(definition);
        if (type == null)
            return null;

        IClassDefinition baseDefinition = type.resolveBaseClass(project);
        return baseDefinition;
    }

    public static boolean isVariableAParameter(IVariableDefinition node,
            IParameterDefinition[] parameters)
    {
        for (IParameterDefinition parameter : parameters)
        {
            if (node.getBaseName().equals(parameter.getBaseName()))
                return true;
        }
        return false;
    }

    public static void fillStaticMembers(IDefinitionNode[] members,
            List<IDefinitionNode> list, boolean excludeFields,
            boolean excludeFunctions)
    {
        for (IDefinitionNode node : members)
        {
            if (node.hasModifier(ASModifier.STATIC))
            {
                if (!excludeFields && node instanceof IVariableNode)
                    list.add(node);
                else if (!excludeFunctions && node instanceof IFunctionNode)
                    list.add(node);
            }
        }
    }

    public static void fillInstanceMembers(IDefinitionNode[] members,
            List<IDefinitionNode> list)
    {
        for (IDefinitionNode node : members)
        {
            if (node instanceof IFunctionNode
                    && ((IFunctionNode) node).isConstructor())
                continue;

            if (!node.hasModifier(ASModifier.STATIC))
            {
                list.add(node);
            }
        }
    }

    public static List<IVariableDefinition> getFields(
            IClassDefinition definition, boolean excludePrivate)
    {
        ArrayList<IVariableDefinition> result = new ArrayList<IVariableDefinition>();
        Collection<IDefinition> definitions = definition.getContainedScope()
                .getAllLocalDefinitions();
        for (IDefinition member : definitions)
        {
            if (!member.isImplicit() && member instanceof IVariableDefinition)
            {
                IVariableDefinition vnode = (IVariableDefinition) member;
                if (!member.isStatic()
                        && (member.isPublic() || member.isProtected()))
                    result.add(vnode);
                // TODO FIX the logic here, this won't add twice though
                if (!excludePrivate && member.isPrivate())
                    result.add(vnode);
            }
        }
        return result;
    }

    public static void fillStaticStatements(IClassNode node,
            List<IASNode> list, boolean excludeFields)
    {
        int len = node.getScopedNode().getChildCount();
        for (int i = 0; i < len; i++)
        {
            IASNode child = node.getScopedNode().getChild(i);
            if (child instanceof IExpressionNode)
                list.add(child);
            else if (child instanceof IDefinitionNode)
            {
                if (!excludeFields
                        && ((IDefinitionNode) child)
                                .hasModifier(ASModifier.STATIC)
                        && child instanceof IVariableNode)
                    list.add(child);
            }
        }
    }

    public static String toPackageName(String name)
    {
        if (!name.contains("."))
            return name;
        final String stem = name.substring(0, name.lastIndexOf("."));
        return stem;
    }

    public static String toBaseName(String name)
    {
        if (!name.contains("."))
            return name;
        final String basename = name.substring(name.lastIndexOf(".") + 1);
        return basename;
    }

    public static Map<Integer, IParameterNode> getDefaults(
            IParameterNode[] nodes)
    {
        Map<Integer, IParameterNode> result = new HashMap<Integer, IParameterNode>();
        int i = 0;
        boolean hasDefaults = false;
        for (IParameterNode node : nodes)
        {
            if (node.hasDefaultValue())
            {
                hasDefaults = true;
                result.put(i, node);
            }
            else
            {
                result.put(i, null);
            }
            i++;
        }

        if (!hasDefaults)
            return null;

        return result;
    }

    public static final boolean isMemberDefinition(IDefinition definition)
    {
        return definition != null
                && (definition.getParent() instanceof IClassDefinition || definition
                        .getParent() instanceof IInterfaceDefinition);
    }

    public static boolean isThisIdentifier(IExpressionNode node)
    {
        return node instanceof ILanguageIdentifierNode
                && ((ILanguageIdentifierNode) node).getKind() == LanguageIdentifierKind.THIS;
    }

    public static IClassDefinition getClassDefinition(ICompilationUnit unit)
    {
        IFileNode fileNode = null;
        try
        {
            fileNode = (IFileNode) unit.getSyntaxTreeRequest().get().getAST();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        IDefinition[] definitions = fileNode.getTopLevelDefinitions(false,
                false);
        return (IClassDefinition) definitions[0];
    }
}
