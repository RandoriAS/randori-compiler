package functional.tests.annotation.support.jsmethod {

[JavaScript(export="false")]
public class AngularTimeoutServiceExample {

	[JavaScriptMethod("")]
	public function createTimer (fn : Function, delay : Number = 0, invokeApply : Boolean = true) : Object {
		return {};
	}

	public function cancelTimer (obj : Object) : Boolean {
		return false;
	}
}
}