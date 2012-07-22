/*


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

*/

if (!Array.prototype.indexOf)
{
  Array.prototype.indexOf = function(elt /*, from*/)
  {
    var len = this.length;

    var from = Number(arguments[1]) || 0;
    from = (from < 0)
         ? Math.ceil(from)
         : Math.floor(from);
    if (from < 0)
      from += len;

    for (; from < len; from++)
    {
      if (from in this &&
          this[from] === elt)
        return from;
    }
    return -1;
  };
}

function validateSearchForm() {
	if (document.forms[0].elements['firstName'].value == '' || document.forms[0].elements['lastName'].value == '') {
		alert('First name and last name are mandatory');
		
		return false;
	}
			
	return true;
}

function validBedCommunityProgram() {
	if(getElement('bedCommunityProgramId') == null) {
		return true;
	}
	var programIdStr=getElement('bedCommunityProgramId').value;
	var programId = Number(programIdStr);
		
	if(programIdStr == "") {
		//alert( "Bed program is mandatory");
		return false;
	} else {
		return true;
	}
}

function validIntakeLocation() {
	var programIdStr=getElement('programInDomainId').value;
	var programId = Number(programIdStr);
		
	if(programIdStr == "") {		
		return false;
	} else {
		return true;
	}
}

function validateEdit() {
	
	if(document.getElementById('skip_validate').value == 'true') {
		return true;
	}
	
	//$("form").validate({meta: "validate"});
	
	var programIdEl = getElement('bedCommunityProgramId');
	var programId = 0;
	if(programIdEl != null) {
		var programIdStr = programIdEl.value;
		programId = Number(programIdStr);
	}	
	var gender=getElement('client.sex');

	if (gender.value == 'M')
	{
		if (programFemaleOnly.indexOf(programId)>=0 ||  programTransgenderOnly.indexOf(programId)>=0)
		{
			return error(gender, "This gender not allowed in selected program.");
		}
	}
	if (gender.value == 'F')
	{
		if (programMaleOnly.indexOf(programId)>=0 ||  programTransgenderOnly.indexOf(programId)>=0)
		{
			return error(gender, "This gender not allowed in selected program.");
		}
	}
	if (gender.value == 'T')
	{
		if (programFemaleOnly.indexOf(programId)>=0 ||  programMaleOnly.indexOf(programId)>=0)
		{
			return error(gender, "This gender not allowed in selected program.");
		}
	}
	if(getElement('client.formattedDob').value.length==0) {
		return error(getElement('client.formattedDob'), "The birth date you've entered is not a valid date.");
	}
	
/*
	var month = parseInt(getElement('client.monthOfBirth').value, 10);
	var day = parseInt(getElement('client.dateOfBirth').value, 10);
	var year = parseInt(getElement('client.yearOfBirth').value, 10);
	if (!validateDate(year, month, day))
	{
		return error(getElement('client.yearOfBirth'), "The birth date you've entered is not a valid date.");
	}

	var age = calculateAge(year, month, day);
	if (!validAgeRangeForProgram(programId, age))
	{
		return error(getElement('client.yearOfBirth'), "This client does not meet the age range requirements for this program.");
	}
*/
	// only allow numbers to be entered
	var hinProvince = getElement('client.hcType').value;
	var healthCardNo = getElement('client.hin').value;
	var healthCardVersion = getElement('client.ver').value;
	if (isNaN(healthCardNo))
    {
    	alert("Please enter only digit characters in the \"Health Card #\" field.");
		document.forms[0].elements['client.hin'].focus();
		return false;
    }

	if (!isValidHin(healthCardNo, hinProvince))
	{
		alert ("You must type in the right HIN.");
		return(false);
	}

	
	//only allow upper case alpha characters to be entered
	if (!checkChar(healthCardVersion)){
		return error(healthCardVersion, "Please enter only alpha characters in the \"Version\" field.");
		
	}
	document.forms[0].elements['client.ver'].value = healthCardVersion.toUpperCase();
	
	if (!check_mandatory()) {
	    return false;
	}

	if (!hasSelection(getElement('serviceProgramIds'))) {
		return window.confirm("No service program has been selected.\nAre you sure you want to submit?");
	}

	//since the date fields can be editable, we need to check them.
	
	var pattern=new RegExp("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]","");
	if(!pattern.test(document.forms[0].elements['client.formattedDob'].value)) {
		return error(getElement('client.formattedDob'), "The birth date you've entered is not a valid date.Please use the yyyy-MM-dd pattern");		
	}
	if(document.forms[0].elements['client.formattedEffDate'].value.length > 0) {
		if(!pattern.test(document.forms[0].elements['client.formattedEffDate'].value)) {
			return error(getElement('client.formattedEffDate'), "The HC Effective Date you've entered is not a valid date.Please use the yyyy-MM-dd pattern");		
		}
	}
	if(document.forms[0].elements['client.formattedRenewDate'].value.length > 0) {
		if(!pattern.test(document.forms[0].elements['client.formattedRenewDate'].value)) {
			return error(getElement('client.formattedRenewDate'), "The HC Renewal Date you've entered is not a valid date.Please use the yyyy-MM-dd pattern");		
		}
	}
	
	return true;
}

function search() {
	setMethod('search');
}

function createLocal() {
	if (confirm("A client with a similar first and last name was found. Are you sure this isn't the client you are looking for?")) {
		setMethod('createLocal');
		
		return true;
	} else {
		return false;
	}
}

function updateLocal(demographicId) {
	document.forms[0].elements['demographicId'].value = demographicId;
	setMethod('updateLocal');
}

function save() {
	var intakeType = document.forms[0].elements['intakeType'].value;
	if(intakeType!="indepth") {
		if(!validBedCommunityProgram()) {
			alert( "Bed program is mandatory");
			return false;
		}
	}
	
	setMethod('save');
}

function save_draft() {
	setMethod('draft');
}

function save_temp() {
	if(!validBedCommunityProgram()) {
		alert( "Bed program is mandatory");
		return false;
	}
	if(!validIntakeLocation()) {
		alert("You cannot make a temporary save without selecting an intake location.");
		return false;
	}
		 
    if(isBedCommunityProgramChanged()) {	
    	      	
       	alert("You cannot make the changes to the admission. Use 'Admit Sign and Save' to change the admission program.");
    	return false;
    }
	
	setMethod('save_temp');
}

function save_admit() {
	if(!validBedCommunityProgram()) {
		alert( "Bed program is mandatory");		
		return false;			
	} 
	if(isBedCommunityProgramChanged()) {
		if(isClientDependentOfFamily) {
			alert("This client is a dependent. To make a bed program admission for this client you must admit the family head or have the dependent status removed.");
			return false;
		} 
	}
	var programIdStr=getElement('bedCommunityProgramId').value;
	var programId = Number(programIdStr);
	if(isNewFacility(programId, oldProgramId) ) {							
		return window.confirm("The client is currently admitted in another facility. Admitting into your facility will result in an automatic discharge from that other facility. As an alternative to this, it is recommended that you perform a temporary save of this intake or an intake without admission and contact the other facility so that they can make a discharge before you admit this client.\n Do you still wish to automatically discharge this client from the other facility and admit into this program? ");
	} 
	
	setMethod('save_admit');
}

function save_notAdmit() {
	if(!validBedCommunityProgram()) {
		alert( "Bed program is mandatory");
		return false;
	}
	if(!validIntakeLocation()) {
		alert("You cannot save without selecting an intake location.");
		return false;
	}
	if(isBedCommunityProgramChanged()) {	       	
       alert("You cannot make the changes to the admission. Use 'Admit Sign and Save' to change the admission program.");
    	return false;
    }	

	
	setMethod('save_notAdmit');
}

function isBedCommunityProgramChanged() {
	var oldId = document.forms[0].elements['currentBedCommunityProgramId_old'].value;
    var newId = document.forms[0].elements['bedCommunityProgramId'].value;
    
    if(oldId!=null && oldId!="null" && oldId!="" && oldId!=newId) {      	
       return true;
    } 
    return false;
}


function check_mandatory() {
    var mquestSingle = new Array();
    var mquestMultiIdx = new Array();
    var mquestMultiName = new Array();
    var mqs = 0;
    var mqm = 0;
    var ret = false;
    for (i=0; i<document.forms[0].elements.length; i++) {
	if (document.forms[0].elements[i].name.substring(0,7)=="mquests") {
	    mquestSingle[mqs] = document.forms[0].elements[i].value;
	    mqs++;
	} else if (document.forms[0].elements[i].name.substring(0,7)=="mquestm") {
	    mquestMultiIdx[mqm] = document.forms[0].elements[i].name;
	    mquestMultiName[mqm] = document.forms[0].elements[i].value;
	    mqm++;
	}
    }
    ret = check_mandatory_single(mquestSingle);
    if (ret) {
	ret = check_mandatory_multi(mquestMultiIdx, mquestMultiName);
    }
    if (!ret) {
	alert("All mandatory questions must be answered!");
    }
    return ret;
}

function check_mandatory_single(mqSingle) {
    var errFree = true;
    for (i=0; i<document.forms[0].elements.length; i++) {
	for (j=0; j<mqSingle.length; j++) {
	    if (document.forms[0].elements[i].name==mqSingle[j]) {
		errFree = checkfilled(document.forms[0].elements[i]);
	    }
	    break;
	}
	if (!errFree) {
	    break;
	}
    }
    return errFree;
}

function check_mandatory_multi(mqIndex, mqName) {
    var errFree = true;
    var ans_ed = 0;
    
    for (i=0; i<mqIndex.length; i++) {
	for (j=0; j<document.forms[0].elements.length; j++) {
	    if (document.forms[0].elements[j].name==mqName[i]) {
		if (checkfilled(document.forms[0].elements[j])) {
		    ans_ed++;
		}
		break;
	    }
	}
	if (i==mqIndex.length-1 || (i<mqIndex.length-1 && nxtGrp(mqIndex[i], mqIndex[i+1]))) {
	    if (ans_ed==0) {
		errFree = false;
		break;
	    } else {
		ans_ed = 0;
	    }
	}
    }
    return errFree;
}

function nxtGrp(first, second) {
    var mrk = first.lastIndexOf('_') + 1;
    var firstIdx = first.substring(mrk, first.length);
    mrk = second.lastIndexOf('_') + 1;
    var secondIdx = second.substring(mrk, second.length);
    
    return (firstIdx!=secondIdx);
}

function checkfilled(elem) {
    if (elem.type=="text" || elem.type=="textarea") {
	if (elem.value.replace(" ","")=="") {
	    return false;
	}
    } else if (elem.type=="checkbox") {
	if (!elem.checked) {
	    return false;
	}
    }
    return true;
}

function clientEdit() {
	setMethod('clientEdit');
}

function setMethod(target) {
	document.forms[0].method.value=target;
}

function hasSelection(checkBoxGroup) {
	if (checkBoxGroup == null) {
		return true;
	}
	
	if (isArray(checkBoxGroup)) {
		for (i = 0; i < checkBoxGroup.length; i++) {
			if (checkBoxGroup[i].checked) {
				return true;
			} j
		}
	} else {
		return checkBoxGroup.checked;
	}
	
	return false;
}

function isArray(obj) {
	return (typeof (obj.length) == "undefined") ? false : true;
}

function isEmpty(elementValue) {
	return elementValue == '';
}

function getElement(name) {
	return document.forms[0].elements[name];
}

function error(element, msg) {
	element.focus();
	alert(msg);
	
	return false;
}

// only allow numbers to be entered
function checkChar(checkString) {
	var checkOK = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";	
	var allValid = true;
	var checkStr = checkString;	
	for (i = 0;  i < checkStr.length;  i++){	
		ch = checkStr.charAt(i);
		for (j = 0;  j < checkOK.length;  j++) 
			if (ch == checkOK.charAt(j)) 		
				break;
				
		if (j == checkOK.length) {
			allValid = false;
			break;
		}						
	}
	if (!allValid)		
		return false;
	else
		return true;
}
