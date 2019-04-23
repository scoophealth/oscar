const MedsearchComponent = {
  bindings: {
	favouriteMeds: '<',
	favSelected: '&',
  	medSelected: '&',
  	customRx: '&'
  },
  templateUrl: '../web/record/rx/search/medsearch.template.jsp',
  controller: ['$stateParams','$state','$log','$timeout','summaryService','rxService','$http','$filter','$uibModal',function($stateParams,$state,$log,$timeout,summaryService,rxService,$http,$filter,$uibModal) {
  	rxSearchComp = this;

  	rxSearchComp.$onInit = function(){
  		console.log("herer??  rxlookup/search",rxSearchComp.favouriteMeds);
 	}
  	
  	rxSearchComp.fullSearch = function(){
  		console.log("Would search for ",rxSearchComp.selected);
  		var dr = $stateParams.demographicNo;
		var modalInstance = $uibModal.open({
			component : 'fullsearchComponent',
			size : 'lg',
			resolve : {
				drug : function() {
					return dr;
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			console.log("reprint item", selectedItem);
			rxSearchComp.medSelected({med:selectedItem});
		}, function() {
			console.log('Modal dismissed at: ' + new Date());
		});
  		
  	}
  	
  	rxSearchComp.customDrug = function(){
  		rxSearchComp.customRx();
  	}
    rxSearchComp.customNote = function(){
    		rxSearchComp.customRx();
    }
    rxSearchComp.DrugOfChoice = function(){
    		alert("Not Implemented Yet");
    }
  	 
  	rxSearchComp.medTypeAheadLabel = function(med) {
  		if (med == null || med == undefined){
     		return;
     	}
        		
     	if (med.name == '' || med.name == undefined) {
     		console.log("label  blank",med);
        		return '';
     	}
     	
     	var label = med.name+" ";
     	console.log("label ",label);
    		return label;
	}
 	
 
 	rxSearchComp.onSelect = function($item, $model, $label){
 		console.log("onSElect",$item, $model, $label);
 		if(angular.isDefined($item.fav) && $item.fav == true){
 			rxSearchComp.selectFav($item);
 		}else{
 			rxSearchComp.medSelected({med:$item});
 		}
 		rxSearchComp.selected = "";
 	};
		 
 	
 	rxSearchComp.lookupMeds = function (val) {
      var urlStr = "../ws/rs/rxlookup/search?string="+ val;
      
    	  //return $http.get(urlStr).then(function (response) {
      return rxService.lookup(val).then(function (response) {
    	  
    		  console.log("lookupMeds return ",response,rxSearchComp.favouriteMeds,val);
    		  
    		  var myFavourites = $filter('filter')(rxSearchComp.favouriteMeds, { name: val });
    		  console.log("MyRedObj",myFavourites);
    		  
    		  for (var i = 0, len = myFavourites.length; i < len; i++) {
    			  myFavourites[i].fav = true;
    			  response.data.drugs.unshift(myFavourites[i]);
    		  }
    		  
         return response.data.drugs;
      });
    };
  	
  	rxSearchComp.selectFav = function(datum){
  		console.log("Calling selectFav ",datum);
  		rxSearchComp.favSelected({fav:datum});
  	};

	
 	} 
  ]
};