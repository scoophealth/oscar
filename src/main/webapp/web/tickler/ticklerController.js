oscarApp.controller('TicklerListCtrl', function($scope, $timeout, $resource, ngTableParams, securityService, $modal, $http) {
    var ticklerAPI = $resource('../ws/rs/tickler/ticklers');
    
   
    //active provider lists for drop downs
    $scope.providers = new Array();
  
    $scope.lastResponse = "";

    $scope.ticklerWriteAccess=false;


    $http(
			{
        url: '../ws/rs/providerService/providers/search',
        method: "POST",
        data: JSON.stringify({"active":true}),
        headers: {'Content-Type': 'application/json'}
      }).success(function (data, status, headers, config) {
    	  $scope.providers = data.content;
        }).error(function (data, status, headers, config) {
          alert('Failed to get provider lists.');
        });
    

  
     securityService.getRightsAsPromise().then(function(result){
    	 $scope.rights = result;
    	 $scope.ticklerWriteAccess = $scope.hasRight('_tickler','w');
     },function(reason){
    	 alert(reason);
     });
   
    
   
    
    //object which represents all the filters, initialize status.
    $scope.search = {status:'A'};
    
    
    $scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10
    }, {
        total: 0,           // length of data
        getData: function($defer, params) {
            // ajax request to api
        	$scope.search.count = params.url().count;
        	$scope.search.page = params.url().page;
        	$scope.search.includeLinks='true';
        	$scope.search.includeComments='true';
        	//$scope.search.includeUpdates='true';
        	
        	ticklerAPI.get($scope.search, function(data) {
                $timeout(function() {
                	
                    // update table params
                    params.total(data.total);
                    // set new data
                    $defer.resolve(data.tickler);
                    
                    $scope.lastResponse = data.tickler;
                }, 500);
            });
        }
    });
    
    $scope.doSearch = function() {
    	$scope.tableParams.reload();
    }
    
    $scope.clear = function() {
    	$scope.search = angular.copy({status:'A'});
    	$scope.tableParams.reload();
    }
    
   
    
    $scope.checkAll = function() {
        angular.forEach($scope.lastResponse, function (item) {
            item.checked = true;
        });
    }
    
    $scope.checkNone = function() {
    	angular.forEach($scope.lastResponse, function (item) {
            item.checked = false;
        })
    }
    
    $scope.completeTicklers = function() {
    	var selectedTicklers = new Array();
        angular.forEach($scope.lastResponse, function (item) {
            if(item.checked) {
            	selectedTicklers.push(item.id);
            }
        });
        
        $http(
    			{
            url: '../ws/rs/tickler/complete',
            method: "POST",
            data: JSON.stringify({"ticklers":selectedTicklers}),
            headers: {'Content-Type': 'application/json'}
          }).success(function (data, status, headers, config) {
               // r$scope.users = data.users; // assign  $scope.persons here as promise is resolved here 
        	  $scope.tableParams.reload();
            }).error(function (data, status, headers, config) {
              alert('Failed to set ticklers to complete.');
            });
        
        
    }
    $scope.deleteTicklers = function() {
    	var selectedTicklers = new Array();
        angular.forEach($scope.lastResponse, function (item) {
            if(item.checked) {
            	selectedTicklers.push(item.id);
            }
        });
        
        $http(
    			{
            url: '../ws/rs/tickler/delete',
            method: "POST",
            data: JSON.stringify({"ticklers":selectedTicklers}),
            headers: {'Content-Type': 'application/json'}
          }).success(function (data, status, headers, config) {
               $scope.tableParams.reload();
            }).error(function (data, status, headers, config) {
            	alert('Failed to set ticklers to deleted.');
            });
       
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
    
    $scope.addTickler = function() {
    	var windowProps = "height=400,width=600,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    	window.open('../tickler/ticklerAdd.jsp','ticklerAdd',windowProps);
    }
    
    $scope.editTickler = function(ticklerId) {
    	var windowProps = "height=600,width=800,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    	window.open('../tickler/ticklerEdit.jsp?tickler_no='+ticklerId,'ticklerEdit',windowProps);
    }
    
    $scope.editNote2 = function (tickler) {
        $http.get('../ws/rs/notes/ticklerGetNote/'+tickler.id).success(function(data) {
        	if(data.ticklerNote != null) {
        		$scope.ticklerNote = data.ticklerNote;
        	} else {
        		$scope.ticklerNote = {"editor":"you","note":"","noteId":0,"observationDate":"now","revision":1};

        	}
            var modalInstance = $modal.open({
            	templateUrl: 'tickler/ticklerNote.html',
                controller: 'TicklerNoteController',
                resolve: {
                    ticklerNote: function () {
                        return $scope.ticklerNote;
                    },
                    tickler: function () {
                    	return tickler;
                    }
                }
            });
        }).error(function(data,status,headers,config){
        	alert('Failed to complete operation.');
        });
    };
    
    $scope.showComments = function(tickler){
    	$scope.tickler = tickler;
        var modalInstance = $modal.open({
        	templateUrl: 'tickler/ticklerComments.html',
            controller: 'TicklerCommentController',
            resolve: {
                tickler: function () {
                	return $scope.tickler;
                }
            }
        });
    };
   
    
});


oscarApp.controller('TicklerNoteController',function($scope, $modalInstance, ticklerNote, tickler, $http) {
    $scope.ticklerNote = ticklerNote;
    $scope.originalNote = ticklerNote.note;
    $scope.tickler = tickler;
    
    $scope.close = function () {
        $modalInstance.close("Someone Closed Me");
    };
    $scope.save = function () {
    	var updatedNote = $scope.ticklerNote.note;
    	$scope.ticklerNote.tickler = $scope.tickler;
    	 
    	$http(
    			{
            url: '../ws/rs/notes/ticklerSaveNote',
            method: "POST",
            data: JSON.stringify($scope.ticklerNote),
            headers: {'Content-Type': 'application/json'}
          }).success(function (data, status, headers, config) {
               // $scope.users = data.users; // assign  $scope.persons here as promise is resolved here 
            }).error(function (data, status, headers, config) {
               alert('Failed to save note.');
            });
    

        $modalInstance.close("Someone Saved Me");
    };
});

oscarApp.controller('TicklerCommentController',function($scope, $modalInstance, tickler) {
	   $scope.tickler = tickler;
	    
	    $scope.close = function () {
	        $modalInstance.close("Someone Closed Me");
	    };
});
