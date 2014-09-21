/*

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

*/
oscarApp.controller('DetailsCtrl', function ($scope,$http,$location,$stateParams,$state,demographicService,patientDetailStatusService,securityService,demo,user) {
	console.log("details ctrl ", $stateParams, $state, demo);
	
	var page = {};
	$scope.page = page;
	$scope.page.demo = demo;

	//get access rights
	securityService.hasRight("_demographic", "r", demo.demographicNo).then(function(data){
		page.canRead = data.value;
	});
	securityService.hasRight("_demographic", "u", demo.demographicNo).then(function(data){
		page.cannotChange = !data.value;
	});
	
	//get patient detail status
	patientDetailStatusService.getStatus(demo.demographicNo).then(function(data){
		page.macPHRLoggedIn = data.macPHRLoggedIn;
		page.macPHRIdsSet = data.macPHRIdsSet;
		page.macPHRVerificationLevel = data.macPHRVerificationLevel;
		
		page.integratorEnabled = data.integratorEnabled;
		page.integratorOffline = data.integratorOffline;
		page.integratorAllSynced = data.integratorAllSynced;
		
		page.conformanceFeaturesEnabled = data.conformanceFeaturesEnabled;
		page.workflowEnhance = data.workflowEnhance;
		page.billregion = data.billregion;
		page.defaultView = data.defaultView;
		page.hospitalView = data.hospitalView;
		
		if (page.integratorEnabled) {
			if (page.integratorOffline) {
				page.integratorStatusColor = "#ff5500";
				page.integratorStatusMsg = "NOTE: Integrator is not available at this time";
			}
			else if (!page.integratorAllSynced) {
				page.integratorStatusColor = "#ff5500";
				page.integratorStatusMsg = "NOTE: Integrated Community is not synced";
			}
		}
		
		page.billingHistoryLabel = "Invoice List";
		if (page.billregion=="ON") page.billingHistoryLabel = "Billing History";
	});

	//show notes
	if (demo.notes!=null) {
		var pageNotes = demo.notes.substring("<unotes>".length);
		pageNotes = pageNotes.substring(0, pageNotes.length-("</unotes>".length));
		page.notes = pageNotes;
	}
	
	//show referral doctor
	if (demo.familyDoctor!=null) {
		var referralDoc = demo.familyDoctor;
		var begin = referralDoc.indexOf("<rdohip>") + "<rdohip>".length;
		var end = referralDoc.indexOf("</rdohip>");
		if (end>begin && end>=0 && begin>=0) page.referralDocNo = referralDoc.substring(begin, end);
		
		begin = referralDoc.indexOf("<rd>") + "<rd>".length;
		end = referralDoc.indexOf("</rd>");
		if (end>begin && end>=0 && begin>=0) page.referralDoc = referralDoc.substring(begin, end);
	}
	
	//initialize "extras" in page
	page.demoCell = {};
	page.aboriginal = {};
	page.hPhoneExt = {};
	page.wPhoneExt = {};
	page.cytolNum = {};
	page.phoneComment = {};
	page.paperChartArchived = {};
	page.paperChartArchivedDate = {};
	page.usSigned = {};
	page.privacyConsent = {};
	page.informedConsent = {};
	page.securityQuestion1 = {};
	page.securityAnswer1 = {};
	
	//show extras
	if (demo.extras!=null) {
		if (demo.extras.key!=null) { //only 1 entry
			var tmp = copyDemoExt(demo.extras);
			demo.extras = [];
			demo.extras.push(tmp);
		}
		for (var i=0; i<demo.extras.length; i++) {
			if (demo.extras[i].key=="demo_cell") page.demoCell = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="aboriginal") page.aboriginal = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="hPhoneExt") page.hPhoneExt = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="wPhoneExt") page.wPhoneExt = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="cytolNum") page.cytolNum = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="phoneComment") page.phoneComment = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="paper_chart_archived") page.paperChartArchived = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="paper_chart_archived_date") page.paperChartArchivedDate = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="usSigned") page.usSigned = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="privacyConsent") page.privacyConsent = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="informedConsent") page.informedConsent = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="securityQuestion1") page.securityQuestion1 = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="securityAnswer1") page.securityAnswer1 = copyDemoExt(demo.extras[i]);
		}
	}
	
	//show dates
	var datePart = getDateParts(demo.endDate);
	if (datePart!=null) {
		page.endDateYear = datePart["year"];
		page.endDateMonth = pad0(datePart["month"]);
		page.endDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.effDate);
	if (datePart!=null) {
		page.hcEffDateYear = datePart["year"];
		page.hcEffDateMonth = pad0(datePart["month"]);
		page.hcEffDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.hcRenewDate);
	if (datePart!=null) {
		page.hcRenewDateYear = datePart["year"];
		page.hcRenewDateMonth = pad0(datePart["month"]);
		page.hcRenewDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.rosterDate);
	if (datePart!=null) {
		page.rosterDateYear = datePart["year"];
		page.rosterDateMonth = pad0(datePart["month"]);
		page.rosterDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.rosterTerminationDate);
	if (datePart!=null) {
		page.rosterTerminationDateYear = datePart["year"];
		page.rosterTerminationDateMonth = pad0(datePart["month"]);
		page.rosterTerminationDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.patientStatusDate);
	if (datePart!=null) {
		page.patientStatusDateYear = datePart["year"];
		page.patientStatusDateMonth = pad0(datePart["month"]);
		page.patientStatusDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.dateJoined);
	if (datePart!=null) {
		page.dateJoinedYear = datePart["year"];
		page.dateJoinedMonth = pad0(datePart["month"]);
		page.dateJoinedDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.onWaitingListSinceDate);
	if (datePart!=null) {
		page.onWaitingListSinceYear = datePart["year"];
		page.onWaitingListSinceMonth = pad0(datePart["month"]);
		page.onWaitingListSinceDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(page.paperChartArchivedDate.value);
	if (datePart!=null) {
		page.paperChartArchivedDateYear = datePart["year"];
		page.paperChartArchivedDateMonth = pad0(datePart["month"]);
		page.paperChartArchivedDateDay = pad0(datePart["day"]);
	}
	
	var colorAttn = "#ffff99";
	
	//show phone numbers with preferred check
	page.cellPhone = getPhoneNum(page.demoCell.value);
	page.homePhone = getPhoneNum(demo.phone);
	page.workPhone = getPhoneNum(demo.alternativePhone);
	
	var defPhTitle = "Check to set preferred contact number";
	var prefPhTitle = "Preferred contact number";
	
	page.cellPhonePreferredMsg = defPhTitle;
	page.homePhonePreferredMsg = defPhTitle;
	page.workPhonePreferredMsg = defPhTitle;
	if (isPreferredPhone(page.demoCell.value)) {
		page.preferredPhone = "C";
		page.preferredPhoneNumber = page.cellPhone;
		page.cellPhonePreferredMsg = prefPhTitle;
		page.cellPhonePreferredColor = colorAttn;
	}
	else if (isPreferredPhone(demo.phone)) {
		page.preferredPhone = "H";
		page.preferredPhoneNumber = page.homePhone;
		page.homePhonePreferredMsg = prefPhTitle;
		page.homePhonePreferredColor = colorAttn;
	}
	else if (isPreferredPhone(demo.alternativePhone)) {
		page.preferredPhone = "W";
		page.preferredPhoneNumber = page.workPhone;
		page.workPhonePreferredMsg = prefPhTitle;
		page.workPhonePreferredColor = colorAttn;
	}
	else {
		page.preferredPhoneNumber = page.homePhone;
	}
	
	//show demoContacts/demoContactPros
	if (demo.demoContacts!=null) {
		demo.demoContacts = demoContactShow(demo.demoContacts);
	}
	if (demo.demoContactPros!=null) {
		demo.demoContactPros = demoContactShow(demo.demoContactPros);
	}

	//show doctors/nurses/midwives
	if (demo.doctors!=null) {
		demo.doctors = providerToArray(demo.doctors);
	}
	if (demo.nurses!=null) {
		demo.nurses = providerToArray(demo.nurses);
	}
	if (demo.midwives!=null) {
		demo.midwives = providerToArray(demo.midwives);
	}
	
	//show referralDoc specialties & names
	if (demo.referralDoctors!=null) {
		if (demo.referralDoctors.id!=null) { //only 1 entry, convert to array
			var tmp = {};
			tmp.name = demo.referralDoctors.name;
			tmp.referralNo = demo.referralDoctors.referralNo;
			tmp.specialtyType = demo.referralDoctors.specialtyType;
			demo.referralDoctors = [tmp];
		}
		for (var i=0; i<demo.referralDoctors.length; i++) {
			demo.referralDoctors[i].label = demo.referralDoctors[i].name;
			if (demo.referralDoctors[i].specialtyType!=null && demo.referralDoctors[i].specialtyType!="") {
				demo.referralDoctors[i].label += " ["+demo.referralDoctors[i].specialtyType+"]";
			}
		}
	}
	
	//show waitingListNames
	if (demo.waitingListNames!=null) {
		if (demo.waitingListNames.id!=null) { //only 1 entry, convert to array
			var tmp = {};
			tmp.id = demo.waitingListNames.id;
			tmp.name = demo.waitingListNames.name;
			tmp.groupNo = demo.waitingListNames.groupNo;
			tmp.providerNo = demo.waitingListNames.providerNo;
			tmp.createDate = demo.waitingListNames.createDate;
			tmp.isHistory = demo.waitingListNames.isHistory;
			demo.waitingListNames = [tmp];
		}
	}
	
	//show patientStatusList & rosterStatusList values
	demo.patientStatusList = statusValueToArray(demo.patientStatusList);
	demo.patientStatusList.unshift({"value":"FI", "label":"FI - Fired"});
	demo.patientStatusList.unshift({"value":"MO", "label":"MO - Moved"});
	demo.patientStatusList.unshift({"value":"DE", "label":"DE - Deceased"});
	demo.patientStatusList.unshift({"value":"IN", "label":"IN - Inactive"});
	demo.patientStatusList.unshift({"value":"AC", "label":"AC - Active"});
	
	demo.rosterStatusList = statusValueToArray(demo.rosterStatusList);
	demo.rosterStatusList.unshift({"value":"FS", "label":"FS - fee for service"});
	demo.rosterStatusList.unshift({"value":"TE", "label":"TE - terminated"});
	demo.rosterStatusList.unshift({"value":"NR", "label":"NR - not rostered"});
	demo.rosterStatusList.unshift({"value":"RO", "label":"RO - rostered"});
	

	//----------------------//
	// on-screen operations //
	//----------------------//
	//monitor data changed
	page.dataChanged = -2;
	$scope.$watch(function(){
		return $("#pd1").html()+$("#pd2").html();
	}, function(){
		page.dataChanged++;
		if (page.cannotChange) page.dataChanged = 0; //do not monitor data change if user only has read-access
	});
	$scope.needToSave = function(){
		if (page.dataChanged>0) return "btn-primary";
	}
	
	//remind user of unsaved data
	$scope.$on("$stateChangeStart", function(event){
		if (page.dataChanged>0) {
			var discard = confirm("You may have unsaved data. Are you sure to leave?");
			if (!discard) event.preventDefault();
		}
	});
	
	//disable click and keypress if user only has read-access
	$scope.checkAction = function(event){
		if (page.cannotChange) {
			event.preventDefault();
			event.stopPropagation();
			$scope.setSwipeReady();
		}
	}

	//format lastname, firstname
	$scope.formatLastName = function(){
		demo.lastName = demo.lastName.toUpperCase();
	}
	$scope.formatFirstName = function(){
		demo.firstName = demo.firstName.toUpperCase();
	}
	$scope.formatLastName(); //done on page load
	$scope.formatFirstName(); //done on page load
	
	//calculate age
	var now = new Date();
	$scope.calculateAge = function(){
		demo.age = now.getFullYear() - demo.dobYear;
		if (now.getMonth()<demo.dobMonth-1) demo.age--;
		else if (now.getMonth()==demo.dobMonth-1 && now.getDate()<demo.dobDay) demo.age--;
	}
	$scope.calculateAge(); //done on page load
	
	//set ready for swipe card
	$scope.setSwipeReady = function(status){
		if (status=="off") {
			page.readyForSwipe = "";
			page.swipecardMsg = "Click for Card Swipe";
		}
		else if (status=="done") {
			page.readyForSwipe = "btn-primary";
			page.swipecardMsg = "Done Health Card Swipe";
		}
		else {
			$("#swipecard").focus();
			page.readyForSwipe = "btn-success";
			page.swipecardMsg = "Ready for Card Swipe";
			page.swipecard = "";
		}
	}
	$scope.setSwipeReady(); //done on page load

	//Health card verification
	var hcParts = {};
	$scope.healthCardHandler = function(keycode){
	    if (keycode==13) { //carriage-return
	    	var swipeCardData = page.swipecard;
	    	page.swipecard = "";
	    	
	    	if (swipeCardData.substring(0,3)=="%E?") { //swipe card error
	    		alert("Error reading card");
	    	} else {
	    		if (swipeCardData.substring(2,8)=="610054") { //Ontario
	    			hcParts["issuer"] = "ON";
	    			hcParts["hin"] = swipeCardData.substring(8, 18);
	    			
	    			var namePos = swipeCardData.indexOf("^") + 1;
	    			var endNamePos = swipeCardData.indexOf("^", namePos);
	    			hcParts["fullName"] = swipeCardData.substring(namePos, endNamePos);
	    			hcParts["lastName"] = hcParts["fullName"].split("/")[0];
	    			hcParts["firstName"] = hcParts["fullName"].split("/")[1].trim();
	    			
	    			hcParts["sex"] = swipeCardData.substring(endNamePos + 8, endNamePos + 9);
	    			hcParts["dob"] = swipeCardData.substring(endNamePos + 9, endNamePos + 17);
	    			hcParts["hinExp"] = swipeCardData.substring(endNamePos + 1, endNamePos + 5) + hcParts["dob"].substring(6,8);
	    			hcParts["hinVer"] = swipeCardData.substring(endNamePos + 17, endNamePos + 19);
	    			hcParts["firstNameShort"] = swipeCardData.substring(endNamePos + 19, endNamePos + 24);
	    			hcParts["issueDate"] = swipeCardData.substring(endNamePos + 24, endNamePos + 30);
	    			hcParts["lang"] = swipeCardData.substring(endNamePos + 30, endNamePos + 32);
	    			
	    			if (notNumber(hcParts["dob"])) {
	    				hcParts["dob"] = null;
	    				hcParts["hinExp"] = null;
	    			}
	    			if (notNumber(hcParts["hinExp"])) {
	    				hcParts["hinExp"] = null;
	    			}
	    			if (notNumber(hcParts["issueDate"])) {
	    				hcParts["issueDate"] = null;
	    			}
	    			
    				$scope.setSwipeReady("done");
    				$scope.healthCardUpdateDemographics();
	    		} else {
	    			alert("Not Ontario Health Card");
	    		}
	    	}
	    }
	};
	
	$scope.healthCardUpdateDemographics = function(){
		if (demo.hcType!=hcParts["issuer"]) {
			demo.hcType = hcParts["issuer"];
			page.hcTypeColor = colorAttn;
		}
		if (demo.lastName!=hcParts["lastName"]) {
			demo.lastName = hcParts["lastName"];
			page.lastNameColor = colorAttn;
		}
		if (demo.firstName!=hcParts["firstName"]) {
			demo.firstName = hcParts["firstName"];
			page.firstNameColor = colorAttn;
		}
		if (isNumber(hcParts["hin"]) && demo.hin!=hcParts["hin"]) {
			demo.hin = hcParts["hin"];
			page.hinColor = colorAttn;
		}
		if (demo.ver!=hcParts["hinVer"]) {
			demo.ver = hcParts["hinVer"];
			page.verColor = colorAttn;
		}
		var hcSex = hcParts["sex"]==1 ? "M" : (hcParts["sex"]==2 ? "F" : null);
		if (hcSex!=null && demo.sex!=hcSex) {
			demo.sex = hcSex;
			page.sexColor = colorAttn;
		}
		var dateParts = {};
		if (hcParts["dob"]!=null) {
			dateParts["year"] = hcParts["dob"].substring(0,4);
			dateParts["month"] = hcParts["dob"].substring(4,6);
			dateParts["day"] = hcParts["dob"].substring(6);
			if (demo.dobYear!=dateParts["year"]) {
				demo.dobYear = dateParts["year"];
				page.dobYearColor = colorAttn;
			}
			if (demo.dobMonth!=dateParts["month"]) {
				demo.dobMonth = dateParts["month"];
				page.dobMonthColor = colorAttn;
			}
			if (demo.dobDay!=dateParts["day"]) {
				demo.dobDay = dateParts["day"];
				page.dobDayColor = colorAttn;
			}
		}
		if (hcParts["issueDate"]!=null) {
			dateParts["year"] = "20" + hcParts["issueDate"].substring(0,2);
			dateParts["month"] = hcParts["issueDate"].substring(2,4);
			dateParts["day"] = hcParts["issueDate"].substring(4);
			if (page.hcEffDateYear!=dateParts["year"]) {
				page.hcEffDateYear = dateParts["year"];
				page.hcEffDateYearColor = colorAttn;
			}
			if (page.hcEffDateMonth!=dateParts["month"]) {
				page.hcEffDateMonth = dateParts["month"];
				page.hcEffDateMonthColor = colorAttn;
			}
			if (page.hcEffDateDay!=dateParts["day"]) {
				page.hcEffDateDay = dateParts["day"];
				page.hcEffDateDayColor = colorAttn;
			}
		}
		if (hcParts["hinExp"]!=null) {
			dateParts["year"] = "20" + hcParts["hinExp"].substring(0,2);
			dateParts["month"] = hcParts["hinExp"].substring(2,4);
			dateParts["day"] = hcParts["hinExp"].substring(4);
			if (page.hcRenewDateYear!=dateParts["year"]) {
				page.hcRenewDateYear = dateParts["year"];
				page.hcRenewDateYearColor = colorAttn;
			}
			if (page.hcRenewDateMonth!=dateParts["month"]) {
				page.hcRenewDateMonth = dateParts["month"];
				page.hcRenewDateMonthColor = colorAttn;
			}
			if (page.hcRenewDateDay!=dateParts["day"]) {
				page.hcRenewDateDay = dateParts["day"];
				page.hcRenewDateDayColor = colorAttn;
			}
			var hinExpDate = new Date(dateParts["year"], dateParts["month"]-1, dateParts["day"]);
			if (now>hinExpDate) {
				alert("This health card has expired!");
				page.hcRenewDateYearColor = colorAttn;
				page.hcRenewDateMonthColor = colorAttn;
				page.hcRenewDateDayColor = colorAttn;
			}
		}
	}

	//HCValidation
	$scope.validateHC = function(){
		if (demo.hin==null || demo.hin=="") return;
		if (demo.ver==null) demo.ver = "";
		patientDetailStatusService.validateHC(demo.hin,demo.ver).then(function(data){
			if (data.valid==null) page.HCValidation = "n/a";
			else page.HCValidation = data.valid ? "valid" : "invalid";
		});
	}
	
	//manage hin/hinVer entries
	var hin0 = demo.hin;
	var ver0 = demo.ver;
	$scope.checkHin = function(){
		if (demo.hcType=="ON") {
			if (demo.hin.length>10) demo.hin = hin0;
			if (notNumber(demo.hin)) demo.hin = hin0;
		}
		hin0 = demo.hin;
		page.HCValidation = null;
	}
	$scope.checkHinVer = function(){
		if (demo.hcType=="ON") {
			if (demo.ver.length>2) demo.ver = ver0;
			if (!(/^[a-zA-Z()]*$/.test(demo.ver))) demo.ver = ver0;
			demo.ver = demo.ver.toUpperCase();
		}
		ver0 = demo.ver;
	}
	
	//manage date entries
	$scope.checkDate = function(id){
		if (id=="DobY") demo.dobYear = checkYear(demo.dobYear);
		else if (id=="DobM") demo.dobMonth = checkMonth(demo.dobMonth);
		else if (id=="DobD") demo.dobDay = checkDay(demo.dobDay, demo.dobMonth, demo.dobYear);
		else if (id=="HedY") page.hcEffDateYear = checkYear(page.hcEffDateYear);
		else if (id=="HedM") page.hcEffDateMonth = checkMonth(page.hcEffDateMonth);
		else if (id=="HedD") page.hcEffDateDay = checkDay(page.hcEffDateDay, page.hcEffDateMonth, page.hcEffDateYear);
		else if (id=="HrdY") page.hcRenewDateYear = checkYear(page.hcRenewDateYear);
		else if (id=="HrdM") page.hcRenewDateMonth = checkMonth(page.hcRenewDateMonth);
		else if (id=="HrdD") page.hcRenewDateDay = checkDay(page.hcRenewDateDay, page.hcRenewDateMonth, page.hcRenewDateYear);
		else if (id=="RodY") page.rosterDateYear = checkYear(page.rosterDateYear);
		else if (id=="RodM") page.rosterDateMonth = checkMonth(page.rosterDateMonth);
		else if (id=="RodD") page.rosterDateDay = checkDay(page.rosterDateDay, page.rosterDateMonth, page.rosterDateYear);
		else if (id=="RtdY") page.rosterTerminationDateYear = checkYear(page.rosterTerminationDateYear);
		else if (id=="RtdM") page.rosterTerminationDateMonth = checkMonth(page.rosterTerminationDateMonth);
		else if (id=="RtdD") page.rosterTerminationDateDay = checkDay(page.rosterTerminationDateDay, page.rosterTerminationDateMonth, page.rosterTerminationDateYear);
		else if (id=="PsdY") page.patientStatusDateYear = checkYear(page.patientStatusDateYear);
		else if (id=="PsdM") page.patientStatusDateMonth = checkMonth(page.patientStatusDateMonth);
		else if (id=="PsdD") page.patientStatusDateDay = checkDay(page.patientStatusDateDay, page.patientStatusDateMonth, page.patientStatusDateYear);
		else if (id=="JndY") page.dateJoinedYear = checkYear(page.dateJoinedYear);
		else if (id=="JndM") page.dateJoinedMonth = checkMonth(page.dateJoinedMonth);
		else if (id=="JndD") page.dateJoinedDay = checkDay(page.dateJoinedDay, page.dateJoinedMonth, page.dateJoinedYear);
		else if (id=="EddY") page.endDateYear = checkYear(page.endDateYear);
		else if (id=="EddM") page.endDateMonth = checkMonth(page.endDateMonth);
		else if (id=="EddD") page.endDateDay = checkDay(page.endDateDay, page.endDateMonth, page.endDateYear);
		else if (id=="PcaY") page.paperChartArchivedDateYear = checkYear(page.paperChartArchivedDateYear);
		else if (id=="PcaM") page.paperChartArchivedDateMonth = checkMonth(page.paperChartArchivedDateMonth);
		else if (id=="PcaD") page.paperChartArchivedDateDay = checkDay(page.paperChartArchivedDateDay, page.paperChartArchivedDateMonth, page.paperChartArchivedDateYear);
		else if (id=="OlsY") page.onWaitingListSinceYear = checkYear(page.onWaitingListSinceYear);
		else if (id=="OlsM") page.onWaitingListSinceMonth = checkMonth(page.onWaitingListSinceMonth);
		else if (id=="OlsD") page.onWaitingListSinceDay = checkDay(page.onWaitingListSinceDay, page.onWaitingListSinceMonth, page.onWaitingListSinceYear);
	}
	
	$scope.formatDate = function(id){
		if (id=="DobY" || id=="DobM" || id=="DobD") $scope.calculateAge();
		
		if (id=="DobM") demo.dobMonth = pad0(demo.dobMonth);
		else if (id=="DobD") demo.dobDay = pad0(demo.dobDay);
		else if (id=="HedM") page.hcEffDateMonth = pad0(page.hcEffDateMonth);
		else if (id=="HedD") page.hcEffDateDay = pad0(page.hcEffDateDay);
		else if (id=="HrdM") page.hcRenewDateMonth = pad0(page.hcRenewDateMonth);
		else if (id=="HrdD") page.hcRenewDateDay = pad0(page.hcRenewDateDay);
		else if (id=="RodM") page.rosterDateMonth = pad0(page.rosterDateMonth);
		else if (id=="RodD") page.rosterDateDay = pad0(page.rosterDateDay);
		else if (id=="RtdM") page.rosterTerminationDateMonth = pad0(page.rosterTerminationDateMonth);
		else if (id=="RtdD") page.rosterTerminationDateDay = pad0(page.rosterTerminationDateDay);
		else if (id=="PsdM") page.patientStatusDateMonth = pad0(page.patientStatusDateMonth);
		else if (id=="PsdD") page.patientStatusDateDay = pad0(page.patientStatusDateDay);
		else if (id=="JndM") page.dateJoinedMonth = pad0(page.dateJoinedMonth);
		else if (id=="JndD") page.dateJoinedDay = pad0(page.dateJoinedDay);
		else if (id=="EddM") page.endDateMonth = pad0(page.endDateMonth);
		else if (id=="EddD") page.endDateDay = pad0(page.endDateDay);
		else if (id=="PcaM") page.paperChartArchivedDateMonth = pad0(page.paperChartArchivedDateMonth);
		else if (id=="PcaD") page.paperChartArchivedDateDay = pad0(page.paperChartArchivedDateDay);
		else if (id=="OlsM") page.onWaitingListSinceMonth = pad0(page.onWaitingListSinceMonth);
		else if (id=="OlsD") page.onWaitingListSinceDay = pad0(page.onWaitingListSinceDay);
	}
	$scope.formatDate("DobM"); //done on page load
	$scope.formatDate("DobD"); //done on page load
	
	//check Patient Status if endDate is entered
	$scope.checkPatientStatus = function(){
		if (demo.patientStatus=="AC") {
			if (!dateEmpty(page.endDateYear, page.endDateMonth, page.endDateDay)) {
				if (dateValid(page.endDateYear, page.endDateMonth, page.endDateDay)) {
					var endDate = new Date(page.endDateYear, (page.endDateMonth-1), page.endDateDay);
					if (now > endDate) {
						alert("Patient Status cannot be Active after End Date.");
						return false;
					}
				}
			}
		}
	}
	
	//check postal code (Canada provinces only)
	var postal0 = demo.address.postal;
	$scope.checkPostal = function(){
		if (demo.address.province==null || demo.address.province=="OT" || demo.address.province.indexOf("US")==0)
			return;

		demo.address.postal = demo.address.postal.toUpperCase();
		if ($scope.invalidPostal()) {
			demo.address.postal = postal0;
		} else {
			demo.address.postal = demo.address.postal.replace(/\s/g, "");
			if (demo.address.postal.length>3) {
				demo.address.postal = demo.address.postal.substring(0,3) + " " + demo.address.postal.substring(3);
			}
		}
		postal0 = demo.address.postal;
	}
	
	$scope.isPostalComplete = function(){
		var province = demo.address.province;
		if (province!=null && province!="OT" && province.indexOf("US")!=0) {
			if (($scope.invalidPostal() || demo.address.postal.length!=7) && demo.address.postal!="") {
				alert("Invalid/Incomplete Postal Code");
				return false;
			}
		}
		return true;
	}
	
	$scope.invalidPostal = function(){
		var postal = demo.address.postal;
		if (postal!=null && postal!="") {
			postal = postal.replace(/\s/g, "");
			if (postal.length>6) return true;
			
			postal = postal.toUpperCase();
			for (var i=0; i<postal.length; i+=2) {
				var cc = postal.charCodeAt(i);
				if (cc<65 || cc>90) return true;

				if (i<postal.length-1) {
					cc = postal.charAt(i+1);
					if (notDigit(cc)) return true;
				}
			}
		}
		return false;
	}
	
	//show/hide items
	$scope.isRosterTerminated = function(){
		return (demo.rosterStatus=="TE");
	}
	$scope.showReferralDocList = function(){
		page.showReferralDocList = !page.showReferralDocList;
	}
	$scope.showAddNewRosterStatus = function(){
		page.showAddNewRosterStatus = !page.showAddNewRosterStatus;
		page.newRosterStatus = null;
	}
	$scope.showAddNewPatientStatus = function(){
		page.showAddNewPatientStatus = !page.showAddNewPatientStatus;
		page.newPatientStatus = null;
	}
	
	//fill referral doc from list
	$scope.fillReferralDoc = function(){
		if (page.referralDocObj!=null) {
			page.referralDocNo = page.referralDocObj.referralNo;
			page.referralDoc = page.referralDocObj.name;
		}
		page.showReferralDocList = false;
	}
	
	//add new Roster Status
	$scope.addNewRosterStatus = function(){
		if (page.newRosterStatus!=null && page.newRosterStatus!="") {
			demo.rosterStatusList.push({"value":page.newRosterStatus, "label":page.newRosterStatus});
			demo.rosterStatus = page.newRosterStatus;
		}
		$scope.showAddNewRosterStatus();
	}
	
	//add new Patient Status
	$scope.addNewPatientStatus = function(){
		if (page.newPatientStatus!=null && page.newPatientStatus!="") {
			demo.patientStatusList.push({"value":page.newPatientStatus, "label":page.newPatientStatus});
			demo.patientStatus = page.newPatientStatus;
		}
		$scope.showAddNewPatientStatus();
	}
	
	//check phone numbers
	var phoneNum = {};
	phoneNum["C"] = page.cellPhone;
	phoneNum["H"] = page.homePhone;
	phoneNum["W"] = page.workPhone;
	phoneNum["HX"] = page.hPhoneExt.value;
	phoneNum["WX"] = page.wPhoneExt.value;
	
	$scope.checkPhone = function(type){
		if (type=="C") {
			if (invalidPhoneNumber(page.cellPhone)) page.cellPhone = phoneNum["C"];
			else phoneNum["C"] = page.cellPhone;
		}
		else if (type=="H") {
			if (invalidPhoneNumber(page.homePhone)) page.homePhone = phoneNum["H"];
			else phoneNum["H"] = page.homePhone;
		}
		else if (type=="W") {
			if (invalidPhoneNumber(page.workPhone)) page.workPhone = phoneNum["W"];
			else phoneNum["W"] = page.workPhone;
		}
		else if (type=="HX") {
			if (notNumber(page.hPhoneExt.value)) page.hPhoneExt.value = phoneNum["HX"];
			else phoneNum["HX"] = page.hPhoneExt.value;
		}
		else if (type=="WX") {
			if (notNumber(page.wPhoneExt.value)) page.wPhoneExt.value = phoneNum["WX"];
			else phoneNum["WX"] = page.wPhoneExt.value;
		}
	}
	
	//set preferred contact phone number
	$scope.setPreferredPhone = function(){
		page.cellPhonePreferredMsg = defPhTitle;
		page.cellPhonePreferredColor = "";
		page.homePhonePreferredMsg = defPhTitle;
		page.homePhonePreferredColor = "";
		page.workPhonePreferredMsg = defPhTitle;
		page.workPhonePreferredColor = "";
		
		if (page.preferredPhone=="C") {
			page.preferredPhoneNumber = page.cellPhone;
			page.cellPhonePreferredMsg = prefPhTitle;
			page.cellPhonePreferredColor = colorAttn;
		}
		else if (page.preferredPhone=="H") {
			page.preferredPhoneNumber = page.homePhone;
			page.homePhonePreferredMsg = prefPhTitle;
			page.homePhonePreferredColor = colorAttn;
		}
		else if (page.preferredPhone=="W") {
			page.preferredPhoneNumber = page.workPhone;
			page.workPhonePreferredMsg = prefPhTitle;
			page.workPhonePreferredColor = colorAttn;
		}
	}
	
	//disable set-preferred if phone number empty
	$scope.isPhoneVoid = function(phone){
		return (phone==null || phone=="");
	}
	
	//upload photo
	$scope.launchPhoto = function(){
		var url = "../casemgmt/uploadimage.jsp?demographicNo="+demo.demographicNo;
		window.open(url, "uploadWin", "width=500, height=300");
	}
	
	//manage contacts
	$scope.manageContacts = function(){
		var discard = true;
		if (page.dataChanged>0) {
			discard = confirm("You may have unsaved data. Are you sure to leave?");
		}
		if (discard) {
			var url="../demographic/Contact.do?method=manage&demographic_no="+demo.demographicNo;
			window.open(url, "ManageContacts", "width=960, height=700");
		}
	}
	
	//print buttons
	$scope.printLabel = function(label){
		var url = null;
		if (label=="PDFLabel") url="../demographic/printDemoLabelAction.do?appointment_no=null&demographic_no="+demo.demographicNo;
		else if (label=="PDFAddress") url="../demographic/printDemoAddressLabelAction.do?demographic_no="+demo.demographicNo;
		else if (label=="PDFChart") url="../demographic/printDemoChartLabelAction.do?demographic_no="+demo.demographicNo;
		else if (label=="PrintLabel") url="../demographic/demographiclabelprintsetting.jsp?demographic_no="+demo.demographicNo;
		else if (label=="ClientLab") url="../demographic/printClientLabLabelAction.do?demographic_no="+demo.demographicNo;
		window.open(url, "Print", "width=960, height=700");
	}
	
	//integrator buttons
	$scope.integratorDo = function(func){
		var url = null;
		if (func=="ViewCommunity") url="../admin/viewIntegratedCommunity.jsp";
		else if (func=="Linking") url="../integrator/manage_linked_clients.jsp?demographicId="+demo.demographicNo;
		else if (func=="Compare") url="../demographic/DiffRemoteDemographics.jsp?demographicId="+demo.demographicNo;
		else if (func=="Update") url="../demographic/copyLinkedDemographicInfoAction.jsp?displaymode=edit&dboperation=search_detail&demographicId="+demo.demographicNo+"&demographic_no="+demo.demographicNo;
		else if (func=="SendNote") url="../demographic/followUpSelection.jsp?demographicId="+demo.demographicNo;
		window.open(url, "Integrator", "width=960, height=700");
	}
	
	//MacPHR buttons
	$scope.macPHRDo = function(func){
		var url = null;
		if (func=="Register") {
			if (!page.macPHRLoggedIn) {
				alert("Please login to MyOscar first");
				return;
			}
			url="../phr/indivo/RegisterIndivo.jsp?demographicNo="+demo.demographicNo;
		}
		else if (func=="SendMessage") {
			url="../phr/PhrMessage.do?method=createMessage&providerNo="+user.providerNo+"&demographicNo="+demo.demographicNo;
		}
		else if (func=="ViewRecord") {
			url="../demographic/viewPhrRecord.do?demographic_no="+demo.demographicNo;
		}
		else if (func=="Verification") {
			url="../phr/PHRVerification.jsp?demographic_no="+demo.demographicNo;
		}
		window.open(url, "MacPHR", "width=960, height=700");
	}
	
	//appointment buttons
	$scope.appointmentDo = function(func){
		var url = null;
		if (func=="ApptHistory") url="../demographic/demographiccontrol.jsp?displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25&orderby=appttime&demographic_no="+demo.demographicNo+"&last_name="+encodeURI(demo.lastName)+"&first_name="+encodeURI(demo.firstName);
		else if (func=="WaitingList") url="../oscarWaitingList/SetupDisplayPatientWaitingList.do?demographic_no="+demo.demographicNo;
		window.open(url, "Appointment", "width=960, height=700");
	}
	
	//billing buttons
	$scope.billingDo = function(func){
		var url = null;
		if (func=="BillingHistory") {
			if (page.billregion=="CLINICAID") {
				url="../billing.do?billRegion=CLINICAID&action=invoice_reports";
			}
			else if (page.billregion=="ON") {
				url="../billing/CA/ON/billinghistory.jsp?demographic_no="+demo.demographicNo+"&last_name="+encodeURI(demo.lastName)+"&first_name="+encodeURI(demo.firstName)+"&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10";
			}
			else {
				url="../billing/CA/BC/billpage.jsp?lastName="+encodeURI(demo.lastName)+"&firstName="+encodeURI(demo.firstName)+"&filterPatient=true&demographicNo="+demo.demographicNo;
			}
		}
		else if (func=="CreateInvoice") {
			url="../billing.do?billRegion="+page.billregion+"&billForm="+page.defaultView+"&hotclick=&appointment_no=0&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&demographic_no="+demo.demographicNo+"&providerview="+demo.providerNo+"&user_no="+user.providerNo+"&apptProvider_no=none&appointment_date="+now.getFullYear+"-"+(now.getMonth()+1)+"-"+now.getDate()+"&start_time=00:00:00&bNewForm=1&status=t";
		}
		else if (func=="FluBilling") {
			url="../billing/CA/ON/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid="+demo.demographicNo+"&creator="+user.providerNo+"&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&hin="+demo.hin+demo.ver+"&demo_sex="+demo.sex+"&demo_hctype="+demo.hcType+"&rd="+encodeURI(page.referralDoc)+"&rdohip="+page.referralDocNo+"&dob="+demo.dobYear+demo.dobMonth+demo.dobDay+"&mrp="+demo.providerNo;
		}
		else if (func=="HospitalBilling") {
			url="../billing/CA/ON/billingShortcutPg1.jsp?billRegion="+page.billregion+"&billForm="+encodeURI(page.hospitalView)+"&hotclick=&appointment_no=0&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&demographic_no="+demo.demographicNo+"&providerview="+demo.providerNo+"&user_no="+user.providerNo+"&apptProvider_no=none&appointment_date="+now.getFullYear+"-"+(now.getMonth()+1)+"-"+now.getDate()+"&start_time=00:00:00&bNewForm=1&status=t";
		}
		else if (func=="AddBatchBilling") {
			url="../billing/CA/ON/addBatchBilling.jsp?demographic_no="+demo.demographicNo+"&creator="+user.providerNo+"&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&hin="+demo.hin+demo.ver+"&dob="+demo.dobYear+demo.dobMonth+demo.dobDay;
		}
		else if (func=="AddINR") {
			url="../billing/CA/ON/inr/addINRbilling.jsp?function=demographic&functionid="+demo.demographicNo+"&creator="+user.providerNo+"&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&hin="+demo.hin+demo.ver+"&dob="+demo.dobYear+demo.dobMonth+demo.dobDay;
		}
		else if (func=="BillINR") {
			url="../billing/CA/ON/inr/reportINR.jsp?provider_no="+user.providerNo;
		}
		window.open(url, "Billing", "width=960, height=700");
	}
	
	//export demographic
	$scope.exportDemographic = function(){
		var url = "../demographic/demographicExport.jsp?demographicNo="+demo.demographicNo;
		window.open(url, "DemographicExport", "width=960, height=700");
	}
	
	
	//-----------------//
	// save operations //
	//-----------------//
	$scope.save = function(){
		//check required fields
		if (demo.lastName==null || demo.lastName=="") {
			alert("Last Name is required"); return;
		}
		else if (demo.firstName==null || demo.firstName=="") {
			alert("First Name is required"); return;
		}
		else if (demo.sex==null || demo.sex=="") {
			alert("Sex is required"); return;
		}
		else if (dateEmpty(demo.dobYear, demo.dobMonth, demo.dobDay)) {
			alert("Date of Birth is required"); return;
		}
		
		//check patient status and end date
		if ($scope.checkPatientStatus()) return;

		//check postal code complete
		if (!$scope.isPostalComplete()) return;
		
		//check dates
		demo.dateOfBirth = buildDate(demo.dobYear, demo.dobMonth, demo.dobDay);
		if (demo.dateOfBirth==null) {
			alert("Invalid Date of Birth"); return;
		}
		demo.effDate = buildDate(page.hcEffDateYear, page.hcEffDateMonth, page.hcEffDateDay);
		if (demo.effDate==null) {
			alert("Invalid Health Card Effective Date"); return;
		}
		demo.hcRenewDate = buildDate(page.hcRenewDateYear, page.hcRenewDateMonth, page.hcRenewDateDay);
		if (demo.hcRenewDate==null) {
			alert("Invalid Health Card Renew Date"); return;
		}
		demo.rosterDate = buildDate(page.rosterDateYear, page.rosterDateMonth, page.rosterDateDay);
		if (demo.rosterDate==null) {
			alert("Invalid Date Rostered"); return;
		}
		demo.rosterTerminationDate = buildDate(page.rosterTerminationDateYear, page.rosterTerminationDateMonth, page.rosterTerminationDateDay);
		if (demo.rosterTerminationDate==null) {
			alert("Invalid Roster Termination Date"); return;
		}
		demo.patientStatusDate = buildDate(page.patientStatusDateYear, page.patientStatusDateMonth, page.patientStatusDateDay);
		if (demo.patientStatusDate==null) {
			alert("Invalid Patient Status Date"); return;
		}
		demo.dateJoined = buildDate(page.dateJoinedYear, page.dateJoinedMonth, page.dateJoinedDay);
		if (demo.dateJoined==null) {
			alert("Invalid Date Joined"); return;
		}
		demo.endDate = buildDate(page.endDateYear, page.endDateMonth, page.endDateDay);
		if (demo.endDate==null) {
			alert("Invalid End Date"); return;
		}
		demo.onWaitingListSinceDate = buildDate(page.onWaitingListSinceYear, page.onWaitingListSinceMonth, page.onWaitingListSinceDay);
		if (demo.onWaitingListSinceDate==null) {
			alert("Invalid Date Of Request (Waiting List)"); return;
		}
		page.paperChartArchivedDate.value = buildDate(page.paperChartArchivedDateYear, page.paperChartArchivedDateMonth, page.paperChartArchivedDateDay);
		if (page.paperChartArchivedDate.value==null) {
			alert("Invalid Paper Chart Archive Date"); return;
		}
		
		
		//save notes
		if (page.notes!=null) {
			demo.notes = "<unotes>" + page.notes + "</unotes>";
		}
		
		//save referral doctor
		demo.familyDoctor = "<rdohip>";
		if (page.referralDocNo!=null && page.referralDocNo!="") {
			demo.familyDoctor += page.referralDocNo;
		}
		demo.familyDoctor += "</rdohip><rd>";
		if (page.referralDoc!=null && page.referralDoc!="") {
			demo.familyDoctor += page.referralDoc;
		}
		demo.familyDoctor += "</rd>";

		//save phone numbers
		page.demoCell.value = page.cellPhone;
		demo.phone = page.homePhone;
		demo.alternativePhone = page.workPhone;
		
		if (page.preferredPhone=="C") page.demoCell.value += "*";
		else if (page.preferredPhone=="H") demo.phone += "*";
		else if (page.preferredPhone=="W") demo.alternativePhone += "*";
		
		//save extras
		demo.extras = [];
		if (page.demoCell.value!=null) {
			if (page.demoCell.key==null) page.demoCell.key = "demo_cell";
			demo.extras.push(copyDemoExt(page.demoCell));
		}
		if (page.aboriginal.value!=null) {
			if (page.aboriginal.key==null) page.aboriginal.key = "aboriginal";
			demo.extras.push(copyDemoExt(page.aboriginal));
		}
		if (page.hPhoneExt.value!=null) {
			if (page.hPhoneExt.key==null) page.hPhoneExt.key = "hPhoneExt";
			demo.extras.push(copyDemoExt(page.hPhoneExt));
		}
		if (page.wPhoneExt.value!=null) {
			if (page.wPhoneExt.key==null) page.wPhoneExt.key = "wPhoneExt";
			demo.extras.push(copyDemoExt(page.wPhoneExt));
		}
		if (page.cytolNum.value!=null) {
			if (page.cytolNum.key==null) page.cytolNum.key = "cytolNum";
			demo.extras.push(copyDemoExt(page.cytolNum));
		}
		if (page.phoneComment.value!=null) {
			if (page.phoneComment.key==null) page.phoneComment.key = "phoneComment";
			demo.extras.push(copyDemoExt(page.phoneComment));
		}
		if (page.paperChartArchived.value!=null) {
			if (page.paperChartArchived.key==null) page.paperChartArchived.key = "paper_chart_archived";
			demo.extras.push(copyDemoExt(page.paperChartArchived));
		}
		if (page.paperChartArchivedDate.value!=null) {
			if (page.paperChartArchivedDate.key==null) page.paperChartArchivedDate.key = "paper_chart_archived_date";
			demo.extras.push(copyDemoExt(page.paperChartArchivedDate));
		}
		if (page.usSigned.value!=null) {
			if (page.usSigned.key==null) page.usSigned.key = "usSigned";
			demo.extras.push(copyDemoExt(page.usSigned));
		}
		if (page.privacyConsent.value!=null) {
			if (page.privacyConsent.key==null) page.privacyConsent.key = "privacyConsent";
			demo.extras.push(copyDemoExt(page.privacyConsent));
		}
		if (page.informedConsent.value!=null) {
			if (page.informedConsent.key==null) page.informedConsent.key = "informedConsent";
			demo.extras.push(copyDemoExt(page.informedConsent));
		}
		if (page.securityQuestion1.value!=null) {
			if (page.securityQuestion1.key==null) page.securityQuestion1.key = "securityQuestion1";
			demo.extras.push(copyDemoExt(page.securityQuestion1));
		}
		if (page.securityAnswer1.value!=null) {
			if (page.securityAnswer1.key==null) page.securityAnswer1.key = "securityAnswer1";
			demo.extras.push(copyDemoExt(page.securityAnswer1));
		}
		
		//save to database
		demographicService.updateDemographic(demo);
		
		//show Saving... message and refresh screen
		page.saving = true;
		location.reload();
	}
});

function copyDemoExt(ext1) {
	var ext2 = {};
	if (ext1!=null) {
		ext2.id = ext1.id;
		ext2.demographicNo = ext1.demographicNo;
		ext2.providerNo = ext1.providerNo;
		ext2.key = ext1.key;
		ext2.value = ext1.value;
		ext2.dateCreated = ext1.dateCreated;
		ext2.hidden = ext1.hidden;
	}
	return ext2;
}

function getDateParts(dateDB) {
	if (dateDB!=null && dateDB!="") {
		var date = new Date(dateDB);
		var parts = {};
		parts["year"] = date.getFullYear();
		parts["month"] = date.getMonth() + 1;
		parts["day"] = date.getDate();
		return parts;
	}
	return null;
}

function buildDate(year, month, day) {
	if (dateEmpty(year, month, day)) return "";
	if (dateValid(year, month, day)) return year + "-" + month + "-" + day;
	return null;
}

function checkYear(year) {
	for (var i=0; i<year.length; i++) {
		if (notDigit(year.charAt(i))) {
			year = year.substring(0,i) + year.substring(i+1);
		}
	}
	if (year!="") {
		year = parseInt(year).toString();
		if (year.length>4) year = year.substring(0,4);
		if (year==0) year = "";
	}
	return year;
}

function checkMonth(month) {
	for (var i=0; i<month.length; i++) {
		if (notDigit(month.charAt(i))) {
			month = month.substring(0,i) + month.substring(i+1);
		}
	}
	if (month!="") {
		if (month.length>2) month = month.substring(0,2);
		if (month>12) month = month.substring(0,1);
	}
	return month;
}

var daysOfMonth = [31,28,31,30,31,30,31,31,30,31,30,31];

function checkDay(day, month, year) {
	for (var i=0; i<day.length; i++) {
		if (notDigit(day.charAt(i))) {
			day = day.substring(0,i) + day.substring(i+1);
		}
	}
	if (day!="") {
		if (day.length>2) day = day.substring(0,2);

		if (month==null) {
			if (day>31) day = day.substring(0,1);
		}
		else if (year==null) {
			if (day>daysOfMonth[month-1]) day.substring(0,1);
		}
		else if (!dateValid(year, month, day)) {
			day = day.substring(0,1);
		}
	}
	return day;
}

function dateValid(year, month, day) {
	if (dateComplete(year, month, day)) {
		var maxDaysOfMonth = daysOfMonth[month-1];
		if (month==2) {
			if ((year%4==0 && year%100!=0) || year%400==0) {
				maxDaysOfMonth = 29;
			}
		}
		return (day>0 && day<=maxDaysOfMonth);
	}
	return dateEmpty(year, month, day);
}

function dateComplete(year, month, day) {
	return (year!=null && year!="" && month!=null && month!="" && day!=null && day!="");
}

function dateEmpty(year, month, day) {
	return ((year==null || year=="") && (month==null || month=="") && (day==null || day==""));
}

function pad0(s) {
	if (s!=null && s!="") {
		s = parseInt(s).toString();
		if (s.length<2) s = "0"+s;
		if (s==0) s = "";
	}
	return s;
}

function isNumber(s) {
	if (s!=null && s!="") {
		for (var i=0; i<s.length; i++) {
			if (notDigit(s.charAt(i))) return false;
		}
	}
	return true;
}

function notNumber(s) {
	return (!isNumber(s));
}

function notDigit(n) { //n: 1-digit
	return ("0123456789".indexOf(n)<0);
}

function invalidPhoneNumber(phone) {
	if (phone!=null && phone!="") {
		for (var i=0; i<phone.length; i++) {
			var n = phone.charAt(i);
			if (n!=" " && n!="-" && n!="(" && n!=")" && notDigit(n)) return true;
		}
	}
	return false;
}

function isPreferredPhone(phone) {
	phone = String(phone);
	if (phone!=null && phone!="") {
		if (phone.charAt(phone.length-1)=="*") return true;
	}
	return false;
}

function getPhoneNum(phone) {
	if (isPreferredPhone(phone)) {
		phone = phone.substring(0, phone.length-1);
	}
	return phone;
}

function demoContactShow(demoContact) {
	var contactShow = demoContact;
	if (demoContact.role!=null) { //only 1 entry
		var tmp = {};
		tmp.role = demoContact.role;
		tmp.sdm = demoContact.sdm;
		tmp.ec = demoContact.ec;
		tmp.category = demoContact.category;
		tmp.lastName = demoContact.lastName;
		tmp.firstName = demoContact.firstName;
		tmp.phone = demoContact.phone;
		contactShow = [tmp];
	}
	for (var i=0; i<contactShow.length; i++) {
		if (contactShow[i].sdm==true) contactShow[i].role += " /sdm";
		if (contactShow[i].ec==true) contactShow[i].role += " /ec";
		if (contactShow[i].role==null || contactShow[i].role=="") contactShow[i].role = "-";
		
		if (contactShow[i].phone==null || contactShow[i].phone=="") {
			contactShow[i].phone = "-";
		}
		else if (contactShow[i].phone.charAt(contactShow[i].phone.length-1)=="*") {
			contactShow[i].phone = contactShow[i].phone.substring(0, contactShow[i].phone.length-1);
		}
	}
	return contactShow;
}

function providerToArray(provider) {
	if (provider.providerNo!=null) { //only 1 entry
		var tmp = {};
		tmp.providerNo = provider.providerNo;
		tmp.lastName = provider.lastName;
		tmp.firstName = provider.firstName;
		tmp.name = provider.name;
		return [tmp];
	}
	return provider;
}

function statusValueToArray(statusValue) {
	if (statusValue==null) {
		statusValue = [];
	}
	else if (statusValue.value!=null) { //only 1 entry
		var tmp = {};
		tmp.value = statusValue.value;
		tmp.label = statusValue.value;
		return [tmp];
	}
	return statusValue;
}
