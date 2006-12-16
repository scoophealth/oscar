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
				<th title="Programs">Exit Interviews</th>
			</tr>
		</table>
	</div>
	
	<table class="b" width="100%">
		<tr>
			<th style="color:black">Interview Id</th>
			<th style="color:black">Form Name</th>
			<th style="color:black">Form Version</th>
			<th style="color:black">language</th>
			<th style="color:black">language (read)</th>
			<th style="color:black">education</th>
			<th style="color:black">review</th>
			<th style="color:black">pressure</th>
			<th style="color:black">information</th>
			<th style="color:black">followup</th>
			<th style="color:black">comments</th>
		</tr>
		<c:forEach var="interview" items="${interviews}">
			<tr>
				<td><a href="<html:rewrite action="/PMmodule/Reports/ExitInterviewReportDetailed"/>?method=view&id=<c:out value="${interview.id}"/>"><c:out value="${interview.id}"/></a></td>
				<td><c:out value="${interview.formName}"/></td>
				<td><c:out value="${interview.formVersion}"/></td>
				<td><c:out value="${interview.language}"/></td>
				<td><c:out value="${interview.languageRead}"/></td>
				<td><c:out value="${interview.education}"/></td>
				<td><c:out value="${interview.review}"/></td>
				<td><c:out value="${interview.pressure}"/></td>
				<td><c:out value="${interview.information}"/></td>
				<td><c:out value="${interview.followup}"/></td>
				<td><c:out value="${interview.comments}"/></td>
			</tr>
		</c:forEach>
		</table>
		<br/>
		<br/>
		<a href="<html:rewrite action="/PMmodule/Reports/ExitInterviewReport.do"/>?method=export&type=csv">Export all Detailed data to Excel</a>