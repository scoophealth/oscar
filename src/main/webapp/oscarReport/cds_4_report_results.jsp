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
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.CdsFormOption"%>
<%@page import="org.oscarehr.web.Cds4ReportUIBean"%>
<%
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));
	
	String functionalCentreId=request.getParameter("functionalCentreId");

	Cds4ReportUIBean cds4ReportUIBean=new Cds4ReportUIBean(functionalCentreId, startYear, startMonth, endYear, endMonth);
	
	List<CdsFormOption> cdsFormOptions=Cds4ReportUIBean.getCdsFormOptions();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>


<%@page import="org.apache.commons.lang.StringEscapeUtils"%><h3>CDS Report</h3>
<span style="font-weight:bold">Functional Centre : </span><%=cds4ReportUIBean.getFunctionalCentreDescription()%>
<br />
<span style="font-weight:bold">Dates : </span><%=cds4ReportUIBean.getDateRangeForDisplay()%>

<br />

<table class="genericTable borderedTableAndCells" style="font-size:12px">
	<tr class="genericTableHeader">
		<td style="width:5em">CDS Category ID</td>
		<td style="width:15em">CDS Category Description</td>
		<td>Multi<br />Admn</td>
		<%
			for (int i=0; i<Cds4ReportUIBean.NUMBER_OF_COHORT_BUCKETS; i++)
			{
				%>
					<td>Coh<br /><%=i%></td>
				<%
			}
		%>
		<td>Coh<br />Total</td>
	</tr>
	<%
		int rowCounter=0;
		for (CdsFormOption cdsFormOption : cdsFormOptions)
		{
			rowCounter++;
			String backgroundColour;
			if (rowCounter%2==0) backgroundColour="#eeeeee";
			else backgroundColour="#dddddd";
				
			int[] dataRow=cds4ReportUIBean.getDataRow(cdsFormOption);
			%>
				<tr class="genericTableRow" style="background-color:<%=backgroundColour%>">
					<td style="font-weight:bold"><%=StringEscapeUtils.escapeHtml(cdsFormOption.getCdsDataCategory())%></td>
					<td style="font-weight:bold"><%=StringEscapeUtils.escapeHtml(cdsFormOption.getCdsDataCategoryName())%></td>
					<%
						for (int dataElement : dataRow)
						{
							%>
								<td><%=dataElement==-1?"N/A":String.valueOf(dataElement)%></td>
							<%
						}
					%>
					<td><%=Cds4ReportUIBean.getCohortTotal(dataRow)%></td>
				</tr>
			<%
		}
	%>
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
