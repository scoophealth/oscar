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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../../logout.jsp");%>
</security:oscarSec>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage REST Clients (OAuth)</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script>
	function addNewClient() {
		
		$('#new-form').dialog('open');
		
		
		
       
	
	}
	
	function listClients() {
		 $("#clientTable tbody").find("tr").remove();
		
		jQuery.getJSON("clientManage.json",{method: "list"},
                function(data,textStatus){

					for(var x=0;x<data.length;x++) {
						var id = data[x].id;
						var name = data[x].name;
						var key = data[x].key;
						var secret = data[x].secret;
						var uri = data[x].uri;
						
						$('#clientTable > tbody:last').append('<tr><td>'+name+'</td><td>'+key+'</td><td>'+secret+'</td><td>'+uri+'</td><td><a href="javascript:void(0);" onclick="deleteClient('+id+');"><img border="0" title="delete" src="../../images/Delete16.gif"/></a></td></tr>');
                		
					}
        });
	}
	
	function deleteClient(id) {

        jQuery.getJSON("clientManage.json",
                {
                        method: "delete",
                        id: id
                },
                function(xml){
                	if(xml.success)
                		listClients();
                	else
                		alert(xml.error);
                });
	
	}
	
	
	
	$(document).ready(function(){
		listClients();
		
		$( "#new-form" ).dialog({
			autoOpen: false,
			height: 275,
			width: 450,
			modal: true,
			buttons: {
				"Add Client": function() {			
					$( this ).dialog( "close" );	
					var name = $("#clientName").val();
					var uri = $("#clientURI").val();
					 jQuery.getJSON("clientManage.json",
				                {
				                        method: "add",
				                        name: name,
				                        uri:uri
				                },
				                function(xml){
				                	if(xml.success)
				                		listClients();
				                	else
				                		alert(xml.error);
				                });
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
	});
	
	
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage Clients</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<h3>Clients</h3>
			<br/>
			<table width="80%" id="clientTable" name="clientTable">
				<thead>
					<tr>
						<td><b>Name</b></td>
						<td><b>Client Key</B></td>
						<td><b>Client Secret</B></td>
						<td><b>URI</B></td>
						<td><b>Actions</b></td>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
					
			<br/><br/>
			<input type="button" value="Add New" onClick="addNewClient()"/>	
			
			<br/><br/>
			
			<%
				String thisUrl = request.getRequestURL().toString();
				String contextPath = request.getContextPath();
				
				String here = thisUrl.substring(0,thisUrl.indexOf(contextPath) + contextPath.length());
			%>
			
			<table  border="1">
				<tr>
					<td>Temporary Credential Request:</td>
					<td><%=here%>/ws/test/initiate</td>
				</tr>
				<tr>
					<td>Resource Owner Authorization URI:</td>
					<td><%=here%>/ws/authorize</td>
				</tr>
				<tr>
					<td>Token Request URI:</td>
					<td><%=here%>/ws/token</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>



<div id="new-form" title="Create Client">
	<p class="validateTips"></p>

	<form>
	<fieldset>
		<input type="text" name="clientName" id="clientName" class="text ui-widget-content ui-corner-all" />
		<label for="clientName">Name:</label>
		<br/>
		<input type="text" name="clientURI" id="clientURI" class="text ui-widget-content ui-corner-all" />
		<label for="clientURI">URI:</label>		
	</fieldset>
	</form>
</div>

</body>


</html:html>
