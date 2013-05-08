package functional.tests.annotation
{

import demo.foo.support.MenuItem2;

public class JavaScriptTest
{
    
    public function test_json_with_default_args():void
    {
        var menuItems:Array = new Array(
            new MenuItem2( "Targets", "views/targets.html" ),
            new MenuItem2( "Labs", "views/labs.html", false, "bar", 142 ),
            new MenuItem2( "Intel", "views/intel.html"  )
        );
    } 
}
}