////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2006-2007 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

var ECLIPSE_FRAME_NAME = "ContentViewFrame";
var eclipseBuild = false;
var liveDocsBaseUrl = "http://livedocs.macromedia.com/flex/2/langref/";

function findObject(objId) {
	if (document.getElementById)
		return document.getElementById(objId);

	if (document.all)
		return document.all[objId];
}

function isEclipse() {
	return eclipseBuild;
//	return (window.name == ECLIPSE_FRAME_NAME) || (parent.name == ECLIPSE_FRAME_NAME) || (parent.parent.name == ECLIPSE_FRAME_NAME);
}

function configPage() {
	if (isEclipse()) {
		if (window.name != "classFrame")
		{
			var localRef = window.location.href.indexOf('?') != -1 ? window.location.href.substring(0, window.location.href.indexOf('?')) : window.location.href;
			localRef = localRef.substring(localRef.indexOf("langref/") + 8);
			if (window.location.search != "")
				localRef += ("#" + window.location.search.substring(1));

			window.location.replace(baseRef + "index.html?" + localRef);
			return;
		}
		else
		{
			setStyle(".eclipseBody", "display", "block");
//			var isIE  = (navigator.appVersion.indexOf("MSIE") != -1) ? true : false;
//			if (isIE == false && window.location.hash != "")
			if (window.location.hash != "")
				window.location.hash=window.location.hash.substring(1);
		}
	}
	else if (window == top) { // no frames
		findObject("titleTable").style.display = "";
	}
	else { // frames
		findObject("titleTable").style.display = "none";
	}
	showTitle(asdocTitle);
}

function loadFrames(classFrameURL, classListFrameURL) {
	var classListFrame = findObject("classListFrame");
	if(classListFrame != null && classListFrameContent!='')
		classListFrame.document.location.href=classListFrameContent;
 
	if (isEclipse()) {
		var contentViewFrame = findObject(ECLIPSE_FRAME_NAME);
		if (contentViewFrame != null && classFrameURL != '')
			contentViewFrame.document.location.href=classFrameURL;
	}
	else {
		var classFrame = findObject("classFrame");
		if(classFrame != null && classFrameContent!='')
			classFrame.document.location.href=classFrameContent;
	}
}

function showTitle(title) {
	if (!isEclipse())
		top.document.title = title;
}

function loadClassListFrame(classListFrameURL) {
	if (parent.frames["classListFrame"] != null) {
		parent.frames["classListFrame"].location = classListFrameURL;
	}
	else if (parent.frames["packageFrame"] != null) {
		if (parent.frames["packageFrame"].frames["classListFrame"] != null) {
			parent.frames["packageFrame"].frames["classListFrame"].location = classListFrameURL;
		}
	}
}

function loadPackageListFrame(packageListFrameURL) {
	if (parent.frames["packageListFrame"] != null) {
		parent.frames["packageListFrame"].location = packageListFrameURL;
	}
	else if (parent.frames["packageFrame"] != null) {
		if (parent.frames["packageFrame"].frames["packageListFrame"] != null) {
			parent.frames["packageFrame"].frames["packageListFrame"].location = packageListFrameURL;
		}
	}
}



function gotoLiveDocs(primaryURL, secondaryURL) {
	var url = liveDocsBaseUrl + "index.html?" + primaryURL;
	if (secondaryURL != null && secondaryURL != "")
		url += ("&" + secondaryURL);
	window.open(url, "mm_livedocs", "menubar=1,toolbar=1,status=1,scrollbars=1");
}

function findTitleTableObject(id)
{
	if (isEclipse())
		return parent.titlebar.document.getElementById(id);
	else if (top.titlebar)
		return top.titlebar.document.getElementById(id);
	else
		return document.getElementById(id);
}

function titleBar_setSubTitle(title)
{
	if (isEclipse() || top.titlebar)
		findTitleTableObject("subTitle").childNodes.item(0).data = title;
}

function titleBar_setSubNav(
	showVariables,
	showProperties,
	showMethods,
	showEvents,
	showStyles,
	showSkinparts,
	showSkinstates,
	showEffects,
	showConstants,
	showExamples,
	showClasses,
	showInterfaces)
{
	if (isEclipse() || top.titlebar)
	{
		
		findTitleTableObject("publicVariablesLink").style.display = showVariables ? "inline" : "none";
		findTitleTableObject("publicVariablesBar").style.display = 
			(showVariables && 
			(showProperties || 
			showMethods || 
			showEvents || 
			showStyles || 
			showSkinparts ||
			showSkinstates ||
			showEffects || 
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("publicPropertiesLink").style.display = showProperties ? "inline" : "none";
		findTitleTableObject("publicPropertiesBar").style.display = 
			(showProperties && 
			(showMethods || 
			showEvents || 
			showStyles || 
			showSkinparts ||
			showSkinstates ||
			showEffects || 
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";

		findTitleTableObject("publicMethodsLink").style.display = showMethods ? "inline" : "none";
		findTitleTableObject("publicMethodsBar").style.display = 
			(showMethods && 
			(showEvents || 
			showStyles || 
			showSkinparts ||
			showSkinstates ||
			showEffects || 
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";

		findTitleTableObject("publicEventsLink").style.display = showEvents ? "inline" : "none";
		findTitleTableObject("publicEventsBar").style.display = 
			(showEvents && 
			(showStyles || 
			showSkinparts ||
			showSkinstates ||
			showEffects || 
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("publicStylesLink").style.display = showStyles ? "inline" : "none";
		findTitleTableObject("publicStylesBar").style.display = 
			(showStyles && 
			(showSkinparts ||
			showSkinstates ||
			showEffects || 
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("publicSkinpartsLink").style.display = showSkinparts ? "inline" : "none";
		findTitleTableObject("publicSkinpartsBar").style.display = 
			(showSkinparts && 
			(showSkinstates ||
			showEffects ||
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("publicSkinstatesLink").style.display = showSkinstates ? "inline" : "none";
		findTitleTableObject("publicSkinstatesBar").style.display = 
			(showSkinstates && 
			(showEffects ||
			showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("publicEffectsLink").style.display = showEffects ? "inline" : "none";
		findTitleTableObject("publicEffectsBar").style.display = 
			(showEffects && 
			(showConstants || 
			showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("publicConstantsLink").style.display = showConstants ? "inline" : "none";
		findTitleTableObject("publicConstantsBar").style.display = 
			(showConstants && 
			(showExamples || 
			showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		
		findTitleTableObject("examplesLink").style.display = showExamples ? "inline" : "none";
		findTitleTableObject("examplesBar").style.display = 
			(showExamples && 
			(showClasses || 
			showInterfaces)) ? "inline" : "none";
		
		findTitleTableObject("interfacesLink").style.display = showInterfaces ? "inline" : "none";
		findTitleTableObject("interfacesBar").style.display = 
			(showInterfaces && 
			showClasses) ? "inline" : "none";

		findTitleTableObject("classesLink").style.display = showClasses ? "inline" : "none";
		findTitleTableObject("classesBar").style.display = 
			(showClasses) ? "none" : "none";

	}
}

function titleBar_gotoClassFrameAnchor(anchor)
{
	if (isEclipse())
		parent.classFrame.location = parent.classFrame.location.toString().split('#')[0] + "#" + anchor;
	else
		top.classFrame.location = top.classFrame.location.toString().split('#')[0] + "#" + anchor;
}

function setMXMLOnly() 
{
	if (getCookie("showMXML") == "false")
	{
		toggleMXMLOnly();
	}
}
function toggleMXMLOnly() 
{
	var mxmlDiv = findObject("mxmlSyntax");
	var mxmlShowLink = findObject("showMxmlLink");
	var mxmlHideLink = findObject("hideMxmlLink");
	if (mxmlDiv && mxmlShowLink && mxmlHideLink)
	{
		if (mxmlDiv.style.display == "none")
		{
			mxmlDiv.style.display = "block";
			mxmlShowLink.style.display = "none";
			mxmlHideLink.style.display = "inline";
			setCookie("showMXML","true", new Date(3000,1,1,1,1), "/", document.location.domain);
		}
		else
		{
			mxmlDiv.style.display = "none";
			mxmlShowLink.style.display = "inline";
			mxmlHideLink.style.display = "none";
			setCookie("showMXML","false", new Date(3000,1,1,1,1), "/", document.location.domain);
		}
	}
}

function showHideInherited()
{	
	setInheritedVisible(getCookie("showInheritedPublicVariable") == "true", "PublicVariable");
	setInheritedVisible(getCookie("showInheritedProtectedVariable") == "true", "ProtectedVariable");
	
	setInheritedVisible(getCookie("showInheritedPublicAccessor") == "true", "PublicAccessor");
	setInheritedVisible(getCookie("showInheritedProtectedAccessor") == "true", "ProtectedAccessor");
	
	setInheritedVisible(getCookie("showInheritedPublicMethod") == "true", "PublicMethod");
	setInheritedVisible(getCookie("showInheritedProtectedMethod") == "true", "ProtectedMethod");
	
	setInheritedVisible(getCookie("showInheritedPublicConstant") == "true", "PublicConstant");
	setInheritedVisible(getCookie("showInheritedProtectedConstant") == "true", "ProtectedConstant");
	
	setInheritedVisible(getCookie("showInheritedPublicEvent") == "true", "PublicEvent");
	setInheritedVisible(getCookie("showInheritedPublicStyle") == "true", "PublicStyle");
	setInheritedVisible(getCookie("showInheritedPublicPart") == "true", "PublicPart");
	setInheritedVisible(getCookie("showInheritedPublicSkinstate") == "true", "PublicSkinstate");
	setInheritedVisible(getCookie("showInheritedPublicEffect") == "true", "PublicEffect");
}
function setInheritedVisible(show, selectorText)
{
	try {
		rulez = document.styleSheets[0].cssRules;
	}
	catch(e) {
		return;
	}
	
	if (rulez != undefined)
	{
		var rules = document.styleSheets[0].cssRules;
		var stl = selectorText.toLowerCase(); // added
		for (var i = 0; i < rules.length; i++)
		{
			var selector = rules[i].selectorText; //added
			if (rules[i].selectorText == ".hideInherited" + selectorText)
				rules[i].style.display = show ? "" : "none";
				
			if (rules[i].selectorText == ".showInherited" + selectorText)
				rules[i].style.display = show ? "none" : "";
			
			// added
			selector = selector.toLowerCase();
			
			if (selector == ".hideinherited" + stl)
				rules[i].style.display = show ? "" : "none";
				
			if (selector == ".showinherited" + stl)
				rules[i].style.display = show ? "none" : "";
			// end added			
		}
	}
	else
	{
		rulez.addRule(".hideInherited" + selectorText, show ? "display:inline" : "display:none");
		rulez.addRule(".showInherited" + selectorText, show ? "display:none" : "display:inline");
	}
	setCookie("showInherited" + selectorText, show ? "true" : "false", new Date(3000,1,1,1,1), "/", document.location.domain);
	setRowColors(show, selectorText);
}

function setRowColors(show, selectorText)
{
	var rowColor = "#F2F2F2";
	var table = findObject("summaryTable" + selectorText);
	if (table != null)
	{
		var rowNum = 0;
		for (var i = 1; i < table.rows.length; i++)
		{
			if (table.rows[i].className.indexOf("hideInherited") == -1 || show)
			{
				rowNum++;
				table.rows[i].bgColor = (rowNum % 2 == 0) ? rowColor : "#FFFFFF";
			}			
		}
	}
}

function setStyle(selectorText, styleName, newValue)
{
	if (document.styleSheets[0].cssRules != undefined)
	{
		var rules = document.styleSheets[0].cssRules;
		for (var i = 0; i < rules.length; i++)
		{
			if (rules[i].selectorText == selectorText)
			{
				rules[i].style[styleName] = newValue;
				break;
			}
		}
	}
	else
	{
		document.styleSheets[0].addRule(selectorText, styleName + ":" + newValue);
	}
}