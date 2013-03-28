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

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;

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
    private HashMap<String, ITypeDefinition> dependencies = new HashMap<String, ITypeDefinition>();

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
    public boolean setInAssignment(boolean value)
    {
        return inAssignment = value;
    }

    private boolean skipOperator;

    @Override
    public boolean skipOperator()
    {
        return skipOperator;
    }

    @Override
    public boolean setSkipOperator(boolean value)
    {
        return skipOperator = value;
    }

    //--------------------------------------------------------------------------
    // Dependencies
    //--------------------------------------------------------------------------

    public void addDependency(ITypeDefinition definition)
    {
        if (dependencies.containsKey(definition.getQualifiedName()))
            return;

        // if this class is considered native, pass
        if (MetaDataUtils.isNative(definition))
            return;

        // if this class has export="false" pass
        //if (!MetaDataUtils.isExport(definition))
        //    return;
        if (!isExport(definition))
            return;

        dependencies.put(definition.getQualifiedName(), definition);
    }

    private boolean isExport(ITypeDefinition definition)
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
    public Collection<ITypeDefinition> getDependencies()
    {
        return dependencies.values();
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
}
