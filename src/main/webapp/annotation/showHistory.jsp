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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page	import="org.springframework.web.context.WebApplicationContext,
		org.springframework.web.context.support.WebApplicationContextUtils,
		org.oscarehr.casemgmt.model.CaseManagementNote,
		org.oscarehr.casemgmt.model.CaseManagementNoteLink,
		org.oscarehr.casemgmt.service.CaseManagementManager,
		java.util.List"%>
<%
    HttpSession se = request.getSession();
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
    
    String display = request.getParameter("display");
    String uuid = request.getParameter("uuid");
    if (display==null) display = "";
    if (uuid==null) uuid = "";
    List<CaseManagementNote> lcmn = cmm.getNotesByUUID(uuid);
 %>
<html>
<head>
<title>Annotation History</title>
</head>
<body>
<h3 style="text-align: center;"><%=display%> Annotation Revision History</h3>

<% for (CaseManagementNote cmn : lcmn) { 
      String showNote = cmn.getNote();
      if (showNote.startsWith("imported.cms4.2011.06")) showNote = showNote.substring("imported.cms4.2011.06".length());
%>
    <div style="width: 99%; background-color: #EFEFEF; font-size: 12px; border-left: thin groove #000000; border-bottom: thin groove #000000; border-right: thin groove #000000;">
	<%=showNote%>
	<div style="color: #0000FF;">
	    Documentation Date: <%=cmn.getCreate_date()%><br>
	    Saved by <%=cmn.getProviderName()%>
	</div>
    </div>
    <br>
<% } %>

</body>
</html>
