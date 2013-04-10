package demo.inject
{
import demo.foo.ClassA;

import guice.GuiceModule;
import guice.binding.Binder;

import randori.webkit.xml.XMLHttpRequest;

public class TestModule extends GuiceModule
{
    override public function configure(binder:Binder):void
    {
        super.configure(binder);
        binder.bind(InjectTest).to(EmptyInherit);
        binder.bind(XMLHttpRequest).to(ClassA);
    }
}
}