<%  
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%> 

<%@ page import = "java.io.*"  errorPage="../errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
  String filename = request.getParameter("filename");
  String path = oscarVariables.getProperty("eform_image");

  File fl = new File(path+filename);
  fl.delete();
%>

<jsp:forward page="uploadimages.jsp"/>