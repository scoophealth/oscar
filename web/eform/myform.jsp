<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String demographic_no = request.getParameter("demographic_no"); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.net.*,java.sql.*"   errorPage="../errorpage.jsp"%> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_eform", "select * from eform where status = 1 order by ?, form_date desc, form_time desc" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);

  String param = request.getParameter("orderby")!=null?request.getParameter("orderby"):"";
  ResultSet rs = myFormBean.queryResults(param, "search_eform");
%>

<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Form Library</title>
<link rel="stylesheet" href="../web.css">
<script language="javascript">
<!--
//if (document.all || document.layers)
//  window.resizeTo(790,580)
function checkHtml(){
  if(document.myForm.FileName.value==""){ 
    alert("Please choose a file first, then click Upload");
  } else {
    document.myForm.submit();
  } 
}
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
}
function returnMain(demographic_no) {
  top.location.href = "../demographic/demographiceditdemographic.jsp?demographic_no="+demographic_no;
}
//-->
</script>
</head>

<body topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica">E-FORM</font></th></tr>
</table>

<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="<%=weakColor%>">
  <tr><td align='right'><a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail">Back &nbsp;</a></td>
  </tr>
</table> 
   
<center>
<table border="0" cellspacing="0" cellpadding="0" width="98%">
  <tr><td>Form Library </td>
  <td align='right'></td></tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="98%" >
  <tr>
    <td>
    <table border="0" cellspacing="2" cellpadding="2" width="100%">
      <tr bgcolor=<%=deepColor%> >
      <th><a href="myform.jsp?demographic_no=<%=demographic_no%>&orderby=form_name">Form Name</a></th>
      <th><a href="myform.jsp?demographic_no=<%=demographic_no%>&orderby=subject">Subject</a></th>
      <th><a href="myform.jsp?demographic_no=<%=demographic_no%>&orderby=file_name">File</a></th>
      <th><a href="myform.jsp?demographic_no=<%=demographic_no%>">Form Date</a></th>
      <th><a href="myform.jsp?demographic_no=<%=demographic_no%>">Form Time</a></th> 
      </tr> 
<%
  String bgcolor = null;
  while (rs.next()){
    bgcolor = rs.getRow()%2==0?weakColor:"white" ;
%>
      <tr bgcolor="<%=bgcolor%>">
	    <td width=25%>
        <a href="makemyform.jsp?fid=<%=rs.getInt("fid")%>&form_name=<%=rs.getString("form_name")%>&demographic_no=<%=demographic_no%>&subject=<%=rs.getString("subject")%>">
	    <%=rs.getString("form_name")%>
        </a></td>
		<td width=30% ><%=rs.getString("subject")%></td>
		<td width=25% ><%=rs.getString("file_name")%></td>
		<td nowrap align='center'><%=rs.getString("form_date")%></td>
		<td nowrap align='center'><%=rs.getString("form_time")%></td>
	  </tr>
<%
  }  
  myFormBean.closePstmtConn();
%>               
 
</table>
</center>

</body>
</html>

  