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

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	String formClass = "AdfV2";
	String formLink = "formadfv2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

	//get project_home
	String project_home = getServletContext().getRealPath("/") ;
	String sep = project_home.substring(project_home.length()-1);
	project_home = project_home.substring(0, project_home.length()-1) ;
	project_home = project_home.substring(project_home.lastIndexOf(sep)+1) ;

	boolean bView = false;
  	if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>MEDICAL HISTORY AND ADMISSION EXAMINATION</title>
<link rel="stylesheet" type="text/css" href="arStyle.css">
<html:base />
</head>


<script type="text/javascript" language="Javascript">

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

    function reset() {
        document.forms[0].target = "apptProviderSearch";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(650,850,'../provider/notice.htm');
            document.forms[0].action = "../form/createpdf?__title=&__cfgfile=&__cfgfile=&__template=";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        }
        return ret;
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
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            //var y = dt[2];  var m = dt[1];  var d = dt[0];
            var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex)  {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].sigDate)==false){
            b = false;
        } else if(valDate(document.forms[0].influDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pneuDate)==false){
            b = false;
        } 

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<%--
@oscar.formDB Table="formAdfV2" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
--%>
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
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
%> <input type="button" value="Exit"
				onclick="javascript:return onExit();" /> <input type="button"
				value="Print" onclick="javascript:window.print(); return false;" />
			</td>
			<%
  if (!bView) {
%>
			<td align="right"><a
				href="javascript: popupFixedPage(700,950,'../decision/annualreview/annualreviewplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>');">Planner</a>
			</td>
			<%
  }
%>
		</tr>
	</table>


	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<th>MEDICAL HISTORY AND ADMISSION EXAMINATION Ver. 2 (<font
				color="blue"><%= props.getProperty("c_patientname", "") %></font>)</th>
			<input type="hidden" name="c_patientname" size="60" maxlength="60"
				value="<%= props.getProperty("c_patientname", "") %>" @oscar.formDB />
		</tr>
	</table>


	<!--table width="100%" border="1"  cellspacing="0" cellpadding="0" >
<tr width="100%">
	<td width="15%">Patient Name</td>
	<td>
	<input type="text" name="c_patientname" size="60" maxlength="60" value="<%= props.getProperty("c_surname", "")+", " + props.getProperty("c_givenName", "") %>" />
	</td>
</tr>
</table>
<br-->

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="20%">SENDING FACILITY:</td>
			<td width="30%"><input type="text" name="sendFacility"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("sendFacility", "") %>" @oscar.formDB />
			</td>
			<td width="20%" align="right">POA/SDM:</td>
			<td width="30%"><input type="text" name="poaSdm"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("poaSdm", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td colspan="4">CAPABLE for TREATMENT DECISIONS:
			&nbsp;&nbsp;&nbsp; <input type="radio" name="capTreatDecY"
				<%= props.getProperty("capTreatDecY", "") %> @oscar.formDB
				dbType="tinyint(1)" onclick='onCheck(this, "capTreatDec")' /> YES
			&nbsp;&nbsp;&nbsp; <input type="radio" name="capTreatDecN"
				<%= props.getProperty("capTreatDecN", "") %> @oscar.formDB
				dbType="tinyint(1)" onclick='onCheck(this, "capTreatDec")' /> NO</td>
		</tr>
		<tr>
			<td>ADVANCE DIRECTIVES:</td>
			<td colspan="3"><input type="text" name="advDirective"
				style="width: 100%" size="100" maxlength="100"
				value="<%= props.getProperty("advDirective", "") %>" @oscar.formDB />
			</td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td>ACTIVE PROBLEMS: <textarea name="actProblem"
				style="width: 100%" cols="80" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("actProblem", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td>PAST/INACTIVE PROBLEMS: <textarea name="inactProblem"
				style="width: 100%" cols="80" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("inactProblem", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td>COURSE OVER THE YEAR: <textarea name="courseOverYear"
				style="width: 100%" cols="80" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("courseOverYear", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td>MEDICATIONS: <textarea name="medications"
				style="width: 100%" cols="80" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("medications", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td>ALLERGIES: <input type="text" name="allergy"
				style="width: 100%" size="100" maxlength="255"
				value="<%= props.getProperty("allergy", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>DIET: <input type="text" name="diet" style="width: 100%"
				size="100" maxlength="255"
				value="<%= props.getProperty("diet", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>IMMUNIZATIONS: INFLUENZA date: <input type="text"
				name="influDate" size="10" maxlength="10"
				value="<%= props.getProperty("influDate", "") %>" @oscar.formDB
				dbType="date" /> PNEUMOCOCCAL date: <input type="text"
				name="pneuDate" size="10" maxlength="10"
				value="<%= props.getProperty("pneuDate", "") %>" @oscar.formDB
				dbType="date" /></td>
		</tr>
		<tr>
			<td>MANTOUX date and response: <input type="text"
				name="mantDateRes" size="30" maxlength="50"
				value="<%= props.getProperty("mantDateRes", "") %>" @oscar.formDB />
			OTHERS: <input type="text" name="immuOthers" size="30" maxlength="50"
				value="<%= props.getProperty("immuOthers", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>PERTINENT LABS / INVESTIGATIONS: <textarea
				name="pertLabInvest" style="width: 100%" cols="80" rows="2"
				@oscar.formDB dbType="text" /><%= props.getProperty("pertLabInvest", "") %></textarea>
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th colspan="2">TARGETTED REVIEW OF SYSTEMS</th>
		</tr>
		<tr>
			<td width="22%">Communication:</td>
			<td><input type="text" name="communication" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("communication", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td><font size="-1"> Appetite/Dysphagia/Weight Loss:</font></td>
			<td><input type="text" name="appetDysphWeight"
				style="width: 100%" size="80" maxlength="100"
				value="<%= props.getProperty("appetDysphWeight", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td>Sleep / Energy:</td>
			<td><input type="text" name="sleepEnergy" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("sleepEnergy", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Depressive Symptoms:</td>
			<td><input type="text" name="depreSymptom" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("depreSymptom", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Problem Behaviours:</td>
			<td><input type="text" name="problemBehav" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("problemBehav", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Functional Status:</td>
			<td><input type="text" name="funcStatus" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("funcStatus", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Falls/Fractures:</td>
			<td><input type="text" name="fallFracture" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("fallFracture", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Pain:</td>
			<td><input type="text" name="pain" style="width: 100%" size="80"
				maxlength="100" value="<%= props.getProperty("pain", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td>Continence:</td>
			<td><input type="text" name="continence" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("continence", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Skin:</td>
			<td><input type="text" name="sysSkin" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("sysSkin", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Social/Supports:</td>
			<td><input type="text" name="socialSupp" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("socialSupp", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Other:</td>
			<td><input type="text" name="sysOther" style="width: 100%"
				size="80" maxlength="100"
				value="<%= props.getProperty("sysOther", "") %>" @oscar.formDB /></td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th colspan="8">TARGETTED PHYSICAL EXAMINATION:</th>
		</tr>
		<tr>
			<td width="10%" align="right">Weight (kg):</td>
			<td width="15%"><input type="text" name="phyWeight"
				style="width: 100%" size="10" maxlength="10"
				value="<%= props.getProperty("phyWeight", "") %>" @oscar.formDB />
			</td>
			<td width="10%" align="right">Height:</td>
			<td width="15%"><input type="text" name="phyHeight"
				style="width: 100%" size="10" maxlength="10"
				value="<%= props.getProperty("phyHeight", "") %>" @oscar.formDB />
			</td>
			<td width="10%" align="right">BP:lying</td>
			<td width="15%"><input type="text" name="phyBPlying"
				style="width: 100%" size="10" maxlength="16"
				value="<%= props.getProperty("phyBPlying", "") %>" @oscar.formDB />
			</td>
			<td width="10%" align="right">BP:Standing</td>
			<td width="15%"><input type="text" name="phyBPStanding"
				style="width: 100%" size="10" maxlength="16"
				value="<%= props.getProperty("phyBPStanding", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr align="right">
			<td>General Appearance:</td>
			<td colspan="7"><input type="text" name="phyGenAppear"
				style="width: 100%" size="80" maxlength="100"
				value="<%= props.getProperty("phyGenAppear", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td align="right">Eyes:</td>
			<td colspan="3"><input type="text" name="phyEyes"
				style="width: 100%" size="30" maxlength="50"
				value="<%= props.getProperty("phyEyes", "") %>" @oscar.formDB /></td>
			<td align="right">Ears:</td>
			<td colspan="3"><input type="text" name="phyEars"
				style="width: 100%" size="30" maxlength="50"
				value="<%= props.getProperty("phyEars", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td align="right">Oral hygeine:</td>
			<td colspan="3"><input type="text" name="phyOralHygeine"
				style="width: 100%" size="30" maxlength="50"
				value="<%= props.getProperty("phyOralHygeine", "") %>" @oscar.formDB />
			</td>
			<td align="right">Breast:</td>
			<td colspan="3"><input type="text" name="phyBreast"
				style="width: 100%" size="30" maxlength="50"
				value="<%= props.getProperty("phyBreast", "") %>" @oscar.formDB />
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr align="right">
			<td width="15%">Cardiac: heart sounds:</td>
			<td width="18%"><input type="text" name="phyCardHeartSound"
				style="width: 100%" size="20" maxlength="30"
				value="<%= props.getProperty("phyCardHeartSound", "") %>"
				@oscar.formDB /></td>
			<td width="15%">Peripheral pulses / edema</td>
			<td width="18%"><input type="text" name="phyPeriPulse"
				style="width: 100%" size="20" maxlength="30"
				value="<%= props.getProperty("phyPeriPulse", "") %>" @oscar.formDB />
			</td>
			<td width="10%">Other</td>
			<td><input type="text" name="phyOther" style="width: 100%"
				size="20" maxlength="30"
				value="<%= props.getProperty("phyOther", "") %>" @oscar.formDB /></td>
		</tr>
		<tr align="right">
			<td>Respiratory:</td>
			<td colspan="5"><input type="text" name="phyRespiratory"
				style="width: 100%" size="80" maxlength="100"
				value="<%= props.getProperty("phyRespiratory", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr align="right">
			<td>Neurological: Gait/Mobility</td>
			<td><input type="text" name="phyNeurological"
				style="width: 100%" size="20" maxlength="30"
				value="<%= props.getProperty("phyNeurological", "") %>"
				@oscar.formDB /></td>
			<td>Reflexes</td>
			<td><input type="text" name="phyReflexes" style="width: 100%"
				size="20" maxlength="30"
				value="<%= props.getProperty("phyReflexes", "") %>" @oscar.formDB />
			</td>
			<td>Babinski</td>
			<td><input type="text" name="phyBabinski" style="width: 100%"
				size="20" maxlength="30"
				value="<%= props.getProperty("phyBabinski", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr align="right">
			<td>Power</td>
			<td><input type="text" name="phyPower" style="width: 100%"
				size="20" maxlength="30"
				value="<%= props.getProperty("phyPower", "") %>" @oscar.formDB /></td>
			<td>Tone</td>
			<td><input type="text" name="phyTone" style="width: 100%"
				size="20" maxlength="30"
				value="<%= props.getProperty("phyTone", "") %>" @oscar.formDB /></td>
			<td>Other</td>
			<td><input type="text" name="phyPowerOther" style="width: 100%"
				size="20" maxlength="30"
				value="<%= props.getProperty("phyPowerOther", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr align="right">
			<td colspan="2">MMSE: <input type="text" name="phyMMSE"
				size="20" maxlength="20"
				value="<%= props.getProperty("phyMMSE", "") %>" @oscar.formDB /> /
			30 Comment</td>
			<td colspan="4"><input type="text" name="phyComment"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("phyComment", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr align="right">
			<td>Skin:</td>
			<td colspan="5"><input type="text" name="phySkin"
				style="width: 100%" size="80" maxlength="100"
				value="<%= props.getProperty("phySkin", "") %>" @oscar.formDB /></td>
		</tr>
		<tr align="right">
			<td>Abdomen/Rectal<br>
			<font size="-2">(if indicated)</font>:</td>
			<td colspan="5"><input type="text" name="phyAbdomen"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("phyAbdomen", "") %>" @oscar.formDB />
			</td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th colspan="2">IDENTIFICATION OF HIGH RISK PROBLEMS AND PLAN OF
			CARE:</th>
		</tr>
		<tr>
			<th width="50%">High Risk Problem:</th>
			<th>Investigations/ Plan of Care:</th>
		</tr>
		<tr>
			<td><input type="text" name="highRiskProb1" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("highRiskProb1", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="investPlanCare1"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("investPlanCare1", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="highRiskProb2" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("highRiskProb2", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="investPlanCare2"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("investPlanCare2", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="highRiskProb3" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("highRiskProb3", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="investPlanCare3"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("investPlanCare3", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="highRiskProb4" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("highRiskProb4", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="investPlanCare4"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("investPlanCare4", "") %>"
				@oscar.formDB /></td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td title="yyyy/mm/dd" width="30%">Date of assessment: <input
				type="text" name="sigDate" size="10" maxlength="10"
				value="<%= props.getProperty("sigDate", "") %>" @oscar.formDB
				dbType="date" /></td>
			<td>Signature : <input type="text" name="signature" size="50"
				maxlength="60" value="<%= props.getProperty("signature", "") %>"
				@oscar.formDB /></td>
			<td>Name : <input type="text" name="sigName" size="50"
				maxlength="60" value="<%= props.getProperty("sigName", "") %>"
				@oscar.formDB />
		</tr>
	</table>


	<table class="Head" class="hidePrint">
		<tr>
			<td>
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="button" value="Exit"
				onclick="javascript:return onExit();" /> <input type="button"
				value="Print" onclick="javascript:window.print(); return false;" />
			</td>
			<%
  if (!bView) {
%>
			<td align="right"><a
				href="javascript: popupFixedPage(700,950,'../decision/annualreview/annualreviewplanner?demographic_no=<%=demoNo%>&formId=<%=formId%>');">Planner</a>
			</td>
			<%
  }
%>
		</tr>
	</table>


</html:form>
</body>
</html:html>
