<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicWs"%>
<%@page import="org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" >

<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedProvider"%>
<%@page import="org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention.Attributes"%>
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
			Integer remoteFacilityId=Integer.valueOf(request.getParameter("remoteFacilityId"));
			Integer remotePreventionId=Integer.valueOf(request.getParameter("remotePreventionId"));
			
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();

			FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
			pk.setIntegratorFacilityId(remoteFacilityId);
			pk.setCaisiItemId(remotePreventionId);
			CachedDemographicPrevention remotePrevention = demographicWs.getCachedDemographicPreventionsByPreventionId(pk);
			
			CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(remoteFacilityId);
			FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
			providerPk.setIntegratorFacilityId(remotePrevention.getFacilityPreventionPk().getIntegratorFacilityId());
			providerPk.setCaisiItemId(remotePrevention.getCaisiProviderId());
			CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(providerPk);
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
				CachedDemographicPrevention.Attributes attributes=remotePrevention.getAttributes();
				List<CachedDemographicPrevention.Attributes.Entry> entryList=attributes.getEntry();
				for (CachedDemographicPrevention.Attributes.Entry entry : entryList)
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