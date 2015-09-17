<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

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
	import="oscar.util.*, oscar.form.*, oscar.form.data.*, org.oscarehr.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="org.springframework.context.*,org.springframework.web.context.support.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	String formClass = "DischargeSummary";
	String formLink = "formDischargeSummary.jsp";
	String formLink_printPreview = "formDischargeSummaryPrint.jsp";
	int programNo = Integer.parseInt((String)request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID));
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    //java.util.Properties props = rec.getFormRecord(demoNo, formId);
    java.util.Properties props = rec.getCaisiFormRecord(demoNo, formId, provNo, programNo);
    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

	//get project_home
	String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>MULTI-DISCIPLINARY TEAM DISCHARGE SUMMARY</title>
<link rel="stylesheet" type="text/css" href="arStyle.css">
<html:base />
</head>


<script type="text/javascript" language="Javascript">

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
    function onPrintPreview() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();   
        if(ret) {         	
        	document.forms[0].action = "/<%=project_home%>/form/formname.do" ; 
        	ret = confirm("Are you sure you want to save this form and see the print preview?");         		
        }    
        return ret; 
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret) {
            //reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true) {
            //reset();
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
        if(valDate(document.forms[0].dischargeDate)==false){
            b = false;
        } 

		if(valDate(document.forms[0].pg1_eddByDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_eddByUs)==false){
            b = false;
        } 

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />

	<% if (formId > 0) { %>
	<input type="hidden" name="form_link"
		value="<%=formLink_printPreview%>" />
	<%}else{ %>
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<%} %>

	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
	<input type="hidden" name="submit" value="exit" />

	<!-- 
<table width="100%" class="Head" class="hidePrint">
 -->
	<table width="100%">
		<tr width="100%">
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="button" value="Exit"
				onclick="javascript:return onExit();" /> <%
            String appPath = request.getContextPath();            
            %> <% if (formId > 0) { %> <input type="submit"
				value="Print Preview" onclick="javascript:return onPrintPreview();" />
			<!--  
            <input type="button" value="Print Preview" onclick="location.href='<%= appPath %>/form/formDischargeSummaryPrint.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&user=<%=provNo%>' " />
            
            <a href='<%=appPath %>/form/formDischargeSummaryPrint.jsp' onClick="window.open(this.href,'Discharge Summary Form Print Preview','width=800,height=600,toolbar=no,location=no,directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=no,resizable=yes');return false;">Print Preview</a> 
            --> <% } %>
			</td>
		</tr>
	</table>


	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">Sherbourne Health Centre Infirmary</font></th>
		</tr>
		<tr bgcolor="#486ebd">
			<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">(ph) 416-324-4108</font></th>
		</tr>
		<tr bgcolor="#486ebd">
			<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">(fax) 416-324-4258</font></th>
		</tr>
		<tr>
			<td align="center" bgcolor="#CCCCCC"><b><font
				face="Verdana, Arial, Helvetica, sans-serif">MULTI-DISCIPLINARY
			TEAM DISCHARGE SUMMARY </font></b></td>
		</tr>
	</table>

	<br>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr width="100%">
			<td width="10%" align="left">Client Name:</td>
			<td width="25%"><input type="text" name="clientName"
				readonly="true" style="width: 100%" size="30" maxlength="30"
				value="<%= props.getProperty("clientName", "") %>" /></td>
			<td width="15%" align="right">DOB<small>(yyyy/mm/dd)</small>:</td>
			<td width="15%"><input type="text" name="birthDate"
				readonly="true" style="width: 100%" size="20" maxlength="12"
				value="<%= props.getProperty("birthDate", "") %>" /></td>
			<td width="5%" align="right">OHIP#:</td>
			<td width="30%"><input type="text" name="ohip" readonly="true"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("ohip", "") %>" /></td>
		</tr>

		<tr width="100%">
			<td width="5%" align="left">Admit Date:</td>
			<td width="10%"><input type="text" name="admitDate"
				readonly="true" style="width: 100%" size="10" maxlength="10"
				value="<%= props.getProperty("admitDate", "") %>" /></td>
			<td width="10%" align="right">Discharge Date<small>(yyyy/mm/dd):<small></td>
			<td width="5%"><input type="text" name="dischargeDate"
				style="width: 100%" size="10" maxlength="12"
				value="<%= props.getProperty("dischargeDate", "") %>" /></td>
			<!--  
	 <td width="5%" align="right">Program Name: </td>
	 <td width="25%"><input type="text" name="programName" readonly style="width:100%" value="<%= props.getProperty("programName", "") %>"/></td>
	-->
			<td width="5%" align="right">Allergies:</td>
			<td width="25%"><input type="text" name="allergies" readonly
				style="width: 100%"
				value="<%= props.getProperty("allergies", "") %>" /></td>
		</tr>
		<!--  
	 <tr width="100%">
	 <td align="left">Allergies: </td>
     <td colspan="5">
      <input type="text" name="allergies" readonly="true" style="width:100%" value="<%= props.getProperty("allergies", "") %>"/>
	 </td>
    </tr>
     -->
	</table>
	<br>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3">Admitting Diagnosis/Primary Diagnosis: <textarea
				name="admissionNotes" style="width: 100%" cols="20" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("admissionNotes", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3">Problem List: <textarea name="currentIssues"
				readonly style="width: 100%" cols="20" rows="3" @oscar.formDB
				dbType="text" /><%= props.getProperty("currentIssues", "") %></textarea></td>
		</tr>
		<tr>
			<td colspan="3">Brief Summary of stay (special
			procedures/treatment/complications): <textarea name="briefSummary"
				style="width: 100%" cols="20" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("briefSummary", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3">Discharge Plan of
			Care/Recommendations/Outstanding Issues: <textarea
				name="dischargePlan" style="width: 100%" cols="20" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("dischargePlan", "") %></textarea>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<!--  NOT USED followUpAppointment CHECKBOX any more
	<td colspan="4">Follow-up Appointment(s):  To be arranged by Patient:</td>	
	<td align="right">
	-->
			<%//if(props.getProperty("followUpAppointment","").equals("1")){%>
			<!--  
	<input type="radio" name="followUpAppointment" value="1" checked />
-->
			<%//}else { %>
			<!--  
	<input type="radio" name="followUpAppointment" value="1" />
-->
			<%//} %>
			<!--  
	</td>
	<td>Yes</td>
	<td align="right">
-->
			<% //if(props.getProperty("followUpAppointment","").equals("0")){%>
			<!--
	<input type="radio" name="followUpAppointment" value="0" checked />
-->
			<%//}else { %>
			<!--  
	<input type="radio" name="followUpAppointment" value="0"/>
-->
			<%//} %>
			<!--
	</td>
	<td>No</td>
-->
			<td colspan="4">Follow-up Appointment(s):</td>
		</tr>
		<tr>
			<td align="left">Agency/Health Care Provider</td>
			<td align="left">Phone No</td>
			<td align="left">Date/Time</td>
			<td align="left">Location</td>
		</tr>
		<tr>
			<td align="left"><input type="text" name="doctor1"
				style="width: 100%" size="30" maxlength="30"
				value="<%= props.getProperty("doctor1", "") %>" /></td>
			<td align="left"><input type="text" name="phoneNumber1"
				style="width: 100%" size="25" maxlength="25"
				value="<%= props.getProperty("phoneNumber1", "") %>" /></td>
			<td align="left"><input type="text" name="date1"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("date1", "") %>" /></td>
			<td align="left"><input type="text" name="location1"
				style="width: 100%" size="45" maxlength="45"
				value="<%= props.getProperty("location1", "") %>" /></td>
		</tr>
		<tr>
			<td align="left"><input type="text" name="doctor2"
				style="width: 100%" size="30" maxlength="30"
				value="<%= props.getProperty("doctor2", "") %>" /></td>
			<td align="left"><input type="text" name="phoneNumber2"
				style="width: 100%" size="25" maxlength="25"
				value="<%= props.getProperty("phoneNumber2", "") %>" /></td>
			<td align="left"><input type="text" name="date2"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("date2", "") %>" /></td>
			<td align="left"><input type="text" name="location2"
				style="width: 100%" size="45" maxlength="45"
				value="<%= props.getProperty("location2", "") %>" /></td>
		</tr>
		<tr>
			<td align="left"><input type="text" name="doctor3"
				style="width: 100%" size="30" maxlength="30"
				value="<%= props.getProperty("doctor3", "") %>" /></td>
			<td align="left"><input type="text" name="phoneNumber3"
				style="width: 100%" size="25" maxlength="25"
				value="<%= props.getProperty("phoneNumber3", "") %>" /></td>
			<td align="left"><input type="text" name="date3"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("date3", "") %>" /></td>
			<td align="left"><input type="text" name="location3"
				style="width: 100%" size="35" maxlength="35"
				value="<%= props.getProperty("location3", "") %>" /></td>
		</tr>
		<tr>
			<td align="left"><input type="text" name="doctor4"
				style="width: 100%" size="30" maxlength="30"
				value="<%= props.getProperty("doctor4", "") %>" /></td>
			<td align="left"><input type="text" name="phoneNumber4"
				style="width: 100%" size="25" maxlength="25"
				value="<%= props.getProperty("phoneNumber4", "") %>" /></td>
			<td align="left"><input type="text" name="date4"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("date4", "") %>" /></td>
			<td align="left"><input type="text" name="location4"
				style="width: 100%" size="35" maxlength="35"
				value="<%= props.getProperty("location4", "") %>" /></td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%">Current Medications: <!-- 
		<textarea name="prescriptionSummary" readonly="true" style="width:100%" cols="30" rows="7" @oscar.formDB dbType="text"/><%= props.getProperty("prescriptionSummary", "") %></textarea>
	 --> <textarea name="prescriptionSummary" readonly="true"
				style="width: 100%" cols="30" rows="4" @oscar.formDB dbType="text" />Please see Attached Summary</textarea>
			</td>
			<td width="50%">
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>Prescription Provided:</td>
					<td align="right">
					<%if(props.getProperty("prescriptionProvided","").equals("1")){%> <input
						type="radio" name="prescriptionProvided" value="1" checked /> <%}else { %>
					<input type="radio" name="prescriptionProvided" value="1" /> <%} %>
					</td>
					<td>Yes</td>
					<td align="right">
					<%if(props.getProperty("prescriptionProvided","").equals("0")){%> <input
						type="radio" name="prescriptionProvided" value="0" checked /> <%}else { %>
					<input type="radio" name="prescriptionProvided" value="0" /> <%} %>
					</td>
					<td>No</td>
					<td></td>

				</tr>

				<tr>
					<td>Changes in Medications (include explanation):</td>
					<td align="right">
					<%if(props.getProperty("medicationProvided","").equals("1")){%> <input
						type="radio" name="medicationProvided" value="1" checked /> <%}else { %>
					<input type="radio" name="medicationProvided" value="1" /> <%} %>
					</td>
					<td>Yes</td>
					<td align="right">
					<%if(props.getProperty("medicationProvided","").equals("0")){%> <input
						type="radio" name="medicationProvided" value="0" checked /> <%}else { %>
					<input type="radio" name="medicationProvided" value="0" /> <%} %>
					</td>
					<td>No</td>
					<td></td>

				</tr>
				<tr>
					<td colspan="6">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><textarea name="changeMedications" style="width: 100%"
								cols="30" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("changeMedications", "") %></textarea>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<!--
		<tr>
			<td>ODB Form Required:</td>
			<td align="right">
			<%if(props.getProperty("ODBFormReqired","").equals("1")){%>
			<input type="radio" name="ODBFormReqired" value="1" checked />
			<%}else { %>
			<input type="radio" name="ODBFormReqired" value="1" />
			<%} %>
			</td>
			<td>Yes</td>
			<td align="right">
			<%if(props.getProperty("ODBFormReqired","").equals("0")){%>
			<input type="radio" name="ODBFormReqired" value="0" checked />
			<%}else { %>
			<input type="radio" name="ODBFormReqired" value="0"/>
			<%} %>
			</td>
			<td>No</td>	
			<td align="right">
			<%if(props.getProperty("ODBFormReqired","").equals("2")){%>
			<input type="radio" name="ODBFormReqired" value="2" checked />
			<%}else { %>
			<input type="radio" name="ODBFormReqired" value="2"/>
			<%} %>
			</td>
			<td>Completed</td>					
		</tr>
		 -->
				<!-- 
		<tr>
			<td>Counselling Provided by:</td>
			<td><input type="text" name="counsellorName" style="width:100%" size="26" maxlength="26" value="<%= props.getProperty("counsellorName", "") %>"/><td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>			
		</tr>
		<tr>
			<td>Follow-up Required:</td>
			<td align="right">
	
			<%if(props.getProperty("followUpRequired","").equals("1")){%>
			<input type="radio" name="followUpRequired" value="1" checked />
			<%}else { %>
			<input type="radio" name="followUpRequired" value="1" />
			<%} %>
			</td>
			<td>Yes</td>
			<td align="right">
			<%if(props.getProperty("followUpRequired","").equals("0")){%>
			<input type="radio" name="followUpRequired" value="0" checked />
			<%}else { %>
			<input type="radio" name="followUpRequired" value="0"/>
			<%} %>
			</td>
			<td>No</td>				
			<td></td>
			<td></td>			
		</tr>
		<tr>
			<td>If yes, please specify:</td>
			<td colspan="6"><input type="text" name="followUpRequiredDetail" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("followUpRequiredDetail", "") %>"/><td>						
		</tr>
	-->

			</table>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3"><b>Referrals:</b></td>
		</tr>
		<tr>
			<td>Program</td>
			<td>Referral Made</td>
			<td>Outcome</td>
		<tr>
		<tr>
			<td><input type="text" name="referralProgram1"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralProgram1", "") %>" /></td>
			<td><input type="text" name="referralMade1" style="width: 100%"
				size="255" maxlength="255"
				value="<%= props.getProperty("referralMade1","") %>" /></td>
			<td><input type="text" name="referralOutcome1"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralOutcome1","") %>" /></td>
		</tr>
		<tr>
			<td><input type="text" name="referralProgram2"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralProgram2", "") %>" /></td>
			<td><input type="text" name="referralMade2" style="width: 100%"
				size="255" maxlength="255"
				value="<%= props.getProperty("referralMade2","") %>" /></td>
			<td><input type="text" name="referralOutcome2"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralOutcome2","") %>" /></td>
		</tr>
		<tr>
			<td><input type="text" name="referralProgram3"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralProgram3", "") %>" /></td>
			<td><input type="text" name="referralMade3" style="width: 100%"
				size="255" maxlength="255"
				value="<%= props.getProperty("referralMade3","") %>" /></td>
			<td><input type="text" name="referralOutcome3"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralOutcome3","") %>" /></td>
		</tr>
		<tr>
			<td><input type="text" name="referralProgram4"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralProgram4", "") %>" /></td>
			<td><input type="text" name="referralMade4" style="width: 100%"
				size="255" maxlength="255"
				value="<%= props.getProperty("referralMade4","") %>" /></td>
			<td><input type="text" name="referralOutcome4"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralOutcome4","") %>" /></td>
		</tr>
		<tr>
			<td><input type="text" name="referralProgram5"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralProgram5", "") %>" /></td>
			<td><input type="text" name="referralMade5" style="width: 100%"
				size="255" maxlength="255"
				value="<%= props.getProperty("referralMade5","") %>" /></td>
			<td><input type="text" name="referralOutcome5"
				style="width: 100%" size="255" maxlength="255"
				value="<%= props.getProperty("referralOutcome5","") %>" /></td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="6">Notes: <textarea name="notes"
				style="width: 100%" cols="20" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("notes", "") %></textarea>
			</td>
		</tr>

		<tr>
			<td align="left">Infirmary Health Care Provider:</td>
			<td><input type="text" name="providerName" readonly="true"
				style="width: 100%" size="30" maxlength="30"
				value="<%= props.getProperty("providerName", "") %>" /></td>
			<td>Provider's Signature:</td>
			<td><input type="text" name="signature" size="25" maxlength="25"
				value="<%= props.getProperty("signature", "") %>" @oscar.formDB />
			</td>
			<td>Date(yyyy/mm/dd):</td>
			<td><input type="text" name="signatureDate" size="10"
				maxlength="10" value="<%= props.getProperty("signatureDate", "") %>" />
			</td>
		</tr>


	</table>

	<br>
	<table>
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="button" value="Exit"
				onclick="javascript:return onExit();" /> <% if (formId > 0) { %> <input
				type="submit" value="Print Preview"
				onclick="javascript:return onPrintPreview();" /> <%} %>
			</td>
		</tr>
	</table>


</html:form>
</body>
</html:html>
