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
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_newCasemgmt.templates" rights="w" reverse="true">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_newCasemgmt.templates");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*,oscar.util.*"	errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.EncounterTemplate" %>
<%@ page import="org.oscarehr.common.dao.EncounterTemplateDao" %>
<%
	EncounterTemplateDao encounterTemplateDao = SpringUtils.getBean(EncounterTemplateDao.class);
%>
<%
  //save or delete the settings
  int rowsAffected = 0;
  if(request.getParameter("dboperation")!=null && (request.getParameter("dboperation").compareTo(" Save ")==0 ||
      request.getParameter("dboperation").equals("Delete") ) ) {

    EncounterTemplate et = encounterTemplateDao.find(request.getParameter("name"));
    if(et != null) {
    	encounterTemplateDao.remove(et.getId());
    }

    if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals(" Save ") ) {
    	et = new EncounterTemplate();
    	et.setEncounterTemplateName( request.getParameter("name"));
    	et.setEncounterTemplateValue(request.getParameter("value"));
    	et.setCreatorProviderNo(request.getParameter("creator"));
    	et.setCreatedDate(new java.util.Date());
    	encounterTemplateDao.persist(et);
    }
  }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="org.oscarehr.common.dao.EncounterTemplateDao"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.EncounterTemplate"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%><html:html locale="true">
<head>

<title><bean:message key="admin.providertemplate.title" /></title>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

</head>
<body onLoad="setfocus()">

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
		<!--Body content-->
		
		<h3><bean:message key="admin.providertemplate.msgTitle" /></h3>
		
<div class="well">		
	<form name="edittemplate" method="post" action="providertemplate.jsp" class="form-inline">
			<!--<bean:message key="admin.providertemplate.formEdit" />:-->
			Select Template<br>
			<select name="name">
			<%
				List<EncounterTemplate> allTemplates=encounterTemplateDao.findAll();

				for (EncounterTemplate encounterTemplate : allTemplates)
				{
					String templateName=StringEscapeUtils.escapeHtml(encounterTemplate.getEncounterTemplateName());
					%>
						<option value="<%=templateName%>"><%=templateName%></option>
					<%
				}
			%>
			</select>
			<input type="hidden" value="Edit" name="dboperation">
			<input type="button" value="<bean:message key="admin.providertemplate.btnEdit"/>" name="dboperation" class="btn" onclick="document.forms['edittemplate'].dboperation.value='Edit'; document.forms['edittemplate'].submit();">
	</form>	
	
</div>	
	
<%
  boolean bEdit=request.getParameter("dboperation")!=null&&request.getParameter("dboperation").equals("Edit")?true:false;
  String tName = null;
  String tValue = null;
  if(bEdit) {
	  List<EncounterTemplate> templates = encounterTemplateDao.findByName(request.getParameter("name"));
    for(EncounterTemplate template:templates) {
      tName = template.getEncounterTemplateName();
      tValue =template.getEncounterTemplateValue();
	}
  }
%>

			<div class="well">	
				<form name="template" method="post" action="providertemplate.jsp">
				<input type="hidden" name="dboperation" value="">
			
					<bean:message key="admin.providertemplate.formTemplateName" />:<br>
					<input type="text" name="name" value="<%=bEdit?tName:""%>" class="span10" maxlength="20">
				
					<br><br>
				
					<bean:message key="admin.providertemplate.formTemplateText" />:<br>
					<textarea name="value" rows="20" class="span10"><%=bEdit?tValue:""%></textarea>
			
					<br>
					<input type="button" value="<bean:message key="admin.providertemplate.btnDelete"/>" class="btn btn-danger" onClick="document.forms['template'].dboperation.value='Delete'; document.forms['template'].submit();">
			
					<INPUT TYPE="hidden" NAME="creator"	VALUE="<%=curUser_no%>"> 
					<input type="button" value="<bean:message key="admin.providertemplate.btnSave"/>"	class="btn btn-primary" onClick="document.forms['template'].dboperation.value=' Save '; document.forms['template'].submit();">
					
			
					<input type="button" name="Button" id="exit-btn" value="<bean:message key="admin.providertemplate.btnExit"/>"	 class="btn" onClick="window.close();">
			
				</form>
			</div>	
			
		</div><!-- span12 -->
	</div><!-- row fluid -->
</div><!-- container -->




<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script>
<!--
function setfocus() {
  this.focus();
  document.template.name.focus();
}
//-->

var isInIFrame = (window.location != window.parent.location);
if(isInIFrame==true){
    $('#exit-btn').hide();
}
</script>
</body>
</html:html>
