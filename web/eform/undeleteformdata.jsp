<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int demographic_no =new Integer(request.getParameter("demographic_no")).intValue(); 
  int fdid =new Integer(request.getParameter("fdid")).intValue(); 
%>  

<%@ page import = "java.sql.ResultSet, java.net.*" errorPage="../errorpage.jsp"%> 
<jsp:useBean id="dataBean" scope="session" class="oscar.eform.EfmDataOpt" />
 
<%  
  dataBean.update_eform_data_status(fdid, 1);
  response.sendRedirect("calldeletedformdata.jsp?demographic_no="+demographic_no);
%>
