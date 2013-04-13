package randori.compiler.internal.visitor.as;

import java.io.IOException;
import java.util.Collection;

import org.apache.flex.compiler.definitions.IAccessorDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.INamespaceDefinition;
import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.projects.IASProject;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IInterfaceNode;
import org.apache.flex.compiler.tree.as.INamespaceNode;
import org.apache.flex.compiler.tree.as.IPackageNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.visitor.as.IASVisitor;
import randori.compiler.visitor.as.IASWalker;

public class ASWalker implements IASWalker
{

    private final IASVisitor visitor;

    @SuppressWarnings("unused")
    private IASProject project;

    public ASWalker(IASVisitor visitor)
    {
        this.visitor = visitor;
    }

    @Override
    public void walkProject(IASProject project) throws IOException
    {
        this.project = project;

        boolean visitChildren = visitor.visitProject(project);
        if (visitChildren)
        {
            //            Collection<ICompilationUnit> units = project.getCompilationUnits();
            //            for (ICompilationUnit unit : units)
            //            {
            //                //if (unit.getCompilationUnitType() == UnitType.AS_UNIT)
            //                //{
            //                    System.out.println(unit.getAbsoluteFilename());
            //                    walkCompilationUnit(unit);
            //                //}
            //            }
            Collection<IDefinition> collection = project.getScope()
                    .getAllLocalDefinitions();
            for (IDefinition iDefinition : collection)
            {
                if (iDefinition instanceof IClassDefinition)
                {
                    walkClass((IClassDefinition) iDefinition);
                }
                else if (iDefinition instanceof IInterfaceDefinition)
                {
                    walkInterface((IInterfaceDefinition) iDefinition);
                }
            }
        }

        this.project = null;
    }

    @Override
    public void walkCompilationUnit(ICompilationUnit element)
            throws IOException
    {
        boolean visitChildren = visitor.visitCompilationUnit(element);
        if (visitChildren)
        {
            IFileNode node = null;
            try
            {
                node = (IFileNode) element.getSyntaxTreeRequest().get()
                        .getAST();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            if (node != null)
            {
                walkFile(node);
            }
            else
            {
                //                // SWC
                //                List<IDefinition> promises = element.getDefinitionPromises();
                //                promises.size();
                //                for (IDefinition definition : promises)
                //                {
                //                    DefinitionPromise
                //                    walkClass((IClassDefinition) definition);
                //                }
            }
        }
    }

    @Override
    public void walkFile(IFileNode node)
    {
        boolean visitChildren = visitor.visitFile(node);
        if (visitChildren)
        {
            IASNode pnode = node.getChild(0);
            if (pnode instanceof IPackageNode)
            {
                IPackageNode packageNode = (IPackageNode) pnode;
                walkPackage((IPackageDefinition) packageNode.getDefinition());
            }
            else
            {
                // what is here
            }
        }
    }

    @Override
    public void walkPackage(IPackageDefinition definition)
    {
        boolean visitChildren = visitor.visitPackage(definition);
        if (visitChildren)
        {
            @SuppressWarnings("unused")
            IScopedNode node = definition.getNode().getScopedNode();
            IDefinitionNode child = findTypeNode(definition.getNode());
            if (child instanceof IClassNode)
            {
                walkClass((IClassDefinition) child.getDefinition());
            }
            else if (child instanceof IInterfaceNode)
            {
                walkInterface((IInterfaceDefinition) child.getDefinition());
            }
            else if (child instanceof INamespaceNode)
            {
                walkNamespace((INamespaceDefinition) child.getDefinition());
            }
            else if (child instanceof IFunctionNode)
            {
                walkMethod((IFunctionDefinition) child.getDefinition());
            }
        }
    }

    @Override
    public void walkClass(IClassDefinition definition)
    {
        boolean visitChildren = visitor.visitClass(definition);
        if (visitChildren)
        {
            IClassNode node = (IClassNode) definition.getNode();

            walkTypeMetaTags(definition);
            if (node == null)
                return;

            for (IDefinitionNode child : node.getAllMemberNodes())
            {
                IDefinition cdefinition = child.getDefinition();
                if (cdefinition instanceof IConstantDefinition)
                {
                    walkConstant((IConstantDefinition) cdefinition);
                }
                else if (cdefinition instanceof IAccessorDefinition)
                {
                    walkAccessor((IAccessorDefinition) cdefinition);
                }
                else if (cdefinition instanceof IVariableDefinition)
                {
                    walkField((IVariableDefinition) cdefinition);
                }
                else if (cdefinition instanceof IFunctionDefinition)
                {
                    walkMethod((IFunctionDefinition) cdefinition);
                }
                else if (cdefinition instanceof INamespaceDefinition)
                {
                    walkNamespace((INamespaceDefinition) cdefinition);
                }
            }
        }
    }

    @Override
    public void walkTypeMetaTags(ITypeDefinition definition)
    {
        for (IMetaTag tag : definition.getAllMetaTags())
        {
            visitor.visitTypeMetaTag(tag);
        }
    }

    @Override
    public void walkInterface(IInterfaceDefinition definition)
    {
        visitor.visitInterface(definition);
    }

    @Override
    public void walkNamespace(INamespaceDefinition definition)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void walkMethod(IFunctionDefinition definition)
    {
        visitor.visitMethod(definition);
    }

    @Override
    public void walkAccessor(IAccessorDefinition definition)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void walkField(IVariableDefinition definition)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void walkConstant(IConstantDefinition definition)
    {
        // TODO Auto-generated method stub

    }

    protected ITypeNode findTypeNode(IPackageNode node)
    {
        IScopedNode scope = node.getScopedNode();
        for (int i = 0; i < scope.getChildCount(); i++)
        {
            IASNode child = scope.getChild(i);
            if (child instanceof ITypeNode)
                return (ITypeNode) child;
        }
        return null;
    }

}
