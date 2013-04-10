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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.ITypeDefinition;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.internal.access.MemberNames;
import randori.compiler.asdoc.internal.access.MemberType;
import randori.compiler.asdoc.internal.template.asdoc.ASDocTemplateRenderer;
import randori.compiler.asdoc.internal.template.asdoc.data.SubNavigationVO;
import randori.compiler.asdoc.internal.template.asdoc.rows.DetailTableTemplate;
import randori.compiler.asdoc.internal.template.asdoc.rows.SummaryTableTemplate;
import org.apache.velocity.util.StringUtils;

public class ClassDetailPageContentTemplate extends ASDocTemplateRenderer
{
    private ClassDetailSignatureTemplate signature;
    private ClassDetailDescriptionTemplate description;

    private Map<String, List<String>> sumaries = new HashMap<String, List<String>>();
    private Map<String, List<String>> details = new HashMap<String, List<String>>();

    private ClassDetailPageFactory factory = new ClassDetailPageFactory();

    // --------------------------------------------------------------------------
    //
    // Public Get-Set :: Methods
    //
    // --------------------------------------------------------------------------

    // ----------------------------------
    // signature
    // ----------------------------------

    /**
     * @return the signature
     */
    public ClassDetailSignatureTemplate getSignature()
    {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(ClassDetailSignatureTemplate signature)
    {
        this.signature = signature;
        set("signature", signature);
    }

    // ----------------------------------
    // description
    // ----------------------------------

    /**
     * @return the description
     */
    public ClassDetailDescriptionTemplate getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(ClassDetailDescriptionTemplate description)
    {
        this.description = description;
        set("description", description);
    }

    public ClassDetailPageContentTemplate(IDocConfiguration config)
    {
        super(config);
    }

    @Override
    protected Boolean renderTemplate()
    {
        ITypeDefinition definition = (ITypeDefinition) getDefinition();

        System.out.println("Render : " + definition.getQualifiedName());

        getConvertor().setPackageContext(
                getConfiguration().getAccess().getPackageBundle(definition));
        getConvertor().setRenderType(definition);

        renderBodyDetail(definition);
        renderMembers(definition);

        set("summaries", sumaries);
        set("details", details);

        if (getConfiguration().isTagActive("source"))
        {
            set("source", getConvertor().returnSourceTag(getDefinition()));
        }

        getConvertor().setPackageContext(null);
        getConvertor().setRenderType(null);

        return false;
    }

    private void renderBodyDetail(ITypeDefinition definition)
    {
        ClassDetailSignatureTemplate signature = new ClassDetailSignatureTemplate(
                getConfiguration());
        ClassDetailDescriptionTemplate description = new ClassDetailDescriptionTemplate(
                getConfiguration());

        description.setDefinition(definition);

        initialize(signature);
        initialize(description);

        setSignature(signature);
        setDescription(description);
    }

    protected void renderMembers(ITypeDefinition definition)
    {
        if (definition instanceof IClassDefinition)
        {
            renderClassDetail();
        }
        else if (definition instanceof IInterfaceDefinition)
        {
            renderInterfaceDetail();
        }
    }

    protected void renderClassDetail()
    {
        setSubNavigation(new SubNavigationVO());

        List<MemberType> members = getConfiguration().getDocMemberTypes();
        List<String> namespaces = getConfiguration().getDocNamespace();

        for (String visibility : namespaces)
        {
            for (MemberType type : members)
            {
                boolean show = renderElements(type, visibility);
                if (show)
                {
                    //System.out
                    //        .println("   render - " + visibility + ":" + type);
                }
                if (visibility.equals("public"))
                {
                    getSubNavigation().setSubNavItem(type, show);
                }
            }
        }

        //ITypeDefinition e = (ITypeDefinition) getDefinition();

        // if (e.getDocumentation().hasDocTag("mxml")) {
        // set("classFileMXMLBlock", new ClassFileMXMLBlock());
        // }
    }

    protected void renderInterfaceDetail()
    {
        setSubNavigation(new SubNavigationVO());

        List<MemberType> kinds = getConfiguration().getDocMemberTypes();

        // XXX TEMP
        List<MemberType> ikinds = new ArrayList<MemberType>();
        //if (kinds == null)
        //    return;
        
        if (kinds.contains(MemberType.ACCESSOR))
            ikinds.add(MemberType.ACCESSOR);
        if (kinds.contains(MemberType.METHOD))
            ikinds.add(MemberType.METHOD);

        List<String> visibilities = new ArrayList<String>();
        visibilities.add("public");

        for (String visibility : visibilities)
        {
            for (MemberType type : ikinds)
            {
                boolean show = renderElements(type, visibility);
                if (show)
                {
                    // System.out
                    //         .println("   render - " + visibility + ":" + type);
                }
                getSubNavigation().setSubNavItem(type, show);
            }
        }
    }

    protected boolean renderElements(MemberType type, String namespace)
    {
        List<IDefinition> members = getMembers(
                (ITypeDefinition) getDefinition(), type, namespace, true);

        if (members == null || members.size() == 0)
            return false;

        List<IDefinition> result = new ArrayList<IDefinition>();

        for (IDefinition member : members)
        {
            boolean add = true;
            if (member instanceof IFunctionDefinition)
            {
                IFunctionDefinition fdef = (IFunctionDefinition) member;

                if (fdef.isConstructor() && fdef.getParent() != getDefinition())
                {
                    add = false;
                }
            }

            if (add)
                result.add(member);
        }

        boolean retain = getConfiguration().getRetainMemberOrder();

        if (!retain)
            Collections.sort(result, new MemberComparator());

        renderSummaries(result, type, namespace);
        renderDetails(result, type, namespace);

        return true;
    }

    protected void renderSummaries(List<IDefinition> members, MemberType type,
            String modifier)
    {
        SummaryTableTemplate summary = factory.createSummaryAndSet(type,
                members, modifier, getConfiguration());

        Map<String, String> map = MemberNames.getMap(type);

        String ucmodifier = StringUtils.capitalizeFirstLetter(modifier);

        // String singular = map.get( "singular" );
        // String plural = map.get( "plural" );
        String ucsingular = map.get("ucsingular");
        String ucplural = map.get("ucplural");

        summary.setMainHeaderText(ucmodifier + " " + ucplural);

        summary.setTitleID(ucmodifier + ucsingular);
        summary.setLcTitleID(modifier + ucsingular);
        summary.setInheritedTitle(ucmodifier + ucplural);
        summary.setHeaderText(ucsingular);
        summary.setUcModifier(ucmodifier);
        summary.setUcTypeSingular(ucsingular);

        initialize(summary);

        addSummary(type, summary);
    }

    private void renderDetails(List<IDefinition> elements, MemberType type,
            String modifier)
    {
        DetailTableTemplate tpl = factory.createDetailAndSet(type, elements,
                modifier, getConfiguration());

        if (tpl == null)
            return;

        Map<String, String> map = MemberNames.getMap(type);

        String ucmodifier = StringUtils.capitalizeFirstLetter(modifier);

        String singular = map.get("singular");
        // String plural = map.get( "plural" );
        String ucsingular = map.get("ucsingular");
        // String ucplural = map.get( "ucplural" );
        tpl.setProperty("lcModifier", singular);

        tpl.setProperty("ucModifier", ucmodifier);
        tpl.setProperty("ucTypeSingular", ucsingular);

        initialize(tpl);

        List<String> l = details.get(type.toString());
        if (l == null)
        {
            l = new ArrayList<String>();
            details.put(type.toString(), l);
        }

        l.add(tpl.render());
    }

    @SuppressWarnings("unchecked")
    public List<IDefinition> getMembers(ITypeDefinition element,
            MemberType type, String namespace, boolean inherit)
    {
        @SuppressWarnings("rawtypes")
        List elements = null;

        if (type.equals(MemberType.VARIALBE))
        {
            elements = getConfiguration().getAccess().getVariables(
                    (IClassDefinition) element, namespace, inherit);
        }
        else if (type.equals(MemberType.CONSTANT))
        {
            elements = getConfiguration().getAccess().getConstants(
                    (IClassDefinition) element, namespace, inherit);
        }
        else if (type.equals(MemberType.ACCESSOR))
        {
            elements = getConfiguration().getAccess().getAccessors(element,
                    namespace, inherit);
        }
        else if (type.equals(MemberType.METHOD))
        {
            elements = getConfiguration().getAccess().getFunctions(element,
                    namespace, inherit);
        }
        else if (type.equals(MemberType.EVENT))
        {
            elements = getConfiguration().getAccess().getEvents(
                    (IClassDefinition) element, namespace, inherit);
        }
        else if (type.equals(MemberType.EFFECT))
        {
            elements = getConfiguration().getAccess().getEffects(
                    (IClassDefinition) element, namespace, inherit);
        }
        else if (type.equals(MemberType.STYLE))
        {
            elements = getConfiguration().getAccess().getStyles(
                    (IClassDefinition) element, namespace, inherit);
        }
        else if (type.equals(MemberType.SKINPART))
        {
            elements = getConfiguration().getAccess().getSkinParts(
                    (IClassDefinition) element, namespace, inherit);
        }
        else if (type.equals(MemberType.SKINSTATE))
        {
            elements = getConfiguration().getAccess().getSkinStates(
                    (IClassDefinition) element, namespace, inherit);
        }
        return elements;
    }

    private void addSummary(MemberType type, SummaryTableTemplate summary)
    {
        List<String> list = sumaries.get(type.toString());
        if (list == null)
        {
            list = new ArrayList<String>();
            sumaries.put(type.toString(), list);
        }

        list.add(summary.render());
    }

    class MemberComparator implements Comparator<IDefinition>
    {
        MemberComparator()
        {
        }

        @Override
        public int compare(IDefinition one, IDefinition two)
        {
            return one.getBaseName().compareTo(two.getBaseName());
        }
    }
}

class ClassDetailPageFactory
{
    public SummaryTableTemplate createSummaryAndSet(MemberType type,
            List<IDefinition> elements, String modifier,
            IDocConfiguration config)
    {
        SummaryTableTemplate template = new SummaryTableTemplate(config);
        template.setDefinitions(elements);
        return template;
    }

    public DetailTableTemplate createDetailAndSet(MemberType type,
            List<IDefinition> elements, String modifier,
            IDocConfiguration config)
    {
        DetailTableTemplate template = new DetailTableTemplate(config);
        template.setElements(elements);
        return template;
    }
}
