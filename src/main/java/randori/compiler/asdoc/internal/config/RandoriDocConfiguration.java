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

package randori.compiler.asdoc.internal.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.flex.compiler.config.Configuration;
import org.apache.flex.compiler.config.ConfigurationValue;
import org.apache.flex.compiler.exceptions.ConfigurationException.BadValue;
import org.apache.flex.compiler.internal.config.annotations.Arguments;
import org.apache.flex.compiler.internal.config.annotations.Config;
import org.apache.flex.compiler.internal.config.annotations.Mapping;
import randori.compiler.asdoc.internal.access.MemberType;

public class RandoriDocConfiguration extends Configuration
{

    public RandoriDocConfiguration()
    {
    }

    //
    // 'jasdoc.main-title' option
    //

    private String mainTitle;

    public String getMainTitle()
    {
        return mainTitle;
    }

    @Config(allowMultiple = false)
    @Mapping({ "jasdoc", "main-title" })
    public void setMainTitle(ConfigurationValue cv, String value)
            throws BadValue
    {
        mainTitle = value;
    }

    //
    // 'jasdoc.footer' option
    //

    private String footer;

    public String getFooter()
    {
        return footer;
    }

    @Config(allowMultiple = false)
    @Mapping({ "jasdoc", "footer" })
    public void setFooter(ConfigurationValue cv, String value) throws BadValue
    {
        footer = value;
    }

    /**
     * doc-member member [...]
     */
    private final List<String> docMember = new ArrayList<String>();

    private ArrayList<MemberType> docMemberTypes;

    @Config(compcOnly = true, allowMultiple = true)
    @Mapping("doc-member")
    @Arguments({ "member" })
    public void setDocMember(ConfigurationValue cv, List<String> values)
    {
        docMember.addAll(values);
        docMemberTypes = new ArrayList<MemberType>();
        for (String member : docMember)
        {
            MemberType type = MemberType.toType(member);
            if (type != null)
                docMemberTypes.add(type);
            //else XXX
            //    throw new Exception();
        }
    }

    public List<String> getDocMember()
    {
        return docMember;
    }

    public List<MemberType> getDocMemberTypes()
    {
        return docMemberTypes;
    }

    /**
     * doc-namespace namespace [...]
     */
    private final List<String> docNamespace = new ArrayList<String>();

    @Config(compcOnly = true, allowMultiple = true)
    @Mapping("doc-namespace")
    @Arguments({ "namespace" })
    public void setDocNamespace(ConfigurationValue cv, List<String> values)
    {
        docNamespace.addAll(values);
    }

    public List<String> getDocNamespace()
    {
        return docNamespace;
    }

    //
    // 'retain-member-order' option
    //
    private boolean retainMemberOrder = false;

    /**
     * @return true if dead code removal has been enabled.
     */
    public boolean getRetainMemberOrder()
    {
        return this.retainMemberOrder;
    }

    @Config(advanced = true)
    @Mapping({ "jasdoc", "retain-member-order" })
    public void setRetainMemberOrder(ConfigurationValue cfgval, boolean b)
    {
        this.retainMemberOrder = b;
    }
}
