/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.asdoc.internal.template.asdoc.panes;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.ClassListVO;
import randori.compiler.asdoc.internal.utils.HTMLUtils;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

/**
 * Renders the <code>output/all-classes.html</code> index.
 * <p>
 * This is the bottom left "All Classes" pane.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 */
public class ClassListPaneTemplate extends ASDocTemplateRenderer
{
    // --------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    // --------------------------------------------------------------------------

    public static final String NAME = "ClassListPaneProxy";

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public ClassListPaneTemplate(IDocConfiguration config)
    {
        super(config);
        setTitle(IASDocTemplate.CLASS_LIST_TITLE);

        setOutputFile(IASDocTemplate.CLASS_LIST_OUTPUT);
        setTemplateFile(IASDocTemplate.CLASS_LIST_TEMPLATE);
    }

    // --------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected Boolean renderTemplate()
    {
        List<ClassListVO> result = new ArrayList<ClassListVO>();
        Collection<ITypeDefinition> types = getConfiguration().getAccess()
                .getTypes();

        for (ITypeDefinition definition : types)
        {
            ClassListVO vo = new ClassListVO();

            String anchor = definition.getBaseName();

            vo.setName(anchor);
            vo.setAnchor(returnTypeListPaneLink(definition));
            vo.setIsClass(definition instanceof IClassDefinition);

            result.add(vo);
        }

        Collections.sort(result);

        set("pages", result);

        return true;
    }

    public String returnTypeListPaneLink(ITypeDefinition definition)
    {
        String qname = definition.getQualifiedName();
        String name = definition.getBaseName();
        String pname = definition.getPackageName();
        if (pname == null || pname.equals("toplevel"))
        {
            qname = name;
        }

        String path = HTMLUtils.convertTypeNameToPath(qname);

        StringWriter writer = new StringWriter();
        HTMLUtils.html(writer).a().attr("target", "classFrame")
                .attr("href", path).text(name).endAll();

        return writer.getBuffer().toString();
    }
}
