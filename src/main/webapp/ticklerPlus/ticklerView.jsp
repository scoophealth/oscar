
<%-- Updated by Eugene Petruhin on 17 dec 2008 while fixing #2422864 & #2317933 & #2379840 --%>

<%@ include file="/ticklerPlus/header.jsp" %>
	<tr>
            <td class="searchTitle" colspan="4">View Tickler #<c:out value="${tickler.tickler_no}"/></td>
	</tr>
</table>
<%@ include file="messages.jsp" %>

<br/>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<script>
	function reassign_tickler() {
		document.ticklerForm.id.value=<c:out value="${tickler.tickler_no}"/>;
		document.ticklerForm.method.value='reassign';
		document.ticklerForm.submit();
	}

	function update_status() {
		document.ticklerForm.id.value=<c:out value="${tickler.tickler_no}"/>;
		document.ticklerForm.method.value='update_status';
		document.ticklerForm.submit();
	}
	
	function add_comment() {
		document.ticklerForm.id.value=<c:out value="${tickler.tickler_no}"/>;
		document.ticklerForm.method.value='add_comment';
		document.ticklerForm.submit(); 
	}
</script>
<html:form action="/Tickler" onsubmit="add_comment();return false;">
<input type="hidden" name="id" value=""/>
<input type="hidden" name="method" value=""/>

<table width="60%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<%
	String demographic_name="";
	String program_name="";
	String provider_name="";
	String assignee_id="";
	String inactive_name="";
	String status = "Active";
	String priorityStyle = "";
	
	Tickler temp = (Tickler)request.getAttribute("tickler");
	if(temp != null) {
		Demographic demographic = (Demographic)temp.getDemographic();
		if(demographic != null) {
			demographic_name = demographic.getLastName() + "," + demographic.getFirstName();
		}
		
		if (temp.getProgram_id()!=null) {
			ProgramDao programDao=(ProgramDao)SpringUtils.getBean("programDao");
			Program program=programDao.getProgram(temp.getProgram_id());
			program_name=program.getName();
		}
			
		Provider provider = (Provider)temp.getProvider();
		if(provider != null) {
			provider_name = provider.getLastName() + "," + provider.getFirstName();
		}

		Provider assignee = (Provider)temp.getAssignee();
		if(assignee != null) {
			assignee_id = assignee.getProviderNo();
			if (!((java.util.List)request.getAttribute("providers")).contains(assignee)) {
				inactive_name = assignee.getLastName() + "," + assignee.getFirstName();
			}
		}

		switch(temp.getStatus()) {
			case 'A': status="Active";break;
			case 'D': status="Deleted";break;
			case 'C': status="Completed";break;
		}

		if ("High".equals(temp.getPriority())) {
			priorityStyle = " style='color:red;'";
		}
	}
%>
	<tr>
		<td colspan="2" class="title">Main Information</td>
	</tr>
	<tr>
		<td class="fieldTitle">Demographic:</td>
		<td class="fieldValue"><a href="../demographic/demographiccontrol.jsp?demographic_no=<c:out value="${tickler.demographic_no}"/>&displaymode=edit&dboperation=search_detail" target="demographic"><%=demographic_name %></a></td>
	</tr>
	<tr>
		<td class="fieldTitle">Program:</td>
		<td class="fieldValue"><%=program_name %></td>
	</tr>
	<tr>
		<td class="fieldTitle">Provider:</td>
		<td class="fieldValue"><%=provider_name %></td>
	</tr>
	<tr>
		<td class="fieldTitle">Service Date:</td>
		<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${tickler.service_date}"/></td>
	</tr>
	<tr>
		<td class="fieldTitle">Date Created:</td>
		<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${tickler.update_date}"/></td>
	</tr>
	<tr>
		<td class="fieldTitle">Priority:</td>
		<td class="fieldValue" <%=priorityStyle%>><c:out value="${tickler.priority}"/></td>
	</tr>
	<tr>
		<td class="fieldTitle">Task Assigned To:</td>
		<td class="fieldValue">
	            <html:select property="tickler.task_assigned_to" value="<%=assignee_id%>">
					<% if (inactive_name.length() > 0) { %>
    	    		    <option value="<%=assignee_id%>" selected><%=inactive_name%></option>
					<% } %>
        		    <html:options collection="providers" property="providerNo" labelProperty="formattedName"/>
            	</html:select>
				<input type="button" value="Re-Assign Task" onclick="reassign_tickler()"/>
				<% if (inactive_name.length() > 0) { %>
   	    		    <span style='color: red'>Warning! <%=inactive_name%> is inactive.</span>
				<% } %>
		</td>
	</tr>
	<tr>
		<td class="fieldTitle">Status:</td>
		<td class="fieldValue">
				<select name="status">
					<option value="A" <%=("Active".equals(status)?"selected":"")%>>Active</option>
					<option value="C" <%=("Completed".equals(status)?"selected":"")%>>Completed</option>
					<option value="D" <%=("Deleted".equals(status)?"selected":"")%>>Deleted</option>
				</select>
				<input type="button" value="Update Status" onclick="update_status()"/>
		</td>
	</tr>
	<tr>
		<td class="fieldTitle">Message:</td>
		<td class="fieldValue"><c:out escapeXml="false" value="${tickler.message}"/></td>
	</tr>
</table>

<br/>

<table width="60%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr>
		<td colspan="3" class="title">Updates</td>
	</tr>
	<tr class="fieldTitle">
		<th>Date</th>
		<th>Provider</th>
		<th>Status</th>
	</tr>
	<c:forEach var="update" items="${tickler.updates}">
	<%
		provider_name="";
		status="";
		TicklerUpdate update = (TicklerUpdate)pageContext.getAttribute("update");
		if(update != null) {
			Provider provider = (Provider)update.getProvider();
			if(provider != null) {
				provider_name = provider.getLastName() + "," + provider.getFirstName();
			}
			switch(update.getStatus()) {
				case 'A': status="Active";break;
				case 'D': status="Deleted";break;
				case 'C': status="Completed";break;
			}
		}
	%>
		<tr>
			<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${update.update_date}"/></td>
			<td class="fieldValue"><%=provider_name %></td>
			<td class="fieldValue"><%=status %></td>
		</tr>
	</c:forEach>
</table>
<br/>
<table width="60%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr>
		<td colspan="3" class="title">Comments</td>
	</tr>
	<tr class="fieldTitle">
		<th>Date</th>
		<th>Provider</th>
		<th>Comment</th>
	</tr>
	<c:forEach var="comment" items="${tickler.comments}">
	<%
		provider_name="";
		TicklerComment comment = (TicklerComment)pageContext.getAttribute("comment");
		if(comment != null) {
			Provider provider = (Provider)comment.getProvider();
			if(provider != null) {
				provider_name = provider.getLastName() + "," + provider.getFirstName();
			}
		}
	%>
		<tr>
			<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${comment.update_date}"/></td>
			<td class="fieldValue"><%=provider_name %></td>
			<td class="fieldValue"><c:out value="${comment.message}"/></td>
		</tr>
	</c:forEach>
		<tr class="fieldValue" height="15">
			<td colspan="3" class="fieldValue">
			
			</td>
		</tr>
		<tr>
				<td class="fieldValue" colspan=3">
					<input type="text" size="50" name="comment"/>					
					<input type="button" value="Add Comment" onclick="add_comment()"/>
				</td>
		</tr>
</table>
<br/>
<table width="100%">
	<tr>
		<td><html:link action="Tickler.do?method=filter">Return to list</html:link></td>
	</tr>
</table>
	</html:form>
</body>
</html>