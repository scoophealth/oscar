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
            <td><c:out value="${facilityManagerForm.facility.description}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">HIC:</td>
            <td><c:out value="${facilityManagerForm.facility.hic}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Name:</td>
            <td><c:out value="${facilityManagerForm.facility.contactName}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Email:</td>
            <td><c:out value="${facilityManagerForm.facility.contactEmail}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Phone:</td>
            <td><c:out value="${facilityManagerForm.facility.contactPhone}" /></td>
        </tr>


    </table>

    <div class="tabs" id="tabs">
        <table cellpadding="3" cellspacing="0" border="0">
            <tr>
                <th title="Associated programs">Associated programs</th>
            </tr>
        </table>
    </div>
    <display:table class="simple" cellspacing="2" cellpadding="3" id="program" name="associatedPrograms" export="false" requestURI="/PMmodule/FacilityManager.do">
        <display:setProperty name="basic.msg.empty_list" value="No programs." />
        <display:column sortable="true" sortProperty="name" title="Program Name">
            <a href="<html:rewrite action="/PMmodule/ProgramManagerView"/>?id=<c:out value="${program.id}"/>"><c:out value="${program.name}" /></a>
        </display:column>
        <display:column property="type" sortable="true" title="Program Type" />
        <display:column property="queueSize" sortable="true" title="Clients in Queue" />
    </display:table>

</html:form>

