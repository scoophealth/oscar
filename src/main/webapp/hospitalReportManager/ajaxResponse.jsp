<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page language="java" contentType="text/html" %>
<% if (request.getAttribute("success") != null) { %>
	<%=((Boolean) request.getAttribute("success") ? "Success" : "Error encountered") %>
<% } %>
