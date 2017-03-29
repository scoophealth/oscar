
var oscarApp = angular.module('oscarProviderViewModule', ['ui.router','ngResource','ui.bootstrap','demographicServices','programServices',
														  'ticklerServices','scheduleServices','billingServices','securityServices','staticDataServices',
														  'patientDetailStatusServices','formServices','providerServices','noteServices','infinite-scroll',
														  'uxServices','ngTable','oscarFilters','messageServices','inboxServices','k2aServices', 'personaServices'
														  ,'consultServices','appServices','systemInfoServices','diseaseRegistryServices','angular-loading-bar','rxServices']);


oscarApp.config(['$stateProvider', '$urlRouterProvider',function($stateProvider, $urlRouterProvider) {
	  //
	  // For any unmatched url, redirect to /state1
	  $urlRouterProvider.otherwise("/dashboard");
	  //
	  // Now set up the states
	  $stateProvider
	  	.state('dashboardManager', {
		  url: '/dashboard/admin',
		  templateUrl: 'dashboard/admin/DashboardManager.jsp',
		  controller: 'dashboardManagerController'
		})
		.state('dashboard', {
		  url: '/dashboard',
		  templateUrl: 'dashboard/dashboard.jsp',
		  controller: 'DashboardCtrl'
		})
		.state('inbox', {
		  url: '/inbox',
		  templateUrl: 'inbox/inbox_popup.jsp',
		  controller: 'InboxCtrl'
		})
  		.state('consultRequests', {
  			url: '/consults',
  			templateUrl: 'consults/consultRequestList.jsp',
  			controller: 'ConsultRequestListCtrl'
		})
  		.state('consultResponses', {
  			url: '/consultResponses',
  			templateUrl: 'consults/consultResponseList.jsp',
  			controller: 'ConsultResponseListCtrl'
		})
  		.state('billing', {
  		  url: '/billing',
  		  templateUrl: 'billing/billing_popup.jsp',
			controller: 'BillingCtrl'
  		})
  		.state('schedule', {
  		  url: '/schedule',
  		  templateUrl: 'schedule/classic.jsp',
			controller: 'ScheduleCtrl'
  		})
  		.state('admin', {
  		  url: '/admin',
  		  templateUrl: 'admin/admin_popup.jsp',
			controller: 'AdminCtrl'
  		})
  		.state('ticklers', {
  		  url: '/ticklers',
  		  templateUrl: 'tickler/ticklerList.jsp',
			controller: 'TicklerListCtrl',
			resolve: { 
				providers: function(providerService) { return providerService.searchProviders({active:true}); },
			}
  		})
  		.state('search', {
  		  url: '/search',
  		  templateUrl: 'patientsearch/patientSearch.jsp',
			controller: 'PatientSearchCtrl'
  		}) 
  		.state('reports', {
  		  url: '/reports',
  		  templateUrl: 'report/reports.jsp',
  		  //templateUrl: 'report/reports_classic.jsp',
			controller: 'ReportsCtrl'
  		}) 
  		.state('documents', {
  		  url: '/documents',
  		  templateUrl: 'document/documents_classic.jsp',
			controller: 'DocumentsCtrl'
  		})
  		.state('clinicalconnect', {
  		  url: '/clinicalconnect',
  		  templateUrl: 'clinicalconnect/clinicalconnect.jsp',
			controller: 'ClinicalConnectCtrl'
  		})
  		.state('settings', {
  		  url: '/settings',
  		  templateUrl: 'settings/settings.jsp',
			controller: 'SettingsCtrl',
			resolve: { 
				user: function(providerService) { return providerService.getMe(); },
				billingServiceTypes: function(billingService) { return billingService.getUniqueServiceTypes(); },
				providerList: function(providerService) { return providerService.searchProviders({'active':true});},
				loadedSettings: function(providerService) { return providerService.getSettings();},
				encounterForms: function(formService) { return formService.getAllEncounterForms();},
				eforms: function(formService) { return formService.getAllEForms();},
				teams: function(providerService) { return providerService.getActiveTeams();},
				groupNames: function(formService) { return formService.getGroupNames();},
				loadedApps: function(appService) { return appService.getApps();}
			}
  		})
		.state('support', {
  		  url: '/support',
  		  templateUrl: 'help/support.jsp',
			controller: 'SupportCtrl'
  		})
  		.state('help', {
  		  url: '/help',
  		  templateUrl: 'help/help.jsp',
			controller: 'HelpCtrl'
  		}) 
		.state('record', {
			url: '/record/:demographicNo', 
			templateUrl: 'record/record.jsp',
			controller: 'RecordCtrl',
			resolve: { 
				demo: function($stateParams, demographicService) { return demographicService.getDemographic($stateParams.demographicNo); },
				user: function(providerService) { return providerService.getMe(); }
			}
		})
		.state('record.details', {
			url: '/details', 
			templateUrl: 'record/details/details.jsp',
			controller: 'DetailsCtrl'
		})
		.state('record.summary', {
			url: '/summary?appointmentNo&encType', 
			templateUrl: 'record/summary/summary.jsp',
			controller: 'SummaryCtrl'
		})
		.state('record.forms', {
			url: '/forms', 
			templateUrl: 'record/forms/forms.jsp',
			controller: 'FormCtrl'
		}).state('record.forms.new', {
			url: '/:type/:id', 
			templateUrl: 'record/forms/forms.jsp',
			controller: 'FormCtrl'
		}).state('record.forms.existing', {
			url: '/:type/id/:id', 
			templateUrl: 'record/forms/forms.jsp',
			controller: 'FormCtrl'
		})
  		.state('record.consultRequests', {
  			url: '/consults',
  			templateUrl: 'consults/consultRequestList.jsp',
  			controller: 'ConsultRequestListCtrl'
		})
  		.state('record.consultResponses', {
  			url: '/consultResponses',
  			templateUrl: 'consults/consultResponseList.jsp',
  			controller: 'ConsultResponseListCtrl'
		})
		.state('record.consultRequest', {
			url: '/consult/:requestId',
			templateUrl: 'consults/consultRequest.jsp',
			controller: 'ConsultRequestCtrl',
			resolve: {
				consult: function($stateParams, consultService) { return consultService.getRequest($stateParams.requestId, $stateParams.demographicNo); },
				user: function(providerService) { return providerService.getMe(); }
			}
		})
		.state('record.consultResponse', {
			url: '/consultResponse/:responseId',
			templateUrl: 'consults/consultResponse.jsp',
			controller: 'ConsultResponseCtrl',
			resolve: {
				consult: function($stateParams, consultService) { return consultService.getResponse($stateParams.responseId, $stateParams.demographicNo); },
				user: function(providerService) { return providerService.getMe(); }
			}
		})
		.state('record.tickler', {
			url: '/tickler', 
			templateUrl: 'tickler/ticklerList.jsp',
			controller: 'TicklerListCtrl',
			resolve: { 
				providers: function(providerService) { return providerService.searchProviders({active:true}); }
			}
		}).state('record.tracker', {
			url: '/tracker', 
			templateUrl: 'record/tracker/tracker.jsp',
			controller: 'TrackerCtrl'
		})
		.state('record.rx', {
			url: '/rx',
			templateUrl: 'record/rx/rx.jsp',
			controller: 'RxCtrl'
		})
		.state('record.phr', {
			url: '/phr', 
			templateUrl: 'record/phr/phr.jsp',
			controller: 'PHRCtrl'
		})
	   
		
		
}]);

// For debugging purposes
/*
oscarApp.run( function($rootScope, $location) {
	
$rootScope.$on('$stateChangeStart',function(event, toState, toParams, fromState, fromParams){
	  console.log('$stateChangeStart to '+toState.to+'- fired when the transition begins. toState,toParams : \n',toState, toParams);
	});
	$rootScope.$on('$stateChangeError',function(event, toState, toParams, fromState, fromParams){
	  console.log('$stateChangeError - fired when an error occurs during transition.');
	  console.log(arguments);
	});
	$rootScope.$on('$stateChangeSuccess',function(event, toState, toParams, fromState, fromParams){
	  console.log('$stateChangeSuccess to '+toState.name+'- fired once the state transition is complete.');
	});
	// $rootScope.$on('$viewContentLoading',function(event, viewConfig){
	//   // runs on individual scopes, so putting it in "run" doesn't work.
	//   console.log('$viewContentLoading - view begins loading - dom not rendered',viewConfig);
	// });
	$rootScope.$on('$viewContentLoaded',function(event){
	  console.log('$viewContentLoaded - fired after dom rendered',event);
	});
	$rootScope.$on('$stateNotFound',function(event, unfoundState, fromState, fromParams){
	  console.log('$stateNotFound '+unfoundState.to+'  - fired when a state cannot be found by its name.');
	  console.log(unfoundState, fromState, fromParams);
	});

});
*/

	//We already have a limitTo filter built-in to angular,
	//let's make a startFrom filter
	oscarApp.filter('startFrom', function() {
	  return function(input, start) {
		  start = +start; //parse to int
		  // return input.slice(start);
		  return input;
	  }
	});

/*
 
 user: function($stateParams, UserService) {
	  return UserService.find($stateParams.id);
	},
oscarApp.config(['$routeProvider',
					function($routeProvider) {

						when('/settings', {
							templateUrl: 'partials/settings-classic.jsp',
							controller: 'SettingsCtrl'
						}).
						when('/support', {
							templateUrl: 'partials/support.jsp',
							controller: 'SupportCtrl'
						}).
						when('/help', {
							templateUrl: 'partials/help.jsp',
							controller: 'HelpCtrl'
						}).
						
						when('/messenger', {
							templateUrl: 'partials/messenger.jsp',
							controller: 'MessengerCtrl'
						}).
						when('/eform', {
							templateUrl: 'partials/eform.jsp',
							controller: 'EformFullCtrl'
						}).
						when('/eform2', {
							templateUrl: 'partials/eform2.jsp',
							controller: 'EformFull2Ctrl'
						}).
					   
					}
]);
*/



//for dev - just to keep the cache clear
/*
oscarApp.run(function($rootScope, $templateCache) {
	$rootScope.$on('$viewContentLoaded', function() {
		$templateCache.removeAll();
		console.log("onclick of tab");
	});
});


//reset the left nav back
oscarApp.run( function($rootScope, $location) {
	$rootScope.$on( "$routeChangeStart", function(event, next, current) {
		$("#left_pane").addClass("col-md-2");
		$("#left_pane").show();
		$("#right_pane").removeClass("col-md-12");
		$("#right_pane").addClass("col-md-10");
	});  
});

*/

oscarApp.filter('offset', function() {
	  return function(input, start) {
		 if(input == null) {
			 return 0;
		 }
		start = parseInt(start, 10);
		return input.slice(start);
	  };
	});



//this is for the patient list control. Tells us which template to load
oscarApp.factory('Navigation', function($rootScope) {
  return {
	location: '',
	
	load: function(msg) {
	  this.location = msg;
	}
  }
});

/*

angular.module('oscarProviderViewModule').directive('oscarSecurityShow', function ($animate, securityService) {
	  return function(scope, element, attr) {
			scope.$watch(attr.oscarSecurityShow, function ngShowWatchAction(value){
				//console.log('valuee='+value.toSource());
				
				if(value.objectName != null && value.privilege != null) {
					//securityService.hasHigh(value.objectName,value.privilege,null);
					console.log('gtg');
				}
				
			  //set value to true to show, or else hide
			  $animate[value ? 'removeClass' : 'addClass'](element, 'ng-hide');
			});
	  };
});
*/
