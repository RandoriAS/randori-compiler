package functional.tests.annotation.support.jsconstructor
{

[JavaScriptConstructor(factoryMethod="foo.bar('static stuff')")]
public class JSConstructorFactoryMethodBasic
{
    public function JSConstructorFactoryMethodBasic()
    {
    }
}
}