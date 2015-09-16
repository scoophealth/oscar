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
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment" rights="u" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_appointment");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF", tableTitle = "#99ccff";
  boolean bEdit = request.getParameter("appointment_no") != null ? true : false;
%>
<%@ page import="java.util.*, oscar.*, oscar.util.*, java.sql.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
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
	String createdDateTime = UtilDateUtilities.DateToString(new java.util.Date(),"yyyy-MM-dd HH:mm:ss");
	String userName =  (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");
	String everyNum = request.getParameter("everyNum")!=null? request.getParameter("everyNum") : "0";
	String everyUnit = request.getParameter("everyUnit")!=null? request.getParameter("everyUnit") : "day";
	String endDate = request.getParameter("endDate")!=null? request.getParameter("endDate") : UtilDateUtilities.DateToString(new java.util.Date(),"dd/MM/yyyy");
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
        String[] param = new String[19];
        int rowsAffected=0, datano=0;

  	    java.util.Date iDate = ConversionUtils.fromDateString(request.getParameter("appointment_date"));
  	        
		while (true) {
			Appointment a = new Appointment();
			a.setProviderNo(request.getParameter("provider_no"));
			a.setAppointmentDate(iDate);
			a.setStartTime(ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")));
			a.setEndTime(ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")));
			a.setName(request.getParameter("keyword"));
			a.setNotes(request.getParameter("notes"));
			a.setReason(request.getParameter("reason"));
			a.setLocation(request.getParameter("location"));
			a.setResources(request.getParameter("resources"));
			a.setType(request.getParameter("type"));
			a.setStyle(request.getParameter("style"));
			a.setBilling(request.getParameter("billing"));
			a.setStatus(request.getParameter("status"));
			a.setCreateDateTime(new java.util.Date());
			a.setCreator(userName);
			a.setRemarks(request.getParameter("remarks"));
			if (request.getParameter("demographic_no")!=null && !(request.getParameter("demographic_no").equals(""))) {
				a.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
		    } else {
		    	a.setDemographicNo(0);
		    }
			
			a.setProgramId(Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
			a.setUrgency(request.getParameter("urgency"));
			a.setReasonCode(Integer.parseInt(request.getParameter("reasonCode")));
			appointmentDao.persist(a);
			

			gCalDate.setTime(UtilDateUtilities.StringToDate(param[1], "yyyy-MM-dd"));
			gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

			if (gCalDate.after(gEndDate)) 
				break;
			else 
				iDate = gCalDate.getTime();
		}
        bSucc = true;
	}


    // repeat updating
    if (request.getParameter("groupappt").equals("Group Update") || request.getParameter("groupappt").equals("Group Cancel") ||
    		request.getParameter("groupappt").equals("Group Delete")) {
        int rowsAffected=0, datano=0;

        Object[] paramE = new Object[10];
        Appointment aa = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
        if (aa != null) {
                paramE[0]=ConversionUtils.toDateString(aa.getAppointmentDate());
                paramE[1]=aa.getProviderNo();
                paramE[2]=ConversionUtils.toTimeStringNoSeconds(aa.getStartTime());
                paramE[3]=ConversionUtils.toTimeStringNoSeconds(aa.getEndTime());
                paramE[4]=aa.getName();
                paramE[5]=aa.getNotes();
                paramE[6]=aa.getReason();
                paramE[7]=ConversionUtils.toTimestampString(aa.getCreateDateTime());
                paramE[8]=aa.getCreator();
                paramE[9]=String.valueOf(aa.getDemographicNo());

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
				Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
			    appointmentArchiveDao.archiveAppointment(appt);
			    
			    List<Appointment> appts = appointmentDao.find(dayFormatter.parse((String)param[3]), (String)param[4], ConversionUtils.fromTimeStringNoSeconds((String)param[5]), ConversionUtils.fromTimeStringNoSeconds((String)param[6]),
						(String)param[7], (String)param[8], (String)param[9], ConversionUtils.fromTimestampString((String)param[10]), (String)param[11], Integer.parseInt((String)param[12]));
			    
            	for(Appointment a:appts) {
            		a.setStatus("C");
            		a.setUpdateDateTime(ConversionUtils.fromTimestampString(createdDateTime));
            		a.setLastUpdateUser(userName);
            		appointmentDao.merge(a);
            		rowsAffected++;
            	}
				
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

				List<Appointment> appts = appointmentDao.find(dayFormatter.parse((String)param[0]), (String)param[1], ConversionUtils.fromTimeStringNoSeconds((String)param[2]), ConversionUtils.fromTimeStringNoSeconds((String)param[3]),
						(String)param[4], (String)param[5], (String)param[6],  ConversionUtils.fromTimestampString((String)param[7]), (String)param[8], Integer.parseInt((String)param[9]));
				for(Appointment appt:appts) {
					appointmentArchiveDao.archiveAppointment(appt);
					appointmentDao.remove(appt.getId());
					rowsAffected++;
				}
				
				gCalDate.setTime(UtilDateUtilities.StringToDate((String)param[0], "yyyy-MM-dd"));
				gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

				if (gCalDate.after(gEndDate)) break;
				else param[0] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");
			}
        	bSucc = true;
		}

		if (request.getParameter("groupappt").equals("Group Update")) {
			Object[] param = new Object[22];
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
            param[10]=request.getParameter("urgency");
            param[11]=request.getParameter("reasonCode");
 	        for(int k=0; k<paramE.length; k++) 
 	        	param[k+12] = paramE[k];

			// repeat doing
			while (true) {
				List<Appointment> appts = appointmentDao.find(dayFormatter.parse((String)paramE[0]), (String)paramE[1], ConversionUtils.fromTimeStringNoSeconds((String)paramE[2]), ConversionUtils.fromTimeStringNoSeconds((String)paramE[3]),
						(String)paramE[4], (String)paramE[5], (String)paramE[6],  ConversionUtils.fromTimestampString((String)paramE[7]), (String)paramE[8], Integer.parseInt((String)paramE[9]));
				for(Appointment appt:appts) {
					appointmentArchiveDao.archiveAppointment(appt);
					appt.setStartTime(ConversionUtils.fromTimeString(MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"))));
					appt.setEndTime(ConversionUtils.fromTimeString(MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"))));
					appt.setName(request.getParameter("keyword"));
					appt.setDemographicNo(Integer.parseInt((String)paramE[9]));
					appt.setNotes(request.getParameter("notes"));
					appt.setReason(request.getParameter("reason"));
					appt.setLocation(request.getParameter("location"));
					appt.setResources(request.getParameter("resources"));
					appt.setUpdateDateTime(ConversionUtils.fromTimestampString(createdDateTime));
					appt.setLastUpdateUser(userName);
					appt.setUrgency(request.getParameter("urgency"));
					appt.setReasonCode(Integer.parseInt(request.getParameter("reasonCode")));
					appointmentDao.merge(appt);
					rowsAffected++;
				}
				
				
				gCalDate.setTime(UtilDateUtilities.StringToDate((String)param[12], "yyyy-MM-dd"));
				gCalDate = addDateByYMD(gCalDate, everyUnit, delta);

				if (gCalDate.after(gEndDate)) break;
				else param[12] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");
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

<%
    }
    return;
  }
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="appointment.appointmentgrouprecords.title" /></title>
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
</script>
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
		<th><font face="Helvetica"><bean:message key="appointment.appointmenteditrepeatbooking.title"/></font></th>
	</tr>
</table>

<table border="0" cellspacing="1" cellpadding="2" width="100%">
	<tr>
		<td width="20%"></td>
		<td nowrap><bean:message key="appointment.appointmenteditrepeatbooking.howoften"/></td>
	</tr>
	<tr>
		<td></td>
		<td nowrap>&nbsp;&nbsp;&nbsp; 
		
		<input type="radio" name="dateUnit" value="<bean:message key="day"/>"   <%="checked"%> onclick='onCheck(this, "day")'><bean:message key="day"/> &nbsp;&nbsp; 
		<input type="radio" name="dateUnit" value="<bean:message key="week"/>"  <%=""%>        onclick='onCheck(this, "week")'><bean:message key="week"/> &nbsp;&nbsp; 
		<input type="radio" name="dateUnit" value="<bean:message key="month"/>" <%=""%>        onclick='onCheck(this, "month")'><bean:message key="month"/> &nbsp;&nbsp; 
		<input type="radio" name="dateUnit" value="<bean:message key="year"/>"  <%=""%>        onclick='onCheck(this, "year")'><bean:message key="year"/></td>
	</tr>
</table>

<table border="0" cellspacing="1" cellpadding="2" width="100%">
	<tr>
		<td width="20%"></td>
		<td width="16%" nowrap><bean:message key="appointment.appointmenteditrepeatbooking.every"/></td>
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
		<td><bean:message key="appointment.appointmenteditrepeatbooking.endon"/> &nbsp;&nbsp;
		<button type="button" id="f_trigger_b">...</button>
		<br>
		<font size="-1"><bean:message key="ddmmyyyy"/></font></td>
		<td nowrap valign="top"><input type="text" name="endDate"
			id="endDate" size="10"
			value="<%=UtilDateUtilities.DateToString(new java.util.Date(),"dd/MM/yyyy")%>"
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
