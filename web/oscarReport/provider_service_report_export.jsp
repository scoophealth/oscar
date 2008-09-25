
<%@page import="org.oscarehr.web.ProviderServiceReportUIBean"%><%-- 
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
	response.setHeader("Content-Disposition", "attachment; filename=" + agencyName+"_"+dateFormatter.format(startDate)+"_"+dateFormatter.format(endDate)+".csv");
	
	// print header
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Agency Name");
		sb.append(',');
		sb.append("Provider Name");
		sb.append(',');
		sb.append("Date");
		sb.append(',');
		sb.append("Unique clients with face to face encounter");
		sb.append(',');
		sb.append("Unique client with any ancounter");

		// for debugging
		// System.err.println(sb.toString());
		
		out.write(sb.toString());
		out.write('\n');
	}
	
	ProviderServiceReportUIBean providerServiceReportUIBean = new ProviderServiceReportUIBean(startDate, endDate);
	for (ProviderServiceReportUIBean.DataRow row : providerServiceReportUIBean.getDataRows())
	{
		StringBuilder sb = new StringBuilder();
		sb.append(StringEscapeUtils.escapeCsv(agencyName));
		sb.append(',');
		sb.append(StringEscapeUtils.escapeCsv(row.providerName));
		sb.append(',');
		sb.append(StringEscapeUtils.escapeCsv(row.date));
		sb.append(',');
		sb.append(row.uniqueFaceToFaceEncounters);
		sb.append(',');
		sb.append(row.uniqueAllEncounters);
		
		// for debugging
		// System.err.println(sb.toString());
		
		out.write(sb.toString());
		out.write('\n');
	}
%>