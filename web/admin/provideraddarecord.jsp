<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page  import="java.sql.*, java.util.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html:html locale="true">
<head><title><bean:message key="admin.provideraddrecord.title"/></title>
<link rel="stylesheet" href="../web.css">
</head>

<body bgproperties="fixed"  topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th><font face="Helvetica" color="#FFFFFF"><bean:message key="admin.provideraddrecord.description"/></font></th>
  </tr>
</table>
<%
  String[] param =new String[17];
  param[0]=request.getParameter("provider_no");
  param[1]=request.getParameter("last_name");
  param[2]=request.getParameter("first_name");
  param[3]=request.getParameter("provider_type");
  param[4]=request.getParameter("specialty");
  param[5]=request.getParameter("team");
  param[6]=request.getParameter("sex");
  param[7]=request.getParameter("dob");
  param[8]=request.getParameter("address");
  param[9]=request.getParameter("phone");
  param[10]=request.getParameter("workphone");
  param[11]=request.getParameter("ohip_no");
  param[12]=request.getParameter("rma_no");
  param[13]=request.getParameter("billing_no");
  param[14]=request.getParameter("hso_no");
  param[15]=request.getParameter("status");
  param[16]=SxmlMisc.createXmlDataString(request,"xml_p");

  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
  if (rowsAffected ==1) {
%>
  <h1><bean:message key="admin.provideraddrecord.msgAdditionSuccess"/>
  </h1>
<%  
  } else {
%>
  <h1><bean:message key="admin.provideraddrecord.msgAdditionFailure"/></title>
<%  
  }
  apptMainBean.closePstmtConn();
%>
<%@ include file="footer2htm.jsp" %>
</center>
</body>
</html:html>
