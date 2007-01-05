<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>
<script>
	function select_program(id) {
		document.clientManagerForm.elements['program.id'].value = id;
		document.clientManagerForm.method.value = 'discharge_select_program';
		document.clientManagerForm.submit();
	}
	
	function do_discharge(id) {
		<c:if test="${empty requestScope.community_discharge}">
			document.clientManagerForm.method.value = 'discharge';
		</c:if>
		<c:if test="${not empty requestScope.community_discharge}">
			document.clientManagerForm.method.value = 'discharge_community';
		</c:if>
		document.clientManagerForm.submit();
	}
	
	function select_program_community() {
		var communityCtl = document.getElementById('community_id');
		var id = communityCtl.options[communityCtl.selectedIndex].value;
		document.clientManagerForm.elements['program.id'].value = id;
		document.clientManagerForm.method.value = 'discharge_community_select_program';
		document.clientManagerForm.submit();
	}
</script>
<html:hidden property="program.id" />
<P>This page is for discharging clients from:
<ol>
	<li>bed programs to non CAISI community bed programs</li>
	<li>service programs</li>
	<li>temporary admissions in bed programs</li>
</ol>
To discharge clients from a bed program to a CAISI bed program, you must make a referral to that bed program. The client can then be admitted to that bed program from the queue.
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
	<c:forEach var="cprogram" items="${communityPrograms}">
		<c:choose>
			<c:when test="${clientManagerForm.map.program.id == cprogram.id}">
				<option value="${cprogram.id}" selected="true"><c:out value="${cprogram.name}" /></option>
			</c:when>
			<c:otherwise>
				<option value="${cprogram.id}"><c:out value="${cprogram.name}" /></option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>
&nbsp;
<input type="button" value="Discharge" onclick="select_program_community()" />
<br />
<br />
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Discharge - Service Programs</th>
	</tr>
</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="serviceAdmissions" export="false" pagesize="0" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs." />
	<display:column sortable="false">
		<input type="button" value="Discharge" onclick="select_program('<c:out value="${admission.programId}"/>')" />
	</display:column>
	<display:column sortable="true" title="Program Name">
		<c:out value="${admission.programName}" />
	</display:column>
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<display:column property="admissionNotes" sortable="true" title="Admission Notes" />
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
<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="temporaryAdmissions" export="false" pagesize="0" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs." />
	<display:column sortable="false">
		<input type="button" value="Discharge" onclick="select_program('<c:out value="${admission.programId}"/>')" />
	</display:column>
	<display:column sortable="true" title="Program Name">
		<c:out value="${admission.programName}" />
	</display:column>
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<display:column property="admissionNotes" sortable="true" title="Admission Notes" />
</display:table>
<c:if test="${requestScope.do_discharge != null}">
	<br />
	<br />
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="20%">Discharge Notes:</td>
			<td><html:textarea cols="50" rows="7" property="admission.dischargeNotes" /></td>
		</tr>
		<tr class="b">
			<td colspan="2"><input type="button" value="Process Discharge" onclick="do_discharge();" /> <input type="button" value="Cancel" onclick="document.clientManagerForm.submit()" /></td>
		</tr>
	</table>
</c:if>