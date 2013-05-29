package demo.args
{
public class DefaultArguments
{
    public function no_arguments():void
    {
        no_arguments();
    }
    
    public function one_argument(foo:String):void
    {
        one_argument("bar");
    }
    
    public function one_argument_default(foo:String = "goo"):void
    {
        one_argument_default(); // fills in arg as 'goo'
        one_argument_default("bar");
    }
    
    public function two_argument_default(foo:String = "goo", bar:int = 42):void
    {
        two_argument_default(); // fills in arg as 'goo', 42
        two_argument_default("bar"); // fills in 42
        two_argument_default("bar", 420);
    }
    
    public function three_params_two_argument_default(foo:String, bar:int = 42, baz:Object = undefined):void
    {
        three_params_two_argument_default("bar"); // foo("bar", 42)
        three_params_two_argument_default("bar", 420); // foo("bar", 420)
        three_params_two_argument_default("bar", 420, null); // foo("bar", 420, null)
    }
}
}