const DrughistoryComponent = {
  bindings: {
	  close: '&',
	  dismiss: '&',
	  resolve: '<',
  },
  templateUrl: '../web/record/rx/history/history.template.jsp',
  controller: ['$stateParams','$state','$uibModal','$log','rxService',function($stateParams,$state,$uibModal,$log,rxService) {
	  
  	drugHistoryComp = this;
  	
  	drugHistoryComp.$onInit = function(){
 		console.log("oninit drugHistoryComp component",this);

 		drugHistoryComp.drug = this.resolve.drug;
 		drugHistoryComp.druglist = [];

 		
 		rxService.history(drugHistoryComp.drug, drugHistoryComp.drug.demographicNo, drugHistoryComp.fillMeds);


   };
 		
   drugHistoryComp.fillMeds = function(drugList){
	   drugHistoryComp.druglist = drugList;
	   console.log("drugList",drugList);
   };

   drugHistoryComp.ok = function () {
  		console.log("ok");
  		drugHistoryComp.close({$value: disconComp.discon});
    };

    drugHistoryComp.cancel = function () {
    		console.log("cancel");
    		drugHistoryComp.dismiss({$value: 'cancel'});
 	};
 	
 	} 
  ]
};
