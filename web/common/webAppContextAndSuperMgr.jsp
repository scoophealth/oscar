<%-- This code makes new business logic facade OscarSuperManager bean accessible from jsp layer --%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="oscar.service.OscarSuperManager"%>
<%
	WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(
			request.getSession().getServletContext());
	OscarSuperManager oscarSuperManager = (OscarSuperManager)webApplicationContext.getBean("oscarSuperManager");
%>