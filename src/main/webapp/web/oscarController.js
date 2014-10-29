oscarApp.controller('OscarCtrl', function ($rootScope, $scope, providerService,securityService) {
	
	
	/* functionality to handle showing the patient list or not - state changes trigger reset to showPtList=true 
	 * Controllers who want to hide it for the duration of their existance can just call
	 * 
	 * $scope.$emit('configureShowPatientList', false);
	 * */
	$scope.showPtList = true;
	
	$scope.showPatientList = function() {
		return $scope.showPtList;
	}
	
	$scope.getRightClass = function(name) {
		if($scope.showPtList)
			return name + "-10";
		else
			return name + "-12";
	}
	
	$scope.$on('configureShowPatientList', function (event, data) {
		console.log("received a configureShowPatientList event:"+ data);
		$scope.showPtList=data;
	});
	
	$rootScope.$on('$stateChangeStart', 
			function(event, toState, toParams, fromState, fromParams){ 
			    console.log('startChangeStart' + toState);
			    $scope.showPtList=true;
	});
	
	providerService.getMe().then(function(data){
		securityService.setUser(data);
	},function(reason){
		alert(reason);
	});
	 
	 
});

