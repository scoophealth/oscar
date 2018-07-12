const RxPrintComponent = {
  bindings: {
	  close: '&',
	  dismiss: '&',
	  resolve: '<',
  },
  templateUrl: '../web/record/rx/print/print.template.jsp',
  controller: ['$stateParams','$state','$uibModal','$log','rxService',function($stateParams,$state,$uibModal,$log,rxService) {
	  
	/*
	 Options on the print page:
	 -Print PDF with drop box for size.  //look at ViewScript2.jsp
	 -Print
	 -Print & Paste into EMR
	 -Create New Rx
	 
	  Add additional notes to rx  (Add to Rx)
	  
	  Signature spot.
	  
	 */  
	  
  	rxPrint = this;
  	
  	rxPrint.print = function(){
  		printIframe();
  	}
  	
  	rxPrint.$onInit = function(){
 		console.log("oninit print component",this);

 		rxPrint.printId = this.resolve.scriptId;
 		rxPrint.pharamacyId = null;
 		rxPrint.scriptURL = "../web/record/rx/print/PrintView.jsp?scriptId="+rxPrint.printId+"&rePrint=false&pharmacyId="+rxPrint.pharamacyId;
 		rxService.recordPrescriptionPrint(rxPrint.printId);
 	}
 	
 	
  	rxPrint.ok = function () {
  		console.log("ok");
  		rxPrint.close({$value: 'hi'});
    };

    rxPrint.cancel = function () {
    		console.log("cancel");
    		rxPrint.dismiss({$value: 'cancel'});
 	};
 	
 	
 	function printIframe(){
 	   var browserName=navigator.appName; 
 	   if (browserName=="Microsoft Internet Explorer"){
 	      try{ 
 		     iframe = document.getElementById('preview'); 
 		     iframe.contentWindow.document.execCommand('print', false, null); 
 		  }catch(e){ 
 		     window.print(); 
 		  } 
 	   }else{
          preview.focus();
 		  preview.print();
 	   }	
    }
 	
 	
 	} 
  ]
};





  