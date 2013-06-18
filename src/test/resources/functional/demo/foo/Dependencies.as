package demo.foo
{
import demo.foo.support.AnotherStaticClass;
import demo.foo.support.Bar;
import demo.foo.support.Mode1;
import demo.foo.support.Static2;
import demo.foo.support.SupportClassA;
import demo.foo.support.trace;

public class Dependencies
{
    public static var pfoo:Dependencies = Dependencies;
    public static var pfoo:String = ClassB.FOO;
    public static var pbar:Object = MyFunction();
    public static var pbar2 = MyTestFunction();
    
    public function runtime_dependencies():void {
        var c:Dependencies = new Dependencies();
        var myClass = Mode1;
        var x = new Bar();
        trace("Yo");
        AnotherStaticClass.current = 3;
        SupportClassA.inputMode = Static2.property;
    }
}
}