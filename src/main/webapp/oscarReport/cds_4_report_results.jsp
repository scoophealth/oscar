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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="java.util.HashSet"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.CdsFormOption"%>
<%@page import="org.oscarehr.web.Cds4ReportUIBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");
	ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");

    SimpleDateFormat sdf=new SimpleDateFormat(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
	Date startDate=sdf.parse(request.getParameter("startDate"));
	Date endDateInclusive=sdf.parse(request.getParameter("endDate"));

	
	String functionalCentreId=request.getParameter("functionalCentreId");

	// null for none selected, array of providerIds if selected
	String[] providerIdList=request.getParameterValues("providerIds");
	String[] programIdListTemp=request.getParameterValues("programIds");
	HashSet<Integer> programIds=null;
	if (programIdListTemp!=null && programIdListTemp.length>0)
	{
		programIds=new HashSet<Integer>();
		
		for (String s: programIdListTemp)
		{
			s=StringUtils.trimToNull(s);
			if (s!=null)
			{
				programIds.add(new Integer(s));
			}
		}
	}
			
	Cds4ReportUIBean cds4ReportUIBean=new Cds4ReportUIBean(loggedInInfo, functionalCentreId, startDate, endDateInclusive, providerIdList, programIds);
	
	List<CdsFormOption> cdsFormOptions=Cds4ReportUIBean.getCdsFormOptions();
	
	StringBuilder providerNamesList=new StringBuilder();
	if (providerIdList!=null  && providerIdList.length>0) 
	{
		for (String providerId : providerIdList)
		{
			Provider provider=providerManager.getProvider(loggedInInfo, providerId);
			providerNamesList.append(provider.getFormattedName()+" ("+provider.getProviderNo()+"), ");
		}
	}

	StringBuilder programNamesList=new StringBuilder();
	if (programIds!=null)
	{
		for (Integer programId : programIds)
		{
			Program program=programManager.getProgram(programId);
			programNamesList.append(program.getName()+" ("+program.getType()+"), ");
		}
	}
%>

<%@include file="/layouts/caisi_html_top.jspf"%>


<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<h3>CDS Report</h3>
<span style="font-weight:bold">Functional Centre : </span><%=cds4ReportUIBean.getFunctionalCentreDescription()%>
<br />
<span style="font-weight:bold">Dates : </span><%=cds4ReportUIBean.getDateRangeForDisplay()%>
<br />

<%
	if (providerIdList!= null)
	{
		%>
		<span style="font-weight:bold">Providers : </span><%=StringEscapeUtils.escapeHtml(providerNamesList.toString())%>
		<br />
		<%
	}

	if (programIds!=null)
	{
		%>
			<span style="font-weight:bold">Programs : </span><%=StringEscapeUtils.escapeHtml(programNamesList.toString())%>
			<br />
		<%
	}
%>



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
