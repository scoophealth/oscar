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
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ include file="/taglibs.jsp"%>
<%
	int diff = 0;
	if(request.getAttribute("difference") != null) {
		diff = ((Integer)request.getAttribute("difference")).intValue();
	}
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script>
	
	
	function doAll() {
		popupDifference();
		InitializeTimer();
	}

	function popupDifference() {
		var diff = <%=diff%>;
		if(diff > 0) {
			alert('You have ' + diff + ' new ticklers since the last check');
		}
	}

<!--
var secs
var timerID = null
var timerRunning = false
var delay = 1000

function InitializeTimer()
{
    // Set the length of the timer, in seconds
    secs = 10
    StopTheClock()
    StartTheTimer()
}

function StopTheClock()
{
    if(timerRunning)
        clearTimeout(timerID)
    timerRunning = false
}

function StartTheTimer()
{
	document.unreadTicklerForm.target='_self';
    if (secs==0)
    {
        StopTheClock()
        // Here's where you put something useful that's
        // supposed to happen after the allotted time.
        // For example, you could display a message:
		document.unreadTicklerForm.submit();
    }
    else
    {
        self.status = secs
        secs = secs - 1
        timerRunning = true
        timerID = self.setTimeout("StartTheTimer()", delay)
    }
}
//-->
</SCRIPT>

</head>
<body onload="doAll()">
<%
	String ticklerProvName = (String)request.getAttribute("tklerProviderName");
	if (ticklerProvName == null) {
		ticklerProvName = "You";
	}
%>
<%=ticklerProvName %> have
<c:out value="${sessionScope.num_ticklers}" />
ticklers.
<html:form styleId="unreadTicklerForm" action="/UnreadTickler.do" target="_self">
	<input type="hidden" name="method" value="refresh"/>
</html:form>

<!-- 
<table width="100%">

	<c:forEach var="tickler" items="${ticklers}">
		<tr>
			<td>
			</td>
		</tr>
	</c:forEach>
</table>
-->
<br />
<input type="button" value="Logout"
	onclick="document.unreadTicklerForm.method.value='logout';document.unreadTicklerForm.submit()" />
</body>
</html>
