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
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");

	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.currentFacility.getId());
%>

<div class="page-header">
	<h4>CDS Reports</h4>
</div>

<form class="well form-horizontal" action="cds_4_report_results.jsp"
	id="cdsForm">
	<fieldset>

		<!-- Form Name -->
		<legend>CDS-MH 4.05</legend>

		<div class="control-group">
			<label class="control-label">Functional Centre</label>
			<div class="controls">
				<select id="functionalCentreId" name="functionalCentreId" class="input-large">
					<%
						for (FunctionalCentre functionalCentre : functionalCentres)
									{
					%>
					<option value="<%=functionalCentre.getAccountId()%>"><%=functionalCentre.getAccountId()+", "+functionalCentre.getDescription()%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Date Start</label>
			<div class="controls">
				<select name="startYear" class="input-medium">
					<%
						GregorianCalendar cal=new GregorianCalendar();
								int year=cal.get(GregorianCalendar.YEAR);
								for (int i=0; i<10; i++)
								{
					%>
					<option value="<%=year-i%>"><%=year-i%></option>
					<%
						}
					%>
				</select> <select name="startMonth" class="input-mini">
					<%
						DateFormatSymbols dateFormatSymbols=DateFormatSymbols.getInstance();
								String[] months=dateFormatSymbols.getShortMonths();
								
								for (int i=1; i<13; i++)
								{
					%>
					<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Date End (inclusive)</label>
			<div class="controls">
				<select name="endYear" class="input-medium">
					<%
						for (int i=0; i<10; i++)
								{
					%>
					<option value="<%=year-i%>"><%=year-i%></option>
					<%
						}
					%>
				</select> <select name="endMonth" class="input-mini">
					<%
						for (int i=1; i<13; i++)
								{
					%>
					<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Providers to include
				<small>
					(leave blank to report on all providers, multi select is allowed)
				</small>
			</label>
			<div class="controls">
				<select name="providerIds" class="input-medium" multiple="multiple">
					<%
						// null for both active and inactive because the report might be for a provider who's just left in the current reporting period.
						List<Provider> providers=providerManager.getProviders(null);

						for (Provider provider : providers)
						{
							// skip (system,system) user
							if (provider.getProviderNo().equals(Provider.SYSTEM_PROVIDER_NO)) continue;
							
							%>
								<option value="<%=provider.getProviderNo()%>"><%=StringEscapeUtils.escapeHtml(provider.getFormattedName())%></option>
							<%
						}
					%>
				</select>
			</div>
		</div>


		<div class="control-group">
			<div class="controls">
				<button type="submit" class="btn btn-primary">View Report</button>
			</div>
		</div>

	</fieldset>
</form>

<div id="cds-results"></div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#cdsForm').validate({
			rules : {
				functionalCentreId : {
					required : true
				}
			}
		});
	});

	registerFormSubmit('cdsForm', 'cds-results');
</script>