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
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.CdsFormOption"%>
<%@page import="org.oscarehr.web.Cds4ReportUIBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");

    int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));
	String functionalCentreId=request.getParameter("functionalCentreId");
	
	// null for none selected, array of providerIds if selected
	String[] providerIdList=request.getParameterValues("providerIds");
	
	Cds4ReportUIBean cds4ReportUIBean=new Cds4ReportUIBean(functionalCentreId, startYear, startMonth, endYear, endMonth, providerIdList);
	
	List<CdsFormOption> cdsFormOptions=Cds4ReportUIBean.getCdsFormOptions();
	
	StringBuilder providerNamesList=new StringBuilder();
	if (providerIdList==null) providerNamesList.append("All Providers");
	else
	{
		for (String providerId : providerIdList)
		{
			Provider provider=providerManager.getProvider(providerId);

			providerNamesList.append(provider.getFormattedName()+" ("+provider.getProviderNo()+"), ");
		}
	}
%>

<%@include file="/layouts/caisi_html_top.jspf"%>


<%@page import="org.apache.commons.lang.StringEscapeUtils"%><h3>CDS Report</h3>
<span style="font-weight:bold">Functional Centre : </span><%=cds4ReportUIBean.getFunctionalCentreDescription()%>
<br />
<span style="font-weight:bold">Dates : </span><%=cds4ReportUIBean.getDateRangeForDisplay()%>
<br />
<span style="font-weight:bold">Providers : </span><%=StringEscapeUtils.escapeHtml(providerNamesList.toString())%>
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
