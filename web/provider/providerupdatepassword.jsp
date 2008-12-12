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
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
%>


<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*,java.security.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="pwdMainBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%
  String curUser_no = (String) session.getAttribute("user");
  MessageDigest md = MessageDigest.getInstance("SHA");
%>
<%@ include file="../admin/dbconnection.jsp"%>
<%
  //operation available to the client -- dboperation
  String [][] dbQueries=new String[][] {
    {"searchpassword", "select password from security where provider_no = ?" },
	  {"updatepassword", "update security set password = ? where  provider_no= ?" },
  };
   
   //associate each operation with an output JSP file -- displaymode
   String[][] responseTargets=new String[][] {   };
   pwdMainBean.doConfigure(dbParams,dbQueries,responseTargets);
   ResultSet rsgroup = pwdMainBean.queryResults(curUser_no, "searchpassword");

   while (rsgroup.next()) { 
     StringBuffer sbTemp = new StringBuffer();
     byte[] btOldPasswd= md.digest(request.getParameter("oldpassword").getBytes());
     for(int i=0; i<btOldPasswd.length; i++) sbTemp = sbTemp.append(btOldPasswd[i]);
   
     String stroldpasswd = sbTemp.toString();
//     System.out.println(stroldpasswd+ " oldpassword ");

     String strDBpasswd = rsgroup.getString("password");
     if(rsgroup.getString("password").length()<20) {
       sbTemp = new StringBuffer();
       byte[] btDBPasswd= md.digest(rsgroup.getString("password").getBytes());
       for(int i=0; i<btDBPasswd.length; i++) sbTemp = sbTemp.append(btDBPasswd[i]);
       strDBpasswd = sbTemp.toString();
     }
//     System.out.println(rsgroup.getString("password")+"  db ");
     
     if( stroldpasswd.equals(strDBpasswd ) && request.getParameter("mypassword").equals(request.getParameter("confirmpassword")) ) {
       sbTemp = new StringBuffer();
       byte[] btNewPasswd= md.digest(request.getParameter("mypassword").getBytes());
       for(int i=0; i<btNewPasswd.length; i++) sbTemp = sbTemp.append(btNewPasswd[i]);

       String[] param =new String[2];
	     param[0]=sbTemp.toString();
	     param[1]=curUser_no;
       int rowsAffected = pwdMainBean.queryExecuteUpdate(param, "updatepassword");
       if(rowsAffected==1) out.println("<script language='javascript'>self.close();</script>");
     } else {
       response.sendRedirect("providerchangepassword.jsp");
     }
   }
%>