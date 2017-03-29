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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.web.MisReportUIBean"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.web.MisReportUIBean.DataRow"%>
<%@page import="java.util.GregorianCalendar"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   		 
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));

	GregorianCalendar startDate=new GregorianCalendar(startYear, startMonth-1, 1); //Jan is 0.
	GregorianCalendar endDate=new GregorianCalendar(endYear, endMonth, 1);
	GregorianCalendar actualEndDate=(GregorianCalendar)endDate.clone();
	//actualEndDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive of the 30/31 days of the month

	MisReportUIBean misReportUIBean=null;	
	
	String reportBy=request.getParameter("reportBy");
	if ("functionalCentre".equals(reportBy))
	{
		String functionalCentreId=request.getParameter("functionalCentreId");
		misReportUIBean=new MisReportUIBean(loggedInInfo, functionalCentreId, startDate, actualEndDate);
	}
	else if ("programs".equals(reportBy))
	{
		String[] programIds=request.getParameterValues("programIds");
		boolean reportProgramsIndividually=WebUtils.isChecked(request, "reportProgramsIndividually");

		if (!reportProgramsIndividually)
		{
			misReportUIBean=new MisReportUIBean(programIds, startDate, actualEndDate);
		}
		else
		{
			misReportUIBean=MisReportUIBean.getSplitProgramReports(programIds, startDate, actualEndDate);
		}
	}
	else if("provider".equals(reportBy)) {
		String providerId = request.getParameter("providerId");
		misReportUIBean=new MisReportUIBean(providerId, "Service", startDate, actualEndDate);
	}
	else
	{
		throw(new IllegalStateException("missed a case : reportBy="+reportBy));
	}	
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<table style="width:100%">
	<tr>
		<td><h2>MIS Report</h2></td>
		<td style="text-align:right"><input type="button" value="back" onclick="history.go(-1)" /></td>
	</tr>
</table>

<span style="font-weight:bold">ReportBy : </span><%=misReportUIBean.getReportByDescription()%>
<br /><br />
<span style="font-weight:bold">Dates : </span><%=MisReportUIBean.getDateRangeForDisplay(startDate, endDate)%>

<br /><br />

<table class="genericTable borderedTableAndCells" style="font-size:12px">
	<tr class="genericTableHeader">
		<%
			for (String header : misReportUIBean.getHeaderSPIRow())
			{
				%>
					<td><%=header%></td>
				<%
			}
		%>
	</tr>

	<%
		int rowCounter_SPI=0;
		
		for (MisReportUIBean.DataSPIRow dataSPIRow : misReportUIBean.getDataSPIRows())
		{
			rowCounter_SPI++;
			String backgroundColour;
			if (rowCounter_SPI%2==0) backgroundColour="#eeeeee";
			else backgroundColour="#dddddd";
				
			%>
				<tr class="genericTableRow" style="background-color:<%=backgroundColour%>">
					<td style="font-weight:bold"><%=dataSPIRow.dataSPIString%></td>
					<td style="font-weight:bold"><%=dataSPIRow.dataSPIProgramName%></td>
					<td style="font-weight:bold"><%=dataSPIRow.dataSPIEncounterType%></td>
					<%
						for (String tempResult : dataSPIRow.dataSPIReportResult)
						{
							%>
								<td><%=tempResult==null?"-":tempResult%></td>
							<%
						}
					%>
				</tr>
			<%
		}
	%>
</table>
<br /><br />
<span style="font-weight:bold">SRI Report</span>
<table class="genericTable borderedTableAndCells" style="font-size:12px">
	<tr class="genericTableHeader">
		<%
			for (String headerSRI : misReportUIBean.getHeaderSRIRow())
			{
				%>
					<td><%=headerSRI%></td>
				<%
			}
		%>
	</tr>

	<%
		int rowCounter_SRI=0;

		for (MisReportUIBean.DataSRIRow dataSRIRow : misReportUIBean.getDataSRIRows())
		{
			rowCounter_SRI++;
			String backgroundColour;
			if (rowCounter_SRI%2==0) backgroundColour="#eeeeee";
			else backgroundColour="#dddddd";
				
			%>
				<tr class="genericTableRow" style="background-color:<%=backgroundColour%>">
					<td style="font-weight:bold"><%=dataSRIRow.dataSRIProgram%></td>
				
					<%
						for (String tempResult : dataSRIRow.dataSRIReportResult)
						{
							%>
								<td><%=tempResult==null?"-":tempResult%></td>
							<%
						}
					%>
				</tr>
			<%
		}
	%>
</table>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
