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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.consult" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.consult");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.dao.PropertyDao, org.oscarehr.common.model.Property" %>
<%@ page import="org.oscarehr.managers.ConsultationManager" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	PropertyDao dao = (PropertyDao)SpringUtils.getBean(PropertyDao.class);
	ConsultationManager manager = new ConsultationManager();
		
	boolean consultRequestEnabled = false;
	boolean consultResponseEnabled = false;
	
	List<Property> results = dao.findByName(manager.CON_REQUEST_ENABLED);
	if (results.size()>0 && manager.ENABLED_YES.equals(results.get(0).getValue())) consultRequestEnabled = true;
	results = dao.findByName(manager.CON_RESPONSE_ENABLED);
	if (results.size()>0 && manager.ENABLED_YES.equals(results.get(0).getValue())) consultResponseEnabled = true;

	if (!consultRequestEnabled && !consultResponseEnabled) consultRequestEnabled = true;
%>
<html:html locale="true">


<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.oscarConsultationRequest.config.EnableRequestResponse.title" />
</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Consultation</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td class="Header"><bean:message
					key="oscarEncounter.oscarConsultationRequest.config.EnableRequestResponse.title" />
				</td>
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
		<%oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar(request);
                out.print(titlebar.estBar(request));
                %>
		</td>
		<td class="MainTableRightColumn">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<tr><td>&nbsp;</td></tr>
			<%
                      String updated = (String) request.getAttribute("ENABLE_REQUEST_RESPONSE_UPDATED");
                      if (updated != null){  %>
			<tr>
				<td><font color="red"><bean:message
					key="oscarEncounter.oscarConsultationRequest.config.EnableRequestResponse.msgUpdated" /></font></td>
			</tr>
			<%}%>
			<tr>
				<td>
					<table>
						<html:form action="/oscarEncounter/EnableConRequestResponse">
							<tr>
								<td>
									<input type="checkbox" name="consultRequestEnabled" <%=consultRequestEnabled?"checked":"" %>/> <bean:message key="oscarEncounter.oscarConsultationRequest.config.EnableRequestResponse.enableRequest" />
									<br/>
									<input type="checkbox" name="consultResponseEnabled" <%=consultResponseEnabled?"checked":"" %>/> <bean:message key="oscarEncounter.oscarConsultationRequest.config.EnableRequestResponse.enableResponse" />
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td><input type="submit"
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.config.EnableRequestResponse.btnUpdate"/>" />
								</td>
							</tr>
						</html:form>
					</table>
				</td>
			</tr>

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
