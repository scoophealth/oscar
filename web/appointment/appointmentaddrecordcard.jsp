<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>
<body background="../images/gray_bg.jpg"
	bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.addappointment.msgMainLabel" /></font></th>
	</tr>
</table>
<%

	String[] param = new String[17];
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
    if (request.getParameter("demographic_no") != null && !(request.getParameter("demographic_no").equals(""))) {
        param[16] = request.getParameter("demographic_no");
    } else {
        param[16] = "0";
    }

    int rowsAffected = oscarSuperManager.update("appointmentDao", request.getParameter("dboperation"), param);
	if (rowsAffected == 1) {
%>
<p>
<h1><bean:message key="appointment.addappointment.msgAddSuccess" /></h1>
</p>
<script LANGUAGE="JavaScript">
	self.opener.refresh();
	popupPage(350,750,'appointmentcard.jsp?demographic_no=<%=param[16]%>') ;
	self.close();
</script>
<%
	} else {
%>
<p>
<h1><bean:message key="appointment.addappointment.msgAddFailure" /></h1>
</p>
<%
	}
%>
<p></p>
<hr width="90%"/>
<form>
<input type="button" value="<bean:message key="global.btnClose"/>" onClick="window.close();">
</form>
</center>
</body>
</html:html>
