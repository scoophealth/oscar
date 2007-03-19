<%@ include file="/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>
<h3>New Client</h3>
<p>Please enter the following information. The system will try to determine if the client has already been entered into the system.</p>
<html:form action="/PMmodule/GenericIntake/Search" onsubmit="return validateSearchForm()">
	<html:hidden property="method" />
	<html:hidden property="clientId" />
	<html:hidden property="agencyId" />
	
	<table id="genericIntakeSearch" width="50%">
		<tr>
			<th>First Name</th>
			<td><html:text property="firstName" /></td>
		</tr>
		<tr>
			<th>Last Name</th>
			<td><html:text property="lastName" /></td>
		</tr>
		<tr>
			<th>Birth Date</th>
			<td>
				<html:select property="monthOfBirth">
					<html:optionsCollection property="months" value="value" label="label" />
				</html:select>&nbsp;
				<html:select property="dayOfBirth">
					<html:optionsCollection property="days" value="value" label="label" />
				</html:select>&nbsp;
				<html:text property="yearOfBirth" size="4" />&nbsp;(YYYY)
			</td>
		</tr>
		<tr>
			<th>Health Card</th>
			<td>
				<html:text property="healthCardNumber" size="10" />&nbsp;
				<html:text property="healthCardVersion" size="2" />&nbsp;(version)
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<html:submit onclick="search()">Search</html:submit>
			</td>
		</tr>
	</table>
	<c:if test="${not empty requestScope.genericIntakeSearchForm.matches}">
		<br />
		<p>The following possible matches were found in the system.</p>
		<p>If your client is not one of these matches, choose the 'New Client' button. Otherwise, you can update the existing client file.</p>
		<display:table class="simple" name="requestScope.genericIntakeSearchForm.matches" uid="client" pagesize="15" requestURI="/PMmodule/GenericIntake/Search.do">
			<display:caption>Results from: <c:out value="${requestScope.genericIntakeSearchForm.matchType}" /></display:caption>
			<display:setProperty name="paging.banner.placement" value="bottom" />
			
			<display:column style="width: 10%">
				<c:choose>
					<c:when test="${client.agencyId == 0 or client.agencyId == applicationScope.agency.id}">
						<html-el:submit onclick="updateLocal('${client.demographicNo}')">Update Local</html-el:submit>
					</c:when>
					<c:otherwise>
						<html-el:submit onclick="copyRemote('${client.agencyId}', '${client.demographicNo}')">Copy Remote</html-el:submit>
					</c:otherwise>
				</c:choose>
			</display:column>
			
			<display:column property="agencyName" sortable="true" title="Agency" />
			<display:column property="formattedName" sortable="true" title="Name" />
			<display:column property="formattedDob" sortable="true" title="Date of Birth" />
			
			<display:column title="EMPI Links">
				<span style="text-decoration:underline" title="<c:out value="${client.formattedLinks}"/>">
					<c:out value="${client.numLinks}" />
				</span>
			</display:column>
		</display:table>
		<br />
		<p><html:submit onclick="createLocal()">New Client</html:submit></p>
		<br />
		<c:if test="${requestScope.genericIntakeSearchForm.remoteMatch}">
			<h5>
				We cannot provide any assurance that the information enclosed is accurate, complete, or up-to-date for any particular purpose.
				Please verify this information before relying upon it.
				We do not assume responsibility for the consequences of any reliance on this information.
			</h5>
		</c:if>
	</c:if>
</html:form>