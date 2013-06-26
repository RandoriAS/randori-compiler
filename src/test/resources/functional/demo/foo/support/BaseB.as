package demo.foo.support
{
public class BaseB extends BaseA
{
    public function BaseB()
    {
        super("is the best");
    }
    
    public function foo():void
    {
        super.foo("something");
    }
    
    public function test_bar():void
    {
        bar("foo");
    }
}
}