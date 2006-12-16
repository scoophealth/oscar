<%@ include file="/taglibs.jsp"%>

<script>
	function enableIntegrator() {
		document.agencyManagerForm.method.value='enable_integrator';
		if(confirm('By enabling this service, you agree to the terms and conditions....\nare you sure you would like to continue?')) {
			document.agencyManagerForm.submit();
		}
	}
	
	function disableIntegrator() {
		document.agencyManagerForm.method.value='disable_integrator';
		if(confirm('By disabling this service, you will remove all community functionality')) {
			document.agencyManagerForm.submit();		
		}
	}
</script>

<!-- Show current status here -->
<c:choose>
	<c:when test="${requestScope.integratorEnabled == false}">
		<h3 style="color:red">Service is currently disabled</h3>
	</c:when>
	<c:otherwise>
		<h3 style="color:red">Service is currently enabled</h3>
	</c:otherwise>
</c:choose>
<br />

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Integrator Information</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Agency Id:</td>
		<td><c:out value="${requestScope.id}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Integrator URL:</td>
		<td><html:text property="agency.integratorURL" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Integrator username:</td>
		<td><html:text property="agency.integratorUserName" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Integrator password:</td>
		<td><html:text property="agency.integratorPassword" /></td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="button" value="Save" onclick="this.form.method.value='save';this.form.submit()" />
			<html:cancel />
		</td>
	</tr>
</table>
<br />
<c:choose>
	<c:when test="${requestScope.integratorEnabled == false}">
		<input type="button" value="Enable Integrator Service" onclick="enableIntegrator();" />
	</c:when>
	<c:otherwise>
		<input type="button" value="Disable Integrator Service" onclick="disableIntegrator();" />
	</c:otherwise>
</c:choose>