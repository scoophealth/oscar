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
<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*, oscar.util.*,java.lang.String,org.apache.commons.lang.StringEscapeUtils"%>
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
   boolean patientIndependent = (Boolean) curform.get("patientIndependent");
   
   String formHtmlRaw = (String) curform.get("formHtml");
   String formHtml = "";
   if (request.getAttribute("formHtml") != null) {
       formHtml = (String) request.getAttribute("formHtml");
   }
	formHtml =org.apache.commons.lang.StringEscapeUtils.escapeHtml(formHtmlRaw);
	if(formHtml==null){formHtml="";}	
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.edithtml.msgEditEform" /></title>




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

<%@ include file="efmTopNav.jspf"%>

<%if (request.getParameter("fid") != null){%>
<h3><bean:message key="eform.edithtml.msgEditEform" /></h3>
<%}else{%>
<h3>Create New eForm</h3>
<%}%>

<html:form action="/eform/editForm" styleId="editform" method="POST" enctype="multipart/form-data">

<div class="well" style="position: relative;">
		
	

	<% if ((request.getAttribute("success") != null) && (errors.size() == 0)) { %>
		<font class="warning" style="font-size: 12px;"><bean:message key="eform.edithtml.msgChangesSaved" /></font>
	<% } %> 

		<input type="hidden" name="fid" id="fid" value="<%= curform.get("fid")%>">
       
		<% if ((request.getAttribute("success") == null) || (errors.size() != 0)) {%>
<!--		 error? -->
		<% } %>
		
			<!--LAST SAVED-->
			<div style="position:absolute;top:2px;right:4px;">			
			<em><bean:message key="eform.edithtml.msgLastModified" />: 	<%= curform.get("formDate")%>&nbsp;<%= curform.get("formTime") %></em>
			</div>

			<!--FORM NAME-->
			<div style="display:inline-block">
			<bean:message key="eform.uploadhtml.formName" />:<br />
			<input type="text" name="formName" value="<%= curform.get("formName") %>"
				<% if (errors.containsKey("formNameMissing") || (errors.containsKey("formNameExists"))) { %>
				class="warning" 
				<% } %> size="30" /> 

                                <%String formNameMissing = (String) errors.get("formNameMissing"); 
                                  if (errors.containsKey("formNameMissing")) {  
                                %>
			<font class="warning"><bean:message key="<%=formNameMissing%>" /></font> <%} else if (errors.containsKey("formNameExists")) { %>
			<font class="warning"><bean:message key="<%=formNameMissing%>" /></font> <%} %><br />
			</div>
			
           
			<!--FORM ADDITIONAL INFO-->
			<div style="display:inline-block">
			<bean:message key="eform.uploadhtml.formSubject" />:<br />
                        <input type="text" name="formSubject" value="<%= curform.get("formSubject") %>" size="30" /><br />
			</div>

			<!--ROLE TYPE-->
			<div style="display:inline-block">			
			<bean:message key="eform.uploadhtml.btnRoleType"/><br />
			<select name="roleType">
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
			</select><br />
			</div>

			<!--PATIENT INDEPENDANT-->
			<div style="display:inline-block">
			<bean:message key="eform.uploadhtml.showLatestFormOnly" />	<input type="checkbox" name="showLatestFormOnly" value="true" <%= (Boolean)curform.get("showLatestFormOnly")?"checked":"" %> />
				<br/>
			<bean:message key="eform.uploadhtml.patientIndependent" /> <input type="checkbox" name="patientIndependent" value="true" <%= patientIndependent?"checked":"" %> /><br />
			</div>

<!--
<div class="span4">		
			<bean:message key="eform.uploadhtml.formFileName" /> <small>optional</small>: <br />
			<input type="text" name="formFileName" value="<%= curform.get("formFileName")%>" size="50" /> <br />
</div>
		

<div class="span4">		
			<bean:message key="eform.edithtml.frmUploadFile" /> <small>optional</small>:<br />
                        <input type="file" name="uploadFile" size="30"
				<% if (errors.containsKey("uploadError")) { %> class="warning"
				<% } %>> <input type="hidden" name="uploadMarker"
				id="uploadMarker" value="false"> <input type="button"
				name="uploadbtn" id="uploadbtn" class="btn"
				value="<bean:message key="eform.edithtml.frmUpload"/>"
				onclick="disablenupload()"> <% if (errors.containsKey("uploadError")) { 
                                    String uploadError = (String) errors.get("uploadError"); %>
                                <font class="warning"><bean:message key="<%=uploadError%>" /></font>
			<% } %><br />
		
</div>		
-->


			<br />			
			<bean:message key="eform.edithtml.msgEditHtml" />:<br />
			<textarea wrap="off" name="formHtml" style="" class="span12" rows="40"><%= formHtml%></textarea><br />
	
			
			
	
			
<p>
				<a href="<%=request.getContextPath()%>/eform/efmformmanager.jsp" class="btn contentLink">
				 <i class="icon-circle-arrow-left"></i> Back to eForm Library<!--<bean:message key="eform.edithtml.msgBackToForms"/>-->
				</a>

				<input type="button" class="btn"
				value="<bean:message key="eform.edithtml.msgPreviewLast"/>"
				<% if (curform.get("fid") == null) {%> disabled
				<%}%> name="previewlast" onclick="openLastSaved()"> 


				<a href="<%=request.getContextPath()%>/eform/efmformmanageredit.jsp?fid=<%= curform.get("fid") %>" class="btn contentLink"> <bean:message key="eform.edithtml.cancelChanges"/></a>

				<input type="submit" class="btn btn-primary"
				value="<bean:message key="eform.edithtml.msgSave"/>"
				name="savebtn" onclick="disablensubmit()"> 
	
</p>	

</div>
</html:form>


<%@ include file="efmFooter.jspf"%>
</body>
</html:html>
