package demo.foo
{

public class ConditionalSuperConstructor extends ClassA
{
    public function ConditionalSuperConstructor(textures:Array)
    {
        var ridiculousCrap:Boolean = false;
        if (textures.length > 0) {
            if (true) {
                if (ridiculousCrap) {
                    
                } else {
                    super(textures[0]);
                }
            }
        } else {
            throw new Error("Empty texture array");
        }
    }
}
}