
<%
  if(session.getAttribute("user") == null)    response.sendRedirect("../logout.jsp");
  String curProvider_no = request.getParameter("provider_no");
  String curUser_no = (String) session.getAttribute("user");
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");
  

  boolean bFirstDisp=true; //this is the first time to display the window
  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
  
  boolean bObj = (new ApptOpt()).cutAppointment(request);
%>
<%@ page
	import="oscar.oscarDemographic.data.*, java.util.*, java.sql.*, oscar.appt.*"
	errorPage="errorpage.jsp"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script LANGUAGE="JavaScript">
<!--
function start(){
  this.focus();
}
function closeit() {
  self.opener.refresh();
  close();
}   
//-->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.appointmentupdatearecord.msgMainLabel" /></font></th>
	</tr>
</table>
<%
  if (bObj) {
%>
<p>
<h1><bean:message
	key="appointment.appointmentupdatearecord.msgUpdateSuccess" /></h1>
</p>
<script LANGUAGE="JavaScript">
     	self.opener.refresh();
      self.close();
</script> <%  
  } else {
%>
<p>
<h1><bean:message
	key="appointment.appointmentupdatearecord.msgUpdateFailure" /></h1>
</p>
<%  
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button"
	value="<bean:message key="global.btnClose"/>" onClick="closeit()">
</form>
</center>
</body>
</html:html>
