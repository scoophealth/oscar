const ProviderNameComponent = {
  bindings: {
	providerNo: '<'
  },
  templateUrl: '../web/common/components/provider/provider.template.jsp',
  controller: ['$stateParams','$state','$log','$timeout','providerService','rxService','$http','$filter',function($stateParams,$state,$log,$timeout,providerService,rxService,$http,$filter) {
  	var providerNameComp = this;
  	providerNameComp.providerName = "-";

  	providerNameComp.$onInit = function(){
  		console.log("herer??  providerNameComp",providerNameComp);
  		providerService.getProvider(providerNameComp.providerNo).then(function(data){
  			console.log("get PRovidere ",data);
  			providerNameComp.providerName  = data.lastName+", "+data.firstName;
		},function(){providerNameComp.providerName = "N/A";});
 	}
  	
  	
	
 	} 
  ]
};