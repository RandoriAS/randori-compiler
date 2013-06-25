package demo.foo.support {

[JavaScript(export="false", mode="json")]
public class CharacterParam {
    
    public function CharacterParam(alpha:Boolean=undefined, 
                                   casing:Boolean=undefined, 
                                   pool:String=undefined, 
                                   symbols:Boolean=undefined) {
        super();
    }
    
    public var alpha:Boolean;
    public var casing:Boolean;
    public var pool:String;
    public var symbols:Boolean
}
}