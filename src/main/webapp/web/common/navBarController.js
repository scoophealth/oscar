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


oscarApp.controller('NavBarCtrl', function ($scope,$http,$location,$modal, $state,providerService, securityService) {
	
	
	providerService.getMe().then(function(result){
			$scope.me = result;
		    },function(reason){
		   	 alert("unable to get my info..can't load page");
	});
		
	
    securityService.hasRights({items:[{objectName:'_search',privilege:'r'},
                                      {objectName:'_demographic',privilege:'w'},
                                      {objectName:'_msg',privilege:'r'}]
    }).then(function(result){
    	console.log(result.toSource());
    	if(result.content != null) {
    		 $scope.searchRights = result.content[0];
    		 $scope.newDemographicRights = result.content[1];
    		 $scope.messageRights = result.content[2];
    	}
    });
  
	
	
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
	
	  $scope.loadRecord = function(demographicNo) {
	 		 $state.go('record.details', {demographicNo:demographicNo, hideNote:true});
	     }
	
	//to help ng-clicks on buttons
	$scope.transition = function ( state ) {
		$state.go( state );
	};
	
	$scope.goHome = function() {
		$state.go('dashboard');
	}
	
	$scope.goToPatientSearch = function() {
		$state.go('search');
	}
	
	$scope.openClassicMessenger = function() {
		if($scope.me != null) {
			window.open('../oscarMessenger/DisplayMessages.do?providerNo='+$scope.me.providerNo,'msgs','height=700,width=1024');
		}
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
		//$('#myModal').modal({remote:'modaldemo.jsp',show:true});
		
		var modalInstance = $modal.open({
		      templateUrl: 'modaldemo.jsp',
		      controller: NewPatientCtrl,
		      size: size
		    });
		
		modalInstance.result.then(function (selectedItem) {
		      console.log(selectedItem);
		      console.log('patient #: '+selectedItem.demographicNo);
		      console.log($location.path());
		      $location.path('/record/'+selectedItem.demographicNo+'/details');
		      console.log($location.path());
		    }, function () {
		      console.log('Modal dismissed at: ' + new Date());
		    });
		
		console.log($('#myModal'));
	}
	
	$scope.isActive = function (state) { 
		return $state.is(state);
	};
		
});


function NewPatientCtrl($scope,$http,$modal,$modalInstance,demographicService,patientDetailStatusService){
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
  	
	//get access right for creating new patient
	securityService.hasRight("_demographic", "w", demo.demographicNo).then(function(data){
		$scope.hasRight = data.value;
	});
  	
  	$scope.ok = function () {
  	    $modalInstance.close($scope.selected.item);
  	  };

  	 $scope.cancel = function () {
  	    $modalInstance.dismiss('cancel');
  	 };
}
