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

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>


<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
	<%authed=false; %>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<html:html locale="true">
    <head>
        <title>Edit Facility</title>
        <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/tigris.css" />' />
        <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/displaytag.css" />' />

        <script type="text/javascript"
            src="<%=request.getContextPath()%>/js/validation.js">
        </script>
        <script type="text/javascript">
            function validateForm()
            {
                if (bCancel) return bCancel;

                var isOk = false;
                isOk = validateRequiredField('facilityName', 'Facility Name', 32);
                if (isOk) isOk = validateRequiredField('facilityDesc', 'Facility Description', 70);
//                if (isOk) isOk = validateUpdateInterval();
                if (isOk) isOk = validateRemoveDemoId();
                return isOk;
            }

            function validateUpdateInterval()
            {
                var ret = false;
                var interval = document.forms[0].updateInterval.value;
                if (!isInteger(interval)) {
                    alert("Integrator Update Interval must be an integer!");
                } else if (parseInt(interval)<1) {
                    alert("Integrator Update Interval must be > 0");
                } else {
                    ret = true;
                }
                return ret;
            }

            function validateRemoveDemoId() {
                var ret = true;
                var rid = document.forms[0].removeDemographicIdentity.checked;
                if (!rid) {
                    ret = confirm("Remove Demographic Identity NOT checked! Is it OK?");
                }
                return ret;
            }
        </script>
        <!-- don't close in 1 statement, will break IE7 -->
        
    </head>
    <body>
        <h1>Edit Facility</h1>
        <html:form action="/FacilityManager.do"
            onsubmit="return validateForm();">
            <input type="hidden" name="method" value="save" />
            <html:hidden property="facility.orgId" />
            <html:hidden property="facility.sectorId" />
<!-- Ronnie
            < :hidden property="facility.ocanServiceOrgNumber" />
-->
            <table width="100%" border="1" cellspacing="2" cellpadding="3">
                <tr class="b">
                    <td>Facility Id:</td>
                    <td><c:out value="${requestScope.id}" /></td>
                </tr>
                <tr class="b">
                    <td>Name: *</td>
                    <td><html:text property="facility.name" size="32"
                           maxlength="32" styleId="facilityName" /></td>
                </tr>
                <tr class="b">
                    <td>Description: *</td>
                    <td><html:text property="facility.description" size="60"
                           maxlength="70" styleId="facilityDesc" /></td>
                </tr>
                <tr class="b">
					<td width="20%">Enable Digital Signatures:</td>
					<td><html:checkbox property="facility.enableDigitalSignatures" /></td>
				</tr>
                <tr class="b">
                    <td>Enable Integrator:</td>
                    <td><html:checkbox property="facility.integratorEnabled" /></td>
                </tr>
                <tr class="b">
                    <td>Integrator Url:</td>
                    <td><html:text property="facility.integratorUrl" size="40" /></td>
                </tr>
                <tr class="b">
                    <td>Integrator User:</td>
                    <td><html:text property="facility.integratorUser" /></td>
                </tr>
                <tr class="b">
                    <td>Integrator Password:</td>
                    <td><html:password property="facility.integratorPassword" /></td>
                </tr>
                <tr class="b">
                    <td>Remove Demographic Identity:</td>
                    <td>
                        <html:checkbox property="removeDemographicIdentity" />
                        (All patients' names, hin# & sin# will be removed in Integrator)
                        <br>
                    </td>
                </tr>
                
		<tr class="b">
			<td>Rx Interaction Warning Level:</td>
			<td>
				<html:select property="facility.rxInteractionWarningLevel">
					<html:option value="0">Not Specified</html:option>
					<html:option value="1">Low</html:option>
					<html:option value="2">Medium</html:option>
					<html:option value="3">High</html:option>
					<html:option value="4">None</html:option>
				</html:select>
			</td>
		</tr>                
<!--Ronnie
                </tr>
                <tr class="b">
                    <td>Integrator Update Interval:</td>
                    <td>
                        <html:text property="updateInterval" size="3" />
                        Hour(s)
                        <br>
                    </td>
                </tr>
-->
                <tr>
                    <td colspan="2">* Mandatory fields</td>
                <tr>
                    <td colspan="2"><html:submit property="submit.save" onclick="bCancel=false;">Save</html:submit>
                        <html:cancel>Cancel</html:cancel></td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>
