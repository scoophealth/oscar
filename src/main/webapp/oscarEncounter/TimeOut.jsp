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

<!-- Jan 20, 2003,-->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
    oscar.oscarEncounter.pageUtil.EctSessionBean sessionbean = null;
    if((sessionbean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
        response.sendRedirect("error.jsp");
        return;
    }
%>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html:html locale="true">
<script type="text/javascript" language=javascript>
    function loadUp(){
        window.resizeTo(900,50);
        setTimeout("window.close()",1000);
<%
    if (sessionbean.status != null && !sessionbean.status.equals("")) {
        out.print("opener.refresh();");
    }
%>
    }
</script>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.timeOut.title" /></title>
</head>
<body onload="javascript:loadUp()">

<!--  -->
<table class="MainTable" id="scrollNumber1"
	name="<bean:message key="oscarEncounter.timeOut.msgEncounter"/>">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td style="color: white"><bean:message
					key="oscarEncounter.timeOut.msgSaveExit" /></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html:html>
