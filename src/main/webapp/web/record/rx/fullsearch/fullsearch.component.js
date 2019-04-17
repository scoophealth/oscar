const FullSearchComponent = {
  bindings: {
	  close: '&',
	  dismiss: '&',
	  resolve: '<',
  },
  templateUrl: '../web/record/rx/fullsearch/fullsearch.template.jsp',
  controller: ['$stateParams','$state','$uibModal','$log','rxService',function($stateParams,$state,$uibModal,$log,rxService) {
	  
  	var fullSearchComp = this;
  	
  	fullSearchComp.$onInit = function(){
 		console.log("oninit drugHistoryComp component",this);

 		//drugHistoryComp.drug = this.resolve.drug;
 		fullSearchComp.druglist = [];
 		fullSearchComp.brand = [];
 		fullSearchComp.gen = [];
 		fullSearchComp.afhcClass = [];
 		
 		//rxService.history(drugHistoryComp.drug, drugHistoryComp.drug.demographicNo, drugHistoryComp.fillMeds);


   };
   
   fullSearchComp.listBrands = function(drug){
	   console.log("listBrands",drug);
	   rxService.searchByElement(drug.id).then(function(data){
			  console.log("searchByElement",data); 
			  fullSearchComp.brand = data.data.drugs;
		   });
	   
   }
 		
   fullSearchComp.fullSearch = function(drugList){
	   rxService.search(fullSearchComp.searchTerm).then(function(data){
		  console.log("search",data); 
		  
		  fullSearchComp.druglist = [];
	 		fullSearchComp.brand = [];
	 		fullSearchComp.gen = [];
	 		fullSearchComp.afhcClass = [];
		  
		  fullSearchComp.druglist = data.data.drugs;
		  
		  for(i =0; i < fullSearchComp.druglist.length; i++){
			  d = fullSearchComp.druglist[i];
		  
			  if(d.category === 13 ){
				  fullSearchComp.brand.push(d);
			  }else if (d.category === 11 || d.category === 12 ){
				  fullSearchComp.gen.push(d);
			  }else if (d.category === 8 || d.category === 10 ){
				  fullSearchComp.afhcClass.push(d);
			  }
		  }
	   });
   };

   fullSearchComp.checkForEnter = function(event){
	   if (keyEvent.which === 13){
		   fullSearchComp.fullSearch();
	   }
   };
   
   fullSearchComp.selectDrug = function (drug) {
  		console.log("ok");
  		fullSearchComp.close({$value: drug});
    };

    fullSearchComp.cancel = function () {
    		console.log("cancel");
    		fullSearchComp.dismiss({$value: 'cancel'});
 	};
 	
 	} 
  ]
};
