<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String param = request.getParameter("orderby")!=null?request.getParameter("orderby"):"";
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

  ResultSet rs = myFormBean.queryResults(param, "search_eform");
%>

<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Upload</title>
<link rel="stylesheet" href="../web.css">
</head>
<script language="javascript">
<!--
  function checkHtml(){
    if(document.myForm.FileName.value==""){ 
      alert("Please input a file first, then click 'Upload' button.");
    } else {
      document.myForm.submit();
    } 
  }

  function BackHtml(){
    top.location.href = "../admin/admin.jsp";
  }
  function newWindow(file,window) {
    msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
    if (msgWindow.opener == null) msgWindow.opener = self;
  }
  /*
  function saveIt(saveString) {
    msgWindow=window.open("","displayWindow","menubar=no,scrollbars=no,status=no,width=1,height=1")
    msgWindow.document.write(saveString)
    msgWindow.document.execCommand("SaveAs");
    msgWindow.close();
  }
  function getDelimitor(fid) {
     msgWindow = window.open('','_blank','resizable=no,scrollbars=yes,width=240,height=200,top=300,left=300');
     msgWindow.document.write("<BODY>");
     msgWindow.document.write("<table><tr><td><form action='Export.jsp'>");
     msgWindow.document.write("<input type='hidden' name='fid' value='"+fid+"'></td>");
     msgWindow.document.write("<b>input delimiter:</b> &nbsp;(default: \"|\")<br><br><input type='text' name='delemitor'></td>");
     msgWindow.document.write("<td><br><br><input type='submit' value='submit' ></td></tr></table></form>");
     msgWindow.document.write("<tr><td></td></tr></BODY>");
     msgWindow.document.close();
  } 
  */
//-->
</script>
<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica">UPLOAD E-FORM</font></th></tr>
</table>
<table border="0" cellspacing="2" cellpadding="2" width="100%" >
  <tr><td align='right'><a href=# onclick="javascript:BackHtml()">Back to Admin Page</a></td></tr>
</table>

<table cellspacing="2" cellpadding="2" width="80%" border="0" BGCOLOR="<%=weakColor%>">
<FORM NAME="myForm" ENCTYPE="multipart/form-data" ACTION="../servlet/oscar.eform.UploadServlet" METHOD="post" onSubmit="return checkHtml()">
  <tr><td align='right'><b>Form name </b></td><td><input type="text" size="50" name="form_name"></td></tr>
  <tr><td align='right'><b>Subject </b></td><td><input type="text" size="50" name="subject"></td></tr>
  <tr><td align='right'><b>File name </b></td><td><input type="file" name="FileName" size="80"></td></tr>
  <tr><td></td><td><input type="button" value="Upload" onclick="javascript:checkHtml()"></td></tr>
 </form>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="98%">
  <tr><td>Form Library </td>
  <td align='right'><a href="calldeletedform.jsp">List Deleted Forms </a></td></tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="98%" >
  <tr>
    <td>
    <table border="0" cellspacing="2" cellpadding="2" width="100%">
      <tr bgcolor=<%=deepColor%> >
      <th><a href="uploadhtml.jsp?orderby=form_name">Form Name</a></th>
      <th><a href="uploadhtml.jsp?orderby=subject">Subject</a></th>
      <th><a href="uploadhtml.jsp?orderby=file_name">File</a></th>
      <th><a href="uploadhtml.jsp?">Form Date</a></th>
      <th><a href="uploadhtml.jsp?">Form Time</a></th> 
      <th>Action</th> 
      </tr> 
<%
  String bgcolor = null;
  while (rs.next()){
    bgcolor = rs.getRow()%2==0?weakColor:"white" ;
%>
      <tr bgcolor="<%=bgcolor%>">
	    <td width=25%>
        <a href="JavaScript:newWindow('showhtml.jsp?fid=<%=rs.getInt("fid")%>')"><%=rs.getString("form_name")%></a></td>
		<td width=30% ><%=rs.getString("subject")%></td>
		<td width=25% ><%=rs.getString("file_name")%></td>
		<td nowrap align='center'><%=rs.getString("form_date")%></td>
		<td nowrap align='center'><%=rs.getString("form_time")%></td>
		<td nowrap align='center'><a href="deleteform.jsp?fid=<%=rs.getInt("fid")%>">Delete</td>
	  </tr>
<%
  }  
  myFormBean.closePstmtConn();
%>               
</center>

</body>
</html>

  