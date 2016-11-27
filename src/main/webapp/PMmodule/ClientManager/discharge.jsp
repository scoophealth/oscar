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
<%@ page import="org.oscarehr.PMmodule.model.DischargeReason"%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<script>
	function select_program(id,admissionId) {
		document.clientManagerForm.elements['program.id'].value = id;
		document.clientManagerForm.elements['admission.id'].value = admissionId;
		document.clientManagerForm.method.value = 'discharge_select_program';
		document.clientManagerForm.submit();
	}
	
	function select_program_community(admissionId) {
		var communityCtl = document.getElementById('community_id');
		var id = communityCtl.options[communityCtl.selectedIndex].value;
		document.clientManagerForm.elements['program.id'].value = id;
		document.clientManagerForm.method.value = 'discharge_community_select_program';
		document.clientManagerForm.submit();
	}
	
	function do_discharge() {
		var dischargeDate = document.getElementById('dischargeDate').value; 
		var dischargedFromFunctionalCentreRadioBox = document.clientManagerForm.elements['dischargedFromFunctionalCentre'];
		var fcDischarged = false;
		for(var i = 0; i < dischargedFromFunctionalCentreRadioBox.length; i++)
		   {
		      if(dischargedFromFunctionalCentreRadioBox[i].checked)
		      {
		         fcDischarged = true;
		      }
		   }
				
	      <%
	      	String admissionDateString=(String)request.getAttribute("admissionDate");
	      %>
	      	var adDate = "<%=admissionDateString%>";
	    	
	    	if(!dischargeDate || typeof dischargeDate == 'undefined') {
	    		alert("Please choose discharge date");
	    		return false;
	    	}    	
	    	if(!fcDischarged) {
	    		alert("Please choose being discharged from functional centre or not.");
	    		return false;
	    	}
	    	var today = new Date();
	 	    var dischargeDateString = dischargeDate.split('-') ;
	 	    var dischargeDateYear = dischargeDateString[0];
	 	    var dischargeDateMonth = dischargeDateString[1];
	 	    var dischargeDateDate = dischargeDateString[2];
	 	    var enterDate = new Date(dischargeDateYear, parseInt(dischargeDateMonth)-1, dischargeDateDate);
	 	    if (enterDate > today)
	 	    {
	 	        alert("Please don't enter future date");
	 	        return false;
	 	    }	
		    
	    	if(!compareDates(dischargeDate,adDate)) {
	    		alert("The discharge date should be later or equal to the admission Date.");
	    		return false;
	    	} 
	    	
		<c:if test="${empty requestScope.community_discharge}">						
			document.clientManagerForm.method.value = 'discharge';
		</c:if>
		<c:if test="${not empty requestScope.community_discharge}">			
			document.clientManagerForm.method.value = 'discharge_community';
		</c:if>
		document.clientManagerForm.submit();
	}
	
	function nestedReason() {
		<c:if test="${empty requestScope.community_discharge}">			
			document.clientManagerForm.method.value = 'nested_discharge_select_program';
			document.clientManagerForm.submit();
		</c:if>
		<c:if test="${not empty requestScope.community_discharge}">
			var communityCtl = document.getElementById('community_id');
			var id = communityCtl.options[communityCtl.selectedIndex].value;
			document.clientManagerForm.elements['program.id'].value = id;
			document.clientManagerForm.method.value = 'nested_discharge_community_select_program';
			document.clientManagerForm.submit();
		</c:if>					
	}
	//true: date2 <= date1
	function compareDates(date1, date2) {	
		
		var aDateString = date1.split('-') ;	
		
		
		if(aDateString[1]=='01') aDateString[1]=1;
		if(aDateString[1]=='02') aDateString[1]=2;
		if(aDateString[1]=='03') aDateString[1]=3;
		if(aDateString[1]=='04') aDateString[1]=4;
		if(aDateString[1]=='05') aDateString[1]=5;
		if(aDateString[1]=='06') aDateString[1]=6;
		if(aDateString[1]=='07') aDateString[1]=7;
		if(aDateString[1]=='08') aDateString[1]=8;
		if(aDateString[1]=='09') aDateString[1]=9;
		
		if(aDateString[2]=='01') aDateString[2]=1;
		if(aDateString[2]=='02') aDateString[2]=2;
		if(aDateString[2]=='03') aDateString[2]=3;
		if(aDateString[2]=='04') aDateString[2]=4;
		if(aDateString[2]=='05') aDateString[2]=5;
		if(aDateString[2]=='06') aDateString[2]=6;
		if(aDateString[2]=='07') aDateString[2]=7;
		if(aDateString[2]=='08') aDateString[2]=8;
		if(aDateString[2]=='09') aDateString[2]=9;	
		
		
		var sDateString ;
		if(date2 && typeof date2 != 'undefined') {
			sDateString = date2.split('-') ; 
			if(sDateString[1]=='01') sDateString[1]=1;
			if(sDateString[1]=='02') sDateString[1]=2;
			if(sDateString[1]=='03') sDateString[1]=3;
			if(sDateString[1]=='04') sDateString[1]=4;
			if(sDateString[1]=='05') sDateString[1]=5;
			if(sDateString[1]=='06') sDateString[1]=6;
			if(sDateString[1]=='07') sDateString[1]=7;
			if(sDateString[1]=='08') sDateString[1]=8;
			if(sDateString[1]=='09') sDateString[1]=9;
			
			if(sDateString[2]=='01') sDateString[2]=1;
			if(sDateString[2]=='02') sDateString[2]=2;
			if(sDateString[2]=='03') sDateString[2]=3;
			if(sDateString[2]=='04') sDateString[2]=4;
			if(sDateString[2]=='05') sDateString[2]=5;
			if(sDateString[2]=='06') sDateString[2]=6;
			if(sDateString[2]=='07') sDateString[2]=7;
			if(sDateString[2]=='08') sDateString[2]=8;
			if(sDateString[2]=='09') sDateString[2]=9;		
			
			if (sDateString[0]>aDateString[0]) {		  
			  	return false;
			} else if(sDateString[0]==aDateString[0] && sDateString[1] > aDateString[1]) {
				return false;
			} else if(sDateString[0]==aDateString[0] && sDateString[1] ==aDateString[1] && sDateString[2] > aDateString[2]) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}
	function radioEvent(form) 
	{
		var radioValue ;
	 	for (var i = 0; i < form.dischargedFromFunctionalCentre.length; i++) 
		{
	    	if (form.dischargedFromFunctionalCentre[i].checked) 
	    	{   
	    		radioValue = form.dischargedFromFunctionalCentre[i].value;
	    	}
		}
	 	if(radioValue == "true")
	 	{
	        alert("Please complete CDS and CBI.");
	    } else {
	    	alert("Please complete CDS and CBI if client address or other CBI data has changed.");
	    }
	
	 }
	
</script>

<html:hidden property="program.id" />
<html:hidden property="admission.id"/>

<p>This page is for discharging clients from:
<ol>
	<li>bed programs to non-CAISI community bed programs</li>
	<li>service programs</li>
	<li>temporary admissions in bed programs</li>
</ol>
To discharge clients from a bed program to a CAISI bed program, you must
make a referral to that bed program. The client can then be admitted to
that bed program from the queue.
</p>
<br />

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Discharge to non-CAISI community bed program</th>
	</tr>
</table>
</div>
Community Program:&nbsp;
<select id='community_id'>
	<c:forEach var="communityProgram" items="${communityPrograms}">
		<c:choose>
			<c:when
				test="${clientManagerForm.map.program.id == communityProgram.id}">
				<option value="<c:out value="${communityProgram.id}"/>"
					selected="true"><c:out value="${communityProgram.name}" /></option>
			</c:when>
			<c:otherwise>
				<option value="<c:out value="${communityProgram.id}"/>"><c:out
					value="${communityProgram.name}" /></option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
&nbsp;<input type="button" value="Discharge"
		onclick="select_program_community()" />
</caisi:isModuleLoad>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
	<c:if test="${sessionScope.performDischargeBed=='true'}">
&nbsp;<input type="button" value="Discharge"
			onclick="select_program_community()" />
	</c:if>
</caisi:isModuleLoad>


<br />
<br />

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Discharge - Service Programs</th>
	</tr>
</table>
</div>
<display:table class="simple" name="serviceAdmissions" uid="admission"
	requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list"
		value="This client is not currently admitted to any programs." />

	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
		<display:column sortable="false">
			<input type="button" value="Discharge"
				onclick="select_program('<c:out value="${admission.programId}"/>','<c:out value="${admission.id}"/>')" />
		</display:column>
	</caisi:isModuleLoad>

	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
		<c:if test="${sessionScope.performDischargeService=='true'}">
			<display:column sortable="false">
				<input type="button" value="Discharge"
					onclick="select_program('<c:out value="${admission.programId}"/>','<c:out value="${admission.id}"/>')" />
			</display:column>
		</c:if>
	</caisi:isModuleLoad>

	<display:column sortable="true" title="Program Name">
		<c:out value="${admission.programName}" />
	</display:column>
	<display:column property="admissionDate" sortable="true"
		title="Admission Date" />
	<display:column property="admissionNotes" sortable="true"
		title="Admission Notes" />
</display:table>
<br />
<br />

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Discharge - Temporary Program</th>
	</tr>
</table>
</div>
<display:table class="simple" name="temporaryAdmissions" uid="admission"
	requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list"
		value="This client is not currently admitted to any programs." />

	<display:column sortable="false">
		<input type="button" value="Discharge"
			onclick="select_program('<c:out value="${admission.programId}"/>',,'<c:out value="${admission.id}"/>')" />
	</display:column>
	<display:column sortable="true" title="Program Name">
		<c:out value="${admission.programName}" />
	</display:column>
	<display:column property="admissionDate" sortable="true"
		title="Admission Date" />
	<display:column property="admissionNotes" sortable="true"
		title="Admission Notes" />
</display:table>

<c:if test="${requestScope.do_discharge != null}">
	<br />
	<br />
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		 <tr><td>Discharge Reason</td>
                        <td><html:select property="admission.radioDischargeReason">
                                <html:option value='<%="" + DischargeReason.ADMITTED_TO_LTC_FACILITY.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.ADMITTED_TO_LTC_FACILITY" /></html:option>
                                <html:option value='<%="" + DischargeReason.DOES_NOT_FIT_CRITERIA.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.DOES_NOT_FIT_CRITERIA" /></html:option>
                                <html:option value='<%="" + DischargeReason.REQUIRES_ACUTE_CARE.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.REQUIRES_ACUTE_CARE" /></html:option>
                                <html:option value='<%="" + DischargeReason.COMPLETION_WITH_REFERRAL.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.COMPLETION_WITH_REFERRAL" /></html:option>
                                <html:option value='<%="" + DischargeReason.COMPLETION_WITHOUT_REFERRAL.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.COMPLETION_WITHOUT_REFERRAL" /></html:option>
                                <html:option value='<%="" + DischargeReason.DEATH.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.DEATH" /></html:option>
                                <html:option value='<%="" + DischargeReason.NO_SPACE_AVAILABLE.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.NO_SPACE_AVAILABLE" /></html:option>
                                <html:option value='<%="" + DischargeReason.RELOCATION.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.RELOCATION" /></html:option>
                                <html:option value='<%="" + DischargeReason.SERVICE_PLAN_COMPLETED.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.SERVICE_PLAN_COMPLETED" /></html:option>
                                <html:option value='<%="" + DischargeReason.SUICIDE.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.SUICIDE" /></html:option>
                                <html:option value='<%="" + DischargeReason.WITHDRAWL_CLIENT_PREFERENCE.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.WITHDRAWL_CLIENT_PREFERENCE" /></html:option>
                                <html:option value='<%="" + DischargeReason.OTHER.ordinal()%>'><bean:message bundle="pmm" key="discharge.reason.OTHER" /></html:option>

                                </html:select>
                        </td>
                </tr>
	
		<tr class="b">
			<td width="20%">Discharge Notes:</td>
			<td><html:textarea cols="50" rows="7"
				property="admission.dischargeNotes" /></td>
		</tr>
		<tr>
			<td width="20%">Discharge Date:</td>
			<td><input id="dischargeDate" name="dischargeDate" onfocus="this.blur()" readonly="readonly" type="text" value='<c:out value="${admission.formattedDischargeDate}"/>'> <img title="Calendar" id="cal_dischargeDate" src="<%=request.getContextPath()%>/images/cal.gif" alt="Calendar" border="0"><script type="text/javascript">Calendar.setup({inputField:'dischargeDate',ifFormat :'%Y-%m-%d',button :'cal_dischargeDate',align :'cr',singleClick :true,firstDay :1});</script>
			</td>
		</tr>
		
		<tr>
			<td width="20%">Discharge From Functional Centre</td>
			<td><input type="radio" id="dischargedFromFunctionalCentre1" name="dischargedFromFunctionalCentre"  value="true" onclick="radioEvent(this.form);"/>Yes	
				<input type="radio" id="dischargedFromFunctionalCentre2" name="dischargedFromFunctionalCentre"  value="false" onclick="radioEvent(this.form);"/>	No	
			</td>			
		</tr>
		
		<tr class="b">
			<td colspan="2"><input type="button" value="Process Discharge"
				onclick="return do_discharge();" /> <input type="button" value="Cancel"
				onclick="document.clientManagerForm.submit()" /></td>
		</tr>
	</table>
</c:if>
