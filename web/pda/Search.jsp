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

<jsp:useBean id="beanAboutDate" scope="session" class="bean.AboutDate" />

<%
  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");


    String todayString = beanAboutDate.getTodayString();
%>

<HTML>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta content="no-cache" />
<TITLE>Search</TITLE>
</HEAD>
<BODY>
<table>
	<form action="SearchIt.jsp" method="get">
	<tr>
		<td><a href="AppointmentToday.jsp?todayString=<%=todayString%>">Return</a></td>
		<td><a href="logout.jsp">LogOut</a></td>
	</tr>
	<tr>
		<td>Input keywords :</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="2"><input type="text" name="keyword"></td>
		<td></td>
	</tr>
	<tr>
		<td><input type="submit" name="button" value="name"></td>
		<td><input type="submit" name="button" value="phone"></td>
	</tr>
	<tr>
		<td><input type="submit" name="button" value="DOB"></td>
		<td><input type="submit" name="button" value="Address"></td>
	</tr>
	<tr>
		<td><input type="submit" name="button" value="HIN"></td>
		<td><input type="submit" name="button" value="ChartNo"></td>
	</tr>
	</form>
</table>
</body>
</HTML>
