const DiscontinueComponent = {
  bindings: {
	  close: '&',
	  dismiss: '&',
	  resolve: '<',
  },
  templateUrl: '../web/record/rx/discontinue/discontinue.template.jsp',
  controller: ['$stateParams','$state','$uibModal','$log',function($stateParams,$state,$uibModal,$log) {
	  
  	disconComp = this;
  	
  	disconComp.$onInit = function(){
 		console.log("oninit disconComp component",this);

 		disconComp.drug = this.resolve.drug;
 		disconComp.discon = {};
 		disconComp.discon.drug = this.resolve.drug;

 	}

  	disconComp.ok = function () {
  		console.log("ok");
  		disconComp.close({$value: disconComp.discon});
    };

    disconComp.cancel = function () {
    		console.log("cancel");
    		disconComp.dismiss({$value: 'cancel'});
 	};
 	
 	} 
  ]
};
