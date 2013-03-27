package demo.inject
{
import demo.foo.ClassA;
import demo.foo.ClassB;

import guice.reflection.TypeDefinition;

import randori.jquery.JQuery;
import randori.webkit.html.HTMLDivElement;

public class InjectTest
{
    [Inject]
    public var injectField:ClassA;
    
    [Inject]
    public function get injectAccessor():String
    {
        return null;
    }
    
    public function set injectAccessor(value:String):void
    {
    }
    
    [View]
    public var viewField:JQuery;
    
    public function InjectTest(param1:String, param2:int = 1)
    {
        var div:HTMLDivElement = new HTMLDivElement();
        var a:ClassA = new ClassA();
        var b:TypeDefinition = new TypeDefinition();
    }
    
    [Inject]
    public function injectMethod(param1:ClassB, param2:String = "foo"):void
    {
    }
}
}