<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

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

<%
	String formClass = "BCClientChartChecklist";
	String formLink = "formbcclientchartchecklist.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    //FrmData fd = new FrmData();
    //String resource = fd.getResource();
    //resource = resource + "ob/riskinfo/";
    //props.setProperty("c_lastVisited", "pg1");

	//get project_home
	String project_home = request.getContextPath().substring(1);
	boolean bSync = false;
	if(!props.getProperty("c_surname_cur", "").equals("") && !(props.getProperty("c_surname_cur", "").equals(props.getProperty("c_surname", "")) 
	        && props.getProperty("c_givenName_cur", "").equals(props.getProperty("c_givenName", ""))
	        && props.getProperty("c_address_cur", "").equals(props.getProperty("c_address", ""))
	        && props.getProperty("c_city_cur", "").equals(props.getProperty("c_city", ""))
	        && props.getProperty("c_province_cur", "").equals(props.getProperty("c_province", ""))
	        && props.getProperty("c_postal_cur", "").equals(props.getProperty("c_postal", ""))
	        //&& props.getProperty("c_phn_cur", "").equals(props.getProperty("c_phn", ""))
	        && props.getProperty("c_phone_cur", "").trim().equals(props.getProperty("c_phone", "").trim())
	        )) {
	    bSync = true;
	}
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Chart Checklist</title>
<link rel="stylesheet" type="text/css" href="bcArStyle.css">
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
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<html:base />
</head>

<script type="text/javascript" language="Javascript">
    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
    	windows.print();
    }



    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
			ret = checkAllTimes();
		}
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret==true) {
			ret = checkAllTimes();
		}
        if(ret == true) {
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
    
function syncDemo() { 
    document.forms[0].c_surname.value = "<%=props.getProperty("c_surname_cur", "")%>";
    document.forms[0].c_givenName.value = "<%=props.getProperty("c_givenName_cur", "")%>";
    document.forms[0].c_address.value = "<%=props.getProperty("c_address_cur", "")%>";
    document.forms[0].c_city.value = "<%=props.getProperty("c_city_cur", "")%>";
    document.forms[0].c_province.value = "<%=props.getProperty("c_province_cur", "")%>";
    document.forms[0].c_postal.value = "<%=props.getProperty("c_postal_cur", "")%>";
    document.forms[0].c_phn.value = "<%=props.getProperty("c_phn_cur", "")%>";
    document.forms[0].c_phone.value = "<%=props.getProperty("c_phone_cur", "")%>";
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

    function valDate(dateBox)   {
        try   {
            var dateString = dateBox.value;
            if(dateString == "") {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[2];  var m = dt[1];  var d = dt[0];
            //var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true) {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex) {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function valTime(dateBox)   {
        try   {
            var dateString = dateBox.value;
            if(dateString == "") {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split(':');
            var m = dt[1];  var h = dt[0];
            var pass = false;
			if(h >= 0 && h <=23 && m >=0 && m <=59) { pass = true; }

            if(pass!=true) {
                alert('Invalid data in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex) {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].pg1_formDate)==false){
            b = false;
        } else if(valDate(document.forms[0].birFHR)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate1)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate2)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate3)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate4)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate5)==false){
            b = false;
        } 

        return b;
    }

	function checkAllTimes() {
        var b = true;
        if(valTime(document.forms[0].birTimeHour1)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour2)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour3)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour4)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour5)==false){
            b = false;
        } 

        return b;
    }
    
function calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin) {
    var sdd = sDate.substring(0, sDate.indexOf("/"));
    var smm = eval(sDate.substring(eval(sDate.indexOf("/")+1), sDate.lastIndexOf("/")) - 1);
    var syyyy  = sDate.substring(eval(sDate.lastIndexOf("/")+1));
    var edd = eDate.substring(0, eDate.indexOf("/"));
    var emm = eval(eDate.substring(eval(eDate.indexOf("/")+1), eDate.lastIndexOf("/")) - 1);
    var eyyyy  = eDate.substring(eval(eDate.lastIndexOf("/")+1));
	tempV = sTime;
	sHour = tempV.substring(0, tempV.indexOf(":"));
	sMin = tempV.substring(eval(tempV.indexOf(":")+1) );
	tempV = eTime;
	eHour = tempV.substring(0, tempV.indexOf(":"));
	eMin = tempV.substring(eval(tempV.indexOf(":")+1) );
    var s_date=new Date(syyyy,smm,sdd,sHour,sMin,0);
    var e_date=new Date(eyyyy,emm,edd,eHour,eMin,0);

	var one_hour=1000*60*60;
	//alert(s_date.getTime());	//alert(e_date.getTime());
	b = Math.floor((e_date.getTime()-s_date.getTime())/one_hour);
	objHour.value = b;
	b = Math.ceil((e_date.getTime()-s_date.getTime())%one_hour);
	b = Math.ceil(b/(1000*60));
	objMin.value = b;
    return b;
}
function calToday(field) {
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = varDate + '/' + (varMonth) + '/' + calDate.getFullYear();
}
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"
	onLoad="setfocus()">
<!--
@oscar.formDB Table="formBCClientChartChecklist" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> /-->
	<!--input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
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
				onclick="javascript:return onExit();" /> <input type="button"
				value="Print" onclick="window.print();" /></td>
		</tr>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%" valign="top">

			<table class="headline" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
					<%=props.getProperty("c_clinicName","") %> <br>
					Client Chart Checklist</th>
					<input type="hidden" name="c_clinicName"
						value="<%=props.getProperty("c_clinicName","") %>" />
				</tr>
			</table>

			</td>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<!--r>
      <td width="55%" >DATE
		<img src="../images/cal.gif" id="pg1_formDate_cal">
          <%=bSync? ("<b><a href=# onClick='syncDemo(); return false;'><font color='red'>Synchronize</font></a></b>") :"" %>
      <br>
      <input type="text" name="pg1_formDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB dbType="date" />
      </td>
      <td >      </td>
    </tr-->
				<tr>
					<td>GIVEN NAME<br>
					<input type="text" name="c_givenName" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_givenName", "") %>"
						@oscar.formDB /></td>
					<td width="50%">SURNAME<br>
					<input type="text" name="c_surname" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_surname", "") %>"
						@oscar.formDB /></td>
				</tr>
				<!--tr>
      <td>ADDRESS<br>
      <input type="text" name="c_address" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("c_address", "") %>" @oscar.formDB />
      <input type="text" name="c_city" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("c_city", "") %>" @oscar.formDB />
      <input type="text" name="c_province" size="18" maxlength="50" value="<%= props.getProperty("c_province", "") %>" @oscar.formDB />
      <input type="text" name="c_postal" size="7" maxlength="8" value="<%= props.getProperty("c_postal", "") %>" @oscar.formDB />
	  </td>
	  <td valign="top">PHONE NUMBER<br>
      <input type="text" name="c_phone" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("c_phone", "") %>" @oscar.formDB />
		PHN<br>
      <input type="text" name="c_phn" style="width:100%" size="20" maxlength="20" value="<%= props.getProperty("c_phn", "") %>" @oscar.formDB />
	  </td>
    </tr-->
			</table>

			</td>
		</tr>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<th width="20%">Status</th>
			<th width="10%">Date</th>
			<th>Description</th>
			<th>Comments</th>
		</tr>
		<tr>
			<th colspan='4'>Consult/Hx/PE visits</th>
		</tr>
		<% 
String descCon[] = {
		"Contact #s/when to call", 
		"Letter to GP completed", 
		"Release of med info form", 
		"Practice booklet",
		"3 day diet history",
		"Intake form reviewed",
		"History to ANI",
		"Early U/S",
		"Physical exam",
		"Pap Discussed",
		"GC/CT Discussed",
		"HIV/AIDS",
		"HPV vaccination",
		"HSV",
		}; 
//int n0 = descCon.length;
String[] strStatus = {"Discussed", "Completed", "Not Applicable", "Late Into Care", "At Physician's Office", "Declined"};
String[] strStatus2 = {"Low", "Medium", "High", "Unknown"};
%>
		<% for(int i=0; i<descCon.length; i++) { %>
		<tr>
			<td><select name="statusCon<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("statusCon"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap><input type="text" id="dateCon<%=i %>"
				name="dateCon<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("dateCon"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="dateCon<%=i %>_cal"></td>
			<td nowrap><%=descCon[i] %></td>
			<td><input type="text" id="cCon<%=i %>" name="cCon<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("cCon"+i, "") %>" /></td>
		</tr>
		<% } %>
		<tr>
			<td colspan='4'>Initial Labs</td>
		</tr>
		<% 
String descConA[] = {
		"Type/screen",
		"Hematology Panel",
		"Urine culture ",
		"HbSAg ",
		"HIV",
		"Syph",
		"Rubella",
		"Rh Neg FOB blood gp",
		"<input type='text' name='descCon0' size='70' maxlength='70' value='" + props.getProperty("descCon0", "")+ "'",
		"<input type='text' name='descCon1' size='70' maxlength='70' value='" + props.getProperty("descCon1", "")+ "'",
		"<input type='text' name='descCon2' size='70' maxlength='70' value='" + props.getProperty("descCon2", "")+ "'",
		}; 
%>
		<% for(int i=0; i<descConA.length; i++) { %>
		<tr>
			<td><select name="statusConA<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("statusConA"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap><input type="text" id="dateConA<%=i %>"
				name="dateConA<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("dateConA"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="dateConA<%=i %>_cal"></td>
			<% if(!descConA[i].startsWith("<input")) { %>
			<td nowrap><%=descConA[i] %></td>
			<td><input type="text" id="cConA<%=i %>" name="cConA<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("cConA"+i, "") %>" /></td>
			<% } else { %>
			<td colspan='2'><%=descConA[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<th colspan='4'>1st Trimester</th>
		</tr>
		<!--  tr>
  <th width="20%">Status</th>
  <th width="10%">Date</th>
  <th>Description</th>
</tr-->
		<% 
String desc1st[] = {
		"Genetic counseling", 
		"MSS ordered (16 wk)", 
		"Amniocentesis", 
		"U/S ordered (16 wk) Gender <input type='checkbox' name='genderYes' " + props.getProperty("genderYes", "") 
		+ "/> yes <input type='checkbox' name='genderNo' " + props.getProperty("genderNo", "") + "/> no",
		"Teratogens",
		"Toxoplasmosis",
		"Lifestyle/exercise",
		"Nutritional review",
		"Complementary Therapies",
		"Doula info. Given",
		"Lab results",
		"Prenatal classes",
		"<input type='text' name='desc1st0' size='70' maxlength='70' value='" + props.getProperty("desc1st0", "")+ "'",
		"<input type='text' name='desc1st1' size='70' maxlength='70' value='" + props.getProperty("desc1st1", "")+ "'",
		}; 
%>
		<% for(int i=0; i<desc1st.length; i++) { %>
		<tr>
			<td width="20%"><select name="status1st<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status1st"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date1st<%=i %>"
				name="date1st<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date1st"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date1st<%=i %>_cal"></td>
			<% if(!desc1st[i].startsWith("<input")) { %>
			<td nowrap><%=desc1st[i] %></td>
			<td><input type="text" id="c1st<%=i %>" name="c1st<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c1st"+i, "") %>" /></td>
			<% } else { %>
			<td colspan='2'><%=desc1st[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<th colspan='4'>2nd trimester</th>
		</tr>
		<tr>
			<td colspan='4'>24 weeks</td>
		</tr>
		<% 
String desc2nd[] = {
		"Review dates (22wks)", 
		"Quickening", 
		"Gestational diabetes  (20-24 wks)", 
		"50 gr/ 2 hr pc glucose",
		"Repeat Hgb",
		"FHT fetoscope (20+ wk)",
		"Work issues (22-24 wk)",
		"Rh Neg Repeat screen (24-26 wks)",
		"Rh Neg WinRho ordered (24-26 wk)",
		"Home birth  pkg / copy of HBDP booklet",
		"Hosp pre-reg (29 wks)",
		"<input type='text' name='desc2nd0' size='70' maxlength='70' value='" + props.getProperty("desc2nd0", "")+ "'",
		"<input type='text' name='desc2nd1' size='70' maxlength='70' value='" + props.getProperty("desc2nd1", "")+ "'",
		}; 
%>
		<% for(int i=0; i<desc2nd.length; i++) { %>
		<tr>
			<td width="20%"><select name="status2nd<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status2nd"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date2nd<%=i %>"
				name="date2nd<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date2nd"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date2nd<%=i %>_cal"></td>
			<% if(!desc2nd[i].startsWith("<input")) { %>
			<td nowrap><%=desc2nd[i] %></td>
			<td><input type="text" id="c2nd<%=i %>" name="c2nd<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c2nd"+i, "") %>" /></td>
			<% } else {%>
			<td colspan='2'><%=desc2nd[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<th colspan='4'>3rd trimester</th>
		</tr>
		<tr>
			<td colspan='4'>28 weeks</td>
		</tr>
		<% 
String desc3rd1[] = {
		"home birth consent signed", 
		"S/Sx preterm labour", 
		"Birth Planning", 
		"Optimal Fetal Positioning)",
		"Nutrition review",
		"Kegels/perineal massage",
		"Sibling preparation",
		"Rh Neg  Win Rho given (28 wks)",
		"Iron/anemia information",
		"Hospital - registered",
		"Circumcision info",
		"Blood transfusion info",
		"<input type='text' name='desc3rd10' size='70' maxlength='70' value='" + props.getProperty("desc3rd10", "")+ "'",
		"<input type='text' name='desc3rd11' size='70' maxlength='70' value='" + props.getProperty("desc3rd11", "")+ "'",
		}; 
%>
		<% for(int i=0; i<desc3rd1.length; i++) { %>
		<tr>
			<td width="20%"><select name="status3rd1<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status3rd1"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd1<%=i %>"
				name="date3rd1<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd1"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd1<%=i %>_cal"></td>
			<% if(!desc3rd1[i].startsWith("<input")) { %>
			<td nowrap><%=desc3rd1[i] %></td>
			<td><input type="text" id="c3rd1<%=i %>" name="c3rd1<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c3rd1"+i, "") %>" /></td>
			<% }else{ %>
			<td colspan='2'><%=desc3rd1[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan='4'>32 - 35 weeks</td>
		</tr>
		<% 
String desc3rd2[] = {
		"Mother's cordial", 
		"GBS", 
		"Pain management", 
		"Complications - SRM",
		"Breastfeeding",
		"PP preparation / care",
		"Risk of PPD",
		"Home birth - birth papers in chart",
		"<input type='text' name='desc3rd20' size='70' maxlength='70' value='" + props.getProperty("desc3rd20", "")+ "'",
		"<input type='text' name='desc3rd21' size='70' maxlength='70' value='" + props.getProperty("desc3rd21", "")+ "'",
		}; 

%>
		<% for(int i=0; i<desc3rd2.length; i++) { %>
		<tr>
			<td width="20%"><select name="status3rd2<%=i %>">
				<option value=""></option>
				<% if(desc3rd2[i].startsWith("Risk of PPD")) {
      	for(int j=0; j<strStatus2.length; j++) { %>
				<option value="<%=strStatus2[j] %>"
					<%=props.getProperty("status3rd2"+i, "").equalsIgnoreCase(strStatus2[j])?"selected":""%>><%=strStatus2[j] %></option>
				<%} } else {
      	for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status3rd2"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } }%>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd2<%=i %>"
				name="date3rd2<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd2"+i, "")%>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd2<%=i %>_cal"></td>
			<% if(!desc3rd2[i].startsWith("<input")) { %>
			<td nowrap><%=desc3rd2[i] %></td>
			<td><input type="text" id="c3rd2<%=i %>" name="c3rd2<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c3rd2"+i, "") %>" /></td>
			<% }else { %>
			<td colspan='2'><%=desc3rd2[i] %></td>
			<% } %>
		</tr>
		<% } %>
		<tr>
			<td colspan='4'>Newborn procedures</td>
		</tr>
		<tr>
			<td width="20%"><select name="status3rd2a0">
				<option value=""
					<%=props.getProperty("status3rd2a0", "").equalsIgnoreCase("")?"selected":""%>></option>
				<option value="Discussed"
					<%=props.getProperty("status3rd2a0", "").equalsIgnoreCase("Discussed")?"selected":""%>>Discussed</option>
				<option value="IM"
					<%=props.getProperty("status3rd2a0", "").equalsIgnoreCase("IM")?"selected":""%>>IM</option>
				<option value="PO"
					<%=props.getProperty("status3rd2a0", "").equalsIgnoreCase("PO")?"selected":""%>>PO</option>
				<option value="Declined"
					<%=props.getProperty("status3rd2a0", "").equalsIgnoreCase("Declined")?"selected":""%>>Declined</option>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd2a0"
				name="date3rd2a0" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd2a0", "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd2a0_cal"></td>
			<td>&nbsp;&nbsp;Vit K</td>
			<td><input type="text" id="c3rd2a0" name="c3rd2a0" size="40"
				maxlength="40" value="<%= props.getProperty("c3rd2a0", "") %>" /></td>
		</tr>
		<tr>
			<td width="20%"><select name="status3rd2a1">
				<option value=""
					<%=props.getProperty("status3rd2a1", "").equalsIgnoreCase("")?"selected":""%>></option>
				<option value="Discussed"
					<%=props.getProperty("status3rd2a1", "").equalsIgnoreCase("Discussed")?"selected":""%>>Discussed</option>
				<option value="Yes"
					<%=props.getProperty("status3rd2a1", "").equalsIgnoreCase("Yes")?"selected":""%>>Yes</option>
				<option value="Delayed"
					<%=props.getProperty("status3rd2a1", "").equalsIgnoreCase("Delayed")?"selected":""%>>Delayed</option>
				<option value="Declined"
					<%=props.getProperty("status3rd2a1", "").equalsIgnoreCase("Declined")?"selected":""%>>Declined</option>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd2a1"
				name="date3rd2a1" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd2a1", "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd2a1_cal"></td>
			<td>&nbsp;&nbsp;Eye prophylaxis</td>
			<td><input type="text" id="c3rd2a1" name="c3rd2a1" size="40"
				maxlength="40" value="<%= props.getProperty("c3rd2a1", "") %>" /></td>
		</tr>
		<tr>
			<td width="20%"><select name="status3rd2a2">
				<option value=""
					<%=props.getProperty("status3rd2a2", "").equalsIgnoreCase("")?"selected":""%>></option>
				<option value="Discussed"
					<%=props.getProperty("status3rd2a2", "").equalsIgnoreCase("Discussed")?"selected":""%>>Discussed</option>
				<option value="Yes"
					<%=props.getProperty("status3rd2a2", "").equalsIgnoreCase("Yes")?"selected":""%>>Yes</option>
				<option value="Declined"
					<%=props.getProperty("status3rd2a2", "").equalsIgnoreCase("Declined")?"selected":""%>>Declined</option>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd2a2"
				name="date3rd2a2" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd2a2", "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd2a2_cal"></td>
			<td>&nbsp;&nbsp;NB Screen</td>
			<td><input type="text" id="c3rd2a2" name="c3rd2a2" size="40"
				maxlength="40" value="<%= props.getProperty("c3rd2a2", "") %>" /></td>
		</tr>
		<tr>
			<td width="20%"><select name="status3rd2a3">
				<option value=""
					<%=props.getProperty("status3rd2a3", "").equalsIgnoreCase("")?"selected":""%>></option>
				<option value="Discussed"
					<%=props.getProperty("status3rd2a3", "").equalsIgnoreCase("Discussed")?"selected":""%>>Discussed</option>
				<option value="Yes"
					<%=props.getProperty("status3rd2a3", "").equalsIgnoreCase("Yes")?"selected":""%>>Yes</option>
				<option value="Declined"
					<%=props.getProperty("status3rd2a3", "").equalsIgnoreCase("Declined")?"selected":""%>>Declined</option>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd2a3"
				name="date3rd2a3" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd2a3", "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd2a3_cal"></td>
			<td>&nbsp;&nbsp;Circumcision</td>
			<td><input type="text" id="c3rd2a3" name="c3rd2a3" size="40"
				maxlength="40" value="<%= props.getProperty("c3rd2a3", "") %>" /></td>
		</tr>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan='4'>36 weeks</td>
		</tr>
		<% 
String desc3rd3[] = {
		"Active mgt 3rd stage",
		"Home birth - home visit", 
		"Home birth - supplies checked", 
		"Birth preferences review", 
		"NB procedures I/C signed",
		"When to call in labour",
		"Chart reviewed / faxed",
		"Review Emergency birth",
		"<input type='text' name='desc3rd30' size='70' maxlength='70' value='" + props.getProperty("desc3rd30", "")+ "'",
		"<input type='text' name='desc3rd31' size='70' maxlength='70' value='" + props.getProperty("desc3rd31", "")+ "'",
		}; 

%>
		<% for(int i=0; i<desc3rd3.length; i++) { %>
		<tr>
			<td width="20%"><select name="status3rd3<%=i %>">
				<% if(i==0) { %>
				<option value=""
					<%=props.getProperty("status3rd30", "").equalsIgnoreCase("")?"selected":""%>></option>
				<option value="Discussed"
					<%=props.getProperty("status3rd30", "").equalsIgnoreCase("Discussed")?"selected":""%>>Discussed</option>
				<option value="Yes"
					<%=props.getProperty("status3rd30", "").equalsIgnoreCase("Yes")?"selected":""%>>Yes</option>
				<option value="Declined"
					<%=props.getProperty("status3rd30", "").equalsIgnoreCase("Declined")?"selected":""%>>Declined</option>
				<% } else { %>
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status3rd3"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd3<%=i %>"
				name="date3rd3<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd3"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd3<%=i %>_cal"></td>
			<% if(!desc3rd3[i].startsWith("<input")) { %>
			<td nowrap><%=desc3rd3[i] %></td>
			<td><input type="text" id="c3rd3<%=i %>" name="c3rd3<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c3rd3"+i, "") %>" /></td>
			<% }else{ %>
			<td colspan='2'><%=desc3rd3[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan='4'>38 - 40 weeks</td>
		</tr>
		<% 
String desc3rd4[] = {
		"Postdates discussion", 
		"<input type='text' name='desc3rd40' size='70' maxlength='70' value='" + props.getProperty("desc3rd40", "")+ "'",
		"<input type='text' name='desc3rd41' size='70' maxlength='70' value='" + props.getProperty("desc3rd41", "")+ "'",
		}; 

%>
		<% for(int i=0; i<desc3rd4.length; i++) { %>
		<tr>
			<td width="20%"><select name="status3rd4<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status3rd4"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd4<%=i %>"
				name="date3rd4<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd4"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd4<%=i %>_cal"></td>
			<% if(!desc3rd4[i].startsWith("<input")) { %>
			<td nowrap><%=desc3rd4[i] %></td>
			<td><input type="text" id="c3rd4<%=i %>" name="c3rd4<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c3rd4"+i, "") %>" /></td>
			<% } else {%>
			<td colspan='2'><%=desc3rd4[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan='4'>41+ weeks</td>
		</tr>
		<% 
String desc3rd5[] = {
		"Sweeps", 
		"FM counts", 
		"NST scheduled", 
		"AFI / U/S scheduled", 
		"Induction", 
		"<input type='text' name='desc3rd50' size='70' maxlength='70' value='" + props.getProperty("desc3rd50", "")+ "'",
		"<input type='text' name='desc3rd51' size='70' maxlength='70' value='" + props.getProperty("desc3rd51", "")+ "'",
		}; 
%>
		<% for(int i=0; i<desc3rd5.length; i++) { %>
		<tr>
			<td width="20%"><select name="status3rd5<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("status3rd5"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="date3rd5<%=i %>"
				name="date3rd5<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("date3rd5"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="date3rd5<%=i %>_cal"></td>
			<% if(!desc3rd5[i].startsWith("<input")) { %>
			<td nowrap><%=desc3rd5[i] %></td>
			<td><input type="text" id="c3rd5<%=i %>" name="c3rd5<%=i %>"
				size="40" maxlength="40"
				value="<%= props.getProperty("c3rd5"+i, "") %>" /></td>
			<% } else {%>
			<td colspan='2'><%=desc3rd5[i] %></td>
			<% } %>
		</tr>
		<% } %>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<th colspan='4'>Other</th>
		</tr>
		<% 
String descOther[] = {
		"", 
		"", 
		"", 
		"", 
		"", 
		"", 
		""}; 

%>
		<% for(int i=0; i<descOther.length; i++) { %>
		<tr>
			<td width="20%"><select name="statusOther<%=i %>">
				<option value=""></option>
				<% for(int j=0; j<strStatus.length; j++) { %>
				<option value="<%=strStatus[j] %>"
					<%=props.getProperty("statusOther"+i, "").equalsIgnoreCase(strStatus[j])?"selected":""%>><%=strStatus[j] %></option>
				<% } %>
			</select></td>
			<td nowrap width="10%"><input type="text" id="dateOther<%=i %>"
				name="dateOther<%=i %>" size="10" maxlength="10"
				value="<%= props.getProperty("dateOther"+i, "") %>"
				onDblClick="calToday(this)" @oscar.formDB dbType="date" /> <img
				src="../images/cal.gif" id="dateOther<%=i %>_cal"></td>
			<td><input type="text" name="descOther<%=i %>" size="50"
				maxlength="70" value="<%= props.getProperty("descOther"+i, "") %>" />
			</td>
		</tr>
		<% } %>
	</table>

	<!--input type="checkbox" name="singleton" 
      <%--= props.getProperty("singleton", "") --%>  @oscar.formDB dbType="tinyint(1)"/> Singleton-->

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
				onclick="javascript:return onExit();" /> <input type="button"
				value="Print" onclick="window.print();" /></td>
		</tr>
	</table>

</html:form>

<script type="text/javascript">
<% for(int i=0; i<14; i++) { %>
Calendar.setup({ inputField : "dateCon<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "dateCon<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<11; i++) { %>
Calendar.setup({ inputField : "dateConA<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "dateConA<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<14; i++) { %>
Calendar.setup({ inputField : "date1st<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date1st<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<13; i++) { %>
Calendar.setup({ inputField : "date2nd<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date2nd<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<14; i++) { %>
Calendar.setup({ inputField : "date3rd1<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date3rd1<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<10; i++) { %>
Calendar.setup({ inputField : "date3rd2<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date3rd2<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<4; i++) { %>
Calendar.setup({ inputField : "date3rd2a<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date3rd2a<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<10; i++) { %>
Calendar.setup({ inputField : "date3rd3<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date3rd3<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<3; i++) { %>
Calendar.setup({ inputField : "date3rd4<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date3rd4<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<7; i++) { %>
Calendar.setup({ inputField : "date3rd5<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date3rd5<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
<% for(int i=0; i<7; i++) { %>
Calendar.setup({ inputField : "dateOther<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "dateOther<%=i%>_cal", singleClick : true, step : 1 });
<% } %>
</script>
</body>
</html:html>
