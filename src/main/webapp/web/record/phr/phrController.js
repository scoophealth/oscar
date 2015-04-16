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
oscarApp.controller('PHRCtrl', function ($scope,$http,$location,$stateParams,demographicService,demo,$state,formService) {
	console.log("phr ctrl ",$stateParams,$state);
	
	$scope.page = {};
	$scope.page.currentFormList = [];
	$scope.page.currentForm = {};
	$scope.page.currentlistId = 0;
	
	console.log("What is the state "+$state.params.type+" : "+angular.isUndefined($state.params.type)+" id "+$state.params.id,$state); // Use this to load the current form if the page is refreshed
	
	
	$scope.page.formlists = [{id:0,label:'Data'}];//,{id:1,label:'Msgs'}];  //Need to get this from the server.
	
	$scope.page.currentFormList[0] = [{id:0, name:'Glucose', url:'../oscarEncounter/myoscar/measurements_glucose.do?type=GLUCOSE&demoNo=',type:'frame'},{id:1, name:'BP', url:'../oscarEncounter/myoscar/measurements_blood_pressure.do?type=BLOOD_PRESSURE&demoNo=',type:'frame'},{id:2,name:'Height & Weight', url:'../oscarEncounter/myoscar/measurements_height_and_weight.do?type=HEIGHT_AND_WEIGHT&demoNo=',type:'frame' },{id:3,name:'Open Record', url:'../demographic/viewPhrRecord.do?demographic_no=',type:'window' },{id:4,name:'Send a Message', url:'../phr/PhrMessage.do?method=createMessage&demographicNo=',type:'window' },{id:5,name:'Data Sync', url:'../admin/oscar_myoscar_sync_config_redirect.jsp?uselessParam=',type:'window' }]; 
	

	
	/*$scope.page.formlists.forEach(function (item, index) {
		console.log('What is the item ',item);
		formService.getAllFormsByHeading($stateParams.demographicNo,item.label).then(function(data) {
	        console.debug('whats the index'+index,data);
	        $scope.page.currentFormList[index] = data.list;
	    });
	});
	*/
	
	$scope.changeTo = function(listId){
		$scope.page.currentlistId = listId;
		console.log('set currentlist to '+listId);
		if(listId == 0){
			formService.getAllFormsByHeading($stateParams.demographicNo,'Completed').then(function(data) {
		        console.debug('whats the index'+0,data);
		        $scope.page.currentFormList[0] = data.list;
		    });
		}
	}
	
	$scope.viewFormState = function(item){
		
		while(document.getElementById('formInViewFrame').hasChildNodes()){
			document.getElementById('formInViewFrame').removeChild( document.getElementById('formInViewFrame').firstChild ) 
		}
		
		var url = item.url + $stateParams.demographicNo;
		console.log("item",item);
		
		if(item.type == 'frame'){
			$scope.page.currentForm = item;
			var pymParent = new pym.Parent('formInViewFrame',url, {});
			$scope.pymParent = pymParent;
		}else{
			var rnd = Math.round(Math.random() * 1000);
			win = "win" + rnd;
			window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");  
			return;
		}
		
		
	}
	
	
	
	/*
	 * Used to make the left side list tab be active
	 */
	$scope.getListClass = function(listId){
		if(listId === $scope.page.currentlistId ){
			return "active";
		}
	}
	
	/*
	 * Used to mark which form is active.
	 */
	$scope.getActiveFormClass = function(item){
		if(item.type == $scope.page.currentForm.type && item.id == $scope.page.currentForm.id && angular.isDefined(item.id)){
			return "active";
		}else if(item.type == $scope.page.currentForm.type && angular.isUndefined(item.id) && item.formId == $scope.page.currentForm.formId  ){
			return "active";
		}
	}
	
	function handleError(errorMessage){
		console.log(errorMessage);
	}

	
	
	
	
	/*
	 * This still needs to be tested
	 */
	$scope.keypress = function(event){
		if(event.altKey == true && event.key == "Up"){
			console.log("up",event);
			console.log($scope.page.currentFormList[$scope.page.currentlistId].indexOf($scope.page.currentForm));
			var currIdx = $scope.page.currentFormList[$scope.page.currentlistId].indexOf($scope.page.currentForm);
			if(currIdx > 0){
				$scope.page.currentForm = $scope.page.currentFormList[$scope.page.currentlistId][currIdx-1];
				$scope.viewFormState($scope.page.currentForm);
			}
		}else if (event.altKey == true && event.key == "Down"){
			console.log("down",event);
			var currIdx = $scope.page.currentFormList[$scope.page.currentlistId].indexOf($scope.page.currentForm);
			console.log(currIdx,$scope.page.currentFormList[$scope.page.currentlistId].length);
			if(currIdx <= $scope.page.currentFormList[$scope.page.currentlistId].length){
				$scope.page.currentForm = $scope.page.currentFormList[$scope.page.currentlistId][currIdx+1];
				$scope.viewFormState($scope.page.currentForm);
			}
		}else{
			console.log("keypress",event.altKey,event.key,event);
		}
	}
	
	
});
