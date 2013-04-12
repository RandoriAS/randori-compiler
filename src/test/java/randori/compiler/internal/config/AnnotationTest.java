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

package randori.compiler.internal.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import randori.compiler.config.IAnnotationDefinition;

/**
 * @author Michael Schmalle
 */
public class AnnotationTest extends AnnotationTestBase
{

    @Override
    @Before
    public void setUp()
    {
        super.setUp();
        compile();
    }

    @Override
    @After
    public void tearDown() throws IOException
    {
        super.tearDown();
    }

    @Override
    protected void initializeArgs()
    {
        super.initializeArgs();
        getArgs().addSourcepath(srcAnnotationCore.getAbsolutePath());
        //getArgs().addSourcepath(srcAnnotation.getAbsolutePath());
    }

    //--------------------------------------------------------------------------
    // Annotation Definitions
    //--------------------------------------------------------------------------

    @Test
    public void test_Annotation()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.Annotation");
        assertEquals("Annotation", definition.getBaseName());
        assertEquals("randori.annotations.Annotation",
                definition.getQualifiedName());
    }

    @Test
    public void test_Retention()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.Retention");
        assertEquals("Retention", definition.getBaseName());
        assertEquals("randori.annotations.Retention",
                definition.getQualifiedName());
    }

    @Test
    public void test_Target()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.Target");
        assertEquals("Target", definition.getBaseName());
        assertEquals("randori.annotations.Target",
                definition.getQualifiedName());
    }

    @Test
    public void test_JavaScript()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.JavaScript");

        assertNotNull(definition);
        assertEquals("JavaScript", definition.getBaseName());
        assertEquals("randori.annotations.JavaScript",
                definition.getQualifiedName());
    }

    //--------------------------------------------------------------------------
    // Annotation Attributes
    //--------------------------------------------------------------------------

    @Test
    public void test_JavaScript_validTarget()
    {
        IAnnotationDefinition definition = getTargetSettings()
                .getAnnotationManager().getDefinition(
                        "randori.annotations.JavaScript");

        assertEquals(1, definition.getTargets().size());
        assertTrue(definition.isValidTarget(IAnnotationDefinition.TARGET_CLASS));
        assertFalse(definition.isValidTarget(IAnnotationDefinition.TARGET_ALL));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_CONSTRUCTOR));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_FIELD));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_INTERFACE));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_METHOD));
        assertFalse(definition
                .isValidTarget(IAnnotationDefinition.TARGET_PROPERTY));
    }

}
