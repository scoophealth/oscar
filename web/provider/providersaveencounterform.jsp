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

<%
  
%>    
<%@ page  import="java.sql.*, java.util.*, oscar.MyDateFormat"  errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html>
<head><title></title>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body  onload="start()">
<%
  if(request.getParameter("submit").equals("Delete")) {
    String[] param =new String[1];
    param[0]=request.getParameter("encounterform_name");
    int rowsAffected = apptMainBean.queryExecuteUpdate(param,"delete_encounterform");
    out.println("<script language='JavaScript'>self.close();</script>");
    apptMainBean.closePstmtConn();
  } else {
%>
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="90%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF">
            ADD A FORM RECORD</font></th>
      </tr>
    </table>
<%
  String[] param =new String[2];
  param[0]=request.getParameter("formname");
	param[1]="form"+request.getParameter("formfilename")+".jsp?demographic_no=";

  int rowsAffected = apptMainBean.queryExecuteUpdate(param,request.getParameter("dboperation"));
  if (rowsAffected ==1) {
%>
  <p><h1>Successful Addition of a encounter-form Record.</h1></p>
<script LANGUAGE="JavaScript">
      self.close();
</script>
<%
  }  else {
%>
  <p><h1>Sorry, addition has failed.</h1></p>
<%  
  }
  apptMainBean.closePstmtConn();
%>
  <p></p>
  <hr width="90%"></hr>
<form>
<input type="button" value="Close this window" onClick="window.close()">
</form>
</center>
<%
}
%>
</body>
</html>