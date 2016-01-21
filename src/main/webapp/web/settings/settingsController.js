oscarApp.controller('SettingsCtrl', function ($scope,$http,$state, providerList, user, billingServiceTypes, 
		loadedSettings, providerService, $modal, encounterForms,eforms, $filter,teams,groupNames,loadedApps,appService) {

	$scope.$emit('configureShowPatientList', false);
	 
	$scope.providerList = providerList;
	$scope.user = user;
	$scope.billingServiceTypes = billingServiceTypes;
	$scope.pref = loadedSettings;
	$scope.encounterForms = encounterForms;
	$scope.eforms = eforms;
	$scope.loadedApps = loadedApps;
	
	if($scope.pref.recentPatients==null){
		$scope.pref.recentPatients="8";
	}
	
	//convert to value/label object list from string array
	$scope.formGroupNames = [{"value":"","label":"None"}];
	for(var i=0;i<groupNames.length;i++) {
		$scope.formGroupNames.push({"value":groupNames[i],"label":groupNames[i]});
	}
	
	
	//convert to value/label obj list. Add all/none
	$scope.teams = [{"value":"-1","label":"All"}];
	for(var i=0;i<teams.length;i++) {
		$scope.teams.push({"value":teams[i],"label":teams[i]});
	}
	$scope.teams.push({"value":"","label":"None"});
		
	//add none -option to start.
	$scope.billingServiceTypesMod = [];
	angular.copy($scope.billingServiceTypes,$scope.billingServiceTypesMod);
	$scope.billingServiceTypesMod.splice(0, 0, {"type":"no","name":"--None--"});

	//this needs to be done to do the weird checkbox lists. basically add a property to each encounterList object called checked:[true|false]
	for(var i=0;i<$scope.pref.appointmentScreenForms.length;i++) {
		var selected = $filter('filter')($scope.encounterForms, {formName: $scope.pref.appointmentScreenForms[i]});
		if(selected != null) {
			for(var x=0;x<selected.length;x++) {
				if(selected[x].formName === $scope.pref.appointmentScreenForms[i]) {
					selected[x].checked=true;
				}
			}
		}
	}
	
	//this needs to be done to do the weird checkbox lists. basically add a property to each encounterList object called checked:[true|false]
	for(var i=0;i<$scope.pref.appointmentScreenEforms.length;i++) {
		var selected = $filter('filter')($scope.eforms, {id: $scope.pref.appointmentScreenEforms[i]});
		if(selected != null) {
			for(var x=0;x<selected.length;x++) {
				if(selected[x].id === $scope.pref.appointmentScreenEforms[i]) {
					selected[x].checked=true;
				}
			}
		}
	}
	
	
	$scope.tabs = [
	                 	{id : 0,displayName : 'Persona',path : 'persona'},
	                 	{id : 1,displayName : 'General',path : 'general'},	                 	
	                 	{id : 2,displayName : 'Schedule',path : 'schedule'},
	                 	{id : 3,displayName : 'Billing',path : 'billing'},
	                 	{id : 4,displayName : 'Rx',path : 'rx'},
	                 	{id : 5,displayName : 'Master Demographic',path : 'masterdemo'},
	                 	{id : 6,displayName : 'Consultations',path : 'consults'}, 
	                 	{id : 7,displayName : 'Documents',path : 'documents'},
	                 	{id : 8,displayName : 'Summary',path : 'summary'},
	                 	{id : 9,displayName : 'eForms',path : 'eforms'},
	                 	{id : 10,displayName : 'Inbox',path : 'inbox'},
	                 	{id : 11,displayName : 'Programs',path : 'programs'},
	                 	{id : 12,displayName : 'Integration',path : 'integration'},
	];
	$scope.pageSizes = [{value:'PageSize.A4',label:'A4'},{value:'PageSize.A6',label:'A6'}];
	
	$scope.rxInteractionWarningLevels = [
	                    {value:'0',label:'Not Specified'},
	                    {value:'1',label:'Low'},
	                    {value:'2',label:'Medium'},
	                    {value:'3',label:'High'},
	                    {value:'4',label:'None'}
	];
	
	$scope.staleDates = [
	                     {value:'A',label:'All'},    
	                     {value:'0',label:'0'}, 
	                     {value:'-1',label:'-1'},  {value:'-2',label:'2'},   {value:'-3',label:'3'},   {value:'-4',label:'4'},  
	                     {value:'-5',label:'5'},   {value:'-6',label:'6'},   {value:'-7',label:'7'},   {value:'-8',label:'8'},  
	                     {value:'-9',label:'9'},   {value:'-10',label:'10'},   {value:'-11',label:'11'},   {value:'-12',label:'12'},  
	                     {value:'-13',label:'13'},   {value:'-14',label:'14'},   {value:'-15',label:'15'},   {value:'-16',label:'16'},  
	                     {value:'-17',label:'17'},   {value:'-18',label:'18'},   {value:'-19',label:'19'},   {value:'-20',label:'20'},  
	                     {value:'-21',label:'21'},   {value:'-22',label:'22'},   {value:'-23',label:'23'},   {value:'-24',label:'24'},  
	                     {value:'-25',label:'25'},   {value:'-26',label:'26'},   {value:'-27',label:'27'},   {value:'-28',label:'28'},  
	                     {value:'-29',label:'29'},   {value:'-30',label:'30'},   {value:'-31',label:'31'},   {value:'-32',label:'32'},  
	                     {value:'-33',label:'33'},   {value:'-34',label:'34'},   {value:'-35',label:'35'},   {value:'-36',label:'36'},  
	                     ];
	
	$scope.olisLabs = [
					{value:'',label:''},
					{value:'5552',label:'Gamma-Dynacare'},
					{value:'5407',label:'CML'},
					{value:'5687',label:'LifeLabs'}
	                   ];
	
	$scope.pasteFormats = [{value:'single',label:'Single Line'},{value:'multi',label:'Multi Line'}];
	
	$scope.letterHeadNameDefaults = [{value:'1',label:'Provider (user)'},{value:'2',label:'MRP'},{value:'3',label:'Clinic'}];
	
	if($scope.pref.consultationLetterHeadNameDefault==null){
		$scope.pref.consultationLetterHeadNameDefault = "1";
	}

	
	$scope.currentTab = $scope.tabs[0];
	
	$scope.isActive = function(tab) {
		return (tab != null && $scope.currentTab != null && tab.id == $scope.currentTab.id);
	}
	
	$scope.changeTab = function(tab) {
		$scope.currentTab = tab;
	}
	
	$scope.save = function() {
		var newList = [];
		for(var i=0;i<$scope.pref.appointmentScreenQuickLinks.length;i++) {
    		if($scope.pref.appointmentScreenQuickLinks[i].checked == null || $scope.pref.appointmentScreenQuickLinks[i].checked == false) {			
    			newList.push({name:$scope.pref.appointmentScreenQuickLinks[i].name,url:$scope.pref.appointmentScreenQuickLinks[i].url});
    		}
    	}
    	$scope.pref.appointmentScreenQuickLinks = newList;
    	
		providerService.saveSettings($scope.user.providerNo,$scope.pref).then(function(data){
			alert('saved');
		});
		
	}
	
	$scope.cancel = function() {
		$scope.pref={};
		$state.go('dashboard');
	}

    $scope.selectEncounterForms = function () {
    	var selected = $filter('filter')($scope.encounterForms, {checked: true});
    	var tmp = [];
        for(var i=0;i<selected.length;i++) {
        	tmp.push(selected[i].formName);
        }
        $scope.pref.appointmentScreenForms = tmp;
    }
    
    $scope.selectEForms = function () {
    	var selected = $filter('filter')($scope.eforms, {checked: true});
    	var tmp = [];
        for(var i=0;i<selected.length;i++) {
        	tmp.push(selected[i].id);
        }
        $scope.pref.appointmentScreenEforms = tmp;
    }
    
    $scope.removeQuickLinks = function() {
    	var newList = [];
    	
    	for(var i=0;i<$scope.pref.appointmentScreenQuickLinks.length;i++) {
    		if($scope.pref.appointmentScreenQuickLinks[i].checked == null || $scope.pref.appointmentScreenQuickLinks[i].checked == false) {  			
    			newList.push($scope.pref.appointmentScreenQuickLinks[i]);
    		}
    	}
    	$scope.pref.appointmentScreenQuickLinks = newList;
    }
    
    
    $scope.openChangePasswordModal = function () {
        /*
        var modalInstance = $modal.open({
        	templateUrl: 'settings/changePassword.jsp',
            controller: 'ChangePasswordCtrl'
        });
     */
    	window.open('../provider/providerchangepassword.jsp','change_password','width=750,height=500');
    }
    
    
    $scope.openQuickLinkModal = function () {       
        var modalInstance = $modal.open({
        	templateUrl: 'settings/quickLink.jsp',
            controller: 'QuickLinkCtrl'
        });
        
        modalInstance.result.then(function(selectedItem){
        	if(selectedItem != null) {
	        	if(selectedItem != null && selectedItem.name != null && selectedItem.url != null) {
	        		$scope.pref.appointmentScreenQuickLinks.push(selectedItem);
	        	}
        	}
        })
    }
    
    $scope.editDocumentTemplates = function() {
    	window.open('../admin/displayDocumentDescriptionTemplate.jsp','document_templates','width=700,height=450');
    }
    
    $scope.showProviderColourPopup = function() {
    	window.open('../provider/providerColourPicker.jsp','provider_colour','width=700,height=450');
    }
    
    $scope.showDefaultEncounterWindowSizePopup = function() {
    	window.open('../setProviderStaleDate.do?method=viewEncounterWindowSize','encounter_window_sz','width=700,height=450');
    }
    
    $scope.openConfigureEChartCppPopup = function() {
    	window.open('../provider/CppPreferences.do','configure_echart_cpp','width=700,height=450');
    }
    
    $scope.openManageAPIClientPopup = function() {
    	window.open('../provider/clients.jsp','api_clients','width=700,height=450');
    }
    
    $scope.openMyOscarUsernamePopup = function() {
    	window.open('../provider/providerIndivoIdSetter.jsp','invivo_setter','width=700,height=450');
    }
    
    $scope.authenticate = function(app){
    	window.open('../apps/oauth1.jsp?id='+app.id,'appAuth','width=700,height=450');
    }
    
    $scope.refreshAppList = function(){
    	console.log("refresh",$scope.loadedApps);
    	appService.getApps().then(function(data) {
    		$scope.loadedApps = data;
        },
    		function(errorMessage){
    	       console.log("applist:"+errorMessage);
     		}
        );
    	///
    	console.log("refresh",$scope.loadedApps);
    }
	
});


oscarApp.controller('ChangePasswordCtrl',function($scope, $modalInstance) {
    
    $scope.close = function () {
        $modalInstance.close("Someone Closed Me");
    }
   
    $scope.changePassword = function() {
    	console.log('password saved - NOT');
    	$modalInstance.close("Someone Saved Me");
    }
});

oscarApp.controller('QuickLinkCtrl',function($scope, $modalInstance) {
    
	$scope.qll = {};
	
    $scope.close = function () {
        $modalInstance.close();
    }
   
    $scope.addQuickLink = function(qlForm) {
    	if(qlForm.$valid) {
    	//	alert($scope.qll.toSource());
        	$modalInstance.close($scope.qll);
    	}
    }
});