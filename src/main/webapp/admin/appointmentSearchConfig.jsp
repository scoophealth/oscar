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

<html ng-app="appointmentSearchConfig">
<head>
	<title><bean:message key="admin.admin.surveillanceConfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
	<script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/ui-bootstrap-tpls-0.11.0.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/scheduleServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/providerServices.js"></script>	
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div ng-controller="appointmentSearchConfig">
		<div class="page-header">
			
			<h4><bean:message key="admin.admin.appointmentSearchConfig"/> <button  class="btn btn-default " style="margin-left=3px;" ng-click="createNewSearchConfig();">New Config</button></h4>
		</div>
		<div class="row">
		 	<div class="col-xs-3">
		 	
		 		<ul class="nav nav-tabs nav-justified">
  					<li role="presentation" ng-class="sideTabActive('online')"><a ng-click="setActiveSideTab('online')">Active</a></li>
  					<li role="presentation" ng-class="sideTabActive('offline')"><a ng-click="setActiveSideTab('offline')">Archived</a></li>
  					
				</ul>	
		 		<div class="tab-content">
    					<div role="tabpanel" class="tab-pane" ng-class="sideTabActive('online')" id="home">
    						<div class="list-group">
						  <a ng-click="openSearchConfig(searchConfig)" ng-if="searchConfig.active" class="list-group-item" ng-class="itemActive(searchConfig.id)" data-ng-repeat="searchConfig in searchConfigList " >  <%--| limitTo:loadedSurveillanceConfigsQuantity --%>
						  	
						  	<h4 class="list-group-item-heading">{{searchConfig.id}}:{{searchConfig.searchName}}</h4>
							 <p class="list-group-item-text">Create :{{searchConfig.createDate | date:'medium'}}</p>
							 <p class="list-group-item-text">Update :{{searchConfig.updateDate | date:'medium'}}</p>
							 <p class="list-group-item-text">Provider : {{getProviderName(searchConfig.providerNo)}}</p>
							 <p class="list-group-item-text">Type: {{searchConfig.searchType}}  </p>
							 <br>
		  					<button ng-if="searchConfig.active" class="btn btn-default btn-sm " ng-click="disableSearchConfig(searchConfig.id);$event.stopPropagation();">Disable</button>
						  </a>
						</div>
    					</div>
    					<div role="tabpanel" class="tab-pane" ng-class="sideTabActive('offline')" id="profile">
    						<div class="list-group">
						  <a ng-click="openSearchConfig(searchConfig)" ng-if="!searchConfig.active"  class="list-group-item" ng-class="itemActive(searchConfig.id)" data-ng-repeat="searchConfig in searchConfigList | limitTo:loadedSurveillanceConfigsQuantity" >
						  	
						  	<h4 class="list-group-item-heading">{{searchConfig.id}}:{{searchConfig.searchName}}</h4>
							 <p class="list-group-item-text">Create :{{searchConfig.createDate | date:'medium'}}</p>
							 <p class="list-group-item-text">Update :{{searchConfig.updateDate | date:'medium'}}</p>
							 <p class="list-group-item-text">Provider : {{getProviderName(searchConfig.providerNo)}}</p>
							 <p class="list-group-item-text">Type: {{searchConfig.searchType}}</p>
							 <br>
		  					<button ng-if="!searchConfig.active" class="btn btn-default btn-sm" ng-click="enableSearchConfig(searchConfig.id);$event.stopPropagation();">Enable</button>
						  </a>
						</div>
    					</div>
  				</div>
		  		
		 	</div>
		 	<div class="col-xs-9" ng-show="currentSearchConfig == null">
		 		
		 		<div class="jumbotron">
				  <h2>New Configuration</h2>				
				  <form>
					  <div class="form-group">
					    <label for="searchNameText">Search Name</label>
					    <input type="text" class="form-control" id="exampleInputEmail1" placeholder="Describe Search" ng-model="new.searchName">
					  </div>
					  <div class="form-group">
					    <label for="searchTypeSelect">Search Type <small>(online only available currently)</small></label>
					    <select class="form-control" ng-model="new.searchType">
					    		<option value="online">Online</option>
					    </select>
					  </div>
					  <div class="form-group">
					    <label for="providerSelect">Provider <small>(link to one specific provider)</small></label>
					    <select class="form-control" ng-model="new.providerNo">
					    		<option value="{{prov.providerNo}}" ng-repeat="prov in activeProviders">{{prov.lastName}},  {{prov.firstName}}  ({{prov.providerNo}})</option>
					    </select>
					  </div>
					  
					  <button  class="btn btn-default" ng-click="addSearchConfig()">Submit</button>
					</form>	  
				</div>
		 		
		 	</div>
		 	<div class="col-xs-9" ng-show="currentSearchConfig != null">
		 		<h4>{{currentApptSearch.searchName}} - {{getProviderName(currentApptSearch.providerNo)}} - {{currentApptSearch.searchType}}</h4>
		 		<div class="form-group">
				    <label for="ClinicTitleId">Booking Title</label>
				    <input type="text" class="form-control" id="ClinicTitleId" placeholder="Name of Clinic" ng-model="currentSearchConfig.title">
				  </div>
		 		<ul class="nav nav-tabs nav-justified">
  					<li role="presentation" ng-class="tabActive('main')"><a ng-click="setActiveTab('main')"><bean:message key="admin.appointmentSearchConfig.main"/></a></li>
  					<li role="presentation" ng-class="tabActive('codes')"><a ng-click="setActiveTab('codes')"><bean:message key="admin.appointmentSearchConfig.apptCodes"/></a></li>
  					<li role="presentation" ng-class="tabActive('types')"><a ng-click="setActiveTab('types')"><bean:message key="admin.appointmentSearchConfig.apptTypes"/></a></li>
  					
				</ul>	
		 		<div class="tab-content">
    					<div role="tabpanel" class="tab-pane" ng-class="tabActive('main')" id="home">
    						<table class="table table-bordered table-striped table-hover">
				            <tr>
				                <th><bean:message key="admin.appointmentSearchConfig.provider"/></th>
				                <th><bean:message key="admin.appointmentSearchConfig.team"/></th>
				                <td ng-repeat="appt in currentSearchConfig.bookingAppointmentTypes"><small>{{appt.name}}</small></td>
				            </tr>
				            <tr ng-repeat-start="provider in currentSearchConfig.bookingProviders"> 
				                <td><a ng-click="viewProvider(provider,$event)" class="clickable">{{getProviderName(provider.providerNo)}}</a>
				                    <md-icon class="glyphicon glyphicon-trash" aria-label="Trash" ng-click="removeProvider(provider, currentSearchConfig.bookingProviders, $index, $event)"></md-icon>
				                </td>
				                <td>
				                    <%-- button class="btn btn-default" aria-label="Add Appointment Code"  ng-click="cnf.openAddTeamMemberDialog(provider, $event)" style="padding:0px; margin:0px;"><bean:message key="admin.appointmentSearchConfig.add"/></button> --%>
				                    <div class="btn-group">
									  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									    <bean:message key="admin.appointmentSearchConfig.add"/> <span class="caret"></span>
									  </button>
									  <ul class="dropdown-menu">
									    <li ng-repeat="prov in  activeProviders"><a ng-click="addTeamMember(provider,prov)">{{prov.lastName+", "+prov.firstName}}</a></li>
									  </ul>
									</div>
				                </td>
				                <td ng-repeat="appt in currentSearchConfig.bookingAppointmentTypes" ng-click="editApptTypeForProvider(appt,provider,$event)" style="cursor: pointer;"><span><i ng-class="{'glyphicon glyphicon-ok' : checkAppt(provider,appt) }" ></i><small> {{listAppt(provider,appt) | blankFilter:checkAppt(provider,appt)}}</small></span></td>
				            </tr>
				            <tr ng-repeat="teamProvider in provider.teamMembers" ng-repeat-end>
				                <td>&nbsp;</td>
				                <td><small><a ng-click="viewProvider(teamProvider, $event)" class="clickable">{{getProviderName(teamProvider.providerNo)}}</a> <md-icon class="glyphicon glyphicon-trash" aria-label="Trash" ng-click="removeProvider(teamProvider,provider.teamMembers,$index,$event)" ></md-icon></small></td>
				                <td ng-repeat="appt in currentSearchConfig.bookingAppointmentTypes" ng-click="editApptTypeForProvider(appt,teamProvider,$event)" style="cursor: pointer;"><span><i ng-class="{'glyphicon glyphicon-ok' : checkAppt(teamProvider,appt) }" ></i><small> {{listAppt(teamProvider,appt) | blankFilter:checkAppt(teamProvider,appt)}}</small></span></td>
				            </tr>
				            <tr>
				                <td>
				                		<div class="btn-group">
									  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									    <bean:message key="admin.appointmentSearchConfig.add"/> <span class="caret"></span>
									  </button>
									  <ul class="dropdown-menu">
									    <li ng-repeat="prov in  activeProviders"><a ng-click="addProvider(prov)">{{prov.lastName+", "+prov.firstName}}</a></li>
									  </ul>
									</div>
				                </td>
				            </tr>
				        </table>	 
    					</div>
    					<div role="tabpanel" class="tab-pane" ng-class="tabActive('types')" id="profile">
    						<h2>Appointment Types</h2>
			            <div class="row">
			                <div class="col-xs-6">
			                		<a ng-click="openSurvey(surveyConfig)" class="list-group-item" <%-- ng-class="itemActive(surveyConfig.id)" --%> 
		    						   data-ng-repeat="appointmentType in currentSearchConfig.bookingAppointmentTypes" >
								  	
								  	<h4 class="list-group-item-heading">{{appointmentType.name}}</h4>
								  	<%-- 
									 <p class="list-group-item-text">
									 	<a style="margin-right:5px;" ng-hide="$first" ng-click="mat.moveUp(appointmentType,$index)"><i class="fa fa-arrow-up"></i></a>
			                            <a style="margin-right:5px;" ng-hide="$last" ng-click="mat.moveDown(appointmentType,$index)"><i class="fa fa-arrow-down"></i></a>
			                            <a ng-click="mat.removeFromApptList(appointmentType,$index)"><i class="fa fa-trash"></i></a>
									 </p>
									 
									 <md-list ng-cloak>
			                        <md-list-item ng-repeat="appointmentType in currentSearchConfig.bookingAppointmentTypes">
			                            <p>{{appointmentType.name}}</p>
			                            <a style="margin-right:5px;" ng-hide="$first" ng-click="mat.moveUp(appointmentType,$index)"><i class="fa fa-arrow-up"></i></a>
			                            <a style="margin-right:5px;" ng-hide="$last" ng-click="mat.moveDown(appointmentType,$index)"><i class="fa fa-arrow-down"></i></a>
			                            <a ng-click="mat.removeFromApptList(appointmentType,$index)"><i class="fa fa-trash"></i></a>
			                        </md-list-item>
			                    </md-list>
									 
									 --%>
								  </a>
			                
			                
			                    
			                </div>
			                <div class="col-xs-6">
			                    <h2>Existing Types from oscar</h2>
			                    
			                    <a ng-click="openSurvey(surveyConfig)" class="list-group-item" <%-- ng-class="itemActive(surveyConfig.id)" --%> 
		    						   data-ng-repeat="appointmentType in oscarAppointmentTypes.types" >
								  	
								  	<h4 class="list-group-item-heading">{{appointmentType.name}} 
								  		
								  			<button class="btn btn-default btn-xs pull-right" aria-label="Add" ng-click="addApptTypeFromOSCAR(appointmentType)" <%-- ng-disabled="mat.isApptAlreadyAdded(apptType.id)" --%> class="md-raised md-primary"><bean:message key="admin.appointmentSearchConfig.add"/></button>
								  		
								  	</h4>
								</a>
			                    
			                    
			                   
			                    <h3>Custom</h3>
			                     <div class="form-group">
								    <label for="exampleInputEmail1">Appointment Type Label</label>
								    <input type="text" class="form-control" id="exampleInputEmail1" placeholder="Appt description user will see" ng-model="apptLabelDesc">
								 </div>
								 <button type="button" class="btn btn-default btn-xs" ng-click="addApptType(apptLabelDesc)"><bean:message key="admin.appointmentSearchConfig.add"/></button>
			                   
			                </div>
			            </div>
    					</div>
    					<div role="tabpanel" class="tab-pane" ng-class="tabActive('codes')" id="profile">
    						<h2>Appointment Codes</h2>
			            <div class="row">
			                <div class="col-xs-12">
			                    <table class="table table-bordered table-hover">
			                        <tr>
			                            <td>&nbsp;</td>
			                            <td>Appt Code Desc</td>
			                            <td>Book Code</td>
			                            <td>Open Access Code</td>
			                        </tr>
			                        <tr ng-repeat="appCode in oscarTemplateCodes">
			                            <td style="background-color:{{appCode.color}}">{{appCode.code}}</td>
			                            <td>{{appCode.description}} ({{appCode.duration}})</td>
			                            <td>
			                            		<button type="button" class="btn btn-success btn-xs" ng-if="isCodeEnabled(currentSearchConfig,appCode)"  ng-click="addOrRemoveFromCodeList(appCode)">Enabled</button>
  											<button type="button" class="btn btn-default btn-xs" ng-if="!isCodeEnabled(currentSearchConfig,appCode)" ng-click="addOrRemoveFromCodeList(appCode)">Disabled</button>
			                            		
			                            </td>
			                            <td>
			                            		<button type="button" class="btn btn-success btn-xs" ng-if="isOnlineBookingEnabled(currentSearchConfig,appCode)"  ng-click="addOrRemoveFromOnlineBookingCodeList(appCode)">Enabled</button>
  											<button type="button" class="btn btn-default btn-xs" ng-if="!isOnlineBookingEnabled(currentSearchConfig,appCode)" ng-click="addOrRemoveFromOnlineBookingCodeList(appCode)">Disabled</button>
			                            		
			                            
			                            		
			                            </td>
			                        </tr>
			                    </table>
			                
			                
			                
			                </div>
			            </div>
    					</div>
  				</div>
  				<button class="btn btn-primary" ng-click="saveSearchConfig()">Save</button>
		 		
		 				
		 	</div>
		 	
		 	
		 	<%-- div class="col-xs-4" ng-show="currentSurveyId != null">
		 	<h4>Files
		 	<a class="btn btn-default" ng-click="generateExport(survey)" role="button">Generate Export</a>
		 	</h4>

		 		
		 		<table class="table table-condensed table-striped"> 
		 			<thead> 
		 				<tr> 
		 					<th>Name</th> 
		 					<th>Created</th> 
		 					<th>Sent</th> 
						</tr> 
					</thead> 
		 			<tbody> 
		 				<tr ng-repeat="exportFile in exportFiles"> 
		 					<th scope="row"><a ng-click="download(exportFile)">{{exportFile.name}}<i class="icon-download-alt"></i></a></th> 
		 					<td>{{exportFile.createDate | date}}</td> 
		 					<td>
		 						<button type="button" ng-if="!exportFile.sent" class="btn btn-default btn-xs" ng-click="setAsSent(exportFile)">Set as Sent</button>
		 						<span ng-if="exportFile.sent">{{exportFile.transmissionDate | date}}</span>
		 					</td> 
		 				</tr> 
		 			</tbody> 
		 		</table>
		 		

		 	</div --%>
	 	</div>
	 	
	 	
		
	</div>
	<script type="text/ng-template" id="providerCopy.html">
		<div class="modal-header">
            <h3 class="modal-title" id="modal-title">Copy Provider Configuration :{{vp.getProviderName(vp.provider.providerNo)}}</h3>
        </div>
		<div class="modal-body" id="modal-body">
            <div class="md-dialog-content" id="dialogContentProviderCopy">
				<table class="table table-bordered table-striped table-hover">
	            		<tr>
	                		<td>Provider</td>
	                		<td>Team</td>
	                		<td>&nbsp;</td>
	                		<td ng-repeat="appt in vp.clinic.bookingAppointmentTypes"><small>{{appt.name}}</small></td>
	            		</tr>
	            		<tr ng-repeat-start="prov in vp.clinic.bookingProviders">
	                		<td><a class="clickable">{{vp.getProviderName(prov.providerNo)}}</a></td>
	                		<td>&nbsp;</td>
	                		<td>
							<button class="btn btn-primary" type="button" ng-click="vp.copyProvidersTemplate(prov)">Copy</button>
	                		</td>
	                		<td ng-repeat="appt in vp.clinic.bookingAppointmentTypes"><span><small>{{vp.listAppt(prov, appt)}}</small></span></td>
	            		</tr>
	            		<tr ng-repeat="teamProvider in prov.teamMembers" ng-repeat-end>
	                		<td>&nbsp;</td>
	                		<td><small><a class="clickable">{{vp.getProviderName(teamProvider.providerNo)}}</a></small></td>
	                		<td>
							<button class="btn btn-primary" type="button" ng-click="vp.copyProvidersTemplate(teamProvider)">Copy</button>
	                		</td>
	                		<td ng-repeat="appt in vp.clinic.bookingAppointmentTypes"><span><small>{{vp.listAppt(teamProvider,appt)}}</small></span></td>
	            		</tr>
	        		</table>
        		</div>
		</div>
	</script>
	
	<script type="text/ng-template" id="myModalContent.html">
        <div class="modal-header">
            <h3 class="modal-title" id="modal-title">Provider Configuration</h3>
        </div>
        <div class="modal-body" id="modal-body">
            <div class="md-dialog-content" id="dialogContentApptProvider">
            
            <div class="row">
                <div class="col-xs-6">

                    <table class="table table-bordered table striped" >
                        <tr>
                            <td>&nbsp;</td>
                            <td>Appt Code Desc.</td>
                            <td>Option for Booking</td>
                        </tr>
                        <tr ng-repeat="appCode in mpa.apptCodes">
                            <td style="background-color:{{appCode.color}}">{{appCode.code}}</td>
                            <td>{{appCode.description}} ({{appCode.duration}})</td>
                            <td>
								<div class="checkbox">
									<label>
										<input type="checkbox"  ng-model="appCode.accepting" aria-label="apptCodeAccepting">
									</label>
								</div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="col-xs-6">
                    <md-input-container class="md-block" flex-gt-xs>
                        <label>Appointment Duration (if it is different than the appt code)</label>
                        <input type="text" ng-model="mpa.durationOverride" />
                    </md-input-container>
                </div>
            </div>
        </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="saveManageApptProvider()">Save</button>
            <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
        </div>
    </script>
	
	
	<script>
		var app = angular.module("appointmentSearchConfig", ['scheduleServices','providerServices','ui.bootstrap']);
		
		app.controller("appointmentSearchConfig", function($scope,scheduleService,providerService,$modal) {
			$scope.loadedSurveillanceConfigsQuantity = 100;
			$scope.activeProviders = [];
			activeProvidersHash = {};
			$scope.oscarAppointmentTypes = [];
			
			$scope.currentId = null;
			$scope.currentSearchConfig = null;
			//$scope.currentSearchConfig.bookingProviders= [];
			//$scope.currentSearchConfig.bookingAppointmentTypes = [];
			//$scope.currentSearchConfig.appointmentCodeDurations = {};
			//$scope.currentSearchConfig.openAccessList = [];
			$scope.searchConfigList = [];
			
		    $scope.new = {};
			
			$scope.loadedSurveillanceConfigs = [];
			
			$scope.k2aSurveillanceConfigs = [];
			
			
			getSearchConfigList = function(){
	    			scheduleService.getSearchConfigList().then(function(data){
	    			    console.log("getSearchConfigList ",data);
	    			    $scope.searchConfigList = data;
	    			    //if($scope.currentId != null && $scope.currentId != 0){
	    			    	//$scope.openSearchConfig($scope.currentId);
	    			    //}
		    			//angular.forEach($scope.activeProviders, function(provider) {
		    			//	activeProvidersHash[provider.providerNo] = provider;
		    			//});
		    			//console.log("getTypes", activeProvidersHash); //data);
				});
	    		};
	    		
	    		getSearchConfigList();	
	    		
	    		$scope.disableSearchConfig = function(id){
	    			scheduleService.disableSearchConfig(id).then(function(data){
	    			    console.log("disable ",data);
	    			    
	    			    getSearchConfigList();
	    			    console.log("(id === $scope.currentId)",id,$scope.currentId,(id === $scope.currentId));
	    			    if(id === $scope.currentId){
	    			    		console.log("setting right side to null");
	    			    		$scope.currentId = null;
	    			    		$scope.currentApptSearch = null;
	    					$scope.currentSearchConfig = {};
	    					$scope.currentSearchConfig = null;
	    				}
	    			    
	    			    
	    			    //if($scope.currentId != null && $scope.currentId != 0){
	    			    	//$scope.openSearchConfig($scope.currentId);
	    			    //}
		    			//angular.forEach($scope.activeProviders, function(provider) {
		    			//	activeProvidersHash[provider.providerNo] = provider;
		    			//});
		    			//console.log("getTypes", activeProvidersHash); //data);
				});
	    		}
	    			
	    		$scope.enableSearchConfig = function(id){
	    			console.log("EBLED CALLED with id "+id);
	    			scheduleService.enableSearchConfig(id).then(function(data){
	    			    console.log("enable ",data);
	    			    getSearchConfigList();
	    			    $scope.openSearchConfig(data);
	    			    $scope.setActiveSideTab('online')
	    			    //if($scope.currentId != null && $scope.currentId != 0){
	    			    	//$scope.openSearchConfig($scope.currentId);
	    			    //}
		    			//angular.forEach($scope.activeProviders, function(provider) {
		    			//	activeProvidersHash[provider.providerNo] = provider;
		    			//});
		    			//console.log("getTypes", activeProvidersHash); //data);
				});
	    			
	    		}
	    		
	    		
			
			
	    	 	$scope.createNewSearchConfig = function(){
	    	 		$scope.currentId = null;
	    			$scope.currentSearchConfig = null;
	    	 		/*$scope.currentId = 0;
	    			$scope.currentSearchConfig = {};
	    			$scope.currentSearchConfig.bookingProviders= [];
	    			$scope.currentSearchConfig.bookingAppointmentTypes = [];
	    			$scope.currentSearchConfig.appointmentCodeDurations = {};
	    			$scope.currentSearchConfig.openAccessList = [];
	    			*/
	    	 	}
			
			getTypes = function(){
	    			scheduleService.getTypes().then(function(data){
	    			    console.log("getTypes ",data);
		    			$scope.oscarAppointmentTypes = data;
		    			//angular.forEach($scope.activeProviders, function(provider) {
		    			//	activeProvidersHash[provider.providerNo] = provider;
		    			//});
		    			//console.log("getTypes", activeProvidersHash); //data);
				});
	    		};
	    		
	    		getTypes();
	    		
	    		
	    		$scope.addSearchConfig = function(){
	    			scheduleService.addSearchConfig($scope.new).then(function(data){
	    			    console.log("addSearchConfig ",data);
	    			    //$scope.currentId  = data.id;
	    			    getSearchConfigList();
	    			    $scope.openSearchConfig(data);
		    			//console.log("getTypes", activeProvidersHash); //data);
				});
	    		} 
	    		
	    		getTemplateCodes = function(){
	    			scheduleService.getTemplateCodes().then(function(data){
	    			    console.log("getTemplateCodes ",data);
		    			$scope.oscarTemplateCodes = data;
		    			/* I dont think this is needed anymore
		    			angular.forEach($scope.oscarTemplateCodes, function(codes) {
		    				if($scope.currentSearchConfig.appointmentCodeDurations[codes.code] != null){
		    					codes.onlineBooking = true;
		    				}
		    				//activeProvidersHash[provider.providerNo] = provider;
		    			});
		    			*/
		    			//console.log("getTypes", activeProvidersHash); //data);
		    			console.log("getTemplateCodes2 ",$scope.currentSearchConfig);
	    			
				});
	    		};
	    		
	    		getTemplateCodes();
	    		
	    		$scope.isCodeEnabled = function(currentSearchConfig,appCode){
	    			if(hasCurrentSearchConfig() && $scope.currentSearchConfig.appointmentCodeDurations[appCode.code] != null){
    					return true;
    				}
	    			return false;
            }
			
	    		$scope.addOrRemoveFromCodeList = function(appCode){
	    			
	    			if($scope.currentSearchConfig.appointmentCodeDurations[appCode.code] != null){
    					delete $scope.currentSearchConfig.appointmentCodeDurations[appCode.code];
    				}else{
    					$scope.currentSearchConfig.appointmentCodeDurations[appCode.code] = appCode.duration;
    				}
	    			
	    			console.log("$scope.currentSearchConfig.appointmentCodeDurations",$scope.currentSearchConfig.appointmentCodeDurations);
	    		}
	    	
	    		hasCurrentSearchConfig = function(){
	    			if(angular.isDefined($scope.currentSearchConfig) && $scope.currentSearchConfig != null){
	    				return true;
	    			}
	    			return false;
	    		}
	    		
	    		$scope.isOnlineBookingEnabled = function(currentSearchConfig,appCode){
	    			if(hasCurrentSearchConfig() && angular.isDefined($scope.currentSearchConfig.openAccessList) && $scope.currentSearchConfig.openAccessList.indexOf(appCode.code) != -1){
    					return true;
    				}
	    			return false;
            }
	    		
			$scope.addOrRemoveFromOnlineBookingCodeList = function(appCode){
	    			
	    			if($scope.currentSearchConfig.openAccessList.indexOf(appCode.code) != -1){
	    				$scope.currentSearchConfig.openAccessList.splice($scope.currentSearchConfig.openAccessList.indexOf(appCode.code), 1);
    					//delete $scope.currentSearchConfig.openAccessList[appCode.code];
    				}else{
    					
    					$scope.currentSearchConfig.openAccessList.push(appCode.code);
    				}
	    			
	    			console.log("addOrRemoveFromOnlineBookingCodeList",$scope.currentSearchConfig.openAccessList,$scope.currentSearchConfig.openAccessList.indexOf(appCode.code),appCode.code);
	    		}
			
			getAllActiveProviders = function(){
	    			providerService.getAllActiveProviders().then(function(data){
	    			    console.log("allActive ",data);
		    			$scope.activeProviders = data;
		    			angular.forEach($scope.activeProviders, function(provider) {
		    				activeProvidersHash[provider.providerNo] = provider;
		    			});
		    			console.log("getAllActiveProviders", activeProvidersHash); //data);
				});
	    		};
	    		
	    		getAllActiveProviders();
	    		
	    		$scope.removeProvider = function(provider, providersList, idx, ev) {
	    			var r = confirm("Are you sure you want to remove this provider?");
	    			console.log("removeProvider",r,provider,idx,providersList);
	    			if (r == true) {
	    				  providersList.splice(idx, 1); 
	    			} 
	    		},
			
			
	    		$scope.addProvider = function(prov){
	    			
	    			var provider2Add = {};
		    	    provider2Add.providerNo = prov.providerNo;
		    	    provider2Add.lastName = prov.lastName;
		    	    provider2Add.firstName = prov.firstName;
		    	    provider2Add.teamMembers = [];
		    	    provider2Add.appointmentTypes = [];
		    	    provider2Add.appointmentDurations = {};

		    	    $scope.currentSearchConfig.bookingProviders.push(provider2Add);
	    			
	    		}
	    		
	    		
	    		$scope.checkAppt = function(provider, appt) {
	    			if(angular.isDefined(provider) && angular.isDefined(provider.appointmentTypes)) {
	    				for(var x = 0; x < provider.appointmentTypes.length; x++) {
	    					if(provider.appointmentTypes[x].id === appt.id){
	    						return true;
	    					}
	    				}
	    			}
	    			return false;
	    		}
	    		
	    		$scope.listAppt = function(provider, appt) {
	    			if(angular.isDefined(provider) && angular.isDefined(provider.appointmentTypes)) {
	    				for(var x = 0; x < provider.appointmentTypes.length; x++) {
	    					if(provider.appointmentTypes[x].id === appt.id) {
	    						return provider.appointmentTypes[x].codes.join();
	    					}
	    				}
	    			}
	    			return "";
	    		}
	    		
	    		$scope.addTeamMember =function(provider,prov){
	    			var provider2Add = {};
		    	    provider2Add.providerNo = prov.providerNo;
		    	    provider2Add.lastName = prov.lastName;
		    	    provider2Add.firstName = prov.firstName;
		    	    provider2Add.teamMembers = [];
		    	    provider2Add.appointmentTypes = [];
		    	    provider2Add.appointmentDurations = {};
		    	    provider.teamMembers.push(provider2Add);	    			
	    		}
	    		
	    		$scope.addApptType = function(description){
	    			console.log('adding d:'+description);
	    			
	    			var largestId = 0;
      	 		for (var i = 0; i < $scope.currentSearchConfig.bookingAppointmentTypes.length; i++) {
      	 			if ($scope.currentSearchConfig.bookingAppointmentTypes[i].id > largestId) {
      	 				largestId = $scope.currentSearchConfig.bookingAppointmentTypes[i].id;
      	 			}
      	 		}
      	 		largestId++;
	      	 		
	      	 	newApptType = { duration: 0, id: largestId, name: description };
	      	 	$scope.currentSearchConfig.bookingAppointmentTypes.push(newApptType);			      	    
	      	 	$scope.apptLabelDesc = "";
	      	 	console.log("now types ",$scope.currentSearchConfig.bookingAppointmentTypes);
	    		}
	    		
	    		$scope.addApptTypeFromOSCAR = function(appointmentType){
	    			$scope.addApptType(appointmentType.name);
	    		}
	    		
	    		
	    		$scope.saveSearchConfig = function(){
	    			 
	    			console.log("$scope.currentId",$scope.currentId);
	    			
	    			scheduleService.saveSearchConfig($scope.currentId,$scope.currentSearchConfig).then(function(data){
	    			    console.log("saveSearchConfig ",data);
	    			    //$scope.currentId  = data.id;
	    			    getSearchConfigList();
	    			    $scope.openSearchConfig(data);
		    			//console.log("getTypes", activeProvidersHash); //data);
				});
	    			
	    		}
	    		
	    		
	    		
	    		 
				
			/*
			allLoadedSurveillanceConfigs = function(){
				surveillanceService.allLoadedSurveillanceConfigs().then(function(data){
		    			$scope.loadedSurveillanceConfigs = data;
		    			console.log("loadedSurveillanceConfigs",data);
		    			if(firstLoad){
		    				firstLoad = false;
		    				for (var i = 0, len = $scope.loadedSurveillanceConfigs.length; i < len; i++) {
		    					config = $scope.loadedSurveillanceConfigs[i];
		    					if(config.active){
		    						$scope.openSurvey(config);
		    						i= len;
		    					}
		    					
			    				console.log("all:A ",config);
			    			}
		    			}
				});
	    		}; 
		    
	    		allLoadedSurveillanceConfigs();
	    		*/
	    		
	    		/*getAllActiveProviders = function(){
	    			providerService.getAllActiveProviders().then(function(data){
		    			$scope.activeProviders = data;
		    			angular.forEach($scope.activeProviders, function(provider) {
		    				activeProvidersHash[provider.providerNo] = provider;
		    			});
		    			console.log("getAllActiveProviders", activeProvidersHash); //data);
				});
	    		};
	    		
	    		getAllActiveProviders();
	    		*/
	    		
	    		
	    		
	    		$scope.itemActive = function(id){
	    			if($scope.currentId === id){
	    				return "active";
	    			}
	    			return "";
	    		}
	    		
	    		
	    		
	    		
		    
	    		$scope.activeTab = "main";
	    		$scope.tabActive = function(tab){
	    			if(tab === $scope.activeTab){
	    				return "active";
	    			}
	    			return "";
	    		}
	    		
	    		$scope.setActiveTab = function(tab){
	    			$scope.activeTab = tab;
	    			console.log("$scope.activeTab",$scope.activeTab);
	    		}
	    		
	    		$scope.activeSideTab = "online";
	    		$scope.sideTabActive = function(tab){
	    			if(tab === $scope.activeSideTab){
	    				return "active";
	    			}
	    			return "";
	    		}
	    		
	    		$scope.setActiveSideTab = function(tab){
	    			$scope.activeSideTab = tab;
	    			console.log("$scope.activeTab",$scope.activeSideTab);
	    		}
	    		
	    		$scope.getProviderName = function(providerNumber){
	    			provider = activeProvidersHash[providerNumber];
	    			if(provider == null){ return providerNumber+" N/A inactive"}
	    			return provider.lastName+", "+provider.firstName;
	    		}
	    		
	    		$scope.getFullProviderName = function(providerNumber){
	    			provider = activeProvidersHash[providerNumber];
	    			if(provider == null){ return providerNumber+" N/A inactive"}
	    			return provider.lastName+", "+provider.firstName+" ("+provider.providerNo+")";
	    		}
	    		
	    		
		    $scope.openSearchConfig = function(searchConfig){
		    		console.log("calling open Search Config")
		    		$scope.currentId = searchConfig.id;
		    		$scope.currentApptSearch = searchConfig;
		    		scheduleService.getSearchConfig($scope.currentId).then(function(data){
	    			    console.log("openSearchConfig/getSearchConfig ",data);
	    			    $scope.currentSearchConfig = data;
	    			    	
	    			    if($scope.currentSearchConfig == null || $scope.currentSearchConfig ===""){
	    			    		console.log("was blank");
	    			    		$scope.currentSearchConfig = {};	
	    			    }
	    			    if($scope.currentSearchConfig == null){
	    			    	    console.log("$scope.currentSearchConfig was null");
	    			    		$scope.currentSearchConfig = {};	
	    			    }
	    			    console.log("AAAAA",(!angular.isDefined($scope.currentSearchConfig.bookingProviders) || $scope.currentSearchConfig.bookingProviders == null));
	    			    if(!angular.isDefined($scope.currentSearchConfig.bookingProviders) || $scope.currentSearchConfig.bookingProviders == null){
	    			    	    console.log("initializing $scope.currentSearchConfig.bookingProviders");
		    				$scope.currentSearchConfig.bookingProviders= [];
	    			    }
	    			    console.log("$scope.currentSearchConfig 1",$scope.currentSearchConfig);
	    			    if(!angular.isDefined($scope.currentSearchConfig.bookingAppointmentTypes) || $scope.currentSearchConfig.bookingAppointmentTypes == null){
		    				$scope.currentSearchConfig.bookingAppointmentTypes = [];
	    			    }
	    			    console.log("$scope.currentSearchConfig 2",$scope.currentSearchConfig);
	    			    console.log("CCCCC",(!angular.isDefined($scope.currentSearchConfig.appointmentCodeDurations) || $scope.currentSearchConfig.appointmentCodeDurations == null));
	    			    if(!angular.isDefined($scope.currentSearchConfig.appointmentCodeDurations) || $scope.currentSearchConfig.appointmentCodeDurations == null){
	    			    		console.log("initializing $scope.currentSearchConfig.appointmentCodeDurations");
		    				$scope.currentSearchConfig.appointmentCodeDurations = {};
	    			    }
	    			    console.log("$scope.currentSearchConfig 3",$scope.currentSearchConfig);
	    			    if(!angular.isDefined($scope.currentSearchConfig.openAccessList) || $scope.currentSearchConfig.openAccessList == null){
	    			    		$scope.currentSearchConfig.openAccessList = [];
	    			    }
	    			    console.log("$scope.currentSearchConfig 4",$scope.currentSearchConfig);
		
	    			    
		    			//console.log("getTypes", activeProvidersHash); //data);
				});

		    }
		    
		   
			$scope.editApptTypeForProvider = function(appt,provider,$event){
	    			
	    			//alert("SEARCH ManageProviderApptDialogCtrl in clinic app for what to pass here in. The current clinic is wrong here.");
	    		    
	    		    var modalInstance = $modal.open({
	    		      
	    		      templateUrl: 'myModalContent.html',
	    		      controller: 'ModalInstanceCtrl',
	    		      controllerAs: 'mpa',
	    		      parent: angular.element(document.body),
	    		      size: 'lg',
	    		      appendTo: $event,
	    		      resolve: {
	    		    	  	
	    		    	  		provider: function () {
	    		          		return provider;
	    		        		},
	    		        		appt: function () {
	    		          		return appt;
	    		        		},
	    		        		apptCodes: function () {
	    		          		return $scope.oscarTemplateCodes;  // apptCodes: clinic.apptCodes
		    		        	},
		    		        	appointmentCodeDurations: function(){
		    		        		return $scope.currentSearchConfig.appointmentCodeDurations;
		    		        	}
	    		      }
	    		    });

	    		    modalInstance.result.then(function (selectedItem) {
	    		      selected = selectedItem;
	    		    }, function () {
	    		      console.log('Modal dismissed at: ' + new Date());
	    		    });
	    		  };
		    
	    		  
	    		  $scope.viewProvider = function(provider,$event){
	    			  var modalInstance = $modal.open({
		    		      
		    		      templateUrl: 'providerCopy.html',
		    		      controller: 'ViewProviderDialogCtrl',
		    		      controllerAs: 'ppa',
		    		      parent: angular.element(document.body),
		    		      size: 'lg',
		    		      appendTo: $event,
		    		      resolve: {
		    		    	  	
		    		    	  		provider: function () {
		    		          		return provider;
		    		        		},
		    		        		searchConfig: function () {
		    		          		return $scope.currentSearchConfig;
		    		        		},
		    		        		providerName : function(){
		    		        			return $scope.getProviderName;
		    		        		}
		    		      }
		    		    });

		    		    modalInstance.result.then(function (selectedItem) {
		    		      selected = selectedItem;
		    		    }, function () {
		    		      console.log('Modal dismissed at: ' + new Date());
		    		    });
	    		
	    		  }
		
			
		});
		
		app.controller('ViewProviderDialogCtrl',function ViewProviderDialogCtrl($scope,$modal,$modalInstance,provider,searchConfig,providerName){
			console.log("ViewProviderDialogCtrl",provider,searchConfig);
			$scope.vp = {};
			$scope.vp.getProviderName = providerName;
			$scope.vp.clinic = searchConfig;
			$scope.vp.copyProvidersTemplate = function(providerToCopy) {
				console.log("vpd ",provider,providerToCopy);
				provider.appointmentDurations = angular.copy(providerToCopy.appointmentDurations);
				provider.appointmentTypes = angular.copy(providerToCopy.appointmentTypes);
				$modalInstance.close(providerToCopy);	
			};
			$scope.vp.listAppt = function(prov, appt){
				if(angular.isDefined(prov) && angular.isDefined(prov.appointmentTypes)) {
			
					for(var x = 0; x < prov.appointmentTypes.length; x++) {
						if(prov.appointmentTypes[x].id === appt.id) {
							return prov.appointmentTypes[x].codes.join();
						}
					}
				}
				return "";
			}
		});
		
		app.controller('ModalInstanceCtrl', function ModalInstanceCtrl($scope, $modal, $modalInstance,provider,appt,apptCodes,appointmentCodeDurations){
			console.log("ModalInstanceCtrl",provider,appt,apptCodes);
			//console.log("mdic",mpa);
			$scope.mpa = {};
			$scope.mpa.provider = provider;
			$scope.mpa.appt = appt;
			$scope.mpa.apptCodes = [];
			console.log($scope.mpa);
			
			
			///////////////
			var ctrl = this;
		      //  	ctrl.apptCodes = [];
		        	ctrl.provider = provider;
		        	apptType = null;
		        	
		        	ctrl.getProviderName = function(prov) { return mainService.getProviderName(prov) };
		        	
		        	ctrl.providerHasApptType = function(apptId) {
		        		console.log("providerHasApptType",apptId);
		        	    for (i = 0; i < provider.appointmentTypes.length; i++) {
		        	        if (provider.appointmentTypes[i].id === apptId) {
		        	            apptType = provider.appointmentTypes[i];
		        	            if (angular.isDefined(apptType.duration) && isFinite(apptType.duration) && apptType.duration > 0) {
		        	            	$scope.mpa.durationOverride = apptType.duration;
		        	            }
		        	        }
		        	    }
		        	    
		        	}
		        	ctrl.providerHasApptType(appt.id);

		        	ctrl.getApptCodes = function() {
			        	for (i = 0; i < apptCodes.length; i++) {
			        		console.log("apptCodes",apptCodes[i]);
			        	    //if (apptCodes[i].onlineBooking) {
			        	    	if(appointmentCodeDurations[apptCodes[i].code] != null){
			        	        appointment = angular.copy(apptCodes[i]);
			        	        if (apptType != null && apptType.codes.indexOf(appointment.code) >= 0) {
			        	            appointment.accepting = true;
			        	            if (apptCodes[i].duration != apptType.duration && apptType.duration != "0") {
			        	                appointment.durationOverride = apptType.duration;
			        	            }
			        	        }
			        	        $scope.mpa.apptCodes.push(appointment);
			        	    }
			        	}
		        	}
		        	ctrl.getApptCodes();
		        	console.log("mpa2",$scope.mpa);

		        	$scope.saveManageApptProvider = function() {
		        	    var onlineAcceptingAppt = [];
		        	    for (i = 0; i < $scope.mpa.apptCodes.length; i++) {
		        	        if ($scope.mpa.apptCodes[i].accepting) onlineAcceptingAppt.push($scope.mpa.apptCodes[i].code);
		        	    }

		        	    if (apptType != null && onlineAcceptingAppt.length == 0) {
		        	        var indx = provider.appointmentTypes.indexOf(apptType);
		        	        provider.appointmentTypes.splice(indx, 1);
		        	    } else if (apptType != null) {
		        	        apptType.codes = onlineAcceptingAppt;
		        	        if ($scope.mpa.durationOverride != null && $scope.mpa.durationOverride != "" && isFinite($scope.mpa.durationOverride)) {
		        	            apptType.duration = $scope.mpa.durationOverride;
		        	        } 
		        	    } else if (onlineAcceptingAppt.length > 0) {
		        	        newApptType = { 
		        	        	codes: onlineAcceptingAppt,
		        	        	id: appt.id,
		        	        	name: appt.name
		        	        };

		        	      	if ($scope.mpa.durationOverride != null && $scope.mpa.durationOverride != "" && isFinite($scope.mpa.durationOverride)) {
		        	            newApptType.duration = $scope.mpa.durationOverride;
		        	        } 
		        	        ctrl.provider.appointmentTypes.push(newApptType);
		        	    }
		        	    $modalInstance.close(ctrl.provider);	
		        	   // $mdDialog.hide();
		        	}

				$scope.cancel = function(){
					
					$modalInstance.close(false);	
				}
			
			///////////////
		      
			//mpa = sConfig;
			
		});
	
		app.filter('blankFilter', function() {
			return function(input, input2)
			{
				if(input2){
					return (input);
				} else{
					return ('Select Appt Type');
				}
			};
		});
	</script>
	</body>
</html>	    			