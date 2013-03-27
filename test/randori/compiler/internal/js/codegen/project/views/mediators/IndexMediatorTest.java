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

package randori.compiler.internal.js.codegen.project.views.mediators;

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

import randori.compiler.internal.js.codegen.project.RandoriTestProjectBase;

/**
 * @author Michael Schmalle
 */
public class IndexMediatorTest extends RandoriTestProjectBase
{
    @Test
    public void test_constructor()
    {
        IFunctionNode node = findFunction("IndexMediator", classNode);
        asBlockWalker.visitFunction(node);
        assertOut("mediators.IndexMediator = function() {"
                + "\n\tthis.viewStack = null;\n\tthis.menu = null;\n\t"
                + "randori.behaviors.AbstractMediator.call(this);\n}");
    }

    @Test
    public void test_onRegister()
    {
        IFunctionNode node = findFunction("onRegister", classNode);
        asBlockWalker.visitFunction(node);
        assertOut("mediators.IndexMediator.prototype.onRegister = function() {"
                + "\n\tvar menuItems = [{name:\"Targets\", url:\"views\\/targets.html\"}, "
                + "{name:\"Labs\", url:\"views\\/labs.html\"}, {name:\"Intel\", "
                + "url:\"views\\/intel.html\"}];\n\tthis.menu.menuItemSelected."
                + "add($createStaticDelegate(this, this.menuItemSelected));\n\t"
                + "this.menu.set_data(menuItems);\n}");
    }

    @Test
    public void test_menuItemSelected()
    {
        IFunctionNode node = findFunction("menuItemSelected", classNode);
        asBlockWalker.visitFunction(node);
        assertOut("mediators.IndexMediator.prototype.menuItemSelected = function(menuData) {"
                + "\n\tthis.viewStack.popView();\n\tvar promise = this.viewStack.pushView("
                + "menuData.url);\n\tpromise.then(function(result) {\n\t});\n}");
    }

    @Test
    public void test_file()
    {
        asBlockWalker.visitFile(fileNode);
    }

    protected String getBasePath()
    {
        return "C:\\Users\\Work\\Documents\\git\\RandoriAS\\DemoApplication\\src";
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "mediators.IndexMediator";
    }
}
