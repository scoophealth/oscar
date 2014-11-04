oscarApp.controller('ConsultListCtrl2', function ($scope,$timeout,ngTableParams, consultService, providerService, demographicService) {
	$scope.consultReadAccess=true;
	$scope.consultWriteAccess=true;
	$scope.lastResponse = "";
	$scope.teams = [];
	
	$scope.search = {team:'All Teams', startIndex:0, numToReturn:10};
	
	providerService.getActiveTeams().then(function(data){
		$scope.teams = data;
		$scope.teams.unshift('All Teams');
		console.log(JSON.stringify(data));
	},function(reason){
		alert(reason);
	});
	
    $scope.searchPatients  = function(term) {
    	var search = {type:'Name','term':term,active:true,integrator:false,outofdomain:true};
    	return demographicService.search(search,0,25).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.content.length;x++) {
    			resp.push({demographicNo:response.content[x].demographicNo,name:response.content[x].lastName + ',' + response.content[x].firstName});
    		}
    		return resp;
    	});
    }
    
    $scope.updateDemographicNo = function(item, model, label) {
    	
    	demographicService.getDemographic(model).then(function(data){
    		$scope.search.demographicNo=data.demographicNo;
    		$scope.consult.demographicName = data.lastName + "," + data.firstName;
    	});
    	
    }
    


	$scope.addConsult = function() {
		
	}
	
    $scope.checkAll = function() {
        angular.forEach($scope.lastResponse, function (item) {
            item.checked = true;
        });
    }
    
    $scope.checkNone = function() {
    	angular.forEach($scope.lastResponse, function (item) {
            item.checked = false;
        })
    }
    
	$scope.editConsult = function(consult) {
		windowprops = "height=700,width=960,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
		var popup=window.open('../oscarEncounter/ViewRequest.do?requestId='+consult.id, "oscarConsultationRequest", windowprops);
	}
	
	$scope.doSearch = function() {
		$scope.tableParams.reload();
	}
	
    $scope.removeDemographicAssignment = function() {
    	$scope.search.demographicNo=null;
    	$scope.consult.demographicName='';
    }
    
    $scope.clear = function() {
    	$scope.removeDemographicAssignment(); 	 
    	$scope.search = angular.copy({team:'All Teams', startIndex:0, numToReturn:10});
    	$scope.tableParams.reload();
    }
	
	$scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10,
        sorting: {
            ReferralDate: 'desc'     // initial sorting
        }
    }, {
        total: 0,           // length of data
        getData: function($defer, params) {
        	var count = params.url().count;
        	var page = params.url().page;
        	
        	//$scope.search.params = params.url();
        //	console.log(params.url().toSource());
        	
        	$scope.search.startIndex = ((page-1)*count);
        	$scope.search.numToReturn=parseInt(count);
        	
        	var search1 = angular.copy($scope.search);
        	search1.params = params.url();
        	
        	if(search1.team === 'All Teams') {
        			search1.team = null;
        	}
        	
        	consultService.search(search1).then(function(result){
        		//console.log(JSON.stringify(result));
        		 params.total(result.total);
                 $defer.resolve(result.content);
                 $scope.lastResponse = result.content;
                 
        	},function(reason){
        		alert(reason);
        	});
        	
         }
    
    });

});




oscarApp.controller('ConsultListCtrl', function ($scope,$http,$resource) {
	$scope.init = function(value) {
		$scope.providerNo = value;
	}
});
