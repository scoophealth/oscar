<%  
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
  int fid =new Integer(request.getParameter("fid")).intValue(); 
%>
<%@ page import = "java.sql.*"   errorPage="../errorpage.jsp"%> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_eform", "select form_html from eform where fid = ?" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);

  ResultSet rs = myFormBean.queryResults(fid, "search_eform");

  if (rs.next()){ 
    String theStr = rs.getString("form_html");
	out.clear();
    out.print(rs.getString("form_html")); 
	return;
  }else{
%> 
NO such file in database.
<%
  }
  myFormBean.closePstmtConn();
%>