package randori.compiler.bundle;

import org.apache.flex.compiler.problems.ICompilerProblem;

/**
 * An {@link IBundle} that can have {@link IBundleLibrary}s added at runtime.
 * 
 * @author Roland Zwaga <roland@stackandheap.com>
 */
public interface IMutableBundle extends IBundle
{
    /**
     * Adds a {@link IBundleLibrary} to the {@link IMutableBundle}.
     * 
     * @param library The library to add.
     */
    void addLibrary(IBundleLibrary library);

    /**
     * Adds a {@link ICompilerProblem} to the {@link IBundle#getProblems()}
     * collection.
     * 
     * @param problem The {@link IBundle} problem encountered.
     */
    void addProblem(ICompilerProblem problem);
}
