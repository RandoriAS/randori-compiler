package demo.foo
{

import demo.foo.support.AnotherStaticClass;
import demo.foo.support.MapTypeIdExportFalse;
import demo.foo.support.MapTypeIdExportTrue;
import demo.foo.support.MenuItem2;
import demo.foo.support.Mode1;
import demo.foo.support.PolyFill;
import demo.foo.support.StaticClass;
import demo.foo.support.SupportClassA;
import demo.foo.support.trace;

import randori.async.Promise;
import randori.behaviors.AbstractMediator;
import randori.behaviors.SimpleList;
import randori.behaviors.ViewStack;
import randori.jquery.Event;
import randori.jquery.JQuery;
import randori.jquery.JQueryStatic;
import randori.webkit.fileapi.FileReader;
import randori.webkit.html.HTMLBRElement;
import randori.webkit.page.Location;
import randori.webkit.page.Window;

public class ClassB extends ClassA
{
    protected static const REGEXP:RegExp = /^\s+|\s+$/g;
    
    private static const SAMPLEMAP:Object = new Object();
    
    private static const D2:String = "a2d";
    
    private static var sAncestors:Vector.<Event> = new <Event>[];
    
    private static var _stageInstance:SupportClassA;
    
    public var tempClass:Class;
    
    private var thenContracts:Array;
    
    public var field1:String = "Hello";
    
    public var field2:int = 42;
    
    public var field3:ClassB = null;
    
    public static var onSelectHandler:Function;
    
    public function get type():* {
        return null;
    }
    
    private var j:JQuery;
    
    public static var FOO:Object;
    
    public function get accessor1():int{}
    
    public function set accessor1(value:int):void{}
    
    public function ClassB(param1:Object, param2:int = 42, param3:String = 'foo')
    {
        super(param1);
        field2 = param2;
    }
    
    protected function method1(param:String):String
    {
        c.foo.bar(42);
        var a:int;
        var c:ClassA;
        var b;
        c.data = "foo";
        c.data = function(foo, bar) {
            function () {
                Window.alert("dd");
            }
        };
        // Not Impl
        // super.data = "";
        data = a = data;
        data = data[a[data[field1][0]]] = c.foo.concat(field1)[0];
        c.foo.bar(42);
        data = data[a][b][a = data];
        return data;
    }
    
    public function while_binary_getter()
    {
        while (thenContracts.length > 0) {
            
        }
    }
    
    public function this_no_parent()
    {
        var foo:ClassB = this;
    }
    
    public function for_each_statement()
    {
        for each (var id:String in thenContracts) 
        {
        }
    }
    
    public function for_with_vector_length()
    {
        var viewPoints:Vector.<ClassA> = new Vector.<ClassA>();
        for (var i:int = 0; i < viewPoints.length; i++) {
        }
    }
    
    public function as_cast()
    {
        var a:Object;
        var b:Array = a as Array;
    }
    
    public function is_type_check()
    {
        if (field1 is Array) 
        {
        }
    }
    
    public function undefined_literal()
    {
        if (field1 != undefined)
        {
        }
    }
    
    public function static_var_qualified()
    {
        FOO[foo] = "bar";
    }
    
    public function static_var_qualified_prepend_Type()
    {
        ClassB.FOO[foo] = "bar";
    }
    
    [JavaScriptProperty(name="FileReader")]
    /**
     *  @see randori.webkit.fileapi.FileReader
     */
    public static function get FileReader_():FileReader { return null; }
    public static function set FileReader_(value:FileReader):void { }
    
    public function FileReader_use_get_set_rename():void
    {
        FileReader_ = new FileReader();
        var a:FileReader = ClassB.FileReader_;
    }
    
    public function method_call_rename():void
    {
        j.show();
    }
    
    [JavaScriptMethod(name="fooBar")]
    public function method_annotation_rename()
    {
    }
    
    public function window_static_method():void
    {
        Window.alert("foo");
    }
    
    public function window_static_accessor():void
    {
        if (Window.console != null)
            Window.console.log("foo");
    }
    
    public function member_accessor_get_and_set():void
    {
        var c:ClassA;
        c.foo.bar(42);
    }
    
    public function default_parameters(foo:int, bar:String = "42", baz:Number = 0.42):void
    {
        var a:Object = baz;        
    }
    
    public function window_reduction():void
    {
        var nextLevel:* = Window.window;
    }
    
    public function this_get_accessor_explicit():void
    {
        var c:Object;
        c = this.accessor1;
    }
    
    public function this_get_accessor_explicit():void
    {
        var c:Object;
        c = this.accessor1;
    }
    
    public function this_get_accessor_anytype_access():void
    {
        if (type.injectionPoints == null) {
            
        }
    }
    
    public function new_anytype():void
    {
        var instance:Object = null;
        
        if (false)
            instance = new type();
    }
    
    public function JQueryStatic_J():void
    {
        foo.injectPotentialNode(FOO, JQueryStatic.J(field1));
    }
    
    public function anonymous_function_argument_delegate():void
    {
        Window.setTimeout(function():void {
            method1(foo);
        }, 1);
    }
    
    public function anonymous_function_field_delegate():void
    {
        onSelectHandler = function(event:Event) {
            foo = "Hello";
        }
    }
    
    [JavaScriptCode(file="resolve.js")]
    public function jscontext():void
    {
    }
    
    public function conditional_lhs_getter():void
    {
        if ((data == null) || (data.length == 0)) {
            return;
        }
    }
    
    public function functioncall_getter_invoke():void
    {
        var a;
        a = renderFunction(j, data[j]);
    }
    
    public function const_string():void
    {
        const RANDORI_VENDOR_ITEM_EXPRESSION:String = ";";
        var anyVendorItems:RegExp = new RegExp(RANDORI_VENDOR_ITEM_EXPRESSION, "g");
        foo(CONSTANT);
    }
    
    public function transform_rest_to_arguments( ...args ):void {
        var listener:Function
      
        while ( thenContracts.length > 0 ) {
            listener = thenContracts.pop();
            listener.apply(this, args );
        }
    }
    
    public function new_HTMLBRElement():void
    {
        var breakIt:HTMLBRElement = new HTMLBRElement();
        breakIt.onchange = function() {
            Window.console.log("We did it!");
        };
    }
    
    public function proto_default_parameter_arg_replacement(one:int, two:int = 2, three:String = "default"):void
    {
        var something:Function;
        something(one, two, three);
        var a:int = two;
        foo = three;
    }
    
    public function getter_in_return():void
    {
        return data;
    }
    
    public static function handleMe(event:Object):void
    {
    }
    
    public function static_delegate():void
    {
        var something:Function;
        something(handleMe);
    }
    
    public function remove_type_cast():void
    {
        var a:ClassA = ClassA(foo);
    }
    
    public function simple_get_assign():void
    {
        var a:String = foo;
    }    
    
    public function json_with_default_params():void
    {
        var menuItems:Array = new Array(
            new MenuItem2( "Targets", "views/targets.html" ),
            new MenuItem2( "Labs", "views/labs.html", false, "bar", 142 ),
            new MenuItem2( "Intel", "views/intel.html"  )
        );
    } 
    
    public function private_constructor_invoke():void
    {
        var location:Location = new Location();
        location.port = "420";
    }
    
    [View]
    public var names:SimpleList;
    
    public function local_array_literal():void
    {
        var o1:Object = {name:"Mike"};
        var o2:Object = {name:"Roland"};
        var ar:Array = [o1,o2];
        names.data = ar;
    }
    
    public function global_static():void
    {
        PolyFill.fillConsoleForIE();
    }
    
    public function global_static_qualified():void
    {
        demo.foo.support.PolyFill.fillConsoleForIE();
    }
    
    public function getter_in_while():void
    {
        var viewStack:ViewStack;
        while ( viewStack.currentViewUrl != null ) {
            viewStack.popView();
        }
    }
    
    public function constant_export_true():void
    {
        var mapOptions:Object;
        mapOptions.mapTypeId = MapTypeIdExportTrue.ROADMAP;
    }
    
    public function constant_export_false():void
    {
        var mapOptions:Object;
        mapOptions.mapTypeId = MapTypeIdExportFalse.ROADMAP;
    }
    
    public function non_resolving_identifier_masks_member():void
    {
        var newMap:Object;
        var o:Object = {position:42, field1:newMap};
    }
    
    public function named_inner_function():void
    {
        function showMe( counter:int ):void {
          Window.console.log("this is my counter" + counter );
        }

        for ( var i:int = 0; i<5; i++ ) {
          showMe( i );
        }
    }
    
    public function delegate_field():void
    {
        renderFunction(FOO, method1);
    }
    
    public function delegate_composite_field():void
    {
        var v:ClassA;
        renderFunction(FOO, v.renderFunction);
    }
    
    public function delegate_static_field():void
    {
        renderFunction(FOO, onSelectHandler);
    }
    
    public function delegate_bug1():void
    {
        var promise:Promise;
        promise.then(function(mediator:AbstractMediator):void
        {
             Window.console.log("got it");
        });
    }
    
    public function static_export_false_rename():void
    {
        StaticClass.foo();
    }
    
    public function static_qualified_export_false_rename():void
    {
        demo.foo.support.StaticClass.foo();
    }
    
    public function parseInt_radix():void
    {
        parseInt("42");
        parseInt("42", 10);
    }
    
    public function for_each_impl():void
    {
        var x:Array = ["e","f"];
        for each(var s:String in x)
        {
            Window.console.log(s);
        } 
    }
    
    public function regexp_literal():void
    {
        var fooPattern:RegExp = /bar/ms;
        var barPattern:RegExp = /bar/;
    }
    
    private const WIDTH:Number = 500;
    private const HEIGHT:Number = 300;
    
    public function constant_private():void
    {
        var margin:Object;
        field2 = WIDTH - margin.left - margin.right;
        field2 = HEIGHT - margin.top - margin.bottom;
    }
    
    public var modela:ClassA;
    
    // selectedMovieModel.selectedMovie.similars = value;
    public function getter_bug():void
    {
        modela.menuItem.isRedirect = false;
    }
    
    public function compound_assignment():void
    {
        foo <<= 42;
        foo >>= 42;
        foo >>>= 42;
        foo *= 42;
        foo /= 42;
        foo %= 42;
        foo &= 42;
        foo ^= 42;
        foo |= 42;
        foo += 42;
        foo -= 42;
        foo &&= 42;
    }
    
    public function explicit_this_rhs():void
    {
        var field3:ClassB = this.field3;
        var accessor1:int = this.accessor1;
        var field1:String = this.field1;
    }
    
    override public function get goo(value:Object):void
    {
        super.goo = value;
        var o:Object = super.goo;
        super.goo = super.goo;
    }
    
    override public function get foo(value:Object):void
    {
    }
    
    // issue89 overridding an accessor ruins calls to it
    override public function override_and_use_foo(value:Object):void
    {
        var o:Object = foo;
    }
    
    override public function static_accessor_to_setter(value:Object):void
    {
        var o:Object = AnotherStaticClass.current.stage.width = 42;
    }
    
    public function private_static_var_access():void
    {
        SAMPLEMAP[D2] = new ClassA(D2);
    }
    
    public function regexp_literal_access():void
    {
        var line:String = "";
        line = line.replace(REGEXP, "");
    }
    
    public function namespace_static_access():void 
    {
        ClassB.bug_internal::staticInternalTest();
    }
    
    bug_internal static function staticInternalTest():String
    {
        return "hi";
    }
    
    static private function init():void { }
    
    public function static_private_access():void 
    {
        init();
    }
    
    public function instanceof_qualified():void 
    {
        var o:Object;
        if (o is ClassA)
            return;
    }
    
    public function static_dependency():void 
    {
        method1(ClassA.CONSTANT);
        var o:Object = AnotherStaticClass.MODE;
    }
    
    public function private_static_var_instance_access():void 
    {
        _stageInstance.width = 42;
    }
    
    public function if_explicit_parens():void 
    {
        if (!(this is ClassA))
            return;
    }
    
    public function static_var_access():void 
    {
        sAncestors.indexOf(accessor1);
    }
    
    public function instance_var_class_instantiation():void 
    {
        this.foo = new tempClass();
    }
    
    public function type_array_liternal_element():void 
    {
        var classesa:Array = ["SomeClassA", StaticClass]; // has JavaScript(name)
        var classesb:Array = ["SomeClassB", AnotherStaticClass, tempClass];
    }
    
    public function compound_assign_parens():void 
    {
        var obj:Object = {x:2};
        this.accessor1 += obj.x;
    }
    
    public function package_function_call():void 
    {
        MyFunction();
    }
    
    public function package_function_dependency():void 
    {
        trace("Hello Worlds! You are mine!");
    }
    
    public function static_var_dependency():void 
    {
        SupportClassA.inputMode = AnotherStaticClass.MODE;
        Mode1.prepareStuff();
    }
}
}