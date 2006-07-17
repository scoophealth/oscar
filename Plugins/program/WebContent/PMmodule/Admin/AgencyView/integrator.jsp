<%@ include file="/taglibs.jsp" %>

	<br/>
	<%@ include file="/messages.jsp"%>
	<br/>

	<c:choose>
		<c:when test="${requestScope.integrator_enabled == false}">
			<h3 style="color:red">Service is currently disabled</h3>
		</c:when>
		<c:otherwise>
			<h3 style="color:red">Service is currently enabled</h3>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${requestScope.integrator_registered == false}">
			<h3 style="color:red">Not Registered</h3>
		</c:when>
		<c:otherwise>
			<h3 style="color:red">Registered</h3>
		</c:otherwise>
	</c:choose>