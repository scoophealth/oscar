<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ page errorPage="/casemgmt/error.jsp"%>
<%@ page language="java"%>

<% if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp"); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean");
    
    pageContext.setAttribute("providerNo",bean.providerNo, pageContext.PAGE_SCOPE);
%>

<%--<nested:define id="rowOneSize" name="caseManagementViewForm" property="ectWin.rowOneSize"/>
<nested:define id="rowTwoSize" name="caseManagementViewForm" property="ectWin.rowTwoSize"/>
--%>
<html:html locale="true">
<head>

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css"
	type="text/css">
<link rel="stylesheet"
	href="<c:out value="${ctx}"/>/oscarEncounter/encounterStyles.css"
	type="text/css">
<link rel="stylesheet" type="text/css"
	href="<c:out value="${ctx}"/>/css/print.css" media="print">
<script src="<c:out value="${ctx}"/>/share/javascript/prototype.js"
	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js"
	type="text/javascript"></script>

<%-- for popup menu of forms --%>
<script src="<c:out value="${ctx}"/>/share/javascript/popupmenu.js"
	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/menutility.js"
	type="text/javascript"></script>

<!-- library for rounded elements -->
<link rel="stylesheet" type="text/css"
	href="<c:out value="${ctx}/share/css/niftyCorners.css" />">
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/nifty.js"/>"></script>

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="<c:out value="${ctx}"/>/share/calendar/calendar.css"
	title="win2k-cold-1">

<!-- main calendar program -->
<script type="text/javascript"
	src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>

<%--<!-- js implementation of markdown -->
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/showdown.js"/>"></script>--%>

<!-- js window size utility funcs since prototype's funcs are buggy in ie6 -->
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>

<!-- scriptaculous based select box -->
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/select.js"/>"></script>

<!--js code for newCaseManagementView.jsp -->
<script type="text/javascript"
	src="<c:out value="${ctx}/js/newCaseManagementView.js"/>"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<!--[if IE]>
    <style type=text/css>
    
        .showEdContent { 
            width:450px;
        }
     </style>
 <![endif]-->
<html:base />
<title><bean:message key="oscarEncounter.Index.title" /> - <oscar:nameage
	demographicNo="<%=(String) request.getAttribute(\"demographicNo\")%>" /></title>
<meta http-equiv="Cache-Control" content="no-cache">
<script type="text/javascript">
        
function init() {       
        
    //console.log("Begin init");
    //var start = new Date();
    //var current;
    showIssueNotes();
    
    //current = new Date();
    //var delta = current.getTime() - start.getTime();
    //console.log("show Issues " + delta);
    
    var navBars = new navBarLoader();
    navBars.load();  
    
    //current = new Date();
    //var delta = current.getTime() - start.getTime();
    //console.log("navbars " + delta);
    monitorNavBars(null);
    
    Element.observe(window, "resize", monitorNavBars);    
    
    if(!NiftyCheck())
        return;

    Rounded("div.showEdContent","all","transparent","#CCCCCC","big border #000000"); 
    Rounded("div.printOps","all","transparent","#CCCCCC","big border #000000");
    Calendar.setup({ inputField : "printStartDate", ifFormat : "%d-%b-%Y", showsTime :false, button : "printStartDate_cal", singleClick : true, step : 1 });    
    Calendar.setup({ inputField : "printEndDate", ifFormat : "%d-%b-%Y", showsTime :false, button : "printEndDate_cal", singleClick : true, step : 1 });    
    
    <c:url value="/CaseManagementEntry.do" var="issueURLCPP">
        <c:param name="method" value="issueList"/>
        <c:param name="demographicNo" value="${demographicNo}" />
        <c:param name="providerNo" value="${providerNo}" />
        <c:param name="all" value="true" />
    </c:url>
    var issueAutoCompleterCPP = new Ajax.Autocompleter("issueAutocompleteCPP", "issueAutocompleteListCPP", "<c:out value="${issueURLCPP}" />", {minChars: 4, indicator: 'busy2', afterUpdateElement: addIssue2CPP, onShow: autoCompleteShowMenuCPP, onHide: autoCompleteHideMenuCPP});
    
    strToday = $F("serverDate");
    
    <nested:notEmpty name="DateError">
        alert("<nested:write name="DateError"/>");
    </nested:notEmpty>

}

</script>
</head>
<body id="body" style="margin: 0px;" onload="init()"
	onunload="onClosing()">

<div id="main">
<div id="header"><tiles:insert attribute="header" /></div>

<div id="leftNavBar" style="display: inline; float: left; width: 20%;">
<tiles:insert attribute="leftNavigation" /></div>

<div id="content"
	style="display: inline; float: left; width: 60%; background-color: #CCCCFF;">
<tiles:insert attribute="body" /></div>

<div id="rightNavBar"
	style="display: inline; float: right; width: 20%; margin-left: -3px;">
<tiles:insert attribute="rightNavigation" /></div>

<!-- hovering divs -->
<div id="showEditNote" class="showEdContent">
<form id="frmIssueNotes" action="" method="post"
	onsubmit="return updateCPPNote();"><input type="hidden"
	id="reloadUrl" name="reloadUrl" value=""> <input type="hidden"
	id="containerDiv" name="containerDiv" value=""> <input
	type="hidden" id="issueChange" name="issueChange" value="">
<div id="winTitle"></div>
<textarea style="margin: 10px;" cols="50" rows="15" id="noteEditTxt"
	name="value" wrap="soft"></textarea><br>
<span style="float: right; margin-right: 10px;"> <input
	style="padding-right: 10px;" type="image"
	src="<c:out value="${ctx}/oscarEncounter/graphics/note-save.png"/>"
	title='<bean:message key="oscarEncounter.Index.btnSignSave"/>'>
<input type="image"
	src="<c:out value="${ctx}/oscarEncounter/graphics/system-log-out.png"/>"
	onclick="this.focus();$('channel').style.visibility ='visible';$('showEditNote').style.display='none';return false;"
	title='<bean:message key="global.btnExit"/>'> </span>
<div id="issueNoteInfo" style="clear: both; text-align: left;"></div>
<div id="issueListCPP"
	style="background-color: #FFFFFF; height: 200px; width: 350px; position: absolute; z-index: 1; display: none; overflow: auto;">
<div class="enTemplate_name_auto_complete" id="issueAutocompleteListCPP"
	name="issueAutocompleteListCPP"
	style="position: relative; left: 0px; display: none;"></div>
</div>
Assign Issues&nbsp;<input tabindex="100" type="text"
	id="issueAutocompleteCPP" name="issueSearch" style="z-index: 2;"
	size="25">&nbsp; <span id="busy2" style="display: none"><img
	style="position: absolute;"
	src="<c:out value="${ctx}/oscarEncounter/graphics/busy.gif"/>"
	alt="Working..." /></span></form>
</div>
<div id="printOps" class="printOps">
<h3 style="margin-bottom: 5px; text-align: center;">Print Dialog</h3>
<form id="frmPrintOps" action="" onsubmit="return false;"><input
	type="radio" id="printopSelected" name="printop" value="selected">Selected<br>
<input type="radio" id="printopAll" name="printop" value="all">All<br>
<input type="radio" id="printopDates" name="printop" value="dates">Dates&nbsp;<a
	style="font-variant: small-caps;" href="#"
	onclick="return printToday(event);">Today</a><br>
<div style="float: left; margin-left: 5px; width: 30px;">From:</div>
<img src="<c:out value="${ctx}/images/cal.gif" />"
	id="printStartDate_cal" alt="calendar">&nbsp;<input type="text"
	id="printStartDate" name="printStartDate" ondblclick="this.value='';"
	style="font-style: italic; border: 1px solid #7682b1; width: 125px; background-color: #FFFFFF;"
	readonly value=""><br>
<div style="float: left; margin-left: 5px; width: 30px;">To:</div>
<img src="<c:out value="${ctx}/images/cal.gif" />" id="printEndDate_cal"
	alt="calendar">&nbsp;<input type="text" id="printEndDate"
	name="printEndDate" ondblclick="this.value='';"
	style="font-style: italic; border: 1px solid #7682b1; width: 125px; background-color: #FFFFFF;"
	readonly value=""><br>
<div style="margin-top: 5px; text-align: center"><input
	type="submit" id="printOp" style="border: 1px solid #7682b1;"
	value="Print" onclick="return printNotes();"> <input
	type="submit" id="cancelprintOp" style="border: 1px solid #7682b1;"
	value="Cancel" onclick="$('printOps').style.display='none';"> <input
	type="submit" id="clearprintOp" style="border: 1px solid #7682b1;"
	value="Clear"
	onclick="$('printOps').style.display='none'; return clearAll(event);">
</div>
</form>
</div>

</div>
</body>
</html:html>
