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

package randori.compiler.codegen.js;

import org.apache.flex.compiler.tree.as.ITypeNode;

/**
 * The {@link IRandoriEmitter} interface allows abstraction between the base
 * JavaScript and the randori specific source code production.
 * 
 * @author Michael Schmalle
 */
public interface IRandoriEmitter extends IJSEmitter
{
    public static final String ANON_DELEGATE_NAME = "$createAnonDelegate";

    public static final String STATIC_DELEGATE_NAME = "$createStaticDelegate";

    /**
     * Returns the session model for the current session.
     */
    ISessionModel getModel();

    /**
     * Returns a qualified name for a class that is produced using the
     * <code>JavaScript</code> annotation export rules.
     * <p>
     * Note; All emitters must use this method to correctly resolve class names.
     * 
     * @param node The {@link ITypeNode} for which to return a resolved
     * qualified name.
     */
    String toQualifiedNameFromType(ITypeNode node);
}
