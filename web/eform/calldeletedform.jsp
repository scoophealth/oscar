<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no =new Integer(request.getParameter("demographic_no")).intValue(); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.net.*,java.sql.*"   errorPage="../errorpage.jsp"%> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_deleted", "select * from eform where status = 0 order by ?, form_date desc, form_time desc" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
%>
<%  
  String param = request.getParameter("orderby")!=null?request.getParameter("orderby"):"";
  ResultSet rs = myFormBean.queryResults(new String[] {param}, "search_deleted");
%>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>CallDeletedForm</title>
<link rel="stylesheet" href="../web.css">
</head>
<script language="javascript">
<!--
function newWindow(file,window) {
    msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
    if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>
<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica">DELETED FORM</font></th></tr>
</table>

<table border="0" cellspacing="0" cellpadding="2" width="98%">
  <tr bgcolor=<%=weakColor%>>
    <td>Deleted Forms: </td>
    <td align='right'><a href="uploadhtml.jsp">Go to Current Form Library</a> | 
      <a href="../admin/admin.jsp"> Back to Admin Page</a>
    </td> 
  </tr>
</table>
  
<table border="0" cellspacing="2" cellpadding="2" width="98%">
  <tr bgcolor=<%=deepColor%> >
  <th width=20%><a href="calldeletedform.jsp?orderby=form_name">Form Name</a></th>
  <th width=40%><a href="calldeletedform.jsp?orderby=subject">Subject</a></th>
  <th><a href="calldeletedform.jsp">Form Date</a></th>
  <th><a href="calldeletedform.jsp">Form Time</a></th> 
  <th>Action</th>
  </tr> 
<%
  String bgcolor = null;
  if (rs.next()){ 
    rs.beforeFirst();
    while (rs.next()){
      bgcolor = rs.getRow()%2==0?"white":weakColor ;
%>
  <tr bgcolor="<%=bgcolor%>">
  <td><a href="JavaScript:newWindow('showhtml.jsp?fid=<%=rs.getInt("fid")%>','window2')"><%=rs.getString("form_name")%></a></td>
  <td><%=rs.getString("subject")%></td>
  <td nowrap align='center'><%=rs.getString("form_date")%></td>
  <td nowrap align='center'><%=rs.getString("form_time")%></td>
  <td nowrap align='center'><a href="undeleteform.jsp?fid=<%=rs.getInt("fid")%>">UnDelete</a></td>
  </tr>
<%
    }  
  }else {
    out.print("<tr><td>No Data!!!</td></tr>");
  }
  myFormBean.closePstmtConn();
%>               
</table>
</center>

</body>
</html>
 