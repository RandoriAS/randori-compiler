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

import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.tree.as.ITypeNode;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriBackend;

/**
 * The compilation set for the base Randori library project.
 * 
 * @author Michael Schmalle
 */
public class RandoriModel extends BaseCompilationSet
{
    private static final String RANDORI_JS = "Randori.js";

    private static final String RANDORI = "randori";

    public RandoriModel(FlexProject project, IRandoriTargetSettings settings)
    {
        super(project, settings);
    }

    @Override
    protected boolean accept(ITypeNode node)
    {
        return node != null && node.getQualifiedName().startsWith(RANDORI);
    }

    @Override
    public void generate(IRandoriBackend backend, File output)
    {
        super.generate(backend, output);

        if (getCompilationUnits().size() == 0)
            return;

        String libraryPath = settings.getJsLibraryPath();
        writeFull(libraryPath, RANDORI_JS);
    }
}
