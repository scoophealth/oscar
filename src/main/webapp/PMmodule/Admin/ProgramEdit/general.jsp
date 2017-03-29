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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="org.oscarehr.PMmodule.model.ProgramSignature"%>
<%@ page import="org.oscarehr.PMmodule.model.Program"%>
<script>
	function save() {
		var maxAllowed = document.programManagerForm.elements['program.maxAllowed'].value;		
		if(isNaN(maxAllowed)) {
			alert("Maximum participants '" + maxAllowed + "' is not a number");
			return false;
		}
		if(document.programManagerForm.elements['program.maxAllowed'].value <= 0) {
			alert('Maximum participants must be a positive integer');
			return false;
		}
		
		if(document.programManagerForm.elements['program.name'].value==null || document.programManagerForm.elements['program.name'].value.length <= 0) {
			alert('The program name can not be blank.');
			return false;
		}
		
		
		document.programManagerForm.method.value='save';
		document.programManagerForm.submit()
	}
	
	function getProgramSignatures(id) {
	    if (id==null || id == "") return;
		var url = '<html:rewrite action="/PMmodule/ProgramManager.do"/>?method=programSignatures&programId=';
		window.open(url + id, 'signature', 'width=600,height=600,scrollbars=1');
	}
</script>
<html:hidden property="program.numOfMembers" />
<html:hidden property="program.id" />
<%
Program p = (Program)request.getAttribute("oldProgram");

%>
<input type="hidden" name="old_maxAllowed" value=<%if(p!=null) { %> "<%=p.getMaxAllowed() %>" <%}else{ %> "0" <%} %> />
<input type="hidden" name="old_name" value=<%if(p!=null) { %> "<%=p.getName()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_descr" value=<%if(p!=null) { %> "<%=p.getDescription()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_type" value=<%if(p!=null) { %> "<%=p.getType()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_address" value=<%if(p!=null) { %> "<%=p.getAddress()%>"<%}else{ %> "" <%} %> />
<input type="hidden" name="old_phone" value=<%if(p!=null) { %> "<%=p.getPhone()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_fax" value=<%if(p!=null) { %> "<%=p.getFax() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_url" value=<%if(p!=null) { %> "<%=p.getUrl()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_email" value=<%if(p!=null) { %> "<%=p.getEmail()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_emergencyNumber" value=<%if(p!=null) { %> "<%=p.getEmergencyNumber()%>"<%}else{ %> "" <%} %> />
<input type="hidden" name="old_location" value=<%if(p!=null) { %> "<%=p.getLocation()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_programStatus" value=<%if(p!=null) { %> "<%=p.getProgramStatus()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_bedProgramLinkId" value=<%if(p!=null) { %> "<%=p.getBedProgramLinkId()%>" <%}else{ %> "0" <%} %> />
<input type="hidden" name="old_manOrWoman" value=<%if(p!=null) { %> "<%=p.getManOrWoman() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_abstinenceSupport" value=<%if(p!=null) { %> "<%=p.getAbstinenceSupport() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_exclusiveView" value=<%if(p!=null) { %> "<%=p.getExclusiveView() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_holdingTank" value=<%if(p!=null) { %> "<%=p.isHoldingTank() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_allowBatchAdmission" value=<%if(p!=null) { %> "<%=p.isAllowBatchAdmission() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_allowBatchDischarge" value=<%if(p!=null) { %> "<%=p.isAllowBatchDischarge() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_hic" value=<%if(p!=null) { %> "<%=p.isHic() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_transgender" value=<%if(p!=null) { %> "<%=p.isTransgender() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_firstNation" value=<%if(p!=null) { %> "<%=p.isFirstNation() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_bedProgramAffiliated" value=<%if(p!=null) { %> "<%=p.isBedProgramAffiliated() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_alcohol" value=<%if(p!=null) { %> "<%=p.isAlcohol()%>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_physicalHealth" value=<%if(p!=null) { %> "<%=p.isPhysicalHealth() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_mentalHealth" value=<%if(p!=null) { %> "<%=p.isMentalHealth() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_housing" value=<%if(p!=null) { %> "<%=p.isHousing() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_facility_id" value=<%if(p!=null) { %> "<%=p.getFacilityId() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_enableEncounterTime" value=<%if(p!=null) { %> "<%=p.getEnableEncounterTime() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_enableEncounterTransportationTime" value=<%if(p!=null) { %> "<%=p.isEnableEncounterTransportationTime() %>" <%}else{ %> "" <%} %> />
<input type="hidden" name="old_enableOCAN" value=<%if(p!=null) { %> "<%=p.isEnableOCAN() %>" <%}else{ %> "" <%} %> />

<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">General Information</th>
		<th title="Templates" class="nofocus">
			<a onclick="javascript:clickTab2('General','Vacancy Templates');return false;" href="javascript:void(0)">Vacancy Templates</a>
		</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Name:</td>
		<td><html:text property="program.name" size="30" maxlength="70" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Facility</td>
		<td><html-el:select property="program.facilityId">
			<c:forEach var="facility" items="${facilities}">
				<html-el:option value="${facility.id}">
					<c:out value="${facility.name}" />
				</html-el:option>
			</c:forEach>
		</html-el:select></td>
	</tr>
	<tr class="b">
		<td width="20%">Description:</td>
		<td><html:text property="program.description" size="30"
			maxlength="255" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Functional Centre:</td>
		<td>
			<html:select property="program.functionalCentreId">
				<option value="">&nbsp;</option>
				
				<c:forEach var="functionalCentre" items="${functionalCentres}">
					<option value="<c:out value="${functionalCentre.accountId}" />" <c:if test="${oldProgram.functionalCentreId == functionalCentre.accountId}">selected</c:if> ><c:out value="${functionalCentre.accountId}" />, <c:out value="${functionalCentre.description}" /></option>
				</c:forEach>
			</html:select>
		</td>
	</tr>
	<tr class="b">
		<td width="20%">HIC:</td>
		<td><html:checkbox property="program.hic" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Address:</td>
		<td><html:text property="program.address" size="30"
			maxlength="255" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Phone:</td>
		<td><html:text property="program.phone" size="30" maxlength="25" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Fax:</td>
		<td><html:text property="program.fax" size="30" maxlength="25" /></td>
	</tr>
	<tr class="b">
		<td width="20%">URL:</td>
		<td><html:text property="program.url" size="30" maxlength="100" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Email:</td>
		<td><html:text property="program.email" size="30" maxlength="50" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Emergency Number:</td>
		<td><html:text property="program.emergencyNumber" size="30"
			maxlength="25" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Type:</td>
		<td><html:select property="program.type">
			<html:option value="Bed" />
			<html:option value="Service" />
			<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
				<html:option value="External" />
				<html:option value="community">Community</html:option>
			</caisi:isModuleLoad>
		</html:select></td>
	</tr>
	<tr class="b">
		<td width="20%">Status:</td>
		<td><html:select property="program.programStatus">
			<html:option value="active" />
			<html:option value="inactive" />
		</html:select></td>
	</tr>
	<tr class="b">
		<td width="20%">Location:</td>
		<td><html:text property="program.location" size="30"
			maxlength="70" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Max Participants:</td>
		<td><html:text property="program.maxAllowed" size="8"
			maxlength="8" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Holding Tank:</td>
		<td><html:checkbox property="program.holdingTank" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Allow Batch Admissions:</td>
		<td><html:checkbox property="program.allowBatchAdmission" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Allow Batch Discharges:</td>
		<td><html:checkbox property="program.allowBatchDischarge" /></td>
	</tr>
	<!-- 
	<tr class="b">
		<td width="20%">Link to Bed Program:</td>
		<td><html-el:select property="program.bedProgramLinkId">
			<html:option value="0">&nbsp;</html:option>
			<c:forEach var="bp" items="${bed_programs}">
				<html-el:option value="${bp.id}">
					<c:out value="${bp.name}" />
				</html-el:option>
			</c:forEach>
		</html-el:select></td>
	</tr>
	-->
	<tr class="b">
		<td width="20%">Man or Woman:</td>
		<td><html:select property="program.manOrWoman">
			<html:option value="" />
			<html:option value="Man" />
			<html:option value="Woman" />
		</html:select></td>
	</tr>
	<tr class="b">
		<td width="20%">Transgender:</td>
		<td><html:checkbox property="program.transgender" /></td>
	</tr>
	<tr class="b">
		<td width="20%">First Nation:</td>
		<td><html:checkbox property="program.firstNation" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Bed Program Affiliated:</td>
		<td><html:checkbox property="program.bedProgramAffiliated" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Alcohol:</td>
		<td><html:checkbox property="program.alcohol" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Abstinence Support?</td>
		<td><html:select property="program.abstinenceSupport">
			<html:option value=" " />
			<html:option value="Harm Reduction" />
			<html:option value="Abstinence Support" />
			<html:option value="Not Applicable" />
		</html:select></td>
	</tr>
	<tr class="b">
		<td width="20%">Physical Health:</td>
		<td><html:checkbox property="program.physicalHealth" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Mental Health:</td>
		<td><html:checkbox property="program.mentalHealth" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Housing:</td>
		<td><html:checkbox property="program.housing" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Exclusive View:</td>
		<td><html:select property="program.exclusiveView">
			<html:option value="no">No</html:option>
			<html:option value="appointment">Appointment View</html:option>
			<html:option value="case-management">Case-management View</html:option>
		</html:select> (Selecting "No" allows users to switch views)</td>
	</tr>
	<tr class="b">
		<td width="20%">Minimum Age (inclusive):</td>
		<td><html:text property="program.ageMin" size="8" maxlength="8" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Maximum Age (inclusive):</td>
		<td><html:text property="program.ageMax" size="8" maxlength="8" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Enable Mandatory Encounter Time:</td>
		<td><html:checkbox property="program.enableEncounterTime" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Enable Mandatory Transportation Time:</td>
		<td><html:checkbox property="program.enableEncounterTransportationTime" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Email Notification Addresses (csv):</td>
		<td><html:text property="program.emailNotificationAddressesCsv" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Enable OCAN:</td>
		<td><html:checkbox property="program.enableOCAN" /></td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Save" onclick="return save()" /> <html:cancel /></td>
	</tr>
</table>

</br>
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="sinatures">Signature</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td>&nbsp;</td>
		<td>Provider Name</td>
		<td>Role</td>
		<td>Date</td>
	</tr>
	<tr class="b">
		<td><a href="javascript:void(0)"
			onClick="getProgramSignatures('<c:out value="${id}"/>')"> <img
			alt="View details" src="<c:out value='${ctx}' />/images/details.gif"
			border="0" /> </a></td>
		<td><c:out value="${programFirstSignature.providerName}" /></td>
		<td><c:out value="${programFirstSignature.caisiRoleName}" /></td>
		<td><c:out value="${programFirstSignature.updateDate}" /></td>
	</tr>
</table>
