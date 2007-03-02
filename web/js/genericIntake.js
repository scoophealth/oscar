function validateSearchForm() {
	if (document.forms[0].elements['firstName'].value == '' && document.forms[0].elements['lastName'].value == '' &&
		document.forms[0].elements['monthOfBirth'].value == '' && document.forms[0].elements['dayOfBirth'].value == '' && document.forms[0].elements['yearOfBirth'].value == '' &&
		document.forms[0].elements['healthCardNumber'].value == '' && document.forms[0].elements['healthCardVersion'].value == '') {
		
		alert('You must use at least one of the search fields');
		
		return false;
	}
	
	return true;
}
	
function search() {
	setMethod('search');
}

function createLocal() {
	setMethod('createLocal');
}

function updateLocal(clientId) {
	document.forms[0].elements['clientId'].value = clientId;
	setMethod('updateLocal');
}

function copyRemote(agencyId, clientId) {
	document.forms[0].elements['agencyId'].value = agencyId;
	document.forms[0].elements['clientId'].value = clientId;
	setMethod('copyRemote');
}

function save() {
	setMethod('save');
}

function setMethod(target) {
	document.forms[0].method.value=target;
}