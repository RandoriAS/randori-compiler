package randori.compiler.bundle;

import org.apache.flex.compiler.problems.ICompilerProblem;

/**
 * @author Roland Zwaga <roland@stackandheap.com>
 */
public interface IMutableBundle extends IBundle
{
    void addLibrary(IBundleLibrary library);

    void addProblem(ICompilerProblem problem);
}
