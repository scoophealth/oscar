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
oscarApp.controller('NavBarCtrl', function ($scope,$http,$location,$modal) {
	
	$http({
	    url: '../ws/rs/persona/navbar',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		$scope.currentProgram = response.currentProgram.program;
		if (response.programDomain.program instanceof Array) {
			$scope.programDomain = response.programDomain.program;
		} else {
			var arr = new Array();
			arr[0] = response.programDomain.program;
			$scope.programDomain = arr;
		}
		$scope.unreadMessagesCount = response.unreadMessagesCount;
		$scope.unreadPatientMessagesCount = response.unreadPatientMessagesCount;
		
		
		$scope.demographicSearchDropDownItems = response.menus.patientSearchMenu.items;
		$scope.menuItems = response.menus.menu.items;
		$scope.moreMenuItems = response.menus.moreMenu.items;
		$scope.userMenuItems = response.menus.userMenu.items;

	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//reload the navbar at any time..not sure why i can't call this form the controller.
	$scope.getNavbar = function () {
		$http({
		    url: '../ws/rs/persona/navbar',
		    dataType: 'json',
		    method: 'GET',
		    headers: {
		        "Content-Type": "application/json"
		    }

		}).success(function(response){
			$scope.currentProgram = response.currentProgram.program;
			if (response.programDomain.program instanceof Array) {
				$scope.programDomain = response.programDomain.program;
			} else {
				var arr = new Array();
				arr[0] = response.programDomain.program;
				$scope.programDomain = arr;
			}
			$scope.unreadMessagesCount = response.unreadMessagesCount;
			$scope.unreadPatientMessagesCount = response.unreadPatientMessagesCount;
			
			
			$scope.demographicSearchDropDownItems = response.menus.patientSearchMenu.items;
			$scope.menuItems = response.menus.menu.items;
			$scope.moreMenuItems = response.menus.moreMenu.items;
			$scope.userMenuItems = response.menus.userMenu.items;
		}).error(function(error){
		    $scope.error = error;
		});	
	}
	
	
	
	//to help ng-clicks on buttons
	$scope.go = function ( path ) {
		$location.path( path );
	};
	
	$scope.isActive = function(temp){
		if($scope.currenttab === undefined || $scope.currenttab === null) {
			return false;
		}
		//return temp === $scope.currenttab.id;
		return false;
	}

	$scope.isMoreActive = function(temp){
		if($scope.currenttab === undefined || $scope.currenttab === null) {
			return false;
		}
		if($scope.currentmoretab=== null) {
			return false;
		}
		return temp === $scope.currentmoretab.id;
	}

	$scope.changeMoreTab = function(temp){
		console.log('changeMoreTab');
		var beforeChangeTab = $scope.currentmoretab;
		$scope.currentmoretab = $scope.moreMenuItems[temp];
		$scope.currenttab = null;
	}

	$scope.changeTab = function(temp){
		console.log("changetab "+ temp);
		$scope.currenttab = $scope.menuItems[temp];
		$scope.currentmoretab=null;
	}	 

	$scope.getMoreTabClass = function(id){ 
		if($scope.currentmoretab != null && id == $scope.currentmoretab.id) {
			return "more-tab-highlight";
		}
		return "";
	}
	
	$scope.goHome = function() {
		console.log("goHome called");
		$scope.currenttab = null;
		$scope.currentmoretab = null;
		$window.location.href="index.jsp#/dashboard";
		$window.location.reload();
	}
	
	$scope.goToPatientSearch = function() {
		$scope.currenttab = null;
		$scope.currentmoretab = null;
		$window.location.href="index.jsp#/search";
		$window.location.reload();
	}
	
	$scope.changeProgram = function(temp){
		
		$http({
		    url: '../ws/rs/program/setDefaultProgramInDomain?programId='+temp,
		    dataType: 'json',
		    method: 'GET',
		    headers: {
		        "Content-Type": "application/json"
		    }

		}).success(function(response){
			$scope.getNavbar();
		}).error(function(error){
		    $scope.error = error;
		});	
		
		//TODO: need an action called or something to update the session variable on the class oscar ui
		
	}	 
	
	$scope.newDemographic = function(size){
		console.log("modal?");
		//$('#myModal').modal({remote:'modaldemo.html',show:true});
		
		var modalInstance = $modal.open({
		      templateUrl: 'modaldemo.html',
		      controller: NewPatientCtrl,
		      size: size
		    });
		
		modalInstance.result.then(function (selectedItem) {
		      console.log(selectedItem);
		      console.log(selectedItem.demographicNo);
		      console.log('/#/patient/'+selectedItem.demographicNo);
		      console.log($location.path());
		      $location.path('/patient/'+selectedItem.demographicNo);
		      console.log($location.path());
		    }, function () {
		      console.log('Modal dismissed at: ' + new Date());
		    });
		 

		
		console.log($('#myModal'));
	}
	
	
});


function NewPatientCtrl($scope,$http,$modal,demographicService,$modalInstance){
	//
	console.log("newpatient called");
	$scope.demographic = {};
	
  	$scope.saver = function(ngModelContoller){
  		console.log($scope.demographic.lastName);
  		console.log($scope.demographic.firstName);
  		console.log($scope.demographic.yearOfBirth);
  		console.log($scope.demographic.monthOfBirth);
  		console.log($scope.demographic.dayOfBirth);
  		console.log($scope.demographic.sex);
		//var demographic = {lastName:$scope.lastName,firstName:$scope.firstName,yearOfBirth:$scope.yearOfBirth,monthOfBirth:$scope.monthOfBirth};
  		
		console.log($scope.demographic);
		//demographicService
		console.log(ngModelContoller.$valid);
		console.log($scope);
		if(ngModelContoller.$valid){
			console.log("Save!!");
			$scope.demographic.dateOfBirth = $scope.demographic.yearOfBirth+'-'+$scope.demographic.monthOfBirth+"-"+$scope.demographic.dayOfBirth;
			$scope.demoRetVal = {};
			demographicService.saveDemographic($scope.demographic).then(function(data){
					console.log(data);
					$scope.demoRetVal = data;
					$modalInstance.close(data);
			    },
			    function(errorMessage){
			    	console.log("saveDemo "+errorMessage);   
			    }
			);
			
		}else{
			console.log("ERR!!");
		}
		
		
		
	}
  	
  	$scope.ok = function () {
  	    $modalInstance.close($scope.selected.item);
  	  };

  	 $scope.cancel = function () {
  	    $modalInstance.dismiss('cancel');
  	 };
  	
}
