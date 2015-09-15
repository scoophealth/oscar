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

<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	String formClass = "BCBrithSumMo";
	String formLink = "formbcbirthsummo.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
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
<title>Birth Summary (Mother:)</title>
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
    
function calc1() {
	sDate = document.forms[0].birTimeDate2.value;
	eDate = document.forms[0].birTimeDate3.value;
	
	sTime = document.forms[0].birTimeHour2.value;
	eTime = document.forms[0].birTimeHour3.value;
	objHour = document.forms[0].birDurHour1;
	objMin = document.forms[0].birDurMin1;
	//alert(sHour + sMin + " " + eHour + eMin);
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calc2() {
	sDate = document.forms[0].birTimeDate3.value;
	eDate = document.forms[0].birTimeDate4.value;
	sTime = document.forms[0].birTimeHour3.value;
	eTime = document.forms[0].birTimeHour4.value;
	objHour = document.forms[0].birDurHour2;
	objMin = document.forms[0].birDurMin2;
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calc3() {
	sDate = document.forms[0].birTimeDate4.value;
	eDate = document.forms[0].birTimeDate5.value;
	sTime = document.forms[0].birTimeHour4.value;
	eTime = document.forms[0].birTimeHour5.value;
	objHour = document.forms[0].birDurHour3;
	objMin = document.forms[0].birDurMin3;
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calcRup() {
	sDate = document.forms[0].birTimeDate1.value;
	eDate = document.forms[0].birTimeDate4.value;
	sTime = document.forms[0].birTimeHour1.value;
	eTime = document.forms[0].birTimeHour4.value;
	objHour = document.forms[0].birDurRupHour;
	objMin = document.forms[0].birDurRupMin;
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
	document.forms[0].birDurRupHour.value = document.forms[0].birDurRupHour.value + ":" + objMin.value;
}
function calcLOS() {
	tempV = document.forms[0].admisDateTime.value;
	sDate = tempV.length>10? tempV.substring(0,10) : "";
	sTime = tempV.length>10? tempV.substring(11) : "";
	tempV = document.forms[0].dischargeDateTime.value;
	eDate = tempV.length>10? tempV.substring(0,10) : "";
	eTime = tempV.length>10? tempV.substring(11) : "";
	objHour = document.forms[0].conLos;
	objMin = "";
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calcPPLOS() {
	sDate = document.forms[0].birTimeDate5.value;
	sTime = document.forms[0].birTimeHour5.value;
	tempV = document.forms[0].dischargeDateTime.value;
	eDate = tempV.length>10? tempV.substring(0,10) : "";
	eTime = tempV.length>10? tempV.substring(11) : "";
	objHour = document.forms[0].ppLos;
	objMin = "";
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
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
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"
	onLoad="setfocus()">
<!--
@oscar.formDB Table="formBCBirthSumMo" 
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
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();return false;" />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%" valign="top">

			<table class="headline" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
					British Columbia Labour and Birth Summary Record</th>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th align="left">1. IDENTIFICATION</th>
				</tr>
				<tr>
					<td>

					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<td width="40%" rowspan="2" valign="top">NEWBORN ID NUMBER<br>
							<input type="text" name="c_newBornID" style="width: 100%"
								size="30" maxlength="30"
								value="<%= props.getProperty("c_newBornID", "") %>"
								@oscar.formDB /></td>
							<td width="20%" valign="top"><input type="checkbox"
								name="singleton"
								<%= props.getProperty("singleton", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Singleton</td>
							<td valign="top"><input type="checkbox" name="twinA"
								<%= props.getProperty("twinA", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Twin A <br>
							<input type="checkbox" name="twinB"
								<%= props.getProperty("twinB", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Twin B</td>
							<td><input type="checkbox" name="tripletA"
								<%= props.getProperty("tripletA", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Triplet A <br>
							<input type="checkbox" name="tripletB"
								<%= props.getProperty("tripletB", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Triplet B <br>
							<input type="checkbox" name="tripletC"
								<%= props.getProperty("tripletC", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Triplet C</td>
						</tr>
						<tr>
							<td colspan="3">Gest. Age: <input type="text" name="gestAge"
								size="5" maxlength="5"
								value="<%= props.getProperty("gestAge", "") %>" @oscar.formDB />
							wks. <input type="text" name="gestWks" size="5" maxlength="5"
								value="<%= props.getProperty("gestWks", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td><I>(status prior to this delivery as on Antenatal
					Record, Part 1)</I><br>
					<br>
					<b>G</b><span class="small8">ravida</span> <input type="text"
						name="pg1_gravida" size="4" maxlength="4"
						value="<%= props.getProperty("pg1_gravida", "") %>" @oscar.formDB />
					<b>T</b><span class="small8">erm</span> <input type="text"
						name="pg1_term" size="3" maxlength="4"
						value="<%= props.getProperty("pg1_term", "") %>" @oscar.formDB />
					<b>P</b><span class="small8">reterm</span> <input type="text"
						name="pg1_preterm" size="3" maxlength="4"
						value="<%= props.getProperty("pg1_preterm", "") %>" @oscar.formDB />
					<b>A</b><span class="small8">bortion</span> <input type="text"
						name="pg1_abortion" size="3" maxlength="3"
						value="<%= props.getProperty("pg1_abortion", "") %>" @oscar.formDB />
					<b>L</b><span class="small8">iving</span> <input type="text"
						name="pg1_living" size="3" maxlength="3"
						value="<%= props.getProperty("pg1_living", "") %>" @oscar.formDB />

					</td>
				</tr>
			</table>

			</td>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="55%">DATE <img src="../images/cal.gif"
						id="pg1_formDate_cal"> <%=bSync? ("<b><a href=# onClick='syncDemo(); return false;'><font color='red'>Synchronize</font></a></b>") :"" %>
					<br>
					<input type="text" name="pg1_formDate" id="pg1_formDate"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB
						dbType="date" /></td>
					<td>MOTHER'S I.D. NUMBER<br>
					<input type="text" name="pg1_motherID" style="width: 100%"
						size="15" maxlength="20"
						value="<%= props.getProperty("pg1_motherID", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td>SURNAME<br>
					<input type="text" name="c_surname" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_surname", "") %>"
						@oscar.formDB /></td>
					<td>GIVEN NAME<br>
					<input type="text" name="c_givenName" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_givenName", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>ADDRESS<br>
					<input type="text" name="c_address" style="width: 100%" size="50"
						maxlength="60" value="<%= props.getProperty("c_address", "") %>"
						@oscar.formDB /> <input type="text" name="c_city"
						style="width: 100%" size="50" maxlength="60"
						value="<%= props.getProperty("c_city", "") %>" @oscar.formDB /> <input
						type="text" name="c_province" size="18" maxlength="50"
						value="<%= props.getProperty("c_province", "") %>" @oscar.formDB />
					<input type="text" name="c_postal" size="7" maxlength="8"
						value="<%= props.getProperty("c_postal", "") %>" @oscar.formDB />
					</td>
					<td valign="top">PHONE NUMBER<br>
					<input type="text" name="c_phone" style="width: 100%" size="60"
						maxlength="60" value="<%= props.getProperty("c_phone", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>PERSONAL HEALTH NUMBER<br>
					<input type="text" name="c_phn" style="width: 100%" size="20"
						maxlength="20" value="<%= props.getProperty("c_phn", "") %>"
						@oscar.formDB /></td>
					<td><span class="small9"> <a href=#
						onClick="popupFixedPage(600, 300, 'formbcarpg1namepopup.jsp?fieldname=c_phyMid'); return false;">PHYSICIAN
					/ MIDWIFE NAME</a></span><br>
					<input type="text" name="c_phyMid" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th align="left">2. LABOUR</th>
		</tr>
		<tr>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="45%" nowrap><span class="small9"> <input
						type="checkbox" name="labSpontaneous1"
						<%= props.getProperty("labSpontaneous1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> SPONTANEOUS &nbsp;&nbsp; <input
						type="checkbox" name="labAugmented1"
						<%= props.getProperty("labAugmented1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> AUGMENTED: &nbsp;&nbsp; <input
						type="checkbox" name="labArm1"
						<%= props.getProperty("labArm1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> ARM &nbsp;&nbsp; <input type="checkbox"
						name="labOxytocin1"
						<%= props.getProperty("labOxytocin1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OXYTOCIN &nbsp;&nbsp; </span></td>
					<td nowrap><span class="small9"> <input type="checkbox"
						name="labOtherChk1"
						<%= props.getProperty("labOtherChk1", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER: </span></td>
					<td width="10%"><input type="text" name="labOther1"
						style="width: 100%" size="15" maxlength="60"
						value="<%= props.getProperty("labOther1", "") %>" @oscar.formDB />
					</td>
					<td nowrap><span class="small9"> &nbsp;INDICATION </td>
					<td width="30%"><input type="text" name="labIndication1"
						style="width: 100%" size="50" maxlength="80"
						value="<%= props.getProperty("labIndication1", "") %>"
						@oscar.formDB /> </span></td>
				</tr>
				<tr>
					<td nowrap><span class="small8"> <input type="checkbox"
						name="labInduced2"
						<%= props.getProperty("labInduced2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> INDUCED &nbsp;&nbsp; <input type="checkbox"
						name="labArm2"
						<%= props.getProperty("labArm2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> ARM: &nbsp;&nbsp; <input type="checkbox"
						name="labFoley2"
						<%= props.getProperty("labFoley2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FOLEY &nbsp;&nbsp; <input type="checkbox"
						name="labProstaglandin2"
						<%= props.getProperty("labProstaglandin2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> PROSTAGLANDIN &nbsp;&nbsp; <input
						type="checkbox" name="labOxytocin2"
						<%= props.getProperty("labOxytocin2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OXYTOCIN &nbsp;&nbsp; </span></td>
					<td nowrap><input type="checkbox" name="labOtherChk2"
						<%= props.getProperty("labOtherChk2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> <span class="small9"> OTHER: </span></td>
					<td><input type="text" name="labOther2" style="width: 100%"
						size="15" maxlength="60"
						value="<%= props.getProperty("labOther2", "") %>" @oscar.formDB />
					</td>
					<td><span class="small9"> &nbsp;INDICATION </span></td>
					<td><input type="text" name="labIndication2"
						style="width: 100%" size="50" maxlength="80"
						value="<%= props.getProperty("labIndication2", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><span class="small9"> <B>MEMBRANE RUPTURE:</B> <input
						type="checkbox" name="labMemRupSpont"
						<%= props.getProperty("labMemRupSpont", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> SPONTANEOUS &nbsp;&nbsp; <input
						type="checkbox" name="labMemRupObv"
						<%= props.getProperty("labMemRupObv", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OBVIOUS &nbsp;&nbsp; <input type="checkbox"
						name="labMemRupQuer"
						<%= props.getProperty("labMemRupQuer", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> QUERIED &nbsp;&nbsp; <input type="checkbox"
						name="labMemRupConf"
						<%= props.getProperty("labMemRupConf", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> CONFIRMED &nbsp;&nbsp; </span></td>
				</tr>
				<tr>
					<td><span class="small9"> <B>AMNIOTIC FLUID:</B> <input
						type="checkbox" name="labAmnFluClear"
						<%= props.getProperty("labAmnFluClear", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> CLEAR &nbsp;&nbsp; <input type="checkbox"
						name="labAmnFluBloody"
						<%= props.getProperty("labAmnFluBloody", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> BLOODY &nbsp;&nbsp; <input type="checkbox"
						name="labAmnFluMeco"
						<%= props.getProperty("labAmnFluMeco", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> MECONIUM &nbsp;&nbsp; TIME MECONIUM NOTED <input
						type="text" name="labAmnFluTimeMeco" size="10" maxlength="10"
						value="<%= props.getProperty("labAmnFluTimeMeco", "") %>"
						@oscar.formDB /> HRS. </span></td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td><span class="small9"> <B>FETAL SURVEILLANCE:</B> <input
						type="checkbox" name="labInterAusc"
						<%= props.getProperty("labInterAusc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> INTERMITTENT AUSCULTATION &nbsp;&nbsp; <input
						type="checkbox" name="labExtEfm"
						<%= props.getProperty("labExtEfm", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> EXTERNAL EFM &nbsp;&nbsp; <input
						type="checkbox" name="labFetalEcg"
						<%= props.getProperty("labFetalEcg", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FETAL ECG &nbsp;&nbsp; <input
						type="checkbox" name="labIupc"
						<%= props.getProperty("labIupc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> IUPC <br>
					<B>FETAL BLOOD SAMPLING:</B> NO. OF TIMES <input type="text"
						name="labFetalBldNo" size="5" maxlength="5"
						value="<%= props.getProperty("labFetalBldNo", "") %>"
						@oscar.formDB /> LOWEST: pH <input type="text"
						name="labFetalBldPh" size="10" maxlength="10"
						value="<%= props.getProperty("labFetalBldPh", "") %>"
						@oscar.formDB /> B.E. <input type="text" name="labFetalBldBe"
						size="10" maxlength="10"
						value="<%= props.getProperty("labFetalBldBe", "") %>"
						@oscar.formDB /> </span></td>
					<td width="30%"><B>INTRAPARTUM ANTIBIOTICS:</B><br>
					<input type="checkbox" name="labIntAntibioN"
						<%= props.getProperty("labIntAntibioN", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> NO &nbsp;&nbsp; <input type="checkbox"
						name="labIntAntibioY"
						<%= props.getProperty("labIntAntibioY", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> YES <I>(specify)</I>: <input type="text"
						name="labIntAntibio" size="30" maxlength="60"
						value="<%= props.getProperty("labIntAntibio", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>


			</td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="45%" valign="top">

			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="4"><B>
					<div class="small10">3. DELIVERY</div>
					</B></td>
				</tr>
				<tr>
					<td colspan="4" nowrap><input type="checkbox" name="delSvd"
						<%= props.getProperty("delSvd", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> SVD &nbsp;FETAL POSITION IN LABOUR: <input
						type="text" name="delFetalPosLab" size="30" maxlength="60"
						value="<%= props.getProperty("delFetalPosLab", "") %>"
						@oscar.formDB /> <br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FETAL POSITION AT
					DELIVERY:&nbsp;&nbsp;&nbsp; <input type="text"
						name="delFetalPosDel" size="30" maxlength="60"
						value="<%= props.getProperty("delFetalPosDel", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="delAssiDel"
						<%= props.getProperty("delAssiDel", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> ASSISTED DELIVERY:</td>
					<td valign="top"><input type="checkbox" name="delVacuum"
						<%= props.getProperty("delVacuum", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> VACUUM <br>
					<input type="checkbox" name="delForceps"
						<%= props.getProperty("delForceps", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FORCEPS</td>
					<td valign="top"><input type="checkbox" name="delOutlet"
						<%= props.getProperty("delOutlet", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OUTLET <br>
					<input type="checkbox" name="delLow"
						<%= props.getProperty("delLow", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> LOW <br>
					<input type="checkbox" name="delMid"
						<%= props.getProperty("delMid", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> MID</td>
					<td valign="top"><input type="checkbox" name="delEasy"
						<%= props.getProperty("delEasy", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> EASY <br>
					<input type="checkbox" name="delModDiff"
						<%= props.getProperty("delModDiff", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> MOD. DIFFICULT <br>
					<input type="checkbox" name="delDiff"
						<%= props.getProperty("delDiff", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> DIFFICULT</td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td colspan="2"><input type="checkbox" name="delForcRot"
						<%= props.getProperty("delForcRot", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FORCEPS ROTATION</td>
				</tr>
				<tr>
					<td><input type="checkbox" name="delVBACAttep"
						<%= props.getProperty("delVBACAttep", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> VBAC ATTEMPTED</td>
					<td colspan="2"><input type="checkbox" name="delVBACDecl"
						<%= props.getProperty("delVBACDecl", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> VBAC DECLINED</td>
					<td><input type="checkbox" name="delNotCandi"
						<%= props.getProperty("delNotCandi", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> NOT A CANDIDATE</td>
				</tr>
				<tr>
					<td colspan="2"><input type="checkbox" name="delCesSect"
						<%= props.getProperty("delCesSect", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> CESARIAN SECTION # <input type="text"
						name="delCesSectNo" size="6" maxlength="20"
						value="<%= props.getProperty("delCesSectNo", "") %>" @oscar.formDB />
					</td>
					<td><input type="checkbox" name="delPrim"
						<%= props.getProperty("delPrim", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> PRIMARY</td>
					<td><input type="checkbox" name="delElect"
						<%= props.getProperty("delElect", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> ELECTIVE</td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><input type="checkbox" name="delRepeat"
						<%= props.getProperty("delRepeat", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> REPEAT</td>
					<td><input type="checkbox" name="delEmergy"
						<%= props.getProperty("delEmergy", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> EMERGENCY</td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td colspan="2"><input type="checkbox" name="delLowTranInc"
						<%= props.getProperty("delLowTranInc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> LOW TRANSVERSE INCISION</td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td colspan="2"><input type="checkbox" name="delOther"
						<%= props.getProperty("delOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER (<I>specify</I>) <input type="text"
						name="delOtherSpec" size="20" maxlength="60"
						value="<%= props.getProperty("delOtherSpec", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="delBreech"
						<%= props.getProperty("delBreech", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> BREECH</td>
					<td colspan="2" valign="top"><input type="checkbox"
						name="delFrank"
						<%= props.getProperty("delFrank", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FRANK<br>
					<input type="checkbox" name="delComplete"
						<%= props.getProperty("delComplete", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> COMPLETE<br>
					<input type="checkbox" name="delIncomplete"
						<%= props.getProperty("delIncomplete", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> INCOMPLETE<br>
					<input type="checkbox" name="delFootling"
						<%= props.getProperty("delFootling", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FOOTLING<br>
					</td>
					<td valign="top"><input type="checkbox" name="delSpont"
						<%= props.getProperty("delSpont", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> SPONTANEOUS<br>
					<input type="checkbox" name="delAssit"
						<%= props.getProperty("delAssit", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> ASSISTED<br>
					<input type="checkbox" name="delForHead"
						<%= props.getProperty("delForHead", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FORCEPS TO HEAD<br>
					<input type="checkbox" name="delExtracted"
						<%= props.getProperty("delExtracted", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> EXTRACTED<br>
					<input type="checkbox" name="delVersion"
						<%= props.getProperty("delVersion", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> VERSION</td>
				</tr>
				<tr>
					<td colspan="4" valign="top"><input type="checkbox"
						name="delOtherPres"
						<%= props.getProperty("delOtherPres", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER PRESENTATION (<I>specify</I>) <input
						type="text" name="delOtherPresSpec" size="20" maxlength="60"
						value="<%= props.getProperty("delOtherPresSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2" width="55%" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="3"><B>ANALGESIA/ANESTHESIA</B></td>
						</tr>
						<tr>
							<td width="33%"><input type="checkbox" name="delAnaNone"
								<%= props.getProperty("delAnaNone", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> NONE</td>
							<td width="33%"><input type="checkbox"
								name="delAnaNarcotics"
								<%= props.getProperty("delAnaNarcotics", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> NARCOTICS</td>
							<td width="33%"><input type="checkbox" name="delAnaEpidu"
								<%= props.getProperty("delAnaEpidu", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> EPIDURAL</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delAnaN2O2"
								<%= props.getProperty("delAnaN2O2", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> N2O2/O2</td>
							<td><input type="checkbox" name="delAnaLocal"
								<%= props.getProperty("delAnaLocal", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> LOCAL</td>
							<td><input type="checkbox" name="delAnaSpinal"
								<%= props.getProperty("delAnaSpinal", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> SPINAL</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delAnaCSE"
								<%= props.getProperty("delAnaCSE", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> CSE</td>
							<td><input type="checkbox" name="delAnaPudendal"
								<%= props.getProperty("delAnaPudendal", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> PUDENDAL</td>
							<td><input type="checkbox" name="delAnaGen"
								<%= props.getProperty("delAnaGen", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> GENERAL</td>
						</tr>
						<tr>
							<td colspan="3"><input type="checkbox" name="delAnaOther"
								<%= props.getProperty("delAnaOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> OTHER (<I>specify</I>) <input type="text"
								name="delAnaOtherSpec" size="20" maxlength="60"
								value="<%= props.getProperty("delAnaOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td><B>OXYTOCIN POSTPARTUM</B></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delOxyIV"
								<%= props.getProperty("delOxyIV", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> IV <input type="checkbox" name="delOxyIM"
								<%= props.getProperty("delOxyIM", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> IM DOSE(S) <input type="text"
								name="delOxyIMDose" size="20" maxlength="60"
								value="<%= props.getProperty("delOxyIMDose", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delOxyOther"
								<%= props.getProperty("delOxyOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> OTHER (<I>specify</I>) <input type="text"
								name="delOxyOtherSpec" size="15" maxlength="60"
								value="<%= props.getProperty("delOxyOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td>SIGNATURE: <input type="text" name="delOxySign"
								size="20" maxlength="60"
								value="<%= props.getProperty("delOxySign", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td colspan="3">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="4"><B>PLACENTA AND CORD</B></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delPlaSpont"
								<%= props.getProperty("delPlaSpont", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> SPONTANEOUS &nbsp;&nbsp; <input
								type="checkbox" name="delPlaAssis"
								<%= props.getProperty("delPlaAssis", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> ASSISTED</td>
							<td align="right">COMPLETE</td>
							<td><input type="checkbox" name="delPlaCompN"
								<%= props.getProperty("delPlaCompN", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> NO</td>
							<td><input type="checkbox" name="delPlaCompY"
								<%= props.getProperty("delPlaCompY", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> YES</td>
						</tr>
						<tr>
							<td colspan="2"><input type="checkbox" name="delPlaManOper"
								<%= props.getProperty("delPlaManOper", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> MANUAL/OPERATIVE REMOVAL</td>
							<td align="right">CORD VESSELS</td>
							<td><input type="checkbox" name="delPla2"
								<%= props.getProperty("delPla2", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> 2</td>
							<td><input type="checkbox" name="delPla3"
								<%= props.getProperty("delPla3", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> 3</td>
						</tr>
						<tr>
							<td colspan="2">ABNORMALITIES:</td>
							<td align="right">CORD GASES SENT</td>
							<td><input type="checkbox" name="delPlaCGSN"
								<%= props.getProperty("delPlaCGSN", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> NO</td>
							<td><input type="checkbox" name="delPlaCGSY"
								<%= props.getProperty("delPlaCGSY", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> YES</td>
						</tr>
						<tr>
							<td colspan="2"><input type="text" name="delPlaAbnormal"
								style="width: 100%" size="30" maxlength="60"
								value="<%= props.getProperty("delPlaAbnormal", "") %>"
								@oscar.formDB /></td>
							<td align="right">PLACENTA SENT TO PATHOLOGY</td>
							<td><input type="checkbox" name="delPlaPSPN"
								<%= props.getProperty("delPlaPSPN", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> NO</td>
							<td><input type="checkbox" name="delPlaPSPY"
								<%= props.getProperty("delPlaPSPY", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> YES</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td width="40%" rowspan="2" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="2"><B>PERINEUM/VAGINA/CERVIX</B></td>
						</tr>
						<tr>
							<td colspan="2"><input type="checkbox" name="delPerIntact"
								<%= props.getProperty("delPerIntact", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> INTACT <br>
							<input type="checkbox" name="delPerLaceration"
								<%= props.getProperty("delPerLaceration", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> LACERATION <br>
							&nbsp;&nbsp;&nbsp; <input type="checkbox" name="delPer1"
								<%= props.getProperty("delPer1", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> 1st <input type="checkbox" name="delPer2"
								<%= props.getProperty("delPer2", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> 2nd <input type="checkbox" name="delPer3"
								<%= props.getProperty("delPer3", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> 3rd <input type="checkbox" name="delPer4"
								<%= props.getProperty("delPer4", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> 4th <br>
							<input type="checkbox" name="delPerCerTear"
								<%= props.getProperty("delPerCerTear", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> CERVICAL TEAR <br>
							<input type="checkbox" name="delPerOthTrau"
								<%= props.getProperty("delPerOthTrau", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> OTHER TRAUMA <input type="text"
								name="delPerOthTrauSpec" size="12" maxlength="60"
								value="<%= props.getProperty("delPerOthTrauSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delPerEpisio"
								<%= props.getProperty("delPerEpisio", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> EPISIOTOMY</td>
							<td><input type="checkbox" name="delPerMidline"
								<%= props.getProperty("delPerMidline", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> MIDLINE</td>
						</tr>
						<tr>
							<td></td>
							<td><input type="checkbox" name="delPerMedio"
								<%= props.getProperty("delPerMedio", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> MEDIOLATERAL</td>
						</tr>
						<tr>
							<td colspan="2">Sutured by: <input type="text"
								name="delPerSutu" size="12" maxlength="60"
								value="<%= props.getProperty("delPerSutu", "") %>" @oscar.formDB />
							MD/RM</td>
						</tr>
					</table>

					</td>
					<td colspan="2" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td><B>ESTIMATED BLOOD LOSS</B></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="delEstBld1"
								<%= props.getProperty("delEstBld1", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> &lt;500 cc &nbsp;&nbsp; <input
								type="checkbox" name="delEstBld2"
								<%= props.getProperty("delEstBld2", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> &lt;500-1000 cc &nbsp;&nbsp; <input
								type="checkbox" name="delEstBld3"
								<%= props.getProperty("delEstBld3", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> &gt;1000 cc <BR>
							BLOOD TRANSFUSION <BR>
							<input type="checkbox" name="delEstBldN"
								<%= props.getProperty("delEstBldN", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> NO <input type="checkbox"
								name="delEstBldY"
								<%= props.getProperty("delEstBldY", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> YES &nbsp;&nbsp; No. of units <input
								type="text" name="delEstBldUnit" size="6" maxlength="8"
								value="<%= props.getProperty("delEstBldUnit", "") %>"
								@oscar.formDB /> <BR>
							<input type="checkbox" name="delEstBldOther"
								<%= props.getProperty("delEstBldOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> OTHER (specify): <input type="text"
								name="delEstBldOtherSpec" size="15" maxlength="30"
								value="<%= props.getProperty("delEstBldOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>


					</td>
				</tr>
				<tr>
					<td colspan="2">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td></td>
							<td>NO</td>
							<td>YES</td>
							<td>INITIALS:</td>
						</tr>
						<tr>
							<td align="right">SPONGE COUNT CORRECT</td>
							<td><input type="checkbox" name="delEstSpoN1"
								<%= props.getProperty("delEstSpoN1", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><input type="checkbox" name="delEstSpoY1"
								<%= props.getProperty("delEstSpoY1", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><input type="text" name="delEstSpoInit1" size="10"
								maxlength="50"
								value="<%= props.getProperty("delEstSpoInit1", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right">NEEDLE COUNT CORRECT</td>
							<td><input type="checkbox" name="delEstSpoN2"
								<%= props.getProperty("delEstSpoN2", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><input type="checkbox" name="delEstSpoY2"
								<%= props.getProperty("delEstSpoY2", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><input type="text" name="delEstSpoInit2" size="10"
								maxlength="50"
								value="<%= props.getProperty("delEstSpoInit2", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>


					</td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td colspan="2">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2"><B>INDICATION FOR OPERATIVE DELIVERY:</B></td>
				</tr>
				<tr>
					<td width="50%">PRINCIPAL <input type="text"
						name="delIndPrinc" style="width: 100%" size="60" maxlength="80"
						value="<%= props.getProperty("delIndPrinc", "") %>" @oscar.formDB />
					</td>
					<td>OTHER: <input type="text" name="delIndOther"
						style="width: 100%" size="60" maxlength="80"
						value="<%= props.getProperty("delIndOther", "") %>" @oscar.formDB />
					</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>



	<table width="100%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<td width="50%" valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2"><B>
					<div class="small10">4. BIRTH AND NEWBORN</div>
					</B></td>
				</tr>
				<tr>
					<td width="60%" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="2">
						<tr>
							<th colspan="3">TIME SUMMARY</th>
						</tr>
						<tr>
							<td width="20%" valign="top"></td>
							<th width="30%" valign="top">HOURS:MINS</th>
							<th>DAY/MONTH/YEAR</th>
						</tr>
						<tr>
							<td align="right">MEMBRANES RUPTURED</td>
							<td><input type="text" name="birTimeHour1"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour1", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate1" id="birTimeDate1"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate1", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate1_cal"></td>
						</tr>
						<tr>
							<td align="right">1st STAGE STARTED</td>
							<td><input type="text" name="birTimeHour2"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour2", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate2" id="birTimeDate2"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate2", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate2_cal"></td>
						</tr>
						<tr>
							<td align="right">2nd STAGE STARTED</td>
							<td><input type="text" name="birTimeHour3"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour3", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate3" id="birTimeDate3"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate3", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate3_cal"></td>
						</tr>
						<tr>
							<td align="right">NEWBORN DELIVERED</td>
							<td><input type="text" name="birTimeHour4"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour4", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate4" id="birTimeDate4"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate4", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate4_cal"></td>
						</tr>
						<tr>
							<td align="right">PLACENTA DELIVERED</td>
							<td><input type="text" name="birTimeHour5"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour5", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate5" id="birTimeDate5"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate5", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate5_cal"></td>
						</tr>
					</table>

					</td>
					<td align="center">

					<table class="small8" width="70%" border="0" cellspacing="0"
						cellpadding="2">
						<tr>
							<th colspan="3">DURATION</th>
						</tr>
						<tr>
							<td width="33%"></td>
							<th>HOURS</th>
							<th>MINS</th>
						</tr>
						<tr>
							<td align="right">1st STAGE</td>
							<td><input type="text" name="birDurHour1"
								onDblClick="calc1();" class="spe" size="2" maxlength="2"
								value="<%= props.getProperty("birDurHour1", "") %>"
								@oscar.formDB /></td>
							<td><input type="text" name="birDurMin1" size="2"
								maxlength="2" value="<%= props.getProperty("birDurMin1", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right">2nd STAGE</td>
							<td><input type="text" name="birDurHour2"
								onDblClick="calc2();" class="spe" size="2" maxlength="2"
								value="<%= props.getProperty("birDurHour2", "") %>"
								@oscar.formDB /></td>
							<td><input type="text" name="birDurMin2" size="2"
								maxlength="2" value="<%= props.getProperty("birDurMin2", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right">3rd STAGE</td>
							<td><input type="text" name="birDurHour3"
								onDblClick="calc3();" class="spe" size="2" maxlength="2"
								value="<%= props.getProperty("birDurHour3", "") %>"
								@oscar.formDB /></td>
							<td><input type="text" name="birDurMin3" size="2"
								maxlength="2" value="<%= props.getProperty("birDurMin3", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right" colspan="2" rowspan="2">DURATION OF
							RUPTURED MEMBRANES</td>
							<td>HOURS</td>
						</tr>
						<tr>
							<td><input type="text" name="birDurRupHour"
								onDblClick="calcRup();" class="spe" style="width: 100%" size="5"
								maxlength="5"
								value="<%= props.getProperty("birDurRupHour", "") %>"
								@oscar.formDB /> <input type="hidden" name="birDurRupMin" /></td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td colspan="2">

					<table class="small8" width="100%" border="1" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="20%" rowspan="2" nowrap><input type="checkbox"
								name="birMale"
								<%= props.getProperty("birMale", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> MALE<BR>
							<input type="checkbox" name="birFemale"
								<%= props.getProperty("birFemale", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> FEMALE<BR>
							<input type="checkbox" name="birAmbiguous"
								<%= props.getProperty("birAmbiguous", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> AMBIGUOUS</td>
							<td width="33%" colspan="3">APGAR <I>(see criteria on
							Newborn Record)</I></td>
							<td width="10%" rowspan="2" nowrap>WEIGHT (g)<BR>
							<input type="text" name="birWeight" style="width: 100%" size="5"
								maxlength="6" value="<%= props.getProperty("birWeight", "") %>"
								@oscar.formDB /></td>
							<td align="center" rowspan="2"><B>STILLBIRTH</B>
							(dd/mm/yyyy)<br>
							Last FHR <img src="../images/cal.gif" id="birFHR_cal"> <input
								type="text" name="birFHR" id="birFHR" size="10" maxlength="10"
								value="<%= props.getProperty("birFHR", "") %>" @oscar.formDB
								dbType="date" /></td>
						</tr>
						<tr>
							<td width="10%">at 1 min.<br>
							<input type="text" name="birApgar1" size="6" maxlength="10"
								value="<%= props.getProperty("birApgar1", "") %>" @oscar.formDB />
							</td>
							<td width="10%">at 5 min.<br>
							<input type="text" name="birApgar5" size="6" maxlength="10"
								value="<%= props.getProperty("birApgar5", "") %>" @oscar.formDB />
							</td>
							<td width="20%">at 10 min.<br>
							<input type="text" name="birApgar10" size="10" maxlength="20"
								value="<%= props.getProperty("birApgar10", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>


					</td>
				</tr>
			</table>



			</td>
			<td valign="top">

			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="2">
				<tr>
					<td width="10%">DELIVERED<br>
					BY:</td>
					<td><input type="text" name="birDelBy" style="width: 100%"
						size="60" maxlength="80"
						value="<%= props.getProperty("birDelBy", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="checkbox" name="birDelMD"
						<%= props.getProperty("birDelMD", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> MD &nbsp;&nbsp; <input type="checkbox"
						name="birDelRM"
						<%= props.getProperty("birDelRM", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> RM &nbsp;&nbsp; <input type="checkbox"
						name="birDelRN"
						<%= props.getProperty("birDelRN", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> RN &nbsp;&nbsp; <input type="checkbox"
						name="birDelOther"
						<%= props.getProperty("birDelOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER <I>(specify):</I> <input type="text"
						name="birDelOtherSpec" size="6" maxlength="15"
						value="<%= props.getProperty("birDelOtherSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>MD/RMS<br>
					PRESENT:</td>
					<td><textarea name="birMDPres" style="width: 100%" cols="30"
						rows="3" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("birMDPres", "") %> </textarea>
					</td>
				</tr>
				<tr>
					<td>NURSES<br>
					PRESENT:</td>
					<td><textarea name="birNurPres" style="width: 100%" cols="30"
						rows="3" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("birNurPres", "") %> </textarea>
					</td>
				</tr>
				<tr>
					<td>OTHERS<br>
					PRESENT:</td>
					<td><textarea name="birOtherPres" style="width: 100%"
						cols="30" rows="3" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("birOtherPres", "") %> </textarea>
					</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>


	<table class="small8" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td width="60%"><B>COMMENTS ON LABOUR AND BIRTH:</B> <input
				type="checkbox" name="comLabNormal"
				<%= props.getProperty("comLabNormal", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> NORMAL &nbsp;&nbsp; IF NOT (SPECIFY): <input
				type="text" name="comLabNormalSpec" size="10" maxlength="20"
				value="<%= props.getProperty("comLabNormalSpec", "") %>"
				@oscar.formDB /></td>
			<td>PLACE OF BIRTH: <input type="checkbox" name="comLabPlaHosp"
				<%= props.getProperty("comLabPlaHosp", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> HOSPITAL &nbsp;&nbsp; <input type="checkbox"
				name="comLabPlaHome"
				<%= props.getProperty("comLabPlaHome", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> HOME &nbsp;&nbsp; <input type="checkbox"
				name="comLabPlaTran"
				<%= props.getProperty("comLabPlaTran", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> TRANSFER IN LABOUR</td>
		</tr>
		<tr>
			<td colspan="2"><textarea name="comLabBirth" style="width: 100%"
				cols="100" rows="2" @oscar.formDB dbType="text"> <%= props.getProperty("comLabBirth", "") %> </textarea>
			</td>
		</tr>
	</table>

	<hr>
	<table class="small8" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td colspan="2" nowrap><B>CONSULT TO:</B> <input type="text"
				name="consultTo" size="30" maxlength="120"
				value="<%= props.getProperty("consultTo", "") %>" @oscar.formDB />
			&nbsp;| ADMISSION <input type="text" name="admisDateTime"
				id="admisDateTime" size="12" maxlength="16"
				value="<%= props.getProperty("admisDateTime", "") %>" @oscar.formDB />
			<img src="../images/cal.gif" id="admisDateTime_cal"></td>
			<td width="20%">SIGNATURE</td>
			<td width="20%">SIGNATURE</td>
		</tr>
		<tr>
			<td width="15%"><input type="checkbox" name="conObste"
				<%= props.getProperty("conObste", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> OBSTETRICIAN</td>
			<td><input type="checkbox" name="conPedia"
				<%= props.getProperty("conPedia", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> PEDIATRICIAN &nbsp;&nbsp;&nbsp; | DISCHARGE <input
				type="text" name="dischargeDateTime" id="dischargeDateTime"
				size="12" maxlength="16"
				value="<%= props.getProperty("dischargeDateTime", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="dischargeDateTime_cal"> | PP LOS <input type="text"
				name="ppLos" onDblClick="calcPPLOS();" class="spe" size="3"
				maxlength="3" value="<%= props.getProperty("ppLos", "") %>"
				@oscar.formDB /> hr</td>
			<td><input type="text" name="conRmSignat" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("conRmSignat", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="conMdSignat" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("conMdSignat", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="conFamPhy"
				<%= props.getProperty("conFamPhy", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> FAMILY PHYSICIAN</td>
			<td nowrap>
			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td nowrap><input type="checkbox" name="conOther"
						<%= props.getProperty("conOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER: <input type="text"
						name="conOtherTxt" size="20" maxlength="30"
						value="<%= props.getProperty("conOtherTxt", "") %>" @oscar.formDB />
					</td>
					<td align="right" nowrap>L.O.S. <input type="text"
						name="conLos" onDblClick="calcLOS();" class="spe" size="3"
						maxlength="3" value="<%= props.getProperty("conLos", "") %>"
						@oscar.formDB /> hr</td>
				</tr>
			</table>

			</td>
			<td align="right">RM/RN</td>
			<td align="right">MD/RM</td>
		</tr>
	</table>

	<table class="small8" width="100%" border="1" cellspacing="0"
		cellpadding="0">
		<tr>
			<td>HLTH 1588 Rev. 02/03 Prepared by the B.C Reproductive Care
			Program <br>
			* Date format: <span class="small8">(dd/mm/yyyy)</span></td>
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
				value="Print" onclick="javascript:return onPrint();return false()" />
			</td>
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
Calendar.setup({ inputField : "pg1_formDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_formDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate5_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "birFHR", ifFormat : "%d/%m/%Y", showsTime :false, button : "birFHR_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "admisDateTime", ifFormat : "%d/%m/%Y %H:%M", showsTime :true, button : "admisDateTime_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "dischargeDateTime", ifFormat : "%d/%m/%Y %H:%M", showsTime :true, button : "dischargeDateTime_cal", singleClick : true, step : 1 });
</script>
</body>
</html:html>
