
<%-- Updated by Eugene Petruhin on 18 dec 2008 while fixing #2422864 & #2317933 & #2379840 --%>

	<%@include file="/ticklerPlus/header.jsp"%>
	
	<%@page import="java.util.GregorianCalendar"%>
	<%@page import="java.util.Calendar"%>

	<%
		GregorianCalendar now = new GregorianCalendar();
	
		int curYear = now.get(Calendar.YEAR);
		int curMonth = now.get(Calendar.MONTH);
		int curDay = now.get(Calendar.DAY_OF_MONTH);
		int curHour = now.get(Calendar.HOUR);
		int curMinute = now.get(Calendar.MINUTE);
		
		boolean curAm = (now.get(Calendar.HOUR_OF_DAY) <= 12) ? true : false;
	%>
	<script type="text/javascript" src="../js/checkDate.js"></script>
	<script type="text/javascript">
		function check_tickler_service_date() {
			var serviceDate = document.ticklerForm.elements['tickler.serviceDate'].value;
			return check_date(serviceDate);
		}

<%--		
		function search_demographic() {
			var popup = window.open('<c:out value="${ctx}"/>/ticklerPlus/demographicSearch.jsp?query=' + document.ticklerForm.elements['tickler.demographic_webName'].value,'demographic_search');
		
			if (popup != null) {
	    			if (popup.opener == null) {
	      			popup.opener = self;
	    			}
	    			popup.focus();
	  			}	
		}

		function search_provider() {
			url = '<c:out value="${ctx}"/>/provider/receptionistfindprovider.jsp';
			url += '?caisi=true&pyear=<%=curYear%>&pmonth=<%=curMonth%>&pday=<%=curDay%>&providername=';
			var popup = window.open(url + document.ticklerForm.elements['tickler.task_assigned_to_name'].value,'provider_search');
		}
--%>		
		function validateTicklerForm(form) {
			if (form.elements['tickler.task_assigned_to'].value == 'none') {
				alert('You must assign the task to a valid provider');
				return false;
			}
			
			if (form.elements['tickler.serviceDate'].value == '') {
				alert('You must provide a valid service date');
				return false;
			}
			
			if (form.elements['tickler.message'].value == '') {
				alert('You must provide a message');
				return false;
			}
			
			return check_tickler_service_date();
		}
	</script>

	<tr>
		<td class="searchTitle" colspan="4">Create New Tickler</td>
	</tr>
</table>

<%@ include file="/common/messages.jsp"%>

<table width="60%" border="0" cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<html:form action="/Tickler" focus="tickler.demographic_no" onsubmit="return validateTicklerForm(this);">
	
		<input type="hidden" name="method" value="save" />
		<html:hidden property="tickler.creator" value='<%=(String) session.getAttribute("user")%>' />
		<html:hidden property="tickler.tickler_no" />
		
		<tr>
			<td class="fieldTitle">
				Demographic:
			</td>
			<td class="fieldValue">
				<html:hidden property="tickler.demographic_no" />
				<%=request.getParameter("tickler.demographic_webName")%>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">Program:</td>
			<td class="fieldValue"><c:out value="${requestScope.program_name}"/></td>
		</tr>
		<tr>
			<td class="fieldTitle">Service Date:</td>
			<%
				Calendar rightNow = Calendar.getInstance();
				int year = rightNow.get(Calendar.YEAR);
				int month = rightNow.get(Calendar.MONTH) + 1;
				int day = rightNow.get(Calendar.DAY_OF_MONTH);
				String formattedDate = year + "-" + month + "-" + day;
			%>
			<td class="fieldValue">
				<html:text property="tickler.serviceDate" value="<%=formattedDate%>" maxlength="10"/>
				<span onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=tickler.serviceDate&amp;year=<%=year%>&amp;month=<%=month%>','','width=300,height=300')">
					<img border="0" src="images/calendar.jpg" />
				</span>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Service Time:
			</td>
			<td class="fieldValue">
				<select name="tickler.service_hour">
				<%
					for (int x = 1; x < 13; x++) {
						String selected = "";
						
						if (curHour == x) {
							selected = "selected";
						}
				%>
					<option value="<%=x%>" <%=selected%>><%=x%></option>
				<%
					}
				%>
				</select> : <select name="tickler.service_minute">
				<%
					for (int x = 0; x < 60; x += 15) {
						String selected = "";
						
						if (curMinute >= x && curMinute < (x + 15)) {
							selected = "selected";
						}
						
						String val = String.valueOf(x);
						
						if (val.equals("0")) {
							val = "00";
						}
				%>
					<option value="<%=val%>" <%=selected %>><%=val%></option>
				<%
					}
				%>
				</select> &nbsp; <select name="tickler.service_ampm">
					<option value="AM">AM</option>
				<%
					if (!curAm) {
				%>
					<option value="PM" selected>PM</option>
				<%
					} else {
				%>
					<option value="PM">PM</option>
				<%
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Priority:
			</td>
			<td class="fieldValue">
				<html:select property="tickler.priority">
					<option value="Normal">Normal</option>
					<option value="High">High</option>
					<option value="Low">Low</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Task Assigned To:
			</td>
			<td class="fieldValue">
				<html:hidden property="tickler.task_assigned_to_name" />
	            <html:select property="tickler.task_assigned_to" value="none">
        		    <option value="none">- select -</option>
        		    <html:options collection="providers" property="providerNo" labelProperty="formattedName"/>
            	</html:select>
				<%--html:hidden property="tickler.task_assigned_to" />
				<html:text property="tickler.task_assigned_to_name" />
				<input type="button" value="Search" onclick="search_provider();" /--%>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Status:
			</td>
			<td class="fieldValue">
				<html:select property="tickler.status">
					<option value="A">Active</option>
					<option value="C">Completed</option>
					<option value="D">Deleted</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Message:
			</td>
			<td class="fieldValue">
				<html:textarea cols="40" rows="10" property="tickler.message"></html:textarea>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Post to eChart:
			</td>
			<td class="fieldValue">
				<input name="echart" value="true" type="checkbox" />
			</td>
		</tr>
		<tr>
			<td class="fieldValue" colspan="3" align="left">
				<html:submit styleClass="button">Save</html:submit>
				<input type="button" value="Cancel" onclick="window.close()"/>
			</td>
		</tr>
	</html:form>
</table>

<c:if test="${requestScope.from ne 'CaseMgmt'}">
	</body>
</html>
</c:if>
