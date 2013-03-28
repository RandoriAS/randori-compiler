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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.internal.codegen.as.ASFilterWriter;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.units.ICompilationUnit.UnitType;
import org.apache.flex.compiler.visitor.as.IASBlockWalker;

import randori.compiler.codegen.js.IRandoriEmitter;
import randori.compiler.common.VersionInfo;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.driver.IRandoriTarget;
import randori.compiler.internal.utils.DefinitionUtils;
import randori.compiler.internal.utils.MetaDataUtils;

/**
 * The bas class for project set's containing compilation units to produce
 * source code for based on their specific output rules.
 * 
 * @author Michael Schmalle
 */
public abstract class BaseCompilationSet
{
    private static final String OBJECT = "Object";

    protected HashMap<IClassDefinition, BinaryEntry> map = new HashMap<IClassDefinition, BinaryEntry>();

    private Collection<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>();

    protected final FlexProject project;

    protected IRandoriTargetSettings settings;

    protected File outputDirectory;

    private File outputFile;

    // TODO (mschmalle) I'm sure this needs a refactor to Writer, but this was quick and dirty
    protected StringBuilder builder;

    protected IRandoriBackend backend;

    protected List<ICompilerProblem> problems;

    public BaseCompilationSet(FlexProject project,
            IRandoriTargetSettings settings)
    {
        this.project = project;
        this.settings = settings;

        builder = new StringBuilder();
    }

    /**
     * Filters the collection of units for this project bases on the
     * implementing class's {@link #accept(ITypeNode)} return value.
     * 
     * @param units The full valid compilation collection to filter.
     */
    public void filter(Collection<ICompilationUnit> units)
    {
        for (ICompilationUnit unit : units)
        {
            IClassNode node = getClassNode(unit);
            if (accept(node))
            {
                addCompilationUnit(unit);
            }
        }
    }

    /**
     * Called to filter out compilation units in tha main set based on the
     * subclasses requirements realting to which project the unit is located in.
     * 
     * @param node The {@link ITypeNode} of the {@link ICompilationUnit}.
     * @return <code>true</code> if the {@link ICompilationUnit} should be
     * included in this source code generated for this set.
     */
    abstract protected boolean accept(ITypeNode node);

    /**
     * Returns all the {@link ICompilationUnit}s the {@link IRandoriTarget}
     * passed this set.
     * 
     * @return
     */
    protected Collection<ICompilationUnit> getCompilationUnits()
    {
        return compilationUnits;
    }

    /**
     * Adds a {@link ICompilationUnit} to the internal collection that will be
     * used during source code generation.
     * 
     * @param unit The {@link ICompilationUnit} to add to the generate
     * collection.
     */
    protected void addCompilationUnit(ICompilationUnit unit)
    {
        compilationUnits.add(unit);
    }

    /**
     * Generated the specific source code based in the
     * {@link IRandoriTargetSettings} compiler arguments.
     * <p>
     * This should be abstract and the parameters not saved on the instance,
     * would it work?
     * <p>
     * Subclasses override this method to generate specific output locations.
     * 
     * @param backend The current {@link IRandoriBackend} factory.
     * @param problems The current compiler {@link ProblemQuery}.
     * @param output The base output path specified. (this should be deleted and
     * just use {@link IRandoriTargetSettings#getOutput()}.
     */
    protected void generate(IRandoriBackend backend,
            List<ICompilerProblem> problems, File output)
    {
        this.backend = backend;
        this.problems = problems;
        this.outputDirectory = output;
    }

    /**
     * Used during the binary tree walk to find dependencies of inheritance.
     * 
     * @param targetClass
     * @param dependency
     */
    protected void handleClass(IClassDefinition targetClass,
            IClassDefinition dependency)
    {
        BinaryEntry entry;

        if (!map.containsKey(targetClass))
        {
            entry = new BinaryEntry();
            entry.setRoot(true);
            entry.clear();

            IClassDefinition baseClass = targetClass.resolveBaseClass(project);
            if (baseClass != null && !baseClass.getBaseName().equals(OBJECT))
            {
                entry.setRoot(false);
                handleClass(baseClass, targetClass);
            }

            map.put(targetClass, entry);
        }

        if (dependency != null)
        {
            entry = map.get(targetClass);
            if (!entry.getDependencies().contains(dependency))
            {
                entry.getDependencies().add(dependency);
            }
        }
    }

    /**
     * Recursively method that walks a classes dependency list while calling
     * {@link #writeClass(IClassDefinition)} to output the file in inheritance
     * order.
     * 
     * @param definition The parent {@link ITypeDefinition} to walk it's
     * dependencies.
     */
    protected void walkClass(IClassDefinition definition)
    {
        BinaryEntry entry = map.get(definition);

        writeClass(definition);

        if (entry.getDependencies() != null)
        {
            for (IClassDefinition dependency : entry.getDependencies())
            {
                walkClass(dependency);
            }
        }
    }

    /**
     * Writes a single {@link IClassDefinition} to the buffer during a full
     * project output.
     * 
     * @param definition The {@link IClassDefinition} to write to the buffer.
     */
    protected void writeClass(IClassDefinition definition)
    {
        if (!accept(definition.getNode()))
            return;

        builder.append("\n// ====================================================\n");
        builder.append("// " + definition.getQualifiedName() + "\n");
        builder.append("// ====================================================\n\n");

        IClassNode node = (IClassNode) definition.getNode();
        IFileNode fileNode = (IFileNode) node
                .getAncestorOfType(IFileNode.class);

        ASFilterWriter writer = backend.createWriterBuffer(project);
        IRandoriEmitter emitter = (IRandoriEmitter) backend
                .createEmitter(writer);
        IASBlockWalker visitor = backend.createWalker(project, problems,
                emitter);

        visitor.visitFile(fileNode);

        builder.append(writer.toString());
    }

    /**
     * Writes a full project's classes out to one single monolithic file in
     * inheritance order.
     * 
     * @param basePath The resolved base path to generated the single file.
     * @param fileName The project's file name.
     */
    protected void writeFull(String basePath, String fileName)
    {
        builder = new StringBuilder();

        File outputFolder = new File(outputDirectory.getAbsoluteFile(),
                basePath);
        outputFolder.mkdirs();

        outputFile = new File(outputFolder, fileName);

        List<IClassNode> nodes = new ArrayList<IClassNode>();

        List<IClassNode> globals = new ArrayList<IClassNode>();

        for (ICompilationUnit unit : getCompilationUnits())
        {
            IClassNode node = getClassNode(unit);
            if (node != null)
            {
                if (MetaDataUtils.isGlobal(node))
                {
                    globals.add(node);
                }
                else
                {
                    nodes.add(node);
                }
            }
        }

        builder.append("/** Compiled by the Randori compiler v"
                + VersionInfo.getCompilerVersion() + " on "
                + new Date().toString() + " */\n\n");

        for (IClassNode node : nodes)
        {
            handleClass(node.getDefinition(), null);
        }

        for (IClassNode node : globals)
        {
            writeClass(node.getDefinition());
        }

        for (Entry<IClassDefinition, BinaryEntry> entry : map.entrySet())
        {
            if (entry.getValue().isRoot())
            {
                walkClass(entry.getKey());
            }
        }

        flush();

        System.out.println("Finished");
    }

    /**
     * Flushes the buffer of a full project write to the target file.
     */
    protected void flush()
    {
        BufferedOutputStream out;
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(outputFile));
            out.write(builder.toString().getBytes());
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected IClassNode getClassNode(ICompilationUnit unit)
    {
        if (unit.getCompilationUnitType() != UnitType.AS_UNIT)
            return null;

        try
        {
            IFileNode fileNode = (IFileNode) unit.getSyntaxTreeRequest().get()
                    .getAST();
            IASNode child = fileNode.getChild(0);
            if (child instanceof IPackageNode)
            {
                IPackageNode packageNode = (IPackageNode) child;
                IClassNode classNode = DefinitionUtils
                        .findClassNode(packageNode);
                if (classNode != null)
                {
                    if (MetaDataUtils.isExport(classNode.getDefinition()))
                    {
                        return classNode;
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * An entry in the binary tree for inheritance.
     */
    class BinaryEntry
    {
        private boolean isRoot = false;

        public boolean isRoot()
        {
            return isRoot;
        }

        public void setRoot(boolean value)
        {
            isRoot = value;
        }

        List<IClassDefinition> dependencies;

        List<IClassDefinition> getDependencies()
        {
            return dependencies;
        }

        void clear()
        {
            dependencies = new ArrayList<IClassDefinition>();
        }
    }
}
