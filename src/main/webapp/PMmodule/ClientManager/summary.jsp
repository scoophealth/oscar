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
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.List"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.ClientReferral"%>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@page import="java.util.Date"%>
<%@page	import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.casemgmt.dao.ClientImageDAO"%>
<%@page import="org.oscarehr.casemgmt.model.ClientImage"%>
<%@page import="org.oscarehr.common.dao.IntegratorConsentDao"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent"%>
<%@page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.caisi_integrator.ws.GetConsentTransfer"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramProviderDAO"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%
	ProgramProviderDAO ppd =(ProgramProviderDAO)SpringUtils.getBean("programProviderDAO");
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	Demographic currentDemographic=(Demographic)request.getAttribute("client");
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	boolean caisiSearchWorkflow = Boolean.valueOf(OscarProperties.getInstance().getProperty("caisi.search.workflow","true"));
%>



<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.caisi_integrator.ws.ConsentState"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.common.model.CdsClientForm"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.web.ClientManagerAction"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.DemographicCust"%>
<%@page import="org.oscarehr.common.dao.DemographicCustDao"%>
<%@page import="org.oscarehr.PMmodule.web.AdmissionForDisplay"%>
<%@ page import="org.oscarehr.PMmodule.service.ProgramManager"%>

<input type="hidden" name="clientId" value="" />
<input type="hidden" name="formId" value="" />
<input type="hidden" id="formInstanceId" value="0" />

<%
//get current program, to check for OCAN
boolean programEnableOcan=false;
String currentProgram = (String)session.getAttribute(org.oscarehr.util.SessionConstants.CURRENT_PROGRAM_ID);
if(currentProgram != null) {
	ProgramManager pm = SpringUtils.getBean(ProgramManager.class);
	Program program = pm.getProgram(currentProgram);
	programEnableOcan =program.isEnableOCAN();
}
%>
<script>
var XMLHttpRequestObject = false;

if (window.XMLHttpRequest) {
	XMLHttpRequestObject = new XMLHttpRequest();
} else if (window.ActiveXObject) {
	XMLHttpRequestObject = new ActiveXObject("Microsoft.XMLHTTP");
}

window.onload = new Function("summary_load();");

function summary_load() {
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

function openHealthSafety(){
	var url = '<html:rewrite action="/PMmodule/HealthSafety.do"/>';
		url += '?method=form&id='+ '<c:out value="${client.demographicNo}"/>';
	window.open(url,'consent');
}


function saveJointAdmission(clientId,headClientId,jType){
	location.href = '<html:rewrite action="/PMmodule/ClientManager.do"/>' + "?method=save_joint_admission&clientId=<c:out value='${client.demographicNo}'/>&headClientId="+headClientId+"&dependentClientId="+clientId+"&type="+jType;
}
function removeJointAdmission(clientId){
	location.href = '<html:rewrite action="/PMmodule/ClientManager.do"/>' + "?method=remove_joint_admission&clientId=<c:out value='${client.demographicNo}'/>&dependentClientId="+clientId;
}

function openSurvey() {
	var selectBox = getElement('form.formId');
	var formId = selectBox.options[selectBox.selectedIndex].value;
	document.clientManagerForm.clientId.value='<c:out value="${client.demographicNo}"/>';
	document.clientManagerForm.formId.value=formId;
	var id = document.getElementById('formInstanceId').value;
	location.href = '<html:rewrite action="/PMmodule/Forms/SurveyExecute.do"/>' + "?method=survey&formId=" + formId + "&formInstanceId=" + id + "&clientId=" + '<c:out value="${client.demographicNo}"/>';
}

function updateCdsForm(message, cdsFormId) {
	if(confirm("You are about to change a CDS form for "+message)) {
		document.location="ClientManager/cds_form_4.jsp?cdsFormId="+cdsFormId ;
	}
	return false;
}
</script>

<div style="text-align:left;color:red;">

</div>


<table style="width:100%;">
	<tr>
	<td style="text-align:left;color:red;">
	<%
		DemographicCustDao demographicCustDao = SpringUtils.getBean(DemographicCustDao.class);
		DemographicCust demographicCust = demographicCustDao.find(currentDemographic.getDemographicNo());
		if(demographicCust != null) {
			String alert = demographicCust.getAlert();
			if(alert != null && alert.length()>0) {
				alert = alert.replaceAll("\n", "<br/>");
    %>
    			<%=alert %>
    <%
		}}
	%>
	</td>
	</tr>
</table>



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
		<td width="80%"><c:out value="${client.demographicNo}" /></td>
		<td rowspan="4" width="98px">
		<div style="text-align:right">
			<%
				ClientImageDAO clientImageDAO=(ClientImageDAO)SpringUtils.getBean("clientImageDAO");
				ClientImage clientImage=clientImageDAO.getClientImage(currentDemographic.getDemographicNo());

				String imagePlaceholder=ClientImage.imageMissingPlaceholderUrl;
				String imageUrl=imagePlaceholder;

				if (clientImage!=null)
				{
					imagePlaceholder=ClientImage.imagePresentPlaceholderUrl;
					imageUrl="/imageRenderingServlet?source="+ImageRenderingServlet.Source.local_client.name()+"&clientId="+currentDemographic.getDemographicNo();
				}
			%>
			<img style="height:96px; width:96px" src="<%=request.getContextPath()+imagePlaceholder%>" alt="client_image_<%=currentDemographic.getDemographicNo()%>" onmouseover="src='<%=request.getContextPath()+imageUrl%>'" onmouseout="src='<%=request.getContextPath()+imagePlaceholder%>'" onClick="window.open('<%=request.getContextPath()%>/casemgmt/uploadimage.jsp?demographicNo=<%=currentDemographic.getDemographicNo()%>', '', 'height=500,width=500,location=no,scrollbars=no,menubars=no,toolbars=no,resizable=yes,top=50,left=50')" />
		</div>
		</td>
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
		<td><c:out value="${client.yearOfBirth}" />/<c:out
			value="${client.monthOfBirth}" />/<c:out
			value="${client.dateOfBirth}" /></td>
	</tr>
	<tr>
		<th width="20%">Gender</th>
		<td colspan="2"><c:out value="${client.sexDesc}" /></td>
	</tr>
	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
		<tr>
			<th width="20%">Health Card</th>
			<td colspan="2">
				<c:out value="${client.hin}" />&nbsp;<c:out value="${client.ver}" />&nbsp;(<c:out value="${client.hcType}" />)
				<%
					// show the button even if integrator is disabled, this is to allow people to validate local data with integrator disabled.
					if (loggedInInfo.getCurrentFacility().isEnableHealthNumberRegistry())
					{
						%>
							<input type="button" value="Manage Health Number Registry" onclick="document.location='ClientManager/manage_hnr_client.jsp?demographicId=<%=currentDemographic.getDemographicNo()%>'" />
						<%
					}
				%>
			</td>
		</tr>
		<tr>
			<th width="20%">Resources</th>
			<td colspan="2">
				<%
						Integer demographicNo=currentDemographic.getDemographicNo();
						pageContext.setAttribute("demographicNo", demographicNo);

						if (!OscarProperties.getInstance().isTorontoRFQ())
						{
							%>
								<a href="javascript:void(0);" onclick="window.open('<c:out value="${ctx}"/>/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=<c:out value="${demographicNo}"/>','master_file');return false;">OSCAR Master File</a>
							<%
	 					}
 				%>
			</td>
		</tr>
<!--
		<tr>
			<th width="20%">EMPI</th>
			<td colspan="2"><span id='empi_links'>Loading...</span></td>
		</tr>
-->
	</caisi:isModuleLoad>
	<tr>
		<th width="20%">Active?</th>
		<td colspan="2"><logic:equal value="0" property="activeCount" name="client">No</logic:equal>
		<logic:notEqual value="0" property="activeCount" name="client">Yes</logic:notEqual>
		</td>
	</tr>

	<tr>
		<th width="20%">Health and Safety</th>
		<td colspan="2">
		<table width="100%" class="simple" border="0" cellspacing="2"
			cellpadding="3">
			<c:choose>
				<c:when test="${empty healthsafety}">
					<tr>
						<td><span style="color: red">None found</span></td>
						<td><input type="button" value="New Health and Safety" onclick="openHealthSafety()" /></td>
					</tr>
				</c:when>
				<c:when test="${empty healthsafety.message}">
					<tr>
						<td><span style="color: red">None found</span></td>
						<td><input type="button" value="New Health and Safety" onclick="openHealthSafety()" /></td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="3"><c:out value="${healthsafety.message}" /></td>
					</tr>
					<tr>
						<td width="50%">User Name: <c:out value="${healthsafety.userName}" /></td>
						<td width="30%">Date: <fmt:formatDate value="${healthsafety.updateDate}" pattern="yyyy-MM-dd" /></td>
						<td width="20%"><input type="button" value="Edit" onclick="openHealthSafety()" /></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</table>
		</td>
	</tr>
	<%
		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled())
		{
			%>
				<tr>
					<th width="20%">Integrator Consent</th>
					<td colspan="2">
						<%
							String consentString="System is unavailable";
							boolean isIntegratorContactable=false;

							try
							{
								GetConsentTransfer remoteConsent=CaisiIntegratorManager.getConsentState(loggedInInfo, loggedInInfo.getCurrentFacility(), currentDemographic.getDemographicNo());

								if (remoteConsent!=null)
								{
									StringBuilder sb=new StringBuilder();

									if (remoteConsent.getConsentState()==ConsentState.ALL) sb.append("Consented to all, ");
									if (remoteConsent.getConsentState()==ConsentState.SOME) sb.append("Limited consent, ");
									if (remoteConsent.getConsentState()==ConsentState.NONE) sb.append("No consent, ");

									CachedFacility myFacility=CaisiIntegratorManager.getCurrentRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility());
									if (myFacility.getIntegratorFacilityId().equals(remoteConsent.getIntegratorFacilityId()))
									{
										sb.append("set locally on ");
									}
									else
									{
										sb.append("set by another facility on ");
									}

									sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(remoteConsent.getConsentDate()));
									consentString=sb.toString();
								}
								else
								{
									consentString="Not yet obtained";
								}

								isIntegratorContactable=true;
							}
							catch (Exception e)
							{
								MiscUtils.getLogger().error("Unexpected error on summary page.", e);
							}
						%>
						<input type="button" <%=isIntegratorContactable?"":"disabled=\"disabled\""%> value="Change Consent" onclick="document.location='ClientManager/manage_consent.jsp?demographicId=<%=currentDemographic.getDemographicNo()%>'" /> <%=consentString%>
					</td>
				</tr>
				<tr>
					<th width="20%">Linked clients</th>
				 	<td colspan="2"><input type="button" <%=isIntegratorContactable?"":"disabled=\"disabled\""%> value="Manage Linked Clients" onclick="document.location='ClientManager/manage_linked_clients.jsp?demographicId=<%=currentDemographic.getDemographicNo()%>'" /></td>
				</tr>
			<%
		}
			%>
</table>

<br />

<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th>Family <c:if test="${groupHead != null}">
                                -- <c:out value="${client.formattedName}" /> ( HEAD )
                            </c:if></th>
	</tr>
</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<c:choose>
		<c:when test="${relations == null}">
			<tr>
				<td><span style="color: red">No Family Members Registered</span> <input type="button" value="Update" onclick="openRelations()" /></td>
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
					<td><a
						href="<html:rewrite action="/PMmodule/ClientManager.do"/>?method=edit&id=<c:out value="${rHash['demographicNo']}"/>">
					<c:out value="${rHash['lastName']}" />, <c:out
						value="${rHash['firstName']}" /> </a><!-- <c:out value="${rHash}"/> -->
					</td>
					<td><c:out value="${rHash['relation']}" /></td>
					<td><c:choose>
						<c:when test="${rHash['dependent'] == null}">
							<c:if test="${rHash['dependentable'] != null}">
                                        Add as
                                        <input type="button" onclick="saveJointAdmission('<c:out value="${rHash['demographicNo']}"/>','<c:out value="${client.demographicNo}" />','2')" value="dependent" />
								<input type="button" onclick="saveJointAdmission('<c:out value="${rHash['demographicNo']}"/>','<c:out value="${client.demographicNo}" />','1')" value="spouse" />
							</c:if>
						</c:when>
						<c:when test="${rHash['dependent'] == 2}">
                                    Dependent <input type="button"
								onclick="removeJointAdmission('<c:out value="${rHash['demographicNo']}"/>')"
								value="remove" />
						</c:when>
						<c:when test="${rHash['dependent'] == 1}">
                                    Spouse <input type="button"
								onclick="removeJointAdmission('<c:out value="${rHash['demographicNo']}"/>')"
								value="remove" />
						</c:when>
						<c:when test="${rHash['dependent'] == 0}">
                                    Head
                                </c:when>
					</c:choose></td>
					<td><c:choose>
						<c:when test="${rHash['jointAdmission'] == null}">
                                    No
                                </c:when>
						<c:otherwise>
                                    Yes
                                </c:otherwise>
					</c:choose></td>
					<td><c:out value="${rHash['age']}" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="4"><c:choose>
					<c:when test="${groupName == null}">
                            Joint admit total for <c:out
							value="${client.formattedName}" /> : <c:out
							value="${relationSize}" />
					</c:when>
					<c:otherwise>
                             Joint admit total for <c:out
							value="${groupName}" /> : <c:out value="${relationSize}" />
					</c:otherwise>
				</c:choose></td>
				<td><input type="button" value="Update"
					onclick="openRelations()" /></td>
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
						<td><fmt:formatDate value="${roomDemographic.assignEnd}"
							pattern="yyyy-MM-dd" /></td>
					</tr>

				</c:when>
				<c:otherwise>
					<tr>
						<td><span style="color: red">No bed or room reserved</span></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${bedDemographic != null}">
			<tr>
				<th width="20%">Assigned Room:</th>
				<td><c:out value="${bedDemographic.roomName}" /> (<c:out
					value="${bedDemographic.programName}" />)</td>
			</tr>
			<tr>
				<th width="20%">Assigned Bed:</th>
				<td><c:out value="${bedDemographic.bedName}" /> (<c:out
					value="${bedDemographic.programName}" />)</td>
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
				<td><fmt:formatDate value="${bedDemographic.reservationEnd}"
					pattern="yyyy-MM-dd" /></td>
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
		<th>Intake Form/Follow up Intake forms</th>
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
			<td><c:out value="${mostRecentQuickIntake.intakeStatus}" /></td>
			<td>
			<%
				if (!UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external))
					{
					if(caisiSearchWorkflow) {
			%> <input type="button" value="Update"
				onclick="updateQuickIntake('<c:out value="${client.demographicNo}" />')" />&nbsp;
			<%
				} }
			%> <input type="button" value="Print Preview"
				onclick="printQuickIntake('<c:out value="${client.demographicNo}" />', '<c:out value="${mostRecentQuickIntake.id}"/>')" />
			</td>
		</c:if>
		<c:if test="${mostRecentQuickIntake == null}">
			<td><span style="color: red">None found</span></td>
			<td></td>
			<td>
				<%
					if(caisiSearchWorkflow) {
				%>
			<input type="button" value="Create"
				onclick="updateQuickIntake('<c:out value="${client.demographicNo}" />')" />
				<% } %>
				</td>
		</c:if>
	</tr>
	<%
		List<CdsClientForm> allLatestCdsForms=(List<CdsClientForm>)request.getAttribute("allLatestCdsForms");
		if (allLatestCdsForms!=null && allLatestCdsForms.size()>0)
		{
			for (CdsClientForm cdsClientForm : allLatestCdsForms)
			{
				%>
					<tr>
						<td width="20%">CDS : <%=ClientManagerAction.getCdsProgramDisplayString(cdsClientForm)%></td>
						<td><%=StringEscapeUtils.escapeHtml(DateUtils.formatDateTime(cdsClientForm.getCreated(), request.getLocale()))%></td>
						<td><%=ClientManagerAction.getEscapedProviderDisplay(cdsClientForm.getProviderNo())%></td>
						<td><%=cdsClientForm.isSigned()?"signed":"unsigned"%></td>
						<td>
							
							<input type="button" value="Update" onclick="updateCdsForm('<%=ClientManagerAction.getCdsProgramDisplayString(cdsClientForm) %>','<%=cdsClientForm.getId()%>')" />
														
							<input type="button" value="Print Preview" onclick="document.location='ClientManager/cds_form_4.jsp?cdsFormId=<%=cdsClientForm.getId()%>&print=true'" />
						</td>
					</tr>
				<%				
			}
		}
		%>
			<tr>
				<td colspan="5">
					<input type="button" value="New CDS Form" onclick="document.location='ClientManager/cds_form_4.jsp?demographicId=<%=currentDemographic.getDemographicNo()%>'" />						
				</td>
			</tr>
		<%

		if (loggedInInfo.getCurrentFacility().isEnableOcanForms() && programEnableOcan )
		{
	%>
	<tr>
		<td width="20%">FULL OCAN 2.0 Staff Assessment</td>
		<c:if test="${ocanStaffForm != null}">
			<td><c:out value="${ocanStaffForm.created}" /></td>
			<td><c:out value="${ocanStaffForm.providerName}" /></td>
			<td><c:out value="${ocanStaffForm.assessmentStatus}" /></td>
			<td>
				<input type="button" value="Update" onclick="document.location='ClientManager/ocan_form.jsp?ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="Print Preview" onclick="document.location='ClientManager/ocan_form.jsp?ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>&print=true'" />
				<input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_FULL_v2.0.5.pdf"/>')"/>
			</td>
		</c:if>
		<c:if test="${ocanStaffForm == null}">
			<td><span style="color: red">None found</span></td>
			<td></td>
			<td>
				<input type="button" value="New FULL OCAN Form" onclick="document.location='ClientManager/ocan_form.jsp?prepopulate=0&ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="New FULL OCAN Form - Prepopulated" onclick="document.location='ClientManager/ocan_form.jsp?prepopulate=1&ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
			</td>
			<td><input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_FULL_v2.0.5.pdf"/>')"/>
			</td>
		</c:if>
	</tr>

	<tr>
		<td width="20%">FULL OCAN 2.0 Consumer Self-Assessment</td>
		<c:if test="${ocanStaffForm != null}">
			<td><c:out value="${ocanStaffForm.clientFormCreated}" /></td>
			<td><c:out value="${ocanStaffForm.clientFormProviderName}" /></td>
			<td>N/A</td>
			<td>
				<input type="button" value="Update" onclick="document.location='ClientManager/ocan_client_form.jsp?ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="Print Preview" onclick="document.location='ClientManager/ocan_client_form.jsp?ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>&print=true'" />
			</td>
		</c:if>
		<c:if test="${ocanStaffForm == null}">
			<td><span style="color: red">None found</span></td>
			<td></td>
			<td>
				<input type="button" value="New FULL OCAN Form" onclick="document.location='ClientManager/ocan_client_form.jsp?prepopulate=0&ocanType=FULL&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
			</td>
			<td>
			</td>
		</c:if>
	</tr>


	<tr>
		<td width="20%">SELF+CORE OCAN 2.0 Staff Assessment</td>
		<c:if test="${selfOcanStaffForm != null}">
			<td><c:out value="${selfOcanStaffForm.created}" /></td>
			<td><c:out value="${selfOcanStaffForm.providerName}" /></td>
			<td><c:out value="${selfOcanStaffForm.assessmentStatus}" /></td>
			<td>
				<input type="button" value="Update" onclick="document.location='ClientManager/ocan_form.jsp?ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="Print Preview" onclick="document.location='ClientManager/ocan_form.jsp?ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>&print=true'" />
				<input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_CORE_SELF_v2.0.5.pdf"/>')"/>

			</td>
		</c:if>
		<c:if test="${selfOcanStaffForm == null}">
			<td><span style="color: red">None found</span></td>
			<td></td>
			<td>
				<input type="button" value="New SELF+CORE OCAN Form" onclick="document.location='ClientManager/ocan_form.jsp?prepopulate=0&ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="New SELF+CORE OCAN Form - Prepopulated" onclick="document.location='ClientManager/ocan_form.jsp?prepopulate=1&ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
			</td>
			<td><input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_CORE_SELF_v2.0.5.pdf"/>')"/>
				</td>
		</c:if>
	</tr>

	<tr>
		<td width="20%">SELF+CORE OCAN 2.0 Consumer Self-Assessment</td>
		<c:if test="${selfOcanStaffForm != null}">
			<td><c:out value="${selfOcanStaffForm.clientFormCreated}" /></td>
			<td><c:out value="${selfOcanStaffForm.clientFormProviderName}" /></td>
			<td>N/A</td>
			<td>
				<input type="button" value="Update" onclick="document.location='ClientManager/ocan_client_form.jsp?ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="Print Preview" onclick="document.location='ClientManager/ocan_client_form.jsp?ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>&print=true'" />
				<input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_CORE_SELF_v2.0.5.pdf"/>')"/>

			</td>
		</c:if>
		<c:if test="${selfOcanStaffForm == null}">
			<td><span style="color: red">None found</span></td>
			<td></td>
			<td>
				<input type="button" value="New SELF+CORE OCAN Form" onclick="document.location='ClientManager/ocan_client_form.jsp?prepopulate=0&ocanType=SELF&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
			</td>
			<td></td>
		</c:if>
	</tr>

	<tr>
		<td width="20%">CORE OCAN 2.0 Assessment</td>
		<c:if test="${coreOcanStaffForm != null}">
			<td><c:out value="${coreOcanStaffForm.created}" /></td>
			<td><c:out value="${coreOcanStaffForm.providerName}" /></td>
			<td><c:out value="${coreOcanStaffForm.assessmentStatus}" /></td>
			<td>
				<input type="button" value="Update" onclick="document.location='ClientManager/ocan_form.jsp?ocanType=CORE&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				<input type="button" value="Print Preview" onclick="document.location='ClientManager/ocan_form.jsp?ocanType=CORE&demographicId=<%=currentDemographic.getDemographicNo()%>&print=true'" />
				<input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_CORE_v2.0.5.pdf"/>')"/>

			</td>
		</c:if>
		<c:if test="${coreOcanStaffForm == null}">
			<td><span style="color: red">None found</span></td>
			<td></td>
			<td>
				<input type="button" value="New CORE OCAN Form" onclick="document.location='ClientManager/ocan_form.jsp?prepopulate=0&ocanType=CORE&demographicId=<%=currentDemographic.getDemographicNo()%>'" />

				<input type="button" value="New CORE OCAN Form - Prepopulated" onclick="document.location='ClientManager/ocan_form.jsp?prepopulate=1&ocanType=CORE&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
			</td>
			<td><input type="button" value="Blank Form" onclick="window.open('<html:rewrite page="/ocan/OCAN_2.0_CORE_v2.0.5.pdf"/>')"/>
				</td>
		</c:if>
	</tr>

	<% } %>
	
	
	<%
	if (loggedInInfo.getCurrentFacility().isEnableCbiForm())
	{
		List<OcanStaffForm> allLatestCbiForms=(List<OcanStaffForm>)request.getAttribute("allLatestCbiForms");
		if (allLatestCbiForms!=null && allLatestCbiForms.size()>0)
		{
			for (OcanStaffForm cbiForm : allLatestCbiForms)
			{
				%>
					<tr>
						<td width="20%"><b>CBI :</b> <%=ClientManagerAction.getCbiProgramDisplayString(cbiForm)%></td>
						<td><%=StringEscapeUtils.escapeHtml(DateUtils.formatDateTime(cbiForm.getCreated(), request.getLocale()))%></td>
						<td><%=ClientManagerAction.getEscapedProviderDisplay(cbiForm.getProviderNo())%></td>
						<td><%=cbiForm.isSigned()?"signed":"unsigned"%></td>
						<td>
							<input type="button" value="Update" onclick="document.location='ClientManager/cbi_form.jsp?ocanStaffFormId=<%=cbiForm.getId()%>&ocanType=CBI&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
							
						</td>
					</tr>
				<%				
			}
		}
		%>
			<tr>
				<td colspan="5">
					<input type="button" value="New CBI Form" onclick="document.location='ClientManager/cbi_form.jsp?ocanStaffFormId=0&prepopulate=0&ocanType=CBI&demographicId=<%=currentDemographic.getDemographicNo()%>'" />
				</td>
	</tr>
	<%} %>
</table>
<br />

<%
	/*User Created Form*/
%>
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
				<td><input type="button" value="update"
					onclick="document.clientManagerForm.elements['form.formId'].value='<c:out value="${form.formId}"/>';document.clientManagerForm.elements['formInstanceId'].value='<c:out value="${form.id}"/>';openSurvey();" /></td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<table cellspacing="0" cellpadding="0">
		<tr>
			<td>New User Created Form:</td>
			<td><html:select property="form.formId" onchange="openSurvey()">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="survey_list" property="formId"
					labelProperty="description" />
			</html:select></td>
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

<% boolean bShowEncounterLink = false;%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
<% bShowEncounterLink = true; %>
</security:oscarSec>
<%
String curUser_no = (String) session.getAttribute("user");
String rsAppointNO="0";
int demographic_no = currentDemographic.getDemographicNo();
String status = "T";
String userfirstname = (String) session.getAttribute("userfirstname");;
String userlastname = (String) session.getAttribute("userlastname");
String reason ="";
%>
<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissions" export="false" pagesize="10"	requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs." />

	<display:column property="facilityName" sortable="true" title="Facility" />
	<display:column property="programName" sortable="true" title="Program" />
	<display:column sortable="true" title="">
		<% if(bShowEncounterLink) {
			HttpSession se = request.getSession();
			AdmissionForDisplay tempAdmission=(AdmissionForDisplay)pageContext.getAttribute("admission");

			if (!tempAdmission.isFromIntegrator())
			{
				//Check program is in provider's program domain:
				if(ppd.isThisProgramInProgramDomain(curUser_no,tempAdmission.getProgramId()))
				{
					String eURL = "../oscarEncounter/IncomingEncounter.do?programId="+tempAdmission.getProgramId()+"&providerNo="+curUser_no+"&appointmentNo="+rsAppointNO+"&demographicNo="+demographic_no+"&curProviderNo="+curUser_no+"&reason="+java.net.URLEncoder.encode(reason)+"&encType="+java.net.URLEncoder.encode("face to face encounter with client","UTF-8")+"&userName="+java.net.URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate=null&appointmentDate=null&startTime=0:0"+"&status="+status+"&source=cm";
					%>
					<logic:notEqual value="community" property="programType" name="admission">
						<a href=# onClick="popupPage(710, 1024,'../oscarSurveillance/CheckSurveillance.do?programId=<%=tempAdmission.getProgramId()%>&demographicNo=<%=demographic_no%>&proceed=<%=java.net.URLEncoder.encode(eURL)%>');return false;" title="<bean:message key="global.encounter"/>">
						   <bean:message key="provider.appointmentProviderAdminDay.btnEncounter" />
						</a>
					</logic:notEqual>
					<%
				}
			}
		}
	%>
	</display:column>

	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<display:column property="daysInProgram" sortable="true" title="Days in Program" />
	<caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
		<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	</caisi:isModuleLoad>
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
	<display:column property="selectVacancy" sortable="true" title="Vacancy Name" />
	<display:column property="vacancyTemplateName" sortable="true" title="Vacancy Template Name" />
	<display:column property="programName" sortable="true" title="Facility / Program Name" />
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="referralDate" sortable="true" title="Referral Date" />
	<display:column property="referringProvider" sortable="true" title="Referring Provider/Facility" />
	<display:column property="daysInQueue" sortable="true" title="Days in Queue" />
	
</display:table>
