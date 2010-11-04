<%@page import="org.oscarehr.eyeform.model.OcularProc"%>
<%@page import="org.oscarehr.eyeform.web.OcularProcAction"%>


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
Book Ocular Procedure
<br />

<html:form action="/eyeform/OcularProc.do">
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<input type="hidden" name="method" value="save"/>
		
		<html:hidden property="proc.demographicNo"/>
		<html:hidden property="proc.appointmentNo"/>
		<html:hidden property="proc.id"/>
		

		<tr>
			<td class="genericTableHeader">Date</td>
			<td class="genericTableData">				
				<html:text property="proc.dateStr" size="10" styleId="pdate"/> <img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal">
			</td>
			<script type="text/javascript">
				Calendar.setup({ inputField : "pdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal", singleClick : true, step : 1 });
			</script>		
		</tr>
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
				<html:select property="proc.eye">
					<html:option value="OD">OD</html:option>
					<html:option value="OS">OS</html:option>
					<html:option value="OU">OU</html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Procedure</td>
			<td class="genericTableData">
				<html:text property="proc.procedureName" size="50"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Doctor Name</td>
			<td class="genericTableData">
					<html:select property="proc.doctor">
						<html:options collection="providers" property="providerNo" labelProperty="formattedName" />
					</html:select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Location</td>
			<td class="genericTableData">
					<html:text property="proc.location" size="35"/>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Procedure Notes</td>
			<td class="genericTableData">
					<html:textarea rows="5" cols="40" property="proc.procedureNote"></html:textarea>		
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
