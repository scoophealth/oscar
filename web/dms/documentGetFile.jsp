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

   <%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
  <%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>
 
   <%   
   String filename="", filetype="", doc_no="";
                         String docdownload = oscarVariables.getProperty("DOCUMENT_DIR") ;;
       session.setAttribute("docdownload", docdownload);      

   
   if (request.getParameter("document") != null) {
   
   filename = request.getParameter("document");
   filetype = request.getParameter("type");
  doc_no = request.getParameter("doc_no");
// response.sendRedirect("../document/"+filename);

if (filetype.compareTo("active") == 0){
%>
<html>
<head>
<title>OSCAR - Document Viewer</title>
<meta http-equiv="Content-Type" content="text/html;">
</head>
<frameset rows="21,*" frameborder="NO" border="0" framespacing="0" cols="*"> 
  <frame name="topFrame" scrolling="NO" noresize src="docViewerHead.jsp" >
  <frame name="mainFrame" src="../../OscarDocument/<%=oscarVariables.getProperty("project_home")%>/document/<%=filename%>">
</frameset>
<noframes>
<body bgcolor="#FFFFFF" text="#000000">
</body>
</noframes> 
</html>
   <%}
   else {
   ResultSet rslocal2 = null;
 	rslocal2 = apptMainBean.queryResults(doc_no, "search_document_content");
 	  while(rslocal2.next()){
 	  

   %>
<%= rslocal2.getString("docxml")%>
  
 <%
 }
   }
   }else{
       String errormsg = "Error: File not found.";
   %>
   <jsp:forward page='../dms/errorpage.jsp' >
   <jsp:param name="msg" value='<%=errormsg%>' />
</jsp:forward>

      
      
      %>
      <%
      }
      %>
