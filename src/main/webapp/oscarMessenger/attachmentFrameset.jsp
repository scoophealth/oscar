<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.util.*"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<%
String demographic_no = (String) request.getParameter("demographic_no");
%>

<title>OSCAR attachment</title>

<% if ( demographic_no != null ) { %>

<frameset rows="300,0">
	<frame name="main"
		src="generatePreviewPDF.jsp?demographic_no=<%=demographic_no%>"
		noresize scrolling=auto marginheight=5 marginwidth=5>
	<frame name="srcFrame" src="">
</frameset>
<% } else { %>
Please select a demographic.
<% } %>
</html>