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

import java.util.HashMap;
import java.util.Map;

public class MemberNames
{
    private static final Map<MemberType, Map<String, String>> map = new HashMap<MemberType, Map<String, String>>();

    private static final String SINGULAR = "singular";

    private static final String PLURAL = "plural";

    private static final String USINGULAR = "ucsingular";

    private static final String UPLURAL = "ucplural";

    static
    {
        Map<String, String> constants = new HashMap<String, String>();
        constants.put(SINGULAR, "constant");
        constants.put(PLURAL, "constants");
        constants.put("ucsingular", "Constant");
        constants.put(UPLURAL, "Constants");

        Map<String, String> variables = new HashMap<String, String>();
        variables.put(SINGULAR, "variable");
        variables.put(PLURAL, "variables");
        variables.put(USINGULAR, "Variable");
        variables.put(UPLURAL, "Variables");

        Map<String, String> accessors = new HashMap<String, String>();
        accessors.put(SINGULAR, "accessor");
        accessors.put(PLURAL, "accessors");
        accessors.put(USINGULAR, "Accessor");
        accessors.put(UPLURAL, "Accessors");

        Map<String, String> methods = new HashMap<String, String>();
        methods.put(SINGULAR, "method");
        methods.put(PLURAL, "methods");
        methods.put(USINGULAR, "Method");
        methods.put(UPLURAL, "Methods");

        Map<String, String> styles = new HashMap<String, String>();
        styles.put(SINGULAR, "style");
        styles.put(PLURAL, "styles");
        styles.put(USINGULAR, "Style");
        styles.put(UPLURAL, "Styles");

        Map<String, String> skinstates = new HashMap<String, String>();
        skinstates.put(SINGULAR, "skinstate");
        skinstates.put(PLURAL, "skinstates");
        skinstates.put(USINGULAR, "SkinState");
        skinstates.put(UPLURAL, "SkinStates");

        Map<String, String> skinparts = new HashMap<String, String>();
        skinparts.put(SINGULAR, "skinpart");
        skinparts.put(PLURAL, "skinparts");
        skinparts.put(USINGULAR, "SkinPart");
        skinparts.put(UPLURAL, "SkinParts");

        Map<String, String> events = new HashMap<String, String>();
        events.put(SINGULAR, "event");
        events.put(PLURAL, "events");
        events.put(USINGULAR, "Event");
        events.put(UPLURAL, "Events");

        Map<String, String> effects = new HashMap<String, String>();
        effects.put(SINGULAR, "effect");
        effects.put(PLURAL, "effects");
        effects.put(USINGULAR, "Effect");
        effects.put(UPLURAL, "Effects");

        map.put(MemberType.CONSTANT, constants);
        map.put(MemberType.VARIALBE, variables);
        map.put(MemberType.ACCESSOR, accessors);
        map.put(MemberType.METHOD, methods);
        map.put(MemberType.STYLE, styles);
        map.put(MemberType.SKINSTATE, skinstates);
        map.put(MemberType.SKINPART, skinparts);
        map.put(MemberType.EVENT, events);
        map.put(MemberType.EFFECT, effects);
    }

    public static Map<String, String> getMap(MemberType type)
    {
        if (map.containsKey(type))
            return map.get(type);
        return null;
    }
}
