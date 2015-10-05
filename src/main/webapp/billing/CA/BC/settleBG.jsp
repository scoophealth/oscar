<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="oscar.oscarBilling.ca.bc.MSP.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  if ("false".equals(request.getParameter("settled"))) {
    MSPReconcile rec = new MSPReconcile();
    rec.settleBGBills();
%>
<jsp:forward page="settleBG.jsp">
	<jsp:param name="settled" value="true" />
</jsp:forward>
<%}%>
<html>
<head>
	<title>Automate Internal Adjustments</title>
	<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<h3><bean:message key="admin.admin.settlePaidClaims"/></h3>
	
	<div class="container-fluid well">
		<%
		  if ("true".equals(request.getParameter("settled"))) {
		%>
		<h4>All claims with an explanation of type 'BG' have been adjusted and settled</h4>
		<%} else {%>
		<h4>Automatically settle claims that have been over/under paid(BG)</h4>
		<form method="post" action="settleBG.jsp">
			<input type="hidden" name="settled" value="false" /> <br>
			<input class="btn btn-primary" type="submit" name="Continue" value="Submit">
		</form>
		<%}%>
	</div>
</body>
</html>
