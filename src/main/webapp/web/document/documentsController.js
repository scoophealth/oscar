oscarApp.controller('DocumentsCtrl', function ($scope,securityService) {
	$scope.me = null;
	
	$scope.$watch(function() {
		  return securityService.getUser();
		}, function(newVal) {
			$scope.me = newVal;
			
			if(newVal != null) {
				window.open('../dms/documentReport.jsp?function=provider&functionid='+$scope.me.providerNo,'edocView','height=700,width=1024');
			}
		}, true);
	
	
	$scope.openPopup = function() {
		window.open('../dms/documentReport.jsp?function=provider&functionid='+$scope.me.providerNo,'edocView','height=700,width=1024');
	}
});
