<%@ page language="java" contentType="text/html" %>
<% if (request.getAttribute("success") != null) { %>
	<%=((Boolean) request.getAttribute("success") ? "Success" : "Error encountered") %>
<% } %>