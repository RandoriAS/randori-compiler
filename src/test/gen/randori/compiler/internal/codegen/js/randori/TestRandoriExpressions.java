package randori.compiler.internal.codegen.js.randori;

import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

import randori.compiler.driver.IBackend;
import randori.compiler.internal.ASTestBase;
import randori.compiler.internal.driver.RandoriBackend;

public class TestRandoriExpressions extends ASTestBase
{
    @Override
    protected IBackend createBackend()
    {
        return new RandoriBackend();
    }

    protected IFunctionNode getMethod(String code)
    {
        return (IFunctionNode) getNode(
                "package{public class A { public function get bar():String {return null}"
                        + code + "}}", IFunctionNode.class);
    }

    //----------------------------------
    // Arithmetic compound assignment
    //----------------------------------

    @Test
    public void testVisitBinaryOperatorNode_PlusAssignment()
    {
        IBinaryOperatorNode node = getBinaryNode("a += b");
        visitor.visitBinaryOperator(node);
        assertOut("a += b");
    }

    @Test
    public void testVisitBinaryOperatorNode_MinusAssignment()
    {
        IBinaryOperatorNode node = getBinaryNode("a -= b");
        visitor.visitBinaryOperator(node);
        assertOut("a -= b");
    }

    @Test
    public void testVisitBinaryOperatorNode_DivideAssignment()
    {
        IBinaryOperatorNode node = getBinaryNode("a /= b");
        visitor.visitBinaryOperator(node);
        assertOut("a /= b");
    }

    @Test
    public void testVisitBinaryOperatorNode_ModuloAssignment()
    {
        IBinaryOperatorNode node = getBinaryNode("a %= b");
        visitor.visitBinaryOperator(node);
        assertOut("a %= b");
    }

    @Test
    public void testVisitBinaryOperatorNode_MultiplyAssignment()
    {
        IBinaryOperatorNode node = getBinaryNode("a *= b");
        visitor.visitBinaryOperator(node);
        assertOut("a *= b");
    }

}
