<%@ include file="/taglibs.jsp"%>

<html>
	<head>
		<title>Current list of agencies</title>
	</head>
	
	<body>
		<h1>Agencies</h1>
		<br/>
		<ul>
			<c:forEach var="agency" items="${agencies}">
				<c:if test="${agency.key != 0}">
					<li><c:out value="${agencies[agency.key].name}"/></li>
				</c:if>
			</c:forEach>
		</ul>
	</body>
	
</html>	