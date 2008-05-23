<%@ page import="org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean" %>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display-el" %>
<%@ include file="/common/messages.jsp"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedDemographicInfo"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.MatchingDemographicInfoResult"%>
<%@page import="java.util.HashMap"%>
<h3>New Client</h3>
<p>Please enter the following information. The system will try to determine if the client has already been entered into the system.</p>
<html:form action="/PMmodule/GenericIntake/Search" onsubmit="return validateSearchForm()">
<html:hidden property="method" />
<html:hidden property="demographicId" />

<table id="genericIntakeSearch" width="50%">
    <tr>
        <th>First Name</th>
        <td><html:text property="firstName" size="30" maxlength="30" /></td>
    </tr>
    <tr>
        <th>Last Name</th>
        <td><html:text property="lastName" size="30" maxlength="30" /></td>
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
            <html:text property="yearOfBirth" size="4" maxlength="4" />&nbsp;(YYYY)
        </td>
    </tr>
	<tr>
		<th> Gender</th>
		<td>
			<html-el:select property="gender">
				<html-el:option value="">Any</html-el:option>
				<c:forEach var="gen" items="${genders}">
					<html-el:option value="${gen.code}"><c:out value="${gen.description}"/></html-el:option>
				</c:forEach>
			</html-el:select>
		</td>
	</tr>
    <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
    <caisi:isModuleLoad moduleName="GET_OHIP_INFO" reverse="false">
        <tr>
            <th>Health Card</th>
            <td>
                <html:text property="healthCardNumber" size="10" maxlength="10" />&nbsp;
                <html:text property="healthCardVersion" size="2" maxlength="2"/>&nbsp;(version)
            </td>
        </tr>
    </caisi:isModuleLoad>
	</caisi:isModuleLoad>
    <tr>
        <td colspan="2">
            <br />
            <html:submit onclick="search()">Search</html:submit>
        </td>
    </tr>
</table>

<c:if test="${not empty requestScope.genericIntakeSearchForm.localMatches}">
    <h5>Local matches</h5>
    <p>The following possible matches were found in the system.</p>
    <p>If your client is not one of these matches, choose the 'New Client' button. Otherwise, you can update the existing client file.</p>

    <display-el:table class="simple" name="requestScope.genericIntakeSearchForm.localMatches" uid="client" pagesize="15" requestURI="/PMmodule/GenericIntake/Search.do">
        <display-el:setProperty name="paging.banner.placement" value="bottom" />

        <display-el:column style="width: 10%">

            <html-el:submit onclick="updateLocal('${client.demographicNo}')">Update Local</html-el:submit>


        </display-el:column>

        <display-el:column property="formattedName" sortable="true" title="Name" />
        <display-el:column property="formattedDob" sortable="true" title="Date of Birth" />

    </display-el:table>

</c:if>

<c:if test="${not empty requestScope.remoteMatches}">
    <%
		HashMap<Integer,String> facilitiesNameMap=(HashMap<Integer,String>)request.getAttribute("facilitiesNameMap");
	%>
	<input type="hidden" name="remoteFacilityId" />
	<input type="hidden" name="remoteDemographicId" />
	<script>
		function copyRemote(remoteFacilityId, remoteDemographicId)
		{
			document.forms[0].elements['remoteFacilityId'].value = remoteFacilityId;
			document.forms[0].elements['remoteDemographicId'].value = remoteDemographicId;
			document.forms[0].method.value='copyRemote';
		}
	</script>
   	<p>The following possible matches were found in the integrated community.</p>
    <display-el:table class="simple" name="requestScope.remoteMatches" id="x">
       	<%
       		MatchingDemographicInfoResult mdir=(MatchingDemographicInfoResult)pageContext.getAttribute("x");
       		CachedDemographicInfo cdi=mdir.getCachedDemographicInfo();
   			int facilityId=cdi.getFacilityDemographicPrimaryKey().getFacilityId();
			String facilityName=facilitiesNameMap.get(facilityId);
   		%>
        <display-el:setProperty name="paging.banner.placement" value="bottom" />

        <display-el:column title="">
        	<input type="submit" value="Copy to Local" onclick="copyRemote(<%=cdi.getFacilityId()%>,<%=cdi.getFacilityDemographicId()%>)" />
        </display-el:column>
        <display-el:column title="Facility Name">
        	<%=facilityName%>
        </display-el:column>
        <display-el:column title="Client Name" >
        	<c:out value="${x.cachedDemographicInfo.lastName}" />, <c:out value="${x.cachedDemographicInfo.firstName}" />
        </display-el:column>
        <display-el:column title="BirthDate">
        	<c:out value="${x.cachedDemographicInfo.birthDate.year}" />-<c:out value="${x.cachedDemographicInfo.birthDate.month}" />-<c:out value="${x.cachedDemographicInfo.birthDate.day}" />
        </display-el:column>
        <display-el:column property="cachedDemographicInfo.gender" title="Gender" />
        <display-el:column property="cachedDemographicInfo.hin" title="Health Number" />
        <display-el:column property="score" title="Matching Score" />
    </display-el:table>
</c:if>

<c:if test="${requestScope.genericIntakeSearchForm.searchPerformed}">
    <br />
    <p><html:submit onclick="createLocal()">New Client</html:submit></p>
    <br />
</c:if>
</html:form>
