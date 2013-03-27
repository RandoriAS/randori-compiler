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

package randori.compiler.internal.driver;

import java.util.ArrayList;
import java.util.List;

import org.apache.flex.compiler.clients.problems.ProblemQuery;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.units.ICompilationUnit.UnitType;

import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriApplication;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.internal.driver.model.ApplicationModel;
import randori.compiler.internal.driver.model.RandoriGuiceModel;
import randori.compiler.internal.driver.model.RandoriModel;

/**
 * @author Michael Schmalle
 */
public class RandoriApplication implements IRandoriApplication
{
    @SuppressWarnings("unused")
    private final FlexProject project;

    private RandoriGuiceModel guice;

    private RandoriModel randori;

    private ApplicationModel application;

    private List<ICompilationUnit> compilationUnits;

    private IRandoriTargetSettings settings;

    public RandoriApplication(FlexProject project,
            List<ICompilationUnit> compilationUnits,
            IRandoriTargetSettings settings)
    {
        this.project = project;
        this.settings = settings;

        List<ICompilationUnit> units = new ArrayList<ICompilationUnit>();
        for (ICompilationUnit unit : compilationUnits)
        {
            if (unit.getCompilationUnitType() == UnitType.AS_UNIT)
                units.add(unit);
        }

        this.compilationUnits = units;
        
        // XXX need to check whether the lib paths contain Randori.swc or RandoriGuice.swc
        guice = new RandoriGuiceModel(project, settings);
        randori = new RandoriModel(project, settings);
        application = new ApplicationModel(project, settings);
    }

    @Override
    public boolean compile(IRandoriBackend backend, ProblemQuery problems)
    {
        filter(problems);
        generate(backend, problems);
        return true;
    }

    protected void filter(ProblemQuery problems)
    {
        guice.filter(compilationUnits);
        randori.filter(compilationUnits);
        application.filter(compilationUnits);
    }

    protected void generate(IRandoriBackend backend, ProblemQuery problems)
    {
        guice.generate(backend, problems.getProblems(), settings.getOutput());

        randori.generate(backend, problems.getProblems(), settings.getOutput());

        application.generate(backend, problems.getProblems(),
                settings.getOutput());
    }

}
