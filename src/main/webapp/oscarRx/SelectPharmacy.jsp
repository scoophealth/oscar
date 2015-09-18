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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarRx.data.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="SelectPharmacy.title" /></title>
<html:base />

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>

<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");

%>

<bean:define id="patient"
	type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<%@ include file="SideLinksNoEditFavorites.jsp"%><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><a href="SearchDrug.jsp"> <bean:message
					key="SearchDrug.title" /></a> > <bean:message key="SelectPharmacy.title" /></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><b><bean:message
					key="SearchDrug.nameText" /></b> <jsp:getProperty name="patient"
					property="surname" />, <jsp:getProperty name="patient"
					property="firstName" /></div>
				<br />
				&nbsp; <bean:message key="SelectPharmacy.instructions" /></td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead"><a
					href="ManagePharmacy.jsp?type=Add"><bean:message
					key="SelectPharmacy.addLink" /></a></div>
				</td>
			</tr>
			<tr>
				<td>
				<% RxPharmacyData pharmacy = new RxPharmacyData();
                         List< org.oscarehr.common.model.PharmacyInfo> pharList = pharmacy.getAllPharmacies();
                       %>

                <div style=" width:860px; height:460px; overflow:auto;">
				<table>
					<tr>
						<td><bean:message key="SelectPharmacy.table.pharmacyName" /></td>
						<td><bean:message key="SelectPharmacy.table.address" /></td>
						<td><bean:message key="SelectPharmacy.table.city" /></td>
						<td><bean:message key="SelectPharmacy.table.phone" /></td>
						<td><bean:message key="SelectPharmacy.table.fax" /></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<% for (int i = 0 ; i < pharList.size(); i++){
                               org.oscarehr.common.model.PharmacyInfo ph = pharList.get(i);
                            %>
					<tr>
						<td><a
							href="LinkPharmacy.do?ID=<%=ph.getId()%>&DemoId=<jsp:getProperty name="patient" property="demographicNo"/>"><%=ph.getName()%></a></td>
						<td><%=ph.getAddress()%></td>
						<td><%=ph.getCity()%></td>
						<td><%=ph.getPhone1()%></td>
						<td><%=ph.getFax()%></td>
						<td><a href="ManagePharmacy.jsp?type=Edit&ID=<%=ph.getId()%>"><bean:message
							key="SelectPharmacy.editLink" /></a></td>
						<td><a href="ManagePharmacy.jsp?type=Delete&ID=<%=ph.getId()%>"><bean:message
							key="SelectPharmacy.deleteLink" /></a></td>
					</tr>
					<% } %>
				</table>
                </div>
				</td>
			</tr>

			<tr>
				<td>
				<%
                        String sBack="SearchDrug.jsp";
                      %> <input type=button class="ControlPushButton"
					onclick="javascript:window.location.href='<%=sBack%>';"
					value="Back to Search Drug" /></td>
			</tr>
			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>



</body>

</html:html>
