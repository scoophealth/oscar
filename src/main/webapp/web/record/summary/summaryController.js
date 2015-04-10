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
oscarApp.controller('SummaryCtrl', function ($rootScope,$scope,$http,$location,$stateParams,$state,$filter,$modal,user,noteService,summaryService) {
	console.log("in summary Ctrl ",$stateParams);

	$scope.page = {};
	$scope.page.columnOne = {};
	$scope.page.columnOne.modules = {};

	$scope.page.columnThree = {};
	$scope.page.columnThree.modules = {};

	$scope.page.notes = {};
	$scope.index = 0;
	$scope.page.notes = {};
	$scope.page.notes.notelist = [];
	$scope.busy = false;
	$scope.page.noteFilter = {};
	$scope.page.currentFilter = 'none';
	$scope.page.onlyNotes = false;
    
   
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
   		console.log("jhistory",note);
		var rnd = Math.round(Math.random() * 1000);
		win = "win" + rnd;
		var url = "../CaseManagementEntry.do?method=notehistory&noteId=" + note.noteId;
		window.open(url,win,"scrollbars=yes, location=no, width=647, height=600","");   			
   	}

   	$scope.openRx = function(demoNo){
		var rnd = Math.round(Math.random() * 1000);
		win = "win" + rnd;
		var url = "../oscarRx/choosePatient.do?demographicNo=" + demoNo;
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
    
  



$scope.expandlist = function(mod){
	console.log(mod);
	mod.displaySize = mod.summaryItem.length;
	//$scope.documentlabsSize = $scope.documentlabs.length;
}

$scope.showMoreDocuments = function(mod){
	console.log('showMoreDocuments',mod);
	if(!angular.isDefined(mod.summaryItem)){
		return false;
	}
	
	if(mod.summaryItem.length == 0){
		return false;
	}
	
	
	//if ( mod.displaySize < mod.summaryItem.length) {
	//	return true;
	//}
	return true;
	
}
$scope.showMoreDocumentsSymbol = function(mod){
	if(!angular.isDefined(mod.summaryItem)){
		return "";
	}
	console.log("mod symbol output ",mod.displaySize, mod.summaryItem.length,mod);
	if ( mod.displaySize < mod.summaryItem.length) {
		return "glyphicon glyphicon-chevron-down pull-right";
	}else{
		return "glyphicon glyphicon-chevron-up pull-right";	
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
	          }
	        }
	    });
	
	modalInstance.result.then(function (selectedItem) {
	      console.log(selectedItem);
	      
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
	
	console.log($('#myModal'));
}


$scope.gotoState = function(item,mod,itemId){

	if(item=="add"){
		editGroupedNotes('lg',mod,null);
		
	}else if(item.type == 'lab' || item.type == 'document'  || item.type == 'rx' ){
		var rnd = Math.round(Math.random() * 1000);
		win = "win_item.type_" + rnd;
		
		window.open(item.action,win,"scrollbars=yes, location=no, width=900, height=600","");  
		return;
	}else if(item.action == 'action'){
		editGroupedNotes('lg',mod,itemId);

	}else{	
		$state.transitionTo(item.action,{demographicNo:$stateParams.demographicNo, type: item.type ,id: item.id},{location:'replace',notify:true});
	}

};



});


GroupNotesCtrl = function ($scope,$modal,$modalInstance,mod,action,$stateParams,$state,noteService){


	$scope.page = {};
	$scope.page.title = mod.displayName;
	$scope.page.items = mod.summaryItem;

	//$scope.action = action;
	$scope.page.code = mod.summaryCode;
	
	$scope.groupNotesForm = {};

	//set hidden which can can move out of hidden to $scope values
	var now = new Date();
    	$scope.groupNotesForm.annotation_attrib = "anno"+now.getTime();


	displayIssueId = function(issueCode){

			noteService.getIssueId(issueCode).then(function(data){
	    	
			$scope.page.issueId = data.id;

		    	},function(reason){
		    		alert(reason);
		    	});

	}

	displayIssueId($scope.page.code);

	


	displayGroupNote = function(item,itemId){

				noteService.getIssueNote($scope.page.items[itemId].noteId).then(function(iNote){

			       	//$scope.master = angular.copy( "iNote----" +  JSON.stringify(iNote) );
	
				$scope.groupNotesForm.encounterNote = iNote.encounterNote;
				$scope.groupNotesForm.groupNoteExt = iNote.groupNoteExt;

				},function(reason){
					alert(reason);
				});
	
	};



	if(action!=null){
		displayGroupNote($scope.page.items,action);
	}else{
	//todo

	}

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

		noteService.saveIssueNote($stateParams.demographicNo, $scope.groupNotesForm).then(function(data){
    		$modalInstance.dismiss('cancel');
		//alert(JSON.stringify(data));
/*{"appointmentNo":1146,"cpp":true,"document":false,"editable":true,"eformData":false,"encounterForm":false,"encounterTime":"","encounterType":"","groupNote":false,"invoice":false,"isSigned":true,"note":"test new note","noteId":590,"observationDate":"2015-03-11T23:41:10.543-04:00","readOnly":false,"rxAnnotation":false,"ticklerNote":false,"updateDate":"2015-03-11T23:41:10.664-04:00","uuid":"bbc1fea1-42d9-4418-99b4-24902e902484"}
*/

		$state.transitionTo($state.current, $stateParams, { reload: true, inherit: false, notify: true });

	    	},function(reason){
	    		alert(reason);
	    	});

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

};


