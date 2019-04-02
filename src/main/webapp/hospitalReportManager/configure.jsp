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
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.SecurityInfoManager"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	boolean isHrmAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm.administrator", "r", null);
	boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
	boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
%>


<!DOCTYPE html > 
<html:html locale="true" >
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>HRM Configuration - OSCAR EMR</title>

	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
 	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" /> 
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/hospitalReportManager/inbox.css" />
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js" ></script>
	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.ui.widget.js" ></script>
	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.fileupload.js" ></script>
	
	<script>
		$(document).ready(function() {
			loadValues();
			
			$("#saveBtn").click(function(){
				saveValues();
			});
			
			$('#private_key').fileupload({
		        dataType: 'json',
		        done: function (e, data) {
		        	loadValues();
		        }
		    });
		});
		
		function loadValues() {
			$.ajax({
				type:"GET",
				url:'../hospitalReportManager/hrm.do?method=getConfigurationDetails',
				dataType:'json',
				async:true, 
				success:function(data) {
					$("#hostname").val(data.hostname);
					$("#port").val(data.port);
					$("#username").val(data.username);
					$("#directory").val(data.remoteDirectory);
					$("#key").val(data.decryptionKey);
					$("#private_key_info_current_file").html(data.privateKeyFile);
				
					$('input:radio[name=polling_enable]').val([data.polling_enabled]);
					$("#polling_interval").val(data.polling_interval);
				}
			});	
		}
		
		function saveValues() {
			$.ajax({
				type:"POST",
				url:'../hospitalReportManager/hrm.do?method=saveConfigurationDetails',
				data: {
					hostname: $("#hostname").val(),
					port: $("#port").val(),
					username: $("#username").val(),
					remoteDirectory: $("#directory").val(),
					decryptionKey: $("#key").val(),
					polling_enabled: $("input:radio[name=polling_enable]:checked").val(),
					polling_interval: $("#polling_interval").val()
					
				},
				dataType:'json',
				async:true, 
				success:function(data) {
					loadValues();
				}
			});	
		}
	</script>
	
</head>
<body>
<div class="col-sm-12">
	
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <b>
          <a class="navbar-brand" href="#">Health Report Manager</a>
          </b>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            
          </ul>
          <ul class="nav navbar-nav navbar-right">
          	<li><a href="hrmShowMapping.jsp">Class Mappings</a></li>
          	<li><a href="hrmCategories.jsp">Categories</a></li>
          <li><a href="inbox.jsp">HRM Inbox</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
	
	<div class="table-responsive" id="libraryTableContainer">
	
	<%if(isAdmin) { %>
	<form>
	
		<div class="col-sm-12">
				
				<div class="panel panel-default">
				  <div class="panel-heading">
				    <h3 class="panel-title">Connection Details</h3>
				  </div>
				  <div class="panel-body">
				  
				  	<table class="table">
					  	<tr>
					  		<td>
					  			<label for="hostname">Hostname / IP: </label>
	  							<input type="text" class="form-control" id="hostname" placeholder="Hostname">
					  		</td>
					  		<td>
					  			<label for="port">Port: </label>
	  							<input type="text" class="form-control" id="port" placeholder="Port">
					  		</td>
					  	</tr>
					  	
					  	<tr>
					  		<td>
					  			<label for="username">Username: </label>
	  							<input type="text" class="form-control" id="username" placeholder="Username">
					  		</td>
					  		<td>
					  			<label for="directory">Remote Directory: </label>
	  							<input type="text" class="form-control" id="directory" placeholder="Remote Directory">
					  		</td>
					  	</tr>
					    
	  					<tr>
					  		<td>
					  			<label for="key">Decryption Key: </label>
	  							<input type="text" class="form-control" id="key" placeholder="Decryption Key">
					  		</td>
					  		
					  		
					  		<td>
					  			<label for="private_key">Private Key: </label>
					  			<input id="private_key" type="file" name="privateKeyFile" data-url="../hospitalReportManager/hrm.do?method=uploadPrivateKey" >
								<div id="private_key_info">
									<p><b>Current File:</b><span id="private_key_info_current_file" style="color:red">None</p>
								</div>
					  			<!-- 
	  							<input type="file" class="form-control" id="private_key" >
	  							&nbsp;
	  							<input type="button" class="btn" id="upload_private_key_btn" value="Upload Private Key" >
	  							-->
					  		</td>
					  		
					  		
					  	</tr>
  					</table>
				  </div>
				</div>


				<div class="panel panel-default">
				  <div class="panel-heading">
				    <h3 class="panel-title">Auto-polling</h3>
				  </div>
				  <div class="panel-body">
				  
				  	<table class="table">
					  	<tr>
					  		<td>
					  			<label for="polling_enable">Automated Polling:</label>
	  							<label class="radio-inline"><input type="radio" name="polling_enable" value="true" class="form-control">Enabled</label>
								<label class="radio-inline"><input type="radio" name="polling_enable" value="false" class="form-control">Disabled</label>
					  		</td>
					  	</tr>
					  	<tr>
					  	
					  		<td>
					  			<label for="polling_interval">Polling Interval (minutes): </label>
	  							<input type="text" class="form-control" id="polling_interval" placeholder="Polling Interval">
					  		</td>
					  	</tr>
					
  					</table>
				  </div>
				</div>		
				
				<input type="button" value="Save Changes" class="btn btn-primary" id="saveBtn"/>	
				
	</div>
	
	</form>
	<% } else { %>
	<div class="col-sm-12">
		<h3>You need to be an admin (_admin.hrm)</h3>
	</div>
	<% } %>
</div>	
</div> <!-- end container -->
</body>
</html:html>
