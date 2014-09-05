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
oscarApp.controller('DetailsCtrl', function ($scope,$http,$location,$stateParams,$state,demographicService,patientDetailStatusService,demo,user) {
	console.log("details ctrl ", $stateParams, $state, demo);
	$scope.page = {};
	$scope.page.demo = demo;
	$scope.page.extras = {};
	$scope.page.status = {};
	$scope.page.color = {};
	$scope.page.msg = {};
	
	
	patientDetailStatusService.getStatus(demo.demographicNo).then(function(data){
		$scope.page.status.macPHRLoggedIn = data.macPHRLoggedIn;
		$scope.page.status.macPHRIdsSet = data.macPHRIdsSet;
		$scope.page.status.macPHRVerificationLevel = data.macPHRVerificationLevel;
		
		$scope.page.status.integratorEnabled = data.integratorEnabled;
		$scope.page.status.integratorOffline = data.integratorOffline;
		$scope.page.status.integratorAllSynced = data.integratorAllSynced;
		
		$scope.page.status.conformanceFeaturesEnabled = data.conformanceFeaturesEnabled;
		$scope.page.status.workflowEnhance = data.workflowEnhance;
		$scope.page.status.billregion = data.billregion;
		$scope.page.status.defaultView = data.defaultView;
		$scope.page.status.hospitalView = data.hospitalView;
		
		if (data.integratorEnabled) {
			if (data.integratorOffline) {
				$scope.page.color.integratorStatus = "#ff5500";
				$scope.page.msg.integratorStatus = "NOTE: Integrator is not available at this time";
			}
			else if (!data.integratorAllSynced) {
				$scope.page.color.integratorStatus = "#ff5500";
				$scope.page.msg.integratorStatus = "NOTE: Integrated Community is not synced";
			}
		}
	});
	
	var now = new Date();
	
	//calculate age
	$scope.calculateAge = function(){
		demo.age = now.getFullYear() - demo.dobYear;
		if (now.getMonth()<demo.dobMonth-1) demo.age--;
		else if (now.getMonth()==demo.dobMonth-1 && now.getDate()<demo.dobDay) demo.age--;
	}
	$scope.calculateAge();

	//show notes
	if (demo.notes!=null) {
		var pageNotes = demo.notes.substring("<unotes>".length);
		pageNotes = pageNotes.substring(0, pageNotes.length-("</unotes>".length));
		$scope.page.notes = pageNotes;
	}
	
	//show referral doctor
	if (demo.familyDoctor!=null) {
		var referralDoc = demo.familyDoctor;
		var begin = referralDoc.indexOf("<rdohip>") + "<rdohip>".length;
		var end = referralDoc.indexOf("</rdohip>");
		if (end>begin && end>=0 && begin>=0) $scope.page.referralDocNo = referralDoc.substring(begin, end);
		
		begin = referralDoc.indexOf("<rd>") + "<rd>".length;
		end = referralDoc.indexOf("</rd>");
		if (end>begin && end>=0 && begin>=0) $scope.page.referralDoc = referralDoc.substring(begin, end);
	} 

	//initialize page.extras
	$scope.page.extras.demo_cell = {};
	$scope.page.extras.aboriginal = {};
	$scope.page.extras.hPhoneExt = {};
	$scope.page.extras.wPhoneExt = {};
	$scope.page.extras.cytolNum = {};
	$scope.page.extras.phoneComment = {};
	$scope.page.extras.paper_chart_archived = {};
	$scope.page.extras.paper_chart_archived_date = {};
	$scope.page.extras.usSigned = {};
	$scope.page.extras.privacyConsent = {};
	$scope.page.extras.informedConsent = {};
	$scope.page.extras.securityQuestion1 = {};
	$scope.page.extras.securityAnswer1 = {};
	
	//show extras
	if (demo.extras!=null) {
		if (demo.extras.key!=null) { //only 1 entry
			var tmp = copyDemoExt(demo.extras);
			demo.extras = [];
			demo.extras.push(tmp);
		}
		for (var i=0; i<demo.extras.length; i++) {
			if (demo.extras[i].key=="demo_cell") $scope.page.extras.demo_cell = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="aboriginal") $scope.page.extras.aboriginal = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="hPhoneExt") $scope.page.extras.hPhoneExt = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="wPhoneExt") $scope.page.extras.wPhoneExt = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="cytolNum") $scope.page.extras.cytolNum = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="phoneComment") $scope.page.extras.phoneComment = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="paper_chart_archived") $scope.page.extras.paper_chart_archived = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="paper_chart_archived_date") $scope.page.extras.paper_chart_archived_date = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="usSigned") $scope.page.extras.usSigned = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="privacyConsent") $scope.page.extras.privacyConsent = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="informedConsent") $scope.page.extras.informedConsent = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="securityQuestion1") $scope.page.extras.securityQuestion1 = copyDemoExt(demo.extras[i]);
			else if (demo.extras[i].key=="securityAnswer1") $scope.page.extras.securityAnswer1 = copyDemoExt(demo.extras[i]);
		}
	}
	
	//show dates
	var datePart = getDateParts(demo.endDate);
	if (datePart!=null) {
		$scope.page.endDateYear = datePart["year"];
		$scope.page.endDateMonth = pad0(datePart["month"]);
		$scope.page.endDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.effDate);
	if (datePart!=null) {
		$scope.page.hcEffDateYear = datePart["year"];
		$scope.page.hcEffDateMonth = pad0(datePart["month"]);
		$scope.page.hcEffDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.hcRenewDate);
	if (datePart!=null) {
		$scope.page.hcRenewDateYear = datePart["year"];
		$scope.page.hcRenewDateMonth = pad0(datePart["month"]);
		$scope.page.hcRenewDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.rosterDate);
	if (datePart!=null) {
		$scope.page.rosterDateYear = datePart["year"];
		$scope.page.rosterDateMonth = pad0(datePart["month"]);
		$scope.page.rosterDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.rosterTerminationDate);
	if (datePart!=null) {
		$scope.page.rosterTerminationDateYear = datePart["year"];
		$scope.page.rosterTerminationDateMonth = pad0(datePart["month"]);
		$scope.page.rosterTerminationDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.patientStatusDate);
	if (datePart!=null) {
		$scope.page.patientStatusDateYear = datePart["year"];
		$scope.page.patientStatusDateMonth = pad0(datePart["month"]);
		$scope.page.patientStatusDateDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.dateJoined);
	if (datePart!=null) {
		$scope.page.dateJoinedYear = datePart["year"];
		$scope.page.dateJoinedMonth = pad0(datePart["month"]);
		$scope.page.dateJoinedDay = pad0(datePart["day"]);
	}
	datePart = getDateParts(demo.onWaitingListSinceDate);
	if (datePart!=null) {
		$scope.page.onWaitingListSinceYear = datePart["year"];
		$scope.page.onWaitingListSinceMonth = pad0(datePart["month"]);
		$scope.page.onWaitingListSinceDay = pad0(datePart["day"]);
	}
	datePart = getDateParts($scope.page.extras.paper_chart_archived_date.value);
	if (datePart!=null) {
		$scope.page.paper_chart_archived_dateYear = datePart["year"];
		$scope.page.paper_chart_archived_dateMonth = pad0(datePart["month"]);
		$scope.page.paper_chart_archived_dateDay = pad0(datePart["day"]);
	}
	
	var colorAttn = "#ffff99";
	
	//show phone numbers with preferred check
	$scope.page.cellPhone = getPhoneNum($scope.page.extras.demo_cell.value);
	$scope.page.homePhone = getPhoneNum(demo.phone);
	$scope.page.workPhone = getPhoneNum(demo.alternativePhone);
	
	var defPhTitle = "Check to set preferred contact number";
	var prefPhTitle = "Preferred contact number";
	
	$scope.page.msg.cellPhonePreferred = defPhTitle;
	$scope.page.msg.homePhonePreferred = defPhTitle;
	$scope.page.msg.workPhonePreferred = defPhTitle;
	if (isPreferredPhone($scope.page.extras.demo_cell.value)) {
		$scope.page.preferredPhoneNumber = $scope.page.cellPhone;
		$scope.page.preferredPhone = "C";
		$scope.page.msg.cellPhonePreferred = prefPhTitle;
		$scope.page.color.cellPhonePreferred = colorAttn;
	}
	else if (isPreferredPhone(demo.phone)) {
		$scope.page.preferredPhoneNumber = $scope.page.homePhone;
		$scope.page.preferredPhone = "H";
		$scope.page.msg.homePhonePreferred = prefPhTitle;
		$scope.page.color.homePhonePreferred = colorAttn;
	}
	else if (isPreferredPhone(demo.alternativePhone)) {
		$scope.page.preferredPhoneNumber = $scope.page.workPhone;
		$scope.page.preferredPhone = "W";
		$scope.page.msg.workPhonePreferred = prefPhTitle;
		$scope.page.color.workPhonePreferred = colorAttn;
	}
	else {
		$scope.page.preferredPhoneNumber = $scope.page.homePhone;
	}
	
	//show demoContacts/demoContactPros
	if (demo.demoContacts!=null) {
		demo.demoContacts = demoContactToArray(demo.demoContacts);
	}
	if (demo.demoContactPros!=null) {
		demo.demoContactPros = demoContactToArray(demo.demoContactPros);
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
	
	//show referralDoc names
	if (demo.referralDoctors!=null) {
		if (demo.referralDoctors.id!=null) { //only 1 entry, convert to array
			var tmp = {};
			tmp.name = demo.referralDoctors.name;
			tmp.referralNo = demo.referralDoctors.referralNo;
			demo.referralDoctors = [tmp];
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
	

	//----------------------//
	// on-screen operations //
	//----------------------//
	//set ready for swipe card
	$scope.setSwipeReady = function(status){
		if (status=="off") {
			$scope.page.readyForSwipe = "";
			$scope.page.msg.swipecard = "Click for Card Swipe";
		}
		else if (status=="done") {
			$scope.page.readyForSwipe = "btn-primary";
			$scope.page.msg.swipecard = "Done Health Card Swipe";
		}
		else {
			$("#swipecard").focus();
			$scope.page.readyForSwipe = "btn-success";
			$scope.page.msg.swipecard = "Ready for Card Swipe";
			$scope.page.swipecard = "";
		}
	}
	$scope.setSwipeReady();

	//Health card verification
	var hcParts = {};
	$scope.healthCardHandler = function(keycode){
	    if (keycode==13) { //carriage-return
	    	var swipeCardData = $scope.page.swipecard;
	    	$scope.page.swipecard = "";
	    	
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
	    			
	    			if (notNumber(hcParts["hin"])) {
	    				hcParts["hin"] = null;
	    			}
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
			$scope.page.color.hcType = colorAttn;
		}
		if (demo.lastName!=hcParts["lastName"]) {
			demo.lastName = hcParts["lastName"];
			$scope.page.color.lastName = colorAttn;
		}
		if (demo.firstName!=hcParts["firstName"]) {
			demo.firstName = hcParts["firstName"];
			$scope.page.color.firstName = colorAttn;
		}
		if (hcParts["hin"]!=null && demo.hin!=hcParts["hin"]) {
			demo.hin = hcParts["hin"];
			$scope.page.color.hin = colorAttn;
		}
		if (demo.ver!=hcParts["hinVer"]) {
			demo.ver = hcParts["hinVer"];
			$scope.page.color.ver = colorAttn;
		}
		var hcSex = hcParts["sex"]==1 ? "M" : (hcParts["sex"]==2 ? "F" : null);
		if (hcSex!=null && demo.sex!=hcSex) {
			demo.sex = hcSex;
			$scope.page.color.sex = colorAttn;
		}
		var dateParts = {};
		if (hcParts["dob"]!=null) {
			dateParts["year"] = hcParts["dob"].substring(0,4);
			dateParts["month"] = hcParts["dob"].substring(4,6);
			dateParts["day"] = hcParts["dob"].substring(6);
			if (demo.dobYear!=dateParts["year"]) {
				demo.dobYear = dateParts["year"];
				$scope.page.color.dobYear = colorAttn;
			}
			if (demo.dobMonth!=dateParts["month"]) {
				demo.dobMonth = dateParts["month"];
				$scope.page.color.dobMonth = colorAttn;
			}
			if (demo.dobDay!=dateParts["day"]) {
				demo.dobDay = dateParts["day"];
				$scope.page.color.dobDay = colorAttn;
			}
		}
		if (hcParts["issueDate"]!=null) {
			dateParts["year"] = "20" + hcParts["issueDate"].substring(0,2);
			dateParts["month"] = hcParts["issueDate"].substring(2,4);
			dateParts["day"] = hcParts["issueDate"].substring(4);
			if ($scope.page.hcEffDateYear!=dateParts["year"]) {
				$scope.page.hcEffDateYear = dateParts["year"];
				$scope.page.color.hcEffDateYear = colorAttn;
			}
			if ($scope.page.hcEffDateMonth!=dateParts["month"]) {
				$scope.page.hcEffDateMonth = dateParts["month"];
				$scope.page.color.hcEffDateMonth = colorAttn;
			}
			if ($scope.page.hcEffDateDay!=dateParts["day"]) {
				$scope.page.hcEffDateDay = dateParts["day"];
				$scope.page.color.hcEffDateDay = colorAttn;
			}
		}
		if (hcParts["hinExp"]!=null) {
			dateParts["year"] = "20" + hcParts["hinExp"].substring(0,2);
			dateParts["month"] = hcParts["hinExp"].substring(2,4);
			dateParts["day"] = hcParts["hinExp"].substring(4);
			if ($scope.page.hcRenewDateYear!=dateParts["year"]) {
				$scope.page.hcRenewDateYear = dateParts["year"];
				$scope.page.color.hcRenewDateYear = colorAttn;
			}
			if ($scope.page.hcRenewDateMonth!=dateParts["month"]) {
				$scope.page.hcRenewDateMonth = dateParts["month"];
				$scope.page.color.hcRenewDateMonth = colorAttn;
			}
			if ($scope.page.hcRenewDateDay!=dateParts["day"]) {
				$scope.page.hcRenewDateDay = dateParts["day"];
				$scope.page.color.hcRenewDateDay = colorAttn;
			}
			var hinExpDate = new Date(dateParts["year"], dateParts["month"]-1, dateParts["day"]);
			if (now>hinExpDate) {
				alert("This health card has expired!");
				$scope.page.color.hcRenewDateYear = colorAttn;
				$scope.page.color.hcRenewDateMonth = colorAttn;
				$scope.page.color.hcRenewDateDay = colorAttn;
			}
		}
	}

	//HCValidation
	$scope.validateHC = function(){
		if (demo.hin==null || demo.hin=="") return;
		patientDetailStatusService.validateHC(demo.hin,demo.ver).then(function(data){
			if (data.valid==null) $scope.page.status.HCValidation = "n/a";
			else $scope.page.status.HCValidation = data.valid ? "valid" : "invalid";
		});
	}
	
	//stop input when maxLength reached
	var hin0 = demo.hin;
	var ver0 = demo.ver;
	$scope.checkHin = function() {
		if (demo.hin.length>10) demo.hin = hin0;
		if (notNumber(demo.hin)) demo.hin = hin0;
		hin0 = demo.hin;
		$scope.page.status.HCValidation = null;
	}
	$scope.checkHinVer = function() {
		if (demo.ver.length>2) demo.ver = ver0;
		if (!(/^[a-zA-Z()]*$/.test(demo.ver))) demo.ver = ver0;
		demo.ver = demo.ver.toUpperCase();
		ver0 = demo.ver;
	}
	
	//manage date entries
	$scope.checkDate = function(id) {
		if (id=="DobY") demo.dobYear = checkYear(demo.dobYear);
		else if (id=="DobM") demo.dobMonth = checkMonth(demo.dobMonth);
		else if (id=="DobD") demo.dobDay = checkDay(demo.dobDay, demo.dobMonth, demo.dobYear);
		else if (id=="HedY") $scope.page.hcEffDateYear = checkYear($scope.page.hcEffDateYear);
		else if (id=="HedM") $scope.page.hcEffDateMonth = checkMonth($scope.page.hcEffDateMonth);
		else if (id=="HedD") $scope.page.hcEffDateDay = checkDay($scope.page.hcEffDateDay, $scope.page.hcEffDateMonth, $scope.page.hcEffDateYear);
		else if (id=="HrdY") $scope.page.hcRenewDateYear = checkYear($scope.page.hcRenewDateYear);
		else if (id=="HrdM") $scope.page.hcRenewDateMonth = checkMonth($scope.page.hcRenewDateMonth);
		else if (id=="HrdD") $scope.page.hcRenewDateDay = checkDay($scope.page.hcRenewDateDay, $scope.page.hcRenewDateMonth, $scope.page.hcRenewDateYear);
		else if (id=="RodY") $scope.page.rosterDateYear = checkYear($scope.page.rosterDateYear);
		else if (id=="RodM") $scope.page.rosterDateMonth = checkMonth($scope.page.rosterDateMonth);
		else if (id=="RodD") $scope.page.rosterDateDay = checkDay($scope.page.rosterDateDay, $scope.page.rosterDateMonth, $scope.page.rosterDateYear);
		else if (id=="RtdY") $scope.page.rosterTerminationDateYear = checkYear($scope.page.rosterTerminationDateYear);
		else if (id=="RtdM") $scope.page.rosterTerminationDateMonth = checkMonth($scope.page.rosterTerminationDateMonth);
		else if (id=="RtdD") $scope.page.rosterTerminationDateDay = checkDay($scope.page.rosterTerminationDateDay, $scope.page.rosterTerminationDateMonth, $scope.page.rosterTerminationDateYear);
		else if (id=="PsdY") $scope.page.patientStatusDateYear = checkYear($scope.page.patientStatusDateYear);
		else if (id=="PsdM") $scope.page.patientStatusDateMonth = checkMonth($scope.page.patientStatusDateMonth);
		else if (id=="PsdD") $scope.page.patientStatusDateDay = checkDay($scope.page.patientStatusDateDay, $scope.page.patientStatusDateMonth, $scope.page.patientStatusDateYear);
		else if (id=="JndY") $scope.page.dateJoinedYear = checkYear($scope.page.dateJoinedYear);
		else if (id=="JndM") $scope.page.dateJoinedMonth = checkMonth($scope.page.dateJoinedMonth);
		else if (id=="JndD") $scope.page.dateJoinedDay = checkDay($scope.page.dateJoinedDay, $scope.page.dateJoinedMonth, $scope.page.dateJoinedYear);
		else if (id=="EddY") $scope.page.endDateYear = checkYear($scope.page.endDateYear);
		else if (id=="EddM") $scope.page.endDateMonth = checkMonth($scope.page.endDateMonth);
		else if (id=="EddD") $scope.page.endDateDay = checkDay($scope.page.endDateDay, $scope.page.endDateMonth, $scope.page.endDateYear);
		else if (id=="PcaY") $scope.page.paper_chart_archived_dateYear = checkYear($scope.page.paper_chart_archived_dateYear);
		else if (id=="PcaM") $scope.page.paper_chart_archived_dateMonth = checkMonth($scope.page.paper_chart_archived_dateMonth);
		else if (id=="PcaD") $scope.page.paper_chart_archived_dateDay = checkDay($scope.page.paper_chart_archived_dateDay, $scope.page.paper_chart_archived_dateMonth, $scope.page.paper_chart_archived_dateYear);
		else if (id=="OlsY") $scope.page.onWaitingListSinceYear = checkYear($scope.page.onWaitingListSinceYear);
		else if (id=="OlsM") $scope.page.onWaitingListSinceMonth = checkMonth($scope.page.onWaitingListSinceMonth);
		else if (id=="OlsD") $scope.page.onWaitingListSinceDay = checkDay($scope.page.onWaitingListSinceDay, $scope.page.onWaitingListSinceMonth, $scope.page.onWaitingListSinceYear);
	}
	
	$scope.formatDate = function(id) {
		if (id=="DobY" || id=="DobM" || id=="DobD") $scope.calculateAge();
		
		if (id=="DobM") demo.dobMonth = pad0(demo.dobMonth);
		else if (id=="DobD") demo.dobDay = pad0(demo.dobDay);
		else if (id=="HedM") $scope.page.hcEffDateMonth = pad0($scope.page.hcEffDateMonth);
		else if (id=="HedD") $scope.page.hcEffDateDay = pad0($scope.page.hcEffDateDay);
		else if (id=="HrdM") $scope.page.hcRenewDateMonth = pad0($scope.page.hcRenewDateMonth);
		else if (id=="HrdD") $scope.page.hcRenewDateDay = pad0($scope.page.hcRenewDateDay);
		else if (id=="RodM") $scope.page.rosterDateMonth = pad0($scope.page.rosterDateMonth);
		else if (id=="RodD") $scope.page.rosterDateDay = pad0($scope.page.rosterDateDay);
		else if (id=="RtdM") $scope.page.rosterTerminationDateMonth = pad0($scope.page.rosterTerminationDateMonth);
		else if (id=="RtdD") $scope.page.rosterTerminationDateDay = pad0($scope.page.rosterTerminationDateDay);
		else if (id=="PsdM") $scope.page.patientStatusDateMonth = pad0($scope.page.patientStatusDateMonth);
		else if (id=="PsdD") $scope.page.patientStatusDateDay = pad0($scope.page.patientStatusDateDay);
		else if (id=="JndM") $scope.page.dateJoinedMonth = pad0($scope.page.dateJoinedMonth);
		else if (id=="JndD") $scope.page.dateJoinedDay = pad0($scope.page.dateJoinedDay);
		else if (id=="EddM") $scope.page.endDateMonth = pad0($scope.page.endDateMonth);
		else if (id=="EddD") $scope.page.endDateDay = pad0($scope.page.endDateDay);
		else if (id=="PcaM") $scope.page.paper_chart_archived_dateMonth = pad0($scope.page.paper_chart_archived_dateMonth);
		else if (id=="PcaD") $scope.page.paper_chart_archived_dateDay = pad0($scope.page.paper_chart_archived_dateDay);
		else if (id=="OlsM") $scope.page.onWaitingListSinceMonth = pad0($scope.page.onWaitingListSinceMonth);
		else if (id=="OlsD") $scope.page.onWaitingListSinceDay = pad0($scope.page.onWaitingListSinceDay);
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
		$scope.page.referralDocList = !$scope.page.referralDocList;
	}
	
	//fill referral doc from list
	$scope.fillReferralDoc = function(){
		if ($scope.page.referralDocObj!=null) {
			$scope.page.referralDocNo = $scope.page.referralDocObj.referralNo;
			$scope.page.referralDoc = $scope.page.referralDocObj.name;
		}
		$scope.page.referralDocList = false;
	}
	
	//check phone numbers
	var phoneNum = {};
	phoneNum["C"] = $scope.page.cellPhone;
	phoneNum["H"] = $scope.page.homePhone;
	phoneNum["W"] = $scope.page.workPhone;
	phoneNum["HX"] = $scope.page.extras.hPhoneExt.value;
	phoneNum["WX"] = $scope.page.extras.wPhoneExt.value;
	
	$scope.checkPhone = function(type){
		if (type=="C") {
			if (invalidPhoneNumber($scope.page.cellPhone)) $scope.page.cellPhone = phoneNum["C"];
			else phoneNum["C"] = $scope.page.cellPhone;
		}
		else if (type=="H") {
			if (invalidPhoneNumber($scope.page.homePhone)) $scope.page.homePhone = phoneNum["H"];
			else phoneNum["H"] = $scope.page.homePhone;
		}
		else if (type=="W") {
			if (invalidPhoneNumber($scope.page.workPhone)) $scope.page.workPhone = phoneNum["W"];
			else phoneNum["W"] = $scope.page.workPhone;
		}
		else if (type=="HX") {
			if (notNumber($scope.page.extras.hPhoneExt.value)) $scope.page.extras.hPhoneExt.value = phoneNum["HX"];
			else phoneNum["HX"] = $scope.page.extras.hPhoneExt.value;
		}
		else if (type=="WX") {
			if (notNumber($scope.page.extras.wPhoneExt.value)) $scope.page.extras.wPhoneExt.value = phoneNum["WX"];
			else phoneNum["WX"] = $scope.page.extras.wPhoneExt.value;
		}
	}
	
	//set preferred contact phone number
	$scope.setPreferredPhone = function(){
		$scope.page.msg.cellPhonePreferred = defPhTitle;
		$scope.page.color.cellPhonePreferred = "";
		$scope.page.msg.homePhonePreferred = defPhTitle;
		$scope.page.color.homePhonePreferred = "";
		$scope.page.msg.workPhonePreferred = defPhTitle;
		$scope.page.color.workPhonePreferred = "";
		
		if ($scope.page.preferredPhone=="C") {
			$scope.page.preferredPhoneNumber = $scope.page.cellPhone;
			$scope.page.msg.cellPhonePreferred = prefPhTitle;
			$scope.page.color.cellPhonePreferred = colorAttn;
		}
		else if ($scope.page.preferredPhone=="H") {
			$scope.page.preferredPhoneNumber = $scope.page.homePhone;
			$scope.page.msg.homePhonePreferred = prefPhTitle;
			$scope.page.color.homePhonePreferred = colorAttn;
		}
		else if ($scope.page.preferredPhone=="W") {
			$scope.page.preferredPhoneNumber = $scope.page.workPhone;
			$scope.page.msg.workPhonePreferred = prefPhTitle;
			$scope.page.color.workPhonePreferred = colorAttn;
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
		var url="../demographic/Contact.do?method=manage&demographic_no="+demo.demographicNo;
		window.open(url, "ManageContacts", "width=960, height=700");
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
			if (!$scope.page.status.macPHRLoggedIn) {
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
			$scope.page.billingHistoryLabel = "Invoice List";
			if ($scope.page.status.billregion=="CLINICAID") {
				url="../billing.do?billRegion=CLINICAID&action=invoice_reports";
			}
			else if ($scope.page.status.billregion=="ON") {
				$scope.page.billingHistoryLabel = "Billing History";
				url="../billing/CA/ON/billinghistory.jsp?demographic_no="+demo.demographicNo+"&last_name="+encodeURI(demo.lastName)+"&first_name="+encodeURI(demo.firstName)+"&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10";
			}
			else {
				url="../billing/CA/BC/billStatus.jsp?lastName="+encodeURI(demo.lastName)+"&firstName="+encodeURI(demo.firstName)+"&filterPatient=true&demographicNo="+demo.demographicNo;
			}
		}
		else if (func=="CreateInvoice") {
			url="../billing.do?billRegion="+$scope.page.status.billregion+"&billForm="+$scope.page.status.defaultView+"&hotclick=&appointment_no=0&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&demographic_no="+demo.demographicNo+"&providerview="+demo.providerNo+"&user_no="+user.providerNo+"&apptProvider_no=none&appointment_date="+now.getFullYear+"-"+(now.getMonth()+1)+"-"+now.getDate()+"&start_time=00:00:00&bNewForm=1&status=t";
		}
		else if (func=="FluBilling") {
			url="../billing/CA/ON/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid="+demo.demographicNo+"&creator="+user.providerNo+"&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&hin="+demo.hin+demo.ver+"&demo_sex="+demo.sex+"&demo_hctype="+demo.hcType+"&rd="+encodeURI($scope.page.referralDoc)+"&rdohip="+$scope.page.referralDocNo+"&dob="+demo.dobYear+demo.dobMonth+demo.dobDay+"&mrp="+demo.providerNo;
		}
		else if (func=="HospitalBilling") {
			url="../billing/CA/ON/billingShortcutPg1.jsp?billRegion="+$scope.page.status.billregion+"&billForm="+encodeURI($scope.page.status.hospitalView)+"&hotclick=&appointment_no=0&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&demographic_no="+demo.demographicNo+"&providerview="+demo.providerNo+"&user_no="+user.providerNo+"&apptProvider_no=none&appointment_date="+now.getFullYear+"-"+(now.getMonth()+1)+"-"+now.getDate()+"&start_time=00:00:00&bNewForm=1&status=t";
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

		//check postal code complete
		if (!$scope.isPostalComplete()) return;
		
		//check dates
		demo.dateOfBirth = buildDate(demo.dobYear, demo.dobMonth, demo.dobDay);
		if (demo.dateOfBirth==null) {
			alert("Invalid Date of Birth"); return;
		}
		demo.effDate = buildDate($scope.page.hcEffDateYear, $scope.page.hcEffDateMonth, $scope.page.hcEffDateDay);
		if (demo.effDate==null) {
			alert("Invalid Health Card Effective Date"); return;
		}
		demo.hcRenewDate = buildDate($scope.page.hcRenewDateYear, $scope.page.hcRenewDateMonth, $scope.page.hcRenewDateDay);
		if (demo.hcRenewDate==null) {
			alert("Invalid Health Card Renew Date"); return;
		}
		demo.rosterDate = buildDate($scope.page.rosterDateYear, $scope.page.rosterDateMonth, $scope.page.rosterDateDay);
		if (demo.rosterDate==null) {
			alert("Invalid Date Rostered"); return;
		}
		demo.rosterTerminationDate = buildDate($scope.page.rosterTerminationDateYear, $scope.page.rosterTerminationDateMonth, $scope.page.rosterTerminationDateDay);
		if (demo.rosterTerminationDate==null) {
			alert("Invalid Roster Termination Date"); return;
		}
		demo.patientStatusDate = buildDate($scope.page.patientStatusDateYear, $scope.page.patientStatusDateMonth, $scope.page.patientStatusDateDay);
		if (demo.patientStatusDate==null) {
			alert("Invalid Patient Status Date"); return;
		}
		demo.dateJoined = buildDate($scope.page.dateJoinedYear, $scope.page.dateJoinedMonth, $scope.page.dateJoinedDay);
		if (demo.dateJoined==null) {
			alert("Invalid Date Joined"); return;
		}
		demo.endDate = buildDate($scope.page.endDateYear, $scope.page.endDateMonth, $scope.page.endDateDay);
		if (demo.endDate==null) {
			alert("Invalid End Date"); return;
		}
		demo.onWaitingListSinceDate = buildDate($scope.page.onWaitingListSinceYear, $scope.page.onWaitingListSinceMonth, $scope.page.onWaitingListSinceDay);
		if (demo.onWaitingListSinceDate==null) {
			alert("Invalid Date Of Request (Waiting List)"); return;
		}
		$scope.page.extras.paper_chart_archived_date.value = buildDate($scope.page.paper_chart_archived_dateYear, $scope.page.paper_chart_archived_dateMonth, $scope.page.paper_chart_archived_dateDay);
		if ($scope.page.extras.paper_chart_archived_date.value==null) {
			alert("Invalid Paper Chart Archive Date"); return;
		}
		
		
		//save notes
		if ($scope.page.notes!=null) {
			demo.notes = "<unotes>" + $scope.page.notes + "</unotes>";
		}
		
		//save referral doctor
		demo.familyDoctor = "<rdohip>";
		if ($scope.page.referralDocNo!=null && $scope.page.referralDocNo!="") {
			demo.familyDoctor += $scope.page.referralDocNo;
		}
		demo.familyDoctor += "</rdohip><rd>";
		if ($scope.page.referralDoc!=null && $scope.page.referralDoc!="") {
			demo.familyDoctor += $scope.page.referralDoc;
		}
		demo.familyDoctor += "</rd>";

		//save phone numbers
		$scope.page.extras.demo_cell.value = $scope.page.cellPhone;
		demo.phone = $scope.page.homePhone;
		demo.alternativePhone = $scope.page.workPhone;
		
		if ($scope.page.preferredPhone=="C") $scope.page.extras.demo_cell.value += "*";
		else if ($scope.page.preferredPhone=="H") demo.phone += "*";
		else if ($scope.page.preferredPhone=="W") demo.alternativePhone += "*";
		
		//save extras
		demo.extras = [];
		if ($scope.page.extras.demo_cell.value!=null) {
			if ($scope.page.extras.demo_cell.key==null) $scope.page.extras.demo_cell.key = "demo_cell";
			demo.extras.push(copyDemoExt($scope.page.extras.demo_cell));
		}
		if ($scope.page.extras.aboriginal.value!=null) {
			if ($scope.page.extras.aboriginal.key==null) $scope.page.extras.aboriginal.key = "aboriginal";
			demo.extras.push(copyDemoExt($scope.page.extras.aboriginal));
		}
		if ($scope.page.extras.hPhoneExt.value!=null) {
			if ($scope.page.extras.hPhoneExt.key==null) $scope.page.extras.hPhoneExt.key = "hPhoneExt";
			demo.extras.push(copyDemoExt($scope.page.extras.hPhoneExt));
		}
		if ($scope.page.extras.wPhoneExt.value!=null) {
			if ($scope.page.extras.wPhoneExt.key==null) $scope.page.extras.wPhoneExt.key = "wPhoneExt";
			demo.extras.push(copyDemoExt($scope.page.extras.wPhoneExt));
		}
		if ($scope.page.extras.cytolNum.value!=null) {
			if ($scope.page.extras.cytolNum.key==null) $scope.page.extras.cytolNum.key = "cytolNum";
			demo.extras.push(copyDemoExt($scope.page.extras.cytolNum));
		}
		if ($scope.page.extras.phoneComment.value!=null) {
			if ($scope.page.extras.phoneComment.key==null) $scope.page.extras.phoneComment.key = "phoneComment";
			demo.extras.push(copyDemoExt($scope.page.extras.phoneComment));
		}
		if ($scope.page.extras.paper_chart_archived.value!=null) {
			if ($scope.page.extras.paper_chart_archived.key==null) $scope.page.extras.paper_chart_archived.key = "paper_chart_archived";
			demo.extras.push(copyDemoExt($scope.page.extras.paper_chart_archived));
		}
		if ($scope.page.extras.paper_chart_archived_date.value!=null) {
			if ($scope.page.extras.paper_chart_archived_date.key==null) $scope.page.extras.paper_chart_archived_date.key = "paper_chart_archived_date";
			demo.extras.push(copyDemoExt($scope.page.extras.paper_chart_archived_date));
		}
		if ($scope.page.extras.usSigned.value!=null) {
			if ($scope.page.extras.usSigned.key==null) $scope.page.extras.usSigned.key = "usSigned";
			demo.extras.push(copyDemoExt($scope.page.extras.usSigned));
		}
		if ($scope.page.extras.privacyConsent.value!=null) {
			if ($scope.page.extras.privacyConsent.key==null) $scope.page.extras.privacyConsent.key = "privacyConsent";
			demo.extras.push(copyDemoExt($scope.page.extras.privacyConsent));
		}
		if ($scope.page.extras.informedConsent.value!=null) {
			if ($scope.page.extras.informedConsent.key==null) $scope.page.extras.informedConsent.key = "informedConsent";
			demo.extras.push(copyDemoExt($scope.page.extras.informedConsent));
		}
		if ($scope.page.extras.securityQuestion1.value!=null) {
			if ($scope.page.extras.securityQuestion1.key==null) $scope.page.extras.securityQuestion1.key = "securityQuestion1";
			demo.extras.push(copyDemoExt($scope.page.extras.securityQuestion1));
		}
		if ($scope.page.extras.securityAnswer1.value!=null) {
			if ($scope.page.extras.securityAnswer1.key==null) $scope.page.extras.securityAnswer1.key = "securityAnswer1";
			demo.extras.push(copyDemoExt($scope.page.extras.securityAnswer1));
		}
		
		//save to database
		demographicService.updateDemographic(demo);
		
		//show Saving... message and refresh screen
		$scope.page.saving = true;
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
	if (s!="") {
		s = parseInt(s).toString();
		if (s.length<2) s = "0"+s;
		if (s==0) s = "";
	}
	return s;
}

function notNumber(s) {
	if (s!=null && s!="") {
		for (var i=0; i<s.length; i++) {
			if (notDigit(s.charAt(i))) return true;
		}
	}
	return false;
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

function demoContactToArray(demoContact) {
	if (demoContact.role!=null) { //only 1 entry
		var tmp = {};
		tmp.role = demoContact.role;
		tmp.sdm = demoContact.sdm;
		tmp.ec = demoContact.ec;
		tmp.category = demoContact.category;
		tmp.lastName = demoContact.lastName;
		tmp.firstName = demoContact.firstName;
		tmp.phone = demoContact.phone;
		return [tmp];
	}
	return demoContact;
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
