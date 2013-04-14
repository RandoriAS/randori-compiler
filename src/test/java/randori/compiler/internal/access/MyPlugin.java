package randori.compiler.internal.access;

import randori.compiler.plugin.IPreProcessPlugin;
import randori.compiler.projects.IRandoriApplicationProject;

public class MyPlugin implements IPreProcessPlugin
{
    public MyPlugin()
    {
    }

    @Override
    public void analyze(IRandoriApplicationProject project)
    {
        //        ASWalker projectAccessWalker = new ASWalker(new ProjectAccessVisitor(
        //                project.getTargetSettings().getProjectAccess()));
    }
}
