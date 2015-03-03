oscarApp.controller('AppointmentAddController',function($scope, $filter, $modalInstance, $timeout, demographicService,me,providerService,scheduleService,apptDate) {

	$scope.types = [];
	
	$scope.urgencies = [{value:'',label:'Normal'},{value:'critical',label:'Critical'}];
	
	$scope.me = me;

	$scope.appointment= {status:'t', appointmentDate:$filter('date')(apptDate,'yyyy-MM-dd'),startTime:'09:00 AM',type:'',
			duration:15,providerName:me.formattedName,providerNo:me.providerNo,reason:'',notes:'',
			location:'',resources:'',critical:''};
	
	scheduleService.getTypes().then(function(data){
		$scope.types = data.types;
		$scope.types.unshift({name:'',duration:15,location:'',notes:'',reason:'',resources:''});
		console.log(JSON.stringify(data));
	},function(error){
		alert(error);
	});
	
	$scope.selectType = function() {
		var type = null;
		
		for(var x=0;x<$scope.types.length;x++) {
			if($scope.types[x].name == $scope.appointment.type) {
				type = $scope.types[x];
				break;
			}
		}
		if(type != null) {
			$scope.appointment.duration = type.duration;
			$scope.appointment.location = type.location;
			$scope.appointment.notes = type.notes;
			$scope.appointment.reason = type.reason;
			$scope.appointment.resources = type.resources;
			
		}
		
	}
	
	$scope.close = function () {
    	if($scope.needsUpdate) {
    		if(confirm("You have unsaved changes, are you sure?")) {
    			$modalInstance.close(false);
    		}
    	} else {
    		$modalInstance.close(false);
    	}
        
    }
	
	$scope.validate = function() {
		var t = $scope.appointment;
		
		$scope.errors = [];
		
		if(t.demographic == null) {
			$scope.errors.push('You must select a patient');
		}
		if(t.providerNo == null ) {
			$scope.errors.push('You must select a provider');
		}
		if(t.startTime == null || t.startTime.length == 0) {
			$scope.errors.push('start time is required');
		}
		if(t.duration == null || t.duration.length == 0) {
			$scope.errors.push('start time is required');
		}
		
		if($scope.errors.length>0) {
			return false;
		}
		return true;
	}
    
    $scope.save = function () {
    	$scope.showErrors=true;
    	if(!$scope.validate()) {
    		return;
    	}
    	 	
    	var x = {};
    	x.status = $scope.appointment.status;
    	x.appointmentDate = $scope.appointment.appointmentDate ;
    	x.startTime12hWithMedian = $scope.appointment.startTime ;
    	x.type = $scope.appointment.type ;
    	x.duration = $scope.appointment.duration ;
    	x.providerNo = $scope.appointment.providerNo ;
    	x.reason = $scope.appointment.reason ;
    	x.notes = $scope.appointment.notes ;
    	x.location = $scope.appointment.location ;
    	x.resources = $scope.appointment.resources ;
    	x.urgency = $scope.appointment.critical ;
    	x.demographicNo = $scope.appointment.demographicNo;
    	
    	console.log(JSON.stringify(x));
    	scheduleService.addAppointment(x).then(function(data){
    		$modalInstance.close(true);
    	},function(reason){
    		alert(reason);
    	});
    	
    	
    }

    
    $scope.updateDemographicNo = function(item, model, label) {
    	
    	demographicService.getDemographic(model).then(function(data){
    		$scope.appointment.demographicNo=data.demographicNo;
    		$scope.appointment.demographicName = '';
        	$scope.appointment.demographic = data;
        	
    	});
    	
    }
    
    $scope.searchPatients  = function(term) {
    	var search = {type:'Name','term':term,active:true,integrator:false,outofdomain:true};
    	return demographicService.search(search,0,25).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.content.length;x++) {
    			resp.push({demographicNo:response.content[x].demographicNo,name:response.content[x].lastName + ',' + response.content[x].firstName});
    		}
    		return resp;
    	});
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
    
   
});
