<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%-- Updated by Eugene Petruhin on 17 dec 2008 while fixing #2422864 & #2317933 & #2379840 --%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ include file="/ticklerPlus/header.jsp" %>

	<tr>
            <td class="searchTitle" colspan="4">View Tickler #<c:out value="${tickler.id}"/></td>
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
		document.ticklerForm.id.value=<c:out value="${tickler.id}"/>;
		document.ticklerForm.method.value='reassign';
		document.ticklerForm.submit();
	}

	function update_status() {
		document.ticklerForm.id.value=<c:out value="${tickler.id}"/>;
		document.ticklerForm.method.value='update_status';
		document.ticklerForm.submit();
	}
	
	function add_comment() {
		document.ticklerForm.id.value=<c:out value="${tickler.id}"/>;
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
		Demographic demographic = temp.getDemographic();
		if(demographic != null) {
	demographic_name = demographic.getLastName() + "," + demographic.getFirstName();
		}
		
		if (temp.getProgramId()!=null) {
	ProgramDao programDao=(ProgramDao)SpringUtils.getBean("programDao");
	Program program=programDao.getProgram(temp.getProgramId());
	program_name=program.getName();
		}
	
		Provider provider = temp.getProvider();
		if(provider != null) {
	provider_name = provider.getLastName() + "," + provider.getFirstName();
		}

		Provider assignee = temp.getAssignee();
		if(assignee != null) {
	assignee_id = assignee.getProviderNo();
	@SuppressWarnings("unchecked")
	java.util.List<Provider> providers = (java.util.List<Provider>)request.getAttribute("providers");
	boolean found=false;
	for(Provider p:providers) {
		if(p.getProviderNo().equals(assignee.getProviderNo())) {
	found=true;
	break;
		}
	}
	if(!found) {
		inactive_name = assignee.getLastName() + "," + assignee.getFirstName();
	}						
		}

		status = "Active";
		if(temp.getStatus().equals(Tickler.STATUS.C)) {
	status="Completed";
		}
		if(temp.getStatus().equals(Tickler.STATUS.D)) {
	status="Deleted";
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
		<td class="fieldValue"><a href="../demographic/demographiccontrol.jsp?demographic_no=<c:out value="${tickler.demographicNo}"/>&displaymode=edit&dboperation=search_detail" target="demographic"><%=demographic_name%></a></td>
	</tr>
	<tr>
		<td class="fieldTitle">Program:</td>
		<td class="fieldValue"><%=program_name%></td>
	</tr>
	<tr>
		<td class="fieldTitle">Provider:</td>
		<td class="fieldValue"><%=provider_name%></td>
	</tr>
	<tr>
		<td class="fieldTitle">Service Date:</td>
		<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${tickler.serviceDate}"/></td>
	</tr>
	<tr>
		<td class="fieldTitle">Date Created:</td>
		<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${tickler.updateDate}"/></td>
	</tr>
	<tr>
		<td class="fieldTitle">Priority:</td>
		<td class="fieldValue" <%=priorityStyle%>><c:out value="${tickler.priority}"/></td>
	</tr>
	<tr>
		<td class="fieldTitle">Task Assigned To:</td>
		<td class="fieldValue">
	            <html:select property="tickler.taskAssignedTo" value="<%=assignee_id%>">
					<%
						if (inactive_name.length() > 0) {
					%>
    	    		    <option value="<%=assignee_id%>" selected><%=inactive_name%></option>
					<%
						}
					%>
        		    <html:options collection="providers" property="providerNo" labelProperty="formattedName"/>
            	</html:select>
				<input type="button" value="Re-Assign Task" onclick="reassign_tickler()"/>
				<%
					if (inactive_name.length() > 0) {
				%>
   	    		    <span style='color: red'>Warning! <%=inactive_name%> is inactive.</span>
				<%
					}
				%>
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
		Provider provider = update.getProvider();
		if(provider != null) {
			provider_name = provider.getLastName() + "," + provider.getFirstName();
		}
		
		if(update.getStatus().equals(Tickler.STATUS.A)){
			status="Active";
		}
		if(update.getStatus().equals(Tickler.STATUS.C)) {
			status="Completed";
		}
		if(update.getStatus().equals(Tickler.STATUS.D)) {
			status="Deleted";
		}
		
			}
	%>
		<tr>
			<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${update.updateDate}"/></td>
			<td class="fieldValue"><%=provider_name%></td>
			<td class="fieldValue"><%=status%></td>
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
		Provider provider = comment.getProvider();
		if(provider != null) {
			provider_name = provider.getLastName() + "," + provider.getFirstName();
		}
			}
	%>
		<tr>
			<td class="fieldValue"><fmt:formatDate pattern="MM/dd/yy : hh:mm a" value="${comment.updateDate}"/></td>
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
