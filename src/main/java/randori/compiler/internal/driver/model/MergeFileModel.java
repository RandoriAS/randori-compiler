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

import java.io.File;

import org.apache.flex.compiler.tree.as.ITypeNode;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.internal.config.MergedFileSettings;
import randori.compiler.projects.IRandoriApplicationProject;

/**
 * @author Michael Schmalle
 */
public class MergeFileModel extends BaseCompilationSet
{

    private final MergedFileSettings mergeFile;

    public MergeFileModel(IRandoriApplicationProject project,
            IRandoriTargetSettings settings, MergedFileSettings mergeFile)
    {
        super(project, settings);
        this.mergeFile = mergeFile;
    }

    @Override
    public void generate(IRandoriBackend backend, File output)
    {
        super.generate(backend, output);

        String basePath = settings.getJsBasePath();
        String fileName = mergeFile.getFileName();
        if (fileName == null || fileName.equals(""))
        {
            throw new RuntimeException(
                    "no fileName specified during monolithic merge");
        }

        writeFull(basePath, fileName);
    }

    @Override
    protected boolean accept(ITypeNode node)
    {
        if (node == null)
            return false;

        final String qualifiedName = node.getQualifiedName();

        for (String name : mergeFile.getQualifiedNames())
        {
            if (qualifiedName.equals(name))
                return true;
        }

        return false;
    }
}
