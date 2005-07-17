<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.util.*"%>

<html>
<head>
<%
String demographic_no = (String) request.getParameter("demographic_no");
String uri = (String) request.getParameter("uri");

%>

<title>OSCAR attachment <%=uri%> </title>

<frameset rows="100,100">
    <frame name="attMain" src="processPDF.jsp?demographic_no=<%=demographic_no%>&uri=<%=uri%>" noresize scrolling=auto marginheight=5 marginwidth=5>
    <frame name="attFrame" src="<%=uri%>&demographic_no=<%=demographic_no%>">
</frameset>

</html>