function save() {
	setMethod('save');
}

function setMethod(target) {
	document.forms[0].method.value=target;
}