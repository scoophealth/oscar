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

<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Waiting List</title>

</head>
<script>
function removePatient(demographicNo, waitingList){
    var agree=confirm("Are you sure you want to remove this patient from the waiting list?");
    if (agree){
        windowprops = "height=50,width=50,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
        var page = 'RemoveFromWaitingList.jsp?listId='+waitingList+'&demographicNo='+demographicNo; 
        var popup = window.open(page, "removeWaitingList", windowprops); 
    }
    else{
        return false ;
    }
        
    
}
</script>
<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Waiting List</td>
		<td class="MainTableTopRowRightColumn" width="400">
		<table class="TopStatusBar">
			<tr>
				<td><logic:present name="demoInfo">
					<bean:write name="demoInfo" /> years</logic:present></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn"><a
			href="../demographic/demographiccontrol.jsp?demographic_no=<bean:write name="demographicNo"/>&displaymode=edit&dboperation=search_detail"><bean:message
			key="global.btnBack" />&nbsp;</a></td>
		<td class="MainTableRightColumn">
		<table border=0 cellspacing=4 width=700>
			<tr>
				<td>
				<table>
					<tr>
						<td>
					<tr>
						<td align="left" class="Header" width="100">Waiting List</td>
						<td align="left" class="Header" width="50">Position</td>
						<td align="left" class="Header" width="100">Note</td>
						<td align="left" class="Header" width="100">On the Waiting
						List Since</td>
						<td align="left" class="Header" width="100"></td>
					</tr>
					<logic:iterate id="waitingListBean" name="patientWaitingList"
						property="patientWaitingList">
						<tr class="data">
							<td width="100"><bean:write name="waitingListBean"
								property="waitingList" /></td>
							<td width="50"><bean:write name="waitingListBean"
								property="position" /></td>
							<td width="100"><bean:write name="waitingListBean"
								property="note" /></td>
							<td width="100"><bean:write name="waitingListBean"
								property="onListSince" /></td>
							<td><a href=#
								onClick="removePatient('<bean:write name="waitingListBean" property="demographicNo"/>', '<bean:write name="waitingListBean" property="waitingListID" />');">Remove</a>
							</td>
						</tr>
					</logic:iterate>
					</td>
					</tr>
				</table>
				<table>
					<tr>
						<td><input type="button" name="Button"
							value="<bean:message key="global.btnClose"/>"
							onClick="window.close()"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
