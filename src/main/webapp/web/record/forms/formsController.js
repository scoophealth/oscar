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
oscarApp.controller('FormCtrl', function ($scope,$http,$location,$stateParams,demographicService,demo,$state,formService) {
	console.log("form ctrl ",$stateParams,$state);
	
	$scope.page = {};
	$scope.page.currentFormList = [];
	$scope.page.currentForm = {};
	$scope.page.currentlistId = 0;
	
	console.log("What is the state "+$state.params.type+" : "+angular.isUndefined($state.params.type)+" id "+$state.params.id,$state); // Use this to load the current form if the page is refreshed
	
	
	
	
	
	$scope.page.formlists = [{id:0,label:'Completed'},{id:1,label:'Library'}];  //Need to get this from the server.
	
	$scope.page.formlists.forEach(function (item, index) {
		console.log('What is the item ',item);
		formService.getAllFormsByHeading($stateParams.demographicNo,item.label).then(function(data) {
	        console.debug('whats the index'+index,data);
	        $scope.page.currentFormList[index] = data.list;
	    });
	});
	
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
		
		var url = '';
		console.log("item",item);
		if(item.type == 'eform' && angular.isDefined(item.id)){
			url = '../eform/efmshowform_data.jsp?fdid='+item.id;
			$state.go('record.forms.existing',{demographicNo:$stateParams.demographicNo, type: 'eform' ,id:item.id});
		}else if(item.type == 'eform'  && angular.isUndefined(item.id)){
			url = '../eform/efmformadd_data.jsp?fid='+item.formId+'&demographic_no='+ $stateParams.demographicNo;
			//$state.go('record.forms.new',{demographicNo:$stateParams.demographicNo, type: 'eform' ,id:item.formId});
			
			var rnd = Math.round(Math.random() * 1000);
			win = "win" + rnd;
			
			window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");  
			return;
		}

		$scope.page.currentForm = item;
		var pymParent = new pym.Parent('formInViewFrame',url, {});
		$scope.pymParent = pymParent;  
		
		
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

	
	if($state.current.name == 'record.forms.new'){
		var item = {};
		item.type = $state.params.type;
		item.formId = $state.params.id;
		$scope.viewFormState(item);
		$scope.changeTo(1);
		
	}else if($state.current.name == 'record.forms.existing'){
		var item = {};
		item.type = $state.params.type;
		item.id = $state.params.id;
		$scope.viewFormState(item);
		$scope.changeTo(0);
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
