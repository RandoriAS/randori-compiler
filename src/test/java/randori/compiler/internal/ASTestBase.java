package randori.compiler.internal;

import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IAccessorNode;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IUnaryOperatorNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

public class ASTestBase extends TestBase
{
    protected IASNode getNode(String code, Class<? extends IASNode> type)
    {
        IFileNode node = (IFileNode) compile(code);

        if (type.isInstance(node))
            return node;

        return (IASNode) findFirstDescendantOfType(node, type);
    }

    protected IAccessorNode getAccessor(String code)
    {
        return (IAccessorNode) getNode(
                "package{public class A {" + code + "}}", IAccessorNode.class);
    }

    protected IVariableNode getField(String code)
    {
        return (IVariableNode) getNode(
                "package{public class A {" + code + "}}", IVariableNode.class);
    }

    protected IFunctionNode getMethod(String code)
    {
        return (IFunctionNode) getNode(
                "package{public class A {" + code + "}}", IFunctionNode.class);
    }

    protected IExpressionNode getExpressionNode(String code,
            Class<? extends IASNode> type)
    {
        return (IExpressionNode) getNode(
                "package{public class A {public function a():void{" + code
                        + "}}}", type);
    }

    protected IBinaryOperatorNode getBinaryNode(String code)
    {
        return (IBinaryOperatorNode) getNode(
                "package{public class A {public function a():void{" + code
                        + "}}}", IBinaryOperatorNode.class);
    }

    protected IUnaryOperatorNode getUnaryNode(String code)
    {
        return (IUnaryOperatorNode) getNode(
                "package{public class A {public function a():void{" + code
                        + "}}}", IUnaryOperatorNode.class);
    }
}
