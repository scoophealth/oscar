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
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_appointment");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat, org.oscarehr.common.OtherIdManager, oscar.util.ConversionUtils"%>
<%@ page import="org.oscarehr.event.EventService, org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<style type="text/css" media="print">
    .DoNotPrint {
        display: none;
    }
</style>
</head>
<body>
<center>
<div class="DoNotPrint">
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.addappointment.msgMainLabel" /></font></th>
	</tr>
</table>
<%

	int demographicNo = 0;
	if (request.getParameter("demographic_no") != null && !(request.getParameter("demographic_no").equals(""))) {
		demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
	} 

    int rowsAffected = 0;
    
    if(request.getParameter("appointment_no") == null) {
    	Appointment a = new Appointment();
    	a.setProviderNo(request.getParameter("provider_no"));
    	a.setAppointmentDate(ConversionUtils.fromDateString(request.getParameter("appointment_date")));
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
    	a.setCreator(request.getParameter("creator"));
    	a.setRemarks(request.getParameter("remarks"));
    	a.setUrgency((request.getParameter("urgency")!=null)?request.getParameter("urgency"):"");
    	if (request.getParameter("demographic_no")!=null && !(request.getParameter("demographic_no").equals(""))) {
      		a.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
     	} else {
    	 	a.setDemographicNo(0);
     	}
    	a.setProgramId(Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
    	a.setCreator(request.getParameter("creator"));
    	a.setCreateDateTime(ConversionUtils.fromDateString(request.getParameter("createdatetime")));
    	a.setReasonCode(Integer.parseInt(request.getParameter("reasonCode")));
    	appointmentDao.persist(a);
   		rowsAffected=1;
    }
    if(request.getParameter("appointment_no") != null) {
	    Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
	    if(appt != null) {
	    	appt.setProviderNo(request.getParameter("provider_no"));
	    	appt.setAppointmentDate(ConversionUtils.fromDateString(request.getParameter("appointment_date")));
	    	appt.setStartTime(ConversionUtils.fromTimeString(MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"))));
	    	appt.setEndTime(ConversionUtils.fromTimeString(MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"))));
	    	appt.setName(request.getParameter("keyword"));
	    	appt.setNotes(request.getParameter("notes"));
	    	appt.setReason(request.getParameter("reason"));
	    	appt.setLocation(request.getParameter("location"));
	    	appt.setResources(request.getParameter("resources"));
	    	appt.setType(request.getParameter("type"));
	    	appt.setStyle(request.getParameter("style"));
	    	appt.setBilling(request.getParameter("billing"));
	    	appt.setStatus(request.getParameter("status"));
	    	appt.setLastUpdateUser((String)session.getAttribute("user"));
	    	appt.setRemarks(request.getParameter("remarks"));
	    	appt.setUpdateDateTime(new java.util.Date());
	    	appt.setUrgency((request.getParameter("urgency")!=null)?request.getParameter("urgency"):"");
	    	if (request.getParameter("demographic_no")!=null && !(request.getParameter("demographic_no").equals(""))) {
	      		appt.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
	     	} else {
	    	 	appt.setDemographicNo(0);
	     	}
	    	appt.setProgramId(Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
	    	appt.setCreator(request.getParameter("creator"));
	    	appt.setCreateDateTime(ConversionUtils.fromDateString(request.getParameter("createdatetime")));
	    	appt.setReasonCode(Integer.parseInt(request.getParameter("reasonCode")));
	    	appointmentDao.merge(appt);
	    	rowsAffected=1;
	    }
    
    }
	if (rowsAffected == 1) {

                String patientname = "";
            	
                
                List<Appointment> appts = appointmentDao.search_appt(ConversionUtils.fromDateString(request.getParameter("appointment_date")), request.getParameter("provider_no"),
                		ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")), ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")), 
                		ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")), ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")),
                		ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")), ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")), 
                		Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
                if(appts.size()>0) {
                	patientname = appts.get(0).getName();
                }
%>
<p>
<h3><bean:message key="appointment.addappointment.msgAddSuccess" /></h3>

    </div>
<form>
    <table border="1" bgcolor="white" >
        <tr><td>
 
        <table style="font-size: 8pt;"  align="left" valign="top">

            <tr style="font-family: arial, sans-serif; font-size: 6pt;" >
                <th colspan="3"><%=patientname%></th>
            </tr>
             <tr style="font-family: arial, sans-serif; font-size: 8pt;" >
		<th style="padding-right: 10px"><bean:message key="Appointment.formDate" /></th>
 		<th width="60" style="padding-right: 10px"><bean:message key="Appointment.formStartTime" /></th>
		<th width="120" style="padding-right: 10px"><bean:message key="appointment.addappointment.msgProvider" /></th>

            </tr>
        <%
        String demoNo = String.valueOf(demographicNo);
        String appt_date = request.getParameter("appointment_date");
        String appt_time = MyDateFormat.getTimeXX_XXampm(MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time")));
        int iRow=0;
        int iPageSize=5;
        String pname="";
        // if the booking is not matched to a demographic demoNo=="0" as a default
        if( demoNo != null && demoNo.equals("0") ) {

        %>
            <tr bgcolor="#eeeeff">
		<td style="padding-right: 10px"><%=appt_date%></td>
		<td style="padding-right: 10px"><%=appt_time%></td>
		<td style="padding-right: 10px">&nbsp;</td>
            </tr>
	<%
        } else if( demoNo != null && demoNo.length() > 0) {

           
            Calendar cal = Calendar.getInstance();
           cal.add(Calendar.YEAR, 1);
            
            List<Object[]> appts1 = appointmentDao.search_appt_future(Integer.parseInt(demoNo), new java.util.Date(), cal.getTime());
            for (Object[] appt1: appts1) {
            	Appointment ap = (Appointment)appt1[0];
            	Provider p =  (Provider)appt1[1];
                iRow ++;
                if (iRow > iPageSize) break;
                appt_time = MyDateFormat.getTimeXX_XXampm(ConversionUtils.toTimeStringNoSeconds(ap.getStartTime()));
                pname = "" + p.getFirstName();
                pname = ""+ p.getLastName()+ ", "+ pname.substring(0,1);
    %>
            <tr bgcolor="#eeeeff">
		<td style="padding-right: 10px"><%=ConversionUtils.toDateString(ap.getAppointmentDate())%></td>
		<td style="padding-right: 10px"><%=appt_time%></td>
		<td style="padding-right: 10px"><%=pname%></td>
            </tr>
	<%
            }
        }
    %>
    
            <tr class="DoNotPrint">
		<td style="padding-left: 10px"><input type="button" value="<bean:message key="global.btnPrint"/>" onClick="window.print();"></td>
                <td>&nbsp;</td>
		<td>&nbsp;</td>
            </tr>
       </table>
       </td></tr>
</table>
<%
		int demoNo1 = 0;
		if (request.getParameter("demographic_no") != null && !(request.getParameter("demographic_no").equals(""))) {
			demoNo1 = Integer.parseInt(request.getParameter("demographic_no"));
	    }
		
		Appointment aa = appointmentDao.search_appt_no(request.getParameter("provider_no"), ConversionUtils.fromDateString(request.getParameter("appointment_date")),
				ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")), ConversionUtils.fromTimeStringNoSeconds(request.getParameter("ned_time")), 
				ConversionUtils.fromDateString(request.getParameter("createdatetime")), request.getParameter("creator"), demoNo1);
		if (aa != null) {
			Integer apptNo = aa.getId();
			String mcNumber = request.getParameter("appt_mc_number");
			OtherIdManager.saveIdAppointment(apptNo, "appt_mc_number", mcNumber);
			
			EventService eventService = SpringUtils.getBean(EventService.class); //print button when making an appointment
			eventService.appointmentCreated(this,apptNo.toString(), request.getParameter("provider_no"));
		}
	} else {
%>
<p>
<h1><bean:message key="appointment.addappointment.msgAddFailure" /></h1>

<%
	}
%>
<div class="DoNotPrint">
<p></p>
<hr width="90%"/>

<input type="button" value="<bean:message key="global.btnClose"/>" onClick="window.close();">
</div>
</form>
</center>
</body>
</html:html>
