<%@ include file="/taglibs.jsp"%>

<%@ include file="/common/messages.jsp"%>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"><%-- don't close in 1 statement, will break IE7 --%></script>

<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Facility">Edit facility</th>
        </tr>
    </table>
</div>

<html:form action="/PMmodule/FacilityManager.do" onsubmit="return validateRequiredField('facilityName', 'Name', 32)">
    <html:hidden property="agencyId"/>
    <input type="hidden" name="method" value="save" />
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <tr class="b">
            <td width="20%">Facility Id:</td>
            <td><c:out value="${requestScope.id}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Name: *</td>
            <td><html:text property="facility.name" size="32" maxlength="32" styleId="facilityName" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Description:</td>
            <td><html:text property="facility.description" size="70" maxlength="70"/></td>
        </tr>
        <tr class="b">
            <td width="20%">HIC:</td>
            <td><html:checkbox property="facility.hic" /></td>
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
        <tr>
            <td colspan="2">
                <html:submit property="submit.save">Save</html:submit>
                <html:cancel property="submit.cancel">Cancel</html:cancel>
            </td>
        </tr>
    </table>
</html:form>
<div>
    <p><a href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=list" />Return to facilities list</a></p>
</div>
