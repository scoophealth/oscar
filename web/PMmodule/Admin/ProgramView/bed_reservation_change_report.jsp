<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bed Reservation Change Report</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>
<table width="100%">
	<tr>
		<td width="50%"><input type="button" value="Print"
			onclick="window.print()"></td>
		<td width="50%" align="right"><input type="button" value="Close"
			onclick="self.close()" /></td>
	</tr>
</table>
<br />
<display:table class="simple" name="bedReservationChanges"
	uid="bedReservationChange">
	<display:column property="who" title="Who" />
	<display:column property="when" title="When" />
	<display:column property="property" title="Property" />
	<display:column property="old" title="Old" />
	<display:column property="new" title="New" />
</display:table>
</body>
</html:html>
