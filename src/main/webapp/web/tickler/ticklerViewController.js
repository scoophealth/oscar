oscarApp.controller('TicklerViewController',function($scope, $filter, $modalInstance, tickler, ticklerService, ticklerNote, ticklerWriteAccess, providerService, $timeout, me) {
    
	$scope.tickler = angular.copy(tickler);
	$scope.me = me;
	$scope.ticklerWriteAccess = ticklerWriteAccess;
    
    //this object keeps track of the changes being made
    $scope.ticklerUpdate = {message:$scope.tickler.message};
    

    $scope.needsUpdate = false;
    
    
    $scope.showUpdates=false;
    $scope.showComments=true;
    
    if(ticklerNote != null) {
    	$scope.ticklerNote = ticklerNote.ticklerNote;
    }
    
    $scope.close = function () {
    	if($scope.needsUpdate) {
    		if(confirm("You have unsaved changes, are you sure?")) {
    			$modalInstance.close(false);
    		}
    	} else {
    		$scope.ticklerUpdate = undefined;
    		$modalInstance.close(false);
    	}
        
    }
    $scope.save = function () {
    	$modalInstance.close("Someone Saved Me");
    }
   
    $scope.editTaskAssignedTo = function() {
    	$scope.showTaskAssignedToFormControl=true;
    	$scope.ticklerUpdate.taskAssignedTo = $scope.tickler.taskAssignedTo;
    	$scope.ticklerUpdate.taskAssignedToName = $scope.tickler.taskAssignedName; 	
    }
    
    $scope.updateTaskAssignedTo = function(item, model, label) {
    	$scope.needsUpdate=true;   	
    	$scope.tickler.taskAssignedTo = model;
    	$scope.tickler.taskAssignedToName = label;
    	$scope.showTaskAssignedToFormControl=false; 	
    }
    
    $scope.cancelTaskAssignedToUpdate = function() {
    	$scope.ticklerUpdate.taskAssignedTo=null;
    	$scope.ticklerUpdate.taskAssignedToName=null;
    	
    	$scope.showTaskAssignedToFormControl=false; 	
    	
    }
    
    $scope.editServiceDateAndTime = function() {
    	$scope.ticklerUpdate.serviceDate =  $filter('date')($scope.tickler.serviceDate, 'yyyy-MM-dd');
    	$scope.ticklerUpdate.serviceTime =  $filter('date')($scope.tickler.serviceDate, 'HH:mm');
     	
    	$scope.showServiceDateAndTimeFormControl=true;
      }
    
    
    $scope.updateServiceDateAndTime = function() {
    	var dp = $scope.ticklerUpdate.serviceDate.split("-");
    	var tp = $scope.ticklerUpdate.serviceTime.split(":");
    	
    	if(dp.length != 3 || tp.length !=2 ) {
    		alert('Invalid Date/time. Please use yyyy-MM-dd and HH:mm formats');
    		return;
    	}
    	
    	var d = new Date(dp[0],parseInt(dp[1])-1, dp[2], tp[0],tp[1], 0, 0); 
    		
    	if(d == null || isNaN( d.getTime() )) {
    		alert('Invalid Date/time. Please use yyyy-MM-dd and HH:mm formats');
    		return;
    	} 
    	$scope.needsUpdate=true;
    	$scope.tickler.serviceDate = d;
    	$scope.showServiceDateAndTimeFormControl=false;
    }
    
    $scope.cancelServiceDateAndTimeUpdate = function() {
    	$scope.ticklerUpdate.serviceDate = null;
    	$scope.showServiceDateAndTimeFormControl=false;
    }
    
    $scope.editPriority = function() {
    	$scope.ticklerUpdate.priority =  $scope.tickler.priority;
    	$scope.priorities = ['Low','Normal','High'];
    	
    	$scope.showPriorityFormControl=true;
      }
    
    $scope.updatePriority = function(item, model, label) {
    	$scope.needsUpdate=true;   	
    	$scope.tickler.priority = model;
    	$scope.showPriorityFormControl=false;
    }
    
    
    $scope.cancelPriorityUpdate = function() {
    	$scope.ticklerUpdate.priority = null;
    	$scope.showPriorityFormControl=false;
    }
    
    $scope.editStatus = function() {
    	$scope.ticklerUpdate.statusName =  $scope.tickler.statusName;
    	$scope.ticklerUpdate.status = $scope.tickler.statusName;
    	$scope.statuses = [{id:'A',label:'Active'},{id:'C',label:'Completed'},{id:'D',label:'Deleted'}];
    	
    	$scope.showStatusFormControl=true;
      }
    
    $scope.updateStatus = function(item, model, label) {
    	$scope.needsUpdate=true;   	
    	$scope.tickler.status = model;
    	$scope.tickler.statusName = label;
    	$scope.showStatusFormControl=false;
    }
    
    $scope.cancelStatusUpdate = function() {
    	$scope.ticklerUpdate.statusName =  null;
    	$scope.ticklerUpdate.status = null;
	
    	$scope.showStatusFormControl=false;
   	
    }
    
    $scope.addComment = function() {
    	$scope.ticklerUpdate.comment = '';
    	$scope.showCommentFormControl=true;
    	
    }
    
    $scope.saveComment = function() {
    	$scope.needsUpdate=true;
    	if($scope.tickler.ticklerComments == null) {
    		$scope.tickler.ticklerComments = [];
    	}
    	var comment = {message:$scope.ticklerUpdate.comment, providerName: me.formattedName, 
    			providerNo:me.providerNo,updateDate:new Date(), newComment:true};
    	$scope.tickler.ticklerComments.unshift(comment);
    	$scope.showCommentFormControl=false;	
    	$scope.showComments=true;
    }
    
    $scope.cancelCommentUpdate = function() {
    	$scope.ticklerUpdate.comment=null;
    	$scope.showCommentFormControl=false;	    	
    }
    
    
    $scope.searchProviders = function(val) {
    	var search = {searchTerm:val,active:true};
    	return providerService.searchProviders(search,0,10).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({providerNo:response[x].providerNo,name:response[x].firstName + ' ' + response[x].lastName});
    		}
    		return resp;
    	});
    }
    
    $scope.saveChanges = function() {
    	if($scope.tickler.message != $scope.ticklerUpdate.message) {
    		$scope.needsUpdate=true;
    	}
    	if($scope.needsUpdate) {
    		$scope.tickler.message = $scope.ticklerUpdate.message;
    		ticklerService.update($scope.tickler).then(function(data){
    			$modalInstance.close(true);
    		});
    	} else {
    		$modalInstance.close(false);
    	}
    	
    }
    
    
    $scope.completeTickler = function() {
    	ticklerService.setCompleted([tickler.id]).then(function(data){
    		$modalInstance.close(true);
        },function(reason){
        	alert(reason);
        });
    	
    }
    
    $scope.deleteTickler = function() {
    	ticklerService.setDeleted([tickler.id]).then(function(data){
    		$modalInstance.close(true);
        },function(reason){
        	alert(reason);
        });
    	
    }
    
    $scope.printTickler = function() {
    	window.open('../Tickler.do?method=print&id='+tickler.id);
    	
    }
    
});
