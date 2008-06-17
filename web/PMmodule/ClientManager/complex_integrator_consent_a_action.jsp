<%@page import="java.util.Enumeration"%>
<%
	Enumeration e=request.getParameterNames();
	while (e.hasMoreElements())
	{
		String key=(String)e.nextElement();
		%>
			<%=key%>=<%=request.getParameter(key)%><br />
		<%
	}
%>