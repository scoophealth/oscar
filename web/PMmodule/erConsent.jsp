<%@ include file="/taglibs.jsp" %>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*" %>
		<style>
			.sortable {
				background-color: #555;
				color: #555;
			}
			.b th{
				border-right: 1px solid #333;
				background-color: #ddd;
				color: #ddd;
				border-left: 1px solid #fff;
			}
			.message {
				color: red;
				background-color: white;
			}
			.error {
				color: red;
				background-color: white;
			}
		</style>
		
		<script>
			function cancel() {
				location.href='<html:rewrite action="/PMmodule/ClientSearch2"/>';
			}
			
			function explicit() {
				var reason = document.clientManagerForm.elements['erconsent.consentReason'].value;
				if(reason == '') {
					alert('Please provide a reason client is visiting your agency');
					return false;
				}
				document.clientManagerForm.elements['erconsent.consentType'].value='explicit';
				document.clientManagerForm.method.value='submit_erconsent';
				document.clientManagerForm.submit();
			}
			
			function implied() {
				var reason = document.clientManagerForm.elements['erconsent.consentReason'].value;
				if(reason == '') {
					alert('Please provide a reason client is visiting your agency');
					return false;
				}
				document.clientManagerForm.elements['erconsent.consentType'].value='implied';
				document.clientManagerForm.method.value='submit_erconsent';
				document.clientManagerForm.submit();
			}
		</script>
		
		<br/><br/><br/>
		<html:form action="/PMmodule/ClientManager.do">
			<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>"/>
			<input type="hidden" name="method" value="submit_erconsent"/>	
			<html:hidden property="erconsent.consentType"/>
			
			<p>If appropriate, please ask client if you have their explicit consent to view their CAISI record.</p>
			<br>
			<table width="100%" cellpadding="3" cellspacing="4">
				<tr>
					<td>Please enter reason client is visiting your agency (presenting problem):</td>
				</tr>
				<tr>
				
				</tr>
					<td><html:textarea property="erconsent.consentReason" rows="8" cols="80"></html:textarea></td>
				<tr>
					<td>
						<input type="button" value="Implied Consent" onclick="implied()"/>
						<input type="button" value="Explicit Consent" onclick="explicit()"/>
						<input type="button" value="Cancel" onclick="cancel()"/>
					</td>
			</table>
		</html:form>