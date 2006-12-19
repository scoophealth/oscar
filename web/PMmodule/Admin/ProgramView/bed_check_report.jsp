<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html:html locale="true">
	<head>
		<html:base />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Bed Check Report</title>
		<style type="text/css">
			@import "<html:rewrite page="/css/displaytag.css" />";
		</style>
	</head>
	<body>
		<table width="100%">
			<tr>
				<td width="50%">
					<input type="button" value="Print" onclick="window.print()">
				</td>
				<td width="50%" align="right">
					<input type="button" value="Close" onclick="self.close()" />
				</td>
			</tr>
		</table>
		<br />
		<display:table class="simple" name="reservedBeds" uid="bed" requestURI="/PMmodule/ProgramManagerView.do">
			<display:column property="name" title="Bed" />
			<display:column property="roomName" title="Room" />
			<display:column property="demographicName" title="Client" />
			<display:column property="statusName" title="Status" />
			<display:column property="latePass" title="Late Pass" />
			<display:column property="reservationStart" title="Reserved Since" format="{0, date, yyyy-MM-dd}"/>
			<display:column property="reservationEnd" title="Reserved Until" format="{0, date, yyyy-MM-dd}"/>
			<display:column title="Not Present">
				<input type="checkbox" />
			</display:column>
		</display:table>
</body>
</html:html>
