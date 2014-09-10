    
var oscarApp = angular.module('oscarProviderViewModule', ['ui.router','ngResource','ui.bootstrap','demographicServices','patientDetailStatusServices','formServices','providerServices','noteServices','infinite-scroll','uxServices']);


oscarApp.config(['$stateProvider', '$urlRouterProvider',function($stateProvider, $urlRouterProvider) {
	  //
	  // For any unmatched url, redirect to /state1
	  $urlRouterProvider.otherwise("/dashboard");
	  //
	  // Now set up the states
	  $stateProvider
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
        .state('consults', {
  	      url: '/consults',
  	      templateUrl: 'consults/consultList.jsp',
            controller: 'ConsultListCtrl'
  	    })  
  	    .state('billing', {
  	      url: '/billing',
  	      templateUrl: 'billing/billing_popup.jsp',
            controller: 'BillingCtrl'
  	    })  
  	    .state('admin', {
  	      url: '/admin',
  	      templateUrl: 'admin/admin_popup.jsp',
            controller: 'AdminCtrl'
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
	    	url: '^/record/:demographicNo/details', 
            templateUrl: 'record/details/details.jsp',
            controller: 'DetailsCtrl'
        })
        .state('record.summary', {
	    	url: '^/record/:demographicNo/summary?appointmentNo&encType', 
            templateUrl: 'record/summary/summary.jsp',
            controller: 'SummaryCtrl'
        })
        .state('record.forms', {
	    	url: '^/record/:demographicNo/forms', 
            templateUrl: 'record/forms/forms.jsp',
            controller: 'FormCtrl'
        }).state('record.forms.new', {
	    	url: '^/record/:demographicNo/forms/:type/:id', 
            templateUrl: 'record/forms/forms.jsp',
            controller: 'FormCtrl'
        }).state('record.forms.existing', {
	    	url: '^/record/:demographicNo/forms/:type/id/:id', 
            templateUrl: 'record/forms/forms.jsp',
            controller: 'FormCtrl'
        })
        
	    
}]);

/* For debugging purposes
 
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

	                    when('/schedule', {
		                    templateUrl: 'partials/schedule.jsp',
		                    controller: 'ScheduleCtrl'
	                    }).
	                    when('/consults', {
		                    templateUrl: 'partials/consultList.jsp',
		                    controller: 'ConsultListCtrl'
	                    }).
	                    
	                    when('/patient/:demographicNo', {
		                    templateUrl: 'partials/patient/index.jsp',
		                    controller: 'PatientCtrl'
	                    }).
	                    when('/report', {
		                    templateUrl: 'partials/report.html',
		                    controller: 'ReportCtrl'
	                    }).
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
	                    
	                    when('/search', {
		                    templateUrl: 'partials/patientSearch.jsp',
		                    controller: 'PatientSearchCtrl'
	                    }).
	                    when('/searchResults', {
		                    templateUrl: 'partials/patientSearchResults.jsp',
		                    controller: 'PatientSearchCtrl'
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
	                    otherwise({
	                    	redirectTo: '/dashboard'
	                    });
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
	    start = parseInt(start, 10);
	    return input.slice(start);
	  };
	});