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
<jsp:useBean id="beanUtility" scope="session" class="bean.Utility" />
 
<%  
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");

  int demographic_no =new Integer(request.getParameter("demographic_no")).intValue(); 

  int fdid =new Integer(request.getParameter("fdid")).intValue(); 

  String query = " UPDATE eforms_data set status = 1 where fdid= "+fdid ;
    try {
           beanDBConnect.executeUpdate(query);
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }

    response.sendRedirect("ShowMyForm.jsp?demographic_no="+demographic_no);

%>               
 
</body>
</html>

  