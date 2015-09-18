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

<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.web.OcanReportUIBean"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Facility facility=loggedInInfo.getCurrentFacility();
   		 
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));
	String ocanType = request.getParameter("ocanType");
		
	//response.setHeader("Content-Disposition", "attachment; filename="+OcanReportUIBean.getFilename(startYear,startMonth,1));
	
	//ByteArrayOutputStream sos = new ByteArrayOutputStream();
	OcanReportUIBean.sendSubmissionToIAR(facility,OcanReportUIBean.generateOCANSubmission(facility.getId(), startYear, startMonth,endYear, endMonth, 1,ocanType));
	//String data = sos.toString();
	
//	OcanReportUIBean.writeIARExportData(data,response.getOutputStream());
	
//	response.getOutputStream().flush();
	
	
//	OcanReportUIBean.testLog();
%>
<html>
<head>
<title>OCAN Submission</title>
</head>
<body>
Testing
</body>
</html>
