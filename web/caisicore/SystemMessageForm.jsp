<%@ include file="/taglibs.jsp"%>
<%@page import="java.util.Calendar" %>
<html>
	<head>
	
		<style type="text/css">
		/* <![CDATA[ */
		@import "<html:rewrite page="/css/core.css" />";
		/*  ]]> */
		</style>
		
		<title>System Messages</title>
	</head>
	<script>
function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}
</script>
	<body>
<body>
<table border="0" cellspacing="0" cellpadding="0" width="100%" bgcolor="#CCCCFF">
	  <tr class="subject"><th colspan="4">CAISI</th></tr>

	<tr>
            <td class="searchTitle" colspan="4">System Message Editor</td>
	</tr>
</table>

<br/>
		<html:form action="/SystemMessage">
			<input type="hidden" name="method" value="save"/>
			<html:hidden property="system_message.id"/>
			<table width="60%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
				<tr>
					<td class="fieldTitle">Expiry Day:&nbsp;</td>
					<td class="fieldValue"><html:text property="system_message.expiry_day"/>
					<%
                      	Calendar rightNow = Calendar.getInstance();              
   	                  	int year = rightNow.get(Calendar.YEAR);
   	                  	int month = rightNow.get(Calendar.MONTH)+1;
   	                  	int day = rightNow.get(Calendar.DAY_OF_MONTH);
   	                  	String formattedDate = year + "-" + month + "-" + day;
                     %>
                     <a href="#" onClick="openBrWindow('../calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=systemMessageForm&amp;openerElement=system_message.expiry_day&amp;year=<%=year %>&amp;month=<%=month %>','','width=300,height=300')"><img border="0" src="images/calendar.jpg"/></a>
                     </td>
                     <td></td>
				</tr>
				<tr>
					<td class="fieldTitle">Expiry Time:&nbsp;</td>
					<td class="fieldValue">Hour: 
						<html:select property="system_message.expiry_hour">
							<%for(int x=1;x<24;x++){ %>
								<html:option value="<%=String.valueOf(x) %>"><%=x %></html:option>
							<% } %>
						</html:select>
					&nbsp;&nbsp;
					Minute: 
						<html:select property="system_message.expiry_minute">
							<%for(int x=0;x<60;x++) {%>
								<html:option value="<%=String.valueOf(x) %>"><%=x %></html:option>
							<% } %>
						</html:select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="fieldTitle">Message&nbsp;</td>
					<td colspan="2" class="fieldValue"><html:text size="60" property="system_message.message"/></td>
				</tr>
				<tr>
					<td class="fieldValue" colspan="3">
						<html:submit>Save</html:submit>
						<input type="button" value="Cancel" onclick="location.href='SystemMessage.do'"/>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>