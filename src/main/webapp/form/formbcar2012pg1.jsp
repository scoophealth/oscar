<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
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

<%@ page language="java"%>
<%@ page import=" oscar.form.*, oscar.form.data.*, java.util.Properties"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
String formClass = "BCAR2012";
String formLink = "formbcar2012pg1.jsp";

int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
int formId = Integer.parseInt(request.getParameter("formId"));
int provNo = Integer.parseInt((String) session.getAttribute("user"));


FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

FrmData fd = new FrmData();
String resource = fd.getResource();
resource = resource + "ob/riskinfo/";
props.setProperty("c_lastVisited", "pg1");

//get project_home
String project_home = request.getContextPath().substring(1);
//sync
boolean bSync = false;
if(!props.getProperty("c_surname_cur", "").equals("") && !(props.getProperty("c_surname_cur", "").equals(props.getProperty("c_surname", ""))
&& props.getProperty("c_givenName_cur", "").equals(props.getProperty("c_givenName", ""))
&& props.getProperty("c_address_cur", "").equals(props.getProperty("c_address", ""))
&& props.getProperty("c_city_cur", "").equals(props.getProperty("c_city", ""))
&& props.getProperty("c_province_cur", "").equals(props.getProperty("c_province", ""))
&& props.getProperty("c_postal_cur", "").equals(props.getProperty("c_postal", ""))
&& props.getProperty("c_phn_cur", "").equals(props.getProperty("c_phn", ""))
&& props.getProperty("c_phone_cur", "").trim().equals(props.getProperty("c_phone", "").trim())
&& props.getProperty("c_phoneAlt1_cur", "").trim().equals(props.getProperty("c_phoneAlt1", "").trim())
&& props.getProperty("c_phoneAlt2_cur", "").trim().equals(props.getProperty("c_phoneAlt2", "").trim())
)) {
    bSync = true;
}

boolean bView = false;
if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">


<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" >


function showHideBox(layerName, iState) { // 1 visible, 0 hidden
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
function insertBox(str, field, layerName) { // 1 visible, 0 hidden
    if(document.getElementById)	{
        var obj = document.getElementById(field);
        obj.value = str;
    }
    showHideBox(layerName, 0);
}
function showDeliBox(layerName, iState, field, e) { // 1 visible, 0 hidden
    fieldObj = field;

    if(document.layers)	{   //NN4+
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById) {	  //gecko(NN6) + IE 5+
        var obj = document.getElementById(layerName);
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	{// IE 4
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
}
function insertBox1(str, layerName) { // 1 visible, 0 hidden
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

function syncDemo() { 
    document.forms[0].c_surname.value = "<%=props.getProperty("c_surname_cur", "")%>";
    document.forms[0].c_givenName.value = "<%=props.getProperty("c_givenName_cur", "")%>";
    document.forms[0].c_address.value = "<%=props.getProperty("c_address_cur", "")%>";
    document.forms[0].c_city.value = "<%=props.getProperty("c_city_cur", "")%>";
    document.forms[0].c_province.value = "<%=props.getProperty("c_province_cur", "")%>";
    document.forms[0].c_postal.value = "<%=props.getProperty("c_postal_cur", "")%>";
    document.forms[0].c_phn.value = "<%=props.getProperty("c_phn_cur", "")%>";
    document.forms[0].c_phone.value = "<%=props.getProperty("c_phone_cur", "")%>";
    document.forms[0].c_phoneAlt1.value = "<%=props.getProperty("c_phoneAlt1_cur", "")%>";
    document.forms[0].c_phoneAlt2.value = "<%=props.getProperty("c_phoneAlt2_cur", "")%>";
}

function wtEnglish2Metric(obj) {
	//if(!isNaN(document.forms[0].c_ppWt) ) {
	//	weight = document.forms[0].c_ppWt.value;
        weight = obj.value;
	if( !isNaN(weight) ) {
		
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;
		if(confirm("Are you sure you want to change " + weight + " pounds to " + weightM +"kg?") ) {
			//document.forms[0].c_ppWt.value = weightM;
			obj.value = weightM;
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
		
		//if(!isNaN(feet) && !isNaN(inch) )
			height = Math.round((feet * 30.48 + inch * 2.54) * 10) / 10 ;
			if(confirm("Are you sure you want to change " + feet + " feet " + inch + " inch(es) to " + height +"cm?") ) {
				document.forms[0].c_ppHt.value = height;
			}
		//}
	}
}
function calcBMIMetric() {
	if(!isNaN(document.forms[0].c_ppWt.value) && !isNaN(document.forms[0].c_ppHt.value)) {
		weight = document.forms[0].c_ppWt.value;
		height = document.forms[0].c_ppHt.value / 100;
		if(weight!="" && weight!="0" && height!="" && height!="0") {
			document.forms[0].c_ppBMI.value = Math.round(weight * 10 / height / height) / 10;
		}
	}
}


    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();

        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Antenatal+Record+Part+1&__cfgfile=bcar1PrintCfgPg1_2012&__template=bcar1_2012";
            document.forms[0].target="_blank";            
        }
        return ret;
    }
    function onPrint12() {
        document.forms[0].submit.value="printAll"; 
                
        document.forms[0].action = "../form/formname.do?__title=British+Columbia+Antenatal+Record&__cfgfile=bcar1PrintCfgPg1_2012&__cfgfile=bcar2PrintCfgPg1_2012&__cfgGraphicFile=bcar2PrintGraphCfgPg1_2012&__graphicPage=2<%= props.getProperty("pg3_date1", "") == "" ? "&__template=bcarARs2_2012" : "&__cfgfile=bcar2PrintCfgPg2_2012&__graphicPage=3&__template=bcarARs1_2012" %>";
        document.forms[0].target="_blank";            
        
        return true;
    }
    function onPrintAll() {
        document.forms[0].submit.value="printAll";                
        document.forms[0].action = "../form/formname.do?__title=British+Columbia+Antenatal+Record&__cfgfile=bcar1PrintCfgPg1_2012&__cfgfile=bcar2PrintCfgPg1_2012&__cfgGraphicFile=bcar2PrintGraphCfgPg1_2012&__graphicPage=2<%= props.getProperty("pg3_date1", "") == "" ? "&__cfgfile=bcar1PrintCfgPg2_2012&__cfgfile=bcar2PrintCfgScores_2012&__template=bcarAll2_2012" : "&__cfgfile=bcar2PrintCfgPg2_2012&__graphicPage=3&__cfgfile=bcar1PrintCfgPg2_2012&__cfgfile=bcar2PrintCfgScores_2012&__template=bcarAll1_2012" %>";
        document.forms[0].target="_blank";            
        
        return true;
    }
    function onPrintRisk() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Antenatal+Record+Part+2&__cfgfile=bcar1PrintCfgPg2_2012&__template=bcarRisk_2012";
            document.forms[0].target="_blank";            
        }
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
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
    function convertWeightUnits(weightField, newUnits){
        var value = weightField.value;
        if (newUnits=="lbs"){
            value = value*2.20462262185;
        }else{
            value = value/2.20462262185;
        }
        weightField.value = value.toFixed(3);
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
        if (month<0 || month>12){
            return "month"
        }
        if (day<0 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
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
				//alert('dateString'+dateString);
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

    function checkAllDates() {
    
        var b = true;
        if(valDate(document.forms[0].pg1_eddByDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_eddByUs)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_dateOfBirth)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_lmp)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_stopDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_examination)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_smokeQuitDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_alcoQuitDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_firstUsPerf)==false){
        	b = false;
        }
        return b;
    }
    
function calByLMP() {
	if (document.forms[0].pg1_lmp.value!="" && valDate(document.forms[0].pg1_lmp)==true) {
		var str_date = document.forms[0].pg1_lmp.value;
        var dd = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var yyyy  = str_date.substring(eval(str_date.lastIndexOf("/")+1));
		var calDate=new Date();
		calDate.setFullYear(yyyy);
		calDate.setMonth(mm);
		calDate.setDate(dd);
		calDate.setHours("8");
		var odate = new Date(calDate.getTime() + (280 * 86400000));
		document.forms[0].pg1_eddByDate.value = odate.getDate() + '/' + (odate.getMonth()+1) + '/' + odate.getFullYear();
	}
}

function calcAgeAtEDD(){

    var EDD = "<%= props.getProperty("c_EDD", "") %>";
    var DOB_array=pg1_dateOfBirth.value.split("/");    
    var age = 0;
    
    if ( EDD == "" ){
        alert("Please enter EDD in Section 14 of AR2 before calculating the age at EDD");
    }else if (pg1_dateOfBirth.value.length != 10){
        alert("Please enter a date of birth first.");
    }else{
        var EDD_array = EDD.split("/");
        age = EDD_array[2] - DOB_array[2];
        if (EDD_array[1] < DOB_array[1]){
            age--;
        }else if (EDD_array[1] == DOB_array[1] && EDD_array[0] < DOB_array[0]){
            age--;
        }
        
        pg1_ageAtEDD.value = age;
    }

}


</script>

<title>Antenatal Record 1</title>

<link rel="stylesheet" type="text/css"
	href="<%=bView?"bcArStyleView.css" : "bcAr2007Style.css"%>">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<html:base />
<style type="text/css">
        <!--
.demo  {color:#000033; background-color:#cccccc; layer-background-color:#cccccc;
        position:absolute; top:150px; left:270px; width:120px; height:230px;
        z-index:99;  visibility:hidden;}
.demo1  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:40px; left:370px; width:190px; height:80px;
        z-index:99;  visibility:hidden;}
.demo2  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:40px; left:320px; width:150px; height:220px;
        z-index:99;  visibility:hidden;}
.demo3  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:460px; left:430px; width:180px; height:160px;
        z-index:99;  visibility:hidden;}
.demo4  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:102px; left:70px; width:100px; height:160px;
        z-index:99;  visibility:hidden;}
.demo5  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:429px; left:430px; width:100px; height:160px;
        z-index:99;  visibility:hidden;}
.demo6  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:1185px; left:380px; width:190px; height:80px;
        z-index:99;  visibility:hidden;}
        -->
    </style>
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<div ID="Langdiv" class="demo">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Langdiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('English','pg1_langPref', 'Langdiv'); return false;">English</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('French','pg1_langPref', 'Langdiv'); return false;">French</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Mandarin','pg1_langPref', 'Langdiv'); return false;">Mandarin</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Cantonese','pg1_langPref', 'Langdiv'); return false;">Cantonese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Hindu','pg1_langPref', 'Langdiv'); return false;">Hindu</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Punjabi','pg1_langPref', 'Langdiv'); return false;">Punjabi</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Pushtu','pg1_langPref', 'Langdiv'); return false;">Pushtu</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Spanish','pg1_langPref', 'Langdiv'); return false;">Spanish</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Tagalog','pg1_langPref', 'Langdiv'); return false;">Tagalog</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Urdu','pg1_langPref', 'Langdiv'); return false;">Urdu</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Tamil','pg1_langPref', 'Langdiv'); return false;">Tamil</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Other','pg1_langPref', 'Langdiv'); return false;">Other</a></td>
	</tr>
</table>
</div>
<div ID="Origdiv" class="demo2">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Origdiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Caucasian','pg1_ethOrig', 'Origdiv'); return false;">Caucasian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Black','pg1_ethOrig', 'Origdiv'); return false;">Black</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('South Asian','pg1_ethOrig', 'Origdiv'); return false;">South
		Asian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Pakistani','pg1_ethOrig', 'Origdiv'); return false;">Pakistani</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Sri Lankan','pg1_ethOrig', 'Origdiv'); return false;">Sri
		Lankan</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Japanese','pg1_ethOrig', 'Origdiv'); return false;">Japanese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Filipino','pg1_ethOrig', 'Origdiv'); return false;">Filipino</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Chinese','pg1_ethOrig', 'Origdiv'); return false;">Chinese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Vietnamese','pg1_ethOrig', 'Origdiv'); return false;">Vietnamese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Hispanic','pg1_ethOrig', 'Origdiv'); return false;">Hispanic</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('First Nations','pg1_ethOrig', 'Origdiv'); return false;">First
		Nations</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Jewish','pg1_ethOrig', 'Origdiv'); return false;">Jewish</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Other','pg1_ethOrig', 'Origdiv'); return false;">Other</a></td>
	</tr>
</table>
</div>
<div ID="Origdiv1" class="demo2">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Origdiv1',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Caucasian','pg1_faEthOrig', 'Origdiv1'); return false;">Caucasian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Black','pg1_faEthOrig', 'Origdiv1'); return false;">Black</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('South Asian','pg1_faEthOrig', 'Origdiv1'); return false;">South
		Asian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Pakistani','pg1_faEthOrig', 'Origdiv1'); return false;">Pakistani</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Sri Lankan','pg1_faEthOrig', 'Origdiv1'); return false;">Sri
		Lankan</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Japanese','pg1_faEthOrig', 'Origdiv1'); return false;">Japanese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Filipino','pg1_faEthOrig', 'Origdiv1'); return false;">Filipino</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Chinese','pg1_faEthOrig', 'Origdiv1'); return false;">Chinese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Vietnamese','pg1_faEthOrig', 'Origdiv1'); return false;">Vietnamese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Hispanic','pg1_faEthOrig', 'Origdiv1'); return false;">Hispanic</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('First Nations','pg1_faEthOrig', 'Origdiv1'); return false;">First
		Nations</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Jewish','pg1_faEthOrig', 'Origdiv1'); return false;">Jewish</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Other','pg1_faEthOrig', 'Origdiv1'); return false;">Other</a></td>
	</tr>
</table>
</div>
<div ID="Instrdiv" class="demo1">
<center>
<table bgcolor='#007FFF' width='99%'>
	<tr>
		<th align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Instrdiv',0); return false;"><font
			color="red">X</font></a></th>
	</tr>
	<tr>
		<th><a href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Instrdiv',0); return false;"><font
			color="#66FF66">Double click shaded fields for drop down or
		calculation.</font><br>
		&nbsp;</a></th>
	</tr>
</table>
</center>
</div>
<div ID="Delidiv" class="demo3">
<center>
<table bgcolor='silver' width='99%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Delidiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('SVD','Delidiv'); return false;">SVD</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('C-section','Delidiv'); return false;">C-section</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Vacuum','Delidiv'); return false;">Vacuum</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Forceps','Delidiv'); return false;">Forceps</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Vacuum and Forceps','Delidiv'); return false;">Vacuum
		and Forceps</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Forceps Trial and C-section','Delidiv'); return false;">Forceps
		Trial and C-section</a></td>
	</tr>
</table>
</center>
</div>
<div ID="Hospdiv" class="demo4">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Hospdiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('BCWH','c_hospital', 'Hospdiv'); return false;">BCWH</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('SPH','c_hospital', 'Hospdiv'); return false;">SPH</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('LGH','c_hospital', 'Hospdiv'); return false;">LGH</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('RCH','c_hospital', 'Hospdiv'); return false;">RCH</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('RGH','c_hospital', 'Hospdiv'); return false;">RGH</a></td>
	</tr>
</table>
</div>
<div ID="Contradiv" class="demo5">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Contradiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Nothing','pg1_contrMethod', 'Contradiv'); return false;">Nothing</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Condom','pg1_contrMethod', 'Contradiv'); return false;">Condom</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('OCP','pg1_contrMethod', 'Contradiv'); return false;">OCP</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Diaphragm','pg1_contrMethod', 'Contradiv'); return false;">Diaphragm</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('IUD','pg1_contrMethod', 'Contradiv'); return false;">IUD</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Other','pg1_contrMethod', 'Contradiv'); return false;">Other</a></td>
	</tr>
</table>
</div>
<div ID="BMIdiv" class="demo6">
<table bgcolor='#007FFF' width='99%'>
	<tr>
		<th align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('BMIdiv',0); return false;"><font
			color="red">X</font></a></th>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('BMIdiv',0); return false;"> <font
			color="#66FF66">The height and weight MUST be in metric for
		the BMI to calculate when you double click in the shaded cell. <br>
		If putting in weight or height in Standard measurement, double click
		each cell to convert to metric. Then, double click in the BMI cell to
		calculate. Do not put any text in the height or weight cells (kg.) or
		it will not calculate the BMI.</font><br>
		&nbsp;</a></td>
	</tr>
</table>
</div>

<!--
@oscar.formDB Table="formBCAR" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
@oscar.formDB Field="c_lastVisited" Type="char(3)" 
-->
<html:form action="/form/formname">

	<input type="hidden" name="commonField" value="ar2_" />
	<input type="hidden" name="c_lastVisited" value="pg1" />
	<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
            if (!bView) {
            %> <input type="submit" 
            	style="width:40px;" 
            	value="Save"
				onclick="javascript:return onSave();" /> 
				
				<input type="submit"
				value="Save and Exit" 
				onclick="javascript:return onSaveExit();" /> <%
            }
            %> <input type="submit" style="width: 40px;" value="Exit"
				onclick="javascript:return onExit();" /> 
				
				<input type="submit"
				style="width: 50px;" value="Print"
				onclick="javascript:return onPrint();" /> 
				
				<input type="submit"
				style="width: 75px;" value="Print Risk"
				onclick="javascript:return onPrintRisk();" /> 
				
				<input type="submit"
				value="Print AR1 & AR2" onclick="javascript:return onPrint12();" />
				
				<input type="submit" style="width: 75px;" value="Print All"
				onclick="javascript:return onPrintAll();" />
			</td>
			<%
        if (!bView) {
        %>
			<td><a href="javascript: function myFunction() {return false; }"
				title="Double click shaded fields for drop down or calculation"
				onClick="showHideBox('Instrdiv',1);return false;"><font
				color='red'>Instruction</font></a></td>

			<td align="right"><!-- font size=-2><b>View:</b> </font>
            <a href="javascript: popupPage('formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');"><font size=-2>AR2 (pg.1)</font></a> |
            <a href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');"><font size=-2>AR2 (pg.2)</font></a>
            &nbsp;</font> --></td>
			<td align="right"><b>Edit:</b>AR1 | <a
				href="formbcar2012pg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formbcar2012pg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a></td>
			<%
        }
        %>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			
		
		<%-- Alignment modified by: Dennis Warren @ Treatment March 2012 --%>
			<td width="60%">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
					British Columbia Antenatal Record Part 1 <font size="-2">BCPHP
					(HLTH) 1582-1 Rev. 2012/03/12</font></th>
				</tr>
			</table>
			

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="30%">
					
						<b>1.</b> Hospital:<br />
					<input type="text" name="c_hospital" id="c_hospital" class="spe"
						onDblClick="showHideBox('Hospdiv',1);"
						<%=oscarVariables.getProperty("BCAR_hospital")==null? " ": ("class=\"spe\" onDblClick='showDef(\""+oscarVariables.getProperty("BCAR_hospital")+"\", this);'") %>
						style="width: 100%" size="30" maxlength="60"
						value="<%= props.getProperty("c_hospital", "") %>" @oscar.formDB />
					</td>
					
					<td width="33%">
					
					<a href="javascript: function myFunction() {return false; }"
						onClick="popupFixedPage(600, 300, 'formbcarpg1namepopup.jsp'); return false;">Attending
					Physician/Midwife:</a><br />
					<input type="text" name="pg1_priCare" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("pg1_priCare", "") %>"
						@oscar.formDB />
						</td>
					<td colspan="2">
					
						Referring Physician/Midwife:<br />
					<input type="text" name="pg1_famPhy" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("pg1_famPhy", "") %>"
						@oscar.formDB />
						</td>
				</tr>
			</table>
			<table>	
				<tr>
					<td colspan="2">
					<span class="small9">Mother's Name </span>
					
					<input
						type="text" name="pg1_moName" style="width: 100%" size="60"
						maxlength="60"
						value="<%= props.getProperty("pg1_moName", (props.getProperty("c_surname", "")+", "+props.getProperty("c_givenName", "")) ) %>"
						@oscar.formDB />
						</td>
						
					<td>
					
					<span class="small9">Date of Birth dd/mm/yyyy</span> <br />
					
					<input type="text" name="pg1_dateOfBirth"
						id="pg1_dateOfBirth" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dateOfBirth", "") %>"
						@oscar.formDB dbType="date" /></td>
						
					<td>
					
					<span class="small9">Age at EDD</span> <br />
					<input type="text" name="pg1_ageAtEDD" id="pg1_ageAtEDD"
						style="width: 100%" class="spe" ondblclick="calcAgeAtEDD()"
						size="2" maxlength="2"
						value="<%= props.getProperty("pg1_ageAtEDD", "") %>" @oscar.formDB />
						
					</td>
				</tr>


				<tr>
					<td colspan="2">
					
					<span class="small9">Mother's Maiden Name</span><br />
					<input type="text" name="pg1_maidenName" style="width: 100%"
						size="30" maxlength="60"
						value="<%= props.getProperty("pg1_maidenName", "") %>"
						@oscar.formDB /></td>
						
					<td>
					
					<span class="small9">Ethnic Origin</span><br />
					<input type="text" name="pg1_ethOrig" id="pg1_ethOrig" class="spe"
						onDblClick="showHideBox('Origdiv',1);" style="width: 100%"
						size="30" maxlength="60"
						value="<%= props.getProperty("pg1_ethOrig", "") %>" @oscar.formDB />
					</td>

					<td>
					
					<span class="small9">Language Preferred</span><br />
					<input type="text" name="pg1_langPref" id="pg1_langPref"
						class="spe" onDblClick="showHideBox('Langdiv',1);"
						style="width: 100%" size="30" maxlength="60"
						value="<%= props.getProperty("pg1_langPref", "") %>" @oscar.formDB />
					</td>
				</tr>

				<tr>
					<td colspan="2">
					
					<span class="small9">Occupation</span> <br />
					<input type="text" name="pg1_work" size="30" maxlength="60"
						value="<%= props.getProperty("pg1_work", "") %>" @oscar.formDB />
					</td>
					<td>
					
					<span class="small9">Work hrs/day</span> <br />
					<input type="text" name="pg1_workHourDay" size="3" maxlength="5"
						value="<%= props.getProperty("pg1_workHourDay", "") %>"
						@oscar.formDB /></td>
					<td>
					
					<span class="small9">No. of school yrs completed</span> <br />
					<input type="text" name="pg1_schYear" size="3" maxlength="3"
						value="<%= props.getProperty("pg1_schYear", "") %>" @oscar.formDB />
					</td>
					
				</tr>

				<tr>
					<td width="40%">
					
					<span class="small9">Partner's Name</span><br />
					<input type="text" name="pg1_partnerName" style="width: 100%"
						size="20" maxlength="60"
						value="<%= props.getProperty("pg1_partnerName", "") %>"
						@oscar.formDB />
						</td>
						
					<td width="5%">
					
					<span class="small9">Age </span><input type="text"
						name="pg1_partnerAge" style="width: 100%" size="2" maxlength="2"
						value="<%= props.getProperty("pg1_partnerAge", "") %>"
						@oscar.formDB /></td>
						
					<td valign="bottom">
					
					<span class="small9">Ethnic Origin of Newborn's Father</span><br /> 
						<input type="text" 
							name="pg1_faEthOrig"
							id="pg1_faEthOrig" 
							class="spe"
							onDblClick="showHideBox('Origdiv1',1);" 
							style="width: 100%"
							size="20" maxlength="50"
							value="<%= props.getProperty("pg1_faEthOrig", "") %>"
							@oscar.formDB />
					</td>
						
					<td>
					
					<span class="small9">Partner's Work:</span> <br />
					<input type="text" name="pg1_ptWork" size="30" maxlength="60"
						value="<%= props.getProperty("pg1_ptWork", "") %>" @oscar.formDB />
					</td>
				</tr>
			</table>

			</td>
			
			
			
			
			
			<td valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" colspan="2"><%=bSync? ("<b><a href=\"javascript: function myFunction() {return false; }\" onClick='syncDemo(); return false;'><font size='+1' color='red'>Synchronize</font></a></b>") :"" %></td>
				</tr>
				<tr>
					<td width="55%">Surname<br>
					<input type="text" name="c_surname" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_surname", "") %>"
						@oscar.formDB /></td>
					<td>Given Name<br>
					<input type="text" name="c_givenName" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_givenName", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2">Address<br>
					<input type="text" name="c_address" style="width: 100%" size="50"
						maxlength="60" value="<%= props.getProperty("c_address", "") %>"
						@oscar.formDB /> <input type="text" name="c_city"
						style="width: 50%" size="50" maxlength="60"
						value="<%= props.getProperty("c_city", "") %>" @oscar.formDB /> <input
						type="text" name="c_province" size="10" maxlength="50"
						value="<%= props.getProperty("c_province", "") %>" @oscar.formDB />
					<input type="text" name="c_postal" size="7" maxlength="8"
						value="<%= props.getProperty("c_postal", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td valign="top">Phone Number<br>
					<input type="text" name="c_phone" style="width: 100%" size="60"
						maxlength="60" value="<%= props.getProperty("c_phone", "") %>"
						@oscar.formDB /></td>
					<td>Personal Health Number<br>
					<input type="text" name="c_phn" style="width: 100%" size="20"
						maxlength="20" value="<%= props.getProperty("c_phn", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>Alternate Phone Number #1<br>
					<input type="text" name="c_phoneAlt1" style="width: 100%" size="60"
						maxlength="60" value="<%= props.getProperty("c_phoneAlt1", "") %>"
						@oscar.formDB /></td>
					<td></td>
				</tr>
				<tr>
					<td>Alternate Phone Number #2<br>
					<input type="text" name="c_phoneAlt2" style="width: 100%" size="60"
						maxlength="60" value="<%= props.getProperty("c_phoneAlt2", "") %>"
						@oscar.formDB /></td>
					<td></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="33%"><b>2.</b> <span style="background-color:red; color:white; padding:2px 5px;">Allergies</span> 
			
			<input type="checkbox"
				name="pg1_allergyN"
				<%= props.getProperty("pg1_allergyN", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> None known 
			<span style="display:block; margin-top:5px;">	
			<input type="checkbox" name="pg1_allergyY"
				<%= props.getProperty("pg1_allergyY", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> Yes (reaction): 
				
				<input type="text"
				name="pg1_allergy" size="25" maxlength="50"
				value="<%= props.getProperty("pg1_allergy", "") %>" @oscar.formDB />
				
			</span>
			</td>
			<td valign="top" width="33%">Medications/herbals 
			<textarea
				name="pg1_curMedic" style="width: 100%" cols="30" rows="2"
				@oscar.formDB dbType="varchar(255)"><%= props.getProperty("pg1_curMedic", "") %></textarea>
			</td>
			<td valign="top" width="33%">Beliefs & Practices <input type="text"
				name="pg1_beliPract" style="width: 100%" size="60" maxlength="60"
				value="<%= props.getProperty("pg1_beliPract", "") %>" @oscar.formDB />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="6"><b>3. Obstetrical History G</b><span
				class="small8">ravida</span> <input type="text" name="pg1_gravida"
				style="width: 30px;" size="2" maxlength="4"
				value="<%= props.getProperty("pg1_gravida", "") %>" @oscar.formDB />
			<b>T</b><span class="small8">erm</span> <input type="text"
				name="pg1_term" style="width: 30px;" size="2" maxlength="4"
				value="<%= props.getProperty("pg1_term", "") %>" @oscar.formDB /> <b>P</b><span
				class="small8">reterm</span> <input type="text" name="pg1_preterm"
				style="width: 30px;" size="2" maxlength="4"
				value="<%= props.getProperty("pg1_preterm", "") %>" @oscar.formDB />
				
			<b>A</b><span class="small8">bortion </span> <%-- input type="text"
				name="pg1_abortion" style="width: 30px;" size="2" maxlength="3"
				value="<%= props.getProperty("pg1_abortion", "") %>" @oscar.formDB /--%>
				
			( <span class="small8">Induced</span> <input type="text"
				name="pg1_induced" style="width: 30px;" size="2" maxlength="3"
				value="<%= props.getProperty("pg1_induced", "") %>" @oscar.formDB />
				
			<span class="small8">Spontaneous</span> <input type="text"
				name="pg1_spontaneous" style="width: 30px;" size="2" maxlength="3"
				value="<%= props.getProperty("pg1_spontaneous", "") %>"
				@oscar.formDB /> ) <b>L</b><span class="small8">iving</span> <input
				type="text" name="pg1_living" style="width: 30px;" size="2"
				maxlength="3" value="<%= props.getProperty("pg1_living", "") %>"
				@oscar.formDB /></td>
			<td colspan="3" align="center"><b>Children</b></td>
		</tr>
		<tr>
			<th width="6%">Date</th>
			<th width="12%">Place of birth/<br>
			abortion</th>
			<th width="6%">Hrs. in<br>
			labour</th>
			<th width="5%">Gest.<br>
			age</th>
			<th width="12%">Type of<br>
			birth</th>

			<th width="33%">Perinatal Complications</th>
			<th width="3%">Sex</th>
			<th width="12%">Birth<br>
			weight</th>
			<th>Breastfed (y/n)</th>
			<th width="8%">Present<br>
			health</th>
		</tr>
		<tr>
			<td nowrap><input type="text" name="pg1_obHistDate1"
				id="pg1_obHistDate1" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate1", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate1_cal"></td>
				
			<td><input type="text" name="pg1_birthOrAbort1"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort1", "") %>"
				@oscar.formDB /></td>
				
			<td><input type="text" name="pg1_laboHr1" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr1", "") %>" @oscar.formDB />
			</td>
			
			<td><input type="text" name="pg1_deliWeek1" style="width: 100%"
				size="5" maxlength="6"
				value="<%= props.getProperty("pg1_deliWeek1", "") %>" @oscar.formDB />
			</td>
			
			<td><input type="text" name="pg1_deliType1" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="15"
				value="<%= props.getProperty("pg1_deliType1", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp1" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp1", "") %>" @oscar.formDB />
			</td>
			<td><%-- input type="text" name="pg1_obHistSex1" 
			style="width:100%" size="1" maxlength="1" 
			value="<%= props.getProperty("pg1_obHistSex1", "") %>" @oscar.formDB / --%>
			
			<select name="pg1_obHistSex1">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex1", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex1", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td align="center"><input type="text" name="pg1_birthWeit1" id="pg1_birthWeit1"
				size="4" maxlength="8"
				value="<%= props.getProperty("pg1_birthWeit1", "") %>" @oscar.formDB />
			<select name="pg1_birthWeitUnits1"
				onchange="convertWeightUnits(pg1_birthWeit1, pg1_birthWeitUnits1.value);">
				<option value="kg"
					<%=props.getProperty("pg1_birthWeitUnits1", "").equalsIgnoreCase("kg")?"selected":""%>>kg</option>
				<option value="lbs"
					<%=props.getProperty("pg1_birthWeitUnits1", "").equalsIgnoreCase("lbs")?"selected":""%>>lbs</option>
			</select></td>
			<td align="center">
				<input type="text" name="pg1_breastfed1" 
						size="3"
						maxlength="4"
						value="<%= props.getProperty("pg1_breastfed1", "") %>"
						@oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth1" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth1", "") %>"
				@oscar.formDB /></td>
		</tr>
		
		<tr>
			<td><input type="text" name="pg1_obHistDate2"
				id="pg1_obHistDate2" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate2", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate2_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort2"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort2", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_laboHr2" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr2", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliWeek2" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek2", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType2" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="30"
				value="<%= props.getProperty("pg1_deliType2", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp2" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp2", "") %>" @oscar.formDB />
			</td>
			<td><!-- input type="text" name="pg1_obHistSex2" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex2", "") %>" @oscar.formDB /-->
			<select name="pg1_obHistSex2">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex2", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex2", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td align="center"><input type="text" name="pg1_birthWeit2" size="4"
				maxlength="8" value="<%= props.getProperty("pg1_birthWeit2", "") %>"
				@oscar.formDB /> <select name="pg1_birthWeitUnits2"
				onchange="convertWeightUnits(pg1_birthWeit2, pg1_birthWeitUnits2.value);">
				<option value="kg"
					<%=props.getProperty("pg1_birthWeitUnits2", "").equalsIgnoreCase("kg")?"selected":""%>>kg</option>
				<option value="lbs"
					<%=props.getProperty("pg1_birthWeitUnits2", "").equalsIgnoreCase("lbs")?"selected":""%>>lbs</option>
			</select></td>
			<td align="center">
				<input type="text" name="pg1_breastfed2" 
						size="3"
						maxlength="4"
						value="<%= props.getProperty("pg1_breastfed2", "") %>"
						@oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth2" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth2", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate3"
				id="pg1_obHistDate3" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate3", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate3_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort3"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort3", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_laboHr3" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr3", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliWeek3" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek3", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType3" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="30"
				value="<%= props.getProperty("pg1_deliType3", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp3" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp3", "") %>" @oscar.formDB />
			</td>
			<td><select name="pg1_obHistSex3">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex3", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex3", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td align="center"><input type="text" name="pg1_birthWeit3" size="4"
				maxlength="8" value="<%= props.getProperty("pg1_birthWeit3", "") %>"
				@oscar.formDB /> <select name="pg1_birthWeitUnits3"
				onchange="convertWeightUnits(pg1_birthWeit3, pg1_birthWeitUnits3.value);">
				<option value="kg"
					<%=props.getProperty("pg1_birthWeitUnits3", "").equalsIgnoreCase("kg")?"selected":""%>>kg</option>
				<option value="lbs"
					<%=props.getProperty("pg1_birthWeitUnits3", "").equalsIgnoreCase("lbs")?"selected":""%>>lbs</option>
			</select></td>
			<td align="center">
				<input type="text" name="pg1_breastfed3" 
						size="3"
						maxlength="4"
						value="<%= props.getProperty("pg1_breastfed3", "") %>"
						@oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth3" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth3", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate4"
				id="pg1_obHistDate4" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate4", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate4_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort4"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort4", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_laboHr4" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr4", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliWeek4" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek4", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType4" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="30"
				value="<%= props.getProperty("pg1_deliType4", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp4" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp4", "") %>" @oscar.formDB />
			</td>
			<td><select name="pg1_obHistSex4">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex4", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex4", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td align="center"><input type="text" name="pg1_birthWeit4" size="4"
				maxlength="8" value="<%= props.getProperty("pg1_birthWeit4", "") %>"
				@oscar.formDB /> <select name="pg1_birthWeitUnits4"
				onchange="convertWeightUnits(pg1_birthWeit4, pg1_birthWeitUnits4.value);">
				<option value="kg"
					<%=props.getProperty("pg1_birthWeitUnits4", "").equalsIgnoreCase("kg")?"selected":""%>>kg</option>
				<option value="lbs"
					<%=props.getProperty("pg1_birthWeitUnits4", "").equalsIgnoreCase("lbs")?"selected":""%>>lbs</option>
			</select></td>
			<td align="center">
				<input type="text" name="pg1_breastfed4" 
						size="3"
						maxlength="4"
						value="<%= props.getProperty("pg1_breastfed4", "") %>"
						@oscar.formDB />
			</td>
			
			<td><input type="text" name="pg1_presHealth4" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth4", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate5"
				id="pg1_obHistDate5" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate5", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate5_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort5"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort5", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_laboHr5" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr5", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliWeek5" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek5", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType5" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="30"
				value="<%= props.getProperty("pg1_deliType5", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp5" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp5", "") %>" @oscar.formDB />
			</td>
			<td><select name="pg1_obHistSex5">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex5", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex5", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td align="center"><input type="text" name="pg1_birthWeit5" size="4"
				maxlength="8" value="<%= props.getProperty("pg1_birthWeit5", "") %>"
				@oscar.formDB /> <select name="pg1_birthWeitUnits5"
				onchange="convertWeightUnits(pg1_birthWeit5, pg1_birthWeitUnits5.value);">
				<option value="kg"
					<%=props.getProperty("pg1_birthWeitUnits5", "").equalsIgnoreCase("kg")?"selected":""%>>kg</option>
				<option value="lbs"
					<%=props.getProperty("pg1_birthWeitUnits5", "").equalsIgnoreCase("lbs")?"selected":""%>>lbs</option>
			</select></td>
			
			<td align="center">
				<input type="text" name="pg1_breastfed5" 
						size="3"
						maxlength="4"
						value="<%= props.getProperty("pg1_breastfed5", "") %>"
						@oscar.formDB />
			</td>
			
			<td><input type="text" name="pg1_presHealth5" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth5", "") %>"
				@oscar.formDB /></td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr valign="bottom">
			<td valign="bottom"><b>4.</b> 
			<span class="small9">LMP 
			<br />
			
			(dd/mm/yyyy)</span>
			
			
			<br />
			<input type="text" name="pg1_lmp" id="pg1_lmp" style="margin-top:3px;" size="10" maxlength="10"
				value="<%= props.getProperty("pg1_lmp", "") %>" @oscar.formDB
				dbType="date" /><img src="../images/cal.gif" id="pg1_lmp_cal">
				
				</td>
				
			<td valign="bottom">
			
			<span class="small9">Menses Cycle</span>  <br />
			
			<input type="text" name="pg1_mensCycle"  size="8" style="margin-top:3px;"
				maxlength="8" value="<%= props.getProperty("pg1_mensCycle", "") %>"
				@oscar.formDB />
				
				</td>
			<td valign="bottom">
			
			<table border="0">
				<tr>
					<td width="50%" valign="bottom">
					
					<span class="small9">Contraception Method:</span> 
					
					<br />
					<input type="text" name="pg1_contrMethod" id="pg1_contrMethod"
						class="spe" onDblClick="showHideBox('Contradiv',1);"
							style="margin-top:3px;"
						 size="12" maxlength="15"
						value="<%= props.getProperty("pg1_contrMethod", "") %>"
						@oscar.formDB />
						
						</td>
					<td>
					
					<span class="small9">When Stopped:
					<br />
					(dd/mm/yyyy)</span> 
					
					<br />
					<input type="text" name="pg1_stopDate" id="pg1_stopDate"
					style="margin-top:3px;"
						 size="10" maxlength="10"
						value="<%= props.getProperty("pg1_stopDate", "") %>" @oscar.formDB />
						<img src="../images/cal.gif" id="pg1_stopDate_cal">
					</td>
				</tr>
			</table>
			</td>
			<td valign="bottom">
			
			<span class="small9">EDD by Dates
			<br />
			(dd/mm/yyyy)</span>
			
			<br />
			<input type="text" 
				name="pg1_eddByDate" 
				id="pg1_eddByDate"
				class="spe"
				size="10" maxlength="10"
				style="margin-top:3px;"
				onDblClick="calByLMP();"
				value="<%= props.getProperty("pg1_eddByDate", "") %>" @oscar.formDB dbType="date" />

			</td>
			
			<td valign="bottom">
			
			<span class="small9">Confirmed EDD 
			<br />
			(dd/mm/yyyy)</span>
			
			<br />
			<input type="text" name="pg1_eddByUs" id="pg1_eddByUs" size="10"
			style="margin-top:3px;"
				maxlength="10" value="<%= props.getProperty("pg1_eddByUs", "") %>"
				@oscar.formDB dbType="date"
				/><img src="../images/cal.gif" id="pg1_eddByUs_cal" />
				
            <%-- 
            	Modified by Dennis Warren @ Treatment March 2012
            	input type="checkbox" name="pg1_eddByUsPerf" <%= props.getProperty("pg1_eddByUsPerf", "") %>  @oscar.formDB dbType="tinyint(1)" />
				US performed
				<input type="text" name="pg1_eddByUsGestWks" style="width:20px;" maxlength="2" value="<%= props.getProperty("pg1_eddByUsGestWks", "") %>" @oscar.formDB />
				Gest wks.
				<input type="text" name="pg1_eddByUsGestDays" style="width:20px;" maxlength="2" value="<%= props.getProperty("pg1_eddByUsGestDays", "") %>" @oscar.formDB />
				days --%>
				
				</td>
				
				<td valign="bottom" width="30%">
					<div style="display:block; float:left; ">
					
					<span class="small9">1st US	<br /> (dd/mm/yyyy)</span>
					
					<br />
					<input type="text" 
						name="pg1_firstUsPerf"
						id="pg1_firstUsPerf"
						size="10"
						maxlength="10" 
						style="margin-top:3px;"
						value="<%= props.getProperty("pg1_firstUsPerf", "") %>" 
						@oscar.formDB dbType="date" />
						<img src="../images/cal.gif" id="pg1_firstUsPerf_cal" />
					</div>
					
					<div style="display:block; float:left; margin-left:20px; margin-top:10px;">
					<span class="small9">GA by US (weeks + days)</span>
					<br />
					<span class="small9" style="margin-top:3px;" > Weeks
					<input type="text" name="pg1_eddByUsGestWks" style="width:20px;" maxlength="2" value="<%= props.getProperty("pg1_eddByUsGestWks", "") %>" @oscar.formDB />
					</span>
					<span class="small9" style="margin-top:3px;" >Days
					<input type="text" name="pg1_eddByUsGestDays" style="width:20px;" maxlength="2" value="<%= props.getProperty("pg1_eddByUsGestDays", "") %>" @oscar.formDB />
					</span>
					</div>
					
				
				</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="33%" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>

					<table class="shrinkMe" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="3"><b>5. Present Pregnancy</b></td>
						</tr>
						<tr>
							<td colspan="2"><i>no</i></td>
							<td align="center"><i>yes (specify)</i></td>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox"
								name="pg1_IVFpregnancy"
								<%= props.getProperty("pg1_IVFpregnancy", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="30%"><span class="small9">IVF Pregnancy</span></td>
							<td><input type="text" name="pg1_IVFpregnancySpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_IVFpregnancySpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_bleeding"
								<%= props.getProperty("pg1_bleeding", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">Bleeding</span></td>
							<td><input type="text" name="pg1_bleedingSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_bleedingSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_nausea"
								<%= props.getProperty("pg1_nausea", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">Nausea</span></td>
							<td><input type="text" name="pg1_nauseaSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_nauseaSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_infect"
								<%= props.getProperty("pg1_infect", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">Infections<br>
							or Fever</span></td>
							<td><input type="text" name="pg1_infectSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_infectSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_prePregOther"
								<%= props.getProperty("pg1_prePregOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">Other</span></td>
							<td><input type="text" name="pg1_prePregOtherSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_prePregOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="3" nowrap><b>6. Family History</b></td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;<i>no</i></td>
							<td align="center"><i>yes (specify)</i></td>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox" name="pg1_heartDise"
								<%= props.getProperty("pg1_heartDise", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="30%"><span class="small8">Heart Disease</span></td>
							<td><input type="text" name="pg1_heartDiseSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_heartDiseSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_hyperts"
								<%= props.getProperty("pg1_hyperts", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Hypertension</span></td>
							<td><input type="text" name="pg1_hypertsSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_hypertsSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_diabete"
								<%= props.getProperty("pg1_diabete", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Diabetes</span></td>
							<td><input type="text" name="pg1_diabeteSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_diabeteSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_deprPsychiat"
								<%= props.getProperty("pg1_deprPsychiat", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Depression or<br>
							Psychiatric</span></td>
							<td><input type="text" name="pg1_deprPsychiatSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_deprPsychiatSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_alcohDrug"
								<%= props.getProperty("pg1_alcohDrug", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Alcohol/<br>
							Drug use</span></td>
							<td><input type="text" name="pg1_alcohDrugSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_alcohDrugSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_thromCoag"
								<%= props.getProperty("pg1_thromCoag", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Thromboembolic / Coag.</span></td>
							<td><input type="text" name="pg1_thromCoagSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_thromCoagSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td><span class="small8" style="float: left;">Maternal</span><span
								class="small8" style="float: right;">Newborn's Father</span></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_inherDisease"
								<%= props.getProperty("pg1_inherDisease", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Inherited<br>
							Disease/Defect</span></td>
							<td><input type="text" name="pg1_inherDiseaseSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_inherDiseaseSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_ethnic"
								<%= props.getProperty("pg1_ethnic", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Ethnic ( e.g.<br>
							Taysachs, Sickle)</span></td>
							<td><input type="text" name="pg1_ethnicSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_ethnicSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_famHistOther"
								<%= props.getProperty("pg1_famHistOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Other</span></td>
							<td><input type="text" name="pg1_famHistOtherSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_famHistOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
				</tr>
			</table>


			</td>
			<td width="33%" valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3"><b>7. Medical History</b></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;<i>no</i></td>
					<td align="center"><i>yes (specify)</i></td>
				</tr>
				<tr>
					<td width="1%" valign="top"><input type="checkbox"
						name="pg1_operation"
						<%= props.getProperty("pg1_operation", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="30%" valign="top"><span class="small8">Surgery</span></td>
					<td><textarea name="pg1_operationSpec" style="width: 100%"
						cols="60" rows="1" @oscar.formDB dbType="varchar(255)"><%= props.getProperty("pg1_operationSpec", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_anesthetic"
						<%= props.getProperty("pg1_anesthetic", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Anesthesia</span></td>
					<td><input type="text" name="pg1_anestheticSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_anestheticSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="pg1_uterineCxProc"
						<%= props.getProperty("pg1_uterineCxProc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Uterine/Cx procedure</span></td>
					<td><input type="text" name="pg1_uterineCxProcSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_uterineCxProcSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<%--tr>
					<td valign="top"><input type="checkbox" name="pg1_cvOrResp"
						<%= props.getProperty("pg1_cvOrResp", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td valign="top"><span class="small8">RESP. OR CV</span></td>
					<td><input type="text" name="pg1_cvOrRespSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_cvOrRespSpec", "") %>"
						@oscar.formDB /></td>
					</td>
				</tr --%>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_infectStd"
						<%= props.getProperty("pg1_infectStd", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">STIs / infections</span></td>
					<td><input type="text" name="pg1_infectStdSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_infectStdSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_susChiPox"
						<%= props.getProperty("pg1_susChiPox", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Susceptible to chicken pox</span></td>
					<td><input type="text" name="pg1_susChiPoxSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_susChiPoxSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_thrCoag"
						<%= props.getProperty("pg1_thrCoag", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Thromboembolic / coag.</span></td>
					<td><input type="text" name="pg1_thrCoagSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_thrCoagSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_hyper"
						<%= props.getProperty("pg1_hyper", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Hypertension</span></td>
					<td><input type="text" name="pg1_hyperSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_hyperSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_piGi"
						<%= props.getProperty("pg1_piGi", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">GI</span></td>
					<td><input type="text" name="pg1_piGiSpec" style="width: 100%"
						size="40" maxlength="40"
						value="<%= props.getProperty("pg1_piGiSpec", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_piUrin"
						<%= props.getProperty("pg1_piUrin", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Urinary</span></td>
					<td><input type="text" name="pg1_piUrinSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_piUrinSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_dbEndoc"
						<%= props.getProperty("pg1_dbEndoc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Endocrine/diabetes</span></td>
					<td><input type="text" name="pg1_dbEndocSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_dbEndocSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_seizNeur"
						<%= props.getProperty("pg1_seizNeur", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Neurologic</span></td>
					<td><input type="text" name="pg1_seizNeurSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_seizNeurSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_deprPsy"
						<%= props.getProperty("pg1_deprPsy", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td valign="top"><span class="small8">Hx of mental illness</span></td>
					
					<td><textarea name="pg1_deprPsySpec" style="width: 100%"
						cols="60" rows="1" @oscar.formDB dbType="varchar(255)"><%= props.getProperty("pg1_deprPsySpec", "") %></textarea>
				</tr>
				<tr>
					<td></td>
					<td colspan="2" align="right">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td valign="top" width="1%"><input type="checkbox"
								name="pg1_hxAnxiety"
								<%= props.getProperty("pg1_hxAnxiety", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							</td>
							<td><span class="small8">Anxiety</span></td>
							<td valign="top" width="1%"><input type="checkbox"
								name="pg1_hxDepr"
								<%= props.getProperty("pg1_hxDepr", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							</td>
							<td><span class="small8">Depression</span></td>
							<td valign="top" width="1%"><input type="checkbox"
								name="pg1_hxBipolar"
								<%= props.getProperty("pg1_hxBipolar", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							</td>
							<td><span class="small8">Bipolar</span></td>
						</tr>
						<tr>
							<td valign="top"><input type="checkbox" name="pg1_hxPPDepr"
								<%= props.getProperty("pg1_hxPPDepr", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							</td>
							<td><span class="small8">PP depression</span></td>
							<td valign="top"><input type="checkbox"
								name="pg1_hxUnknown"
								<%= props.getProperty("pg1_hxUnknown", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							</td>
							<td><span class="small8">Unknown</span></td>
							<td valign="top"><input type="checkbox" name="pg1_hxOther"
								<%= props.getProperty("pg1_hxOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							</td>
							<td><span class="small8">Other</span></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_piOther"
						<%= props.getProperty("pg1_piOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Other</span></td>
					<td><input type="text" name="pg1_piOtherSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_piOtherSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4"><b>8. Lifestyle &amp; Social</b></td>
						</tr>
						<tr>
							<td colspan="2" align="left"><i><span class="small8">Discussed</span></i></td>
							<td align="center"><i><span class="small8">Concerns</span></i></td>
							<td align="right"><i><span class="small8">Referred</span></i></td>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox" name="pg1_nutrition"
								<%= props.getProperty("pg1_nutrition", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="30%"><span class="small8">Diet</span></td>
							<td><input type="text" name="pg1_nutritionSpec"
								style="width: 100%" size="32" maxlength="40"
								value="<%= props.getProperty("pg1_nutritionSpec", "") %>"
								@oscar.formDB /></td>
							<td width="1%" align="right"><input type="checkbox"
								name="pg1_nutritionRef"
								<%= props.getProperty("pg1_nutritionRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
						</tr>
						
						<tr>
							<td><input type="checkbox" name="pg1_foliAcid"
								<%= props.getProperty("pg1_foliAcid", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Folic Acid</span></td>
							<td><input type="text" name="pg1_foliAcidSpec"
								style="width: 100%" size="10" maxlength="30"
								value="<%= props.getProperty("pg1_foliAcidSpec", "") %>"
								@oscar.formDB /></td>
							<td align="right"><input type="checkbox"
								name="pg1_foliAcidRef"
								<%= props.getProperty("pg1_foliAcidRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_stpWrkDate"
								<%= props.getProperty("pg1_stpWrkDate", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Physical activity /<br>
							rest / stop work date</span></td>
							<td><input type="text" name="pg1_stpWrkDateSpec"
								style="width: 100%" size="10" maxlength="30"
								value="<%= props.getProperty("pg1_stpWrkDateSpec", "") %>"
								@oscar.formDB /></td>
							<td align="right"><input type="checkbox"
								name="pg1_stpWrkDateRef"
								<%= props.getProperty("pg1_stpWrkDateRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_drug"
								<%= props.getProperty("pg1_drug", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">OTC drugs / vitamins</span></td>
							<td><input type="text" name="pg1_drugSpec"
								style="width: 100%" size="22" maxlength="28"
								value="<%= props.getProperty("pg1_drugSpec", "") %>"
								@oscar.formDB /></td>
							<td align="right"><input type="checkbox" name="pg1_drugRef"
								<%= props.getProperty("pg1_drugRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
						</tr>
						<tr>
							<td colspan="3">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="3"></td>
									<td><span class="small8">dd/mm/yyyy</span><img
										src="../images/cal.gif" id="pg1_alcoQuitDate_cal"></td>
								</tr>
								<tr>
									<td><input type="checkbox" name="pg1_alco"
										<%= props.getProperty("pg1_alco", "") %>  @oscar.formDB
										dbType="tinyint(1)" /><span class="small8">Alcohol</span></td>
									<td><input type="checkbox" name="pg1_alcoNever"
										<%= props.getProperty("pg1_alcoNever", "") %>  @oscar.formDB
										dbType="tinyint(1)" /><span class="small8">never</span></td>
									<td><input type="checkbox" name="pg1_alcoQuit"
										<%= props.getProperty("pg1_alcoQuit", "") %>  @oscar.formDB
										dbType="tinyint(1)" /><span class="small8">quit</span></td>
									<td><input type="text" name="pg1_alcoQuitDate"
										id="pg1_alcoQuitDate" style="width: 100%" size="10"
										maxlength="10"
										value="<%= props.getProperty("pg1_alcoQuitDate", "") %>"
										@oscar.formDB dbType="date" /></td>
								</tr>
							</table>
							</td>
							<td width="1%" align="right"><input type="checkbox"
								name="pg1_alcoholRef"
								<%= props.getProperty("pg1_alcoholRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><span class="small8">Drinks/wk:
							before pregnancy</span> <input type="text" name="pg1_alcoBef" size="2"
								maxlength="5"
								value="<%= props.getProperty("pg1_alcoBef", "") %>"
								@oscar.formDB /> <span class="small8">current</span><input
								type="text" name="pg1_alcoCurr" size="2" maxlength="5"
								value="<%= props.getProperty("pg1_alcoCurr", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><span class="small8">Binge drinking</span> <input
								type="checkbox" name="pg1_alcoBingeNo"
								<%= props.getProperty("pg1_alcoBingeNo", "") %>  @oscar.formDB
								dbType="tinyint(1)" /><span class="small8">No</span> <input
								type="checkbox" name="pg1_alcoBingeYes"
								<%= props.getProperty("pg1_alcoBingeYes", "") %>  @oscar.formDB
								dbType="tinyint(1)" /><span class="small8">Yes</span></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_tweak"
								<%= props.getProperty("pg1_tweak", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">TWEAK score</span></td>
							<td><input type="text" name="pg1_tweakScore"
								style="width: 100%" size="15" maxlength="15"
								value="<%= props.getProperty("pg1_tweakScore", "") %>"
								@oscar.formDB /></td>
							<td></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_subUse"
								<%= props.getProperty("pg1_subUse", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">Substance use</span></td>
							<td>
								<input type="checkbox" name="pg1_subUseNo" />
								<%= props.getProperty("pg1_subUseNo", "") %>
								
								<span class="small8">No</span>
									<input type="checkbox" name="pg1_subUseYes" <%= props.getProperty("pg1_subUseYes", "") %> />
								<span class="small8">Yes</span>
								
                                </td>
                                <td width="1%" align="right"><input type="checkbox"
								name="pg1_substanceRef"
								<%= props.getProperty("pg1_substanceRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
                            </tr><tr>
                                <td></td>
                                <td colspan="2"><input type="text" name="pg1_subUseSpec" style="width:100%" size="28" maxlength="35" value="<%= props.getProperty("pg1_subUseSpec", "") %>" @oscar.formDB /></td>
                                <td></td>
                            </tr><tr>
                                <td></td>
                                <td colspan="3">
                                    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td><input type="checkbox" name="pg1_heroin" <%= props.getProperty("pg1_heroin", "") %> />
                                            <span class="small8">Heroin</span></td>
                                            <td><input type="checkbox" name="pg1_cocaine" <%= props.getProperty("pg1_cocaine", "") %> />
                                            <span class="small8">Cocaine</span></td>
                                            <td><input type="checkbox" name="pg1_marijuana" <%= props.getProperty("pg1_marijuana", "") %> />
                                            <span class="small8">Marijuana</span></td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" name="pg1_methadone" <%= props.getProperty("pg1_methadone", "") %> />
                                            <span class="small8">Methadone</span></td>
                                            <td><input type="checkbox" name="pg1_solvents" <%= props.getProperty("pg1_solvents", "") %> />
                                            <span class="small8">Solvents</span></td>
                                            <td><input type="checkbox" name="pg1_subUseOther" <%= props.getProperty("pg1_subUseOther", "") %> />
                                            <span class="small8">Other</span></td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" name="pg1_prescription" <%= props.getProperty("pg1_prescription", "") %> />
                                            <span class="small8">Prescription</span></td>
                                            <td><input type="checkbox" name="pg1_subUseUnknown" <%= props.getProperty("pg1_subUseUnknown", "") %> />
                                            <span class="small8">Unknown</span></td>
                                            <td></td>
                                        </tr>
                                        
                                    </table>
                                </td>
                                
                            </tr><tr>
                                <td colspan="3">
                                    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td colspan="3"></td>
                                            <td><span class="small8">dd/mm/yyyy</span><img src="../images/cal.gif" id="pg1_smokeQuitDate_cal"></td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" name="pg1_smokeCur" <%= props.getProperty("pg1_smokeCur", "") %>  @oscar.formDB dbType="tinyint(1)"/><span class="small8">Smoking</span></td>
                                            <td><input type="checkbox" name="pg1_smokeNever" <%= props.getProperty("pg1_smokeNever", "") %>  @oscar.formDB dbType="tinyint(1)"/><span class="small8">never</span></td>
                                            <td><input type="checkbox" name="pg1_smokeQuit" <%= props.getProperty("pg1_smokeQuit", "") %>  @oscar.formDB dbType="tinyint(1)"/><span class="small8">quit</span></td>
                                            <td><input type="text" name="pg1_smokeQuitDate" id="pg1_smokeQuitDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_smokeQuitDate", "") %>" @oscar.formDB dbType="date" /></td>
                                        </tr>
                                        
                                    </table>
                                </td>
                                <td></td>
                            </tr><tr>
                                <td></td>
                                <td colspan="2"><span class="small8">Cig/day: before pregnancy</span>
                                    <input type="text" name="pg1_smokeBefSpec" size="2" maxlength="5" value="<%= props.getProperty("pg1_smokeBefSpec", "") %>" @oscar.formDB />
                                    <span class="small8">current</span><input type="text" name="pg1_smokeCurSpec" size="2" maxlength="5" value="<%= props.getProperty("pg1_smokeCurSpec", "") %>" @oscar.formDB />
                                </td>
                                <td width="1%" align="right">
                                <input type="checkbox"
								name="pg1_smokingRef"
								<%= props.getProperty("pg1_smokingRef", "") %>  @oscar.formDB dbType="tinyint(1)" /></td>
                            </tr><tr>
                                <td><input type="checkbox" name="pg1_secSmoke" <%= props.getProperty("pg1_secSmoke", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                                <td><span class="small8">Exposure 2<sup>nd</sup> hand smoke</span></td>
                                <td><input type="checkbox" name="pg1_secSmokeNo" <%= props.getProperty("pg1_secSmokeNo", "") %>  @oscar.formDB dbType="tinyint(1)"/><span class="small8">No</span>
                                <input type="checkbox" name="pg1_secSmokeYes" <%= props.getProperty("pg1_secSmokeYes", "") %>  @oscar.formDB dbType="tinyint(1)"/><span class="small8">Yes</span>
                                <input type="text" name="pg1_secSmokeSpec" style="width:50%" size="10" maxlength="15" value="<%= props.getProperty("pg1_secSmokeSpec", "") %>" @oscar.formDB />
                                       </td>
                            </tr><tr>
                                <td><input type="checkbox" name="pg1_finaHouse" <%= props.getProperty("pg1_finaHouse", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                                <td><span class="small8">Financial &amp; Housing</span></td>
                                <td><input type="text" name="pg1_finaHouseSpec" style="width:100%" size="26" maxlength="29" value="<%= props.getProperty("pg1_finaHouseSpec", "") %>" @oscar.formDB /></td>
                                <td align="right"><input type="checkbox" name="pg1_finaHouseRef" <%= props.getProperty("pg1_finaHouseRef", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                            </tr><tr>
                                <td><input type="checkbox" name="pg1_supSys" <%= props.getProperty("pg1_supSys", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                                <td><span class="small8">Support system</span></td>
                                <td><input type="text" name="pg1_supSysSpec" style="width:100%" size="26" maxlength="30" value="<%= props.getProperty("pg1_supSysSpec", "") %>" @oscar.formDB /></td>
                                <td align="right"><input type="checkbox" name="pg1_supSysRef" <%= props.getProperty("pg1_supSysRef", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                            </tr><tr>
                                <td><input type="checkbox" name="pg1_ipv" <%= props.getProperty("pg1_ipv", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                                
                                <td><span class="small8">IPV</span></td>
                                <td><input type="text" name="pg1_ipvSpec" style="width:100%" size="26" maxlength="40" value="<%= props.getProperty("pg1_ipvSpec", "") %>" @oscar.formDB /></td>
                                
                                <td align="right"><input type="checkbox" name="pg1_ipvRef" <%= props.getProperty("pg1_ipvRef", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
                                
                            </tr>
                            <%-- Addition of data item: Dennis Warren @ Treatment March 2012 --%>
                            <tr>
                            	<td>
                            		<input type="checkbox" name="pg1_phnFu" id="pg1_phnFu" <%= props.getProperty("pg1_phnFu", "") %>  @oscar.formDB dbType="tinyint(1)"/>
                            	</td>
                            	<td colspan="2">
                            		<span class="small8">Public Health Nursing follow-up/assessment</span>                    		
                            	</td>
                            	<!--  td>
                            		<input type="text" name="pg1_" id="pg1_" style="width:100%" size="26" maxlength="40" />
                            	</td -->
                            	<td align="right">
                            		<input type="checkbox" name="pg1_phnRef" id="pg1_phnRef" <%= props.getProperty("pg1_phnRef", "") %>  @oscar.formDB dbType="tinyint(1)"/>
                            	</td>
                            </tr>
                            
                            
                        </table>
                        
                    </td>
                </tr>
            </table>
            
        </td>
    </tr>
</table>

<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
<td width="50%" valign="top">
    
    <table class="shrinkMe" width="100%" border="0"  cellspacing="0" cellpadding="0">
        <tr>
            <td colspan="3"><b>9. Examination</b></td> 
        </tr>
        <tr>
            <td colspan="3">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td>dd/mm/yyyy <img src="../images/cal.gif" id="pg1_examination_cal"></td>
                        <td width="10%">BP</td>
                        <td>Height</td>
                        <td>Pre-pregnant weight</td>
                        <td><a href="javascript: function myFunction() {return false; }" onclick="showHideBox('BMIdiv',1); return false;" title='The height and weight MUST be in metric for the BMI to calculate when you double click in the shaded cell.  If putting in weight or height in Standard measurement, double click each cell to convert to metric. Then, double click in the BMI cell to calculate. Do not put any text in the height or weight cells (kg.) or it will not calculate the BMI.'>
                        <font color='red'><b>Pre-pregnant BMI</b></color></a></td>
                    </tr>
                    <tr>
                        <td><input type="text" name="pg1_examination" id="pg1_examination" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_examination", "") %>" @oscar.formDB  dbType="date"/></td>
                        <td><input type="text" name="pg1_bp"  style="width:100%" size="10" maxlength="12" value="<%= props.getProperty("pg1_bp", "") %>" @oscar.formDB /></td>
                        <td><input type="text" name="c_ppHt"  class="spe" style="width:100%;" onDblClick="htEnglish2Metric();" size="5" maxlength="5" value="<%= props.getProperty("c_ppHt", "") %>" @oscar.formDB /></td>
                        <td><input type="text" name="c_ppWt"  style="width:100%;" class="spe" onDblClick="wtEnglish2Metric(this);" size="5" maxlength="5" value="<%= props.getProperty("c_ppWt", "") %>" @oscar.formDB /></td>
                        <td><input type="text" name="c_ppBMI"  class="spe" style="width:100%;" onDblClick="calcBMIMetric();" size="5" maxlength="5" value="<%= props.getProperty("c_ppBMI", "") %>" @oscar.formDB /></td>
                    </tr>
                </table>                
            </td>
        </tr><tr>
            <td width="48%"><span class="small9">Head &amp neck</span><br>
                <input type="text" name="pg1_headNeck"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="35" maxlength="40" value="<%= props.getProperty("pg1_headNeck", "") %>" @oscar.formDB />
                   </td>
            <td width="4%"></td>
            <td width="48%"><span class="small9">Muscoloskeletal</span><br>
                <input type="text" name="pg1_muscSpine"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="30" maxlength="40" value="<%= props.getProperty("pg1_muscSpine", "") %>" @oscar.formDB />
                   </td>
        </tr><tr>
            <td><span class="small9">Breasts &amp; nipples</span><br>
                <input type="text" name="pg1_breaNipp"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="35" maxlength="40" value="<%= props.getProperty("pg1_breaNipp", "") %>" @oscar.formDB />
                   </td>
            <td></td>
            <td><span class="small9">Varicies &amp; skin</span><br>
                <input type="text" name="pg1_variSkin"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="30" maxlength="40" value="<%= props.getProperty("pg1_variSkin", "") %>" @oscar.formDB />
                   </td>
        </tr><tr>
            <td><span class="small9">Heart &amp; lungs</span><br>
                <input type="text" name="pg1_heartLung"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="35" maxlength="40" value="<%= props.getProperty("pg1_heartLung", "") %>" @oscar.formDB />
                   </td>
            <td></td>
            <td><span class="small9">Pelvic exam</span><br>
                <input type="text" name="pg1_pelvic"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="30" maxlength="40" value="<%= props.getProperty("pg1_pelvic", "") %>" @oscar.formDB />
                   </td>
        </tr><tr>
            <td><span class="small9">Abdomen</span><br>
                <input type="text" name="pg1_abdomen"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="35" maxlength="40" value="<%= props.getProperty("pg1_abdomen", "") %>" @oscar.formDB />
                   </td>
            <td></td>
            <td><span class="small9">Swabs/cervix cytology</span><br>
                <input type="text" name="pg1_swabsCerv"  class="spe" onDblClick='showDef("NAD", this);' style="width:100%" size="30" maxlength="40" value="<%= props.getProperty("pg1_swabsCerv", "") %>" @oscar.formDB />
                   </td>
        </tr>
    </table>
    
</td>
<td valign="top">
    
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
        <tr>
            <td colspan="2"><b>10. <span class="small9">First Trimester Topics Discussed</span></b></td>
            <td colspan="2" align="right"><span class="small8">Plans to breastfeed</span></td>
        </tr>
        <tr>
            <td>
                <input type="checkbox" name="pg1_disMSS" <%= props.getProperty("pg1_disMSS", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">MSS offered</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disGenCouns" <%= props.getProperty("pg1_disGenCouns", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Genetic Counselling</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disHIV" <%= props.getProperty("pg1_disHIV", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">HIV &amp other tests</span>
            </td>
            <td width="15%">
                <input type="checkbox" name="pg1_disBfY" <%= props.getProperty("pg1_disBfY", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">yes</span>
            </td>
        </tr>
        <tr>
            <td>
                <input type="checkbox" name="pg1_disBestCh" <%= props.getProperty("pg1_disBestCh", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Baby's Best Chance</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disPreEdu" <%= props.getProperty("pg1_disPreEdu", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Prenatal Education</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disMatPat" <%= props.getProperty("pg1_disMatPat", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Maternity pathway</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disBfN" <%= props.getProperty("pg1_disBfN", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">no</span>
            </td>
        </tr>
        <tr>
            <td>
                <input type="checkbox" name="pg1_disBeltUse" <%= props.getProperty("pg1_disBeltUse", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Seat belt use</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disSexu" <%= props.getProperty("pg1_disSexu", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Sexual Relations</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disFdSafety" <%= props.getProperty("pg1_disFdSafety", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">Food Safety</span>
            </td>
            <td>
                <input type="checkbox" name="pg1_disBfM" <%= props.getProperty("pg1_disBfM", "") %> @oscar.formDB dbType="tinyint(1)"/>
                <span class="small8">maybe</span>
            </td>
        </tr>
    <tr>
        <td colspan="4"><b>11. Summary</b></td>
    </tr>
    <tr>
        <td colspan="4">
            <textarea name="pg1_summary" style="width:100%" cols="100" rows="3" @oscar.formDB dbType="text" ><%= props.getProperty("pg1_summary", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td colspan="4" align="right">SIGNATURE: 
        <input type="text" name="pg1_signature" size="35" maxlength="40" value="<%= props.getProperty("pg1_signature", "") %>" @oscar.formDB/>   MD/RM</td>
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
            <input type="submit" style="width:40px;" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <%
            }
            %>
            <input type="submit" style="width:40px;" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" style="width:50px;" value="Print" onclick="javascript:return onPrint();"/>
            <input type="submit" style="width:75px;" value="Print Risk" onclick="javascript:return onPrintRisk();"/>
            <input type="submit" value="Print AR1 & AR2" onclick="javascript:return onPrint12();"/>
            <input type="submit" style="width:75px;" value="Print All" onclick="javascript:return onPrintAll();"/>
        </td>
        <%
        if (!bView) {
        %>
        <td>
            <!--a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a-->
							</td>
							<td align="right"><b>View:</b> <a
								href="javascript: popupPage('formbcar2012pg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
							<font size=-2>(pg.1)</font></a> | <a
								href="javascript: popupPage('formbcar2012pg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
							<font size=-2>(pg.2)</font></a> &nbsp;</td>
							<td align="right"><b>Edit:</b>AR1 | <a
								href="formbcar2012pg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
							<font size=-2>(pg.1)</font></a> | <a
								href="formbcar2012pg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
							<font size=-2>(pg.2)</font></a> | <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR Planner</a-->
							</td>
							<%
        }
        %>
						</tr>
					</table>


					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<th align="center">RISK IDENTIFICATION</th>
						</tr>
						<tr>
							<td>

							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td valign="top">

									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<th colspan="2" align="left">PAST OBSTETRICAL HISTORY</th>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskAbortion"
												<%= props.getProperty("ar2_riskAbortion", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Abortion (12 - 20 weeks)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPriCesBirth"
												<%= props.getProperty("ar2_riskPriCesBirth", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Cesarean birth (uterine surgery)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskHabitAbort"
												<%= props.getProperty("ar2_riskHabitAbort", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Habitual abortion (3+)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskHypDisorder"
												<%= props.getProperty("ar2_riskHypDisorder", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Hypertensive disorders of pregnancy</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPriIUGR"
												<%= props.getProperty("ar2_riskPriIUGR", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>IUGR baby</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPriMacr"
												<%= props.getProperty("ar2_riskPriMacr", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Macrosomic baby</td>
										</tr>
										<tr>
											<td valign="top"><input type="checkbox"
												name="ar2_riskMajCongAnom"
												<%= props.getProperty("ar2_riskMajCongAnom", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Major congenital anomalies<br>
											(e.g. Cardiac, CNS, Down's Syndrome)</td>
										</tr>
										<tr>
											<td width="1%"><input type="checkbox"
												name="ar2_riskNeonDeath"
												<%= props.getProperty("ar2_riskNeonDeath", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Neonatal death</td>
										</tr>
										<tr>
											<td width="1%"><input type="checkbox"
												name="ar2_riskPlacAbruption"
												<%= props.getProperty("ar2_riskPlacAbruption", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Placental abruption</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPPHemo"
												<%= props.getProperty("ar2_riskPPHemo", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Postpartum Hemorrhage</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPriPretBirth20"
												<%= props.getProperty("ar2_riskPriPretBirth20", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Preterm birth (< 37 weeks)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskRhImmuY"
												<%= props.getProperty("ar2_riskRhImmuY", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Rh isoimmunization (affected infant)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskRhImmuN"
												<%= props.getProperty("ar2_riskRhImmuN", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Rh isoimmunization (unaffected infant)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskStillbirth"
												<%= props.getProperty("ar2_riskStillbirth", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Stillbirth</td>
										</tr>
									</table>

									</td>
									<td valign="top">

									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<th colspan="2" align="left">MEDICAL HISTORY RISK
											FACTORS</th>
										</tr>
										<tr>
											<th colspan="2" align="left"><br>
											<span class="small9">DIABETES</span></th>
										</tr>
										<tr>
											<td width="1%"><input type="checkbox"
												name="ar2_riskConDiet"
												<%= props.getProperty("ar2_riskConDiet", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Controlled by diet only</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskInsDepend"
												<%= props.getProperty("ar2_riskInsDepend", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Insulin dependent</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskRetDoc"
												<%= props.getProperty("ar2_riskRetDoc", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Retinopathy documented</td>
										</tr>
										<tr>
											<th colspan="2" align="left"><span class="small9">HEART
											DISEASE</span></th>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskAsymt"
												<%= props.getProperty("ar2_riskAsymt", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Asymptomatic (no effect on daily living)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskSymt"
												<%= props.getProperty("ar2_riskSymt", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Symptomatic (affects daily living)</td>
										</tr>
										<tr>
											<th colspan="2" align="left"><span class="small9">HYPERTENSION</span></th>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_risk14090"
												<%= props.getProperty("ar2_risk14090", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>140/90 or greater</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskHyperDrug"
												<%= props.getProperty("ar2_riskHyperDrug", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Anti-hypertensive drugs</td>
										</tr>
										<tr>
											<td><input type="checkbox"
												name="ar2_riskChroRenalDisease"
												<%= props.getProperty("ar2_riskChroRenalDisease", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Chronic renal disease</td>
										</tr>
										<tr>
											<th colspan="2" align="left"><span class="small9">OTHER</span></th>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskUnder18"
												<%= props.getProperty("ar2_riskUnder18", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Age under 18 at delivery</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskOver35"
												<%= props.getProperty("ar2_riskOver35", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Age 35 or over at delivery</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskAlcoDrug"
												<%= props.getProperty("ar2_riskAlcoDrug", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Alcohol and/or Drugs</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskUnderweight"
												<%= props.getProperty("ar2_riskUnderweight", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>BMI less than 18.5 (Underweight)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskObesity"
												<%= props.getProperty("ar2_riskObesity", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>BMI over 30 (Obesity)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskDepre"
												<%= props.getProperty("ar2_riskDepre", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Depression</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskH152"
												<%= props.getProperty("ar2_riskH152", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Height (under 152 cm or 5 ft. 0 in.)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskSmoking"
												<%= props.getProperty("ar2_riskSmoking", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Smoking</td>
										</tr>
										<tr>
											<td valign="top"><input type="checkbox"
												name="ar2_riskOtherMedical"
												<%= props.getProperty("ar2_riskOtherMedical", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Other medical/surgical disorders<br>
											e.g. epilepsy, severe asthma, Lupus etc.</td>
										</tr>
									</table>



									</td>
									<td valign="top">

									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<th colspan="2" align="left">PROBLEMS IN CURRENT
											PREGNANCY</th>
										</tr>
										<tr>
											<td width="1%"><input type="checkbox"
												name="ar2_riskAbnSerum"
												<%= props.getProperty("ar2_riskAbnSerum", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Abnormal maternal serum screening<br>
											(HCG or AFP > 2.0 MOM)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskAlcoDrugCur"
												<%= props.getProperty("ar2_riskAlcoDrugCur", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Alcohol and/or Drugs</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskAnemia"
												<%= props.getProperty("ar2_riskAnemia", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Anemia (< 100g per L)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskBleeding"
												<%= props.getProperty("ar2_riskBleeding", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Antepartum Bleeding</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskBloodAnti"
												<%= props.getProperty("ar2_riskBloodAnti", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Blood antibodies (Rh, Anti C, Anti K etc.)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskFetalMov"
												<%= props.getProperty("ar2_riskFetalMov", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Decreased fetal movement</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskDepreCur"
												<%= props.getProperty("ar2_riskDepreCur", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Depression</td>
										</tr>
										<tr>
											<td width="1%"><input type="checkbox"
												name="ar2_riskDiagLarge"
												<%= props.getProperty("ar2_riskDiagLarge", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Diagnosis of large for dates</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskDiagSmall"
												<%= props.getProperty("ar2_riskDiagSmall", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Diagnosis of small for dates (IUGR)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskGesDiabete"
												<%= props.getProperty("ar2_riskGesDiabete", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Gestational diabetes</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPregIndHypert"
												<%= props.getProperty("ar2_riskPregIndHypert", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Hypertension disorders of pregnancy</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskMalpres"
												<%= props.getProperty("ar2_riskMalpres", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Malpresentation</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskMemRupt37"
												<%= props.getProperty("ar2_riskMemRupt37", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Membrane rupture before 37 weeks</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskMulPreg"
												<%= props.getProperty("ar2_riskMulPreg", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Multiple pregnancy</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPolyhyd"
												<%= props.getProperty("ar2_riskPolyhyd", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Polyhydramnios or oligohydramnios</td>
										</tr>
										<tr>
											<td valign="top"><input type="checkbox"
												name="ar2_riskWtLoss"
												<%= props.getProperty("ar2_riskWtLoss", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Poor weight gain 26 - 36 weeks<br>
											(<0.5 kg/wk or weight loss)</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskPreg42W"
												<%= props.getProperty("ar2_riskPreg42W", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Pregnancy > 42 weeks</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskAdmPreterm"
												<%= props.getProperty("ar2_riskAdmPreterm", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Preterm labour</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskProte1"
												<%= props.getProperty("ar2_riskProte1", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Proteinura 1+ or greater</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="ar2_riskSmokingCur"
												<%= props.getProperty("ar2_riskSmokingCur", "") %> @oscar.formDB
												dbType="tinyint(1)" /></td>
											<td>Smoking at any time during pregnancy</td>
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
<script type="text/javascript">
Calendar.setup({ inputField : "pg1_lmp", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_lmp_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_stopDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_stopDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_eddByUs", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_eddByUs_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_examination", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_examination_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_smokeQuitDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_smokeQuitDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_alcoQuitDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_alcoQuitDate_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "pg1_eddByDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_eddByDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_firstUsPerf", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_firstUsPerf_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "pg1_obHistDate1", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate2", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate3", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate4", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate5", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate5_cal", singleClick : true, step : 1 });
</script>

</html:html>
