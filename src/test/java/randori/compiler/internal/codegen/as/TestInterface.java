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

package randori.compiler.internal.codegen.as;

import org.apache.flex.compiler.tree.as.IInterfaceNode;
import org.junit.Test;

import randori.compiler.internal.ASTestBase;

/**
 * This class tests the production of valid ActionScript3 code for Interface
 * production.
 * 
 * @author Michael Schmalle
 */
public class TestInterface extends ASTestBase
{
    //--------------------------------------------------------------------------
    // Interface
    //--------------------------------------------------------------------------

    @Test
    public void testSimple()
    {
        IInterfaceNode node = (IInterfaceNode) getNode("public interface IA{}",
                IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA {\n}");
    }

    @Test
    public void testSimpleExtends()
    {
        IInterfaceNode node = (IInterfaceNode) getNode(
                "public interface IA extends IB{}", IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA extends IB {\n}");
    }

    @Test
    public void testSimpleExtendsMultiple()
    {
        IInterfaceNode node = (IInterfaceNode) getNode(
                "public interface IA extends IB, IC, ID {}",
                IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA extends IB, IC, ID {\n}");
    }

    @Test
    public void testQualifiedExtendsMultiple()
    {
        IInterfaceNode node = (IInterfaceNode) getNode(
                "public interface IA extends foo.bar.IB, baz.goo.IC, foo.ID {}",
                IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA extends foo.bar.IB, baz.goo.IC, foo.ID {\n}");
    }

    @Test
    public void testAccessors()
    {
        IInterfaceNode node = (IInterfaceNode) getNode("public interface IA {"
                + "function get foo1():Object;"
                + "function set foo1(value:Object):void;}",
                IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA {\n\tfunction get foo1():Object;\n\t"
                + "function set foo1(value:Object):void;\n}");
    }

    @Test
    public void testMethods()
    {
        IInterfaceNode node = (IInterfaceNode) getNode("public interface IA {"
                + "function foo1():Object;"
                + "function foo1(value:Object):void;}", IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA {\n\tfunction foo1():Object;\n\t"
                + "function foo1(value:Object):void;\n}");
    }

    @Test
    public void testAccessorsMethods()
    {
        IInterfaceNode node = (IInterfaceNode) getNode("public interface IA {"
                + "function get foo1():Object;"
                + "function set foo1(value:Object):void;"
                + "function baz1():Object;"
                + "function baz2(value:Object):void;}", IInterfaceNode.class);
        visitor.visitInterface(node);
        assertOut("public interface IA {\n\tfunction get foo1():Object;"
                + "\n\tfunction set foo1(value:Object):void;\n\tfunction baz1()"
                + ":Object;\n\tfunction baz2(value:Object):void;\n}");
    }
}
