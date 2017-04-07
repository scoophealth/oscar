<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page import="java.io.*, java.util.*, oscar.eform.*, oscar.eform.EFormUtil"%>

<option value="">template</option>

<%
ArrayList<String> templates = EFormUtil.listRichTextLetterTemplates();
Collections.sort(templates);
File file = null;
for (String template : templates) {
	String name = template;
	// Stripping extension from name if appropriate.
	if (name.lastIndexOf('.') > 0) {
		name  = template.substring(0, name.lastIndexOf('.'));
	}
%>

<option value="<%=template%>"><%=name%></option>

<%		
}
%>
