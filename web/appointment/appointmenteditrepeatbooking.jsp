<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF", tableTitle = "#99ccff";
  boolean bEdit = request.getParameter("appointment_no") != null ? true : false;
%>
<%@ page import="java.util.*, oscar.*, oscar.util.*, java.sql.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%!
  GregorianCalendar addDateByYMD(GregorianCalendar cal, String unit, int n) {
	if (unit.equals("day")) {
		cal.add(Calendar.DATE, n);
	} else if (unit.equals("month")) {
		cal.add(Calendar.MONTH, n);
	} else if (unit.equals("year")) {
		cal.add(Calendar.YEAR, n);
	} 
	return cal;
  }
%>
<%
  if (request.getParameter("groupappt") != null) {
    boolean bSucc = false;
	String createdDateTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(),"yyyy-MM-dd HH:mm:ss");
	String userName =  (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");
	String everyNum = request.getParameter("everyNum")!=null? request.getParameter("everyNum") : "0";
	String everyUnit = request.getParameter("everyUnit")!=null? request.getParameter("everyUnit") : "day";
	String endDate = request.getParameter("endDate")!=null? request.getParameter("endDate") : UtilDateUtilities.DateToString(UtilDateUtilities.now(),"dd/MM/yyyy");
	int delta = Integer.parseInt(everyNum);
	if (everyUnit.equals("week") ) {
		delta = delta*7;
		everyUnit = "day";
	}
	GregorianCalendar gCalDate = new GregorianCalendar();
	GregorianCalendar gEndDate = (GregorianCalendar) gCalDate.clone();
	gEndDate.setTime(UtilDateUtilities.StringToDate(endDate, "dd/MM/yyyy"));

    // repeat adding
    if (request.getParameter("groupappt").equals("Add Group Appointment") ) {
        String[] param = new String[17];
        int rowsAffected=0, datano=0;

 	    param[0]=request.getParameter("provider_no");
        param[1]=request.getParameter("appointment_date");
	    param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
        param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
        param[4]=request.getParameter("keyword");
        param[5]=request.getParameter("notes");
        param[6]=request.getParameter("reason");
	    param[7]=request.getParameter("location");
        param[8]=request.getParameter("resources");
        param[9]=request.getParameter("type");
	    param[10]=request.getParameter("style");
        param[11]=request.getParameter("billing");
        param[12]=request.getParameter("status");
 	    param[13]=createdDateTime;   //request.getParameter("createdatetime");
        param[14]=userName;  //request.getParameter("creator");
	    param[15]=request.getParameter("remarks");
  	    if (request.getParameter("demographic_no")!=null && !(request.getParameter("demographic_no").equals(""))) {
			param[16]=request.getParameter("demographic_no");
	    } else param[16]="0";

		while (true) {
			rowsAffected = oscarSuperManager.update("appointmentDao", "add_apptrecord", param);
            if (rowsAffected != 1) break;

			gCalDate.setTime(UtilDateUtilities.StringToDate(param[1], "yyyy-MM-dd"));
			gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

			if (gCalDate.after(gEndDate)) break;
			else param[1] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");
		}
        if (rowsAffected == 1) bSucc = true;
	}


    // repeat updating
    if (request.getParameter("groupappt").equals("Group Update") || request.getParameter("groupappt").equals("Group Cancel") ||
    		request.getParameter("groupappt").equals("Group Delete")) {
        int rowsAffected=0, datano=0;

        Object[] paramE = new Object[10];
		List<Map> resultList = oscarSuperManager.find("appointmentDao", "search", new Object [] {request.getParameter("appointment_no")});
		if (resultList.size() > 0) { 
			Map appt = resultList.get(0);
	        paramE[0]=String.valueOf(appt.get("appointment_date")); //request.getParameter("appointment_date"); // param[3] - appointment_date
     	    paramE[1]=appt.get("provider_no"); //request.getParameter("provider_no");
    	    paramE[2]=appt.get("start_time"); //MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	        paramE[3]=appt.get("end_time"); //MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
            paramE[4]=appt.get("name"); //request.getParameter("keyword");
	        paramE[5]=appt.get("notes"); //request.getParameter("notes");
	        paramE[6]=appt.get("reason"); //request.getParameter("reason");
     	    paramE[7]=appt.get("createdatetime"); //request.getParameter("createdatetime");
	        paramE[8]=appt.get("creator"); //request.getParameter("creator");
	        paramE[9]=appt.get("demographic_no"); //request.getParameter("creator");
		}

        // group cancel
        if (request.getParameter("groupappt").equals("Group Cancel")) {
        	Object[] param = new Object[13];
            param[0]="C";
            param[1]=createdDateTime;
 	        param[2]=userName;
 	        for (int k=0; k<paramE.length; k++) param[k+3] = paramE[k];

			// repeat doing
			while (true) {
				rowsAffected = oscarSuperManager.update("appointmentDao", "cancel_appt", param);

				gCalDate.setTime(UtilDateUtilities.StringToDate((String)param[3], "yyyy-MM-dd"));
				gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

				if (gCalDate.after(gEndDate)) break;
				else param[3] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");
			}
        	bSucc = true;
		}

		// group delete
		if (request.getParameter("groupappt").equals("Group Delete")) {
			Object[] param = new Object[10];
 	        for(int k=0; k<paramE.length; k++) param[k] = paramE[k];

			// repeat doing
			while (true) {
				rowsAffected = oscarSuperManager.update("appointmentDao", "delete_appt", param);

				gCalDate.setTime(UtilDateUtilities.StringToDate((String)param[0], "yyyy-MM-dd"));
				gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

				if (gCalDate.after(gEndDate)) break;
				else param[0] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");
			}
        	bSucc = true;
		}

		if (request.getParameter("groupappt").equals("Group Update")) {
			Object[] param = new Object[20];
    	    param[0]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	        param[1]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
		    param[2]=request.getParameter("keyword");
			param[3]=request.getParameter("demographic_no");
			param[4]=request.getParameter("notes");
	        param[5]=request.getParameter("reason");
		    param[6]=request.getParameter("location");
		    param[7]=request.getParameter("resources");
 			param[8]=createdDateTime;   
	        param[9]=userName;  
 	        for(int k=0; k<paramE.length; k++) param[k+10] = paramE[k];

			// repeat doing
			while (true) {
				rowsAffected = oscarSuperManager.update("appointmentDao", "update_appt", param);

				gCalDate.setTime(UtilDateUtilities.StringToDate((String)param[10], "yyyy-MM-dd"));
				gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

				if (gCalDate.after(gEndDate)) break;
				else param[10] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");
			}
        	bSucc = true;
		}
	}

    if (bSucc) {
%>
<h1><bean:message
	key="appointment.appointmentgrouprecords.msgAddSuccess" /></h1>
<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script>
<%
    } else {
%>
<p>
<h1><bean:message
	key="appointment.appointmentgrouprecords.msgAddFailure" /></h1>
</p>
<%  
    }
    return;
  }
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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="appointment.appointmentgrouprecords.title" /></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--

function onCheck(a, b) {
    if (a.checked) {
		document.getElementById("everyUnit").value = b;
		//document.groupappt.everyUnit.value = b;
    }
}


function onExit() {
    if (confirm("<bean:message key="appointment.appointmentgrouprecords.msgExitConfirmation"/>")) {
        window.close()
	}
}

var saveTemp=0;
function onButDelete() {
  saveTemp=1;
}
function onSub() {
  if( saveTemp==1 ) {
    return (confirm("<bean:message key="appointment.appointmentgrouprecords.msgDeleteConfirmation"/>")) ; 
  } 
}
//-->
</SCRIPT>
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

<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<form name="groupappt" method="POST"
	action="appointmenteditrepeatbooking.jsp" onSubmit="return ( onSub());">
<INPUT TYPE="hidden" NAME="groupappt" value="">
<table width="100%" BGCOLOR="silver">
	<tr>
		<TD>
		<%    if (bEdit) {    %> <INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Update'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupUpdate"/>">
		<INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Cancel'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupCancel"/>">
		<INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Delete'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupDelete"/>"
			onClick="onButDelete()"> <%    } else {    %> <INPUT
			TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Add Group Appointment'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnAddGroupAppt"/>">
		<%    }    %>
		</TD>
		<TD align="right"><INPUT TYPE="button"
			VALUE=" <bean:message key="global.btnBack"/> "
			onClick="window.history.go(-1);return false;"> <INPUT
			TYPE="button" VALUE=" <bean:message key="global.btnExit"/> "
			onClick="onExit()"></TD>
	</tr>
</table>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica">Repeat Booking</font></th>
	</tr>
</table>

<table border="0" cellspacing="1" cellpadding="2" width="100%">
	<tr>
		<td width="20%"></td>
		<td nowrap>How often?</td>
	</tr>
	<tr>
		<td></td>
		<td nowrap>&nbsp;&nbsp;&nbsp; <input type="radio" name="dateUnit"
			value="day" <%="checked"%> onclick='onCheck(this, "day")'>
		Day &nbsp;&nbsp; <input type="radio" name="dateUnit" value="week"
			<%=""%> onclick='onCheck(this, "week")'> Week &nbsp;&nbsp; <input
			type="radio" name="dateUnit" value="month" <%=""%>
			onclick='onCheck(this, "month")'> Month &nbsp;&nbsp; <input
			type="radio" name="dateUnit" value="year" <%=""%>
			onclick='onCheck(this, "year")'> Year</td>
	</tr>
</table>

<table border="0" cellspacing="1" cellpadding="2" width="100%">
	<tr>
		<td width="20%"></td>
		<td width="16%" nowrap>Every</td>
		<td nowrap><select name="everyNum">
			<%	
for (int i = 1; i < 12; i++) {
%>
			<option value="<%=i%>"><%=i%></option>
			<%
}
%>
		</select> <input type="text" name="everyUnit" id="everyUnit" size="10"
			value="<%="day"%>" readonly></td>
	</tr>
	<tr>
		<td></td>
		<td>End on &nbsp;&nbsp;
		<button type="button" id="f_trigger_b">...</button>
		<br>
		<font size="-1">(dd/mm/yyyy)</font></td>
		<td nowrap valign="top"><input type="text" name="endDate"
			id="endDate" size="10"
			value="<%=UtilDateUtilities.DateToString(UtilDateUtilities.now(),"dd/MM/yyyy")%>"
			readonly></td>
	</tr>
</table>
<%
String temp = null;
for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	temp=e.nextElement().toString();
	if(temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")) continue;
	out.println("<input type='hidden' name='"+temp+"' value=\"" + UtilMisc.htmlEscape(request.getParameter(temp)) + "\">");
}
%>
</form>

<script type="text/javascript">
    Calendar.setup({
        inputField     :    "endDate",      // id of the input field
        ifFormat       :    "%d/%m/%Y",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "f_trigger_b",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
</script>

</body>
</html:html>
