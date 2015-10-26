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
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="oscar.util.DateUtils"%>
<%@page import="oscar.util.SqlUtils"%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.web.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.*"%>
<%@page import="java.text.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page contentType="text/csv"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String agencyName = oscar.OscarProperties.getInstance().getProperty("db_name","");
	String startDateString = request.getParameter("startDate");
	String endDateString = request.getParameter("endDate");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM");
	Date startDate = null;
	Date endDate = null;

	// default to this month calendar range.
	Calendar startCalendar = Calendar.getInstance();
	try
	{
		startDate = dateFormatter.parse(startDateString);
		startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	DateUtils.setToBeginningOfMonth(startCalendar);

	Calendar endCalendar = Calendar.getInstance();
	try
	{
		endDate = dateFormatter.parse(endDateString);
		endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		endCalendar.add(Calendar.MONTH, 1);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	DateUtils.setToBeginningOfMonth(endCalendar);

	// for each program...
	//    for each month...
	//       print monthly data

	session.removeAttribute("progressStatus");
	ProgressStatus progressStatus=new ProgressStatus();
	session.setAttribute("progressStatus", progressStatus);
	
	PopulationReportUIBean populationReportUIBean = new PopulationReportUIBean(loggedInInfo);
	populationReportUIBean.skipTotalRow=true;
	
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
		sb.append("Provider Role");
		sb.append(',');
		sb.append("Encounter Type");

		for (IssueGroup issueGroup : populationReportUIBean.getIssueGroups())
		{
			sb.append(',');
			sb.append(StringEscapeUtils.escapeCsv(issueGroup.getName()));
		}

		sb.append(',');
		sb.append("Total Unique Encounters");
		sb.append(',');
		sb.append("Total Unique Clients");
		
		response.setHeader("Content-Disposition", "attachment; filename=activity_report_" + agencyName+"_"+dateFormatter.format(startCalendar.getTime())+"_"+dateFormatter.format(endCalendar.getTime())+".csv");

		out.write(sb.toString());
		out.write('\n');
	}

	long months=(endCalendar.getTimeInMillis()-startCalendar.getTimeInMillis())/(org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY*30);
	long total=populationReportUIBean.getAllPrograms().size()*populationReportUIBean.getSecRoles().size()*3*months;
	progressStatus.total="Estimated total "+total+" rows ("+populationReportUIBean.getAllPrograms().size()+" programs * "+populationReportUIBean.getSecRoles().size()+" roles * 3 encounter Types * "+months+" months)";
	int rowsProcessed=0;
	
	for (Program program : populationReportUIBean.getAllPrograms())
	{
		populationReportUIBean.setProgramId(program.getId());

		if (!program.isBed() && !program.isService()) continue;

		// this line is here to ensure the calendar is materialised before cloning, it's a known "issue" in java and not considered a bug....
		startCalendar.getTimeInMillis();
		Calendar tempStartCalendar = (Calendar)startCalendar.clone();
		tempStartCalendar.clear(Calendar.DAY_OF_MONTH);
		
		while (tempStartCalendar.compareTo(endCalendar) < 0)
		{
			Calendar tempEndCalendar = (Calendar)tempStartCalendar.clone();
			tempEndCalendar.add(Calendar.MONTH, 1);
			DateUtils.setToBeginningOfMonth(tempEndCalendar);
			
			populationReportUIBean.setStartDate(tempStartCalendar.getTime());
			populationReportUIBean.setEndDate(tempEndCalendar.getTime());
			PopulationReportDataObjects.RoleDataGrid roleDataGrid = populationReportUIBean.getRoleDataGrid();

			for (Map.Entry<SecRole, PopulationReportDataObjects.EncounterTypeDataGrid> roleEntry : roleDataGrid.entrySet())
			{
				for (Map.Entry<EncounterUtil.EncounterType, PopulationReportDataObjects.EncounterTypeDataRow> encounterEntry : roleEntry.getValue().entrySet())
				{
					StringBuilder sb = new StringBuilder();
					sb.append(StringEscapeUtils.escapeCsv(agencyName));
					sb.append(',');
					sb.append(StringEscapeUtils.escapeCsv(program.getName()));
					sb.append(',');
					sb.append(StringEscapeUtils.escapeCsv(program.getType()));
					sb.append(',');
					sb.append(dateFormatter.format(tempStartCalendar.getTime()));
					sb.append(',');
					sb.append(StringEscapeUtils.escapeCsv(roleEntry.getKey().getName()));
					sb.append(',');
					sb.append(StringEscapeUtils.escapeCsv(encounterEntry.getKey().name()));

					for (Integer issueGroupEntry : encounterEntry.getValue().values())
					{
						sb.append(',');
						sb.append(issueGroupEntry);
					}
					
					sb.append(',');
					sb.append(encounterEntry.getValue().rowTotalUniqueEncounters);
					sb.append(',');
					sb.append(encounterEntry.getValue().rowTotalUniqueClients);
					
					out.write(sb.toString());
					out.write('\n');
					
					rowsProcessed++;
					progressStatus.processed=""+rowsProcessed+" rows processed";
					progressStatus.percentComplete=(int)(rowsProcessed*100/total);
					progressStatus.currentItem=""+program.getName()+" "+dateFormatter.format(tempStartCalendar.getTime())+" "+roleEntry.getKey().getName();
				}
			}
			
			tempStartCalendar.add(Calendar.MONTH, 1);
			tempStartCalendar.getTime();
		}
	}
	
	progressStatus.completed=true;
%>
