<%@page import="org.oscarehr.eyeform.model.ProcedureBook"%>
<%@page import="org.oscarehr.eyeform.web.ProcedureBookAction"%>


<%@ include file="/taglibs.jsp"%>

 
<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />

		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
		
</head>
<body>
Book Procedure
<br />

<html:form action="/eyeform/ProcedureBook.do">
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<input type="hidden" name="method" value="save"/>
				
		<html:hidden property="data.id"/>
		<html:hidden property="data.demographicNo"/>		
		<html:hidden property="data.appointmentNo"/>
		
						
		<tr>
			<td class="genericTableHeader">Procedure name</td>
			<td class="genericTableData">
				<html:text property="data.procedureName" size="50"/>
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
				<html:select property="data.eye">
					<html:option value="OU">OU</html:option>
					<html:option value="OD">OD</html:option>
					<html:option value="OS">OS</html:option>
					<html:option value="OD then OS">OD then OS</html:option>
					<html:option value="OS then OD">OS then OD</html:option>
				</html:select>
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Location</td>
			<td class="genericTableData">
					<html:text property="data.location" size="35"/>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Comment</td>
			<td class="genericTableData">
					<html:textarea rows="5" cols="40" property="data.comment"></html:textarea>		
			</td>
		</tr>

		<tr style="background-color:white">
			<td colspan="2">
				<br />
				
				
							&nbsp;&nbsp;&nbsp;&nbsp;
							<html:submit value="Book Procedure" />
						
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="cancel" value="Cancel" onclick="window.close()" />

			</td>
		</tr>
	</table>

</html:form>

</body>
</html>
