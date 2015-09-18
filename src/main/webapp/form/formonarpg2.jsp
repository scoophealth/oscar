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

<%@page import="oscar.OscarProperties"%>
<%@ page
	import="oscar.form.graphic.*, oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%
    String formClass = "ONAR";
    String formLink = "formonarpg2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg2");

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
                document.forms[0].action = "../form/createpdf?__title=Antenatal+Record+Part+2&__cfgfile=onar2PrintCfgPg1&__cfgGraphicFile=onar2PrintGraphCfgPg1&__template=onar2";
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
        if(valDate(document.forms[0].pg2_formDate)==false){
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
		var temp = ""; //pg2_gest1 - pg2_date1
		var n1 = name.substring(eval(name.indexOf("t")+1));

		if(name.indexOf("ar2_")>=0) {
			n1 = name.substring(eval(name.indexOf("A")+1));
			name = "ar2_uDate" + n1;
		} else if (n1>36) {
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
		value=<%=props.getProperty("c_lastVisited", "pg2")%> />
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
				href="formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <a
				href="formonarpg4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.3)</font></a> &nbsp;|&nbsp; <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%--=demoNo%>&formId=<%=formId%>&provNo=<%=provNo--%>');">AR Planner</a-->
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
			RECORD 2 (page 1)</th>
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
			&nbsp;&nbsp;&nbsp;<b>Hep B vaccine</b> <input type="checkbox"
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
			<td width="7%"><!--  a href=# onclick="javascript:onWtSVG(); return false;"-->Weight<br>
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
			<td nowrap><input type="text" name="pg2_date1" id="pg2_date1"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date1", "") %>"></td>
			<td><input type="text" name="pg2_gest1" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest1", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt1" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt1", "")) %>"></td>
			<td><input type="text" name="pg2_BP1" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP1", "")) %>"></td>
			<td width="4%"><input type="text" name="pg2_urinePr1" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr1", "")) %>"></td>
			<td width="4%"><input type="text" name="pg2_urineGl1" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl1", "")) %>"></td>
			<td><input type="text" name="pg2_ht1" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht1", "")) %>"></td>
			<td><input type="text" name="pg2_presn1" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn1", "")) %>"></td>
			<td><input type="text" name="pg2_FHR1" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR1", "")) %>"></td>
			<td><input type="text" name="pg2_comments1" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments1", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date2" id="pg2_date2"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date2", "") %>"></td>
			<td><input type="text" name="pg2_gest2" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest2", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt2" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt2", "")) %>"></td>
			<td><input type="text" name="pg2_BP2" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP2", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr2" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr2", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl2" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl2", "")) %>"></td>
			<td><input type="text" name="pg2_ht2" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht2", "")) %>"></td>
			<td><input type="text" name="pg2_presn2" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn2", "")) %>"></td>
			<td><input type="text" name="pg2_FHR2" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR2", "")) %>"></td>
			<td><input type="text" name="pg2_comments2" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments2", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date3" id="pg2_date3"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date3", "") %>"></td>
			<td><input type="text" name="pg2_gest3" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest3", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt3" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt3", "")) %>"></td>
			<td><input type="text" name="pg2_BP3" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP3", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr3" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr3", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl3" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl3", "")) %>"></td>
			<td><input type="text" name="pg2_ht3" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht3", "")) %>"></td>
			<td><input type="text" name="pg2_presn3" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn3", "")) %>"></td>
			<td><input type="text" name="pg2_FHR3" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR3", "")) %>"></td>
			<td><input type="text" name="pg2_comments3" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments3", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date4" id="pg2_date4"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date4", "") %>"></td>
			<td><input type="text" name="pg2_gest4" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest4", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt4" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt4", "")) %>"></td>
			<td><input type="text" name="pg2_BP4" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP4", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr4" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr4", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl4" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl4", "")) %>"></td>
			<td><input type="text" name="pg2_ht4" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht4", "")) %>"></td>
			<td><input type="text" name="pg2_presn4" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn4", "")) %>"></td>
			<td><input type="text" name="pg2_FHR4" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR4", "")) %>"></td>
			<td><input type="text" name="pg2_comments4" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments4", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date5" id="pg2_date5"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date5", "") %>"></td>
			<td><input type="text" name="pg2_gest5" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest5", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt5" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt5", "")) %>"></td>
			<td><input type="text" name="pg2_BP5" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP5", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr5" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr5", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl5" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl5", "")) %>"></td>
			<td><input type="text" name="pg2_ht5" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht5", "")) %>"></td>
			<td><input type="text" name="pg2_presn5" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn5", "")) %>"></td>
			<td><input type="text" name="pg2_FHR5" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR5", "")) %>"></td>
			<td><input type="text" name="pg2_comments5" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments5", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date6" id="pg2_date6"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date6", "") %>"></td>
			<td><input type="text" name="pg2_gest6" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest6", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt6" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt6", "")) %>"></td>
			<td><input type="text" name="pg2_BP6" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP6", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr6" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr6", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl6" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl6", "")) %>"></td>
			<td><input type="text" name="pg2_ht6" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht6", "")) %>"></td>
			<td><input type="text" name="pg2_presn6" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn6", "")) %>"></td>
			<td><input type="text" name="pg2_FHR6" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR6", "")) %>"></td>
			<td><input type="text" name="pg2_comments6" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments6", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date7" id="pg2_date7"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date7", "") %>"></td>
			<td><input type="text" name="pg2_gest7" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest7", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt7" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt7", "")) %>"></td>
			<td><input type="text" name="pg2_BP7" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP7", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr7" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr7", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl7" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl7", "")) %>"></td>
			<td><input type="text" name="pg2_ht7" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht7", "")) %>"></td>
			<td><input type="text" name="pg2_presn7" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn7", "")) %>"></td>
			<td><input type="text" name="pg2_FHR7" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR7", "")) %>"></td>
			<td><input type="text" name="pg2_comments7" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments7", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date8" id="pg2_date8"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date8", "") %>"></td>
			<td><input type="text" name="pg2_gest8" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest8", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt8" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt8", "")) %>"></td>
			<td><input type="text" name="pg2_BP8" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP8", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr8" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr8", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl8" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl8", "")) %>"></td>
			<td><input type="text" name="pg2_ht8" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht8", "")) %>"></td>
			<td><input type="text" name="pg2_presn8" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn8", "")) %>"></td>
			<td><input type="text" name="pg2_FHR8" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR8", "")) %>"></td>
			<td><input type="text" name="pg2_comments8" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments8", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date9" id="pg2_date9"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date9", "") %>"></td>
			<td><input type="text" name="pg2_gest9" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest9", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt9" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt9", "")) %>"></td>
			<td><input type="text" name="pg2_BP9" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP9", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr9" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr9", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl9" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl9", "")) %>"></td>
			<td><input type="text" name="pg2_ht9" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht9", "")) %>"></td>
			<td><input type="text" name="pg2_presn9" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn9", "")) %>"></td>
			<td><input type="text" name="pg2_FHR9" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR9", "")) %>"></td>
			<td><input type="text" name="pg2_comments9" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments9", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date10" id="pg2_date10"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date10", "") %>"></td>
			<td><input type="text" name="pg2_gest10" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest10", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt10" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt10", "")) %>"></td>
			<td><input type="text" name="pg2_BP10" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP10", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr10" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr10", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl10" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl10", "")) %>"></td>
			<td><input type="text" name="pg2_ht10" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht10", "")) %>"></td>
			<td><input type="text" name="pg2_presn10" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn10", "")) %>"></td>
			<td><input type="text" name="pg2_FHR10" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR10", "")) %>"></td>
			<td><input type="text" name="pg2_comments10" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments10", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date11" id="pg2_date11"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date11", "") %>"></td>
			<td><input type="text" name="pg2_gest11" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest11", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt11" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt11", "")) %>"></td>
			<td><input type="text" name="pg2_BP11" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP11", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr11" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr11", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl11" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl11", "")) %>"></td>
			<td><input type="text" name="pg2_ht11" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht11", "")) %>"></td>
			<td><input type="text" name="pg2_presn11" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn11", "")) %>"></td>
			<td><input type="text" name="pg2_FHR11" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR11", "")) %>"></td>
			<td><input type="text" name="pg2_comments11" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments11", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date12" id="pg2_date12"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date12", "") %>"></td>
			<td><input type="text" name="pg2_gest12" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest12", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt12" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt12", "")) %>"></td>
			<td><input type="text" name="pg2_BP12" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP12", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr12" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr12", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl12" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl12", "")) %>"></td>
			<td><input type="text" name="pg2_ht12" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht12", "")) %>"></td>
			<td><input type="text" name="pg2_presn12" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn12", "")) %>"></td>
			<td><input type="text" name="pg2_FHR12" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR12", "")) %>"></td>
			<td><input type="text" name="pg2_comments12" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments12", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date13" id="pg2_date13"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date13", "") %>"></td>
			<td><input type="text" name="pg2_gest13" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest13", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt13" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt13", "")) %>"></td>
			<td><input type="text" name="pg2_BP13" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP13", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr13" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr13", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl13" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl13", "")) %>"></td>
			<td><input type="text" name="pg2_ht13" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht13", "")) %>"></td>
			<td><input type="text" name="pg2_presn13" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn13", "")) %>"></td>
			<td><input type="text" name="pg2_FHR13" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR13", "")) %>"></td>
			<td><input type="text" name="pg2_comments13" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments13", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date14" id="pg2_date14"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date14", "") %>"></td>
			<td><input type="text" name="pg2_gest14" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest14", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt14" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt14", "")) %>"></td>
			<td><input type="text" name="pg2_BP14" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP14", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr14" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr14", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl14" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl14", "")) %>"></td>
			<td><input type="text" name="pg2_ht14" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht14", "")) %>"></td>
			<td><input type="text" name="pg2_presn14" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn14", "")) %>"></td>
			<td><input type="text" name="pg2_FHR14" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR14", "")) %>"></td>
			<td><input type="text" name="pg2_comments14" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments14", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date15" id="pg2_date15"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date15", "") %>"></td>
			<td><input type="text" name="pg2_gest15" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest15", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt15" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt15", "")) %>"></td>
			<td><input type="text" name="pg2_BP15" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP15", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr15" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr15", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl15" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl15", "")) %>"></td>
			<td><input type="text" name="pg2_ht15" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht15", "")) %>"></td>
			<td><input type="text" name="pg2_presn15" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn15", "")) %>"></td>
			<td><input type="text" name="pg2_FHR15" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR15", "")) %>"></td>
			<td><input type="text" name="pg2_comments15" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments15", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date16" id="pg2_date16"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date16", "") %>"></td>
			<td><input type="text" name="pg2_gest16" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest16", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt16" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt16", "")) %>"></td>
			<td><input type="text" name="pg2_BP16" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP16", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr16" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr16", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl16" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl16", "")) %>"></td>
			<td><input type="text" name="pg2_ht16" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht16", "")) %>"></td>
			<td><input type="text" name="pg2_presn16" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn16", "")) %>"></td>
			<td><input type="text" name="pg2_FHR16" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR16", "")) %>"></td>
			<td><input type="text" name="pg2_comments16" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments16", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date17" id="pg2_date17"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date17", "") %>"></td>
			<td><input type="text" name="pg2_gest17" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest17", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt17" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt17", "")) %>"></td>
			<td><input type="text" name="pg2_BP17" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP17", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr17" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr17", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl17" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl17", "")) %>"></td>
			<td><input type="text" name="pg2_ht17" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht17", "")) %>"></td>
			<td><input type="text" name="pg2_presn17" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn17", "")) %>"></td>
			<td><input type="text" name="pg2_FHR17" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR17", "")) %>"></td>
			<td><input type="text" name="pg2_comments17" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments17", "")) %>"></td>
		</tr>
		<tr align="center">
			<td nowrap><input type="text" name="pg2_date18" id="pg2_date18"
				class="spe" size="9" maxlength="10" onDblClick="calToday(this)"
				value="<%= props.getProperty("pg2_date18", "") %>"></td>
			<td><input type="text" name="pg2_gest18" class="spe" size="6"
				maxlength="6" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_gest18", "")) %>"
				onDblClick="calcWeek(this)"></td>
			<td><input type="text" name="pg2_wt18" size="6" maxlength="6"
				onDblClick="wtEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_wt18", "")) %>"></td>
			<td><input type="text" name="pg2_BP18" size="6" maxlength="8"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_BP18", "")) %>"></td>
			<td><input type="text" name="pg2_urinePr18" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urinePr18", "")) %>"></td>
			<td><input type="text" name="pg2_urineGl18" size="2"
				maxlength="3" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_urineGl18", "")) %>"></td>
			<td><input type="text" name="pg2_ht18" size="6" maxlength="6"
				onDblClick="htEnglish2Metric(this);" style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_ht18", "")) %>"></td>
			<td><input type="text" name="pg2_presn18" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_presn18", "")) %>"></td>
			<td><input type="text" name="pg2_FHR18" size="6" maxlength="6"
				style="width: 90%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_FHR18", "")) %>"></td>
			<td><input type="text" name="pg2_comments18" size="20"
				maxlength="70" style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_comments18", "")) %>"></td>
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
					<td><%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg2_labExtra2Name" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_labExtra2Name", "")) %>">
						<%}%>
					</td>
					<td><%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg2_labExtra2Value" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_labExtra2Value", "")) %>">
						<%}%>
					</td>
				</tr>
				<tr>
					<td><%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg2_labExtra2Name" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_labExtra2Name", "")) %>">
						<%}%>
					</td>
					<td><%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg2_labExtra2Value" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_labExtra2Value", "")) %>">
						<%}%>
					</td>
				</tr>
				<tr>
					<td><%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg2_labExtra3Name" size="10" maxlength="20" 
									value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_labExtra3Name", "")) %>">
						<%}%>
					</td>
					<td><%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg2_labExtra3Value" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_labExtra3Value", "")) %>">
						<%}%>
					</td>
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
			<input type="text" name="pg2_signature" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_signature", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg2_formDate" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg2_formDate", "") %>"></td>
			<td width="30%">Signature<br>
			<input type="text" name="pg2_signature2" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_signature2", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg2_formDate2" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg2_formDate2", "") %>"></td>
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
				href="formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <a
				href="formonarpg4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.3)</font></a> &nbsp;|&nbsp; <a
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
