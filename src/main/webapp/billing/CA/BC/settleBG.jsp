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
