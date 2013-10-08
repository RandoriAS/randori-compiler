package functional.tests.annotation {

import functional.tests.annotation.support.jsmethod.AngularTimeoutServiceExample;
import randori.jquery.JQueryStatic;

public class JavaScriptMethodTest {

	public function testTimeout (timeout : AngularTimeoutServiceExample) : void {
		var timer : Object = timeout.createTimer(function () : void {
			return;
		}, 2000);

		timer.cancelTimer();
	}

	public function testJQueryStaticJ () : void {
		JQueryStatic.J("sup");
		randori.jquery.JQueryStatic.J("sup2");
	}
}
}