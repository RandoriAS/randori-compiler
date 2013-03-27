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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.flex.compiler.definitions.ITypeDefinition;

/**
 * @author Michael Schmalle
 */
public class FileUtils
{
    public static String readFileAsString(String filePath) throws IOException
    {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static void writeFile(String filePath, String data)
    {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(data);
        }
        catch (IOException e)
        {
        }
        finally
        {
            try
            {
                if (writer != null)
                    writer.close();
            }
            catch (IOException e)
            {
            }
        }
    }

    public static File toOutputFile(String qualifiedName, File outputFolder,
            String extension)
    {
        String head = returnFullPath(qualifiedName, extension);
        File targetFile = new File(outputFolder, head);
        if (!targetFile.getParentFile().exists())
            targetFile.getParentFile().mkdirs();
        return targetFile;
    }

    public static String returnFullPath(String qualifiedName, String extension)
    {
        return convertToFilePath(qualifiedName) + "." + extension;
    }

    public static String returnFullPath(ITypeDefinition element,
            String extension)
    {
        return convertToFilePath(element.getQualifiedName()) + "." + extension;
    }

    private static String convertToFilePath(String qualfiedName)
    {
        return qualfiedName.replace(".", File.separator);
    }
}
