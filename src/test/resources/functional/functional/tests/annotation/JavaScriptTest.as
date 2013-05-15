package functional.tests.annotation
{

import demo.foo.support.MenuItem2;

import functional.tests.annotation.support.ExportGlobal;
import functional.tests.annotation.support.ExportJSON;
import functional.tests.annotation.support.ExportPrototype;
import functional.tests.annotation.support.ExportPrototypeDefault;
import functional.tests.annotation.support.NameEmpty;
import functional.tests.annotation.support.NameQualified;
import functional.tests.annotation.support.NameSimple;
import functional.tests.annotation.support.NameSimpleNull;

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
    
    //----------------------------------
    // mode
    //----------------------------------
    
    public function test_export_global():void
    {
        ExportGlobal.staticMethod();
        ExportGlobal.thing = "Foo";
    }
    
    public function test_export_prototype():void
    {
        ExportPrototype.staticMethod();
        ExportPrototype.thing = "Foo";
    }
    
    public function test_export_prototype_default():void
    {
        ExportPrototypeDefault.staticMethod();
        ExportPrototypeDefault.thing = "Foo";
    }
    
    public function test_export_json_constructor():void
    {
        var x1:ExportJSON = new ExportJSON("7", "8");
    }
    
    public function test_export_json_variable():void
    {
        var x1:ExportJSON = new ExportJSON();
        x1.prop = "7";
        x1.thing = "8";
    }
    
    //----------------------------------
    // name
    //----------------------------------
    
    public function test_name_empty():void
    {
        var x:NameEmpty = new NameEmpty();
    }
    
    public function test_name_simple_null():void
    {
        var x:NameSimpleNull = new NameSimpleNull();
        x.thing = "7";
        x.method();
        NameSimpleNull.staticMethod();
    }
    
    public function test_name_simple():void
    {
        var x:NameSimple = new NameSimple(1);
        x.thing = "7";
        x.method();
        NameSimple.staticMethod();
    }
    
    public function test_name_qualified():void
    {
        var x:NameQualified = new NameQualified(1);
        x.thing = "7";
        x.method();
        NameQualified.staticMethod();
    }
    
}
}