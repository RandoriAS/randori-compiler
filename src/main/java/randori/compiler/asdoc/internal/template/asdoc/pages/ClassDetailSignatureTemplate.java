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

package randori.compiler.asdoc.internal.template.asdoc.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IClassDefinition.IClassIterator;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;
import org.apache.flex.compiler.definitions.references.IReference;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.ClassListVO;
import randori.compiler.asdoc.internal.utils.HTMLUtils;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

class ClassDetailSignatureTemplate extends ASDocTemplateRenderer
{
    private static final String SUPERCLASSES = "superclasses";
    private static final String IMPLEMENTATIONS = "implementations";
    private static final String SUBCLASSES = "subclasses";
    private static final String SUPERINTERFACES = "superinterfaces";
    private static final String INTERFACEIMPLEMENTORS = "interfaceimplementors";
    private static final String SUBINTERFACES = "subinterfaces";
    private String packageName;
    private String fileType;
    private String modifiers;
    private String type;
    private String name;

    @Override
    public void setDefinition(IDefinition definition)
    {
        super.setDefinition(definition);
        ITypeDefinition type = (ITypeDefinition) definition;
        
        setPackageName(type.getPackageName());
        setModifiers(HTMLUtils.toModifierString(type.getModifiers()));
        set("namespace", "public");
        setFileType(HTMLUtils.toFileType(type));
        setType(HTMLUtils.toFileType(type).toLowerCase());
        setName(type.getBaseName());
    }

    // --------------------------------------------------------------------------
    //
    // Constructor
    //
    // --------------------------------------------------------------------------

    /**
     * @return the packageName
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
        set("packageName", packageName);
    }

    /**
     * @return the fileType
     */
    public String getFileType()
    {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
        set("fileType", fileType);
    }

    /**
     * @return the modifiers
     */
    public String getModifiers()
    {
        return modifiers;
    }

    /**
     * @param modifiers the modifiers to set
     */
    public void setModifiers(String modifiers)
    {
        this.modifiers = modifiers;
        set("modifiers", modifiers);
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
        set("type", type);
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
        set("name", name);
    }

    /**
     * Constructor.
     */
    public ClassDetailSignatureTemplate(IDocConfiguration config)
    {
        super(config);

        setTemplateFile(IASDocTemplate.CLASS_DETAIL_SIGNATURE_TEMPLATE);

        set(SUPERCLASSES, new ArrayList<ClassListVO>());
        set(SUBCLASSES, new ArrayList<ClassListVO>());
        set(IMPLEMENTATIONS, new ArrayList<ClassListVO>());
    }

    @Override
    protected Boolean renderTemplate()
    {
        ITypeDefinition type = (ITypeDefinition) getDefinition();

        if (type instanceof IClassDefinition)
        {
            renderSuperClasses((IClassDefinition) type);
            renderImplementations((IClassDefinition) type);
            renderSubClasses((IClassDefinition) type);
        }
        else if (type instanceof IInterfaceDefinition)
        {
            renderSuperInterface((IInterfaceDefinition) type);
            renderInterfaceImplementors((IInterfaceDefinition) type);
            renderSubInterfaces((IInterfaceDefinition) type);
        }

        return false;
    }

    private void renderSuperClasses(IClassDefinition element)
    {
        List<ClassListVO> rsupers = new ArrayList<ClassListVO>();

        IClassIterator i = element.classIterator(getConfiguration().getAccess()
                .getProject(), false);
        boolean isSubType = i.hasNext();

        if (isSubType)
        {

            ClassListVO vo = new ClassListVO();
            vo.setName(element.getBaseName());
            vo.setAnchor(element.getBaseName());
            rsupers.add(vo);

            while (i.hasNext())
            {
                IClassDefinition superDef = (IClassDefinition) i.next();
                vo = new ClassListVO();
                vo.setName(superDef.getBaseName());

                String anchor = vo.getName();
                anchor = getConvertor().returnTypeContentLink(superDef,
                        getConvertor().getPackageContext());

                vo.setAnchor(anchor);
                rsupers.add(vo);

                if (!i.hasNext())
                {

                    // check builtin types
                    IReference ref = superDef.getBaseClassReference();
                    if (ref != null)
                    {
                        vo = new ClassListVO();
                        vo.setName(ref.getDisplayString());
                        anchor = vo.getName();
                        vo.setAnchor(anchor);
                        rsupers.add(vo);
                    }
                    vo.setRowFlag(true);
                }
            }

        }
        else
        {
            // XXX duplicate code, get refactored
            ClassListVO vo = new ClassListVO();
            vo.setName(element.getBaseName());
            vo.setAnchor(element.getBaseName());
            rsupers.add(vo);

            // built in type
            IReference ref = element.getBaseClassReference();
            if (ref != null)
            {
                vo = new ClassListVO();
                vo.setRowFlag(true);
                vo.setName(ref.getDisplayString());
                vo.setAnchor(ref.getDisplayString());
                rsupers.add(vo);
            }
        }

        set(SUPERCLASSES, rsupers);
    }

    private void renderImplementations(IClassDefinition element)
    {
        List<ClassListVO> implementations = new ArrayList<ClassListVO>();

        Iterator<IInterfaceDefinition> i = element
                .interfaceIterator(getConfiguration().getAccess().getProject());

        if (i.hasNext())
        {
            while (i.hasNext())
            {
                IInterfaceDefinition def = (IInterfaceDefinition) i.next();
                ClassListVO vo = new ClassListVO();
                vo.setName(def.getBaseName());
                String anchor = vo.getName();

                anchor = getConvertor().returnTypeContentLink(def,
                        getConvertor().getPackageContext());

                vo.setAnchor(anchor);

                implementations.add(vo);
            }
        }

        sortAndSetLast(implementations);

        set(IMPLEMENTATIONS, implementations);
    }

    void sortAndSetLast(List<ClassListVO> list)
    {
        Collections.sort(list);
        if (list.size() > 0)
            list.get(list.size() - 1).setRowFlag(true);
    }

    private void renderSubClasses(IClassDefinition element)
    {
        List<ClassListVO> result = new ArrayList<ClassListVO>();
        Collection<IClassDefinition> subs = getConfiguration().getAccess()
                .getSubClasses(element);

        if (subs != null && subs.size() > 0)
        {
            for (IClassDefinition selement : subs)
            {
                ClassListVO vo = new ClassListVO();
                vo.setName(selement.getBaseName());

                String anchor = vo.getName();
                anchor = getConvertor().returnTypeContentLink(selement,
                        getConvertor().getPackageContext());

                vo.setAnchor(anchor);

                result.add(vo);
            }
        }

        sortAndSetLast(result);

        set(SUBCLASSES, result);
    }

    private void renderSuperInterface(IInterfaceDefinition definition)
    {
        List<ClassListVO> result = new ArrayList<ClassListVO>();

        Iterator<IInterfaceDefinition> i = definition.interfaceIterator(
                getConfiguration().getAccess().getProject(), false);
        while (i.hasNext())
        {
            IInterfaceDefinition def = (IInterfaceDefinition) i.next();
            ClassListVO vo = new ClassListVO();
            vo.setName(def.getBaseName());

            String anchor = vo.getName();
            anchor = getConvertor().returnTypeContentLink(def,
                    getConvertor().getPackageContext());

            vo.setAnchor(anchor);

            result.add(vo);
        }

        sortAndSetLast(result);

        set(SUPERINTERFACES, result);
    }

    private void renderInterfaceImplementors(IInterfaceDefinition element)
    {
        List<ClassListVO> result = new ArrayList<ClassListVO>();

        Collection<IClassDefinition> supers = getConfiguration().getAccess()
                .getInterfaceImplementors(element);
        if (supers != null)
        {
            ClassListVO vo = new ClassListVO();

            for (IClassDefinition celement : supers)
            {
                vo = new ClassListVO();
                vo.setName(celement.getBaseName());

                String anchor = vo.getName();
                anchor = getConvertor().returnTypeContentLink(celement,
                        getConvertor().getPackageContext());

                vo.setAnchor(anchor);

                result.add(vo);
            }
        }

        sortAndSetLast(result);

        set(INTERFACEIMPLEMENTORS, result);
    }

    private void renderSubInterfaces(IInterfaceDefinition definition)
    {
        List<ClassListVO> result = new ArrayList<ClassListVO>();

        Collection<IInterfaceDefinition> supers = getConfiguration()
                .getAccess().getSubInterfaces(definition);
        if (supers != null)
        {
            ClassListVO vo = new ClassListVO();
            for (IInterfaceDefinition celement : supers)
            {
                vo = new ClassListVO();
                vo.setName(celement.getBaseName());

                String anchor = vo.getName();
                anchor = getConvertor().returnTypeContentLink(celement,
                        getConvertor().getPackageContext());

                vo.setAnchor(anchor);

                result.add(vo);
            }
        }

        sortAndSetLast(result);

        set(SUBINTERFACES, result);
    }
}
