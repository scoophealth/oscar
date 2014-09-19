oscarApp.controller('DocumentsCtrl', function ($scope,providerService,$state) {
	providerService.getMe().then(function(result){
   	   if(result != null && result.providerNo != null) {
    		   window.open('../dms/documentReport.jsp?function=provider&functionid='+result.providerNo,'edocView','height=700,width=1024');
   	   }
    },function(reason){
   	 alert("unable to get my info..can't load page");
    });
	
	$scope.transition = function(state) {
		$state.go(state);
	}
});
