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
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.model.UserProperty" %>

<%
String curUser_no = (String) session.getAttribute("user");
String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
%>

<security:oscarSec objectName="_admin" roleName="<%=roleName$%>" rights="r" reverse="false">


<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function disableNextAndFuturePush(flag) {
	$.ajax({
		type: "POST",
		url: "<%=request.getContextPath()%>/integrator/IntegratorPush.do?method=disableNextAndFuturePushes",
		data: { type: flag},
		dataType:'json'
		})
		.done(function( msg ) {
			if(msg && msg.success == 'false')
			alert(msg.reason);
		});
}

function updatePushData() {
	$.ajax({
		type: "POST",
		url: "<%=request.getContextPath()%>/integrator/IntegratorPush.do?method=getPushData",
		dataType:'json'
		})
		.done(function( data ) {
			var msg = data.items;
			
			for(var x=0;x<msg.length;x++) {
				
				$("#dataTable tbody").append("<tr><td>");
				
				if(msg[x].status == 'running') {
					$("#dataTable tbody").append("<input type=\"button\" onClick=\"pause()\" value=\"Pause\"/>");
				
					$("#dataTable tbody").append("<input type=\"button\" onClick=\"resume()\" value=\"Resume\"/>");
				}  else {
					$("#dataTable tbody").append("&nbsp;");
				}
				
					
				
				$("#dataTable tbody").append("</td>"+
						"<td>"+msg[x].status+"</td><td>"+msg[x].dateCreatedAsString+"</td>"+
						"<td>"+msg[x].progressAsPercentageString+"</td><td>"+msg[x].estimatedDateOfCompletionAsString+"</td>"+
						"<td>"+msg[x].totalDemographics+"</td>"+
						"</tr>");
				
			}
			
		});
}

function pause() {
	$.ajax({
		type: "POST",
		url: "<%=request.getContextPath()%>/integrator/IntegratorPush.do?method=togglePause",
		data: { pause: 'true'},
		dataType:'json'
		})
		.done(function( msg ) {
			if(msg && msg.success == 'false')
			alert(msg.reason);
		});
}

function resume() {
	$.ajax({
		type: "POST",
		url: "<%=request.getContextPath()%>/integrator/IntegratorPush.do?method=togglePause",
		data: { pause: 'false'},
		dataType:'json'
		})
		.done(function( msg ) {
			if(msg && msg.success == 'false')
			alert(msg.reason);
		});
}

$( document ).ready(function() {
	updatePushData();
});



</script>
<html:base />
<meta http-equiv="Content-Type" content="text/html;">
<title>Integrator Push Manager</title>

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">

</head>


<body>

<h3>Integrator Push Events</h3>
<br/>

<table class="MainTable" id="dataTable" name="dataTable" border="1">
	<thead>
		<tr>
			<th>Actions</th>
			<th>Status</th>
			<th>Date Started</th>
			<th>Progress</th>
			<th>Est. Time to Completion</th>
			<th>Total Records</th>
		</tr>
	</thead>
	
	<tbody>
	</tbody>
</table>
</body>

<br>

<input type="button" onClick="disableNextAndFuturePush(false)" value="Enable Next and Future Pushes"/>

<input type="button" onClick="disableNextAndFuturePush(true)" value="Disable Next and Future Pushes"/>



</html:html>

	
</security:oscarSec>