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

package randori.compiler.internal.codegen.js.emitter;

import java.util.Collection;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * Handles the production of the specialized footer Randori requires for;
 * 
 * <ul>
 * <li>Class Inheritance</li>
 * <li>Static ClassName assignment</li>
 * <li>Class dependency tracking</li>
 * </ul>
 * <strong>Injection Points</strong>
 * <ul>
 * <li>Constructor, Field and Method Injection</li>
 * <li>View Injection</li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public class FooterEmitter extends BaseSubEmitter implements
        ISubEmitter<IClassNode>
{
    // Constructor[0], Property[1], Method[2], View[3]

    private static final String INHERIT_NAME = "$inherit";

    public FooterEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    @Override
    public void emit(IClassNode node)
    {
        emitInherit(node);
        emitClassName(node);
        emitGetClassDependencies(node);
        emitInjectionPoints(node);
        emitLast(node);
    }

    void emitInherit(IClassNode node)
    {
        // $Inherit(foo.bar.Baz, foo.bar.SuperClass);
        if (node.getBaseClassName() == null)
            return;

        final String baseClassName = toBaseQualifiedName(node.getDefinition());
        if (baseClassName.equals("Object"))
            return;

        final String qualifiedName = MetaDataUtils.getExportQualifiedName(node
                .getDefinition());

        write(INHERIT_NAME);
        write("(");
        write(qualifiedName);
        write(", ");

        write(baseClassName);
        writeNewline(");");
        writeNewline();
    }

    void emitClassName(IClassNode node)
    {
        // foo.bar.Baz.className = "foo.bar.Baz";
        final String qualifiedName = MetaDataUtils.getExportQualifiedName(node
                .getDefinition());

        write(qualifiedName);
        write(".className = ");
        write("\"" + qualifiedName + "\"");
        writeNewline(";");
        writeNewline();
    }

    void emitGetClassDependencies(IClassNode tnode)
    {
        // foo.bar.Baz.getClassDependencies = function () {
        //     var p;
        //     return  [];
        // };

        final String qualifiedName = MetaDataUtils.getExportQualifiedName(tnode
                .getDefinition());

        write(qualifiedName);
        writeNewline(".getClassDependencies = function(t) {", true);
        writeNewline("var p;");

        Collection<ITypeDefinition> dependencies = getEmitter().getModel()
                .getDependencies();

        if (dependencies.size() > 0)
        {
            writeNewline("p = [];");
            for (ITypeDefinition type : dependencies)
            {
                writeNewline("p.push('"
                        + MetaDataUtils.getExportQualifiedName(type) + "');");
            }
            writeNewline("return p;", false);
        }
        else
        {
            writeNewline("return [];", false);
        }

        writeNewline("};");
        writeNewline();
    }

    void emitInjectionPoints(IClassNode node)
    {
        IClassDefinition definiton = node.getDefinition();
        IClassDefinition baseDefinition = definiton
                .resolveBaseClass(getProject());

        final String qualifiedName = MetaDataUtils.getExportQualifiedName(node
                .getDefinition());

        boolean hasArgs = DefinitionUtils.hasConstructorParameters(definiton);
        boolean isValidBase = isValidBaseClasse(baseDefinition);

        Collection<IMetaTag> propertyInjections = getEmitter().getModel()
                .getPropertyInjections();
        Collection<IMetaTag> methodInjections = getEmitter().getModel()
                .getMethodInjections();
        Collection<IMetaTag> viewInjections = getEmitter().getModel()
                .getViewInjections();

        if (!isValidBase)
        {
            if (!hasArgs && propertyInjections.size() == 0
                    && methodInjections.size() == 0
                    && viewInjections.size() == 0)
            {
                emitEmptyInjectionPoints(definiton);
                return;
            }
        }

        write(qualifiedName);
        writeNewline(".injectionPoints = function(t) {", true);
        writeNewline("var p;");
        writeNewline("switch (t) {", true);

        emitInjectionConstructor(node, hasArgs);
        emitInjectionProperty(node, propertyInjections, isValidBase);
        emitInjectionMethod(node, methodInjections, isValidBase);
        emitInjectionView(node, viewInjections, isValidBase);
        emitInjectionDefault(node, isValidBase);

        writeNewline("}");
        writeNewline("return p;", false);
        writeNewline("};");
        writeNewline();
    }

    private void emitEmptyInjectionPoints(IClassDefinition definiton)
    {
        // native base class and no constructor injection
        // just return an empty array
        final String qualifiedName = MetaDataUtils
                .getExportQualifiedName(definiton);

        write(qualifiedName);
        writeNewline(".injectionPoints = function(t) {", true);
        writeNewline("return [];", false);
        writeNewline("};");
    }

    private void emitInjectionConstructor(IClassNode node, boolean hasArgs)
    {
        if (!hasArgs)
            return;

        IClassDefinition definition = node.getDefinition();
        IFunctionDefinition constructor = definition.getConstructor();
        IParameterDefinition[] parameters = constructor.getParameters();
        String baseQualifiedName = toBaseQualifiedName(definition);

        writeNewline("case 0:", true);

        final int len = parameters.length;
        int i = 0;
        if (constructor.isImplicit() || len == 0)
        {
            write("p = " + baseQualifiedName + ".injectionPoints(t);");
        }
        else
        {
            write("p = [];");
            writeNewline();
            for (IParameterDefinition parameter : parameters)
            {
                ITypeDefinition rtype = parameter.resolveType(getProject());
                String exportName = MetaDataUtils.getExportQualifiedName(rtype);
                write("p.push({n:'" + parameter.getBaseName() + "'");
                write(",");
                write(" t:'" + exportName + "'");
                write("});");

                if (i < len - 1)
                    writeNewline();
                i++;
            }
        }

        writeNewline();
        writeNewline("break;", false);
    }

    private void emitInjectionProperty(IClassNode node,
            Collection<IMetaTag> injections, boolean isValidBase)
    {
        if (!isValidBase && injections.size() == 0)
            return;

        IClassDefinition definition = node.getDefinition();
        String baseQualfiiedName = toBaseQualifiedName(definition);

        writeNewline("case 1:", true);
        emitInjectionHeader(baseQualfiiedName, isValidBase);

        for (IMetaTag tag : injections)
        {
            IVariableDefinition owner = (IVariableDefinition) tag
                    .getDecoratedDefinition();
            ITypeDefinition ownerType = owner.resolveType(getProject());

            write("p.push({n:'" + owner.getBaseName() + "'");

            if (RandoriUtils.isValidInjectType(ownerType))
            {
                String name = MetaDataUtils.getExportQualifiedName(ownerType);
                write(",");
                write(" t:'" + name + "'");
            }

            write(",");
            write(" r:" + toInjectRequired(tag));

            String value = DefinitionUtils.returnInitialVariableValue(
                    (IVariableNode) owner.getNode(), getEmitter());
            write(",");
            write(" v:" + value);

            writeNewline("});");
        }

        writeNewline("break;", false);
    }

    private void emitInjectionMethod(IClassNode node,
            Collection<IMetaTag> injections, boolean isValidBase)
    {
        if (!isValidBase && injections.size() == 0)
            return;

        IClassDefinition definition = node.getDefinition();
        String baseQualfiiedName = toBaseQualifiedName(definition);

        writeNewline("case 2:", true);
        emitInjectionHeader(baseQualfiiedName, isValidBase);

        for (IMetaTag tag : injections)
        {
            IFunctionDefinition owner = (IFunctionDefinition) tag
                    .getDecoratedDefinition();

            write("p.push({n:'" + owner.getBaseName() + "'");
            write(", p:[");

            IParameterDefinition[] parameters = owner.getParameters();
            int i = 0;
            int len = parameters.length;
            for (IParameterDefinition parameter : parameters)
            {
                ITypeDefinition ownerType = parameter.resolveType(getProject());

                write("{");
                write("n:'" + parameter.getBaseName() + "'");

                if (!MetaDataUtils.isNative(ownerType))
                {
                    write(",");
                    write(" t:'" + ownerType.getQualifiedName() + "'");
                }

                write("}");

                if (i < len - 1)
                    write(", ");
                i++;
            }

            write("]");
            writeNewline("});");
        }

        writeNewline("break;", false);
    }

    private void emitInjectionView(IClassNode node,
            Collection<IMetaTag> injections, boolean isValidBase)
    {
        if (!isValidBase && injections.size() == 0)
            return;

        IClassDefinition definition = node.getDefinition();
        // XXX BASE needs to have its export javascript name used
        String baseQualfiiedName = toBaseQualifiedName(definition);
        //final String qualifiedName = MetaDataUtils
        //       .getExportQualifiedName(definition);

        writeNewline("case 3:", true);
        emitInjectionHeader(baseQualfiiedName, isValidBase);

        for (IMetaTag tag : injections)
        {
            String required = toViewRequired(tag);
            IVariableDefinition owner = (IVariableDefinition) tag
                    .getDecoratedDefinition();
            ITypeDefinition type = owner.resolveType(getProject());

            write("p.push({n:'" + owner.getBaseName() + "'");

            if (!MetaDataUtils.isNative(type))
            {
                write(",");
                write(" t:'" + MetaDataUtils.getExportQualifiedName(type) + "'");
            }

            if (tag.getAttributeValue("required") != null
                    && required.equals("0"))
            {
                write(",");
                write(" r:" + required);
            }

            write("});");
            writeNewline();
        }

        writeNewline("break;", false);
    }

    private void emitInjectionDefault(IClassNode tnode, boolean isBaseNative)
    {
        writeNewline("default:", true);
        write("p = [];");
        writeNewline();
        indentPop(); // take the extra indent a case uses out
        writeNewline("break;", false);
    }

    private void emitInjectionHeader(String baseQualfiiedName,
            boolean isValidBase)
    {
        if (isValidBase)
        {
            writeNewline("p = " + baseQualfiiedName + ".injectionPoints(t);");
        }
        else
        {
            writeNewline("p = [];");
        }
    }

    private static String toViewRequired(IMetaTag tag)
    {
        String value = tag.getAttributeValue("required");
        if (value == null)
            return "1";
        if (value.equals("true"))
            return "1";
        return "0";
    }

    private static String toInjectRequired(IMetaTag tag)
    {
        String value = tag.getAttributeValue("required");
        if (value == null)
            return "0";
        if (value.equals("true"))
            return "1";
        return "0";
    }

    final String toBaseQualifiedName(IClassDefinition definition)
    {
        return DefinitionUtils.toBaseClassQualifiedName(definition,
                getProject());
    }

    private boolean isValidBaseClasse(IClassDefinition definition)
    {
        if (definition == null)
            return false;
        return !definition.getBaseName().equals("Object");
    }

    void emitLast(IClassNode node)
    {
        IClassDefinition definition = node.getDefinition();
        IMetaTag tag = definition.getMetaTagByName("JavaScript");
        if (tag != null)
        {
            String main = tag.getAttributeValue("main");
            if (main != null && main.equals("true"))
            {
                write("window.onload = function() {");
                write("var main");
                write(" = ");
                write("new ");
                write(definition.getQualifiedName());
                write(";");
                write("}");
            }
        }
    }
}