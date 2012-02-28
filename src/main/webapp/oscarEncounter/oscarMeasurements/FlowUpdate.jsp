<html>

<head>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/newCaseManagementView.js"></script>

</head>


<body>
<b>Following fields had bad input and could not be updated:</b>
<br>
<pre><%=request.getAttribute("testOutput") %></pre>
</body>

</html>