function deferedSubmit(methodName)
{
	if(deferSubmit) {
		if(methodName == '') {
			setTimeout("submitForm()", 200);
		}
		else
		{
			setTimeout("submitForm('" + methodName + "')", 200);
		}
	}
	else
	{
		if(methodName == '') {
			submitForm();
		}
		else
		{
			submitForm(methodName);
		}
	}
	return false;
}
function openDatePickerCalendar(url){
  	if(readOnly==true) return false;
  	cancelOnCalBlur();
  	if(win!=null) win.close();
  	win=window.open(url, '', 'width=310,height=310'); 
  	return false;
}
function onCalBlur(checkedDateName)
{
	isDateValid = true;
	deferSubmit = true;
	if(doOnBlur)
	{
	    timerId = setTimeout("check_date('" + checkedDateName + "')", 100);
	}
}

function cancelOnCalBlur()
{
	if(timerId > 0) clearTimeout(timerId);
	deferSubmit = false;
}

function onCalKeyPress(event, checkedDateName)
{
	var keynum;
	if(window.event) // IE
 	{
 		keynum = event.keyCode;
 	}
	else if(event.which) // Netscape/Firefox/Opera
 	{
 		keynum = event.which;
 	}
	if (keynum == 13) {
		if(!check_date(checkedDateName)) {
			if(event.stopPropagation) {
				event.stopPropagation();
			}
			else
			{
				event.cancelBubble=true;
			}
		}
	}
	else
	{
		var checkedDateObj = document.getElementsByName(checkedDateName)[0];
		checkedDateObj.style.backgroundColor='#ffffff';
	}
}

function setDate(form_name,element_name,year1,month1,day1) {
	  win.close();
  	  var dtElement = document.getElementsByName(element_name)[0];
      var val = getFormatedDate(year1,month1,day1);
      dtElement.value =  val;
	  dtElement.style.backgroundColor='#ffffff';
	  dtElement.focus();
}
function getFormatedDate(year1, month1,day1)
{
	// date format defined as YYYY/MM/DD
	var sM = month1;
	if (month1 < 10) sM = "0" + sM;
	var sD = day1;
	if (day1 < 10) sD = "0" + sD;
	return year1 + "/" + sM + "/" + sD;
}
function setInvalid(checkedDateObj)
{
		isDateValid = false;
		doOnBlur = false;
    	alert('Date entered is not valid.');
  	    checkedDateObj.style.backgroundColor='#ff0000';
		checkedDateObj.focus();
		doOnBlur = true;
}
function check_date(checkedDateName) 
{	
	// Regular expression used to check if date is in correct format
   	// pattern = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;	 //'2007-09-21'
   	
   	if(readOnly==true) return true;
   	var checkedDateObj = document.getElementsByName(checkedDateName)[0];
   	var checkedDate = checkedDateObj.value;
   	if(checkedDate==''){
   	   return true;
    }
    
   	if(checkedDate.length<8 || checkedDate.length>10){
   		setInvalid(checkedDateObj);
       	return false;
    }
   	 
   	pattern = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;  //'2007-09-21'	
	if(checkedDate.match(pattern))
	{
      	var date_array = checkedDate.split('/');
      	if (date_array.length == 1) {
      		date_array = checkedDate.split('-');
      	}
      	var year = date_array[0];
      	// Attention! Javascript consider months in the range 0 - 11      	
      	var month = date_array[1] - 1;      		
      	var day = date_array[2];
		
		source_date = new Date(year,month,day);
		if(year != source_date.getFullYear() || day != source_date.getDate() || month != source_date.getMonth() )
      	{
			setInvalid(checkedDateObj);
         	return false;
     	}	
    }
   	else
   	{
		setInvalid(checkedDateObj);
      	return false;
   	}
    return true;
}


function convert_date(convertedDate) {
	// from '21-Sep-2007' to '21-09-2007'
	var hasIt = false;
	for(i=0;i<convertedDate.length;i++) {
		if(convertedDate.charAt(i)== '-') {
			hasIt = true;
		}
	}
	if(!hasIt) {
		alert("Date format is not valid");
		return null;
	}
	
	var date_array = convertedDate.split('-');
    var day = date_array[0];
    var month = date_array[1].toUpperCase();
    var year = date_array[2];	
      	
	if(month =='JAN') month1= '01';
	else if(month =='FEB') month1 = '02';
	else if(month =='MAR') month1 = '03';
	else if(month =='APR') month1 = '04';
	else if(month =='MAY') month1 = '05';
	else if(month =='JUN') month1 = '06';
	else if(month =='JUL') month1 = '07';
	else if(month =='AUG') month1 = '08';
	else if(month =='SEP') month1 = '09';
	else if(month =='OCT') month1 = '10';
	else if(month =='NOV') month1 = '11';
	else if(month =='DEC') month1 = '12';
	else {
		alert("Date format is not valid.");
		return null;
	}
	var newDate = day+'-'+month1+'-'+year;
		
	return newDate;
}

function checkAndValidateDate(dateStr, datePattern) {
	var matchArray = null;
	
	if(datePattern != null && datePattern != "") {
		matchArray = dateStr.match(datePattern);
	} else {
		//default is yyyy-mm-dd
		var datePat = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;
		matchArray = dateStr.match(datePat);
	}
	if (matchArray == null) {
		alert("Please enter the date as yyyy-mm-dd. Your current selection reads: " + dateStr);
		return false;
	} else {
		var dateArr = dateStr.split('-');
		if(validateDate(dateArr[0], dateArr[1], dateArr[2])) {
			return true;
		} else {
			alert("Invalid Date");
			return false;
		}
	}
}
	
function check_date_format1(dateStr) {
	//eg. checkedDate = '21-09-2007'
	// Regular expression used to check if date is in correct format
   	
   	var datePat = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
	var matchArray = dateStr.match(datePat); // is the format ok?

	if (matchArray == null) {
		alert("Please enter the date as dd-mmm-yyyy. Your current selection reads: " + dateStr);
		return false;
	} else {	
		return true;
	}
}

function calculateAge(year, month, date) {
	 month = month - 1;
	
	 if (month != parseInt(month)) { alert('Type Month of birth in digits only!'); return false; }
	 if (date != parseInt(date)) { alert('Type Date of birth in digits only!'); return false; }
	 if (year != parseInt(year)) { alert('Type Year of birth in digits only!'); return false; }
	 if (year.length < 4) { alert('Type Year of birth in full!'); return false; }
	
	 today = new Date();
	 dateStr = today.getDate();
	 monthStr = today.getMonth();
	 yearStr = today.getFullYear();
	
	 var theYear=0;
	 theYear = yearStr - year;
	 theMonth = monthStr - month;
	 theDate = dateStr - date;
	
	 var days = "";
	 if (monthStr == 0 || monthStr == 2 || monthStr == 4 || monthStr == 6 || monthStr == 7 || monthStr == 9 || monthStr == 11) days = 31;
	 if (monthStr == 3 || monthStr == 5 || monthStr == 8 || monthStr == 10) days = 30;
	 if (monthStr == 1) days = 28;
	
	 theYear = theYear;
	
	 if (month < monthStr && date > dateStr) { theYear = theYear + 1;
	                                           theMonth = theMonth - 1; }
	 if (month < monthStr && date <= dateStr) { theMonth = theMonth; }
	 else if (month == monthStr && (date < dateStr || date == dateStr)) { theMonth = 0; }
	 else if (month == monthStr && date > dateStr) { theMonth = 11; }
	 else if (month > monthStr && date <= dateStr) { theYear = theYear - 1;
	                                                 theMonth = ((12 - -(theMonth)) + 1); }
	 else if (month > monthStr && date > dateStr) { theMonth = ((12 - -(theMonth))); }
	
	 if (date < dateStr) { theDate = theDate; }
	 else if (date == dateStr) { theDate = 0; }
	 else { theYear = theYear - 1; theDate = days - (-(theDate)); }
	 
	 return(theYear);
}

/* input in numbers according to iso number format */
function validateDate(year, month, day)
{
	var date=new Date();
/*
	// Updated by Eugene Petruhin on 01.04.2009 while fixing #2723507
	// Never ever do the following, always set all date parts at once!

	date.setFullYear(year);
	date.setMonth(month-1);
	date.setDate(day);

	// This construct depends on execution time's day and can result in a wrong month being set.
	// i.e. say today is 31st of March 2009, we pass (2009, September, 11) and date will be set to 2009 October 11.
	// This happens because today's day is 31st and September only has 30 days
	// so date is normalized to 2009 October 01 when setMonth() is called.
*/
	var iYear = parseInt(year);
	var iMonth = parseInt(month);
	var iDay = parseInt(day);

	date.setFullYear(iYear, iMonth-1, iDay);
	date.setMonth(iMonth-1);
	date.setDate(iDay);

        if (iYear!=date.getFullYear()) return(false);
        if (iMonth!=date.getMonth()+1) return(false);
        if (iDay!=date.getDate()) return(false);
	
	return(true);
}

function validateBirthDay(myDate){
	var date=new Date();
	var myDate_array=myDate.split("/");
	date.setFullYear(myDate_array[0], myDate_array[1]-1, myDate_array[2]);
	
	var today = new Date;
    if (date > today){
      alert('Date of birth must not be greater than current date.');
      return false;
    }

    date.setFullYear(date.getFullYear()+100, date.getMonth(), date.getDate());  
    if (date < today){   
      alert('Date of birth may not be older than 100 years.');
      return false;
    }
}
