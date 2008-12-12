<%@ include file="/taglibs.jsp"%>

<html:hidden property="addTime" />
<html:hidden property="removeId" />

<script type="text/javascript">
	function addBedCheckTime(id) {
		var addPopupTimePicker = dojo.widget.byId("addPopupTimePicker");
		
		document.programManagerForm.addTime.value = 
			addPopupTimePicker.timePicker.selectedTime['hour'] + ":" + 
			addPopupTimePicker.timePicker.selectedTime['minute'] + " " + 
			addPopupTimePicker.timePicker.selectedTime['amPm'];
		
		if (!dojo.validate.is12HourTime(document.programManagerForm.addTime.value)) {
			return false;
		}

		document.programManagerForm.method.value = 'addBedCheckTime';
		document.programManagerForm.submit();
	}
	
	function removeBedCheckTime(id) {
		document.programManagerForm.removeId.value = id;
		document.programManagerForm.method.value = 'removeBedCheckTime';
		document.programManagerForm.submit();
	}
</script>

<table width="100%" summary="Edit bed check">
	<tr>
		<td>
		<div class="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Times</th>
			</tr>
		</table>
		</div>
		<display:table class="simple"
			name="sessionScope.programManagerForm.bedCheckTimes"
			uid="bedCheckTime" requestURI="/PMmodule/ProgramManager.do">
			<display:column property="time" format="{0, time, short}" title="" />
			<display:column>
				<input type="button" value="Remove"
					onclick="removeBedCheckTime('<c:out value="${bedCheckTime.id}"/>');" />
			</display:column>
		</display:table></td>
	</tr>
</table>
<table>
	<tr>
		<td><input id="addPopupTimePicker" dojoType="DropdownTimePicker"
			readonly="true" /> <input type="button" value="Add"
			onclick="addBedCheckTime();" /></td>
	</tr>
</table>