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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<script type="text/javascript">
	function popupBedReservationChangeReport(reservedBedId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedReservationChangeReport&reservedBedId="/>';
		window.open(url + reservedBedId, 'bedHistoryReport', 'width=800,height=400');
	}
	
	function popupBedCheckReport(programId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedCheckReport&programId="/>';
		window.open(url + programId, 'bedCheckReport', 'width=1000,height=600,scrollbars=1');
	}
	
	function switchBeds(){
		var theForm = document.forms[0];
		var switchBedId = "";
		var indexBedId = -1;
		var count = 0;
		for(i=0; i < theForm.elements.length; i++){
   			var elementName = theForm.elements[i].name;
   			
   			if( elementName.indexOf("switchBedIds") != -1 ){
   				if(theForm.elements[i].checked == true){
	   				count++;
				}
   			}
		}
 		if(count < 2  ||  count > 2){
 			//alert("switchBeds(): count 1= " + count);
			alert("You must select 2 beds for bed switching.");
			return;
 		}
 		theForm.switchBed1.value = "";
 		theForm.switchBed2.value = "";
   		for(i=0; i < theForm.elements.length; i++){
   			var elementName = theForm.elements[i].name;
   			if( elementName.indexOf("switchBedIds") != -1 ){
   				indexBedId = elementName.indexOf("_");
   				if(indexBedId != -1){
   					switchBedId = elementName.substring(indexBedId+1, elementName.length);
   				}
	 			if(theForm.elements[i].checked == true){
					if(theForm.switchBed1.value == ""){
						theForm.switchBed1.value = switchBedId;
					}
					if(theForm.switchBed2.value == ""  || theForm.switchBed1.value != switchBedId){
						theForm.switchBed2.value = switchBedId;
					}
	 			}
			}
		}		
		//alert("switchBeds(): switchBed1 2= " + theForm.switchBed1.value);
		//alert("switchBeds(): switchBed2 2= " + theForm.switchBed2.value);
		document.forms[0].action = '<html:rewrite action="/PMmodule/ProgramManagerView.do?method=switch_beds" />';
		document.forms[0].submit();
	}
	
</script>

<html:hidden property="switchBed1" />
<html:hidden property="switchBed2" />

<table width="100%" summary="View program reserved beds">
	<tr>
		<td>
		<div class="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Reserved Beds</th>
			</tr>
		</table>
		</div>
		<display:table class="simple"
			name="sessionScope.programManagerViewForm.reservedBeds"
			uid="reservedBed" requestURI="/PMmodule/ProgramManagerView.do">
			<display:column>
				<a href="javascript:void(0)"
					onclick="popupBedReservationChangeReport('<c:out value="${reservedBed.id}" />')">
				<img src="<c:out value="${ctx}" />/images/details.gif" border="0" />
				</a>
			</display:column>

			<display:column>
				<input type="checkbox"
					name="switchBedIds[<c:out value="${reservedBed_rowNum - 1}" />]_<c:out value="${reservedBed.id}" />" />
			</display:column>

			<display:column property="roomName" title="Room" />
			<display:column property="name" title="Bed" />
			<display:column property="demographicName" title="Client" />
			<display:column property="familyId" title="Family Id" />
			<!-- status is editable -->
			<display:column title="Status">
				<select
					name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].statusId">
					<c:forEach var="bedDemographicStatus"
						items="${bedDemographicStatuses}">
						<c:choose>
							<c:when test="${bedDemographicStatus.id == reservedBed.statusId}">
								<option value="<c:out value="${bedDemographicStatus.id}"/>"
									selected="selected"><c:out
									value="${bedDemographicStatus.name}" /></option>
							</c:when>
							<c:otherwise>
								<option value="<c:out value="${bedDemographicStatus.id}"/>">
								<c:out value="${bedDemographicStatus.name}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</display:column>
			<!-- latePass is editable -->
			<display:column title="Late Pass">
				<c:choose>
					<c:when test="${reservedBed.latePass}">
						<input type="checkbox"
							name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].latePass"
							checked="checked" />
					</c:when>
					<c:otherwise>
						<input type="checkbox"
							name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].latePass" />
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column property="reservationStart" title="Since" />
			<!-- reservationEnd is editable -->
			<display:column title="Until">
				<input type="text"
					name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].strReservationEnd"
					id="strReservationEnd_<c:out value="${reservedBed_rowNum - 1}" />_field"
					readonly="readonly"
					value="<c:out value="${reservedBed.strReservationEnd}"/>" />

				<img align="top" src="<html:rewrite page="/images/calendar.gif" />"
					id="strReservationEnd_<c:out value="${reservedBed_rowNum - 1}" />_field-button"
					alt="Until Calendar" title="Until Calendar" />

				<script type="text/javascript">
						Calendar.setup(
							{
								inputField :  'strReservationEnd_' + <c:out value="${reservedBed_rowNum - 1}" /> + '_field',
								ifFormat :    '%Y-%m-%d',
								button :      'strReservationEnd_' + <c:out value="${reservedBed_rowNum - 1}" /> + '_field-button',
								align :       'cr',
								singleClick : true,
								firstDay :    1
							}
						);
					</script>
			</display:column>
			<!-- communityProgramId is editable -->

			<display:column title="Discharge To">
				<select
					name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].communityProgramId">
					<option value="0"></option>
					<c:choose>
						<c:when test="${isFamilyDependents != null}">
							<c:if test="${!isFamilyDependents[reservedBed_rowNum - 1]}">
								<c:forEach var="communityProgram" items="${communityPrograms}">
									<option value="<c:out value="${communityProgram.id}"/>"><c:out
										value="${communityProgram.name}" /></option>
								</c:forEach>
							</c:if>
						</c:when>
					</c:choose>
				</select>
			</display:column>
		</display:table></td>

	</tr>
	<tr>
		<td>Generate: <a href="javascript:void(0)"
			onclick="popupBedCheckReport('<c:out value="${id}"/>')">Bed Check
		Report</a> <input type="button" name="bedSwitch" onclick="switchBeds()"
			value="Switch Beds" /> <html:submit
			onclick="programManagerViewForm.method.value='saveReservedBeds';">Save Changes</html:submit>
		</td>
	</tr>
	<tr>
		<td><br />
		</td>
	</tr>
	<tr>
		<td>
		<div class="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Todays' Expired Reservations</th>
			</tr>
		</table>
		</div>
		<display:table class="simple" name="expiredReservations"
			uid="expiredReservation">
			<display:column property="bedName" title="Bed" />
			<display:column property="demographicName" title="Client" />
		</display:table></td>
	</tr>
</table>
