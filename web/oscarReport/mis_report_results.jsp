<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.web.MisReportUIBean"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%-- 
/*
* Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
* 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. 
* 
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.  
* 
* This software was written for 
* CAISI, 
* Toronto, Ontario, Canada 
*/
--%>
<%
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));

	MisReportUIBean misReportUIBean=null;
	ArrayList<String> reportIndividualProgramNames=null;
	HashMap<MisReportUIBean.DataRow, ArrayList<MisReportUIBean.DataRow>> reportIndividualProgramDataRows=null;
	
	String reportBy=request.getParameter("reportBy");
	if ("functionalCentre".equals(reportBy))
	{
		String functionalCentreId=request.getParameter("functionalCentreId");
		misReportUIBean=new MisReportUIBean(functionalCentreId, startYear, startMonth, endYear, endMonth);
	}
	else if ("programs".equals(reportBy))
	{
		String[] programIds=request.getParameterValues("programIds");
		misReportUIBean=new MisReportUIBean(programIds, startYear, startMonth, endYear, endMonth);

		boolean reportProgramsIndividually=WebUtils.isChecked(request, "reportProgramsIndividually");
		if (reportProgramsIndividually)
		{
			
		}
	}
	else
	{
		throw(new IllegalStateException("missed a case : reportBy="+reportBy));
	}	
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h3>MIS Report</h3>
<span style="font-weight:bold">ReportBy : </span><%=misReportUIBean.getReportByDescription()%>
<br /><br />
<span style="font-weight:bold">Dates : </span><%=misReportUIBean.getDateRangeForDisplay()%>

<br /><br />

<table class="genericTable borderedTableAndCells" style="font-size:12px">
	<%
		int rowCounter=0;

		// report as a single column summary i.e. normal reporting
		if (reportIndividualProgramNames==null)
		{
			for (MisReportUIBean.DataRow dataRow : misReportUIBean.getDataRows())
			{
				rowCounter++;
				String backgroundColour;
				if (rowCounter%2==0) backgroundColour="#eeeeee";
				else backgroundColour="#dddddd";
					
				%>
					<tr class="genericTableRow" style="background-color:<%=backgroundColour%>">
						<td style="font-weight:bold"><%=dataRow.dataReportId%></td>
						<td style="font-weight:bold"><%=dataRow.dataReportDescription%></td>
						<td><%=dataRow.dataReportResult%></td>
					</tr>
				<%
			}
		}
		else // report as multiple columns for programs
		{
			
		}
	%>
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>