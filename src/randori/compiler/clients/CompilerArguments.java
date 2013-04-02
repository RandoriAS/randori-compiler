/***
 * Copyright 2013 Teoti Graphix, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.clients;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Schmalle
 */
public class CompilerArguments
{

    private List<String> libraries = new ArrayList<String>();

    private List<String> sources = new ArrayList<String>();

    private List<String> includes = new ArrayList<String>();

    private String appName = "";

    private String output;

    private String jsLibraryPath = "";

    private String jsBasePath = "";

    private boolean jsOutputAsFiles = true;

    public void addLibraryPath(String path)
    {
        if (libraries.contains(path))
            return;
        libraries.add(path);
    }

    public void addSourcepath(String path)
    {
        if (sources.contains(path))
            return;
        sources.add(path);
    }

    public void addIncludedSources(String path)
    {
        if (includes.contains(path))
            return;
        includes.add(path);
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String value)
    {
        appName = value;
    }

    public String getOutput()
    {
        return output;
    }

    public void setOutput(String path)
    {
        output = path;
    }

    public String getJsLibraryPath()
    {
        return jsLibraryPath;
    }

    public void setJsLibraryPath(String jsLibraryPath)
    {
        this.jsLibraryPath = jsLibraryPath;
    }

    public String getJsBasePath()
    {
        return jsBasePath;
    }

    public void setJsBasePath(String jsBasePath)
    {
        this.jsBasePath = jsBasePath;
    }

    public boolean isJsOutputAsFiles()
    {
        return jsOutputAsFiles;
    }

    public void setJsOutputAsFiles(boolean jsOutputAsFiles)
    {
        this.jsOutputAsFiles = jsOutputAsFiles;
    }

    public void clear()
    {
        jsBasePath = "";
        jsLibraryPath = "";
        jsOutputAsFiles = false;
        output = "";
        clearLibraries();
        clearSourcePaths();
    }

    public void clearSourcePaths()
    {
        sources = new ArrayList<String>();
    }

    public void clearLibraries()
    {
        libraries = new ArrayList<String>();
    }

    public String[] toArguments()
    {
        List<String> result = new ArrayList<String>();

        for (String arg : libraries)
        {
            result.add("-library-path=" + arg);
        }

        for (String arg : sources)
        {
            result.add("-source-path=" + arg);
        }

        for (String arg : includes)
        {
            result.add("-include-sources=" + arg);
        }

        // XXX TEMP
        String name = getAppName();
        if (!name.equals(""))
            result.add("-app-name=" + name);

        String base = getJsBasePath();
        if (!base.equals(""))
            result.add("-js-base-path=" + base);

        String library = getJsLibraryPath();
        if (!library.equals(""))
            result.add("-js-library-path=" + library);

        result.add("-js-classes-as-files="
                + (isJsOutputAsFiles() ? "true" : "false"));
        result.add("-output=" + getOutput());

        return result.toArray(new String[] {});
    }

    //    public void configure(Project project, RandoriProjectModel model)
    //    {
    //        setAppName(project.getName());
    //        setJsBasePath(model.getBasePath());
    //        setJsLibraryPath(model.getLibraryPath());
    //        setJsOutputAsFiles(model.isClassesAsFile());
    //        setOutput(project.getBasePath());
    //    }

    public static class CompilerArgument
    {
        private String name;

        private String value;

        CompilerArgument(String name, String value)
        {
            this.name = name;
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public static CompilerArgument create(String name, String value)
        {
            return new CompilerArgument(name, value);
        }
    }

}
