oscarApp.controller('TicklerListCtrl', function($scope, $timeout, $resource, ngTableParams, securityService, $modal, $http, ticklerService, noteService, providers, providerService,$stateParams) {
    var ticklerAPI = $resource('../ws/rs/tickler/ticklers');
         
    $scope.lastResponse = "";
    $scope.providers = providers;
    
    
    securityService.hasRights({items:[{objectName:'_tickler',privilege:'w'},{objectName:'_tickler',privilege:'r'}]}).then(function(result){
    	if(result.content != null && result.content.length == 2) {
    		 $scope.ticklerWriteAccess = result.content[0];
        	 $scope.ticklerReadAccess = result.content[1];
        	 
        	 if($scope.ticklerReadAccess) {

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
        		        	$scope.search.includeUpdates='true';
        		        	$scope.search.includeProgram=true;
        		        	
        		        	if(angular.isDefined($stateParams.demographicNo)){
        		        		$scope.search.demographicNo = $stateParams.demographicNo;
        		        	}
        		        	
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
        	 }
    	} else {
    		alert('failed to load rights');
    	}
    },function(reason){
    	alert(reason);
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
  
        ticklerService.setCompleted(selectedTicklers).then(function(data){
        	$scope.tableParams.reload();
        },function(reason){
        	alert(reason);
        });
    }
    $scope.deleteTicklers = function() {
    	var selectedTicklers = new Array();
        angular.forEach($scope.lastResponse, function (item) {
            if(item.checked) {
            	selectedTicklers.push(item.id);
            }
        });
        
        ticklerService.setDeleted(selectedTicklers).then(function(data){
        	$scope.tableParams.reload();
        },function(reason){
        	alert(reason);
        });
        
    }
    
    $scope.addTickler = function() {
    	var windowProps = "height=400,width=600,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    	//window.open('../tickler/ticklerAdd.jsp','ticklerAdd',windowProps);
    	
      	var modalInstance = $modal.open({
        	templateUrl: 'tickler/ticklerAdd.jsp',
            controller: 'TicklerAddController',
            backdrop: false,
            size: 'lg'
        });
        
        modalInstance.result.then(function(data){
        	console.log('data from modalInstance '+data);
        	if(data != null && data == true) {
        		$scope.tableParams.reload();
        	}
        },function(reason){
        	alert(reason);
        });
        
        
    }
    
    $scope.editTickler = function(tickler) {
    	
        var modalInstance = $modal.open({
        	templateUrl: 'tickler/ticklerView.jsp',
            controller: 'TicklerViewController',
            backdrop: false,
            size: 'lg',
            resolve: {
                tickler: function () {
                	return tickler;
                },
                ticklerNote: function() {
                	return noteService.getTicklerNote(tickler.id);
                },
                ticklerWriteAccess: function() {
                	return  $scope.ticklerWriteAccess;
                },
                me: function() {
                	return providerService.getMe();
                }
            }
        });
        
        modalInstance.result.then(function(data){
        	console.log('data from modalInstance '+data);
        	if(data != null && data == true) {
        		$scope.tableParams.reload();
        	}
        },function(reason){
        	alert(reason);
        });
        
    }
    
    $scope.editNote2 = function (tickler) {
    	
    	noteService.getTicklerNote(tickler.id).then(function(data){
    		if(data.ticklerNote != null) {
        		$scope.ticklerNote = data.ticklerNote;
        	} else {
        		$scope.ticklerNote = {"editor":"you","note":"","noteId":0,"observationDate":"now","revision":0};

        	}
            var modalInstance = $modal.open({
            	templateUrl: 'tickler/ticklerNote.jsp',
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
    	},function(reason){
    		alert(reason);
    	});
    	
    };
    
    $scope.showComments = function(tickler){
    	$scope.tickler = tickler;
        var modalInstance = $modal.open({
        	templateUrl: 'tickler/ticklerComments.jsp',
            controller: 'TicklerCommentController',
            resolve: {
                tickler: function () {
                	return $scope.tickler;
                }
            }
        });
    };
    
    $scope.printArea = function() {
    	window.print();
    }
   
    
});


oscarApp.controller('TicklerNoteController',function($scope, $modalInstance, ticklerNote, tickler, $http, noteService) {
    $scope.ticklerNote = ticklerNote;
    $scope.originalNote = ticklerNote.note;
    $scope.tickler = tickler;
    
    $scope.close = function () {
        $modalInstance.close("Someone Closed Me");
    };
    $scope.save = function () {
    	var updatedNote = $scope.ticklerNote.note;
    	$scope.ticklerNote.tickler = $scope.tickler;
    	
    	noteService.saveTicklerNote($scope.ticklerNote).then(function(data){
    		 $modalInstance.close("Someone Saved Me");
    	},function(reason){
    		alert(reason);
    	});
    };
});

oscarApp.controller('TicklerCommentController',function($scope, $modalInstance, tickler) {
	   $scope.tickler = tickler;
	    
	    $scope.close = function () {
	        $modalInstance.close("Someone Closed Me");
	    };
});

