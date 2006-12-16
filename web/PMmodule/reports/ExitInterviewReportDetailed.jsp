<%@ include file="/taglibs.jsp" %>
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
	
	<br/><br/>
	
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Exit Interview - Detailed View</th>
			</tr>
		</table>
	</div>
	
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="20%">Interview Id:</td>
			<td><c:out value="${interview.id}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Form Name:</td>
			<td><c:out value="${interview.formName}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Form Version:</td>
			<td><c:out value="${interview.formVersion}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Preferred language:</td>
			<td><c:out value="${interview.language}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Preferred Language - Other:</td>
			<td><c:out value="${interview.languageOther}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Preferred Language (Read):</td>
			<td><c:out value="${interview.languageRead}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Preferred Language (Read) - Other:</td>
			<td><c:out value="${interview.languageReadOther}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Highest Level of Education:</td>
			<td><c:out value="${interview.education}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Sufficient time to review:</td>
			<td><c:out value="${interview.review}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Sufficient time to review - Comments:</td>
			<td><c:out value="${interview.reviewOther}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Pressure to join CAISI:</td>
			<td><c:out value="${interview.pressure}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Pressure to join CAISI - Comments:</td>
			<td><c:out value="${interview.pressureOther}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Require more Information:</td>
			<td><c:out value="${interview.information}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Require more Information - Comments:</td>
			<td><c:out value="${interview.informationOther}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Follow-Up:</td>
			<td><c:out value="${interview.followup}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Follow-Up - Comments:</td>
			<td><c:out value="${interview.followupOther}"/></td>
		</tr>
		
		<tr class="b">
			<td width="20%">Comments:</td>
			<td><c:out value="${interview.comments}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Comments:</td>
			<td><c:out value="${interview.commentsOther}"/></td>
		</tr>
		
		
	</table>
	
			