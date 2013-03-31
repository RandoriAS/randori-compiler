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

import java.io.File;
import java.io.IOException;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.internal.definitions.AppliedVectorDefinition;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.metadata.IMetaTagNode;

import randori.compiler.internal.utils.MetaDataUtils.MetaData.Mode;
import randori.compiler.internal.utils.NativeUtils.NativeType;

/**
 * Possible metadata;
 * 
 * <ul>
 * <li>
 * [JavaScript(export="true|false",mode=[global,prototype,json],name=[String]
 * ,nativecondition="[String])]</li>
 * <li>[JavaScriptProperty()]</li>
 * <li>[JavaScriptMethod()]</li>
 * <li></li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public class MetaDataUtils
{

    public enum MetaData
    {
        JavaScript("JavaScript"),

        JavaScriptProperty("JavaScriptProperty"),

        JavaScriptMethod("JavaScriptMethod"),

        JavaScriptConstructor("JavaScriptConstructor"),

        JavaScriptCode("JavaScriptCode"),

        View("View"), // required(Boolean)

        Inject("Inject");

        private final String name;

        public final String getName()
        {
            return name;
        }

        MetaData(String name)
        {
            this.name = name;
        }

        public enum Mode
        {
            GLOBAL, PROTOTYPE, JSON;
        }
    }

    public static final String TAG_NATIVE = "native";

    public static final String ATT_EXPORT = "export";

    public static final String ATT_FILE = "file";

    public static final String ATT_MODE = "mode";

    public static final String ATT_NAME = "name";

    public static final String VALUE_TRUE = "true";

    public static final String VALUE_FALSE = "false";

    public static final String VALUE_GLOBAL = "global";

    public static final String VALUE_JSON = "json";

    public static final Mode getMode(IDefinition definition)
    {
        IMetaTag tag = findTag(definition, metaDataFor(definition));
        if (tag == null)
            return Mode.PROTOTYPE;

        String value = tag.getAttributeValue(ATT_MODE);
        if (value == null)
            return Mode.PROTOTYPE;

        if (value.equals("global"))
        {
            return Mode.GLOBAL;
        }
        else if (value.equals(VALUE_JSON))
        {
            return Mode.JSON;
        }
        else
        {
            return Mode.PROTOTYPE;
        }
    }

    public static boolean isProtoExport(IDefinition definition)
    {
        IMetaTag tag = findTag(definition, MetaData.JavaScript);
        if (tag != null)
        {
            // [JavaScript(export="true", mode="global", name="Object")]
            final String exportValue = tag.getAttributeValue(ATT_EXPORT);
            final String modeValue = tag.getAttributeValue(ATT_MODE);
            if (exportValue == null && modeValue == null)
                return true;
            if (exportValue == null && !modeValue.equals(VALUE_GLOBAL)
                    && !modeValue.equals(VALUE_JSON))
                return true;

            if (exportValue != null && modeValue != null)
            {
                if (!modeValue.equals(VALUE_GLOBAL)
                        && !modeValue.equals(VALUE_JSON))
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns whether a {@link IClassDefinition} is an exported class.
     * <p>
     * If the class has a JavaScript tag and it's export is <code>true</code>,
     * if the {@link IClassDefinition} has no JavaScript tag
     * 
     * @param definition
     * @return
     */
    public static final boolean isClassExport(IClassDefinition definition)
    {
        if (definition == null)
            return false;

        IMetaTag tag = findTag(definition, MetaData.JavaScript);
        // no JavaScript tag; is export
        if (tag == null)
            return true;

        // export="false"
        final String value = tag.getAttributeValue(ATT_EXPORT);
        if (value != null && value.equals(VALUE_FALSE))
            return false;

        // all other possibilities; export true
        return true;
    }

    /**
     * Returns true if the {@link IDefinition} will be exported to javascript
     * source code.
     * 
     * @param definition The definition to check for export.
     */
    public static final boolean isExport(IDefinition definition)
    {
        IClassDefinition type = DefinitionUtils.getClassDefinition(definition);
        if (type == null)
            return false;

        // if the type is native, no export
        if (isNative(type))
            return false;

        IMetaTag tag = findTag(type, MetaData.JavaScript);
        if (tag != null)
        {
            // only if the tag has export="false" will we return false
            String value = tag.getAttributeValue(ATT_EXPORT);
            if (value != null && value.equals(VALUE_FALSE))
                return false;
        }

        return true;
    }

    public static final boolean isNative(IDefinition definition)
    {
        IClassDefinition type = DefinitionUtils.getClassDefinition(definition);
        if (type == null)
            return false;

        return isNative(type);
    }

    public static final boolean isNative(IClassDefinition definition)
    {
        if (definition == null)
            return false;

        IMetaTag tag = findTag(definition, MetaData.JavaScript);
        if (tag != null)
        {
            // only if the tag has export="false" will we return true
            String value = tag.getAttributeValue(ATT_EXPORT);
            if (value != null && value.equals(VALUE_FALSE))
                return true;
        }

        if (definition instanceof AppliedVectorDefinition)
            return true;

        tag = definition.getMetaTagByName(TAG_NATIVE);
        if (tag != null)
            return true;

        return false;
    }

    public static final IMetaTagNode findMetaTag(IDefinitionNode node,
            String name)
    {
        if (node.getMetaTags() == null)
            return null;
        return node.getMetaTags().getTagByName(name);
    }

    public static final IMetaTagNode[] findMetaTags(IDefinitionNode node,
            String name)
    {
        return node.getMetaTags().getTagsByName(name);
    }

    public static String getAccessorName(IAccessorDefinition definition,
            ICompilerProject project)
    {
        String name = definition.getBaseName();
        IMetaTag tag = definition.getMetaTagByName(MetaData.JavaScriptProperty
                .getName());
        if (tag == null)
        {
            IAccessorDefinition other = definition
                    .resolveCorrespondingAccessor(project);
            if (other == null)
                return name;
            tag = other.getMetaTagByName(MetaData.JavaScriptProperty.getName());
            if (tag == null)
                return name;
        }

        String value = tag.getAttributeValue(ATT_NAME);
        if (value != null)
            return value;

        return name;
    }

    public static String getFunctionName(IFunctionDefinition definition)
    {
        String name = definition.getBaseName();
        IMetaTag tag = definition.getMetaTagByName(MetaData.JavaScriptMethod
                .getName());
        if (tag == null)
            return name;

        String value = tag.getAttributeValue(ATT_NAME);
        if (value != null)
            return value;

        return name;
    }

    public static String getExportName(ITypeDefinition definition)
    {
        if (definition instanceof AppliedVectorDefinition)
            return NativeType.Array.getValue();
        if (definition.getBaseName().equals(NativeType.unit.getValue()))
            return NativeType._int.getValue();
        if (definition.getBaseName().equals(NativeType.Any.getValue()))
            return NativeType.Object.getValue();

        if (!isNative(definition))
            return definition.getQualifiedName();

        IMetaTag tag = findTag(definition, MetaData.JavaScript);
        if (tag == null)
            return definition.getQualifiedName();
        return tag.getAttributeValue(ATT_NAME);
    }

    public static String findJavaScriptCodeTag(IMetaTagNode node)
    {
        String path = node.getSourcePath();
        File file = new File(path).getParentFile();
        String reletivePath = node.getAttributeValue(ATT_FILE);
        File open = new File(file, reletivePath);
        String result = "";
        try
        {
            result = FileUtils.readFileAsString(open.getAbsolutePath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;

    }

    public static String findJavaScriptCodeTag(IFunctionNode node)
    {
        IFunctionDefinition definition = node.getDefinition();
        IMetaTag tag = definition.getMetaTagByName(MetaData.JavaScriptCode
                .getName());
        if (tag == null)
            return null;

        String path = node.getSourcePath();
        File file = new File(path).getParentFile();
        String reletivePath = tag.getAttributeValue(ATT_FILE);
        File open = new File(file, reletivePath);
        String result = "";
        try
        {
            result = FileUtils.readFileAsString(open.getAbsolutePath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isGlobal(IClassNode node)
    {
        if (node == null)
            return false;

        IMetaTag tag = findTag(node.getDefinition(), MetaData.JavaScript);
        if (tag == null)
            return false;

        String value = tag.getAttributeValue(ATT_MODE);
        if (value != null && value.equals(VALUE_GLOBAL))
            return true;

        return false;
    }

    /**
     * Returns the {@link MetaData} for the {@link IDefinition} type.
     * 
     * @param definition The definition that could contain metadata.
     */
    public static final MetaData metaDataFor(IDefinition definition)
    {
        MetaData tagName = null;
        if (definition instanceof IClassDefinition)
        {
            return MetaData.JavaScript;
        }
        else if (definition instanceof IAccessorDefinition)
        {
            return MetaData.JavaScriptProperty;
        }
        else if (definition instanceof IFunctionDefinition)
        {
            return MetaData.JavaScriptMethod;
        }
        return tagName;
    }

    /**
     * Returns whether the {@link IFunctionDefinition} contains the
     * {@link MetaData#JavaScriptCode} metadata.
     * 
     * @param definition The {@link IFunctionDefinition} to check.
     */
    public static final boolean hasJavaScriptCode(IFunctionDefinition definition)
    {
        IMetaTag tag = findTag(definition, MetaData.JavaScriptCode);
        if (tag != null)
            return true;
        return false;
    }

    public static final IMetaTag findTag(IDefinition definition,
            MetaData metaData)
    {
        return definition.getMetaTagByName(metaData.getName());
    }

    public static IMetaTag findJavaScriptTag(IDefinition definition)
    {
        return findTag(definition, MetaData.JavaScript);
    }

    public static boolean hasJavaScriptTag(IClassDefinition definition)
    {
        return findTag(definition, MetaData.JavaScript) != null;
    }

    public static Mode getModeDefaultJson(IClassDefinition definition)
    {
        IMetaTag tag = findTag(definition, metaDataFor(definition));
        if (tag == null)
            return Mode.PROTOTYPE;

        String value = tag.getAttributeValue(ATT_MODE);
        if (value == null)
            return Mode.JSON;

        if (value.equals("global"))
        {
            return Mode.GLOBAL;
        }
        else if (value.equals(VALUE_JSON))
        {
            return Mode.JSON;
        }
        else
        {
            return Mode.PROTOTYPE;
        }
    }

    public static IMetaTag getViewTag(IDefinition definition)
    {
        return findTag(definition, MetaData.View);
    }

    public static IMetaTag getInjectTag(IDefinition definition)
    {
        return findTag(definition, MetaData.Inject);
    }

}
