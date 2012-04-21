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
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	
	List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.currentFacility.getId());
	List<Program> programs=programDao.getProgramsByFacilityId(loggedInInfo.currentFacility.getId());
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<table style="width:100%">
	<tr>
		<td><h2>MIS Reports</h2></td>
		<td style="text-align:right"><input type="button" value="back" onclick="history.go(-1)" /></td>
	</tr>
</table>
				
<script type="text/javascript">
	function validate(form)
	{
		var fields = form.elements;
		var reportBy=getSelectedRadioValue(fields.reportBy);

		if (reportBy=="functionalCentre")
		{
			if (fields.functionalCentreId.value==null||fields.functionalCentreId.value=="")
			{
				alert('Please select a functional centre.');
				return(false);
			}
		}
		else if (reportBy=="programs")
		{
			if (fields.programIds.value==null||fields.programIds.value=="")
			{
				alert('Please select one or more programs.');
				return(false);
			}
		}
		else
		{
			alert("error, missed an option : "+reportBy);
			return(false);
		}
	}
</script>

<form method="post" action="mis_report_results.jsp" onsubmit="return(validate(this))">
	<table class="borderedTableAndCells">
		<tr>
			<td style="vertical-align:top">
				<script type="text/javascript">
					function showFunctionalCentreSelectionBlock()
					{
						document.getElementById("functionalCentreSelectionBlock").style.display="block";
						document.getElementById("programsSelectionBlock").style.display="none";
					}

					function showProgramsSelectionBlock()
					{
						document.getElementById("functionalCentreSelectionBlock").style.display="none";
						document.getElementById("programsSelectionBlock").style.display="block";
					}
				</script>
			
				Report By
				<table style="border:none;border-collapse:collapse">
					<tr>
						<td style="border:none"><input type="radio" name="reportBy" value="functionalCentre" checked="checked" onclick="showFunctionalCentreSelectionBlock()" /></td>
						<td style="border:none">Functional Centre</td>
					</tr>
					<tr>
						<td style="border:none"><input type="radio" name="reportBy" value="programs" onclick="showProgramsSelectionBlock()" /></td>
						<td style="border:none">Programs</td>
					</tr>
				</table>
			</td>
			<td>
				<div id="functionalCentreSelectionBlock">
					<select name="functionalCentreId">
						<%
							for (FunctionalCentre functionalCentre : functionalCentres)
							{
								%>
									<option value="<%=functionalCentre.getAccountId()%>"><%=StringEscapeUtils.escapeHtml(functionalCentre.getAccountId()+", "+functionalCentre.getDescription())%></option>
								<%
							}
						%>
					</select>
				</div>
				<div id="programsSelectionBlock" style="display:none">
					<select name="programIds" multiple='multiple' >
						<%
							for (Program program : programs)
							{
								if (program.isBed() || program.isService())
								{
									%>
										<option value="<%=program.getId()%>"><%=StringEscapeUtils.escapeHtml(program.getName() + " ("+program.getType()+')')%></option>
									<%
								}
							}
						%>
					</select>
					<br />
					<input type="checkbox" name="reportProgramsIndividually" /> Report Programs Separately
				</div>
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
			<td></td>
			<td><input type="submit" value="View Report" /></td>
		</tr>
	</table>	
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
