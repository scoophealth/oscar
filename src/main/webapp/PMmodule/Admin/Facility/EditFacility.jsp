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

<%@ include file="/common/messages.jsp"%>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/validation.js">
</script>
<script type="text/javascript">

	//Check if string is a whole number(digits only).
	var isWhole_re       = /^\s*\d+\s*$/;
	function isWhole (s) {
	   var result =  String(s).search (isWhole_re) != -1
	   if(s.trim().length > 0 && !result) {
			alert('Default Client ID must be a number');   
	   }
	   return result;
	}


	function validateForm()
	{
		if (bCancel == true)
			return confirm("Do you really want to Cancel?");
		var isOk = false;
		isOk = validateRequiredField('facilityName', 'Facility Name', 32);
		if (isOk) isOk = validateRequiredField('facilityDesc', 'Facility Description', 70);
		if(isOk) isOk = isWhole($("input[name='facility.assignNewVacancyTicklerDemographic']").val()) || $("input[name='facility.assignNewVacancyTicklerDemographic']").val() == '';
		
		return isOk;
	}
</script>
<!-- don't close in 1 statement, will break IE7 -->

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Facility">Edit facility</th>
	</tr>
</table>
</div>

<html:form action="/PMmodule/FacilityManager.do"
	onsubmit="return validateForm();">
	<input type="hidden" name="method" value="save" />
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="20%">Facility Id:</td>
			<td><c:out value="${requestScope.id}" /></td>

		</tr>
		<tr class="b">
			<td width="20%">Name: *</td>
			<td><html:text property="facility.name" size="32" maxlength="32"
				styleId="facilityName" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Description: *</td>
			<td><html:text property="facility.description" size="70"
				maxlength="70" styleId="facilityDesc" /></td>
		</tr>
		<tr class="b">
			<td width="20%">HIC:</td>
			<td><html:checkbox property="facility.hic" /></td>
		</tr>
		<tr class="b">
			<td width="20%">OCAN Service Org Number:</td>
			<td><html:text property="facility.ocanServiceOrgNumber" size="5" maxlength="5"
				styleId="ocanServiceOrgNumber" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Primary Contact Name:</td>
			<td><html:text property="facility.contactName" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Primary Contact Email:</td>
			<td><html:text property="facility.contactEmail" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Primary Contact Phone:</td>
			<td><html:text property="facility.contactPhone" /></td>
		</tr>
		<%
        	Integer orgId = (Integer)request.getAttribute("orgId");
        	Integer sectorId = (Integer)request.getAttribute("sectorID");

        %>
		<tr class="b">
			<td width="20%">Organization:</td>
			<td><select name="facility.orgId">
				<option value="0">&nbsp;</option>
				<c:forEach var="org" items="${orgList}">
					<c:choose>
						<c:when test="${orgId == org.code }">
							<option value="<c:out value="${org.code}"/>" selected><c:out
								value="${org.description}" /></option>
						</c:when>
						<c:otherwise>
							<option value="<c:out value="${org.code}"/>"><c:out
								value="${org.description}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select></td>
		</tr>
		<tr class="b">
			<td width="20%">Sector:</td>
			<td><select name="facility.sectorId">
				<option value="0">&nbsp;</option>
				<c:forEach var="sector" items="${sectorList}">
					<c:choose>
						<c:when test="${sectorId == sector.code }">
							<option value="<c:out value="${sector.code}"/>" selected><c:out
								value="${sector.description}" /></option>
						</c:when>
						<c:otherwise>
							<option value="<c:out value="${sector.code}"/>"><c:out
								value="${sector.description}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Digital Signatures:</td>
			<td><html:checkbox property="facility.enableDigitalSignatures" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Integrator:</td>
			<td><html:checkbox property="facility.integratorEnabled" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Integrator Url:</td>
			<td><html:text property="facility.integratorUrl" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Integrator User:</td>
			<td><html:text property="facility.integratorUser" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Integrator Password:</td>
			<td><html:password property="facility.integratorPassword" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Allow SIMS Integration:</td>
			<td><html:checkbox property="facility.allowSims" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Integrated Referrals:</td>
			<td><html:checkbox property="facility.enableIntegratedReferrals" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Health Number Registry:</td>
			<td><html:checkbox property="facility.enableHealthNumberRegistry" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable OCAN Forms:</td>
			<td><html:checkbox property="facility.enableOcanForms" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable CBI Form:</td>
			<td><html:checkbox property="facility.enableCbiForm" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Anonymous Clients:</td>
			<td><html:checkbox property="facility.enableAnonymous" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Phone Encounter Clients:</td>
			<td><html:checkbox property="facility.enablePhoneEncounter" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Group Notes:</td>
			<td><html:checkbox property="facility.enableGroupNotes" /></td>
		</tr>
		
		<tr class="b">
			<td width="20%">Assign vacancy withdrawn tickler notification:</td>
			<td>
				<html:select property="facility.vacancyWithdrawnTicklerProvider">
					<html:option value="">Select Below</html:option>
					<%for(Provider p : providerDao.getActiveProviders()) { %>
						<html:option value="<%=p.getProviderNo() %>"><%=p.getFormattedName() %></html:option>
					<% } %>
				</html:select>
				&nbsp;Default client ID:&nbsp;
				<html:text property="facility.vacancyWithdrawnTicklerDemographic"/>
			</td>
		</tr>
		
		<tr class="b">
			<td width="20%">Assign new vacancy tickler notification to:</td>
			<td>
				<html:select property="facility.assignNewVacancyTicklerProvider">
					<html:option value="">Select Below</html:option>
					<%for(Provider p : providerDao.getActiveProviders()) { %>
						<html:option value="<%=p.getProviderNo() %>"><%=p.getFormattedName() %></html:option>
					<% } %>
				</html:select>
				&nbsp;Default client ID:&nbsp;
				<html:text property="facility.assignNewVacancyTicklerDemographic"/>
			</td>
		</tr>
		
		<tr class="b">
			<td width="20%">Assign notification of rejected applicant from a vacancy:</td>
			<td>
				<html:select property="facility.assignRejectedVacancyApplicant">
					<html:option value="">Select Below</html:option>
					<%for(Provider p : providerDao.getActiveProviders()) { %>
						<html:option value="<%=p.getProviderNo() %>"><%=p.getFormattedName() %></html:option>
					<% } %>
				</html:select>
				
			</td>
		</tr>
		
		<tr class="b">
            <td width="20%">Registration Intake</td>
            <td>
                <html:select property="facility.registrationIntake">
                    <html:option value="-1">Null</html:option>
                    <html:optionsCollection property="registrationIntakeForms" label="formName" value="id"/>
                </html:select>
            </td>
		</tr>
		<tr class="b">
            <td width="20%">Display All vacancies</td>
            <td>
                <html:select property="facility.displayAllVacancies">                    
                    <html:option value="1">All vacancies in all facilities</html:option>
					<html:option value="0">All vacancies in users facility program domain</html:option>
			</html:select>
            </td>
		</tr>
		
		<tr class="b">
			<td width="20%">Enable Mandatory Encounter Time in Encounter:</td>
			<td><html:checkbox property="facility.enableEncounterTime" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Enable Mandatory Transportation Time in Encounter:</td>
			<td><html:checkbox property="facility.enableEncounterTransportationTime" /></td>
		</tr>

		<tr class="b">
			<td width="20%">Rx Interaction Warning Level:</td>
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

		<tr>
			<td colspan="2"><html:submit property="submit.save" onclick="bCancel=false;">Save</html:submit>
			<html:cancel>Cancel</html:cancel></td>
		</tr>
	</table>
</html:form>
<div>
<p><a
	href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=list" >Return
to facilities list</a></p>
</div>
