<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@page import="java.util.List" %>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Schedule">Schedule Settings</th>
	</tr>
</table>
</div>

<table width="100%" border="1" cellspacing="2" cellpadding="3">

	<%
		List<String> myGroups = (List<String>)request.getAttribute("myGroups");
		List<String> allMyGroups = (List<String>)request.getAttribute("allMyGroups");
	%>	
	<tr class="b">
		<td width="20%">Schedule Groups:</td>
		<td>
			<%
				for(String g:allMyGroups) {
					String checked=getChecked(g,myGroups);
			%>
		
			<input name="checked_group" value="<%=g %>"
				type="checkbox" <%=checked %> />&nbsp;<%=g %>
			<br />
			<% } %>
		</td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Save"
			onclick="this.form.method.value='saveScheduleGroups';this.form.submit()" />
		<html:cancel /></td>
	</tr>
</table>


<%!
String getChecked(String g, List<String> col) {
	if(col.contains(g)) {
		return " checked=\"checked\" ";
	} 
	return "";
}
%>