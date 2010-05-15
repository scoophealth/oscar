<%--  
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<%
if(session.getAttribute("user") == null)    response.sendRedirect("../../../../logout.htm");
String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
//  mygroupno = (String) session.getAttribute("groupno");  
userfirstname = (String) session.getAttribute("userfirstname");
userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"
	errorPage="../../errorpage.jsp"%>
<%@ include file="../../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbINR.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();      
    }   
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A BILLING RECORD</font></th>
	</tr>
</table>
<%
//System.out.println(subject+content); 
String[] param =new String[15]; 
param[0]=request.getParameter("demoid");
param[1]=request.getParameter("demo_name");
param[2]=request.getParameter("demo_hin");
param[3]=request.getParameter("demo_dob");
param[4]=request.getParameter("provider_no");
param[5]=request.getParameter("provider_ohip_no");
param[6]=request.getParameter("provider_rma_no");
param[7]=request.getParameter("doccreator");
param[8]=request.getParameter("diag_code");
param[9]=request.getParameter("service_code");
param[10]=request.getParameter("service_desc");
param[11]=request.getParameter("service_amount");
param[12]=request.getParameter("service_unit");
param[13]=request.getParameter("docdate");
param[14]="N"; 

int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_inrbilling");

//	  int[] demo_no = new int[1]; demo_no[0]=Integer.parseInt(request.getParameter("demographic_no")); int rowsAffected = apptMainBean.queryExecuteUpdate(demo_no,param,request.getParameter("dboperation"));

if (rowsAffected ==1) {
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script> <%
}  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
}
apptMainBean.closePstmtConn();
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>