oscarApp.controller('TicklerAddController',function($scope, $modalInstance, demographicService, providerService, ticklerService, $filter,$stateParams) {
    
	$scope.tickler = {template:{id:1,name:''},serviceDateDate:new Date(),serviceDateTime:new Date(), suggestedTextId:0};
	$scope.priorities = ['Low','Normal','High'];
    
	ticklerService.getTextSuggestions().then(function(data){
		$scope.textSuggestions = data.content;
		$scope.textSuggestions.unshift({id:0,suggestedText:''});	
	},function(reason){
		alert(reason);
	});
	
	$scope.close = function () {
        $modalInstance.close(false);
    }
	
	$scope.validate = function() {
		var t = $scope.tickler;
		$scope.errors = [];
		
		if(t.demographic == null) {
			$scope.errors.push('You must select a patient');
		}
		if(t.taskAssignedTo == null || t.taskAssignedTo.length == 0) {
			$scope.errors.push('You must assign a provider');
		}
		if(t.message == null || t.message.length == 0) {
			$scope.errors.push('Message is required');
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
    	
    	var t = {};
    	t.demographicNo = $scope.tickler.demographicNo;
    	t.taskAssignedTo = $scope.tickler.taskAssignedTo;
    	t.priority = $scope.tickler.priority;
    	t.status = 'A';
    	t.message = $scope.tickler.message;
    	
    	
    	var givenDate = $scope.tickler.serviceDateDate;
    	var givenTime = $scope.tickler.serviceDateTime;
    	givenDate.setHours(givenTime.getHours());
    	givenDate.setMinutes(givenTime.getMinutes());
    	
    	t.serviceDate = givenDate;
    	
    	ticklerService.add(t).then(function(data){
    		$modalInstance.close(true);
    	},function(reason){
    		alert(reason);
    	});
    	
    	
    }
    
    $scope.updateDemographicNo = function(item, model, label) {
    	
    	demographicService.getDemographic(model).then(function(data){
    		$scope.tickler.demographicNo=data.demographicNo;
    		$scope.tickler.demographicName = '';
        	$scope.tickler.demographic = data;
        	
    	});
    	
    }
    
    if(angular.isDefined($stateParams) && angular.isDefined($stateParams.demographicNo)){
		$scope.tickler.demographicNo = $stateParams.demographicNo;
		$scope.updateDemographicNo(null,$scope.tickler.demographicNo,null);
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
    
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.tickler.taskAssignedTo = model;
    	$scope.tickler.taskAssignedToName = label;
    }
    
    $scope.setSuggestedText = function() {
    	var results = $filter('filter')($scope.textSuggestions,{id:$scope.tickler.suggestedTextId},true);
    		
    	if(results!=null) {
    		$scope.tickler.message = results[0].suggestedText;
    	}
    }
});

