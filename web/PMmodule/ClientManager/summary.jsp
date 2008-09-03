<%@ include file="/taglibs.jsp"%>

<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.ClientReferral"%>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>

<%@page import="oscar.OscarProperties"%>
<input type="hidden" name="clientId" value="" />
<input type="hidden" name="formId" value="" />
<input type="hidden" id="formInstanceId" value="0" />

<script>
var XMLHttpRequestObject = false;

if (window.XMLHttpRequest) {
	XMLHttpRequestObject = new XMLHttpRequest();
} else if (window.ActiveXObject) {
	XMLHttpRequestObject = new ActiveXObject("Microsoft.XMLHTTP");
}

window.onload = new Function("summary_load();");

function summary_load() {
	showEMPILinks();
}

function consent_mandatory() {
	<c:if test="${empty consent}">
		openConsent();
	</c:if>
}

function openRelations(){
        var url = '../demographic/AddAlternateContact.jsp';
		url += '?demo='+ '<c:out value="${client.demographicNo}"/>&pmmClient=yes';
	location.href = url;
}

function updateQuickIntake(clientId) {
	location.href = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=update&type=quick&clientId=" + clientId;
}

function printQuickIntake(clientId,intakeId) {
	url = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=print&type=quick&clientId=" + clientId+"&intakeId=" + intakeId;
	window.open(url, 'quickIntakePrint', 'width=1024,height=768,scrollbars=1');
}

function openConsent(){
	var url = '<html:rewrite action="/PMmodule/Consent.do"/>';
		url += '?method=form&id='+ '<c:out value="${client.demographicNo}"/>';
	window.open(url,'consent');
}	

function showConsent(){
	var url = '<html:rewrite action="/PMmodule/Consent.do?method=view" />';
		url += "&clientName="+'<c:out value="${client.formattedName}"/>';
		url += "&demographicNo="+'<c:out value="${client.demographicNo}"/>';
	window.open(url);	
}		

function openHealthSafety(){
	var url = '<html:rewrite action="/PMmodule/HealthSafety.do"/>';
		url += '?method=form&id='+ '<c:out value="${client.demographicNo}"/>';
	window.open(url,'consent');
}	

function showEMPILinks() {
	if (XMLHttpRequestObject) {
		var obj = document.getElementById('empi_links');
		if (obj == null) return;
		XMLHttpRequestObject.open("GET",'<html:rewrite action="/PMmodule/ClientManager"/>?method=getLinks&id=<c:out value="${client.demographicNo}"/>');
		
		XMLHttpRequestObject.onreadystatechange = function()
		{
			if (XMLHttpRequestObject.readyState == 4 && XMLHttpRequestObject.status == 200) {
				obj.innerHTML = XMLHttpRequestObject.responseText;
			}
		}
		
		XMLHttpRequestObject.send(null);
	}
}

function updateSharingOpting(state) {
	location.href = '<html:rewrite action="/PMmodule/ClientManager.do"/>' + "?method=update_sharing_opting&state="+state+"&id=<c:out value='${client.demographicNo}'/>";
}

function saveJointAdmission(clientId,headClientId,jType){
	location.href = '<html:rewrite action="/PMmodule/ClientManager.do"/>' + "?method=save_joint_admission&clientId=<c:out value='${client.demographicNo}'/>&headClientId="+headClientId+"&dependentClientId="+clientId+"&type="+jType;
}
function removeJointAdmission(clientId){
	location.href = '<html:rewrite action="/PMmodule/ClientManager.do"/>' + "?method=remove_joint_admission&clientId=<c:out value='${client.demographicNo}'/>&dependentClientId="+clientId;
}
function goToClient(clientId){
	location.href = '<html:rewrite action="/PMmodule/ClientManager.do"/>' + "?method=edit&clientId="+clientId;
}

function openSurvey() {	
	var selectBox = getElement('form.formId');		
	var formId = selectBox.options[selectBox.selectedIndex].value;	
	document.clientManagerForm.clientId.value='<c:out value="${client.demographicNo}"/>';
	document.clientManagerForm.formId.value=formId;	
	var id = document.getElementById('formInstanceId').value; 	
	location.href = '<html:rewrite action="/PMmodule/Forms/SurveyExecute.do"/>' + "?method=survey&formId=" + formId + "&formInstanceId=" + id + "&clientId=" + '<c:out value="${client.demographicNo}"/>';
}

</script>

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Personal Information</th>
		</tr>
	</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<tr>
		<th width="20%">Client No</th>
		<td><c:out value="${client.demographicNo}" /></td>
	</tr>
	<tr>
		<th width="20%">Name</th>
		<td><c:out value="${client.formattedName}" /></td>
	</tr>
	<tr>
		<th width="20%">Alias</th>
		<td><c:out value="${client.alias}" /></td>
	</tr>
	<tr>
		<th width="20%">Date of Birth</th>
		<td><c:out value="${client.yearOfBirth}" />/<c:out value="${client.monthOfBirth}" />/<c:out value="${client.dateOfBirth}" /></td>
	</tr>
	<tr>
		<th width="20%">Gender</th>
		<td><c:out value="${client.sexDesc}" /></td>
	</tr>
	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	<tr>
		<th width="20%">Health Card</th>
		<td><c:out value="${client.hin}" />&nbsp;<c:out value="${client.ver}" /></td>
	</tr>
	<tr>
		<th width="20%">Resources</th>
		<td>
		<%
		if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
			Integer demographicNo = ((Demographic) request.getAttribute("client")).getDemographicNo();
			pageContext.setAttribute("demographicNo", demographicNo);
		%>
		<% if (!OscarProperties.getInstance().isTorontoRFQ()) {%>
			<a href="javascript:void(0);" onclick="window.open('<c:out value="${ctx}"/>/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=<c:out value="${demographicNo}"/>','master_file');return false;">OSCAR Master File</a>
		<%}%>
		<%
 		}
		%>
		</td>
	</tr>
	<tr>
		<th width="20%">EMPI</th>
		<td><span id='empi_links'>Loading...</span></td>
	</tr>
	</caisi:isModuleLoad>
	<tr>
		<th width="20%">Active?</th>
		<td>
			<logic:equal value="0" property="activeCount" name="client">No</logic:equal>
			<logic:notEqual value="0" property="activeCount" name="client">Yes</logic:notEqual>
		</td>
	</tr>	

	<tr>
		<th width="20%">Health and Safety</th>
		<td>
        <table width="100%" class="simple" border="0" cellspacing="2" cellpadding="3">
			<c:choose>
				<c:when test="${empty healthsafety}">
					<tr><td><span style="color:red">None found</span></td>
					<td><input type="button" value="New Health and Safety" onclick="openHealthSafety()" /></td></tr>
				</c:when>
			    <c:when test="${empty healthsafety.message}">
					<tr><td><span style="color:red">None found</span></td>
					<td><input type="button" value="New Health and Safety" onclick="openHealthSafety()" /></td></tr>
				</c:when>
				<c:otherwise>
					<tr><td colspan="3"><c:out value="${healthsafety.message}" /></td></tr>
					<tr><td width="50%">User Name: <c:out value="${healthsafety.userName}" /></td>
					<td width="30%">Date: <fmt:formatDate value="${healthsafety.updateDate}" pattern="yyyy-MM-dd" /></td>
					<td width="20%">
					<input type="button" value="Edit" onclick="openHealthSafety()" />
					</td></tr>
				</c:otherwise>
			</c:choose>
         </table>
		</td>
	</tr>
	<tr>
		<th width="20%">Integrator Consent : </th>
		<td>
			<c:out value="${integratorConsent}" />
			&nbsp;&nbsp;&nbsp;
			<c:if test="${useQuickConsent}">
				<input type="button" value="Change Consent (Quick/Detailed)" onclick="document.location='ClientManager/integrator_consent.jsp?demographicId=<c:out value="${demographicId}" />'" />
			</c:if>
			<c:if test="${!useQuickConsent}">
				<input type="button" value="Change Consent (Complex)" onclick="window.open('ClientManager/complex_integrator_consent.jsp?demographicId=<c:out value="${demographicId}" />')" />
			</c:if>
		</td>
	</tr>	
</table>

<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>
                            Family
                            <c:if test="${groupHead != null}">
                                -- <c:out value="${client.formattedName}" /> ( HEAD )
                            </c:if>
                        </th>
		</tr>
	</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
    <c:choose>
        <c:when test="${relations == null}">
            <tr>
                <td>
                    <span style="color:red">No Family Members Registered</span>
                    <input type="button" value="Update" onclick="openRelations()" />
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <thead>
                <th>Name</th>
                <th>Relation</th>
                <th>Status</th>
                <th>Joint Admission</th>
                <th>Age</th>
            </thead>
            <c:forEach var="rHash" items="${relations}">
                <tr>
                        <td>
                            <a href="<html:rewrite action="/PMmodule/ClientManager.do"/>?method=edit&id=<c:out value="${rHash['demographicNo']}"/>">
                                <c:out value="${rHash['lastName']}"/>, <c:out value="${rHash['firstName']}"/>
                            </a><!-- <c:out value="${rHash}"/> -->
                        </td> 
                        <td><c:out value="${rHash['relation']}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${rHash['dependent'] == null}">
                                    <c:if test="${rHash['dependentable'] != null}">
                                        Add as 
                                        <input type="button" onclick="saveJointAdmission('<c:out value="${rHash['demographicNo']}"/>','<c:out value="${client.demographicNo}" />','2')" value="dependent"/>
                                        <input type="button" onclick="saveJointAdmission('<c:out value="${rHash['demographicNo']}"/>','<c:out value="${client.demographicNo}" />','1')" value="spouse"/>
                                    </c:if>
                                </c:when>
                                <c:when test="${rHash['dependent'] == 2}">
                                    Dependent <input type="button" onclick="removeJointAdmission('<c:out value="${rHash['demographicNo']}"/>')" value="remove"/>
                                </c:when>
                                <c:when test="${rHash['dependent'] == 1}">
                                    Spouse <input type="button" onclick="removeJointAdmission('<c:out value="${rHash['demographicNo']}"/>')" value="remove"/>
                                </c:when>
                                 <c:when test="${rHash['dependent'] == 0}">
                                    Head
                                </c:when>
                            </c:choose>    
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${rHash['jointAdmission'] == null}">
                                    No
                                </c:when>
                                <c:otherwise>
                                    Yes
                                </c:otherwise>
                            </c:choose>  
                        </td>
                        <td><c:out value="${rHash['age']}"/></td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4">
                    <c:choose>
                        <c:when test="${groupName == null}">
                            Joint admit total for <c:out value="${client.formattedName}" /> : <c:out value="${relationSize}"/>
                        </c:when>
                        <c:otherwise>
                             Joint admit total for <c:out value="${groupName}" /> : <c:out value="${relationSize}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><input type="button" value="Update" onclick="openRelations()" /></td>
            </tr>
        </c:otherwise>
    </c:choose>
    
</table>

<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Bed/Room Reservation</th>
		</tr>
	</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<c:choose>
		<c:when test="${bedDemographic == null}">
			<c:choose>
				<c:when test="${roomDemographic != null}">
					<tr>
						<th width="20%">Assigned Room:</th>
						<td><c:out value="${roomDemographic.room.name}" /></td>
					</tr>
					<tr>
						<th width="20%">Assigned Bed:</th>
						<td>N/A</td>
					</tr>
					<tr>
						<th width="20%">Until</th>
						<td><fmt:formatDate value="${roomDemographic.assignEnd}" pattern="yyyy-MM-dd" /></td>
					</tr>
					
				</c:when>	
				<c:otherwise>
					<tr>
						<td>
							<span style="color:red">No bed or room reserved</span>
						</td>
					</tr>
				</c:otherwise>	
			</c:choose>
		</c:when>
		<c:when test="${bedDemographic != null}">
				<tr>
					<th width="20%">Assigned Room:</th>
					<td><c:out value="${bedDemographic.roomName}" /> (<c:out value="${bedDemographic.programName}" />)</td>
				</tr>
				<tr>
					<th width="20%">Assigned Bed:</th>
					<td><c:out value="${bedDemographic.bedName}" /> (<c:out value="${bedDemographic.programName}" />)</td>
				</tr>
				<tr>
					<th width="20%">Status</th>
					<td><c:out value="${bedDemographic.statusName}" /></td>
				</tr>
				<tr>
					<th width="20%">Late Pass</th>
					<td><c:out value="${bedDemographic.latePass}" /></td>
				</tr>
				<tr>
					<th width="20%">Until</th>
					<td><fmt:formatDate value="${bedDemographic.reservationEnd}" pattern="yyyy-MM-dd" /></td>
				</tr>
		</c:when>
		<c:otherwise>		
		</c:otherwise>
	</c:choose>
</table>

<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Intake Form</th>
		</tr>
	</table>
</div>
<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Name</th>
			<th>Most Recent</th>
			<th>Staff</th>
			<th>Status</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tr>
		<td width="20%">Registration Intake</td>
		<c:if test="${mostRecentQuickIntake != null}">
			<td><c:out value="${mostRecentQuickIntake.createdOnStr}" /></td>
			<td><c:out value="${mostRecentQuickIntake.staffName}" /></td>
			<td><c:out value="${mostRecentQuickIntake.intakeStatus}"/></td>
			<td>
				<%
					if (!UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external))
					{
						%>
						<input type="button" value="Update" onclick="updateQuickIntake('<c:out value="${client.demographicNo}" />')" />&nbsp;
						<%
					}
				%>			
				<input type="button" value="Print Preview" onclick="printQuickIntake('<c:out value="${client.demographicNo}" />', '<c:out value="${mostRecentQuickIntake.id}"/>')" />
			</td>
		</c:if>
		<c:if test="${mostRecentQuickIntake == null}">
			<td><span style="color:red">None found</span></td>
			<td></td>
			<td><input type="button" value="Create" onclick="updateQuickIntake('<c:out value="${client.demographicNo}" />')" /></td>
		</c:if>
	</tr>
</table>
<br />

<%/*User Created Form*/ %>
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Assessments</th>
		</tr>
	</table>
</div>
<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Form Name</th>
			<th>Date</th>
			<th>Staff</th>
			<th>Actions</th>
		</tr>
	</thead>
	<c:forEach var="form" items="${surveys}">
	<tr>
		<td width="20%"><c:out value="${form.description}" /></td>
		<td><c:out value="${form.dateCreated}" /></td>
		<td><c:out value="${form.username}" /></td>
		<td><input type="button" value="update" onclick="document.clientManagerForm.elements['form.formId'].value='<c:out value="${form.formId}"/>';document.clientManagerForm.elements['formInstanceId'].value='<c:out value="${form.id}"/>';openSurvey();" /></td>
	</tr>
	</c:forEach>
</table>
<br />
<table cellspacing="0" cellpadding="0">
	<tr><td>New User Created Form:</td>
	<td>	
		<html:select property="form.formId" onchange="openSurvey()">
		<html:option value="0">&nbsp;</html:option>
		<html:options collection="survey_list" property="formId" labelProperty="description" />
		</html:select>
	</td>
	</tr>
</table>
<br />
</caisi:isModuleLoad>

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Current Programs</th>
		</tr>
	</table>
</div>

<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissions" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs." />
	
	<display:column property="programName" sortable="true" title="Program Name" />
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="admissionDate" format="{0, date, yyyy-MM-dd kk:mm}" sortable="true" title="Admission Date" />
	<display:column sortable="true" title="Days in Program">
		<%
			Admission tempAdmission = (Admission) pageContext.getAttribute("admission");
			Date admissionDate = tempAdmission.getAdmissionDate();
			Date dischargeDate = tempAdmission.getDischargeDate() != null ? tempAdmission.getDischargeDate() : new Date();
			
			long diff = dischargeDate.getTime() - admissionDate.getTime();
			diff = diff / 1000; // seconds
			diff = diff / 60; // minutes
			diff = diff / 60; // hours
			diff = diff / 24; // days

			String numDays = String.valueOf(diff);
		%>
		<%=numDays%>
	</display:column>
	<caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
	<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	</caisi:isModuleLoad>
	<display:column property="admissionNotes" sortable="true" title="Admission Notes" />
</display:table>

<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Referrals</th>
		</tr>
	</table>
</div>

<display:table class="simple" cellspacing="2" cellpadding="3" id="referral" name="referrals" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	
	<display:column property="programName" sortable="true" title="Program Name" />
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="referralDate" format="{0, date, yyyy-MM-dd kk:mm}" sortable="true" title="Referral Date" />
	<display:column property="providerFormattedName" sortable="true" title="Referring Provider" />
	<display:column sortable="true" title="Days in Queue">
		<%
			ClientReferral tempReferral = (ClientReferral) pageContext.getAttribute("referral");
			Date referralDate = tempReferral.getReferralDate();
			Date currentDate = new Date();
			
			long referralDiff = currentDate.getTime() - referralDate.getTime();
			referralDiff = referralDiff / 1000; // seconds
			referralDiff = referralDiff / 60; // minutes
			referralDiff = referralDiff / 60; // hours
			referralDiff = referralDiff / 24; // days

			String referralNumDays = String.valueOf(referralDiff);
		%>
		<%=referralNumDays%>
	</display:column>
</display:table>
