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
if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
String demographic_no = request.getParameter("demographic_no");
String roleName = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName%>" objectName="_admin" rights="r" reverse="true">
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%
DemographicSets  ds = new DemographicSets();
List<String> setsList = ds.getDemographicSets();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>CIHI Export</title>
<script type="text/javascript">
	function setReportType(select) {

		if(select.options[select.selectedIndex].value == "<%=DataExportDao.CIHI_OMD4%>") {
			document.forms[0].action = "<c:out value="${ctx}"/>/demographic/cihiExportOMD4.do";

		}
		else if(select.options[select.selectedIndex].value == "<%=DataExportDao.CIHI_PHC_VRS%>") {
			document.forms[0].action = "<c:out value="${ctx}"/>/demographic/cihiExportPHC_VRS.do";
		}

	}

</script>
<style type="text/css">

.container {
border: 1px solid black;
margin-top:5px;
}
div.vendor {
font-size: 8px;
width:48%;
margin-top:5px;
}
input.right {
float:right;
}
input.setright {
	position:relative;
	left: 25%;
}
</style>
</head>
<body>
<html:form action="/demographic/cihiExportOMD4.do" method="get">
<h3>Vender Information</h3>
<div class="container" style="width:100%;">
<div class="vendor" style="float:right">
		<html:text styleClass="right" property="vendorBusinessName"></html:text>Vendor Business Name<br clear="right">
		<html:text styleClass="right" readonly="true" property="vendorId"></html:text>Vendor ID<br clear="right">
		<html:text styleClass="right" readonly="true" property="vendorCommonName"></html:text>Vendor Common Name<br clear="right">
		<html:text styleClass="right" readonly="true" property="vendorSoftware"></html:text>Vendor Software<br clear="right">
		<html:text styleClass="right" readonly="true" property="vendorSoftwareCommonName"></html:text>Vendor Software Common Name<br clear="right">
		<html:text styleClass="right" readonly="true" property="vendorSoftwareVer"></html:text>Vendor Software Ver<br clear="right">
		<html:text styleClass="right" readonly="true" property="installDate"></html:text>Vendor Install Date
</div>

<div class="vendor" style="float:left;height:100%;">
	Organization Name<html:text styleClass="right" property="orgName"></html:text><br clear="right">
	Contact Last Name<html:text styleClass="right" property="contactLName"></html:text><br clear="right">
	Contact First Name<html:text styleClass="right" property="contactFName"></html:text><br clear="right">
	Contact Phone<html:text  styleClass="right" property="contactPhone"></html:text><br clear="right">
	Contact Email<html:text styleClass="right" property="contactEmail"></html:text><br clear="right">
	Contact Username<html:text styleClass="right" property="contactUserName"></html:text><br clear="right">
	&nbsp;
</div>
</div>
<div>
			Extract Type<html:select property="extractType" onchange="setReportType(this);">
				<html:option value="<%=DataExportDao.CIHI_OMD4%>"><%=DataExportDao.CIHI_OMD4%></html:option>
				<html:option value="<%=DataExportDao.CIHI_PHC_VRS%>"><%=DataExportDao.CIHI_PHC_VRS%></html:option>
			</html:select><br>



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

<input type="submit" value="Run Report"/>
</div>
<h3>Previous Reports</h3>
<table width="100%">
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
			<td><a href='<c:out value="${ctx}/demographic/cihiExportOMD4.do"></c:out>?method=getFile&zipFile=<%=file%>'><%=file %></a></td>
			<td><%=dataExport.getUser()%>
			<td><%=dataExport.getType()%></td>
		</tr>
<%
	}
%>
</table>
</html:form>
</body>
</html>
