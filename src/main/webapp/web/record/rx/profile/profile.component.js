const RxProfileComponent = {
  bindings: {
  	fulldrugs: '<',
  	reRx: '&',
  	dsMessages: '<',
  	showAlert: '&',
  	addFavorite: '&'
  		
  },
  templateUrl: '../web/record/rx/profile/profile.template.jsp',
  controller: ['$stateParams','$state','$log','$timeout','summaryService','rxService','$filter','$window','$uibModal',function($stateParams,$state,$log,$timeout,summaryService,rxService,$filter,$window,$uibModal) {
  	rxProfileComp = this;

  	
  	rxProfileComp.$onInit = function(){
  		console.log("herer??  RxProfileComponent",rxProfileComp);
  		rxProfileComp.rxComp = {};
  		rxProfileComp.rxComp.drugs = [];
  		rxProfileComp.rxComp.profileHash = {};
  		fullDrugList = [];
  		rxProfileComp.mode = 0;
  		rxProfileComp.tab = 0;
  		rxProfileComp.dsMessagesHash = {};

 	}
  	
  	
  	
  	rxProfileComp.$onChanges = function(changesObj){
  		console.log("hereZZ",changesObj);
  		if(angular.isDefined(changesObj.fulldrugs)){
  			console.log("was defined");
  			drugList = changesObj.fulldrugs.currentValue;
  			fullDrugList = drugList;//$filter('orderBy')(drugList, 'rxDate', true);
  			rxProfileComp.processList(fullDrugList);
  		}
  		
  		if(angular.isDefined(changesObj.dsMessages)){
  			console.log("dsMessages was defined");
  			rxProfileComp.dsMessagesHash = changesObj.dsMessages.currentValue;
  		}
  	}
  	
  	rxProfileComp.setTab = function(tabNum){
  		rxProfileComp.tab = tabNum;
  	}
  	
  	rxProfileComp.booleanTab = function(tabNum){
  		if(rxProfileComp.tab === tabNum){
  			return true;
  		}
  		return false;
  	}
  	
  	rxProfileComp.daysToExp = function(drug){
  		//console.log("drug--",drug);
  		var oneDay = 24*60*60*1000; // hours*minutes*seconds*milliseconds
  		var endDate = Date.parse(drug.endDate);
  	    var now = new Date();
  	    
  	    if(now > endDate){ // if it's after the end date then it's already expired
  	    		return 0;
  	    }
  	    return Math.round(Math.abs((now.getTime() - endDate)/(oneDay)));
  	}
  	
  	profileAll = function(drugList){
  		profileObject = {}
  		profileObject.profileHash = {};
  		profileObject.profileList = [];
  		for(i=0; i < drugList.length; i++){
	  			//console.log("here1."+i+" ",drugList[i]);
	  			if(angular.isDefined(profileObject.profileHash[drugList[i].atc])){
	  				profileObject.profileHash[drugList[i].atc].push(drugList[i]);
	  			}else{
	  				profileObject.profileHash[drugList[i].atc] = [];
	  				profileObject.profileHash[drugList[i].atc].push(drugList[i]);
	  			}
	  			profileObject.profileList.push(drugList[i]);
	  			
	  	}
  		return profileObject;
  	}
  	
  	profileCurrent = function(drugList){
  		profileObject = {}
  		profileObject.profileHash = {};
  		profileObject.profileList = [];
  		for(i=0; i < drugList.length; i++){
	  			//Group drugs by ATC code, if ATC code is blank treat as separate drugs
  				
  				if(drugList[i].atc == null || drugList[i].atc === ""){
  					profileObject.profileList.push(drugList[i]);
  				}else{
  			
		  			if(angular.isDefined(profileObject.profileHash[drugList[i].atc])){
		  				profileObject.profileHash[drugList[i].atc].push(drugList[i]);
		  			}else{
		  				profileObject.profileHash[drugList[i].atc] = [];
		  				profileObject.profileHash[drugList[i].atc].push(drugList[i]);
		  				profileObject.profileList.push(drugList[i]);
		  			}	
  				}
	  	}
  		newList = [];
  		for(i=0; i < profileObject.profileList.length; i++){
  			console.log(i+":"+profileObject.profileList.length+"  -- archived"+drugList[i].drugId+": drugname "+drugList[i].brandName+"  --  "+drugList[i].rxDate,drugList[i].longTerm);
			if(!profileObject.profileList[i].archived){
				newList.push(profileObject.profileList[i]);
			}
  		}
  		profileObject.profileList = newList;
  		return profileObject;
  	}
  	
  	rxProfileComp.getHeading = function(warn){
  		if(warn.heading != null){
  			return warn.heading;
  		}
  		return warn.summary;
  	}
  	
  	rxProfileComp.getAlertStyl = function(alert) {
		if (alert.significance === 3) {
			return "danger";
		} else if (alert.significance === 2) {
			return "warning";
		} else if (alert.significance === 1) {
			return "info";
		} else {
			return "warning";
		}
	}
  	rxProfileComp.checkIfHidden = function(alert) {
		if (alert.hidden) {
			return true;
		}
		return false;
	}

  	
  	rxProfileComp.processList = function(drugList){
  		profileObject = {};
		profileObject.profileHash = {};
	  	profileObject.profileList = [];
	  	rxProfileComp.rxComp = {};
	  	 
	  	allProfileObject = profileAll(drugList);
	  	
		if(rxProfileComp.mode == 0){
			profileObject = profileCurrent(drugList);
		}else{
			profileObject = allProfileObject;
		}
		
		rxProfileComp.rxComp.profileHash = profileObject.profileHash;
		rxProfileComp.rxComp.drugs = profileObject.profileList;
		rxProfileComp.rxComp.allDrugsList = allProfileObject.profileList;
  	}
  	
  	/*Profile Mode*/
  	rxProfileComp.setMode = function(mode){
  		rxProfileComp.mode = mode
  		rxProfileComp.processList(fullDrugList);
  	}
  	
  	rxProfileComp.buttonStyle = function(mode){
  		if(mode === rxProfileComp.mode){
  			return "btn-primary";
  		}
  		return "btn-default";
  	}
  	/*Profile Mode End*/
  	

  	rxProfileComp.print = function(){
  		//http://localhost:8080/oscar/oscarRx/PrintDrugProfile2.jsp
  		var winX = (document.all)?window.screenLeft:window.screenX;
        var winY = (document.all)?window.screenTop:window.screenY;

        var top = winY+70;
        var left = winX+110;
        var url = "../web/record/rx/print/PrintDrugProfile2.jsp?demographicNo="+$stateParams.demographicNo;
        windowName = 'windowNameProfilePrint'+$stateParams.demographicNo;
        
        windowprops = "height=575,width=650,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=" + top + ",left=" + left;
  	  	var reasonWindow = $window.open(url, windowName, windowprops);
  		console.log("Not Implemented Yet",drug);
  		
  		alert("Not Implemented Yet");
  	}
    rxProfileComp.rePrint= function(){
    		var dr = $stateParams.demographicNo;
		var modalInstance = $uibModal.open({
			component : 'reprintComponent',
			size : 'lg',
			resolve : {
				drug : function() {
					return dr;
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			console.log("reprint item", selectedItem);
			callPrint(selectedItem);
		}, function() {
			console.log('Modal dismissed at: ' + new Date());
		});
  	}
    rxProfileComp.timeline = function(){
  		alert("Not Implemented Yet");
  	}
    
    
    callPrint = function(scriptNo){
    		var scriptNoId = scriptNo;
    		var modalInstance = $uibModal.open({
			component : 'rxPrintComponent',
			size : 'lg',
			resolve : {
				scriptId : function() {
					return scriptNoId;
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			console.log(selectedItem);
			
		}, function() {
			console.log('Modal dismissed at: ' + new Date());
		});
    	
    }
    
    rxProfileComp.medhistory = function(drug){
		var dr = drug;
		var modalInstance = $uibModal.open({
			component : 'drughistoryComponent',
			size : 'lg',
			resolve : {
				drug : function() {
					return dr;
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			console.log("history item", selectedItem);
			
		}, function() {
			console.log('Modal dismissed at: ' + new Date());
		});
	
  		console.log("Not Implemented Yet",drug,$stateParams);
  	}
  	
  	/*Action Drop Box methods*/
  	rxProfileComp.discontinue = function(drug){
  		
  		
		var dr = drug;
		var modalInstance = $uibModal.open({
			component : 'discontinueComponent',
			size : 'lg',
			resolve : {
				drug : function() {
					return dr;
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			console.log("discontinue item", selectedItem);
			rxService.discontinueMedication(selectedItem.drug.demographicNo,selectedItem.drug.drugId,selectedItem.reason).then(
					function(d) {
						console.log("discontinue success",drug,d);
						dr.archived = true;
					},
					function(errorMessage) {
						console.log("Error parsing Intruction",errorMessage);
					}
				);
		}, function() {
			console.log('Modal dismissed at: ' + new Date());
		});
	
  		console.log("Not Implemented Yet",drug,$stateParams);
  	}
  	
    rxProfileComp.delete = function(drug){
  		if (confirm("Are you sure you wish to delete the selected prescriptions?")) {
  			rxService.discontinueMedication(drug.demographicNo,drug.drugId,"deleted").then(
					function(d) {
						console.log("delete success",drug,d);
						drug.archived = true;
					},
					function(errorMessage) {
						console.log("Error parsing Intruction",errorMessage);
					}
				);
		}
  		console.log("deleting",drug);
  	}
    
    rxProfileComp.addReason = function(drug){
  		var winX = (document.all)?window.screenLeft:window.screenX;
        var winY = (document.all)?window.screenTop:window.screenY;

        var top = winY+70;
        var left = winX+110;
        var url = "../oscarRx/SelectReason.jsp?demographicNo="+drug.demographicNo+"&drugId="+drug.drugId;
        windowName = 'windowNameRxReason'+drug.demographicNo;
        
        windowprops = "height=575,width=650,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=" + top + ",left=" + left;
  	  	var reasonWindow = $window.open(url, windowName, windowprops);
  		console.log("Not Implemented Yet",drug);
  	}
    
    rxProfileComp.setAsLongTermMed = function(drug){
    	if (confirm("Are you sure you want to set this medication to long term?")) {
    			drug.longTerm = true;
  			rxService.updateMedication(drug.demographicNo,drug).then(
					function(d) {
						
						console.log("d",d)
					},
					function(errorMessage) {
						console.log("Error parsing Intruction",errorMessage);
					}
				);
		}
  		
  		
  	}
    
    rxProfileComp.unsetAsLongTermMed = function(drug){
    	if (confirm("Are you sure you want to unset this medication as long term?")) {
			drug.longTerm = false;
			rxService.updateMedication(drug.demographicNo,drug).then(
				function(d) {
					
					console.log("d",d)
				},
				function(errorMessage) {
					console.log("Error parsing Intruction",errorMessage);
				}
			);
	}
		
		
	}
    
    rxProfileComp.annotate = function(drug){
    	    
    		var winX = (document.all)?window.screenLeft:window.screenX;
        var winY = (document.all)?window.screenTop:window.screenY;

        var top = winY+70;
        var left = winX+110;
        var url = "../annotation/annotation.jsp?display=Prescriptions&table_id="+drug.drugId+"&demo="+drug.demographicNo+"+&drugSpecial="+drug.instructions;
        
        windowName = 'windowNameRxAnnotation'+drug.demographicNo;
        
        windowprops = "height=575,width=650,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=" + top + ",left=" + left;
  	  	var annotationWindow = $window.open(url, windowName, windowprops);
  		
  	}
    rxProfileComp.hideFromCPP = function(drug){
  		alert("Not Implemented Yet");
  		/*
  		 function(event) {
		  var val = $('hidecpp_242').checked;
		  new Ajax.Request('/oscar/oscarRx/hideCpp.do?method=update&prescriptId=242&value=' + val, {
		    method: 'get',
		    onSuccess: function(transport) {}
		  });
		
		} 
  		  
  		 */
  		console.log("Not Implemented Yet",drug);
  	}
    rxProfileComp.moveUpInList = function(drug){
  		alert("Not Implemented Yet");
  		//http://localhost:8080/oscar/oscarRx/reorderDrug.do?method=update&direction=down&drugId=544&swapDrugId=543&demographicNo=153&rand=7177
  		console.log("Not Implemented Yet",drug);
  	}
    rxProfileComp.moveDownInList = function(drug){
  		alert("Not Implemented Yet");
  		console.log("Not Implemented Yet",drug);
  	}
 	/*Action Drop Box methods END*/
 	 	
	
 	} 
  ]
};