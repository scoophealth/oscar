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

<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />
<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>EditProblimList</title>
</head>
<body>


<%   

String keyword,button,content,xml_Problem_List,xml_Medication,xml_Alert,xml_Family_Social_History ="";

int demographic_no= new Integer( request.getParameter("demographic_no").trim() ).intValue();

keyword = request.getParameter("keyword").trim();
button = request.getParameter("button").trim();
xml_Problem_List = request.getParameter("xml_Problem_List").trim();
xml_Medication = request.getParameter("xml_Medication").trim();
xml_Alert = request.getParameter("xml_Alert").trim();
xml_Family_Social_History = request.getParameter("xml_Family_Social_History").trim();

content = "<xml_Problem_List>"+xml_Problem_List+"</xml_Problem_List>"
         +"<xml_Medication>"+xml_Medication+"</xml_Medication>"
         +"<xml_Alert>"+xml_Alert+"</xml_Alert>"
         +"<xml_Family_Social_History>"+xml_Family_Social_History+"</xml_Family_Social_History>";
         
   String queryString = "update demographicaccessory set content='"+content
                           +"' where demographic_no= "+demographic_no ;

//if action is good, then give me the result
 
    if (beanDBConnect.executeUpdate(queryString)) {
       response.sendRedirect("ProblimList.jsp?demographic_no="+demographic_no+"&keyword="+keyword+"&button="+button);


//if action is failed,
    } else {
%>
<br>
<br>
Sorry, update
<br>
has failed.
<%  
    }

%>

</body>
</html>