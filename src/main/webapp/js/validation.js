/* This method will return true if valid, false otherwise (and present an alert box). */ 
String.prototype.trim = function() { return this.replace(/^\s+|\s+$/, ''); };
function isInt(str)
{
	var iv =  parseInt(str);
	var fv = parseFloat(str);
	return iv == fv;
}
function isName(str) 
{ 
    var reg = new RegExp(/^[\s\'\-a-zA-Z]+$/); 
    var flag = reg.test(str);
    if(flag){
		var len = str.length;
		var startChar = str.substring(0,1);
		var endChar = str.substring(len-1);
		if(startChar == "'" || startChar == "-" || endChar == "'" || endChar == "-" || str.indexOf("''") >= 0 || str.indexOf("--") >= 0|| str.indexOf("  ") >= 0){
			flag = false	
		}	
	}
	return flag;
}
function isUserId(str) 
{ 
    var reg = new RegExp(/^[\sa-zA-Z0-9]+$/); 
    var flag = reg.test(str);
    if(!flag){
    	alert("User ID should be alphanumeric");
	}
	return flag;
}


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
function validateLength(field, fieldNameDisplayed, maxLength, minLength){
	
	if (maxLength > 0 && field.value.length > maxLength){
		alert('The value you entered for "'+ fieldNameDisplayed + '" is too long, maximum length allowed is '+maxLength+' characters.');
		return(false);
	}

	if (minLength > 0 && field.value.length < minLength){
		alert('The value you entered for "' + fieldNameDisplayed + '" is too short, minimum length allowed is ' + minLength+' characters.');
		return(false);
	}
	
	return(true);
}
 
function isBeforeNow(inputDay, inputHour, inputMinute) {
	if(inputDay==null || inputDay==''){
      	return false;
	}
	var now = new Date();
	var date=new Date();
	var myDate_array=inputDay.split("/");
	date.setFullYear(myDate_array[0], myDate_array[1]-1, myDate_array[2]);
	if (!(inputHour==null || inputHour =='')) date.setHours(inputHour);
	if (!(inputMinute==null || inputMinute =='')) date.setMinutes(inputMinute);
	date.setSeconds(59.999);
    if (now < date){
      return false;
    }	
	else return true;	
} 
function isBeforeNowxMin(inputDay, inputHour, inputMinute, allowedMinutes) {
	if(inputDay==null || inputDay==''){
      	return false;
	}
	var now = new Date();
	var date=new Date();
	var myDate_array=inputDay.split("/");
	date.setFullYear(myDate_array[0], myDate_array[1]-1, myDate_array[2]);
	if (!(inputHour==null || inputHour =='' || inputHour == 0)) date.setHours(inputHour);
	if (!(inputMinute==null || inputMinute =='' || inputMinute == 0)) date.setMinutes(inputMinute);
	date.setSeconds(0.0);
	now.setSeconds(0.0);
    if ((now.getTime() - date.getTime()) > allowedMinutes*60000) {
      return true;
    }	
	else return false;
} 

function isBeforeToday(inputStr) {
	if(inputStr == null || inputStr == '') return false;
	var myDate_array=inputStr.split("/");
	var yr1 = myDate_array[0];
	var m1 = myDate_array[1]-1;
	var d1 = myDate_array[2];
	
	var today = new Date();
	var yr2 = today.getFullYear();
	var m2 = today.getMonth();
	var d2 = today.getDate();

	if (yr1 > yr2) return false;
	if (yr1 < yr2) return true;
	
	if(m1 > m2) return false;
	if(m1 < m2) return true; 	

	return d1 < d2;
} 

function isBefore(inputStr1, inputStr2) {
	var myDate_array1=inputStr1.split("/");
	var yr1 = myDate_array1[0];
	var m1 = myDate_array1[1]-1;
	var d1 = myDate_array1[2];

	var myDate_array2=inputStr2.split("/");
	var yr2 = myDate_array2[0];
	var m2 = myDate_array2[1]-1;
	var d2 = myDate_array2[2];
	

	if (yr1 > yr2) return false;
	if (yr1 < yr2) return true;
	
	if(m1 > m2) return false;
	if(m1 < m2) return true; 	

	return d1 < d2;
} 
function isAfter(inputStr1, inputStr2) {
	var myDate_array1=inputStr1.split("/");
	var yr1 = myDate_array1[0];
	var m1 = myDate_array1[1]-1;
	var d1 = myDate_array1[2];

	var myDate_array2=inputStr2.split("/");
	var yr2 = myDate_array2[0];
	var m2 = myDate_array2[1]-1;
	var d2 = myDate_array2[2];
	

	if (yr1 < yr2) return false;
	if (yr1 > yr2) return true;
	
	if(m1 < m2) return false;
	if(m1 > m2) return true; 	

	return d1 > d2;
} 

function isBeforeorEqual(inputStr1, inputStr2) {
	var myDate_array1=inputStr1.split("/");
	var yr1 = myDate_array1[0];
	var m1 = myDate_array1[1]-1;
	var d1 = myDate_array1[2];

	var myDate_array2=inputStr2.split("/");
	var yr2 = myDate_array2[0];
	var m2 = myDate_array2[1]-1;
	var d2 = myDate_array2[2];
	

	if (yr1 > yr2) return false;
	if (yr1 < yr2) return true;
	
	if(m1 > m2) return false;
	if(m1 < m2) return true; 	

	return d1 <= d2;
} 

//for html tag on pages, added by Dawson
function validateRequiredFieldByName(fieldName, fieldNameDisplayed, maxLength)
{
	var field=document.getElementsByName(fieldName)[0];

	if (field.value==null || field.value=='')
	{
		alert('The field '+fieldName+' is required.');
		return(false);
	}
	
	if (field.value.length > maxLength)
	{
		alert('The value you entered for '+fieldNameDisplayed+' is too long, maximum length allowed is '+maxLength+' characters.');
		return(false);
	}
	
	return(true);
}

function isInteger(s){
    var i;

    if (isEmpty(s))
      if (isInteger.arguments.length == 1) return 0;
    else
      return (isInteger.arguments[1] == true);

    for(i = 0; i < s.length; i++){
       var c = s.charAt(i);

       if (!isDigit(c)) return false;
    }

    return true;
}

function isEmpty(s){
   return ((s == null) || (s.length == 0))
}

function isDigit(c){
   return ((c >= "0") && (c <= "9"))
}
function trimInputBox()
{
    var k = document.forms[0].elements.length;
    for(var i=0; i < k; i++) 
    {
       var elem = document.forms[0].elements[i];
       if (elem) {
           if (elem.type == 'textarea'|| elem.type=='text') {
              elem.value = elem.value.trim().trim();
           }  
       }
    }
}
