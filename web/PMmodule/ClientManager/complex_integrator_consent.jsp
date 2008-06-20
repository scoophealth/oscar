<%@page import="java.util.Random"%>
<%!
	private Random random=new Random();
%>
<%
	if (random.nextInt()%2==0)
	{
		response.sendRedirect("complex_integrator_consent_a.jsp?demographicId="+request.getParameter("demographicId"));
	}
	else
	{
		response.sendRedirect("complex_integrator_consent_b.jsp?demographicId="+request.getParameter("demographicId"));
	}
%>
