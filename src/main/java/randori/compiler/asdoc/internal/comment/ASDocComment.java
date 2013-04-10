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

package randori.compiler.asdoc.internal.comment;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.internal.parsing.as.ASDocToken;
import org.apache.flex.compiler.internal.parsing.as.ASDocTokenizer;
import org.apache.flex.compiler.internal.parsing.as.ASToken;
import org.apache.flex.compiler.internal.parsing.as.ASTokenTypes;
import org.apache.flex.compiler.tree.as.IDocumentableDefinitionNode;

import randori.compiler.asdoc.comment.IDocComment;
import randori.compiler.asdoc.comment.IDocTag;

public class ASDocComment implements IASDocComment, IDocComment
{
    private ASToken token;

    @SuppressWarnings("unused")
    private IDocumentableDefinitionNode node;

    private String description;

    Map<String, List<IDocTag>> tags = new TreeMap<String, List<IDocTag>>();

    public ASDocComment(ASToken token, IDocumentableDefinitionNode node)
    {
        this.token = token;
        this.node = node;
    }

    @Override
    public void compile()
    {
        if (token == null)
            return;

        String data = token.getText();
        ASDocTokenizer tokenizer = new ASDocTokenizer(false);
        tokenizer.setReader(new StringReader(data));
        ASDocToken tok = tokenizer.next();
        boolean foundDescription = false;
        DocTag pendingTag = null;

        try
        {
            while (tok != null)
            {
                if (!foundDescription
                        && tok.getType() == ASTokenTypes.TOKEN_ASDOC_TEXT)
                {
                    description = tok.getText();
                }
                else
                {
                    // do tags
                    if (tok.getType() == ASTokenTypes.TOKEN_ASDOC_TAG)
                    {
                        if (pendingTag != null)
                        {
                            addTag(pendingTag);
                            pendingTag = null;
                        }
                        pendingTag = new DocTag(tok.getText().substring(1));
                    }
                    else if (tok.getType() == ASTokenTypes.TOKEN_ASDOC_TEXT)
                    {
                        pendingTag.setDescription(tok.getText());
                        addTag(pendingTag);
                        pendingTag = null;
                    }
                }

                foundDescription = true;

                tok = tokenizer.next();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addTag(DocTag tag)
    {
        List<IDocTag> list = tags.get(tag.getName());
        if (list == null)
        {
            list = new ArrayList<IDocTag>();
            tags.put(tag.getName(), list);
        }
        list.add(tag);
    }

    @Override
    public String toString()
    {
        return "ASASDocComment [description=" + description + "]";
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public class DocTag implements IDocTag
    {
        private String name;
        private String description;

        public DocTag(String name)
        {
            if (name.indexOf('@') == 0)
                name = name.substring(1);
            this.setName(name);
        }

        public DocTag(String name, String description)
        {
            this(name);
            setDescription(description);
        }

        @Override
        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        @Override
        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        @Override
        public boolean hasDescription()
        {
            return description != null && !description.equals("");
        }
    }

    @Override
    public boolean hasTag(String name)
    {
        List<IDocTag> list = tags.get(name);
        if (list == null || list.size() == 0)
            return false;
        return true;
    }

    @Override
    public IDocTag getTag(String name)
    {
        List<IDocTag> list = tags.get(name);
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    @Override
    public List<IDocTag> getTags(String name)
    {
        List<IDocTag> result = new ArrayList<IDocTag>();
        List<IDocTag> list = tags.get(name);
        if (list == null)
            return result;

        for (IDocTag tag : list)
        {
            result.add(tag);
        }
        return result;
    }

    @Override
    public void paste(IDocComment source)
    {
        // 
        description = source.getDescription();
        IDocTag tag;
        if (source.hasTag("return"))
        {
            tag = source.getTag("return");
            addTag(new DocTag(tag.getName(), tag.getDescription()));
        }
        if (source.hasTag("param"))
        {
            List<IDocTag> tags = source.getTags("param");
            for (IDocTag ptag : tags)
            {
                addTag(new DocTag(ptag.getName(), ptag.getDescription()));
            }
        }
    }

}
