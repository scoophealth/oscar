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

  int demographic_no =new Integer(request.getParameter("demographic_no")).intValue(); 

  String query = " select * from eforms_data where status = 1 and demographic_no="+demographic_no ;
  ResultSet RS =  beanDBConnect.executeQuery(query);

%>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>CallDeletedFormData</title>
<link rel="stylesheet" href="web.css">
</head>
<script language="javascript">
<!--
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
            My FORMS </font></th>
      </tr>
    </table>
<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="#C4D9E7">
     <tr> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp; </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF">&nbsp; </font></td>
     </tr>
     <tr valign="top">
      <td align="center" valign="middle" colspan="3"> <font face="Verdana" color="#0000FF"><b><i> 
          Select a form to show  </i></b></font></td>
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
 
</table> 
  <table border="0" cellspacing="0" cellpadding="0" width="90%">
    <tr>
      <td> The forms you have deleted: </td>
      <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <a href="ShowMyForm.jsp?demographic_no=<%=demographic_no%>"> 
         Go to Current Form Library</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

      <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail"> Return</b></a>
      </td> 
     <tr>
</table>
  
  <table border="0" cellspacing="0" cellpadding="0" width="90%">
    <tr>
      <td> 
         </td>
      <td>
         <table border="1" cellspacing="0" cellpadding="0" width="100%">
<%

if (RS.next()){ 
    RS.beforeFirst();
 
 while (RS.next()){
 
        out.print("<tr><td width=130><a href=\"JavaScript:");
        out.print("newWindow('ShowMyFormData.jsp?fdid="+RS.getInt("fdid")+"','window2')\">");        
        out.print(RS.getString("form_name"));
        out.print("</a></td>");

        out.print("<td width=200><a href=\"JavaScript:");
        out.print("newWindow('ShowMyFormData.jsp?fdid="+RS.getInt("fdid")+"','_blank')\">");        
        out.print(RS.getString("subject"));
        out.print("</a></td>"); 

        out.print("<td width=100><a href=\"JavaScript:");
        out.print("newWindow('ShowMyFormData.jsp?fdid="+RS.getInt("fdid")+"','window2')\">");        
        out.print(RS.getString("form_date"));
/*
        out.print("</a></td><td width=100>");
        out.print(RS.getString("demographic_no"));
        out.print("</td><td width=100><a href=UnDeleteFormData.jsp?fdid="+RS.getInt("fdid")+"&demographic_no="+demographic_no+">");
*/
        out.print("</a></td><td width=100>");
        out.print(RS.getString("form_time"));
        out.print("</td><td width=100><a href=UnDeleteFormData.jsp?fdid="+RS.getInt("fdid")+"&demographic_no="+demographic_no+">");
        out.print("UnDelete");
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

  