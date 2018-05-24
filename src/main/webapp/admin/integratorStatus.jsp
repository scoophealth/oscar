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
<%@page import="org.oscarehr.managers.IntegratorFileLogManager"%>
<%@page import="org.oscarehr.common.model.IntegratorFileLog"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.common.dao.IntegratorControlDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.FacilityDao"%>
<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>

<%@page import="oscar.OscarProperties"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html"%>
<%@page import="org.oscarehr.common.model.UserProperty" %>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>

<%
if(!authed) {
	return;
}
%>

<%
	OscarProperties p = OscarProperties.getInstance();
	FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
	Facility facility = LoggedInInfo.getLoggedInInfoFromSession(request).getCurrentFacility();
	IntegratorControlDao integratorControlDao = SpringUtils.getBean(IntegratorControlDao.class);
	boolean removeDemographicIdentity = integratorControlDao.readRemoveDemographicIdentity(facility.getId());
	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
	UserProperty integratorPatientConsent = userPropertyDao.getProp( UserProperty.INTEGRATOR_PATIENT_CONSENT );
	
	List<CachedFacility> facilities=null;
	if(facility.isIntegratorEnabled()) {
		facilities = CaisiIntegratorManager.getRemoteFacilities(LoggedInInfo.getLoggedInInfoFromSession(request), facility,false);
	}
	boolean allSynced = true;
	
	int secondsTillConsideredStale = Integer.parseInt(p.getProperty("seconds_till_considered_stale"));
	try{
		allSynced  = CaisiIntegratorManager.haveAllRemoteFacilitiesSyncedIn(LoggedInInfo.getLoggedInInfoFromSession(request), facility, secondsTillConsideredStale); 
	}catch(Exception remoteFacilityException){
		MiscUtils.getLogger().error("Error checking Remote Facilities Sync status",remoteFacilityException);
		CaisiIntegratorManager.checkForConnectionError(request.getSession(), remoteFacilityException);
	}
	if(secondsTillConsideredStale == -1){  
		allSynced = true; 
	}
	boolean offline = CaisiIntegratorManager.isIntegratorOffline(request.getSession());

	java.util.Calendar timeConsideredStale = java.util.Calendar.getInstance();
	timeConsideredStale.add(java.util.Calendar.SECOND, -secondsTillConsideredStale);
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	CachedFacility myCachedFacility = null;
	if(facility.isIntegratorEnabled()) {
		myCachedFacility = CaisiIntegratorManager.getFacilityWs(LoggedInInfo.getLoggedInInfoFromSession(request), facility).getMyFacility();
	}
	
	IntegratorFileLogManager integratorFileLogManager = SpringUtils.getBean(IntegratorFileLogManager.class);
	
	List<IntegratorFileLog> logs = integratorFileLogManager.getFileLogHistory(LoggedInInfo.getLoggedInInfoFromSession(request));
	
%>
<html:html locale="true">
    <head>
        <title>Integrator Status</title>
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
 	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" /> 
	
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js" ></script>
	
	
        <script>
           
        </script>
    </head>
    <body>
    <div class="container">
<div class="row">
 
 <%if(facility.isIntegratorEnabled()) { %>
 <div class="span12">
 <span><span style="font-size:24px">Integrated Community</span> &nbsp;&nbsp;&nbsp;&nbsp; <span style="background:orange">&nbsp;&nbsp;&nbsp;</span>Stale&nbsp;&nbsp;&nbsp;<span style="background:cyan">&nbsp;&nbsp;&nbsp;</span>Home</span>
<table id="communityTable" name="communityTable" class="table table-bordered table-condensed">
	<thead>
		<tr style="border: solid black 2px;background:lightgray">
			<th>Facility Name</th>
			<th>Description</th>
			<th>Contact</th>
			<th>Last Updated</th>
		</tr>
	</thead>
	<tbody>
	<%
		for (CachedFacility x : facilities)
		{
			java.util.Calendar lastDataUpdate = x.getLastDataUpdate();
			String background = "white";
			if( lastDataUpdate == null || timeConsideredStale.after(lastDataUpdate)){
				background = "orange";
			}
			
			if(x.getIntegratorFacilityId().intValue() == myCachedFacility.getIntegratorFacilityId().intValue()) {
				background="cyan";
			}
			
			
			%>
	<tr style="border: solid black 2px; background: <%=background %>; color: gray" >
		<td style="border: solid black 1px"><%=StringUtils.trimToEmpty(x.getName())%></td>
		<td style="border: solid black 1px"><%=StringUtils.trimToEmpty(x.getDescription())%></td>
		<td style="border: solid black 1px"><%=StringUtils.trimToEmpty(x.getContactName()) + "<br>" + StringUtils.trimToEmpty(x.getContactEmail()) + "<br/>" + StringUtils.trimToEmpty(x.getContactPhone()) %></td>
		<td style="border: solid black 1px"><%=(x.getLastDataUpdate()!=null?sdf.format(x.getLastDataUpdate().getTime()):"")%></td>
	</tr>
	<%
		}
	%>
	</tbody>
</table>
 </div>
</div>
 <%} %>
 
 <div class="span12">
 <span><span style="font-size:24px">File Log History</span> </span>
<table id="fileLogTable" name="fileLogTable" class="table table-bordered table-condensed">
	<thead>
		<tr style="border: solid black 2px;background:lightgray">
			<th>Filename</th>
			<th>MDS5 checksum</th>
			<th>Previous Date Updated</th>
			<th>Date Updated</th>
			<th>Integrator Status</th>
		</tr>
	</thead>
	<tbody>
	<%
		for (IntegratorFileLog log : logs)
		{
			String background = "white";
			
			
			
			%>
	<tr style="border: solid black 2px; background: <%=background %>; color: black" >
		<td style="border: solid black 1px"><%=log.getFilename().substring(log.getFilename().lastIndexOf("/")+1)%></td>
		<td style="border: solid black 1px"><%=log.getChecksum()%></td>
		<td style="border: solid black 1px"><%=sdf.format(log.getLastDateUpdated())%></td>
		<td style="border: solid black 1px"><%=sdf.format(log.getCurrentDate())%></td>
		<td style="border: solid black 1px"><%=StringUtils.trimToEmpty(log.getIntegratorStatus()) %></td>
	</tr>
	<%
		}
	%>	
	</tbody>
</table>
 </div>

<div class="span12">
 <table class="table">
  		<tbody>
  			<tr>
  				<td colspan="2"><b>Configuration Properties</b></td>
  				
  			</tr>
  			<tr>
  				<td>Update Frequency:</td>
  				<td><%=p.getProperty("INTEGRATOR_UPDATE_PERIOD") %> ms</td>
  			</tr>
  			<tr>
  				<td>Force Full Push:</td>
  				<td><%=p.getProperty("INTEGRATOR_FORCE_FULL") %></td>
  			</tr>
  			<tr>
  				<td>Max File Size:</td>
  				<td><%=p.getProperty("INTEGRATOR_MAX_FILE_SIZE") %></td>
  			</tr>
  			<tr>
  				<td>Local Store:</td>
  				<td><%=p.getProperty("FORCED_ROSTER_INTEGRATOR_LOCAL_STORE") %></td>
  			</tr>
  			<tr>
  				<td>Issue Codetype:</td>
  				<td><%=p.getProperty("COMMUNITY_ISSUE_CODETYPE") %></td>
  			</tr>
  			<tr>
  				<td>Seconds until stale:</td>
  				<td><%=p.getProperty("seconds_till_considered_stale") %> (&lt; <%=sdf.format(timeConsideredStale.getTime()) %>)</td>
  			</tr>
  			<tr>
  				<td colspan="2"><b>Patient Consent</b></td>
  				
  			</tr>
  			<tr>
  				<td>Module Enabled:</td>
  				<td><%=p.getProperty("USE_NEW_PATIENT_CONSENT_MODULE")%></td>
  			</tr>
  			<tr>
  				<td>Integrator patient consent:</td>
  				<td><%=(integratorPatientConsent != null) && ("1".equals( integratorPatientConsent.getValue()) )%></td>
  			</tr>
  			<tr>
  				<td colspan="2"><b>Facility Settings</b></td>
  				
  			</tr>
  			<tr>
  				<td>Enabled:</td>
  				<td><%=facility.isIntegratorEnabled() %></td>
  			</tr>
  			<tr>
  				<td>URL:</td>
  				<td><%=facility.getIntegratorUrl() %></td>
  			</tr>
  			<tr>
  				<td>Username:</td>
  				<td><%=facility.getIntegratorUser() %></td>
  			</tr>
  			<tr>
  				<td>Password:</td>
  				<td><%=facility.getIntegratorPassword() %></td>
  			</tr>
  			<tr>
  				<td colspan="2"><b>Other Settings</b></td>
  				
  			</tr>
  			<tr>
  				<td>Remove Demographic Identity:</td>
  				<td><%=removeDemographicIdentity %></td>
  			</tr>
  		</tbody>
	</table>
 </div>
 

</div>
 
 
 
    </div>
    </body>
</html:html>
