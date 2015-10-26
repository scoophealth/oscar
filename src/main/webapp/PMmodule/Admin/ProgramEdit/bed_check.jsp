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

<html:hidden property="addTime" />
<html:hidden property="removeId" />

<script type="text/javascript">
	function addBedCheckTime(id) {
		var addPopupTimePicker = dojo.widget.byId("addPopupTimePicker");
		
		document.programManagerForm.addTime.value = 
			addPopupTimePicker.timePicker.selectedTime['hour'] + ":" + 
			addPopupTimePicker.timePicker.selectedTime['minute'] + " " + 
			addPopupTimePicker.timePicker.selectedTime['amPm'];
		
		if (!dojo.validate.is12HourTime(document.programManagerForm.addTime.value)) {
			return false;
		}

		document.programManagerForm.method.value = 'addBedCheckTime';
		document.programManagerForm.submit();
	}
	
	function removeBedCheckTime(id) {
		document.programManagerForm.removeId.value = id;
		document.programManagerForm.method.value = 'removeBedCheckTime';
		document.programManagerForm.submit();
	}
</script>

<table width="100%" summary="Edit bed check">
	<tr>
		<td>
		<div class="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Times</th>
			</tr>
		</table>
		</div>
		<display:table class="simple"
			name="sessionScope.programManagerForm.bedCheckTimes"
			uid="bedCheckTime" requestURI="/PMmodule/ProgramManager.do">
			<display:column property="time" format="{0, time, short}" title="" />
			<display:column>
				<input type="button" value="Remove"
					onclick="removeBedCheckTime('<c:out value="${bedCheckTime.id}"/>');" />
			</display:column>
		</display:table></td>
	</tr>
</table>
<table>
	<tr>
		<td><input id="addPopupTimePicker" dojoType="DropdownTimePicker"
			readonly="true" /> <input type="button" value="Add"
			onclick="addBedCheckTime();" /></td>
	</tr>
</table>
