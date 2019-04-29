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

<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.oscarehr.common.model.DemographicArchive"%>
<%@page import="org.oscarehr.common.dao.DemographicArchiveDao"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.casemgmt.dao.CaseManagementNoteDAO"%>
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="oscar.oscarDB.*" %>
<%@page import="java.sql.*" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<html>
<head>
<title>Upgrade Roster Data</title>
</head>
<body>

<h2>Upgrade Roster Data</h2>

<!-- 
	Roster statuses in previous version
	------------------------------------
	""
	RO
	NR
	TE
	FS
	Custom
 -->

<%
	String action = request.getParameter("action");
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	DemographicArchiveDao demographicArchiveDao = SpringUtils.getBean(DemographicArchiveDao.class);
%>				


<h3>
	Rostering code was updated as per the OntarioMD 5.0 EMR Specification. As such, previously entered data needs to be adjusted to the updated database.
	Specifically, a new field has been added to "Enrolled To Physician". Previously, only the MRP field was available.
	<br/>
	This utility will set the Enrolled To Physician to that of MRP for patients where Roster Status was set to ROSTERED.
</h3>

<%if(action == null || !"run".equals(action)) { %>
<form action="upgradeRosterData.jsp">
	Run Utility:<br/>
	<!-- 
	Physician:
	
		<select name="physician">
		<option value="ALL">ALL</option>
		<%for(Provider p:providerDao.getActiveProviders()) { %>
			<option value="<%=p.getProviderNo()%>"><%=p.getFormattedName() %></option>
		<% } %>
	</select>
	-->
	<input type="hidden" name="action" value="run"/>
	<input type="submit" value="Run Report"/>
</form>
<%} else {
	//String provider = request.getParameter("physician");
	for(Integer demographicNo : demographicDao.getDemographicIds()) {
		Demographic d = demographicDao.getDemographicById(demographicNo);
		
		for(DemographicArchive da:demographicArchiveDao.findByDemographicNoChronologically(demographicNo)) { //ASC
			if("RO".equals(da.getRosterStatus()) || "TE".equals(da.getRosterStatus())) {
				if(!StringUtils.isEmpty(da.getProviderNo()) && StringUtils.isEmpty(da.getRosterEnrolledTo())) {
					da.setRosterEnrolledTo(da.getProviderNo());
					demographicArchiveDao.merge(da);
				} else {
					//that;s weird
				}
			}
		
			if("TE".equals(da.getRosterStatus()) && StringUtils.isEmpty(da.getRosterTerminationReason())) {
				da.setRosterTerminationReason("33");
				demographicDao.save(d);
			}
		}
		
		if("RO".equals(d.getRosterStatus()) || "TE".equals(d.getRosterStatus())) {
			if(!StringUtils.isEmpty(d.getProviderNo())  && StringUtils.isEmpty(d.getRosterEnrolledTo())) {
				d.setRosterEnrolledTo(d.getProviderNo());
				demographicDao.save(d);
			} else {
				//that;s weird
			}
		}
		
		if("TE".equals(d.getRosterStatus()) && StringUtils.isEmpty(d.getRosterTerminationReason())) {
			d.setRosterTerminationReason("33");
			demographicDao.save(d);
		}
		
	}
	
%>
	
	<H2>Upgrade complete</H2>
 <%
	}
 %>
</body>
</html>
