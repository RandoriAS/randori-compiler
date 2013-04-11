package randori.annotations
{

/**
 * Indicates how long annotations with the annotated type are to be retained. 
 */
[Target("ElementType.ANNOTATION")]
public class Retention extends Annotation
{
    public var value:Vector.<RetentionPolicy>; // CLASS, RUNTIME, SOURCE
    
    public function Retention()
    {
    }
}
}