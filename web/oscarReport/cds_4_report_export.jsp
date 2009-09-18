
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.web.Cds4ReportUIBean"%><%-- 
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
	int programId = Integer.parseInt(request.getParameter("programId"));
	
	ArrayList<String> results=Cds4ReportUIBean.getAsciiExportData(programId, startYear, startMonth, endYear, endMonth);
	
	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment; filename="+Cds4ReportUIBean.getFilename(programId)+"");
	PrintWriter responseWriter=response.getWriter();
	
	for (String s : results)
	{
		responseWriter.println(s);
	}
	
	responseWriter.flush();
%>