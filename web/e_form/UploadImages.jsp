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

<%@ page import = "java.io.*" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<%  
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
%> 
<html>
<head>
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
//-->
</script>
<body   topmargin="0" leftmargin="0" rightmargin="0">
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF">
            IMAGES ADMINISTRATOR</font></th>
      </tr>
    </table>

<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="#C4D9E7">

 <FORM NAME="aForm" ENCTYPE="multipart/form-data" ACTION="../servlet/UploadImage" METHOD="POST" >

      <tr> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp; </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp; </font></td>
     </tr>
     <tr valign="top">
      <td rowspan="2" align="middle" valign="middle" colspan="3"> <font face="Verdana" color="#0000FF"><b><i> 
          </font> <br>
        <font face="Verdana" color="#0000FF"><b><i> File name</i></b>
           <input type="file" name="FileName" size="80"></td>
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
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF">
            <input type="button" value="Return to Admin" onclick="javascript:BackHtml()">  
            <input type="submit" value="Upload" >
        </font></td>

    </tr>
 </form>
</table>
  <table border="0" cellspacing="0" cellpadding="0" width="70%">
    <tr>
      <td>  </td>
      <td> 
           
           </a>
      </td> 
     <tr>
	   <tr>
                 <td>  The images you already have:
                 </td>
	   </tr> 


</table>
  
  <table border="0" cellspacing="0" cellpadding="0" width="70%">

    <tr>
      <td>  
         </td>
      <td>
         <table border="1" cellspacing="0" cellpadding="0" width="100%">
 
<%
 	String wxz="../webapps/"+oscarVariables.getProperty("project_home")+"/e_form/images/"; 
  	
	File dir=new File(wxz); 
   
	String temp[]=dir.list(); 
	for(int i=0;i<temp.length;i++){ 
		File ft=new File(temp[i]); 
		String wxz1=wxz+ft; 
		File ppp=new File(wxz1); 
                out.println("<tr>"); 
                out.println("<td>"); 
                out.println(ft); 
                out.println("</td>");
                out.print("<td width=100 align=right><a href=\"JavaScript:newWindow('../e_form/images/"+ft+"','_blank')\">");        
                out.println("View"); 
                out.println("</td>"); 
                out.println("<td width=100 align=right><input type=\"button\" value=\"Delete\" onclick=\"DoEmpty('"+ft+"')\">");
                out.println("</td>"); 
                out.println("</tr>"); 
	} 
%>    
         </table>
      </td>
    </tr>
  </table>

</center>

</body>
<script language="javascript"><!--

function DoEmpty(str){
if (confirm("Are you sure you want to permanently delete all messages in this folder?"))
window.location = "DeleteImage.jsp?filename="+str;
} 
//-->
</script> 


</html>

  