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

oscarApp.controller('RecordCtrl', function ($rootScope,$scope,$http,$location,$stateParams,demographicService,demo,$state,noteService,$timeout,uxService) {
	
	console.log("in patient Ctrl ",demo);
	console.log("in RecordCtrl state params ",$stateParams,$location.search());
	
	$scope.demographicNo = $stateParams.demographicNo;
	$scope.demographic= demo;
	$scope.page = {};
	$scope.hideNote = false;
	
	//this doesn't actually work, hideNote is note showing up in the $stateParams
	if($stateParams.hideNote != null) {
		$scope.hideNote = $stateParams.hideNote;
	}
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
	$scope.recordtabs2 = [];
	/*
	                 	 {id : 0,displayName : 'Details'  ,path : 'record.details'},
	                 	 {id : 1,displayName : 'Summary'  ,path : 'record.summary'},
	                 	 {id : 2,displayName : 'Forms'    ,path : 'record.forms'},
	                 	 {id : 3,displayName : 'Labs/Docs',path : 'partials/eform.jsp'},
	                 	 {id : 4,displayName : 'Rx'       ,path : 'partials/eform.jsp'}];
	*/
	
	$scope.fillMenu = function(){
		uxService.menu($stateParams.demographicNo).then(function(data){
			$scope.recordtabs2 = data;
		});
	}
	
	$scope.fillMenu();
	
	//var transitionP = $state.transitionTo($scope.recordtabs2[0].path,$stateParams,{location:'replace',notify:true});
	//console.log("transition ",transitionP);
	
	$scope.changeTab = function(temp) {
		$scope.currenttab2 = $scope.recordtabs2[temp.id];
		
		if(angular.isDefined(temp.state)){
			if ((temp.state=="consultRequests" || temp.state=="consultResponses") && angular.isDefined(temp.extra)){
				 var params = {demographicId:temp.extra};
				 $state.go(temp.state, params);
			} else {
				$state.go(temp.state);
			}
		}else if(angular.isDefined(temp.url)){
			var rnd = Math.round(Math.random() * 1000);
			win = "win" + rnd;
			window.open(temp.url,win,"scrollbars=yes, location=no, width=1000, height=600","");   
		}
		//console.log($scope.recordtabs2[temp].path);
		
		
	}
	
	$scope.isTabActive = function(tab){
		//console.log('current state '+$state.current.name.substring(0,tab.path.length)+" -- "+($state.current.name.substring(0,tab.path.length) == tab.path),$state.current.name,tab);
		//console.log('ddd '+$state.current.name.length+"  eee "+tab.path.length);
		//if($state.current.name.length < tab.path.length) return "";
	
		if(tab.dropdown){
			return "dropdown";
		}
		
		if(tab.state != null && ($state.current.name.substring(0,tab.state.length) == tab.state)){
			return "active";
		}
		
	}
	
	$scope.$on('$destroy', function(){
		console.log("save the last note!!",$scope.page.encounterNote,noteDirty);
		if(noteDirty){
			noteService.tmpSave($stateParams.demographicNo,$scope.page.encounterNote);
		}
		
	});
	
	//////AutoSave
	var saveIntervalSeconds = 2;

	var timeout = null;
	var saveUpdates = function() {
	    console.log("save",$scope.page.encounterNote);
	    noteService.tmpSave($stateParams.demographicNo,$scope.page.encounterNote); 
	};
	var skipTmpSave = false;
	var noteDirty = false;
	
	var delayTmpSave = function(newVal, oldVal) {
		console.log("whats the val ",(newVal != oldVal));
		if(!skipTmpSave){
		    if (newVal != oldVal) {
		    	noteDirty = true;
		      if (timeout) {
		        $timeout.cancel(timeout);
		      }
		      timeout = $timeout(saveUpdates, saveIntervalSeconds * 1000);
		    }else{
		    	noteDirty= false;
		    }
		}
		skipTmpSave = false; // only skip once
	  };
	$scope.$watch('page.encounterNote.note', delayTmpSave);
	
	//////
	
	
	
	
		
	// Note Input Logic
	$scope.toggleNote = function() {
		if ($scope.hideNote == true) {
			$scope.hideNote = false;
		} else {
			$scope.hideNote = true;
		}
	};

		
	$scope.saveNote = function(){
		console.log("This is the note"+$scope.page.encounterNote);
		$scope.page.encounterNote.observationDate = new Date(); 
		noteService.saveNote($stateParams.demographicNo,$scope.page.encounterNote).then(function(data) {
			$rootScope.$emit('noteSaved',data);
			skipTmpSave = true;
			$scope.page.encounterNote = data;
			console.debug('whats the index',data);
			if($scope.page.encounterNote.isSigned){
				$scope.hideNote = false;
				$scope.getCurrentNote(false);
			}
	    });
	};
	
	$scope.saveSignNote = function(){
		$scope.page.encounterNote.isSigned = true;
		$scope.saveNote() ;
	}
	
	$scope.saveSignVerifyNote = function(){
		$scope.page.encounterNote.isVerified = true;
		$scope.page.encounterNote.isSigned = true;
		$scope.saveNote() ;
	}
	
	console.log('RecordCtrlEnd',$state);
	
	$scope.page.currentNoteConfig = {};


	$scope.getCurrentNote = function(showNoteAfterLoadingFlag) {
		noteService.getCurrentNote($stateParams.demographicNo,$location.search()).then(function(data) {
			$scope.page.encounterNote = data;
			console.log($scope.page.encounterNote );
			//$scope.hideNote = true;
			$scope.hideNote = showNoteAfterLoadingFlag;
			$rootScope.$emit('currentlyEditingNote',$scope.page.encounterNote);
	    });
	};
	
	$scope.getCurrentNote(true);
	
	
	
	 $scope.editNote = function(note){
	    	$rootScope.$emit('',note);
	    }
	    
	    
	 $rootScope.$on('loadNoteForEdit', function(event,data) {
	    	console.log('loadNoteForEdit ',data);
	    	$scope.page.encounterNote = data;
	    	//Need to check if note has been saved yet.
	    	$scope.hideNote = true;
	    	$rootScope.$emit('currentlyEditingNote',$scope.page.encounterNote);
	 });

	
});

