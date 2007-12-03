<%@ taglib uri="http://displaytag.sf.net/el" prefix="display-el" %>
<%@ page import="org.oscarehr.PMmodule.model.ProgramClientRestriction" %>
<%@ page import="org.oscarehr.PMmodule.model.Provider" %>
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
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Service Restrictions">Active Service Restrictions</th>
        </tr>
    </table>
</div>
<script type="text/javascript">
    function disableRestriction(id) {
        document.programManagerForm.elements['restriction.id'].value = id;
        document.programManagerForm.method.value='disable_restriction';
        document.programManagerForm.submit();
    }

    function enableRestriction(id) {
        document.programManagerForm.elements['restriction.id'].value = id;
        document.programManagerForm.method.value='enable_restriction';
        document.programManagerForm.submit();
    }
</script>
<html:hidden property="restriction.id" />

<display-el:table class="simple" cellspacing="2" cellpadding="3" id="restriction" name="service_restrictions" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
    <display-el:setProperty name="paging.banner.placement" value="bottom" />
    <display-el:setProperty name="basic.msg.empty_list" value="No service restrictions currently in place for this program." />

    <display-el:column sortable="false">
        <%
            String demographicNo = "" + ((ProgramClientRestriction)pageContext.getAttribute("restriction")).getDemographicNo();
        %>
        <caisirole:SecurityAccess accessName="Disable service restriction" accessType="access" providerNo="<%=((Provider)request.getSession().getAttribute("provider")).getProvider_no()%>" demoNo="<%=demographicNo%>" programId="<%=request.getParameter("id")%>">
            <a onclick="disableRestriction('<c:out value="${restriction.id}"/>');" href="javascript:void(0);"> Disable </a>
        </caisirole:SecurityAccess>
    </display-el:column>
    <display-el:column property="id" sortable="true" title="Id" />
    <display-el:column property="client.formattedName" sortable="true" title="Client" />
    <display-el:column property="provider.formattedName" sortable="true" title="Restricted By"/>
    <display-el:column property="comments" sortable="true" title="Comments" />
    <display-el:column property="startDate" sortable="true" title="Start date" />
    <display-el:column property="endDate" sortable="true" title="End date" />
</display-el:table>

<br/>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Service Restrictions">Disabled Service Restrictions</th>
        </tr>
    </table>
</div>

<display-el:table class="simple" cellspacing="2" cellpadding="3" id="restriction" name="disabled_service_restrictions" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
    <display-el:setProperty name="paging.banner.placement" value="bottom" />
    <display-el:setProperty name="basic.msg.empty_list" value="No service restrictions currently in place for this program." />

    <display-el:column sortable="false">
        <%
            String demographicNo = "" + ((ProgramClientRestriction)pageContext.getAttribute("restriction")).getDemographicNo();
        %>
        <caisirole:SecurityAccess accessName="Create service restriction" accessType="access" providerNo="<%=((Provider)request.getSession().getAttribute("provider")).getProvider_no()%>" demoNo="<%=demographicNo%>" programId="<%=request.getParameter("id")%>">
            <a onclick="enableRestriction('<c:out value="${restriction.id}"/>');" href="javascript:void(0);"> Enable </a>
        </caisirole:SecurityAccess>
    </display-el:column>
    <display-el:column property="id" sortable="true" title="Id" />
    <display-el:column property="client.formattedName" sortable="true" title="Client" />
    <display-el:column property="provider.formattedName" sortable="true" title="Restricted By"/>
    <display-el:column property="comments" sortable="true" title="Comments" />
    <display-el:column property="startDate" sortable="true" title="Start date" />
    <display-el:column property="endDate" sortable="true" title="End date" />
</display-el:table>

