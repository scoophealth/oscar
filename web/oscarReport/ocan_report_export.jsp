
<%@page import="java.util.HashMap"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.web.OcanReportUIBean"%><%-- 
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
<%@page contentType="application/octet-stream"%>
<%
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	
	response.setHeader("Content-Disposition", "attachment; filename="+OcanReportUIBean.getFilename(startYear,startMonth,1));
	
	
	OcanReportUIBean.writeXmlExportData(startYear, startMonth, 1,response.getOutputStream());
	
	response.getOutputStream().flush();
%>