package foo.bar
{

[JavaScript(export="true", name="Goo")]
public class Baz
{
    public function Baz()
    {
    }
    
    public function method():void
    {
        var goo:Baz = new Baz();
    }
}
}