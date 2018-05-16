const DsviewComponent = {
  bindings: {
	  close: '&',
	  dismiss: '&',
	  resolve: '<',
  },
  templateUrl: '../web/record/rx/dsview/dsview.template.jsp',
  controller: ['$stateParams','$state','$uibModal','$log',function($stateParams,$state,$uibModal,$log) {
	  
  	dsView = this;
  	
  	dsView.$onInit = function(){
 		console.log("oninit dsView component",this);

 		dsView.alert = this.resolve.alert;

 	}
 	
 	
  	dsView.ok = function () {
  		console.log("ok");
  		dsView.close({$value: dsView.alert});
    };

    dsView.cancel = function () {
    		console.log("cancel");
    		dsView.dismiss({$value: 'cancel'});
 	};
 	
 	
 	
 	
 	
 	} 
  ]
};
