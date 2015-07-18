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
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html ng-app="k2aConfig">
<head>
	<title><bean:message key="admin.admin.Know2ActConfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/k2aServices.js"></script>	
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div ng-controller="k2aConfig">
		<div class="page-header">
			<h4><bean:message key="admin.admin.Know2ActConfig"/></h4>
		</div>
	 	<div data-ng-show="k2aActive">
	  		<bean:message key="admin.k2a.active"/>
	 	</div>
		<div data-ng-hide="k2aActive">
			<form action="Know2actConfiguration.jsp"  method="POST">
				<fieldset>
					<div class="form-group col-xs-5">
						<label><bean:message key="admin.k2a.clinicName"/><small>(<bean:message key="admin.k2a.clinicName.reason"/>)</small></label>
						<div class="controls">
							<input class="form-control" name="clinicName" ng-model="clinicName" type="text" maxlength="255"/>  <br/>
						</div>
						<input type="button" class="btn btn-primary" value="<bean:message key="admin.k2a.initbtn"/>"  ng-click="initK2A()"/>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
	<script>
		var app = angular.module("k2aConfig", ['k2aServices']);
		
		app.controller("k2aConfig", function($scope,k2aService) {
			checkStatus = function(){
			    k2aService.isK2AInit().then(function(data){
			    	console.log("data coming back",data);
			    	$scope.k2aActive = data.success;
			    	console.log($scope.k2aActive );
				});
			}
		    checkStatus();
			
		    $scope.initK2A = function(){
		    	console.log($scope.clinicName);
		    	var clinic = {};
		    	clinic.name = $scope.clinicName;
		    	k2aService.initK2A(clinic).then(function(data){
		    		checkStatus();
		    	});
		    }   
		});
	
	</script>
</html>
