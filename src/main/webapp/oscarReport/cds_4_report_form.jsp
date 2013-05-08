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

<%
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");

	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.currentFacility.getId());
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h1>CDS Reports</h1>
				
<script type="text/javascript">
	function validate(form)
	{
		var fields = form.elements;

		if (fields.functionalCentreId.value==null||fields.functionalCentreId.value=="")
		{
			alert('Please select a functional centre.');
			return(false);
		}
	}
</script>

<form method="post" action="cds_4_report_results.jsp" onsubmit="return(validate(this))">
	<table class="borderedTableAndCells">
		<tr>
			<td colspan="2">CDS-MH 4.05</td>
		</tr>
		<tr>
			<td>Functional Centre to report on</td>
			<td>
				<select name="functionalCentreId">
					<%
						for (FunctionalCentre functionalCentre : functionalCentres)
						{
							%>
								<option value="<%=functionalCentre.getAccountId()%>"><%=functionalCentre.getAccountId()+", "+functionalCentre.getDescription()%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Date Range Start</td>
			<td>
				<select name="startYear">
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
				</select>
				-
				<select name="startMonth">
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
			</td>
		</tr>
		<tr>
			<td>Date Range End (inclusive)</td>
			<td>
				<select name="endYear">
				<%
					for (int i=0; i<10; i++)
					{
						%>
							<option value="<%=year-i%>"><%=year-i%></option>
						<%
					}
				%>
				</select>
				-
				<select name="endMonth">
				<%
					for (int i=1; i<13; i++)
					{
						%>
							<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
						<%
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				Providers to include
				<div style="font-size:smaller">
					(leave blank to report on all providers, multi select is allowed)
				</div>
			<td>
				<select multiple="multiple" name="providerIds">
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
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Download Report" /></td>
		</tr>
	</table>	
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
