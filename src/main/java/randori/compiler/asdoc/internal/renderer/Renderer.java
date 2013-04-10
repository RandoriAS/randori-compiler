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

package randori.compiler.asdoc.internal.renderer;

import java.util.ArrayList;
import java.util.List;

import randori.compiler.asdoc.config.IDocConfiguration;
import randori.compiler.asdoc.render.IRenderAware;
import randori.compiler.asdoc.render.IRenderer;

public abstract class Renderer implements IRenderer
{

    private IDocConfiguration configuration;

    public IDocConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(IDocConfiguration value)
    {
        configuration = value;
    }

    private List<IRenderAware> renderers = new ArrayList<IRenderAware>();

    public Renderer()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void render()
    {
        prerender();

        for (IRenderAware client : renderers)
        {
            client.render();
        }
    }

    /**
     * Creates the child renderers that will be called during
     * <code>render()</code>, use <code>addItem()</code>.
     */
    abstract protected void createRenderers();

    protected void prerender()
    {
        createRenderers();
    }

    /**
     * Adds the <code>IRenderClient</code> to the renderers collection.
     * 
     * @param renderer An <code>IRenderClient</code> that will render an output
     * page.
     */
    protected void addRenderClient(IRenderAware renderer)
    {
        if (!renderers.contains(renderer))
        {
            renderers.add(renderer);
        }
    }
}
