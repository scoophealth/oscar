<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int fid =new Integer(request.getParameter("fid")).intValue(); 
%>  

<%@ page import = "java.sql.ResultSet, java.net.*" errorPage="../errorpage.jsp"%> 
<jsp:useBean id="dataBean" scope="session" class="oscar.eform.EfmDataOpt" />
 
<%  
  dataBean.update_eform_status(fid, 1);
  response.sendRedirect("calldeletedform.jsp");
%>
