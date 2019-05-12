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

<body vlink="#0000FF" class="BodyStyle" ng-cloak>
	<div ng-controller="phrConfig" ng-cloak>
		<div class="page-header">
			<h4><bean:message key="admin.admin.phrconfig"/> <small data-ng-show="phrActive"> <bean:message key="admin.phr.active"/></small><small data-ng-show="serverOffline"> Connector Offline</small></h4>
		</div>
		
		<%--  div class="container"> --%>
			<div class="row">
  				<div class="col-md-12">
					<div>
						<button style="margin-left:3px;" ng-repeat="recc in audit.launchItems" type="button" class="btn btn-primary" ng-click="openPHRWindow(recc)">{{recc.heading}}</button>
					</div>
				</div>
			</div>	
			<div class="row">
  				<div class="col-md-9">	
					<div data-ng-hide="phrActive" class="jumbotron" ng-cloak>
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
		
					<div data-ng-hide="phrConsentConfigured || !phrActive" class="jumbotron" ng-cloak>
						<h2>PHR Clinic Configuration Wizard</h2>
						<div class="alert alert-warning" role="alert" data-ng-show="userpassError">Invalid Username and Password.</div>
						<form   method="POST">
							<fieldset>
								<h4>The PHR needs an OSCAR system user to interact with. This OSCAR user will show as the creator of appointments booked online.</h4>
								<div class="form-group col-xs-10">
									
								
									<h3>Set PHR User</h3>
										<div ng-if="selectUserMethod">
									 		<p>This will include: 
												 <ul> 
													 <li>creating a provider record with the name self-Book</li>
													 <li>creating a security record with a strong random user/password</li>
													 <li>creating consent type to record which patients are participating with using the PHR</li>
													 <li>And communicate this user to the PHR server for integration.</li>
												 </ul>
												<button type="button" class="btn btn-primary btn-block" ng-click="selectPHRUser()">Create a new User</button>
											<h3>Link an existing user</h3>
									 			<p>This will create the consentType to record which patients are participating with using the PHR. Communicating the user information to the PHR will need to be done manually.</p>
												<button type="button" class="btn btn-primary btn-block" ng-click="selectLinkExistingUser()">Link an existing user</button>
									</div>
									<div ng-if="showPHRUserCreate">
										<label>Provider No</label>
										<div class="controls">
											<input class="form-control" name="providerNo" ng-model="newProvider.providerNo" type="text" maxlength="6"/>  <br/>
										</div>
									
										<label>First Name</label>
										<div class="controls">
											<input class="form-control" name="firstName" ng-model="newProvider.firstName" type="text" maxlength="25"/>  <br/>
										</div>
									
										<label>Last Name</label>
										<div class="controls">
											<input class="form-control" name="lastName" ng-model="newProvider.lastName" type="text" maxlength="25"/>  <br/>
										</div>
										<label>Public OSCAR URL</label>
										<div class="controls">
											<input class="form-control" name="comments" ng-model="newProvider.comments" type="text" />  <br/>
										</div>
				
										<button type="button" class="btn btn-primary btn-block" ng-click="createPHRUser()">Create a new User</button>
									</div>
									<div ng-if="showPHRUserLink">
										<label>Provider No</label>
										<div class="controls">
											<select ng-model="newProvider.providerNo" class="form-control">
											  	<option ng-repeat="pro in activeProviders" value="{{pro.providerNo}}">{{pro.lastName}}, {{pro.firstName}} ({{pro.providerNo}})</option>
											</select>
										</div>
									
										<label>Password</label>
										<div class="controls">
											<input class="form-control" name="firstName" ng-model="newProvider.firstName" type="text" maxlength="25"/>  <br/>
										</div>
									
										<label>Public OSCAR URL</label>
										<div class="controls">
											<input class="form-control" name="comments" ng-model="newProvider.comments" type="text" />  <br/>
										</div>
										
										<button type="button" class="btn btn-primary btn-block" ng-click="linkExistingUser()">Link User</button>
									</div>
								</div>
							</fieldset>
						</form>
						
					</div>
					
					<%-- APPT CONFIG UI --%>
					<div  ng-show="currentSearchConfig != null">
		 		
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
					<%-- APPT CONFIG UI END --%>
					
					<div btf-markdown="audit.markdownText"></div>
				</div>
				<div class="col-md-3">
					
					<div  > <%-- </div>class="jumbotron" ng-if="!serverOffline && !audit.clinicInformationSetup">  --%>
					  <h3></h3>
					  <div ng-repeat="recc in audit.recommendations" class="alert alert-warning" role="alert" >
					  	<strong>{{recc.heading}}</strong> <br>{{recc.description}}<button ng-click="openPHRWindow(recc)"  class="btn btn-info btn-block" type="button">Configure</button>
					  </div>
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
			
			$scope.initButtonText = "Initialize";
			
			checkStatus = function(){
			    phrService.isPHRInit().then(function(data){
				    	console.log("data coming back",data);
				    	$scope.phrActive = data.success;
				    	console.log($scope.phrActive );
				    	
				    	if($scope.phrActive){
				    		//getAbilities();
				    		checkConsent();

				    		
				    		
				    		console.log("$scope.phrActive");	
				    	}
				});
			};
		    checkStatus();

		    checkConsent = function(){
		    	
			    	phrService.isPHRConsentCheck().then(function(data){
				    	console.log("data coming back",data);
				    	$scope.phrConsentConfigured = data.success;
				    	console.log($scope.phrConsent);
			    		getPhrSetupAudit();
			    		if($scope.phrConsentConfigured){
			    			getConsent(data.message);	
			    		}
			    		
				});
		    };
		    
		    
		    
		    getConsent = function(id){
		    		consentService.getConsentType(id).then(function(data){
		    			data.providerNo;
		    			getScheduleForProvider(data.providerNo); 
		    		});
		    }
		    
		    
		    
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
		    
		    $scope.selectPHRUser = function(){
		    		$scope.selectUserMethod = false;
		   		$scope.showPHRUserCreate = true;
				$scope.newProvider = {};
				
    				providerService.suggestProviderNo().then(function(data){
	    				console.log("suggestProviderNo",data);
    					$scope.newProvider.providerNo = data.message;
	    			
				});
				
				$scope.newProvider.firstName = "SelfBook"
				$scope.newProvider.lastName = "SelfBook";
				var location = $location.absUrl();
		    		var n = location.indexOf("/admin/PHRConfiguration.jsp");
		    		$scope.newProvider.comments = location.substring(0,n);
		    		
		    }		    
		    $scope.createPHRUser = function(){
		    		//-Just do it and then show report of what has happened
		    		console.log("$scope.newProvider",$scope.newProvider);
		    		phrService.createPHRuser($scope.newProvider).then(function(resp){
				    	console.log("createPHRuser coming back",resp);  
				    	alert(resp.message);
				    	$scope.showPHRUserCreate = false;
				    	checkStatus();
				});
		    	
		    }
		    
		    $scope.selectLinkExistingUser = function(){
		    		$scope.selectUserMethod = false;
		   		$scope.showPHRUserLink = true;
				$scope.newProvider = {};
				
				var location = $location.absUrl();
	    			var n = location.indexOf("/admin/PHRConfiguration.jsp");
	    			$scope.newProvider.comments = location.substring(0,n);
		    }
		    
			$scope.linkExistingUser = function(){
		    		//Show drop list to select user.
		    		console.log("$scope.newProvider",$scope.newProvider);
				phrService.createPHRuser($scope.newProvider).then(function(resp){
			    		console.log("createPHRuser coming back",resp);   
			    		alert(resp.message);
			    		$scope.showPHRUserCreate = false;
				    	checkStatus();  
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
		    
		    
		    /////APPT BOOKING
			$scope.currentSearchConfig = null;
			
		    $scope.oscarAppointmentTypes = [];
		    
		    
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
    			if($scope.currentSearchConfig.title == null){
    				alert("Name of clinic is required");
    				return
    			}
    			
    			scheduleService.saveSearchConfig($scope.currentSearchConfig.id,$scope.currentSearchConfig).then(function(data){
    			    console.log("saveSearchConfig ",data);
    			    alert("Online booking template has been saved");
    			    //$scope.currentId  = data.id;
    			    //getSearchConfigList();
    			    ///// commenting this one right now???$scope.openSearchConfig(data);
	    			//console.log("getTypes", activeProvidersHash); //data);
			});
    			
    		}

    		getScheduleForProvider = function(providerNo){
		    	scheduleService.getSearchConfigByProvider(providerNo).then(function(data){
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
		
		  };

	
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
	
</html>
