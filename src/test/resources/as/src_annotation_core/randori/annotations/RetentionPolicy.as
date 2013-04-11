package randori.annotations
{

/**
 * Annotation retention policy. 
 */
public final class RetentionPolicy
{
    public static const CLASS:RetentionPolicy = new RetentionPolicy("class");
    
    public static const RUNTIME:RetentionPolicy = new RetentionPolicy("runtime");
    
    public static const SOURCE:RetentionPolicy = new RetentionPolicy("source");
    
    public function RetentionPolicy(value:String)
    {
    }
}
}