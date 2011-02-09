<%@page import="org.oscarehr.eyeform.model.SpecsHistory"%>


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
Follow Up/Consult
<br /><br /><br />

<html:form action="/eyeform/FollowUp.do">
 

		<input type="hidden" name="method" value="save"/>
		
		<html:hidden property="followup.demographicNo"/>
		
		<html:hidden property="followup.appointmentNo"/>
		
		
		<table border="0">
           <tbody><tr>
            <td width="25%">
            <html:select property="followup.type">
            	<html:option value="followup">Follow up</html:option>
            	<html:option value="consult">Consult</html:option>
            </html:select>
            </td>
            <td width="25%">
             Doctor:
            <html:select property="followup.followupProvider">
            	<c:forEach var="item" items="${providers}">
            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
            	</c:forEach>            	
			</html:select>
			</td>
			</tr>
			<tr>
			<td colspan="2">
			
           
             <html:text property="followup.timespan" size="4"  styleId="width: 25px;" styleClass="special"/>
             
             <html:select property="followup.timeframe" styleId="width: 50px;" styleClass="special">
             	<html:option value="days">days</html:option>
            	<html:option value="weeks">weeks</html:option>
            	<html:option value="months">months</html:option>
            </html:select>
            </td> 			                       
            </tr>
           <tr>
           <td>
            <html:select property="followup.urgency" styleId="width: 50px;" styleClass="special">
             	<html:option value="routine">routine</html:option>
            	<html:option value="asap">ASAP</html:option>            	
            </html:select>
           </td>
           </tr>
           <tr>
           <td>
           	Comment:<html:textarea rows="5" cols="40" property="followup.comment"></html:textarea>		
           </td>
           </tr>
            <tr>
            <td><br/><html:submit value="submit"/></td>
            </tr>
           </tbody>
			</table>
		
			

</html:form>

</body>
</html>
