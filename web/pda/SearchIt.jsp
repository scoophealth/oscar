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

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page errorPage="ErrorPage.jsp"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />

<% 
//  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");


  String button = request.getParameter("button").trim();
  String keyword = request.getParameter("keyword").trim();
//*****************************************************************  

ResultSet RS = null;

// if has keywords input

  if(keyword.length()>0){	
	if ( button.equals("name")){
	 
	   RS = beanDBQuery.getSearchName(keyword);
	}
 
	if (button.equals("phone")){
	   RS = beanDBQuery.getSearchPhone(keyword);
	}
	 
	if (button.equals("DOB")){
	   RS = beanDBQuery.getSearchDob(keyword);
	}
	
	 
	if (button.equals("Address")){
	   RS = beanDBQuery.getSearchAddress(keyword);
	}
	
	if (button.equals("HIN")){
	   RS = beanDBQuery.getSearchHin(keyword);
	}
	
	if (button.equals("ChartNo")){
	   RS = beanDBQuery.getSearchChartNo(keyword);
	}


// if NO keywords input
	 
  }else{

	if ( button.equals("name")){
	 
	   RS = beanDBQuery.getSearchByOrder("last_name");
	}
 
	if (button.equals("phone")){
	   RS = beanDBQuery.getSearchByOrder("phone");
	}
	 
	if (button.equals("DOB")){
	   RS = beanDBQuery.getSearchByOrder("year_of_birth");
	}
	
	 
	if (button.equals("Address")){
	   RS = beanDBQuery.getSearchByOrder("address");
	}
	
	if (button.equals("HIN")){
	   RS = beanDBQuery.getSearchByOrder("hin");
	}
	
	if (button.equals("ChartNo")){
	   RS = beanDBQuery.getSearchByOrder("chart_no");
	}


  }  

%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>SearchIt</title>
<meta http-equiv="Cache-Control" content="no-cache">
</head>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<body>
<table border="1">
	<%

if ( button.equals("name")){
       out.print("<tr><td><a href=\"Search.jsp\">Return</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspName</td><td>DOB</td><td>RS</td><td>sex</td></tr>");   
   while( RS.next()){
       out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+RS.getInt("demographic_no")+"&keyword="+keyword+"&button="+button+"\">"+RS.getString("last_name")+","+RS.getString("first_name")+"</a></td><td>"+RS.getString("year_of_birth")+"-"+RS.getString("month_of_birth")+"-"+RS.getString("date_of_birth")+"</td><td>"+RS.getString("roster_status")+"</td><td>"+RS.getString("sex")+"</td></tr>");   
   }
}

if ( button.equals("phone")){
       out.print("<tr><td><a href=\"Search.jsp\">Return</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspName</td><td>phone</td><td>RS</td><td>sex</td></tr>");   
   while( RS.next()){
       out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+RS.getInt("demographic_no")+"&keyword="+keyword+"&button="+button+"\">"+RS.getString("last_name")+","+RS.getString("first_name")+"</a></td><td>"+RS.getString("phone")+"</td><td>"+RS.getString("roster_status")+"</td><td>"+RS.getString("sex")+"</td></tr>");   
   }
}

if ( button.equals("DOB")){
       out.print("<tr><td><a href=\"Search.jsp\">Return</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspName</td><td>DOB</td><td>RS</td><td>sex</td></tr>");   
   while( RS.next()){
       out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+RS.getInt("demographic_no")+"&keyword="+keyword+"&button="+button+"\">"+RS.getString("last_name")+","+RS.getString("first_name")+"</a></td><td>"+RS.getString("year_of_birth")+"-"+RS.getString("month_of_birth")+"-"+RS.getString("date_of_birth")+"</td><td>"+RS.getString("roster_status")+"</td><td>"+RS.getString("sex")+"</td></tr>");   
   }
}

if ( button.equals("Address")){
       out.print("<tr><td><a href=\"Search.jsp\">Return</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspName</td><td colspan=\"3\">Address</td></tr>");   
   while( RS.next()){
       out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+RS.getInt("demographic_no")+"&keyword="+keyword+"&button="+button+"\">"+RS.getString("last_name")+","+RS.getString("first_name")+"</a></td><td>"+RS.getString("address")+"</td><td></td><td></td></tr>");   
   }
}

if ( button.equals("HIN")){
       out.print("<tr><td><a href=\"Search.jsp\">Return</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspName</td><td>HIN</td><td>RS</td><td>sex</td></tr>");   
   while( RS.next()){
       out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+RS.getInt("demographic_no")+"&keyword="+keyword+"&button="+button+"\">"+RS.getString("last_name")+","+RS.getString("first_name")+"</a></td><td>"+RS.getString("hin")+"</td><td>"+RS.getString("roster_status")+"</td><td>"+RS.getString("sex")+"</td></tr>");   
   }
}

if ( button.equals("ChartNo")){
       out.print("<tr><td><a href=\"Search.jsp\">Return</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspName</td><td>ChartNo</td><td>RS</td><td>sex</td></tr>");   
   while( RS.next()){
       out.print("<tr><td><a href=\"Demographic.jsp?demographic_no="+RS.getInt("demographic_no")+"&keyword="+keyword+"&button="+button+"\">"+RS.getString("last_name")+","+RS.getString("first_name")+"</a></td><td>"+RS.getString("chart_no")+"</td><td>"+RS.getString("roster_status")+"</td><td>"+RS.getString("sex")+"</td></tr>");   
   }
}


RS.close();   
%>

</table>
</body>
</html>