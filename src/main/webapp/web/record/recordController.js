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

oscarApp.controller('RecordCtrl', function ($scope,$http,$location,$stateParams,$state,demo) {
	
	console.log("in patient Ctrl ",demo);
	
	$scope.demographicNo = $stateParams.demographicNo;
	$scope.demographic= demo;

	/*
	$scope.recordtabs2 = [ 
	 {id : 0,name : 'Master',url : 'partials/master.html'},
	 {id : 1,name : 'Summary',url : 'partials/summary.html'},
	 {id : 2,name : 'Rx',url : 'partials/rx.jsp'},
	 {id : 3,name : 'Msg',url : 'partials/summary.html'},
	 {id : 4,name : 'Trackers',url : 'partials/tracker.jsp'},
	 {id : 5,name : 'Consults',url : 'partials/summary.html'},
	 {id : 6,name : 'Forms',url : 'partials/formview.html'},
	 {id : 7,name : 'Prevs/Measurements',url : 'partials/summary.html'},
	 {id : 8,name : 'Ticklers',url : 'partials/summary.html'},
	 {id : 9,name : 'MyOscar',url : 'partials/blank.jsp'},
	 {id : 10,name : 'Allergies',url : 'partials/summary.html'},
	 {id : 11,name : 'CPP',url : 'partials/cpp.html'},
	 {id : 12,name : 'Labs/Docs',url : 'partials/labview.html'},
	 {id : 13,name : 'Billing',url : 'partials/billing.jsp'}	
	*/
	$scope.recordtabs2 = [ 
	                 	 {id : 0,displayName : 'Details'  ,path : 'record.details'},
	                 	 {id : 1,displayName : 'Summary'  ,path : 'record.summary'},
	                 	 {id : 2,displayName : 'Forms'    ,path : 'record.forms'},
	                 	 {id : 3,displayName : 'Labs/Docs',path : 'partials/eform.jsp'},
	                 	 {id : 4,displayName : 'Rx'       ,path : 'partials/eform.jsp'}];
	
	//var transitionP = $state.transitionTo($scope.recordtabs2[0].path,$stateParams,{location:'replace',notify:true});
	//console.log("transition ",transitionP);
	
	$scope.changeTab = function(temp) {
		console.log($scope.recordtabs2[temp].path);
		$scope.currenttab2 = $scope.recordtabs2[temp];
		$state.go($scope.recordtabs2[temp].path);
		
	}
	
	$scope.isTabActive = function(tab){
		//console.log('current state '+$state.current.name.substring(0,tab.path.length)+" -- "+($state.current.name.substring(0,tab.path.length) == tab.path),$state.current.name,tab);
		//console.log('ddd '+$state.current.name.length+"  eee "+tab.path.length);
		//if($state.current.name.length < tab.path.length) return "";
		if($state.current.name.substring(0,tab.path.length) == tab.path){
			return "active";
		}
	}
	
	console.log('RecordCtrlEnd',$state);
});

/*
oscarApp.controller('PatientDetailCtrl', function ($scope,$http,$location,$stateParams,demographicService,demo,$state) {
	console.log("in patientDetail Ctrl");
});
*/
