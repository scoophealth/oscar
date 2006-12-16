<%@ include file="/taglibs.jsp" %>

<html:html>
	<head>
		<title>Program Activity Report Generator</title>
	</head>
	
	<body>
	<br/><br/>
	
	<h1>Summary</h1>
	
	<table class="b" width="100%">
		<tr>
			<th style="color:black" bgcolor="grey">Program</th>
			<th style="color:black" bgcolor="grey">Total Admissions</th>
			<th style="color:black" bgcolor="grey">Total Referrals</th>
		</tr>
		<c:forEach var="stat" items="${summary}">
			<tr>
				<td><c:out value="${stat.key}"/></td>
				<c:forEach var="value" items="${stat.value }">
					<td><c:out value="${value}"/></td>
				</c:forEach>			
			</tr>
		</c:forEach>
	</table>
	

	</body>
	
</html:html>