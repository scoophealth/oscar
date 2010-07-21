<%@ page
	import="com.crystaldecisions.report.web.viewer.CrystalImageHandler"%><%! com.crystaldecisions.report.web.viewer.CrystalImageHandler imageHandler = null; %>
<%
   imageHandler = new CrystalImageHandler();
   imageHandler.handleImage(request, response, getServletConfig().getServletContext());
%>