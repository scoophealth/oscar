 <jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" /> 
 <%   
  if(session.getValue("user") == null)
    response.sendRedirect("../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
           String docdownload = oscarVariables.getProperty("project_home") ;;
           session.setAttribute("homepath", docdownload);      

%>

<html>
<head>
<title>EDT OBEC Response Report Generator</title>
<meta http-equiv="Content-Type" content="text/html">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<p><font face="Arial, Helvetica, sans-serif" size="2"><b>EDT OBEC Response Report Generator</b></font></p>
<form method="post" action="../../../oscarBilling/DocumentErrorReportUpload.do" ENCTYPE="multipart/form-data">
  <font face="Arial, Helvetica, sans-serif" size="2"> </font>
<table width="400" border="0">
    <tr> 
      <td width="181"><b><font face="Arial, Helvetica, sans-serif" size="2">Select 
        diskette </font></b></td>
      <td width="209"><font face="Arial, Helvetica, sans-serif" size="2"> 
        <input type="file" name="file1" value=""></font></td>
    </tr>
        <tr> 
      <td width="181"> 
            <input type="submit" name="Submit" value="Create Report">
      </td>
      <td width="209">&nbsp;</td>
    </tr>
  </table>

  <p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
  <p>&nbsp; </p>
</form>
</body>
</html>
