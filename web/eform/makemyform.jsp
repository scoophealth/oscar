<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int fid =new Integer(request.getParameter("fid")).intValue(); 
  int demographic_no = new Integer(request.getParameter("demographic_no")).intValue();
  String subject =  request.getParameter("subject") ; 
  String form_name = request.getParameter("form_name");
%>  
<%@ page import = "java.sql.ResultSet, oscar.eform.*"  errorPage="../errorpage.jsp"%>
<jsp:useBean id="beanMakeForm" class="oscar.eform.EfmMakeForm" scope="session"/> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_formhtml", "select form_html from eform where fid = ?" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
%>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Make My Form</title>
</head>
<body>
<%
  ResultSet rs = myFormBean.queryResults(fid, "search_formhtml");
  if (rs.next()){ 
    String theStr = rs.getString("form_html");
    EfmPrepData prepData = new EfmPrepData(demographic_no, fid, form_name) ;

	out.clear();
    out.print(beanMakeForm.addContent(theStr, prepData.getMeta(), prepData.getValue(), prepData.getTagSymbol() ) );
  }else{
%> 
NO such file in database.
<%
  }
  myFormBean.closePstmtConn();
%>               
 
</body>
</html>
