<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.common.dao.DataExportDao"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="oscar.util.StringUtils"%>
<%@page import="oscar.oscarReport.data.DemographicSets" %>
<%@page import="org.apache.struts.validator.DynaValidatorForm" %>
<%@page import="java.util.ArrayList, java.util.List" %>
<%@page import="org.oscarehr.common.model.DataExport" %>
<%@include file="/casemgmt/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<%
String demographic_no = request.getParameter("demographic_no");
%>

<%
DemographicSets  ds = new DemographicSets();
List<String> setsList = ds.getDemographicSets();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>CIHI Export</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container-fluid well">
<html:form action="/demographic/eRourkeExport.do" method="post">
<h3>Vender Information</h3>
<table class="table-condensed">
	<tr><td>Organization Name</td><td><html:text styleClass="right" property="orgName"></html:text></td></tr>
	<tr><td>Contact Last Name</td><td><html:text styleClass="right" property="contactLName"></html:text></td></tr>
	<tr><td>Contact First Name</td><td><html:text styleClass="right" property="contactFName"></html:text></td></tr>
	<tr><td>Contact Phone</td><td><html:text  styleClass="right" property="contactPhone"></html:text></td></tr>
	<tr><td>Contact Email</td><td><html:text styleClass="right" property="contactEmail"></html:text></td></tr>
	<tr><td>Contact Username</td><td><html:text styleClass="right" property="contactUserName"></html:text></td></tr>
	<tr><td>Vendor Business Name</td><td><html:text styleClass="right" property="vendorBusinessName"></html:text></td></tr>
	<tr><td>Vendor ID</td><td><html:text styleClass="right" readonly="true" property="vendorId"></html:text></td></tr>
	<tr><td>Vendor Common Name</td><td><html:text styleClass="right" readonly="true" property="vendorCommonName"></html:text></td></tr>
	<tr><td>Vendor Software</td><td><html:text styleClass="right" readonly="true" property="vendorSoftware"></html:text></td></tr>
	<tr><td>Vendor Software Common Name</td><td><html:text styleClass="right" readonly="true" property="vendorSoftwareCommonName"></html:text></td></tr>
	<tr><td>Vendor Software Ver</td><td><html:text styleClass="right" readonly="true" property="vendorSoftwareVer"></html:text></td></tr>
	<tr><td>Vendor Install Date</td><td><html:text styleClass="right" readonly="true" property="installDate"></html:text></td></tr>

<tr><td>
Extract Type
</td><td>
<html:select property="extractType">
				<html:option value="<%=DataExportDao.ROURKE%>"><%=DataExportDao.ROURKE%></html:option>
			</html:select>
</td></tr>

<tr><td>
Patient Set
</td><td>
<html:select property="patientSet">
	<html:option value="-1">--Select Set--</html:option>
<%
String setName;
for( int idx = 0; idx < setsList.size(); ++idx ) {
	setName = setsList.get(idx);
%>
	<html:option value="<%=setName%>"><%=setName%></html:option>
<%
}
%>
</html:select>
</td></tr>

<tr><td colspan="2" align="right">

<input class="btn btn-primary" type="submit" value="Run Report"/>
</td></tr>
</table>

<h3>Previous Reports</h3>
<table class="table table-striped  table-condensed">
<tr>
	<th>Run Date</th>
	<th>File</th>
	<th>User</th>
	<th>Type</th>
</tr>
<%
	List<DataExport> dataExportList = (List<DataExport>)request.getAttribute("dataExportList");
	for( int idx = dataExportList.size()-1; idx >= 0; --idx) {
		DataExport dataExport = dataExportList.get(idx);
		String file = dataExport.getFile();
		%>
		<tr>
			<td><%=DateFormatUtils.format(dataExport.getDaterun().getTime(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()) %></td>
			<td><a href='<c:out value="${ctx}/demographic/eRourkeExport.do"></c:out>?method=getFile&zipFile=<%=file%>'><%=file %></a></td>
			<td><%=dataExport.getUser()%>
			<td><%=dataExport.getType()%></td>
		</tr>
<%
	}
%>
</table>
</html:form>
</div>
</body>
</html>
