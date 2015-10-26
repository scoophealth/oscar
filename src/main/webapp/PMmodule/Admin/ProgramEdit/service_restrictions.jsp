<%@ taglib uri="http://displaytag.sf.net/el" prefix="display-el" %>
<%@ page import="org.oscarehr.PMmodule.model.ProgramClientRestriction" %>
<%@ page import="org.oscarehr.common.model.Provider" %>

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

<script>
	function save() {
		var maxDays = document.programManagerForm.elements['program.maximumServiceRestrictionDays'].value;
		if(maxDays != undefined && isNaN(maxDays)) {
			alert("Maximum length of service restriction '" + maxDays + "' is not a number");
			return false;
		}

        var defDays = document.programManagerForm.elements['program.defaultServiceRestrictionDays'].value;
		if(isNaN(defDays)) {
			alert("Default length of service restrcition '" + defDays + "' is not a number");
			return false;
		}

        document.programManagerForm.method.value='save_restriction_settings';
		document.programManagerForm.submit()
	}

</script>

<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Service Restrictions">Service Restriction Settings</th>
        </tr>
    </table>
</div>
Please define the following parameters control the behaviour of new service restrictions for this program.
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Maximum length of service restriction (in days):</td>
		<td><html:text property="program.maximumServiceRestrictionDays" size="4" maxlength="4"/>&nbsp;(empty or zero means no maximum)</td>
	</tr>
	<tr class="b">
		<td width="20%">Default service restriction length (in days):</td>
		<td><html:text property="program.defaultServiceRestrictionDays" size="4" maxlength="4"/></td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="button" value="Save" onclick="return save()" />
		</td>
	</tr>
</table>
<br/>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Service Restrictions">Current Service Restrictions</th>
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
        <caisirole:SecurityAccess accessName="Disable service restriction" accessType="access" providerNo='<%=((Provider)request.getSession().getAttribute("provider")).getProviderNo()%>' demoNo="<%=demographicNo%>" programId='<%=request.getParameter("id")%>'>
            <a onclick="disableRestriction('<c:out value="${restriction.id}"/>');return false;" href="javascript:void(0);"> Disable </a>
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
        <caisirole:SecurityAccess accessName="Create service restriction" accessType="access" providerNo='<%=((Provider)request.getSession().getAttribute("provider")).getProviderNo()%>' demoNo="<%=demographicNo%>" programId='<%=request.getParameter("id")%>'>
            <a onclick="enableRestriction('<c:out value="${restriction.id}"/>');return false;" href="javascript:void(0);"> Enable </a>
        </caisirole:SecurityAccess>
    </display-el:column>
    <display-el:column property="id" sortable="true" title="Id" />
    <display-el:column property="client.formattedName" sortable="true" title="Client" />
    <display-el:column property="provider.formattedName" sortable="true" title="Restricted By"/>
    <display-el:column property="comments" sortable="true" title="Comments" />
    <display-el:column property="startDate" sortable="true" title="Start date" />
    <display-el:column property="endDate" sortable="true" title="End date" />
</display-el:table>
