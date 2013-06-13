package randori.compiler.internal.config;

import java.util.ArrayList;
import java.util.List;

public class PathCollection
{
    private String name;

    public String getName()
    {
        return name;
    }

    private List<String> paths = new ArrayList<String>();

    public List<String> getPaths()
    {
        return paths;
    }

    public PathCollection(String name)
    {
        this.name = name;
    }

    public void addPath(String path)
    {
        paths.add(path);
    }
}
