const RxComponent = {
  bindings: {
  	

  },
  templateUrl: 'record/rx/rx.template.jsp',
  controller: ['$stateParams','$state','$log','summaryService','rxService',function($stateParams,$state,$log,summaryService,rxService) {
  	rxComp = this;

 	rxComp.$onInit = function(){
 		console.log("oninit rxComp",this);
 		
 		rxComp.page = {};
 		rxComp.page.columnOne = {};
 		rxComp.page.columnOne.modules = {};

 		rxComp.page.columnThree = {};
 		rxComp.page.columnThree.modules = {};
 		rxComp.page.drugs = [];
 		rxComp.page.dsMessageList = [];
 		
 		rxComp.toRxList = [];  //might want to cache this server side and check back so that we can switch between tabs.
 		
 		
 		rxService.getMedications($stateParams.demographicNo,"").then(function(data){
			  console.log("getMedications--",data);
			  rxComp.page.drugs = data.data.content;
		    	},
		    	function(errorMessage){
			      console.log("getMedications++"+errorMessage);
			      rxComp.error=errorMessage;
		    	}
		);
 		
 		rxComp.getDSMessages($stateParams.demographicNo,rxComp.toRxList);
 		
 		getRightItems();
 		getLeftItems();
 		
 		
 	}
 	rxComp.shortDSMessage = function(){
 		console.log("shortDSMessage",rxComp.toRxList);
 		rxComp.getDSMessages($stateParams.demographicNo,rxComp.toRxList);
 	}
 	
 	rxComp.getAlertStyl = function(alert){
 		console.log("alert",alert);
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
 	
 	rxComp.getDSMessages = function(demo,meds){
 		rxService.getDSMessages($stateParams.demographicNo,meds).then(function(data){
			  console.log("dsMessageList--",data);
			  rxComp.page.dsMessageList = data.data.dsMessages;
		    	},
		    	function(errorMessage){
			      console.log("getMedications++"+errorMessage);
			      rxComp.error=errorMessage;
		    	}
 		);
 	};
 	
 	rxComp.medSelected = function(med){
 		if(angular.isDefined(med.favoriteName)){
 		   var m = {};
 		   m.drug = med;
 		   var d = new Drug();
 	       d.applyFavorite(m);
 	       rxComp.toRxList.push(d);
 		}else{
 			console.log("calling getMEd details ",med);
 			rxService.getMedicationDetails(med.id).then(function (d) {
 				console.log("getMeddetails returns",d);
 	            newMed = new Drug($stateParams.demographicNo);
 	            newMed.name = med.name;
 	            newMed.newMed = true;
 	            newMed.populateFromDrugSearchDetails(d.data.drugs[0]);

 	           rxComp.toRxList.push(newMed);
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