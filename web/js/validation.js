/* This method will return true if valid, false otherwise (and present an alert box). */ 
function validateRequiredField(fieldId, fieldName, maxLength)
{
	var field=document.getElementById(fieldId);

	if (field.value==null || field.value=='')
	{
		alert('The field '+fieldName+' is required.');
		return(false);
	}
	
	if (field.value.length > maxLength)
	{
		alert('The value you entered for '+fieldName+' is too long, maximum length allowed is '+maxLength+' characters.');
		return(false);
	}
	
	return(true);
}