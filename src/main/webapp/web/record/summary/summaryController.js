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
oscarApp.controller('SummaryCtrl', function ($rootScope,$scope,$http,$location,$stateParams,$state,$filter,user,noteService) {
	console.log("in summary Ctrl ",$stateParams);
	
	$scope.page = {};
	$scope.page.columnOne = {};
	$scope.page.columnOne.modules = {};
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
   
   	$scope.isCurrentStatus = function(stat){
	   console.log("stat",stat);
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
    
    $scope.addMoreItems();
        
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
    	console.log(firstL + " --"+dateStr+"-- " + firstL.indexOf(dateStr));
    	if(firstL.indexOf(dateStr) == 0 ){
    		firstL = firstL.substring(dateStr.length);
    	}
    	return firstL;
    };

    $scope.getTrackerUrl = function(demographicNo) {
    	
    url = '../oscarEncounter/oscarMeasurements/HealthTrackerSlim.jspf?template=tracker&demographic_no='+ demographicNo;
  	
    return url;  
    
    };
    
});

//for demo will resolve 
function resizeIframe(iframe) {
	
	var h = iframe.contentWindow.document.body.scrollHeight;
	if(h>0){
    iframe.height =  h + "px";
    //alert("h > 0");
	}
    //alert("h" + h);
  }
