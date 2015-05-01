oscarApp.controller('ConsultRequestListCtrl', function ($scope, $timeout, $location, ngTableParams, consultService, providerService, demographicService, staticDataService) {
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
	
    $scope.searchMrps  = function(term) {
    	var search = {searchTerm:term, active:true};
    	return providerService.searchProviders(search).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({mrpNo:response[x].providerNo, name:response[x].name});
    		}
    		return resp;
    	});
    }

    $scope.updateMrpNo = function(model) {
		$scope.search.mrpNo = Number(model.mrpNo);
		
		if ($scope.consult==null) $scope.consult = {};
		$scope.consult.mrpName = model.name;
		
		$scope.doSearch();
    }
    
    $scope.updateDemographicNo = function(item, model, label) {
    	demographicService.getDemographic(model).then(function(data){
    		$scope.search.demographicNo=data.demographicNo;
    		
    		if ($scope.consult==null) $scope.consult = {};
    		$scope.consult.demographicName = data.lastName + "," + data.firstName;
    		
    		$scope.doSearch();
    	});
    }

    //get parameter "demographicId"
    $scope.demographicId = $location.search().demographicId;
    
    //Show patient referral history if "demographicId" is passed
    if ($scope.demographicId!=null) $scope.updateDemographicNo(null, $scope.demographicId);
    


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
    
	$scope.addConsult = function() {
		var url = "/consults/new/"+$scope.search.demographicNo;
		if ($scope.demographicId==null) $location.url(url);
		else $location.path(url).search({"demographicId":$scope.demographicId});
	}
	
	$scope.editConsult = function(consult) {
		var url = "/consults/"+consult.id;
		if ($scope.demographicId==null) $location.url(url);
		else $location.path(url).search({"demographicId":$scope.demographicId});
	}
	
	$scope.doSearch = function() {
		$scope.tableParams.reload();
		$scope.demographicId = $scope.search.demographicNo;
	}
	
    $scope.removeDemographicAssignment = function() {
    	$scope.search.demographicNo=null;
    	$scope.consult.demographicName='';
    }
	
    $scope.removeMrpAssignment = function() {
    	$scope.search.mrpNo=null;
    	$scope.consult.mrpName='';
    }
    
    $scope.clear = function() {
    	$scope.removeDemographicAssignment();
    	$scope.removeMrpAssignment();
    	$scope.search = angular.copy({team:'All Teams', startIndex:0, numToReturn:10});
    	$scope.doSearch();
    }

	//set search statuses
	$scope.statuses = staticDataService.getConsultRequestStatuses();
    
    //get urgencies list
    $scope.urgencies = staticDataService.getConsultUrgencies();
	
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
        	
        	consultService.searchRequests(search1).then(function(result){
        		//console.log(JSON.stringify(result));
        		 params.total(result.total);
                 $defer.resolve(result.content);
                 
                 for (var i=0; i<result.content.length; i++) {
                	 var consult = result.content[i];
                	 
                	 //add statusDescription
                	 for (var j=0; j<$scope.statuses.length; j++) {
                		 if (consult.status==$scope.statuses[j].value) {
                			 consult.statusDescription = $scope.statuses[j].name;
                			 break;
                		 }
                	 }
                	 
                	 //add urgencyDescription
                	 for (var j=0; j<$scope.urgencies.length; j++) {
                		 if (consult.urgency==$scope.urgencies[j].value) {
                			 consult.urgencyDescription = $scope.urgencies[j].name;
                			 break;
                		 }
                	 }
                	 
                     //add urgencyColor if consult urgency=Urgent(1)
                	 if (consult.urgency==1) {
                		 consult.urgencyColor = "text-danger"; //= red text
                	 }
                	 
                	 //when searching "All Active", hide consults with status=Completed(4)/Cancelled(5)/Deleted(7)
                	 if (search1.status==null || search1.status=="") {
                		 if (consult.status==4 || consult.status==5 || consult.status==7) {
                			 result.content.splice(i, 1);
                			 i--;
                			 continue;
                		 }
                	 }
                 }
                 
                 $scope.lastResponse = result.content;
                 
        	},function(reason){
        		alert(reason);
        	});
        	
         }
    
    });

});
