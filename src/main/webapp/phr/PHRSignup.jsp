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

<%
	if(!authed) {
		return;
	}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html ng-app="phrConfig" >
<head ng-cloak>
	<title><bean:message key="admin.admin.phrconfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
	<script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/ui-bootstrap-tpls-0.11.0.js"></script>
	<script src="<%=request.getContextPath() %>/web/common/phrServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/providerServices.js"></script>
	<script src="<%=request.getContextPath() %>/web/common/consentServices.js"></script>
	<script src="<%=request.getContextPath() %>/web/common/scheduleServices.js"></script>	
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/showdown.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/markdown.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular-sanitize.min.js"></script>			
</head>

<body vlink="#0000FF" class="BodyStyle" ng-cloak style="padding:15px;">
	<div ng-controller="phrConfig" ng-cloak>
		<div class="page-header">
			<h4>PHR Provider Sign up</h4>
		</div>
		
		<%--  div class="container"> --%>
				
			<div class="row">
  				<div class="col-md-9">	
					
		
					<div data-ng-hide="phrConsentConfigured || !phrActive"  ng-cloak>
						<h2>PHR Provider Sign up</h2>
						<div class="alert alert-warning" role="alert" data-ng-show="userpassError">Invalid Username and Password.</div>
						<form   method="POST">
							<fieldset>
								<div class="form-group col-xs-10">
									<label>First Name</label>
										<div class="controls">
											<input class="form-control" name="firstName" ng-model="newProvider.firstName" type="text" maxlength="25" />  <br/>
										</div>
									
										<label>Last Name</label>
										<div class="controls">
											<input class="form-control" name="comments" ng-model="newProvider.lastName" type="text" />  <br/>
										</div>
										
										<label>Email</label>
										<div class="controls">
											<input class="form-control" name="comments" ng-model="newProvider.email" type="text" />  <br/>
										</div>
										
										<button type="button" class="btn btn-primary btn-block" ng-click="createPHRAccount()" ng-disabled="working" >{{initButtonText}}</button>
								</div>
							</fieldset>
						</form>
						
					</div>
				</div>
				
			</div>
		<%-- /div> containrer --%>
	
	<script>
		var app = angular.module("phrConfig", ['phrServices','providerServices','btford.markdown','consentServices','scheduleServices','ui.bootstrap']);
		
		app.controller("phrConfig", function($scope,$window,phrService,providerService,consentService,scheduleService,$location,$modal) {
			
			$scope.serverOffline = false;
			$scope.activeProviders = [];
			activeProvidersHash = {};
			$scope.userpassError = false;
			$scope.working = false;
			$scope.phrConsentConfigured = false;
			$scope.showPHRUserCreate = false;
			$scope.selectUserMethod = true;
			
			$scope.initButtonText = "Register";
			$scope.newProvider = {};
			getMe = function(){
				providerService.getMe().then(function(resp){
					console.log("ME",resp);
					$scope.newProvider.firstName=resp.firstName;
					$scope.newProvider.lastName=resp.lastName;
					$scope.newProvider.email=resp.email;
				});
				
			}
			
			getMe();
			checkStatus = function(){
			    phrService.isPHRInit().then(function(data){
				    	console.log("data coming back",data);
				    	$scope.phrActive = data.success;
				    	console.log($scope.phrActive );
				    	
				    	if($scope.phrActive){
				    		//getAbilities();
				    		

				    		
				    		
				    		console.log("$scope.phrActive");	
				    	}
				});
			};
		    checkStatus();
		    
		   	    
		    $scope.createPHRAccount = function(){
		    		//-Just do it and then show report of what has happened
		    		$scope.working = true;
		    		$scope.initButtonText = "...working";
		    		console.log("$scope.newProvider",$scope.newProvider);
		    		phrService.createProviderPHRuser($scope.newProvider).then(function(resp){
				    	console.log("createPHRuser coming back",resp);  
				    
				    	window.opener.refresh();
				    	alert(resp.message);
				    	window.location = "PhrMessage.do?method=viewMessages";
				    	$scope.initButtonText="Registered"
				    	$scope.showPHRUserCreate = false;
				    	checkStatus();
				});
		    	
		    }

});
	</script>
	
</html>
