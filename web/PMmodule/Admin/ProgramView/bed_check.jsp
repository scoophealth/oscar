<%@ include file="/taglibs.jsp"%>
<html:hidden property="bedId" />
<script type="text/javascript">
	function unreserveBed(id) {
		document.programManagerViewForm.bedId.value = id;
		document.programManagerViewForm.method.value = 'unreserveBed';
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
			<display:table class="simple" name="reservedBeds" uid="bed" export="true" requestURI="/PMmodule/ProgramManagerView.do">
				<display:setProperty name="paging.banner.placement" value="bottom" />
				
				<display:setProperty name="export.banner" value="Generate: {0}" />
				<display:setProperty name="export.excel" value="false" />
				<display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.label" value="Bed Check Report"/>
				<display:setProperty name="export.pdf.filename" value="BedCheckReport.pdf"/>
				
				<display:column property="name" title="Bed" />
				<display:column property="roomName" title="Room" />
				<display:column property="demographicName" title="Client" />
				<display:column property="statusName" title="Status" />
				<display:column property="latePass" title="Late Pass" />
				<display:column property="reservationStart" title="Reserved Since" />
				<display:column property="reservationEnd" title="Reserved Until" />
				<display:column title="Not Present" media="pdf" />
				<display:column media="html">
					<html:submit onclick="unreserveBed('${bed.id}');">Unreserve Bed</html:submit>
				</display:column>
			</display:table>
			<!-- recently unreserved beds go here -->
		</td>
	</tr>
</table>