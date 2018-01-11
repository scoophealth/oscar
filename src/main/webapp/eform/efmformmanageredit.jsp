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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.eform" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*, oscar.util.*, org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
HashMap<String, Object> curform = new HashMap<String, Object>();
HashMap<String, String> errors = new HashMap<String, String>();

if (request.getAttribute("submitted") != null) {
    curform = (HashMap<String, Object>) request.getAttribute("submitted");
    errors = (HashMap<String, String>) request.getAttribute("errors");
} else if (request.getParameter("fid") != null ) {
    String curfid = request.getParameter("fid");
    curform = EFormUtil.loadEForm(curfid);
}

   //remove "null" values
   if (curform.get("fid") == null) curform.put("fid", "");
   if (curform.get("formName") == null) curform.put("formName", "");
   if (curform.get("formSubject") == null) curform.put("formSubject", "");
   if (curform.get("formFileName") == null) curform.put("formFileName", "");
   if (curform.get("roleType") == null) curform.put("roleType", "");
   if (curform.get("programNo") == null) curform.put("programNo", "");
   
   
   if (request.getParameter("formHtmlG") != null){
       //load html from hidden form from eformGenerator.jsp,the html is then injected into edit-eform
      curform.put("formHtml", StringEscapeUtils.unescapeHtml(request.getParameter("formHtmlG")));
   }
   if (curform.get("formDate") == null) curform.put("formDate", "--");
   if (curform.get("formTime") == null) curform.put("formTime", "--");
   
   if (curform.get("showLatestFormOnly") ==null) curform.put("showLatestFormOnly", false);
   if (curform.get("patientIndependent") ==null) curform.put("patientIndependent", false);
   if (curform.get("restrictByProgram") ==null) curform.put("restrictByProgram", false);
   if (curform.get("disabledUpdate") ==null) curform.put("disabledUpdate", false);
   
   String formHtml = StringEscapeUtils.escapeHtml((String) curform.get("formHtml"));
	if(formHtml==null){formHtml="";}	
%>
<!DOCTYPE html>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.edithtml.msgEditEform" /></title>

<style>
.input-error{   
    border-color: rgba(229, 103, 23, 0.8) !important; 
    box-shadow: 0 1px 1px rgba(229, 103, 23, 0.075) inset, 0 0 8px rgba(229, 103, 23, 0.6) !important; 
    outline: 0 none !important;  
}

#popupDisplay{display:inline-block;}
#panelDisplay{display:none;}
</style>

<script type="text/javascript" language="JavaScript">
function openLastSaved() {
    window.open('<%=request.getContextPath()%>/eform/efmshowform_data.jsp?fid=<%= curform.get("fid") %>', 'PreviewForm', 'toolbar=no, location=no, status=yes, menubar=no, scrollbars=yes, resizable=yes, width=700, height=600, left=300, top=100');   
}

//using this to check if page is being viewing in admin panel or in popup
var elementExists = document.getElementById("dynamic-content");

if (elementExists){
document.getElementById("popupDisplay").style.display = 'none';
document.getElementById("panelDisplay").style.display = 'inline';
}else{
document.write('<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">');
}

<% if ((request.getAttribute("success") != null) && (errors.size() == 0)) { %>
if (elementExists==null){
window.opener.location.href = '<%=request.getContextPath()%>/administration/?show=Forms';
}
<%}%>
</script>

</head>

<body id="eformBody">

<%@ include file="efmTopNav.jspf"%>

<%if (request.getParameter("fid") != null){%>
<h3><bean:message key="eform.edithtml.msgEditEform" /></h3>
<%}else{%>
<h3>Create New eForm</h3>
<%}%>

<form action="<%=request.getContextPath()%>/eform/editForm.do" method="POST" enctype="multipart/form-data" id="editform" name="eFormEdit">

<div class="well" style="position: relative;">
		
<% if ((request.getAttribute("success") != null) && (errors.size() == 0)) { %>
<div class="alert alert-success">
<button type="button" class="close" data-dismiss="alert">&times;</button>
<bean:message key="eform.edithtml.msgChangesSaved" />.
</div>
<% } %> 
	
	<%String formNameMissing = errors.get("formNameMissing");
    if (errors.containsKey("formNameMissing")) { %>
	<div class="alert alert-error">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <bean:message key="<%=formNameMissing%>" />
    </div>
	<%} else if (errors.containsKey("formNameExists")) { %>
	<div class="alert alert-error">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <bean:message key="<%=formNameMissing%>" />
    </div>
	<%}%>

		<input type="hidden" name="fid" id="fid" value="<%= curform.get("fid")%>">
       
		<% if ((request.getAttribute("success") == null) || (errors.size() != 0)) {%>
			<!--error? -->
		<% } %>
		
			<!--LAST SAVED-->
			<div style="position:absolute;top:2px;right:4px;">			
			<em><bean:message key="eform.edithtml.msgLastModified" />: 	<%= curform.get("formDate")%>&nbsp;<%= curform.get("formTime") %></em>
			</div>

			<!--FORM NAME-->
			<div style="display:inline-block">
			 
			<bean:message key="eform.uploadhtml.formName" />:
			<br />
			<input type="text" name="formName" value="<%= curform.get("formName") %>" class="<% if (errors.containsKey("formNameMissing") || (errors.containsKey("formNameExists"))) { %> input-error <% } %>" size="30" /> 
			<br />
			
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

			<!--ProgramNo-->
			<div style="display:inline-block">			
			<bean:message key="eform.uploadhtml.btnProgram"/><br />
			<select name="programNo">
			<option value="">- select one -</option>
			<%  List<org.oscarehr.PMmodule.model.Program> pList = EFormUtil.listPrograms(); 
			selected = "";
			for (int i=0; i<pList.size(); i++) {  
				selected = "";
				if(pList.get(i).getId().toString().equals(curform.get("programNo"))) {
					selected = "selected";
				}
			%>  			
			<option value="<%=pList.get(i).getId() %>" <%= selected%> %><%=pList.get(i).getName() %></option>
	
			<%} %>
			</select><br />
			</div>
			
			<!--PATIENT INDEPENDANT-->
			<div style="display:inline-block">
			<bean:message key="eform.uploadhtml.showLatestFormOnly" />	<input type="checkbox" name="showLatestFormOnly" value="true" <%= (Boolean)curform.get("showLatestFormOnly")?"checked":"" %> />
				<br/>
			<bean:message key="eform.uploadhtml.patientIndependent" /> <input type="checkbox" name="patientIndependent" value="true" <%= (Boolean)curform.get("patientIndependent")?"checked":"" %> /><br />
			<bean:message key="eform.uploadhtml.restrictByProgram" /> <input type="checkbox" name="restrictByProgram" value="true" <%= (Boolean)curform.get("restrictByProgram")?"checked":"" %> /><br />
			<bean:message key="eform.uploadhtml.disableUpdate" /> <input type="checkbox" name="disableUpdate" value="true" <%= curform.get("disableUpdate") != null?"checked":"" %> /><br />
						
			</div>

			<br />			
			<bean:message key="eform.edithtml.msgEditHtml" />:<br />
			<textarea wrap="off" name="formHtml" style="" class="span12" rows="40"><%= formHtml%></textarea><br />

<p>
	<div id="panelDisplay">
	<a href="<%=request.getContextPath()%>/eform/efmformmanager.jsp" class="btn contentLink">
	 <i class="icon-circle-arrow-left"></i> Back to eForm Library<!--<bean:message key="eform.edithtml.msgBackToForms"/>-->
	</a>
	<input type="button" class="btn" value="<bean:message key="eform.edithtml.msgPreviewLast"/>" <% if (curform.get("fid") == null) {%> disabled	<%}%> name="previewlast" onclick="openLastSaved()"> 
	<a href="<%=request.getContextPath()%>/eform/efmformmanageredit.jsp?fid=<%= curform.get("fid") %>" class="btn contentLink"> <bean:message key="eform.edithtml.cancelChanges"/></a>
	</div>

	<a href="#" class="btn" id="popupDisplay" onClick="window.close()"> 
	 <i class="icon-circle-arrow-left"></i> Back to eForm Library<!--<bean:message key="eform.edithtml.msgBackToForms"/>-->
	</a>

	<input type="submit" class="btn btn-primary" value="<bean:message key="eform.edithtml.msgSave"/>" data-loading-text="Saving..." name="savebtn" id="savebtn"  > 

</p>	
</div>
</form>


<%@ include file="efmFooter.jspf"%>

<script>
registerFormSubmit('editform', 'dynamic-content');

$(document).ready(function () {

$("html, body").animate({ scrollTop: 0 }, "slow");
return false;

});
</script>


</body>
</html:html>

