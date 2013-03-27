package demo.foo.support
{
[JavaScript(export="false",name="Object",mode="json")]
public class MenuItem2 {
    public var name:String;
    public var url:String;
    public var isRedirect:Boolean;
    
    public function MenuItem2( name:String, url:String, isRedirect:Boolean=true, param4:String = "bah", param5:int = 42 ) {
        this.name = name;
        this.url = url;
        this.isRedirect = isRedirect;
    }
}
}