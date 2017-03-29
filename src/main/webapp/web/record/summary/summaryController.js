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
oscarApp.controller('SummaryCtrl', function ($rootScope,$scope,$http,$location,$stateParams,$state,$filter,$modal,$interval,user,noteService,summaryService,securityService) {
	console.log("in summary Ctrl ",$stateParams);

	$scope.page = {};
	$scope.page.columnOne = {};
	$scope.page.columnOne.modules = {};

	$scope.page.columnThree = {};
	$scope.page.columnThree.modules = {};
	$scope.page.selectedNotes = [];

	$scope.page.notes = {};
	$scope.index = 0;
	$scope.page.notes = {};
	$scope.page.notes.notelist = [];
	$scope.busy = false;
	$scope.page.noteFilter = {};
	$scope.page.currentFilter = 'none';
	$scope.page.onlyNotes = false;
    
	//get access rights
	securityService.hasRight("_eChart", "r", $stateParams.demographicNo).then(function(data){
		$scope.page.canRead = data;
	});
	securityService.hasRight("_eChart", "u", $stateParams.demographicNo).then(function(data){
		$scope.page.cannotChange = !data;
	});
	securityService.hasRight("_eChart", "w", $stateParams.demographicNo).then(function(data){
		$scope.page.cannotAdd = !data;
	});
	
	//disable click and keypress if user only has read-access
	$scope.checkAction = function(event){
		if ($scope.page.cannotChange) {
			event.preventDefault();
			event.stopPropagation();
		}
	}
   
    // Note list filtering functions   
    $scope.setOnlyNotes = function(){
	   if($scope.page.onlyNotes){
		   $scope.page.onlyNotes = false;
	   }else{
		   $scope.page.onlyNotes = true;
	   }
	   console.log("$scope.page.onlyNotes ",$scope.page.onlyNotes );
   	}
   
   	$scope.isOnlyNotesStatus = function(){
	   if($scope.page.onlyNotes){
		   return "active";
	   }else{
		   return "";
	   }

   	}
   	
   	
   	$scope.openRevisionHistory = function(note){
   		//var rnd = Math.round(Math.random() * 1000);
		win = "revision";
		var url = "../CaseManagementEntry.do?method=notehistory&noteId=" + note.noteId;
		window.open(url,win,"scrollbars=yes, location=no, width=647, height=600","");   			
   	}

   	$scope.openRx = function(demoNo){
		win = "Rx"+demoNo;
		var url = "../oscarRx/choosePatient.do?demographicNo=" + demoNo;
		window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");   			
   	}
   	
   	$scope.openAllergies = function(demoNo){
		win = "Allergy"+demoNo;
		var url = "../oscarRx/showAllergy.do?demographicNo=" + demoNo;
		window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");   
		return false;
   	}

   	$scope.openPreventions = function(demoNo){
		win = "prevention"+demoNo;
		var url = "../oscarPrevention/index.jsp?demographic_no=" + demoNo;
		window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");   
		return false;
   	}

	$scope.openDx = function(demoNo){
		win = "Dx"+demoNo;
		var url = "../oscarResearch/oscarDxResearch/setupDxResearch.do?quickList=&demographicNo=" + demoNo;
		window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");
	}



	$scope.isCurrentStatus = function(stat){
	   //console.log("stat",stat);
	   if(stat == $scope.page.currentFilter){
		   return "active";
	   }else{
		   return "";
	   }

   	}
   
   	// How do we handle showing what filter has been selected???
   	$scope.changeNoteFilter = function(){
	   $scope.index = 0;
	   $scope.page.noteFilter.filterProviders = [user.providerNo]; //<- need to fix this?
	   $scope.page.notes.notelist = [];
	   $scope.page.currentFilter = 'Just My Notes';
	   $scope.addMoreItems();
   	}
   
   	$scope.removeFilter = function(){
	   $scope.index = 0;
	   $scope.page.noteFilter = {};
	   $scope.page.notes.notelist = [];
	   $scope.addMoreItems();
	   $scope.page.currentFilter = 'none';
	   
   	}
   
   	
   	//Note display functions
    $scope.addMoreItems = function(){
    	console.log($scope.busy);
    	if ($scope.busy) return;
    	
    	$scope.busy = true;
    	
    	noteService.getNotesFrom($stateParams.demographicNo,$scope.index,20,$scope.page.noteFilter).then(function(data) {
            console.debug('whats the data',angular.isUndefined(data.notelist),data.notelist);
            if(angular.isDefined(data.notelist)){
          	  //$scope.page.notes = data;
  	          if(data.notelist instanceof Array){
  	  			console.log("ok its in an array",$scope.busy);
  	  				for (var i = 0; i < data.notelist.length; i++) {
  	  					$scope.page.notes.notelist.push(data.notelist[i]);
  	  				}
  	          }else{
  	        	$scope.page.notes.notelist.push(data.notelist);
  	  		  }
  	          $scope.index =  $scope.page.notes.notelist.length ;
            }
            $scope.busy = false;
        },
    		function(errorMessage){
    	       console.log("notes:"+errorMessage);
    	       $scope.error=errorMessage;
    	       $scope.busy = false;
     		}
        );
    	
    };
    
    //$scope.addMoreItems();
    
    $scope.editNote = function(note){
    	$rootScope.$emit('loadNoteForEdit',note);
    }
    
    $scope.page.currentEditNote = {};
    
    $scope.isNoteBeingEdited = function(note){
    	
    	if(note.uuid == $scope.page.currentEditNote.uuid ){
    		return "noteInEdit";
    	}
    	
    	return ""
    }
    
    $rootScope.$on('currentlyEditingNote',function(event,data) {
    	$scope.page.currentEditNote = data;
    });
    
    
    $rootScope.$on('noteSaved', function(event,data) {
    	console.log('new data coming in',data);
    	var noteFound = false;
    	for (var notecount = 0; notecount < $scope.page.notes.notelist.length; notecount++) {
    		if(data.uuid == $scope.page.notes.notelist[notecount].uuid){
    			console.log('uuid '+data.uuid+' notecount '+notecount,data,$scope.page.notes.notelist[notecount]);
    			$scope.page.notes.notelist[notecount] = data;
    			noteFound = true;
    			break;
    		}
    	}
    	
    	if(noteFound == false){
    		$scope.page.notes.notelist.unshift(data);
    	}
    	$scope.index =  $scope.page.notes.notelist.length ;
	 });
    
    
        
    //Note display functions   
    $scope.setColor = function(note){
    	if(note.eformData){
    		return { 'background-color': '#DFF0D8' };
    	}else if(note.document){
    		return { 'background-color': '#476BB3', 'color' : 'white' };
    	}else if(note.rxAnnotation){
    		return { 'background-color': 'lightgrey' };
    	}else if(note.encounterForm){
    		return { 'background-color': '#917611' };
		}else if(note.invoice){
			return { 'background-color': '#red' };
		}else if(note.ticklerNote){
			return { 'background-color': '#FF6600' };
		}else if(note.cpp){
			return { 'background-color': '#996633', 'color' : 'white' };
		} 	
    };
    
    $scope.showNoteHeader = function(note){
    	if($scope.page.onlyNotes){
    		if(note.document || note.rxAnnotation || note.eformData || note.encounterForm || note.invoice || note.ticklerNote || note.cpp){
    			return false;
    		}
    	} 
    	return true;
    }
    
    $scope.showNote = function(note){
    	if($scope.page.onlyNotes){
    		if(note.document || note.rxAnnotation || note.eformData || note.encounterForm || note.invoice || note.ticklerNote || note.cpp){
    			return false;
    		}
    	} 
    	
    	if(note.eformData || note.document ){
    		return false;
    	}
    	return true;
    };
    
    
    $scope.firstLine = function(note){
    	var firstL = note.note.trim().split('\n')[0];
    	var dateStr = $filter('date')(note.observationDate, 'dd-MMM-yyyy');
    	dateStr = "["+dateStr;
    	//console.log(firstL + " --"+dateStr+"-- " + firstL.indexOf(dateStr));
    	if(firstL.indexOf(dateStr) == 0 ){
    		firstL = firstL.substring(dateStr.length);
    	}
    	return firstL;
    };

    $scope.trackerUrl="";

    $scope.getTrackerUrl = function(demographicNo) {
      $scope.trackerUrl = '../oscarEncounter/oscarMeasurements/HealthTrackerPage.jspf?template=tracker&demographic_no=' + demographicNo + '&numEle=4&tracker=slim';    
    };
    
var initialDisplayLimit = 5;  
$scope.toggleList = function(mod){
	i = initialDisplayLimit; 
	
	if(mod.summaryItem.length>i){
		if(mod.displaySize>i){
			mod.displaySize = i;
		}else{
			mod.displaySize = mod.summaryItem.length;
		}
	}
}

$scope.showMoreItems = function(mod){

	if(!angular.isDefined(mod.summaryItem)){
		return false;
	}
	
	if(mod.summaryItem.length == 0){
		return false;
	}
	
	return true;
}

$scope.showMoreItemsSymbol = function(mod){	
	if(!angular.isDefined(mod.summaryItem)){
		return "";
	}
	
	if ( (mod.displaySize < mod.summaryItem.length) && mod.displaySize == initialDisplayLimit ) {
		return "glyphicon glyphicon-chevron-down hand-hover pull-right";
	}else if ( (mod.displaySize == mod.summaryItem.length) && mod.displaySize != initialDisplayLimit ){
		return "glyphicon glyphicon-chevron-up hand-hover pull-right";	
	}else if ( mod.summaryItem.length <= initialDisplayLimit ) {
		return "glyphicon glyphicon-chevron-down glyphicon-chevron-down-disabled pull-right";	
	}else{
		return "";
	}

}

function getLeftItems(){
	summaryService.getSummaryHeaders($stateParams.demographicNo,'left').then(function(data){
		  console.log("left",data);
		  $scope.page.columnOne.modules = data;
	      fillItems($scope.page.columnOne.modules);
    	},
    	function(errorMessage){
	       console.log("left"+errorMessage);
	       $scope.error=errorMessage;
    	}
	);
};

getLeftItems();


function getRightItems(){
	summaryService.getSummaryHeaders($stateParams.demographicNo,'right').then(function(data){
		  console.log("right",data);
		  $scope.page.columnThree.modules = data;
	      fillItems($scope.page.columnThree.modules);
    	},
    	function(errorMessage){
	       console.log("left"+errorMessage);
	       $scope.error=errorMessage;
    	}
	);
};

getRightItems();

var summaryLists = {};

function fillItems(itemsToFill){
	for (var i = 0; i < itemsToFill.length; i++) {
		console.log(itemsToFill[i].summaryCode);
		summaryLists[itemsToFill[i].summaryCode] = itemsToFill[i];
	 
		summaryService.getFullSummary($stateParams.demographicNo,itemsToFill[i].summaryCode).then(function(data){
			console.log("FullSummary returned ",data);
				if(angular.isDefined(data.summaryItem)){
	 				if(data.summaryItem instanceof Array){
	 					summaryLists[data.summaryCode].summaryItem = data.summaryItem;
	 				}else{
	 					summaryLists[data.summaryCode].summaryItem = [data.summaryItem];
	 				}
				}
			},
			function(errorMessage){
				console.log("fillItems"+errorMessage); 
			}
			 
		);
	}
}


editGroupedNotes = function(size,mod,action){

	var modalInstance = $modal.open({
		templateUrl: 'record/summary/groupNotes.jsp',
		controller: GroupNotesCtrl,
		size: size,
		resolve: {
			mod: function () {
				return mod;
			},
			action: function (){
				return action;
			},
			user: function (){
				return user;
			}
		}
	});
	
	modalInstance.result.then(function (selectedItem) {
		console.log(selectedItem);
	}, function () {
		if (editingNoteId!=null) {
			noteService.removeEditingNoteFlag(editingNoteId, user.providerNo);
			$interval.cancel(itvSet);
			itvSet = null;
			$interval.cancel(itvCheck);
			itvCheck = null;
			editingNoteId = null;
		}
		
		console.log('Modal dismissed at: ' + new Date());
	});
	
	console.log($('#myModal'));
}


$scope.gotoState = function(item,mod,itemId){
	
	if(item=="add"){
		editGroupedNotes('lg',mod,null);
		
	}else if(item.action == 'add' && item.type == 'dx_reg'){
		
		editGroupedNotes('lg',mod,itemId);
		
	}else if(item.type == 'lab' || item.type == 'document'  || item.type == 'rx'|| item.type == 'allergy' || item.type == 'prevention' || item.type == 'dsguideline' || item.type =='diseases' ){

		if(item.type == 'rx'){
			win = "Rx" + $stateParams.demographicNo;
		}else if(item.type == 'allergy' ){
			win = "Allergy" + $stateParams.demographicNo;
		}else if(item.type == 'prevention'){
			win = "prevention" + $stateParams.demographicNo;
		}else if(item.type == 'diseases'){
			win = "diseases" + $stateParams.demographicNo;
		}else{
			//item.type == 'lab' || item.type == 'document'
			//var rnd = Math.round(Math.random() * 1000);
			win = "win_item.type_";
		}
		
		window.open(item.action,win,"scrollbars=yes, location=no, width=900, height=600","");  
		return false;
	}else if(item.action == 'action'){
		editGroupedNotes('lg',mod,itemId);

	}else{	
		$state.transitionTo(item.action,{demographicNo:$stateParams.demographicNo, type: item.type ,id: item.id},{location:'replace',notify:true});
	}

};

	 
	 $scope.showPrintModal = function(mod,action){
		 var size = 'lg';
		 var modalInstance = $modal.open({
		      templateUrl: 'record/print.jsp',
		      controller: RecordPrintCtrl,
		      size: size,
		      resolve: {
		          mod: function () {
		            return mod;
		          },
		          
		          action: function (){
		        	  return action;
		          }
		        }
		    });
		
		modalInstance.result.then(function (selectedItem) {
		      console.log(selectedItem);
		      
		    }, function () {
		      console.log('Modal dismissed at: ' + new Date());
		    });
	 }

});


GroupNotesCtrl = function ($scope,$modal,$modalInstance,mod,action,user,$stateParams,$state,$interval,noteService,securityService,diseaseRegistryServices){


	$scope.page = {};
	$scope.page.title = mod.displayName;
	$scope.page.items = mod.summaryItem;
	$scope.page.quickLists = [];

	//$scope.action = action;
	$scope.page.code = mod.summaryCode;
	
	$scope.groupNotesForm = {assignedCMIssues:[]};
	$scope.groupNotesForm.encounterNote = {position:1};
	
	
	//set hidden which can can move out of hidden to $scope values
	var now = new Date();
    $scope.groupNotesForm.annotation_attrib = "anno"+now.getTime();

    
	//get access rights
	securityService.hasRight("_eChart", "u", $stateParams.demographicNo).then(function(data){
		$scope.page.cannotChange = !data;
	});
	
	diseaseRegistryServices.getQuickLists().then(function(data){
		console.log(data);
		$scope.page.quickLists = data;
	});
	
	$scope.addDxItem = function(item){
		for(var x=0;x<$scope.groupNotesForm.assignedCMIssues.length;x++) {
    		if($scope.groupNotesForm.assignedCMIssues[x].issue.code === item.code && $scope.groupNotesForm.assignedCMIssues[x].issue.type === item.codingSystem) {
    			return;
    		}
    	}
		
		diseaseRegistryServices.findLikeIssue(item).then(function(response){
    		var cmIssue = {acute:false,certain:false,issue:response,issue_id:response.issueId,major:false,resolved:false,unsaved:true};
        	$scope.groupNotesForm.assignedCMIssues.push(cmIssue);
    	});
		
		
	}
	
	//disable click and keypress if user only has read-access
	$scope.checkAction = function(event){
		if ($scope.page.cannotChange) {
			event.preventDefault();
			event.stopPropagation();
		}
	}
    
    displayIssueId = function(issueCode){
    	noteService.getIssueId(issueCode).then(function(data){
    		$scope.page.issueId = data.id;
    	},function(reason){
		   	alert(reason);
		});
	}

	displayIssueId($scope.page.code);

	displayGroupNote = function(item,itemId){
			if($scope.page.items[itemId].noteId != null){
				noteService.getIssueNote($scope.page.items[itemId].noteId).then(function(iNote){
					//$scope.master = angular.copy( "iNote----" +  JSON.stringify(iNote) );
					$scope.groupNotesForm.encounterNote = iNote.encounterNote;
					$scope.groupNotesForm.groupNoteExt = iNote.groupNoteExt;
					$scope.groupNotesForm.assignedCMIssues = iNote.assignedCMIssues;
					
					$scope.groupNotesForm.assignedCMIssues = [];
					
					if(iNote.assignedCMIssues instanceof Array) {
						$scope.groupNotesForm.assignedCMIssues = iNote.assignedCMIssues;
					} else {
						if(iNote.assignedCMIssues != null) {
							$scope.groupNotesForm.assignedCMIssues.push(iNote.assignedCMIssues);
						}
					}
					
					action = itemId;
					$scope.setAvailablePositions();
									
					$scope.removeEditingNoteFlag();
									
					if($scope.groupNotesForm.encounterNote.position<1){
						$scope.groupNotesForm.encounterNote.position=1;
					}
					
				},function(reason){
					alert(reason);
				});
			}else if($scope.page.items[itemId].type === "dx_reg"){
				$scope.groupNotesForm.assignedCMIssues = [];
				diseaseRegistryServices.findLikeIssue($scope.page.items[itemId].extra).then(function(response){
		    		var cmIssue = {acute:false,certain:false,issue:response,issue_id:response.issueId,major:false,resolved:false,unsaved:true};
		    		console.log("find like issue ", cmIssue, response);
		        	$scope.groupNotesForm.assignedCMIssues.push(cmIssue);
		        	$scope.groupNotesForm.encounterNote = {};
		        	$scope.groupNotesForm.groupNoteExt = {};
		        	$scope.groupNotesForm.encounterNote = {position:1};
		        	action = itemId;
		    	});
			}
	};
	
	//action is NULL when new , action is some id when not
	if(action!=null){
		displayGroupNote($scope.page.items,action);
	}else{
		//new entry
	}
	
	$scope.setAvailablePositions = function() {
		$scope.availablePositions = [];
		if($scope.page.items == null || $scope.page.items.length == 0) {
			$scope.availablePositions.push(1);
		} else {
			var x=0;
			for(x=0;x<$scope.page.items.length;x++) {
				$scope.availablePositions.push(x+1);
			}
			if(action == null) {
				$scope.availablePositions.push(x+1);
			}
		}
	}
	
	$scope.setAvailablePositions();

	$scope.changeNote = function(item, itemId){
		return displayGroupNote(item,itemId);
	};

	$scope.saveGroupNotes = function(){
		if($scope.groupNotesForm.encounterNote.noteId==null){
			$scope.groupNotesForm.encounterNote.noteId=0;
		}

		$scope.groupNotesForm.encounterNote.noteId = $scope.groupNotesForm.encounterNote.noteId; //tmp crap
		$scope.groupNotesForm.encounterNote.cpp = true;
		$scope.groupNotesForm.encounterNote.editable = true;
		$scope.groupNotesForm.encounterNote.isSigned = true;
		$scope.groupNotesForm.encounterNote.observationDate = new Date();
		$scope.groupNotesForm.encounterNote.appointmentNo=$stateParams.appointmentNo; //TODO: make this dynamic so it changes on edit
		$scope.groupNotesForm.encounterNote.encounterType="";
		$scope.groupNotesForm.encounterNote.encounterTime="";
		
		$scope.groupNotesForm.encounterNote.summaryCode = $scope.page.code; //'ongoingconcerns';

		$scope.groupNotesForm.assignedIssues = [];
		
		noteService.saveIssueNote($stateParams.demographicNo, $scope.groupNotesForm).then(function(data){
    		$modalInstance.dismiss('cancel');
    		$state.transitionTo($state.current, $stateParams, { reload: true, inherit: false, notify: true });

	    },function(reason){
	    	alert(reason);
	    });
	}
	
	/*
	 * handle concurrent note edit - EditingNoteFlag
	 */
	$scope.doSetEditingNoteFlag = function(){
		noteService.setEditingNoteFlag(editingNoteId, user.providerNo).then(function(resp){
			if (!resp.success) {
				if (resp.message=="Parameter error") alert("Parameter Error: noteUUID["+editingNoteId+"] userId["+user.providerNo+"]");
				else alert("Warning! Another user is editing this note now.");
			}
		});
	}
 
	$scope.setEditingNoteFlag = function(){
		if ($scope.groupNotesForm.encounterNote.uuid==null) return;
		
		$scope.removeEditingNoteFlag(); //remove any previous flag actions
		editingNoteId = $scope.groupNotesForm.encounterNote.uuid;
		
		itvSet = $interval($scope.doSetEditingNoteFlag(), 30000); //set flag every 5 min
		itvCheck = $interval(function(){
			noteService.checkEditNoteNew(editingNoteId, user.providerNo).then(function(resp){
				if (!resp.success) { //someone else wants to edit this note
					alert("Warning! Another user tries to edit this note. Your update may be replaced by later revision(s).");
					
					//cancel 10sec check after 1st time warning when another user tries to edit this note
					$interval.cancel(itvCheck);
					itvCheck = null;
				}
			});
		}, 10000); //check for new edit every 10 sec
	}
	
	$scope.removeEditingNoteFlag = function(){
		if (editingNoteId!=null) {
			noteService.removeEditingNoteFlag(editingNoteId, user.providerNo);
			$interval.cancel(itvSet);
			$interval.cancel(itvCheck);
			itvSet = null;
			itvCheck = null;
			editingNoteId = null;
		}
	}
	
	
	$scope.removeIssue = function(i) {
		i.unchecked=true;
	}
	$scope.restoreIssue = function(i) {
		i.unchecked=false;
	}

	$scope.archiveGroupNotes = function(){
		//$scope.master = angular.copy($scope.groupNotesForm);
		$scope.groupNotesForm.encounterNote.archived = true;
		$scope.saveGroupNotes();
	}
		
	$scope.cancel = function () {
  		$modalInstance.dismiss('cancel');
  	};

	//temp load into pop-up
   	$scope.openRevisionHistory = function(note){
		var rnd = Math.round(Math.random() * 1000);
		win = "win" + rnd;
		var url = "../CaseManagementEntry.do?method=notehistory&noteId=" + note.noteId;
		window.open(url,win,"scrollbars=yes, location=no, width=647, height=600","");   			
   	}

    $scope.searchIssues  = function(term) {
    	var search = {'term':term};
    	return noteService.searchIssues(search,0,100).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.content.length;x++) {
    			resp.push({issueId:response.content[x].id,code: response.content[x].description + '(' + response.content[x].code + ')'});
    		}
    		if(response.total > response.content.length) {
    			//warn user there's more results somehow?
    		}
    		return resp;
    	});
    }
    
    $scope.assignIssue = function(item, model, label) {
    	for(var x=0;x<$scope.groupNotesForm.assignedCMIssues.length;x++) {
    		if($scope.groupNotesForm.assignedCMIssues[x].issue.id == model) {
    			return;
    		}
    	}
    	
    	noteService.getIssue(model).then(function(response){
    		var cmIssue = {acute:false,certain:false,issue:response,issue_id:item.issueId,major:false,resolved:false,unsaved:true};
        	$scope.groupNotesForm.assignedCMIssues.push(cmIssue);
    	});
    }
    
    $scope.isSelected = function(item) {
    	if(item.id == action) {
    		return "group-note-selected";
    	}
    }
    
    $scope.addToDxRegistry = function(issue){
    	diseaseRegistryServices.addToDxRegistry($stateParams.demographicNo,issue).then(function(data){
    		console.log(data);
    	});
    	
    }
    
};
var itvSet = null;
var itvCheck = null;
var editingNoteId = null;

RecordPrintCtrl = function($scope,$modal,$modalInstance,mod,action,$stateParams,summaryService,$filter){
	
	$scope.pageOptions = {};
	$scope.pageOptions.printType = {};
	$scope.pageOptions.dates = {};
	$scope.page = {}; 
	$scope.page.selectedWarning = false;
	
	/* 
	 *If mod length > 0 than the user has selected a note. = Default to Note
	 *Other wise default to All 
	 */
	var atleastOneSelected = false;
	for(var i = 0; i < mod.length; i++){
		if(mod[i].isSelected){
			atleastOneSelected = true;
			i = mod.length;
		}
	}
	
	if(atleastOneSelected){
		console.log("mod len ",mod.length);
		$scope.pageOptions.printType = 'selected';
	}else{
		console.log("printType = all");
		$scope.pageOptions.printType = 'all';
	}
	
	$scope.printToday = function(){
		$scope.pageOptions.printType = 'dates';
		var date = new Date();
		$scope.pageOptions.dates.start = date;
		$scope.pageOptions.dates.end = date;
	}
	
	$scope.cancelPrint = function(){
		$modalInstance.dismiss('cancel');
	}
	
	$scope.clearPrint = function(){
		$scope.pageOptions = {};
		$scope.pageOptions.printType = {};
	}
	
	
	$scope.sendToPhr = function(){
		var queryString = "demographic_no="+$stateParams.demographicNo;
		queryString = queryString + "&module=echart";

		if($scope.pageOptions.printType == 'all'){
			queryString = queryString + '&notes2print=ALL_NOTES';
		}else if ($scope.pageOptions.printType == 'selected'){
			//get array
			var selectedList = [];
			for(var i = 0; i < mod.length; i++){
				if(mod[i].isSelected){
					selectedList.push(mod[i].noteId);
				}
			}
			queryString = queryString + '&notes2print='+selectedList.join();
		}else if($scope.pageOptions.printType == 'dates'){
			queryString = queryString + '&notes2print=ALL_NOTES';
			queryString = queryString + '&startDate='+$scope.pageOptions.dates.start.getTime();
			queryString = queryString + '&endDate='+$scope.pageOptions.dates.end.getTime();
		}
		
		if($scope.pageOptions.cpp){
			queryString = queryString + '&printCPP=true';
		}
		if($scope.pageOptions.cpp){
			queryString = queryString + '&printRx=true';
		}
		if($scope.pageOptions.cpp){
			queryString = queryString + '&printLabs=true';
		}
		console.log("QS"+queryString);
		
		if($scope.pageOptions.printType === 'selected' && selectedList.length ==0 ){
			$scope.page.selectedWarning = true;
			return;
		}else{
			$scope.page.selectedWarning = false;
		}
		
		window.open('../SendToPhr.do?'+queryString,'_blank');
	}
	
	$scope.print = function(){
		//console.log('processList',mod);
		console.log($scope.pageOptions);
		var selectedList = [];
		for(var i = 0; i < mod.length; i++){
			if(mod[i].isSelected){
				selectedList.push(mod[i].noteId);
			}
		}
		console.log("selected list",selectedList);
				
		if($scope.pageOptions.printType === 'selected' && selectedList.length ==0 ){
			$scope.page.selectedWarning = true;
			return;
		}else{
			$scope.page.selectedWarning = false;
		}
		
		$scope.pageOptions.selectedList = selectedList;
		var ops = encodeURIComponent(JSON.stringify($scope.pageOptions));
		window.open('../ws/rs/recordUX/'+$stateParams.demographicNo+'/print?printOps='+ops,'_blank');
	
		
		
	}
};
