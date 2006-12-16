<%@ include file="/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty requestScope.client }">
		<c:choose>
			<c:when test="${empty requestScope.client.formattedLinks}">
				None Found
			</c:when>
			<c:otherwise>
				<c:out value="${client.formattedLinks}" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		Unable to complete request
	</c:otherwise>
</c:choose>