<%@ include file="/taglibs.jsp"%>

<html:hidden property="bedId" />

<script type="text/javascript">
	function unreserveBed(id) {
		document.programManagerViewForm.bedId.value = id;
		document.programManagerViewForm.method.value = 'unreserveBed';
	}
	
	function popupBedCheckReport(programId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedCheckReport&programId="/>';
		window.open(url + programId, 'bedCheckReport', 'width=800,height=600');
	}
</script>

<table width="100%" summary="View program beds">
	<tr>
		<td width="80%">
			<div class="tabs" id="bedsHeader">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Reserved Beds</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="reservedBeds" uid="bed" requestURI="/PMmodule/ProgramManagerView.do">
				<display:column property="name" title="Bed" />
				<display:column property="roomName" title="Room" />
				<display:column property="demographicName" title="Client" />
				<display:column property="statusName" title="Status" />
				<display:column property="latePass" title="Late Pass" />
				<display:column property="reservationStart" title="Reserved Since" />
				<display:column property="reservationEnd" title="Reserved Until" />
				<display:column>
					<html:submit onclick="unreserveBed('${bed.id}')">Unreserve Bed</html:submit>
				</display:column>
			</display:table>
			<!-- recently unreserved beds go here -->
		</td>
	</tr>
	<tr>
		<td>
			Generate: <a href="javascript:void(0)" onclick="popupBedCheckReport('<c:out value="${id}"/>')">Bed Check Report</a> 
		</td>
	</tr>
</table>