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


oscarApp.controller('NavBarCtrl', function ($scope, $location, $modal, $state, $rootScope, appService, securityService, personaService, billingService, inboxService) {
	
	$scope.unAckLabDocTotal = 0;
	
	$scope.$watch(function() {
		return securityService.getUser();
	}, function(newVal) {
		$scope.me = newVal;
	}, true);
	
	billingService.getBillingRegion().then(function(response){
		$scope.billRegion = response.message;
	},function(reason){
		alert(reason);
	});	
	
	
	securityService.hasRights({items:[{objectName:'_search',privilege:'r'},
	                                  {objectName:'_demographic',privilege:'w'},
	                                  {objectName:'_msg',privilege:'r'}]
	}).then(function(result){
		if(result.content != null) {
			$scope.searchRights = result.content[0];
			$scope.newDemographicRights = result.content[1];
			$scope.messageRights = result.content[2];
		}
	});
	
	personaService.getDashboardMenu().then(function(response){
		$scope.dashboardMenu = response.menus.menu;
	}),function(reason){
		alert(reason);
	};
	
	personaService.getNavBar().then(function(response){
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
		getUnAckLabDocCount();
		$scope.demographicSearchDropDownItems = response.menus.patientSearchMenu.items;
		$scope.menuItems = response.menus.menu.items;
		$scope.userMenuItems = response.menus.userMenu.items;
		$scope.messengerMenu = response.menus.messengerMenu.items;
	},function(reason){
		alert(reason);
	});
	
	
	getUnAckLabDocCount = function(){
		inboxService.getUnAckLabDocCount().then(function(response){
			$scope.unAckLabDocTotal = response;
		},function(reason){
			alert(reason);
		});
	}
	
	//reload the navbar at any time..not sure why i can't call this form the controller.
	getNavBar = function() {
		personaService.getNavBar().then(function(response){
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
			$scope.userMenuItems = response.menus.userMenu.items;
			$scope.messengerMenu = response.menus.messengerMenu.items;
		},function(reason){
			alert(reason);
		});
	}
	
	$scope.loadRecord = function(demographicNo) {
		$state.go('record.details', {demographicNo:demographicNo, hideNote:true});
	}
	
	//pre-load clinical connect url
	appService.getClinicalConnectURL().then(function(data){
		$scope.clinicalConnectUrl = data;
	});
	
	//to help ng-clicks on buttons
	$scope.transition = function (item) {
		
		if(angular.isDefined(item) && angular.isDefined(item.state)){
			var url = "";
			var wname = "";
			if(item.state=="inbox"){
				url = "../dms/inboxManage.do?method=prepareForIndexPage";
				wname="inbox";
			}else if(item.state=="billing"){
				url = "../billing/CA/" + $scope.billRegion + "/billingReportCenter.jsp?displaymode=billreport";
				wname="billing";
			}else if(item.state=="admin"){
				url = "../administration/";
				wname="admin";
			}else if(item.state=="documents"){
				url = "../dms/documentReport.jsp?function=provider&functionid="+$scope.me.providerNo;
				wname="edocView";
			}else if(item.state=="clinicalconnect"){
				url = $scope.clinicalConnectUrl;
				if (url=="") {
					alert("Cannot access ClinicalConnect. Please contact the adminstrator.");
					return;
				}
				wname="clinicalconnect";
			}else{
				$state.go(item.state);
			}
			
			if(url!="" && wname!=""){
				var newwindow = window.open(url, wname, 'scrollbars=1,height=700,width=1000');
				if (window.focus) {
					newwindow.focus();
				}
			}
			
			
		}else if(angular.isDefined(item) && angular.isDefined(item.url)){
			
			if(item.label=="Schedule"){
				qs = "";
				path = $location.path();
				path = path.substring(1); //remove leading /
				param = path.split("/");
				
				if(param.length==1){
					qs = "?module=" + param[0];
				}else if(param.length==3){
					qs = "?record=" + param[1] + "&module=" + param[2];
				}
				
				window.location = item.url + qs;
				return false;
			}else{
				window.location = item.url;
			}
		}
	};
	
	
	
	$scope.goHome = function() {
		$state.go('dashboard');
	}
	
	$scope.goToPatientSearch = function() {
		$state.go('search');
	}
	
	$scope.openMessenger = function(item){
		if($scope.me != null) {
			if(angular.isDefined(item) && angular.isDefined(item.url) && item.url == 'phr'){
				window.open('../phr/PhrMessage.do?method=viewMessages','INDIVOMESSENGER'+$scope.me.providerNo,'height=700,width=1024,scrollbars=1');
			}else if(angular.isDefined(item) && angular.isDefined(item.url) && item.url == 'k2a'){
				if(item.extra === "-"){ //If user is not logged in
					var win = window.open('../apps/oauth1.jsp?id=K2A','appAuth','width=700,height=450,scrollbars=1');
					win.focus();
				}else{
					var win = window.open('../apps/notifications.jsp','appAuth','width=450,height=700,scrollbars=1');
					win.focus();
				}
			}else{ // by default open classic messenger
				window.open('../oscarMessenger/DisplayMessages.do?providerNo='+$scope.me.providerNo,'msgs','height=700,width=1024,scrollbars=1');
			}
		}
	}
	
	$scope.newDemographic = function(size){
		console.log("modal?");
		
		var modalInstance = $modal.open({
			templateUrl: 'newPatient.jsp',
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
	
	$scope.isActive = function (item) {
		if(angular.isDefined(item) && angular.isDefined(item.state) && item.state != null){
			return $state.is(item.state);
		}
		return false;
	};
	
	$scope.changeProgram = function(programId){
		personaService.setCurrentProgram(programId).then(function(response){
			this.getNavBar();
		},function(reason){
			alert(reason);
		});
	}
	
	$scope.switchToAdvancedView = function() {
		$rootScope.$apply(function() {
			$location.path("/search");
			$location.search('term',$scope.quickSearchTerm);
		});
	}
	
	$scope.setQuickSearchTerm = function(term) {
		$scope.quickSearchTerm = term;
	}
	
	$scope.showPatientList = function() {
		$scope.$emit('configureShowPatientList', true);
	}
	
});


function NewPatientCtrl($scope, $modal, $modalInstance, demographicService, securityService, programService, staticDataService){
	console.log("newpatient called");
	$scope.demographic = {};
	
	//get access right for creating new patient
	securityService.hasRight("_demographic", "w").then(function(data){
		$scope.hasRight = data;
	});
	
	//get programs to be selected
	programService.getPrograms().then(function(data){
		$scope.programs = data;
		if ($scope.programs.length==1) $scope.demographic.admissionProgramId = $scope.programs[0].id;
	});
	
	//get genders to be selected
	$scope.genders = staticDataService.getGenders();
	
	$scope.saver = function(ngModelContoller){
		console.log($scope.demographic.lastName);
		console.log($scope.demographic.firstName);
		console.log($scope.demographic.dobYear);
		console.log($scope.demographic.dobMonth);
		console.log($scope.demographic.dobDay);
		console.log($scope.demographic.sex);
		console.log($scope.demographic);
		
		//demographicService
		console.log(ngModelContoller.$valid);
		console.log($scope);
		if(ngModelContoller.$valid){
			console.log("Saving...");
			
			if (!isCorrectDate($scope.demographic.dobYear, $scope.demographic.dobMonth, $scope.demographic.dobDay)) {
				alert("Incorrect Date of Birth!");
				return;
			}
			
			$scope.demographic.dateOfBirth = $scope.demographic.dobYear+'-'+$scope.demographic.dobMonth+"-"+$scope.demographic.dobDay;
			$scope.demographic.patientStatusDate = new Date();
			$scope.demographic.dateJoined = new Date();
			$scope.demoRetVal = {};
			
			demographicService.saveDemographic($scope.demographic).then(function(data){
				console.log(data);
				$scope.demoRetVal = data;
				$modalInstance.close(data);
			},
			function(errorMessage){
				console.log("saveDemo "+errorMessage);
			});
			
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
	
	$scope.capName = function () {
		if ($scope.demographic.lastName!=null) {
			$scope.demographic.lastName = $scope.demographic.lastName.toUpperCase();
		}
		if ($scope.demographic.firstName!=null) {
			$scope.demographic.firstName = $scope.demographic.firstName.toUpperCase();
		}
	}
}


function isCorrectDate(year, month, day) {
	var d = new Date(year, month-1, day);
	
	if (d=="Invalid Date") return false;
	if (d.getFullYear()!=year) return false;
	if (d.getMonth()!=month-1) return false;
	if (d.getDate()!=day) return false;
	
	return true;
}
