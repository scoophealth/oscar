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
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!--add for con report-->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>


<%
    String formClass = "ONAR";
    String formLink = "formonarpg1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg1");

    //get project_home
    String project_home = request.getContextPath().substring(1);    
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Antenatal Record 1</title>
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
<html:base />
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
            document.forms[0].action = "../form/createpdf?__title=Antenatal+Record+Part+1&__cfgfile=onar1PrintCfgPg1&__template=onar1";
            document.forms[0].target="_blank";            
        }
        setTimeout('setLock(wasLocked)', 500);
        return ret;
    }
    
    function refreshOpener() {
		if (window.opener && window.opener.name=="inboxDocDetails") {
			window.opener.location.reload(true);
		}	
    }
    window.onunload=refreshOpener;
    function onSave() {
    	setLock(false);
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        if (ret)
            window.onunload=null;
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
        	refreshOpener();
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
    	setLock(false);
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        if (ret)
        	refreshOpener();
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
    function popupPageFull(varpage) {
        windowprops = "location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
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
function calcBMIMetric(obj) {
	if(isNumber(document.forms[0].pg1_wt) && isNumber(document.forms[0].pg1_ht)) {
		weight = document.forms[0].pg1_wt.value;
		height = document.forms[0].pg1_ht.value / 100;
		if(weight!="" && weight!="0" && height!="" && height!="0") {
			obj.value = Math.round(weight * 10 / height / height) / 10;
		}
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

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].c_finalEDB)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg1_formDate)==false){
            b = false;
        }

        return b;
    }
function calToday(field) {
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '/' + (varMonth) + '/' + varDate;
}
function calByLMP(obj) {
        if (document.forms[0].pg1_menLMP.value!="" && valDate(document.forms[0].pg1_menLMP)==true) {
                var str_date = document.forms[0].pg1_menLMP.value;
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd  = str_date.substring(eval(str_date.lastIndexOf("/")+1));
                var calDate=new Date();
                calDate.setFullYear(yyyy);
                calDate.setDate(dd);
                calDate.setMonth(mm);
                calDate.setHours("0");

                calDate.setDate(calDate.getDate() + 280);

                varMonth1 = calDate.getMonth()+1;
                varMonth1 = varMonth1>9? varMonth1 : ("0"+varMonth1);
                varDate1 = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
                obj.value = calDate.getFullYear() + '/' + varMonth1 + '/' + varDate1;

        }
}

	function commentMode (checked) {
		var visible = checked ? "" : "none";
		var span = checked ? "1" : "4";	
		for (var n=2; n<=4; n++) {
			document.getElementById("pg1_commentsCol_"+n).style.display = visible;
		}
		document.getElementById("pg1_commentsCol_1").colSpan = span; 
	}

</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">

<html:form action="/form/formname">
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "pg1")%> />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>" /-->
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

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
				value="Print" onclick="javascript:return onPrint();" />
            <plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
            <%if (formId!=0) {%>
<a style="font-weight: bold; color: red;" href="javascript: popupPageFull('<%=request.getContextPath()%>
/mod/specialencounterComp/EyeForm.do?method=conReportHis&demographicNo=<%=demoNo%>&ARformId=<%=formId%>&provNo=<%=provNo%>');">
Con Report</a> |
            <%} %>
			</plugin:hideWhenCompExists>
</td>
			<%
  if (!bView) {
%>
			<td><a
				href="javascript: popPage('formlabreq07.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AnteNatal','LabReq');">LAB</a>
			</td>

			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formonarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="javascript: popupPage('formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;</td>
			<td align="right"><b>Edit:</b> <a
				href="formonarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%--=demoNo%>&formId=<%=formId%>&provNo=<%=provNo--%>');">AR Planner</a-->
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
			RECORD 1
			
			<% if (!bView) { %>
			(<input type="checkbox" name="pg1_lockPage" id="pg1_lockPage" onClick="setLock(this.checked);"
			<%= props.getProperty("pg1_lockPage", "") %> /> Lock )
			<% } %>
			
			</th>
			<%
	if (request.getParameter("historyet") != null) {
		out.println("<input type=\"hidden\" name=\"historyet\" value=\"" + request.getParameter("historyet") + "\">" );
	}
%>
		</tr>
	</table>
	<table width="50%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="50%">Patient's Last Name<br>
			<input type="text" name="c_lastName" style="width: 100%" size="30"
				maxlength="60" value="<%= props.getProperty("c_lastName", "") %>" />
			</td>
			<td valign="top" colspan='2'>Patient's First Name<br>
			<input type="text" name="c_firstName" style="width: 100%" size="30"
				maxlength="60" value="<%= props.getProperty("c_firstName", "") %>" />
			</td>
		</tr>
		<tr>
			<td colspan='2'>Address - number, street name<br>
			<input type="text" name="c_address" style="width: 100%" size="60"
				maxlength="80"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_address", "")) %>" />
			</td>
			<td width="25%">Apt/Suite/Unit<br>
			<input type="text" name="c_apt" style="width: 100%" size="20"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_apt", "")) %>" />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" colspan='2'>City/Town<br>
			<input type="text" name="c_city" style="width: 100%" size="60"
				maxlength="80"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_city", "")) %>" />
			</td>
			<td valign="top" width="8%">Province<br>
			<input type="text" name="c_province" style="width: 100%" size="60"
				maxlength="80"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_province", "")) %>" />
			</td>
			<td width="12%">Postal Code<br>
			<input type="text" name="c_postal" style="width: 100%" size="7"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_postal", "")) %>" />
			</td>
			<td colspan="2" width="25%">Partner's Last Name<br>
			<input type="text" name="c_partnerLastName" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("c_partnerLastName", "") %>" /></td>
			<td colspan="2" width="25%">Partner's First Name<br>
			<input type="text" name="c_partnerFirstName" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("c_partnerFirstName", "") %>" /></td>
		</tr>
		<tr>
			<td width="15%">Telephone - Home<br>
			<input type="text" name="pg1_homePhone" size="15" style="width: 100%"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_homePhone", "")) %>" />
			</td>
			<td width="15%">Telephone - Work<br>
			<input type="text" name="pg1_workPhone" size="15" style="width: 100%"
				maxlength="20" value="<%= props.getProperty("pg1_workPhone", "") %>" />
			</td>
			<td colspan='2'>Language<br>
			<input type="text" name="pg1_language" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_language", "")) %>" />
			</td>
			<td width="20%">Partner's Occupation<br>
			<input type="text" name="pg1_partnerOccupation" size="15"
				style="width: 100%" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerOccupation", "")) %>" />
			</td>
			<td colspan="2">Partner's Education level <br>
			<input type="text" name="pg1_partnerEduLevel" size="15"
				style="width: 100%" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerEduLevel", "")) %>" />
			</td>
			<td width="8%">Age<br>
			<input type="text" name="pg1_partnerAge" style="width: 100%" size="5"
				maxlength="5" value="<%= props.getProperty("pg1_partnerAge", "") %>" />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="12%">Date of birth<br>
			<input type="text" name="pg1_dateOfBirth" style="width: 100%"
				size="10" maxlength="10"
				value="<%= props.getProperty("pg1_dateOfBirth", "") %>"
				readonly="true" /></td>
			<td width="8%">Age<br>
			<input type="text" name="pg1_age" style="width: 100%" size="10"
				maxlength="10" value="<%= props.getProperty("pg1_age", "") %>" /></td>
			<td width="15%">Occupation<br>
			<input type="text" name="pg1_occupation" size="15"
				style="width: 100%" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_occupation", "")) %>" />
			</td>
			<td width="15%">Educational level <br>
			<input type="text" name="pg1_eduLevel" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_eduLevel", "")) %>" />
			</td>
			<td colspan="3" width="50%">Ethnic or Racial backgrounds: Mother
			/ Father <br>
			<input type="text" name="pg1_ethnicBg" size="100" style="width: 100%"
				maxlength="200"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_ethnicBg", "")) %>" />
			</td>
		</tr>
		<tr>
			<td colspan="2" width="20%">OHIP No. <br>
			<input type="text" name="c_hin" size="10" style="width: 100%"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_hin", "")) %>" />
			</td>
			<td width="15%">Patient File No. <br>
			<input type="text" name="c_fileNo" size="10" style="width: 100%"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_fileNo", "")) %>" />
			</td>
			<td width="15%" nowrap>Marital status <br>
			<input type="checkbox" name="pg1_msMarried"
				<%= props.getProperty("pg1_msMarried", "") %> />M <input
				type="checkbox" name="pg1_msCommonLaw"
				<%= props.getProperty("pg1_msCommonLaw", "") %> />CL <input
				type="checkbox" name="pg1_msSingle"
				<%= props.getProperty("pg1_msSingle", "") %> />S</td>
			<td width="17%"><font size="-1">Birth attendants<br>
			<input type="checkbox" name="pg1_baObs"
				<%= props.getProperty("pg1_baObs", "") %> />OBS <input
				type="checkbox" name="pg1_baFP"
				<%= props.getProperty("pg1_baFP", "") %> />FP <input
				type="checkbox" name="pg1_baMidwife"
				<%= props.getProperty("pg1_baMidwife", "") %> />Midwife<br>
			</font> <input type="text" name="c_ba" size="10" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_ba", "")) %>" />
			</td>
			<td width="17%"><font size="-1">Newborn care<br>
			<input type="checkbox" name="pg1_ncPed"
				<%= props.getProperty("pg1_ncPed", "") %> />Ped. <input
				type="checkbox" name="pg1_ncFP"
				<%= props.getProperty("pg1_ncFP", "") %> />FP <input type="checkbox"
				name="pg1_ncMidwife" <%= props.getProperty("pg1_ncMidwife", "") %> />Midwife<br>
			</font> <input type="text" name="c_nc" size="10" style="width: 100%"
				maxlength="25" value="<%= props.getProperty("c_nc", "") %>" /></td>
			<td>Family physician<br>
			<input type="text" name="c_famPhys" size="30" maxlength="80"
				style="width: 100%"
				value="<%= props.getProperty("c_famPhys", "") %>" /></td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%">Allergies or Sensitivities (describe reaction
			details)<br>
			<div align="center"><textarea name="c_allergies"
				style="width: 100%" cols="30" rows="2"><%= props.getProperty("c_allergies", "") %></textarea></div>
			</td>
			<td width="50%">Medications/Herbals<br>
			<div align="center"><textarea name="c_meds" style="width: 100%"
				cols="30" rows="2"><%= props.getProperty("c_meds", "") %></textarea></div>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr bgcolor="#99FF99">
			<td valign="top" bgcolor="#CCCCCC">
			<div align="center"><b>Pregnancy Summary</b></div>
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" nowrap>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top" nowrap>LMP <input type="text"
						name="pg1_menLMP" id="pg1_menLMP" size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_menLMP", "")) %>" />
					<img src="../images/cal.gif" id="pg1_menLMP_cal"></td>
					<td>Certain</td>
					<td>Yes <input type="checkbox" name="pg1_psCertY"
						<%= props.getProperty("pg1_psCertY", "") %> /> No <input
						type="checkbox" name="pg1_psCertN"
						<%= props.getProperty("pg1_psCertN", "") %> /></td>
				</tr>
				<tr>
					<td>Cycle q__ <input type="text" name="pg1_menCycle" size="7"
						maxlength="7"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_menCycle", "")) %>" />
					</td>
					<td>Regular</td>
					<td>Yes <input type="checkbox" name="pg1_menReg"
						<%= props.getProperty("pg1_menReg", "") %> /> No <input
						type="checkbox" name="pg1_menRegN"
						<%= props.getProperty("pg1_menRegN", "") %> /></td>
				</tr>
				<tr>
					<td>Contraceptive type <input type="text" name="pg1_contracep"
						size="15" maxlength="25"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_contracep", "")) %>" />
					<!--  input type="checkbox" name="pg1_iud" <%= props.getProperty("pg1_iud", "") %>/>IUD
            <input type="checkbox" name="pg1_hormone" <%= props.getProperty("pg1_hormone", "") %>/>Hormonal(type)
            <input type="text" name = "pg1_hormoneType" size="15" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_hormoneType", "")) %>"/>
            <input type="checkbox" name="pg1_otherAR1" <%= props.getProperty("pg1_otherAR1", "") %>/>Other
            <input type="text" name="pg1_otherAR1Name" size="15" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_otherAR1Name", "")) %>"/>
            --></td>
					<td>Last used</td>
					<td><input type="text" name="pg1_lastUsed" id="pg1_lastUsed"
						size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_lastUsed", "")) %>" />
					<img src="../images/cal.gif" id="pg1_lastUsed_cal"></td>
				</tr>
			</table>

			</td>
			<td valign="top" nowrap>EDB (by dates)</br>
			<input type="text" name="pg1_menEDB" id="pg1_menEDB" class="spe"
				onDblClick="calByLMP(this);" size="10" maxlength="10"
				value="<%= props.getProperty("pg1_menEDB", "") %>" /> <img
				src="../images/cal.gif" id="pg1_menEDB_cal"></td>


			<td valign="top" width="35%" rowspan="2">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top" align="center"><font size="+1"><b>Final
					EDB</font></b></font><br>
					<input type="text" name="c_finalEDB" id="c_finalEDB" size="10"
						maxlength="10" value="<%= props.getProperty("c_finalEDB", "") %>" />
					<img src="../images/cal.gif" id="c_finalEDB_cal"></td>
					<td width="40%"><u>Dating Method</u></br>
					<input type="checkbox" name="pg1_edbByDate"
						<%= props.getProperty("pg1_edbByDate", "") %> />Dates<br>
					<input type="checkbox" name="pg1_edbByT1"
						<%= props.getProperty("pg1_edbByT1", "") %> />T1US<br>
					<input type="checkbox" name="pg1_edbByT2"
						<%= props.getProperty("pg1_edbByT2", "") %> />T2US<br>
					<input type="checkbox" name="pg1_edbByART"
						<%= props.getProperty("pg1_edbByART", "") %> />ART (e.g. IVF)</td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td colspan="2">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>Gravida <input type="text" name="c_gravida" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_gravida", "")) %>" />
					</td>
					<td>Term <input type="text" name="c_term" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_term", "")) %>" />
					</td>
					<td>Premature <input type="text" name="c_prem" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_prem", "")) %>" />
					</td>
					<td valign="top" nowrap>Abortuses <input type="text"
						name="c_abort" size="1" maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_abort", "")) %>" />
					<!--     No. of pregnancy loss(es)<br>&nbsp; &nbsp;
		            <input type="checkbox" name="pg1_ectopic" <%= props.getProperty("pg1_ectopic", "") %>/>Ectopic
		            <input type="text" name="pg1_ectopicBox" size="2" maxlength="2" value="<%= props.getProperty("pg1_ectopicBox", "") %>" />&nbsp; &nbsp;
		            <input type="checkbox" name="pg1_termination" <%= props.getProperty("pg1_termination", "") %> />Termination
		            <input type="text" name="pg1_terminationBox" size="2" maxlength="2" value="<%= props.getProperty("pg1_terminationBox", "") %>" />&nbsp; &nbsp;
		            <input type="checkbox" name="pg1_spontaneous" <%= props.getProperty("pg1_spontaneous", "") %> />Spontaneous
		            <input type="text" name="pg1_spontaneousBox" size="2" maxlength="2" value="<%= props.getProperty("pg1_spontaneousBox", "") %>" />&nbsp; &nbsp;
		            <input type="checkbox" name="pg1_stillborn" <%= props.getProperty("pg1_stillborn", "") %> />Stillborn
		            <input type="text" name="pg1_stillbornBox" size="2" maxlength="2" value="<%= props.getProperty("pg1_stillbornBox", "") %>" />
		        --></td>
					<td>Living <input type="text" name="c_living" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_living", "")) %>" />
					</td>
					<!--  td nowrap>Multipregnancy<br>
		            No.<input type="text" name="pg1_multi" size="5" style="width:100%" maxlength="10" value="<%= props.getProperty("pg1_multi", "") %>"/>
			    </td>-->
				</tr>
			</table>

			</td>
		</tr>

	</table>
	<table width="100%" border="0">
		<tr bgcolor="#99FF99">
			<td align="center" colspan="2" bgcolor="#CCCCCC"><b>Obstetrical
			History</b></td>
		</tr>
		<tr>
			<td valign="top">
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr align="center">
					<td width="20">No.</td>
					<td width="40">Year</td>
					<td width="30">Sex<br>
					M/F</td>
					<td width="60">Gest. age<br>
					(weeks)</td>
					<td width="60">Birth<br>
					weight</td>
					<td width="60">Length<br>
					of labour</td>
					<td width="80">Place<br>
					of birth</td>
					<td width="90">Type of delivery<br>
					<font size="-1">SVB CS Ass'd</font></td>
					<td nowrap>Comments regarding pregnancy and birth</td>
				</tr>
				<tr align="center">
					<td>1</td>
					<td><input type="text" name="pg1_year1" size="5" maxlength="8"
						style="width: 90%"
						value="<%= props.getProperty("pg1_year1", "") %>" /></td>
					<td><input type="text" name="pg1_sex1" size="1" maxlength="1"
						style="width: 50%"
						value="<%= props.getProperty("pg1_sex1", "") %>" /></td>
					<td><input type="text" name="pg1_oh_gest1" size="3"
						maxlength="5" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_gest1", "")) %>" /></td>
					<td><input type="text" name="pg1_weight1" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_weight1", "")) %>" /></td>
					<td><input type="text" name="pg1_length1" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_length1", "")) %>" /></td>
					<td><input type="text" name="pg1_place1" size="8"
						maxlength="20" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_place1", "")) %>" /></td>
					<td><input type="checkbox" name="pg1_svb1"
						<%= props.getProperty("pg1_svb1", "") %> /> <input type="checkbox"
						name="pg1_cs1" <%= props.getProperty("pg1_cs1", "") %> /> <input
						type="checkbox" name="pg1_ass1"
						<%= props.getProperty("pg1_ass1", "") %> /></td>
					<td align="left"><input type="text" name="pg1_oh_comments1"
						size="20" maxlength="80" style="width: 100%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_comments1", "")) %>" /></td>
				</tr>
				<tr align="center">
					<td>2</td>
					<td><input type="text" name="pg1_year2" size="5" maxlength="8"
						style="width: 90%"
						value="<%= props.getProperty("pg1_year2", "") %>" /></td>
					<td><input type="text" name="pg1_sex2" size="1" maxlength="1"
						style="width: 50%"
						value="<%= props.getProperty("pg1_sex2", "") %>" /></td>
					<td><input type="text" name="pg1_oh_gest2" size="3"
						maxlength="5" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_gest2", "")) %>" /></td>
					<td><input type="text" name="pg1_weight2" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_weight2", "")) %>" /></td>
					<td><input type="text" name="pg1_length2" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_length2", "")) %>" /></td>
					<td><input type="text" name="pg1_place2" size="8"
						maxlength="20" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_place2", "")) %>" /></td>
					<td><input type="checkbox" name="pg1_svb2"
						<%= props.getProperty("pg1_svb2", "") %> /> <input type="checkbox"
						name="pg1_cs2" <%= props.getProperty("pg1_cs2", "") %> /> <input
						type="checkbox" name="pg1_ass2"
						<%= props.getProperty("pg1_ass2", "") %> /></td>
					<td align="left"><input type="text" name="pg1_oh_comments2"
						size="20" maxlength="80" style="width: 100%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_comments2", "")) %>" /></td>
				</tr>
				<tr align="center">
					<td>3</td>
					<td><input type="text" name="pg1_year3" size="5" maxlength="8"
						style="width: 90%"
						value="<%= props.getProperty("pg1_year3", "") %>" /></td>
					<td><input type="text" name="pg1_sex3" size="1" maxlength="1"
						style="width: 50%"
						value="<%= props.getProperty("pg1_sex3", "") %>" /></td>
					<td><input type="text" name="pg1_oh_gest3" size="3"
						maxlength="5" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_gest3", "")) %>" /></td>
					<td><input type="text" name="pg1_weight3" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_weight3", "")) %>" /></td>
					<td><input type="text" name="pg1_length3" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_length3", "")) %>" /></td>
					<td><input type="text" name="pg1_place3" size="8"
						maxlength="20" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_place3", "")) %>" /></td>
					<td><input type="checkbox" name="pg1_svb3"
						<%= props.getProperty("pg1_svb3", "") %> /> <input
						type="checkbox" name="pg1_cs3"
						<%= props.getProperty("pg1_cs3", "") %> /> <input type="checkbox"
						name="pg1_ass3" <%= props.getProperty("pg1_ass3", "") %> /></td>
					<td align="left"><input type="text" name="pg1_oh_comments3"
						size="20" maxlength="80" style="width: 100%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_comments3", "")) %>" /></td>
				</tr>
				<tr align="center">
					<td>4</td>
					<td><input type="text" name="pg1_year4" size="5" maxlength="8"
						style="width: 90%"
						value="<%= props.getProperty("pg1_year4", "") %>" /></td>
					<td><input type="text" name="pg1_sex4" size="1" maxlength="1"
						style="width: 50%"
						value="<%= props.getProperty("pg1_sex4", "") %>" /></td>
					<td><input type="text" name="pg1_oh_gest4" size="3"
						maxlength="5" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_gest4", "")) %>" /></td>
					<td><input type="text" name="pg1_weight4" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_weight4", "")) %>" /></td>
					<td><input type="text" name="pg1_length4" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_length4", "")) %>" /></td>
					<td><input type="text" name="pg1_place4" size="8"
						maxlength="20" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_place4", "")) %>" /></td>
					<td><input type="checkbox" name="pg1_svb4"
						<%= props.getProperty("pg1_svb4", "") %> /> <input
						type="checkbox" name="pg1_cs4"
						<%= props.getProperty("pg1_cs4", "") %> /> <input type="checkbox"
						name="pg1_ass4" <%= props.getProperty("pg1_ass4", "") %> /></td>
					<td align="left"><input type="text" name="pg1_oh_comments4"
						size="20" maxlength="80" style="width: 100%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_comments4", "")) %>" /></td>
				</tr>
				<tr align="center">
					<td>5</td>
					<td><input type="text" name="pg1_year5" size="5" maxlength="8"
						style="width: 90%"
						value="<%= props.getProperty("pg1_year5", "") %>" /></td>
					<td><input type="text" name="pg1_sex5" size="1" maxlength="1"
						style="width: 50%"
						value="<%= props.getProperty("pg1_sex5", "") %>" /></td>
					<td><input type="text" name="pg1_oh_gest5" size="3"
						maxlength="5" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_gest5", "")) %>" /></td>
					<td><input type="text" name="pg1_weight5" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_weight5", "")) %>" /></td>
					<td><input type="text" name="pg1_length5" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_length5", "")) %>" /></td>
					<td><input type="text" name="pg1_place5" size="8"
						maxlength="20" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_place5", "")) %>" /></td>
					<td><input type="checkbox" name="pg1_svb5"
						<%= props.getProperty("pg1_svb5", "") %> /> <input
						type="checkbox" name="pg1_cs5"
						<%= props.getProperty("pg1_cs5", "") %> /> <input type="checkbox"
						name="pg1_ass5" <%= props.getProperty("pg1_ass5", "") %> /></td>
					<td align="left"><input type="text" name="pg1_oh_comments5"
						size="20" maxlength="80" style="width: 100%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_comments5", "")) %>" /></td>
				</tr>
				<tr align="center">
					<td>6</td>
					<td><input type="text" name="pg1_year6" size="5" maxlength="8"
						style="width: 90%"
						value="<%= props.getProperty("pg1_year6", "") %>" /></td>
					<td><input type="text" name="pg1_sex6" size="1" maxlength="1"
						style="width: 50%"
						value="<%= props.getProperty("pg1_sex6", "") %>" /></td>
					<td><input type="text" name="pg1_oh_gest6" size="3"
						maxlength="5" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_gest6", "")) %>" /></td>
					<td><input type="text" name="pg1_weight6" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_weight6", "")) %>" /></td>
					<td><input type="text" name="pg1_length6" size="5"
						maxlength="6" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_length6", "")) %>" /></td>
					<td><input type="text" name="pg1_place6" size="8"
						maxlength="20" style="width: 80%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_place6", "")) %>" /></td>
					<td><input type="checkbox" name="pg1_svb6"
						<%= props.getProperty("pg1_svb6", "") %> /> <input
						type="checkbox" name="pg1_cs6"
						<%= props.getProperty("pg1_cs6", "") %> /> <input type="checkbox"
						name="pg1_ass6" <%= props.getProperty("pg1_ass6", "") %> /></td>
					<td align="left"><input type="text" name="pg1_oh_comments6"
						size="20" maxlength="80" style="width: 100%"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_oh_comments6", "")) %>" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>


	<table class="shrinkMe" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr rowspan="2">
			<td width="65%">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3" align="center" bgcolor="#CCCCCC" nowrap><b>
					Medical History and Physical Exam (provide details in comments)</b></td>
				</tr>
				<tr>
					<td valign="top" width="33%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>Current Pregnancy</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td width="6%">1.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c1p1");return false;'>Bleeding</a></td>
							<td><input type="checkbox" name="pg1_cp1"
								<%= props.getProperty("pg1_cp1", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp1N"
								<%= props.getProperty("pg1_cp1N", "") %> /></td>
						</tr>
						<tr>
							<td>2.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c2p1");return false;'>Nausea,
							vomiting</a></td>
							<td><input type="checkbox" name="pg1_cp2"
								<%= props.getProperty("pg1_cp2", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp2N"
								<%= props.getProperty("pg1_cp2N", "") %> /></td>
						</tr>
						<tr>
							<td>3.</td>
							<td nowrap><a href=#
								onClick='popupPage("<%=resource%>c3p1");return false;'>Smoking</a>
							<font size=1> <input type="text" name="pg1_box3" size="1"
								maxlength="3"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box3", "")) %>">cig/day</font>
							</td>
							<td><input type="checkbox" name="pg1_cp3"
								<%= props.getProperty("pg1_cp3", "") %>></td>
							<td><input type="checkbox" name="pg1_cp3N"
								<%= props.getProperty("pg1_cp3N", "") %>></td>
						</tr>
						<tr>
							<td>4.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c4p1");return false;'>Alcohol,
							street drugs</a></td>
							<td><input type="checkbox" name="pg1_cp4"
								<%= props.getProperty("pg1_cp4", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp4N"
								<%= props.getProperty("pg1_cp4N", "") %> /></td>
						</tr>
						<!--  tr>
                    <td valign="top">5.</td>
                    <td nowrap><a href=# onClick='popupPage("<%=resource%>c5p1");return false;'>Alcohol</a><br>
                        <font size=1>drinks/day
                        <input type="text" name="pg1_box5" size="2" maxlength="3" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box5", "")) %>"></font>
                    </td>
                    <td valign="bottom"><input type="checkbox" name="pg1_cp5" <%= props.getProperty("pg1_cp5", "") %>/></td>
                </tr> -->
						<tr>
							<td valign="top">5.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c8p1");return false;'>Occup/Environ.
							risks</a></td>
							<td><input type="checkbox" name="pg1_cp8"
								<%= props.getProperty("pg1_cp8", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp8N"
								<%= props.getProperty("pg1_cp8", "") %> /></td>
						</tr>
						<tr>
							<td valign="top">6.</td>
							<td>Dietary restrictions</td>
							<td><input type="checkbox" name="pg1_naDietRes"
								<%= props.getProperty("pg1_naDietRes", "") %>></td>
							<td><input type="checkbox" name="pg1_naDietBal"
								<%= props.getProperty("pg1_naDietBal", "") %>></td>
						</tr>
						<tr>
							<td valign="top">7.</td>
							<td>Calcium adequate</td>
							<td><input type="checkbox" name="pg1_naMilk"
								<%= props.getProperty("pg1_naMilk", "") %>></td>
							<td><input type="checkbox" name="pg1_naMilkN"
								<%= props.getProperty("pg1_naMilkN", "") %>></td>
						</tr>
						<tr>
							<td valign="top">8.</td>
							<td>Preconceptual folate</td>
							<td><input type="checkbox" name="pg1_naFolic"
								<%= props.getProperty("pg1_naFolic", "") %>></td>
							<td><input type="checkbox" name="pg1_naFolicN"
								<%= props.getProperty("pg1_naFolicN", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" nowrap><b>Medical History</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td>9.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c9p1");return false;'>Hypertension</a></td>
							<td><input type="checkbox" name="pg1_yes9"
								<%= props.getProperty("pg1_yes9", "") %>></td>
							<td><input type="checkbox" name="pg1_no9"
								<%= props.getProperty("pg1_no9", "") %>></td>
						</tr>
						<tr>
							<td>10.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c10p1");return false;'>Endocrine</a></td>
							<td><input type="checkbox" name="pg1_yes10"
								<%= props.getProperty("pg1_yes10", "") %>></td>
							<td><input type="checkbox" name="pg1_no10"
								<%= props.getProperty("pg1_no10", "") %>></td>
						</tr>
						<!--  tr>
                    <td>11.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c11p1");return false;'>Heart</a></td>
                    <td><input type="checkbox" name="pg1_yes11" <%= props.getProperty("pg1_yes11", "") %>></td>
                    <td><input type="checkbox" name="pg1_no11" <%= props.getProperty("pg1_no11", "") %>></td>
                </tr>-->
						<tr>
							<td>11.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c12p1");return false;'>Urinary
							tract</a></td>
							<td><input type="checkbox" name="pg1_yes12"
								<%= props.getProperty("pg1_yes12", "") %>></td>
							<td><input type="checkbox" name="pg1_no12"
								<%= props.getProperty("pg1_no12", "") %>></td>
						</tr>
						<tr>
							<td>12.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c13p1");return false;'>Cardiac/Pulmonary</a></td>
							<td><input type="checkbox" name="pg1_yes13"
								<%= props.getProperty("pg1_yes13", "") %>></td>
							<td><input type="checkbox" name="pg1_no13"
								<%= props.getProperty("pg1_no13", "") %>></td>
						</tr>
						<tr>
							<td>13.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c14p1");return false;'>Liver,
							hepatitis, GI</a></td>
							<td><input type="checkbox" name="pg1_yes14"
								<%= props.getProperty("pg1_yes14", "") %>></td>
							<td><input type="checkbox" name="pg1_no14"
								<%= props.getProperty("pg1_no14", "") %>></td>
						</tr>
						<tr>
							<td>14.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c17p1");return false;'>Gynaecology/Breast</a></td>
							<td><input type="checkbox" name="pg1_yes17"
								<%= props.getProperty("pg1_yes17", "") %>></td>
							<td><input type="checkbox" name="pg1_no17"
								<%= props.getProperty("pg1_no17", "") %>></td>
						</tr>
						<tr>
							<td>15.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c22p1");return false;'>Hem./Immunology</a></td>
							<td><input type="checkbox" name="pg1_yes22"
								<%= props.getProperty("pg1_yes22", "") %>></td>
							<td><input type="checkbox" name="pg1_no22"
								<%= props.getProperty("pg1_no22", "") %>></td>
						</tr>
						<tr>
							<td>16.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c20p1");return false;'>Surgeries</a></td>
							<td><input type="checkbox" name="pg1_yes20"
								<%= props.getProperty("pg1_yes20", "") %>></td>
							<td><input type="checkbox" name="pg1_no20"
								<%= props.getProperty("pg1_no20", "") %>></td>
						</tr>
						<tr>
							<td>17.</td>
							<td>Blood transfusion</td>
							<td><input type="checkbox" name="pg1_bloodTranY"
								<%= props.getProperty("pg1_bloodTranY", "") %>></td>
							<td><input type="checkbox" name="pg1_bloodTranN"
								<%= props.getProperty("pg1_bloodTranN", "") %>></td>
						</tr>
						<tr>
							<td>18.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c21p1");return false;'>Anesthetics
							compl.</a></td>
							<td><input type="checkbox" name="pg1_yes21"
								<%= props.getProperty("pg1_yes21", "") %>></td>
							<td><input type="checkbox" name="pg1_no21"
								<%= props.getProperty("pg1_no21", "") %>></td>
						</tr>
						<tr>
							<td>19.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c24p1");return false;'>Psychiatric</a></td>
							<td><input type="checkbox" name="pg1_yes24"
								<%= props.getProperty("pg1_yes24", "") %>></td>
							<td><input type="checkbox" name="pg1_no24"
								<%= props.getProperty("pg1_no24", "") %>></td>
						</tr>
						<tr>
							<td>20.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c15p1");return false;'>Epilepsy/Neurological</a></td>
							<td><input type="checkbox" name="pg1_yes15"
								<%= props.getProperty("pg1_yes15", "") %>></td>
							<td><input type="checkbox" name="pg1_no15"
								<%= props.getProperty("pg1_no15", "") %>></td>
						</tr>
						<tr>
							<td>21.</td>
							<td>Other <input type="text" name="pg1_box25" size="8"
								maxlength="25"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box25", "")) %>">
							</td>
							<td><input type="checkbox" name="pg1_yes25"
								<%= props.getProperty("pg1_yes25", "") %>></td>
							<td><input type="checkbox" name="pg1_no25"
								<%= props.getProperty("pg1_no25", "") %>></td>
						</tr>
					</table>


					</td>
					<td valign="top" width="35%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>Genetic History</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td>22.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c27p1");return false;'>At
							risk population</a></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_yes27" <%= props.getProperty("pg1_yes27", "") %>></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_no27" <%= props.getProperty("pg1_no27", "") %>></td>
						</tr>
						<tr>
							<td colspan="4"><span class="small">(e.g.:Ashkenazi,
							consanguinity, CF, sickle cell,Tay Sachs,thalassemia)</span></td>
						</tr>
						<tr>
							<td colspan="4" nowrap><u>Family history of:</u></td>
						</tr>
						<tr>
							<td>23.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c31p1");return false;'>Developmental
							delay</a></td>
							<td align="center"><input type="checkbox" name="pg1_yes31"
								<%= props.getProperty("pg1_yes31", "") %>></td>
							<td align="center"><input type="checkbox" name="pg1_no31"
								<%= props.getProperty("pg1_no31", "") %>></td>
						</tr>
						<tr>
							<td>24.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c32p1");return false;'>Congenital
							anomalies</a></td>
							<td align="center"><input type="checkbox" name="pg1_yes32"
								<%= props.getProperty("pg1_yes32", "") %>></td>
							<td align="center"><input type="checkbox" name="pg1_no32"
								<%= props.getProperty("pg1_no32", "") %>></td>
						</tr>
						<!--  tr>
                    <td>33.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c33p1");return false;'>Congenital hypotonias</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes33" <%= props.getProperty("pg1_yes33", "") %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no33" <%= props.getProperty("pg1_no33", "") %>></td>
                </tr>-->
						<tr>
							<td>25.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c34p1");return false;'>Chromosomal
							disorders</a></td>
							<td align="center"><input type="checkbox" name="pg1_yes34"
								<%= props.getProperty("pg1_yes34", "") %>></td>
							<td align="center"><input type="checkbox" name="pg1_no34"
								<%= props.getProperty("pg1_no34", "") %>></td>
						</tr>
						<tr>
							<td>26.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c35p1");return false;'>Genetic
							disorders</a></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_yes35" <%= props.getProperty("pg1_yes35", "") %>></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_no35" <%= props.getProperty("pg1_no35", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" nowrap><b>Infectious Disease</b></td>
						</tr>
						<tr>
							<td>27.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c40p1");return false;'>Varicella
							susceptible</a></td>
							<td><input type="checkbox" name="pg1_idt40"
								<%= props.getProperty("pg1_idt40", "") %>></td>
							<td><input type="checkbox" name="pg1_idt40N"
								<%= props.getProperty("pg1_idt40N", "") %>></td>
						</tr>
						<tr>
							<td>28.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c38p1");return false;'>STDs/HSV/BV</a></td>
							<td><input type="checkbox" name="pg1_idt38"
								<%= props.getProperty("pg1_idt38", "") %>></td>
							<td><input type="checkbox" name="pg1_idt38N"
								<%= props.getProperty("pg1_idt38N", "") %>></td>
						</tr>
						<tr>
							<td>29.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c42p1");return false;'>Tuberculosis
							risk</a></td>
							<!--  input type="text" name="pg1_box42" size="10" maxlength="20" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box42", "")) %>"></td>-->
							<td><input type="checkbox" name="pg1_idt42"
								<%= props.getProperty("pg1_idt42", "") %>></td>
							<td><input type="checkbox" name="pg1_idt42N"
								<%= props.getProperty("pg1_idt42N", "") %>></td>
						</tr>
						<tr>
							<td>30.</td>
							<td>Other <input type="text" name="pg1_infectDisOther"
								size="10" maxlength="20"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_infectDisOther", "")) %>"></td>
							<td><input type="checkbox" name="pg1_infectDisOtherY"
								<%= props.getProperty("pg1_infectDisOtherY", "") %>></td>
							<td><input type="checkbox" name="pg1_infectDisOtherN"
								<%= props.getProperty("pg1_infectDisOtherN", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" nowrap><b>Psychosocial</b></td>
						</tr>
						<tr>
							<td>31.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c43p1");return false;'>Poor
							social support</a></td>
							<td><input type="checkbox" name="pg1_pdt43"
								<%= props.getProperty("pg1_pdt43", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt43N"
								<%= props.getProperty("pg1_pdt43N", "") %>></td>
						</tr>
						<tr>
							<td>32.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c44p1");return false;'>Relationship
							problems</a></td>
							<td><input type="checkbox" name="pg1_pdt44"
								<%= props.getProperty("pg1_pdt44", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt44N"
								<%= props.getProperty("pg1_pdt44N", "") %>></td>
						</tr>
						<tr>
							<td>33.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c45p1");return false;'>Emotional/Depression</a></td>
							<td><input type="checkbox" name="pg1_pdt45"
								<%= props.getProperty("pg1_pdt45", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt45N"
								<%= props.getProperty("pg1_pdt45N", "") %>></td>
						</tr>
						<tr>
							<td>34.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c46p1");return false;'>Substance
							abuse</a></td>
							<td><input type="checkbox" name="pg1_pdt46"
								<%= props.getProperty("pg1_pdt46", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt46N"
								<%= props.getProperty("pg1_pdt46N", "") %>></td>
						</tr>
						<tr>
							<td>35.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c47p1");return false;'>Family
							violence</a></td>
							<td><input type="checkbox" name="pg1_pdt47"
								<%= props.getProperty("pg1_pdt47", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt47N"
								<%= props.getProperty("pg1_pdt47N", "") %>></td>
						</tr>
						<tr>
							<td>36.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c48p1");return false;'>Parenting
							concerns</a></td>
							<td><input type="checkbox" name="pg1_pdt48"
								<%= props.getProperty("pg1_pdt48", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt48N"
								<%= props.getProperty("pg1_pdt48N", "") %>></td>
						</tr>
						<tr>
							<td>37.</td>
							<td>Relig./Cultural issues</td>
							<td><input type="checkbox" name="pg1_reliCultY"
								<%= props.getProperty("pg1_reliCultY", "") %>></td>
							<td><input type="checkbox" name="pg1_reliCultN"
								<%= props.getProperty("pg1_reliCultN", "") %>></td>
						</tr>
					</table>

					</td>
					<td valign="top">

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>Family History</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td>38.</td>
							<td>At risk population</td>
							<td><input type="checkbox" name="pg1_fhRiskY"
								<%= props.getProperty("pg1_fhRiskY", "") %>></td>
							<td><input type="checkbox" name="pg1_fhRiskN"
								<%= props.getProperty("pg1_fhRiskN", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">(e.g.: DM, DVT/PE, PIH/HT,</br>
							postpartum depression, thyroid)</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" nowrap><b>Physical Examination</b></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4">Ht.<input type="text" name="pg1_ht"
								onDblClick="htEnglish2Metric(this);" size="5" maxlength="6"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_ht", "")) %>" />
							Wt.<input type="text" name="pg1_wt"
								onDblClick="wtEnglish2Metric(this);" size="5" maxlength="6"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_wt", "")) %>" />
							</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4">BMI<input type="text" name="c_bmi"
								class="spe" onDblClick="calcBMIMetric(this);" size="6"
								maxlength="6"
								value="<%= UtilMisc.htmlEscape(props.getProperty("c_bmi", "")) %>">
							BP<input type="text" name="pg1_BP" size="6" maxlength="10"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_BP", "")) %>">
							</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
							<td align="center" width="12%">N</td>
							<td align="center" nowrap width="12%">Abn</td>
						</tr>
						<tr>
							<td>39.</td>
							<td>Thyroid</td>
							<td><input type="checkbox" name="pg1_thyroid"
								<%= props.getProperty("pg1_thyroid", "") %>></td>
							<td><input type="checkbox" name="pg1_thyroidA"
								<%= props.getProperty("pg1_thyroidA", "") %>></td>
						</tr>
						<tr>
							<td>40.</td>
							<td>Chest</td>
							<td><input type="checkbox" name="pg1_chest"
								<%= props.getProperty("pg1_chest", "") %>></td>
							<td><input type="checkbox" name="pg1_chestA"
								<%= props.getProperty("pg1_chestA", "") %>></td>
						</tr>
						<tr>
							<td>41.</td>
							<td>Breasts</td>
							<td><input type="checkbox" name="pg1_breasts"
								<%= props.getProperty("pg1_breasts", "") %>></td>
							<td><input type="checkbox" name="pg1_breastsA"
								<%= props.getProperty("pg1_breastsA", "") %>></td>
						</tr>
						<tr>
							<td>42.</td>
							<td>Cardiovascular</td>
							<td><input type="checkbox" name="pg1_cardio"
								<%= props.getProperty("pg1_cardio", "") %>></td>
							<td><input type="checkbox" name="pg1_cardioA"
								<%= props.getProperty("pg1_cardioA", "") %>></td>
						</tr>
						<tr>
							<td>43.</td>
							<td>Abdomen</td>
							<td><input type="checkbox" name="pg1_abdomen"
								<%= props.getProperty("pg1_abdomen", "") %>></td>
							<td><input type="checkbox" name="pg1_abdomenA"
								<%= props.getProperty("pg1_abdomenA", "") %>></td>
						</tr>
						<tr>
							<td>44.</td>
							<td>Varicosities / Extrm.</td>
							<td><input type="checkbox" name="pg1_vari"
								<%= props.getProperty("pg1_vari", "") %>></td>
							<td><input type="checkbox" name="pg1_variA"
								<%= props.getProperty("pg1_variA", "") %>></td>
						</tr>
						<!-- tr>
                    <td>Neurological</td>
                    <td><input type="checkbox" name="pg1_neuro" <%= props.getProperty("pg1_neuro", "") %>></td>
                </tr>
                <tr>
                    <td>Pelvic architecture</td>
                    <td><input type="checkbox" name="pg1_pelvic" <%= props.getProperty("pg1_pelvic", "") %>></td>
                </tr>-->
						<tr>
							<td>45.</td>
							<td>External genitalia</td>
							<td><input type="checkbox" name="pg1_extGen"
								<%= props.getProperty("pg1_extGen", "") %>></td>
							<td><input type="checkbox" name="pg1_extGenA"
								<%= props.getProperty("pg1_extGenA", "") %>></td>
						</tr>
						<tr>
							<td>46.</td>
							<td>Cervix, vagina</td>
							<td><input type="checkbox" name="pg1_cervix"
								<%= props.getProperty("pg1_cervix", "") %>></td>
							<td><input type="checkbox" name="pg1_cervixA"
								<%= props.getProperty("pg1_cervixA", "") %>></td>
						</tr>
						<tr>
							<td>47.</td>
							<td>Uterus</td>
							<td><input type="checkbox" name="pg1_uterus"
								<%= props.getProperty("pg1_uterus", "") %>></td>
							<td><input type="checkbox" name="pg1_uterusA"
								<%= props.getProperty("pg1_uterusA", "") %>></td>
						</tr>
						<tr>
							<td>48.</td>
							<td>Size: <input type="text" name="pg1_uterusBox" size="3"
								maxlength="3"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_uterusBox", "")) %>">
							<span class="small"> weeks</span></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td>49.</td>
							<td>Adnexa</td>
							<td><input type="checkbox" name="pg1_adnexa"
								<%= props.getProperty("pg1_adnexa", "") %>></td>
							<td><input type="checkbox" name="pg1_adnexaA"
								<%= props.getProperty("pg1_adnexaA", "") %>></td>
						</tr>
						<tr>
							<td>50.</td>
							<td>Other <input type="text" name="pg1_pExOther" size="6"
								maxlength="20"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_pExOther", "")) %>"></td>
							<td><input type="checkbox" name="pg1_pExOtherN"
								<%= props.getProperty("pg1_pExOtherN", "") %>></td>
							<td><input type="checkbox" name="pg1_pExOtherA"
								<%= props.getProperty("pg1_pExOtherA", "") %>></td>
						</tr>
					</table>

					</td>
				</tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="4" align="center" bgcolor="#CCCCCC"><b><font
						face="Verdana, Arial, Helvetica, sans-serif"> Initial
					Laboratory Investigations</font></b></td>
				</tr>
				<tr>
					<td width="30%">Test</td>
					<td width="20%">Result</td>
					<td width="30%">Test</td>
					<td>Result</td>
				</tr>
				<tr>
					<td>Hb</td>
					<td><input type="text" name="pg1_labHb" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHb", "")) %>"></td>
					</td>
					<td>HIV</td>
					<td><input type="text" name="pg1_labHIV" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHIV", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>MCV</td>
					<td><input type="text" name="pg1_labMCV" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labMCV", "")) %>"></td>
					</td>
					<td colspan="2"><input type="checkbox"
						name="pg1_labHIVCounsel"
						<%= props.getProperty("pg1_labHIVCounsel", "") %>>
					Counseled and test declined</td>
				</tr>
				<tr>
					<td>ABO</td>
					<td><input type="text" name="pg1_labABO" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labABO", "")) %>"></td>
					</td>
					<td valign="top" rowspan="2">Last Pap <img
						src="../images/cal.gif" id="pg1_labLastPapDate_cal"> </br>
					<input type="text" name="pg1_labLastPapDate"
						id="pg1_labLastPapDate" size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labLastPapDate", "")) %>"></td>
					</td>
					<td valign="top" rowspan="2"><input type="text"
						name="pg1_labLastPap" size="10" maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labLastPap", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>Rh</td>
					<td><input type="text" name="pg1_labRh" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labRh", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>Antibody Screen</td>
					<td><input type="text" name="pg1_labAntiScr" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labAntiScr", "")) %>"></td>
					</td>
					<td>GC/Chlamydia</td>
					<td><input type="text" name="pg1_labGC" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labGC", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>Rubella immune</td>
					<td><input type="text" name="pg1_labRubella" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labRubella", "")) %>"></td>
					</td>
					<td>Urine C&S</td>
					<td><input type="text" name="pg1_labUrine" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labUrine", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>HBsAg</td>
					<td><input type="text" name="pg1_labHBsAg" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHBsAg", "")) %>"></td>
					</td>
					<td>
						<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg1_labExtra1Name" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labExtra1Name", "")) %>">
						<%}%>
					</td>
					<td>
						<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg1_labExtra1Value" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labExtra1Value", "")) %>">
						<%}%>
					</td>
				</tr>
				<tr>
					<td>VDRL</td>
					<td><input type="text" name="pg1_labVDRL" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labVDRL", "")) %>"></td>
					</td>
					<td>
						<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg1_labExtra2Name" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labExtra2Name", "")) %>">
						<%}%>
					</td>
					<td>
						<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg1_labExtra2Value" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labExtra2Value", "")) %>">
						<%}%>
					</td>
				</tr>
				<tr>
					<td>Sickle Cell</td>
					<td><input type="text" name="pg1_labSickle" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labSickle", "")) %>"></td>
					</td>
					<td>
						<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg1_labExtra3Name" size="10" maxlength="20" 
									value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labExtra3Name", "")) %>">
						<%}%>
					</td>
					<td>
						<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
						<input type="text" name="pg1_labExtra3Value" size="10" maxlength="20" 
							value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labExtra3Value", "")) %>">
						<%}%>
					</td>
				</tr>
			</table>
			<br>
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<th><u>Prenatal Genetic Investigations</u></th>
					<th>Result</th>
				</tr>
				<tr>
					<td>a) All ages-<a href=#
						onClick='popupPage("<%=resource%>c37p1");return false;'>MSS</a>,
					IPS, FTS</td>
					<td><input type="text" name="pg1_geneticA" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_geneticA", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td><a href=#
						onClick='popupPage("<%=resource%>c26p1");return false;'>b) Age
					&gt;= 35 at EDB-CVS/amnio</a></td>
					<td><input type="text" name="pg1_geneticB" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_geneticB", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>c) If a or b declined, or twins, then MSAFP</td>
					<td><input type="text" name="pg1_geneticC" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_geneticC", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>d) Counseled and test declined, or too late</td>
					<td align="center"><input type="checkbox" name="pg1_geneticD"
						<%= props.getProperty("pg1_geneticD", "") %>></td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr bgcolor="#CCCCCC">
			<th colspan="4"><b>Comments</b>
			<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
				(x4<input type="checkbox" name="pg1_4ColCom" id="pg1_4ColCom" onClick="commentMode(this.checked);"
						<%= props.getProperty("pg1_4ColCom", "") %> <%= props.getProperty("pg1_4ColCom") == null ? "checked='checked'" : "" %> />)
			<%}%>
			</th>
		</tr>
		<tr>
			<%if (OscarProperties.getInstance().getProperty("ar2005_enhance")!=null && OscarProperties.getInstance().getProperty("ar2005_enhance").equals("true")) {%>
				<%
						String commentSpan = "1";	
						String commentDisplay = "";
						if (props.getProperty("pg1_4ColCom") == "") {
							commentSpan = "4";
							commentDisplay = "display: none;";
						}
					 %>
					<td colspan="<%= commentSpan %>" id="pg1_commentsCol_1">
						<textarea name="pg1_commentsAR1" style="width: 100%;"
							 cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1", "") %></textarea>
					</td>
					<td colspan="1" id="pg1_commentsCol_2" style="<%= commentDisplay %>">
						<textarea name="pg1_commentsAR1_2" style="width: 100%;"
							 cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1_2", "") %></textarea>
					</td>
					<td colspan="1" id="pg1_commentsCol_3" style="<%= commentDisplay %>">
						<textarea name="pg1_commentsAR1_3" style="width: 100%;" 
							cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1_3", "") %></textarea>
					</td>
					<td colspan="1" id="pg1_commentsCol_4" style="<%= commentDisplay %>">
						<textarea name="pg1_commentsAR1_4" style="width: 100%;" 
							cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1_4", "") %></textarea>
					</td>
			<%} else {%>
			<td colspan="4"><textarea name="pg1_commentsAR1"
				style="width: 100%" cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1", "") %></textarea>
			</td>
			<%}%>
		</tr>
		<tr>
			<td width="30%">Signature<br>
			<input type="text" name="pg1_signature" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_signature", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg1_formDate" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg1_formDate", "") %>"></td>
			<td width="30%">Signature<br>
			<input type="text" name="pg1_signature2" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_signature2", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg1_formDate2" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg1_formDate2", "") %>"></td>
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
			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formonarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="javascript: popupPage('formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;</td>
			<td align="right"><b>Edit:</b> <a
				href="formonarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formonarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <a
				href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&query_name=search_formonarrisk');">AR
			Planner</a></td>
			<%
  }
%>
		</tr>
	</table>

</html:form>
<% if (bView || (props.getProperty("pg1_lockPage", "") != "")) { %>
<script type="text/javascript">
window.onload= function() {
	setLock(true);
}
</script>
<% } %>
</body>
<script type="text/javascript">
Calendar.setup({ inputField : "pg1_menLMP", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_menLMP_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_lastUsed", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_lastUsed_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_menEDB", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_menEDB_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "c_finalEDB", ifFormat : "%Y/%m/%d", showsTime :false, button : "c_finalEDB_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_labLastPapDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_labLastPapDate_cal", singleClick : true, step : 1 });

</script>
</html:html>
