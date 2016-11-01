oscarApp.controller('ClinicalConnectCtrl', function ($scope, $http, appService) {
	appService.getClinicalConnectURL().then(function(data){
		$scope.url = data;
	});
});
