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
		<th title="Functional Centres">Functional Centre Management</th>
	</tr>
</table>
</div>
<html:form action="/PMmodule/FunctionalCentreManager.do">
	<display:table class="simple" cellspacing="2" cellpadding="3"
		id="functionalCentre" name="functionalCentres" export="false" pagesize="0"
		requestURI="/PMmodule/FunctionalCentreManager.do">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="agency" />
		<display:setProperty name="paging.banner.items_name"
			value="functionalCentres" />
		<display:setProperty name="basic.msg.empty_list"
			value="No Functional Centres found." />

		
		<display:column sortable="false" title="">
			<a
				href="<html:rewrite action="/PMmodule/FunctionalCentreManager.do"/>?method=edit&id=<c:out value="${functionalCentre.accountId}" />">
			Edit </a>
		</display:column>
		
		<display:column property="accountId" sortable="true" title="Functional Centre ID" />
		<display:column property="description" sortable="true"
			title="Description" />
		
	</display:table>
</html:form>
<div>
<p><a
	href="<html:rewrite action="/PMmodule/FunctionalCentreManager.do"/>?method=add">
Add new FunctionalCentre </a></p>
</div>
