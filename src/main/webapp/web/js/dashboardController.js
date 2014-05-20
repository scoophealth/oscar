oscarApp.controller('DashboardCtrl', function ($scope,$http) {

	//header
	$scope.displayDate= new Date();
	$http({
	    url: '../ws/rs/providerService/provider/me.json',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		$scope.userFirstName = response.firstName;
	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//ticklers
	$http({
	    url: '../ws/rs/tickler/mine?limit=6',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.tickler instanceof Array) {
			$scope.ticklers = response.tickler;
		} else {
			var arr = new Array();
			arr[0] = response.tickler;
			$scope.ticklers = arr;
		}
		
	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//oscar messages
	$http({
	    url: '../ws/rs/messaging/unread?limit=6',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.message instanceof Array) {
			$scope.messages = response.message;
		} else {
			var arr = new Array();
			arr[0] = response.message;
			$scope.messages = arr;
		}
	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//inbox
	$http({
	    url: '../ws/rs/inbox/mine?limit=20',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.inbox instanceof Array) {
			$scope.inbox = response.inbox;
		} else {
			var arr = new Array();
			arr[0] = response.inbox;
			$scope.inbox = arr;
		}

	}).error(function(error){
	    $scope.error = error;
	});	
	
	//k2a
	$http({
	    url: '../ws/rs/rssproxy/rss?key=k2a',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.item instanceof Array) {
			$scope.k2afeed = response.item;
		} else {
			var arr = new Array();
			arr[0] = response.item;
			$scope.k2afeed = arr;
		}

	}).error(function(error){
	    $scope.error = error;
	});	
	
	
});
