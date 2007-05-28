<%@ include file="/taglibs.jsp"%>
<script>
	function save() {
		if(document.programManagerForm.elements['program.maxAllowed'].value <= 0) {
			alert('Maximum participants must be a positive integer');
			return false;
		}
		
		document.programManagerForm.method.value='save';
		document.programManagerForm.submit()
	}
</script>
<html:hidden property="program.numOfMembers" />
<html:hidden property="program.agencyId" value="0" />
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">General Information</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Name:</td>
		<td><html:text property="program.name" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Description:</td>
		<td><html:text property="program.descr" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">HIC:</td>
		<td><html:checkbox property="program.hic" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Address:</td>
		<td><html:text property="program.address" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Phone:</td>
		<td><html:text property="program.phone" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Fax:</td>
		<td><html:text property="program.fax" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">URL:</td>
		<td><html:text property="program.url" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Email:</td>
		<td><html:text property="program.email" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Emergency Number:</td>
		<td><html:text property="program.emergencyNumber" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Type:</td>
		<td>
			<html:select property="program.type">
				<html:option value="Bed" />
				<html:option value="Service" />
			</html:select>
		</td>
	</tr>
	<tr class="b">
		<td width="20%">Status:</td>
		<td>
			<html:select property="program.programStatus">
				<html:option value="active" />
				<html:option value="inactive" />
			</html:select>
		</td>
	</tr>
	<tr class="b">
		<td width="20%">Location:</td>
		<td><html:text property="program.location" size="30" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Max Participants:</td>
		<td><html:text property="program.maxAllowed" size="5" /></td>
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
	<tr class="b">
		<td width="20%">Link to Bed Program:</td>
		<td>
			<html-el:select property="program.bedProgramLinkId">
				<html:option value="0">&nbsp;</html:option>
				<c:forEach var="bp" items="${bed_programs}">
					<html-el:option value="${bp.id}"><c:out value="${bp.name}"/></html-el:option>
				</c:forEach>
			</html-el:select>
		</td>
	</tr>	
	<tr class="b">
		<td width="20%">Man or Woman:</td>
		<td>
			<html:select property="program.manOrWoman">
				<html:option value=""/>				
				<html:option value="Man" />
				<html:option value="Woman" />
			</html:select>
		</td>
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
		<td>
			<html:select property="program.abstinenceSupport">
				<html:option value=" "/>
				<html:option value="Harm Reduction" />
				<html:option value="Abstinence Support" />
				<html:option value="Not Applicable" />			
			</html:select>
		</td>
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
		    </html:select>
		    (Selecting "No" allows users to switch views)
		</td>
	</tr>	
	<tr>
		<td colspan="2">
			<input type="button" value="Save" onclick="return save()" />
			<html:cancel />
		</td>
	</tr>
</table>
