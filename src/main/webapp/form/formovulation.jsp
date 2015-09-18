<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="oscar.form.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%
	String formClass = "Ovulation";
	String formLink = "formovulation.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
	
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Ovalation</title>
<link rel="stylesheet" type="text/css" href="formovulation.css" />
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<style type="text/css" media="print">
BODY {
	font-size: 85%;
}

TABLE {
	font-size: 85%;
}
</style>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<html:base />


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />



<script language="Javascript"><!--

var fieldObj;
function assignBackgroundColor(obj)
{
	var flagObj = null;
	var flagObjName = "flag_" + obj.name;
	flagObj = document.getElementsByName(flagObjName);

	if(flagObj[0].value == 'red')
	{
		obj.style.backgroundColor = 'red'; 
		obj.style.color = 'white';
	}
	else
	{
		obj.style.backgroundColor = 'white'; 
		obj.style.color = '#677677';
	}
}
function changeColor(obj)
{
	var flagObjName = "";
	var flagObj = null;
	if(obj.style.backgroundColor == 'white')
	{
		obj.style.backgroundColor = 'red';	
		obj.style.color = 'white';
		flagObjName = "flag_" + obj.name;
		flagObj = document.getElementsByName(flagObjName);
		flagObj[0].value = "red";
	}
	else
	{
		obj.style.backgroundColor = 'white';	
		obj.style.color = '#677677';
		flagObjName = "flag_" + obj.name;
		flagObj = document.getElementsByName(flagObjName);
		flagObj[0].value = "";
	}
}

function showHideBox(layerName, iState) 
{ // 1 visible, 0 hidden
    if(document.layers)	   //NN4+
    {
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById)	  //gecko(NN6) + IE 5+
    {
        var obj = document.getElementById(layerName);
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	// IE 4
    {
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
}
function showBox(layerName, iState, field, e) { // 1 visible, 0 hidden
    fieldObj = field;
    //get the number of the field
    fieldName = fieldObj.name;
    fieldName = fieldName.substring("pg2_pos".length);

    if(document.layers)	{   //NN4+
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById) {	  //gecko(NN6) + IE 5+
        var obj = document.getElementById(layerName);
        obj.style.top = e.screenY + (481-e.screenY + 26*fieldName);
        obj.style.left = "390px";
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	// IE 4
    {
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
}
function showBMIBox(layerName, iState, field, e) { // 1 visible, 0 hidden

    fieldObj = field;
    //get the number of the field
    fieldName = fieldObj.name;
    //fieldName = fieldName.substring("pg2_pos".length);

    if(document.layers)	{   //NN4+
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById) {	  //gecko(NN6) + IE 5+
        var obj = document.getElementById(layerName);
        obj.style.top = e.screenY + (401-e.screenY + 26*fieldName);
        obj.style.left = "30px";
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	// IE 4
    {
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
}
function showPGBox(layerName, iState, field, e, prefix, origX, origY, deltaY) { // 1 visible, 0 hidden
    fieldObj = field;
    //get the number of the field
    fieldName = fieldObj.name;
    fieldName = fieldName.substring(prefix.length);
    if (fieldName=="")
	{
    	fieldName=0;  
	}
//alert("Ovulation/showPGBox(): fieldName = " + fieldName);

    if(document.layers)	
	{   //NN4+
//alert("Ovulation/showPGBox(): document.layers = " + document.layers);
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } 
	else if(document.getElementById) 
	{	  //gecko(NN6) + IE 5+
	
//alert("Ovulation/showPGBox(): document.getElementById = " + document.getElementById);
//alert("Ovulation/showPGBox(): layerName = " + layerName);

        var obj = document.getElementById(layerName);
        obj.style.top = e.screenY + (origY-e.screenY + deltaY*fieldName);
        obj.style.left = origX;
        obj.style.visibility = iState ? "visible" : "hidden";
		
		obj.style.visibility = "visible";
		
//alert("Ovulation/showPGBox(): obj.style.visibility = " + obj.style.visibility);
		
    } 
	else if(document.all)
	{// IE 4
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
}
function insertBox(str, layerName) { // 1 visible, 0 hidden
    if(document.getElementById)	{
        //var obj = document.getElementById(field);
        fieldObj.value = str;
    }
    showHideBox(layerName, 0);
}
function showDef(str, field) { 
    if(document.getElementById)	{
        field.value = str;
    }
}
function syncDemo() { 
    document.forms[0].c_surname.value = "BRO'WN";
    document.forms[0].c_givenName.value = "PREGNANT";
    document.forms[0].c_address.value = "12 Mockingbird lane";
    document.forms[0].c_city.value = "Pemberton";
    document.forms[0].c_province.value = "BC";
    document.forms[0].c_postal.value = "V2S 1V9";
    document.forms[0].c_phn.value = "9069158251";
    document.forms[0].c_phone.value = "604-778-4593  ";
}


function wtEnglish2Metric(obj) {
	//if(isNumber(document.forms[0].c_ppWt) ) {
	//	weight = document.forms[0].c_ppWt.value;

	if(isNumber(obj) ) {
		weight = obj.value;
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;
		if(confirm("Are you sure you want to change " + weight + " pounds to " + weightM +"kg?") ) {
			//document.forms[0].c_ppWt.value = weightM;
			obj.value = weightM;
		}

	}
}
function htEnglish2Metric(obj) {

	height = obj.value;
	if(height.length > 1 && height.indexOf("'") > 0 ) {
		feet = height.substring(0, height.indexOf("'"));
		inch = height.substring(height.indexOf("'"));
		if(inch.length == 1) {
			inch = 0;
		} else {
			inch = inch.charAt(inch.length-1)=='"' ? inch.substring(0, inch.length-1) : inch;
			inch = inch.substring(1);
		}
		
		//if(isNumber(feet) && isNumber(inch) )
			height = Math.round((feet * 30.48 + inch * 2.54) * 10) / 10 ;
			if(confirm("Are you sure you want to change " + feet + " feet " + inch + " inch(es) to " + height +"cm?") ) {
				obj.value = height;
			}
		//}
	}

}
function calcBMIMetric(wt, ht, obj) {

	if(isNumber(wt) && isNumber(ht)) 
	{
		var weight = parseFloat(wt.value);
		var height = parseFloat(ht.value);

		height = height / 100;

		if(weight > 0  &&  height > 0) 
		{
			obj.value =  "" + Math.round(weight * 10 / height / height) / 10;
		}
	}
}

function calcTMC(volumeObj, densityObj, motilityObj, obj) {

	if(isNumber(volumeObj) && isNumber(densityObj)  &&  isNumber(motilityObj)) 
	{
		var volume = parseFloat(volumeObj.value);
		var density = parseFloat(densityObj.value);
		var motility = parseFloat(motilityObj.value);

		motility = motility / 100;

		if(volume > 0  &&  density > 0  &&  motility > 0) 
		{
//			obj.value =  "" +  Math.round(volume * density * motility);
			obj.value =  "" +  to2DecimalDigits(volume * density * motility);

		}
	}
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function  to2DecimalDigits(decimal)
{
// 649; 649.; 649.0; 649.23; 649.9000000000001
    var decimalDouble = 0.00;
	decimalDouble = decimal;
    var rtnStr = "";

	try
    {
        decimalDouble = (Math.round(decimalDouble * 1000)) / 1000.00;
        rtnStr = "" + decimalDouble;
    }
    catch(ex)
    {
        rtnStr = decimal;
    }


    if(decimal == null)
    {
        return "0.00"; 
    }

    var index = 0;
    
    index = rtnStr.indexOf("."); 

	var pos = rtnStr.length - index;

	if(pos == 3)
			; // in  xxx.xx format already
	else if(pos == 2)
			rtnStr += "0";
	else if(pos == 1)
			rtnStr += "00";
	else if(pos <= 0)
	{
		rtnStr += ".00";
	}
	else if(pos > 4)
	{
		rtnStr = rtnStr.substring(0,index+3);
	}

    return rtnStr;

}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    function onPrint_old() {
        window.print();
    }

    function onPrint() {
        document.forms[0].submit.value="print"; 
//alert("formovulation.jsp/onPrint(): submit.value = " + document.forms[0].submit.value);			
		
        var ret = checkAllDates();
        if(ret==true)
        {
//            document.forms[0].action = "../form/createpdf?__title=Ovulation+Form&__cfgfile=bcar1PrintCfgPg1&__template=bcar1";
//				document.all.FrmForm.action="../form/createpdf?__title=Invoice&__cfgfile=invoice&__template=invoice";

//            document.forms[0].action = "../form/createpdf?__title=Ovulation+Form&__cfgfile=ovulationPrintCfgPg1&__template=bcar1";
			  document.forms[0].action = "../form/createpdf?__title=Ovulation+Form&__cfgfile=ovulationPrintCfgPg1&__cfgfile=ovulationPrintCfgPg2&__template=OvulationForm_95";
//			  document.forms[0].action = "../form/formGrowthChartPrint.jsp?print=1&__title=GrowthCharts&__cfgfile=<//%=bGirl?"growthChartGirlPrint":"growthChartBoyPrint"%>&__cfgGraphicFile=<//%=bGirl?"growthChartGirlGraphic":"growthChartBoyGraphic"%>&__cfgGraphicFile=<//%=bGirl?"growthChartGirlGraphic2":"growthChartBoyGraphic2"%>&__template=<//%=bGirl?"growthChartGirlStatureWeight":"growthChartBoyStatureWeight"%>";
			  

//alert("formovulation.jsp/onPrint(): action = " + document.forms[0].action);			
            document.forms[0].target="_blank";            
        }
        return ret;
    }

    function onSave(urlPath) {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
//        ret = checkAllNumber();
        if(ret==true) {
            ret = confirm("Are you sure you want to save this form?");
			reset(urlPath);
        }
        return ret;
    }
    
    function onSaveExit(urlPath) {
        document.forms[0].submit.value="exit";
        var ret = true; //checkAllDates();
//        ret = checkAllNumber();
        if(ret == true) {
            ret = confirm("Are you sure you wish to save and close this window?");
            reset(urlPath);
        }
		
        return ret;
    }

    function reset(urlPath) {
        document.forms[0].target = "";
        document.forms[0].action = urlPath;
	}

	function isNumber(ss){
		var s = ss.value;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
			if (c == '.') {
				continue;
			} else if (((c < "0") || (c > "9"))) {
                alert('Invalid '+s+' in field ' + ss.name);
                ss.focus();
                return false;
			}
        }
        // All characters are numbers.
        return true;
    }
    function checkAllNumber() {
        var b = true;
        if(isNumber(document.forms[0].pg2_ht1)==false){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht2) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht3) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht4) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht5) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht6) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht7) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht8) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht9) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht10) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht11) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht12) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht13) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht14) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht15) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht16) ){
            b = false;
		} else if(!isNumber(document.forms[0].pg2_ht17) ){
            b = false;
		}
		return b;
	}

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=9900;

    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
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


    function checkTypeIn(obj) {
      if(!checkTypeNum(obj.value) ) {
          alert ("You must type in a number in the field.");
        }
    }

    function valDate(dateBox)
    {
        try
        {
            var dateString = dateBox.value;
            if(dateString == "")
            {
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[2];  var m = dt[1];  var d = dt[0];
            //var y = dt[0];  var m = dt[1];  var d = dt[2];
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

    function checkAllDates()
    {
        var b = true;
		
        if(valDate(document.forms[0].lmp)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date1)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date2)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date3)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date4)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date5)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date6)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date7)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date8)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date9)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date10)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date11)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date12)==false){
            b = false;
        }else
        if(valDate(document.forms[0].collectionDate)==false){
            b = false;
		}
        return b;
    }

	function calcWeek(source) {

	    var delta = 0;
        var str_date = getDateField(source.name);
        if (str_date.length < 10) return;
        //var yyyy = str_date.substring(0, str_date.indexOf("/"));
        //var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        //var dd = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        var dd = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var yyyy  = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        var check_date=new Date(yyyy,mm,dd);
        var start=new Date("December 25, 2003");

		if (check_date.getUTCHours() != start.getUTCHours()) {
			if (check_date.getUTCHours() > start.getUTCHours()) {
			    delta = -1 * 60 * 60 * 1000;
			} else {
			    delta = 1 * 60 * 60 * 1000;
			}
		} 

		var day = eval((check_date.getTime() - start.getTime() + delta) / (24*60*60*1000));
        var week = Math.floor(day/7);
		var weekday = day%7;
        source.value = week + "w+" + weekday;

}

	function getDateField(name) {
		var temp = ""; //pg2_gest1 - pg2_date1
		var n1 = name.substring(eval(name.indexOf("t")+1));

		if (n1>17) {
			name = "pg3_date" + n1;
		} else {
			name = "pg2_date" + n1;
		}
        
        for (var i =0; i <document.forms[0].elements.length; i++) {
            if (document.forms[0].elements[i].name == name) {
               return document.forms[0].elements[i].value;
    	    }
	    }
        return temp;
    }
function calToday(field) {
	var calDate=new Date();
	field.value = calDate.getDate() + '/' + (calDate.getMonth()+1) + '/' + calDate.getFullYear();
}

//-->
</script>
</head>

<body>
<html:form action="/form/formname">

	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />


	<div ID="ffDiv" class="demo">
	<table bgcolor='silver' width='100%'>
		<tr>
			<td align='right'><a href=#
				onclick="showHideBox('ffDiv',0); return false;">X</a></td>
		</tr>
		<tr>
			<td><a href=# onclick="insertBox('-', 'ffDiv'); return false;">-</a></td>
		</tr>
		<tr>
			<td><a href=# onclick="insertBox('+', 'ffDiv'); return false;">+</a></td>
		</tr>
		<tr>
			<td><a href=# onclick="insertBox('++', 'ffDiv'); return false;">++</a></td>
		</tr>
		<tr>
			<td><a href=# onclick="insertBox('+++', 'ffDiv'); return false;">+++</a></td>
		</tr>
		<tr>
			<td><a href=#
				onclick="insertBox('++++', 'ffDiv'); return false;">++++</a></td>
		</tr>
	</table>
	</div>

	<div ID="BMIdiv" class="demo2">
	<table bgcolor='#007FFF' width='99%'>
		<tr>
			<th align='right'><a href=#
				onclick="showHideBox('BMIdiv',0); return false;"><font
				color="red">X</font></a></th>
		</tr>
		<tr>
			<td><a href=# onclick="showHideBox('BMIdiv',0); return false;">
			<font color="#66FF66">The height and weight MUST be in metric
			for the BMI to calculate when you double click in the shaded cell. <br>
			If putting in weight or height in Standard measurement, double click
			each cell to convert to metric. Then, double click in the BMI cell to
			calculate. Do not put any text in the height or weight cells (kg.) or
			it will not calculate the BMI.</font><br>
			&nbsp;</a></td>
		</tr>
	</table>
	</div>


	<table>
		<tr>
			<td height="15" class="style76">&nbsp;</td>
		</tr>
	</table>

	<table align="center" width="90%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td valign="top">
			<table align="center" width="100%" border="1" cellspacing="1"
				cellpadding="1">
				<tr>

					<td width="60%" height="232" valign="top">
					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr valign="top">


							<td width="81%" height="93" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="72%" height="52" valign="top" class="style76">
									Ovulation Form
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									HIN:&nbsp; <%=props.getProperty("healthNum", "")%> <input
										type="hidden" name="healthNum"
										value="<%=props.getProperty("healthNum", "")%>"> <br>
									<br>
									First Name:<input type="text" size="12" name="clientFirstName"
										value="<%=props.getProperty("clientFirstName", "")%>"
										class="style71" readonly> Last:<input type="text"
										size="12" name="clientLastName"
										value="<%=props.getProperty("clientLastName", "")%>"
										class="style71" readonly> &nbsp;&nbsp;&nbsp; Age: <input
										type="text" size="4" name="dob"
										value="<%=props.getProperty("dob", "")%>" class="style71"
										readonly></td>
								</tr>
								<tr>

									<td height="29" colspan="2" class="style76">Work: <input
										type="text" name="workPhone" size="12"
										value="<%=props.getProperty("workPhone", "")%>"
										class="style71">&nbsp;&nbsp; Home: <input type="text"
										name="homePhone" size="12"
										value="<%=props.getProperty("homePhone", "")%>"
										class="style71">&nbsp;&nbsp; Other: <input type="text"
										name="otherPhone" size="12"
										value="<%=props.getProperty("otherPhone", "")%>"
										class="style71">&nbsp;&nbsp;</td>
								</tr>
							</table>
							</td>
							<td width="19%" valign="top">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr valign="top">
									<td width="218" valign="top" class="style76">Diagnosis</td>
								</tr>
								<tr>

									<td height="72" valign="top"><textarea name="diagnosis"
										cols="17" rows="4" class="style71"><%=props.getProperty("diagnosis", "")%></textarea>
									</td>
								</tr>

							</table>
							</td>
						</tr>

					</table>

					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr valign="top">


							<td width="72%" height="133" valign="top">
							<table width="100%" border="0">
								<tr>


									<td width="85%" height="129" valign="top" class="style76">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td colspan="5" class="style76">WH: BMI:
											LMP:&nbsp;&nbsp; <input type="text" name="lmp" size="12"
												value="<%=props.getProperty("lmp", "")%>" class="style71"
												onDblClick="calToday(this)">&nbsp; (dd/mm/yyyy)</td>
										</tr>
										<tr>

											<td width="16%" height="112" valign="top" class="style76">
											<table width="100%" border="1" cellpadding="0"
												cellspacing="0">
												<tr>

													<td width="16%" height="22" class="style76">Diag</td>

													<td width="84%" height="22" class="style76"><input
														type="text" size="5" name="diag"
														value="<%=props.getProperty("diag", "")%>" class="style71">
													</td>
												</tr>
												<tr>

													<td height="22" class="style76">Femara</td>

													<td height="22" class="style76"><input type="text"
														size="5" name="femara"
														value="<%=props.getProperty("femara", "")%>"
														class="style71"></td>
												</tr>
												<tr>

													<td height="22" class="style76">Tamoxi</td>

													<td height="22" class="style76"><input type="text"
														size="5" name="tamoxi"
														value="<%=props.getProperty("tamoxi", "")%>"
														class="style71"></td>
												</tr>

												<tr>

													<td height="22" class="style76">CC/Nol</td>

													<td height="22" class="style76"><input type="text"
														size="5" name="ccNol"
														value="<%=props.getProperty("ccNol", "")%>"
														class="style71"></td>
												</tr>
												<tr>

													<td height="22" class="style76">Gonad</td>

													<td height="22" class="style76"><input type="text"
														size="5" name="gonad"
														value="<%=props.getProperty("gonad", "")%>"
														class="style71"></td>
												</tr>
											</table>
											</td>
											<td width="14%" valign="top">
											<table width="100%" border="1" cellspacing="0"
												cellpadding="0">
												<tr>

													<td width="39%" height="32" align="center" class="style76">
													Metfor<br>
													Min</td>

													<td width="61%" height="32" class="style76"><input
														type="text" size="5" name="metForMin"
														value="<%=props.getProperty("metForMin", "")%>"
														class="style71"></td>
												</tr>
												<tr>

													<td height="34" align="center" class="style76">Parlod<br>
													El</td>

													<td height="34" class="style76"><input type="text"
														size="5" name="parlodEl"
														value="<%=props.getProperty("parlodEl", "")%>"
														class="style71"></td>
												</tr>
												<tr>

													<td height="38" align="center" class="style76">Folic<br>
													Acid</td>

													<td height="38" class="style76"><input type="text"
														size="5" name="folicAcid"
														value="<%=props.getProperty("folicAcid", "")%>"
														class="style71"></td>
												</tr>
											</table>
											</td>
											<td width="28%" valign="top">
											<table width="100%" height="105" border="1" cellpadding="0"
												cellspacing="0">
												<tr>

													<td height="41" class="style76">Ovul/Hcg <br>
													<input type="text" size="12" name="ovul"
														value="<%=props.getProperty("ovul", "")%>" class="style71"></td>
												</tr>
												<tr>

													<td height="48" class="style76">Post Ov/Progest <br>
													<input type="text" size="12" name="postOv"
														value="<%=props.getProperty("postOv", "")%>"
														class="style71"></td>
												</tr>
											</table>
											</td>
											<td width="24%" valign="top">
											<table width="99%" border="1" cellspacing="0" cellpadding="0">
												<tr>

													<td height="24" class="style76">Side Effects this
													cycle</td>
												</tr>
												<tr>

													<td height="78" valign="top" class="style76"><textarea
														name="sideEffects" cols="17" rows="4" class="style71"><%=props.getProperty("sideEffects", "")%></textarea>
													</td>
												</tr>
											</table>
											</td>
											<td width="18%" valign="top">
											<table width="100%" height="106" border="1" cellpadding="0"
												cellspacing="0">
												<tr>

													<td height="40" class="style76">IUI <input type="text"
														size="12" name="iui"
														value="<%=props.getProperty("iui", "")%>" class="style71"></td>
												</tr>
												<tr>

													<td height="60" class="style76">TDI <input type="text"
														size="12" name="tdi"
														value="<%=props.getProperty("tdi", "")%>" class="style71">
													</td>
												</tr>
											</table>
											</td>
										</tr>
									</table>
									</td>
								</tr>

							</table>
							</td>
						</tr>

					</table>

					</td>
					<td width="17%" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr valign="top">
							<td valign="top" class="style76">Abnormal Results</td>
						</tr>
						<tr>
							<td valign="top" class="style76"><textarea
								name="abnormalResult" cols="27" rows="6" class="style71"><%=props.getProperty("abnormalResult", "")%></textarea>
							Previous Cycles: <textarea name="previousCycles" cols="27"
								rows="4" class="style71"><%=props.getProperty("previousCycles", "")%></textarea>
							</td>
						</tr>
					</table>
					</td>
					<td width="23%" valign="top">
					<table width="100%" border="0" cellspacing="1" cellpadding="1">
						<tr>

							<td height="23" class="style76">Semen Analysis</td>
						</tr>

						<tr>
							<td class="style76">Volume: <input type="text"
								name="semenVolume" size="12"
								value="<%=props.getProperty("semenVolume", "")%>"
								class="style71"></td>
						</tr>

						<tr>
							<td class="style76">P. Motility: <input type="text"
								name="semenPMotility" size="12"
								value="<%=props.getProperty("semenPMotility", "")%>"
								class="style71"></td>
						</tr>

						<tr>
							<td class="style76">T. Motility: <input type="text"
								name="semenTMotility" size="12"
								value="<%=props.getProperty("semenTMotility", "")%>"
								class="style71"></td>
						</tr>

						<tr>
							<td class="style76">Morphology: <input type="text"
								name="semenMorphology" size="12"
								value="<%=props.getProperty("semenMorphology", "")%>"
								class="style71"></td>
						</tr>

						<tr>
							<td class="style76">Concentration: <input type="text"
								name="semenConcentration" size="12"
								value="<%=props.getProperty("semenConcentration", "")%>"
								class="style71"></td>
						</tr>

						<tr>
							<td class="style76">Viable Sperm: <input type="text"
								name="semenSperm" size="12"
								value="<%=props.getProperty("semenSperm", "")%>" class="style71">
							</td>
						</tr>
						<tr>
							<td class="style76">Sperm PH: <input type="text"
								name="semenSpermPh" size="12"
								value="<%=props.getProperty("semenSpermPh", "")%>"
								class="style71"></td>
						</tr>

					</table>
					</td>

				</tr>
			</table>
			<br>
			<table width="100%" align="center" border="1" cellspacing="1"
				cellpadding="1">
				<tr>
					<td colspan="5" rowspan="2" valign="middle" class="style76"><b>*****DAY
					3 FSH:</b> <input type="text" size="12" name="fsh"
						value="<%=props.getProperty("fsh", "")%>" class="style71">
					</td>
					<td colspan="2" rowspan="2" align="center" class="style76"><b><i>RIGHT
					OVARY</i></b></td>
					<td colspan="2" rowspan="2" align="center" class="style76"><b><i>LEFT
					OVARY</i></b></td>
					<td colspan="4" rowspan="2" class="style76">CURRENT CYCLE:</td>
				</tr>
				<tr>
				</tr>
				<tr>
					<td width="5%" rowspan="2" align="center" class="style76"><b>DATE<br>
					(dd/mm/yyyy)</b></td>
					<td width="6%" rowspan="2" align="center" class="style76"><b>DAY</b></td>
					<td width="3%" rowspan="2" align="center" class="style76">E2<br>
					LH</td>
					<td width="3%" rowspan="2" align="center" class="style76">TSH<br>
					PRL</td>
					<td width="5%" rowspan="2" align="center" class="style76">PROG<br>
					BHOG</td>
					<td width="11%" rowspan="2" align="center" class="style76">>LO</td>
					<td width="5%" rowspan="2" align="center" class="style76">CYST<br>
					#FOLL</td>
					<td width="11%" rowspan="2" align="center" class="style76">>LO</td>
					<td width="5%" rowspan="2" align="center" class="style76">CYST<br>
					#FOLL</td>
					<td width="7%" rowspan="2" align="center" class="style76">FF</td>
					<td width="3%" rowspan="2" align="center" class="style76">ET<br>
					TEX</td>
					<td width="11%" rowspan="2" align="center" class="style76">MEDS/DOSE</td>
					<td width="24%" rowspan="2" align="center" class="style76">DR'S
					COMMENTS &<br>
					INSTRUCTIONS</td>
				</tr>
				<tr>
					<td width="1%"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date1" size="7"
						value="<%=props.getProperty("date1", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day1" class="style71">
						<option value="00"
							<%=props.getProperty("day1", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day1", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day1", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day1", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day1", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day1", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day1", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day1", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day1", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day1", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day1", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day1", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day1", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day1", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day1", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day1", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day1", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day1", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day1", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day1", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day1", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day1", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day1", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day1", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day1", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day1", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day1", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day1", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day1", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day1", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day1", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day1", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day1", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day1", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day1", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day1", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day1", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day1", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day1", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day1", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day1", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day1", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day1", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day1", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day1", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day1", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day1", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day1", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day1", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day1", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day1", "").equals("50")?"selected":""%>>50</option>

					</select></td>
					<td height="36" align="center" class="style76"><input
						type="text" name="e2Lh1a" size="3"
						value="<%=props.getProperty("e2Lh1a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh1a"
						value="<%=props.getProperty("flag_e2Lh1a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh1a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl1a" size="3"
						value="<%=props.getProperty("tshPrl1a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl1a"
						value="<%=props.getProperty("flag_tshPrl1a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl1a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog1a" size="3"
						value="<%=props.getProperty("progBhog1a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog1a"
						value="<%=props.getProperty("flag_progBhog1a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog1a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo1" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo1", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst1a" size="5"
						value="<%=props.getProperty("rightCyst1a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo1" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo1", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst1a" size="5"
						value="<%=props.getProperty("leftCyst1a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff1" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff1", "")%>"
						onDblClick="showPGBox('ffDiv',1, this, event, 'ff1', 500, 423, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX1a" size="5"
						value="<%=props.getProperty("etTEX1a", "")%>" class="style71">
					</td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds1" cols="12" rows="2" class="style71"><%=props.getProperty("meds1", "")%></textarea>
					</td>
					<td rowspan="2" class="style76"><textarea name="comment1"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment1", "")%></textarea>
					</td>
				</tr>
				<tr>
					<td height="28" align="center" class="style76"><input
						type="text" name="e2Lh1b" size="3"
						value="<%=props.getProperty("e2Lh1b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh1b"
						value="<%=props.getProperty("flag_e2Lh1b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh1b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl1b" size="3"
						value="<%=props.getProperty("tshPrl1b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl1b"
						value="<%=props.getProperty("flag_tshPrl1b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl1b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog1b" size="3"
						value="<%=props.getProperty("progBhog1b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog1b"
						value="<%=props.getProperty("flag_progBhog1b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog1b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst1b" size="5"
						value="<%=props.getProperty("rightCyst1b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst1b" size="5"
						value="<%=props.getProperty("leftCyst1b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX1b" size="5"
						value="<%=props.getProperty("etTEX1b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date2" size="7"
						value="<%=props.getProperty("date2", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day2" class="style71">
						<option value="00"
							<%=props.getProperty("day2", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day2", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day2", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day2", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day2", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day2", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day2", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day2", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day2", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day2", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day2", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day2", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day2", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day2", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day2", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day2", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day2", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day2", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day2", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day2", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day2", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day2", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day2", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day2", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day2", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day2", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day2", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day2", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day2", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day2", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day2", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day2", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day2", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day2", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day2", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day2", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day2", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day2", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day2", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day2", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day2", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day2", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day2", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day2", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day2", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day2", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day2", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day2", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day2", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day2", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day2", "").equals("50")?"selected":""%>>50</option>

					</select></td>
					<td height="37" align="center" class="style76"><input
						type="text" name="e2Lh2a" size="3"
						value="<%=props.getProperty("e2Lh2a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh2a"
						value="<%=props.getProperty("flag_e2Lh2a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh2a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl2a" size="3"
						value="<%=props.getProperty("tshPrl2a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl2a"
						value="<%=props.getProperty("flag_tshPrl2a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl2a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog2a" size="3"
						value="<%=props.getProperty("progBhog2a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog2a"
						value="<%=props.getProperty("flag_progBhog2a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog2a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo2" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo2", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst2a" size="5"
						value="<%=props.getProperty("rightCyst2a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo2" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo2", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst2a" size="5"
						value="<%=props.getProperty("leftCyst2a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff2" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff2", "")%>"
						onDblClick="showPGBox('ffDiv',2, this, event, 'ff2', 500, 423, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX2a" size="5"
						value="<%=props.getProperty("etTEX2a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds2" cols="12" rows="2" class="style71"><%=props.getProperty("meds2", "")%></textarea>
					</td>
					<td rowspan="2" class="style76"><textarea name="comment2"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment2", "")%></textarea></td>
				</tr>
				<tr>
					<td height="26" align="center" class="style76"><input
						type="text" name="e2Lh2b" size="3"
						value="<%=props.getProperty("e2Lh2b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh2b"
						value="<%=props.getProperty("flag_e2Lh2b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh2b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl2b" size="3"
						value="<%=props.getProperty("tshPrl2b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl2b"
						value="<%=props.getProperty("flag_tshPrl2b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl2b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog2b" size="3"
						value="<%=props.getProperty("progBhog2b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog2b"
						value="<%=props.getProperty("flag_progBhog2b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog2b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst2b" size="5"
						value="<%=props.getProperty("rightCyst2b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst2b" size="5"
						value="<%=props.getProperty("leftCyst2b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX2b" size="5"
						value="<%=props.getProperty("etTEX2b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date3" size="7"
						value="<%=props.getProperty("date3", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day3" class="style71">
						<option value="00"
							<%=props.getProperty("day3", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day3", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day3", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day3", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day3", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day3", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day3", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day3", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day3", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day3", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day3", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day3", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day3", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day3", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day3", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day3", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day3", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day3", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day3", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day3", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day3", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day3", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day3", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day3", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day3", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day3", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day3", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day3", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day3", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day3", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day3", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day3", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day3", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day3", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day3", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day3", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day3", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day3", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day3", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day3", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day3", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day3", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day3", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day3", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day3", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day3", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day3", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day3", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day3", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day3", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day3", "").equals("50")?"selected":""%>>50</option>

					</select></td>
					<td height="35" align="center" class="style76"><input
						type="text" name="e2Lh3a" size="3"
						value="<%=props.getProperty("e2Lh3a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh3a"
						value="<%=props.getProperty("flag_e2Lh3a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh3a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl3a" size="3"
						value="<%=props.getProperty("tshPrl3a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl3a"
						value="<%=props.getProperty("flag_tshPrl3a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl3a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog3a" size="3"
						value="<%=props.getProperty("progBhog3a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog3a"
						value="<%=props.getProperty("flag_progBhog3a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog3a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo3" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo3", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst3a" size="5"
						value="<%=props.getProperty("rightCyst3a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo3" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo3", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst3a" size="5"
						value="<%=props.getProperty("leftCyst3a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff3" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff3", "")%>"
						onDblClick="showPGBox('ffDiv',3, this, event, 'ff3', 500, 423, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX3a" size="5"
						value="<%=props.getProperty("etTEX3a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds3" cols="12" rows="2" class="style71"><%=props.getProperty("meds3", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment3"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment3", "")%></textarea></td>
				</tr>
				<tr>
					<td height="26" align="center" class="style76"><input
						type="text" name="e2Lh3b" size="3"
						value="<%=props.getProperty("e2Lh3b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh3b"
						value="<%=props.getProperty("flag_e2Lh3b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh3b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl3b" size="3"
						value="<%=props.getProperty("tshPrl3b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl3b"
						value="<%=props.getProperty("flag_tshPrl3b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl3b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog3b" size="3"
						value="<%=props.getProperty("progBhog3b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog3b"
						value="<%=props.getProperty("flag_progBhog3b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog3b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst3b" size="5"
						value="<%=props.getProperty("rightCyst3b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst3b" size="5"
						value="<%=props.getProperty("leftCyst3b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX3b" size="5"
						value="<%=props.getProperty("etTEX3b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date4" size="7"
						value="<%=props.getProperty("date4", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day4" class="style71">
						<option value="00"
							<%=props.getProperty("day4", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day4", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day4", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day4", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day4", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day4", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day4", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day4", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day4", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day4", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day4", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day4", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day4", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day4", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day4", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day4", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day4", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day4", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day4", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day4", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day4", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day4", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day4", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day4", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day4", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day4", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day4", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day4", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day4", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day4", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day4", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day4", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day4", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day4", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day4", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day4", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day4", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day4", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day4", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day4", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day4", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day4", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day4", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day4", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day4", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day4", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day4", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day4", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day4", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day4", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day4", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="34" align="center" class="style76"><input
						type="text" name="e2Lh4a" size="3"
						value="<%=props.getProperty("e2Lh4a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh4a"
						value="<%=props.getProperty("flag_e2Lh4a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh4a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl4a" size="3"
						value="<%=props.getProperty("tshPrl4a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl4a"
						value="<%=props.getProperty("flag_tshPrl4a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl4a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog4a" size="3"
						value="<%=props.getProperty("progBhog4a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog4a"
						value="<%=props.getProperty("flag_progBhog4a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog4a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo4" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo4", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst4a" size="5"
						value="<%=props.getProperty("rightCyst4a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo4" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo4", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst4a" size="5"
						value="<%=props.getProperty("leftCyst4a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff4" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff4", "")%>"
						onDblClick="showPGBox('ffDiv',4, this, event, 'ff4', 500, 623, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX4a" size="5"
						value="<%=props.getProperty("etTEX4a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds4" cols="12" rows="2" class="style71"><%=props.getProperty("meds4", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment4"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment4", "")%></textarea></td>
				</tr>
				<tr>
					<td align="center" class="style76"><input type="text"
						name="e2Lh4b" size="3"
						value="<%=props.getProperty("e2Lh4b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh4b"
						value="<%=props.getProperty("flag_e2Lh4b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh4b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl4b" size="3"
						value="<%=props.getProperty("tshPrl4b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl4b"
						value="<%=props.getProperty("flag_tshPrl4b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl4b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog4b" size="3"
						value="<%=props.getProperty("progBhog4b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog4b"
						value="<%=props.getProperty("flag_progBhog4b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog4b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst4b" size="5"
						value="<%=props.getProperty("rightCyst4b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst4b" size="5"
						value="<%=props.getProperty("leftCyst4b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX4b" size="5"
						value="<%=props.getProperty("etTEX4b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date5" size="7"
						value="<%=props.getProperty("date5", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day5" class="style71">
						<option value="00"
							<%=props.getProperty("day5", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day5", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day5", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day5", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day5", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day5", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day5", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day5", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day5", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day5", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day5", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day5", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day5", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day5", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day5", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day5", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day5", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day5", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day5", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day5", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day5", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day5", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day5", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day5", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day5", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day5", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day5", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day5", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day5", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day5", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day5", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day5", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day5", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day5", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day5", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day5", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day5", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day5", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day5", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day5", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day5", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day5", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day5", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day5", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day5", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day5", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day5", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day5", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day5", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day5", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day5", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="38" align="center" class="style76"><input
						type="text" name="e2Lh5a" size="3"
						value="<%=props.getProperty("e2Lh5a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh5a"
						value="<%=props.getProperty("flag_e2Lh5a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh5a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl5a" size="3"
						value="<%=props.getProperty("tshPrl5a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl5a"
						value="<%=props.getProperty("flag_tshPrl5a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl5a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog5a" size="3"
						value="<%=props.getProperty("progBhog5a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog5a"
						value="<%=props.getProperty("flag_progBhog5a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog5a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo5" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo5", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst5a" size="5"
						value="<%=props.getProperty("rightCyst5a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo5" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo5", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst5a" size="5"
						value="<%=props.getProperty("leftCyst5a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff5" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff5", "")%>"
						onDblClick="showPGBox('ffDiv',5, this, event, 'ff5', 500, 623, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX5a" size="5"
						value="<%=props.getProperty("etTEX5a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds5" cols="12" rows="2" class="style71"><%=props.getProperty("meds5", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment5"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment5", "")%></textarea></td>
				</tr>
				<tr>
					<td align="center" class="style76"><input type="text"
						name="e2Lh5b" size="3"
						value="<%=props.getProperty("e2Lh5b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh5b"
						value="<%=props.getProperty("flag_e2Lh5b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh5b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl5b" size="3"
						value="<%=props.getProperty("tshPrl5b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl5b"
						value="<%=props.getProperty("flag_tshPrl5b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl5b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog5b" size="3"
						value="<%=props.getProperty("progBhog5b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog5b"
						value="<%=props.getProperty("flag_progBhog5b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog5b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst5b" size="5"
						value="<%=props.getProperty("rightCyst5b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst5b" size="5"
						value="<%=props.getProperty("leftCyst5b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX5b" size="5"
						value="<%=props.getProperty("etTEX5b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date6" size="7"
						value="<%=props.getProperty("date6", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day6" class="style71">
						<option value="00"
							<%=props.getProperty("day6", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day6", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day6", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day6", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day6", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day6", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day6", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day6", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day6", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day6", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day6", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day6", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day6", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day6", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day6", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day6", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day6", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day6", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day6", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day6", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day6", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day6", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day6", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day6", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day6", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day6", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day6", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day6", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day6", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day6", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day6", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day6", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day6", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day6", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day6", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day6", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day6", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day6", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day6", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day6", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day6", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day6", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day6", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day6", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day6", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day6", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day6", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day6", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day6", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day6", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day6", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="34" align="center" class="style76"><input
						type="text" name="e2Lh6a" size="3"
						value="<%=props.getProperty("e2Lh6a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh6a"
						value="<%=props.getProperty("flag_e2Lh6a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh6a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl6a" size="3"
						value="<%=props.getProperty("tshPrl6a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl6a"
						value="<%=props.getProperty("flag_tshPrl6a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl6a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog6a" size="3"
						value="<%=props.getProperty("progBhog6a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog6a"
						value="<%=props.getProperty("flag_progBhog6a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog6a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo6" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo6", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst6a" size="5"
						value="<%=props.getProperty("rightCyst6a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo6" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo6", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst6a" size="5"
						value="<%=props.getProperty("leftCyst6a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff6" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff6", "")%>"
						onDblClick="showPGBox('ffDiv',6, this, event, 'ff6', 500, 623, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX6a" size="5"
						value="<%=props.getProperty("etTEX6a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds6" cols="12" rows="2" class="style71"><%=props.getProperty("meds6", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment6"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment6", "")%></textarea></td>
				</tr>
				<tr>
					<td align="center" class="style76"><input type="text"
						name="e2Lh6b" size="3"
						value="<%=props.getProperty("e2Lh6b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh6b"
						value="<%=props.getProperty("flag_e2Lh6b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh6b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl6b" size="3"
						value="<%=props.getProperty("tshPrl6b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl6b"
						value="<%=props.getProperty("flag_tshPrl6b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl6b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog6b" size="3"
						value="<%=props.getProperty("progBhog6b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog6b"
						value="<%=props.getProperty("flag_progBhog6b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog6b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst6b" size="5"
						value="<%=props.getProperty("rightCyst6b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst6b" size="5"
						value="<%=props.getProperty("leftCyst6b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX6b" size="5"
						value="<%=props.getProperty("etTEX6b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date7" size="7"
						value="<%=props.getProperty("date7", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day7" class="style71">
						<option value="00"
							<%=props.getProperty("day7", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day7", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day7", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day7", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day7", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day7", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day7", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day7", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day7", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day7", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day7", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day7", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day7", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day7", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day7", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day7", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day7", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day7", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day7", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day7", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day7", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day7", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day7", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day7", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day7", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day7", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day7", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day7", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day7", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day7", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day7", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day7", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day7", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day7", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day7", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day7", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day7", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day7", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day7", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day7", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day7", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day7", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day7", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day7", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day7", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day7", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day7", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day7", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day7", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day7", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day7", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="36" align="center" class="style76"><input
						type="text" name="e2Lh7a" size="3"
						value="<%=props.getProperty("e2Lh7a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh7a"
						value="<%=props.getProperty("flag_e2Lh7a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh7a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl7a" size="3"
						value="<%=props.getProperty("tshPrl7a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl7a"
						value="<%=props.getProperty("flag_tshPrl7a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl7a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog7a" size="3"
						value="<%=props.getProperty("progBhog7a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog7a"
						value="<%=props.getProperty("flag_progBhog7a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog7a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo7" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo7", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst7a" size="5"
						value="<%=props.getProperty("rightCyst7a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo7" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo7", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst7a" size="5"
						value="<%=props.getProperty("leftCyst7a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff7" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff7", "")%>"
						onDblClick="showPGBox('ffDiv',7, this, event, 'ff7', 500, 823, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX7a" size="5"
						value="<%=props.getProperty("etTEX7a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds7" cols="12" rows="2" class="style71"><%=props.getProperty("meds7", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment7"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment7", "")%></textarea></td>
				</tr>
				<tr>
					<td height="27" align="center" class="style76"><input
						type="text" name="e2Lh7b" size="3"
						value="<%=props.getProperty("e2Lh7b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh7b"
						value="<%=props.getProperty("flag_e2Lh7b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh7b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl7b" size="3"
						value="<%=props.getProperty("tshPrl7b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl7b"
						value="<%=props.getProperty("flag_tshPrl7b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl7b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog7b" size="3"
						value="<%=props.getProperty("progBhog7b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog7b"
						value="<%=props.getProperty("flag_progBhog7b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog7b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst7b" size="5"
						value="<%=props.getProperty("rightCyst7b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst7b" size="5"
						value="<%=props.getProperty("leftCyst7b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX7b" size="5"
						value="<%=props.getProperty("etTEX7b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date8" size="7"
						value="<%=props.getProperty("date8", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day8" class="style71">
						<option value="00"
							<%=props.getProperty("day8", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day8", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day8", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day8", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day8", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day8", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day8", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day8", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day8", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day8", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day8", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day8", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day8", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day8", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day8", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day8", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day8", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day8", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day8", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day8", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day8", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day8", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day8", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day8", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day8", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day8", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day8", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day8", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day8", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day8", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day8", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day8", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day8", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day8", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day8", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day8", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day8", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day8", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day8", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day8", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day8", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day8", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day8", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day8", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day8", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day8", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day8", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day8", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day8", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day8", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day8", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="34" align="center" class="style76"><input
						type="text" name="e2Lh8a" size="3"
						value="<%=props.getProperty("e2Lh8a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh8a"
						value="<%=props.getProperty("flag_e2Lh8a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh8a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl8a" size="3"
						value="<%=props.getProperty("tshPrl8a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl8a"
						value="<%=props.getProperty("flag_tshPrl8a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl8a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog8a" size="3"
						value="<%=props.getProperty("progBhog8a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog8a"
						value="<%=props.getProperty("flag_progBhog8a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog8a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo8" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo8", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst8a" size="5"
						value="<%=props.getProperty("rightCyst8a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo8" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo8", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst8a" size="5"
						value="<%=props.getProperty("leftCyst8a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff8" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff8", "")%>"
						onDblClick="showPGBox('ffDiv',8, this, event, 'ff8', 500, 823, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX8a" size="5"
						value="<%=props.getProperty("etTEX8a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds8" cols="12" rows="2" class="style71"><%=props.getProperty("meds8", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment8"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment8", "")%></textarea></td>
				</tr>
				<tr>
					<td align="center" class="style76"><input type="text"
						name="e2Lh8b" size="3"
						value="<%=props.getProperty("e2Lh8b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh8b"
						value="<%=props.getProperty("flag_e2Lh8b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh8b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl8b" size="3"
						value="<%=props.getProperty("tshPrl8b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl8b"
						value="<%=props.getProperty("flag_tshPrl8b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl8b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog8b" size="3"
						value="<%=props.getProperty("progBhog8b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog8b"
						value="<%=props.getProperty("flag_progBhog8b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog8b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst8b" size="5"
						value="<%=props.getProperty("rightCyst8b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst8b" size="5"
						value="<%=props.getProperty("leftCyst8b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX8b" size="5"
						value="<%=props.getProperty("etTEX8b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date9" size="7"
						value="<%=props.getProperty("date9", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day9" class="style71">
						<option value="00"
							<%=props.getProperty("day9", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day9", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day9", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day9", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day9", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day9", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day9", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day9", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day9", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day9", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day9", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day9", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day9", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day9", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day9", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day9", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day9", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day9", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day9", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day9", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day9", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day9", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day9", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day9", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day9", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day9", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day9", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day9", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day9", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day9", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day9", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day9", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day9", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day9", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day9", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day9", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day9", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day9", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day9", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day9", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day9", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day9", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day9", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day9", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day9", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day9", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day9", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day9", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day9", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day9", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day9", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="32" align="center" class="style76"><input
						type="text" name="e2Lh9a" size="3"
						value="<%=props.getProperty("e2Lh9a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh9a"
						value="<%=props.getProperty("flag_e2Lh9a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh9a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl9a" size="3"
						value="<%=props.getProperty("tshPrl9a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl9a"
						value="<%=props.getProperty("flag_tshPrl9a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl9a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog9a" size="3"
						value="<%=props.getProperty("progBhog9a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog9a"
						value="<%=props.getProperty("flag_progBhog9a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog9a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo9" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo9", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst9a" size="5"
						value="<%=props.getProperty("rightCyst9a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo9" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo9", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst9a" size="5"
						value="<%=props.getProperty("leftCyst9a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff9" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff9", "")%>"
						onDblClick="showPGBox('ffDiv',9, this, event, 'ff9', 500, 823, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX9a" size="5"
						value="<%=props.getProperty("etTEX9a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds9" cols="12" rows="2" class="style71"><%=props.getProperty("meds9", "")%></textarea>
					</td>
					<td rowspan="2" class="style76"><textarea name="comment9"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment9", "")%></textarea>
					</td>
				</tr>
				<tr>
					<td height="28" align="center" class="style76"><input
						type="text" name="e2Lh9b" size="3"
						value="<%=props.getProperty("e2Lh9b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh9b"
						value="<%=props.getProperty("flag_e2Lh9b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh9b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl9b" size="3"
						value="<%=props.getProperty("tshPrl9b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl9b"
						value="<%=props.getProperty("flag_tshPrl9b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl9b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog9b" size="3"
						value="<%=props.getProperty("progBhog9b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog9b"
						value="<%=props.getProperty("flag_progBhog9b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog9b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst9b" size="5"
						value="<%=props.getProperty("rightCyst9b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst9b" size="5"
						value="<%=props.getProperty("leftCyst9b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX9b" size="5"
						value="<%=props.getProperty("etTEX9b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date10" size="7"
						value="<%=props.getProperty("date10", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day10" class="style71">
						<option value="00"
							<%=props.getProperty("day10", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day10", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day10", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day10", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day10", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day10", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day10", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day10", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day10", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day10", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day10", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day10", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day10", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day10", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day10", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day10", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day10", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day10", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day10", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day10", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day10", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day10", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day10", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day10", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day10", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day10", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day10", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day10", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day10", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day10", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day10", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day10", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day10", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day10", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day10", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day10", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day10", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day10", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day10", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day10", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day10", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day10", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day10", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day10", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day10", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day10", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day10", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day10", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day10", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day10", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day10", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="38" align="center" class="style76"><input
						type="text" name="e2Lh10a" size="3"
						value="<%=props.getProperty("e2Lh10a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh10a"
						value="<%=props.getProperty("flag_e2Lh10a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh10a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl10a" size="3"
						value="<%=props.getProperty("tshPrl10a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl10a"
						value="<%=props.getProperty("flag_tshPrl10a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl10a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog10a" size="3"
						value="<%=props.getProperty("progBhog10a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog10a"
						value="<%=props.getProperty("flag_progBhog10a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog10a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo10" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo10", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst10a" size="5"
						value="<%=props.getProperty("rightCyst10a", "")%>" class="style71">
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo10" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo10", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst10a" size="5"
						value="<%=props.getProperty("leftCyst10a", "")%>" class="style71">
					</td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff10" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff10", "")%>"
						onDblClick="showPGBox('ffDiv',10, this, event, 'ff10', 500, 1023, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX10a" size="5"
						value="<%=props.getProperty("etTEX10a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds10" cols="12" rows="2" class="style71"><%=props.getProperty("meds10", "")%></textarea></td>
					<td rowspan="2" class="style76"><textarea name="comment10"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment10", "")%></textarea></td>
				</tr>
				<tr>
					<td align="center" class="style76"><input type="text"
						name="e2Lh10b" size="3"
						value="<%=props.getProperty("e2Lh10b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh10b"
						value="<%=props.getProperty("flag_e2Lh10b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh10b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl10b" size="3"
						value="<%=props.getProperty("tshPrl10b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl10b"
						value="<%=props.getProperty("flag_tshPrl10b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl10b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog10b" size="3"
						value="<%=props.getProperty("progBhog10b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog10b"
						value="<%=props.getProperty("flag_progBhog10b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog10b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst10b" size="5"
						value="<%=props.getProperty("rightCyst10b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst10b" size="5"
						value="<%=props.getProperty("leftCyst10b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX10b" size="5"
						value="<%=props.getProperty("etTEX10b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date11" size="7"
						value="<%=props.getProperty("date11", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day11" class="style71">
						<option value="00"
							<%=props.getProperty("day11", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day11", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day11", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day11", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day11", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day11", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day11", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day11", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day11", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day11", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day11", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day11", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day11", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day11", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day11", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day11", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day11", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day11", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day11", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day11", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day11", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day11", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day11", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day11", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day11", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day11", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day11", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day11", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day11", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day11", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day11", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day11", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day11", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day11", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day11", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day11", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day11", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day11", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day11", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day11", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day11", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day11", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day11", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day11", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day11", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day11", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day11", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day11", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day11", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day11", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day11", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="36" align="center" class="style76"><input
						type="text" name="e2Lh11a" size="3"
						value="<%=props.getProperty("e2Lh11a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh11a"
						value="<%=props.getProperty("flag_e2Lh11a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh11a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl11a" size="3"
						value="<%=props.getProperty("tshPrl11a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl11a"
						value="<%=props.getProperty("flag_tshPrl11a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl11a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog11a" size="3"
						value="<%=props.getProperty("progBhog11a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog11a"
						value="<%=props.getProperty("flag_progBhog11a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog11a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo11" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo11", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst11a" size="5"
						value="<%=props.getProperty("rightCyst11a", "")%>" class="style71">
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo11" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo11", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst11a" size="5"
						value="<%=props.getProperty("leftCyst11a", "")%>" class="style71">
					</td>
					<td align="center" rowspan="2" class="style76"><input
						type="text" name="ff11" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff11", "")%>"
						onDblClick="showPGBox('ffDiv',11, this, event, 'ff11', 500, 1023, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX11a" size="5"
						value="<%=props.getProperty("etTEX11a", "")%>" class="style71"></td>
					<td align="center" rowspan="2" class="style76"><textarea
						name="meds11" cols="12" rows="2" class="style71"><%=props.getProperty("meds11", "")%></textarea>
					</td>
					<td rowspan="2" class="style76"><textarea name="comment11"
						cols="17" rows="3" class="style71"><%=props.getProperty("comment11", "")%></textarea>
					</td>
				</tr>
				<tr>
					<td align="center" class="style76"><input type="text"
						name="e2Lh11b" size="3"
						value="<%=props.getProperty("e2Lh11b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh11b"
						value="<%=props.getProperty("flag_e2Lh11b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh11b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl11b" size="3"
						value="<%=props.getProperty("tshPrl11b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl11b"
						value="<%=props.getProperty("flag_tshPrl11b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl11b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog11b" size="3"
						value="<%=props.getProperty("progBhog11b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog11b"
						value="<%=props.getProperty("flag_progBhog11b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog11b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst11b" size="5"
						value="<%=props.getProperty("rightCyst11b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst11b" size="5"
						value="<%=props.getProperty("leftCyst11b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX11b" size="5"
						value="<%=props.getProperty("etTEX11b", "")%>" class="style71"></td>
				</tr>
				<tr>
					<td rowspan="2" align="center" class="style76"><input
						type="text" name="date12" size="7"
						value="<%=props.getProperty("date12", "")%>" class="style71"
						onDblClick="calToday(this)"></td>
					<td rowspan="2" align="center" class="style76"><select
						name="day12" class="style71">
						<option value="00"
							<%=props.getProperty("day12", "").equals("00")?"selected":""%>>0</option>
						<option value="01"
							<%=props.getProperty("day12", "").equals("01")?"selected":""%>>01</option>
						<option value="02"
							<%=props.getProperty("day12", "").equals("02")?"selected":""%>>02</option>
						<option value="03"
							<%=props.getProperty("day12", "").equals("03")?"selected":""%>>03</option>
						<option value="04"
							<%=props.getProperty("day12", "").equals("04")?"selected":""%>>04</option>
						<option value="05"
							<%=props.getProperty("day12", "").equals("05")?"selected":""%>>05</option>
						<option value="06"
							<%=props.getProperty("day12", "").equals("06")?"selected":""%>>06</option>
						<option value="07"
							<%=props.getProperty("day12", "").equals("07")?"selected":""%>>07</option>
						<option value="08"
							<%=props.getProperty("day12", "").equals("08")?"selected":""%>>08</option>
						<option value="09"
							<%=props.getProperty("day12", "").equals("09")?"selected":""%>>09</option>
						<option value="10"
							<%=props.getProperty("day12", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day12", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day12", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day12", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day12", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day12", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day12", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day12", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day12", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day12", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day12", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day12", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day12", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day12", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day12", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day12", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day12", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day12", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day12", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day12", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day12", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day12", "").equals("31")?"selected":""%>>31</option>
						<option value="32"
							<%=props.getProperty("day12", "").equals("32")?"selected":""%>>32</option>
						<option value="33"
							<%=props.getProperty("day12", "").equals("33")?"selected":""%>>33</option>
						<option value="34"
							<%=props.getProperty("day12", "").equals("34")?"selected":""%>>34</option>
						<option value="35"
							<%=props.getProperty("day12", "").equals("35")?"selected":""%>>35</option>
						<option value="36"
							<%=props.getProperty("day12", "").equals("36")?"selected":""%>>36</option>
						<option value="37"
							<%=props.getProperty("day12", "").equals("37")?"selected":""%>>37</option>
						<option value="38"
							<%=props.getProperty("day12", "").equals("38")?"selected":""%>>38</option>
						<option value="39"
							<%=props.getProperty("day12", "").equals("39")?"selected":""%>>39</option>
						<option value="40"
							<%=props.getProperty("day12", "").equals("40")?"selected":""%>>40</option>
						<option value="41"
							<%=props.getProperty("day12", "").equals("41")?"selected":""%>>41</option>
						<option value="42"
							<%=props.getProperty("day12", "").equals("42")?"selected":""%>>42</option>
						<option value="43"
							<%=props.getProperty("day12", "").equals("43")?"selected":""%>>43</option>
						<option value="44"
							<%=props.getProperty("day12", "").equals("44")?"selected":""%>>44</option>
						<option value="45"
							<%=props.getProperty("day12", "").equals("45")?"selected":""%>>45</option>
						<option value="46"
							<%=props.getProperty("day12", "").equals("46")?"selected":""%>>46</option>
						<option value="47"
							<%=props.getProperty("day12", "").equals("47")?"selected":""%>>47</option>
						<option value="48"
							<%=props.getProperty("day12", "").equals("48")?"selected":""%>>48</option>
						<option value="49"
							<%=props.getProperty("day12", "").equals("49")?"selected":""%>>49</option>
						<option value="50"
							<%=props.getProperty("day12", "").equals("50")?"selected":""%>>50</option>

					</select>
					<td height="33" align="center" class="style76"><input
						type="text" name="e2Lh12a" size="3"
						value="<%=props.getProperty("e2Lh12a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh12a"
						value="<%=props.getProperty("flag_e2Lh12a", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh12a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl12a" size="3"
						value="<%=props.getProperty("tshPrl12a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl12a"
						value="<%=props.getProperty("flag_tshPrl12a", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl12a);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog12a" size="3"
						value="<%=props.getProperty("progBhog12a", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog12a"
						value="<%=props.getProperty("flag_progBhog12a", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog12a);</script>
					</td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="rightLo12" cols="12" rows="2" class="style71"><%=props.getProperty("rightLo12", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst12a" size="5"
						value="<%=props.getProperty("rightCyst12a", "")%>" class="style71"></td>
					<td rowspan="2" align="center" class="style76"><textarea
						name="leftLo12" cols="12" rows="2" class="style71"><%=props.getProperty("leftLo12", "")%></textarea>
					</td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst12a" size="5"
						value="<%=props.getProperty("leftCyst12a", "")%>" class="style71"></td>
					<td align="center" class="style76" rowspan="2"><input
						type="text" name="ff12" size="5" class="style71" maxlength="8"
						value="<%=props.getProperty("ff12", "")%>"
						onDblClick="showPGBox('ffDiv',12, this, event, 'ff12', 500, 1023, 26);">
					</td>
					<td align="center" class="style76"><input type="text"
						name="etTEX12a" size="5"
						value="<%=props.getProperty("etTEX12a", "")%>" class="style71"></td>
					<td align="center" class="style76" rowspan="2"><textarea
						name="meds12" cols="12" rows="2" class="style71"><%=props.getProperty("meds12", "")%></textarea></td>
					<td rowspan="2"><textarea name="comment12" cols="17" rows="3"
						class="style71"><%=props.getProperty("comment12", "")%></textarea>
					</td>
				</tr>
				<tr>
					<td height="32" align="center" class="style76"><input
						type="text" name="e2Lh12b" size="3"
						value="<%=props.getProperty("e2Lh12b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_e2Lh12b"
						value="<%=props.getProperty("flag_e2Lh12b", "")%>"> <script>assignBackgroundColor(document.forms[0].e2Lh12b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="tshPrl12b" size="3"
						value="<%=props.getProperty("tshPrl12b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_tshPrl12b"
						value="<%=props.getProperty("flag_tshPrl12b", "")%>"> <script>assignBackgroundColor(document.forms[0].tshPrl12b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="progBhog12b" size="3"
						value="<%=props.getProperty("progBhog12b", "")%>" class="style71"
						onDblClick="javascript:changeColor(this);"> <input
						type="hidden" name="flag_progBhog12b"
						value="<%=props.getProperty("flag_progBhog12b", "")%>"> <script>assignBackgroundColor(document.forms[0].progBhog12b);</script>
					</td>
					<td align="center" class="style76"><input type="text"
						name="rightCyst12b" size="5"
						value="<%=props.getProperty("rightCyst12b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="leftCyst12b" size="5"
						value="<%=props.getProperty("leftCyst12b", "")%>" class="style71"></td>
					<td align="center" class="style76"><input type="text"
						name="etTEX12b" size="5"
						value="<%=props.getProperty("etTEX12b", "")%>" class="style71"></td>
				</tr>
			</table>

			<!-- ################################################################################################################ -->
			<table width="100%">
				<tr>
					<td height="15">&nbsp;</td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr valign="top">
					<td width="50%" valign="top">

					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4" class="style51">Collection Information</td>
						</tr>

						<tr>
							<td colspan="2" class="style76">Date(dd/mm/yyyy)</td>

							<td width="68%" colspan="2" class="style76"><input
								type="text" name="collectionDate"
								value="<%=props.getProperty("collectionDate", "")%>"
								class="style71" onDblClick="calToday(this)"></td>
						</tr>

						<tr>
							<td colspan="2" class="style76">Abstinence</td>

							<td width="68%" colspan="2" class="style76"><input
								type="text" name="abstinenceDays" size="5"
								value="<%=props.getProperty("abstinenceDays", "")%>"
								class="style71"> &nbsp;&nbsp;(days)</td>
						</tr>

						<tr>
							<td colspan="2" class="style76">Collection Time</td>

							<td width="68%" colspan="2" class="style76"><input
								type="text" name="collectionTime" size="5"
								value="<%=props.getProperty("collectionTime", "")%>"
								class="style71">&nbsp;&nbsp; <select
								name="collectionAmPm" class="style71">
								<option value="am"
									<%=props.getProperty("collectionAmPm", "").equals("am")?"selected":""%>>
								AM</option>
								<option value="pm"
									<%=props.getProperty("collectionAmPm", "").equals("pm")?"selected":""%>>
								PM</option>
							</select></td>
						</tr>
						<tr>
							<td colspan="2" class="style76">Collection Method</td>

							<td width="68%" colspan="2" class="style76"><select
								name="collectionMethod" class="style71">
								<option value="masturbation"
									<%=props.getProperty("collectionMethod", "").equals("masturbation")?"selected":""%>>
								Masturbation</option>
							</select></td>
						</tr>
					</table>
					</td>

					<td width="50%" valign="top">

					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4" class="style51">BMI Caculation</td>
						</tr>

						<tr>
							<td width="12%" class="style76">AGE<br>
							<input type="text" name="bmiAge" style="width: 100%;" size="2"
								maxlength="3" value="<%=props.getProperty("bmiAge", "")%>"
								class="style71" /></td>
							<td width="12%" nowrap class="style76">PREPREGNANT WEIGHT<br>
							<input type="text" name="bmiPpWt" style="width: 100%;"
								onDblClick="wtEnglish2Metric(this);" size="4" maxlength="5"
								value="<%=props.getProperty("bmiPpWt", "")%>" class="style71" />
							</td>
							<td width="10%" class="style76">HEIGHT<br>
							<input type="text" name="bmiPpHt" style="width: 100%;"
								onDblClick="htEnglish2Metric(this);" size="4" maxlength="5"
								value="<%=props.getProperty("bmiPpHt", "")%>" class="style71" />
							</td>
							<td width="12%" class="style76"><a href=#
								onClick="showBMIBox('BMIdiv',1, this, event);return false;"
								title='The height and weight MUST be in metric for the BMI to calculate when you double click in the shaded cell.  If putting in weight or height in Standard measurement, double click each cell to convert to metric. Then, double click in the BMI cell to calculate. Do not put any text in the height or weight cells (kg.) or it will not calculate the BMI.'>
							<font color='red'><b>BMI</b></font></a><br>
							<input type="text" name="bmi" style="width: 100%;"
								onDblClick="calcBMIMetric(document.forms[0].bmiPpWt, document.forms[0].bmiPpHt, this);"
								size="4" maxlength="5" value="<%=props.getProperty("bmi", "")%>"
								class="style71" /></td>
						</tr>
					</table>

					</td>

					<table width="100%">
						<tr>
							<td height="15">&nbsp;</td>
						</tr>
					</table>
					<!-- ################################################################################################################ -->
					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<td width="50%">

							<table width="100%" border="1" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="4" class="style51">Pre-Processing Assessment
									</td>
								</tr>

								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp; Colour
									</td>

									<td colspan="2" class="style76"><select name="preColor"
										class="style71">
										<option value="Normal"
											<%=props.getProperty("preColor", "").equals("Normal")?"selected":""%>>
										NORMAL</option>
										<option value="Abnormal"
											<%=props.getProperty("preColor", "").equals("Abnormal")?"selected":""%>>
										ABNORMAL</option>
									</select></td>
								</tr>

								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp;
									Viscosity</td>

									<td colspan="2" class="style76"><select
										name="preViscosity" class="style71">
										<option value="Normal"
											<%=props.getProperty("preViscosity", "").equals("Normal")?"selected":""%>>
										NORMAL</option>
										<option value="Abnormal"
											<%=props.getProperty("preViscosity", "").equals("Abnormal")?"selected":""%>>
										ABNORMAL</option>
									</select></td>
								</tr>

								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp;
									Liquefaction</td>

									<td colspan="2" class="style76"><select
										name="preLiquefaction" class="style71">
										<option value="Complete"
											<%=props.getProperty("preLiquefaction", "").equals("Complete")?"selected":""%>>
										COMPLETE</option>
										<option value="Incomplete"
											<%=props.getProperty("preLiquefaction", "").equals("Incomplete")?"selected":""%>>
										INCOMPLETE</option>
									</select></td>
								</tr>

								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp; pH</td>

									<td colspan="2" class="style76"><input type="text"
										name="prePh" size="7"
										value="<%=props.getProperty("prePh", "")%>" class="style71">
									</td>
								</tr>

								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp; Round
									Cells</td>

									<td colspan="2" class="style76"><input type="text"
										name="preRoundCells" size="7"
										value="<%=props.getProperty("preRoundCells", "")%>"
										class="style71"></td>
								</tr>
								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp;
									Progression</td>

									<td colspan="2" class="style76"><input type="text"
										name="preProgression" size="7"
										value="<%=props.getProperty("preProgression", "")%>"
										class="style71"></td>
								</tr>

								<tr>
									<td width="25%" class="style76">Volume (ml)<br>
									<input type="text" name="preVolume" size="7"
										value="<%=props.getProperty("preVolume", "")%>"
										class="style71"> &nbsp;&nbsp;&nbsp; &nbsp;x</td>

									<td width="28%" class="style76">Density (M/ml)<br>
									<input type="text" name="preDensity" size="7"
										value="<%=props.getProperty("preDensity", "")%>"
										class="style71"> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; x</td>

									<td width="28%" class="style76">Motility (%)<br>
									<input type="text" name="preMotility" size="7"
										value="<%=props.getProperty("preMotility", "")%>"
										class="style71"> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
									&nbsp; =</td>

									<td width="19%" class="style76">TMC<br>
									<input type="text" name="preTmc" size="7"
										value="<%=props.getProperty("preTmc", "")%>" class="style71"
										onDblClick="calcTMC(forms[0].preVolume, forms[0].preDensity, forms[0].preMotility, this);">
									</td>



								</tr>


								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp;
									Morphology</td>

									<td colspan="2" class="style76"><select
										name="preMorphology" class="style71">
										<option value="Normal"
											<%=props.getProperty("preMorphology", "").equals("Normal")?"selected":""%>>
										NORMAL</option>
										<option value="Abnormal"
											<%=props.getProperty("preMorphology", "").equals("Abnormal")?"selected":""%>>
										ABNORMAL</option>
									</select></td>
								</tr>
								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp;
									Agglutination</td>

									<td colspan="2" class="style76"><select
										name="preAgglutination" class="style71">
										<option value="Yes"
											<%=props.getProperty("preAgglutination", "").equals("Yes")?"selected":""%>>
										YES</option>
										<option value="No"
											<%=props.getProperty("preAgglutination", "").equals("No")?"selected":""%>>
										NO</option>
									</select></td>
								</tr>
								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp; Debris
									</td>

									<td colspan="2" class="style76"><select name="preDebris"
										class="style71">
										<option value="Yes"
											<%=props.getProperty("preDebris", "").equals("Yes")?"selected":""%>>
										YES</option>
										<option value="No"
											<%=props.getProperty("preDebris", "").equals("No")?"selected":""%>>
										NO</option>
									</select></td>
								</tr>

							</table>
							</td>
							<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
							<td width="50%" align="left" valign="top">
							<table width="100%" border="1" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="4" class="style51">Post-Processing Assessment
									</td>
								</tr>

								<tr>
									<td colspan="2" class="style76">&nbsp;&nbsp;&nbsp;
									Progression</td>

									<td colspan="2" class="style76"><select
										name="postProgression" class="style71">
										<option value=""
											<%=props.getProperty("postProgression", "").equals("")?"selected":""%>>
										_</option>

										<option value="1"
											<%=props.getProperty("postProgression", "").equals("1")?"selected":""%>>
										1</option>
										<option value="2"
											<%=props.getProperty("postProgression", "").equals("2")?"selected":""%>>
										2</option>
										<option value="3"
											<%=props.getProperty("postProgression", "").equals("3")?"selected":""%>>
										3</option>
										<option value="4"
											<%=props.getProperty("postProgression", "").equals("4")?"selected":""%>>
										4</option>

									</select></td>
								</tr>

								<tr>
									<td height="10" colspan="4">&nbsp;</td>
								</tr>

								<tr>
									<td width="27%" class="style76">Volume (ml)<br>
									<input type="text" name="postVolume" size="7"
										value="<%=props.getProperty("postVolume", "")%>"
										class="style71"> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp; x</td>

									<td width="27%" class="style76">Density (M/ml)<br>
									<input type="text" name="postDensity" size="7"
										value="<%=props.getProperty("postDensity", "")%>"
										class="style71"> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp; x</td>

									<td width="26%" class="style76">Motility (%)<br>
									<input type="text" name="postMotility" size="7"
										value="<%=props.getProperty("postMotility", "")%>"
										class="style71"> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp; =</td>

									<td width="20%" class="style76">TMC<br>
									<input type="text" name="postTmc" size="7"
										value="<%=props.getProperty("postTmc", "")%>" class="style71"
										onDblClick="calcTMC(forms[0].postVolume, forms[0].postDensity, forms[0].postMotility, this);">

									</td>
								</tr>

								<tr valign="top">
									<td colspan="4" valign="top" class="style76">Comment<br>
									<br>
									<textarea name="postComment" cols="75" rows="8" class="style71"><%=props.getProperty("postComment", "")%></textarea>

									</td>
								</tr>

							</table>

							</td>
						</tr>
					</table>

					<!-- ################################################################################################################# -->

					<table>
						<tr>
							<td height="15">&nbsp;</td>
						</tr>
					</table>

					<!-- ################################################################################################################### -->

					<!-- ################################################################################################################### -->
					<table width="95%" border="0">
						<tr>
							<td align="center" class="style76"><input type="submit"
								value="Save"
								onclick="javascript:return onSave('<html:rewrite page="/form/formname.do"/>');" />
							<input type="submit" value="Save and Exit"
								onclick="javascript:return onSaveExit('<html:rewrite page="/form/formname.do"/>');" />
							<input type="submit" value="Exit"
								onclick="javascript:return onExit();" /> <input type="submit"
								value="Print" onClick="javascript:return onPrint();" /></td>
						</tr>
					</table>

					</html:form>
</body>
</html:html>
