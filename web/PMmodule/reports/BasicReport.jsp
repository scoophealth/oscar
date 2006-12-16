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

		
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Program Statistics</th>
			</tr>
		</table>
	</div>
	
	<table class="b" width="100%">
		<tr>
			<th style="color:black">Description</th>
			<th style="color:black">Value</th>
		</tr>
		<c:forEach var="stat" items="${programStatistics}">
			<tr>
				<td><c:out value="${stat.key}"/></td>
				<td><c:out value="${stat.value}"/></td>
			</tr>
		</c:forEach>
	</table>
	
