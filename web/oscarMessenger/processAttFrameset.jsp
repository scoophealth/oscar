<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.util.*"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<%
String demographic_no = (String) request.getParameter("demographic_no");
String uri = (String) request.getParameter("uri");
String pdfTitle = (String) request.getParameter("pdfTitle");

%>

<title>OSCAR attachment <%=uri%></title>
<frameset rows="400,0">
	<frame name="attMain"
		src="processPDF.jsp?demographic_no=<%=demographic_no%>&pdfTitle=<%=pdfTitle%>&uri=<%=uri%>"
		noresize scrolling=auto marginheight=5 marginwidth=5>
	<frame name="attFrame" src="">
</frameset>

</html>