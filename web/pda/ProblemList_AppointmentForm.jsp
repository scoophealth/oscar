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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page errorPage="ErrorPage.jsp"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />
<jsp:useBean id="beanCoercionTest" scope="session"
	class="bean.CoercionTest" />
<jsp:useBean id="beanUtility" scope="session" class="bean.Utility" />

<% 
//  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");


        int demographic_no = new Integer(request.getParameter("demographic_no").trim()).intValue();
        String keyword = request.getParameter("keyword").trim();
        String button = request.getParameter("button").trim();

//*****************************************************************  

    ResultSet RS = null;
    String tempString = "";
    String xml_Problem_List = "";
    String xml_Medication = "";
    String xml_Alert = "";
    
    String queryString="select * from demographicaccessory where demographic_no = "+demographic_no ;
 	   RS = beanDBConnect.executeQuery(queryString);

/*
<xml_Problem_List>Problem_List</xml_Problem_List>
<xml_Medication>
<xml_Alert>
*/
%>


<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ProblemList_AppointmentForm</title>
<meta http-equiv="Cache-Control" content="no-cache">

</head>
<body>
<table border="1">
	<%
         out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+demographic_no+"&keyword="+keyword+"&button="+button+"\">Return</a></td></tr>");   


   if( RS.next()){
              InputStream ipsA =RS.getAsciiStream("content");
              tempString = beanCoercionTest.showAsciiStream(ipsA, 5000);
              tempString = beanUtility.moveQuote(tempString);

        xml_Problem_List = beanFunctionGenerator.getXMLout(tempString,"<xml_Problem_List>","</xml_Problem_List>");
        xml_Medication = beanFunctionGenerator.getXMLout(tempString,"<xml_Medication>","</xml_Medication>");
        xml_Alert = beanFunctionGenerator.getXMLout(tempString,"<xml_Alert>","</xml_Alert>");

              ipsA.close();
         out.print("<tr><td>Problem List:</td></tr>");   
         out.print("<tr><td><textarea rows=\"3\" cols=\"29\" >"+xml_Problem_List+"</textarea></td></tr>");   
         out.print("<tr><td>Medication:</td></tr>");   
         out.print("<tr><td><textarea rows=\"3\" cols=\"29\" >"+xml_Medication+"</textarea></td></tr>");   
         out.print("<tr><td>Allergy/Alert:</td></tr>");   
         out.print("<tr><td><textarea rows=\"3\" cols=\"29\" >"+xml_Alert+"</textarea></td></tr>");   
   }else{
         out.print("<tr><td>no records</td></tr>");   
   
   }

 
RS.close();   
%>

</table>
</body>
</html>