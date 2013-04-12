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

package randori.compiler.asdoc.template.asdoc;

public interface IASDocTemplate
{
    // media
    public static final String MEDIA_IMAGES = "/resources/media/images";
    public static final String MEDIA_ASDOC_JS = "/resources/media/asdoc.js";
    public static final String MEDIA_COOKIES_JS = "/resources/media/cookies.js";
    public static final String MEDIA_PRINT_CSS = "/resources/media/print.css";
    public static final String MEDIA_STYLE_CSS = "/resources/media/style.css";

    // details
    public static final String VARIABLE_DETAIL_TEMPLATE = "detail/variable-detail-row.vm";
    public static final String CONSTANT_DETAIL_TEMPLATE = "detail/constant-detail-row.vm";
    public static final String ACCESSOR_DETAIL_TEMPLATE = "detail/accessor-detail-row.vm";
    public static final String METHOD_DETAIL_TEMPLATE = "detail/method-detail-row.vm";
    public static final String EVENT_DETAIL_TEMPLATE = "detail/event-detail-row.vm";

    // index.html
    public static final String INDEX_TEMPLATE = "index.vm";
    public static final String INDEX_OUTPUT = "index.html";

    // package-frame.html
    public static final String PACKAGE_FRAME_TEMPLATE = "package-frame.vm";
    public static final String PACKAGE_FRAME_OUTPUT = "package-frame.html";

    // index-list.html
    public static final String INDEX_LIST_TEMPLATE = "index-list.vm";
    public static final String INDEX_LIST_OUTPUT = "index-list.html";

    // title-bar.html
    public static final String TITLE_BAR_PAGE_TEMPLATE = "title-bar-page.vm";
    public static final String TITLE_BAR_PAGE_OUTPUT = "title-bar.html";

    public static final String TITLE_BAR_TEMPLATE = "title-bar.vm";

    // package-list.html
    public static final String PACKAGE_LIST_TITLE = "All Packages";
    public static final String PACKAGE_LIST_OUTPUT = "package-list.html";
    public static final String PACKAGE_LIST_TEMPLATE = "package-list-pane.vm";

    public static final String PACKAGE_LIST_DETAIL_PAGE_TEMPLATE = "package-list-detail-page.vm";
    public static final String PACKAGE_LIST_DETAIL_PANE_TEMPLATE = "package-list-detail-pane.vm";
    public static final String PACKAGE_LIST_PAGE_TEMPLATE = "tpl/packages/package-list-page.vm";
    public static final String PACKAGE_LIST_PANE_TEMPLATE = "tpl/packages/package-list-pane.vm";

    // all-classes.html
    public static final String CLASS_LIST_TITLE = "All Classes";
    public static final String CLASS_LIST_OUTPUT = "all-classes.html";
    public static final String CLASS_LIST_TEMPLATE = "class-list-pane.vm";

    // package-summary.html
    public static final String PACKAGE_SUMMARY_OUTPUT = "package-summary.html";
    public static final String PACKAGE_SUMMARY_TEMPLATE = "package-list-page.vm";

    // class-summary.html
    public static final String CLASS_SUMMARY_OUTPUT = "class-summary.html";
    public static final String CLASS_SUMMARY_TEMPLATE = "class-list-page.vm";

    // class detail
    public static final String CLASS_DETAIL_TEMPLATE = "class-detail-page.vm";
    public static final String CLASS_DETAIL_SIGNATURE_TEMPLATE = "class-detail-signature.vm";
    public static final String CLASS_DETAIL_DESCRIPTION_TEMPLATE = "class-detail-description.vm";

    public static final String SUMMARY_TABLE_TEMPLATE = "summary/summary-table.vm";
    public static final String DETAIL_TABLE_TEMPLATE = "detail/detail-table.vm";
    public static final String CLASS_FRAMEPAGE_TEMPLATE = "frame-page.vm";

    public static final String CLASS_MXML_BLOCK_TEMPLATE = "class-mxml-block.vm";
}
