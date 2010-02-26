<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<!--
/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity test2
 * Hamilton
 * Ontario, Canada
 */
-->
<%
            if (session.getValue("user") == null) {
                response.sendRedirect("../logout.htm");
            }
            System.out.println("1");
            String user_no = (String) session.getAttribute("user");
            String userfirstname = (String) session.getAttribute("userfirstname");
            String userlastname = (String) session.getAttribute("userlastname");
%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>

<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="page" />
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarProvider.data.ProviderMyOscarIdData, oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="oscar.oscarProvider.data.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                String name = (String) e.nextElement();
                System.out.println(name + " -> " + request.getParameter(name));
            }
            if (request.getParameter("delDocumentNo") != null) {
                EDocUtil.deleteDocument(request.getParameter("delDocumentNo"));
            }

//view  - tabs
            String view = "all";
            if (request.getParameter("view") != null) {
                view = (String) request.getParameter("view");
            } else if (request.getAttribute("view") != null) {
                view = (String) request.getAttribute("view");
            }
//preliminary JSP code

// "Module" and "function" is the same thing (old dms module)
            String module = "demographic";
            String moduleid = "-1";
//if (request.getParameter("function") != null) {
//    module = request.getParameter("function");
//    moduleid = request.getParameter("functionid");
//} else if (request.getAttribute("function") != null) {
//    module = (String) request.getAttribute("function");
//    moduleid = (String) request.getAttribute("functionid");
//}
            String moduleName = "Unmatched";//EDocUtil.getModuleName(module, moduleid);

            String curUser = "";
            if (request.getParameter("curUser") != null) {
                curUser = request.getParameter("curUser");
            } else if (request.getAttribute("curUser") != null) {
                curUser = (String) request.getAttribute("curUser");
            }

//sorting
            String sort =EDocUtil.SORT_CREATOR+",  "+ EDocUtil.SORT_OBSERVATIONDATE;
            String sortRequest = request.getParameter("sort"); 
            if (sortRequest != null) {
                if (sortRequest.equals("description")) {
                    sort = EDocUtil.SORT_DESCRIPTION;
                } else if (sortRequest.equals("type")) {
                    sort = EDocUtil.SORT_DOCTYPE;
                } else if (sortRequest.equals("contenttype")) {
                    sort = EDocUtil.SORT_CONTENTTYPE;
                } else if (sortRequest.equals("creator")) {
                    sort = EDocUtil.SORT_CREATOR;
                } else if (sortRequest.equals("uploaddate")) {
                    sort = EDocUtil.SORT_DATE;
                } else if (sortRequest.equals("observationdate")) {
                    sort = EDocUtil.SORT_OBSERVATIONDATE;
                }
            }

            ArrayList doctypes = EDocUtil.getDoctypes(module);

//Retrieve encounter id for updating encounter navbar if info this page changes anything
            String parentAjaxId;
            if (request.getParameter("parentAjaxId") != null) {
                parentAjaxId = request.getParameter("parentAjaxId");
            } else if (request.getAttribute("parentAjaxId") != null) {
                parentAjaxId = (String) request.getAttribute("parentAjaxId");
            } else {
                parentAjaxId = "";
            }
            String updateParent;
            if (request.getParameter("updateParent") != null) {
                updateParent = request.getParameter("updateParent");
            } else {
                updateParent = "false";
            }
%>
<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <title><bean:message key="dms.documentReport.title" /></title>
        <meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
        <meta http-equiv="Cache-Control" content="no-cache">

        <link rel="stylesheet" type="text/css"
              href="../share/css/OscarStandardLayout.css" />
        <script type="text/javascript" src="../share/javascript/Oscar.js"></script>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/effects.js"></script>
        <script type="text/javascript" src="../share/javascript/controls.js"></script>

        <link rel="stylesheet" type="text/css"
              href="../share/css/niftyCorners.css" />
        <link rel="stylesheet" type="text/css" href="dms.css" />
        <link rel="stylesheet" type="text/css"
              href="../share/css/niftyPrint.css" media="print" />

        <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


         <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"/></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"/></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"/></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"/></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"/></script>
        <script type="text/javascript" src="<c:out value="${ctx}/js/newCaseManagementView.js"/>"></script>        
        <!-- calendar stylesheet -->
        <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
        <!-- main calendar program -->
        <script type="text/javascript" src="../share/calendar/calendar.js"></script>
        <!-- language for the calendar -->
        <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
        <!-- the following script defines the Calendar.setup helper function, which makes
               adding a calendar a matter of 1 or 2 lines of code. -->
        <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
        <link rel="stylesheet" href="../web.css">
        <style type="text/css">
#myAutoComplete {
    width:15em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
.match {
    font-weight:bold;
}



        .yui-ac {
	    position:relative;font-family:arial;font-size:100%;
	}

	/* styles for input field */
	.yui-ac-input {
	    position:relative;width:100%;
	}

	/* styles for results container */
	.yui-ac-container {
	    position:absolute;top:0em;width:100%;
	}

	/* styles for header/body/footer wrapper within container */
	.yui-ac-content {
	    position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;
	}

	/* styles for container shadow */
	.yui-ac-shadow {
	    position:absolute;margin:.0em;width:100%;background:#000;-moz-opacity: 0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;
	}

	/* styles for results list */
	.yui-ac-content ul{
	    margin:0;padding:0;width:100%;
	}

	/* styles for result item */
	.yui-ac-content li {
	    margin:0;padding:0px 0px;cursor:default;white-space:nowrap;
	}

	/* styles for prehighlighted result item */
	.yui-ac-content li.yui-ac-prehighlight {
	    background:#B3D4FF;
	}

	/* styles for highlighted result item */
	.yui-ac-content li.yui-ac-highlight {
	    background:#426FD9;color:#FFF;
	}
	
</style>

        <script type="text/javascript" src="../share/javascript/nifty.js"></script>
        <script type="text/javascript" src="../phr/phr.js"></script>
        <script type="text/javascript">
        function renderCalendar(id,inputFieldId){    
                Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
        }
       var selectedDemos=new Array();
       var myContacts = [
       <%ArrayList providers = ProviderData.getProviderList();
        for (int i = 0; i < providers.size(); i++) {
           Hashtable h = (Hashtable) providers.get(i);%>
           {id: "<%= h.get("providerNo")%>", lname: "<%= h.get("lastName")%>", fname:"<%= h.get("firstName")%>"},
       <%}%>
           { id: "-1",lname: "none", fname:"none"}

    ];

// Define a custom search function for the DataSource
    var matchNames = function(sQuery) {
        // Case insensitive matching
        var query = sQuery.toLowerCase(),
            contact,
            i=0,
            l=myContacts.length,
            matches = [];
            //console.log(l);

        // Match against each name of each contact
        for(; i<l; i++) {
            contact = myContacts[i];
            if((contact.fname.toLowerCase().indexOf(query) > -1) || (contact.lname.toLowerCase().indexOf(query) > -1) ) {
                matches[matches.length] = contact;
            }
        }

        return matches;
    };

    var oDS = new YAHOO.util.FunctionDataSource(matchNames);
    oDS.responseSchema = {fields: ["id", "fname", "lname"]}
 //   console.log("oDS="+oDS);
//    console.log("oDS.id="+oDS.id);
  //  console.log("oDS.fname="+oDS.fname);
  //  console.log("oDS.lname="+oDS.lname);

	// Helper function for the formatter
    var highlightMatch = function(full, snippet, matchindex) {
        return full.substring(0, matchindex) +
                "<span class='match'>" +
                full.substr(matchindex, snippet.length) +
                "</span>" +
                full.substring(matchindex + snippet.length);
    };


	var resultFormatter = function(oResultData, sQuery, sResultMatch) {
        var query = sQuery.toLowerCase(),
            fname = oResultData.fname,
            lname = oResultData.lname,
                        query = sQuery.toLowerCase(),
            fnameMatchIndex = fname.toLowerCase().indexOf(query),
            lnameMatchIndex = lname.toLowerCase().indexOf(query),

            displayfname, displaylname ;

        if(fnameMatchIndex > -1) {
            displayfname = highlightMatch(fname, query, fnameMatchIndex);
        }
        else {
            displayfname = fname;
        }

        if(lnameMatchIndex > -1) {
            displaylname = highlightMatch(lname, query, lnameMatchIndex);
        }
        else {
            displaylname = lname;
        }


        return displayfname + " " + displaylname ;

    };


var resultFormatter2 = function(oResultData, sQuery, sResultMatch) {
        var query = sQuery.toLowerCase();
        //oscarLog("in resultFormatter2");
           // oscarLog(oResultData);
            fname = oResultData[0];
            dob = oResultData[1];
            //oscarLog(fname);
            //oscarLog(dob);
            //oscarLog(query);
            fnameMatchIndex = fname.toLowerCase().indexOf(query),
            
            displayfname= '';
            //oscarLog("fnameMatchIndex="+fnameMatchIndex);
        if(fnameMatchIndex > -1) {
            displayfname = highlightMatch(fname, query, fnameMatchIndex);
            //oscarLog("displayfname in if="+displayfname);
        }
        else {
            displayfname = fname;
        }

       


        return displayfname + " (" + dob+ ")" ;

    };      
            window.onload=function(){
                if(!NiftyCheck())
                    return;

                Rounded("div.doclist","top","transparent", "#ccccd7", "small border #ccccd7");
                Rounded("div.doclist","bottom","transparent", "#e0ecff", "small border #ccccd7");
                Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
                Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");
                //onloadfunction();
                setup();  //reload parent content if necessary
            }


            var awnd=null;
            function popPage(url) {
                awnd=rs('',url ,400,200,1);
                awnd.focus();
            }


            function checkDelete(url, docDescription){
                // revision Apr 05 2004 - we now allow anyone to delete documents
                if(confirm("<bean:message key="dms.documentReport.msgDelete"/> " + docDescription)) {
                window.location = url;
            }
        }

        function showhide(hideelement, button) {
            var plus = "+";
            var minus = "--";
            if (document.getElementById) { // DOM3 = IE5, NS6
                if (document.getElementById(hideelement).style.display == 'none') {
                    document.getElementById(hideelement).style.display = 'block';
                    document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(plus, minus);
                }
                else {
                    document.getElementById(hideelement).style.display = 'none';
                    document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(minus, plus);
                }
            }
        }


        function checkAll(checkboxId,parentEle, className){
            var f = document.getElementById(checkboxId);
            var val = f.checked;
            var chkList = document.getElementsByClassName(className, parentEle);
            for (i =0; i < chkList.length; i++){
                chkList[i].checked = val;
            }
        }

        function submitForm(actionPath) {

            var form = document.forms[2];
            if(verifyChecks(form)) {
                form.action = actionPath;
                form.submit();
                return true;
            }
            else
                return false;
        }

        function submitPhrForm(actionPath, windowName) {

            var form = document.forms[2];
            if(verifyChecks(form)) {
                form.onsubmit = phrActionPopup(actionPath, windowName);
                form.target = windowName;
                form.action = actionPath;
                form.submit();
                return true;
            }
            else
                return false;
        }

        function verifyChecks(t){

            if ( t.docNo == null ){
                alert("No documents selected");
                return false;
            }else{
                var oneChecked = 0;
                if( t.docNo.length ) {
                    for ( i=0; i < t.docNo.length; i++){
                        if(t.docNo[i].checked){
                            ++oneChecked;
                            break;
                        }
                    }
                }
                else
                    oneChecked = t.docNo.checked ? 1 : 0;

                if ( oneChecked == 0 ){
                    alert("No documents selected");
                    return false;
                }
            }
            return true;
        }

        function popup1(height, width, url, windowName){
            var page = url;
            windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
            var popup=window.open(url, windowName, windowprops);
            if (popup != null){
                if (popup.opener == null){
                    popup.opener = self;
                }
            }
            popup.focus();

        }


        function setup() {
            var update = "<%=updateParent%>";
            var parentId = "<%=parentAjaxId%>";
            if(window.opener){
                var Url = window.opener.URLs;

                if( update == "true" && !window.opener.closed )
                    window.opener.popLeftColumn(Url[parentId], parentId, parentId);
            }

        }



        function saveDemoId(text, li){
            //console.log("saveDemoId "+li.id+" "+text.id);
            var str = text.id.replace("autocompletedemo","demofind");
            //console.log("str "+str);
            $(str).value = li.id;
        }

        function saveProvId(text, li){
         //   console.log("saveProvId "+li.id+" "+text.id+" "+text.value);
            var provName = text.value;
            var str = text.id.replace("autocompletedemo","demofind");
            //console.log("str "+str);
            $(str).value = li.id;

            var bdoc = document.createElement('a');
            bdoc.setAttribute("onclick", "removeProv(this);");
            bdoc.appendChild(document.createTextNode(" -remove- "));

            var adoc = document.createElement('div');
            adoc.appendChild(document.createTextNode(provName));

            var idoc = document.createElement('input');
            idoc.setAttribute("type", "hidden");
            idoc.setAttribute("name","flagproviders");
            idoc.setAttribute("value",li.id);
            adoc.appendChild(idoc);

            adoc.appendChild(bdoc);

            providerList.appendChild(adoc);
            text.value = '';
        }

        function removeProv(th){
            var ele = th.up();
            ele.remove();

        }

        function sendToServer(formId){
        //    console.log("in sendToServer");
       //     console.log(formId);
            var toSend = $(formId).serialize(true);
            var url = "ManageDocument.do";//"send.jsp";
            Effect.SlideUp('document'+toSend.documentId);
        //    console.log(toSend.documentId);
            new Ajax.Request(url, { method: 'post', parameters: toSend, onSuccess: successAdjusting });

            //Effect.SlideUp('document'+toSend.documentId);
            return false;
        }

        function successAdjusting(transport){
            var jason = transport.responseText.evalJSON(true);
            //console.log("successlog"+jason.success+"   "+jason.docId);
        }
        /*
         * $('document'+jason.docId).remove();

         */


        function autoCompleteShowMenu(){
            //console.log("autoCompleteShowMenu");
        }

        function autoCompleteHideMenu(){
            //console.log("autoCompleteHideMenu");
        }

        function checkSave(elementId){
            var curVal=$('autocompletedemo'+elementId).value;
            var isCurValValid=false;
            for(var i=0;i<selectedDemos.length;i++){
                if(curVal==selectedDemos[i]){
                    isCurValValid=true;
                    break;
                }
            }
            if(isCurValValid)
                $('save'+elementId).enable();
            else
                $('save'+elementId).disable();
        }

        </script>


    </head>

    <body class="yui-skin-sam2"> <%-- class="bodystyle" --%>
        

        <table class="MainTable" id="scrollNumber1" name="encounterTable"
               style="margin: 0px;">
            <tr class="MainTableRowTop">
                <td class="MainTableTopRowLeftColumn" width="60px">eDocs</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Documents</td>
                            <td>&nbsp;</td>
                            <td style="text-align: right;"><a
                                    href="javascript: popupStart(300, 400, 'Help.jsp')">Help</a> | <a
                                    href="javascript: popupStart(300, 400, 'About.jsp')">About</a> | <a
                                    href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>

                <td class="MainTableRightColumn" colspan="2" valign="top">
                    <ul>
                        <li>- way to send to a provider (using lab system)</li>
                        <li>+ way to easily add desc and type</li>
                        <li>- if moduleid -1 is keeping the documents here what will keep them here to send to a provider  (EG you match a patient...then</li>
                        <li>- Once linked with a patient offer to mark a physicians record </li>
                        <li>- clicking on the graphic should pop up the viewer</li>
                        <li>- The idea of a queue (EG sfhc will have way to many common documents)</li>
                        <li>- Need a way to show if the document is currently linked with a provider</li>

                    </ul> <!--html:form action="/dms/combinePDFs" -->
                    <input type="hidden" name="curUser" value="<%=curUser%>">
                    <input type="hidden" name="demoId" value="<%=moduleid%>">
                    <div class="documentLists"><%-- STUFF TO DISPLAY --%> <%
            ArrayList categories = new ArrayList();
            ArrayList categoryKeys = new ArrayList();
            ArrayList privatedocs = new ArrayList();
            String viewstatus = "active";

            System.out.println("PRIVATE DOCS GOING");

            privatedocs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PRIVATE, sort, viewstatus);

            System.out.println("PRIVATE DOCS GONE" + privatedocs.size());


            categories.add(privatedocs);
            categoryKeys.add(moduleName + "'s Private Documents");
            if (module.equals("provider")) {
                ArrayList publicdocs = new ArrayList();
                publicdocs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PUBLIC, sort, viewstatus);
                categories.add(publicdocs);
                System.out.println("Adding to Categories " + publicdocs.size());
                categoryKeys.add("Public Documents");
            }

            System.out.println("CATS " + categories.size());

            String preCreatorId="";
            for (int i = 0; i < categories.size(); i++) {
                String currentkey = (String) categoryKeys.get(i);
                System.out.println("currentkey="+currentkey);
                ArrayList category = (ArrayList) categories.get(i);
                        %>
                        <div class="doclist">
                            <div class="headerline">
                                <div class="docHeading">
                                    <a id="plusminus<%=i%>" href="javascript: showhide('documentsInnerDiv<%=i%>', 'plusminus<%=i%>');">-- <%= currentkey%> </a>
                                    <span class="tabs"> View: <a href="?function=<%=module%>&functionid=<%=moduleid%>">All</a>
                                    <% for (int i3 = 0; i3 < doctypes.size(); i3++) {%>
                                        | <a href="?function=<%=module%>&functionid=<%=moduleid%>&view=<%=(String) doctypes.get(i3)%>"><%=(String) doctypes.get(i3)%></a>
                                    <%}%>
                                    </span>
                                </div>
                            </div>
                            <div id="documentsInnerDiv<%=i%>" style="background-color: #f2f7ff;">
                                <%-- <table id="privateDocs" class="docTable" --%> <%
                            int tabindex = 1;

                            String curProvNo=request.getSession().getAttribute("user").toString();
                            for (int i2 = 0; i2 < category.size(); i2++) {
                                EDoc curdoc = (EDoc) category.get(i2);
                                String creatorId=curdoc.getCreatorId();
                                System.out.println("creatorId="+curdoc.getCreatorId()+"--docId="+curdoc.getDocId()+"--name="+curdoc.getCreatorName());
                                //content type (take everything following '/')
                                int slash = 0;
                                String contentType = "";
                                if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                                    contentType = curdoc.getContentType().substring(slash + 1);
                                }
                                String dStatus = "";
                                if ((curdoc.getStatus() + "").compareTo("A") == 0) {
                                    dStatus = "active";
                                } else if ((curdoc.getStatus() + "").compareTo("H") == 0) {
                                    dStatus = "html";
                                }
                                String curDocId=curdoc.getDocId();
                                String url = "ManageDocument.do?method=view&doc_no=" + curDocId;
                                String docUrl=request.getContextPath()+"/dms/ManageDocument.do?method=display&doc_no="+curDocId;
                                
                                %>
                                <div id="document<%=curdoc.getDocId()%>">
                                    <%if(!creatorId.equals(preCreatorId)){%>
                                    <a>*****************************************************************************************</a>
                                    <%} preCreatorId=creatorId;%>
                                    <table class="docTable">
                                        <tr>


                                            <td colspan="8">
                                                <a href="javascript:void(0);">
                                                    <img src="<%=url%>" onclick="popupPage(500,600,'name_<%=curDocId%>','<%=docUrl%>')"  title="Click to Download File" />
                                                </a>
                                            </td>

                                            <td align="left" valign="top">
                                                <fieldset><legend>Document Uploaded :<%=curdoc.getDateTimeStamp()%> - Content Type: <%=contentType%> <%=curdoc.getFileName() %></legend>
                                                    <form id="forms<%=curdoc.getDocId()%>" action="undocumentReport.jsp" onsubmit="return sendToServer('forms<%=curdoc.getDocId()%>');">
                                                        <input type="hidden" name="method" value="documentUpdate" />
                                                        <input type="hidden" name="documentId" value="<%=curdoc.getDocId()%>" />
                                                        <table>
                                                            <tr>
                                                                <td><bean:message key="dms.documentReport.msgDocType" />:</td>
                                                                <td>
                                                                    <select tabindex="<%=tabindex++%>" name="docType" id="docType">
                                                                        <option value=""><bean:message key="dms.addDocument.formSelect" /></option>
                                                                        <%for (int j = 0; j < doctypes.size(); j++) {
                                                                            String doctype = (String) doctypes.get(j);%>
                                                                        <option value="<%= doctype%>" <%=(curdoc.getType().equals(doctype)) ? " selected" : ""%>><%= doctype%></option>
                                                                        <%}%>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><bean:message key="dms.documentReport.msgDocDesc" />:</td>
                                                                <td><input tabindex="<%=tabindex++%>" type="text" name="documentDescription" value="<%=curdoc.getDescription()%>" /></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Observation Date:</td>
                                                                <td>
                                                                    <input tabindex="<%=tabindex++%>" id="observationDate<%=curdoc.getDocId()%>" name="observationDate" type="text" value="<%=curdoc.getObservationDate()%>">
                                                                    <a id="obsdate<%=curdoc.getDocId()%>" onmouseover="renderCalendar(this.id,'observationDate<%=curdoc.getDocId()%>' );" href="javascript:void(0);" ><img title="Calendar" src="../images/cal.gif" alt="Calendar"border="0" /></a>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>Demographic: <% if (request.getParameter("demo_linked_Id") != null && request.getParameter("demo_linked_Id").equals(curdoc.getDocId())) {%>
                                                                    <%=request.getParameter("name")%> <% }%>
                                                                </td>
                                                                <td><input type="hidden" name="demog" id="demofind<%=curdoc.getDocId()%>" />
                                                                    <input tabindex="<%=tabindex++%>" type="text" id="autocompletedemo<%=curdoc.getDocId()%>" onchange="checkSave('<%=curdoc.getDocId()%>')" name="demographicKeyword"  />
                                                                    <div id="autocomplete_choices<%=curdoc.getDocId()%>"class="autocomplete"></div>

                                                                    <script type="text/javascript">       <%-- testDemocomp2.jsp    --%>
                                                                    //new Ajax.Autocompleter("autocompletedemo<%=curdoc.getDocId()%>", "autocomplete_choices<%=curdoc.getDocId()%>", "../demographic/SearchDemographic.do", {minChars: 3, afterUpdateElement: saveDemoId});

                                                                    
                                                                    YAHOO.example.BasicRemote = function() {
                                                                            //var oDS = new YAHOO.util.XHRDataSource("http://localhost:8080/drugref2/test4.jsp");
                                                                            var url = "../demographic/SearchDemographic.do";
                                                                            var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
                                                                            oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
                                                                            // Define the schema of the delimited resultsTEST, PATIENT(1985-06-15)
                                                                            oDS.responseSchema = {
                                                                                resultsList : "results",
                                                                                fields : ["formattedName","fomattedDob","demographicNo"]
                                                                            };
                                                                            // Enable caching
                                                                            oDS.maxCacheEntries = 100;
                                                                            //oDS.connXhrMode ="cancelStaleRequests";

                                                                            // Instantiate the AutoComplete
                                                                            var oAC = new YAHOO.widget.AutoComplete("autocompletedemo<%=curdoc.getDocId()%>", "autocomplete_choices<%=curdoc.getDocId()%>", oDS);
                                                                            oAC.queryMatchSubset = true;
                                                                            oAC.minQueryLength = 3;
                                                                            oAC.maxResultsDisplayed = 25;
                                                                            oAC.formatResult = resultFormatter2;
                                                                            //oAC.typeAhead = true;
                                                                            oAC.queryMatchContains = true;
                                                                            oAC.itemSelectEvent.subscribe(function(type, args) {
                                                                               var str = args[0].getInputEl().id.replace("autocompletedemo","demofind");

                                                                               $(str).value = args[2][2];//li.id;
                                                                               //oscarLog("str value="+$(str).value);
                                                                               //oscarLog(args[2][1]+"--"+args[2][0]);
                                                                               args[0].getInputEl().value = args[2][0] + "("+args[2][1]+")";
                                                                               //oscarLog("--"+args[0].getInputEl().value);
                                                                               selectedDemos.push(args[0].getInputEl().value);
                                                                               //enable Save button whenever a selection is made
                                                                               $('save<%=curdoc.getDocId()%>').enable();
                                                                               
                                                                            });


                                                                            return {
                                                                                oDS: oDS,
                                                                                oAC: oAC
                                                                            };
                                                                        }();



                                                                    </script>
                                                                    <input type="checkbox" name="demoLink" >Send to MRP</input>
                                                                </td>
                                                            </tr>


                                                            <tr>
                                                                <td valign="top">Flag Provider: </td>

                                                                <td>
                                                                    <div class="myAutoComplete">
                                                                        <input tabindex="<%=tabindex++%>" type="text" id="autocompleteprov<%=curdoc.getDocId()%>" name="demographicKeyword"/>
                                                                        <div id="autocomplete_choicesprov<%=curdoc.getDocId()%>" class="autocomplete"></div>
                                                                    </div>

                                                                    <script type="text/javascript">

                                                                    YAHOO.example.FnMultipleFields = function(){

                                                                        // Instantiate AutoComplete
                                                                        var oAC = new YAHOO.widget.AutoComplete("autocompleteprov<%=curdoc.getDocId()%>", "autocomplete_choicesprov<%=curdoc.getDocId()%>", oDS);
                                                                        oAC.useShadow = true;
                                                                        oAC.resultTypeList = false;

                                                                        // Custom formatter to highlight the matching letters
                                                                        oAC.formatResult = resultFormatter;


                                                                        // Define an event handler to populate a hidden form field
                                                                        // when an item gets selected and populate the input field
                                                                        //var myHiddenField = YAHOO.util.Dom.get("myHidden");
                                                                        var myHandler = function(sType, aArgs) {
                                                                            var myAC = aArgs[0]; // reference back to the AC instance
                                                                            var elLI = aArgs[1]; // reference to the selected LI element
                                                                            var oData = aArgs[2]; // object literal of selected item's result data

                                                                            // update hidden form field with the selected item's ID
                                                                            //myHiddenField.value = oData.id;
                                                                           // console.log('IN HERE myHandler');
                                                                            var bdoc = document.createElement('a');
                                                                            bdoc.setAttribute("onclick", "removeProv(this);");
                                                                            bdoc.appendChild(document.createTextNode(" -remove- "));

                                                                            var adoc = document.createElement('div');
                                                                            adoc.appendChild(document.createTextNode(oData.fname + " " + oData.lname));

                                                                            var idoc = document.createElement('input');
                                                                            idoc.setAttribute("type", "hidden");
                                                                            idoc.setAttribute("name","flagproviders");
                                                                            idoc.setAttribute("value",oData.id);
                                                                         //   console.log(oData.id);
                                                                         //   console.log(myAC);
                                                                         //   console.log(elLI);
                                                                         //   console.log(oData);
                                                                         //   console.log(aArgs);
                                                                         //   console.log(sType);
                                                                            adoc.appendChild(idoc);

                                                                            adoc.appendChild(bdoc);
                                                                            var providerList = $('providerList<%=curdoc.getDocId()%>');
                                                                        //    console.log('Now HERE'+providerList);
                                                                            providerList.appendChild(adoc);
                                                        
                                                                            myAC.getInputEl().value = '';//;oData.fname + " " + oData.lname ;
                                                                        };
                                                                        oAC.itemSelectEvent.subscribe(myHandler);

                                                                        return {
                                                                            oDS: oDS,
                                                                            oAC: oAC
                                                                        };
                                                                    }();

                                                                   
                                                                    </script>
                                                                    <div id="providerList<%=curdoc.getDocId()%>"></div>
                                                                </td>
                                                            </tr>



                                                            <tr>
                                                                <td><bean:message key="dms.documentReport.msgCreator"/>:</td>
                                                                <td><%=curdoc.getCreatorName()%></td>
                                                            </tr>

                                                            <tr>
                                                                <td colspan="2" align="right"><input id="save<%=curdoc.getDocId()%>" tabindex="<%=tabindex++%>" type="submit" name="save" value="Save"  disabled/></td>
                                                            </tr>
                                                        </table>

                                                    </form>
                                                </fieldset>

                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <%}
            

                            if (category.size() == 0) {%>
                                <table>
                                    <tr>
                                        <td colspan="6">No documents to display</td>
                                    </tr>
                                </table>
                            <%}%> <%-- /table --%></div>
                        </div>
                        <%}%>
                    </div>
                    <div>
                        <input type="button" name="Button" value="<bean:message key="dms.documentReport.btnDoneClose"/>"onclick=self.close();>
                        <input type="button" name="print" value='<bean:message key="global.btnPrint"/>' onClick="window.print()">
                    </div>
                <!-- /html:form --></td>
            </tr>
            <tr>
                <td colspan="2" class="MainTableBottomRowRightColumn"></td>
            </tr>
        </table>


    </body>
</html:html>
