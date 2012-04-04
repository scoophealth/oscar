<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="oscar.oscarBilling.ca.bc.MSP.*"%>
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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Automate Internal Adjustments</title>
</head>
<body bgcolor="#ffffff">
<div align="center" style="background-color: #EEEEFF;">
<%
  if ("true".equals(request.getParameter("settled"))) {
%>
<h4>All claims with an explanation of type 'BG' have been adjusted
and settled</h4>
<input type="button" value="Close" onClick="javascript:window.close()">
<%} else {%>
<h4>Automatically settle claims that have been over/under paid(BG)</h4>
<form method="post" action="settleBG.jsp"><input type="hidden"
	name="settled" value="false" /> <br>
<br>
<input type="submit" name="Continue" value="Submit"> <input
	type="button" value="Cancel" onClick="javascript:window.close()">
</form>
<%}%>
</div>
</body>
</html>
