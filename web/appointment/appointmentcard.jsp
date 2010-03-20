<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*, oscar.appt.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page import="oscar.appt.status.service.AppointmentStatusMgr"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page import="oscar.appt.status.model.AppointmentStatus"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>

<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
<title><bean:message key="appointment.addappointment.title" /></title>
<style type="text/css" media="print">
    .DoNotPrint {
        display: none;
    }
</style>

</head>
<body onLoad="" topmargin="0" leftmargin="0" rightmargin="0">
<%String demoNo = request.getParameter("demographic_no");%>
<table style="font-size: 8pt;"  align="left" valign="top">

	<tr style="font-family: arial, sans-serif" >
		<th style="padding-right: 25px"><bean:message key="Appointment.formDate" /></th>
 		<th style="padding-right: 25px"><bean:message key="Appointment.formStartTime" /></th>
		<th style="padding-right: 25px"><bean:message key="appointment.addappointment.msgProvider" /></th>

	</tr>

	<%

        int iRow=0;
        int iPageSize=5;
        String f_name="";
        if( demoNo != null && demoNo.length() > 0 ) {

            Object [] para = new Object[3];
            para[0] = demoNo;
            Calendar cal = Calendar.getInstance();
            para[1] = new java.sql.Date(cal.getTime().getTime());
            cal.add(Calendar.YEAR, 1);
            para[2] = new java.sql.Date(cal.getTime().getTime());
    		List<Map> resultList  = oscarSuperManager.find("appointmentDao", "search_appt_future", para);

    		for (Map appt : resultList) {
                iRow ++;
                if (iRow > iPageSize) break;
    %>
	<tr bgcolor="#eeeeff">
		<td style="padding-right: 25px"><%=appt.get("appointment_date")%></td>
		<td style="padding-right: 25px"><%=appt.get("start_time")%></td>
		<td style="padding-right: 25px"><%=appt.get("last_name") + ",&nbsp;" + appt.get("first_name")%></td>

	</tr>

	<%
            }
        }
    %>
</table>
<form>
    <div class="DoNotPrint">
        <input type="button" value="<bean:message key="global.btnPrint"/>" onClick="window.print();">
    </div>
</form>
</body>
</html:html>