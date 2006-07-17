<%@ include file="/taglibs.jsp" %>

<html>
<head>
	<title>CAISI Help</title>
</head>

<body>
<c:choose>
	<c:when test="${param.topic == null}">
		No Topic Chosen
	</c:when>
	<c:otherwise>
		<h3><c:out value="${param.topic}"/></h3><br/>
		<bean:message key="<%=request.getParameter("topic") %>" bundle="help"/>
	</c:otherwise>
</c:choose>

<br/>
<center><input type="button" value="Close" onclick="self.close();"/></center>
</body>
</html>