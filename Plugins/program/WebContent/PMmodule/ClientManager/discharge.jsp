<%@ include file="/taglibs.jsp" %>
<script>
	function select_program(id) {
		document.clientManagerForm.elements['program.id'].value = id;
		document.clientManagerForm.method.value = 'discharge_select_program';
		document.clientManagerForm.submit();
	}
	
	function do_discharge(id) {
		document.clientManagerForm.method.value = 'discharge';
		document.clientManagerForm.submit();
	}
</script>
		<html:hidden property="program.id"/>
	
	<h4>This page is for discharging the client from "service" type programs.
	If you would like to discharge the client from the current "bed" program, you must
	use the 'admit' tab to admit them into a new "bed" program.
	</h4>
	<br/>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Discharge</th>
			</tr>
		</table>
	</div>
	<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="admission" name="serviceAdmissions" export="false" pagesize="0" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  <display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs."/>
	  <display:column sortable="false">
	  	<input type="button" value="Discharge" onclick="select_program('<c:out value="${admission.programId}"/>')"/>
	  </display:column>
	  <display:column sortable="true" title="Program Name">
	  	<c:out value="${admission.programName}"/>
	  </display:column>
	  <display:column property="admissionDate" sortable="true" title="Admission Date"/>
      <display:column property="admissionNotes"  sortable="true" title="Admission Notes" />	
	</display:table>	
	
	<br/>
	<br/>
	<c:if test="${requestScope.do_discharge != null}">
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Discharge Notes:</td>
				<td><html:textarea cols="50" rows="7" property="admission.dischargeNotes"/></td>
			</tr>
			<tr class="b">
				<td colspan="2">
					<input type="button" value="Process Discharge" onclick="do_discharge();"/>
					<input type="button" value="Cancel" onclick="document.clientManagerForm.submit()"/>
				</td>
			</tr>
		</table>
	</c:if>
