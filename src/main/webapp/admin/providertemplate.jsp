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
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_newCasemgmt.templates" rights="w" reverse="true">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%
  String curUser_no = (String) session.getAttribute("user");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
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
<%@page import="org.oscarehr.common.dao.EncounterTemplateDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.EncounterTemplate"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.providertemplate.title" /></title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.template.name.focus();
}
//-->
</SCRIPT>
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor=<%=deepcolor%>>
		<th><font face="Helvetica"><bean:message
			key="admin.providertemplate.msgTitle" /></font></th>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<form name="edittemplate" method="post" action="providertemplate.jsp">
	<tr bgcolor=<%=weakcolor%>>
		<td width="95%" align='right'>
			<bean:message key="admin.providertemplate.formEdit" /> :
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
			<input type="button" value="<bean:message key="admin.providertemplate.btnEdit"/>" name="dboperation" onclick="document.forms['edittemplate'].dboperation.value='Edit'; document.forms['edittemplate'].submit();">
		</td>
		<td>&nbsp;</td>
	</tr>
	</form>
</table>

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
<center>
<table width="90%" border="0" cellspacing="2" cellpadding="2">
	<form name="template" method="post" action="providertemplate.jsp">
	<input type="hidden" name="dboperation" value="">
	<tr>
		<td valign="top" width="20%" align="right" title='no symbol "'><bean:message
			key="admin.providertemplate.formTemplateName" />:</td>
		<td><input type="text" name="name" value="<%=bEdit?tName:""%>"
			style="width: 100%" maxlength="20"></td>
	</tr>
	<tr>
		<td valign="top" width="20%" align="right"><bean:message
			key="admin.providertemplate.formTemplateText" />:</td>
		<td><textarea name="value" style="width: 100%" rows="20"><%=bEdit?tValue:""%></textarea>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor=<%=weakcolor%>>
		<td width="23%" align='right'><input type="button"
			value="<bean:message key="admin.providertemplate.btnDelete"/>"
			onClick="document.forms['template'].dboperation.value='Delete'; document.forms['template'].submit();">
		</td>
		<td width="72%" align='right'><INPUT TYPE="hidden" NAME="creator"
			VALUE="<%=curUser_no%>"> <input type="button"
			value="<bean:message key="admin.providertemplate.btnSave"/>"
			onClick="document.forms['template'].dboperation.value=' Save '; document.forms['template'].submit();">
		<input type="button" name="Button"
			value="<bean:message key="admin.providertemplate.btnExit"/>"
			onClick="window.close();"></td>
		<td>&nbsp;</td>
	</tr>
	</form>
</table>
</center>
</body>
</html:html>
