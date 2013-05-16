package functional.tests.annotation
{

import functional.tests.annotation.support.jsconstructor.JSConstructorFactoryMethodBasic;
import functional.tests.annotation.support.jsconstructor.JSConstructorVarArgs;

public class JavaScriptConstructorTest
{
    public function JavaScriptConstructorTest()
    {
    }
    
    public function test_factoryMethod():void
    {
        var obj:JSConstructorFactoryMethodBasic = new JSConstructorFactoryMethodBasic();
    }
    
    public function test_var_args():void
    {
        var obj:JSConstructorVarArgs = new JSConstructorVarArgs("text", 1, true);
    }
}
}