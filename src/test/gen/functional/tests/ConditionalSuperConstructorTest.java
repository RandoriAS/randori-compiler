package functional.tests;

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

public class ConditionalSuperConstructorTest extends FunctionalTestBase
{
    @Test
    public void test_constructor()
    {
        IFunctionNode node = findFunction("ConditionalSuperConstructor",
                classNode);
        visitor.visitFunction(node);
        assertOut("demo.foo.ConditionalSuperConstructor = function(textures) {"
                + "\n\tvar ridiculousCrap = false;\n\tif (textures.length > 0) {"
                + "\n\t\tif (true) {\n\t\t\tif (ridiculousCrap) {\n\t\t\t} else {"
                + "\n\t\t\t\tdemo.foo.ClassA.call(this, textures[0]);\n\t\t\t}\n\t\t}\n\t} "
                + "else {\n\t\tthrow new Error(\"Empty texture array\", 0);\n\t}\n}");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.ConditionalSuperConstructor";
    }
}
