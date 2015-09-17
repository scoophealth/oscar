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
	import="java.util.*, oscar.util.UtilDateUtilities, oscar.form.*, oscar.form.data.*, oscar.oscarPrevention.PreventionData,oscar.oscarRx.data.RxPrescriptionData"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
String formClass = "BCHP";
String formLink = "formbchppg2.jsp";

int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
int formId = Integer.parseInt(request.getParameter("formId"));
int provNo = Integer.parseInt((String) session.getAttribute("user"));
FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

props.setProperty("c_lastVisited", "pg2");

//get project_home
String project_home = request.getContextPath().substring(1);

boolean bView = false;
if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 



// if this is a new form automatically fill as much as possible
if (props == null || (props.getProperty("pg2_medName1", "").equals("") && props.getProperty("pg2_fluDate1", "").equals("") && props.getProperty("pg2_otherVac1", "").equals(""))){
//if(true){
    
    // fill the medications
    RxPrescriptionData rxData = new RxPrescriptionData();
    RxPrescriptionData.Prescription[] medications = rxData.getPrescriptionsByPatient(demoNo);
    
    if (medications.length > 13){
        for (int i=14; i<medications.length && i<28; i++){
            String tempName = medications[i].getBrandName();

            if (tempName == null || tempName.equals("") || tempName.equals("null"))
                tempName = medications[i].getCustomName();

            props.setProperty("pg2_medName"+(i-13), tempName);
            props.setProperty("pg2_date"+(i-13),UtilDateUtilities.DateToString( medications[i].getRxDate(), "dd/MM/yyyy"));
            //props.setProperty("pg2_date"+(i-13), medications[i].getRxDate().toString().replaceAll("-", "/"));
            props.setProperty("pg2_dose"+(i-13), medications[i].getDosageDisplay());
            props.setProperty("pg2_howOften"+(i-13), medications[i].getFullFrequency());
        }
    }
    
    // fill the preventions
    ArrayList<Map<String,Object>> vaccines = PreventionData.getPreventionData(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;
    int count4 = 0;
    int count5 = 0;
    int otherCount = 0;
    int fluCount = 0;
    String type = "";
    for (int i=0; i < vaccines.size(); i++){
   		Map<String,Object> vacHash = vaccines.get(i);
        type = (String) vacHash.get("type");
        
        String oldDateFormat = "yyyy-MM-dd";
        String newDateFormat = "dd/MM/yyyy";
        
        Date date = UtilDateUtilities.StringToDate((String) vacHash.get("prevention_date"), oldDateFormat);
        String vacDate = UtilDateUtilities.DateToString(date, newDateFormat);
        
        if (!type.equals("Pneu-C") && !type.equals("Td") && !type.equals("HepA") && !type.equals("HepB") && !type.equals(props.getProperty("pg1_otherVac"))){

            if (type.equals("Flu")){        
                fluCount++;
                if (fluCount > 7)
                    props.setProperty("pg2_fluDate"+(fluCount-7), vacDate);
            }else{
                boolean dateNotSet = true;
                while(dateNotSet){
                    dateNotSet = false;
                    String otherVac1 = props.getProperty("pg2_otherVac1", "");
                    String otherVac2 = props.getProperty("pg2_otherVac2", "");
                    String otherVac3 = props.getProperty("pg2_otherVac3", "");
                    String otherVac4 = props.getProperty("pg2_otherVac4", "");
                    String otherVac5 = props.getProperty("pg2_otherVac5", "");
                    if (type.equals(otherVac1)){
                        count1++;
                        props.setProperty("pg2_other1Date"+count1, vacDate);
                    }else if (type.equals(otherVac2)){
                        count2++;
                        props.setProperty("pg2_other2Date"+count2, vacDate);
                    }else if (type.equals(otherVac3)){
                        count3++;
                        props.setProperty("pg2_other3Date"+count3, vacDate);
                    }else if (type.equals(otherVac4)){
                        count4++;
                        props.setProperty("pg2_other4Date"+count4, vacDate);
                    }else if (type.equals(otherVac5)){
                        count5++;
                        props.setProperty("pg2_other5Date"+count5, vacDate);
                    }else{
                        otherCount++;
                        if (otherCount < 6){
                            props.setProperty("pg2_otherVac"+otherCount, type);
                            dateNotSet = true;
                        }
                    }
                }
            }
        }
    }
}
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
            if(valDate(document.forms[0].pg2_date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date8)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date9)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date10)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date11)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date12)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date13)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_date14)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_fluDate7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other1Date7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other2Date7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other3Date7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other4Date7)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date1)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date2)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date3)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date4)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date5)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date6)==false){
                b = false;
            } else if(valDate(document.forms[0].pg2_other5Date7)==false){
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

<html:form action="/form/formname">
	<input type="hidden" name="c_lastVisited" value="pg2" />
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

			<td align="right"><b>Edit:</b><a
				href="formbchppg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">HP
			<font size=-2>(pg.1)</font></a> | HP <font size=-2>(pg.2)</font></td>
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
			<table width="100%" border="0" cellpadding="0" cellspacing="10">
				<tr>
					</td>
					<td width="96%" style="border-bottom: 1px solid black;"><br />
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
					<td><input type="text" name="pg2_date1" id="pg2_date1"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date1", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date1_cal">
					</td>
					<td><input type="text" name="pg2_medName1" id="pg2_medName1"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName1", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose1" id="pg2_dose1"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose1", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften1" id="pg2_howOften1"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason1" id="pg2_reason1"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason1", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date2" id="pg2_date2"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date2", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date2_cal">
					</td>
					<td><input type="text" name="pg2_medName2" id="pg2_medName2"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName2", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose2" id="pg2_dose2"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose2", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften2" id="pg2_howOften2"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften2", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason2" id="pg2_reason2"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason2", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date3" id="pg2_date3"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date3", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date3_cal">
					</td>
					<td><input type="text" name="pg2_medName3" id="pg2_medName3"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName3", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose3" id="pg2_dose3"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose3", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften3" id="pg2_howOften3"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften3", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason3" id="pg2_reason3"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason3", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date4" id="pg2_date4"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date4", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date4_cal">
					</td>
					<td><input type="text" name="pg2_medName4" id="pg2_medName4"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName4", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose4" id="pg2_dose4"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose4", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften4" id="pg2_howOften4"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften4", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason4" id="pg2_reason4"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason4", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date5" id="pg2_date5"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date5", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date5_cal">
					</td>
					<td><input type="text" name="pg2_medName5" id="pg2_medName5"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName5", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose5" id="pg2_dose5"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose5", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften5" id="pg2_howOften5"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason5" id="pg2_reason5"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason5", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date6" id="pg2_date6"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date6", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date6_cal">
					</td>
					<td><input type="text" name="pg2_medName6" id="pg2_medName6"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName6", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose6" id="pg2_dose6"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose6", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften6" id="pg2_howOften6"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften6", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason6" id="pg2_reason6"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason6", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date7" id="pg2_date7"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date7", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date7_cal">
					</td>
					<td><input type="text" name="pg2_medName7" id="pg2_medName7"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName7", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose7" id="pg2_dose7"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose7", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften7" id="pg2_howOften7"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften7", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason7" id="pg2_reason7"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason7", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date8" id="pg2_date8"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date8", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date8_cal">
					</td>
					<td><input type="text" name="pg2_medName8" id="pg2_medName8"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName8", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose8" id="pg2_dose8"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose8", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften8" id="pg2_howOften8"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften8", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason8" id="pg2_reason8"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason8", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date9" id="pg2_date9"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date9", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date9_cal">
					</td>
					<td><input type="text" name="pg2_medName9" id="pg2_medName9"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName9", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_dose9" id="pg2_dose9"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose9", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften9" id="pg2_howOften9"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften9", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason9" id="pg2_reason9"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason9", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date10" id="pg2_date10"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date10", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date10_cal">
					</td>
					<td><input type="text" name="pg2_medName10" id="pg2_medName10"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName10", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_dose10" id="pg2_dose10"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose10", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften10"
						id="pg2_howOften10" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften10", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason10" id="pg2_reason10"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason10", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date11" id="pg2_date11"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date11", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date11_cal">
					</td>
					<td><input type="text" name="pg2_medName11" id="pg2_medName11"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName11", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_dose11" id="pg2_dose11"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose11", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften11"
						id="pg2_howOften11" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften11", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason11" id="pg2_reason11"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason11", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date12" id="pg2_date12"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date12", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date12_cal">
					</td>
					<td><input type="text" name="pg2_medName12" id="pg2_medName12"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName12", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_dose12" id="pg2_dose12"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose12", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften12"
						id="pg2_howOften12" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften12", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason12" id="pg2_reason12"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason12", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date13" id="pg2_date13"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date13", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date13_cal">
					</td>
					<td><input type="text" name="pg2_medName13" id="pg2_medName13"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName13", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_dose13" id="pg2_dose13"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose13", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften13"
						id="pg2_howOften13" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften13", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason13" id="pg2_reason13"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason13", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_date14" id="pg2_date14"
						style="width: 80%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_date14", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="pg2_date14_cal">
					</td>
					<td><input type="text" name="pg2_medName14" id="pg2_medName14"
						style="width: 100%" size="30" maxlength="30"
						value="<%= props.getProperty("pg2_medName14", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_dose14" id="pg2_dose14"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_dose14", "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="pg2_howOften14"
						id="pg2_howOften14" style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg2_howOften14", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_reason14" id="pg2_reason14"
						style="width: 100%" size="20" maxlength="20"
						value="<%= props.getProperty("pg2_reason14", "") %>" @oscar.formDB />
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="10">
				<tr>
					</td>
					<td width="96%" style="border-bottom: 1px solid black;"><br />
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
					<td><input type="text" name="pg2_fluDate1" id="pg2_fluDate1"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate1", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate1_cal"></td>
					<td><input type="text" name="pg2_fluDate2" id="pg2_fluDate2"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate2", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate2_cal"></td>
					<td><input type="text" name="pg2_fluDate3" id="pg2_fluDate3"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate3", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate3_cal"></td>
					<td><input type="text" name="pg2_fluDate4" id="pg2_fluDate4"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate4", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate4_cal"></td>
					<td><input type="text" name="pg2_fluDate5" id="pg2_fluDate5"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate5", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate5_cal"></td>
					<td><input type="text" name="pg2_fluDate6" id="pg2_fluDate6"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate6", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate6_cal"></td>
					<td><input type="text" name="pg2_fluDate7" id="pg2_fluDate7"
						style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_fluDate7", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="pg2_fluDate7_cal"></td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_otherVac1" id="pg2_otherVac1"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_otherVac1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_other1Date1"
						id="pg2_other1Date1" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date1_cal"></td>
					<td><input type="text" name="pg2_other1Date2"
						id="pg2_other1Date2" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date2_cal"></td>
					<td><input type="text" name="pg2_other1Date3"
						id="pg2_other1Date3" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date3_cal"></td>
					<td><input type="text" name="pg2_other1Date4"
						id="pg2_other1Date4" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date4_cal"></td>
					<td><input type="text" name="pg2_other1Date5"
						id="pg2_other1Date5" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date5_cal"></td>
					<td><input type="text" name="pg2_other1Date6"
						id="pg2_other1Date6" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date6_cal"></td>
					<td><input type="text" name="pg2_other1Date7"
						id="pg2_other1Date7" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other1Date7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other1Date7_cal"></td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_otherVac2" id="pg2_otherVac2"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_otherVac2", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_other2Date1"
						id="pg2_other2Date1" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date1_cal"></td>
					<td><input type="text" name="pg2_other2Date2"
						id="pg2_other2Date2" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date2_cal"></td>
					<td><input type="text" name="pg2_other2Date3"
						id="pg2_other2Date3" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date3_cal"></td>
					<td><input type="text" name="pg2_other2Date4"
						id="pg2_other2Date4" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date4_cal"></td>
					<td><input type="text" name="pg2_other2Date5"
						id="pg2_other2Date5" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date5_cal"></td>
					<td><input type="text" name="pg2_other2Date6"
						id="pg2_other2Date6" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date6_cal"></td>
					<td><input type="text" name="pg2_other2Date7"
						id="pg2_other2Date7" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other2Date7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other2Date7_cal"></td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_otherVac3" id="pg2_otherVac3"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_otherVac3", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_other3Date1"
						id="pg2_other3Date1" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date1_cal"></td>
					<td><input type="text" name="pg2_other3Date2"
						id="pg2_other3Date2" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date2_cal"></td>
					<td><input type="text" name="pg2_other3Date3"
						id="pg2_other3Date3" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date3_cal"></td>
					<td><input type="text" name="pg2_other3Date4"
						id="pg2_other3Date4" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date4_cal"></td>
					<td><input type="text" name="pg2_other3Date5"
						id="pg2_other3Date5" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date5_cal"></td>
					<td><input type="text" name="pg2_other3Date6"
						id="pg2_other3Date6" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date6_cal"></td>
					<td><input type="text" name="pg2_other3Date7"
						id="pg2_other3Date7" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other3Date7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other3Date7_cal"></td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_otherVac4" id="pg2_otherVac4"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_otherVac4", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_other4Date1"
						id="pg2_other4Date1" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date1_cal"></td>
					<td><input type="text" name="pg2_other4Date2"
						id="pg2_other4Date2" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date2_cal"></td>
					<td><input type="text" name="pg2_other4Date3"
						id="pg2_other4Date3" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date3_cal"></td>
					<td><input type="text" name="pg2_other4Date4"
						id="pg2_other4Date4" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date4_cal"></td>
					<td><input type="text" name="pg2_other4Date5"
						id="pg2_other4Date5" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date5_cal"></td>
					<td><input type="text" name="pg2_other4Date6"
						id="pg2_other4Date6" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date6_cal"></td>
					<td><input type="text" name="pg2_other4Date7"
						id="pg2_other4Date7" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other4Date7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other4Date7_cal"></td>
				</tr>
				<tr>
					<td><input type="text" name="pg2_otherVac5" id="pg2_otherVac5"
						style="width: 100%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_otherVac5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg2_other5Date1"
						id="pg2_other5Date1" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date1", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date1_cal"></td>
					<td><input type="text" name="pg2_other5Date2"
						id="pg2_other5Date2" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date2", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date2_cal"></td>
					<td><input type="text" name="pg2_other5Date3"
						id="pg2_other5Date3" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date3", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date3_cal"></td>
					<td><input type="text" name="pg2_other5Date4"
						id="pg2_other5Date4" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date4", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date4_cal"></td>
					<td><input type="text" name="pg2_other5Date5"
						id="pg2_other5Date5" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date5", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date5_cal"></td>
					<td><input type="text" name="pg2_other5Date6"
						id="pg2_other5Date6" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date6", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date6_cal"></td>
					<td><input type="text" name="pg2_other5Date7"
						id="pg2_other5Date7" style="width: 75%" size="10" maxlength="15"
						value="<%= props.getProperty("pg2_other5Date7", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg2_other5Date7_cal"></td>
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
				onclick="javascript: return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
            }
            %> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
        if (!bView) {
        %>

			<td align="right"><b>Edit:</b><a
				href="formbchppg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">HP
			<font size=-2>(pg.1)</font></a> | HP <font size=-2>(pg.2)</font></td>
			<%
        }
        %>
		</tr>
	</table>
</html:form>

</body>

<script type="text/javascript">
    Calendar.setup({ inputField : "pg2_date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date8", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date8_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date9", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date9_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date10", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date10_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date11", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date11_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date12", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date12_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date13", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date13_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_date14", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_date14_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_fluDate7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_fluDate7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other1Date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other1Date7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other2Date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other2Date7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other3Date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other3Date7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other4Date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other4Date7_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date1_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date2_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date3_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date4_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date5_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date6_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "pg2_other5Date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg2_other5Date7_cal", singleClick : true, step : 1 });
</script>

</html:html>
