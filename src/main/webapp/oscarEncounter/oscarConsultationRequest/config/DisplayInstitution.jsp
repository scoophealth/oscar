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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.consult" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.consult");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.ResourceBundle"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.List" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Institution" %>
<%@page import="org.oscarehr.common.dao.InstitutionDao" %>
<%@page import="org.oscarehr.common.model.Department" %>
<%@page import="org.oscarehr.common.dao.DepartmentDao" %>
<%@page import="org.oscarehr.common.model.InstitutionDepartment" %>
<%@page import="org.oscarehr.common.model.InstitutionDepartmentPK" %>
<%@page import="org.oscarehr.common.dao.InstitutitionDepartmentDao" %>
<%
	InstitutionDao institutionDao = SpringUtils.getBean(InstitutionDao.class);
    DepartmentDao departmentDao = SpringUtils.getBean(DepartmentDao.class);
    InstitutitionDepartmentDao institutionDepartmentDao = SpringUtils.getBean(InstitutitionDepartmentDao.class);
%>
<html:html locale="true">

<%
String id = (String) request.getAttribute("id");
String name = (String) request.getAttribute("name");
%>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Display Institution</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF">
<html:errors />
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Consultation</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td class="Header">Adjust Institutions 
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
		<%oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar();
                    out.print(titlebar.estBar(request));
                 %>
		</td>
		<td class="MainTableRightColumn">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
				<td>Please check off all the departments offered by <%=name%>.</td>
			</tr>
			<tr>
				<td><html:form
					action="/oscarEncounter/UpdateInstitutionDepartment">
					<input type="hidden" name="id" value="<%=id %>">
					<input type="submit"
						value="Update Institution Department">
					<div class="ChooseRecipientsBox1">
					<table>
						<tr>
							<th>&nbsp;</th>
							<th>Department</th>
							
						</tr>
						<tr>
							<td><!--<div class="ChooseRecipientsBox1">--> <%
								for(Department i:departmentDao.findAll()) {
									InstitutionDepartment assoc = institutionDepartmentDao.find(new InstitutionDepartmentPK(Integer.parseInt(id),i.getId()));
                              %>
							
						<tr>
							<td>
							
							<input type=checkbox name="specialists" value=<%=i.getId()%> <%=assoc!=null?"checked=\"checked\"":"" %>>
							
							</td>
							<td>
							<% out.print(i.getName()); %>
							</td>
							
						</tr>
						<% }%>
						<!--</div>-->
						</td>
						</tr>
					</table>
					</div>
				</html:form></td>
			</tr>
			<!----End new rows here-->

			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
