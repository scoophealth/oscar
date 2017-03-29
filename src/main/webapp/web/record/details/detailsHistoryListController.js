oscarApp.controller('DetailsHistoryListCtrl', function ($scope, $modalInstance, historyList) {
	console.log("details history list ctrl");
	
	$scope.historyList = historyList;
	
	$scope.close = function () {
        $modalInstance.close(false);
    }
});