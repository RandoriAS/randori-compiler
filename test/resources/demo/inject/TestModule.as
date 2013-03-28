package demo.inject
{
import guice.GuiceModule;
import guice.binding.Binder;

public class TestModule extends GuiceModule
{
    override public function configure(binder:Binder):void
    {
        super.configure(binder);
        binder.bind(InjectTest).to(EmptyInherit);
    }
}
}