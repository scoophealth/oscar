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

<html ng-app="phrConfig" ng-cloak>
<head>
	<title><bean:message key="admin.admin.phrconfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/phrServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/providerServices.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/showdown.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/markdown.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular-sanitize.min.js"></script>			
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div ng-controller="phrConfig">
		<div class="page-header">
			<h4><bean:message key="admin.admin.phrconfig"/> <small data-ng-show="phrActive"> <bean:message key="admin.phr.active"/></small><small data-ng-show="serverOffline"> Connector Offline</small></h4>
		</div>
		
		<div class="container">
		
			<div>
				<button style="margin-left:3px;" ng-repeat="recc in audit.launchItems" type="button" class="btn btn-primary" ng-click="openPHRWindow(recc)">{{recc.heading}}</button>
			</div>
		
			<div data-ng-hide="phrActive" class="jumbotron" >
				<h2>PHR Clinic Configuration Wizard</h2>
				<div class="alert alert-warning" role="alert" data-ng-show="userpassError">Invalid Username and Password.</div>
				<form   method="POST">
					<fieldset>
						<h4>Enter your PHR Clinic Credentials provided by your PHR Provider</h4>
						<div class="form-group col-xs-10">
							<label><bean:message key="admin.phr.clinicUsername"/><small> (This will be supplied by the PHR)</small></label>
							<div class="controls">
								<input class="form-control" name="clinicUsername" ng-model="clinicUsername" type="text" maxlength="255"/>  <br/>
							</div>
							<label><bean:message key="admin.phr.clinicPassword"/><small> (This will be supplied by the PHR)</small></label>
							<div class="controls">
								<input class="form-control" name="clinicPassword" ng-model="clinicPassword" type="password" maxlength="255"/>  <br/>
							</div>
							<button type="button" class="btn btn-primary" ng-disabled="clinicUsername==null || clinicUsername=='' || clinicPassword==null || clinicPassword=='' || working"  ng-click="initPHR()"/>{{initButtonText}}</button>
							   
						</div>
					</fieldset>
				</form>
				
			</div>
			<div class="jumbotron" ng-if="!audit.clinicInformationSetup">
			  <h3>PHR Clinic Configuration Wizard</h3>
			  <div ng-repeat="recc in audit.recommendations" class="alert alert-warning" role="alert" ><strong>{{recc.heading}}</strong> <br>{{recc.description}}<button ng-click="openPHRWindow(recc)"  class="btn btn-info pull-right" type="button">Configure</button></div>
			</div>
	
		
			<div btf-markdown="audit.markdownText"></div>
		
		</div>
	
	<script>
		var app = angular.module("phrConfig", ['phrServices','providerServices','btford.markdown']);
		
		app.controller("phrConfig", function($scope,$window,phrService,providerService) {
			
			$scope.serverOffline = false;
			$scope.activeProviders = [];
			$scope.userpassError = false;
			$scope.working = false;
			
			$scope.initButtonText = "Initialize";
			
			checkStatus = function(){
			    phrService.isPHRInit().then(function(data){
				    	console.log("data coming back",data);
				    	$scope.phrActive = data.success;
				    	console.log($scope.phrActive );
				    	
				    	if($scope.phrActive){
				    		//getAbilities();
				    		getPhrSetupAudit();
				    		
				    		console.log("$scope.phrActive");	
				    	}
				});
			}
		    checkStatus();
		    
		    getPhrSetupAudit = function(){  
		    		phrService.phrSetupAudit().then(function(resp){
				    	console.log("abilities coming back",resp);
				    	if(resp.status === 268){
				    		$scope.serverOffline = true;
				    		console.log("setting serverOffline to false ",$scope.serverOffline);
				    	}else{
				    		$scope.audit = resp.data;
				    		$scope.serverOffline = false;
				    	}
				    	console.log($scope.phrActive );
				    	
				    	if($scope.phrActive){
				    		console.log("$scope.phrActive");	
				    	}
				});
		    }
		    
		    $scope.openPHRWindow = function(recc){
		    		console.log("opening window for ",recc);
		    		$window.open('../ws/rs/app/openPHRWindow/'+recc.link);
		    };
		    
		    getAllActiveProviders = function(){
    			providerService.getAllActiveProviders().then(function(data){
		    			$scope.activeProviders = data;
		    			console.log("$scope.activeProviders",data);
		    			angular.forEach($scope.activeProviders, function(provider) {
		    				activeProvidersHash[provider.providerNo] = provider;
		    			});
		    			console.log("getAllActiveProviders", activeProvidersHash); //data);
				});
	    		};
    		
    			getAllActiveProviders();
		    
		    getAbilities = function(){  
			    	phrService.phrAbilities().then(function(resp){
				    	console.log("abilities coming back",resp);
				    	if(resp.status === 268){
				    		$scope.serverOffline = true;
				    		console.log("setting serverOffline to false ",$scope.serverOffline);
				    	}else{
				    		$scope.abilities = resp.data;
				    		$scope.serverOffline = false;
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
		    		$scope.userpassError = false;
		    		$scope.working = true;
			    	console.log($scope.clinicName);
			    	var clinic = {};
			    	$scope.initButtonText = "... Waiting";
			    	clinic.username = $scope.clinicUsername;
			    	clinic.password = $scope.clinicPassword;
			    	phrService.initPHR(clinic).then(function(data){
			    		$scope.working = false;
			    		$scope.initButtonText = "Initialize";
			    		if(data.success){
			    			checkStatus();
			    		}else{
				    		$scope.userpassError = true;
				    	}
			    		
			    	},function(){$scope.working = false;$scope.initButtonText = "Initialize";});
		    } 
		    
		    $scope.setOscarCreds = function(){
			    	console.log($scope.clinicName);
			    	var clinic = {};
			    	clinic.username = $scope.bookingProviderNo;
			    	clinic.password = $scope.bookingPassword;
			    	phrService.initOscarCreds(clinic).then(function(data){
			    		checkStatus();
			    	});
		    }
		});
	
	</script>
</html>
