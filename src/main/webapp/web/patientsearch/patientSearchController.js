oscarApp.controller('PatientSearchCtrl', function ($scope, $timeout, $resource, ngTableParams, securityService, $modal, $http, demographicService, $state, $location) {

	var quickSearchTerm = ($location.search()).term;
	
	//type and term for now
	$scope.search = {type:'Name',term:"",active:true,integrator:false,outofdomain:true};
	
	$scope.lastResponse = '';

   securityService.hasRights({items:[{objectName:'_demographic',privilege:'w'},{objectName:'_demographic',privilege:'r'}]}).then(function(result){
    	if(result.content != null && result.content.length == 2) {
    		 $scope.demographicWriteAccess = result.content[0];
        	 $scope.demographicReadAccess = result.content[1];
        	 
        	 if($scope.demographicReadAccess) {
	        	 $scope.tableParams = new ngTableParams({
	        	        page: 1,            // show first page
	        	        count: 10,
	        	        sorting: {
	        	            Name: 'asc'     // initial sorting
	        	        }
	        	    }, {
	        	        getData: function($defer, params) {
	        	        	$scope.integratorResults = null;
	        	        	var count = params.url().count;
	        	        	var page = params.url().page;
	        	        	
	        	        	$scope.search.params = params.url();
	        	    
	        	        	demographicService.search($scope.search,((page-1)*count),count).then(function(result){
	        	        		 params.total(result.total);
	        	                 $defer.resolve(result.content);
	        	                 $scope.lastResponse = result.content;	        	                
	        	        	},function(reason){
	        	        	 alert("demo-service:"+reason);
	        	        	});
	        	        	
	        	        	if($scope.search.integrator == true) {
	        		        	//Note - I put in this arbitrary maximum
	        		        	demographicService.searchIntegrator($scope.search,100).then(function(result){
	        		       		 	$scope.integratorResults = result;
	        			       	},function(reason){
	        			       	 alert("remote-demo-service:"+reason);
	        			       	});
	        	        	}
	        	        }
	        	    });
	        	 if(quickSearchTerm != null) {
	        		 $scope.search.term = quickSearchTerm;
	        		 $scope.tableParams.reload();
	        	 }
        	 }
    	} else {
    		alert('failed to load rights');
    	}
    },function(reason){
    	alert(reason);
    });	
	
   
	
    
    $scope.doSearch = function() {
    	if ($scope.search.type=="DOB") {
    		if ($scope.search.term.match("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$") || $scope.search.term.match("^[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}$")) {
    			$scope.search.term = $scope.search.term.replace(/\//g, "-");
    			var datePart = $scope.search.term.split("-");
    			$scope.search.term = datePart[0]+"-"+pad0(datePart[1])+"-"+pad0(datePart[2]);
    		} else {
    			alert("Please enter Date of Birth in format YYYY-MM-DD.");
    			return;
    		}
    	}
    	$scope.tableParams.reload();
    }
    
    $scope.doClear = function() {
    	$scope.search = {type:'Name',term:"",active:true,integrator:false,outofdomain:true};
    	$scope.tableParams.reload();
    }
    
    $scope.clearButMaintainSearchType = function() {
    	$scope.search = {type:$scope.search.type,term:"",active:true,integrator:false,outofdomain:true};
    	$scope.tableParams.reload();
    	
    	if ($scope.search.type=="DOB") $scope.searchTermPlaceHolder = "YYYY-MM-DD";
    	else $scope.searchTermPlaceHolder = "Search Term";
    }
    $scope.searchTermPlaceHolder = "Search Term";
    
    $scope.loadRecord = function(demographicNo) {
		 $state.go('record.details', {demographicNo:demographicNo, hideNote:true});
    }
    
    $scope.showIntegratorResults = function () {
    	var result = ($scope.integratorResults != null && $scope.integratorResults.total > 0 ) ? $scope.integratorResults.content : [];
    	var total = ($scope.integratorResults != null) ? $scope.integratorResults.total : 0;
        var modalInstance = $modal.open({
        	templateUrl: 'patientsearch/remotePatientResults.jsp',
            controller: 'RemotePatientResultsController',
            resolve: {
                results: function () {
                    return result;
                },
                total: function() {
                	return total;
                }
            }
        });
    }
});

function pad0(n) {
	if (n.length>1) return n;
	else return "0"+n;
}


oscarApp.controller('RemotePatientResultsController',function($scope, $modalInstance, results, total, $http) {
    $scope.results = results;
    $scope.total = total;
    
    $scope.currentPage = 1;
    $scope.pageSize=5; 
    $scope.startIndex = 0;
    
     
    $scope.close = function () {
        $modalInstance.close("Someone Closed Me");
    }
    
    $scope.doImport = function (d) {
    	var myUrl = '../appointment/copyRemoteDemographic.jsp?remoteFacilityId='+d.remoteFacilityId+'&demographic_no='+d.demographicNo;
    	window.open(myUrl, "ImportDemo", "width=700, height=1027");
    }
    
    $scope.save = function () {
        $modalInstance.close("Someone Saved Me");
    }
    
    $scope.prevPage = function () {
       if($scope.startIndex == 0) {
    	   return;
       }
       $scope.currentPage--;
       $scope.startIndex = ($scope.currentPage-1)*$scope.pageSize;
       
    }
    
    $scope.nextPage = function () {
    	if($scope.startIndex+$scope.pageSize > $scope.total) {
    		return;
    	}
    	$scope.currentPage++;
    	$scope.startIndex = ($scope.currentPage-1)*$scope.pageSize;
    }
    
   
});
