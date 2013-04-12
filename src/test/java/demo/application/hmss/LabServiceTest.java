package demo.application.hmss;

import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.junit.Test;

import randori.compiler.internal.constants.TestConstants;
import randori.compiler.internal.js.codegen.RandoriTestProjectBase;

/**
 * @author Michael Schmalle
 */
public class LabServiceTest extends RandoriTestProjectBase
{
    @Test
    public void test_constructor()
    {
        IFunctionNode node = findFunction("LabService", classNode);
        visitor.visitFunction(node);
        assertOut("services.LabService = function(xmlHttpRequest, targets) {\n\t"
                + "this.path = null;\n\trandori.service.AbstractService.call("
                + "this, xmlHttpRequest);\n\tthis.targets = targets;\n\tthis.path = "
                + "\"assets\\/data\\/gadgets.txt\";\n}");
    }

    @Test
    public void test_get()
    {
        IFunctionNode node = findFunction("get", classNode);
        visitor.visitFunction(node);
        assertOut("services.LabService.prototype.get = function() {\n\tvar promise "
                + "= this.sendRequest(\"GET\", this.path);\n\tvar parserPromise = promise."
                + "then(this.targets.parseResult);\n\treturn parserPromise;\n}");
    }

    @Test
    public void test_file()
    {
        visitor.visitFile(fileNode);
    }

    @Override
    protected String getBasePath()
    {
        return TestConstants.RandoriASFramework
                + "\\randori-demos-bundle\\HMSS\\src";
    }

    @Override
    protected String getTypeUnderTest()
    {
        return "services.LabService";
    }
}