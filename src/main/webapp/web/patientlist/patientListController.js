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

oscarApp.controller('PatientListCtrl', function ($scope,$http,$state,Navigation,personaService,$modal) {
	
	$scope.sidebar = Navigation;
	
    $scope.showFilter=true;
    $scope.patientListConfig = {};

	
	 $scope.goToRecord = function(patient){
		 if(patient.demographicNo != 0){
			 var params = {demographicNo:patient.demographicNo};
			 if(angular.isDefined(patient.appointmentNo)){
				 params.appointmentNo = patient.appointmentNo;
				 params.encType = "face to face encounter with client";
			 }
			 console.log("params",params);
			 $state.go('record.summary',params);
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

$scope.hidePatientList = function() {
	$scope.$emit('configureShowPatientList', false);
}

$scope.changeMoreTab = function(temp,filter){
	var beforeChangeTab = $scope.currentmoretab;
	$scope.currentmoretab = $scope.moreTabItems[temp];
	
	$scope.showFilter=true;
	$scope.currenttab=null;
	$scope.refresh(filter);		
}

$scope.changeTab = function(temp,filter){
	console.log('change tab - ' + temp);
	$scope.currenttab = $scope.tabItems[temp];
	$scope.showFilter=true;
	$scope.currentmoretab=null;
    $scope.refresh(filter);
	
}	 

	$scope.getMoreTabClass = function(id){ 
		if($scope.currentmoretab != null && id == $scope.currentmoretab.id) {
			return "more-tab-highlight";
		}
		return "";
	}

	$scope.currentPage = 0;
	$scope.pageSize = 8;
	$scope.patients = null;
	
	$scope.numberOfPages=function(){
		if($scope.nPages == null || $scope.nPages == 0) {
			return 1;
		}
		return $scope.nPages;       
	}
	

    $scope.$on('updatePatientListPagination', function (event, data) {
        console.log('updatePatientListPagination='+data); 
        $scope.nPages=Math.ceil(data/$scope.pageSize);
        console.log('nPages='+$scope.nPages);
      });
      
    
    $scope.changePage = function(pageNum) {
    	$scope.currentPage = pageNum;
    	//broadcast the change page
    	$scope.$broadcast('updatePatientList', {currentPage:$scope.currentPage,pageSize:$scope.pageSize});
    	
    }
    
 
    
  //  $scope.$watch("currentPage", function(newValue, oldValue) {
   //     console.log('currentPage changes from ' + oldValue + ' to ' + newValue);
   //   });
    
  	
	$scope.$on('togglePatientListFilter', function (event, data) {
		console.log("received a togglePatientListFilter event:"+ data);
		$scope.showFilter = data;
	});


	$scope.process = function(tab,filter) {
		if(tab.url != null) {
			
			var d = undefined;
			if(tab.httpType == 'POST') {
				d = filter!=null?JSON.stringify(filter):{}
			}
				
			$http({
			    url: tab.url,
			    dataType: 'json',
			    data: d,
			    method: tab.httpType,
			    headers: {
			        "Content-Type": "application/json"
			    }
			}).success(function(response){
				
				
				$scope.template = tab.template;
				Navigation.load($scope.template);
				
				$scope.currentPage = 0;
				
				if (response.patients instanceof Array) {
					$scope.patients = response.patients;
				} else if(response.patients == undefined) { 
					$scope.patients = [];
				} else {
					var arr = new Array();
					arr[0] = response.patients;
					$scope.patients = arr;
				}
				
				$scope.nPages = 1;
				if($scope.patients != null && $scope.patients.length>0) {
					$scope.nPages=Math.ceil($scope.patients.length/$scope.pageSize);
				} 
			 
			}).error(function(error){
			   alert('error loading data for patient list:' + error);
			});	
		} else {
			$scope.changePage($scope.currentPage);
			$scope.currentPage = 0;
			$scope.nPages = 1;
			$scope.template = tab.template;
			Navigation.load($scope.template);
		}
	}
	
	$scope.refresh = function(filter) {
	
		if($scope.currenttab != null) {
			$scope.process($scope.currenttab,filter);
		}
		if($scope.currentmoretab != null) {
			$scope.process($scope.currentmoretab,filter);
		}
		
	}

	personaService.getPatientLists().then(function(persona){
		if(persona.patientListTabItems.length == undefined) {
			$scope.tabItems = [persona.patientListTabItems];
		}else{
			$scope.tabItems = persona.patientListTabItems;
		}
		$scope.moreTabItems = persona.patientListMoreTabItems;
		$scope.changeTab(0);
	},function(reason){
		alert(reason);
	});
	
	personaService.getPatientListConfig().then(function(patientListConfig){
		$scope.patientListConfig = patientListConfig;
		$scope.pageSize = $scope.patientListConfig.numberOfApptstoShow;
	},function(reason){
		alert(reason);
	});
	
	
	


	$scope.manageConfiguration = function() {
		var modalInstance = $modal.open({
        	templateUrl: 'patientlist/patientListConfiguration.jsp',
            controller: 'PatientListConfigController',
            backdrop: false,
            size: 'lg',
            resolve: {config: function() {return $scope.patientListConfig;}}
        });
        
        modalInstance.result.then(function(patientListConfig){
        	personaService.setPatientListConfig(patientListConfig).then(function(patientListConfig){
        		$scope.patientListConfig = patientListConfig;
        		$scope.pageSize = $scope.patientListConfig.numberOfApptstoShow;
        		$scope.$emit('updatePatientListPagination',$scope.patients.length);
        	},function(reason){
        		alert(reason);
        	});
        	
        },function(reason){
        	console.log(reason);
        });
	};
	
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

oscarApp.controller('PatientListAppointmentListCtrl', function($scope, Navigation,$http, scheduleService,$q,$filter,$modal,providerService) {	
	 
	scheduleService.getStatuses().then(function(data){
		$scope.statuses = data.content;
	},function(reason){
		alert(reason);
	});
	
	$scope.getAppointmentTextStyle  = function(patient){
		if(patient.demographicNo == 0){
			 return {'color':'white'};
		 }
	}

	//TODO:this gets called alot..should switch to a dictionary.
	 $scope.getAppointmentStyle = function(patient){ 
		 if(patient.demographicNo == 0){
			 return {'background-color':'black'};
		 }
		 
		 if($scope.statuses != null) {
			for(var i=0;i<$scope.statuses.length;i++) {
				 if($scope.statuses[i].status == patient.status) {
	    			return {'background-color':$scope.statuses[i].color};
	    		 }
	    	 }
		}
		 
		 return {};	 
	 }
	 
	
     $scope.today = function() {
	    $scope.appointmentDate = new Date();
	  };
	  
	  $scope.today();

	  $scope.clear = function () {
	    $scope.appointmentDate = null;
	  };
	  
	  $scope.open = function($event) {
		    $event.preventDefault();
		    $event.stopPropagation();
		    $scope.opened = true;
	  };
	  
	  Date.prototype.AddDays = function(noOfDays) {
		    this.setTime(this.getTime() + (noOfDays * (1000 * 60 * 60 * 24)));
		    return this;
		}

	  $scope.switchDay = function (n) {
		    var dateNew = $scope.appointmentDate;
	        dateNew.AddDays(n);
	        
	        $scope.appointmentDate = dateNew; 
	               
	        var formattedDate = $filter('date')(dateNew,'yyyy-MM-dd');	        
	        
	        $scope.changeApptList(formattedDate);
	        
	        
	  };
	  
	$scope.changeApptDate = function(){
		if($scope.appointmentDate == undefined) {
			$scope.today();
		}
		var formattedDate = $filter('date')($scope.appointmentDate,'yyyy-MM-dd');
		$scope.changeApptList(formattedDate);
	};
	
	$scope.changeApptList = function(day){
		
		temp = 0;
		
		$scope.currenttab = $scope.tabItems[temp];
		var lastIndx = $scope.currenttab.url.lastIndexOf("/");
		$scope.currenttab.url =  $scope.currenttab.url.slice(0,lastIndx+1)+day;
		$scope.showFilter=true;
		$scope.refresh();
		
	}
	
	$scope.addNewAppointment = function() {
	       var modalInstance = $modal.open({
	        	templateUrl: 'schedule/appointmentAdd.jsp',
	            controller: 'AppointmentAddController',
	            backdrop: false,
	            size: 'lg',
	            resolve: {
	            	me: function() {return providerService.getMe();},
	       			apptDate: function() {return $scope.appointmentDate;}
	            }
	        });
	        
	        modalInstance.result.then(function(data){
	        	$scope.switchDay(0);
	        },function(reason){
	        	alert(reason);
	        });
	}

	$scope.viewAppointment = function(apptNo) {
		var modalInstance = $modal.open({
        	templateUrl: 'schedule/appointmentView.jsp',
            controller: 'AppointmentViewController',
            backdrop: false,
            size: 'lg',
            resolve: {
            	me: function() {return providerService.getMe();},
            	appointment: function() {return scheduleService.getAppointment(apptNo);},
            	statusList: function() {return scheduleService.getStatuses();}
            }
        });
        
        modalInstance.result.then(function(data){
        	$scope.switchDay(0);
        	
        },function(reason){
        	alert(reason);
        });
	}
	
});


oscarApp.controller('PatientListProgramCtrl', function($scope,$http) {	
	  
	
	 $scope.$on('updatePatientList', function (event, data) {
	        console.log('updatePatientList='+JSON.stringify(data)); 
	        $scope.updateData(data.currentPage,data.pageSize);
	 });
	 
	 
	 //the currentPage is 0 based
	 $scope.updateData = function(currentPage,pageSize) {
		 
		 var startIndex = currentPage*pageSize;
		 
	   $http({
	        url: '../ws/rs/program/patientList?startIndex='+startIndex+'&numToReturn='+pageSize,
	        method: "GET",
	        headers: {'Content-Type': 'application/json'}
	      }).success(function (data, status, headers, config) {
	    	  $scope.admissions = data.content;
	    	  $scope.$emit('updatePatientListPagination', data.total);
	      }).error(function (data, status, headers, config) {
	          alert('Failed to get sets lists.');
	      });
	 }
	
	 //initialize..
	 $scope.updateData(0,$scope.pageSize);
	 $scope.$emit('togglePatientListFilter', false);
	 
});

oscarApp.controller('PatientListConfigController',function($scope, $modalInstance, config) {
	
	$scope.patientListConfig= config; 
	
	$scope.cancel = function(){
		$modalInstance.dismiss();
	};
	
	$scope.saveConfiguration =function(){
    	$modalInstance.close($scope.patientListConfig);
    };

});
