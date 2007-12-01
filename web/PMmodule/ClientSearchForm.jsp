<%@ include file="/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>
<%@page import="oscar.OscarProperties" %>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@page import="java.util.*" %>
<%@page import="org.oscarehr.PMmodule.model.Demographic" %>
<%@page import="org.oscarehr.PMmodule.model.Program" %>

<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<script>
	function resetClientFields() {
		var form = document.clientSearchForm2;
		form.elements['criteria.firstName'].value='';
		form.elements['criteria.lastName'].value='';
		form.elements['criteria.dob'].value='';
		form.elements['criteria.healthCardNumber'].value='';
		form.elements['criteria.healthCardVersion'].value='';
		form.elements['criteria.searchOutsideDomain'].checked = true;
		form.elements['criteria.searchUsingSoundex'].checked = true;
		form.elements['criteria.dateFrom'].value=''; 
		form.elements['criteria.dateTo'].value=''; 
		form.elements['criteria.bedProgramId'].selectedIndex = 0;
	}

	function popupHelp(type) {
		alert('not yet implemented... will show term definitions');
	}
</script>
<html:form action="/PMmodule/ClientSearch2">
	<input type="hidden" name="method" value="search" />
	
	<br />
	
	<div id="projecthome" class="app">
		<div class="h4">
			<h4>Search client by entering search criteria below</h4>
		</div>
		<div class="axial">
			<table border="0" cellspacing="2" cellpadding="3">
				<tr>
					<th>Client First Name</th>
					<td><html:text property="criteria.firstName" size="15" /></td>
				</tr>
		
				<tr>
					<th>Client Last Name</th>
					<td><html:text property="criteria.lastName" size="15" /></td>
				</tr>
				
				<tr>
					<th>Date of Birth <br>
					(yyyy/mm/dd)</th>
					<td><html:text property="criteria.dob" size="15" /></td>
				</tr>
				
				<caisi:isModuleLoad moduleName="GET_OHIP_INFO" reverse="false">
				<tr>
					<th>Health Card Number</th>
					<td><html:text property="criteria.healthCardNumber" size="15" /> <html:text property="criteria.healthCardVersion" size="2" /></td>
				</tr>
			
				</caisi:isModuleLoad>
				<tr>
					<!--  <th>Search outside of domain <a href="javascript:void(0)" onclick="popupHelp('domain')">?</a></th>
					-->					
					<caisi:isModuleLoad moduleName="pmm.client.search.outside.of.domain.enabled" >
					<th>Search all clients <a href="javascript:void(0)" onclick="popupHelp('domain')">?</a></th>
						<td><html:checkbox property="criteria.searchOutsideDomain" /></td>
					</caisi:isModuleLoad>					
				</tr>
				
				<tr>
					<th>Soundex on names <a href="javascript:void(0)" onclick="popupHelp('soundex')">?</a></th>
					<td><html:checkbox property="criteria.searchUsingSoundex" /></td>
				</tr>

				<tr>
					<th>Bed Program</th>
			          <td>
			            <html:select property="criteria.bedProgramId">
			                <html:option value="">
			                </html:option>
			              	<html:options collection="allBedPrograms" property="id" labelProperty="name" />
			            </html:select>
			          </td>
				</tr>

				<tr>
					<th>Admission Date From<br>
						(yyyy/mm/dd)</th>
					<td><html:text property="criteria.dateFrom" size="12" /></td>
				</tr>
				<tr>
					<th>Admission Date To<br>
						(yyyy/mm/dd)</th>
					<td><html:text property="criteria.dateTo" size="12" /></td>
				</tr>
			</table>
			<table>
				<tr>
					<%
						String externalConsentPhrase=OscarProperties.getInstance().getProperty("EXTERNAL_PROVIDER_CONSENT_NOTIFICATION_PHRASE");
						boolean userHasExternalOrErClerkRole=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
						userHasExternalOrErClerkRole=userHasExternalOrErClerkRole || UserRoleUtils.hasRole(request, UserRoleUtils.Roles.er_clerk);
						if (externalConsentPhrase!=null && userHasExternalOrErClerkRole)
						{
							%>
							<td align="center"><input type="submit" value="search" name="search_with_consent" onclick="return confirm('<%=externalConsentPhrase%>')" /></td>
							<td align="center"><input type="submit" value="emergency" name="emergency_search" /></td>
							<%
						}
						else
						{
							%>
							<td align="center"><html:submit value="search" /></td>
							<%
						}
					%>
					<td align="center"><input type="button" name="reset" value="reset" onclick="javascript:resetClientFields();" /></td>
				</tr>
			</table>
		</div>
	</div>
	
	<br />

	<c:if test="${requestScope.clients != null}">
		<display:table class="simple" cellspacing="2" cellpadding="3" id="client" name="clients" export="false" pagesize="10" requestURI="/PMmodule/ClientSearch2.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="basic.msg.empty_list" value="No clients found." />
			
			<display:column sortable="true" title="Name">
				<a href="<html:rewrite action="/PMmodule/ClientManager.do"/>?id=<c:out value="${client.demographicNo}"/>&consent=<c:out value="${consent}"/>"><c:out value="${client.formattedName}" /></a>
			</display:column>
			<display:column sortable="true" title="Date of Birth">
				<c:out value="${client.yearOfBirth}" />/<c:out value="${client.monthOfBirth}" />/<c:out value="${client.dateOfBirth}" />
			</display:column>
			<display:column sortable="true" title="Master File Number">
				<c:out value="${client.demographicNo}" />
			</display:column>
		</display:table>
	</c:if>
</html:form>
