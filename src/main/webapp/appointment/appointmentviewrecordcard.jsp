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
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.oscarehr.common.model.Appointment"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@ page import="org.oscarehr.common.model.Provider"%>

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
		Appointment Card</font></th>
	</tr>
</table>
<%
	String strAppointmentNo = request.getParameter("appointment_no");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aaa");

	
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	Appointment appt = appointmentDao.find(Integer.parseInt(strAppointmentNo));
	
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
%>
<p>

    </div>
<form>
    <table border="1" bgcolor="white" >
        <tr><td>
 
        <table style="font-size: 8pt;"  align="left" valign="top">

            <tr style="font-family: arial, sans-serif; font-size: 6pt;" >
                <th colspan="3"><%=appt.getName()%></th>
            </tr>
             <tr style="font-family: arial, sans-serif; font-size: 8pt;" >
		<th style="padding-right: 10px"><bean:message key="Appointment.formDate" /></th>
 		<th width="60" style="padding-right: 10px"><bean:message key="Appointment.formStartTime" /></th>
		<th width="120" style="padding-right: 10px"><bean:message key="appointment.addappointment.msgProvider" /></th>

            </tr>
        <%
       
        int iRow=0;
        int iPageSize=5;
        String pname="";
        // if the booking is not matched to a demographic demoNo=="0" as a default
        if( appt.getDemographicNo() == 0 ) {

        %>
            <tr bgcolor="#eeeeff">
		<td style="padding-right: 10px"><%=dateFormatter.format(appt.getAppointmentDate())%></td>
		<td style="padding-right: 10px"><%=timeFormatter.format(appt.getStartTime())%></td>
		<td style="padding-right: 10px">&nbsp;</td>
            </tr>
	<%
        } else {
        	
        	for(Appointment a: appointmentDao.findDemoAppointmentsOnDate(appt.getDemographicNo(),appt.getAppointmentDate())) {   	
        		Provider provider = providerDao.getProvider(a.getProviderNo());
     
    %>
            <tr bgcolor="#eeeeff">
		<td style="padding-right: 10px"><%=dateFormatter.format(a.getAppointmentDate())%></td>
		<td style="padding-right: 10px"><%=timeFormatter.format(a.getStartTime())%></td>
		<td style="padding-right: 10px"><%=provider.getLastName() + "," + provider.getFirstName().substring(0,1)%></td>
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

<p>

<div class="DoNotPrint">
<p></p>
<hr width="90%"/>

<input type="button" value="<bean:message key="global.btnClose"/>" onClick="window.close();">
</div>
</form>
</center>
</body>
</html:html>
