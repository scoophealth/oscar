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

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<html:form action="/PMmodule/AgencyManager">
	<input type="hidden" name="method" />
    <script>
        function enableIntegrator() {
            document.agencyManagerForm.method.value='enable_integrator';
            if(confirm('By enabling this service, you agree to the terms and conditions....\nare you sure you would like to continue?')) {
                document.agencyManagerForm.submit();
            }
        }

        function disableIntegrator() {
            document.agencyManagerForm.method.value='disable_integrator';
            if(confirm('By disabling this service, you will remove all community functionality')) {
                document.agencyManagerForm.submit();
            }
        }
    </script>

    <!-- Show current status here -->
    <c:choose>
        <c:when test="${requestScope.integratorEnabled == false}">
            <h3 style="color:red">Service is currently disabled</h3>
        </c:when>
        <c:otherwise>
            <h3 style="color:red">Service is currently enabled</h3>
        </c:otherwise>
    </c:choose>
    <br />

    <div class="tabs" id="tabs">
        <table cellpadding="3" cellspacing="0" border="0">
            <tr>
                <th title="Programs">Integrator Information</th>
            </tr>
        </table>
    </div>
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <tr class="b">
            <td width="20%">Integrator URL:</td>
            <td><html:text property="agency.integratorUrl" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Integrator username:</td>
            <td><html:text property="agency.integratorUsername" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Integrator password:</td>
            <td><html:text property="agency.integratorPassword" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Share Issues and Notes:</td>
            <td><html:checkbox property="agency.shareNotes" /></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="button" value="Save" onclick="this.form.method.value='save';this.form.submit()" />
                <html:cancel />
            </td>
        </tr>
    </table>
    <br />
    <c:choose>
        <c:when test="${requestScope.integratorEnabled == false}">
            <input type="button" value="Enable Integrator Service" onclick="enableIntegrator();" />
        </c:when>
        <c:otherwise>
            <input type="button" value="Disable Integrator Service" onclick="disableIntegrator();" />
        </c:otherwise>
    </c:choose>
    <%@include file="/common/messages.jsp"%>

</html:form>
