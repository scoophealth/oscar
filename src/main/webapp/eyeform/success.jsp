<html>
<head>
<title>Ocular Module</title>
<script>
function c() {
	<%
		String parentAjaxId = request.getParameter("parentAjaxId");
		if(parentAjaxId == null) {
			parentAjaxId = (String) request.getAttribute("parentAjaxId");
		}
		if(parentAjaxId != null) {
	%>
		window.opener.reloadNav('<%=parentAjaxId%>');
		window.close();
	<% } else { %>
	window.opener.location.reload();window.close();
	<% } %>
	
}
</script>
</head>
<body onload="c()">

</body>
</html>
