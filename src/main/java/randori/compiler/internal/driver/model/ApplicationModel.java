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

package randori.compiler.internal.driver.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.utils.FilenameNormalization;

import randori.compiler.codegen.as.IASWriter;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.internal.config.MergedFileSettings;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.FileUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.projects.IRandoriApplicationProject;

/**
 * The compilation set for the base randori application project.
 * <p>
 * TODO There is going to be more logic needed in the class to determine
 * multiple projects? Still in the dark about how exactly the projects other
 * than the libraries can be setup.
 * 
 * @author Michael Schmalle
 */
public class ApplicationModel extends BaseCompilationSet
{

    public ApplicationModel(IRandoriApplicationProject project,
            IRandoriTargetSettings settings)
    {
        super(project, settings);
    }

    @Override
    protected boolean accept(ITypeNode node)
    {
        if (node == null)
            return false;

        final String qualifiedName = node.getQualifiedName();

        // -exclude-packages trumps all for now, if there are problems
        // with this the below logic can be altered
        for (String test : settings.getExcludePackages())
        {
            if (qualifiedName.startsWith(test))
                return false;
        }

        // check for files that are not in the main application
        // using the -js-merged-file
        for (MergedFileSettings mergedFile : settings.getMergedFileSettings())
        {
            for (String name : mergedFile.getQualifiedNames())
            {
                if (qualifiedName.equals(name))
                    return false;
            }
        }

        // no -include-sources everything gets included (-source-path)
        Collection<File> sources = settings.getIncludeSources();
        if (sources == null || sources.size() == 0)
            return true;

        // if incremental files have been passed, filter on them
        Collection<String> incrementals = settings.getIncrementalFiles();
        if (incrementals != null && incrementals.size() > 0)
        {
            String path = FilenameNormalization.normalize(node.getSourcePath());
            for (String filePath : incrementals)
            {
                if (FilenameUtils.equalsNormalized(path, filePath))
                    return true;
            }
            return false;
        }

        // filter out using the expanded -include-sources directory or files
        String path = FilenameNormalization.normalize(node.getSourcePath());
        for (File file : sources)
        {
            if (FilenameUtils.equalsNormalized(path, file.getAbsolutePath()))
                return true;
        }
        return false;
    }

    @Override
    public void generate(IRandoriBackend backend, File output)
    {
        super.generate(backend, output);

        // as you can see, we override generate to allow monolithic or individual
        // file export here using the superclasses methods to do the dirty work
        boolean classesAsFiles = settings.getJsClassesAsFiles();
        if (classesAsFiles)
        {
            for (ICompilationUnit unit : getCompilationUnits())
            {
                try
                {
                    write(unit);
                }
                catch (RuntimeException e)
                {
                    System.err
                            .println("Aborted compile in ApplicationModel.generate()");
                    break;
                }
            }
        }
        else
        {
            String basePath = settings.getJsBasePath();
            String appName = settings.getAppName();
            if (appName == null || appName.equals(""))
            {
                // TODO Create a Problem that app-name is not configured, this should actually
                // be done in the configure() method of the compiler
                throw new RuntimeException(
                        "no -app-name specified during monolithic generation");
            }

            writeFull(basePath, appName + ".js");
        }
    }

    /**
     * Writes an individual {@link ICompilationUnit} to file.
     * <p>
     * This method uses the unit's package name to calculate the directory
     * structure of the output class file.
     * 
     * @param unit The {@link ICompilationUnit} to output.
     */
    void write(ICompilationUnit unit)
    {
        String basePath = settings.getJsBasePath();

        File outputFolder = new File(outputDirectory, basePath);
        if (!outputFolder.exists())
            outputFolder.mkdirs();

        File outputClassFile = null;
        IASWriter writer = null;
        BufferedOutputStream out = null;

        // TODO this is a mess, all this output needs to be refactored into the build target

        try
        {
            IDefinition definition = DefinitionUtils
                    .getTopLevelDefinition(unit);
            final String qualifiedName = MetaDataUtils
                    .getExportQualifiedName(definition);

            outputClassFile = FileUtils.toOutputFile(qualifiedName,
                    outputFolder, "js");

            System.out.println("Compiling file: " + outputClassFile);

            writer = backend.createWriter(project, getProblems(), unit, false);

            out = new BufferedOutputStream(
                    new FileOutputStream(outputClassFile));

            writer.writeTo(out);
            out.flush();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e)
        {
            // XXX HACK not sure how to "not" create this file with the stream
            System.err.println("Compiling file failed  "
                    + outputClassFile.getName());
            e.printStackTrace();
            throw e;
        }
        finally
        {
            try
            {
                out.close();
                writer.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
