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

package randori.compiler.internal.config.annotation;

import java.io.IOException;

import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.config.annotation.IAnnotationManager;
import randori.compiler.internal.visitor.as.ASWalker;
import randori.compiler.plugin.IPreProcessAnnotationPlugin;

/**
 * @author Michael Schmalle
 */
public class PreProcessAnnotationPlugin implements IPreProcessAnnotationPlugin
{

    private ASWalker walker;

    @Override
    public void process(ICompilationUnit unit, IAnnotationManager manager)
    {
        walker = new ASWalker(new AnnotationVisitor(manager));
        try
        {
            walker.walkCompilationUnit(unit);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
