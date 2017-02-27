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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.OscarProperties" %>
<%@page import="org.oscarehr.managers.DashboardManager" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
	DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);	
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	String url = dashboardManager.getSharedOutcomesDashboardLaunchURL(loggedInInfo);
%>
<html>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.9.1.js"></script>
<script>

function testListener( event ) {
         console.log("got a message from the dashboard");
        var data = event.data || event.originalEvent.data;
        var source = event.source || event.originalEvent.source;
        receiveOutcomesMessage1(event);
}

var outcomesWindow; 

window.onbeforeunload = function() {
	//outcomesWindow.close();
    //return "You sure?";
	outcomesWindow.postMessage({'response' : 'CLOSE'}, '<%=OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_host") %>');
};


window.unload = function() {
	outcomesWindow.postMessage({'response' : 'CLOSE'}, '<%=OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_host") %>');
};

function launchOutcomesDashboard1(outcomesHostName) {
	var url = '<%=url%>';
	console.log('url=' + url.toString());
	outcomesWindow = window.open( url.toString(), "OutcomesWindow","toolbar=1,scrollbars=1,status=1,statusbar=1,copyhistory=1,resizable=1,width=1280,height=1024");
	outcomesWindow.focus();
	
	var oTimer = function() {
		outcomesTimeout = setTimeout(function(){
			outcomesWindow.postMessage({'response' : 'CONNECT'}, '<%=OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_host") %>');
			console.log('sending CONNECT');
			oTimer();
		}, 500);
	};
    oTimer();
}

var connected= false;

function receiveOutcomesMessage1( event ) {
	var data = event.data || event.originalEvent.data;
	var outcomesData = data.outcomes;
	var params = outcomesData.request;
	switch( outcomesData.interaction ) {
		case "CONNECTED" :
			clearTimeout( outcomesTimeout );
			console.log('Connected');
			connected = true;
			break;
		case "handlePiePieceClick":
			if(params && params.hasOwnProperty("query") && params.hasOwnProperty("group") && params.hasOwnProperty("username")) {
				//console.log("query="+params.query); //OSCAR Metric Test
				//console.log("group="+params.group); //Up to date, Overdue, ect (the label) - doesn't matter for us
				//console.log("username="+params.username); //999998
				$("#drillDownFrame").attr('src','DrilldownDisplay.do?method=getDrilldownBySharedMetricSetName&sharedMetricSetName=' + encodeURIComponent(params.query) + "&providerNo=" + params.username);
				outcomesWindow.postMessage( outcomesData.onSuccess, '*' );
				//window.focus();
			}
			break;
		case "submitQuery":
			if(params && params.hasOwnProperty("queryList")) {
				var data = params.queryList;
				
			       jQuery.ajax({
		                url: "../OutcomesDashboard.do?method=refreshIndicators",
		            type: 'POST',
		            async:true,
		            data: 'data=' + btoa(JSON.stringify(params)),
		            dataType: 'json',
		            success: function(data) {
		            	outcomesWindow.postMessage( outcomesData.onSuccess, '*' );	
		            }
		        });
			}
			
			break;			
	}
}

$(document).ready(function(){
	$( window ).on( "message", testListener );
	launchOutcomesDashboard1();
});


</script>
<style>
html, body { height: 100% }
</style>
</head>
<body>
<h5>OSCAR EMR - Keep window open to interact with Common Provider Dashboard window.</h5>
<input type="button" value="close" onClick="window.close()"/>
<iframe id="drillDownFrame" style="width:100%;height:100%"></iframe>

</body>

</html>
