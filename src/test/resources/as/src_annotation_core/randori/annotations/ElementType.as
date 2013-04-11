package randori.annotations
{

/**
 * A program element type.
 */
public final class ElementType
{
    public static const ALL:ElementType = new ElementType("all");

    public static const ANNOTATION:ElementType = new ElementType("annotation");
    
    public static const TYPE:ElementType = new ElementType("type");
    
    public static const CLASS:ElementType = new ElementType("class");
    
    public static const INTERFACE:ElementType = new ElementType("interface");
    
    public static const CONSTRUCTOR:ElementType = new ElementType("constructor");
    
    public static const PROPERTY:ElementType = new ElementType("property");
    
    public static const FIELD:ElementType = new ElementType("field");
    
    public static const METHOD:ElementType = new ElementType("method");
    
    public static const VARIABLE:ElementType = new ElementType("variable");
    
    public function ElementType(value:String)
    {
      
    }
}
}