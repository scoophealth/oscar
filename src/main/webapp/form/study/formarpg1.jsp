<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%
    String formClass = "AR";
    String formLink = "study/formarpg1.jsp";

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
<html:base />
</head>

<script type="text/javascript" language="Javascript">
    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
    }
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {                        
            document.forms[0].action = "formar1print.jsp";         
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
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
  if (!bView) {
%>
			<td><a
				href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a>
			</td>

			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="javascript: popupPage('formarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;</td>
			<td align="right"><b>Edit:</b> <a
				href="formarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%--=demoNo--%>&formId=<%--=formId--%>&provNo=<%--=provNo--%>');">AR Planner</a-->
			<a href="ar2ping.jsp?demographic_no=<%=demoNo%>">Send to PING</a></td>
			<%
  }
%>
		</tr>
	</table>

	<table class="title" border="0" cellspacing="0" cellpadding="0"
		width="100%">
		<tr>
			<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>ANTENATAL
			RECORD 1</th>
			<%
	if (request.getParameter("historyet") != null) {
		out.println("<input type=\"hidden\" name=\"historyet\" value=\"" + request.getParameter("historyet") + "\">" );
	}
%>
		</tr>
	</table>
	<table width="60%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" colspan='4'>Name <input type="text"
				name="c_pName" style="width: 100%" size="30" maxlength="60"
				value="<%= props.getProperty("c_pName", "") %>" /></td>
		</tr>
		<tr>
			<td valign="top" colspan='4'>Address <input type="text"
				name="c_address" style="width: 100%" size="60" maxlength="80"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_address", "")) %>" />
			</td>
		</tr>
		<tr>
			<td valign="top" width="28%">Date of birth (yyyy/mm/dd)<br>
			<input type="text" name="pg1_dateOfBirth" style="width: 100%"
				size="10" maxlength="10"
				value="<%= props.getProperty("pg1_dateOfBirth", "") %>"
				readonly="true" /></td>
			<td width="8%">Age<br>
			<input type="text" name="pg1_age" style="width: 100%" size="10"
				maxlength="10" value="<%= props.getProperty("pg1_age", "") %>" /></td>
			<td width="25%" nowrap>Marital status <br>
			<input type="checkbox" name="pg1_msMarried"
				<%= props.getProperty("pg1_msMarried", "") %> />M <input
				type="checkbox" name="pg1_msCommonLaw"
				<%= props.getProperty("pg1_msCommonLaw", "") %> />CL <input
				type="checkbox" name="pg1_msSingle"
				<%= props.getProperty("pg1_msSingle", "") %> />S</td>
			<td>Education level <br>
			<input type="text" name="pg1_eduLevel" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_eduLevel", "")) %>" />
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td>Occupation<br>
			<input type="text" name="pg1_occupation" size="15"
				style="width: 100%" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_occupation", "")) %>" />
			</td>
			<td>Language<br>
			<input type="text" name="pg1_language" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_language", "")) %>" />
			</td>
			<td>Home phone<br>
			<input type="text" name="pg1_homePhone" size="15" style="width: 100%"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_homePhone", "")) %>" />
			</td>
			<td>Work phone<br>
			<input type="text" name="pg1_workPhone" size="15" style="width: 100%"
				maxlength="20" value="<%= props.getProperty("pg1_workPhone", "") %>" />
			</td>
			<td>Name of partner<br>
			<input type="text" name="pg1_partnerName" size="15"
				style="width: 100%" maxlength="50"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerName", "")) %>" />
			</td>
			<td>Age<br>
			<input type="text" name="pg1_partnerAge" size="2" style="width: 100%"
				maxlength="2" value="<%= props.getProperty("pg1_partnerAge", "") %>" />
			</td>
			<td valign="top">Occupation<br>
			<input type="text" name="pg1_partnerOccupation" size="15"
				style="width: 100%" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerOccupation", "")) %>" />
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td nowrap>Birth attendants<br>
			<input type="checkbox" name="pg1_baObs"
				<%= props.getProperty("pg1_baObs", "") %> />OBS <input
				type="checkbox" name="pg1_baFP"
				<%= props.getProperty("pg1_baFP", "") %> />FP <input
				type="checkbox" name="pg1_baMidwife"
				<%= props.getProperty("pg1_baMidwife", "") %> />Midwife<br>
			<input type="text" name="c_ba" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_ba", "")) %>" />
			</td>
			<td nowrap valign="top">Family physician<br>
			<div align="center"><textarea name="pg1_famPhys"
				style="width: 100%" cols="30" rows="2"><%= props.getProperty("pg1_famPhys", "") %></textarea></div>
			</td>
			<td nowrap>Newborn care<br>
			<input type="checkbox" name="pg1_ncPed"
				<%= props.getProperty("pg1_ncPed", "") %> />Ped. <input
				type="checkbox" name="pg1_ncFP"
				<%= props.getProperty("pg1_ncFP", "") %> />FP <input type="checkbox"
				name="pg1_ncMidwife" <%= props.getProperty("pg1_ncMidwife", "") %> />Midwife<br>
			<input type="text" name="c_nc" size="15" style="width: 100%"
				maxlength="25" value="<%= props.getProperty("c_nc", "") %>" /></td>
			<td nowrap valign="top">Ethnic background of mother/father<br>
			<div align="center"><textarea name="pg1_ethnicBg"
				style="width: 100%" cols="30" rows="2"><%= props.getProperty("pg1_ethnicBg", "") %></textarea></div>
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="12%"><input type="checkbox" name="pg1_vbac"
				<%= props.getProperty("pg1_vbac", "") %> />VBAC<br>
			<input type="checkbox" name="pg1_repeatCS"
				<%= props.getProperty("pg1_repeatCS", "") %> />Repeat CS<br>
			</td>
			<td width="44%">Allergies(list):<br>
			<div align="center"><textarea name="c_allergies"
				style="width: 100%" cols="30" rows="2"><%= props.getProperty("c_allergies", "") %></textarea></div>
			</td>
			<td width="44%">Medications (list)<br>
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
			<td valign="top" nowrap>Menstrual history (LMP): <input
				type="text" name="pg1_menLMP" size="10" maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_menLMP", "")) %>" />&nbsp;
			&nbsp; &nbsp; Cycle <input type="text" name="pg1_menCycle" size="7"
				maxlength="7"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_menCycle", "")) %>" />&nbsp;
			&nbsp; &nbsp; <input type="checkbox" name="pg1_menReg"
				<%= props.getProperty("pg1_menReg", "") %> />Regular &nbsp; &nbsp;
			&nbsp; EDB <input type="text" name="pg1_menEDB" size="10"
				maxlength="10" value="<%= props.getProperty("pg1_menEDB", "") %>" /><br>
			Contraception:<br>
			<input type="checkbox" name="pg1_iud"
				<%= props.getProperty("pg1_iud", "") %> />IUD <input type="checkbox"
				name="pg1_hormone" <%= props.getProperty("pg1_hormone", "") %> />Hormonal(type)
			<input type="text" name="pg1_hormoneType" size="15" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_hormoneType", "")) %>" />
			<input type="checkbox" name="pg1_otherAR1"
				<%= props.getProperty("pg1_otherAR1", "") %> />Other <input
				type="text" name="pg1_otherAR1Name" size="15" maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_otherAR1Name", "")) %>" />
			Last used<input type="text" name="pg1_lastUsed" size="10"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_lastUsed", "")) %>" />
			</td>
			<td valign="top" width="25%"><font size="+1"><b>Final
			EDB</font></b></font> (yyyy/mm/dd) <br>
			<input type="text" name="c_finalEDB" style="width: 100%" size="10"
				maxlength="10" value="<%= props.getProperty("c_finalEDB", "") %>" />
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td>Gravida<br>
			<input type="text" name="c_gravida" size="5" style="width: 100%"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_gravida", "")) %>" />
			</td>
			<td>Term<br>
			<input type="text" name="c_term" size="5" style="width: 100%"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_term", "")) %>" />
			</td>
			<td>Prem<br>
			<input type="text" name="c_prem" size="5" style="width: 100%"
				maxlength="8"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_prem", "")) %>" />
			</td>
			<td valign="top" nowrap>No. of pregnancy loss(es)<br>
			&nbsp; &nbsp; <input type="checkbox" name="pg1_ectopic"
				<%= props.getProperty("pg1_ectopic", "") %> />Ectopic <input
				type="text" name="pg1_ectopicBox" size="2" maxlength="2"
				value="<%= props.getProperty("pg1_ectopicBox", "") %>" />&nbsp;
			&nbsp; <input type="checkbox" name="pg1_termination"
				<%= props.getProperty("pg1_termination", "") %> />Termination <input
				type="text" name="pg1_terminationBox" size="2" maxlength="2"
				value="<%= props.getProperty("pg1_terminationBox", "") %>" />&nbsp;
			&nbsp; <input type="checkbox" name="pg1_spontaneous"
				<%= props.getProperty("pg1_spontaneous", "") %> />Spontaneous <input
				type="text" name="pg1_spontaneousBox" size="2" maxlength="2"
				value="<%= props.getProperty("pg1_spontaneousBox", "") %>" />&nbsp;
			&nbsp; <input type="checkbox" name="pg1_stillborn"
				<%= props.getProperty("pg1_stillborn", "") %> />Stillborn <input
				type="text" name="pg1_stillbornBox" size="2" maxlength="2"
				value="<%= props.getProperty("pg1_stillbornBox", "") %>" /></td>
			<td>Living<br>
			<input type="text" name="c_living" size="5" style="width: 100%"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_living", "")) %>" />
			</td>
			<td nowrap>Multipregnancy<br>
			No.<input type="text" name="pg1_multi" size="5" style="width: 100%"
				maxlength="10" value="<%= props.getProperty("pg1_multi", "") %>" />
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
					<td width="40">No.</td>
					<td width="50">Year</td>
					<td width="40">Sex<br>
					M/F</td>
					<td width="70">Gest. age<br>
					(weeks)</td>
					<td width="70">Birth<br>
					weight</td>
					<td width="70">Length<br>
					of labour</td>
					<td width="120">Place<br>
					of birth</td>
					<td width="100">Type of birth<br>
					SVB CS Ass'd</td>
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
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="center" bgcolor="#CCCCCC"><b><font
				face="Verdana, Arial, Helvetica, sans-serif"> Medical History
			and Physical Examination</font></b></td>
		</tr>
	</table>
	<table class="shrinkMe" width="100%" border="1" cellspacing="0"
		cellpadding="0">
		<tr>
			<td align="center"><b>Current Pregnancy</b></td>
			<td align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" nowrap><b>Medical</b></td>
					<td align="center" width="15%">
					<div align="right">Yes</div>
					</td>
					<td align="center" nowrap width="15%">
					<div align="right">No</div>
					</td>
				</tr>
			</table>
			</td>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td nowrap align="center"><b>Genetic/Family</b></td>
					<td align="center" width="15%">
					<div align="right">Yes</div>
					</td>
					<td align="center" width="15%">
					<div align="right">No</div>
					</td>
				</tr>
			</table>
			</td>
			<td align="center"><b>Infection Discussion Topics</b></td>
			<td align="center"><b>Physical examination</b></td>
		</tr>
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3"><i>(check if positive)</i></td>
				</tr>
				<tr>
					<td>1.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c1p1");return false;'>Bleeding</a></td>
					<td width="15%"><input type="checkbox" name="pg1_cp1"
						<%= props.getProperty("pg1_cp1", "") %> /></td>
				</tr>
				<tr>
					<td>2.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c2p1");return false;'>Vomiting</a></td>
					<td width="15%"><input type="checkbox" name="pg1_cp2"
						<%= props.getProperty("pg1_cp2", "") %> /></td>
				</tr>
				<tr>
					<td valign="top">3.</td>
					<td nowrap><a href=#
						onClick='popupPage("<%=resource%>c3p1");return false;'>Smoking</a><br>
					<font size=1>cig/day <input type="text" name="pg1_box3"
						size="2"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box3", "")) %>"></font>
					</td>
					<td valign="bottom"><input type="checkbox" name="pg1_cp3"
						<%= props.getProperty("pg1_cp3", "") %>></td>
				</tr>
				<tr>
					<td>4.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c4p1");return false;'>Drugs</a></td>
					<td width="15%"><input type="checkbox" name="pg1_cp4"
						<%= props.getProperty("pg1_cp4", "") %> /></td>
				</tr>
				<tr>
					<td valign="top">5.</td>
					<td nowrap><a href=#
						onClick='popupPage("<%=resource%>c5p1");return false;'>Alcohol</a><br>
					<font size=1>drinks/day <input type="text" name="pg1_box5"
						size="2" maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box5", "")) %>"></font>
					</td>
					<td valign="bottom"><input type="checkbox" name="pg1_cp5"
						<%= props.getProperty("pg1_cp5", "") %> /></td>
				</tr>
				<tr>
					<td>6.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c6p1");return false;'>Infertility</a></td>
					<td width="15%"><input type="checkbox" name="pg1_cp6"
						<%= props.getProperty("pg1_cp6", "") %>></td>
				</tr>
				<tr>
					<td>7.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c7p1");return false;'>Radiation</a></td>
					<td width="15%"><input type="checkbox" name="pg1_cp7"
						<%= props.getProperty("pg1_cp7", "") %> /></td>
				</tr>
				<tr>
					<td valign="top">8.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c8p1");return false;'>Occup./Env.<br>
					hazards</a></td>
					<td width="15%"><input type="checkbox" name="pg1_cp8"
						<%= props.getProperty("pg1_cp8", "") %> /></td>
				</tr>
				<tr>
					<td colspan="3" nowrap>
					<hr>
					<b> Nutrition Assessment</b></td>
				</tr>
				<tr>
					<td colspan="3"><i>(check if positive)</i></td>
				</tr>
				<tr>
					<td colspan="2">Folic acid/vitamins</td>
					<td><input type="checkbox" name="pg1_naFolic"
						<%= props.getProperty("pg1_naFolic", "") %>></td>
				</tr>
				<tr>
					<td colspan="2">Milk products</td>
					<td><input type="checkbox" name="pg1_naMilk"
						<%= props.getProperty("pg1_naMilk", "") %>></td>
				</tr>
				<tr>
					<td colspan="2">Diet</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp; &nbsp; &nbsp;Balanced</td>
					<td><input type="checkbox" name="pg1_naDietBal"
						<%= props.getProperty("pg1_naDietBal", "") %>></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp; &nbsp; &nbsp;Restricted</td>
					<td><input type="checkbox" name="pg1_naDietRes"
						<%= props.getProperty("pg1_naDietRes", "") %>></td>
				</tr>
				<tr>
					<td colspan="2">Dietitian referral</td>
					<td><input type="checkbox" name="pg1_naRef"
						<%= props.getProperty("pg1_naRef", "") %>></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
						onClick='popupPage("<%=resource%>c10p1");return false;'>Endocrine/Diabetes</a></td>
					<td><input type="checkbox" name="pg1_yes10"
						<%= props.getProperty("pg1_yes10", "") %>></td>
					<td><input type="checkbox" name="pg1_no10"
						<%= props.getProperty("pg1_no10", "") %>></td>
				</tr>
				<tr>
					<td>11.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c11p1");return false;'>Heart</a></td>
					<td><input type="checkbox" name="pg1_yes11"
						<%= props.getProperty("pg1_yes11", "") %>></td>
					<td><input type="checkbox" name="pg1_no11"
						<%= props.getProperty("pg1_no11", "") %>></td>
				</tr>
				<tr>
					<td>12.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c12p1");return false;'>Renal/urinary
					tract</a></td>
					<td><input type="checkbox" name="pg1_yes12"
						<%= props.getProperty("pg1_yes12", "") %>></td>
					<td><input type="checkbox" name="pg1_no12"
						<%= props.getProperty("pg1_no12", "") %>></td>
				</tr>
				<tr>
					<td>13.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c13p1");return false;'>Respiratory</a></td>
					<td><input type="checkbox" name="pg1_yes13"
						<%= props.getProperty("pg1_yes13", "") %>></td>
					<td><input type="checkbox" name="pg1_no13"
						<%= props.getProperty("pg1_no13", "") %>></td>
				</tr>
				<tr>
					<td>14.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c14p1");return false;'>Liver/Hepatitis/GI</a></td>
					<td><input type="checkbox" name="pg1_yes14"
						<%= props.getProperty("pg1_yes14", "") %>></td>
					<td><input type="checkbox" name="pg1_no14"
						<%= props.getProperty("pg1_no14", "") %>></td>
				</tr>
				<tr>
					<td>15.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c15p1");return false;'>Neurological</a></td>
					<td><input type="checkbox" name="pg1_yes15"
						<%= props.getProperty("pg1_yes15", "") %>></td>
					<td><input type="checkbox" name="pg1_no15"
						<%= props.getProperty("pg1_no15", "") %>></td>
				</tr>
				<tr>
					<td>16.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c16p1");return false;'>Autoimmune</a></td>
					<td><input type="checkbox" name="pg1_yes16"
						<%= props.getProperty("pg1_yes16", "") %>></td>
					<td><input type="checkbox" name="pg1_no16"
						<%= props.getProperty("pg1_no16", "") %>></td>
				</tr>
				<tr>
					<td>17.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c17p1");return false;'>Breast</a></td>
					<td><input type="checkbox" name="pg1_yes17"
						<%= props.getProperty("pg1_yes17", "") %>></td>
					<td><input type="checkbox" name="pg1_no17"
						<%= props.getProperty("pg1_no17", "") %>></td>
				</tr>
				<tr>
					<td>18.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c18p1");return false;'>Gyn/PAP</a></td>
					<td><input type="checkbox" name="pg1_yes18"
						<%= props.getProperty("pg1_yes18", "") %>></td>
					<td><input type="checkbox" name="pg1_no18"
						<%= props.getProperty("pg1_no18", "") %>></td>
				</tr>
				<tr>
					<td>19.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c19p1");return false;'>Hospitalizations</a></td>
					<td><input type="checkbox" name="pg1_yes19"
						<%= props.getProperty("pg1_yes19", "") %>></td>
					<td><input type="checkbox" name="pg1_no19"
						<%= props.getProperty("pg1_no19", "") %>></td>
				</tr>
				<tr>
					<td>20.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c20p1");return false;'>Surgeries</a></td>
					<td><input type="checkbox" name="pg1_yes20"
						<%= props.getProperty("pg1_yes20", "") %>></td>
					<td><input type="checkbox" name="pg1_no20"
						<%= props.getProperty("pg1_no20", "") %>></td>
				</tr>
				<tr>
					<td>21.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c21p1");return false;'>Anesthetics</a></td>
					<td><input type="checkbox" name="pg1_yes21"
						<%= props.getProperty("pg1_yes21", "") %>></td>
					<td><input type="checkbox" name="pg1_no21"
						<%= props.getProperty("pg1_no21", "") %>></td>
				</tr>
				<tr>
					<td>22.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c22p1");return false;'>Hem./Transfusions</a></td>
					<td><input type="checkbox" name="pg1_yes22"
						<%= props.getProperty("pg1_yes22", "") %>></td>
					<td><input type="checkbox" name="pg1_no22"
						<%= props.getProperty("pg1_no22", "") %>></td>
				</tr>
				<tr>
					<td>23.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c23p1");return false;'>Varicosities/Phlebitis</a></td>
					<td><input type="checkbox" name="pg1_yes23"
						<%= props.getProperty("pg1_yes23", "") %>></td>
					<td><input type="checkbox" name="pg1_no23"
						<%= props.getProperty("pg1_no23", "") %>></td>
				</tr>
				<tr>
					<td>24.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c24p1");return false;'>Psychiatric
					illness</a></td>
					<td><input type="checkbox" name="pg1_yes24"
						<%= props.getProperty("pg1_yes24", "") %>></td>
					<td><input type="checkbox" name="pg1_no24"
						<%= props.getProperty("pg1_no24", "") %>></td>
				</tr>
				<tr>
					<td>25.</td>
					<td>Other</td>
					<td><input type="checkbox" name="pg1_yes25"
						<%= props.getProperty("pg1_yes25", "") %>></td>
					<td><input type="checkbox" name="pg1_no25"
						<%= props.getProperty("pg1_no25", "") %>></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="text" name="pg1_box25" size="15"
						maxlength="25"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box25", "")) %>"></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>26.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c26p1");return false;'>Age&gt;=35
					at EDB</a></td>
					<td width="15%" align="center"><input type="checkbox"
						name="pg1_yes26" <%= props.getProperty("pg1_yes26", "") %>></td>
					<td width="15%" align="center"><input type="checkbox"
						name="pg1_no26" <%= props.getProperty("pg1_no26", "") %>></td>
				</tr>
				<tr>
					<td valign="top">27.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c27p1");return false;'>&quot;At
					risk&quot; population<br>
					<span class="small">(Tay-Sach's, sicke cell,<br>
					thalassemia, etc.)</span></a></td>
					<td align="center" valign="top"><input type="checkbox"
						name="pg1_yes27" <%= props.getProperty("pg1_yes27", "") %>></td>
					<td align="center" valign="top"><input type="checkbox"
						name="pg1_no27" <%= props.getProperty("pg1_no27", "") %>></td>
				</tr>
				<tr>
					<td valign="top">28.</td>
					<td nowrap><a href=#
						onClick='popupPage("<%=resource%>c28p1");return false;'>Known
					teratogen exposure<br>
					<span class="small">(includes maternal diabetes)</span></a></td>
					<td align="center"><input type="checkbox" name="pg1_yes28"
						<%= props.getProperty("pg1_yes28", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no28"
						<%= props.getProperty("pg1_no28", "") %>></td>
				</tr>
				<tr>
					<td>29.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c29p1");return false;'>Previous
					birth defect</a></td>
					<td align="center"><input type="checkbox" name="pg1_yes29"
						<%= props.getProperty("pg1_yes29", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no29"
						<%= props.getProperty("pg1_no29", "") %>></td>
				</tr>
				<tr>
					<td colspan="4"><b>Family history of:</b></td>
				</tr>
				<tr>
					<td>30.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c30p1");return false;'>Neural
					tube defects</a></td>
					<td align="center"><input type="checkbox" name="pg1_yes30"
						<%= props.getProperty("pg1_yes30", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no30"
						<%= props.getProperty("pg1_no30", "") %>></td>
				</tr>
				<tr>
					<td>31.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c31p1");return false;'>Development
					delay</a></td>
					<td align="center"><input type="checkbox" name="pg1_yes31"
						<%= props.getProperty("pg1_yes31", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no31"
						<%= props.getProperty("pg1_no31", "") %>></td>
				</tr>
				<tr>
					<td valign="top">32.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c32p1");return false;'>Congenital
					physical<br>
					anomalies (includes<br>
					congenital heart disease)</a></td>
					<td align="center"><input type="checkbox" name="pg1_yes32"
						<%= props.getProperty("pg1_yes32", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no32"
						<%= props.getProperty("pg1_no32", "") %>></td>
				</tr>
				<tr>
					<td>33.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c33p1");return false;'>Congenital
					hypotonias</a></td>
					<td align="center"><input type="checkbox" name="pg1_yes33"
						<%= props.getProperty("pg1_yes33", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no33"
						<%= props.getProperty("pg1_no33", "") %>></td>
				</tr>
				<tr>
					<td valign="top">34.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c34p1");return false;'>Chromosomal
					disease<br>
					<span class="small">(Down's, Turner's, etc.) </span></a></td>
					<td align="center"><input type="checkbox" name="pg1_yes34"
						<%= props.getProperty("pg1_yes34", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no34"
						<%= props.getProperty("pg1_no34", "") %>></td>
				</tr>
				<tr>
					<td valign="top">35.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c35p1");return false;'>Genetic
					disease<br>
					<span class="small">(cystic fibrosis, muscular<br>
					dystrophy, etc.)</span></a></td>
					<td align="center" valign="top"><input type="checkbox"
						name="pg1_yes35" <%= props.getProperty("pg1_yes35", "") %>></td>
					<td align="center" valign="top"><input type="checkbox"
						name="pg1_no35" <%= props.getProperty("pg1_no35", "") %>></td>
				</tr>
				<tr>
					<td>36.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c36p1");return false;'>Further
					investigations</a></td>
					<td align="center"><input type="checkbox" name="pg1_yes36"
						<%= props.getProperty("pg1_yes36", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no36"
						<%= props.getProperty("pg1_no36", "") %>></td>
				</tr>
				<tr>
					<td>37.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c37p1");return false;'>MSS</a><br>
					</td>
					<td align="center">&nbsp;</td>
					<td align="center">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Offered</td>
					<td align="center"><input type="checkbox" name="pg1_yes37off"
						<%= props.getProperty("pg1_yes37off", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no37off"
						<%= props.getProperty("pg1_no37off", "") %>></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Accepted</td>
					<td align="center"><input type="checkbox" name="pg1_yes37acc"
						<%= props.getProperty("pg1_yes37acc", "") %>></td>
					<td align="center"><input type="checkbox" name="pg1_no37acc"
						<%= props.getProperty("pg1_no37acc", "") %>></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>38.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c38p1");return false;'>STDs/Herpes</a></td>
					<td><input type="checkbox" name="pg1_idt38"
						<%= props.getProperty("pg1_idt38", "") %>></td>
				</tr>
				<tr>
					<td>39.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c39p1");return false;'>HIV</a></td>
					<td><input type="checkbox" name="pg1_idt39"
						<%= props.getProperty("pg1_idt39", "") %>></td>
				</tr>
				<tr>
					<td>40.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c40p1");return false;'>Varicella</a></td>
					<td><input type="checkbox" name="pg1_idt40"
						<%= props.getProperty("pg1_idt40", "") %>></td>
				</tr>
				<tr>
					<td>41.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c41p1");return false;'>Toxo/CMV/Parvo</a></td>
					<td><input type="checkbox" name="pg1_idt41"
						<%= props.getProperty("pg1_idt41", "") %>></td>
				</tr>
				<tr>
					<td>42.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c42p1");return false;'>TB/Other</a>
					<input type="text" name="pg1_box42" size="10" maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box42", "")) %>"></td>
					<td><input type="checkbox" name="pg1_idt42"
						<%= props.getProperty("pg1_idt42", "") %>></td>
				</tr>
				<tr>
					<td colspan="3">
					<hr>
					<b>Psychosocial discussion topics</b></td>
				</tr>
				<tr>
					<td>43.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c43p1");return false;'>Social
					support</a></td>
					<td><input type="checkbox" name="pg1_pdt43"
						<%= props.getProperty("pg1_pdt43", "") %>></td>
				</tr>
				<tr>
					<td>44.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c44p1");return false;'>Couple's
					relationship</a></td>
					<td><input type="checkbox" name="pg1_pdt44"
						<%= props.getProperty("pg1_pdt44", "") %>></td>
				</tr>
				<tr>
					<td>45.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c45p1");return false;'>Emotional/Depression</a></td>
					<td><input type="checkbox" name="pg1_pdt45"
						<%= props.getProperty("pg1_pdt45", "") %>></td>
				</tr>
				<tr>
					<td>46.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c46p1");return false;'>Substance
					abuse</a></td>
					<td><input type="checkbox" name="pg1_pdt46"
						<%= props.getProperty("pg1_pdt46", "") %>></td>
				</tr>
				<tr>
					<td>47.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c47p1");return false;'>Family
					violence</a></td>
					<td><input type="checkbox" name="pg1_pdt47"
						<%= props.getProperty("pg1_pdt47", "") %>></td>
				</tr>
				<tr>
					<td>48.</td>
					<td><a href=#
						onClick='popupPage("<%=resource%>c48p1");return false;'>Parenting
					concerns</a></td>
					<td><input type="checkbox" name="pg1_pdt48"
						<%= props.getProperty("pg1_pdt48", "") %>></td>
				</tr>
				<tr>
					<td colspan="3">
					<hr>
					<b>Risk factors identified</b></td>
				</tr>
				<tr>
					<td colspan="3"><textarea name="c_riskFactors" cols="20"
						rows="5" style="width: 100%"><%= props.getProperty("c_riskFactors", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2">Ht.<input type="text" name="pg1_ht" size="5"
						maxlength="6"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_ht", "")) %>" />
					Wt.<input type="text" name="pg1_wt" size="5" maxlength="6"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_wt", "")) %>" />
					</td>
				</tr>
				<tr>
					<td colspan="2">Pre-preg. wt.<input type="text" name="c_ppWt"
						size="6" maxlength="6"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_ppWt", "")) %>">
					</td>
				</tr>
				<tr>
					<td colspan="2">BP<input type="text" name="pg1_BP" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_BP", "")) %>">
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<hr>
					<b>Checkmark if normal:</b></td>
				</tr>
				<tr>
					<td>Head, teeth, ENT</td>
					<td align="right"><input type="checkbox" name="pg1_head"
						<%= props.getProperty("pg1_head", "") %>></td>
				</tr>
				<tr>
					<td>Thyroid</td>
					<td align="right"><input type="checkbox" name="pg1_thyroid"
						<%= props.getProperty("pg1_thyroid", "") %>></td>
				</tr>
				<tr>
					<td>Chest</td>
					<td align="right"><input type="checkbox" name="pg1_chest"
						<%= props.getProperty("pg1_chest", "") %>></td>
				</tr>
				<tr>
					<td>Breasts</td>
					<td align="right"><input type="checkbox" name="pg1_breasts"
						<%= props.getProperty("pg1_breasts", "") %>></td>
				</tr>
				<tr>
					<td>Cardiovascular</td>
					<td align="right"><input type="checkbox" name="pg1_cardio"
						<%= props.getProperty("pg1_cardio", "") %>></td>
				</tr>
				<tr>
					<td>Abdomen</td>
					<td align="right"><input type="checkbox" name="pg1_abdomen"
						<%= props.getProperty("pg1_abdomen", "") %>></td>
				</tr>
				<tr>
					<td>Varicosities, extremities</td>
					<td align="right"><input type="checkbox" name="pg1_vari"
						<%= props.getProperty("pg1_vari", "") %>></td>
				</tr>
				<tr>
					<td>Neurological</td>
					<td align="right"><input type="checkbox" name="pg1_neuro"
						<%= props.getProperty("pg1_neuro", "") %>></td>
				</tr>
				<tr>
					<td>Pelvic architecture</td>
					<td align="right"><input type="checkbox" name="pg1_pelvic"
						<%= props.getProperty("pg1_pelvic", "") %>></td>
				</tr>
				<tr>
					<td>Ext. genitalia</td>
					<td align="right"><input type="checkbox" name="pg1_extGen"
						<%= props.getProperty("pg1_extGen", "") %>></td>
				</tr>
				<tr>
					<td>Cervix, vagina</td>
					<td align="right"><input type="checkbox" name="pg1_cervix"
						<%= props.getProperty("pg1_cervix", "") %>></td>
				</tr>
				<tr>
					<td nowrap>Uterus <input type="text" name="pg1_uterusBox"
						size="3" maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_uterusBox", "")) %>">
					<span class="small"> (no. of wks.)</span></td>
					<td align="right"><input type="checkbox" name="pg1_uterus"
						<%= props.getProperty("pg1_uterus", "") %>></td>
				</tr>
				<tr>
					<td>Adnexa</td>
					<td align="right"><input type="checkbox" name="pg1_adnexa"
						<%= props.getProperty("pg1_adnexa", "") %>></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr bgcolor="#CCCCCC">
			<td align="center" colspan="2"><b>Comments re Medical
			History and Physical Examination</b></td>
		</tr>
		<tr>
			<td colspan="2"><textarea name="pg1_commentsAR1"
				style="width: 100%" cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td>Signature of attendant<br>
			<input type="text" name="pg1_signature" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_signature", "")) %>">
			</td>
			<td>Date (yyyy/mm/dd)<br>
			<input type="text" name="pg1_formDate" size="30" maxlength="50"
				style="width: 80%"
				value="<%= props.getProperty("pg1_formDate", "") %>"></td>
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
				href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a>
			</td>
			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="javascript: popupPage('formarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;</td>
			<td align="right"><b>Edit:</b> <a
				href="formarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <a
				href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR
			Planner</a></td>
			<%
  }
%>
		</tr>
	</table>

</html:form>
</body>
</html:html>
