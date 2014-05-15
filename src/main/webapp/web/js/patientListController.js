oscarApp.controller('PatientListCtrl', function ($scope,$http,$resource) {
	
	$scope.tabItems = [
	             	{"id":0,"label":"Appts.","url":"../ws/rs/schedule/999998/day/today"},
	             	{"id":1,"label":"CaseLoad","url":"json/patientList2.json"}
	];
	
	$scope.moreTabItems = [
					{"id":0,"label":"My Residents"},
					{"id":1,"label":"Customize"}
	];
	
	 $scope.getAppointmentStyle = function(patient){ 
		 if (patient.status === "H") {
	        return "success";
		 } else if (patient.status === "P") { 
			return "danger";
		 } else {
			 
	        return "default";
		 }
	 
	 }

//for filter box
$scope.query='';


$scope.isActive = function(temp){
	if($scope.currenttab === null) {
		return false;
	}
	return temp === $scope.currenttab.id;
}

$scope.isMoreActive = function(temp){
	if($scope.currentmoretab=== null) {
		return false;
	}
	return temp === $scope.currentmoretab.id;
}

$scope.changeMoreTab = function(temp){
	var beforeChangeTab = $scope.currentmoretab;
	$scope.currentmoretab = $scope.moreTabItems[temp];
	//I want the patient list to change, and the template to get loaded $scope.patients
	$http({
	    url: $scope.currentmoretab.url,
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }
	}).success(function(response){
		$scope.template = response.template;
		
		if (response.patients instanceof Array) {
			$scope.patients = response.patients;
		} else if(response.patients == undefined) { 
		} else {
			var arr = new Array();
			arr[0] = response.patients;
			$scope.patients = arr;
		}
		
		$scope.currenttab=null;
	}).error(function(error){
	    $scope.error = error;
	});	
}

$scope.changeTab = function(temp){
	$scope.currenttab = $scope.tabItems[temp];
	//I want the patient list to change, and the template to get loaded $scope.patients
	$http({
	url: $scope.currenttab.url,	
	dataType: 'json',		
	method: 'GET',		
	headers: {		
	"Content-Type": "application/json"		
	}		
	}).success(function(response){

		$scope.template = response.template;
	  	
		if (response.patients instanceof Array) {
			$scope.patients = response.patients;
		} else if(response.patients == undefined) { 
		} else {
			var arr = new Array();
			arr[0] = response.patients;
			$scope.patients = arr;
		}
		

		$scope.currentmoretab=null;
	  	
	  	$scope.nPages=Math.ceil($scope.patients.length/$scope.pageSize);
	  	
	}).error(function(error){
	    $scope.error = error;
	});	
}	 

$scope.getMoreTabClass = function(id){ 
	if($scope.currentmoretab != null && id == $scope.currentmoretab.id) {
		return "more-tab-highlight";
	}
	return "";
}

	$scope.changeTab(0);
	$scope.currentPage = 0;
	$scope.pageSize = 8;
	$scope.data = [];
	$scope.patients = null;
	
	$scope.numberOfPages=function(){
		if ($scope.patients == null) {
			return 1;
		}
		
		return Math.ceil($scope.patients.length/$scope.pageSize);                
	}
	
	for (var i=0; i<$scope.pageSize; i++) {
		$scope.data.push("Item "+i);
	}


	

});
