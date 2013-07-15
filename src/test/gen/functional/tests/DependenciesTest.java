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

package functional.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.flex.compiler.definitions.IScopedDefinition;
import org.junit.Test;

import randori.compiler.internal.utils.RandoriUtils;

public class DependenciesTest extends FunctionalTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);

        List<String> rdps = new ArrayList<String>();
        rdps.add("*demo.foo.support.IA");
        rdps.add("demo.foo.support.trace");
        rdps.add("demo.foo.support.AnotherStaticClass");
        rdps.add("demo.foo.support.StaticClass8");
        rdps.add("demo.foo.support.SupportClassA");
        rdps.add("demo.foo.support.Static2");
        rdps.add("demo.foo.support.Mode1");
        rdps.add("demo.foo.support.globalFunc");
        rdps.add("demo.foo.support.StaticClass5");
        rdps.add("demo.foo.support.StaticClass6");
        rdps.add("demo.foo.support.ParticleAssets");
        rdps.add("demo.foo.support.StaticClass7");
        rdps.add("demo.foo.support.Bar");

        Assert.assertEquals(rdps.size(), getEmitter().getModel()
                .getRuntimeDependencies().size());

        int i = 0;
        for (IScopedDefinition definition : getEmitter().getModel()
                .getRuntimeDependencies())
        {
            Assert.assertEquals(rdps.get(i),
                    RandoriUtils.toDependencyName(definition));
            i++;
        }

        Assert.assertEquals(4, getEmitter().getModel().getStaticDependencies()
                .size());
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.Dependencies";
    }
}