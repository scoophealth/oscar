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
oscarApp.controller('DetailsCtrl', function ($scope,$http,$location,$stateParams,demographicService,demo,$state) {
	console.log("details ctrl ",$stateParams,$state,demo);
	
	$scope.page = {};
	$scope.page.demo = demo;
	$scope.page.contact = {};
	$scope.page.extras = {};
	
	//calculate age
	var now = new Date();
	demo.age = now.getFullYear() - demo.dobYear;
	if (now.getMonth()<demo.dobMonth) {
		demo.age--;
	}
	else if (now.getMonth()==demo.dobMonth && now.getDate()<demo.dobDay) {
		demo.age--;
	}

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
		}
	}
	
	//show dates
	var datePart = getDateParts(demo.endDate);
	if (datePart!=null) {
		$scope.page.endDateYear = datePart["year"];
		$scope.page.endDateMonth = datePart["month"];
		$scope.page.endDateDay = datePart["day"];
	}
	datePart = getDateParts(demo.effDate);
	if (datePart!=null) {
		$scope.page.hcEffDateYear = datePart["year"];
		$scope.page.hcEffDateMonth = datePart["month"];
		$scope.page.hcEffDateDay = datePart["day"];
	}
	datePart = getDateParts(demo.hcRenewDate);
	if (datePart!=null) {
		$scope.page.hcRenewDateYear = datePart["year"];
		$scope.page.hcRenewDateMonth = datePart["month"];
		$scope.page.hcRenewDateDay = datePart["day"];
	}
	datePart = getDateParts(demo.rosterDate);
	if (datePart!=null) {
		$scope.page.rosterDateYear = datePart["year"];
		$scope.page.rosterDateMonth = datePart["month"];
		$scope.page.rosterDateDay = datePart["day"];
	}
	datePart = getDateParts(demo.rosterTerminationDate);
	if (datePart!=null) {
		$scope.page.rosterTerminationDateYear = datePart["year"];
		$scope.page.rosterTerminationDateMonth = datePart["month"];
		$scope.page.rosterTerminationDateDay = datePart["day"];
	}
	datePart = getDateParts(demo.patientStatusDate);
	if (datePart!=null) {
		$scope.page.patientStatusDateYear = datePart["year"];
		$scope.page.patientStatusDateMonth = datePart["month"];
		$scope.page.patientStatusDateDay = datePart["day"];
	}
	datePart = getDateParts(demo.dateJoined);
	if (datePart!=null) {
		$scope.page.dateJoinedYear = datePart["year"];
		$scope.page.dateJoinedMonth = datePart["month"];
		$scope.page.dateJoinedDay = datePart["day"];
	}
	datePart = getDateParts(demo.onWaitingListSinceDate);
	if (datePart!=null) {
		$scope.page.onWaitingListSinceYear = datePart["year"];
		$scope.page.onWaitingListSinceMonth = datePart["month"];
		$scope.page.onWaitingListSinceDay = datePart["day"];
	}
	datePart = getDateParts($scope.page.extras.paper_chart_archived_date.value);
	if (datePart!=null) {
		$scope.page.paper_chart_archived_dateYear = datePart["year"];
		$scope.page.paper_chart_archived_dateMonth = datePart["month"];
		$scope.page.paper_chart_archived_dateDay = datePart["day"];
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
	
	//show waitingListNames
	if (demo.waitingListNames!=null) {
		if (demo.waitingListNames.id!=null) { //only 1 entry
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


	//-------------------//
	// screen operations //
	//-------------------//
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
	$scope.formatDate = function(id, part) {
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
	
	//show/hide Roster Termination Date & Reason
	$scope.isRosterTerminated = function(){
		return (demo.rosterStatus=="TE");
	}
	
	//upload photo
	$scope.launchPhoto = function(){
		var url = "../casemgmt/uploadimage.jsp?demographicNo="+demo.demographicNo;
		window.open(url, "uploadWin", "width=500, height=300");
	}
	
	//reset Changes Saved flag
	$scope.resetPageSaved = function(){
		$scope.page.saved = false;
	}

	
	//-----------------//
	// save operations //
	//-----------------//
	$scope.save = function(){
		//save dates
		if (!saveDate("Date of Birth", demo.dateOfBirth, demo.dobYear, demo.dobMonth, demo.dobDay)) return;
		if (!saveDate("Health Card Effective Date", demo.effDate, $scope.page.hcEffDateYear, $scope.page.hcEffDateMonth, $scope.page.hcEffDateDay)) return;
		if (!saveDate("Health Card Renew Date", demo.hcRenewDate, $scope.page.hcRenewDateYear, $scope.page.hcRenewDateMonth, $scope.page.hcRenewDateDay)) return;
		if (!saveDate("Date Rostered", demo.rosterDate, $scope.page.rosterDateYear, $scope.page.rosterDateMonth, $scope.page.rosterDateDay)) return;
		if (!saveDate("Roster Termination Date", demo.rosterTerminationDate, $scope.page.rosterTerminationDateYear, $scope.page.rosterTerminationDateMonth, $scope.page.rosterTerminationDateDay)) return;
		if (!saveDate("Patient Status Date", demo.patientStatusDate, $scope.page.patientStatusDateYear, $scope.page.patientStatusDateMonth, $scope.page.patientStatusDateDay)) return;
		if (!saveDate("Date Joined", demo.dateJoined, $scope.page.dateJoinedYear, $scope.page.dateJoinedMonth, $scope.page.dateJoinedDay)) return;
		if (!saveDate("End Date", demo.endDate, $scope.page.endDateYear, $scope.page.endDateMonth, $scope.page.endDateDay)) return;
		if (!saveDate("Date Of Request (Waiting List)", demo.onWaitingListSinceDate, $scope.page.onWaitingListSinceYear, $scope.page.onWaitingListSinceMonth, $scope.page.onWaitingListSinceDay)) return;
		if (!saveDate("Paper Chart Archive Date", $scope.page.extras.paper_chart_archived_date.value, $scope.page.paper_chart_archived_dateYear, $scope.page.paper_chart_archived_dateMonth, $scope.page.paper_chart_archived_dateDay)) return;
		
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
		
		//save extras
		demo.extras = [];
		if ($scope.page.extras.demo_cell.value!=null && $scope.page.extras.demo_cell.value!="") {
			if ($scope.page.extras.demo_cell.key==null) $scope.page.extras.demo_cell.key = "demo_cell";
			demo.extras.push(copyDemoExt($scope.page.extras.demo_cell));
		}
		if ($scope.page.extras.aboriginal.value!=null && $scope.page.extras.aboriginal.value!="") {
			if ($scope.page.extras.aboriginal.key==null) $scope.page.extras.aboriginal.key = "aboriginal";
			demo.extras.push(copyDemoExt($scope.page.extras.aboriginal));
		}
		if ($scope.page.extras.hPhoneExt.value!=null && $scope.page.extras.hPhoneExt.value!="") {
			if ($scope.page.extras.hPhoneExt.key==null) $scope.page.extras.hPhoneExt.key = "hPhoneExt";
			demo.extras.push(copyDemoExt($scope.page.extras.hPhoneExt));
		}
		if ($scope.page.extras.wPhoneExt.value!=null && $scope.page.extras.wPhoneExt.value!="") {
			if ($scope.page.extras.wPhoneExt.key==null) $scope.page.extras.wPhoneExt.key = "wPhoneExt";
			demo.extras.push(copyDemoExt($scope.page.extras.wPhoneExt));
		}
		if ($scope.page.extras.cytolNum.value!=null && $scope.page.extras.cytolNum.value!="") {
			if ($scope.page.extras.cytolNum.key==null) $scope.page.extras.cytolNum.key = "cytolNum";
			demo.extras.push(copyDemoExt($scope.page.extras.cytolNum));
		}
		if ($scope.page.extras.phoneComment.value!=null && $scope.page.extras.phoneComment.value!="") {
			if ($scope.page.extras.phoneComment.key==null) $scope.page.extras.phoneComment.key = "phoneComment";
			demo.extras.push(copyDemoExt($scope.page.extras.phoneComment));
		}
		if ($scope.page.extras.paper_chart_archived.value!=null && $scope.page.extras.paper_chart_archived.value!="") {
			if ($scope.page.extras.paper_chart_archived.key==null) $scope.page.extras.paper_chart_archived.key = "paper_chart_archived";
			demo.extras.push(copyDemoExt($scope.page.extras.paper_chart_archived));
		}
		if ($scope.page.extras.paper_chart_archived_date.value!=null && $scope.page.extras.paper_chart_archived_date.value!="") {
			if ($scope.page.extras.paper_chart_archived_date.key==null) $scope.page.extras.paper_chart_archived_date.key = "paper_chart_archived_date";
			demo.extras.push(copyDemoExt($scope.page.extras.paper_chart_archived_date));
		}
		if ($scope.page.extras.usSigned.value!=null && $scope.page.extras.usSigned.value!="") {
			if ($scope.page.extras.usSigned.key==null) $scope.page.extras.usSigned.key = "usSigned";
			demo.extras.push(copyDemoExt($scope.page.extras.usSigned));
		}
		if ($scope.page.extras.privacyConsent.value!=null && $scope.page.extras.privacyConsent.value!="") {
			if ($scope.page.extras.privacyConsent.key==null) $scope.page.extras.privacyConsent.key = "privacyConsent";
			demo.extras.push(copyDemoExt($scope.page.extras.privacyConsent));
		}
		if ($scope.page.extras.informedConsent.value!=null && $scope.page.extras.informedConsent.value!="") {
			if ($scope.page.extras.informedConsent.key==null) $scope.page.extras.informedConsent.key = "informedConsent";
			demo.extras.push(copyDemoExt($scope.page.extras.informedConsent));
		}
		
		demographicService.updateDemographic(demo);
		
		//show Changes Saved flag
		$scope.page.saved = true;
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

function saveDate(label, dateDB, year, month, day) {
	if (dateValid(year, month, day)) {
		dateDB = year + "-" + month + "-" + day;
		return true;
	}
	alert("Invalid " + label);
	return false;
}

function checkYear(year) {
	for (var i=0; i<year.length; i++) {
		if (notDigit(year.charAt(i))) {
			year = year.substring(0,i) + year.substring(i+1);
		}
	}
	if (year!="") {
		year = parseInt(year).toString();
		if (year.length>4) {
			year = year.substring(0,4);
		}
		if (year==0) {
			year = "";
		}
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
		if (month.length>2) {
			month = month.substring(0,2);
		}
		if (month>12) {
			month = month.substring(0,1);
		}
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
		if (day.length>2) {
			day = day.substring(0,2);
		}

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
	if (year!=null && year!="" && month!=null && month!="" && day!=null && day!="") {
		var maxDaysOfMonth = daysOfMonth[month-1];
		if (month==2) {
			if ((year%4==0 && year%100!=0) || year%400==0) {
				maxDaysOfMonth = 29;
			}
		}
		return (day>0 && day<=maxDaysOfMonth);
	}
	if ((year==null || year=="") && (month==null || month=="") && (day==null || day=="")) {
		return true;
	}
	return false;
}

function notDigit(n) { //n: 1-digit
	return ("0123456789".indexOf(n)<0);
}

function pad0(s) {
	if (s!="") {
		s = parseInt(s).toString();
		if (s.length<2) {
			s = "0"+s; 
		}
		if (s==0) {
			s = "";
		}
	}
	return s;
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