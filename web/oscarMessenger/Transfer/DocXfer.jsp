<html>
<head>
<title>
DocXfer
</title>
</head>
<body>
<h1>
Document Transfer
</h1>

<%= this.getServletContext().getRealPath(request.getServletPath()) %>

<form method="post" action="SelectItems.jsp">
    Demographic No: <input type="text" name="demographicNo" value="<%= request.getParameter("demo")%>" />
    <input type="submit" name="submitXml" value="Submit to XML">
    <input type="submit" name="submit" value="Submit" onclick="javascript:form.action='SelectItems.jsp';">
    <input type="reset" value="Reset">
</form>
</body>
</html>
