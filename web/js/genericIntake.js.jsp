<%@page import="org.oscarehr.PMmodule.web.ProgramUtils"%>
<%@page import="org.oscarehr.common.dao.IntakeRequiredFieldsDao"%>
<%@page contentType="text/javascript"%>

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

function validateEdit() {
	<%
		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_FIRST_NAME))
		{
			%>
				var firstName = getElement('client.firstName');
				if (isEmpty(firstName.value)) {
					return error(firstName, "First name is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_LAST_NAME))
		{
			%>
				var lastName = getElement('client.lastName');
				if (isEmpty(lastName.value)) {
					return error(lastName, "Last name is mandatory");
				}
			<%
		}

	%>
		var gender = getElement('client.sex');
	<%
		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_GENDER))
		{
			%>
				if (isEmpty(gender.value)) {
					return error(gender, "Gender is mandatory");
				}
			<%
		}
	%> 
		var programId=Number(getElement('bedCommunityProgramId').value);
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
		
		var month = getElement('client.monthOfBirth');
		var day = getElement('client.dateOfBirth');
		var year = getElement('client.yearOfBirth');
		
		var age=calculateAge(year.value,month.value,day.value);
		if (!validAgeRangeForProgram(programId,age))
		{
			return error(year, "This client does not meet the age range requirements for this program.");
		}
		
	<%
		
		
		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_BIRTH_DATE))
		{
			%>
				if (!isEmpty(month.value) || !isEmpty(day.value) || !isEmpty(year.value)) {
					var date = month.value + "/" + day.value + "/" + year.value;
					
					if (!dojo.validate.isValidDate(date, 'MM/DD/YYYY')) {
						return error(month, date + " (MM/DD/YYYY) is not a valid date");
					}
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_EMAIL))
		{
			%>
				var email = getElement('client.email');
				
				if (!isEmpty(email.value) && !dojo.validate.isEmailAddress(email.value)) {
					return error(email, email.value + " is not a valid email address");
				}				
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PHONE))
		{
			%>
				var phoneNumber = getElement('client.phone');
			
				if (!isEmpty(phoneNumber.value) && !dojo.validate.us.isPhoneNumber(phoneNumber.value)) {
					return error(phoneNumber, phoneNumber.value + " is not a valid phone number");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PHONE2))
		{
			%>
				var secondaryPhoneNumber = getElement('client.phone2');
			
				if (!isEmpty(secondaryPhoneNumber.value) && !dojo.validate.us.isPhoneNumber(secondaryPhoneNumber.value)) {
					return error(secondaryPhoneNumber, secondaryPhoneNumber.value + " is not a valid phone number");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_STREET))
		{
			%>
				var address = getElement('client.address');
				if (isEmpty(address.value)) {
					return error(address, "Street is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_CITY))
		{
			%>
				var city = getElement('client.city');
				if (isEmpty(city.value)) {
					return error(city, "City is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PROVINCE))
		{
			%>
				var province = getElement('client.province');
				if (isEmpty(province.value)) {
					return error(province, "Province is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_POSTAL_CODE))
		{
			%>
				var postal = getElement('client.postal');
				if (isEmpty(postal.value)) {
					return error(postal, "Postal Code is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_ALIAS))
		{
			%>
				var alias = getElement('client.alias');
				if (isEmpty(alias.value)) {
					return error(alias, "Alias is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_CITIZENSHIP))
		{
			%>
				var citizenship = getElement('client.citizenship');
				if (isEmpty(citizenship.value)) {
					return error(citizenship, "Citizenship is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_CHILDREN))
		{
			%>
				var children = getElement('client.children');
				if (isEmpty(children.value)) {
					return error(children, "Children is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PREVIOUS_ADDRESS))
		{
			%>
				var previousAddress = getElement('client.previousAddress');
				if (isEmpty(previousAddress.value)) {
					return error(previousAddress, "Previous Address is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_SOURCE_OF_INCOME))
		{
			%>
				var sourceOfIncome = getElement('client.sourceOfIncome');
				if (isEmpty(sourceOfIncome.value)) {
					return error(sourceOfIncome, "Source Of Income is mandatory");
				}
			<%
		}

		if (IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_SIN))
		{
			%>
				var sin = getElement('client.sin');
				if (isEmpty(sin.value)) {
					return error(sin, "Social Insurance Number is mandatory");
				}
			<%
		}
	%>	

	

		
	// only allow numbers to be entered
	var healthCardNo = getElement('client.hin');
	var healthCardVersion = getElement('client.ver');
	if(isNaN(healthCardNo.value))
    {
    	alert("Please enter only digit characters in the \"Health Card #\" field.");
		document.forms[0].elements['client.hin'].focus();
		return false;
    }
	
	//only allow upper case alpha characters to be entered
	if(!checkChar(healthCardVersion)){
		return error(healthCardVersion, "Please enter only alpha characters in the \"Version\" field.");
		
	}
	document.forms[0].elements['client.ver'].value = healthCardVersion.value.toUpperCase();
	
	
	if (!hasSelection(getElement('serviceProgramIds'))) {
		return window.confirm("No service program has been selected.\nAre you sure you want to submit?");
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

function copyRemote(agencyId, demographicId) {
	document.forms[0].elements['remoteAgency'].value = agencyId;
	document.forms[0].elements['remoteAgencyDemographicNo'].value = demographicId;
	setMethod('copyRemote');
}

function save() {
	setMethod('save');
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
			}
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
	var checkStr = checkString.value;	
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