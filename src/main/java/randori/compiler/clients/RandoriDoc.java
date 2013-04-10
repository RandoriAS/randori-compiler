package randori.compiler.clients;

import java.io.IOException;

import org.apache.flex.compiler.asdoc.IASDocDelegate;
import org.apache.flex.compiler.clients.MXMLC;
import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.common.VersionInfo;
import org.apache.flex.compiler.config.Configurator;
import org.apache.flex.compiler.exceptions.ConfigurationException;
import randori.compiler.asdoc.access.IASProjectAccess;
import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.access.ProjectAccess;
import randori.compiler.asdoc.internal.comment.ASDocDelegate;
import randori.compiler.asdoc.internal.config.DocConfiguration;
import randori.compiler.asdoc.internal.config.RandoriDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocConverter;
import randori.compiler.asdoc.internal.template.asdoc.ASDocRenderer;
import randori.compiler.asdoc.internal.template.asdoc.ASDocRowConverter;
import randori.compiler.asdoc.template.asdoc.IASDocConverter;
import randori.compiler.asdoc.template.asdoc.IASDocRowConverter;

public class RandoriDoc extends MXMLC
{
    public RandoriDoc()
    {
        super();
        // create the workspace
        // create the project

        // create the asdoc delegate
        IASDocDelegate asDocDelegate = new ASDocDelegate();
        workspace.setASDocDelegate(asDocDelegate);

        problems = new ProblemQuery();
    }

    /**
     * Entry point for <code>compc</code> tool.
     * 
     * @param args command line arguments
     */
    public static void main(final String[] args)
    {
        int exitCode = staticMainNoExit(args);
        System.exit(exitCode);
    }

    /**
     * Entry point for the {@code <compc>} Ant task.
     * 
     * @param args Command line arguments.
     * @return An exit code.
     */
    public static int staticMainNoExit(final String[] args)
    {
        final RandoriDoc jasdoc = new RandoriDoc();
        return jasdoc.mainNoExit(args);
    }

    @Override
    protected boolean configure(String[] args)
    {
        return super.configure(args);
    }

    @Override
    protected Configurator createConfigurator()
    {
        return new Configurator(RandoriDocConfiguration.class);
    }

    @Override
    protected String getProgramName()
    {
        return "randoridoc";
    }

    @Override
    protected String getStartMessage()
    {
        String message = "Randori ASDoc Documentation Compiler (randoridoc)" + "\n"
                + VersionInfo.buildMessage() + "\n";
        return message;
    }

    @Override
    protected void buildArtifact() throws InterruptedException, IOException
    {
        System.out.println("buildArtifact()");
        IASProjectAccess access = new ProjectAccess(project);
        System.out.println("Proccessing, Parsing...");
        access.process();

        // temp
        IDocConfiguration config = new DocConfiguration();
        RandoriDocConfiguration configuration = (RandoriDocConfiguration) projectConfigurator
                .getConfiguration();
        config.setConfiguration(configuration);
        
        IASDocConverter converter = new ASDocConverter(config);
        IASDocRowConverter rowConverter = new ASDocRowConverter(config);
        config.setAccess(access);
        config.setConverter(converter);
        config.setRowConverter(rowConverter);
        System.out.println("Rendering");
        ASDocRenderer renderer = new ASDocRenderer();
        renderer.setConfiguration(config);
        renderer.render();

        System.out.println("Done");
    }

    /**
     * Compc uses the target file as the output SWC file name. Nothing needs to
     * be done here.
     */
    @Override
    protected boolean setupTargetFile() throws InterruptedException
    {
        return true;
    }

    /**
     * Compc uses the target file as the output SWC file name if provided.
     * Nothing needs to be done here.
     */
    @Override
    protected void validateTargetFile() throws ConfigurationException
    {
    }

    //-----------------------------------------------------------------

}
