<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty FacilityMessages}">
	<table width="100%">
		<c:forEach var="message" items="${FacilityMessages}">
			<c:if test="${message.active eq 'true'}">
				<tr>
					<td><font color="red" size="+1"><c:out
						value="${message.facilityName}" escapeXml="false" />&nbsp;Message
					- <c:out value="${message.message}" escapeXml="false" /></font></td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
</c:if>
