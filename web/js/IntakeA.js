

function redirectToClientSearch(demographicNo,startRowCount,path)
{
	document.intakeAForm.action= path + "?searchForClient=N";
	document.intakeAForm.submit();
}


    function onPrint() {
//        document.forms[0].submitForm.value="print";
//        var ret = checkAllDates();
//        if(ret==true)
//        {
//            ret = confirm("Do you wish to save this form and view the print preview?");
//        }
//        return ret;
        window.print();
    }
    function onSave() 
	{
        document.forms[0].submitForm.value="save";
        document.forms[0].method.value ='save';
        
//        document.forms[0].demographicNo.value="";
        
        var ret = checkAllDates();
		if(ret)
		{
			ret = checkAllIntegers();
		}
        if(ret)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
		
    }
    function onUpdate(path) 
	{
        document.forms[0].submitForm.value="update";
        document.forms[0].method.value ='update';
        var ret = checkAllDates();
		if(ret)
		{
			ret = checkAllIntegers();
		}
        if(ret)
        {
            ret = confirm("Are you sure you want to update this form?");
            if(ret)
            {
            	document.forms[0].action = path+"?actionType=update";
            	
            	document.forms[0].submit();
            	
            }
        }
    }
    
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submitForm.value="exit";
        document.forms[0].method.value ='save';
        var ret = checkAllDates();
		if(ret)
		{
			ret = checkAllIntegers();
		}
		
        if(ret)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
		return ret;
    }

    function popupPage(varpage) { //open a new popup window
      var page = "" + varpage;
      windowprops = "height=600,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
      var popup=window.open(page, "printlocation", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=3100;

    function isInteger(s)
	{
        var i;
//alert("isInteger: s =" + s);			
		
        for (i = 0; i < s.length; i++)
		{
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9")))
			{
				return false;
			}
        }
        // All characters are numbers.
        return true;
    }

    function isCurrency(s)
	{
        var i;
//alert("isInteger: s =" + s);			
		
        for (i = 0; i < s.length; i++)
		{
            // Check that current character is number.
            var c = s.charAt(i);
            if (  ((c < "0") || (c > "9"))  &&  c != "."  )
			{
				return false;
			}
        }
        // All characters are numbers.
        return true;
    }

    function stripCharsInBag(s, bag){
        var i;
        var returnString = "";
        // Search through string's characters one by one.
        // If character is not in bag, append to returnString.
        for (i = 0; i < s.length; i++){
            var c = s.charAt(i);
            if (bag.indexOf(c) == -1) returnString += c;
        }
        return returnString;
    }

    function daysInFebruary (year){
        // February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
    }
    function DaysArray(n) {
        for (var i = 1; i <= n; i++) {
            this[i] = 31
            if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
            if (i==2) {this[i] = 29}
       }
       return this
    }

    function isDate(dtStr){
        var daysInMonth = DaysArray(12)
        var pos1=dtStr.indexOf(dtCh)
        var pos2=dtStr.indexOf(dtCh,pos1+1)
        var strMonth=dtStr.substring(0,pos1)
        var strDay=dtStr.substring(pos1+1,pos2)
        var strYear=dtStr.substring(pos2+1)
        strYr=strYear
        if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
        if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
        for (var i = 1; i <= 3; i++) {
            if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
        }
        month=parseInt(strMonth)
        day=parseInt(strDay)
        year=parseInt(strYr)
        if (pos1==-1 || pos2==-1){
            return "format"
        }
        if (month<1 || month>12){
            return "month"
        }
        if (day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
            return "day"
        }
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            return "year"
        }
        if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
            return "date"
        }
    return true
    }


    function valDate(dateBox)
    {
        try
        {
            var dateString = dateBox.value;
            if(dateString == "")
            {
    //            alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[0];
            var m = dt[1];
            var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }
        catch (ex)
        {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }



	function checkAllIntegers()
	{
        var ret = true;
        if(!isInteger(document.forms[0].year.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].year.value = "";
			document.forms[0].year.focus();
            ret = false;
        }
		else if(!isCurrency(document.forms[0].amtReceived.value) )
		{
	        alert ("You must type in a valid currency in the field.");
		
			document.forms[0].amtReceived.value = "";
			document.forms[0].amtReceived.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].frequencyOfSeeingDoctor.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].frequencyOfSeeingDoctor.value = "";
			document.forms[0].frequencyOfSeeingDoctor.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].frequencyOfSeeingEmergencyRoomDoctor.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].frequencyOfSeeingEmergencyRoomDoctor.value = "";
			document.forms[0].frequencyOfSeeingEmergencyRoomDoctor.focus();
		
            ret = false;
        }
/*		
		else if(!isInteger(document.forms[0].lastIssueDate.value))
		{
            ret = false;
        }
*/
        return ret;
	}

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].assessDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].enterSeatonDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].dateOfReadmission)==false){
            b = false;
        }else
        if(valDate(document.forms[0].lastIssueDate)==false){
            b = false;
        }

        return b;
    }
	
	function clickCheckBox(fieldObj)
	{
		if(fieldObj.checked)
		{
			fieldObj.checked = false;
		}
		else if(!fieldObj.checked)
		{
			fieldObj.checked = true;
		}
	}	
	function clickRadio(fieldObj)
	{
		if(fieldObj.checked)
		{
			fieldObj.checked = false;
		}
		else if(!fieldObj.checked)
		{
			fieldObj.checked = true;
		}
	}	
	
