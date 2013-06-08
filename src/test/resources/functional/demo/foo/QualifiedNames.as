package demo.foo
{
public class QualifiedNames
{
    [Embed(source="source")]
    public static const EMBED_ME:Class;
        
    public function QualifiedNames()
    {
    }
    
    public function public_constant()
    {
        var a:int = ClassA.CONSTANT;
    }
    
    public function new_inner_class()
    {
        var i:InnerClass = new InnerClass();
        i.property = ClassA.CONSTANT;
    }
}
}

class InnerClass {
    
    public function InnerClass()
    {
    }
    
    public function get property():Object
    {
        return null;
    }
    
    public function set property(value:Object):void
    {
    }
}