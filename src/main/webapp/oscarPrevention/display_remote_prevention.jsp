<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.RemotePreventionHelper"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicWs"%>
<%@page import="org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" >

<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedProvider"%>
<%@page import="org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="java.util.List"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>	
		<title>Remote Prevention</title>
		<style>
			.tableLabel
			{
				border: solid black 1px;
				font-weight: bold;
			}
			
			.tableData
			{
				border: solid black 1px;
			}
		</style>
	</head>
	<body>
		<%
			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		    Integer demographicId = Integer.valueOf(request.getParameter("demographic_no"));
			Integer remoteFacilityId=Integer.valueOf(request.getParameter("remoteFacilityId"));
			Integer remotePreventionId=Integer.valueOf(request.getParameter("remotePreventionId"));
			
			FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
			pk.setIntegratorFacilityId(remoteFacilityId);
			pk.setCaisiItemId(remotePreventionId);
			CachedDemographicPrevention remotePrevention  = null;
			
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
					remotePrevention = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getCachedDemographicPreventionsByPreventionId(pk);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}
				
			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				List<CachedDemographicPrevention> remotePreventions = IntegratorFallBackManager.getRemotePreventions(loggedInInfo, demographicId);
				for(CachedDemographicPrevention prev:remotePreventions){
					if ( prev.getFacilityPreventionPk().getIntegratorFacilityId() == remoteFacilityId && prev.getFacilityPreventionPk().getCaisiItemId() == remotePreventionId){
						remotePrevention = prev;
					}
				}
				
			} 
		
			
			
			CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),remoteFacilityId);
			FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
			providerPk.setIntegratorFacilityId(remotePrevention.getFacilityPreventionPk().getIntegratorFacilityId());
			providerPk.setCaisiItemId(remotePrevention.getCaisiProviderId());
			CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), providerPk);
		%>
		<table style="border-collapse:collapse">
			<tr>
				<td class="tableLabel">Prevention Type</td>
				<td class="tableData"><%=remotePrevention.getPreventionType()%></td>
			</tr>
			<tr>
				<td class="tableLabel">At Facility</td>
				<td class="tableData"><%=cachedFacility!=null?cachedFacility.getName():"N/A"%></td>
			</tr>
			<tr>
				<td class="tableLabel">By Provider</td>
				<td class="tableData"><%=cachedProvider!=null?cachedProvider.getLastName()+", "+cachedProvider.getFirstName():"N/A"%></td>
			</tr>
			<tr>
				<td class="tableLabel">Prevention Date</td>
				<td class="tableData"><%=DateFormatUtils.ISO_DATE_FORMAT.format(remotePrevention.getPreventionDate())%></td>
			</tr>
			<tr>
				<td class="tableLabel">Next Prevention Date</td>
				<td class="tableData"><%=remotePrevention.getNextDate()!=null?DateFormatUtils.ISO_DATE_FORMAT.format(remotePrevention.getNextDate()):""%></td>
			</tr>
			<%
				HashMap<String,String> attributes=RemotePreventionHelper.getRemotePreventionAttributesAsHashMap(remotePrevention);
				for (Map.Entry<String,String> entry : attributes.entrySet())
				{
					%>
					<tr>
						<td class="tableLabel"><%=entry.getKey()%></td>
						<td class="tableData"><%=entry.getValue()%></td>
					</tr>
					<%
				}
			%>
		</table>
		<input type="button" value="close" onclick="javascript:window.close();" />
	</body>
</html>
