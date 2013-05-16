package functional.tests.annotation.support.jsconstructor
{

[JavaScriptConstructor(factoryMethod="MyClass.create(...args)")]
public class JSConstructorVarArgs
{
    public function JSConstructorVarArgs()
    {
    }
}
}