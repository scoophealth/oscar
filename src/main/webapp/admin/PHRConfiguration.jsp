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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html ng-app="phrConfig">
<head>
	<title><bean:message key="admin.admin.phrconfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/phrServices.js"></script>	
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div ng-controller="phrConfig">
		<div class="page-header">
			<h4><bean:message key="admin.admin.phrconfig"/> <small data-ng-show="phrActive"> <bean:message key="admin.phr.active"/></small><small data-ng-show="serverOffline"> Connector Offline</small></h4>
		</div>
	 	<div data-ng-show="k2aActive">
	  		<h4><bean:message key="admin.k2a.preventionsListTitle"/> <small>{{currentPreventionRulesSet}}</small></h4>
	  		<table class="table table-bordered table-condensed">
	  			<tr>
	  				<th><bean:message key="admin.k2a.table.filename"/></th>
	  				<th><bean:message key="admin.k2a.table.dateCreated"/></th>
	  				<th><bean:message key="admin.k2a.table.createdBy"/></th>
	  				<th>&nbsp;</th>
	  			</tr>
	  			<tr data-ng-repeat="preventionRuleSet in availablePreventionRuleSets | limitTo:PrevListQuantity"> 
	  				<td>{{preventionRuleSet.name}}</td>
	  				<td>{{preventionRuleSet.created_at}}</td>
	  				<td>{{preventionRuleSet.author}}</td>
	  				<td><button class="btn btn-default btn-sm" ng-click="loadPreventionRuleById(preventionRuleSet)"><bean:message key="admin.k2a.load"/></button></td>
	  			</tr>
	  		</table>
	  		<button  class="btn btn-default btn-sm pull-right" ng-click="increasePrevListQuantity()"><bean:message key="admin.k2a.loadMore"/></button>
	  		
	  		
	 	</div>
	 	<div data-ng-show="k2aActive">
	  		<h4><bean:message key="admin.k2a.LUCodes"/> <small>{{currentLuCodesVersion}}</small></h4>
	  		<table class="table table-bordered table-condensed">
	  			<tr>
	  				<th><bean:message key="admin.k2a.table.luCodeFilename"/></th>
	  				<th><bean:message key="admin.k2a.table.dateCreated"/></th>
	  				<th><bean:message key="admin.k2a.table.createdBy"/></th>
	  				<th>&nbsp;</th>
	  			</tr>
	  			<tr data-ng-repeat="fileSet in availableLuCodes | limitTo:LUListQuantity"> 
	  				<td>{{fileSet.name}}</td>
	  				<td>{{fileSet.created_at}}</td>
	  				<td>{{fileSet.author}}</td>					
	  				<td><button class="btn btn-default btn-sm" ng-click="loadLuCodesById(fileSet)"><bean:message key="admin.k2a.load"/></button></td>
	  			</tr>
	  		</table>
	  		<button  class="btn btn-default btn-sm pull-right" ng-click="increaseLUListQuantity()"><bean:message key="admin.k2a.loadMore"/></button>
	  		
	  		
	 	</div>

	 	
	 	<div >
	 		<button ng-repeat="ability in abilities" class="btn btn-default" ng-click="launch(ability)">{{ability.label}}</button>
	 	</div>
	 	
		<div data-ng-hide="phrActive">
			<form action="Know2actConfiguration.jsp"  method="POST">
				<fieldset>
					<h3>Enter your PHR Clinic Credentials.</h3>
					<div class="form-group col-xs-5">
						<label><bean:message key="admin.phr.clinicUsername"/><small> (This will be supplied by the PHR)</small></label>
						<div class="controls">
							<input class="form-control" name="clinicUsername" ng-model="clinicUsername" type="text" maxlength="255"/>  <br/>
						</div>
						<label><bean:message key="admin.phr.clinicPassword"/><small> (This will be supplied by the PHR)</small></label>
						<div class="controls">
							<input class="form-control" name="clinicPassword" ng-model="clinicPassword" type="password" maxlength="255"/>  <br/>
						</div>
						<input type="button" class="btn btn-primary" ng-disabled="clinicUsername==null || clinicUsername=='' || clinicPassword==null || clinicPassword==''" value="<bean:message key="admin.phr.initbtn"/>"  ng-click="initPHR()"/>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
	
	<script>
		var app = angular.module("phrConfig", ['phrServices']);
		
		app.controller("phrConfig", function($scope,phrService) {
			
			$scope.serverOffline = false;
			
			checkStatus = function(){
			    phrService.isPHRInit().then(function(data){
				    	console.log("data coming back",data);
				    	$scope.phrActive = data.success;
				    	console.log($scope.phrActive );
				    	
				    	if($scope.phrActive){
				    		getAbilities();
				    		console.log("$scope.phrActive");	
				    	}
				});
			}
		    checkStatus();
		    
		    getAbilities = function(){  
			    	phrService.phrAbilities().then(function(resp){
				    	console.log("data coming back",resp);
				    	if(resp.status == 288){
				    		$scope.serverOffline = false;
				    		console.log("setting serverOffline to false ",$scope.serverOffline);
				    	}else{
				    		$scope.abilities = resp.data;
				    	}
				    	console.log($scope.phrActive );
				    	
				    	if($scope.phrActive){
				    		console.log("$scope.phrActive");	
				    	}
				});
		    }
		    
		    $scope.availablePreventionRuleSets = [];
		    $scope.currentPreventionRulesSet = "";
		    $scope.currentLuCodesVersion = "";
		    
		    $scope.availableLuCodes = [];
		    
		    $scope.PrevListQuantity = 10;
		    
		    $scope.increasePrevListQuantity =function(){
		    	$scope.PrevListQuantity = $scope.availablePreventionRuleSets.length;
		    }
		    
			$scope.LUListQuantity = 10;
		    
		    $scope.increaseLUListQuantity =function(){
		    	$scope.LUListQuantity = $scope.availableLuCodes.length;
		    }
		    
		    //
		    
			
		    $scope.initPHR = function(){
			    	console.log($scope.clinicName);
			    	var clinic = {};
			    	clinic.username = $scope.clinicUsername;
			    	clinic.password = $scope.clinicPassword;
			    	phrService.initPHR(clinic).then(function(data){
			    		checkStatus();
			    	});
		    }   
		});
	
	</script>
</html>
