oscarApp.controller('AppointmentViewController',function($scope, $filter, $modalInstance, $timeout, demographicService,me,providerService,scheduleService,appointment,statusList) {

	$scope.me = me;
	$scope.appointment = appointment;
	$scope.statusList = statusList.content;
	$scope.appointmentUpdate = {};

	$scope.appointmentWriteAccess = false;
	
	$scope.getStatus = function(status) {
		
		for(var x=0;x<$scope.statusList.length;x++) {
			console.log(JSON.stringify($scope.statusList[x]));
			if($scope.statusList[x].status == status) {
				return $scope.statusList[x].description;
			}
		}
		return status;
	}
	$scope.close = function () {
    	$modalInstance.close(false);
    }
	
	$scope.deleteAppointment = function() {
		if(confirm('Are you sure you want to delete this appointment?')) {
			scheduleService.deleteAppointment($scope.appointment.id).then(function(data){
				$modalInstance.close(true);
			});
		}
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
    
    
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.appointment.providerNo = model;
    	$scope.appointment.providerName = label;
    }
    
    
    $scope.editProvider = function() {
    	$scope.showProviderFormControl=true;
    	$scope.appointmentUpdate.providerNo = $scope.appointment.providerNo;
    	$scope.appointmentUpdate.providerName = $scope.appointment.provider.lastName + "," + $scope.appointment.provider.lastName;
    }
    
    $scope.updateProvider = function(item, model, label) {
    	$scope.needsUpdate=true;   	
    	$scope.appointment.providerNo = model;
    	$scope.appointment.providerName = label;
    	$scope.showProviderFormControl=false; 	
    }
    
    $scope.cancelProviderUpdate = function() {
    	$scope.appointmentUpdate.providerNo=null;
    	$scope.appointmentUpdate.providerName=null;
    	
    	$scope.showProviderFormControl=false; 	
    	
    }
    
    $scope.showAppointmentHistory = function() {
    	scheduleService.appointmentHistory($scope.appointment.demographicNo).then(function(data){
    		alert(JSON.stringify(data));
    	},function(error){
    		alert(error);
    	});
    }
    
    $scope.noShowAppointment = function() {
    	scheduleService.noShowAppointment($scope.appointment.id).then(function(data){
			$modalInstance.close(true);
		},function(error){
			alert(error);
		});
	
    }
    
    $scope.cancelAppointment = function() {
    	scheduleService.cancelAppointment($scope.appointment.id).then(function(data){
			$modalInstance.close(true);
		},function(error){
			alert(error);
		});
    }
    
});
