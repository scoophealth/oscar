<%@ include file="/survey/taglibs.jsp" %>
<td colspan="2">
<table border="1" width="100%">
<tr>
	<td colspan="2">
		<c:out value="${section.description}"/>
	</td>
</tr>
		<c:forEach var="question" items="${section.questionArray}">
			<tr>
				<c:set var="question" value="${question}" scope="request"/>
				<c:set var="sectionId" value="${section.id}" scope="request"/>
				<jsp:include page="question.jsp"/>
			</tr>
		</c:forEach>
</table>
</td>