<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
--%>




<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.util.*, oscar.util.*, oscar.OscarProperties, oscar.dms.*, oscar.dms.data.*"%>
<%--This is included in documentReport.jsp - wasn't meant to be displayed as a separate page --%>
<%
String user_no = (String) session.getAttribute("user");

String module = "";
String moduleid = "";
if (request.getParameter("function") != null) {
    module = request.getParameter("function");
    moduleid = request.getParameter("functionid");
} else if (request.getAttribute("function") != null) {
    module = (String) request.getAttribute("function");
    moduleid = (String) request.getAttribute("functionid");
}

String curUser = "";
if (request.getParameter("curUser") != null) {
    curUser = request.getParameter("curUser");    
} else if (request.getAttribute("curUser") != null) {
    curUser = (String) request.getAttribute("curUser");    
}

OscarProperties props = OscarProperties.getInstance();

AddEditDocumentForm formdata = new AddEditDocumentForm();
String defaultType = (String) props.getProperty("eDocAddTypeDefault", "");
String defaultDesc = "Enter Title"; //if defaultType isn't defined, this value is used for the title/description
String defaultHtml = "Enter Link URL";

Hashtable docerrors = new Hashtable();
if (request.getAttribute("docerrors") != null) {
    docerrors = (Hashtable) request.getAttribute("docerrors");
}

Hashtable linkhtmlerrors = new Hashtable();
if (request.getAttribute("linkhtmlerrors") != null) {
    linkhtmlerrors = (Hashtable) request.getAttribute("linkhtmlerrors");
}

//for "add document" link from the patient master page - the "mode" variable allows the add div to open up
String mode = "";
if (request.getAttribute("mode") != null) {
    mode = (String) request.getAttribute("mode");
} else if (request.getParameter("mode") != null) {
    mode = (String) request.getParameter("mode");
}

//Retrieve encounter id for updating encounter navbar if info this page changes anything
String parentAjaxId;
if( request.getParameter("parentAjaxId") != null )
    parentAjaxId = request.getParameter("parentAjaxId");
else if( request.getAttribute("parentAjaxId") != null )
    parentAjaxId = (String)request.getAttribute("parentAjaxId");
else
    parentAjaxId = "";

if (request.getAttribute("completedForm") != null) {
formdata = (AddEditDocumentForm) request.getAttribute("completedForm");
/*  Moved the edit to a separate popup window
} else if (editDocumentNo != null) {
    EDoc currentDoc = EDocUtil.getDoc(editDocumentNo);
    formdata.setFunction(currentDoc.getModule());
    formdata.setFunctionId(currentDoc.getModuleId());
    formdata.setDocType(currentDoc.getType());
    formdata.setDocDesc(currentDoc.getDescription());
    formdata.setDocPublic((currentDoc.getDocPublic().equals("1"))?"checked":"");
    formdata.setDocCreator(user_no);
    formdata.setObservationDate(currentDoc.getObservationDate());*/
} else {
    formdata.setFunction(module);  //"module" and "function" are the same
    formdata.setFunctionId(moduleid);
    formdata.setDocType((String) props.getProperty("eDocAddTypeDefault", ""));
    formdata.setDocDesc(defaultType.equals("")?defaultDesc:defaultType);
    formdata.setDocCreator(user_no);
    formdata.setObservationDate(UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"));
    formdata.setHtml(defaultHtml);
}
ArrayList doctypes = EDocUtil.getDoctypes(formdata.getFunction());
%>
<script src="../share/javascript/Oscar.js"
	type="text/javascript language='JavaScript'"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" language="JavaScript">
function onloadfunction() {
    if(!NiftyCheck())
        return;

    Rounded("div.topplane","top","transparent","#d1d5bd","small border #d1d5bd");
    Rounded("div.topplane","bottom","transparent","#f2f7ff","small border #d1d5bd");
    <%--request attribute "linkhtmlerrors" & "docerrors" is used to check if a document was just submitted --%>
    <% if (request.getAttribute("linkhtmlerrors") != null) { //Open AddLink div%>
    showhide('addLinkDiv', 'plusminusLinkA');
    <%} else if ((request.getAttribute("docerrors") != null) || (!mode.equals(""))) { //Open AddDoc div%> 
    showhide('addDocDiv', 'plusminusAddDocA');
    //setFocus();
    <%}%>
    
}
  function checkSel(sel){
  theForm = sel.form;
  if ((theForm.docDesc.value == "") || (theForm.docDesc.value == "<%= defaultDesc%>")) {
       theForm.docDesc.value = theForm.docType.value;
       theForm.docDesc.focus();
       theForm.docDesc.select();
  }
}

function checkDefaultValue(object) {
  //selectBoxType = object.form.docType
  //var selectedType = selectBoxType.options[selectBoxType.selectedIndex].value;
  if ((object.value == "<%= defaultDesc%>") || (object.value == "<%= defaultType%>") || (object.value == "<%= defaultHtml %>")) {
      object.value = "";
  }
}

/* no need
function setFocus() {
	var focusTo = document.getElementsByClassName("warning", "topplane");
            if (focusTo[i].type == "text") {
               focusTo[i].focus();
            i = focusTo.length;
            
           }
            }
}*/

function showhide(hideelement, button) {
    var plus = "+";
    var minus = "--";
    if (document.getElementById) { // DOM3 = IE5, NS6
        if (document.getElementById(hideelement).style.display == 'none') {
              document.getElementById(hideelement).style.display = 'block';
              document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(plus, minus);
              if ((hideelement == "addDocDiv") && (document.getElementById("addLinkDiv").style.display != "none")) {
                   showhide("addLinkDiv", "plusminusLinkA");
              } else if ((hideelement == "addLinkDiv") && (document.getElementById("addDocDiv").style.display != "none")) {
                   showhide("addDocDiv", "plusminusAddDocA");
              }
        }
        else {
              document.getElementById(hideelement).style.display = 'none';
              document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(minus, plus);;
        }
    }
}

function submitUpload(object) {
    object.Submit.disabled = true;
    if (!validDate("observationDate")) {
        alert("Invalid Date: must be in format: yyyy/mm/dd");
        object.Submit.disabled = false;
        return false;
    }
    return true;
}

function submitUploadLink(object) {
    object.Submit.disabled = true;
    return true;
}

//clears default values
function checkDefaultDate(object, defaultValue) {
   if (object.value == defaultValue) {
       object.value = "";
   }
}
</script>
<div class="topplane">
<div class="docHeading" style="background-color: #d1d5bd;">
    <a id="plusminusAddDocA" href="javascript: showhide('addDocDiv', 'plusminusAddDocA');"> +<bean:message key="dms.addDocument.msgAddDocument"/></a> 
    <%-- a id="plusminusAddDocA" href="undocumentReport2.jsp"> +<bean:message key="dms.addDocument.msgManageUploadDocument"/></a --%> 
    <a id="plusminusLinkA" href="javascript: showhide('addLinkDiv', 'plusminusLinkA')"> +<bean:message key="dms.addDocument.AddLink"/> </a> 
    <a href="javascript:;" onclick="popup(450, 600, 'addedithtmldocument.jsp?function=<%=module%>&functionid=<%=moduleid%>&mode=addHtml', 'addhtml')">+<bean:message key="dms.addDocument.AddHTML"/></a>
</div>
<div id="addDocDiv" class="addDocDiv"
	style="background-color: #f2f5e3; display: none;"><html:form
	action="/dms/addEditDocument" method="POST"
	enctype="multipart/form-data" styleClass="forms"
	onsubmit="return submitUpload(this)">
	<%-- Lists Errors --%>
	<% for (Enumeration errorkeys = docerrors.keys(); errorkeys.hasMoreElements();) {%>
	<font class="warning">Error: <bean:message
		key="<%=(String) docerrors.get(errorkeys.nextElement())%>" /></font>
	<br />
	<% } %>
	<input type="hidden" name="function"
		value="<%=formdata.getFunction()%>" size="20">
	<input type="hidden" name="functionId"
		value="<%=formdata.getFunctionId()%>" size="20">
	<input type="hidden" name="functionid" value="<%=moduleid%>" size="20">
	<input type="hidden" name="parentAjaxId" value="<%=parentAjaxId%>">
	<input type="hidden" name="curUser" value="<%=curUser%>">
	<select name="docType" onchange="checkSel(this)"
		<% if (docerrors.containsKey("typemissing")) {%> class="warning" <%}%>>
		<option value=""><bean:message key="dms.addDocument.formSelect" /></option>
		<%
                                   for (int i=0; i<doctypes.size(); i++) {
                                      String doctype = (String) doctypes.get(i); %>
		<option value="<%= doctype%>"
			<%=(formdata.getDocType().equals(doctype))?" selected":""%>><%= doctype%></option>
		<%}%>
	</select>
	<% if (module.equals("provider")) {%>
                                Public: <input type="checkbox"
		name="docPublic" <%=formdata.getDocPublic() + " "%> value="checked">
	<% } %>
	<input type="text" name="docDesc" size="30"
		value="<%=formdata.getDocDesc()%>" onfocus="checkDefaultValue(this)"
		<% if (docerrors.containsKey("descmissing")) {%> class="warning" <%}%>>
	<input type="hidden" name="docCreator"
		value="<%=formdata.getDocCreator()%>" size="20">
	<span class="fieldlabel" title="Observation Date">Obs Date
	(yyyy/mm/dd): </span>
	<input type="text" name="observationDate" id="observationDate"
		value="<%=formdata.getObservationDate()%>"
		onclick="checkDefaultDate(this, '<%=UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd")%>')"
		size="10" style="text-align: center;">
	<a id="obsdate"><img title="Calendar" src="../images/cal.gif"
		alt="Calendar" border="0" /></a>
	<input type="file" name="docFile" size="20"
		<% if (docerrors.containsKey("uploaderror")) {%> class="warning" <%}%>>
	<br />
	<input type="hidden" name="mode" value="add">
	<input type="submit" name="Submit" value="Add">
	<input type="button" name="Button"
		value="<bean:message key="global.btnCancel"/>"
		onclick="javascript: window.location='documentReport.jsp?function=<%=module%>&functionid=<%=moduleid%>'">
</html:form></div>
<div id="addLinkDiv" class="addDocDiv"
	style="background-color: #f2f5e3; display: none;"><html:form
	action="/dms/addLink" method="POST" styleClass="forms"
	onsubmit="return submitUploadLink(this)">
	<%-- Lists Errors --%>
	<% for (Enumeration errorkeys = linkhtmlerrors.keys(); errorkeys.hasMoreElements();) {%>
	<font class="warning">Error: <bean:message
		key="<%=(String) linkhtmlerrors.get(errorkeys.nextElement())%>" /></font>
	<br />
	<% } %>
	<input type="hidden" name="function"
		value="<%=formdata.getFunction()%>" size="20">
	<input type="hidden" name="functionId"
		value="<%=formdata.getFunctionId()%>" size="20">
	<input type="hidden" name="functionid" value="<%=moduleid%>" size="20">
	<input type="hidden" name="observationDate"
		value="<%=formdata.getObservationDate()%>">
	<select name="docType" onchange="checkSel(this)"
		<% if (linkhtmlerrors.containsKey("typemissing")) {%> class="warning"
		<%}%>>
		<option value=""><bean:message
			key="dms.addDocument.formSelect" /></option>
		<%
                                   for (int i=0; i<doctypes.size(); i++) {
                                      String doctype = (String) doctypes.get(i); %>
		<option value="<%= doctype%>"
			<%=(formdata.getDocType().equals(doctype))?" selected":""%>><%= doctype%></option>
		<%}%>
	</select>
	<% if (module.equals("provider")) {%>
                                Public: <input type="checkbox"
		name="docPublic" <%=formdata.getDocPublic() + " "%> value="checked">
	<% } %>
	<input type="text" name="docDesc" size="30"
		value="<%=formdata.getDocDesc()%>" onfocus="checkDefaultValue(this)"
		<% if (linkhtmlerrors.containsKey("descmissing")) {%> class="warning"
		<%}%>>
	<input type="text" name="html" size="30"
		value="<%=formdata.getHtml()%>" onfocus="checkDefaultValue(this)">
	<input type="hidden" name="docCreator"
		value="<%=formdata.getDocCreator()%>" size="20">
	<br />
	<input type="hidden" name="mode" value="addLink">
	<input type="SUBMIT" name="Submit" value="Add">
	<input type="button" name="Button"
		value="<bean:message key="global.btnCancel"/>"
		onclick="javascript: window.location='documentReport.jsp?function=<%=module%>&functionid=<%=moduleid%>'">
</html:form> <script type="text/javascript">
                            Calendar.setup( { inputField : "observationDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "obsdate", singleClick : true, step : 1 } );
                           </script></div>
</div>
