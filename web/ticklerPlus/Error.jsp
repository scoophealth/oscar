<%@ include file="/ticklerPlus/taglibs.jsp"%>
<%@ page language="java" isErrorPage="true"%>

<html>
<head>
<title>An error has occurred</title>
</head>

<body>
<html:errors />


<% if (exception != null) { %>
<pre>
<% exception.printStackTrace(new java.io.PrintWriter(out)); %>
</pre>
<% } else { %>
Please check your log files for further information.
<% } %>
</body>
</html>