oscarApp.controller('ConsultListCtrl', function ($scope,$http,$resource) {
	$scope.init = function(value) {
		$scope.providerNo = value;
	}
});
