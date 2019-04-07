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

<html ng-app="consentConfig">
<head>
	<title><bean:message key="admin.admin.consentConfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/surveillanceServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/providerServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/consentServices.js"></script>
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div ng-controller="consentConfig">
		<div class="page-header">
			<h4><bean:message key="admin.admin.consentConfig"/></h4>
		</div>
		
		
		<h3>Add Provider Specific Consent</h3>
		<form>
		
		<div class="row">
			
					
				  <div class="form-group col-xs-4">
				    <label for="exampleInputEmail1">Name</label>
				    <input type="text" class="form-control" id="exampleInputEmail1" ng-model="newConsent.name">
				  </div>
				  <div class="form-group col-xs-4" >
				    <label for="exampleInputPassword1">Provider</label>
				    <select class="form-control" ng-model="newConsent.providerNo">
							<option ng-repeat="provider in activeProviders" title="{{getFullProviderName(provider)}}" value="{{provider.providerNo}}">
				 				{{getProviderName(provider.providerNo)}}
					 		</option>
					</select>
				  </div>
	 	 </div>
		 <div class="row">
				  <div class="form-group col-xs-12">
				    <label for="exampleInputFile">Description</label>
				    <input type="text" class="form-control" ng-model="newConsent.description" >
				  </div>
		 </div>
		 <div class="row">
 			<div class="form-group col-xs-12"> 
				  <div class="checkbox">
				    <label>
				      <input type="checkbox" ng-model="newConsent.remoteEnabled"> Remote Enabled
				    </label>
				  </div>
				  <button type="submit" class="btn btn-default" ng-click="saveConsent(newConsent)">Save Consent Type</button>
			 </div>
		</div>
				
				
				
				<%--
				<form class="form-inline">
					  <div class="form-group">
					    <!--  label class="control-label" for="exampleInputEmail3">Provider</label -->
					    <select class="form-control" ng-model="newConsent.providerNo">
							<option ng-repeat="provider in activeProviders" title="{{getFullProviderName(provider)}}" value="{{provider.providerNo}}">
				 				{{getProviderName(provider.providerNo)}}
					 		</option>
						</select>
					  </div>
					  <div class="checkbox">
					    <label>
					      <input type="checkbox" ng-model="newConsent.remoteEnabled"> Remote Enabled
					    </label>
					  </div>
					  <button type="submit" class="btn btn-default" ng-click="saveConsent(newConsent)">Save Consent Type</button>
				</form>		 
				   
			</div>  
		</div>--%>
		</form>
		<div class="row">
		 	<div class="col-xs-12">
		 	<h4>Active Consent Types
		 
		 	</h4>
	 		<table class="table table-condensed table-stripedå"> 
	 			<thead> 
	 				<tr> 
	 					<th>id</th> 
	 					<th>type</th> 
	 					<th>name</th>
	 					<th>description</th>
	 					<th>Provider</th>
	 					<th>Remote Enabled</th> 
					</tr> 
				</thead> 
	 			<tbody> 
	 				<tr ng-repeat="cType in activeConsentTypes"> 
	 					<td>{{cType.id}}</td>
	 					<td>{{cType.type}}</td>
	 					<td>{{cType.name}}</td>
	 					<td>{{cType.description}}</td>
	 					<td>{{getFullProviderName(cType.providerNo)}}</td> 
	 					<td>
	 						<span ng-if="cType.remoteEnabled" style="color:green" class="glyphicon glyphicon-ok " aria-hidden="true"></span>
	 					</td>  
	 				</tr> 
	 			</tbody> 
	 		</table>
		 		

	 	</div>
		 	
		 	
		 	
		 	
	 	</div>
	 	
	 	
		
	</div>
	
	<script>
		var app = angular.module("consentConfig", ['surveillanceServices','providerServices','consentServices']);
		
		app.controller("consentConfig", function($scope,surveillanceService,providerService,consentService) {
		
			$scope.activeProviders = [];
			activeProvidersHash = {};
			$scope.activeConsentTypes = []
			$scope.newConsent = {};
		    firstLoad = true;
			
	    		getAllActiveProviders = function(){
	    			providerService.getAllActiveProviders().then(function(data){
		    			$scope.activeProviders = data;
		    			angular.forEach($scope.activeProviders, function(provider) {
		    				activeProvidersHash[provider.providerNo] = provider;
		    			});
		    			console.log("getAllActiveProviders", activeProvidersHash); //data);
				});
	    		};
	    		
	    		getAllActiveProviders();
	    		
	    		getConsentTypes = function(){
	    			consentService.getConsentTypes().then(function(data){
		    			$scope.activeConsentTypes = data.content;
		    			console.log("getConsentTypes", data); //data);
				});
	    		};
	    		
	    		getConsentTypes();
	    		
	    		loadExportFiles = function(id){
	    			console.log("loadExportFiles",id);
	    			surveillanceService.getExportList(id).then(function(data){
		    			$scope.exportFiles = data;
		    			console.log("exportFiles",data);
				});
	    		}; 
		    
	    		
	    		$scope.getProviderName = function(providerNumber){
	    			provider = activeProvidersHash[providerNumber];
	    			if(provider == null){ return providerNumber+" N/A inactive"}
	    			return provider.lastName+", "+provider.firstName;
	    		}
	    		
	    		$scope.getFullProviderName = function(providerNumber){
	    			provider = activeProvidersHash[providerNumber];
	    			if (providerNumber == null){
	    				return "No Provider #";
	    				
	    			}
	    			if(provider == null){ return providerNumber+" N/A inactive"}
	    			return provider.lastName+", "+provider.firstName+" ("+provider.providerNo+")";
	    		}
	
	    		$scope.saveConsent = function(newConsent){
	    			console.log("svat",newConsent);
	    			newConsent.type= "provider_consent_filter";
	    			consentService.saveConsentType(newConsent).then(function(data){
		    			
		    			console.log("saveConsentTypes", data); //data);
		    			getConsentTypes();
				});
	    		}
			
		});
	
	</script>
	</body>
</html>	    			