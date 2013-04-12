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

package randori.compiler.asdoc.internal.template.asdoc;

import java.io.File;
import java.util.Collection;

import org.apache.flex.compiler.definitions.ITypeDefinition;
import randori.compiler.asdoc.access.IPackageBundle;
import randori.compiler.asdoc.internal.renderer.Renderer;
import randori.compiler.asdoc.internal.template.asdoc.pages.ClassDetailPageContentTemplate;
import randori.compiler.asdoc.internal.template.asdoc.pages.ClassListPageContentTemplate;
import randori.compiler.asdoc.internal.template.asdoc.pages.FramePageTemplate;
import randori.compiler.asdoc.internal.template.asdoc.pages.PackageListPageContentTemplate;
import randori.compiler.asdoc.internal.template.asdoc.panes.ClassListPaneTemplate;
import randori.compiler.asdoc.internal.template.asdoc.panes.PackageListDetailPaneTemplate;
import randori.compiler.asdoc.internal.template.asdoc.panes.PackageListPaneTemplate;
import randori.compiler.asdoc.internal.utils.HTMLUtils;
import randori.compiler.asdoc.template.asdoc.IASDocTemplate;

public class ASDocRenderer extends Renderer
{

    public ASDocRenderer()
    {
        super();
    }

    @Override
    protected void createRenderers()
    {
        // creates the whole index.html frame with
        createIndexFrame();

        // package-frame.html (packageFrame)
        createPackageFrame();
        // createIndexList();

        // title-bar.html (titlebar)
        createTitleBarPage();

        // package-summary.html (classFrame)
        createPackageListPane();
        createClassListPane();

        //
        createPackageListPage();
        createClassListPage();

        Collection<IPackageBundle> packages = getConfiguration().getAccess()
                .getPackageBundles();
        for (IPackageBundle bundle : packages)
        {
            createPackageListDetailPane(bundle);
            createPackageListDetailPage(bundle);
        }

        Collection<ITypeDefinition> types = getConfiguration().getAccess()
                .getTypes();
        for (ITypeDefinition type : types)
        {
            createClassDetailPage(type);
        }

        // createIndexPages();
    }

    // --------------------------------------------------------------------------
    //
    // IAS3RendererFactory API :: Methods
    //
    // --------------------------------------------------------------------------

    /**
     * Creates the index.html.
     */
    public void createIndexFrame()
    {
        addRenderClient(new IndexFrameTemplate(getConfiguration()));
    }

    /**
     * Creates the package-frame.html.
     */
    public void createPackageFrame()
    {
        addRenderClient(new PackageFrameTemplate(getConfiguration()));
    }

    /**
     * Creates the titlebar.html.
     */
    public void createTitleBarPage()
    {
        addRenderClient(new TitleBarPageTemplate(getConfiguration()));
    }

    /**
     * Creates the package-list.html
     */
    public void createPackageListPane()
    {
        addRenderClient(new PackageListPaneTemplate(getConfiguration()));
    }

    /**
     * Creates the all-classes.html
     */
    public void createClassListPane()
    {
        addRenderClient(new ClassListPaneTemplate(getConfiguration()));
    }

    /**
     * output/package-summary.html
     */
    public void createPackageListPage()
    {
        FramePageTemplate renderer = new FramePageTemplate(getConfiguration());
        renderer.setTitle(IASDocTemplate.PACKAGE_LIST_TITLE);
        renderer.setSubTitle(IASDocTemplate.PACKAGE_LIST_TITLE);
        renderer.setOutputFile(IASDocTemplate.PACKAGE_SUMMARY_OUTPUT);
        renderer.setBasePath("");

        renderer.setContentTemplateFile(new File(
                IASDocTemplate.PACKAGE_SUMMARY_TEMPLATE));

        addRenderClient(renderer);

        renderer.setContentRendererInstance(new PackageListPageContentTemplate(
                getConfiguration()));
    }

    /**
     * output/class-summary.html
     */
    public void createClassListPage()
    {
        FramePageTemplate renderer = new FramePageTemplate(getConfiguration());
        renderer.setTitle(IASDocTemplate.CLASS_LIST_TITLE);
        renderer.setSubTitle(IASDocTemplate.CLASS_LIST_TITLE);
        renderer.setOutputFile(IASDocTemplate.CLASS_SUMMARY_OUTPUT);
        renderer.setBasePath("");

        renderer.setContentTemplateFile(new File(
                IASDocTemplate.CLASS_SUMMARY_TEMPLATE));

        addRenderClient(renderer);

        renderer.setContentRendererInstance(new ClassListPageContentTemplate(
                getConfiguration()));
    }

    private void createPackageListDetailPane(IPackageBundle bundle)
    {
        String title = bundle.getQualifiedName();
        String packageName = bundle.getQualifiedName();

        String dir = packageName;

        if (packageName.equals("toplevel"))
        {
            title = "Top Level";
            dir = "";
        }

        PackageListDetailPaneTemplate proxy = new PackageListDetailPaneTemplate(
                getConfiguration());

        proxy.setTitle(title);
        // proxy.setSubTitle( title );
        proxy.setOutputFile(new File(HTMLUtils.returnClassListLinkSimple(dir)));
        proxy.setTemplateFile(IASDocTemplate.PACKAGE_LIST_DETAIL_PANE_TEMPLATE);
        proxy.setBasePath(HTMLUtils.returnReletiveBasePath(dir, "\\."));
        proxy.setBundle(bundle);

        addRenderClient(proxy);
    }

    /**
     * output/class-summary.html
     */
    private void createPackageListDetailPage(IPackageBundle bundle)
    {
        String title = bundle.getQualifiedName();
        String packageName = bundle.getQualifiedName();

        String dir = packageName;

        if (packageName.equals("toplevel"))
        {
            title = "Top Level";
            dir = "";
        }

        FramePageTemplate proxy = new FramePageTemplate(getConfiguration());

        proxy.setTitle(title);
        proxy.setSubTitle(title);
        proxy.setOutputFile(new File(HTMLUtils
                .returnPackageDetailLinkSimple(dir)));
        proxy.setContentTemplateFile(new File(
                IASDocTemplate.PACKAGE_LIST_DETAIL_PAGE_TEMPLATE));
        proxy.setBasePath(HTMLUtils.returnReletiveBasePath(dir, "\\."));
        proxy.setBundle(bundle);

        addRenderClient(proxy);

        proxy.setContentRendererInstance(new PackageListDetailContentTemplate(
                getConfiguration()));
    }

    private void createClassDetailPage(ITypeDefinition element)
    {
        String output = HTMLUtils.returnFullHTMLPath(element);
        String title = element.getBaseName();

        String dir = element.getPackageName();

        if (element.getPackageName().equals("toplevel"))
            dir = "";

        FramePageTemplate proxy = new FramePageTemplate(getConfiguration());

        proxy.setTitle(title);
        proxy.setSubTitle(title);
        proxy.setOutputFile(new File(output));
        proxy.setContentTemplateFile(new File(
                IASDocTemplate.CLASS_DETAIL_TEMPLATE));
        proxy.setBasePath(HTMLUtils.returnReletiveBasePath(dir, "\\."));
        proxy.setDefinition(element);

        addRenderClient(proxy);

        ClassDetailPageContentTemplate content = new ClassDetailPageContentTemplate(
                getConfiguration());

        proxy.setContentRendererInstance(content);
    }
}
