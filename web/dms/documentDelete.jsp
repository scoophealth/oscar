<%
  if(session.getValue("user") == null) 
     response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  
  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  
  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
%> 
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %> 
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>

<html:html locale="true">
<head>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    self.opener.refresh();
      close();
    }   
    //-->
</script>
</head>

<body   background="../images/gray_bg.jpg" bgproperties="fixed">
   <center>
      <table border="0" cellspacing="0" cellpadding="0" width="90%" >
         <tr bgcolor="#486ebd"> 
            <th align="CENTER">
               <font face="Helvetica" color="#FFFFFF">
                  <bean:message key="dms.documentDelete.msgDeleteDoc"/>
               </font>
            </th>      
         </tr>    
      </table>
      <%        
      String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

      int rowsAffected = 0;
      String[] param =new String[2];
               param[0]=nowDate;
               param[1]=request.getParameter("document_no");
	 
      rowsAffected = apptMainBean.queryExecuteUpdate(param, "delete_document");

      if (rowsAffected ==1) { %>
      <p><h1><bean:message key="dms.documentDelete.msgSuccess"/></h1></p>
      <script LANGUAGE="JavaScript">         
         self.close();
         self.opener.refresh();      
      </script>
    <%} else { %>
      <p><h1><bean:message key="dms.documentDelete.msgFailed"/></h1></p>
    <%}
      apptMainBean.closePstmtConn(); %>
      <p></p>
      <hr width="90%"></hr>    
      <form>
         <input type="button" value="<bean:message key="global.btnClose"/>" onClick="closeit()">
      </form>
    </center>
</body>
</html:html>