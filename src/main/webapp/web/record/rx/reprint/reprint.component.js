const ReprintComponent = {
  bindings: {
	  close: '&',
	  dismiss: '&',
	  resolve: '<',
  },
  templateUrl: '../web/record/rx/reprint/reprint.template.jsp',
  controller: ['$stateParams','$state','$uibModal','$log','rxService',function($stateParams,$state,$uibModal,$log,rxService) {
	  
  	reprintComp = this;
  	
  	reprintComp.$onInit = function(){
 		console.log("oninit reprint component",this,$stateParams);

 		reprintComp.drug = this.resolve.drug;
 		reprintComp.rxList = [];
 		rxService.getPrescriptions(reprintComp.drug).then(
				function(d) {
			 		reprintComp.rxList = d.data;
					console.log("getPrescriptions",d);
				},
				function(errorMessage) {
					console.log("Error parsing Intruction",errorMessage);
				}
			);
	


 	}

  	reprintComp.reprint = function (rx) {
  		console.log("ok",rx);
  		reprintComp.close({$value: rx.scriptId});
    };

    reprintComp.cancel = function () {
    		console.log("cancel");
    		reprintComp.dismiss({$value: 'cancel'});
 	};
 	
 	} 
  ]
};
