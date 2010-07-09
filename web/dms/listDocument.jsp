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
            String moduleid = "-1";
            String curUser = "";
            if (request.getParameter("curUser") != null) {
                curUser = request.getParameter("curUser");
            } else if (request.getAttribute("curUser") != null) {
                curUser = (String) request.getAttribute("curUser");
            }
            String module = "demographic";
            ArrayList doctypes = EDocUtil.getDoctypes(module);
                        String updateParent;
            if (request.getParameter("updateParent") != null) {
                updateParent = request.getParameter("updateParent");
            } else {
                updateParent = "false";
            }
            String parentAjaxId;
            if (request.getParameter("parentAjaxId") != null) {
                parentAjaxId = request.getParameter("parentAjaxId");
            } else if (request.getAttribute("parentAjaxId") != null) {
                parentAjaxId = (String) request.getAttribute("parentAjaxId");
            } else {
                parentAjaxId = "";
            }
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
<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <title><bean:message key="dms.documentReport.title" /></title>
        <meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
        <meta http-equiv="Cache-Control" content="no-cache">

        
        <script type="text/javascript" src="../share/javascript/Oscar.js"></script>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/effects.js"></script>
        <script type="text/javascript" src="../share/javascript/controls.js"></script>



        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/js/newCaseManagementView.js"/>"></script>
        
        <!-- main calendar program -->
        <script type="text/javascript" src="../share/calendar/calendar.js"></script>
        <!-- language for the calendar -->
        <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
        <!-- the following script defines the Calendar.setup helper function, which makes
               adding a calendar a matter of 1 or 2 lines of code. -->
        <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

        <!-- calendar stylesheet -->
        <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>

        <!--link rel="stylesheet" href="../web.css"-->
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <link rel="stylesheet" type="text/css" href="../share/css/niftyCorners.css" />
        <!--link rel="stylesheet" type="text/css" href="dms.css" /-->
        <link rel="stylesheet" type="text/css" href="../share/css/niftyPrint.css" media="print" />
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />
        <script type="text/javascript" src="../share/javascript/nifty.js"></script>
        <script type="text/javascript" src="../phr/phr.js"></script>
        <script type="text/javascript" src="../js/demographicProviderAutocomplete.js"></script>
        <script type="text/javascript">
        function renderCalendar(id,inputFieldId){
                Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
        }
       //var selectedDemos=new Array();
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
    oDS.responseSchema = {fields: ["id", "fname", "lname"]};
 //   console.log("oDS="+oDS);
//    console.log("oDS.id="+oDS.id);
  //  console.log("oDS.fname="+oDS.fname);
  //  console.log("oDS.lname="+oDS.lname);


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

      /*  function removeProv(th){
            var ele = th.up();
            ele.remove();

        }
*/
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


 /*       function checkSave(elementId){
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
*/
        function callReplacementWebService(url,id,providerNo){
              var ran_number=Math.round(Math.random()*1000000);
              var params = "currentUser="+providerNo+"&rand="+ran_number;
              var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:false,evalScripts:true});
         }
        function callAdditionWebService(url,id,providerNo){
                 var params = "currentProvider="+providerNo;
                 var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:false,insertion: Insertion.Bottom,evalScripts:true});
        }
        function showDocumentProvider(providerNo){
            if($("DocumentProvider_"+providerNo)==null){
                 var params = "currentProvider="+providerNo;
                 var url="listDocumentFromProvider.jsp";
                 var id="doclists";
                 var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:false,insertion: Insertion.Bottom,evalScripts:true});
            }else{
                $("DocumentProvider_"+providerNo).toggle();
            }
     }
     function showDocumentQueue(qid,qname){
            if($("DocumentQueue_"+qid)==null){
                 var params = "currentQueueId="+qid+"&currentQueueName="+qname;
                 oscarLog(params);
                 var url="listDocumentFromQueue.jsp";
                 var id="doclists";
                 var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:false,insertion: Insertion.Bottom,evalScripts:true});
            }else{
                $("DocumentQueue_"+qid).toggle();
            }
     }

        </script>


    </head>

    <body class="yui-skin-sam2"> <%-- class="bodystyle" --%>


        <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">eDocs</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Documents <a onclick="$('todolist').toggle()" href="javascript:void(0);">*</a> </td>
                            <!--td>&nbsp;</td-->
                            <td style="text-align: right;"  >
                                    <a href="javascript: popupStart(300, 400, 'Help.jsp')">Help</a> |
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" valign="top">
                    <ul id="todolist" style="display:none">
                        <li>- way to send to a provider (using lab system)</li>
                        <li>+ way to easily add desc and type</li>
                        <li>- if moduleid -1 is keeping the documents here what will keep them here to send to a provider  (EG you match a patient...then</li>
                        <li>- Once linked with a patient offer to mark a physicians record </li>
                        <li>- clicking on the graphic should pop up the viewer(done)</li>
                        <li>- The idea of a queue (EG sfhc will have way to many common documents)</li>
                        <li>- Need a way to show if the document is currently linked with a provider</li>

                    </ul> <!--html:form action="/dms/combinePDFs" -->
                </td>
            </tr>
            <tr>
                <td colspan="2" valign="top">
                    <input type="hidden" name="curUser" value="<%=curUser%>">
                    <input type="hidden" name="demoId" value="<%=moduleid%>">
                    
<%
                        Hashtable providerDocs=(Hashtable)request.getSession().getAttribute("providerDocs");
                        Hashtable queueDocs=(Hashtable)request.getSession().getAttribute("queueDocs");
                        List<String> providerNos=new ArrayList();
                        if(providerDocs!=null && !providerDocs.isEmpty()){
                            Enumeration keys= providerDocs.keys();
                            while(keys.hasMoreElements())
                                providerNos.add((String)keys.nextElement());
                        }
                        List<String> queueIds=new ArrayList();
                        if(queueDocs!=null && ! queueDocs.isEmpty()){
                            Enumeration keys=queueDocs.keys();
                            while(keys.hasMoreElements()){
                                queueIds.add((String)keys.nextElement());
                            }
                        }

                        %>

                    </td>
            </tr>
            <tr>
                    <td colspan="2" valign="top">
                        <div class="providers">
                            (
                                    <%for(int i4=0;i4<providerNos.size();i4++){
                                         String pn=(String)providerNos.get(i4);
                                         String providerName=ProviderData.getProviderName(pn);
                                         if(i4==providerNos.size()-1)
                                             ;
                                         else
                                             providerName+=",";
                                         %>
                                        <a href="javascript:void(0);" onclick="showDocumentProvider('<%=pn%>')">
                                            <%=providerName%>
                                        </a>
                                    <%}%>                                          )
                        </div>
                        <div class="queues">
                            (
                                    <%for(int i4=0;i4<queueIds.size();i4++){
                                         String qid=(String)queueIds.get(i4);
                                         String qname=QueueData.getQueueName(Integer.parseInt(qid));
                                         if(i4==queueIds.size()-1)
                                             ;
                                         else
                                             qname+=",";
                                         %>
                                         <a href="javascript:void(0);" onclick="showDocumentQueue('<%=qid%>','<%=qname%>')">
                                            <%=qname%>
                                        </a>
                                    <%}%>                                          )
                        </div>
                    </td>
            </tr>
            <tr>
                    <td colspan="2" valign="top">
                    <div id="doclists" class="doclist">     </div>

                    </td>
            </tr>
            <tr>
                    <td colspan="2" valign="top">
                    <div>
                        <input type="button" name="Button" value="<bean:message key="dms.documentReport.btnDoneClose"/>"onclick=self.close();>
                        <input type="button" name="print" value='<bean:message key="global.btnPrint"/>' onClick="window.print()">
                    </div>
                    </td>
            </tr>               
        </table>
<script type="text/javascript">
     showDocumentProvider('<%=request.getSession().getAttribute("user").toString()%>');
</script>

    </body>
</html:html>
