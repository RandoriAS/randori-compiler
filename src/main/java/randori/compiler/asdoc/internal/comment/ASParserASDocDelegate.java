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

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.asdoc.IASParserASDocDelegate;
import org.apache.flex.compiler.asdoc.IMetadataParserASDocDelegate;
import org.apache.flex.compiler.internal.parsing.as.ASToken;
import org.apache.flex.compiler.tree.as.IDocumentableDefinitionNode;

import antlr.Token;

public class ASParserASDocDelegate implements IASParserASDocDelegate
{

    private Token mAsDocToken;

    public ASParserASDocDelegate()
    {
    }

    @Override
    public void beforeVariable()
    {
    }

    @Override
    public void afterVariable()
    {
        //mAsDocToken = null;
    }

    @Override
    public void setCurrentASDocToken(Token asDocToken)
    {
        mAsDocToken = asDocToken;
    }

    @Override
    public IASDocComment afterDefinition(
            IDocumentableDefinitionNode definitionNode)
    {
        // this is the only way we can tell if this might be a license header
        if (mAsDocToken != null && mAsDocToken.getColumn() == 0
                && mAsDocToken.getLine() == 0)
        {
            mAsDocToken = null;
        }

        IASDocComment comment = new ASDocComment((ASToken) mAsDocToken,
                definitionNode);
        mAsDocToken = null;
        return comment;
    }

    @Override
    public IMetadataParserASDocDelegate getMetadataParserASDocDelegate()
    {
        return d;
    }

    private IMetadataParserASDocDelegate d = new IMetadataParserASDocDelegate() {

        @Override
        public void setCurrentASDocToken(Token asDocToken)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void clearMetadataComment(String metaDataTagName)
        {

        }

        @Override
        public void afterMetadata(int metaDataEndOffset)
        {

        }

        @Override
        public IASDocComment afterDefinition(
                IDocumentableDefinitionNode definitionNode)
        {
            IASDocComment comment = new ASDocComment((ASToken) mAsDocToken,
                    definitionNode);
            mAsDocToken = null;
            return comment;
        }
    };

}
