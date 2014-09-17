oscarApp.controller('PatientSearchCtrl', function ($scope, $timeout, $resource, ngTableParams, securityService, $modal, $http, demographicService, $state) {

	//type and term for now
	$scope.search = {type:'Name',term:"",active:true,integrator:false,outofdomain:true};
	
	$scope.lastResponse = '';
	
    securityService.getRightsAsPromise().then(function(result){
    	$scope.rights = result;
    	$scope.demographicReadAccess = $scope.hasRight('_demographic','r');
    	$scope.hasNoDemographicReadAccess = !$scope.hasRight('_demographic','r');
    	$scope.demographicWriteAccess = $scope.hasRight('_demographic','w');
    },function(reason){
   	 alert(reason);
    });
    
   
    
	$scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10,
        sorting: {
            Name: 'asc'     // initial sorting
        }
    }, {
        total: $scope.data.length,           // length of data
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
    
    $scope.doSearch = function() {
    	$scope.tableParams.reload();
    }
    
    $scope.doClear = function() {
    	$scope.search = {type:'Name',term:"",active:true,integrator:false,outofdomain:true};
    	$scope.tableParams.reload();
    }
    
    $scope.clearButMaintainSearchType = function() {
    	$scope.search = {type:$scope.search.type,term:"",active:true,integrator:false,outofdomain:true};
    	$scope.tableParams.reload();
    }
    
    $scope.loadRecord = function(demographicNo) {
		 $state.go('record.details', {demographicNo:demographicNo, hideNote:true});
    }
    
    $scope.hasRight = function(name,privilege) {
    	for(var x=0;x<$scope.rights.privileges.length;x++) {
    		var item = $scope.rights.privileges[x];
    		if(item.objectName == name) { 
    			
    			if(privilege == 'r' && (item.privilege == 'r' || item.privilege == 'w' || item.privilege == 'x')) {
    				return true;
    			}
    			if(privilege == 'w' && (item.privilege == 'w' || item.privilege == 'x')) {
    				return true;
    			}
    			if(privilege == 'u' && (item.privilege == 'u' || item.privilege == 'x')) {
    				return true;
    			}
    			if(privilege == 'd' && (item.privilege == 'd' || item.privilege == 'x')) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    
    $scope.showIntegratorResults = function () {
    	var result = ($scope.integratorResults != null && $scope.integratorResults.total > 0 ) ? $scope.integratorResults.content : [];
    	var total = ($scope.integratorResults != null) ? $scope.integratorResults.total : 0;
        var modalInstance = $modal.open({
        	templateUrl: 'patientsearch/remotePatientResults.html',
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