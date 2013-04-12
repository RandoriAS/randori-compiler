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

package randori.compiler.asdoc.internal.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import randori.compiler.asdoc.access.IASProjectAccess;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.access.MemberType;
import randori.compiler.asdoc.internal.template.asdoc.data.PrimaryLink;
import randori.compiler.asdoc.template.asdoc.IASDocConverter;
import randori.compiler.asdoc.template.asdoc.IASDocRowConverter;

public class DocConfiguration implements IDocConfiguration
{

    private IASProjectAccess access;

    private IASDocConverter convertor;

    private IASDocRowConverter rowConverter;

    private RandoriDocConfiguration configuration;

    @Override
    public void setConfiguration(RandoriDocConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void setConverter(IASDocConverter convertor)
    {
        this.convertor = convertor;
    }

    @Override
    public IASDocRowConverter getRowConverter()
    {
        return rowConverter;
    }

    @Override
    public void setRowConverter(IASDocRowConverter value)
    {
        rowConverter = value;
    }

    @Override
    public List<File> getOutputs()
    {
        List<File> result = new ArrayList<File>();
        result.add(new File("C:/Users/Work/Desktop/asdocs"));
        return result;
    }

    @Override
    public List<File> getTemplateBases()
    {
        List<File> result = new ArrayList<File>();
        result.add(new File(
                "C:/Users/Work/Documents/projects/apache/AFDocApplication/src/org/apache/flex/jasdoc/internal/template/asdoc/tpl"));
        return result;
    }

    @Override
    public int getLeftFrameWidth()
    {
        return 300;
    }

    public DocConfiguration()
    {
    }

    @Override
    public Boolean getFrames()
    {
        return true;
    }

    @Override
    public List<PrimaryLink> getPrimaryLinks()
    {
        // TODO Auto-generated method stub
        return new ArrayList<PrimaryLink>();
    }

    @Override
    public String getMainTitle()
    {
        return configuration.getMainTitle();
    }

    @Override
    public String getFooter()
    {
        return configuration.getFooter();
    }

    @Override
    public String getLogo()
    {
        return "images/logo.jpg";
    }

    @Override
    public IASProjectAccess getAccess()
    {
        return access;
    }

    @Override
    public void setAccess(IASProjectAccess value)
    {
        access = value;
    }

    @Override
    public IASDocConverter getConverter()
    {
        return convertor;
    }

    @Override
    public boolean isTagActive(String tagName)
    {
        if (tagName.equals("source"))
            return false;
        return true;
    }

    @Override
    public List<String> getDocNamespace()
    {
        return configuration.getDocNamespace();
    }

    @Override
    public List<MemberType> getDocMemberTypes()
    {
        return configuration.getDocMemberTypes();
    }

    @Override
    public boolean getRetainMemberOrder()
    {
        return configuration.getRetainMemberOrder();
    }

    @Override
    public List<File> getSourcePaths()
    {
        ArrayList<File> result = new ArrayList<File>();

        for (String path : configuration.getCompilerSourcePath())
        {
            result.add(new File(path));
        }

        return result;
    }

}
