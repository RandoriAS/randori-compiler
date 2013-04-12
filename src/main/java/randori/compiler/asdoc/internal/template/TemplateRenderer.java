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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.flex.compiler.definitions.IDefinition;
import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.render.IRenderAware;
import randori.compiler.asdoc.template.ITemplateRenderer;

public abstract class TemplateRenderer implements ITemplateRenderer,
        IRenderAware
{
    private File templateBase;

    List<File> templateBases = new ArrayList<File>();

    public List<File> getTemplateBases()
    {
        return templateBases;
    }

    private File templateFile = null;

    private File outputBase = null;

    private File outputFile = null;

    private IDocConfiguration configuration;

    protected IDocConfiguration getConfiguration()
    {
        return configuration;
    }

    private IDefinition definition = null;

    private IPackageBundle bundle;

    // ---------------------------------
    // basePath
    // ---------------------------------

    private String basePath = "";

    /**
     * @return string
     */
    @Override
    public String getBasePath()
    {
        return basePath;
    }

    /**
     * @param string title
     */
    @Override
    public void setBasePath(String basePath)
    {
        this.basePath = basePath;
        set("basePath", basePath);
    }

    /**
     * @return string
     */
    @Override
    public IDefinition getDefinition()
    {
        return definition;
    }

    /**
     * @param string title
     */
    @Override
    public void setDefinition(IDefinition element)
    {
        this.definition = element;
        set("element", element);
    }

    @Override
    public IPackageBundle getBundle()
    {
        return bundle;
    }

    @Override
    public void setBundle(IPackageBundle bundle)
    {
        this.bundle = bundle;
        set("bundle", bundle);
    }

    // ----------------------------------
    // outputFile
    // ----------------------------------

    /**
     * The output file path/name relative to the output base.
     */
    public File getOutputFile()
    {
        return outputFile;
    }

    /**
     * @param outputFile the outputFile to set
     */
    public void setOutputFile(File outputFile)
    {
        this.outputFile = outputFile;
    }

    public void setOutputFile(String outputFile)
    {
        setOutputFile(new File(outputFile));
    }

    // ----------------------------------
    // outputBase
    // ----------------------------------

    /**
     * The output file base.
     */
    public File getOutputBase()
    {
        return outputBase;
    }

    /**
     * @param outputFile the outputFile to set
     */
    public void setOutputBase(File outputBase)
    {
        this.outputBase = outputBase;
    }

    // ----------------------------------
    // templateFile
    // ----------------------------------

    /**
     * @return the templateFile
     */
    @Override
    public File getTemplateFile()
    {
        return templateFile;
    }

    /**
     * @param templateFile the templateFile to set
     */
    @Override
    public void setTemplateFile(File templateFile)
    {
        this.templateFile = templateFile;
    }

    public void setTemplateFile(String templateFile)
    {
        setTemplateFile(new File(templateFile));
    }

    // ----------------------------------
    // templateBase
    // ----------------------------------

    /**
     * The template base that is used during renders
     */
    @Override
    public File getTemplateBase()
    {
        return templateBase;
    }

    /**
     * @param outputFile the templateBase to set
     */
    @Override
    public void setTemplateBase(File value)
    {
        templateBase = value;
    }

    @Override
    public void setTemplateBases(List<File> templateBases)
    {
        this.templateBases = templateBases;
    }

    // ---------------------------------
    // contentData
    // ---------------------------------

    private String contentData = null;

    /**
     * @return string
     */
    @Override
    public String getContentData()
    {
        return contentData;
    }

    /**
     * @param string title
     */
    public void setContentData(String contentData)
    {
        this.contentData = contentData;
        set("contentData", contentData);
    }

    public TemplateRenderer(IDocConfiguration config)
    {
        configuration = config;

        outputBase = getConfiguration().getOutputs().get(0);

        setTemplateBases(getConfiguration().getTemplateBases());
        // XXX TEMP
        setTemplateBase(getConfiguration().getTemplateBases().get(0));

    }

    /**
     * Initializes a TemplateDataProxy instance with this instances current
     * global variables.
     * 
     * @param template The ITemplateRenderer to initialize.
     */
    public void initialize(ITemplateRenderer template)
    {
        template.setDefinition(definition);
    }

    /**
     * Sets template variables on the context instance.
     * 
     * @param name The name of the template variable.
     * @param value The value of the template variable.
     */
    protected void set(String name, Object value)
    {
    }

    protected Object get(String name)
    {
        return null;
    }

    @Override
    public Object getProperty(String name)
    {
        return get(name);
    }

    @Override
    public void setProperty(String name, Object value)
    {
        set(name, value);
    }

    @Override
    public abstract String render();

    /**
     * Subclasses need to implement this method to add their template variables
     * into the template context.
     * <p>
     * The subclasses will return <code>true</code> to output the template or
     * <code>false</code> to just process and wait to render it's template.
     * </p>
     */
    abstract protected Boolean renderTemplate();

    /**
     * Writes the template data to disk.
     * 
     * @param file The location to write the template data.
     * @param content The string content to write into the file.
     * @param base ...
     */
    protected void writeFile(File file, String content, String base)
    {
        if (base == null)
        {
            base = outputBase.getPath();
        }

        String output = base + "/" + file.getPath();
        File outputFile = new File(output);

        if (!outputFile.getParentFile().exists())
        {
            outputFile.getParentFile().mkdirs();
        }

        try
        {
            // Tidy tidy = new Tidy();

            // TODO check into VelocityWriter()
            FileWriter writer = new FileWriter(outputFile);

            writer.write(content);
            writer.close();

            // BufferedInputStream is = new BufferedInputStream( new
            // FileInputStream( outputFile ) );
            // BufferedOutputStream os = new BufferedOutputStream( new
            // FileOutputStream( outputFile ) );

            // tidy.setTabsize( 4 );
            // tidy.setWraplen( 100000 );
            // tidy.setIndentContent( true );
            // tidy.parse( is, os );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Writes the template data to disk.
     * 
     * @param file The location to write the template data.
     * @param content The string content to write into the file.
     */
    protected void writeFile(File file, String content)
    {
        writeFile(file, content, null);
    }

    ////////////////////////////////////////////////////////////

    protected Integer getInteger(String name)
    {
        return (Integer) get(name);
    }

    protected String getString(String name)
    {
        return (String) get(name);
    }

    protected Float getFloat(String name)
    {
        return (Float) get(name);
    }
}
