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

<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.managers.DemographicManager"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic"
	rights="r" reverse="<%=true%>">
	<%
		authed = false;
	%>
	<%
		response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");
	%>
</security:oscarSec>
<%
	if (!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>

<%@page import="org.oscarehr.managers.DemographicManager"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.web.AdmissionForDisplay"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedAdmission"%>
<%@page
	import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page
	import="org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager"%>

<%@page import="java.util.Collections"%>
<%@page import="org.oscarehr.util.MiscUtils"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	AdmissionManager admissionManager = SpringUtils.getBean(AdmissionManager.class);

	String demographic_no = request.getParameter("demographic_no");
	String strLimit1 = "0";
	String strLimit2 = "50";
	if (request.getParameter("limit1") != null)
		strLimit1 = request.getParameter("limit1");
	if (request.getParameter("limit2") != null)
		strLimit2 = request.getParameter("limit2");

	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";
	String orderby = "";
	if (request.getParameter("orderby") != null)
		orderby = request.getParameter("orderby");

	Demographic d = demographicManager.getDemographic(loggedInInfo, Integer.parseInt(demographic_no));

	String demolastname = d.getLastName();
	String demofirstname = d.getFirstName();

	ArrayList<AdmissionForDisplay> allResults = new ArrayList<AdmissionForDisplay>();

	List<Admission> addLocalAdmissions = admissionManager.getAdmissionsByFacility(d.getDemographicNo(),
			loggedInInfo.getCurrentFacility().getId());
	for (Admission admission : addLocalAdmissions)
		allResults.add(new AdmissionForDisplay(admission));

	addRemoteAdmissions(loggedInInfo, allResults, d.getDemographicNo());
	
	  boolean bodd=false;
%>


<html:html locale="true">

<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script>
     jQuery.noConflict();
   </script>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<script>
	var ctx = '<%=request.getContextPath()%>
	';
</script>

<oscar:customInterface section="programhistory" />

<title><bean:message key="demographic.programhistory.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<script type="text/javascript">
	function popupPageNew(vheight, vwidth, varpage) {
		var page = "" + varpage;
		windowprops = "height="
				+ vheight
				+ ",width="
				+ vwidth
				+ ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
		var popup = window.open(page, "demographicprofile", windowprops);
		if (popup != null) {
			if (popup.opener == null) {
				popup.opener = self;
			}
		}
	}
</script>

<link rel="stylesheet" type="text/css" media="all"
	href="../share/css/extractedFromPages.css" />
</head>

<body class="BodyStyle" vlink="#0000FF">

	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
					key="demographic.programhistory.msgHistory" /></td>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar">
					<tr>
						<td><bean:message key="demographic.programhistory.msgResults" />:
							<%=demolastname%>,<%=demofirstname%>(<%=request.getParameter("demographic_no")%>)</td>
						<td>&nbsp;</td>
						<td style="text-align: right"><oscar:help
								keywords="program history" key="app.top1" /> | <a
							href="javascript:popupStart(300,400,'About.jsp')"> <bean:message
									key="global.about" /></a> | <a
							href="javascript:popupStart(300,400,'License.jsp')"> <bean:message
									key="global.license" /></a></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn" valign="top"><br /></td>
			<td class="MainTableRightColumn">
			
			<%
				if(allResults.isEmpty()) {
			%>
				<h3>This client is not currently admitted to any programs.</h3>
			<% } else { %>
			
			<table width="95%" border="0" bgcolor="#ffffff">
				<tr bgcolor="<%=deepColor%>">
					<th></th>
					<th>Program Name</th>
					<th>Days in Program</th>
					<th>Admission Date</th>
					<th>Discharge Date</th>
					<th>Discharge Notes</th>
				</tr>
				<%
					for(AdmissionForDisplay admissionForDisplay:allResults) {
						bodd=bodd?false:true; //for the color of rows
				%>
					<tr bgcolor="<%=bodd?weakColor:"white"%>">
						<td>
						<%
							if (!admissionForDisplay.isFromIntegrator()) {
						%>
							local
						
						<%
							} else {
						%>
						
							<%=admissionForDisplay.getFacilityName() %>
						<% } %>
						</td>
						
						
						
						
						<td>
							<%=admissionForDisplay.getProgramName() %>
						</td>
						
						
						<td>
							<%=admissionForDisplay.getDaysInProgram() %>
						</td>
						<td>
							<%=admissionForDisplay.getAdmissionDate()%>
						</td>
						
						<td>
						<%
							if (admissionForDisplay.getDischargeDate() != null) {
						%>
						<%=admissionForDisplay.getDischargeDate()%>
						
						<%
							}
						%>
						</td>
						
						
						<td>
							<%=(admissionForDisplay.getDischargeNotes()!=null)?admissionForDisplay.getDischargeNotes():"" %>
						</td>
					</tr>
				<%
					}
				%>
			</table>
			
			<% } %>
			
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
</body>
</html:html>

<%!void addRemoteAdmissions(LoggedInInfo loggedInInfo, ArrayList<AdmissionForDisplay> admissionsForDisplay,
			Integer demographicId) {
		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {

			try {
				List<CachedAdmission> cachedAdmissions = null;
				try {
					if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
						cachedAdmissions = CaisiIntegratorManager
								.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility())
								.getLinkedCachedAdmissionsByDemographicId(demographicId);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Unexpected error.", e);
					CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(), e);
				}

				if (CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
					cachedAdmissions = IntegratorFallBackManager.getRemoteAdmissions(loggedInInfo, demographicId);
				}

				for (CachedAdmission cachedAdmission : cachedAdmissions)
					admissionsForDisplay.add(new AdmissionForDisplay(loggedInInfo, cachedAdmission));

				Collections.sort(admissionsForDisplay, AdmissionForDisplay.ADMISSION_DATE_COMPARATOR);
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error retrieveing integrated admissions.", e);
			}
		}
	}%>