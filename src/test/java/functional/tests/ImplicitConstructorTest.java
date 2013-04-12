package functional.tests;

import org.junit.Test;

public class ImplicitConstructorTest extends FunctionalTestBase
{
    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
        assertOut("if (typeof demo == \"undefined\")\n\tvar demo = {};\nif (typeof demo.foo "
                + "== \"undefined\")\n\tdemo.foo = {};\n\ndemo.foo.ImplicitConstructor = function()"
                + " {\n\tthis.message = null;\n\trandori.behaviors.AbstractMediator.call(this);\n\t\n};"
                + "\n\n$inherit(demo.foo.ImplicitConstructor, randori.behaviors.AbstractMediator);"
                + "\n\ndemo.foo.ImplicitConstructor.className = \"demo.foo.ImplicitConstructor\";\n\n"
                + "demo.foo.ImplicitConstructor.getClassDependencies = function(t) {\n\tvar p;\n\t"
                + "return [];\n};\n\ndemo.foo.ImplicitConstructor.injectionPoints = function(t) {"
                + "\n\tvar p;\n\tswitch (t) {\n\t\tcase 1:\n\t\t\tp = randori.behaviors."
                + "AbstractMediator.injectionPoints(t);\n\t\t\tbreak;\n\t\tcase 2:\n\t\t\tp "
                + "= randori.behaviors.AbstractMediator.injectionPoints(t);\n\t\t\tbreak;\n\t\t"
                + "case 3:\n\t\t\tp = randori.behaviors.AbstractMediator.injectionPoints(t);\n\t\t\t"
                + "break;\n\t\tdefault:\n\t\t\tp = [];\n\t\t\tbreak;\n\t}\n\treturn p;\n};\n\n");
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "demo.foo.ImplicitConstructor";
    }
}
