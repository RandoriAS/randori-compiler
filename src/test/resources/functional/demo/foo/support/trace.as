package demo.foo.support
{
import randori.webkit.page.Window;

public function trace(...s):void
{   
    if (s.length > 1)
        Window.console.log(s);
    else
        Window.console.log(s[0]);
}
}