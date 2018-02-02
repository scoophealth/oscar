const RxComponent = {
  bindings: {
  	

  },
  templateUrl: 'record/rx/rx.template.jsp',
  controller: ['$stateParams','$state','$log','summaryService','rxService',function($stateParams,$state,$log,summaryService,rxService) {
  	rxComp = this;

 	rxComp.$onInit = function(){
 		console.log("oninit rxComp",this);
 		getRightItems();
 		getLeftItems();
 		rxComp.page = {};
 		rxComp.page.columnOne = {};
 		rxComp.page.columnOne.modules = {};

 		rxComp.page.columnThree = {};
 		rxComp.page.columnThree.modules = {};
 		rxComp.page.drugs = [];
 		
 		rxService.getMedications($stateParams.demographicNo,"").then(function(data){
			  console.log("getMedications--",data);
			  rxComp.page.drugs = data.data.content;
			  
	    	},
	    	function(errorMessage){
		       console.log("getMedications++"+errorMessage);
		      rxComp.error=errorMessage;
	    	}
		);
 		
 		
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