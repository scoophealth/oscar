<%  
  if (session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
      response.sendRedirect("../logout.jsp");
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%> 
<%@ page import = "java.io.*, oscar.eform.*"  errorPage="../errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<html>
<head>
<link rel="stylesheet" href="../web.css">

</head>
<script language="javascript">
<!--
  function checkHtml(){
    if (document.myForm.FileName.value==""){ 
      alert("Please choose a file first, then click Upload");
    } else {
      document.myForm.submit();
    } 
  }
  function BackHtml(){
    top.location.href = "../admin/admin.jsp";
  }
  function newWindow(file,window) {
    msgWindow=open(escape(file),window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
    if (msgWindow.opener == null) msgWindow.opener = self;
  }
//-->
</script>

<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica">UPLOAD IMAGE</font></th></tr>
</table>
<table border="0" cellspacing="2" cellpadding="2" width="100%" >
  <tr><td align='right'><a href=# onclick="javascript:BackHtml()">Back to Admin Page</a></td></tr>
</table>

<table cellspacing="2" cellpadding="2" width="80%" border="0" BGCOLOR="<%=weakColor%>">
<FORM NAME="myForm" ENCTYPE="multipart/form-data" ACTION="uploadimagesproc.jsp" METHOD="post" onSubmit="return checkHtml()">
  <tr><td align='right'><b>File name </b></td><td><input type="file" name="FileName" size="80"></td></tr>
  <tr><td></td><td><input type="button" value="Upload" onclick="javascript:checkHtml()"></td></tr>
</form>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="80%">
  <tr><td>Image Library </td>
  <td align='right'></td></tr>
</table>

<table border="0" cellspacing="2" cellpadding="2" width="80%">
  <tr bgcolor=<%=deepColor%> >
    <th>Image Name</th>
    <th>Action</th> 
  </tr> 
<%
  boolean bOdd = false;
  String imgDir = "../../OscarDocument/" + oscarVariables.getProperty("project_home") + "/eform/images/";

//  EfmImagePath.setEfmImagePath(oscarVariables.getProperty("eform_image"));
  File dir=new File(imgDir); 
  
  String temp[]=dir.list(); 
  //System.out.println(imgDir);
  if(temp !=null) {
    for(int i=0;i<temp.length;i++){ 
      bOdd = bOdd?false:true ;
	  String imgDir1=imgDir+temp[i]; 
//    File ft=new File(temp[i]); 
	//File ppp=new File(imgDir1); 
%>    
  <tr bgcolor="<%=bOdd?weakColor:"white"%>">
    <td><a href="JavaScript:newWindow('<%=imgDir1%>','_blank')"><%=temp[i]%></a></td>
    <td width=100 align='center'><input type="button" value="Delete" onclick="DoEmpty('<%=temp[i]%>')"></td>
  </tr>
<%
    } 
  }
%>    

</table>
</center>

</body>

<script language="javascript">
<!--
  function DoEmpty(str){
    if (confirm("Are you sure you want to permanently delete all messages in this folder?"))
    window.location = "deleteimage.jsp?filename="+str;
  } 
//-->
</script> 

</html>