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

package randori.compiler.internal.codegen.js;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.flex.compiler.definitions.*;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.internal.scopes.TypeScope;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.ITypeNode;

import randori.compiler.codegen.js.ISessionModel;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.MetaDataUtils.MetaData;

/**
 * The default implementation of {@link ISessionModel}.
 * 
 * @author Michael Schmalle
 */
public class SessionModel implements ISessionModel
{
    private HashMap<String, IAccessorDefinition> getterSetterProperties = new HashMap<String, IAccessorDefinition>();

    private HashMap<String, IScopedDefinition> runtimeDependencies = new HashMap<String, IScopedDefinition>();

    private HashMap<String, IScopedDefinition> staticDependencies = new HashMap<String, IScopedDefinition>();

    private List<IMetaTag> propertyInjectTags = new ArrayList<IMetaTag>();

    private List<IMetaTag> methodInjectTags = new ArrayList<IMetaTag>();

    private List<IMetaTag> viewInjectTags = new ArrayList<IMetaTag>();

    private boolean inAssignment;

    @Override
    public boolean isInAssignment()
    {
        return inAssignment;
    }

    @Override
    public void setInAssignment(boolean value)
    {
        inAssignment = value;
        assign = null;
    }

    private IBinaryOperatorNode assign;

    @Override
    public IBinaryOperatorNode getAssign()
    {
        return assign;
    }

    @Override
    public void setAssign(IBinaryOperatorNode value)
    {
        assign = value;
    }

    private boolean skipOperator;

    @Override
    public boolean skipOperator()
    {
        return skipOperator;
    }

    @Override
    public void setSkipOperator(boolean value)
    {
        skipOperator = value;
    }

    private boolean inArguments;

    @Override
    public boolean isInArguments()
    {
        return inArguments;
    }

    @Override
    public void setInArguments(boolean value)
    {
        inArguments = value;
    }

    private boolean inScope;

    @Override
    public boolean isInScope()
    {
        return inScope;
    }

    @Override
    public void setInScope(boolean value)
    {
        inScope = value;
    }

    //--------------------------------------------------------------------------
    // Dependencies
    //--------------------------------------------------------------------------


    public void addGetterSetter(IAccessorDefinition definition)
    {
        if (getterSetterProperties.containsKey(definition.getQualifiedName()))
            return;

        getterSetterProperties.put(definition.getQualifiedName(), definition);
    }

    public Collection<IAccessorDefinition> getGetterSetter()
    {
        return getterSetterProperties.values();
    }

    @Override
    public void addDependency(IScopedDefinition definition, IASNode node)
    {
        // do not allow interfaces
        //if (definition instanceof IInterfaceDefinition)
        //    return;

        //if (definition instanceof ClassTraitsDefinition)
        //{
        //    ITypeNode inode = ((ClassTraitsDefinition) definition).getNode();
        //    if (inode instanceof IInterfaceNode)
        //        return;
        //}

        ITypeNode type = (ITypeNode) node.getAncestorOfType(ITypeNode.class);
        if (type != null)
        {
            if (definition == type.getDefinition())
                return;
        }

        // do not allow private inner classes
        if (definition instanceof IClassDefinition && definition.isPrivate())
            return;

        // if this class is considered native, pass
        if (MetaDataUtils.isNative(definition))
            return;

        // if this class has export="false" pass
        if (!isExport(definition))
            return;

        if (node.getContainingScope().getScope() instanceof TypeScope)
        {
            addStaticDependency(definition);
        }
        else
        {
            addRuntimeDependency(definition);
        }
    }

    void addRuntimeDependency(IScopedDefinition definition)
    {
        if (runtimeDependencies.containsKey(definition.getQualifiedName()))
            return;

        runtimeDependencies.put(definition.getQualifiedName(), definition);
    }

    void addStaticDependency(IScopedDefinition definition)
    {
        if (staticDependencies.containsKey(definition.getQualifiedName()))
            return;

        staticDependencies.put(definition.getQualifiedName(), definition);
    }

    private boolean isExport(IScopedDefinition definition)
    {
        IMetaTag tag = MetaDataUtils.findTag(definition, MetaData.JavaScript);
        if (tag != null)
        {
            // only if the tag has export="false" will we return false
            String value = tag.getAttributeValue("export");
            if (value != null && value.equals("false"))
                return false;
        }

        return true;
    }

    @Override
    public Collection<IScopedDefinition> getRuntimeDependencies()
    {
        return runtimeDependencies.values();
    }

    @Override
    public Collection<IScopedDefinition> getStaticDependencies()
    {
        return staticDependencies.values();
    }

    //--------------------------------------------------------------------------
    // Injections
    //--------------------------------------------------------------------------

    @Override
    public Collection<IMetaTag> getPropertyInjections()
    {
        return Collections.unmodifiableCollection(propertyInjectTags);
    }

    @Override
    public Collection<IMetaTag> getMethodInjections()
    {
        return Collections.unmodifiableCollection(methodInjectTags);
    }

    @Override
    public Collection<IMetaTag> getViewInjections()
    {
        return Collections.unmodifiableCollection(viewInjectTags);
    }

    @Override
    public void addInjection(IDefinition definition)
    {
        IMetaTag tag = MetaDataUtils.getInjectTag(definition);
        if (tag == null)
            return;

        if (definition instanceof IVariableDefinition)
        {
            if (!propertyInjectTags.contains(tag))
                propertyInjectTags.add(tag);
        }
        else if (definition instanceof IFunctionDefinition)
        {
            if (!methodInjectTags.contains(tag))
                methodInjectTags.add(tag);
        }
    }

    @Override
    public void addViewInjection(IDefinition definition)
    {
        IMetaTag tag = MetaDataUtils.getViewTag(definition);
        if (tag == null)
            return;

        // the IAccessorDefinition is a variable, so accessors get in here as well
        if (definition instanceof IVariableDefinition)
        {
            if (!viewInjectTags.contains(tag))
                viewInjectTags.add(tag);
        }
    }

    private boolean isCall;

    @Override
    public void setCall(boolean value)
    {
        isCall = value;
    }

    @Override
    public boolean isCall()
    {
        return isCall;
    }

}
