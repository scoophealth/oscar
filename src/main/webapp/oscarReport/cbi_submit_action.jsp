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

<%@page import="oscar.util.CBIUtil"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.dao.OcanStaffFormDao"%>
<%@page import="java.util.List"%>
<%
	String[] cbiFormIds = request.getParameterValues("cbiFormIdsSelected"); 
    OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
    CBIUtil cbiUtil = new CBIUtil();
    for(int i=0; i<cbiFormIds.length; i++) {
    	OcanStaffForm cbiForm = ocanStaffFormDao.findOcanStaffFormById(Integer.valueOf(cbiFormIds[i]));
    	if(cbiForm!=null)
    		cbiUtil.submitCBIData(cbiForm);
    }    
    		
%>
<html>
<head>
<title>CBI Submission</title>
</head>
<body>
CBI Submission Finished !
</body>
</html>
