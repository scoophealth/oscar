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

	<br>
	<div class="tabs" id="tabs">
        <table cellpadding="3" cellspacing="0" border="0">
            <tr>
                <th title="Facility Messages">Messages</th>
            </tr>
        </table>
    </div>
    <br>This table displays client automatic discharges from this facility from the past seven days. An
automatic discharge occurs when the client is admitted to another facility
while still admitted in this facility.

	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr>
			<th>Name</th>
			<th>Client DOB</th>
			<th>Bed Program</th>
			<th>Discharge Date/Time</th>
		</tr>
		<c:forEach var="client" items="${associatedClients}">
		
			<%String styleColor=""; %>
			<c:if test="${client.inOneDay}">
				<%styleColor="style=\"color:red;\"";%>
			</c:if>
			<tr class="b" <%=styleColor%> >
				<td><c:out value="${client.name}"/></td>
				<td><c:out value="${client.dob}"/></td>
				<td><c:out value="${client.programName}"/></td>
				<td><c:out value="${client.dischargeDate}"/></td>
			</tr>
			
		</c:forEach>
	</table>

    
    <br>
    Automatic discharges in the past 24 hours appear <color:red >red </color:red>
</html:form>

