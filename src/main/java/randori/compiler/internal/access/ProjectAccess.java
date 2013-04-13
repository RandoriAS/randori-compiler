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

package randori.compiler.internal.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition.IClassIterator;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.definitions.IEffectDefinition;
import org.apache.flex.compiler.definitions.IEventDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IGetterDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.IMetadataDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.ISetterDefinition;
import org.apache.flex.compiler.definitions.IStyleDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.projects.ICompilerProject;

import randori.compiler.access.IASProjectAccess;
import randori.compiler.access.IPackageBundle;
import randori.compiler.internal.visitor.as.ASWalker;
import randori.compiler.visitor.as.IASVisitor;
import randori.compiler.visitor.as.IASWalker;

/**
 * @author Michael Schmalle
 */
public class ProjectAccess implements IASProjectAccess
{
    private IASProject project;

    @Override
    public ICompilerProject getProject()
    {
        return project;
    }

    private List<ITypeDefinition> types = new ArrayList<ITypeDefinition>();

    private Map<String, Collection<IClassDefinition>> subclasses = new HashMap<String, Collection<IClassDefinition>>();

    private Map<String, List<IClassDefinition>> implementors = new HashMap<String, List<IClassDefinition>>();

    private Map<String, List<IInterfaceDefinition>> subinterfaces = new HashMap<String, List<IInterfaceDefinition>>();

    private Map<String, List<IClassDefinition>> classes = new HashMap<String, List<IClassDefinition>>();

    private Map<String, List<IInterfaceDefinition>> interfaces = new HashMap<String, List<IInterfaceDefinition>>();

    private Map<String, IPackageBundle> packagesMap = new TreeMap<String, IPackageBundle>();

    private Map<String, List<IVariableDefinition>> variables = new HashMap<String, List<IVariableDefinition>>();

    private Map<String, List<IAccessorDefinition>> accessors = new HashMap<String, List<IAccessorDefinition>>();

    private Map<String, List<IGetterDefinition>> gaccessors = new HashMap<String, List<IGetterDefinition>>();

    private Map<String, List<ISetterDefinition>> saccessors = new HashMap<String, List<ISetterDefinition>>();

    private Map<String, List<IConstantDefinition>> constants = new HashMap<String, List<IConstantDefinition>>();

    private Map<String, List<IFunctionDefinition>> methods = new HashMap<String, List<IFunctionDefinition>>();

    //private Map<String, List<IEventDefinition>> events = new HashMap<String, List<IEventDefinition>>();

    //private Map<String, List<IEffectDefinition>> effects = new HashMap<String, List<IEffectDefinition>>();

    //private Map<String, List<IStyleDefinition>> styles = new HashMap<String, List<IStyleDefinition>>();

    //private Map<String, List<ISkinStateDefinition>> skinstates = new HashMap<String, List<ISkinStateDefinition>>();

    //private Map<String, List<ISkinPartDefinition>> skinparts = new HashMap<String, List<ISkinPartDefinition>>();

    public ProjectAccess(IASProject project)
    {
        this.project = project;
    }

    @Override
    public List<IClassDefinition> getClasses()
    {
        List<IClassDefinition> result = new ArrayList<IClassDefinition>();
        for (List<IClassDefinition> list : classes.values())
        {
            for (IClassDefinition definition : list)
            {
                result.add(definition);
            }
        }
        return result;
    }

    @Override
    public void process()
    {
        IASVisitor visitor = new ProjectAccessVisitor(this);
        IASWalker walker = new ASWalker(visitor);

        try
        {
            walker.walkProject(project);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        build();
        //        processASDocs();
    }

    //    private void processASDocs()
    //    {
    //        // find @inheritDoc
    //        for (List<IFunctionDefinition> list : functions.values())
    //        {
    //            for (IFunctionDefinition function : list)
    //            {
    //                if (function.hasExplicitComment())
    //                {
    //                    IDocComment comment = (IDocComment) function
    //                            .getExplicitSourceComment();
    //                    if (comment.hasTag("inheritDoc"))
    //                    {
    //                        proccessInheritDoc(function);
    //                    }
    //                }
    //            }
    //        }
    //        for (List<IAccessorDefinition> list : accessors.values())
    //        {
    //            for (IAccessorDefinition function : list)
    //            {
    //                if (function.hasExplicitComment())
    //                {
    //                    IDocComment comment = (IDocComment) function
    //                            .getExplicitSourceComment();
    //                    if (comment.hasTag("inheritDoc"))
    //                    {
    //                        proccessInheritDoc(function);
    //                    }
    //                    if (comment.hasTag("copy"))
    //                    {
    //                        processCopy(function);
    //                    }
    //                }
    //            }
    //        }
    //    }

    //    private void processCopy(IDocumentableDefinition definition)
    //    {
    //        IDocComment comment = (IDocComment) definition
    //                .getExplicitSourceComment();
    //        IDocTag tag = comment.getTag("copy");
    //        String uri = tag.getDescription().trim();
    //        String[] split = uri.split("#");
    //        String qualifiedName = split[0];
    //        //String name = split[1];
    //
    //        ITypeDefinition type = getType(qualifiedName);
    //        if (type == null)
    //            return;
    //
    //        IDocumentableDefinition member = getDefinition(uri);
    //        if (member != null)
    //        {
    //            if (member != null && member.hasExplicitComment())
    //            {
    //                IDocComment source = (IDocComment) member
    //                        .getExplicitSourceComment();
    //                comment.paste(source);
    //            }
    //        }
    //    }

    private Map<String, IDocumentableDefinition> definitions = new HashMap<String, IDocumentableDefinition>();

    void addDefinition(IDocumentableDefinition definition)
    {
        String name = definition.getBaseName();
        String parentName = getParentQualifiedName(definition);
        String path = definition.getQualifiedName();
        if (definition instanceof IAccessorDefinition)
        {
            path = parentName + "#" + name;
        }
        else if (definition instanceof IFunctionDefinition)
        {
            path = parentName + "#" + name + "()";
        }
        else if (definition instanceof IMetadataDefinition)
        {
            String type = getMetaDataType((IMetadataDefinition) definition);
            path = parentName + "#" + type + ":" + name;
        }
        definitions.put(path, definition);
    }

    private String getParentQualifiedName(IDocumentableDefinition definition)
    {
        if (definition instanceof IMetadataDefinition)
        {
            return ((IMetadataDefinition) definition).getDecoratedDefinition()
                    .getQualifiedName();
        }
        else
        {
            return definition.getParent().getQualifiedName();
        }
    }

    private String getMetaDataType(IMetadataDefinition definition)
    {
        String type = null;
        if (definition instanceof IEventDefinition)
        {
            type = "event";
        }
        else if (definition instanceof IEffectDefinition)
        {
            type = "effect";
        }
        else if (definition instanceof IStyleDefinition)
        {
            type = "style";
        }
        //        else if (definition instanceof ISkinPartDefinition)
        //        {
        //            type = "skinpart";
        //        }
        //        else if (definition instanceof ISkinStateDefinition)
        //        {
        //            type = "skinstate";
        //        }
        return type;
    }

    @Override
    public IDocumentableDefinition getDefinition(String uri)
    {
        return definitions.get(uri);
    }

    // private IDocumentableDefinition getMember(ITypeDefinition type, String
    // name)
    // {
    // // accessor:foo.bar.Baz#myProp, method:foo.bar.Baz#myMethod(), style
    // foo.bar.Baz#style:myStyle
    // String uri = type.getQualifiedName() + "+" + name;
    // return null;
    // }

    @SuppressWarnings("unused")
    private void proccessInheritDoc(IFunctionDefinition function)
    {
        // first try implemented interface
        IFunctionDefinition override = function
                .resolveImplementedFunction(getProject());

        if (override == null)
        {
            // next try an overridden function
            override = function.resolveOverriddenFunction(getProject());
        }

        //        IDocComment comment = (IDocComment) function.getExplicitSourceComment();
        //        if (override != null && override.hasExplicitComment())
        //        {
        //            IDocComment source = (IDocComment) override
        //                    .getExplicitSourceComment();
        //            comment.paste(source);
        //        }
    }

    private void build()
    {
        for (Entry<String, List<IGetterDefinition>> entry : gaccessors
                .entrySet())
        {
            String unitName = entry.getKey();
            // for each getter in the unit
            for (IGetterDefinition getter : entry.getValue())
            {
                boolean found = false;
                // try and find a matching setter
                List<ISetterDefinition> setters = saccessors.get(unitName);
                if (setters != null)
                {
                    for (ISetterDefinition setter : setters)
                    {
                        // if we found a setter for the getter
                        if (setter.getBaseName().equals(getter.getBaseName()))
                        {
                            // set their siblings
                            // getter.setSibling(setter);
                            // setter.setSibling(getter);
                            addAccessor(getter);
                            found = true;
                            break;
                        }
                    }
                }
                // if we didn't find a getter setter pair
                // add both to the accessors list
                if (!found)
                {
                    addAccessor(getter);
                }
            }
        }
        for (Entry<String, List<ISetterDefinition>> entry : saccessors
                .entrySet())
        {
            // for each setter in the unit
            for (ISetterDefinition setter : entry.getValue())
            {
                // if the setter's sibling hasn't been set, it means it's an
                // orphan
                // for now so add it to the real accessor list
                if (setter.resolveCorrespondingAccessor(getProject()) == null)
                {
                    addAccessor(setter);
                }
            }
        }
        for (ITypeDefinition definition : types)
        {
            if (definition instanceof IClassDefinition)
            {
                proccessClassTree((IClassDefinition) definition);
            }
            else if (definition instanceof IInterfaceDefinition)
            {
                proccessInterfaceTree((IInterfaceDefinition) definition);
            }
        }
    }

    private void proccessInterfaceTree(IInterfaceDefinition definition)
    {
        IInterfaceDefinition[] definitions = definition
                .resolveExtendedInterfaces(project);
        for (IInterfaceDefinition isuper : definitions)
        {
            List<IInterfaceDefinition> list = subinterfaces.get(isuper
                    .getQualifiedName());
            if (list == null)
            {
                list = new ArrayList<IInterfaceDefinition>();
                subinterfaces.put(isuper.getQualifiedName(), list);
            }
            if (!list.contains(definition))
                list.add(definition);
        }

    }

    private void proccessClassTree(IClassDefinition definition)
    {
        IClassIterator i = definition.classIterator(getProject(), false);

        if (i.hasNext())
        {
            IClassDefinition superType = i.next();
            Collection<IClassDefinition> list = subclasses.get(superType
                    .getQualifiedName());

            if (list == null)
            {
                list = new ArrayList<IClassDefinition>();
                subclasses.put(superType.getQualifiedName(), list);
            }

            list.add(definition);
        }

        Set<IInterfaceDefinition> implementations = definition
                .resolveAllInterfaces(getProject());
        for (IInterfaceDefinition idef : implementations)
        {
            List<IClassDefinition> list = implementors.get(idef
                    .getQualifiedName());
            if (list == null)
            {
                list = new ArrayList<IClassDefinition>();
                implementors.put(idef.getQualifiedName(), list);
            }
            if (!list.contains(definition))
            {
                list.add(definition);
            }
        }
    }

    @Override
    public ITypeDefinition getType(String name)
    {
        // XXX make types a Map
        for (ITypeDefinition type : types)
        {
            if (type.getQualifiedName().equals(name))
                return type;
        }
        return null;
    }

    @Override
    public Set<String> getPackages()
    {
        return packagesMap.keySet();
    }

    @Override
    public IPackageBundle getPackageBundle(IDefinition definition)
    {
        PackageBundle bundle = (PackageBundle) getPackageBundle(definition
                .getPackageName());
        return bundle;
    }

    IPackageBundle getPackageBundle(String packageName)
    {
        PackageBundle bundle = (PackageBundle) packagesMap.get(packageName);
        return bundle;
    }

    @Override
    public Collection<IPackageBundle> getPackageBundles()
    {
        // TODO Auto-generated method stub
        return packagesMap.values();
    }

    public void addPackage(IPackageDefinition definition)
    {
        PackageBundle bundle = (PackageBundle) getPackageBundle(definition
                .getQualifiedName());
        if (bundle == null)
        {
            // XXX Get package path from file path, does compiler have util for
            // this?
            String path = definition.getContainingFilePath();
            bundle = new PackageBundle(path, definition.getBaseName(),
                    definition.getQualifiedName());
            packagesMap.put(definition.getQualifiedName(), bundle);
        }
        bundle.add(definition);
    }

    public void addClass(IClassDefinition definition)
    {
        PackageBundle bundle = (PackageBundle) getPackageBundle(definition);
        if (bundle != null)
        {
            addType(definition);
            bundle.addClass(definition);
            classAdded(definition);
        }
    }

    private void classAdded(IClassDefinition definition)
    {
        List<IClassDefinition> list = classes
                .get(definition.getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IClassDefinition>();
            classes.put(definition.getQualifiedName(), list);
        }
        list.add(definition);
    }

    private void interfaceAdded(IInterfaceDefinition definition)
    {
        List<IInterfaceDefinition> list = interfaces.get(definition
                .getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IInterfaceDefinition>();
            interfaces.put(definition.getQualifiedName(), list);
        }
        list.add(definition);
    }

    @Override
    public Collection<IInterfaceDefinition> getInterfaces()
    {
        List<IInterfaceDefinition> result = new ArrayList<IInterfaceDefinition>();
        for (List<IInterfaceDefinition> list : interfaces.values())
        {
            for (IInterfaceDefinition definition : list)
            {
                result.add(definition);
            }
        }
        return result;
    }

    public void addInterface(IInterfaceDefinition definition)
    {
        PackageBundle bundle = (PackageBundle) getPackageBundle(definition);
        if (bundle != null)
        {

            addType(definition);
            bundle.addInterface(definition);
            interfaceAdded(definition);

        }
    }

    private void addType(ITypeDefinition definition)
    {
        types.add(definition);
    }

    List<IVariableDefinition> getVariablesRaw(ITypeDefinition definition,
            String namespace)
    {
        List<IVariableDefinition> result = new ArrayList<IVariableDefinition>();
        if (!variables.containsKey(definition.getQualifiedName()))
            return result;

        for (IVariableDefinition variable : variables.get(definition
                .getQualifiedName()))
        {
            if (variable.getNode().hasNamespace(namespace))
            {
                result.add(variable);
            }
        }
        return result;
    }

    List<IFunctionDefinition> getFunctionsRaw(ITypeDefinition definition,
            String namespace)
    {
        List<IFunctionDefinition> result = new ArrayList<IFunctionDefinition>();
        if (!methods.containsKey(definition.getQualifiedName()))
            return result;

        for (IFunctionDefinition member : methods.get(definition
                .getQualifiedName()))
        {
            if (member.getNode().hasNamespace(namespace)
                    || definition instanceof IInterfaceDefinition)
            {
                result.add(member);
            }
        }
        return result;
    }

    List<IAccessorDefinition> getAccessorsRaw(ITypeDefinition definition,
            String namespace)
    {
        List<IAccessorDefinition> result = new ArrayList<IAccessorDefinition>();
        if (!accessors.containsKey(definition.getQualifiedName()))
            return result;

        for (IAccessorDefinition member : accessors.get(definition
                .getQualifiedName()))
        {
            if (member.getNode().hasNamespace(namespace)
                    || definition instanceof IInterfaceDefinition)
            {
                result.add(member);
            }
        }
        return result;
    }

    List<IConstantDefinition> getConstantsRaw(ITypeDefinition definition,
            String namespace)
    {
        List<IConstantDefinition> result = new ArrayList<IConstantDefinition>();
        if (!constants.containsKey(definition.getQualifiedName()))
            return result;

        for (IConstantDefinition constant : constants.get(definition
                .getQualifiedName()))
        {
            if (constant.getNode().hasNamespace(namespace))
            {
                result.add(constant);
            }
        }
        return result;
    }

    public void addGetAccessor(IGetterDefinition definition)
    {
        ITypeDefinition parent = (ITypeDefinition) definition.getParent();

        List<IGetterDefinition> list = gaccessors
                .get(parent.getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IGetterDefinition>();
            gaccessors.put(parent.getQualifiedName(), list);
        }

        list.add(definition);
    }

    public void addSetAccessor(ISetterDefinition definition)
    {
        ITypeDefinition parent = (ITypeDefinition) definition.getParent();

        List<ISetterDefinition> list = saccessors
                .get(parent.getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<ISetterDefinition>();
            saccessors.put(parent.getQualifiedName(), list);
        }

        list.add(definition);
    }

    public void addAccessor(IAccessorDefinition definition)
    {
        ITypeDefinition parent = (ITypeDefinition) definition.getParent();

        List<IAccessorDefinition> list = accessors.get(parent
                .getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IAccessorDefinition>();
            accessors.put(parent.getQualifiedName(), list);
        }

        list.add(definition);
        addDefinition(definition);
    }

    public void addConstant(IConstantDefinition definition)
    {
        IClassDefinition parent = (IClassDefinition) definition.getParent();

        List<IConstantDefinition> list = constants.get(parent
                .getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IConstantDefinition>();
            constants.put(parent.getQualifiedName(), list);
        }

        list.add(definition);
        addDefinition(definition);
    }

    public void addVariable(IVariableDefinition definition)
    {
        IClassDefinition parent = (IClassDefinition) definition.getParent();

        List<IVariableDefinition> list = variables.get(parent
                .getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IVariableDefinition>();
            variables.put(parent.getQualifiedName(), list);
        }

        list.add(definition);
        addDefinition(definition);
    }

    public void addFunction(IFunctionDefinition definition)
    {
        ITypeDefinition parent = (ITypeDefinition) definition.getParent();

        List<IFunctionDefinition> list = methods.get(parent.getQualifiedName());
        if (list == null)
        {
            list = new ArrayList<IFunctionDefinition>();
            methods.put(parent.getQualifiedName(), list);
        }

        list.add(definition);
        addDefinition(definition);
    }

    @Override
    public Collection<ITypeDefinition> getTypes()
    {
        return types;
    }

    @Override
    public Collection<ITypeDefinition> getTypes(IPackageBundle bundle)
    {
        ArrayList<ITypeDefinition> result = new ArrayList<ITypeDefinition>();
        // XXX do this correctly
        for (ITypeDefinition type : types)
        {
            if (type.getPackageName().equals(bundle.getQualifiedName()))
            {
                result.add(type);
            }
        }
        return result;
    }

    @Override
    public Collection<IClassDefinition> getSubClasses(
            IClassDefinition definition)
    {
        return subclasses.get(definition.getQualifiedName());
    }

    @Override
    public Collection<IClassDefinition> getInterfaceImplementors(
            IInterfaceDefinition definition)
    {
        return implementors.get(definition.getQualifiedName());
    }

    @Override
    public Collection<IInterfaceDefinition> getSubInterfaces(
            IInterfaceDefinition definition)
    {
        return subinterfaces.get(definition.getQualifiedName());
    }

    @Override
    public List<IVariableDefinition> getVariables(IClassDefinition definition,
            String namespace, boolean inherit)
    {
        if (!inherit || namespace.equals("private"))
            return getVariablesRaw(definition, namespace);

        List<IVariableDefinition> result = new ArrayList<IVariableDefinition>();
        Map<String, IVariableDefinition> map = new HashMap<String, IVariableDefinition>();
        Iterator<ITypeDefinition> i = getIterator(definition);
        while (i.hasNext())
        {
            IClassDefinition sdefinition = (IClassDefinition) i.next();
            List<IVariableDefinition> list = getVariablesRaw(sdefinition,
                    namespace);
            for (IVariableDefinition member : list)
            {
                if (!map.containsKey(member.getBaseName()))
                {
                    result.add(member);
                    map.put(member.getBaseName(), member);
                }
            }
        }
        return result;
    }

    @Override
    public List<IConstantDefinition> getConstants(IClassDefinition definition,
            String namespace, boolean inherit)
    {
        if (!inherit || namespace.equals("private"))
            return getConstantsRaw(definition, namespace);

        List<IConstantDefinition> result = new ArrayList<IConstantDefinition>();
        Map<String, IConstantDefinition> map = new HashMap<String, IConstantDefinition>();
        Iterator<ITypeDefinition> i = getIterator(definition);
        while (i.hasNext())
        {
            IClassDefinition sdefinition = (IClassDefinition) i.next();
            List<IConstantDefinition> list = getConstantsRaw(sdefinition,
                    namespace);
            for (IConstantDefinition member : list)
            {
                if (!map.containsKey(member.getBaseName()))
                {
                    result.add(member);
                    map.put(member.getBaseName(), member);
                }
            }
        }
        return result;
    }

    @Override
    public List<IAccessorDefinition> getAccessors(ITypeDefinition definition,
            String visibility, boolean inherit)
    {
        if (definition instanceof IClassDefinition)
        {
            return getAccessors((IClassDefinition) definition, visibility,
                    inherit);
        }
        else
        {
            return getAccessors((IInterfaceDefinition) definition, visibility,
                    inherit);
        }
    }

    @Override
    public List<IFunctionDefinition> getFunctions(ITypeDefinition definition,
            String visibility, boolean inherit)
    {
        if (definition instanceof IClassDefinition)
        {
            return getFunctions((IClassDefinition) definition, visibility,
                    inherit);
        }
        else
        {
            return getFunctions((IInterfaceDefinition) definition, visibility,
                    inherit);
        }
    }

    List<IAccessorDefinition> getAccessors(IClassDefinition definition,
            String namespace, boolean inherit)
    {
        if (!inherit || namespace.equals("private"))
            return getAccessorsRaw(definition, namespace);

        List<IAccessorDefinition> result = new ArrayList<IAccessorDefinition>();
        Map<String, IAccessorDefinition> map = new HashMap<String, IAccessorDefinition>();
        Iterator<ITypeDefinition> i = getIterator(definition);
        while (i.hasNext())
        {
            IClassDefinition sdefinition = (IClassDefinition) i.next();
            List<IAccessorDefinition> list = getAccessorsRaw(sdefinition,
                    namespace);
            for (IAccessorDefinition function : list)
            {
                if (!map.containsKey(function.getBaseName()))
                {
                    result.add(function);
                    map.put(function.getBaseName(), function);
                }
            }
        }
        return result;
    }

    List<IAccessorDefinition> getAccessors(IInterfaceDefinition definition,
            String namespace, boolean inherit)
    {
        if (!inherit || namespace.equals("private"))
            return getAccessorsRaw(definition, namespace);

        List<IAccessorDefinition> result = new ArrayList<IAccessorDefinition>();
        Map<String, IAccessorDefinition> map = new HashMap<String, IAccessorDefinition>();
        Iterator<ITypeDefinition> i = getIterator(definition);
        while (i.hasNext())
        {
            IInterfaceDefinition sdefinition = (IInterfaceDefinition) i.next();
            List<IAccessorDefinition> list = getAccessorsRaw(sdefinition,
                    namespace);
            for (IAccessorDefinition function : list)
            {
                if (!map.containsKey(function.getBaseName()))
                {
                    result.add(function);
                    map.put(function.getBaseName(), function);
                }
            }
        }
        return result;
    }

    List<IFunctionDefinition> getFunctions(IClassDefinition definition,
            String namespace, boolean inherit)
    {
        if (!inherit || namespace.equals("private"))
            return getFunctionsRaw(definition, namespace);

        List<IFunctionDefinition> result = new ArrayList<IFunctionDefinition>();
        Map<String, IFunctionDefinition> map = new HashMap<String, IFunctionDefinition>();
        Iterator<ITypeDefinition> i = getIterator(definition);
        while (i.hasNext())
        {
            IClassDefinition sdefinition = (IClassDefinition) i.next();
            List<IFunctionDefinition> list = getFunctionsRaw(sdefinition,
                    namespace);
            for (IFunctionDefinition function : list)
            {
                if (!map.containsKey(function.getBaseName()))
                {
                    result.add(function);
                    map.put(function.getBaseName(), function);
                }
            }
        }
        return result;
    }

    protected Iterator<ITypeDefinition> getIterator(
            IInterfaceDefinition definition)
    {
        Iterator<IInterfaceDefinition> i = definition.interfaceIterator(
                getProject(), false);
        List<ITypeDefinition> result = new ArrayList<ITypeDefinition>();
        while (i.hasNext())
        {
            IInterfaceDefinition cd = i.next();
            result.add(cd);
        }
        Collections.reverse(result);
        result.add(0, definition);
        return result.iterator();
    }

    protected Iterator<ITypeDefinition> getIterator(IClassDefinition definition)
    {
        IClassIterator i = definition.classIterator(getProject(), false);
        List<ITypeDefinition> result = new ArrayList<ITypeDefinition>();
        while (i.hasNext())
        {
            IClassDefinition cd = i.next();
            result.add(cd);
        }
        Collections.reverse(result);
        result.add(0, definition);
        return result.iterator();
    }

    List<IFunctionDefinition> getFunctions(IInterfaceDefinition definition,
            String namespace, boolean inherit)
    {
        if (!inherit || namespace.equals("private"))
            return getFunctionsRaw(definition, namespace);

        List<IFunctionDefinition> result = new ArrayList<IFunctionDefinition>();
        Map<String, IFunctionDefinition> map = new HashMap<String, IFunctionDefinition>();
        Iterator<IInterfaceDefinition> i = definition.interfaceIterator(
                getProject(), true);
        while (i.hasNext())
        {
            IInterfaceDefinition sdefinition = i.next();
            List<IFunctionDefinition> list = getFunctionsRaw(sdefinition,
                    namespace);
            for (IFunctionDefinition function : list)
            {
                if (!map.containsKey(function.getBaseName()))
                {
                    result.add(function);
                    map.put(function.getBaseName(), function);
                }
            }
        }
        return result;
    }

    //    List<IStyleDefinition> getStylesRaw(ITypeDefinition definition,
    //            String namespace)
    //    {
    //        List<IStyleDefinition> result = new ArrayList<IStyleDefinition>();
    //        if (!styles.containsKey(definition.getQualifiedName()))
    //            return result;
    //
    //        for (IStyleDefinition member : styles
    //                .get(definition.getQualifiedName()))
    //        {
    //            // if (member.getNode().hasNamespace(namespace))
    //            // {
    //            result.add(member);
    //            // }
    //        }
    //        return result;
    //    }
    //
    //    List<IEffectDefinition> getEffectsRaw(ITypeDefinition definition,
    //            String namespace)
    //    {
    //        List<IEffectDefinition> result = new ArrayList<IEffectDefinition>();
    //        if (!effects.containsKey(definition.getQualifiedName()))
    //            return result;
    //
    //        for (IEffectDefinition member : effects.get(definition
    //                .getQualifiedName()))
    //        {
    //            // if (member.getNode().hasNamespace(namespace))
    //            // {
    //            result.add(member);
    //            // }
    //        }
    //        return result;
    //    }
    //
    //    List<IEventDefinition> getEventsRaw(ITypeDefinition definition,
    //            String namespace)
    //    {
    //        List<IEventDefinition> result = new ArrayList<IEventDefinition>();
    //        if (!events.containsKey(definition.getQualifiedName()))
    //            return result;
    //
    //        for (IEventDefinition member : events
    //                .get(definition.getQualifiedName()))
    //        {
    //            // if (member.getNode().hasNamespace(namespace))
    //            // {
    //            result.add(member);
    //            // }
    //        }
    //        return result;
    //    }
    //
    //    List<ISkinPartDefinition> getSkinPartsRaw(ITypeDefinition definition,
    //            String namespace)
    //    {
    //        List<ISkinPartDefinition> result = new ArrayList<ISkinPartDefinition>();
    //        if (!skinparts.containsKey(definition.getQualifiedName()))
    //            return result;
    //
    //        for (ISkinPartDefinition member : skinparts.get(definition
    //                .getQualifiedName()))
    //        {
    //            // if (member.getNode().hasNamespace(namespace))
    //            // {
    //            result.add(member);
    //            // }
    //        }
    //        return result;
    //    }
    //
    //    List<ISkinStateDefinition> getSkinStatesRaw(ITypeDefinition definition,
    //            String namespace)
    //    {
    //        List<ISkinStateDefinition> result = new ArrayList<ISkinStateDefinition>();
    //        if (!skinstates.containsKey(definition.getQualifiedName()))
    //            return result;
    //
    //        for (ISkinStateDefinition member : skinstates.get(definition
    //                .getQualifiedName()))
    //        {
    //            // if (member.getNode().hasNamespace(namespace))
    //            // {
    //            result.add(member);
    //            // }
    //        }
    //        return result;
    //    }
    //
    //    @Override
    //    public List<IEventDefinition> getEvents(IClassDefinition definition,
    //            String namespace, boolean inherit)
    //    {
    //        if (!inherit)
    //            return getEventsRaw(definition, namespace);
    //
    //        List<IEventDefinition> result = new ArrayList<IEventDefinition>();
    //        Map<String, IEventDefinition> map = new HashMap<String, IEventDefinition>();
    //        Iterator<ITypeDefinition> i = getIterator(definition);
    //        while (i.hasNext())
    //        {
    //            IClassDefinition sdefinition = (IClassDefinition) i.next();
    //            List<IEventDefinition> list = getEventsRaw(sdefinition, namespace);
    //            for (IEventDefinition member : list)
    //            {
    //                if (!map.containsKey(member.getBaseName()))
    //                {
    //                    result.add(member);
    //                    map.put(member.getBaseName(), member);
    //                }
    //            }
    //        }
    //        return result;
    //    }
    //
    //    @Override
    //    public List<IStyleDefinition> getStyles(IClassDefinition definition,
    //            String namespace, boolean inherit)
    //    {
    //        if (!inherit)
    //            return getStylesRaw(definition, namespace);
    //
    //        List<IStyleDefinition> result = new ArrayList<IStyleDefinition>();
    //        Map<String, IStyleDefinition> map = new HashMap<String, IStyleDefinition>();
    //        Iterator<ITypeDefinition> i = getIterator(definition);
    //        while (i.hasNext())
    //        {
    //            IClassDefinition sdefinition = (IClassDefinition) i.next();
    //            List<IStyleDefinition> list = getStylesRaw(sdefinition, namespace);
    //            for (IStyleDefinition member : list)
    //            {
    //                if (!map.containsKey(member.getBaseName()))
    //                {
    //                    result.add(member);
    //                    map.put(member.getBaseName(), member);
    //                }
    //            }
    //        }
    //        return result;
    //    }
    //
    //    @Override
    //    public List<IEffectDefinition> getEffects(IClassDefinition definition,
    //            String namespace, boolean inherit)
    //    {
    //        if (!inherit)
    //            return getEffectsRaw(definition, namespace);
    //
    //        List<IEffectDefinition> result = new ArrayList<IEffectDefinition>();
    //        Map<String, IEffectDefinition> map = new HashMap<String, IEffectDefinition>();
    //        Iterator<ITypeDefinition> i = getIterator(definition);
    //        while (i.hasNext())
    //        {
    //            IClassDefinition sdefinition = (IClassDefinition) i.next();
    //            List<IEffectDefinition> list = getEffectsRaw(sdefinition, namespace);
    //            for (IEffectDefinition member : list)
    //            {
    //                if (!map.containsKey(member.getBaseName()))
    //                {
    //                    result.add(member);
    //                    map.put(member.getBaseName(), member);
    //                }
    //            }
    //        }
    //        return result;
    //    }
    //
    //    @Override
    //    public List<ISkinPartDefinition> getSkinParts(IClassDefinition definition,
    //            String namespace, boolean inherit)
    //    {
    //        if (!inherit)
    //            return getSkinPartsRaw(definition, namespace);
    //
    //        List<ISkinPartDefinition> result = new ArrayList<ISkinPartDefinition>();
    //        Map<String, ISkinPartDefinition> map = new HashMap<String, ISkinPartDefinition>();
    //        Iterator<ITypeDefinition> i = getIterator(definition);
    //        while (i.hasNext())
    //        {
    //            IClassDefinition sdefinition = (IClassDefinition) i.next();
    //            List<ISkinPartDefinition> list = getSkinPartsRaw(sdefinition,
    //                    namespace);
    //            for (ISkinPartDefinition member : list)
    //            {
    //                if (!map.containsKey(member.getBaseName()))
    //                {
    //                    result.add(member);
    //                    map.put(member.getBaseName(), member);
    //                }
    //            }
    //        }
    //        return result;
    //    }
    //
    //    @Override
    //    public List<ISkinStateDefinition> getSkinStates(
    //            IClassDefinition definition, String namespace, boolean inherit)
    //    {
    //        if (!inherit)
    //            return getSkinStatesRaw(definition, namespace);
    //
    //        List<ISkinStateDefinition> result = new ArrayList<ISkinStateDefinition>();
    //        Map<String, ISkinStateDefinition> map = new HashMap<String, ISkinStateDefinition>();
    //        Iterator<ITypeDefinition> i = getIterator(definition);
    //        while (i.hasNext())
    //        {
    //            IClassDefinition sdefinition = (IClassDefinition) i.next();
    //            List<ISkinStateDefinition> list = getSkinStatesRaw(sdefinition,
    //                    namespace);
    //            for (ISkinStateDefinition member : list)
    //            {
    //                if (!map.containsKey(member.getBaseName()))
    //                {
    //                    result.add(member);
    //                    map.put(member.getBaseName(), member);
    //                }
    //            }
    //        }
    //        return result;
    //    }
    //
    //    public void addEvent(IEventDefinition definition)
    //    {
    //        IClassDefinition parent = (IClassDefinition) definition
    //                .getDecoratedDefinition();
    //
    //        List<IEventDefinition> list = events.get(parent.getQualifiedName());
    //        if (list == null)
    //        {
    //            list = new ArrayList<IEventDefinition>();
    //            events.put(parent.getQualifiedName(), list);
    //        }
    //
    //        list.add(definition);
    //        addDefinition(definition);
    //    }
    //
    //    public void addStyle(IStyleDefinition definition)
    //    {
    //        IClassDefinition parent = (IClassDefinition) definition
    //                .getDecoratedDefinition();
    //
    //        List<IStyleDefinition> list = styles.get(parent.getQualifiedName());
    //        if (list == null)
    //        {
    //            list = new ArrayList<IStyleDefinition>();
    //            styles.put(parent.getQualifiedName(), list);
    //        }
    //
    //        list.add(definition);
    //        addDefinition(definition);
    //    }
    //
    //    public void addEffect(IEffectDefinition definition)
    //    {
    //        IClassDefinition parent = (IClassDefinition) definition
    //                .getDecoratedDefinition();
    //
    //        List<IEffectDefinition> list = effects.get(parent.getQualifiedName());
    //        if (list == null)
    //        {
    //            list = new ArrayList<IEffectDefinition>();
    //            effects.put(parent.getQualifiedName(), list);
    //        }
    //
    //        list.add(definition);
    //        addDefinition(definition);
    //    }
    //
    //    public void addSkinState(IMetaTag tag)
    //    {
    //        IClassDefinition parent = (IClassDefinition) tag
    //                .getDecoratedDefinition();
    //
    //        List<ISkinStateDefinition> list = skinstates.get(parent
    //                .getQualifiedName());
    //        if (list == null)
    //        {
    //            list = new ArrayList<ISkinStateDefinition>();
    //            skinstates.put(parent.getQualifiedName(), list);
    //        }
    //
    //        list.add(new SkinStateDefinition(tag.getValue(), tag.getTagName(),
    //                (IClassDefinition) tag.getDecoratedDefinition()));
    //        // addDefinition(definition);
    //    }
    //
    //    public void addSkinPart(IMetaTag tag)
    //    {
    //        IVariableDefinition member = (IVariableDefinition) tag
    //                .getDecoratedDefinition();
    //        IClassDefinition parent = (IClassDefinition) member.getParent();
    //
    //        List<ISkinPartDefinition> list = skinparts.get(parent
    //                .getQualifiedName());
    //        if (list == null)
    //        {
    //            list = new ArrayList<ISkinPartDefinition>();
    //            skinparts.put(parent.getQualifiedName(), list);
    //        }
    //
    //        list.add(new SkinPartDefinition(member.getBaseName(), tag.getTagName(),
    //                member));
    //        // addDefinition(definition);
    //    }

}
