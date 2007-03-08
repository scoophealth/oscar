<%@ include file="/ticklerPlus/header.jsp"%>

<%@ page import="java.util.Calendar"%>
<%
Calendar now = Calendar.getInstance();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = now.get(Calendar.MONTH) + 1;

			%>

<script>
	function batch_operation(method) {
		var checked=false;
		
		var checkboxes=document.getElementsByName("checkbox");
		var x=0;
		for(x=0;x<checkboxes.length;x++) {
			if(checkboxes[x].checked==true) {
				checked=true;
			}
		}
		if(checked==false) {
			alert('You must choose a tickler');
			return false;
		}
		var form = document.ticklerForm;
		form.method.value=method;
		form.submit();
	}
	
</script>

<html:form action="/Tickler">
	<input type="hidden" name="method" value="save" />
	<input type="hidden" name="order_tcr" value="asc"/>

	<tr>
		<td class="searchTitle" colspan="4">Filter</td>
	</tr>
	<tr>
		<td class="blueText">Service Date Range:</td>
		<td class="blueText"><span style="text-decoration:underline"
			onClick="openBrWindow('<c:out value="${ctx}"/>/ticklerPlus/calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=filter.startDate&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Begin:</span>
		<html:text property="filter.startDate" /></td>
		<td class="blueText"><span style="text-decoration:underline"
			onClick="openBrWindow('<c:out value="${ctx}"/>/ticklerPlus/calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=filter.endDate&amp;year=<%=curYear%>&amp;month=<%=curMonth %>','','width=300,height=300')">End:</span><html:text
			property="filter.endDate" /></td>
		<td><input type="button" value="Create Report"
			onclick="this.form.method.value='filter';this.form.submit();" /></td>
	</tr>
	<tr>
		<td class="blueText">Status: <html:select property="filter.status"
			onchange="this.form.method.value='filter';this.form.submit();">
			<html:option value="Z">All</html:option>
			<html:option value="A">Active</html:option>
			<html:option value="C">Completed</html:option>
			<html:option value="D">Deleted</html:option>
		</html:select></td>
		<td class="blueText">Provider: <html:select property="filter.provider"
			onchange="this.form.method.value='filter';this.form.submit();">
			<option value="All Providers">All Providers</option>
			<html:options collection="providers" property="provider_no"
				labelProperty="formattedName" />
		</html:select></td>
		<td class="blueText">Task Assigned To: <html:select
			property="filter.assignee"
			onchange="this.form.method.value='filter';this.form.submit();">
			<option value="All Providers">All Providers</option>
			<html:options collection="providers" property="provider_no"
				labelProperty="formattedName" />
		</html:select></td>
	</tr>
	<tr>
		<td colspan="2" class="blueText">Client: <html:select
			property="filter.demographic_no"
			onchange="this.form.method.value='filter';this.form.submit();">
			<option value="All Clients">All Clients</option>
			<html:options collection="demographics" property="demographicNo"
				labelProperty="formattedName" />
		</html:select></td>
	</tr>

	<tr>
		<td><html:link action="CustomFilter.do">Custom Filters:</html:link>&nbsp;
		<html:select property="filter.name"
			onchange="this.form.method.value='run_custom_filter';this.form.submit();">
			<option value=""></option>
			<html:options collection="customFilters" property="name" />
		</html:select></td>

		
		<td></td>
	</tr>


	<br />
	<%@ include file="/ticklerPlus/messages.jsp"%>
	<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<tr class="title">
			<th></th>
			<th></th>
			<th>Demographic Name</th>
			<th class=noprint>Provider Name</th>

			
			 
			<%

			 String click_order =(String)session.getAttribute( "filter_order" );
	
			if(click_order=="DESC") {%>
				<input type="hidden" name="filter.sort_order" value="ASC" />
				<% 
				session.setAttribute( "filter_order", "ASC" );
			} else {%>
				<input type="hidden" name="filter.sort_order" value="DESC" />
				<% session.setAttribute( "filter_order", "DESC");
			}		
			%>
			<th class=noprint><b><input type="button" value="           Date           "
			onclick="this.form.method.value='filter';this.form.submit();" class=noprint/></b></th>

	

			<th class=noprint>Priority</th>
			<th class=noprint>Task Assigned To</th>
			<th class=noprint>Status</th>
			<th>Message</th>
		</tr>

		<tr>
			<%int index = 0;
			String bgcolor;
			String view_image;

			%>
			<c:forEach var="tickler" items="${ticklers}">
				<%
if (index++ % 2 != 0) {
				bgcolor = "white";
				view_image = "details.gif";
			} else {
				bgcolor = "#EEEEFF";
				view_image = "details2.gif";
			}

			%>
				<tr bgcolor="<%=bgcolor %>" align="center">
					<%
String demographic_name = "";
			String provider_name = "";
			String assignee_name = "";
			String status = "Active";
			String late_status = "b";			
			Tickler temp = (Tickler) pageContext.getAttribute("tickler");
			if (temp != null) {
				Demographic demographic = (Demographic) temp.getDemographic();
				if (demographic != null) {
					demographic_name = demographic.getLastName() + ","
							+ demographic.getFirstName();
				}
				Provider provider = (Provider) temp.getProvider();
				if (provider != null) {
					provider_name = provider.getLastName() + ","
							+ provider.getFirstName();
				}
				Provider assignee = (Provider) temp.getAssignee();
				if (assignee != null) {
					assignee_name = assignee.getLastName() + ","
							+ assignee.getFirstName();
				}
				switch (temp.getStatus()) {
				case 'A':
					status = "Active";
					break;
				case 'D':
					status = "Deleted";
					break;
				case 'C':
					status = "Completed";
					break;
				}
				// add by PINE_SOFT
				// get system date
				Date sysdate = new java.util.Date();
				Date service_date = (Date) temp.getService_date();

				if (!sysdate.before(service_date)) {
					late_status = "a";
				}
			}

			%>
					<td ><input type="checkbox" name="checkbox"
						value="<c:out value="${tickler.tickler_no}"/>" /></td>
					<td><a
						href="../Tickler.do?method=view&id=<c:out value="${tickler.tickler_no}"/>"><img
						align="right" src="<c:out value="${ctx}"/>/ticklerPlus/images/<%=view_image %>" border="0" />
					</a></td>

					<%
String style = "";
			style = "color:black;";
			if ("High".equals(temp.getPriority())) {
				style = "color:red;";
			}

			%>
					<td style="<%=style%>"><%=demographic_name%></td>
					<td style="<%=style%>" class=noprint><%=provider_name%></td>
					<td style="<%=style%>" class=noprint><fmt:formatDate pattern="MM/dd/yy : hh:mm a"
						value="${tickler.service_date}" /></td>
					<td style="<%=style%>" class=noprint><c:out value="${tickler.priority}" /></td>
					<td style="<%=style%>" class=noprint><%=assignee_name%></td>
					<td style="<%=style%>" class=noprint><%=status%></td>
					<td style="<%=style%>" align="left"><c:out escapeXml="false"
						value="${tickler.message}" /></td>
				</tr>
			</c:forEach>
		</tr>
		<tr>
			<td colspan="9"><%=((java.util.List) session.getAttribute("ticklers"))
							.size()%> ticklers found.</td>
		</tr>
	</table>

	<table>
		<!-- 
		<tr>
			<td colspan="2"><a href="#" onclick="CheckAll(document.ticklerForm);return false;">Check All</a>&nbsp;<a href="#" onclick="ClearAll(document.ticklerForm);return false;">Clear All</a></td>
		</tr>
	-->
		<tr>
			<td><input type="button" value="Create New Tickler" onclick="location.href='<html:rewrite action="/Tickler"/>?method=edit'"/></td>
			<td class=noprint><input type="button" value="Complete"
				onclick="batch_operation('complete');" /></td>
			<td class=noprint><input type="button" value="Delete"
				onclick="batch_operation('delete');" /></td>	
				
		</tr>
	</table>
</html:form>
<%if ((request.getParameter("from") == null)
					|| (!request.getParameter("from").equals("CaseMgmt"))) {
%>
<table width="100%">
	<tr>
		<!-- <td><a href="../provider/providercontrol.jsp">Back to Schedule Page</a></td> -->
		<td><a href="javascript:void(0)" onclick="window.close()">Close Window</a></td>
	</tr>
</table>

<%}%>
</body>
</html>

