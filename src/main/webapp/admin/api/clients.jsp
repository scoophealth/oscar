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
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage REST Clients (OAuth)</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

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
	
	function listTokens() {
		 $("#tokenTable tbody").find("tr").remove();
		 
		jQuery.getJSON("clientManage.json",{method: "listTokens"},
               function(data,textStatus){
					
						
					for(var x=0;x<data.length;x++) {
						console.log(JSON.stringify(data[x]));
						var clientId = data[x].clientId;
						var dateCreated = data[x].dateCreated;
						var id = data[x].id;
						var issued = data[x].issued;
						var lifetime = data[x].lifetime;
						var persistent = data[x].persistent;
						var providerNo = data[x].providerNo;
						var tokenId = data[x].tokenId;
						var tokenSecret = data[x].tokenSecret;
						
						$('#tokenTable > tbody:last').append('<tr><td>'+tokenId+'</td><td>'+lifetime+'</td><td>'+issued+'</td><td>'+providerNo+'</td><td></td></tr>');
					}
       });
	}
	
	
	function deleteClient(id) {
        jQuery.getJSON("clientManage.json", {
	        method: "delete",
	        id: id
        },
        function(xml) {
        	if(xml.success)
        		listClients();
        	else
        		alert(xml.error);
        });
	}
	
	$(document).ready(function(){
		listClients();
		listTokens();
		
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
<h4>Manage Clients</h4>
<table id="clientTable" name="clientTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>Name</th>
			<th>Client Key</th>
			<th>Client Secret</th>
			<th>URI *</td>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody></tbody>
</table>
<input type="button" class="btn btn-primary" value="Add New" onClick="addNewClient()"/>	
<%
	String thisUrl = request.getRequestURL().toString();
	String contextPath = request.getContextPath();
	String here = thisUrl.substring(0,thisUrl.indexOf(contextPath) + contextPath.length());
%>
<hr />
<h4>Tokens</h4>
<table id="tokenTable" name="tokenTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>ID</th>
			<th>TTL (seconds)</th>
			<th>Issued</th>
			<th>Provider</td>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody></tbody>
</table>

<hr/>
<table class="table table-bordered table-striped table-hover table-condensed">
	<tr>
		<td>Temporary Credential Request URI:</td>
		<td><%=here%>/ws/oauth/initiate</td>
	</tr>
	<tr>
		<td>Resource Owner Authorization URI:</td>
		<td><%=here%>/ws/oauth/authorize</td>
	</tr>
	<tr>
		<td>Token Request URI:</td>
		<td><%=here%>/ws/oauth/token</td>
	</tr>
</table>


<aside>* Callback URI must start with the client URI in your credential request parameters.</aside>

<div id="new-form" title="Create Client">
	<p class="validateTips"></p>
	<form>
		<fieldset>
			<div class="control-group">
				<label class="control-label" for="clientName">Name:</label>
				<div class="controls">
					<input type="text" name="clientName" id="clientName" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="clientURI">URI:</label>
				<div class="controls">
					<input type="text" name="clientURI" id="clientURI" />
				</div>
			</div>
		</fieldset>
	</form>
</div>
</body>
</html:html>
