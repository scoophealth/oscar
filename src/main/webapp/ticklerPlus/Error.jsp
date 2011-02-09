<%@ include file="/ticklerPlus/taglibs.jsp"%>
<%@page isErrorPage="true"%>


<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>An error has occurred</title>
</head>

<body>
<html:errors />

Please check your log files for further information.

<% 
if (exception != null) { 
	MiscUtils.getLogger().error("error", exception); 
}
%>
</body>
</html>