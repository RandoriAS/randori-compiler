package demo.foo.support
{
public class BaseA
{
    public function BaseA(a:String, b:String = "randori")
    {
        if (b.length > 0)
            a = b;
    }
    
    public function foo(a:String, b:String = "randori"):void
    {
    }
    
    public function bar(a:String, b:String = "randori"):void
    {
    }
}
}