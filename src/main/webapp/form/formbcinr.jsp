<%@ page
	import="oscar.form.*, oscar.form.data.*, java.util.*, oscar.util.*"%>
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
	String formClass = "BCINR";
	String formLink = "formbcinr.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
    FrmData fd = new FrmData();
    String lastLabDate = ((FrmBCINRRecord)rec).getLastLabDate(demoNo, formId);

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
	        && props.getProperty("c_phone1_cur", "").trim().equals(props.getProperty("c_phone1", "").trim())
	        && props.getProperty("c_phone2_cur", "").trim().equals(props.getProperty("c_phone2", "").trim())
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
<title>INR</title>
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
<html:base />
</head>

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
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Labour+and+Birth+Summary+Record&__cfgfile=bclbPrintCfgPg1&__template=bcbirthsummary";
            document.forms[0].target="_blank";            
        }
        return ret;
    }


function onCheck(a, groupName) {
    if (a.checked) {
		var s = groupName;
		unCheck(s);
		a.checked = true;
    }
}
function unCheck(s) {
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name.indexOf(s) != -1 && document.forms[0].elements[i].name.indexOf(s) < 1) {
            document.forms[0].elements[i].checked = false;
    	}
	}
}
function isChecked(s) {
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name == s) {
            if (document.forms[0].elements[i].checked) {
				return true;
			} else {
				return false;
			}
    	}
	}
}

function onCheckMaster(a, groupName) {
    if (!a.checked) {
		var s = groupName;
		unCheck(s);
		//a.checked = false;
    }
}
function onCheckSlave(a, masterName) {
    if (a.checked) {
		if (!isChecked(masterName) ) {
		  a.checked = false;
		} else {
		  a.checked = true;
		}
    }
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
        var popup = window.open(varpage, "inr2", windowprops);
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
    document.forms[0].c_phone1.value = "<%=props.getProperty("c_phone1_cur", "")%>";
    document.forms[0].c_phone2.value = "<%=props.getProperty("c_phone2_cur", "")%>";
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
@oscar.formDB Table="formBCINR" 
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
				value="Print" onclick="javascript:window.print();return false()" />
			</td>
		</tr>
	</table>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%" valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th><font size="+2">INR RECORD</font></th>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="1">
				<tr>
					<td>Given Name: <input type="text" name="c_givenName"
						size="12" maxlength="30"
						value="<%= props.getProperty("c_givenName", "") %>" @oscar.formDB />
					Surname: <input type="text" name="c_surname" size="12"
						maxlength="30" value="<%= props.getProperty("c_surname", "") %>"
						@oscar.formDB /> <br />
					Phone #1: <input type="text" name="c_phone1" size="20"
						maxlength="30" value="<%= props.getProperty("c_phone1", "") %>"
						@oscar.formDB /> <br />
					Phone #2: <input type="text" name="c_phone2" size="20"
						maxlength="30" value="<%= props.getProperty("c_phone2", "") %>"
						@oscar.formDB /> <br />
					Phone #3: <input type="text" name="c_phone3" size="20"
						maxlength="30" value="<%= props.getProperty("c_phone3", "") %>"
						@oscar.formDB /> <br />
					<b><font size="+1">Indication:</font></b> <input type="text"
						name="indication" size="20" maxlength="30"
						value="<%= props.getProperty("indication", "") %>" @oscar.formDB />
					<br />
					Target INR Range:<br>
					<input type="checkbox" name="targetINR1"
						<%= props.getProperty("targetINR1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> 2.0-3.0 &nbsp;&nbsp; <input type="checkbox"
						name="targetINR2"
						<%= props.getProperty("targetINR2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> 2.5-3.5 &nbsp;&nbsp; <input type="checkbox"
						name="targetINR3"
						<%= props.getProperty("targetINR3", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> Other <input type="text"
						name="targetINROther" size="6" maxlength="30"
						value="<%= props.getProperty("targetINROther", "") %>"
						@oscar.formDB /> <br />
					Duration: <input type="checkbox" name="duration1"
						<%= props.getProperty("duration1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> 3 mos &nbsp;&nbsp; <input type="checkbox"
						name="duration2"
						<%= props.getProperty("duration2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> lifelong &nbsp;&nbsp; <input
						type="checkbox" name="duration3"
						<%= props.getProperty("duration3", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> Other <input type="text"
						name="durationOther" size="10" maxlength="30"
						value="<%= props.getProperty("durationOther", "") %>"
						@oscar.formDB /> <br />
					Date Started: <input type="text" name="dateStart" id="dateStart"
						size="10" maxlength="10"
						value="<%= props.getProperty("dateStart", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="dateStart_cal">
					<br />
					Coumadin Strength on Hand: <input type="text" name="coumadin"
						size="20" maxlength="20"
						value="<%= props.getProperty("coumadin", "") %>" @oscar.formDB />
					<br />
					Other Anticoagulant: <input type="text" name="anticoagulant"
						size="20" maxlength="20"
						value="<%= props.getProperty("anticoagulant", "") %>"
						@oscar.formDB /> <br />
					Tablet Strengths: <br />
					1-pink &nbsp; 2-lavender &nbsp; 2.5-green &nbsp; 3-tan &nbsp;
					4-blue &nbsp; <br />
					5-peach &nbsp; 6-teal &nbsp; 7-yellow &nbsp; 10-white</td>
				</tr>
			</table>

			</td>
			<td valign="top">


			<table width="100%" border="1" cellspacing="0" cellpadding="2">
				<tr>
					<th valign="top" colspan="2">Dosage Adjustments for Warfarin
					Maintenance Target INR 2.0-3.0</th>
				</tr>
				<tr>
					<th width="20%">INR</th>
					<th>Dosage Adjustment</th>
				</tr>
				<tr>
					<td>&nbsp; &lt; 1.5</td>
					<td>Increase weekly dose by 20% and give one time top-up
					additional amount equal to 20% of weekly dose</td>
				</tr>
				<tr>
					<td>&nbsp; 1.5 - 1.9</td>
					<td>Increase weekly dose by 10%</td>
				</tr>
				<tr>
					<td>&nbsp; 2.0 - 3.0</td>
					<td>No change</td>
				</tr>
				</tr>
				<tr>
					<td>&nbsp; 3.1 - 3.9</td>
					<td>No change - recheck in one week.<br />
					If persistent, decrease weekly dose by 10-20%</td>
				</tr>
				</tr>
				<tr>
					<td>&nbsp; 4.0 - 5.0</td>
					<td>Omit 1 dose; decrease weekly dose by 10-20% and recheck in
					2-5 days</td>
				</tr>
				</tr>
				<tr>
					<td>&nbsp; &gt; 5.0</td>
					<td>See <br />
					<a href=#
						onclick='popupPage("http://www.healthservices.gov.bc.ca/msp/protoguides/gps/overanticoag.pdf"); return false;'>
					http://www.healthservices.gov.bc.ca/msp/<br />
					protoguides/gps/overanticoag.pdf</a></td>
				</tr>
				<tr>
					<td colspan="2">Note: * Changes in warfarin dosage may take
					several days to affect INR. Hence, frequent dosage adjustment is
					not recommended.</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

	<center>
	<table width="80%" border="1" cellspacing="0" cellpadding="1">
		<tr>
			<th>Date</th>
			<th>INR</th>
			<th>Was On</th>
			<th>Change To</th>
			<th>Repeat</th>
			<th>Notified</th>
			<th>Bill</th>
		</tr>

		<%
  Vector vecR = (new FrmBCINRRecord()).getINRLabData(demoNo);
  // get rid of the old lab data
  for(int i = 0; i < vecR.size(); i=i+2) {
	  String tempDate = (String) vecR.get(i);
	  Date dateVec = UtilDateUtilities.StringToDate(tempDate, "dd/MM/yyyy");
	  Date dateStart = UtilDateUtilities.StringToDate(lastLabDate, "dd/MM/yyyy");
	  if(dateVec.compareTo(dateStart) <= 0) {
		  vecR.remove(i);
		  vecR.remove(i);
	  }
  }
  
  for(int i=1; i<21; i++) {
	  String bgcolor = "";
	  String labDate = props.getProperty("date"+i, "");
	  if(labDate.length()==10) {
		  if(vecR.contains(labDate)) {
			  int nv = vecR.indexOf(labDate);
			  vecR.remove(nv);
			  vecR.remove(nv);
		  }
	  }else {
		  if(vecR.size()>0 && formId!=0) {
			  bgcolor = "bgcolor='pink'";
			  props.setProperty("date"+i, (String)vecR.get(0));
			  props.setProperty("inr"+i, (String)vecR.get(1));
			  vecR.remove(0);
			  vecR.remove(0);
		  }
	  }
  %>
		<tr align="center" <%=bgcolor %>>
			<td><input type="text" name="date<%=i %>" id="date<%=i %>"
				class="spe" onDblClick="calToday(this)" size="10" maxlength="10"
				value="<%= props.getProperty("date"+i, "") %>"
				readonly @oscar.formDB dbType="date" /> <img src="../images/cal.gif"
				id="date<%=i %>_cal"></td>
			<td><input type="text" name="inr<%=i %>" style="width: 100%"
				size="6" maxlength="6" value="<%= props.getProperty("inr"+i, "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="wason<%=i %>" style="width: 100%"
				size="10" maxlength="20"
				value="<%= props.getProperty("wason"+i, "") %>" @oscar.formDB /></td>
			<td><input type="text" name="changeto<%=i %>"
				style="width: 100%" size="10" maxlength="20"
				value="<%= props.getProperty("changeto"+i, "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="repeat<%=i %>" id="repeat<%=i %>"
				size="10" maxlength="10"
				value="<%= props.getProperty("repeat"+i, "") %>" @oscar.formDB
				dbType="date" /> <img src="../images/cal.gif" id="repeat<%=i %>_cal">
			</td>
			<td><input type="checkbox" name="notified<%=i %>"
				<%= props.getProperty("notified"+i, "") %>  @oscar.formDB
				dbType="tinyint(1)" /></td>
			<td><input type="text" name="bill<%=i %>" style="width: 100%"
				size="10" maxlength="20"
				value="<%= props.getProperty("bill"+i, "") %>" @oscar.formDB /></td>
		</tr>
		<% } %>

	</table>
	</center>

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
				onclick="javascript:return onExit();" /> <!-- input type="submit" value="Print" onclick="javascript:return onPrint();return false()"/ -->
			<input type="button" value="Print"
				onclick="javascript:window.print();return false()" /></td>
		</tr>
	</table>

</html:form>
<br>
<br>
<br>
<br>
<br>
<br>
<script type="text/javascript">
Calendar.setup({ inputField : "dateStart", ifFormat : "%d/%m/%Y", showsTime :false, button : "dateStart_cal", singleClick : true, step : 1 });
<%  for(int i=1; i<21; i++) { %>
Calendar.setup({ inputField : "date<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "date<%=i %>_cal", singleClick : true, step : 1 });
<% } %>
<%  for(int i=1; i<21; i++) { %>
Calendar.setup({ inputField : "repeat<%=i %>", ifFormat : "%d/%m/%Y", showsTime :false, button : "repeat<%=i %>_cal", singleClick : true, step : 1 });
<% } %>
</script>
</body>
</html:html>
