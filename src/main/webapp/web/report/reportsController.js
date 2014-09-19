oscarApp.controller('ReportsCtrl', function ($scope,$state) {
	$scope.transition = function(state) {
		$state.go(state);
	}
});
