package randori.compiler.problems;

import org.apache.flex.compiler.problems.CompilerProblem;

public final class UnableToBuildApplicationProblem extends CompilerProblem
{
    public static final String DESCRIPTION = "Unable to build Randori Application ${file}";
    
    public final String file;

    public UnableToBuildApplicationProblem(String file)
    {
        super();
        this.file = file;
    }

}
