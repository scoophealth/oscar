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

<%@ page
	import="java.util.*, oscar.util.UtilDateUtilities, oscar.form.*, oscar.form.data.*, oscar.oscarPrevention.PreventionData, oscar.oscarRx.data.RxPrescriptionData"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>


<%
String formClass = "BCHP";
String formLink = "formbchppg1.jsp";

int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
int formId = Integer.parseInt(request.getParameter("formId"));
int provNo = Integer.parseInt((String) session.getAttribute("user"));
FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

//get project_home
String project_home = request.getContextPath().substring(1);

boolean bView = false;
if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 

// if this is a new form automatically fill as much as possible
if (props == null || props.getProperty("c_lastVisited", "").equals("")){
//if (true){
    // fill the medications
    RxPrescriptionData rxData = new RxPrescriptionData();
    RxPrescriptionData.Prescription[] medications = rxData.getPrescriptionsByPatient(demoNo);
    String tempName;
    for (int i=0; i<medications.length && i<14; i++){
        tempName = medications[i].getBrandName();

        if (tempName == null || tempName.equals("") || tempName.equals("null"))
            tempName = medications[i].getCustomName();
            
        props.setProperty("pg1_medName"+(i+1), tempName);
        
        props.setProperty("pg1_date"+(i+1),UtilDateUtilities.DateToString( medications[i].getRxDate(), "dd/MM/yyyy"));
        
        //props.setProperty("pg1_date"+(i+1), medications[i].getRxDate().toString().replaceAll("-", "/"));
        props.setProperty("pg1_dose"+(i+1), medications[i].getDosageDisplay());
        props.setProperty("pg1_howOften"+(i+1), medications[i].getFullFrequency());
    }
    
    
    // fill the vaccines
    ArrayList<Map<String,Object>> vaccines = PreventionData.getPreventionData(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
    int fluCount = 0;
    int pnuCount = 0;
    int tdCount = 0;
    int hepaCount = 0;
    int hepbCount = 0;
    int otherCount = 0;
    String type = "";
    for (int i=0; i < vaccines.size(); i++){
   	 	Map<String,Object> vacHash = vaccines.get(i);
        type = (String) vacHash.get("type");
        
        String oldDateFormat = "yyyy-MM-dd";
        String newDateFormat = "dd/MM/yyyy";
        
        Date date = UtilDateUtilities.StringToDate((String) vacHash.get("prevention_date"), oldDateFormat);
        String vacDate = UtilDateUtilities.DateToString(date, newDateFormat);
        
        if (type.equals("Flu")){        
            fluCount++;
            props.setProperty("pg1_fluDate"+fluCount, vacDate);
        }else if (type.equals("Pneu-C")){
            pnuCount++;
            props.setProperty("pg1_pneumoVaccDate"+pnuCount, vacDate);
        }else if (type.equals("Td")){
            tdCount++;
            props.setProperty("pg1_tdDate"+tdCount, vacDate);
        }else if (type.equals("HepA")){
            hepaCount++;
            props.setProperty("pg1_hepaDate"+hepaCount, vacDate);
        }else if (type.equals("HepB")){
            hepbCount++;
            props.setProperty("pg1_hepbDate"+hepbCount, vacDate);
        }else{
            String otherVac = props.getProperty("pg1_otherVac");
            if (otherVac == null || otherVac.length() == 0){
                props.setProperty("pg1_otherVac", type);
                otherCount++;
                props.setProperty("pg1_otherDate"+otherCount, vacDate);
            }else if (type.equals(otherVac)){
                otherCount++;
                props.setProperty("pg1_otherDate"+otherCount, vacDate);
            }
        }
    }
}

props.setProperty("c_lastVisited", "pg1");

%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>B.C. Health Passport</title>

<link rel="stylesheet" type="text/css"
	href="<%=bView?"bcHpStyleView.css" : "bcHpStyle.css"%>">
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

<script type="text/javascript">
        <!--
        
        var dtCh= "/";
        var minYear=1900;
        var maxYear=9900;
        
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
        
        function reset() {
            document.forms[0].target = "";
            document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
        }
        
        function onPrint() {
            document.forms[0].submit.value="printAll"; 
            var ret = checkAllDates();

            if(ret==true)
            {
                document.forms[0].action = "../form/formname.do?__title=British+Columbia+Health+Passport&__cfgfile=bchpPrintCfgPg1&__cfgfile=bchpPrintCfgPg2&__template=bchp";
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
            //if(ret==true){
              //  reset();
            //}
            
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
        function checkAllDates() {

            var b = true;
            if(valDate(document.forms[0].pg1_formDate)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date8)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date9)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date10)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date11)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date12)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date13)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_date14)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_fluDate7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_pneumoVaccDate7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_tdDate7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepaDate7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_hepbDate7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg1_otherDate7)==false){
                b = false;
            }
            return b;
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
        
        function DaysArray(n) {
            for (var i = 1; i <= n; i++) {
                this[i] = 31
                if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
                if (i==2) {this[i] = 29}
           }
           return this
        }
        
        function daysInFebruary (year){
            // February has 29 days in any year evenly divisible by four,
            // EXCEPT for centurial years which are not also divisible by 400.
            return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
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
        -->
    </script>
<html:base />
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">

<div id="comment1Div" class="comment"
	style="top: 956px; left: 135px; width: 300px; height: 17px;">
<center>1 dose &#64; &ge; 65; 1 Booster &#64; &ge; 5 yrs if
chronic dz* &amp; &uArr; risk</center>
</div>
<div id="comment2Div" class="comment"
	style="top: 981px; left: 135px; width: 300px; height: 17px;">
<center>Repeat every 10 years</center>
</div>
<div id="comment3Div" class="comment"
	style="top: 1006px; left: 135px; width: 300px; height: 17px;">
<center>2 doses* &#64; 6 - 12 months or in combo B vaccine</center>
</div>
<div id="comment4Div" class="comment"
	style="top: 1031px; left: 135px; width: 300px; height: 17px;">
<center>3 doses* &#64; 0, 1 &amp; 6 months</center>
</div>

<html:form action="/form/formname">
	<input type="hidden" name="c_lastVisited" value="pg1" />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="printPageSize" value="PageSize.HALFLETTER" />
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

			<td align="right"><b>Edit:</b>HP <font size=-2>(pg.1)</font> | <a
				href="formbchppg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">HP
			<font size=-2>(pg.2)</font></a></td>
			<%
        }
        %>
		</tr>
	</table>
	<br />
	<table width="90%" border="1" cellspacing="0" cellpadding="0"
		align="center">

		<tr>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr align="left">
					<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
					B.C. Health Passport</th>
				</tr>
			</table>
			<br />
			<table width="100%" border="0" cellpadding="0" cellspacing="2">
				<tr>
					<td width="25%">Patient Name:</td>
					<td><input type="text" name="pg1_patientName"
						id="pg1_patientName" style="width: 100%" size="30" maxlength="40"
						value="<%= props.getProperty("pg1_patientName", "") %>"
						@oscar.formDB /></td>
					<td>Date:</td>
					<td><input type="text" name="pg1_formDate" id="pg1_formDate"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB
						dbType="date" /></td>
				</tr>
				<tr>
					<td>Personal Health Number(PHN):</td>
					<td><input type="text" name="pg1_phn" id="pg1_phn"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_phn", "") %>" @oscar.formDB />
					</td>
					<td>Tel:</td>
					<td><input type="text" name="pg1_phone" id="pg1_phone"
						style="width: 100%" size="15" maxlength="15"
						value="<%= props.getProperty("pg1_phone", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td>Emergency Contact Name:</td>
					<td><input type="text" name="pg1_emergContact"
						id="pg1_emergContact" style="width: 100%" size="30" maxlength="40"
						value="<%= props.getProperty("pg1_emergContact", "") %>"
						@oscar.formDB /></td>
					<td>Tel:</td>
					<td><input type="text" name="pg1_emergContactPhone"
						id="pg1_emergContactPhone" style="width: 100%" size="15"
						maxlength="15"
						value="<%= props.getProperty("pg1_emergContactPhone", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>Allergies:</td>
					<td colspan="3"><input type="text" name="pg1_allergies"
						id="pg1_allergies" style="width: 100%" size="60" maxlength="90"
						value="<%= props.getProperty("pg1_allergies", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2">MD: <input type="text" name="pg1_md"
						id="pg1_md" style="width: 60%" size="30" maxlength="40"
						value="<%= props.getProperty("pg1_md", "") %>" @oscar.formDB />
					MSP for lab cc: <input type="text" name="pg1_msp" id="pg1_msp"
						style="width: 15%" size="10" maxlength="9"
						value="<%= props.getProperty("pg1_msp", "") %>" @oscar.formDB />
					</td>
					<td>Tel:</td>
					<td><input type="text" name="pg1_mdPhone" id="pg1_mdPhone"
						style="width: 100%" size="15" maxlength="15"
						value="<%= props.getProperty("pg1_mdPhone", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td colspan="4">Living Will / Advanced Care Directive:
					&nbsp;&nbsp;Yes <input type="checkbox" name="pg1_livingWillY"
						id="pg1_livingWillY"
						<%= props.getProperty("pg1_livingWillY", "") %> @oscar.formDB
						dbType="tinyint(1)" /> &nbsp;&nbsp;No <input type="checkbox"
						name="pg1_livingWillN" id="pg1_livingWillN"
						<%= props.getProperty("pg1_livingWillN", "") %> @oscar.formDB
						dbType="tinyint(1)" /> &nbsp;&nbsp;(If DNR - enclose copy of
					orders with passport)</td>
				</tr>
			</table>
			<br />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr align="left">
					<th>Medical Conditions</th>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="2">
				<tr>
					<td width="1%"><input type="checkbox" name="pg1_diabetes"
						id="pg1_diabetes"
						<%= props.getProperty("pg1_diabetes", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="24%">Diabetes</td>
					<td width="1%"><input type="checkbox" name="pg1_atrialFib"
						id="pg1_atrialFib"
						<%= props.getProperty("pg1_atrialFib", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="24%">Atrial fibrillation</td>
					<td width="1%"><input type="checkbox" name="pg1_coronary"
						id="pg1_coronary"
						<%= props.getProperty("pg1_coronary", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="34%">Coronary heart disease</td>
					<td width="1%"><input type="checkbox" name="pg1_highBP"
						id="pg1_highBP"
						<%= props.getProperty("pg1_highBP", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="14%">High BP</td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_chf" id="pg1_chf"
						<%= props.getProperty("pg1_chf", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td>CHF</td>
					<td><input type="checkbox" name="pg1_stroke" id="pg1_stroke"
						<%= props.getProperty("pg1_stroke", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td>Stroke</td>
					<td><input type="checkbox" name="pg1_kidneyDisease"
						id="pg1_kidneyDisease"
						<%= props.getProperty("pg1_kidneyDisease", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td>Kidney disease / low GFR <input type="text"
						name="pg1_lowGFR" id="pg1_lowGFR" size="5" maxlength="10"
						value="<%= props.getProperty("pg1_lowGFR", "") %>" @oscar.formDB />
					</td>
					<td><input type="checkbox" name="pg1_asthma" id="pg1_asthma"
						<%= props.getProperty("pg1_asthma", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td>Asthma</td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_copd" id="pg1_copd"
						<%= props.getProperty("pg1_copd", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td>COPD</td>
					<td><input type="checkbox" name="pg1_co2retainer"
						id="pg1_co2retainer"
						<%= props.getProperty("pg1_co2retainer", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td>CO2 retainer</td>
					<td><input type="checkbox" name="pg1_cancer" id="pg1_cancer"
						<%= props.getProperty("pg1_cancer", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td colspan="3">Cancer <input type="text"
						name="pg1_cancerSpec" id="pg1_cancerSpec" size="48" maxlength="40"
						value="<%= props.getProperty("pg1_cancerSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_other" id="pg1_other"
						<%= props.getProperty("pg1_other", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td colspan="7">Other <input type="text" name="pg1_otherSpec"
						id="pg1_otherSpec" size="108" maxlength="90"
						value="<%= props.getProperty("pg1_otherSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_majorSurg"
						id="pg1_majorSurg"
						<%= props.getProperty("pg1_majorSurg", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td colspan="7">Major surg.&nbsp; <input type="text"
						name="pg1_majorSurgSpec" id="pg1_majorSurgSpec" size="103"
						maxlength="80"
						value="<%= props.getProperty("pg1_majorSurgSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="10">
				<tr>
					</td>
					<td width="96%" style="border-bottom: 1px solid black;"><br />
					</td>
				</tr>
				<tr>
					<td><br />
					</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<th>Date</th>
					<th>Medication name</th>
					<th>Dose</th>
					<th>How often</th>
					<th>Reason</th>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date1" id="pg1_date1"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date1", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date1_cal">
					</td>
					<td><input type="text" name="pg1_medName1" id="pg1_medName1"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName1", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose1" id="pg1_dose1"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose1", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften1" id="pg1_howOften1"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason1" id="pg1_reason1"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason1", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date2" id="pg1_date2"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date2", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date2_cal">
					</td>
					<td><input type="text" name="pg1_medName2" id="pg1_medName2"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName2", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose2" id="pg1_dose2"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose2", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften2" id="pg1_howOften2"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften2", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason2" id="pg1_reason2"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason2", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date3" id="pg1_date3"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date3", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date3_cal">
					</td>
					<td><input type="text" name="pg1_medName3" id="pg1_medName3"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName3", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose3" id="pg1_dose3"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose3", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften3" id="pg1_howOften3"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften3", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason3" id="pg1_reason3"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason3", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date4" id="pg1_date4"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date4", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date4_cal">
					</td>
					<td><input type="text" name="pg1_medName4" id="pg1_medName4"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName4", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose4" id="pg1_dose4"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose4", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften4" id="pg1_howOften4"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften4", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason4" id="pg1_reason4"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason4", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date5" id="pg1_date5"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date5", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date5_cal">
					</td>
					<td><input type="text" name="pg1_medName5" id="pg1_medName5"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName5", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose5" id="pg1_dose5"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose5", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften5" id="pg1_howOften5"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason5" id="pg1_reason5"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason5", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date6" id="pg1_date6"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date6", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date6_cal">
					</td>
					<td><input type="text" name="pg1_medName6" id="pg1_medName6"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName6", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose6" id="pg1_dose6"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose6", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften6" id="pg1_howOften6"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften6", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason6" id="pg1_reason6"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason6", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date7" id="pg1_date7"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date7", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date7_cal">
					</td>
					<td><input type="text" name="pg1_medName7" id="pg1_medName7"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName7", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose7" id="pg1_dose7"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose7", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften7" id="pg1_howOften7"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften7", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason7" id="pg1_reason7"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason7", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date8" id="pg1_date8"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date8", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date8_cal">
					</td>
					<td><input type="text" name="pg1_medName8" id="pg1_medName8"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName8", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose8" id="pg1_dose8"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose8", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften8" id="pg1_howOften8"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften8", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason8" id="pg1_reason8"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason8", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date9" id="pg1_date9"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date9", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date9_cal">
					</td>
					<td><input type="text" name="pg1_medName9" id="pg1_medName9"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName9", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_dose9" id="pg1_dose9"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose9", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften9" id="pg1_howOften9"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften9", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason9" id="pg1_reason9"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason9", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date10" id="pg1_date10"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date10", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date10_cal">
					</td>
					<td><input type="text" name="pg1_medName10" id="pg1_medName10"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName10", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_dose10" id="pg1_dose10"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose10", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften10"
						id="pg1_howOften10" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften10", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason10" id="pg1_reason10"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason10", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date11" id="pg1_date11"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date11", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date11_cal">
					</td>
					<td><input type="text" name="pg1_medName11" id="pg1_medName11"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName11", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_dose11" id="pg1_dose11"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose11", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften11"
						id="pg1_howOften11" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften11", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason11" id="pg1_reason11"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason11", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date12" id="pg1_date12"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date12", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date12_cal">
					</td>
					<td><input type="text" name="pg1_medName12" id="pg1_medName12"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName12", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_dose12" id="pg1_dose12"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose12", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften12"
						id="pg1_howOften12" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften12", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason12" id="pg1_reason12"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason12", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date13" id="pg1_date13"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date13", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date13_cal">
					</td>
					<td><input type="text" name="pg1_medName13" id="pg1_medName13"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName13", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_dose13" id="pg1_dose13"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose13", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften13"
						id="pg1_howOften13" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften13", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason13" id="pg1_reason13"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason13", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_date14" id="pg1_date14"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_date14", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_date14_cal">
					</td>
					<td><input type="text" name="pg1_medName14" id="pg1_medName14"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg1_medName14", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_dose14" id="pg1_dose14"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dose14", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg1_howOften14"
						id="pg1_howOften14" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_howOften14", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_reason14" id="pg1_reason14"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg1_reason14", "") %>" @oscar.formDB />
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="10">
				<tr>
					</td>
					<td width="96%" style="border-bottom: 1px solid black;"><br />
					</td>
				</tr>
				<tr>
					<td><br />
					</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<th width="10%">Vaccines</th>
					<th width="13%">Date</th>
					<th width="13%">Date</th>
					<th width="13%">Date</th>
					<th width="13%">Date</th>
					<th width="13%">Date</th>
					<th width="13%">Date</th>
					<th width="13%">Date</th>
				</tr>
				<tr>
					<td>Flu</td>
					<td><input type="text" name="pg1_fluDate1" id="pg1_fluDate1"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate1", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate1_cal"></td>
					<td><input type="text" name="pg1_fluDate2" id="pg1_fluDate2"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate2", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate2_cal"></td>
					<td><input type="text" name="pg1_fluDate3" id="pg1_fluDate3"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate3", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate3_cal"></td>
					<td><input type="text" name="pg1_fluDate4" id="pg1_fluDate4"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate4", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate4_cal"></td>
					<td><input type="text" name="pg1_fluDate5" id="pg1_fluDate5"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate5", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate5_cal"></td>
					<td><input type="text" name="pg1_fluDate6" id="pg1_fluDate6"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate6", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate6_cal"></td>
					<td><input type="text" name="pg1_fluDate7" id="pg1_fluDate7"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_fluDate7", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg1_fluDate7_cal"></td>
				</tr>
				<tr onmouseover="showHideBox('comment1Div',1)"
					onmouseout="showHideBox('comment1Div',0)">
					<td>Pneumo vacc</td>
					<td><input type="text" name="pg1_pneumoVaccDate1"
						id="pg1_pneumoVaccDate1" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate1_cal"></td>
					<td><input type="text" name="pg1_pneumoVaccDate2"
						id="pg1_pneumoVaccDate2" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate2_cal"></td>
					<td><input type="text" name="pg1_pneumoVaccDate3"
						id="pg1_pneumoVaccDate3" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate3_cal"></td>
					<td><input type="text" name="pg1_pneumoVaccDate4"
						id="pg1_pneumoVaccDate4" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate4_cal"></td>
					<td><input type="text" name="pg1_pneumoVaccDate5"
						id="pg1_pneumoVaccDate5" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate5_cal"></td>
					<td><input type="text" name="pg1_pneumoVaccDate6"
						id="pg1_pneumoVaccDate6" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate6_cal"></td>
					<td><input type="text" name="pg1_pneumoVaccDate7"
						id="pg1_pneumoVaccDate7" style="width: 75%" size="10"
						maxlength="15"
						value="<%= props.getProperty("pg1_pneumoVaccDate7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_pneumoVaccDate7_cal"></td>
				</tr>
				<tr onmouseover="showHideBox('comment2Div',1)"
					onmouseout="showHideBox('comment2Div',0)">
					<td>Td</td>
					<td><input type="text" name="pg1_tdDate1" id="pg1_tdDate1"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate1", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate1_cal">
					</td>
					<td><input type="text" name="pg1_tdDate2" id="pg1_tdDate2"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate2", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate2_cal">
					</td>
					<td><input type="text" name="pg1_tdDate3" id="pg1_tdDate3"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate3", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate3_cal">
					</td>
					<td><input type="text" name="pg1_tdDate4" id="pg1_tdDate4"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate4", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate4_cal">
					</td>
					<td><input type="text" name="pg1_tdDate5" id="pg1_tdDate5"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate5", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate5_cal">
					</td>
					<td><input type="text" name="pg1_tdDate6" id="pg1_tdDate6"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate6", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate6_cal">
					</td>
					<td><input type="text" name="pg1_tdDate7" id="pg1_tdDate7"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_tdDate7", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg1_tdDate7_cal">
					</td>
				</tr>
				<tr onmouseover="showHideBox('comment3Div',1)"
					onmouseout="showHideBox('comment3Div',0)">
					<td>Hep A</td>
					<td><input type="text" name="pg1_hepaDate1" id="pg1_hepaDate1"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate1_cal"></td>
					<td><input type="text" name="pg1_hepaDate2" id="pg1_hepaDate2"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate2_cal"></td>
					<td><input type="text" name="pg1_hepaDate3" id="pg1_hepaDate3"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate3_cal"></td>
					<td><input type="text" name="pg1_hepaDate4" id="pg1_hepaDate4"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate4_cal"></td>
					<td><input type="text" name="pg1_hepaDate5" id="pg1_hepaDate5"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate5_cal"></td>
					<td><input type="text" name="pg1_hepaDate6" id="pg1_hepaDate6"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate6_cal"></td>
					<td><input type="text" name="pg1_hepaDate7" id="pg1_hepaDate7"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepaDate7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepaDate7_cal"></td>
				</tr>
				<tr onmouseover="showHideBox('comment4Div',1)"
					onmouseout="showHideBox('comment4Div',0)">
					<td>Hep B</td>
					<td><input type="text" name="pg1_hepbDate1" id="pg1_hepbDate1"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate1_cal"></td>
					<td><input type="text" name="pg1_hepbDate2" id="pg1_hepbDate2"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate2_cal"></td>
					<td><input type="text" name="pg1_hepbDate3" id="pg1_hepbDate3"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate3_cal"></td>
					<td><input type="text" name="pg1_hepbDate4" id="pg1_hepbDate4"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate4_cal"></td>
					<td><input type="text" name="pg1_hepbDate5" id="pg1_hepbDate5"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate5_cal"></td>
					<td><input type="text" name="pg1_hepbDate6" id="pg1_hepbDate6"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate6_cal"></td>
					<td><input type="text" name="pg1_hepbDate7" id="pg1_hepbDate7"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_hepbDate7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_hepbDate7_cal"></td>
				</tr>
				<tr>
					<td><input type="text" name="pg1_otherVac" id="pg1_otherVac"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherVac", "") %>" @oscar.formDB /></td>
					<td><input type="text" name="pg1_otherDate1"
						id="pg1_otherDate1" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate1_cal"></td>
					<td><input type="text" name="pg1_otherDate2"
						id="pg1_otherDate2" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate2_cal"></td>
					<td><input type="text" name="pg1_otherDate3"
						id="pg1_otherDate3" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate3_cal"></td>
					<td><input type="text" name="pg1_otherDate4"
						id="pg1_otherDate4" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate4_cal"></td>
					<td><input type="text" name="pg1_otherDate5"
						id="pg1_otherDate5" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate5_cal"></td>
					<td><input type="text" name="pg1_otherDate6"
						id="pg1_otherDate6" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate6_cal"></td>
					<td><input type="text" name="pg1_otherDate7"
						id="pg1_otherDate7" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg1_otherDate7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_otherDate7_cal"></td>
				</tr>
			</table>
			<table align="right">
				<tr>
					<td>* verify with health unit which chronic dz covered for
					free vaccine &amp;/or booster</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	<br />
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
			<td><!--a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a-->
			</td>
			<td align="right"><b>Edit:</b>HP <font size=-2>(pg.1)</font> | <a
				href="formbchppg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">HP
			<font size=-2>(pg.2)</font></a></td>
			<%
        }
        %>
		</tr>
	</table>

</html:form>
</body>

<script type="text/javascript">
    Calendar.setup({ inputField : "pg1_date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date8", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date8_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date9", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date9_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date10", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date10_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date11", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date11_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date12", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date12_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date13", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date13_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_date14", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_date14_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_fluDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_fluDate7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_pneumoVaccDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_pneumoVaccDate7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_tdDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_tdDate7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepaDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepaDate7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_hepbDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_hepbDate7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg1_otherDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_otherDate7_cal", singleClick : true, step : 1 });
</script>

</html:html>
