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
<%@page import="org.oscarehr.util.SessionConstants,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.oscarehr.common.model.*"%>

<%

            String startDate = request.getParameter("startDate");
            String endDate   = request.getParameter("endDate");
            String byProvider = request.getParameter("provider");
            //String  = null;
            if (startDate == null){
                startDate = oscar.util.UtilDateUtilities.DateToString(oscar.util.UtilDateUtilities.Today(), "yyyy-MM-dd");
            }
            if (endDate == null){
                endDate = oscar.util.UtilDateUtilities.DateToString(oscar.util.UtilDateUtilities.Tomorrow(), "yyyy-MM-dd");
            }
            if (byProvider == null){
                byProvider = (String) session.getAttribute("user");
            }
            System.out.println("PROV "+byProvider);

            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            ProviderDao providerDao = (ProviderDao) ctx.getBean("providerDao");

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
            Integer currentFacilityId = (Integer) session.getAttribute(SessionConstants.CURRENT_FACILITY_ID);

            String viewstatus = "active";

            List<Provider> providers = providerDao.getActiveProviders();
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
            String sort = EDocUtil.SORT_OBSERVATIONDATE;
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
        
        
        <script type="text/javascript" src="../share/javascript/nifty.js"></script>
        <script type="text/javascript" src="../phr/phr.js"></script>
        <script type="text/javascript">
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
                    document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(minus, plus);;
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
            console.log("set up");
        //    var update = "<%=updateParent%>";
        //    var parentId = "<%=parentAjaxId%>";
        //    var Url = window.opener.URLs;
    
        //    if( update == "true" && !window.opener.closed )
        //       window.opener.popLeftColumn(Url[parentId], parentId, parentId);
        }
    
    
    
        function saveDemoId(text, li){
            console.log("saveDemoId "+li.id+" "+text.id);
            var str = text.id.replace("autocompletedemo","demofind");
            console.log("str "+str);
            $(str).value = li.id;
        }
    
        function saveProvId(text, li){
            console.log("saveProvId "+li.id+" "+text.id+" "+text.value);
            var provName = text.value;
            var str = text.id.replace("autocompletedemo","demofind");
            var str2 = text.id.replace("autocompleteprov","providerList");
            console.log("str "+str);
            console.log("str2 "+str2);
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
        
            $(str2).appendChild(adoc);
            text.value = '';
        }
    
        function removeProv(th){
            var ele = th.up();
            ele.remove();
        
        }
    
        function sendToServer(formId){
            var toSend = $(formId).serialize(true);
            var url = "ManageDocument.do";//"send.jsp";
            Effect.SlideUp('document'+toSend.documentId);
            new Ajax.Request(url, { method: 'post', parameters: toSend, onSuccess: successAdjusting });
  
            //Effect.SlideUp('document'+toSend.documentId);
            return false; 
        }
    
        function successAdjusting(transport){
            var jason = transport.responseText.evalJSON(true);
            console.log("successlog"+jason.success+"   "+jason.docId);                
        
        

 

        }
        /*
         * $('document'+jason.docId).remove();
 
         */

    
        function autoCompleteShowMenu(){
            console.log("autoCompleteShowMenu");
        }
    
        function autoCompleteHideMenu(){
            console.log("autoCompleteHideMenu"); 
        }
        
        function callReplacementWebService(url,id){
            var ran_number=Math.round(Math.random()*1000000);
            
            var params = "provNo="+$("byproviderDrop").getValue()+"&start="+$("startRangeDate").getValue()+"&end="+$("endRangeDate").getValue()+"&prov="+$("provMatched").getValue()+"&demo="+$("demoMatched").getValue()+"&rand="+ran_number;  //hack to get around ie caching the page
            new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true,evalScripts:true}); 
        } 
    
        </script>
        
        
        <style type="text/css"> 	 
	         div.autocomplete { 	 
	   position:absolute; 	 
	   width:250px; 	 
	   background-color:white; 	 
	   border:1px solid #888; 	 
	   margin:0px; 	 
	   padding:0px; 	 
	 } 	 
	 div.autocomplete ul { 	 
	   list-style-type:none; 	 
	   margin:0px; 	 
	   padding:0px; 	 
	 } 	 
	 div.autocomplete ul li.selected { background-color: #ffb;} 	 
	 div.autocomplete ul li { 	 
	   list-style-type:none; 	 
	   display:block; 	 
	   margin:0; 	 
	   padding:2px; 	 
	   height:32px; 	 
	   cursor:pointer; 	 
	 } 	 
	         label.fields{ 	 
	             width:200px; 	 
	         } 	 
	  	 
	     </style>
        
        
        
    </head>
    <body class="bodystyle">
    
    <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
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
        <%--
        <ul>
            <li>- Limit to only PDFs being uploaded</li>
            <li>- Change so that other graphics could be displayed</li>
            <!-- li>- if moduleid -1 is keeping the documents here what will keep them here to send to a provider  (EG you match a patient...then</li -->
        </ul>
        <ul>
            <li>+ way to send to a provider (using lab system)</li>
            <li>+ way to easily add desc and type</li>
            <li>- if moduleid -1 is keeping the documents here what will keep them here to send to a provider  (EG you match a patient...then</li>
            <li>- Once linked with a patient offer to mark a physicians record </li>
            <li>+ clicking on the graphic should pop up the viewer</li>
            <li>+ The idea of a queue (EG sfhc will have way to many common documents)</li>
            <li>+ Need a way to show if the document is currently linked with a provider</li>
            
            <li>-Away to short the documents eg for provider , by provider, date range.  matched provider, matched demographic</li>
            <li> Module  <%=module%>| module id<%=moduleid%>|  view <%=view%>| type? <%=EDocUtil.PRIVATE%>| sort <%=sort%>| viewstat <%=viewstatus%>| fac id <%=currentFacilityId%>  
            </li>
            
        </ul>
        --%>
        <input type="hidden" name="curUser" value="<%=curUser%>"> 
        <input type="hidden" name="demoId" value="<%=moduleid%>">
        <div class="documentLists"><%-- STUFF TO DISPLAY --%> <%

            



            
            
            %>
            
            
            
            <div class="doclist">
                <div class="headerline">
                    <div class="docHeading">
                        Show Documents --
                        <%-- For provider .... at somepoint
                                    <select id="providerDrop" name="provider"> 
                                        <option value="-1" <%= ("-1".equals(provider) ? " selected" : "")%> >None</option>
                                        <%for (Provider prov:providers) { %>
                                        <option value="<%= prov.getProviderNo()%>" <%= (prov.getProviderNo().equals(provider) ? " selected" : "")%>><%= prov.getLastName()%> <%= prov.getFirstName()%></option>
                                        <%}%>                    

                                    </select>  |
                        --%>
                        By provider 
                        <select id="byproviderDrop" name="byprovider"> 
                            <option value="-1" <%= ("-1".equals(byProvider) ? " selected" : "")%> >None</option>
                            <%for (Provider prov : providers) {%>
                            <option value="<%= prov.getProviderNo()%>" <%= (prov.getProviderNo().equals(byProvider) ? " selected" : "")%>><%= prov.getLastName()%> <%= prov.getFirstName()%></option>
                            <%}%>                    
                            
                        </select> | 
                        Start: <input id="startRangeDate" name="startRangeDate" type="text" size="10" value="<%=startDate%>" >
                        <a id="startRangeDateCal"><img title="Calendar" src="../images/cal.gif" alt="Calendar"border="0" /></a>.   
                        End: <input   id="endRangeDate" name="endRangeDate" type="text" size="10" value="<%=endDate%>">
                        <a id="endRangeDateCal"><img title="Calendar" src="../images/cal.gif" alt="Calendar"border="0" /></a>
                        Matched <input type="checkbox" name="provMatched" id="provMatched">Provider</input> 
                                <input type="checkbox" name="demoMatched" id="demoMatched">Demographic</input>
                        <br>
                        
                    </div>
                </div>
                <div id="herehere"></div>
                <script type="text/javascript">       
                callReplacementWebService('DocumentDisplay.jsp','herehere');                                                        
                </script>
                
                
                
                
            </div>
        </div>
        
        
        <div>
            <input type="button" name="Button" value="<bean:message key="dms.documentReport.btnDoneClose"/>" onclick=self.close();> 
            <%--<input type="button" name="print" value='<bean:message key="global.btnPrint"/>' onClick="window.print()"> 
            <input type="button" value="Combine PDFs" onclick="return submitForm('<rewrite:reWrite jspPage="combinePDFs.do"/>');" />
            --%>
            <input type="button" value="Upload More Documents" onclick="window.location='uploadMultiDocument.jsp'"/>
            <input type="button" onclick="callReplacementWebService('DocumentDisplay.jsp','herehere');" value="Get More"/>     
        </div>
    </td>
    </tr>
    <tr>
        <td colspan="2" class="MainTableBottomRowRightColumn"></td>
    </tr>
    </table>
    
    
    </body>
</html:html>
