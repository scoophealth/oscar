<%  
  if (session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
      response.sendRedirect("../logout.jsp");
%> 
<%@ page import = "java.io.*, oscar.eform.*"  errorPage="../errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
  EfmImagePath.setEfmImagePath(oscarVariables.getProperty("eform_image"));
  out.clear();
  pageContext.forward("/servlet/oscar.eform.UploadImage"); 
%>