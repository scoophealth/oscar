<%@page import="java.util.*" %>
<%@page import="org.oscarehr.util.*" %>
<%
	Set<Map.Entry<Thread,StackTraceElement[]>> entries=DbConnectionFilter.debugMap.entrySet();
	for (Map.Entry<Thread,StackTraceElement[]> entry : entries)
	{
		Thread thread=entry.getKey();
		StackTraceElement[] stackTrace=entry.getValue();
		%>
			<hr />
			Thread : <%=thread.getName() %> <br />
			StackTrace : <br /> 
			<%
				for (StackTraceElement ste : stackTrace)
				{
					%>
						<%=ste.toString() %><br />
					<%
				}
			%>
		<%
	}
%>