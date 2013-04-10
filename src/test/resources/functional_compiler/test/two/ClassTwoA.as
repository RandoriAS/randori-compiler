package test.two
{
import test.one.ClassOneA;
import test.one.a.SubClassOneA;

public class ClassTwoA extends ClassOneA
{
    public var field1:SubClassOneA = new SubClassOneA();
    
    public function ClassTwoA()
    {
    }
}
}