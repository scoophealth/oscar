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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
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
<%@page import="oscar.util.CBIUtil"%>
<%@page import="java.util.Date"%>

<%
CBIUtil cbiUtil = new CBIUtil();
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   		 
List<Date> submissionDateList = cbiUtil.getSubmissionDateList();
StringBuffer treeDataStr = cbiUtil.generateTree(submissionDateList);

FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");
ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");

List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.getCurrentFacility().getId());
%>

<%@include file="/layouts/html_top.jspf"%>

<h1>CBI Reports</h1>

<script type="text/javascript">
	function validate(form)
	{
		var fields = form.elements;
	    return true; //don't have to select a functional centre for now. 
		if (fields.functionalCentreId.value==null||fields.functionalCentreId.value=="")
		{
			alert('Please select a functional centre.');
			return(false);
		}
	
	}
</script>

<form method="post" action="cbi_report_results.jsp" onsubmit="return(validate(this))">

<table class="borderedTableAndCells">
		<tr>
			<td>Functional Centre to report on</td>
			<td>
				<select name="functionalCentreId">
					<option value=""></option>
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
				<input type="text" name="startDate" id="startDate" />
				<script type="text/javascript">
					jQuery('#startDate').datepicker({ dateFormat: 'yy-mm-dd' });
					
					var d=new Date();
					var month=d.getMonth();
					if (month>0)
					{
						d.setMonth(month-1);
					}
					else
					{
						d.setMonth(11);
						d.setYear(d.getYear()-1);
					}
					
					jQuery('#startDate').datepicker("setDate", d);
					jQuery('#startDate').attr("readonly", true);
				</script>
			</td>
		</tr>
		<tr>
			<td>Date Range End (inclusive)</td>
			<td>
				<input type="text" name="endDate" id="endDate" />
				<script type="text/javascript">
					jQuery('#endDate').datepicker({ dateFormat: 'yy-mm-dd' });					
					jQuery('#endDate').datepicker("setDate", new Date());
					jQuery('#endDate').attr("readonly", true);
				</script>
			</td>
		</tr>
		<tr>
			<td>
				Filter By
			</td>
			<td>
				<select id="filterCriteriaSelection" onchange="showFilterCriteria()">
					<option value="">None</option>
					<option value="PROVIDER">Provider</option>
					<option value="PROGRAM">Program</option>
				</select>				
				<script type="text/javascript">
					function showFilterCriteria()
					{
						var selection=jQuery('#filterCriteriaSelection').val();
						
						if (selection == "PROVIDER")
						{
							jQuery('#providerText').show();
							jQuery('#providerOptions').show();
							jQuery('#programText').hide();
							jQuery('#programOptions').hide();
						}
						else if (selection == "PROGRAM")
						{
							jQuery('#providerText').hide();
							jQuery('#providerOptions').hide();
							jQuery('#programText').show();
							jQuery('#programOptions').show();							
						}
						else
						{
							jQuery('#providerText').hide();
							jQuery('#providerOptions').hide();
							jQuery('#programText').hide();
							jQuery('#programOptions').hide();
						}
					}
					
					$(document).ready(function(){
						showFilterCriteria();
					});
					
				</script>
			</td>		
		</tr>
		<tr>
			<td>
				<div id="providerText">
					Providers to include
					<div style="font-size:smaller">
						(multi select is allowed)
					</div>
				</div>
				<div id="programText">
					Programs to include
					<div style="font-size:smaller">
						(multi select is allowed)
					</div>
				</div>
			</td>
			<td>
				<div id="providerOptions">
					<select multiple="multiple" name="providerIds">
						<option value=""></option>
					<%
						// null for both active and inactive because the report might be for a provider who's just left in the current reporting period.
						List<Provider> providers=providerManager.getProviders(loggedInInfo, null);
					
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
				<div id="programOptions">
					<select multiple="multiple" name="programIds">
						<option value=""></option>
					<%
						List<Program> programs=programManager.getPrograms(loggedInInfo.getCurrentFacility().getId());
					
						for (Program program : programs)
						{
							%>
								<option value="<%=program.getId()%>"><%=StringEscapeUtils.escapeHtml(program.getName()+" ("+program.getType()+")")%></option>
							<%
						}
					%>
					</select>
				</div>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Generate Report" /></td>
		</tr>
</table>
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>





