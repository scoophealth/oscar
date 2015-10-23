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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page	import="org.springframework.web.context.WebApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink" %>
<%@page import="org.oscarehr.casemgmt.model.CaseManagementNote" %>
<%@page import="java.util.List" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>

<%
    String demo = request.getParameter("demo");
    String display = request.getParameter("display");
    String tid = request.getParameter("table_id");
    Long tableId = 0L;
    if (tid!=null && !tid.trim().isEmpty()) tableId = Long.valueOf(tid);

    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");

    Integer tableName = cmm.getTableNameByDisplay(display);
    String dump = "";
    List<CaseManagementNoteLink> lcml = cmm.getLinkByTableIdDesc(tableName, tableId);
    for (CaseManagementNoteLink link : lcml) {

        CaseManagementNote cmnote = cmm.getNote(link.getNoteId().toString());
        if (cmnote.getNote().startsWith("imported.cms4.2011.06")) {
        	dump = cmnote.getNote().substring("imported.cms4.2011.06".length());
        	break;
        }
    }
    Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;
%>


<html:html locale="true">
<head>
    <title><%=display %> Import</title>
    <% if (isMobileOptimized) { %>
        <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardMobileLayout.css" />
    <% } else { %>
    <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <style type="text/css">
            body { font-size: x-small; }
            textarea { width: 100%; margin: 5px 0; }
            div.label { float: left; }
        </style>
    <% } %>
</head>

<body bgcolor="#EEEEFF" onload="document.forms[0].note.focus();">
	<div class="header"></div>
	<div class="panel">
		Extra data from Import:<br>
		<textarea rows="10" name="dump" readonly="readonly"><%=dump%></textarea>
		<input type="button" value="Close" onclick="window.close();"/>
	</div>
</body>

</html:html>
