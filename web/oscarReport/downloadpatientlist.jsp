<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>

<h1>Down Load Patient List</h1>
<tr>
	<td>
	<ul>
		<li><a href="patientlist.txt" /> Down load the Patient List Text
		File</li>
	</ul>
	</td>
</tr>
<tr>
	<td>
	<ul>
		<li><a href="patientlist.jsp">Back to Export Patient List
		Page</li>
	</ul>
	</td>
</tr>
</body>
</html>
