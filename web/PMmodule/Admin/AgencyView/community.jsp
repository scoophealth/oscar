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

<br />
<h4>Integrator Version: <c:out value="${integrator_version}" /></h4>
<br />
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Agencies</th>
	</tr>
</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="agency" name="agencies" export="false" pagesize="0" requestURI="/PMmodule/AgencyManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="paging.banner.item_name" value="agency" />
	<display:setProperty name="paging.banner.items_name" value="agencies" />
	<display:setProperty name="basic.msg.empty_list" value="No agencies found or Integrator is not enabled." />
	<display:column property="id" sortable="true" title="Id" />
	<display:column property="name" sortable="true" title="Name" />
	<display:column property="description" sortable="true" title="Description" />
	<display:column sortable="true" title="Contact Info">
		<c:out value="${agency.contactName}" />
		<br />
		<c:out value="${agency.contactEmail}" />
		<br />
		<c:out value="${agency.contactPhone}" />
	</display:column>
</display:table>
<br />
<input type="button" value="Refresh Client Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_clients'" />
<input type="button" value="Refresh Program Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_programs'" />
<input type="button" value="Refresh Program Participation Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_admissions'" />
<input type="button" value="Refresh Provider Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_providers'" />
<input type="button" value="Refresh Referral Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_referrals'" />
