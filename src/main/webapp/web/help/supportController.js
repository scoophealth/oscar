oscarApp.controller('SupportCtrl', function ($scope,$http,systemInfoService) {

	systemInfoService.getOSPInfo().then(function(result){
		$scope.ospInfo = result;
		//console.log(result.ospName);
	});
	
	//$scope.ospInfo= {name:'FriendlyOSP',phone:'905-555-5555',contact:'John Supportive',email:'john@support.osp.org',url:'http://oscar-emr.org'};
	$scope.buildInfo = {versionDisplayName:'14-Alpha', version:'master-0000'};
});
