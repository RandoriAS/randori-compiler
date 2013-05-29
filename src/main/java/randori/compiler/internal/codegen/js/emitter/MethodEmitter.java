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

import java.util.List;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.internal.tree.as.FunctionNode;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IContainerNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.codegen.js.ISubEmitter;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.MetaDataUtils;
import randori.compiler.internal.utils.RandoriUtils;

/**
 * Handles the production of the {@link IFunctionNode} method and it's
 * associated header block.
 * 
 * @author Michael Schmalle
 */
public class MethodEmitter extends BaseSubEmitter implements
        ISubEmitter<IFunctionNode>
{

    public MethodEmitter(IRandoriEmitter emitter)
    {
        super(emitter);
    }

    /**
     * This is necessary, the class with no explicit constructor does not have a
     * {@link IASNode} connected to the definition in AST.
     * 
     * @param definition The implicit constructor definition.
     */
    public void emitConstructor(IFunctionDefinition definition)
    {
        String prototype = RandoriUtils.toMethodPrefix(definition, getWalker()
                .getProject());
        write(prototype);
        write(" = function() ");
        writeNewline("{", true);
        // we have a synthesized constructor, implict

        emitConstructorFieldInitializers(definition);
        String qualifiedName = DefinitionUtils.toBaseClassQualifiedName(
                (ITypeDefinition) definition.getParent(), getWalker()
                        .getProject());
        if (qualifiedName != null && !qualifiedName.equals("Object"))
        {
            writeNewline(qualifiedName + ".call(this);");
        }

        indentPop();
        writeNewline();
        write("}");
    }

    @Override
    public void emit(IFunctionNode node)
    {
        if (node.isConstructor())
        {
            if (node.isImplicit())
                emitConstructor(node.getDefinition());
            else
                emitConstructor(node);
            return;
        }

        FunctionNode fn = (FunctionNode) node;
        fn.parseFunctionBody(getEmitter().getProblems());
        IFunctionDefinition definition = node.getDefinition();

        IClassNode cnode = (IClassNode) node
                .getAncestorOfType(IClassNode.class);
        if (!MetaDataUtils.isGlobal(cnode))
        {
            String prefix = RandoriUtils.toMethodPrefix(definition, getWalker()
                    .getProject());
            write(prefix);
            write(" = function");

            getEmitter().emitParamters(node);
            getEmitter().emitMethodScope(node);
        }
        else
        {
            write(definition.getBaseName());
            write(" = function");
            getEmitter().emitParamters(node);
            getEmitter().emitMethodScope(node);
        }
    }

    public void emitHeader(IFunctionNode node)
    {
        IFunctionDefinition definition = node.getDefinition();

        if (node.isConstructor())
        {
            emitConstructorFieldInitializers(definition);
            if (!DefinitionUtils.hasSuperCall(node, getWalker().getProject()))
            {
                String qualifiedName = DefinitionUtils
                        .toBaseClassQualifiedName(
                                (ITypeDefinition) definition.getParent(),
                                getWalker().getProject());
                if (qualifiedName != null)
                {
                    writeNewline(qualifiedName + ".call(this);");
                }
            }
        }

        // XXX GenericEmitUtils.emitDefaultParameterCodeBlock(node, getEmitter());

        String code = MetaDataUtils.findJavaScriptCodeTag(node);
        if (code != null)
        {
            write(code);
            writeNewline();
        }
    }

    private void emitConstructor(IFunctionNode node)
    {
        FunctionNode fn = (FunctionNode) node;
        fn.parseFunctionBody(getEmitter().getProblems());
        IFunctionDefinition definition = node.getDefinition();

        String prototype = RandoriUtils.toMethodPrefix(definition, getWalker()
                .getProject());
        write(prototype);
        write(" = function");
        getEmitter().emitParamters(node);
        if (!DefinitionUtils.isImplicit((IContainerNode) node.getScopedNode()))
        {
            getEmitter().emitMethodScope(node);
        }
        else
        {
            // we have a synthesized constructor, implict
            writeNewline(" {");
            write("}");
        }
    }

    protected void emitConstructorFieldInitializers(
            IFunctionDefinition definition)
    {
        IClassDefinition type = (IClassDefinition) definition
                .getAncestorOfType(IClassDefinition.class);
        // emit public fields init values
        List<IVariableDefinition> fields = DefinitionUtils.getFields(type,
                false);

        //final int len = fields.size();
        //int i = 0;
        for (IVariableDefinition field : fields)
        {
            if (field instanceof IAccessorDefinition)
                continue;
            // constants do not get initialized
            if (field instanceof IConstantDefinition)
                continue;
            if (DefinitionUtils.isVariableAParameter(field,
                    definition.getParameters()))
                continue;
            write("this.");
            write(field.getBaseName());
            write(" = ");

            String value = DefinitionUtils.returnInitialVariableValue(
                    (IVariableNode) field.getNode(), getEmitter());

            write(value);
            //if (i < len - 1)
            writeNewline(";");
            //else
            //     write(";");
        }
    }
}
