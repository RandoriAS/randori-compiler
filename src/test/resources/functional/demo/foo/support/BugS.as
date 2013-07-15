package demo.foo.support
{
public class BugS extends BugT
{
    public function BugS() {
        parseFloat("100");
        var x:Object = new Object();
        num = parseFloat(x.attribute("name"));
        
        super("test1", 111);
    }
    private var num:Number; 
}
}