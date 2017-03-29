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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eaaps" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eaaps");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" language="javascript">
<!--
	function updateExport() {
		var checkbox = jQuery("#acknowledged");
		if (!checkbox) {
			return;
		}
		var exportButton = jQuery("#export");
		if (!exportButton) {
			return;
		}

		var isChecked = checkbox.is(":checked");
		var isStudySelected = jQuery("#studyId :selected").val() != "";
		
		var isValid = isChecked && isStudySelected;
		
		exportButton.attr("disabled", isValid ? "" : "disabled");
	}
	
	function disableExport() {
		var exportButton = jQuery("#export");
		if (!exportButton) {
			return;
		}
		exportButton.attr("disabled", "disabled");
	}
	
	jQuery.noConflict();
	jQuery(document).ready(function() {
		updateExport();
	});
	-->
</script>

<title>eAAPS Form</title>

<html:base />
</head>

<body class="mainbody" vlink="#0000FF">
	<table class="MainTable" id="scrollNumber1" name="encounterTable"
		style="margin: 0px;">
		<tr class="topbar">
			<td class="MainTableTopRowLeftColumn" width="60px">eAAPS</td>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar">
					<tr>
						<td>Demographic Export for Electronic Asthma Action Plan</td>
						<td style="text-align: right;">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table cellspacing="0" id="queue_provider_table" style="margin: 0px;">
		<tr>
			<td colspan="2"><a id="dbInfo"></a></td>
		</tr>
		<tr>
			<td>
				<html:form action="/eaaps/export" method="post" onsubmit="disableExport();">
					<html:errors />

					<c:choose>
						<c:when test="${not empty eaapsExportForm.studies}">
							<p style="font-weight: bold;">Please select a demographic
								study to be exported to eAAPS server:</p>
							<html:select styleId="studyId" property="studyId" onchange="updateExport();">
								<html:option value=""> - Please select a study - </html:option>
								<c:forEach var="s" items="${eaapsExportForm.studies}">
									<html:option value="${s.id}">${s.studyName}</html:option>
								</c:forEach>
							</html:select>

							<p>
								<html:checkbox styleId="acknowledged" property="acknowledged"
									onchange="updateExport();" />

								I acknowledge that I understand all requirements of exporting
								the patient data set to a 3rd party server.
							</p>

							<p>Please note that export will be logged.</p>

							<c:choose>
								<c:when test="${eaapsExportForm.acknowledged}">
									<html:submit value="Export" styleId="export"
										onclick="updateExport();" />
								</c:when>
								<c:otherwise>
									<html:submit value="Export" disabled="disabled" styleId="export" />
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<p>
								There are no studies created yet. <br /> Please go to Reports
								&gt; 11. Demographic Report <br /> Create an appropriate query
								and save it.<br /> Then go to demographic study (in Admin &gt;
								Study) to create a new study using the previously created query.
							</p>
						</c:otherwise>
					</c:choose>

				</html:form>
			</td>
		</tr>
	</table>

</body>
</html:html>