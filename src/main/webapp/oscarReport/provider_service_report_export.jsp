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

<%@page import="oscar.util.SqlUtils"%>
<%@page import="org.oscarehr.web.ProviderServiceReportUIBean"%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.web.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.*"%>
<%@page import="java.text.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	String agencyName = oscar.OscarProperties.getInstance().getProperty("db_name","");
	String startDateString = request.getParameter("startDate");
	String endDateString = request.getParameter("endDate");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
	Date startDate = new Date();
	Date endDate = new Date();

	try
	{
		startDate = dateFormatter.parse(startDateString);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}

	try
	{
		endDate = dateFormatter.parse(endDateString);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment; filename=provider_service_" + agencyName+"_"+dateFormatter.format(startDate)+"_"+dateFormatter.format(endDate)+".csv");
	
	// print header
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Agency Name");
		sb.append(',');
		sb.append("Program Name");
		sb.append(',');
		sb.append("Program Type");
		sb.append(',');
		sb.append("Date");
		sb.append(',');
		sb.append("total encounters face to face");
		sb.append(',');
		sb.append("total encounters by phone");
		sb.append(',');
		sb.append("total encounters with out client");
		sb.append(',');
		sb.append("unique client encountered face to face");
		sb.append(',');
		sb.append("unique clients encountered by phone");
		sb.append(',');
		sb.append("unique clients encountered with out client");
		sb.append(',');
		sb.append("total unique clients encountered");
		
		out.write(sb.toString());
		out.write('\n');
	}
	
	ProviderServiceReportUIBean providerServiceReportUIBean = new ProviderServiceReportUIBean(startDate, endDate);
	for (ProviderServiceReportUIBean.DataRow row : providerServiceReportUIBean.getDataRows())
	{
		StringBuilder sb = new StringBuilder();
		sb.append(StringEscapeUtils.escapeCsv(agencyName));
		sb.append(',');
		sb.append(StringEscapeUtils.escapeCsv(row.programName));
		sb.append(',');
		sb.append(StringEscapeUtils.escapeCsv(row.programType));
		sb.append(',');
		sb.append(StringEscapeUtils.escapeCsv(row.date));
		sb.append(',');
		sb.append(row.encounterCounts.nonUniqueCounts.get(EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT));
		sb.append(',');
		sb.append(row.encounterCounts.nonUniqueCounts.get(EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT));
		sb.append(',');
		sb.append(row.encounterCounts.nonUniqueCounts.get(EncounterUtil.EncounterType.ENCOUNTER_WITH_OUT_CLIENT));
		sb.append(',');
		sb.append(row.encounterCounts.uniqueCounts.get(EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT));
		sb.append(',');
		sb.append(row.encounterCounts.uniqueCounts.get(EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT));
		sb.append(',');
		sb.append(row.encounterCounts.uniqueCounts.get(EncounterUtil.EncounterType.ENCOUNTER_WITH_OUT_CLIENT));
		sb.append(',');
		sb.append(row.encounterCounts.totalUniqueCount);
				
		out.write(sb.toString());
		out.write('\n');
	}
%>
