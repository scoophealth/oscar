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

<html ng-app="surveillanceConfig">
<head>
	<title><bean:message key="admin.admin.surveillanceConfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/surveillanceServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/providerServices.js"></script>	
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div ng-controller="surveillanceConfig">
		<div class="page-header">
			<h4><bean:message key="admin.admin.surveillanceConfig"/> <small data-ng-show="k2aActive"><bean:message key="admin.k2a.active"/></small></h4>
		</div>
		<div class="row">
		 	<div class="col-xs-3">
		 	
		 		<ul class="nav nav-tabs nav-justified">
  					<li role="presentation" ng-class="tabActive('local')"><a ng-click="setActiveTab('local')"><bean:message key="admin.surveillance.config.loaded"/></a></li>
  					<li role="presentation" ng-class="tabActive('k2a')"><a ng-click="setActiveTab('k2a')"><bean:message key="admin.surveillance.config.loadedk2aSurveillanceConfigs"/></a></li>
  					
				</ul>	
		 		<div class="tab-content">
    					<div role="tabpanel" class="tab-pane" ng-class="tabActive('local')" id="home">
    						<div class="list-group">
						  <a ng-click="openSurvey(surveyConfig)" class="list-group-item" ng-class="itemActive(surveyConfig.id)" data-ng-repeat="surveyConfig in loadedSurveillanceConfigs | limitTo:loadedSurveillanceConfigsQuantity" >
						  	
						  	<h4 class="list-group-item-heading"><span ng-if="surveyConfig.active" style="color:green" class="glyphicon glyphicon-ok " aria-hidden="true"></span> {{surveyConfig.name}}</h4>
							 <p class="list-group-item-text">{{surveyConfig.date | date:'medium'}}</p>
							 <br>
		  					<button ng-if="!surveyConfig.active" class="btn btn-default btn-sm" ng-click="enable(surveyConfig)">Enable</button>
		  					<button ng-if="surveyConfig.active" class="btn btn-default btn-sm " ng-click="disable(surveyConfig)">Disable</button>
		  					<button ng-if="hasUpdateAvailableFromK2A(surveyConfig.id)" class="btn btn-success btn-sm" ng-click="updateFromK2a(surveyConfig)">Update</button>
		  					
						
						    
						   
						  </a>
						</div>
    					</div>
    					<div role="tabpanel" class="tab-pane" ng-class="tabActive('k2a')" id="profile">
    						<a ng-click="openSurvey(surveyConfig)" class="list-group-item" <%-- ng-class="itemActive(surveyConfig.id)" --%> 
    						   data-ng-repeat="k2aSurvConfig in k2aSurveillanceConfigs | limitTo:loadedSurveillanceConfigsQuantity" >
						  	
						  	<h4 class="list-group-item-heading">{{k2aSurvConfig.name}}</h4>
							 <p class="list-group-item-text"><bean:message key="admin.surveillance.table.author"/>:{{k2aSurvConfig.author}}<br>
							 <span ng-if="k2aSurvConfig.resourceNew">update available</span><br>{{k2aSurvConfig.updated_at | date}}
							 </p>
							 <br>
		  					<button ng-if="canAdd(k2aSurvConfig)" class="btn btn-default btn-sm" ng-click="addSurveyFromK2A(k2aSurvConfig)"><bean:message key="admin.k2a.save"/></button>
		  					<button ng-if="canUpdate(k2aSurvConfig)" class="btn btn-default btn-sm" ng-click="updateSurveyFromK2A(k2aSurvConfig)"><bean:message key="admin.k2a.update"/></button>
		  		
						  </a>
    					</div>
  				</div>
		  		
		 	</div>
		 	<div class="col-xs-5" ng-show="currentSurveyId != null">
		 		<h4>{{survey.surveyTitle}} 
		 			<%-- a class="btn btn-primary" ng-click="updateSurvey(survey)" role="button">Save List</a> --%>
		 			
		 		</h4>
		 		<b>Question:</b>
		 		<pre>{{survey.surveyQuestion}}</pre> 
		 		<b>Randomness:</b>1 in {{survey.randomness}}<br>
		 		<b>Period:</b>{{survey.period}} Days <br>
		 		 
		 		<div class="col-xs-6">
		 			<h4>Participating Providers</h4>
			 		<ol style="padding:0;">
			 			<li ng-repeat="provider in survey.providerList" title="{{getFullProviderName(provider)}}">{{getProviderName(provider)}} <a ng-click="removeFromSurvey(survey,provider,$index)"><i class="icon-trash"></i></a></li>
			 		</ol>
			 		<a class="btn btn-primary" ng-click="updateSurvey(survey)" role="button">Save List</a>		
		 		</div>
		 		<div class="col-xs-6">
		 			<h4>Non-Participating Providers</h4>
			 		<ul style="padding:0;" class="list-unstyled">
			 			<li ng-repeat="provider in activeProviders" ng-if="hideIfParticipating(provider)"  title="{{getFullProviderName(provider)}}">
			 				<a ng-click="addProviderToSurvey(survey,provider)"><i class="icon-li icon-arrow-left"></i> {{getProviderName(provider.providerNo)}}</a>
			 			</li>
			 		</ul>
		 		</div>	 		
		 	</div>
		 	<div class="col-xs-4" ng-show="currentSurveyId != null">
		 	
		 	<h4>Automated Transmission</h4>
	 		<form ng-if="currentSurveyActive">
	 		  <div class="row">	
			  	<div class="form-group col-xs-6">
			    		<label for="exampleInputName2">Username</label>
					<input type="text" class="form-control" id="exampleInputName2" placeholder="username" ng-model="jobUsername">
			  	</div>
			  </div>
			  <div class="row">	
			  	<div class="form-group col-xs-6">
			    		<label for="exampleInputEmail2">Password</label>
			    		<input type="password" class="form-control" id="exampleInputEmail2" placeholder="password" ng-model="jobPassword">
			  	</div>
			  </div>
			  <div class="row">	
			  	<div class="form-group col-xs-6">
			    		<label for="exampleInputName3">Export Filename Prefix</label>
			    		<input type="text" class="form-control" id="prefixid" placeholder="013" ng-model="prefix">
			  	</div>
			  </div>
			  
			  <button type="submit" class="btn btn-default" ng-click="createJob(survey,jobUsername,jobPassword,prefix)" >Create Job</button>
			  
			</form>
		 	
		 	
		 	<h4>Files
		 	<a class="btn btn-default" ng-click="generateExport(survey)" role="button">Generate Export</a>
		 	</h4>

		 		
		 		<table class="table table-condensed table-stripedå"> 
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
		 		

		 	</div>
	 	</div>
	 	
	 	
		
	</div>
	
	<script>
		var app = angular.module("surveillanceConfig", ['surveillanceServices','providerServices']);
		
		app.controller("surveillanceConfig", function($scope,surveillanceService,providerService) {
		
			$scope.loadedSurveillanceConfigs = [];
			$scope.activeProviders = [];
			$scope.k2aSurveillanceConfigs = [];
			activeProvidersHash = {};
			$scope.loadedSurveillanceConfigsQuantity = 10;
			$scope.survey = {}; 
			$scope.updatableConfigs = {};
			$scope.exportFiles = []
		    firstLoad = true;
			$scope.currentSurveyActive = false;
			
			
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
	    		
	    		surveillanceConfigsFromK2A = function(){
	    			surveillanceService.surveillanceConfigList().then(function(data){
		    			$scope.k2aSurveillanceConfigs = data;
		    			angular.forEach($scope.k2aSurveillanceConfigs, function(config) {
		    				$scope.updatableConfigs[config.resourceId] = config;
		    			});
		    			console.log("loadedSurveillanceConfigs",data);
				});
	    		}; 
	    		surveillanceConfigsFromK2A();
	    		
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
	    		
	    		$scope.itemActive = function(id){
	    			if($scope.currentSurveyId === id){
	    				return "active";
	    			}
	    			return "";
	    		}
	    		
	    		$scope.openSurvey = function(resource){
	    			$scope.currentSurveyId = resource.id;
	    			$scope.currentSurveyActive = resource.active;
	    			surveillanceService.getSurvey(resource.id).then(function(data){
		    			$scope.survey = data;
		    			if(angular.isUndefined($scope.survey.providerList) || $scope.survey.providerList ==null ){
		    				$scope.survey.providerList = [];	
		    			}
		    			console.log("survey",data);
				});
	    			loadExportFiles(resource.id);
	    		}
	    		
	    		loadExportFiles = function(id){
	    			console.log("loadExportFiles",id);
	    			surveillanceService.getExportList(id).then(function(data){
		    			$scope.exportFiles = data;
		    			console.log("exportFiles",data);
				});
	    		}; 
		    
	    		$scope.activeTab = "local";
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
	    		
	    		$scope.hideIfParticipating = function(provider){
	    			if(angular.isUndefined($scope.survey.providerList)){
	    				return true;
	    			}
	    			//console.log("hidee",provider.providerNo,$scope.survey.providerList[provider.providerNo],$scope.survey.providerList);
	    			if($scope.survey.providerList.indexOf(provider.providerNo) > -1){
	    				return false;
	    			}
	    			return true;
	    		}
	    		
		    $scope.addProviderToSurvey = function(survey,provider){
		    		survey.providerList.push(provider.providerNo);
		    }
		    
		    $scope.removeFromSurvey = function(survey,provider,index){
		    		survey.providerList.splice(index, 1);
		    }
		    
		    $scope.updateSurvey = function(survey){
		    		surveillanceService.updateSurvey($scope.currentSurveyId,survey).then(function(data){
		    			$scope.survey = data;
		    			console.log("survey",data);
				});
		    }
		    
		    $scope.saveSurveillanceToK2A= function(k2aSurvConfig){
			    	surveillanceService.addSurveyFromK2A(k2aSurvConfig).then(function(data){
			    		allLoadedSurveillanceConfigs();
			    		surveillanceConfigsFromK2A();
				});
		    }
		    
			$scope.addSurveyFromK2A = function(prevSet){
		    	
			    	if(confirm("<bean:message key="admin.k2a.confirmation"/>")){
			    		console.log("prev",prevSet);
			    		prevSet.agreement = "<bean:message key="admin.k2a.confirmation"/>";
			    		surveillanceService.addSurveyFromK2A(prevSet).then(function(data){
					    	console.log("data coming back",data);
					    	allLoadedSurveillanceConfigs();    	
					});
			    	}
		    }; 
		    
		    $scope.updateSurveyFromK2A = function(prevSet){
		    	
		    	if(confirm("<bean:message key="admin.k2a.confirmation"/>")){
		    		console.log("prev",prevSet);
		    		prevSet.agreement = "<bean:message key="admin.k2a.confirmation"/>";
		    		surveillanceService.updateSurveyFromK2A(prevSet).then(function(data){
				    	console.log("data coming back",data);
				    	allLoadedSurveillanceConfigs();    	
				    	surveillanceConfigsFromK2A();
				});
		    	}
	    }; 
	    
	    $scope.updateFromK2a = function(preventionRuleSet){
	    		//console.log("updateFromK2a",preventionRuleSet);
	    		//console.log("$scope.updatableConfigs[preventionRuleSet.id]" ,$scope.updatableConfigs[preventionRuleSet.id]);
	    		$scope.updateSurveyFromK2A($scope.updatableConfigs[preventionRuleSet.id]);
	    }
	    
	    $scope.hasUpdateAvailableFromK2A = function(resourceId){
	    		if(angular.isDefined($scope.updatableConfigs[resourceId]) && $scope.updatableConfigs[resourceId].resourceNew){
	    			return true;
	    		}
	    		return false;
	    }
		    
		    $scope.enable = function(resource){
		    		console.log("going to enable ",resource);
		    		surveillanceService.enableResource(resource.id).then(function(data){
				    	console.log("data coming back",data);
				    	allLoadedSurveillanceConfigs();    	
				});
		    	
		    }
			$scope.disable = function(resource){
				surveillanceService.disableResource(resource.id).then(function(data){
			    		console.log("data coming back",data);
			    		allLoadedSurveillanceConfigs();    	
				});
			}
			
			$scope.generateExport = function(resource){
				console.log("what is this?",resource,$scope.currentSurveyId);
				surveillanceService.generateExport($scope.currentSurveyId).then(function(data){
		    			console.log("data coming back",data);
		    			loadExportFiles($scope.currentSurveyId);
				});
				
			}
			
			
			$scope.canAdd = function(k2aSurvConfig){
				//console.log("k2aSurvConfig",k2aSurvConfig);
				if(angular.isUndefined(k2aSurvConfig.resourceNew)){
					return true;
				}	
				return false;
			}

			$scope.canUpdate = function(k2aSurvConfig){
				//console.log("k2aSurvConfig",k2aSurvConfig);
				if(angular.isDefined(k2aSurvConfig.resourceNew) && k2aSurvConfig.resourceNew){
					return true;
				}	
				return false;
			}
			
			$scope.createJob = function(survey,jobUsername,jobPassword,prefix){
				console.log("survry",survey,jobUsername,jobPassword);
				obj = {};
				obj.username = jobUsername;
				obj.password = jobPassword;
				obj.prefix = prefix;
				surveillanceService.createJob($scope.currentSurveyId,obj).then(function(data){
    					console.log("data coming back",data);
    					alert("Job Saved. Job can be disabled in the Job Management admin menu");
					});
						
			},
		     
			$scope.setAsSent = function(exportFile){
				surveillanceService.setAsSent(exportFile.id).then(function(data){
	    				console.log("data coming back",data);
	    				loadExportFiles($scope.currentSurveyId);
	    				//Will need to call the method that lists files here.
				});
			}
			
			$scope.download = function(exportFile){
				window.open('../ws/rs/surveillance/download/'+exportFile.id,'_blank');
			}
			
		});
	
	</script>
	</body>
</html>	    			