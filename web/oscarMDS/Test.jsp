<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.*" %>

<html>
<head>
<title>
Test Page
</title>
</head>

<body>
<pre>
<%
            out.println("Request parameters:");
            Enumeration enum = request.getParameterNames();
            while (enum.hasMoreElements())            {
                String name = (String)enum.nextElement();
                out.println(name + " : " + request.getParameter(name));
            }
            out.println("Session attributes:");
            enum = session.getAttributeNames();
            while (enum.hasMoreElements())            {
                String name = (String)enum.nextElement();
                out.println(name + " : " + session.getAttribute(name));
            }
%>
</pre>
</body>
</html>