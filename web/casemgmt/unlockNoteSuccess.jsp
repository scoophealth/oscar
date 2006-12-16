<%@ include file="/casemgmt/taglibs.jsp" %>
<html>
<head>
	<title>Case Management</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">

<script>
	function refresh_parent() {
		window.opener.location.reload(true);
	}
</script>
</head>

<body onload="refresh_parent()">
<h4>Note has been temporarily unlocked for you</h4>
<input type="button" value="Close Window" onclick="self.close()"/>
</body>

</html>