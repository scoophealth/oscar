
<%@ page language="java"%>
<%@ page import="oscar.form.graphic.*, oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "BCAR";
	String formLink = "formbcarpg2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    //resource = resource + "ob/riskinfo/";    props.setProperty("c_lastVisited", "pg2");

	//get project_home
	String project_home = request.getContextPath().substring(1);
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
    <title>Antenatal Record 2</title>
    <html:base/>
    <link rel="stylesheet" type="text/css" href="<%=bView?"bcArStyleView.css" : "bcArStyle.css"%>">
<style type="text/css">
<!--
.demo  {color:#000033; background-color:#cccccc; layer-background-color:#cccccc;
        position:absolute; top:150px; left:270px; width:80px; height:120px;
        z-index:99;  visibility:hidden;}
-->
</style>
</head>
<script type="text/javascript">
<!--
var fieldObj;
function showHideBox(layerName, iState, field) { // 1 visible, 0 hidden
    if(document.layers)	   //NN4+
    {
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById)	  //gecko(NN6) + IE 5+
    {
        var obj = document.getElementById(layerName);
        obj.style.top = "400px";
        obj.style.left = "370px";
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	// IE 4
    {
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
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
function showPGBox(layerName, iState, field, e, prefix, origX, origY, deltaY) { // 1 visible, 0 hidden
    fieldObj = field;
    //get the number of the field
    fieldName = fieldObj.name;
    fieldName = fieldName.substring(prefix.length);

    if(document.layers)	{   //NN4+
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById) {	  //gecko(NN6) + IE 5+
        var obj = document.getElementById(layerName);
        obj.style.top = e.screenY + (origY-e.screenY + deltaY*fieldName);
        obj.style.left = origX;
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	{// IE 4
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
}
function insertBox(str, layerName) { // 1 visible, 0 hidden
    if(document.getElementById)	{
        //var obj = document.getElementById(field);
        fieldObj.value = str;
    }
    showHideBox(layerName, 0, null);
}
function wtEnglish2Metric() {
	if(isNumber(document.forms[0].c_ppWt) ) {
		weight = document.forms[0].c_ppWt.value;
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;
		if(confirm("Are you sure you want to change " + weight + " pounds to " + weightM +"kg?") ) {
			document.forms[0].c_ppWt.value = weightM;
		}
	}
}
function htEnglish2Metric() {
	height = document.forms[0].c_ppHt.value;
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
				document.forms[0].c_ppHt.value = height;
			}
		//}
	}
}
function calcBMIMetric() {
	if(isNumber(document.forms[0].c_ppWt) && isNumber(document.forms[0].c_ppHt)) {
		weight = document.forms[0].c_ppWt.value;
		height = document.forms[0].c_ppHt.value / 100;
		if(weight!="" && weight!="0" && height!="" && height!="0") {
			document.forms[0].c_ppBMI.value = Math.round(weight * 10 / height / height) / 10;
		}
	}
}
// -->
</script>

<script type="text/javascript" language="Javascript">
    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {            
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Antenatal+Record+Part+2&__cfgfile=bcar2PrintCfgPg1&__cfgfile=bcar1PrintCfgPg2&__cfgGraphicFile=bcar2PrintGraphCfgPg1&__template=bcar2";
            document.forms[0].target="_blank";            
        }
       return ret;
    }

    function getFormEntity(name) {
		if (name.value.length>0) {
			return true;
		} else {
			return false;
		}
		/*
		for (var j=0; j<document.forms[0].length; j++) { 
				if (document.forms[0].elements[j] != null && document.forms[0].elements[j].name ==  name ) {
					 return document.forms[0].elements[j] ;
				}
		}*/
    }
    function onWtSVG() {
        var ret = checkAllDates();
		var param="";
		var obj = null;
		if (document.forms[0].c_EDD != null && (document.forms[0].c_EDD.value).length==10) {
			param += "?c_EDD=" + document.forms[0].c_EDD.value;
		} else {
			ret = false;
		}
		if (document.forms[0].c_ppWt != null && (document.forms[0].c_ppWt.value).length>0) {
			param += "&c_ppWt=" + document.forms[0].c_ppWt.value;
		} else {
			ret = false;
		}

		obj = document.forms[0].pg2_date1;
		if (getFormEntity(obj))  param += "&pg2_date1=" + obj.value; 
		obj = document.forms[0].pg2_date2;
		if (getFormEntity(obj))  param += "&pg2_date2=" + obj.value; 
		obj = document.forms[0].pg2_date3;
		if (getFormEntity(obj))  param += "&pg2_date3=" + obj.value; 
		obj = document.forms[0].pg2_date4;
		if (getFormEntity(obj))  param += "&pg2_date4=" + obj.value; 
		obj = document.forms[0].pg2_date5;
		if (getFormEntity(obj))  param += "&pg2_date5=" + obj.value; 
		obj = document.forms[0].pg2_date6;
		if (getFormEntity(obj))  param += "&pg2_date6=" + obj.value; 
		obj = document.forms[0].pg2_date7;
		if (getFormEntity(obj))  param += "&pg2_date7=" + obj.value; 
		obj = document.forms[0].pg2_date8;
		if (getFormEntity(obj))  param += "&pg2_date8=" + obj.value; 
		obj = document.forms[0].pg2_date9;
		if (getFormEntity(obj))  param += "&pg2_date9=" + obj.value; 
		obj = document.forms[0].pg2_date10;
		if (getFormEntity(obj))  param += "&pg2_date10=" + obj.value; 
		obj = document.forms[0].pg2_date11;
		if (getFormEntity(obj))  param += "&pg2_date11=" + obj.value; 
		obj = document.forms[0].pg2_date12;
		if (getFormEntity(obj))  param += "&pg2_date12=" + obj.value; 
		obj = document.forms[0].pg2_date13;
		if (getFormEntity(obj))  param += "&pg2_date13=" + obj.value; 
		obj = document.forms[0].pg2_date14;
		if (getFormEntity(obj))  param += "&pg2_date14=" + obj.value; 
		obj = document.forms[0].pg2_date15;
		if (getFormEntity(obj))  param += "&pg2_date15=" + obj.value; 
		obj = document.forms[0].pg2_date16;
		if (getFormEntity(obj))  param += "&pg2_date16=" + obj.value; 
		obj = document.forms[0].pg2_date17;
		if (getFormEntity(obj))  param += "&pg2_date17=" + obj.value; 
		obj = document.forms[0].pg2_wt1;
		if (getFormEntity(obj))  param += "&pg2_wt1=" + obj.value; 
		obj = document.forms[0].pg2_wt2;
		if (getFormEntity(obj))  param += "&pg2_wt2=" + obj.value; 
		obj = document.forms[0].pg2_wt3;
		if (getFormEntity(obj))  param += "&pg2_wt3=" + obj.value; 
		obj = document.forms[0].pg2_wt4;
		if (getFormEntity(obj))  param += "&pg2_wt4=" + obj.value; 
		obj = document.forms[0].pg2_wt5;
		if (getFormEntity(obj))  param += "&pg2_wt5=" + obj.value; 
		obj = document.forms[0].pg2_wt6;
		if (getFormEntity(obj))  param += "&pg2_wt6=" + obj.value; 
		obj = document.forms[0].pg2_wt7;
		if (getFormEntity(obj))  param += "&pg2_wt7=" + obj.value; 
		obj = document.forms[0].pg2_wt8;
		if (getFormEntity(obj))  param += "&pg2_wt8=" + obj.value; 
		obj = document.forms[0].pg2_wt9;
		if (getFormEntity(obj))  param += "&pg2_wt9=" + obj.value; 
		obj = document.forms[0].pg2_wt10;
		if (getFormEntity(obj))  param += "&pg2_wt10=" + obj.value; 
		obj = document.forms[0].pg2_wt11;
		if (getFormEntity(obj))  param += "&pg2_wt11=" + obj.value; 
		obj = document.forms[0].pg2_wt12;
		if (getFormEntity(obj))  param += "&pg2_wt12=" + obj.value; 
		obj = document.forms[0].pg2_wt13;
		if (getFormEntity(obj))  param += "&pg2_wt13=" + obj.value; 
		obj = document.forms[0].pg2_wt14;
		if (getFormEntity(obj))  param += "&pg2_wt14=" + obj.value; 
		obj = document.forms[0].pg2_wt15;
		if (getFormEntity(obj))  param += "&pg2_wt15=" + obj.value; 
		obj = document.forms[0].pg2_wt16;
		if (getFormEntity(obj))  param += "&pg2_wt16=" + obj.value; 
		obj = document.forms[0].pg2_wt17;
		if (getFormEntity(obj))  param += "&pg2_wt17=" + obj.value; 
/*
		for (var i = 1; i < 18; i++) {
				getFormEntity(("pg2_date"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg2_date" + i + "=" + obj.value;
			}
			obj = getFormEntity(("pg2_wt"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg2_wt" + i + "=" + obj.value;
			}
		}
		for (var i = 18; i < 35; i++) {
			var obj = getFormEntity(("pg3_date"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg3_date" + i + "=" + obj.value;
			}
			obj = getFormEntity(("pg3_wt"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg3_wt" + i + "=" + obj.value;
			}
		}
*/

        if(ret==true)  {
            popupFixedPage(650,850,'formbcar2wt.jsp'+param);
        }

    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        ret = checkAllNumber();
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true) {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        ret = checkAllNumber();
        if(ret == true) {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar1", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
    }

function popupFixedPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
  var popup=window.open(page, "planner", windowprop);
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
        if(valDate(document.forms[0].c_EDD)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_formDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_labRATDate1)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_labRATDate2)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_labRATDate3)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_labRhIgG)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_labRhIgG2)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_labDiabDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_lmpDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].ar2_1USoundDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date1)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date2)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date3)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date4)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date5)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date6)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date7)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date8)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date9)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date10)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date11)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date12)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date13)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date14)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date15)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date16)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_date17)==false){
            b = false;
        }
        return b;
    }

	function calcWeek(source) {
<%
String fedb = props.getProperty("c_EDD", "");
String sDate = "";
java.util.Date edbDate = UtilDateUtilities.StringToDate(fedb, "dd/MM/yyyy"); 
fedb = UtilDateUtilities.DateToString(edbDate, "yyyy/MM/dd"); 
if (!fedb.equals("") && fedb.length()==10 ) {
	FrmGraphicAR arG = new FrmGraphicAR();
	edbDate = arG.getStartDate(fedb);
    sDate = UtilDateUtilities.DateToString(edbDate, "MMMMM dd, yyyy"); //"yy,MM,dd");
	//System.out.println(fedb + ":" + sDate);
%>
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
        var start=new Date("<%=sDate%>");

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
<% } %>
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
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<div ID="Langdiv" class="demo">
   <table bgcolor='silver' width='100%'>
     <tr><td align='right'><a href=# onclick="showHideBox('Langdiv',0, null); return false;">X</a></td></tr>
     <tr><td><a href=# onclick="insertBox('ceph', 'Langdiv'); return false;">ceph</a></td></tr>
     <tr><td><a href=# onclick="insertBox('breech', 'Langdiv'); return false;">breech</a></td></tr>
     <tr><td><a href=# onclick="insertBox('transv', 'Langdiv'); return false;">transv</a></td></tr>
   </table>
</div>
<div ID="UrineDiv" class="demo">
   <table bgcolor='silver' width='100%'>
     <tr><td align='right'><a href=# onclick="showHideBox('UrineDiv',0, null); return false;">X</a></td></tr>
     <tr><td><a href=# onclick="insertBox('+', 'UrineDiv'); return false;">+</a></td></tr>
     <tr><td><a href=# onclick="insertBox('++', 'UrineDiv'); return false;">++</a></td></tr>
     <tr><td><a href=# onclick="insertBox('+++', 'UrineDiv'); return false;">+++</a></td></tr>
     <tr><td><a href=# onclick="insertBox('++++', 'UrineDiv'); return false;">++++</a></td></tr>
   </table>
</div>

<html:form action="/form/formname">

<input type="hidden" name="commonField" value="ar2_" />
<input type="hidden" name="c_lastVisited" value="pg2" />
<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />
<input type="hidden" name="form_link" value="<%=formLink%>" />
<input type="hidden" name="formId" value="<%=formId%>" />
<input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>" />
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" />
<input type="hidden" name="submit" value="exit"/>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
<%
  if (!bView) {
%>
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
<%
  }
%>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>

<%
  if (!bView) {
%> 
        <!--td>
           <a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a>
        </td-->

        <td align="right"><b>View:</b>
            <a href="javascript: popupPage('formbcarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');"> AR1</a> |
            <a href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2 <font size=-2>(pg.2)</font></a>
        </td>
        <td align="right"><b>Edit:</b>
            <a href="formbcarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR1</a> |
			AR2<font size=-2>(pg.1)</font> |
            <a href="formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2 <font size=-2>(pg.2)</font></a> |
            <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR Planner</a-->
        </td>
<%
  }
%>
    </tr>
</table>

<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr><td width="60%">

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
    <th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
	  British Columbia Antenatal Record Part 2 <font size="-2">HLTH-1582-2 Rev.02/03</font></th>
    </tr>
  </table>

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="50%"><b>14.</b> HOSPITAL<br>
      <input type="text" name="c_hospital" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_hospital", "") %>" />
      </td>
	  <td width="50%">INTENDED PLACE OF BIRTH<br>
      <input type="text" name="ar2_inBirthPlace" style="width:100%" size="40" maxlength="60" value="<%= props.getProperty("ar2_inBirthPlace", "") %>" @oscar.formDB />
      </td>
    </tr>
  </table>
	  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="33%" valign="top">
	  
	  
      <table width="100%" border="1"  cellspacing="0" cellpadding="0">
        <tr>
          <th colspan="2" align="left">15. LABORATORY</th>
		</tr><tr>
		  <td>BLOOD GROUP<br>
          <!--input type="text" name="ar2_labBlood" style="width:100%" size="10" maxlength="12" value="<%--= props.getProperty("ar2_labBlood", "") --%>" @oscar.formDB /-->
          <select name="ar2_labBlood">
          <%
          String[] optBG = {"", "O", "A", "B", "AB"};
          for (int i=0; i<optBG.length; i++) {
          %>
            <option value="<%=optBG[i]%>" <%=props.getProperty("ar2_labBlood", "").equals(optBG[i])?"selected":""%> ><%=optBG[i]%></option>
          <%}%>
          </select>
		  </td>
		  <td>Rh FACTOR<br>
          <!--input type="text" name="ar2_labRh" style="width:100%" size="10" maxlength="12" value="<%--= props.getProperty("ar2_labRh", "") --%>" @oscar.formDB /-->
          <select name="ar2_labRh">
            <option value="" <%=props.getProperty("ar2_labRh", "").equals("")?"selected":""%> ></option>
            <option value="+ve" <%=props.getProperty("ar2_labRh", "").equals("+ve")?"selected":""%> >+ve</option>
            <option value="-ve" <%=props.getProperty("ar2_labRh", "").equals("-ve")?"selected":""%> >-ve</option>
          </select>
		  </td>
		</tr><tr>
		  <td>RUBELLA TITRE<br>
          <input type="text" name="ar2_labRubella" style="width:100%" size="10" maxlength="12" value="<%= props.getProperty("ar2_labRubella", "") %>" @oscar.formDB />
		  </td>
		  <td>HBsAg.<br>
          <!--input type="text" name="ar2_labHBsAg" style="width:100%" size="10" maxlength="12" value="<%--= props.getProperty("ar2_labHBsAg", "") --%>" @oscar.formDB /-->
		  <select name="ar2_labHBsAg">
		    <option value="" <%= props.getProperty("ar2_labHBsAg", "").equals("")?"selected":""%> ></option>
		    <option value="NR" <%= props.getProperty("ar2_labHBsAg", "").equals("NR")?"selected":""%> >NR</option>
		    <option value="R" <%= props.getProperty("ar2_labHBsAg", "").equals("R")?"selected":""%> >R</option>
		  </select>
		  </td>
		</tr><tr>
		  <td colspan="2">HEMOGLOBIN (1st & 3rd TM)
		  </td>
		</tr><tr>
		  <td>1st:<br>
          <input type="text" name="ar2_labHem1st" style="width:100%" size="10" maxlength="12" value="<%= props.getProperty("ar2_labHem1st", "") %>" @oscar.formDB />
		  </td>
		  <td>3rd:<br>
          <input type="text" name="ar2_labHem3rd" style="width:100%" size="10" maxlength="12" value="<%= props.getProperty("ar2_labHem3rd", "") %>" @oscar.formDB />
		  </td>
		</tr>
      </table>

	  </td><td width="33%" valign="top">
 
      <table width="100%" border="1"  cellspacing="0" cellpadding="0">
        <tr>
          <td colspan="2">Rh ANTIBODY TITRE</td>
		</tr><tr>
		  <td align="center" width="50%"><span class="small9"><I>D M Y</I></font></td>
		  <td align="center"><span class="small9"><I>Results</I></font></td>
		</tr><tr>
		  <td>
          <input type="text" name="ar2_labRATDate1" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRATDate1", "") %>" @oscar.formDB  dbType="date"/>
		  </td>
		  <td>
          <input type="text" name="ar2_labRATRes1" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRATRes1", "") %>" @oscar.formDB />
		  </td>
		</tr><tr>
		  <td>
          <input type="text" name="ar2_labRATDate2" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRATDate2", "") %>" @oscar.formDB  dbType="date"/>
		  </td>
		  <td>
          <input type="text" name="ar2_labRATRes2" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRATRes2", "") %>" @oscar.formDB />
		  </td>
		</tr><tr>
		  <td>
          <input type="text" name="ar2_labRATDate3" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRATDate3", "") %>" @oscar.formDB  dbType="date"/>
		  </td>
		  <td>
          <input type="text" name="ar2_labRATRes3" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRATRes3", "") %>" @oscar.formDB />
		  </td>
		</tr><tr>
		  <td colspan="2">Rh Ig GIVEN <span class="small8">dd/mm/yyyy</span>
		  </td>
		</tr><tr>
		  <td>
          <input type="text" name="ar2_labRhIgG" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRhIgG", "") %>" @oscar.formDB dbType="date"/>
		  </td>
		  <td>
          <input type="text" name="ar2_labRhIgG2" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labRhIgG2", "") %>" @oscar.formDB dbType="date"/>
		  </td>
		</tr>
      </table>
	  
	  </td><td valign="top">
 
      <table width="100%" border="1"  cellspacing="0" cellpadding="0">
        <tr>
          <td><span class="small9">A.F.P./ TRIPLE SCREEN</span><br>
          <!--input type="text" name="ar2_labAfpTS" style="width:100%" size="10" maxlength="10" value="<%--= props.getProperty("ar2_labAfpTS", "") --%>" @oscar.formDB /-->
          <select name="ar2_labAfpTS">
          <%
          String[] optAfp = {"", "Neg", "Pos", "+ve", "-ve", "declined"}; 
          for (int i=0; i<optAfp.length; i++) {
          %>
            <option value="<%=optAfp[i]%>" <%=props.getProperty("ar2_labBlood", "").equals(optAfp[i])?"selected":""%> ><%=optAfp[i]%></option>
          <%}%>
          </select>
		  </td>
		</tr><tr>
          <td><span class="small9">S.T.S.</span>
          <!--input type="text" name="ar2_labSTS" style="width:100%" size="10" maxlength="10" value="<%--= props.getProperty("ar2_labSTS", "") --%>" @oscar.formDB /-->
		  <select name="ar2_labSTS">
		    <option value="" <%= props.getProperty("ar2_labSTS", "").equals("")?"selected":""%> ></option>
		    <option value="NR" <%= props.getProperty("ar2_labSTS", "").equals("NR")?"selected":""%> >NR</option>
		    <option value="R" <%= props.getProperty("ar2_labSTS", "").equals("R")?"selected":""%> >R</option>
		  </select>
		  </td>
		</tr><tr>
          <td><span class="small9">HIV TEST DONE<br>
          <input type="checkbox" name="ar2_labHivTestN" <%= props.getProperty("ar2_labHivTestN", "")%>  @oscar.formDB dbType="tinyint(1)"  /> 
		  No
          <input type="checkbox" name="ar2_labHivTestY" <%= props.getProperty("ar2_labHivTestY", "")%>  @oscar.formDB dbType="tinyint(1)"  /> 
		  Yes</span>
		  <select name="ar2_labHIV" @oscar.formDB dbType="varchar(10)">
		    <option value="" <%= props.getProperty("ar2_labHIV", "").equals("")?"selected":""%> ></option>
		    <option value="NR" <%= props.getProperty("ar2_labHIV", "").equals("NR")?"selected":""%> >NR</option>
		    <option value="R" <%= props.getProperty("ar2_labHIV", "").equals("R")?"selected":""%> >R</option>
		  </select>
		  </td>
		</tr><tr>
          <td><span class="small9">OTHER TESTS<br>
          <!--input type="text" name="ar2_labOtherTest" style="width:100%"  size="20" maxlength="200" value="<%--= props.getProperty("ar2_labOtherTest", "")--%>"  @oscar.formDB /--> 
          <textarea name="ar2_labOtherTest" style="width:100%" cols="50" rows="3"  ><%= props.getProperty("ar2_labOtherTest", "") %></textarea> </td>
		  </td>
		</tr>
      </table>

	  </td>
    </tr>
  </table>
  


  </td>
  <td valign="top">

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <td colspan="2">DATE<br>
      <input type="text" name="pg2_formDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg2_formDate", "") %>" @oscar.formDB dbType="date" />
      </td>
    </tr><tr>
      <td width="55%">SURNAME<br>
      <input type="text" name="c_surname" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("c_surname", "") %>" />
	  </td>
	  <td>GIVEN NAME<br>
      <input type="text" name="c_givenName" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("c_givenName", "") %>" />
	  </td>
    </tr><tr>
      <td>ADDRESS<br>
      <input type="text" name="c_address" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("c_address", "") %>" />
      <input type="text" name="c_city" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("c_city", "") %>" />
      <input type="text" name="c_province" size="18" maxlength="50" value="<%= props.getProperty("c_province", "") %>" />
      <input type="text" name="c_postal" size="7" maxlength="8" value="<%= props.getProperty("c_postal", "") %>" />
	  </td>
	  <td valign="top">PHONE NUMBER<br>
      <input type="text" name="c_phone" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("c_phone", "") %>" />
	  </td>
    </tr><tr>
      <td>PERSONAL HEALTH NUMBER<br>
      <input type="text" name="c_phn" style="width:100%" size="20" maxlength="20" value="<%= props.getProperty("c_phn", "") %>" />
	  </td>
	  <td><span class="small9">PHYSICIAN / MIDWIFE NAME</span><br>
      <input type="text" name="c_phyMid" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>" />
	  </td>
    </tr>
  </table>
  
  </td>
  </tr>
</table>


<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <td width="25%">

      <table width="100%" border="0"  cellspacing="0" cellpadding="0">
        <tr>
          <td width="10%" align="center">GEST.<br><span class="small8">WKS.</span></td>
          <td width="50%" align="center"><span class="small8">DIABETES SCREEN<br>DD/MM/YYYY</span></td>
		  <td align="center">RESULT</td>
		</tr><tr>
          <td>
          <input type="text" name="ar2_labGWeek" style="width:100%" size="3" maxlength="5" value="<%= props.getProperty("ar2_labGWeek", "") %>" @oscar.formDB />
          </td>
          <td>
          <input type="text" name="ar2_labDiabDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labDiabDate", "") %>" @oscar.formDB  dbType="date"/>
          </td>
		  <td>
          <input type="text" name="ar2_labDiabRes" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labDiabRes", "") %>" @oscar.formDB />
          </td>
		</tr>
      </table>

  </td><td width="30%">

      <table width="100%" border="0"  cellspacing="0" cellpadding="0">
        <tr>
          <td colspan="2">GBS SCREEN (35-37 wks.)</td>
		  <td width="33%" align="center">RESULT</td>
		</tr><tr>
          <td width="33%">
          <input type="checkbox" name="ar2_labGBSTestN" <%= props.getProperty("ar2_labGBSTestN", "")%>  @oscar.formDB dbType="tinyint(1)"  > 
		  No
          </td>
          <td width="33%">
          <input type="checkbox" name="ar2_labGBSTestY" <%= props.getProperty("ar2_labGBSTestY", "")%>  @oscar.formDB dbType="tinyint(1)"  > 
		  Yes</span>
          </td>
          <td>
          <input type="text" name="ar2_labGBSRes" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_labGBSRes", "") %>" @oscar.formDB />
          </td>
		</tr>
      </table>

  </td><td rowspan="2" valign="bottom">

      <table width="100%" border="0"  cellspacing="0" cellpadding="0">
        <tr>
          <th colspan="2" align="left">17. PROBLEM LIST <I>(specify)</I>:</th>
		</tr><tr>
          <td width="20%"><span class="small9">PREGNANCY:</span></td>
          <td>
          <input type="text" name="ar2_proPreg" style="width:100%" size="40" maxlength="50" value="<%= props.getProperty("ar2_proPreg", "") %>" @oscar.formDB />
		</tr><tr>
          <td width="20%"><span class="small9">LABOUR:</span></td>
          <td>
          <input type="text" name="ar2_proLabour" style="width:100%" size="40" maxlength="50" value="<%= props.getProperty("ar2_proLabour", "") %>" @oscar.formDB />
		</tr><tr>
          <td width="20%"><span class="small8">POSTPARTUM:</span></td>
          <td>
          <input type="text" name="ar2_proPostPartum" style="width:100%" size="40" maxlength="50" value="<%= props.getProperty("ar2_proPostPartum", "") %>" @oscar.formDB />
		</tr><tr>
          <td width="20%"><span class="small9">NEWBORN:</span></td>
          <td>
          <input type="text" name="ar2_proNewBorn" style="width:100%" size="40" maxlength="50" value="<%= props.getProperty("ar2_proNewBorn", "") %>" @oscar.formDB />
          </td>
		</tr>
      </table>


  </td>
</tr><tr>
  <td colspan="2">

      <table width="100%" border="1"  cellspacing="0" cellpadding="0">
        <tr>
		  <td width="12%" nowrap><B>16.</B> AGE<br>
          <input type="text" name="ar2_age" style="width:100%" size="3" maxlength="5" value="<%= props.getProperty("ar2_age", "") %>" @oscar.formDB />
		  </td>
		  <td width="12%" nowrap><span class="small8">PREPREGNANT WEIGHT</span><br>
          <input type="text" name="c_ppWt"  style="background-color: #99ccff; width:100%;" onDblClick="wtEnglish2Metric();" size="5" maxlength="5" value="<%= props.getProperty("c_ppWt", "") %>" @oscar.formDB />
		  </td>
		  <td width="10%">HEIGHT<br>
          <input type="text" name="c_ppHt"  style="background-color: #99ccff; width:100%;" onDblClick="htEnglish2Metric();" size="5" maxlength="5" value="<%= props.getProperty("c_ppHt", "") %>" @oscar.formDB />
		  </td>
		  <td width="12%">BMI<br>
          <input type="text" name="c_ppBMI"  style="background-color: #99ccff; width:100%;" onDblClick="calcBMIMetric();" size="5" maxlength="5" value="<%= props.getProperty("c_ppBMI", "") %>" @oscar.formDB />
		  </td>
		  <td width="25%">LMP <span class="small8">DD/MM/YYYY</span><br>
          <input type="text" name="ar2_lmpDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_lmpDate", "") %>" @oscar.formDB dbType="date"/>
		  </td>
		  <td>EDD <span class="small8">DD/MM/YYYY</span><br>
          <input type="text" name="c_EDD" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("c_EDD", "") %>" @oscar.formDB dbType="date"/>
		  </td>
		</tr>
      </table>


  </td>
</tr>
</table>


<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
          <td width="7%" align="center">DATE<br><span class="small8">DD/MM/YYYY</span></td>
          <td width="5%" align="center">WT.</td>
          <td width="7%" align="center">B.P.</td>
          <td width="7%" align="center">URINE<br>P &nbsp;&nbsp;&nbsp; G</td>
          <td width="6%" align="center"><span class="small9">GEST.<br>AGE IN<br>WEEKS</span></td>
          <td width="7%" align="center"><span class="small9">FUNDAL<br>HEIGHT<br>CMS.</span></td>
          <td width="7%" align="center">FHR &<br>ACTIVITY</td>
          <td width="7%" align="center"><span class="small9">PRESEN-<br>TATION &<br>POSITION</span></td>
          <td width="44%" colspan="2" align="right" valign="bottom"><span class="small8">Return in</span></td>
          </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date1"  style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date1", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt1" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp1" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp1", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine1" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine1", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG1" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest1" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest1", "") %>"  onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht1" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct1" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos1"  style="background-color: #99ccff; width:100%;" onDblClick="showBox('Langdiv',1, this, event);" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos1", "") %>" @oscar.formDB />
  </td>
  <td width="38%">
  <input type="text" name="pg2_comment1" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment1", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn1" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn1", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date2" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date2", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt2" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp2" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp2", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine2" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine2", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG2" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest2" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest2", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht2" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct2" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos2" style="background-color: #99ccff; width:100%;" onDblClick="showBox('Langdiv',1, this, event);" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment2" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment2", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn2" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn2", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date3" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date3", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt3" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp3" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp3", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine3" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine3", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG3" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest3" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest3", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht3" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct3" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos3" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment3" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment3", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn3" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn3", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date4" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date4", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt4" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp4" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp4", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine4" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine4", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG4" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest4" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest4", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht4" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct4" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos4" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos4", "") %>" @oscar.formDB />
  </td>
  <td><span class="small8"><font color="red">NOTE: SEND A PHOTOCOPY OF ANTENATAL PARTS 1&2 TO<br>
  HOSPITAL AT 20 WEEKS</font>
  <input type="checkbox" name="pg2_SentHosp20" <%= props.getProperty("pg2_SentHosp20", "")%>  @oscar.formDB dbType="tinyint(1)"  > 
  SENT
  <input type="checkbox" name="pg2_toPatient20" <%= props.getProperty("pg2_toPatient20", "")%>  @oscar.formDB dbType="tinyint(1)"  > 
  GIVEN TO PATIENT</span>
  <input type="text" name="pg2_comment4" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment4", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn4" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn4", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date5" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date5", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt5" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp5" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp5", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine5" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine5", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG5" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest5" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest5", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht5" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct5" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos5" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment5" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment5", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn5" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn5", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date6" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date6", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt6" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt6", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp6" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp6", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine6" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine6", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG6" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG6", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest6" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest6", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht6" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht6", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct6" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct6", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos6" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos6", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment6" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment6", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn6" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn6", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date7" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date7", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt7" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt7", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp7" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp7", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine7" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine7", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG7" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG7", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest7" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest7", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht7" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht7", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct7" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct7", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos7" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos7", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment7" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment7", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn7" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn7", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date8" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date8", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt8" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt8", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp8" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp8", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine8" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine8", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG8" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG8", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest8" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest8", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht8" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht8", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct8" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct8", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos8" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos8", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment8" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment8", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn8" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn8", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date9" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date9", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt9" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt9", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp9" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp9", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine9" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine9", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG9" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG9", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest9" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest9", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht9" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht9", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct9" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct9", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos9" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos9", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment9" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment9", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn9" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn9", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date10" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date10", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt10" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt10", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp10" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp10", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine10" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine10", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG10" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG10", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest10" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest10", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht10" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht10", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct10" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct10", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos10" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos10", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment10" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment10", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn10" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn10", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date11" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date11", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt11" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt11", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp11" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp11", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine11" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine11", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG11" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG11", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest11" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest11", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht11" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht11", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct11" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct11", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos11" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos11", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment11" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment11", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn11" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn11", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date12" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date12", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt12" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt12", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp12" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp12", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine12" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine12", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG12" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG12", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest12" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest12", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht12" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht12", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct12" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct12", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos12" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos12", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment12" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment12", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn12" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn12", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date13" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date13", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt13" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt13", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp13" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp13", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine13" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine13", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG13" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG13", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest13" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest13", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht13" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht13", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct13" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct13", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos13" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos13", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment13" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment13", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn13" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn13", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date14" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date14", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt14" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt14", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp14" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp14", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine14" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine14", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG14" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG14", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest14" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest14", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht14" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht14", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct14" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct14", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos14" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos14", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment14" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment14", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn14" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn14", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date15" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date15", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt15" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt15", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp15" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp15", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine15" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine15", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG15" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG15", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest15" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest15", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht15" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht15", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct15" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct15", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos15" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos15", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment15" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment15", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn15" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn15", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date16" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date16", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt16" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt16", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp16" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp16", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine16" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine16", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG16" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG16", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest16" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest16", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht16" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht16", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct16" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct16", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos16" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos16", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_comment16" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment16", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn16" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn16", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg2_date17" style="background-color: #99ccff; width:100%;" size="10" maxlength="10" onDblClick="calToday(this)" value="<%= props.getProperty("pg2_date17", "") %>" @oscar.formDB  dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg2_wt17" style="width:100%" size="5" maxlength="5" value="<%= props.getProperty("pg2_wt17", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_bp17" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_bp17", "") %>" @oscar.formDB />
  </td>
  <td nowrap>
  <input type="text" name="pg2_urine17" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urine', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urine17", "") %>" @oscar.formDB />
  <input type="text" name="pg2_urineG17" onDblClick="showPGBox('UrineDiv',1, this, event, 'pg2_urineG', 200, 481, 26);" size="2" maxlength="8" value="<%= props.getProperty("pg2_urineG17", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_gest17" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_gest17", "") %>" onDblClick="calcWeek(this)" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_ht17" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_ht17", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_fhrAct17" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_fhrAct17", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg2_pos17" onDblClick="showBox('Langdiv',1, this, event);" style="background-color: #99ccff; width:100%;" size="8" maxlength="8" value="<%= props.getProperty("pg2_pos17", "") %>" @oscar.formDB />
  </td>
  <td><span class="small8"><font color="red">NOTE: SEND HOSPITAL COPY AT 36 WEEKS</font></span><br>
  <input type="text" name="pg2_comment17" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg2_comment17", "") %>" @oscar.formDB />
  </td>
  <td >
  <input type="text" name="pg2_retIn17" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg2_retIn17", "") %>" @oscar.formDB />
  </td>

</tr>
</table>


<table width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr><td width="23%"></td>
	  <td>

      <table width="100%" border="1" cellspacing="0" cellpadding="0">
        <tr>
		<th width="22%" align="left" colspan="3">
        18. PROBLEMS, INVESTIGATIONS
		</th>
		</tr><tr>
        <td width="20%"><span class="small9">1ST ULTRASOUND DATE</span><br>
        <input type="text" name="ar2_1USoundDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_1USoundDate", "") %>" @oscar.formDB dbType="date"/>
		</td>
        <td width="15%" nowrap><span class="small9">GEST. AGE BY US </span><br>
        <input type="text" name="ar2_gestAgeUs" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("ar2_gestAgeUs", "") %>" @oscar.formDB />
		</td>
        <td><span class="small9">COMMENTS</span><br>
        <textarea name="ar2_probComment" style="width:100%" cols="40" rows="1"  @oscar.formDB dbType="varchar(255)" ><%= props.getProperty("ar2_probComment", "") %></textarea> </td>
        </tr><tr>
        <td colspan="3"><textarea name="ar2_investigation" style="width:100%" cols="50" rows="5" @oscar.formDB dbType="text"><%= props.getProperty("ar2_investigation", "") %></textarea>
        </td>
        </tr>
      </table>

      <table width="100%" border="0" cellspacing="2" cellpadding="0">
		<tr>
		<td width="5%"><span class="small9">Doula:</span></td>
        <td width="60%"><input type="text" name="ar2_doula" style="width:100%" size="50" maxlength="100" value="<%= props.getProperty("ar2_doula", "") %>" @oscar.formDB /></td>
		</td>
		<td width="5%" align="right"><span class="small9">#:</span></td>
        <td><input type="text" name="ar2_doulaNo" style="width:100%" size="10" maxlength="30" value="<%= props.getProperty("ar2_doulaNo", "") %>" @oscar.formDB /></td>
		</td>
        </tr>
      </table>
      <br>
      <table width="100%" border="0" cellspacing="2" cellpadding="0">
		<tr>
        <td colspan="2"><span class="small9">CONSULTATION FOR MOTHER OR NEWBORN</span></td>
        <td nowrap colspan="2"><span class="small9">SIGNATURE</span></td>
	    </tr><tr>
		<td width="5%"><span class="small9">Name:</span></td>
        <td width="45%"><input type="text" name="pg2_consultName" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("pg2_consultName", "") %>" @oscar.formDB /></td>
		</td>
        <td width="45%"><input type="text" name="pg2_signature" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("pg2_signature", "") %>" @oscar.formDB /></td>
		<td><span class="small9">MD/RM</span>
		</td>
        </tr>
      </table>

	  </td>
    </tr>
</table>


<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
<%
  if (!bView) {
%>
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
<%
  }
%>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>

<%
  if (!bView) {
%> 
        <!--td>
           <a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a>
        </td-->

        <td align="right"><b>View:</b>
            <a href="javascript: popupPage('formbcarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');"> AR1</a> |
            <a href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2 <font size=-2>(pg.2)</font></a>
        </td>
        <td align="right"><b>Edit:</b>
            <a href="formbcarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR1</a> |
			AR2<font size=-2>(pg.1)</font> |
            <a href="formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2 <font size=-2>(pg.2)</font></a> |
            <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR Planner</a-->
        </td>
<%
  }
%>
    </tr>
</table>




<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <th align="center">RISK IDENTIFICATION</th>
</tr><tr>
  <td>

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">PAST OBSTETRICAL HISTORY</th>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">RISK FACTORS</span></th>
	</tr><tr>
      <td width="1%">
	  <input type="checkbox" name="c_riskNeonDeath" <%= props.getProperty("c_riskNeonDeath", "") %> />
	  </td>
      <td>Neonatal death</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskStillbirth" <%= props.getProperty("c_riskStillbirth", "") %> />
	  </td>
      <td>Stillbirth</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAbortion" <%= props.getProperty("c_riskAbortion", "") %> />
	  </td>
      <td>Abortion ( 12 - 20 weeks )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskHabitAbort" <%= props.getProperty("c_riskHabitAbort", "") %> />
	  </td>
      <td>Habitual abortion ( 3+ )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriPretBirth33" <%= props.getProperty("c_riskPriPretBirth33", "") %> />
	  </td>
      <td>Prior preterm birth ( 33 - 36 wks. )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriPretBirth20" <%= props.getProperty("c_riskPriPretBirth20", "") %> />
	  </td>
      <td>Prior preterm birth ( 20 - 33 wks. )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriCesBirth" <%= props.getProperty("c_riskPriCesBirth", "") %> />
	  </td>
      <td>Prior Cesarean birth ( uterine surgery )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriIUGR" <%= props.getProperty("c_riskPriIUGR", "") %> />
	  </td>
      <td>Prior IUGR baby</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriMacr" <%= props.getProperty("c_riskPriMacr", "") %> />
	  </td>
      <td>Prior macrosomic baby</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskRhImmu" <%= props.getProperty("c_riskRhImmu", "") %> />
	  </td>
      <td>Rh Immunized ( antibodies present )</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskPriRhNB" <%= props.getProperty("c_riskPriRhNB", "") %> />
	  </td>
      <td>Prior Rh affected preg. with NB exchange<br>or prem.</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskMajCongAnom" <%= props.getProperty("c_riskMajCongAnom", "") %> />
	  </td>
      <td>Major congenital anomalies (eg. Cardiac,<br>CNS, Down's Syndrome.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPPHemo" <%= props.getProperty("c_riskPPHemo", "") %> />
	  </td>
      <td>P.P. Hemorrhage</td>
	</tr>
	</table>
	
	</td><td valign="top">

    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">MEDICAL HISTORY RISK FACTORS</th>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">DIABETES</span></th>
	</tr><tr>
      <td width="1%">
	  <input type="checkbox" name="c_riskConDiet" <%= props.getProperty("c_riskConDiet", "") %> />
      </td>
      <td>Controlled by diet only</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskDietMacFetus" <%= props.getProperty("c_riskDietMacFetus", "") %> />
      </td>
      <td>Diet only macrosomic fetus</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskInsDepend" <%= props.getProperty("c_riskInsDepend", "") %> />
      </td>
      <td>Insulin dependent</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskRetDoc" <%= props.getProperty("c_riskRetDoc", "") %> />
      </td>
      <td>Retinopathy documented</td>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">HEART DISEASE</span></th>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAsymt" <%= props.getProperty("c_riskAsymt", "") %> />
      </td>
      <td>Asymptomatic (no effect on daily living)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskSymt" <%= props.getProperty("c_riskSymt", "") %> />
      </td>
      <td>Symptomatic ( affects daily living)</td>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">HYPERTENSION</span></th>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_risk14090" <%= props.getProperty("c_risk14090", "") %> />
      </td>
      <td>140 / 90</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskHyperDrug" <%= props.getProperty("c_riskHyperDrug", "") %> />
      </td>
      <td>Hypertensive drugs</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskChroRenalDisease" <%= props.getProperty("c_riskChroRenalDisease", "") %> />
      </td>
      <td>Chronic renal disease documented</td>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">OTHER</span></th>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskUnder18" <%= props.getProperty("c_riskUnder18", "") %> />
      </td>
      <td>Age under 18 at delivery</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskOver35" <%= props.getProperty("c_riskOver35", "") %> />
      </td>
      <td>Age 35 or over at delivery</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskObesity" <%= props.getProperty("c_riskObesity", "") %> />
      </td>
      <td>Obesity (equal or more than 90kg. or 200 lbs.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskH157" <%= props.getProperty("c_riskH157", "") %> />
      </td>
      <td>Height (under 1.57 m 5 ft. 2 in.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskH152" <%= props.getProperty("c_riskH152", "") %> />
      </td>
      <td>Height (under 1.52 m 5 ft. 0 in.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskDepre" <%= props.getProperty("c_riskDepre", "") %> />
      </td>
      <td>Depression</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAlcoDrug" <%= props.getProperty("c_riskAlcoDrug", "") %> />
      </td>
      <td>Alcohol and Drugs</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskSmoking" <%= props.getProperty("c_riskSmoking", "") %> />
      </td>
      <td>Smoking any time during pregnancy</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskOtherMedical" <%= props.getProperty("c_riskOtherMedical", "") %> />
      </td>
      <td>Other medical / surgical disorders<br>e.g. epilepsy, severe asthma, Lupus etc.</td>
	</tr>
	</table>



	</td><td valign="top">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">PROBLEMS IN CURRENT PREGNANCY</th>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">RISK FACTOR</span></th>
	</tr><tr>
      <td width="1%">
	  <input type="checkbox" name="c_riskDiagLarge" <%= props.getProperty("c_riskDiagLarge", "") %> />
      </td>
      <td>Diagnosis of large for dates</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskDiagSmall" <%= props.getProperty("c_riskDiagSmall", "") %> />
      </td>
      <td>Diagnosis of small for dates (IUGR)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPolyhyd" <%= props.getProperty("c_riskPolyhyd", "") %> />
      </td>
      <td>Polyhydramnios or oligohydramnios</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskMulPreg" <%= props.getProperty("c_riskMulPreg", "") %> />
      </td>
      <td>Multiple pregnancy</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskMalpres" <%= props.getProperty("c_riskMalpres", "") %> />
      </td>
      <td>Malpresentations</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskMemRupt37" <%= props.getProperty("c_riskMemRupt37", "") %> />
      </td>
      <td>Membrane rupture before 37 weeks</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskBleeding" <%= props.getProperty("c_riskBleeding", "") %> />
      </td>
      <td>Bleeding</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPregIndHypert" <%= props.getProperty("c_riskPregIndHypert", "") %> />
      </td>
      <td>Pregnancy induced hypertension</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskProte1" <%= props.getProperty("c_riskProte1", "") %> />
      </td>
      <td>Proteinuria > 1+</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskGesDiabete" <%= props.getProperty("c_riskGesDiabete", "") %> />
      </td>
      <td>Gestational diabetes documented</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskBloodAnti" <%= props.getProperty("c_riskBloodAnti", "") %> />
      </td>
      <td>Blood antibodies (Rh, Anti C, Anti K, etc.) </td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAnemia" <%= props.getProperty("c_riskAnemia", "") %> />
      </td>
      <td>Anemia ( < 100g per L ) </td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAdmPreterm" <%= props.getProperty("c_riskAdmPreterm", "") %> />
      </td>
      <td>Admission in preterm labour</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPreg42W" <%= props.getProperty("c_riskPreg42W", "") %> />
      </td>
      <td>Pregnancy >= 42 weeks</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskWtLoss" <%= props.getProperty("c_riskWtLoss", "") %> />
      </td>
      <td>Poor weight gain 26 - 36 weeks ( <.5 kg / wk )<br>or weight loss</td>
	</tr>
	</table>


    </td>
   </tr>
  </table>

  
  </td>
</tr>
</table>



</html:form>
</body>
</html:html>
