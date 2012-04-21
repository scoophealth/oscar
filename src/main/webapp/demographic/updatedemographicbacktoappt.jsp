<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%
  //if action is good, then give me the result
    String[] param =new String[24];
	  param[0]=request.getParameter("last_name");
	  param[1]=request.getParameter("first_name");
	  param[2]=request.getParameter("address");
	  param[3]=request.getParameter("city");
	  param[4]=request.getParameter("province");
	  param[5]=request.getParameter("postal");
	  param[6]=request.getParameter("phone");
	  param[7]=request.getParameter("ext");
	  param[8]=request.getParameter("year_of_birth");
	  param[9]=request.getParameter("month_of_birth");
	  param[10]=request.getParameter("date_of_birth");
	  param[11]=request.getParameter("hin");
	  param[12]=request.getParameter("ver");
	  param[13]=request.getParameter("roster_status");
	  param[14]=request.getParameter("patient_status");
	  param[15]=request.getParameter("date_joined");
	  param[16]=request.getParameter("chart_no");
	  param[17]=request.getParameter("staff");
	  param[18]=request.getParameter("sex");
	  param[19]=request.getParameter("end_date");
	  param[20]=request.getParameter("eff_date");
	  param[21]=request.getParameter("pcn_indicator");
	  param[22]=request.getParameter("hc_type");
	  param[23]=request.getParameter("hc_renew_date");
	  
    String cpp_id=request.getParameter("cpp_id");
	  int []intparam=new int[] {Integer.parseInt(cpp_id)};

  int rowsAffected = apptMainBean.queryExecuteUpdate(param, intparam,  request.getParameter("dboperation"));
  if (rowsAffected ==1) {
    String name = param[0]+","+param[1];
%>
<h2>Update a Provider Record Successfully !
<p><a
	href="cppcontrol.jsp?cpp_id=<%=request.getParameter("cpp_id")%>&displaymode=edit&dboperation=search_detail"><%= request.getParameter("cpp_id") %></a>
</h2>
<%  
  if(true) {
    out.clear();
    pageContext.forward("../appointment/editappointmentfromcpp.jsp?name="+name+"&cpp_id="+cpp_id); //forward request&response to the target page
    return;
  }
  } else {
%>
<h1>Sorry, fail to update !!! <%= request.getParameter("cpp_id") %>.
<%  
  } 
%>
