<%-- 
/*
* Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
<%@page import="java.util.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.web.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.*"%>
<%@page import="java.text.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	String agencyName = request.getParameter("agencyName");
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
	MiscUtils.setToBeginningOfMonth(startCalendar);

	Calendar endCalendar = Calendar.getInstance();
	try
	{
		endDate = dateFormatter.parse(endDateString);
		endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	MiscUtils.setToBeginningOfMonth(endCalendar);

	// for each program...
	//    for each month...
	//       print monthly data

	PopulationReportUIBean populationReportUIBean = new PopulationReportUIBean();
	
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

		// for debugging
		// System.err.println(sb.toString());
		
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=activity_report_" + agencyName+"_"+dateFormatter.format(startDate)+"_"+dateFormatter.format(endDate)+".csv");

		out.write(sb.toString());
		out.write('\n');
	}

	for (Program program : populationReportUIBean.getAllPrograms())
	{
		populationReportUIBean.setProgramId(program.getId());

		String programType = program.getType();
		if (!"Bed".equals(programType) && !"Service".equals(programType)) continue;

		// this line is here to ensure the calendar is materialised before cloning, it's a known "issue" in java and not considered a bug....
		startCalendar.getTimeInMillis();
		Calendar tempStartCalendar = (Calendar)startCalendar.clone();
		tempStartCalendar.clear(Calendar.DAY_OF_MONTH);

		while (tempStartCalendar.compareTo(endCalendar) < 0)
		{
			Calendar tempEndCalendar = (Calendar)startCalendar.clone();
			tempEndCalendar.clear(Calendar.DAY_OF_MONTH);
			tempEndCalendar.add(Calendar.MONTH, 1);

			populationReportUIBean.setStartDate(tempStartCalendar.getTime());
			populationReportUIBean.setEndDate(tempEndCalendar.getTime());
			PopulationReportDataObjects.RoleDataGrid roleDataGrid = populationReportUIBean.getRoleDataGrid();

			for (Map.Entry<Role, PopulationReportDataObjects.EncounterTypeDataGrid> roleEntry : roleDataGrid.entrySet())
			{
				for (Map.Entry<EncounterUtil.EncounterType, PopulationReportDataObjects.EncounterTypeDataRow> encounterEntry : roleEntry.getValue().entrySet())
				{
					StringBuilder sb = new StringBuilder();
					sb.append(StringEscapeUtils.escapeCsv(agencyName));
					sb.append(',');
					sb.append(StringEscapeUtils.escapeCsv(program.getName()));
					sb.append(',');
					sb.append(StringEscapeUtils.escapeCsv(programType));
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
					
					// for debugging
					// System.err.println(sb.toString());
					
					out.write(sb.toString());
					out.write('\n');
				}
			}
			
			tempStartCalendar.add(Calendar.MONTH, 1);
		}
	}
%>