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

package randori.compiler.asdoc.internal.renderer;

import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.access.IASProjectAccess;
import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.render.IConvertor;

/**
 * @author Michael Schmalle
 */
public class Convertor implements IConvertor
{
    private IPackageBundle renderContext;

    private ITypeDefinition renderElement;
    
    private IDocConfiguration config;

    @Override
    public IASProjectAccess getAccess()
    {
        return config.getAccess();
    }

    @Override
    public IPackageBundle getPackageContext()
    {
        return renderContext;
    }

    @Override
    public void setPackageContext(IPackageBundle element)
    {
        renderContext = element;
    }

    @Override
    public String getPackageContextString()
    {
        if (renderContext != null)
            return renderContext.getQualifiedName();
        return "";
    }

    @Override
    public ITypeDefinition getRenderType()
    {
        return renderElement;
    }

    @Override
    public void setRenderType(ITypeDefinition context)
    {
        renderElement = context;
    }

    @Override
    public String getRenderTypeAsString()
    {
        // XXX OPTIMIZE AND CACHE
        if (renderElement != null)
            return renderElement.getQualifiedName();
        return "";
    }

    public Convertor(IDocConfiguration config)
    {
        this.config = config;
    }
}
