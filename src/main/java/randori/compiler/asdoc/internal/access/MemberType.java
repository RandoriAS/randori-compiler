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

package randori.compiler.asdoc.internal.access;

public enum MemberType
{
    VARIALBE("variable"),
    CONSTANT("constant"),
    ACCESSOR("accessor"),
    METHOD("method"),
    EVENT("event"),
    EFFECT("effect"),
    STYLE("style"),
    SKINPART("skinpart"),
    SKINSTATE("skinstate");

    private String mValue;

    MemberType(String value)
    {
        mValue = value;
    }

    public String getValue()
    {
        return mValue;
    }

    public static MemberType toType(String type)
    {
        for (MemberType mt : values())
        {
            if (mt.getValue().equals(type))
                return mt;
        }
        return null;
    }

    @Override
    public String toString()
    {
        return getValue();
    }
}
