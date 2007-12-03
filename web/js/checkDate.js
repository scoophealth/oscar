
function check_date_for_oracle(checkedDate) 
{ 
	var date1 = convert_date(checkedDate);
	if(date1!=null) {	
		if(check_date_format1(date1) ) {						
			return check_date(date1);
		} else {
		return false;
		}
	} else {		
		return false;
	}
}		


function check_date(checkedDate) 
{	
	//eg. checkedDate = '21-09-2007'
	// Regular expression used to check if date is in correct format
   	//var pattern = new RegExp([0-3][0-9]-0|1[0-9]-19|20[0-9]{2});
   	//pattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;	 //'21-09-2007'
   	pattern = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;  //'2007-09-21'	
	if(checkedDate.match(pattern))
	{
      	var date_array = checkedDate.split('-');
      	var year = date_array[0];
      	// Attention! Javascript consider months in the range 0 - 11      	
      	var month = date_array[1] - 1;      		
      	var day = date_array[2];
		
		source_date = new Date(year,month,day);
		if(year != source_date.getFullYear() || day != source_date.getDate() || month != source_date.getMonth() )
      	{
         	alert('Date format is not valid!');
         	return false;
     	}	
    }
   	else
   	{
      	alert('Date format is not valid. The right date format is like 2007-10-16.');
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

