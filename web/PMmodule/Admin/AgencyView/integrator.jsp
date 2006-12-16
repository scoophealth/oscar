<%@ include file="/taglibs.jsp"%>

<c:choose>
	<c:when test="${requestScope.integrator_enabled == false}">
		<h3 style="color:red">Service is currently disabled</h3>
	</c:when>
	<c:otherwise>
		<h3 style="color:red">Service is currently enabled</h3>
	</c:otherwise>
</c:choose>