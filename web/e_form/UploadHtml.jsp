<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import = "java.sql.ResultSet" %> 
<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
 
<%  
if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
String query = " select form_name,fid,form_name,subject,file_name,form_date,form_time from eforms where status = 0" ;
ResultSet RS =  beanDBConnect.executeQuery(query);
%>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Search a Provider</title>
<link rel="stylesheet" href="web.css">
</head>
<script language="javascript"><!--
 function checkHtml(){
 if(document.myForm.FileName.value==""){ 
   alert("Please choose a file first, then click Upload");
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
//-->
</script>
<body   topmargin="0" leftmargin="0" rightmargin="0">
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF">
            FORMS ADMINISTRATOR</font></th>
      </tr>
    </table>

<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="#C4D9E7">

<FORM NAME="myForm" ENCTYPE="multipart/form-data" ACTION="../servlet/bean.UploadServlet" METHOD="post" onSubmit="return checkHtml()">
      <tr> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp; </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp; </font></td>
     </tr>
     <tr valign="top">
      <td rowspan="2" align="middle" valign="middle" colspan="3"> <font face="Verdana" color="#0000FF"><b><i> 
          Form name <input type="text" size="35" name="form_name">&nbsp;&nbsp;&nbsp;
          Subject <input type="text" size="35" name="subject">
            </i></b></font> <br>
        <font face="Verdana" color="#0000FF"><b><i> File name</i></b><input type="file" name="FileName" size="80"></td>
           <td nowrap> 
           </td>
           <td nowrap>
           </td>
      <td valign="middle" rowspan="2" ALIGN="left">
    </tr>
    <tr> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF"></font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF"></font></td>

    </tr>
    <tr> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF"><input type="button" value="Return to Admin" onclick="javascript:BackHtml()"><input type="button" value="Upload" onclick="javascript:checkHtml()"></font></td>

    </tr>
 </form>
</table>
  <table border="0" cellspacing="0" cellpadding="0" width="70%">
    <tr>
      <td> Current Form Library: </td>
      <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <a href="CallDeletedForm.jsp"> 
         List Previously Deleted Forms </a>
      </td> 
     <tr>
</table>
  
  <table border="0" cellspacing="0" cellpadding="0" width="95%">
    <tr>
      <td> 
         </td>
      <td>
         <table border="1" cellspacing="0" cellpadding="0" width="100%">
<%

if(RS.next()){
  RS.beforeFirst();
  while (RS.next()){
        out.print("<tr><td width=120><a href=\"JavaScript:");
        out.print("newWindow('ShowHtml.jsp?fid="+RS.getInt("fid")+"','_blank')\">");        
        out.print(RS.getString("form_name")+"</td><td width=180><a href=\"JavaScript:");
        out.print("newWindow('ShowHtml.jsp?fid="+RS.getInt("fid")+"','_blank')\">");        
        out.print(RS.getString("subject")+"</td><td width=190><a href=\"JavaScript:");
        out.print("</a></td><td>");
        out.print("newWindow('ShowHtml.jsp?fid="+RS.getInt("fid")+"','_blank')\">");        
        out.print(RS.getString("file_name"));
        out.print("</a></td><td>");
        out.print(RS.getString("form_date")+"</td>");
        out.print("<td>");
        out.print(RS.getString("form_time")+"</td>");
        out.print("<td width=30><a href=\"JavaScript:getDelimitor('"+RS.getInt("fid")+"')\">");
        out.print("Exp</td></a>");
        out.print("<td width=50><a href=DeleteForm.jsp?fid="+RS.getInt("fid")+">");
        out.print("Delete");
        out.print("</a></td></tr>");
  }  
  RS.close();
}else {
  out.print("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; no data in it</td></tr>");
}
%>               

         </table>
      </td>
    </tr>
  </table>

</center>

</body>
</html>

  