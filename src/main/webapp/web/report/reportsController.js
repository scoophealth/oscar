oscarApp.factory('ReportNavigation', function($rootScope) {
return {
  location: '',
  
  load: function(msg) {
    this.location = msg;
  }
}
});

oscarApp.controller('ReportsCtrl', function ($scope,$state,$filter,$log, ReportNavigation) {
	
	$scope.reportSidebar = ReportNavigation;
	
	$scope.$emit('configureShowPatientList', false);

	$scope.reports = [
	                  {name:'Daysheets', templateUrl:'report/report_daysheet.jsp'},
	                  {name:'Active Patients',templateUrl:'report/report_iframe.jsp',iframeUrl:'../report/reportactivepatientlist.jsp'},
	                  {name:'Old Patients', templateUrl:'report/report_oldPatients.jsp'},
	                  {name:'Patient Chart List', templateUrl:'report/report_patientChartList.jsp'},
	                  {name:'EDB List', templateUrl:'report/report_edb_list.jsp'},
	                  {name:'Bad Appointments', templateUrl:'report/report_badAppointments.jsp'},
	                  {name:'No Show Appointments', templateUrl:'report/report_noShowAppointments.jsp'},
	                  {name:'Consultations',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/ConsultationReport.jsp'},
	                  {name:'Lab Requisitions',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/LabReqReport.jsp'},
	                  {name:'Preventions',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarPrevention/PreventionReporting.jsp'},
	                  {name:'Injections',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/InjectionReport2.jsp'},
	                  {name:'Demographic Report Tool',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/ReportDemographicReport.jsp'},                  
	                  {name:'Patient Study List',templateUrl:'report/report_iframe.jsp',iframeUrl:'../report/demographicstudyreport.jsp'},
	                  {name:'Chronic Disease Management',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/oscarMeasurements/SetupSelectCDMReport.do'},
	                  {name:'Waiting List',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarWaitingList/SetupDisplayWaitingList.do?waitingListId='},
	                  {name:'Forms',templateUrl:'report/report_iframe.jsp',iframeUrl:'../report/reportFormRecord.jsp'},
	                  {name:'Clinical',templateUrl:'report/report_iframe.jsp',iframeUrl:'../report/ClinicalReports.jsp'},
	                  {name:'SCBP demographic Report',templateUrl:'report/report_iframe.jsp',iframeUrl:'../report/reportBCARDemo.jsp'},
	                  {name:'Report By Template',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/reportByTemplate/homePage.jsp'},
	                  {name:'General Forms', group:'CAISI',templateUrl:'report/report_iframe.jsp',iframeUrl:'../PMmodule/ClientManager.do?method=getGeneralFormsReport'},
	                  {name:'Registration Intake', group:'CAISI',templateUrl:'report/report_registration_intake.jsp'},
	                  {name:'Follow-up Intake', group:'CAISI',templateUrl:'report/report_followup_intake.jsp'},
	                  {name:'Activity Report', group:'CAISI',templateUrl:'report/report_iframe.jsp',iframeUrl:'../PMmodule/Reports/ProgramActivityReport.do'},
	                  {name:'UCF Report', group:'CAISI',templateUrl:'report/report_iframe.jsp',iframeUrl:'../SurveyManager.do?method=reportForm'},
	                  {name:'SH Mental Health Report', group:'CAISI',templateUrl:'report/report_sh_mental_health.jsp'},
	                  {name:'OSIS Report', group:'Public Health',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/OSISReport.jsp'},
	                  {name:'One Time Consult CDS Report', group:'Public Health',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/CDSOneTimeConsultReport.jsp'},
	                  
	                  ];
	
	
	$scope.editDemographicSet = function() {
		$scope.selectReport({name:'Demographic Set Edit',templateUrl:'report/report_iframe.jsp',iframeUrl:'../oscarReport/demographicSetEdit.jsp'});
	}
			
	$scope.getReports = function() {
		if($scope.reportGroup != null && $scope.reportGroup.length>0) {
			var filtered = $filter('filter')($scope.reports, {group: $scope.reportGroup});
			return filtered;
			
		}
		return $scope.reports;
	}
	
	$scope.getReportGroups = function() {
		var groups = [{value:'',label:'All Groups'}];
		var groupMap = {};
		
		for(var i=0;i<$scope.reports.length;i++) {
			if($scope.reports[i].group != null) {
				groupMap[$scope.reports[i].group] = $scope.reports[i].group;
			}
		}
		
		for(var key in groupMap){
			groups.push({value:key,label:key});
		}
		
		return groups;
	}
	
	$scope.selectReport = function(report) {
		$scope.currentReport = report;
		
		if(report.direct === true) {
			window.open(report.window.url,report.name,'width='+report.window.width+'&height='+report.window.height);
		} else {
			ReportNavigation.load(report.templateUrl);
		}
	}
	

	$scope.selectReport($scope.reports[0]);

	$scope.openReportWindow = function(url,name) {
		window.open(url,name,'height=900,width=700');
	}
});


oscarApp.controller('ReportEdbListCtrl', function ($scope,$log,$filter) {
	$scope.params = {startDate:new Date(), endDate:new Date(), version:'', region:'ON'}; //todo: grab region from somewhere
	
	$scope.generateReport = function() {
		$log.log('run edb report');
		var startDate = $filter('date')($scope.params.startDate, 'yyyy-MM-dd');
		var endDate = $filter('date')($scope.params.endDate, 'yyyy-MM-dd');
		
		var url = '';
		
		if($scope.params.region === 'BC') {
			if($scope.params.version == '05') {
				url = '../report/reportbcedblist2007.jsp?startDate=' + startDate + '&endDate=' + endDate;
			} else {
				url = '../report/reportbcedblist.jsp?startDate=' + startDate + '&endDate=' + endDate;
			}
		}
		
		if($scope.params.region === 'ON') {
			if($scope.params.version == '05') {
				url = '../report/reportonedblist.jsp?startDate='+startDate+'&endDate='+endDate;
			} else {
				url = '../report/reportnewedblist.jsp?startDate='+startDate+'&endDate='+endDate;
			}
		}
		
		 
		window.open(url,'report_edb','height=900,width=700');
		 
	}
});

oscarApp.controller('ReportDaySheetCtrl', function ($scope,$log,providerService,$filter) {
    
    $scope.getTime = function(hour,minutes) {
    	var d = new Date();
	    d.setHours( hour );
	    d.setMinutes( minutes );
	    return d;
    }
    
    $scope.params = {providerNo:'',type:'',startDate:new Date(),endDate:new Date(),startTime:$scope.getTime(8,0), endTime:$scope.getTime(18,0)};
    
	$scope.searchProviders = function(val) {
    	var search = {searchTerm:val,active:true};
    	return providerService.searchProviders(search,0,10).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({providerNo:response[x].providerNo,name:response[x].firstName + ' ' + response[x].lastName});
    		}
    		return resp;
    	});
    }
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.params.providerNo = model;
    	$scope.data.providerNo = label;
    }
    
    $scope.generateReport = function() {
    	var p = $scope.params;
    	if(p.type === 'all' || p.type === 'all-nr') {
    		var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    		var endDate = $filter('date')(p.endDate, 'yyyy-MM-dd');
    		
    		var startTime = $filter('date')(p.startTime, 'HH:mm');
    		var endTime = $filter('date')(p.endTime, 'HH:mm');
    		
    		var url = '../report/reportdaysheet.jsp?dsmode=all&provider_no='+ (p.providerNo===''?'*':p.providerNo) + '&sdate=' + startDate + '&edate=' + endDate + '&sTime=' + startTime + '&eTime=' + endTime;
    		
    		if(p.type === 'all-nr') {
        		url += '&rosteredStatus=true';
        	}
    		
    		window.open(url,'report_daysheet','height=900,width=700');
    	}
    	
    	if(p.type === 'new') {
    		var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    		var url = '../report/reportdaysheet.jsp?dsmode=new&provider_no='+ (p.providerNo===''?'*':p.providerNo) + '&sdate=' + startDate;
    		window.open(url,'report_daysheet','height=900,width=700');
     	}    	
    	if(p.type === 'lab') {
    		var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    		var url = '../report/printLabDaySheetAction.do?xmlStyle=labDaySheet.xml&input_date=' + startDate;
    		window.open(url,'report_daysheet','height=900,width=700');

    	}
    	if(p.type === 'billing') {
    		var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    		var url = '../report/printLabDaySheetAction.do?xmlStyle=billDaySheet.xml&input_date=' + startDate;
    		window.open(url,'report_daysheet','height=900,width=700');
    	}
    	if(p.type === 'tab') {
    		var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    		var url = '../report/tabulardaysheetreport.jsp?provider_no='+ (p.providerNo===''?'*':p.providerNo) +'&sdate=' + startDate;
    		window.open(url,'report_daysheet','height=900,width=700');
    	}
    }
    
    $scope.reset = function() {
    	 $scope.params = {providerNo:'',type:'',startDate:new Date(),endDate:new Date(),startTime:$scope.getTime(8,0), endTime:$scope.getTime(18,0)};
    }
});

oscarApp.controller('ReportOldPatientsCtrl', function ($scope,$log, providerService) {
    $scope.params = {providerNo:'', age:65};
    
	$scope.searchProviders = function(val) {
    	var search = {searchTerm:val,active:true};
    	return providerService.searchProviders(search,0,10).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({providerNo:response[x].providerNo,name:response[x].firstName + ' ' + response[x].lastName});
    		}
    		return resp;
    	});
    }
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.params.providerNo = model;
    	$scope.data.providerNo = label;
    }
    
    $scope.generateReport = function() {
    	var p = $scope.params;
    	var url = '../report/reportpatientchartlistspecial.jsp?provider_no='+ (p.providerNo===''?'*':p.providerNo) +'&age=' + p.age;
		window.open(url,'report_oldpatients','height=900,width=700');	
    }
});

oscarApp.controller('ReportPatientChartListCtrl', function ($scope,$log, providerService) {
    $scope.params = {providerNo:''};
    
	$scope.searchProviders = function(val) {
    	var search = {searchTerm:val,active:true};
    	return providerService.searchProviders(search,0,10).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({providerNo:response[x].providerNo,name:response[x].firstName + ' ' + response[x].lastName});
    		}
    		return resp;
    	});
    }
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.params.providerNo = model;
    	$scope.data.providerNo = label;
    }
    
    $scope.generateReport = function() {
    	var p = $scope.params;
    	if(p.providerNo == '') {
    		alert('Please enter a provider');
    		return false;
    	}
    	var url = '../report/reportpatientchartlist.jsp?provider_no='+ p.providerNo;
		window.open(url,'report_patientchartlist','height=900,width=700');	
    }
});

oscarApp.controller('ReportBadAppointmentSheetCtrl', function ($scope,$log, providerService, $filter) {
    $scope.params = {providerNo:'',startDate:new Date()};
    
	$scope.searchProviders = function(val) {
    	var search = {searchTerm:val,active:true};
    	return providerService.searchProviders(search,0,10).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({providerNo:response[x].providerNo,name:response[x].firstName + ' ' + response[x].lastName});
    		}
    		return resp;
    	});
    }
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.params.providerNo = model;
    	$scope.data.providerNo = label;
    }
    
    $scope.generateReport = function() {
    	var p = $scope.params;
    	var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    		
    	var url = '../report/reportapptsheet.jsp?dsmode=all&provider_no='+ p.providerNo + '&sdate=' + startDate;
		window.open(url,'report_badApptSheet','height=900,width=700');	
    }
});

oscarApp.controller('ReportNoShowAppointmentSheetCtrl', function ($scope,$log, providerService, $filter) {
    $scope.params = {providerNo:'',startDate:new Date()};
    
	$scope.searchProviders = function(val) {
    	var search = {searchTerm:val,active:true};
    	return providerService.searchProviders(search,0,10).then(function(response){
    		var resp = [];
    		for(var x=0;x<response.length;x++) {
    			resp.push({providerNo:response[x].providerNo,name:response[x].firstName + ' ' + response[x].lastName});
    		}
    		return resp;
    	});
    }
    $scope.updateProviderNo = function(item,model,label) {
    	$scope.params.providerNo = model;
    	$scope.data.providerNo = label;
    }
    
    $scope.generateReport = function() {
    	var p = $scope.params;
    	var startDate = $filter('date')(p.startDate, 'yyyy-MM-dd');
    	var url = '../report/reportnoshowapptlist.jsp?provider_no='+ p.providerNo + '&sdate=' + startDate;
		window.open(url,'report_noShowApptSheet','height=900,width=700');	
    }
});


oscarApp.controller('ReportRegistrationIntakeCtrl', function ($scope,$log,$filter) {
	$scope.params = {startDate:new Date(), endDate:new Date(), includePastForms:true};
	
	$scope.generateReport = function() {
		$log.log('run registration intake report');
		var startDate = $filter('date')($scope.params.startDate, 'yyyy-MM-dd');
		var endDate = $filter('date')($scope.params.endDate, 'yyyy-MM-dd');
		
		var url = '../PMmodule/GenericIntake/Report.do?method=report&type=quick&startDate='+startDate+'&endDate='+endDate+'&includePast='+$scope.params.includePastForms;
		window.open(url,'report_registration_intake','height=900,width=700');
		 
	}
});


oscarApp.controller('ReportFollowUpIntakeCtrl', function ($scope,$log,$filter) {
	$scope.params = {startDate:new Date(), endDate:new Date(), includePastForms:true};
	
	$scope.generateReport = function() {
		$log.log('run follow up intake report');
		var startDate = $filter('date')($scope.params.startDate, 'yyyy-MM-dd');
		var endDate = $filter('date')($scope.params.endDate, 'yyyy-MM-dd');
		
		var url = '../PMmodule/GenericIntake/Report.do?method=report&type=indepth&startDate='+startDate+'&endDate='+endDate+'&includePast='+$scope.params.includePastForms;
		window.open(url,'report_followup_intake','height=900,width=700');
		 
	}
});

oscarApp.controller('ReportSHMentalHealthCtrl', function ($scope,$log,$filter) {
	$scope.params = {startDate:new Date()};
	
	$scope.generateReport = function() {
		$log.log('run sh mental health report');
		var startDate = $filter('date')($scope.params.startDate, 'yyyy-MM-dd');
		
		var url = '../PMmodule/StreetHealthIntakeReportAction.do?startDate='+startDate;
		window.open(url,'report_sh','height=900,width=700');
		 
	}
});

