const MedsearchComponent = {
  bindings: {
	favouriteMeds: '<',
	favSelected: '&',
  	medSelected: '&',
  	customRx: '&'
  },
  templateUrl: '../web/record/rx/search/medsearch.template.jsp',
  controller: ['$stateParams','$state','$log','$timeout','summaryService','rxService','$http','$filter',function($stateParams,$state,$log,$timeout,summaryService,rxService,$http,$filter) {
  	rxSearchComp = this;

  	rxSearchComp.$onInit = function(){
  		console.log("herer??  rxlookup/search",rxSearchComp.favouriteMeds);
 	}
  	
  	rxSearchComp.fullSearch = function(){
  		alert("Not Implemented Yet");
  		console.log("Would search for ",rxSearchComp.selected);
  	}
  	
  	rxSearchComp.customDrug = function(){
  		rxSearchComp.customRx();
  	}
    rxSearchComp.customNote = function(){
    		alert("Not Implemented Yet");
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