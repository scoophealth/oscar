<%@ include file="/taglibs.jsp"%>

<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Facility">Facility summary</th>
        </tr>
    </table>
</div>

<html:form action="/PMmodule/FacilityManager.do">
    <input type="hidden" name="method" value="save" />
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <tr class="b">
            <td width="20%">Facility Id:</td>
            <td><c:out value="${requestScope.id}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Name:</td>
            <td><c:out value="${requestScope.facilityManagerForm.facility.name}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Description:</td>
            <td><c:out value="${requestScope.facilityManagerForm.facility.description}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">HIC:</td>
            <td><c:out value="${requestScope.facilityManagerForm.facility.hic}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Name:</td>
            <td><c:out value="${requestScope.facilityManagerForm.facility.contactName}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Email:</td>
            <td><c:out value="${requestScope.facilityManagerForm.facility.contactEmail}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Phone:</td>
            <td><c:out value="${requestScope.facilityManagerForm.facility.contactPhone}" /></td>
        </tr>

    </table>
</html:form>
<div>
    <p><a href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=list"/>Return to facilities list</a></p>
</div>
