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

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat, org.oscarehr.common.OtherIdManager"%>
<%@ page import="org.oscarehr.event.EventService, org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<style type="text/css" media="print">
    .DoNotPrint {
        display: none;
    }
</style>
</head>
<body background="../images/gray_bg.jpg"
	bgproperties="fixed">
<center>
<div class="DoNotPrint">
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.addappointment.msgMainLabel" /></font></th>
	</tr>
</table>
<%

	String[] param = new String[20];
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
	param[13]=request.getParameter("createdatetime");
	param[14]=request.getParameter("creator");
	param[15]=request.getParameter("remarks");
	param[17]=(String)request.getSession().getAttribute("programId_oscarView");
	param[18]=(request.getParameter("urgency")!=null)?request.getParameter("urgency"):"";
	param[19]=request.getParameter("createdatetime");
	
    if (request.getParameter("demographic_no") != null && !(request.getParameter("demographic_no").equals(""))) {
        param[16] = request.getParameter("demographic_no");
    } else {
        param[16] = "0";
    }

    int rowsAffected = oscarSuperManager.update("appointmentDao", request.getParameter("dboperation"), param);
	if (rowsAffected == 1) {

                String patientname = "";
            	String[] param2 = new String[9];
                param2[0]=param[1];
                param2[1]=param[0];
                param2[2]=param[2];
                param2[3]=param[2];
                param2[4]=param[3];
                param2[5]=param[3];
                param2[6]=param[2];
                param2[7]=param[3];
                param2[8]=param[17];
                List<Map<String,Object>> resultList  = oscarSuperManager.find("appointmentDao", "search_appt_name", param2);
                if (resultList.size() > 0) {
                    Map name = resultList.get(0);
                    patientname = "" + name.get("name");
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
        String demoNo = param[16];
        String appt_date = param[1];
        String appt_time = MyDateFormat.getTimeXX_XXampm(param[2]);
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

            Object [] para = new Object[3];
            para[0] = demoNo;
            Calendar cal = Calendar.getInstance();
            para[1] = new java.sql.Date(cal.getTime().getTime());
            cal.add(Calendar.YEAR, 1);
            para[2] = new java.sql.Date(cal.getTime().getTime());
            resultList  = oscarSuperManager.find("appointmentDao", "search_appt_future", para);

            for (Map appt : resultList) {
                iRow ++;
                if (iRow > iPageSize) break;
                appt_time = MyDateFormat.getTimeXX_XXampm("" + appt.get("start_time"));
                pname = "" + appt.get("first_name");
                pname = ""+ appt.get("last_name")+ ", "+ pname.substring(0,1);
    %>
            <tr bgcolor="#eeeeff">
		<td style="padding-right: 10px"><%=appt.get("appointment_date")%></td>
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
		String[] param3 = new String[7];
		param3[0]=param[0]; //provider_no
		param3[1]=param[1]; //appointment_date
		param3[2]=param[2]; //start_time
		param3[3]=param[3]; //end_time
		param3[4]=param[13]; //createdatetime
		param3[5]=param[14]; //creator
		param3[6]=param[16]; //demographic_no

		List<Map<String,Object>> apptList = oscarSuperManager.find("appointmentDao", "search_appt_no", param3);
		if (apptList.size()>0) {
			Integer apptNo = (Integer)apptList.get(0).get("appointment_no");
			String mcNumber = request.getParameter("appt_mc_number");
			OtherIdManager.saveIdAppointment(apptNo, "appt_mc_number", mcNumber);
			
			EventService eventService = SpringUtils.getBean(EventService.class); //print button when making an appointment
			eventService.appointmentCreated(this,apptNo.toString(), param[0]);
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
