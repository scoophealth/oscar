<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%

            String user_no = (String) session.getAttribute("user");
            String userfirstname = (String) session.getAttribute("userfirstname");
            String userlastname = (String) session.getAttribute("userlastname");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarProvider.data.ProviderMyOscarIdData, oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="oscar.oscarProvider.data.*"%>
<%
            List providers = ProviderData.getProviderList();
            String provider = "";

//if delete request is made
            if (request.getParameter("delDocumentNo") != null) {
                EDocUtil.deleteDocument(request.getParameter("delDocumentNo"));
            }

//view  - tabs
            String view = "all";
            if (request.getParameter("view") != null) {
                view = request.getParameter("view");
            } else if (request.getAttribute("view") != null) {
                view = (String) request.getAttribute("view");
            }
//preliminary JSP codec

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
            String sort = EDocUtil.SORT_OBSERVATIONDATE;
            String sortRequest = request.getParameter("sort");
            if (sortRequest != null) {
    if (sortRequest.equals("description")) sort = EDocUtil.SORT_DESCRIPTION;
    else if (sortRequest.equals("type")) sort = EDocUtil.SORT_DOCTYPE;
    else if (sortRequest.equals("contenttype")) sort = EDocUtil.SORT_CONTENTTYPE;
    else if (sortRequest.equals("creator")) sort = EDocUtil.SORT_CREATOR;
    else if (sortRequest.equals("uploaddate")) sort = EDocUtil.SORT_DATE;
    else if (sortRequest.equals("observationdate")) sort = EDocUtil.SORT_OBSERVATIONDATE;
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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="dms.documentReport.title" /></title>
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


<script type="text/javascript" src="../share/javascript/swfupload.js"></script>

<script type="text/javascript"
	src="../share/javascript/swfupload.queue.js"></script>
<script type="text/javascript" src="../share/javascript/fileprogress.js"></script>
<script type="text/javascript" src="../share/javascript/handlers.js"></script>


<script type="text/javascript">
            var swfu;

            window.onload = function() {
                var settings = {
                    upload_url: "../dms/addEditDocument.do;jsessionid=<%=request.getRequestedSessionId()%>", 	// Relative to the SWF file
                    file_post_name : "filedata",
                    post_params: {"method" : "multifast"},
                    use_query_string : true,

                    flash_url :  "../share/swfupload_f9.swf",

                    file_size_limit : "100 MB",
                    file_types : "*.pdf",
                    file_types_description : "All Files",
                    file_upload_limit : 100,
                    file_queue_limit : 0,
                    custom_settings : {
                        progressTarget : "fsUploadProgress",
                        cancelButtonId : "btnCancel"
                    },
                    debug: false,

                    // The event handler functions are defined in handlers.js
                    file_queued_handler : fileQueued,
                    file_queue_error_handler : fileQueueError,
                    file_dialog_complete_handler : fileDialogComplete,
                    upload_start_handler : uploadStart,
                    upload_progress_handler : uploadProgress,
                    upload_error_handler : uploadError,
                    upload_success_handler : uploadSuccess,
                    upload_complete_handler : uploadComplete,
                    queue_complete_handler : queueComplete	// Queue plugin event
                };

                swfu = new SWFUpload(settings);


            if(!NiftyCheck())
                return;

            Rounded("div.doclist","top","transparent", "#ccccd7", "small border #ccccd7");
            Rounded("div.doclist","bottom","transparent", "#e0ecff", "small border #ccccd7");
            Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
            Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");
            //onloadfunction();
            setup();  //reload parent content if necessary


            };


            function addProviderToPost(ele){
                //console.log("ele.options[ele.selectedIndex].value "+ele.options[ele.selectedIndex].value);
                swfu.addPostParam("provider",ele.options[ele.selectedIndex].value);
            }
        </script>




<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<script type="text/javascript" src="../phr/phr.js"></script>
<script type="text/javascript">



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
        var update = "<%=updateParent%>";
        var parentId = "<%=parentAjaxId%>";
        var Url = window.opener.URLs;

        if( update == "true" && !window.opener.closed )
            window.opener.popLeftColumn(Url[parentId], parentId, parentId);
    }



    function saveDemoId(text, li){
       //console.log("saveDemoId "+li.id+" "+text.id);
       var str = text.id.replace("autocompletedemo","demofind");
       //console.log("str "+str);
       $(str).value = li.id;
    }

    function saveProvId(text, li){
      // console.log("saveProvId "+li.id+" "+text.id+" "+text.value);
       var provName = text.value;
       var str = text.id.replace("autocompletedemo","demofind");
      // console.log("str "+str);
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
        var toSend = $(formId).serialize(true);
        var url = "ManageDocument.do";//"send.jsp";
        Effect.SlideUp('document'+toSend.documentId);
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

    </script>


</head>
<body class="bodystyle">

<table class="MainTable" id="scrollNumber1" name="encounterTable"
	style="margin: 0px;">
	<tr class="MainTableRowTop">
		<td class="MainTableTopRowLeftColumn" width="60px">eDocs</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Documents</td>
				<td>&nbsp;</td>
				<td style="text-align: right;"><oscar:help keywords="document" key="app.top1" style="" /> | <a
					href="javascript: popupStart(300, 400, 'About.jsp')">About</a> | <a
					href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<%--
         <td class="MainTableLeftColumn" valign="top">
             <div class="leftplane">
                  <h3>&nbsp; Tags</h3>
                  <div style="background-color: #EEEEFF;">
                      <ul>
                         <li>Tag 1</li>
                         <li>Tag 2</li>
                         <li>Tag 3</li>=
                      </ul>
                  </div>
             </div>
         </td>
                --%>
		<td class="MainTableRightColumn" colspan="2" valign="top">


		<form id="form1" action="../dms/addEditDocument.do" method="post"
			enctype="multipart/form-data"><label for="provider"
			class="fields">Send to Provider:</label> <select
			onchange="javascript:addProviderToPost(this);" id="providerDrop"
			name="provider">
			<option value="-1" <%= ("-1".equals(provider) ? " selected" : "")%>>None</option>
			<%for (int i = 0; i < providers.size(); i++) {
                Map h = (Map) providers.get(i);%>
			<option value="<%= h.get("providerNo")%>"
				<%= (h.get("providerNo").equals(provider) ? " selected" : "")%>><%= h.get("lastName")%>
			<%= h.get("firstName")%></option>
			<%}%>

		</select>


		<fieldset class="flash" id="fsUploadProgress"><legend>Upload
		Queue</legend></fieldset>
		<div id="divStatus">0 Files Uploaded</div>
		<div><%--input type="file" name="filedata"/--%> <input
			type="button" value="Upload file (Max 100 MB)"
			onclick="swfu.selectFiles()" style="font-size: 8pt;" /> <input
			id="btnCancel" type="button" value="Cancel All Uploads"
			onclick="swfu.cancelQueue();" disabled="disabled"
			style="font-size: 8pt;" /></div>

		</form>


                <a href="undocumentReport2.jsp">Link Records</a>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>


</body>
</html:html>
