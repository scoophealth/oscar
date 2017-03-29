
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




<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ page import="org.oscarehr.common.model.*"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@ page import="java.util.*"%>

<script>
	function facility_change(facility_id) {
	
		if (document.getElementById("check_box_"+facility_id).checked)
		{
			<%--alert('add_to_facility:' + facility_id);--%>
			document.staffManagerForm.method.value='add_to_facility';
		}
		else
		{
			<%--alert('remove_from_facility:' + facility_id);--%>		
			document.staffManagerForm.method.value='remove_from_facility';
		}
		
		document.staffManagerForm.elements['facility_id'].value=facility_id;
		document.staffManagerForm.submit();
	}
</script>

<input type="hidden" name="facility_id" />
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Facilities">Facilities</th>
	</tr>
</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<tr>
		<th style="color: black">Assigned To</th>
		<th style="color: black">Facility Name</th>
	</tr>
	<%
		List<Integer> providerFacilityIds=(List)request.getAttribute("providerFacilities");
	%>
	<c:forEach var="facility" items="${all_facilities}">
		<%
			String checked="";
			Facility facility=(Facility)pageContext.getAttribute("facility");
			if (providerFacilityIds.contains(facility.getId())) checked="checked=\"checked\"";			
		%>
		<tr>
			<td><input id="check_box_<c:out value='${facility.id}'/>"
				type="checkbox" <%=checked%>
				onchange="facility_change(<c:out value='${facility.id}'/>)" /></td>
			<td><c:out value="${facility.name}" /></td>
	</c:forEach>
</table>
