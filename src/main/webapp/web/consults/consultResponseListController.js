oscarApp.controller('ConsultResponseListCtrl', function ($scope,$timeout,$state,$location,ngTableParams,consultService,providerService,demographicService,securityService,staticDataService) {
	
	//get access rights
	securityService.hasRight("_con", "r").then(function(data){
		$scope.consultReadAccess=data;
	});
	securityService.hasRight("_con", "u").then(function(data){
		$scope.consultUpdateAccess=data; //to be used with batch operations (not yet implemented)
	});
	securityService.hasRight("_con", "w").then(function(data){
		$scope.consultWriteAccess=data;
	});

	//set search statuses
	$scope.statuses = staticDataService.getConsultResponseStatuses();
	
	//get urgencies list
	$scope.urgencies = staticDataService.getConsultUrgencies();
	
	$scope.lastResponse = "";
	$scope.teams = [];
	$scope.consult = {};
	
	var allTeams = "All Teams";
	$scope.search = {team:allTeams, startIndex:0, numToReturn:10};
	
	providerService.getActiveTeams().then(function(data){
		$scope.teams = data;
		$scope.teams.unshift(allTeams);
		console.log(JSON.stringify(data));
	},function(reason){
		alert(reason);
	});

	$scope.searchPatients  = function(term) {
		var search = {type:'Name','term':term,active:true,integrator:false,outofdomain:true};
		return demographicService.search(search,0,25).then(function(response){
			var resp = [];
			for(var x=0;x<response.content.length;x++) {
				resp.push({demographicNo:response.content[x].demographicNo,name:response.content[x].lastName+', '+response.content[x].firstName});
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
		if (model instanceof Object) {
			$scope.search.mrpNo = Number(model.mrpNo);
			$scope.consult.mrpName = model.name;
		} else {
			providerService.getProvider(model).then(function(data){
				$scope.search.mrpNo = Number(model);
				$scope.consult.mrpName = data.lastName+", "+data.firstName;
				$scope.doSearch(true);
			});
		}
	}
	
	$scope.updateDemographicNo = function(item, model, label) {
		if (item!=null) {
			$scope.search.demographicNo = item.demographicNo;
			$scope.consult.demographicName = item.name;
		} else {
			demographicService.getDemographic(model).then(function(data){
				$scope.search.demographicNo = data.demographicNo;
				$scope.consult.demographicName = data.lastName+", "+data.firstName;
				$scope.doSearch(true);
			});
		}
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
		var url = "/record/"+$scope.search.demographicNo+"/consultResponse/new";
		if (consult!="new") url = "/record/"+consult.demographic.demographicNo+"/consultResponse/"+consult.id;
		
		$location.path(url).search($scope.searchParams);
	}
	
	$scope.addConsult = function() {
		if (!$scope.consultWriteAccess) {
			alert("You don't have right to create new consult response");
			return false;
		}
		
		$scope.editConsult("new");
	}
	
	$scope.removeDemographicAssignment = function() {
		$scope.search.demographicNo = null;
		$scope.consult.demographicName = null;
	}
	
	$scope.removeMrpAssignment = function() {
		$scope.search.mrpNo = null;
		$scope.consult.mrpName = null;
	}
	
	$scope.clear = function() {
		$scope.removeDemographicAssignment(); 	 
		$scope.removeMrpAssignment();
		$scope.search = {team:allTeams, startIndex:0, numToReturn:10, demographicNo:parseInt($state.params.demographicNo)};
		$scope.doSearch();
	}
	
	$scope.doSearch = function(init) {
		if (init) {
			$scope.tableParams.reload();
		} else {
			$scope.toPage = 1;
			$scope.tableParams.reload();
			$scope.toPage = null;
		}
	}
	
	//retain search & filters for users to go back
	$scope.setSearchParams = function() {
		$scope.searchParams = {};
		if ($state.$current=="record.consultResponses") $scope.searchParams.list = "patient";
		else if ($scope.search.demographicNo!=null) $scope.searchParams.srhDemoNo = $scope.search.demographicNo;
		
		if ($scope.search.mrpNo!=null) $scope.searchParams.srhMrpNo = $scope.search.mrpNo;
		if ($scope.search.status!=null) $scope.searchParams.srhStatus = $scope.search.status;
		if ($scope.search.team!=allTeams) $scope.searchParams.srhTeam = $scope.search.team;
		if ($scope.search.referralStartDate!=null) $scope.searchParams.srhRefStartDate = $scope.search.referralStartDate.getTime();
		if ($scope.search.referralEndDate!=null) $scope.searchParams.srhRefEndDate = $scope.search.referralEndDate.getTime();
		if ($scope.search.appointmentStartDate!=null) $scope.searchParams.srhApptStartDate = $scope.search.appointmentStartDate.getTime();
		if ($scope.search.appointmentEndDate!=null) $scope.searchParams.srhApptEndDate = $scope.search.appointmentEndDate.getTime();
		
		if ($scope.tableParams.$params.page>1) $scope.searchParams.srhToPage = $scope.tableParams.$params.page;
		if ($scope.tableParams.$params.count>10) $scope.searchParams.srhCountPerPage = $scope.tableParams.$params.count;
		if ($scope.tableParams.$params.sorting["ReferralDate"]!="desc") {
			$scope.searchParams.srhSortMode = Object.keys($scope.tableParams.$params.sorting);
			$scope.searchParams.srhSortDir = $scope.tableParams.$params.sorting[$scope.searchParams.srhSortMode];
		}
	}
	
	$scope.tableParams = new ngTableParams({
		page: 1,	// show first page
		count: 10,	// initial count per page
		sorting: {
			ReferralDate: 'desc'	// initial sorting
		}
	}, {
		total: 0,	// length of data
		getData: function($defer, params) {
			if ($scope.toPage!=null) $scope.tableParams.$params.page = $scope.toPage;
			if ($scope.countPerPage!=null) $scope.tableParams.$params.count = $scope.countPerPage;
			if ($scope.sortMode!=null) $scope.tableParams.$params.sorting = $scope.sortMode;
			$scope.setSearchParams();
			
			var count = params.url().count;
			var page = params.url().page;
			
			$scope.search.startIndex = ((page-1)*count);
			$scope.search.numToReturn=parseInt(count);
			
			var search1 = angular.copy($scope.search);
			search1.params = params.url();
			
			if(search1.team === allTeams) {
					search1.team = null;
			}
			
			consultService.searchResponses(search1).then(function(result){
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
				 }
				 $scope.lastResponse = result.content;
				 
			},function(reason){
				alert(reason);
			});
		 }
	});

	//process search parameters
	if ($state.params.demographicNo!=null) {
		$scope.hideSearchPatient = true;
		$scope.updateDemographicNo(null, $state.params.demographicNo);
	}
	else if ($location.search().srhDemoNo!=null) {
		$scope.updateDemographicNo(null, $location.search().srhDemoNo);
	}
	if ($location.search().srhMrpNo!=null) $scope.updateMrpNo($location.search().srhMrpNo);
	if ($location.search().srhRefStartDate!=null) $scope.search.referralStartDate = new Date(Number($location.search().srhRefStartDate));
	if ($location.search().srhRefEndDate!=null) $scope.search.referralEndDate = new Date(Number($location.search().srhRefEndDate));
	if ($location.search().srhApptStartDate!=null) $scope.search.appointmentStartDate = new Date(Number($location.search().srhApptStartDate));
	if ($location.search().srhApptEndDate!=null) $scope.search.appointmentEndDate = new Date(Number($location.search().srhApptEndDate));
	if ($location.search().srhStatus!=null) $scope.search.status = Number($location.search().srhStatus);
	if ($location.search().srhTeam!=null) $scope.search.team = $location.search().srhTeam;
	if ($location.search().srhCountPerPage!=null) $scope.countPerPage = $location.search().srhCountPerPage;
	if ($location.search().srhToPage!=null) $scope.toPage = $location.search().srhToPage;
	if ($location.search().srhSortMode!=null && $location.search().srhSortDir!=null) {
		$scope.sortMode = {};
		$scope.sortMode[$location.search().srhSortMode] = $location.search().srhSortDir;
	}
	$scope.doSearch(true);
	$scope.countPerPage = null;
	$scope.toPage = null;
	$scope.sortMode = null;
	
});
