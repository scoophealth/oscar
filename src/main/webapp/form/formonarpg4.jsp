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

<%@ page
	import="oscar.form.graphic.*, oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%
    String formClass = "ONAR";
    String formLink = "formonarpg4.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg4");

    //get project_home
    String project_home = request.getContextPath().substring(1);    
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<%
	FrmARBloodWorkTest ar1BloodWorkTest = new FrmARBloodWorkTest(demoNo, formId);
	java.util.Properties ar1Props = ar1BloodWorkTest.getAr1Props();
	int ar1BloodWorkTestListSize = ar1BloodWorkTest.getAr1BloodWorkTestListSize();
	String ar1CompleteSignal = "AR1 labs Complete";
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Antenatal Record 2</title>
<html:base />
<link rel="stylesheet" type="text/css"
	href="<%=bView?"arStyleView.css" : "arStyle.css"%>">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
</head>

<script type="text/javascript" language="Javascript">
    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    
    function setLock (checked) {
    	formElems = document.forms[0].elements;
    	for (var i=0; i<formElems.length; i++) {
    		if (formElems[i].type == "text" || formElems[i].type == "textarea") {
            		formElems[i].readOnly = checked;
    		} else if ((formElems[i].type == "checkbox") && (formElems[i].id != "pg1_lockPage") && (formElems[i].id != "pg1_4ColCom")) {
            		formElems[i].disabled = checked;
    		}
    	}
    }
    
    function onPrint() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        setLock(false);
        if(ret==true)
        {
            if( document.forms[0].c_finalEDB.value == "" && !confirm("<bean:message key="oscarEncounter.formOnar.msgNoEDB"/>")) {
                ret = false;
            }
            else {
                document.forms[0].action = "../form/createpdf?__title=Antenatal+Record+Part+2&__cfgfile=onar2PrintCfgPg3&__cfgGraphicFile=onar2PrintGraphCfgPg3&__template=onar2";
                document.forms[0].target="_blank";
            }
        }
        setTimeout('setLock(wasLocked)', 500);
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
		if (document.forms[0].c_finalEDB != null && (document.forms[0].c_finalEDB.value).length==10) {
			param += "?c_finalEDB=" + document.forms[0].c_finalEDB.value;
		} else {
			ret = false;
		}
		if (document.forms[0].c_ppWt != null && (document.forms[0].c_ppWt.value).length>0) {
			param += "&c_ppWt=" + document.forms[0].c_ppWt.value;
		} else {
			ret = false;
		}

		obj = document.forms[0].pg4_date1;
		if (getFormEntity(obj))  param += "&pg4_date1=" + obj.value; 
		obj = document.forms[0].pg4_date2;
		if (getFormEntity(obj))  param += "&pg4_date2=" + obj.value; 
		obj = document.forms[0].pg4_date3;
		if (getFormEntity(obj))  param += "&pg4_date3=" + obj.value; 
		obj = document.forms[0].pg4_date4;
		if (getFormEntity(obj))  param += "&pg4_date4=" + obj.value; 
		obj = document.forms[0].pg4_date5;
		if (getFormEntity(obj))  param += "&pg4_date5=" + obj.value; 
		obj = document.forms[0].pg4_date6;
		if (getFormEntity(obj))  param += "&pg4_date6=" + obj.value; 
		obj = document.forms[0].pg4_date7;
		if (getFormEntity(obj))  param += "&pg4_date7=" + obj.value; 
		obj = document.forms[0].pg4_date8;
		if (getFormEntity(obj))  param += "&pg4_date8=" + obj.value; 
		obj = document.forms[0].pg4_date9;
		if (getFormEntity(obj))  param += "&pg4_date9=" + obj.value; 
		obj = document.forms[0].pg4_date10;
		if (getFormEntity(obj))  param += "&pg4_date10=" + obj.value; 
		obj = document.forms[0].pg4_date11;
		if (getFormEntity(obj))  param += "&pg4_date11=" + obj.value; 
		obj = document.forms[0].pg4_date12;
		if (getFormEntity(obj))  param += "&pg4_date12=" + obj.value; 
		obj = document.forms[0].pg4_date13;
		if (getFormEntity(obj))  param += "&pg4_date13=" + obj.value; 
		obj = document.forms[0].pg4_date14;
		if (getFormEntity(obj))  param += "&pg4_date14=" + obj.value; 
		obj = document.forms[0].pg4_date15;
		if (getFormEntity(obj))  param += "&pg4_date15=" + obj.value; 
		obj = document.forms[0].pg4_date16;
		if (getFormEntity(obj))  param += "&pg4_date16=" + obj.value; 
		obj = document.forms[0].pg4_date17;
		if (getFormEntity(obj))  param += "&pg4_date17=" + obj.value; 
		obj = document.forms[0].pg4_wt1;
		if (getFormEntity(obj))  param += "&pg4_wt1=" + obj.value; 
		obj = document.forms[0].pg4_wt2;
		if (getFormEntity(obj))  param += "&pg4_wt2=" + obj.value; 
		obj = document.forms[0].pg4_wt3;
		if (getFormEntity(obj))  param += "&pg4_wt3=" + obj.value; 
		obj = document.forms[0].pg4_wt4;
		if (getFormEntity(obj))  param += "&pg4_wt4=" + obj.value; 
		obj = document.forms[0].pg4_wt5;
		if (getFormEntity(obj))  param += "&pg4_wt5=" + obj.value; 
		obj = document.forms[0].pg4_wt6;
		if (getFormEntity(obj))  param += "&pg4_wt6=" + obj.value; 
		obj = document.forms[0].pg4_wt7;
		if (getFormEntity(obj))  param += "&pg4_wt7=" + obj.value; 
		obj = document.forms[0].pg4_wt8;
		if (getFormEntity(obj))  param += "&pg4_wt8=" + obj.value; 
		obj = document.forms[0].pg4_wt9;
		if (getFormEntity(obj))  param += "&pg4_wt9=" + obj.value; 
		obj = document.forms[0].pg4_wt10;
		if (getFormEntity(obj))  param += "&pg4_wt10=" + obj.value; 
		obj = document.forms[0].pg4_wt11;
		if (getFormEntity(obj))  param += "&pg4_wt11=" + obj.value; 
		obj = document.forms[0].pg4_wt12;
		if (getFormEntity(obj))  param += "&pg4_wt12=" + obj.value; 
		obj = document.forms[0].pg4_wt13;
		if (getFormEntity(obj))  param += "&pg4_wt13=" + obj.value; 
		obj = document.forms[0].pg4_wt14;
		if (getFormEntity(obj))  param += "&pg4_wt14=" + obj.value; 
		obj = document.forms[0].pg4_wt15;
		if (getFormEntity(obj))  param += "&pg4_wt15=" + obj.value; 
		obj = document.forms[0].pg4_wt16;
		if (getFormEntity(obj))  param += "&pg4_wt16=" + obj.value; 
		obj = document.forms[0].pg4_wt17;
		if (getFormEntity(obj))  param += "&pg4_wt17=" + obj.value; 
/*
		for (var i = 1; i < 18; i++) {
				getFormEntity(("pg4_date"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg4_date" + i + "=" + obj.value;
			}
			obj = getFormEntity(("pg4_wt"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg4_wt" + i + "=" + obj.value;
			}
		}
		for (var i = 18; i < 35; i++) {
			var obj = getFormEntity(("pg4_date"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg4_date" + i + "=" + obj.value;
			}
			obj = getFormEntity(("pg4_wt"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg4_wt" + i + "=" + obj.value;
			}
		}
*/

        if(ret==true)  {
            popupFixedPage(650,850,'formonar2wt.jsp'+param);
        }

    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
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
function popupFixedPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
  var popup=window.open(page, "planner", windowprop);
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

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].c_finalEDB)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_formDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date37)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date38)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date39)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date40)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date41)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date42)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date43)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date44)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date45)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date46)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date47)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date48)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date49)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date50)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date51)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date52)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date53)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg4_date54)==false){
            b = false;
        }
        return b;
    }

	function calcWeek(source) {
<%
String fedb = props.getProperty("c_finalEDB", "");
String sDate = "";
if (!fedb.equals("") && fedb.length()==10 ) {
	FrmGraphicAR arG = new FrmGraphicAR();
	java.util.Date edbDate = arG.getStartDate(fedb);
    sDate = UtilDateUtilities.DateToString(edbDate, "MMMMM dd, yyyy"); //"yy,MM,dd");

%>
	    var delta = 0;
        var str_date = getDateField(source.name);
        if (str_date.length < 10) return;
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd = str_date.substring(eval(str_date.lastIndexOf("/")+1));
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
		var temp = ""; //pg4_gest1 - pg4_date1
		var n1 = name.substring(eval(name.indexOf("t")+1));

		if(name.indexOf("ar2_")>=0) {
			n1 = name.substring(eval(name.indexOf("A")+1));
			name = "ar2_uDate" + n1;
		} else 		if (n1>36) {
			name = "pg4_date" + n1;
		} else if (n1<=36 && n1>18) {
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
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '/' + (varMonth) + '/' + varDate;
}
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/form/formname">

	<input type="hidden" name="commonField" value="ar2_" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "pg4")%> />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />
	<%
	String historyet = "";
	if (request.getParameter("historyet") != null) {
		out.println("<input type=\"hidden\" name=\"historyet\" value=\"" + request.getParameter("historyet") + "\">" );
		historyet = "&historyet=" + request.getParameter("historyet");
	}
%>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>

			<%
  if (!bView) {
%>
			<td><a
				href="javascript: popPage('formlabreq07.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AnteNatal','LabReq');">LAB</a>
			</td>

			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formonarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo+historyet%>&view=1');">
			AR1</a> | <a
				href="javascript: popupPage('formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo+historyet%>&view=1');">AR2
			<font size=-2>(pg.2)</font></a></td>
			<td align="right"><b>Edit:</b> <a
				href="formonarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR1</a>
			| <a
				href="formonarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;|&nbsp; <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%--=demoNo%>&formId=<%=formId%>&provNo=<%=provNo--%>');">AR Planner</a-->
			<%if(((FrmONARRecord)rec).isSendToPing(""+demoNo)) {	%> <a
				href="study/ar2ping.jsp?demographic_no=<%=demoNo%>">Send to PING</a>
			<% }	%>
			</td>
			<%
  }
%>
		</tr>
	</table>

	<table class="title" border="0" cellspacing="0" cellpadding="0"
		width="100%">
		<tr>
			<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>ANTENATAL
			RECORD 2 (page 3)</th>
		</tr>
	</table>
	<table width="50%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="50%">Patient's Last Name<br>
			<input type="text" name="c_lastName" style="width: 100%" size="30"
				maxlength="60" value="<%= props.getProperty("c_lastName", "") %>" />
			</td>
			<td valign="top">Patient's First Name<br>
			<input type="text" name="c_firstName" style="width: 100%" size="30"
				maxlength="60" value="<%= props.getProperty("c_firstName", "") %>" />
			</td>
		</tr>
		<!--  tr>
        <td valign="top" colspan='2'>
            Name<input type="text" name="c_pName"  style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_pName", "") %>">
        </td>
    </tr>
    <tr>
        <td valign="top" colspan='2'>
            Address<input type="text" name="c_address"  style="width:100%" size="60" maxlength="80" value="<%= UtilMisc.htmlEscape(props.getProperty("c_address", "")) %>">
        </td>
	</tr>-->
		<tr>
			<td valign="top" width="50%">Birth attendants<br>
			<input type="text" name="c_ba" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_ba", "")) %>">
			</td>
			<td>Newborn care<br>
			<input type="text" name="c_nc" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_nc", "")) %>">
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="25%" colspan="5">Family physician<br>
			<input type="text" name="c_famPhys" size="30" maxlength="80"
				style="width: 100%"
				value="<%= props.getProperty("c_famPhys", "") %>" /></td>
			<td valign="top" rowspan="4" width="25%"><b>Final EDB</b>
			(yyyy/mm/dd)<br>
			<input type="text" name="c_finalEDB" style="width: 100%" size="10"
				maxlength="10" value="<%= props.getProperty("c_finalEDB", "") %>">
			</td>
			<td valign="top" rowspan="4" width="25%">Allergies or
			Sensitivities</br>
			<textarea name="c_allergies" style="width: 100%" cols="30" rows="3"><%= props.getProperty("c_allergies", "") %></textarea></td>
			<td valign="top" rowspan="4">Medications / Herbals</br>
			<textarea name="c_meds" style="width: 100%" cols="30" rows="3"><%= props.getProperty("c_meds", "") %></textarea></td>
		</tr>
		<tr>
			<td bgcolor="#CCCCCC" width="5%">G</br>
			<input type="text" name="c_gravida" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_gravida", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">T</br>
			<input type="text" name="c_term" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_term", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">P</br>
			<input type="text" name="c_prem" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_prem", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">A</br>
			<input type="text" name="c_abort" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_abort", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">L</br>
			<input type="text" name="c_living" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_living", "")) %>"></td>
		</tr>
	</table>



	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<th bgcolor="#CCCCCC" width="30%">Identified Risk Factors</th>
			<th bgcolor="#CCCCCC">Plan of Management</th>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors1" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors1", "") %>"></td>
			<td><input type="text" name="c_planManage1" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage1", "") %>"></td>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors2" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors2", "") %>"></td>
			<td><input type="text" name="c_planManage2" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage2", "") %>"></td>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors3" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors3", "") %>"></td>
			<td><input type="text" name="c_planManage3" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage3", "") %>"></td>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors4" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors4", "") %>"></td>
			<td><input type="text" name="c_planManage4" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage4", "") %>"></td>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors5" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors5", "") %>"></td>
			<td><input type="text" name="c_planManage5" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage5", "") %>"></td>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors6" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors6", "") %>"></td>
			<td><input type="text" name="c_planManage6" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage6", "") %>"></td>
		</tr>
		<tr>
			<td><input type="text" name="c_riskFactors7" size="20"
				maxlength="50" style="width: 100%"
				value="<%= props.getProperty("c_riskFactors7", "") %>"></td>
			<td><input type="text" name="c_planManage7" size="60"
				maxlength="100" style="width: 100%"
				value="<%= props.getProperty("c_planManage7", "") %>"></td>
		</tr>
		<tr>
			<td colspan="2"
				style="background-color: green; color: #FFFFFF; font-weight: bold;">
			<%
	if(ar1BloodWorkTestListSize == 9){
%> <%=ar1CompleteSignal%> <%
	}
%>
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th bgcolor="#CCCCCC" colspan="3">Recommended Immunoprophylaxis
			</th>
		</tr>
		<tr>
			<td width="30%"><b>Rh neg.</b> <input type="checkbox"
				name="ar2_rhNeg" <%= props.getProperty("ar2_rhNeg", "") %> />
			&nbsp;&nbsp;&nbsp;<b>Rh IG Given:</b> <input type="text"
				name="ar2_rhIG" size="7" maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_rhIG", "")) %>">
			</td>
			<td width="30%" nowrap><b>Rubella booster postpartum</b> <input
				type="checkbox" name="ar2_rubella"
				<%= props.getProperty("ar2_rubella", "") %> /></td>
			<td><b>Newborn needs: Hep B IG</b> <input type="checkbox"
				name="ar2_hepBIG" <%= props.getProperty("ar2_hepBIG", "") %> />
			&nbsp;&nbsp;&nbsp;<b>Rh B vaccine</b> <input type="checkbox"
				name="ar2_hepBVac" <%= props.getProperty("ar2_hepBVac", "") %> /></td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td valign="top" bgcolor="#CCCCCC" align="center"><b>Subsequent
			Visits</b></td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr align="center">
			<td width="7%">Date<br>
			(yyyy/mm/dd)</td>
			<td width="7%">GA<br>
			(weeks)</td>
			<td width="7%"><!--a href=# onclick="javascript:onWtSVG(); return false;"-->Weight<br>
			(Kg)</a></td>
			<td width="7%">B.P.</td>
			<td width="6%" colspan="2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2" align="center">Urine</td>
				</tr>
				<tr align="center">
					<td>Pr</td>
					<td>Gl</td>
				</tr>
			</table>
			</td>
			<td width="7%">SFH</td>
			<td width="7%">Pres.<br>
			Posn.</td>
			<td width="7%">FHR/</br>
			Fm</td>
			<td nowrap>Comments</td>
			<!--  td nowrap width="4%">Cig./<br>day</td>-->
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date37" id="pg4_date37"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date37", "") %>"></td>
			<td><input type="text" name="pg4_gest37" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest37", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt37" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt37", "")) %>"></td>
			<td><input type="text" name="pg4_BP37" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP37", "")) %>"></td>
			<td width="4%"><input type="text" name="pg4_urinePr37" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr37", "")) %>"></td>
			<td width="4%"><input type="text" name="pg4_urineGl37" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl37", "")) %>"></td>
			<td><input type="text" name="pg4_ht37" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht37", "")) %>"></td>
			<td><input type="text" name="pg4_presn37" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn37", "")) %>"></td>
			<td><input type="text" name="pg4_FHR37" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR37", "")) %>"></td>
			<td><input type="text" name="pg4_comments37" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments37", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date38" id="pg4_date38"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date38", "") %>"></td>
			<td><input type="text" name="pg4_gest38" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest38", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt38" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt38", "")) %>"></td>
			<td><input type="text" name="pg4_BP38" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP38", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr38" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr38", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl38" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl38", "")) %>"></td>
			<td><input type="text" name="pg4_ht38" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht38", "")) %>"></td>
			<td><input type="text" name="pg4_presn38" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn38", "")) %>"></td>
			<td><input type="text" name="pg4_FHR38" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR38", "")) %>"></td>
			<td><input type="text" name="pg4_comments38" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments38", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date39" id="pg4_date39"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date39", "") %>"></td>
			<td><input type="text" name="pg4_gest39" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest39", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt39" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt39", "")) %>"></td>
			<td><input type="text" name="pg4_BP39" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP39", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr39" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr39", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl39" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl39", "")) %>"></td>
			<td><input type="text" name="pg4_ht39" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht39", "")) %>"></td>
			<td><input type="text" name="pg4_presn39" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn39", "")) %>"></td>
			<td><input type="text" name="pg4_FHR39" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR39", "")) %>"></td>
			<td><input type="text" name="pg4_comments39" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments39", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date40" id="pg4_date40"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date40", "") %>"></td>
			<td><input type="text" name="pg4_gest40" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest40", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt40" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt40", "")) %>"></td>
			<td><input type="text" name="pg4_BP40" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP40", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr40" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr40", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl40" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl40", "")) %>"></td>
			<td><input type="text" name="pg4_ht40" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht40", "")) %>"></td>
			<td><input type="text" name="pg4_presn40" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn40", "")) %>"></td>
			<td><input type="text" name="pg4_FHR40" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR40", "")) %>"></td>
			<td><input type="text" name="pg4_comments40" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments40", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date41" id="pg4_date41"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date41", "") %>"></td>
			<td><input type="text" name="pg4_gest41" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest41", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt41" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt41", "")) %>"></td>
			<td><input type="text" name="pg4_BP41" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP41", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr41" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr41", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl41" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl41", "")) %>"></td>
			<td><input type="text" name="pg4_ht41" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht41", "")) %>"></td>
			<td><input type="text" name="pg4_presn41" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn41", "")) %>"></td>
			<td><input type="text" name="pg4_FHR41" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR41", "")) %>"></td>
			<td><input type="text" name="pg4_comments41" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments41", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date42" id="pg4_date42"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date42", "") %>"></td>
			<td><input type="text" name="pg4_gest42" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest42", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt42" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt42", "")) %>"></td>
			<td><input type="text" name="pg4_BP42" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP42", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr42" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr42", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl42" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl42", "")) %>"></td>
			<td><input type="text" name="pg4_ht42" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht42", "")) %>"></td>
			<td><input type="text" name="pg4_presn42" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn42", "")) %>"></td>
			<td><input type="text" name="pg4_FHR42" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR42", "")) %>"></td>
			<td><input type="text" name="pg4_comments42" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments42", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date43" id="pg4_date43"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date43", "") %>"></td>
			<td><input type="text" name="pg4_gest43" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest43", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt43" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt43", "")) %>"></td>
			<td><input type="text" name="pg4_BP43" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP43", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr43" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr43", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl43" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl43", "")) %>"></td>
			<td><input type="text" name="pg4_ht43" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht43", "")) %>"></td>
			<td><input type="text" name="pg4_presn43" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn43", "")) %>"></td>
			<td><input type="text" name="pg4_FHR43" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR43", "")) %>"></td>
			<td><input type="text" name="pg4_comments43" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments43", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date44" id="pg4_date44"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date44", "") %>"></td>
			<td><input type="text" name="pg4_gest44" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest44", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt44" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt44", "")) %>"></td>
			<td><input type="text" name="pg4_BP44" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP44", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr44" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr44", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl44" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl44", "")) %>"></td>
			<td><input type="text" name="pg4_ht44" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht44", "")) %>"></td>
			<td><input type="text" name="pg4_presn44" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn44", "")) %>"></td>
			<td><input type="text" name="pg4_FHR44" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR44", "")) %>"></td>
			<td><input type="text" name="pg4_comments44" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments44", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date45" id="pg4_date45"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date45", "") %>"></td>
			<td><input type="text" name="pg4_gest45" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest45", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt45" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt45", "")) %>"></td>
			<td><input type="text" name="pg4_BP45" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP45", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr45" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr45", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl45" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl45", "")) %>"></td>
			<td><input type="text" name="pg4_ht45" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht45", "")) %>"></td>
			<td><input type="text" name="pg4_presn45" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn45", "")) %>"></td>
			<td><input type="text" name="pg4_FHR45" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR45", "")) %>"></td>
			<td><input type="text" name="pg4_comments45" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments45", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date46" id="pg4_date46"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date46", "") %>"></td>
			<td><input type="text" name="pg4_gest46" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest46", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt46" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt46", "")) %>"></td>
			<td><input type="text" name="pg4_BP46" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP46", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr46" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr46", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl46" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl46", "")) %>"></td>
			<td><input type="text" name="pg4_ht46" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht46", "")) %>"></td>
			<td><input type="text" name="pg4_presn46" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn46", "")) %>"></td>
			<td><input type="text" name="pg4_FHR46" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR46", "")) %>"></td>
			<td><input type="text" name="pg4_comments46" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments46", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date47" id="pg4_date47"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date47", "") %>"></td>
			<td><input type="text" name="pg4_gest47" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest47", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt47" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt47", "")) %>"></td>
			<td><input type="text" name="pg4_BP47" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP47", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr47" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr47", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl47" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl47", "")) %>"></td>
			<td><input type="text" name="pg4_ht47" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht47", "")) %>"></td>
			<td><input type="text" name="pg4_presn47" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn47", "")) %>"></td>
			<td><input type="text" name="pg4_FHR47" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR47", "")) %>"></td>
			<td><input type="text" name="pg4_comments47" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments47", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date48" id="pg4_date48"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date48", "") %>"></td>
			<td><input type="text" name="pg4_gest48" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest48", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt48" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt48", "")) %>"></td>
			<td><input type="text" name="pg4_BP48" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP48", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr48" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr48", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl48" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl48", "")) %>"></td>
			<td><input type="text" name="pg4_ht48" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht48", "")) %>"></td>
			<td><input type="text" name="pg4_presn48" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn48", "")) %>"></td>
			<td><input type="text" name="pg4_FHR48" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR48", "")) %>"></td>
			<td><input type="text" name="pg4_comments48" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments48", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date49" id="pg4_date49"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date49", "") %>"></td>
			<td><input type="text" name="pg4_gest49" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest49", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt49" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt49", "")) %>"></td>
			<td><input type="text" name="pg4_BP49" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP49", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr49" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr49", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl49" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl49", "")) %>"></td>
			<td><input type="text" name="pg4_ht49" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht49", "")) %>"></td>
			<td><input type="text" name="pg4_presn49" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn49", "")) %>"></td>
			<td><input type="text" name="pg4_FHR49" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR49", "")) %>"></td>
			<td><input type="text" name="pg4_comments49" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments49", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date50" id="pg4_date50"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date50", "") %>"></td>
			<td><input type="text" name="pg4_gest50" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest50", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt50" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt50", "")) %>"></td>
			<td><input type="text" name="pg4_BP50" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP50", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr50" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr50", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl50" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl50", "")) %>"></td>
			<td><input type="text" name="pg4_ht50" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht50", "")) %>"></td>
			<td><input type="text" name="pg4_presn50" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn50", "")) %>"></td>
			<td><input type="text" name="pg4_FHR50" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR50", "")) %>"></td>
			<td><input type="text" name="pg4_comments50" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments50", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date51" id="pg4_date51"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date51", "") %>"></td>
			<td><input type="text" name="pg4_gest51" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest51", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt51" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt51", "")) %>"></td>
			<td><input type="text" name="pg4_BP51" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP51", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr51" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr51", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl51" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl51", "")) %>"></td>
			<td><input type="text" name="pg4_ht51" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht51", "")) %>"></td>
			<td><input type="text" name="pg4_presn51" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn51", "")) %>"></td>
			<td><input type="text" name="pg4_FHR51" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR51", "")) %>"></td>
			<td><input type="text" name="pg4_comments51" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments51", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date52" id="pg4_date52"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date52", "") %>"></td>
			<td><input type="text" name="pg4_gest52" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest52", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt52" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt52", "")) %>"></td>
			<td><input type="text" name="pg4_BP52" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP52", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr52" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr52", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl52" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl52", "")) %>"></td>
			<td><input type="text" name="pg4_ht52" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht52", "")) %>"></td>
			<td><input type="text" name="pg4_presn52" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn52", "")) %>"></td>
			<td><input type="text" name="pg4_FHR52" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR52", "")) %>"></td>
			<td><input type="text" name="pg4_comments52" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments52", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date53" id="pg4_date53"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date53", "") %>"></td>
			<td><input type="text" name="pg4_gest53" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest53", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt53" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt53", "")) %>"></td>
			<td><input type="text" name="pg4_BP53" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP53", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr53" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr53", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl53" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl53", "")) %>"></td>
			<td><input type="text" name="pg4_ht53" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht53", "")) %>"></td>
			<td><input type="text" name="pg4_presn53" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn53", "")) %>"></td>
			<td><input type="text" name="pg4_FHR53" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR53", "")) %>"></td>
			<td><input type="text" name="pg4_comments53" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments53", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg4_date54" id="pg4_date54"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg4_date54", "") %>"></td>
			<td><input type="text" name="pg4_gest54" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_gest54", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg4_wt54" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_wt54", "")) %>"></td>
			<td><input type="text" name="pg4_BP54" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_BP54", "")) %>"></td>
			<td><input type="text" name="pg4_urinePr54" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urinePr54", "")) %>"></td>
			<td><input type="text" name="pg4_urineGl54" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_urineGl54", "")) %>"></td>
			<td><input type="text" name="pg4_ht54" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_ht54", "")) %>"></td>
			<td><input type="text" name="pg4_presn54" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_presn54", "")) %>"></td>
			<td><input type="text" name="pg4_FHR54" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_FHR54", "")) %>"></td>
			<td><input type="text" name="pg4_comments54" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_comments54", "")) %>"></td>
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="20%">&nbsp;</td>
			<td width="80%" valign="top">
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<th colspan="3" align="center" bgcolor="#CCCCCC">Ultrasound</th>
					<th colspan="2" align="center" bgcolor="#CCCCCC">Additional
					Lab Investigations</th>
				</tr>
				<tr>
					<td align="center" width="12%">Date</td>
					<td align="center" width="8%">GA</td>
					<td width="50%" align="center">Result</td>
					<td align="center" width="15%">Test</td>
					<td align="center">Result</td>
				</tr>
				<tr>
					<td nowrap><input type="text" name="ar2_uDate1"
						id="ar2_uDate1" class="spe" onDblClick="calToday(this)" size="10"
						maxlength="10" value="<%= props.getProperty("ar2_uDate1", "") %>">
					<img src="../images/cal.gif" id="ar2_uDate1_cal"></td>
					<td><input type="text" name="ar2_uGA1" class="spe"
						onDblClick="calcWeek(this)" size="5" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uGA1", "")) %>"></td>
					<td><input type="text" name="ar2_uResults1" size="50"
						maxlength="50"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uResults1", "")) %>"></td>
					<td>Hb</td>
					<td><input type="text" name="ar2_hb" size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_hb", "")) %>"></td>
				</tr>
				<tr>
					<td><input type="text" name="ar2_uDate2" id="ar2_uDate2"
						class="spe" onDblClick="calToday(this)" size="10" maxlength="10"
						value="<%= props.getProperty("ar2_uDate2", "") %>"> <img
						src="../images/cal.gif" id="ar2_uDate2_cal"></td>
					<td><input type="text" name="ar2_uGA2" class="spe"
						onDblClick="calcWeek(this)" size="5" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uGA2", "")) %>"></td>
					<td><input type="text" name="ar2_uResults2" size="50"
						maxlength="50"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uResults2", "")) %>"></td>
					<td>ABO/Rh</td>
					<%
	String abo = "";
	String rh ="";
	if(UtilMisc.htmlEscape(props.getProperty("ar2_bloodGroup", "")).equals("") ){
		abo = UtilMisc.htmlEscape(ar1Props.getProperty("pg1_labABO", ""));
	}else{
		abo = UtilMisc.htmlEscape(props.getProperty("ar2_bloodGroup", ""));
	}

	if(UtilMisc.htmlEscape(props.getProperty("ar2_rh", "")).equals("")){
		rh = UtilMisc.htmlEscape(ar1Props.getProperty("pg1_labRh", ""));
	}else{
		rh = UtilMisc.htmlEscape(props.getProperty("ar2_rh", ""));
	}
%>
					<td><input type="text" name="ar2_bloodGroup" size="2"
						maxlength="6" value="<%=abo%>"> /<input type="text"
						name="ar2_rh" size="1" maxlength="6" value="<%=rh%>"></td>
				</tr>
				<tr>
					<td><input type="text" name="ar2_uDate3" id="ar2_uDate3"
						class="spe" onDblClick="calToday(this)" size="10" maxlength="10"
						value="<%= props.getProperty("ar2_uDate3", "") %>"> <img
						src="../images/cal.gif" id="ar2_uDate3_cal"></td>
					<td><input type="text" name="ar2_uGA3" class="spe"
						onDblClick="calcWeek(this)" size="5" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uGA3", "")) %>"></td>
					<td><input type="text" name="ar2_uResults3" size="50"
						maxlength="50"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uResults3", "")) %>"></td>
					<td>Repeat ABS</td>
					<td><input type="text" name="ar2_labABS" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labABS", "")) %>"></td>
				</tr>
				<tr>
					<td><input type="text" name="ar2_uDate4" id="ar2_uDate4"
						class="spe" onDblClick="calToday(this)" size="10" maxlength="10"
						value="<%= props.getProperty("ar2_uDate4", "") %>"> <img
						src="../images/cal.gif" id="ar2_uDate4_cal"></td>
					<td><input type="text" name="ar2_uGA4" class="spe"
						onDblClick="calcWeek(this)" size="5" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uGA4", "")) %>"></td>
					<td><input type="text" name="ar2_uResults4" size="50"
						maxlength="50"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_uResults4", "")) %>"></td>
					<td>1 hr. GCT</td>
					<td><input type="text" name="ar2_lab1GCT" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_lab1GCT", "")) %>"></td>
				</tr>
				<tr>
					<th colspan="3">Discussion Topics</th>
					<td>2 hr. GTT</td>
					<td><input type="text" name="ar2_lab2GTT" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_lab2GTT", "")) %>"></td>
				</tr>
				<tr>
					<td colspan="3" rowspan="5">

					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td width="33%"><input type="checkbox" name="ar2_exercise"
								<%= props.getProperty("ar2_exercise", "") %>>Exercise<br>
							<input type="checkbox" name="ar2_workPlan"
								<%= props.getProperty("ar2_workPlan", "") %>>Work plan<br>
							<input type="checkbox" name="ar2_intercourse"
								<%= props.getProperty("ar2_intercourse", "") %>>Intercourse<br>
							<input type="checkbox" name="ar2_travel"
								<%= props.getProperty("ar2_travel", "") %>>Travel<br>
							<input type="checkbox" name="ar2_prenatal"
								<%= props.getProperty("ar2_prenatal", "") %>>Prenatal
							classes<br>
							<input type="checkbox" name="ar2_birth"
								<%= props.getProperty("ar2_birth", "") %>>Birth plan<br>
							<input type="checkbox" name="ar2_onCall"
								<%= props.getProperty("ar2_onCall", "") %>>On call
							providers</td>
							<td width="33%"><input type="checkbox" name="ar2_preterm"
								<%= props.getProperty("ar2_preterm", "") %>>Preterm
							labour<br>
							<input type="checkbox" name="ar2_prom"
								<%= props.getProperty("ar2_prom", "") %>>PROM<br>
							<input type="checkbox" name="ar2_aph"
								<%= props.getProperty("ar2_aph", "") %>>APH<br>
							<input type="checkbox" name="ar2_fetal"
								<%= props.getProperty("ar2_fetal", "") %>>Fetal movement<br>
							<input type="checkbox" name="ar2_admission"
								<%= props.getProperty("ar2_admission", "") %>>Admission
							timing<br>
							<input type="checkbox" name="ar2_pain"
								<%= props.getProperty("ar2_pain", "") %>>Pain management<br>
							<input type="checkbox" name="ar2_labour"
								<%= props.getProperty("ar2_labour", "") %>>Labour
							support<br>
							</td>
							<td width="33%"><input type="checkbox" name="ar2_breast"
								<%= props.getProperty("ar2_breast", "") %>>Breast
							feeding<br>
							<input type="checkbox" name="ar2_circumcision"
								<%= props.getProperty("ar2_circumcision", "") %>>Circumcision<br>
							<input type="checkbox" name="ar2_dischargePlan"
								<%= props.getProperty("ar2_dischargePlan", "") %>>Discharge
							planning<br>
							<input type="checkbox" name="ar2_car"
								<%= props.getProperty("ar2_car", "") %>>Car seat safety<br>
							<input type="checkbox" name="ar2_depression"
								<%= props.getProperty("ar2_depression", "") %>>Depression<br>
							<input type="checkbox" name="ar2_contraception"
								<%= props.getProperty("ar2_contraception", "") %>>Contraception<br>
							<input type="checkbox" name="ar2_postpartumCare"
								<%= props.getProperty("ar2_postpartumCare", "") %>>Postpartum
							care</td>
						</tr>
					</table>

					</td>
					<td>GBS</td>
					<td><input type="text" name="ar2_strep" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_strep", "")) %>"></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td width="30%">Signature<br>
			<input type="text" name="pg4_signature" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_signature", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg4_formDate" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg4_formDate", "") %>"></td>
			<td width="30%">Signature<br>
			<input type="text" name="pg4_signature2" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg4_signature2", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg4_formDate2" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg4_formDate2", "") %>"></td>
		</tr>

	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
  if (!bView) {
%>
			<td><a
				href="javascript: popPage('formlabreq07.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AnteNatal','LabReq');">LAB</a>
			</td>

			<td align="right"><font size="-1"><b>View:</b> <a
				href="javascript: popupPage('formonarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">
			AR1</a> | <a
				href="javascript: popupPage('formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			</font><font size=-2>(pg.2)</font></a></td>
			<td align="right"><b>Edit:</b> <a
				href="formonarpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR1</a>
			| <a
				href="formonarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;|&nbsp; <a
				href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&query_name=search_formonarrisk');">AR
			Planner</a></td>
			<%
  }
%>
		</tr>
	</table>

</html:form>
<% if (bView) { %>
<script type="text/javascript">
window.onload= function() {
setLock(true);
}
</script>
<% } %>
</body>
<script type="text/javascript">
Calendar.setup({ inputField : "ar2_uDate1", ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_uDate1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "ar2_uDate2", ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_uDate2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "ar2_uDate3", ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_uDate3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "ar2_uDate4", ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_uDate4_cal", singleClick : true, step : 1 });

</script>
</html:html>
