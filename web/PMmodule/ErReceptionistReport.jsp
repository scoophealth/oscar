<%@ include file="/taglibs.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<html>
	<head>
	<META HTTP-EQUIV="refresh" content="0;URL=<c:out value="${ctx}"/>/ReceptionistReport.do?id=<c:out value="${id}"/>">
	</head>
</html>
