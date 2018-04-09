const RxComponent = {
  bindings: {
  	

  },
  templateUrl: 'record/rx/rx.template.jsp',
  controller: ['$stateParams','$state','$log','summaryService','rxService','$uibModal',function($stateParams,$state,$log,summaryService,rxService,$uibModal) {
  	rxComp = this;

 	rxComp.$onInit = function(){
 		console.log("oninit rxComp",this);
 		
 		rxComp.page = {};
 		rxComp.page.columnOne = {};
 		rxComp.page.columnOne.modules = {};

 		rxComp.page.columnThree = {};
 		rxComp.page.columnThree.modules = {};
 		rxComp.page.fulldrugs = [];
 		rxComp.page.dsMessageList = [];
 		rxComp.page.dsMessageHash = {};
 		rxComp.page.favouriteDrugs=[];
 		
 		rxComp.toRxList = [];  //might want to cache this server side and check back so that we can switch between tabs.
 		
 		getMeds();
 		rxComp.getDSMessages($stateParams.demographicNo,rxComp.toRxList);
 		
 		rxService.favorites($stateParams.demographicNo,null,rxComp.processFavourites);
		
 		getRightItems();
 		getLeftItems();
 		
 		
 	}
 	
 	getMeds = function(){
 		
 		rxService.getMedications($stateParams.demographicNo,"").then(function(data){
			  console.log("getMedications--",data);
			  rxComp.page.fulldrugs = data.data.content;
			  console.log("set meds", rxComp.page.fulldrugs);
		    	},
		    	function(errorMessage){
			      console.log("getMedications++"+errorMessage);
			      rxComp.error=errorMessage;
		    	}
		);
 		
 	}
 	
 	rxComp.shortDSMessage = function(){
 		console.log("shortDSMessage",rxComp.toRxList);
 		rxComp.getDSMessages($stateParams.demographicNo,rxComp.toRxList);
 	}
 	
 	rxComp.getAlertStyl = function(alert){
 		if(alert.significance === 3){
 			return "danger";
 		}else if(alert.significance ===2){
 			return "warning";
 		}else if(alert.significance ===1){
 			return "info";
 		}else{
 			return "warning";
 		}
 	}
 	
 	rxComp.getDSMessages = function(demo,medsList){
 		
 		meds = [];
 		for(i=0;i< medsList.length; i++){
 			meds.push(medsList[i].toDrugTransferObject().drug);
 		}
 		
 		rxService.getDSMessages($stateParams.demographicNo,meds).then(function(data){
			  console.log("dsMessageList--",data);
			  rxComp.page.dsMessageList = data.data.dsMessages;
			  rxComp.page.dsMessageHash = {};
			  for(i=0;i< rxComp.page.dsMessageList.length; i++){
				 dsmessage = rxComp.page.dsMessageList[i];
				 console.log("dsMessage",dsmessage);
				 if(dsmessage.atc != null){
					 if(angular.isDefined(rxComp.page.dsMessageHash[dsmessage.atc]) ){
						 rxComp.page.dsMessageHash[dsmessage.atc].push(rxComp.page.dsMessageList[i]);
					 }else{
						 rxComp.page.dsMessageHash[dsmessage.atc] = [];
						 rxComp.page.dsMessageHash[dsmessage.atc].push(rxComp.page.dsMessageList[i]);
					 }
				 }
				 if(dsmessage.atc2 != null){
					 if(angular.isDefined(rxComp.page.dsMessageHash[dsmessage.atc2])){
						 rxComp.page.dsMessageHash[dsmessage.atc2].push(rxComp.page.dsMessageList[i]);
					 }else{
						 rxComp.page.dsMessageHash[dsmessage.atc2] = [];
						 rxComp.page.dsMessageHash[dsmessage.atc2].push(rxComp.page.dsMessageList[i]);
					 }
				 }
			  }
			  console.log("rxComp.page.dsMessageHash",rxComp.page.dsMessageHash);
		    	},
		    	function(errorMessage){
			      console.log("getMedications++"+errorMessage);
			      rxComp.error=errorMessage;
		    	}
 		);
 	};
 	
 	rxComp.saveAndPrint = function(){
 		rxService.prescribe($stateParams.demographicNo,rxComp.toRxList,rxComp.processRxSuccess);
 		console.log("PRESCRIBE CALLED");
 	}
 	
 	rxComp.processFavourites = function(resp){
 		console.log("favourites returned",resp);
 		rxComp.page.favouriteDrugs = resp;
 	}
 	
 	rxComp.processRxSuccess = function(resp){
 		console.log("WHAT IS THERE RETURN",resp);
 		
 		if(resp.success){
 			getMeds();
 			var modalInstance = $uibModal.open({
 				component: 'rxPrintComponent',
 				size: 'lg',
 				resolve: {
 				scriptId: function (){
 						return resp.prescription.scriptId;
 					}
 				}
 			});
 			
 			modalInstance.result.then(function (selectedItem) {
 				console.log(selectedItem);
 				rxComp.toRxList = [];
 			}, function () {
 				console.log('Modal dismissed at: ' + new Date());
 			});
 			
 		}else{
 			alert("Error Prescribing" +resp.drugs)
 		}
 	}
 	
 	rxComp.parseInstr = function(med){

 		rxService.parseInstructions(med.instructions).then(function(d){
 			med.applyInstructions(d.data);
 			med.setQuantity();
 		},function(errorMessage){
 			console.log("Error parsing Intruction",errorMessage);		
 		});
 	}
 	
 	rxComp.reRx = function(drug){
 		console.log("reRx in comp",drug);
 		rxService.getMedication(drug.drugId).then(function (resp) {
			var d = new Drug();
			console.log("resp", resp);
			d.fromDrugTransferObject(resp.data.drug);
			d.start = new Date();
			d.endDate = calculateEndDate(d);
			rxComp.toRxList.push(d);
 		},
		function(errorMessage){
	       console.log("getMedicationDetails "+errorMessage);
	       rxComp.error=errorMessage;
	    	});
 		
 	}
 	 	
 	
 	rxComp.favSelected = function(fav){
 		var m = {};
		m.drug = fav.drug;
		var d = new Drug();
	    d.applyFavorite(m);
	    console.log("Fav drug",d,fav);
	    rxComp.toRxList.push(d);
 	}
 	
 	rxComp.medSelected = function(med){
 		console.log("med",med);
 		if(angular.isDefined(med.favoriteName)){
 		   var m = {};
 		   m.drug = med;
 		   var d = new Drug();
 	       d.applyFavorite(m);
 	       //if(angular.isDefined(d.id)){
 	    	   //   delete d.id;
 	       //}
 	       rxComp.toRxList.push(d);
 		}else{
 			console.log("calling getMEd details ",med);
 			rxService.getMedicationDetails(med.id).then(function (d) {
 				console.log("getMeddetails returns",d);
 	            newMed = new Drug($stateParams.demographicNo);
 	            newMed.name = med.name;
 	            newMed.newMed = true;
 	            //if(angular.isDefined(newMed.id)){
 	            //   delete newMed.id;
 	            //}
 	            newMed.populateFromDrugSearchDetails(d.data.drugs[0]);

 	           rxComp.toRxList.push(newMed);
 	           rxComp.shortDSMessage();
 	            //updateStrengthUnits(d.drugs);
 			},
 			function(errorMessage){
  		       console.log("getMedicationDetails "+errorMessage);
  		       rxComp.error=errorMessage;
  	    		});
	
 		}
 		console.log("MEDDDD ",med,rxComp.toRxList);
 	}
 	
 	
 	function getLeftItems(){
 		summaryService.getSummaryHeaders($stateParams.demographicNo,'rxLeft').then(function(data){
 			  console.log("rxLeft",data);
 			 rxComp.page.columnOne.modules = data;
 		      fillItems(rxComp.page.columnOne.modules);
 	    	},
 	    	function(errorMessage){
 		       console.log("rxLeft"+errorMessage);
 		      rxComp.error=errorMessage;
 	    	}
 		);
 	};


 	function getRightItems(){
 		summaryService.getSummaryHeaders($stateParams.demographicNo,'right').then(function(data){
 			  console.log("right",data);
 			 rxComp.page.columnThree.modules = data;
 		      fillItems(rxComp.page.columnThree.modules);
 	    	},
 	    	function(errorMessage){
 		       console.log("left"+errorMessage);
 		      rxComp.error=errorMessage;
 	    	}
 		);
 	};

 	

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
 	
 	
 	
	
 	} 
  ]
};