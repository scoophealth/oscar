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

You have
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