<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ page import="java.util.*,org.oscarehr.common.model.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.schedule" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.appt.status.mgr.title" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="apptStatusList"/>
</head>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<body>
<%
        String reseturl = request.getContextPath();
        reseturl = reseturl + "/appointment/apptStatusSetting.do?dispatch=reset";
    %>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.appt.status.mgr.title" /></font></th>
		<th align="right" NOWRAP><font face="Helvetica" color="#CCCCCC"><a
			href=<%=reseturl%>>reset</a></font></th>
	</tr>
</table>


<table class="borderAll" width="100%">
	<tr>
		<th><bean:message key="admin.appt.status.mgr.label.status" /></th>
		<th><bean:message key="admin.appt.status.mgr.label.desc" /></th>
		<th><bean:message key="admin.appt.status.mgr.label.color" /></th>
		<th><bean:message key="admin.appt.status.mgr.label.enable" /></th>
		<th><bean:message key="admin.appt.status.mgr.label.active" /></th>
		<th>&nbsp;</th>
	</tr>
	<%
            List apptsList = (List) request.getAttribute("allStatus");
            AppointmentStatus apptStatus = null;
            int iStatusID = 0;
            String strStatus = "";
            String strDesc = "";
            String strColor = "";
            int iActive = 0;
            int iEditable = 0;
            for (int i = 0; i < apptsList.size(); i++) {
                apptStatus = (AppointmentStatus) apptsList.get(i);
                iStatusID = apptStatus.getId();
                strStatus = apptStatus.getStatus();
                strDesc = apptStatus.getDescription();
                strColor = apptStatus.getColor();
                iActive = apptStatus.getActive();
                iEditable = apptStatus.getEditable();
%>
	<tr class=<%=(i % 2 == 0) ? "even" : "odd"%>>
		<td class="nowrap"><%=strStatus%></td>
		<td class="nowrap"><%=strDesc%></td>
		<td class="nowrap" bgcolor="<%=strColor%>"><%=strColor%></td>
		<td class="nowrap"><%=iActive%></td>
		<td class="nowrap">
		<%
    String url = request.getContextPath();
    url = url + "/appointment/apptStatusSetting.do?dispatch=modify&statusID=";
    url = url + iStatusID;
        %> <a href=<%=url%>>Edit</a> &nbsp;&nbsp;&nbsp; <%
    int iToStatus = (iActive > 0) ? 0 : 1;
    url = request.getContextPath();
    url = url + "/appointment/apptStatusSetting.do?dispatch=changestatus&iActive=";
    url = url + iToStatus;
    url = url + "&statusID=";
    url = url + iStatusID;
    if (iEditable == 1) {
        %> <a href=<%=url%>><%=(iActive > 0) ? "Disable" : "Enable"%></a>
		<%
    }
        %>
		</td>
	</tr>
	<%
            }
%>
</table>
<br>

<%
    String strUseStatus = (String)request.getAttribute("useStatus");
    if (null!=strUseStatus && strUseStatus.length()>0)
    {
%>
The code [<%=strUseStatus%>] has been used before, please enable that
status.
<%
    }
%>
</body>
</html>
