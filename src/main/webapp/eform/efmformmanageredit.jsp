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
<%
  
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>
<%@ page
	import="oscar.eform.data.*, oscar.eform.*, java.util.*, oscar.util.*,java.lang.String,org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
HashMap<String, Object> curform = new HashMap<String, Object>();
HashMap<String, String> errors = new HashMap<String, String>();
if (request.getAttribute("submitted") != null) {
    curform = (HashMap<String, Object>) request.getAttribute("submitted");
    errors = (HashMap<String, String>) request.getAttribute("errors");
} else if (request.getParameter("fid") != null) {
    String curfid = request.getParameter("fid");
    curform = EFormUtil.loadEForm(curfid);
}
   //remove "null" values
   if (curform.get("fid") == null) curform.put("fid", "");
   if (curform.get("formName") == null) curform.put("formName", "");
   if (curform.get("formSubject") == null) curform.put("formSubject", "");
   if (curform.get("formFileName") == null) curform.put("formFileName", "");
   if (curform.get("roleType") == null) curform.put("roleType", "");
   
   if (request.getParameter("formHtml") != null){
       //load html from hidden form from eformGenerator.jsp,the html is then injected into edit-eform
      curform.put("formHtml",org.apache.commons.lang.StringEscapeUtils.unescapeHtml(request.getParameter("formHtml")));
   }
   if (curform.get("formDate") == null) curform.put("formDate", "--");
   if (curform.get("formTime") == null) curform.put("formTime", "--");
   
   if (curform.get("showLatestFormOnly") ==null) curform.put("showLatestFormOnly", false);
   if (curform.get("patientIndependent") ==null) curform.put("patientIndependent", false);
   
   String formHtmlRaw = (String) curform.get("formHtml");
   String formHtml = "";
   if (request.getAttribute("formHtml") != null) {
       formHtml = (String) request.getAttribute("formHtml");
   }
     formHtml =org.apache.commons.lang.StringEscapeUtils.escapeHtml(formHtmlRaw);
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.edithtml.msgEditEform" /></title>
<link rel="stylesheet" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../share/css/eformStyle.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript" language="JavaScript">
function openLastSaved() {
    window.open('efmshowform_data.jsp?fid=<%= curform.get("fid") %>', 'PreviewForm', 'toolbar=no, location=no, status=yes, menubar=no, scrollbars=yes, resizable=yes, width=700, height=600, left=300, top=100');   
}
function disablensubmit() {
    document.forms['eFormEdit'].savebtn.disabled = true;
    document.forms['eFormEdit'].submit();
}

function disablenupload() {
    document.getElementById('uploadbtn').disabled = true;
    document.getElementById('uploadMarker').value = "true";
    document.forms['eFormEdit'].submit();
}
</script>
</head>

<body>
<html:form action="/eform/editForm" styleId="editform" method="POST"
	enctype="multipart/form-data">
	<div style="background: #CCCCFF; width: 100%;">
	<center><font face="Helvetica"><bean:message
		key="eform.edithtml.msgEditEform" /></font></center>
	</div>
	<center>
	<% if ((request.getAttribute("success") != null) && (errors.size() == 0)) { %><font
		class="warning" style="font-size: 12px;"><bean:message
		key="eform.edithtml.msgChangesSaved" /></font>
	<% } %> <input type="hidden" name="fid" id="fid"
		value="<%= curform.get("fid")%>">
        <table width="100%" height="85%" class="maintable"
		<% if ((request.getAttribute("success") == null) || (errors.size() != 0)) {%>
		style="margin-top: 19px;" <% } %>>
		<tr class="highlight">
			<th style="width: 150px; text-align: right;"><bean:message
				key="eform.uploadhtml.formName" />:</th>
			<td><input type="text" name="formName"
				value="<%= curform.get("formName") %>"
				<% if (errors.containsKey("formNameMissing") || (errors.containsKey("formNameExists"))) {
                                    
                                %>
				class="warning" <% } %> size="30" /> 
                                <%String formNameMissing = (String) errors.get("formNameMissing"); 
                                  if (errors.containsKey("formNameMissing")) {  
                                %>
			<font class="warning"><bean:message key="<%=formNameMissing%>" /></font> <%} else if (errors.containsKey("formNameExists")) { %>
			<font class="warning"><bean:message key="<%=formNameMissing%>" /></font> <%} %>
			</td>
			<td rowspan="3">
				<input type="checkbox" name="showLatestFormOnly" value="true" <%= (Boolean)curform.get("showLatestFormOnly")?"checked":"" %> />
				<bean:message key="eform.uploadhtml.showLatestFormOnly" />
				<br/>
				<input type="checkbox" name="patientIndependent" value="true" <%= (Boolean)curform.get("patientIndependent")?"checked":"" %> />
				<bean:message key="eform.uploadhtml.patientIndependent" />
			</td>
		</tr>
		<tr class="highlight">
			<th style="text-align: right;"><bean:message
				key="eform.uploadhtml.formSubject" />:</th>
            <td><input type="text" name="formSubject"
				value="<%= curform.get("formSubject") %>" size="30" /></td>
		</tr>
		<tr class="highlight">
			<th style="text-align: right;"><bean:message key="eform.uploadhtml.btnRoleType"/>:</th>
			<td><select name="roleType">
				<option value="">- select one -</option>
                <%  ArrayList roleList = EFormUtil.listSecRole(); 
                	String selected = "";
					for (int i=0; i<roleList.size(); i++) {  
						selected = "";
						if(roleList.get(i).equals(curform.get("roleType"))) {
							selected = "selected";
						}
  				%>  			
  					<option value="<%=roleList.get(i) %>" <%= selected%> %><%=roleList.get(i) %></option>
                                        	
                <%} %>
                </select>
            </td>
		</tr>
		<tr class="highlight">
			<th style="text-align: right;"><bean:message key="eform.uploadhtml.formFileName" /> <sup>optional</sup>: </th>
			<td colspan="2"><input type="text" name="formFileName"
				value="<%= curform.get("formFileName")%>" size="50" /></td>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="eform.edithtml.msgLastModified" />:</th>
			<td colspan="2"><%= curform.get("formDate")%>&nbsp;<%= curform.get("formTime") %></td>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="eform.edithtml.frmUploadFile" /> <sup>optional</sup>:</th>
            <td colspan="2"><input type="file" name="uploadFile" size="40"
				<% if (errors.containsKey("uploadError")) { %> class="warning"
				<% } %>> <input type="hidden" name="uploadMarker"
				id="uploadMarker" value="false"> <input type="button"
				name="uploadbtn" id="uploadbtn"
				value="<bean:message key="eform.edithtml.frmUpload"/>"
				onclick="disablenupload()"> <% if (errors.containsKey("uploadError")) { 
                                    String uploadError = (String) errors.get("uploadError"); %>
                                <font class="warning"><bean:message key="<%=uploadError%>" /></font>
			<% } %>
			</td>
		</tr>
		<tr height="100%">
			<th valign="top" style="text-align: right;"><bean:message key="eform.edithtml.msgEditHtml" />:</th>
			<td colspan="2"><textarea style="width: 100%; height: 100%;"
			wrap="off" name="formHtml"><%= formHtml%></textarea></td>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="eform.edithtml.frmSubmit" />:</th>
			<td colspan="2"><input type="button"
				value="<bean:message key="eform.edithtml.msgPreviewLast"/>"
				<% if (curform.get("fid") == null) {%> disabled
				<%}%> name="previewlast" onclick="openLastSaved()"> <input
				type="submit" value="<bean:message key="eform.edithtml.msgSave"/>"
				name="savebtn" onclick="disablensubmit()"> <input
				type="button"
				value="<bean:message key="eform.edithtml.cancelChanges"/>"
				onclick="javascript: window.location='efmformmanageredit.jsp?fid=<%= curform.get("fid") %>'">
				<input type="button"
				value="<bean:message key="eform.edithtml.msgBackToForms"/>"
				onclick="javascript: window.location='efmformmanager.jsp'">

			</td>
		</tr>
	</table>
	</center>
</html:form>
</body>
</html:html>
