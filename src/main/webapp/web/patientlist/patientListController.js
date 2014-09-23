/*

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

*/

oscarApp.controller('PatientListCtrl', function ($scope,$http,$resource,$state,providerService, Navigation, scheduleService) {
	
	$scope.sidebar = Navigation;
	
	$scope.tabItems = [
      	             	{"id":0,"label":"Appts.","url":"../ws/rs/schedule/day/today","template":"patientlist/patientList1.jsp",httpType:'GET'},
      	             	{"id":1,"label":"Recent","url":"../ws/rs/providerService/getRecentDemographicsViewed?startIndex=0&itemsToReturn=100","template":"patientlist/recent.jsp",httpType:'GET'}
    ];
      	
    $scope.moreTabItems = [
      					{"id":0,"label":"Patient Sets","url":"../ws/rs/reporting/demographicSets/patientList",template:"patientlist/demographicSets.html",httpType:'POST'}
    ];
	
  
	 $scope.goToRecord = function(patient){
		 var params = {demographicNo:patient.demographicNo};
		 if(angular.isDefined(patient.appointmentNo)){
			 params.appointmentNo = patient.appointmentNo;
			 params.encType = "face to face encounter with client";
		 }
		 console.log("params",params);
		 $state.go('record.summary',params);
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

$scope.changeMoreTab = function(temp,filter){
	var beforeChangeTab = $scope.currentmoretab;
	$scope.currentmoretab = $scope.moreTabItems[temp];
	
	var d = undefined;
	if($scope.currentmoretab.httpType == 'POST') {
		d = filter!=null?JSON.stringify(filter):{}
	}
	
	$http({
	    url: $scope.currentmoretab.url,
	    dataType: 'json',
	    data: d,
	    method: $scope.currentmoretab.httpType,
	    headers: {
	        "Content-Type": "application/json"
	    }
	}).success(function(response){
		$scope.currentPage = 0;
		
		$scope.template = $scope.moreTabItems[temp].template;
		
		if (response.patients instanceof Array) {
			$scope.patients = response.patients;
		} else if(response.patients == undefined) { 
			$scope.patients = [];
		} else {
			var arr = new Array();
			arr[0] = response.patients;
			$scope.patients = arr;
		}
		
		$scope.currenttab=null;
	  	
		
		$scope.nPages = 1;
		if($scope.patients != null && $scope.patients.length>0) {
			$scope.nPages=Math.ceil($scope.patients.length/$scope.pageSize);
		} 
	
		 Navigation.load($scope.template);
		 
	}).error(function(error){
	   alert('error loading tab '+ error);
	});	
}

$scope.changeTab = function(temp,filter){
	console.log('change tab - ' + temp);
	$scope.currenttab = $scope.tabItems[temp];
	
	var d = undefined;
	if($scope.currenttab.httpType == 'POST') {
		d = filter!=null?JSON.stringify(filter):{}
	}
	$http({
		url: $scope.currenttab.url,	
		data: d,
		dataType: 'json',		
		method: $scope.currenttab.httpType,		
		headers: {		
			"Content-Type": "application/json"		
		}		
	}).success(function(response){
		$scope.currentPage = 0;
		
		$scope.template = $scope.tabItems[temp].template;
	  	
		if (response.patients instanceof Array) {
			$scope.patients = response.patients;
		} else if(response.patients == undefined) { 
			$scope.patients = [];
		} else {
			var arr = new Array();
			arr[0] = response.patients;
			$scope.patients = arr;
		}
		
		$scope.currentmoretab=null;
	  	
		$scope.nPages = 1;
		if($scope.patients != null && $scope.patients.length>0) {
			$scope.nPages=Math.ceil($scope.patients.length/$scope.pageSize);
		} 
		
		Navigation.load($scope.template);
			
	  	
	}).error(function(error){
	    alert('error loading tab '+error);
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
		if ($scope.patients == null || $scope.patients.length == 0) {
			return 1;
		}
		
		return Math.ceil($scope.patients.length/$scope.pageSize);                
	}
	
	for (var i=0; i<$scope.pageSize; i++) {
		$scope.data.push("Item "+i);
	}
});


oscarApp.controller('PatientListDemographicSetCtrl', function($scope, Navigation,$http) {	
	   $http({
	        url: '../ws/rs/reporting/demographicSets/list',
	        method: "GET",
	        headers: {'Content-Type': 'application/json'}
	      }).success(function (data, status, headers, config) {
	    	  $scope.sets = data.content;
	      }).error(function (data, status, headers, config) {
	          alert('Failed to get sets lists.');
	      });

});

oscarApp.controller('PatientListAppointmentListCtrl', function($scope, Navigation,$http, scheduleService,$q) {	
	 
	scheduleService.getStatuses().then(function(data){
		$scope.statuses = data.content;
	},function(reason){
		alert(reason);
	});

	//TODO:this gets called alot..should switch to a dictionary.
	 $scope.getAppointmentStyle = function(patient){ 
		 if($scope.statuses != null) {
			for(var i=0;i<$scope.statuses.length;i++) {
				 if($scope.statuses[i].status == patient.status) {
	    			return {'background-color':$scope.statuses[i].color};
	    		 }
	    	 }
		}
		 
		 return {};	 
	 }

});

