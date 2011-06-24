<%@ page language="java" contentType="text/html" %>
<% if (request.getAttribute("result") != null) { %>
	<%=(String) request.getAttribute("result") %>
<% } %>