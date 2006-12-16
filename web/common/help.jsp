<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
	<head>
		<html:base />
		<title>CAISI Help</title>
		<style type="text/css">
			@import "<html:rewrite page="/css/tigris.css" />";
		</style>
	</head>
	<body>
		<c:choose>
			<c:when test="${param.topic == null}">
				No Topic Chosen
			</c:when>
			<c:otherwise>
				<h3><c:out value="${param.topic}" /></h3>
				<br />
				<bean:message key="<%=request.getParameter("topic") %>" bundle="help" />
			</c:otherwise>
		</c:choose>
	<br />
	<center>
		<input type="button" value="Close" onclick="self.close();" />
	</center>
	</body>
</html:html>