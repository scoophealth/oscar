<%@ include file="/taglibs.jsp" %>
<script>
	function do_admission() {
		var form = document.programManagerViewForm;
		form.method.value='admit';
		form.submit();
		
	}	
	function refresh_queue() {
		var form = document.programManagerViewForm;
		form.method.value='view';
		form.submit();
	}	

	function select_client(client_id,action) {
		var form = document.programManagerViewForm;
		form.elements['clientId'].value=client_id;
		if(action == 'admit') {
			form.method.value='select_client_for_admit';
		}
		if(action == 'reject') {
			if(!confirm('Are you sure you would like to reject admission for this client?')) {
				return;
			}
			form.method.value='reject_from_queue';
		}
		form.submit();
		
	}	
</script>
	<html:hidden property="clientId"/>
	<br/>
	<%@ include file="/messages.jsp"%>
	<br/>

		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Queue</th>
				</tr>
			</table>
		</div>
		<!--  show current clients -->
		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="queue_entry" name="queue" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="basic.msg.empty_list" value="Queue is empty."/>
		  <display:column sortable="false">
		  	<input type="button" value="Admit" onclick="select_client('<c:out value="${queue_entry.clientId}"/>','admit')"/>
		  </display:column>
		  <display:column sortable="false">
		  	<input type="button" value="Reject" onclick="select_client('<c:out value="${queue_entry.clientId}"/>','reject')"/>
		  </display:column>
	  	  <display:column property="clientFormattedName" sortable="true" title="Client Name"/>
		  <display:column property="referralDate"  sortable="true" title="Referral Date" />
		  <display:column property="providerFormattedName"  sortable="true" title="Referring Provider" />
		  <display:column property="notes"  sortable="true" title="Notes" />
		</display:table>	

	<br/>	
	<br/>
	<c:if test="${requestScope.do_admit != null}">
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
		
		<c:if test="${requestScope.current_admission != null}">
			<tr>
				<td colspan="2"><b style="color:red">Warning:<br/>
			This client is currently admitted to a bed program (<c:out value="${current_program.name}"/>).<br/>
			By completing this admission, you will be discharging them
			from this current program.</b>
			</td>
			</tr>
			<tr class="b">
				<td width="20%">Discharge Notes:</td>
				<td><textarea cols="50" rows="7" name="admission.dischargeNotes"></textarea></td>
			</tr>
		</c:if>
			<tr class="b">
				<td width="20%">Admission Notes:</td>
				<td><textarea cols="50" rows="7" name="admission.admissionNotes"></textarea></td>
			</tr>
			<tr class="b">
				<td colspan="2">
					<input type="button" value="Process Admission" onclick="do_admission()"/>
					<input type="button" value="Cancel" onclick="refresh_queue()"/>
				</td>
			</tr>
		</table>
	</c:if>
	
		
