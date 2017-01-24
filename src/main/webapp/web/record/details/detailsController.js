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
oscarApp.controller('DetailsCtrl', function ($scope,$http,$location,$stateParams,$state,$modal, demographicService,patientDetailStatusService,securityService,staticDataService,demo,user) {
	console.log("details ctrl ", $stateParams, $state, demo);
	
	var page = {};
	$scope.page = page;
	$scope.page.demo = demo;

	//get access rights
	securityService.hasRight("_demographic", "r", demo.demographicNo).then(function(data){
		page.canRead = data;
	});
	securityService.hasRight("_demographic", "u", demo.demographicNo).then(function(data){
		page.cannotChange = !data;
	});
	
	//disable click and keypress if user only has read-access
	$scope.checkAction = function(event){
		if (page.cannotChange) {
			event.preventDefault();
			event.stopPropagation();
			$scope.setSwipeReady();
		}
	}
	
	//get static lists to be selected
	page.genders = staticDataService.getGenders();
	page.titles = staticDataService.getTitles();
	page.provinces = staticDataService.getProvinces();
	page.countries = staticDataService.getCountries();
	page.engFre = staticDataService.getEngFre();
	page.spokenlangs = staticDataService.getSpokenLanguages();
	page.rosterTermReasons = staticDataService.getRosterTerminationReasons();
	page.securityQuestions = staticDataService.getSecurityQuestions();
	page.rxInteractionLevels = staticDataService.getRxInteractionLevels();
	page.hasPrimaryCarePhysician = staticDataService.getYesNoNA();
	page.employmentStatus = staticDataService.getEmploymentStatuses();
	
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
		page.showPrimaryCarePhysicianCheck = data.showPrimaryCarePhysicianCheck;
		page.showEmploymentStatus = data.showEmploymentStatus;
		
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
		demo.scrNotes = demo.notes;
		if (/^<unotes>[\s\S]*/.test(demo.scrNotes)) demo.scrNotes = demo.scrNotes.substring("<unotes>".length);
		if (/[\s\S]*<\/unotes>$/.test(demo.scrNotes)) demo.scrNotes = demo.scrNotes.substring(0, demo.scrNotes.lastIndexOf("</unotes>"));
	}
	
	//show referral doctor
	if (demo.familyDoctor!=null) {
		var referralDoc = demo.familyDoctor;
		var begin = referralDoc.indexOf("<rdohip>") + "<rdohip>".length;
		var end = referralDoc.indexOf("</rdohip>");
		if (end>begin && end>=0 && begin>=0) demo.scrReferralDocNo = referralDoc.substring(begin, end);
		
		begin = referralDoc.indexOf("<rd>") + "<rd>".length;
		end = referralDoc.indexOf("</rd>");
		if (end>begin && end>=0 && begin>=0) demo.scrReferralDoc = referralDoc.substring(begin, end);
	}
	
	//show extras
	demo.hasPrimaryCarePhysician = "N/A";
	demo.employmentStatus = "N/A";
	
	var posExtras = {};
	if (demo.extras!=null) {
		demo.extras = toArray(demo.extras);
		for (var i=0; i<demo.extras.length; i++) {
			if (demo.extras[i].key=="demo_cell") demo.scrDemoCell = demo.extras[i].value;
			else if (demo.extras[i].key=="aboriginal") demo.scrAboriginal = demo.extras[i].value;
			else if (demo.extras[i].key=="hPhoneExt") demo.scrHPhoneExt = demo.extras[i].value;
			else if (demo.extras[i].key=="wPhoneExt") demo.scrWPhoneExt = demo.extras[i].value;
			else if (demo.extras[i].key=="cytolNum") demo.scrCytolNum = demo.extras[i].value;
			else if (demo.extras[i].key=="phoneComment") demo.scrPhoneComment = demo.extras[i].value;
			else if (demo.extras[i].key=="paper_chart_archived") demo.scrPaperChartArchived = demo.extras[i].value;
			else if (demo.extras[i].key=="paper_chart_archived_date") demo.scrPaperChartArchivedDate = demo.extras[i].value;
			else if (demo.extras[i].key=="usSigned") demo.scrUsSigned = demo.extras[i].value;
			else if (demo.extras[i].key=="privacyConsent") demo.scrPrivacyConsent = demo.extras[i].value;
			else if (demo.extras[i].key=="informedConsent") demo.scrInformedConsent = demo.extras[i].value;
			else if (demo.extras[i].key=="securityQuestion1") demo.scrSecurityQuestion1 = demo.extras[i].value;
			else if (demo.extras[i].key=="securityAnswer1") demo.scrSecurityAnswer1 = demo.extras[i].value;
			else if (demo.extras[i].key=="rxInteractionWarningLevel") demo.scrRxInteractionLevel = demo.extras[i].value;
			else if (demo.extras[i].key=="HasPrimaryCarePhysician") demo.hasPrimaryCarePhysician = demo.extras[i].value;
			else if (demo.extras[i].key=="EmploymentStatus") demo.employmentStatus = demo.extras[i].value;
			else if (demo.extras[i].key=="statusNum") demo.scrStatusNum = demo.extras[i].value;
			else if (demo.extras[i].key=="fNationCom") demo.scrFNationCom = demo.extras[i].value; 
			else if (demo.extras[i].key=="fNationFamilyNumber") demo.scrFNationFamilyNumber = demo.extras[i].value;
			else if (demo.extras[i].key=="fNationFamilyPosition") demo.scrFNationFamilyPosition = demo.extras[i].value;
			else if (demo.extras[i].key=="ethnicity") demo.scrEthnicity = demo.extras[i].value;

			//record array position of extras by keys - to be used on saving
			posExtras[demo.extras[i].key] = i;
		}
	}
	
	var colorAttn = "#ffff99";
	
	//show phone numbers with preferred check
	demo.scrCellPhone = getPhoneNum(demo.scrDemoCell);
	demo.scrHomePhone = getPhoneNum(demo.phone);
	demo.scrWorkPhone = getPhoneNum(demo.alternativePhone);
	
	var defPhTitle = "Check to set preferred contact number";
	var prefPhTitle = "Preferred contact number";
	
	page.cellPhonePreferredMsg = defPhTitle;
	page.homePhonePreferredMsg = defPhTitle;
	page.workPhonePreferredMsg = defPhTitle;
	if (isPreferredPhone(demo.scrDemoCell)) {
		demo.scrPreferredPhone = "C";
		page.preferredPhoneNumber = demo.scrCellPhone;
		page.cellPhonePreferredMsg = prefPhTitle;
		page.cellPhonePreferredColor = colorAttn;
	}
	else if (isPreferredPhone(demo.phone)) {
		demo.scrPreferredPhone = "H";
		page.preferredPhoneNumber = demo.scrHomePhone;
		page.homePhonePreferredMsg = prefPhTitle;
		page.homePhonePreferredColor = colorAttn;
	}
	else if (isPreferredPhone(demo.alternativePhone)) {
		demo.scrPreferredPhone = "W";
		page.preferredPhoneNumber = demo.scrWorkPhone;
		page.workPhonePreferredMsg = prefPhTitle;
		page.workPhonePreferredColor = colorAttn;
	}
	else {
		page.preferredPhoneNumber = demo.scrHomePhone;
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
		demo.doctors = toArray(demo.doctors);
	}
	if (demo.nurses!=null) {
		demo.nurses = toArray(demo.nurses);
	}
	if (demo.midwives!=null) {
		demo.midwives = toArray(demo.midwives);
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
	demo.patientStatusList = toArray(demo.patientStatusList);
	demo.patientStatusList.unshift({"value":"FI", "label":"FI - Fired"});
	demo.patientStatusList.unshift({"value":"MO", "label":"MO - Moved"});
	demo.patientStatusList.unshift({"value":"DE", "label":"DE - Deceased"});
	demo.patientStatusList.unshift({"value":"IN", "label":"IN - Inactive"});
	demo.patientStatusList.unshift({"value":"AC", "label":"AC - Active"});
	
	demo.rosterStatusList = toArray(demo.rosterStatusList);
	demo.rosterStatusList.unshift({"value":"FS", "label":"FS - fee for service"});
	demo.rosterStatusList.unshift({"value":"TE", "label":"TE - terminated"});
	demo.rosterStatusList.unshift({"value":"NR", "label":"NR - not rostered"});
	demo.rosterStatusList.unshift({"value":"RO", "label":"RO - rostered"});
	

	//----------------------//
	// on-screen operations //
	//----------------------//
	//monitor data changed
	page.dataChanged = -2;
	if (!page.cannotChange) {
		$scope.$watchCollection("page.demo", function(){
			page.dataChanged++;
		});
		$scope.$watchCollection("page.demo.address", function(){
			page.dataChanged++;
		});
	}
	
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
	    			
	    			if (!isNumber(hcParts["dob"])) {
	    				hcParts["dob"] = null;
	    				hcParts["hinExp"] = null;
	    			}
	    			if (!isNumber(hcParts["hinExp"])) {
	    				hcParts["hinExp"] = null;
	    			}
	    			if (!isNumber(hcParts["issueDate"])) {
	    				hcParts["issueDate"] = null;
	    			}
	    			
    				$scope.setSwipeReady("done");
    				$scope.healthCardUpdateDemographics();
	    		} else {
	    			alert("Not Ontario Health Card");
	    		}
	    		$scope.validateHC(); //Run HCValidation
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
			var swipeDate = "20"+hcParts["issueDate"].substring(0,2)+"-"+hcParts["issueDate"].substring(2,4)+"-"+hcParts["issueDate"].substring(4);
			if (demo.effDate!=swipeDate) {
				demo.effDate = swipeDate;
				page.effDateColor = colorAttn;
			}
		}
		if (hcParts["hinExp"]!=null) {
			var swipeDate = "20"+hcParts["hinExp"].substring(0,2)+"-"+hcParts["hinExp"].substring(2,4)+"-"+hcParts["hinExp"].substring(4);
			if (demo.hcRenewDate!=swipeDate) {
				demo.hcRenewDate = swipeDate;
				page.hcRenewDateColor = colorAttn;
			}
			var hinExpDate = buildDate("20"+hcParts["hinExp"].substring(0,2), hcParts["hinExp"].substring(2,4), hcParts["hinExp"].substring(4));
			if (hinExpDate!=null && now>hinExpDate) {
				alert("This health card has expired!");
				page.hcRenewDateColor = colorAttn;
			}
		}
	}

	//HCValidation
	$scope.validateHC = function(){
		if (demo.hcType!="ON" || demo.hin==null || demo.hin=="") return;
		if (demo.ver==null) demo.ver = "";
		patientDetailStatusService.validateHC(demo.hin,demo.ver).then(function(data){
			if (data.valid==null) {
				page.HCValidation = "n/a";
				page.swipecardMsg = "Done Health Card Action";
			} else {
				page.HCValidation = data.valid ? "valid" : "invalid";
				page.swipecardMsg = data.responseDescription+" ("+data.responseCode+")";
			}
		});
	}
	
	//manage hin/hinVer entries
	var hin0 = demo.hin;
	var ver0 = demo.ver;
	$scope.checkHin = function(){
		if (demo.hcType=="ON" && demo.hin!=null && demo.hin!="") {
			if (demo.hin.length>10) demo.hin = hin0;
			if (!isNumber(demo.hin)) demo.hin = hin0;
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
	}
	
	$scope.formatDate = function(id){
		$scope.calculateAge();
		
		if (id=="DobM" && demo.dobMonth!=null && String(demo.dobMonth).length==1) {
			demo.dobMonth = "0"+demo.dobMonth;
		}
		else if (id=="DobD" && demo.dobDay!=null && String(demo.dobDay).length==1) {
			demo.dobDay = "0"+demo.dobDay;
		}
	}
	$scope.formatDate("DobM"); //done on page load
	$scope.formatDate("DobD"); //done on page load
	
	//check Patient Status if endDate is entered
	$scope.checkPatientStatus = function(){
		if (demo.patientStatus=="AC") {
			if (demo.endDate!=null && demo.endDate!="") {
				if (dateValid(demo.endDate)) {
					var datePart = demo.endDate.split("-");
					var endDate = new Date(datePart[0], datePart[1]-1, datePart[2]);
					if (now > endDate) {
						alert("Patient Status cannot be Active after End Date.");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	//check&format postal code (Canada provinces only)
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
			
			for (var i=0; i<postal.length; i+=2) {
				var cc = postal.charAt(i);
				if (/^[^A-Za-z]$/.test(cc)) return true;

				if (i<postal.length-1) {
					cc = postal.charAt(i+1);
					if (!isNumber(cc)) return true;
				}
			}
		}
		return false;
	}
	
	//check email
	$scope.checkEmail = function() {
		if (demo.email==null || demo.email=="") return true;
		
		var regex = /^[^@]+@[^@]+$/;
		if (regex.test(demo.email)) {
			var email = demo.email.split("@");
			
			regex = /^[!#%&'=`~\{}\-\$\*\+\/\?\^\|\w]+(\.[!#%&'=`~\{}\-\$\*\+\/\?\^\|\w]+)*$/;
			if (regex.test(email[0])) { //test email local address part
				
				regex = /^[^\W_]+(([^\W_]|-)+[^\W_]+)*(\.[^\W_]+(([^\W_]|-)+[^\W_]+)*)*\.[^\W_]{2,3}$/;
				if (regex.test(email[1])) return true; //test email address domain part
			}
		}
		alert("Invalid email address");
		return false;
	}
	
	//check Chart No (length)
	var chartNo0 = demo.chartNo;
	$scope.checkChartNo = function() {
		if (demo.chartNo==null || demo.chartNo=="") {
			chartNo0 = demo.chartNo; return;
		}
		if (demo.chartNo.length>10) demo.chartNo = chartNo0;
		else chartNo0 = demo.chartNo;
	}
	
	//check Cytology Number
	var cytolNum0 = demo.scrCytolNum;
	$scope.checkCytoNum = function() {
		if (demo.scrCytolNum==null || demo.scrCytolNum=="") {
			cytolNum0 = demo.scrCytolNum; return;
		}
		if (!isNumber(demo.scrCytolNum)) demo.scrCytolNum = cytolNum0;
		else cytolNum0 = demo.scrCytolNum;
	}
	
	//check Referral Doctor No
	var referralDocNo0 = demo.scrReferralDocNo;
	$scope.checkReferralDocNo = function() {
		if (demo.scrReferralDocNo==null || demo.scrReferralDocNo=="") {
			referralDocNo0 = demo.scrReferralDocNo; return;
		}
		if (!isNumber(demo.scrReferralDocNo) || demo.scrReferralDocNo.length>6) demo.scrReferralDocNo = referralDocNo0;
		else referralDocNo0 = demo.scrReferralDocNo;
	}
	
	$scope.validateReferralDocNo = function() {
		if (demo.scrReferralDocNo==null || demo.scrReferralDocNo=="") return true;
		
		if (!isNumber(demo.scrReferralDocNo || demo.scrReferralDocNo!=6)) {
			alert("Invalid Referral Doctor Number");
			return false;
		}
		return true;
	}
	
	//check SIN
	var sin0 = demo.sin;
	$scope.checkSin = function() {
		if (demo.sin==null || demo.sin=="") {
			sin0 = demo.sin; return;
		}
		
		var sin = demo.sin.replace(/\s/g, "");
		if (!isNumber(sin) || sin.length>9) {
			demo.sin = sin0;
		} else {
			if (sin.length>6) {
				demo.sin = sin.substring(0,3)+" "+sin.substring(3,6)+" "+sin.substring(6);
			}
			else if (sin.length>3) {
				demo.sin = sin.substring(0,3)+" "+sin.substring(3);
			}
			sin0 = demo.sin;
		}
	}
	
	$scope.validateSin = function() {
		if (demo.sin==null || demo.sin=="") return true;
		
		var sin = demo.sin.replace(/\s/g, "");
		if (isNumber(sin) && sin.length==9) {
			var sinNumber = 0;
			for (var i=0; i<sin.length; i++) {
				var n = Number(sin.charAt(i))*(i%2+1);
				sinNumber += n%10 + Math.floor(n/10);
			}
			if (sinNumber%10==0) return true;
		}
		alert("Invalid SIN #");
		return false;
	}
	
	//prevent manual input dates
	var effDate0 = demo.effDate;
	var hcRenewDate0 = demo.hcRenewDate;
	var rosterDate0 = demo.rosterDate;
	var rosterTerminationDate0 = demo.rosterTerminationDate;
	var patientStatusDate0 = demo.patientStatusDate;
	var dateJoined0 = demo.dateJoined;
	var endDate0 = demo.endDate;
	var onWaitingListSinceDate0 = demo.onWaitingListSinceDate;
	var paperChartArchivedDate0 = demo.scrPaperChartArchivedDate;
	
	$scope.preventManualEffDate = function() {
		if (demo.effDate==null) demo.effDate = effDate0;
		else effDate0 = demo.effDate;
	}
	$scope.preventManualHcRenewDate = function() {
		if (demo.hcRenewDate==null) demo.hcRenewDate = hcRenewDate0;
		else hcRenewDate0 = demo.hcRenewDate;
	}
	$scope.preventManualRosterDate = function() {
		if (demo.rosterDate==null) demo.rosterDate = rosterDate0;
		else rosterDate0 = demo.rosterDate;
	}
	$scope.preventManualRosterTerminationDate = function() {
		if (demo.rosterTerminationDate==null) demo.rosterTerminationDate = rosterTerminationDate0;
		else rosterTerminationDate0 = demo.rosterTerminationDate;
	}
	$scope.preventManualPatientStatusDate = function() {
		if (demo.patientStatusDate==null) demo.patientStatusDate = patientStatusDate0;
		else patientStatusDate0 = demo.patientStatusDate;
	}
	$scope.preventManualDateJoined = function() {
		if (demo.dateJoined==null) demo.dateJoined = dateJoined0;
		else dateJoined0 = demo.dateJoined;
	}
	$scope.preventManualEndDate = function() {
		if (demo.endDate==null) demo.endDate = endDate0;
		else endDate0 = demo.endDate;
	}
	$scope.preventManualOnWaitingListSinceDate = function() {
		if (demo.onWaitingListSinceDate==null) demo.onWaitingListSinceDate = onWaitingListSinceDate0;
		else onWaitingListSinceDate0 = demo.onWaitingListSinceDate;
	}
	$scope.preventManualPaperChartArchivedDate = function() {
		if (demo.scrPaperChartArchivedDate==null) demo.scrPaperChartArchivedDate = paperChartArchivedDate0;
		else paperChartArchivedDate0 = demo.scrPaperChartArchivedDate;
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
			demo.scrReferralDocNo = page.referralDocObj.referralNo;
			demo.scrReferralDoc = page.referralDocObj.name;
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
	phoneNum["C"] = demo.scrCellPhone;
	phoneNum["H"] = demo.scrHomePhone;
	phoneNum["W"] = demo.scrWorkPhone;
	phoneNum["HX"] = demo.scrHPhoneExt;
	phoneNum["WX"] = demo.scrWPhoneExt;
	
	$scope.checkPhone = function(type){
		if (type=="C") {
			if (invalidPhoneNumber(demo.scrCellPhone)) demo.scrCellPhone = phoneNum["C"];
			else phoneNum["C"] = demo.scrCellPhone;
		}
		else if (type=="H") {
			if (invalidPhoneNumber(demo.scrHomePhone)) demo.scrHomePhone = phoneNum["H"];
			else phoneNum["H"] = demo.scrHomePhone;
		}
		else if (type=="W") {
			if (invalidPhoneNumber(demo.scrWorkPhone)) demo.scrWorkPhone = phoneNum["W"];
			else phoneNum["W"] = demo.scrWorkPhone;
		}
		else if (type=="HX" && demo.scrHPhoneExt!=null && demo.scrHPhoneExt!="") {
			if (!isNumber(demo.scrHPhoneExt)) demo.scrHPhoneExt = phoneNum["HX"];
			else phoneNum["HX"] = demo.scrHPhoneExt;
		}
		else if (type=="WX" && demo.scrWPhoneExt!=null && demo.scrWPhoneExt!="") {
			if (!isNumber(demo.scrWPhoneExt)) demo.scrWPhoneExt = phoneNum["WX"];
			else phoneNum["WX"] = demo.scrWPhoneExt;
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
		
		if (demo.scrPreferredPhone=="C") {
			page.preferredPhoneNumber = demo.scrCellPhone;
			page.cellPhonePreferredMsg = prefPhTitle;
			page.cellPhonePreferredColor = colorAttn;
		}
		else if (demo.scrPreferredPhone=="H") {
			page.preferredPhoneNumber = demo.scrHomePhone;
			page.homePhonePreferredMsg = prefPhTitle;
			page.homePhonePreferredColor = colorAttn;
		}
		else if (demo.scrPreferredPhone=="W") {
			page.preferredPhoneNumber = demo.scrWorkPhone;
			page.workPhonePreferredMsg = prefPhTitle;
			page.workPhonePreferredColor = colorAttn;
		}
	}
	
	//disable set-preferred if phone number empty
	$scope.isPhoneVoid = function(phone){
		return (phone==null || phone=="");
	}
	
	//show enrollment history (roster staus history)
	$scope.showEnrollmentHistory = function(){
		var url = "../demographic/EnrollmentHistory.jsp?demographicNo="+demo.demographicNo;
		window.open(url, "enrollmentHistory", "width=650, height=1000");
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
				alert("Please login to PHR first");
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
			url="../billing/CA/ON/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid="+demo.demographicNo+"&creator="+user.providerNo+"&demographic_name="+encodeURI(demo.lastName)+encodeURI(",")+encodeURI(demo.firstName)+"&hin="+demo.hin+demo.ver+"&demo_sex="+demo.sex+"&demo_hctype="+demo.hcType+"&rd="+encodeURI(demo.scrReferralDoc)+"&rdohip="+demo.scrReferralDocNo+"&dob="+demo.dobYear+demo.dobMonth+demo.dobDay+"&mrp="+demo.providerNo;
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

	//view as cda
	$scope.viewAsCDA = function(){
		var url = "../ws/rs/e2eCDA/"+demo.demographicNo;
		var title = demo.firstName + " " + demo.lastName + ": E2E CDA Chart";
		window.open(url, title, "width=960, height=700");
	}
	
	//HCValidation on open & save
	$scope.validateHCSave = function(doSave){
		if (demo.hin==null || demo.hin=="") {
			if (doSave) $scope.save();
		} else {
			patientDetailStatusService.isUniqueHC(demo.hin,demo.demographicNo).then(function(data){
				if (!data.success) {
					alert("HIN is already in use!");
				}
				else if (demo.hcType!="ON") {
					if (doSave) $scope.save();
				}
				else {
					if (demo.ver==null) demo.ver = "";
					patientDetailStatusService.validateHC(demo.hin,demo.ver).then(function(data){
						if (data.valid==null) {
							page.HCValidation = "n/a";
						}
						else if (!data.valid) {
							alert("Health Card Validation failed: "+data.responseDescription+" ("+data.responseCode+")");
							doSave = false;
						}
						if (doSave) $scope.save();
					});
				}
			});
		}
	}
	$scope.validateHCSave();
	
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
		
		//validate field inputs
		demo.dateOfBirth = buildDate(demo.dobYear, demo.dobMonth, demo.dobDay);
		if (demo.dateOfBirth==null) {
			alert("Invalid Date of Birth"); return;
		}
		if (!$scope.checkPatientStatus()) return;
		if (!$scope.isPostalComplete()) return;
		if (!$scope.validateSin()) return;
		if (!$scope.validateReferralDocNo()) return;
		
		//save notes
		if (demo.scrNotes!=null) {
			demo.notes = "<unotes>" + demo.scrNotes + "</unotes>";
		}
		
		//save referral doctor (familyDoctor)
		var referralDocNo = "<rdohip></rdohip>";
		var referralDoc = "<rd></rd>";
		if (demo.scrReferralDocNo!=null && demo.scrReferralDocNo!="") {
			referralDocNo = "<rdohip>"+demo.scrReferralDocNo+"</rdohip>";
		}
		if (demo.scrReferralDoc!=null && demo.scrReferralDoc!="") {
			referralDoc = "<rd>"+demo.scrReferralDoc+"</rd>";
		}
		demo.familyDoctor = referralDocNo + referralDoc;

		//save phone numbers
		demo.scrDemoCell = demo.scrCellPhone;
		demo.phone = demo.scrHomePhone;
		demo.alternativePhone = demo.scrWorkPhone;
		
		if (demo.scrPreferredPhone=="C") demo.scrDemoCell += "*";
		else if (demo.scrPreferredPhone=="H") demo.phone += "*";
		else if (demo.scrPreferredPhone=="W") demo.alternativePhone += "*";
		
		//save extras
		var newDemoExtras = [];
		newDemoExtras = updateDemoExtras("demo_cell", demo.scrDemoCell, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("aboriginal", demo.scrAboriginal, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("hPhoneExt", demo.scrHPhoneExt, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("wPhoneExt", demo.scrWPhoneExt, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("cytolNum", demo.scrCytolNum, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("phoneComment", demo.scrPhoneComment, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("paper_chart_archived", demo.scrPaperChartArchived, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("paper_chart_archived_date", demo.scrPaperChartArchivedDate, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("usSigned", demo.scrUsSigned, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("privacyConsent", demo.scrPrivacyConsent, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("informedConsent", demo.scrInformedConsent, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("securityQuestion1", demo.scrSecurityQuestion1, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("securityAnswer1", demo.scrSecurityAnswer1, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("rxInteractionWarningLevel", demo.scrRxInteractionLevel, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("HasPrimaryCarePhysician", demo.hasPrimaryCarePhysician, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("EmploymentStatus", demo.employmentStatus, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("statusNum", demo.scrStatusNum, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("fNationCom", demo.scrFNationCom, posExtras, demo.extras, newDemoExtras); 
		newDemoExtras = updateDemoExtras("fNationFamilyNumber", demo.scrFNationFamilyNumber, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("fNationFamilyPosition", demo.scrFNationFamilyPosition, posExtras, demo.extras, newDemoExtras);
		newDemoExtras = updateDemoExtras("ethnicity", demo.scrEthnicity, posExtras, demo.extras, newDemoExtras);

		demo.extras = newDemoExtras;
		
		//save to database
		demographicService.updateDemographic(demo);
		
		//show Saving... message and refresh screen
		page.saving = true;
		location.reload();
	}

	$scope.loadHistoryList = function() {
		/*
		demographicService.historyList($scope.page.demo.demographicNo).then(function(data){
			alert(JSON.stringify(data));
		},function(error){
			
		});
		*/
		
		var modalInstance = $modal.open({
        	templateUrl: 'record/details/historyList.jsp',
            controller: 'DetailsHistoryListCtrl',
            backdrop: false,
            size: 'lg',
            resolve: {
                historyList: function() {
                	return demographicService.historyList($scope.page.demo.demographicNo);
                }
            }
        });
        
        modalInstance.result.then(function(data){
        	console.log('data from modalInstance '+data);
        },function(reason){
        	alert(reason);
        });
        
	}
});

function updateDemoExtras(extKey, newVal, posExtras, oldExtras, newExtras) {
	if (newVal==null) return newExtras;
	
	var pos = posExtras[extKey];
	if (pos!=null && oldExtras[pos]!=null) { //existing ext
		if (oldExtras[pos].value!=newVal) {
			newExtras.push({id:oldExtras[pos].id, key:extKey, value:newVal, hidden:oldExtras[pos].hidden});
		}
	} else { //newly added ext
		newExtras.push({key:extKey, value:newVal});
	}
	return newExtras;
}

function buildDate(year, month, day) {
	if (dateEmpty(year, month, day)) return "";
	if (date3Valid(year, month, day)) return year + "-" + month + "-" + day;
	return null;
}

function checkYear(year) {
	for (var i=0; i<year.length; i++) {
		if (!isNumber(year.charAt(i))) {
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
		if (!isNumber(month.charAt(i))) {
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
		if (!isNumber(day.charAt(i))) {
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
		else if (!date3Valid(year, month, day)) {
			day = day.substring(0,1);
		}
	}
	return day;
}

function date3Valid(year, month, day) {
	if (year!=null && year!="" && month!=null && month!="" && day!=null && day!="") {
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

function dateEmpty(year, month, day) {
	return ((year==null || year=="") && (month==null || month=="") && (day==null || day==""));
}

function dateValid(dateStr) { //valid date format: yyyy-MM-dd
	if (dateStr==null || dateStr=="") return true;
	
	var datePart = dateStr.toString().split("-");
	if (datePart.length!=3) return false;
	
	var dateDate = new Date(datePart[0], datePart[1]-1, datePart[2]);
	if (isNaN(dateDate.getTime())) return false;
	
	if (dateDate.getFullYear()!=datePart[0]) return false;
	if (dateDate.getMonth()!=datePart[1]-1) return false;
	if (dateDate.getDate()!=datePart[2]) return false;
	
	return true;
}

function isNumber(s) {
	return /^[0-9]+$/.test(s);
}

function invalidPhoneNumber(phone) {
	if (phone==null) return false; //phone number is NOT invalid
	return !(/^[0-9 \-\()]*$/.test(phone));
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

function toArray(obj) { //convert single object to array
	if (obj instanceof Array) return obj;
	else if (obj==null) return [];
	else return [obj];
}
