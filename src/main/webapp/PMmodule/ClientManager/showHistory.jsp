<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page
	import="org.springframework.web.context.*,org.springframework.web.context.support.*, org.oscarehr.PMmodule.service.ProviderManager, org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Admission" %>
<%@page import="org.oscarehr.common.dao.AdmissionDao" %>
<%@page import="org.oscarehr.common.model.OscarLog" %>
<%@page import="org.oscarehr.common.dao.OscarLogDao" %>
<%@page import="java.util.List" %>

<%
	String admissionId = request.getParameter("id");
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    ProviderManager pMgr = (ProviderManager)ctx.getBean("providerManager");
    AdmissionDao admissionDao = SpringUtils.getBean(AdmissionDao.class);
    
    Admission admission = admissionDao.find(Integer.parseInt(admissionId));
    
    OscarLogDao oscarLogDao = SpringUtils.getBean(OscarLogDao.class);
    List<OscarLog> logs = oscarLogDao.findByActionAndData(request.getParameter("type"), admissionId);
    
    pageContext.setAttribute("history", logs);
 %>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Note History</title>
</head>
<body>
<h3 style="text-align: center;"><%=request.getParameter("title") %></h3>
<h3 style="text-align: center;"></h3>
<nested:iterate indexId="idx" id="log" name="history">
	<div
		style="width: 99%; background-color: #EFEFEF; font-size: 12px; border-left: thin groove #000000; border-bottom: thin groove #000000; border-right: thin groove #000000;">
		<p>
			<%	
				String providerName = pMgr.getProvider(((OscarLog)log).getProviderNo()).getFormattedName();
			%>
			<%=providerName%> updated AdmissionDate to <nested:write name="log"
			property="content" format="dd-MMM-yyyy H:mm" />
			
		</p>
		<div style="color: #0000FF;">
		
	        Documentation Date: <nested:write name="log"
			property="created" format="dd-MMM-yyyy H:mm" />
		</div>
	</div>
</nested:iterate>
</body>
</html>

