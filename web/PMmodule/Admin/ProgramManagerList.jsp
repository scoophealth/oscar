<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>
<script>
	function ConfirmDelete(name)
	{
		if(confirm("Are you sure you want to delete " + name + " ?")) {
			return true;
		}
		return false;
	}
</script>
<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Programs</th>
		</tr>
	</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="program" name="programs" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="paging.banner.item_name" value="program" />
	<display:setProperty name="paging.banner.items_name" value="programs" />
	<display:setProperty name="basic.msg.empty_list" value="No programs found." />
	
	<display:column sortable="false" title="">
		<a onclick="return ConfirmDelete('<c:out value="${program.name}"/>')" href="<html:rewrite action="/PMmodule/ProgramManager.do"/>?method=delete&id=<c:out value="${program.id}"/>&name=<c:out value="${program.name}"/>"> Delete </a>
	</display:column>
	<display:column sortable="false" title="">
		<a href="<html:rewrite action="/PMmodule/ProgramManager.do"/>?method=edit&id=<c:out value="${program.id}" />"> Edit </a>
	</display:column>
	<display:column sortable="true" title="Name">
		<a href="<html:rewrite action="/PMmodule/ProgramManagerView.do"/>?id=<c:out value="${program.id}" />"> <c:out value="${program.name}" /> </a>
	</display:column>
	<display:column property="descr" sortable="true" title="Description" />
	<display:column property="type" sortable="true" title="Type" />
	<display:column property="location" sortable="true" title="Location" />
	<display:column sortable="true" title="Participation">
		<c:out value="${program.numOfMembers}" />/<c:out value="${program.maxAllowed}" />&nbsp;(<c:out value="${program.queueSize}" /> waiting)
	</display:column>
</display:table>