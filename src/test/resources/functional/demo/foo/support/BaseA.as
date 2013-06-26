package demo.foo.support
{
public class BaseA
{
    public function BaseA(a:String, b:String = "randori")
    {
        if (b.length > 0)
            a = b;
    }
}
}