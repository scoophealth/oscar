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
if(session.getValue("user") == null) response.sendRedirect("../logout.htm");
String user_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="page" />
<%@ page
	import="java.util.*, oscar.*, oscar.util.*, oscar.dms.*, oscar.dms.data.*"%>


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%
String mode = "";
if (request.getAttribute("mode") != null) {
    mode = (String) request.getAttribute("mode");
} else if (request.getParameter("mode") != null) {
    mode = (String) request.getParameter("mode");
}

String editDocumentNo = "";
if (request.getAttribute("editDocumentNo") != null) {
    editDocumentNo = (String) request.getAttribute("editDocumentNo");
    mode = editDocumentNo;
} else if (request.getParameter("editDocumentNo") != null) {
    editDocumentNo = (String) request.getParameter("editDocumentNo");
    mode = editDocumentNo;
}

String module = "";
String moduleid = "";
if (request.getParameter("function") != null) {
    module = request.getParameter("function");
    moduleid = request.getParameter("functionid");
} else if (request.getAttribute("function") != null) {
    module = (String) request.getAttribute("function");
    moduleid = (String) request.getAttribute("functionid");
}

OscarProperties props = OscarProperties.getInstance();
String defaultType = (String) props.getProperty("eDocAddTypeDefault", "");
String defaultDesc = "Enter Title"; //if defaultType isn't defined, this value is used for the title/description

Hashtable linkhtmlerrors = new Hashtable();
if (request.getAttribute("linkhtmlerrors") != null) {
    linkhtmlerrors = (Hashtable) request.getAttribute("linkhtmlerrors");
}

   String lastUpdate = "";
   String fileName = "";
   AddEditDocumentForm formdata = new AddEditDocumentForm();
if (request.getAttribute("completedForm") != null) {
    formdata = (AddEditDocumentForm) request.getAttribute("completedForm");
    lastUpdate = EDocUtil.getDmsDateTime();
} else if ((editDocumentNo != null) && (!editDocumentNo.equals(""))) {
    EDoc currentDoc = EDocUtil.getDoc(editDocumentNo);
    formdata.setFunction(currentDoc.getModule());
    formdata.setFunctionId(currentDoc.getModuleId());
    formdata.setDocType(currentDoc.getType());
    formdata.setDocDesc(currentDoc.getDescription());
    formdata.setDocPublic((currentDoc.getDocPublic().equals("1"))?"checked":"");
    formdata.setDocCreator(currentDoc.getCreatorId());
    formdata.setObservationDate(currentDoc.getObservationDate());
    formdata.setHtml(UtilMisc.htmlEscape(currentDoc.getHtml()));
    lastUpdate = currentDoc.getDateTimeStamp();
    fileName = currentDoc.getFileName();
} else {
    formdata.setFunction(module);  //"module" and "function" are the same
    formdata.setFunctionId(moduleid);
    formdata.setDocType(defaultType);
    formdata.setDocDesc(defaultType.equals("")?defaultDesc:defaultType);
    formdata.setDocCreator(user_no);
    formdata.setObservationDate(UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"));
    lastUpdate = "--";
}
ArrayList doctypes = EDocUtil.getDoctypes(module);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache">
<title>Edit Document</title>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<link rel="stylesheet" type="text/css"
	href="../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<link rel="stylesheet" type="text/css" href="dms.css" />
<link rel="stylesheet" type="text/css"
	href="../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">
            window.onload=function(){
                if(!NiftyCheck())
                    return;
                    //Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
                    //Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");
            }
            function submitUpload(object) {
                object.Submit.disabled = true;
                if (!validDate("observationDate")) {
                    alert("Invalid Date: must be in format yyyy/mm/dd");
                    object.Submit.disabled = false;
                    return false;
                }
                return true;
            }
            function checkDefaultValue(object) {
              //selectBoxType = object.form.docType
              //var selectedType = selectBoxType.options[selectBoxType.selectedIndex].value;
              if ((object.value == "<%= defaultDesc%>") || (object.value == "<%= defaultType%>")) {
                  object.value = "";
              }
            }
            function checkSel(sel){
              theForm = sel.form;
              if ((theForm.docDesc.value == "") || (theForm.docDesc.value == "<%= defaultDesc%>")) {
                   theForm.docDesc.value = theForm.docType.value;
                   theForm.docDesc.focus();
                   theForm.docDesc.select();
              }
            }
        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body class="mainbody">
<div class="maindiv">
<div class="maindivheading">&nbsp;&nbsp;&nbsp; Edit Document</div>
<%-- Lists linkhtmlerrors --%> <% for (Enumeration errorkeys = linkhtmlerrors.keys(); errorkeys.hasMoreElements();) {%>
<font class="warning">Error: <bean:message
	key="<%=(String) linkhtmlerrors.get(errorkeys.nextElement())%>" /></font><br />
<% } %> <html:form action="/dms/addEditHtml" method="POST"
	enctype="multipart/form-data" styleClass="form"
	onsubmit="return submitUpload(this);">
	<input type="hidden" name="function"
		value="<%=formdata.getFunction()%>" size="20">
	<input type="hidden" name="functionId"
		value="<%=formdata.getFunctionId()%>" size="20">
	<input type="hidden" name="functionid" value="<%=moduleid%>" size="20">
	<input type="hidden" name="mode" value="<%=mode%>">
	<input type="hidden" name="docCreator"
		value="<%=formdata.getDocCreator()%>">
	<table width="100%" height="100%" class="layouttable">
		<tr>
			<td width="180px">Type:</td>
			<td><select name="docType" id="docType"
				onchange="checkSel(this)"
				<% if (linkhtmlerrors.containsKey("typemissing")) {%>
				class="warning" <%}%>>
				<option value=""><bean:message
					key="dms.addDocument.formSelect" /></option>
				<%for (int i=0; i<doctypes.size(); i++) {
                             String doctype = (String) doctypes.get(i); %>
				<option value="<%= doctype%>"
					<%=(formdata.getDocType().equals(doctype))?" selected":""%>><%= doctype%></option>
				<%}%>
			</select></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><input <% if (linkhtmlerrors.containsKey("descmissing")) {%>
				class="warning" <%}%> type="text" name="docDesc" size="30"
				onfocus="checkDefaultValue(this)" value="<%=formdata.getDocDesc()%>"></td>
		</tr>
		<tr>
			<td>Added By:</td>
			<td><%=EDocUtil.getModuleName("provider", formdata.getDocCreator())%></td>
		</tr>
		<tr>
			<td>Date Added/Updated:</td>
			<td><%=lastUpdate%></td>
		</tr>
		<tr>
			<td>Observation Date <font class="comment">(yyyy/mm/dd):</font></td>
			<td><input type="text" name="observationDate"
				id="observationDate" value="<%=formdata.getObservationDate()%>"><a
				id="obsdate"><img title="Calendar" src="../images/cal.gif"
				alt="Calendar" border="0" /></a></td>
		</tr>
		<% if (module.equals("provider")) {%>
		<tr>
			<td>Public?</td>
			<td><input type="checkbox" name="docPublic"
				<%=formdata.getDocPublic() + " "%> value="checked"></td>
		</tr>
		<%}%>
		<tr>
			<td valign="top">Html:</td>
			<td>&nbsp;</td>
		</tr>
		<tr style="height: 100%;">
			<td colspan="2"><textarea name="html"
				<% if (linkhtmlerrors.containsKey("uploaderror")) {%>
				class="warning" <%}%> wrap="off" style="width: 98%; height: 200px;"><%=formdata.getHtml()%></textarea></td>
		</tr>
	</table>
	<center>
	<div><input type="submit" name="Submit" value="Submit"><input
		type="button" value="Cancel" onclick="window.close();"></div>
	</center>
</html:form> <script type="text/javascript">
               Calendar.setup( { inputField : "observationDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "obsdate", singleClick : true, step : 1 } );
           </script></div>
</body>
</html>