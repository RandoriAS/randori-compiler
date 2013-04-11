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

package randori.compiler.asdoc.internal.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.template.ITemplateRenderer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

public abstract class VelocityTemplate extends TemplateRenderer
{
    private Boolean rendered = false;

    private String templateData = null;

    static Boolean VELOCITY_INITIALIZED = false;

    private VelocityContext data;

    private static Properties properties;

    /**
     * Sets template variables on the context instance.
     * 
     * @param name The name of the template variable.
     * @param value The value of the template variable.
     */
    @Override
    protected void set(String name, Object value)
    {
        if (value instanceof ITemplateRenderer)
        {
            // if we are putting a ITemplateRenderer instance into the
            // map, render it's string and place it in the map
            ITemplateRenderer template = (ITemplateRenderer) value;
            // TODO is initialize() necessary here?
            initialize(template);
            String data = template.render();
            getContext().put(name, data);
        }
        else
        {
            getContext().put(name, value);
        }
    }

    @Override
    protected Object get(String name)
    {
        return getContext().get(name);
    }

    public VelocityTemplate(IDocConfiguration config)
    {
        super(config);

        data = new VelocityContext();

        if (VELOCITY_INITIALIZED)

            return;

        properties = new Properties();
        properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
                toBasesString());
        try
        {
            Velocity.init(properties);
        }
        catch (Exception e)
        {
            // - Log.error("TemplateRenderer Velocity.init() Exception");
        }

        VELOCITY_INITIALIZED = true;
    }

    /**
     * Returns the VelocityContext for this template.
     */
    VelocityContext getContext()
    {
        return data;
    }

    @Override
    public String render()
    {
        if (rendered)
            return templateData;

        rendered = true;

        Boolean doWrite = renderTemplate();

        templateData = fetch(getTemplateFile());

        if (doWrite)
        {
            writeFile(getOutputFile(), templateData);
        }

        return templateData;
    }

    /**
     * Fetches a template and merges the template variables into the template
     * returning the final merged string data.
     * 
     * @param templateFile The template file used to merge.
     */
    protected String fetch(File templateFile)
    {
        Template template = null;

        try
        {
            template = Velocity.getTemplate(templateFile.getPath());
        }
        catch (ResourceNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ParseErrorException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        StringWriter writer = new StringWriter();
        // Log.debug(templateFile.getPath());
        try
        {
            template.merge(getContext(), writer);
        }
        catch (ResourceNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ParseErrorException e)
        {
            e.printStackTrace();
        }
        catch (MethodInvocationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return writer.toString();
    }

    @Override
    protected abstract Boolean renderTemplate();

    private String toBasesString()
    {
        StringBuffer sb = new StringBuffer();

        int i = 0;
        int len = getTemplateBases().size();
        for (File base : getTemplateBases())
        {
            sb.append(base.getAbsolutePath());
            if (i < len - 1)
                sb.append(", ");
            i++;
        }

        return sb.toString();
    }
}
