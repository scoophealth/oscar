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
	<title>HRM Prefs - OSCAR EMR</title>

	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
 	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" /> 
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/hospitalReportManager/inbox.css" />
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js" ></script>
	<script>
	// table sorting
	$(document).ready(function(){
		loadConfidentialityStatement();
	});	
	
	function loadConfidentialityStatement() {	
		$.ajax({
			type:"GET",
			url:'../hospitalReportManager/hrm.do?method=getConfidentialityStatement',
			dataType:'json',
			async:true, 
			success:function(data) {
				$("#confidentialityStatement").val(data.value);
			}
		});
	}
	
	function saveConfidentialityStatement() {
		$.ajax({
			type:"POST",
			url:'../hospitalReportManager/hrm.do?method=saveConfidentialityStatement',
			data: {
				value: $("#confidentialityStatement").val()
			},
			dataType:'json',
			async:true, 
			success:function(data) {
				alert('Saved');
			}
		});	
	}
	
	function addToOutageList() {
		$.ajax({
			type:"POST",
			url:'../hospitalReportManager/hrm.do?method=addToOutageList',
			dataType:'json',
			async:true, 
			success:function(data) {
				alert('Saved');
			}
		});	
	}
	</script>
</head>
<body>
<div>
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
          <li><a href="inbox.jsp">HRM Inbox</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
	
	<div class="table-responsive" id="libraryTableContainer">
	
		<div class="col-sm-12">
		
				
				<div class="panel panel-default">
				  <div class="panel-heading">
				    <h3 class="panel-title">Confidentiality Statement</h3>
				  </div>
				  <div class="panel-body">
				  
				  	<table class="table">
				  		
					  	<tr>
					  		<td>
					  			<textarea class="form-control" id="confidentialityStatement" rows="10" style="width:60%"></textarea>
					  		</td>
					  		
					  	</tr>
					  	<tr>
					  		<td>
					  				<input type="button" value="Save" class="btn btn-primary" id="saveBtn" onClick="saveConfidentialityStatement()"/>			
					  		</td>
					  	</tr>
  					</table>
				  </div>
				</div>


				<div class="panel panel-default">
				  <div class="panel-heading">
				    <h3 class="panel-title">HRM Outage Recipient List</h3>
				  </div>
				  <div class="panel-body">
				  
					  <input type="button" value="I don't want to receive any more HRM outtage messages for this outtage instance" class="btn btn-default" id="saveBtn" onClick="addToOutageList()"/>		
				 
				  </div>
				</div>
				
			
		</div>
		
	
	
	</div>

</div>	
</div> <!-- end container -->
</body>
</html:html>
