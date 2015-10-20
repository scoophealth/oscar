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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page
	import="org.oscarehr.common.dao.DemographicDao,org.oscarehr.caisi_integrator.ws.DemographicWs,org.oscarehr.util.SpringUtils,org.oscarehr.common.model.Demographic"%>
<%@page
	import="org.oscarehr.caisi_integrator.ws.DemographicTransfer,org.oscarehr.caisi_integrator.ws.CachedProvider,org.oscarehr.caisi_integrator.ws.*"%>
<%@page
	import="org.oscarehr.PMmodule.caisi_integrator.*,java.util.*,oscar.util.*"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Integer localDemographicId = Integer.parseInt(request.getParameter("demographicId"));
	DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
	List<DemographicTransfer> directLinks=demographicWs.getDirectlyLinkedDemographicsByDemographicId(localDemographicId);
	DemographicTransfer demographicTransfer = null;	
		
	if (directLinks.size()>0){
		demographicTransfer=directLinks.get(0);
	}		
				
	DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
	Demographic demographic=demographicDao.getDemographicById(localDemographicId);
	
	
	FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
	providerPk.setIntegratorFacilityId(demographicTransfer.getIntegratorFacilityId());
	providerPk.setCaisiItemId(demographicTransfer.getLastUpdateUser());
	CachedProvider p = CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), providerPk);
	String remoteProvider = "Unknown"; // i18n
	if(p != null){
		remoteProvider = p.getFirstName() +" "+p.getLastName();
	}
	
	List<Role> roles = CaisiIntegratorManager.getProviderWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getProviderRoles(providerPk);
	StringBuilder remoteRoles = new StringBuilder();
	boolean first = true;
	for(Role role: roles){
	   if(!first){
			 remoteRoles.append(", ");
	   }
	   remoteRoles.append(role.toString());	
	   first = false;
	}
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el"
	prefix="html-el"%>

<%@ page
	import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig"%>
<%@ page
	import="oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet"%>
<%@ page import="org.oscarehr.common.model.Flowsheet"%>
<%@ page import="org.oscarehr.common.dao.FlowsheetDao"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>

<html:html locale="true">
<head>
<script type="text/javascript"
	src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Diff Remote Demographic</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>

<style type="text/css">

</style>
</head>

<body vlink="#0000FF" class="BodyStyle">

	<table class="MainTable">
		<tr class="MainTableTopRow">
			<%-- td class="MainTableTopRowLeftColumn">integrator</td>--%>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar" style="width: 100%;">
					<tr>
						<td>Remote:
						<% if(demographicTransfer!= null){ %>
				
							<%=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), demographicTransfer.getIntegratorFacilityId()).getName()     %>
							-
							<%=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), demographicTransfer.getIntegratorFacilityId()).getDescription()    %>
							<br>
							By: <%=remoteProvider %> -- Role : <%=remoteRoles.toString() %>
						<%} %>
						</td>
					</tr>
				</table></td>
		</tr>
		<tr>
			
			<td class="MainTableRightColumn" valign="top" colspan="2">
				<% if(demographicTransfer!= null){ %>

				<table border="1">

					<%boolean highlight = false;%>

					<tr>
						<td>&nbsp;</td>
						<th>Local (Last Update: <%=DateUtils.formatDate(demographic.getLastUpdateDate(),request.getLocale())%>)</th>
						<th>Remote (Last Update: <%=DateUtils.formatDate(demographicTransfer.getLastUpdateDate(),request.getLocale())%>)</th>
					</tr>

					<%highlight = (demographicTransfer.getBirthDate()!=null && !(DateUtils.getNumberOfDaysBetweenTwoDates(demographicTransfer.getBirthDate(),demographic.getBirthDay()) == 0)); %>
					<tr>
						<td>BirthDate:</td>
						<td><%=DateUtils.formatDate(demographic.getBirthDay(),request.getLocale())%></td>
						<td <%=isHighlighted(highlight)%>><%=DateUtils.formatDate(demographicTransfer.getBirthDate(),request.getLocale())%></td>
					</tr>
					<%highlight = (demographicTransfer.getCity()!=null && !demographicTransfer.getCity().equalsIgnoreCase(demographic.getCity())); %>
					<tr>
						<td>City:</td>
						<td><%=StringUtils.noNull(demographic.getCity())%></td>
						<td <%=isHighlighted(highlight)%>><%=StringUtils.noNull(demographicTransfer.getCity())%>
						</td>
					</tr>
					<%highlight = (demographicTransfer.getFirstName()!=null && !demographicTransfer.getFirstName().equals(demographic.getFirstName())); %>
					<tr>
						<td>FirstName:</td>
						<td><%=demographic.getFirstName()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getFirstName()%></td>
					</tr>
					<%highlight = (demographicTransfer.getGender()!=null && !demographicTransfer.getGender().toString().equals(demographic.getSex())) ;%>
					<tr>
						<td>Gender:</td>
						<td><%=demographic.getSex()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getGender()%></td>
					</tr>
					<%highlight = (demographicTransfer.getHin()!=null && !demographicTransfer.getHin().equals(demographic.getHin())) ;%>
					<tr>
						<td>Hin:</td>
						<td><%=StringUtils.noNull(demographic.getHin())%></td>
						<td <%=isHighlighted(highlight)%>><%=StringUtils.noNull(demographicTransfer.getHin())%></td>
					</tr>
					<%highlight = (demographicTransfer.getHinType()!=null && !demographicTransfer.getHinType().equals(demographic.getHcType())); %>
					<tr>
						<td>HinType:</td>
						<td><%=StringUtils.noNull(demographic.getHcType())%></td>
						<td <%=isHighlighted(highlight)%>><%=StringUtils.noNull(demographicTransfer.getHinType())%></td>
					</tr>
					<%highlight = (demographicTransfer.getHinVersion()!=null && !demographicTransfer.getHinVersion().equals(demographic.getVer())); %>
					<tr>
						<td>HinVersion:</td>
						<td><%=StringUtils.noNull(demographic.getVer())%> </tdZ>
						<td <%=isHighlighted(highlight)%>><%=StringUtils.noNull(demographicTransfer.getHinVersion())%></td>
					</tr>
					
					<%highlight = (ConformanceTestHelper.isRemoteDateDifferent(DateUtils.toGregorianCalendar(demographic.getEffDate()),demographicTransfer.getHinValidStart())); %>
					<tr>
						<td>HinValidStart:</td>
						<td><%=DateUtils.formatDate(demographic.getEffDate(),request.getLocale())%></td>
						<td <%=isHighlighted(highlight)%>><%=DateUtils.formatDate(demographicTransfer.getHinValidStart(),request.getLocale())%></td>
					</tr>
					<%highlight = (ConformanceTestHelper.isRemoteDateDifferent(DateUtils.toGregorianCalendar(demographic.getHcRenewDate()),demographicTransfer.getHinValidEnd())); %>
					<tr>
						<td>HinValidEnd:</td>
						<td><%=DateUtils.formatDate(demographic.getHcRenewDate(),request.getLocale())%></td>
						<td <%=isHighlighted(highlight)%>><%=DateUtils.formatDate(demographicTransfer.getHinValidEnd(),request.getLocale())%></td>
					</tr>
					<%highlight = (demographicTransfer.getLastName()!=null && !demographicTransfer.getLastName().equals(demographic.getLastName() ) ); %>
					<tr>
						<td>LastName:</td>
						<td><%=demographic.getLastName()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getLastName()%></td>
					</tr>
					<%highlight = (demographicTransfer.getProvince()!=null && !demographicTransfer.getProvince().equalsIgnoreCase(demographic.getProvince())); %>
					<tr>
						<td>Province:</td>
						<td><%=demographic.getProvince()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getProvince()%>
						</td>
					</tr>
					<%highlight = (demographicTransfer.getSin()!=null && !demographicTransfer.getSin().equals(demographic.getSin()));%>
					<tr>
						<td>Sin:</td>
						<td><%=StringUtils.noNull(demographic.getSin())%></td>
						<td <%=isHighlighted(highlight)%>><%=StringUtils.noNull(demographicTransfer.getSin())%></td>
					</tr>
					<%highlight = (demographicTransfer.getStreetAddress()!=null && !demographicTransfer.getStreetAddress().equals(demographic.getAddress())) ;%>
					<tr>
						<td>StreetAddress:</td>
						<td><%=demographic.getAddress()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getStreetAddress()%>
						</td>
					</tr>
					<%highlight = (demographicTransfer.getPhone1()!=null && !demographicTransfer.getPhone1().equals(demographic.getPhone())); %>
					<tr>
						<td>Phone1:</td>
						<td><%=demographic.getPhone()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getPhone1()%>
						</td>
					</tr>
					<%highlight = (demographicTransfer.getPhone2()!=null&& !demographicTransfer.getPhone2().equals(demographic.getPhone2())) ;%>
					<tr>
						<td>Phone2:</td>
						<td><%=demographic.getPhone2()%></td>
						<td <%=isHighlighted(highlight)%>><%=demographicTransfer.getPhone2()%>
						</td>
					</tr>
				</table> <%}else{ %> No Remote Demographics <%} %>
			</td>
		</tr>
		<tr>
			

			<td class="MainTableBottomRowRightColumn" colspan="2">&nbsp;</td>
		</tr>
	</table>
</html:html>
<%!String isHighlighted(boolean highlighted) {
		if (highlighted) {
			return " style=\"background-color: yellow;\"";
		}
		return "";
	}
	
%>
