<%@ page import="org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean" %>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display-el" %>
<%@ include file="/common/messages.jsp"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<h3>New Client</h3>
<p>Please enter the following information. The system will try to determine if the client has already been entered into the system.</p>
<html:form action="/PMmodule/GenericIntake/Search" onsubmit="return validateSearchForm()">
<html:hidden property="method" />
<html:hidden property="demographicId" />
<html:hidden property="remoteAgency" />
<html:hidden property="remoteAgencyDemographicNo" />

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
    <caisi:isModuleLoad moduleName="GET_OHIP_INFO" reverse="false">
        <tr>
            <th>Health Card</th>
            <td>
                <html:text property="healthCardNumber" size="10" maxlength="10" />&nbsp;
                <html:text property="healthCardVersion" size="2" maxlength="2"/>&nbsp;(version)
            </td>
        </tr>
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
<c:if test="${not empty requestScope.genericIntakeSearchForm.remoteMatches}">
    <h5>Remote matches</h5>
    <p>The following matches were found by using the Integrator.
        We cannot provide any assurance that the information enclosed is accurate, complete, or up-to-date for any particular purpose.
        Please verify this information before relying upon it.
        We do not assume responsibility for the consequences of any reliance on this information.
    </p>
    <display-el:table class="simple" name="requestScope.genericIntakeSearchForm.remoteMatches" id="client" pagesize="5">
        <display-el:setProperty name="paging.banner.placement" value="bottom" />


        <c:set var="nestedName" value="requestScope.genericIntakeSearchForm.remoteMatches[${client_rowNum - 1}].demographics" />

        <display-el:column sortable="true" title="Client #" property="id" style="width: 10%"/>
        <display-el:column>
            <display-el:table class="simple sublist" name="${nestedName}" id="demographic">
                <display-el:column style="width: 10%">
                    <c:if test="${demographic.agency.username != requestScope.genericIntakeSearchForm.localAgencyUsername}">
                        <html-el:submit onclick="copyRemote('${demographic.agency.username}', '${demographic.agencyDemographicNo}')">Copy Remote</html-el:submit>
                    </c:if>                   
                </display-el:column>

                <display-el:column property="agency.name" sortable="true" title="Agency" />
                <display-el:column sortable="true" title="Name">
                    <c:out value="${demographic.lastName}"/>, <c:out value="${demographic.firstName}"/>
                </display-el:column>

                <display-el:column sortable="true" title="Date of Birth">
                    <c:out value="${demographic.yearOfBirth}"/>-<c:out value="${demographic.monthOfBirth}"/>-<c:out value="${demographic.dateOfBirth}"/>
                </display-el:column>
            </display-el:table>
        </display-el:column>
    </display-el:table>
    <h5>
        We cannot provide any assurance that the information enclosed is accurate, complete, or up-to-date for any particular purpose.
        Please verify this information before relying upon it.
        We do not assume responsibility for the consequences of any reliance on this information.
    </h5>
</c:if>
<c:if test="${requestScope.genericIntakeSearchForm.searchPerformed}">
    <br />
    <p><html:submit onclick="createLocal()">New Client</html:submit></p>
    <br />
</c:if>
</html:form>
